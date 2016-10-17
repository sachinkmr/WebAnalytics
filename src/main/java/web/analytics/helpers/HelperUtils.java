package web.analytics.helpers;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.relevantcodes.extentreports.LogStatus;

import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarNameValuePair;
import web.analytics.common.Constants;
import web.analytics.driver.DriverBuilder;
import web.analytics.exception.AnalyticsException;
import web.analytics.suite.AnalyticsData;
import web.analytics.suite.TestCase;
import web.analytics.suite.TestStep;

public class HelperUtils {

	protected static final Logger logger = LoggerFactory.getLogger(HelperUtils.class);

	public static Map<String, By> getObjectRepository(String objectRepo) {
		Properties prop = new Properties();
		Map<String, By> map = new HashMap<>();
		try {
			FileReader reader = new FileReader(
					"input" + File.separator + "ObjectRepository" + File.separator + objectRepo + ".properties");
			prop.load(reader);
			reader.close();
		} catch (IOException e) {
			logger.error("Unable to find Controller file", e);
			System.out.println("Error in application: " + e);
		}
		for (String key : prop.stringPropertyNames()) {
			map.put(key, getLocator(prop.getProperty(key)));
		}
		return map;
	}

	public static String getResourceFile(String fileName) {
		File file = null;
		Constants.TEMP = new File(System.getProperty("user.dir") + File.separator + "tmp").getAbsolutePath();
		try {
			String str = IOUtils.toString(HelperUtils.class.getClassLoader().getResourceAsStream(fileName));
			file = new File(Constants.TEMP, fileName);
			FileUtils.write(file, str, "utf-8");
		} catch (IOException e) {
			logger.error("Error in file reading.", e);
		}
		return file.getAbsolutePath();
	}

	private static By getLocator(String property) {
		try {
			String key = property.substring(0, property.indexOf("="));
			String value = property.substring(key.length() + 1, property.length());
			switch (key.toLowerCase()) {
			case "name":
				return By.name(value);
			case "id":
				return By.id(value);
			case "class":
				return By.className(value);
			case "css":
				return By.cssSelector(value);
			case "xpath":
				return By.xpath(value);
			case "tagname":
				return By.tagName(value);
			case "partiallinktext":
				return By.partialLinkText(value);
			case "linktext":
				return By.linkText(value);
			default:
				throw new Exception("'" + key + "' keyword is not valid.");
			}
		} catch (Exception e) {
			logger.error("Error in object initialization.", e);
		}
		return null;
	}

	public static String getUniqueString() {
		DateFormat df = new SimpleDateFormat("dd-MMMM-yyyy");
		DateFormat df1 = new SimpleDateFormat("hh-mm-ss-SSaa");
		Calendar calobj = Calendar.getInstance();
		String time = df1.format(calobj.getTime());
		String date = df.format(calobj.getTime());
		return date + "_" + time;
	}

	public static Map<String, String> getQueryParameterMap(List<HarNameValuePair> queryString) {
		Map<String, String> map = new HashMap<>();
		for (HarNameValuePair pair : queryString) {
			map.put(pair.getName(), pair.getValue());
		}
		return map;
	}

	public static AnalyticsData getAnalyticsData(String name, TestCase testcase) throws AnalyticsException {
		for (AnalyticsData data : testcase.getAnalyticsDataList()) {
			if (data.getAnalyticsObjectname().equalsIgnoreCase(name)) {
				Map<String, String> map = data.getData();
				for (String key : map.keySet()) {
					String value = map.get(key);
					while (value.contains("${") && value.contains("}")) {
						String param = value.substring(value.indexOf("${") + 2, value.indexOf("}"));
						if (testcase.getDataMap().containsKey(param)) {
							value = value.replace("${" + param + "}", testcase.getDataMap().get(param));
						} else {
							throw new AnalyticsException("Variable is not defined: " + param);
						}
					}
					map.put(key, value);
				}
				return data;
			}
		}
		return null;
	}

	/**
	 * Method to get current time.
	 *
	 * @return Date date object of current time
	 *
	 **/
	public static Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	public static LogStatus getLogStatus(TestStep step) {
		switch (step.getStatus().name()) {
		case "PASS":
			return LogStatus.PASS;
		case "FAIL":
			return LogStatus.FAIL;
		case "SKIP":
			return LogStatus.SKIP;
		default:
			return LogStatus.UNKNOWN;
		}
	}

	public static String getSiteURL(String address) {
		address = address.toLowerCase();
		boolean ssl = false;
		if (address.startsWith("http://")) {
			address = address.replace("http://", "");
		}
		if (address.startsWith("https://")) {
			address = address.replace("https://", "");
			ssl = true;
		}
		if (address.contains("/")) {
			address = address.substring(0, address.indexOf("/") + 1);
		}
		return ssl ? "https://" + address : "http://" + address;
	}

	public static Map<String, String> getBestMatchingEvent(Map<String, String> map, DriverBuilder builder) {
		TreeMap<Integer, List<Map<String, String>>> filterMap = new TreeMap<>();

		// code block to save all calls having keys(these are analytics keys)
		// with count
		for (HarEntry entry : builder.getProxy().getHar().getLog().getEntries()) {
			int count = 0;
			Map<String, String> query = new TreeMap<>(
					HelperUtils.getQueryParameterMap(entry.getRequest().getQueryString()));
			if (!query.isEmpty())
				for (String key : map.keySet()) {
					if (query.containsKey(key)) {
						count++;
					}
				}
			if (count != 0) {
				List<Map<String, String>> list = filterMap.containsKey(count) ? filterMap.get(count)
						: new ArrayList<>();
				list.add(query);
				filterMap.put(count, list);
			}
		}

		// selecting calls with maximum analytics key
		List<Map<String, String>> lastEntry = filterMap.lastEntry().getValue();
		filterMap = null;
		TreeMap<Integer, Map<String, String>> queryMap = new TreeMap<>();
		for (Map<String, String> q : lastEntry) {
			int distance = 0;
			for (String key : map.keySet()) {
				distance += StringUtils.getLevenshteinDistance(map.get(key), q.get(key));
			}
			queryMap.put(distance, q);
		}
		Map<String, String> bestQuery = queryMap.get(queryMap.firstKey());
		queryMap = null;
		Map<String, String> result = new HashMap<>();
		for (String key : bestQuery.keySet()) {
			if (map.containsKey(key)) {
				result.put(key, bestQuery.get(key));
			}
		}
		return result;
	}

	public static boolean isDigit(String data) {
		try {
			Integer.parseInt(data);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
