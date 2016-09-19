package web.analytics.helper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.relevantcodes.extentreports.LogStatus;

import net.lightbody.bmp.core.har.HarNameValuePair;
import web.analytics.common.Constants;
import web.analytics.excel.AnalyticsData;
import web.analytics.excel.TestCase;
import web.analytics.excel.TestStep;

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
	    System.exit(1);
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
	String arr[] = property.split("=");
	try {
	    switch (arr[0].toLowerCase()) {
	    case "name":
		return By.name(arr[1]);
	    case "id":
		return By.id(arr[1]);
	    case "class":
		return By.className(arr[1]);
	    case "css":
		return By.cssSelector(arr[1]);
	    case "xpath":
		return By.xpath(arr[1]);
	    default:
		throw new Exception("'" + arr[0] + "' keyword is not valid.");
	    }
	} catch (Exception e) {
	    logger.error("Error in object initialization.", e);
	    System.exit(1);
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

    public static AnalyticsData getAnalyticsData(String name, TestCase testcase) {
	for (AnalyticsData data : testcase.getAnalyticsDataList()) {
	    if (data.getAnalyticsObjectname().equalsIgnoreCase(name)) {
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
}
