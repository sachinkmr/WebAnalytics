package web.analytics.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import web.analytics.helpers.HelperUtils;

/**
 * @author Sachin
 * 
 *         This class is use to instantiate all the constants for the utility
 * 
 **/
public class Constants {

	protected static final Logger logger = LoggerFactory.getLogger(Constants.class);
	public static final String USER_AGENT; // default is Chrome 52
	public static final String USERNAME;
	public static final String PASSWORD;
	public static final String BROWSER; // default is Chrome
	public static final String URL_PARAMETER_1;;
	public static final String URL_PARAMETER_2;
	public static final Map<String, String> QUERY_MAP = new HashMap<>();
	public static String REPORT_PATH, REPORT_FILE_LOCATION;
	public static final String REPORT_NAME;
	public static String TEMP;

	static {
		Properties prop = new Properties();
		try {
			FileReader reader = new FileReader(new File("input/Config.properties"));
			prop.load(reader);
			reader.close();
		} catch (FileNotFoundException e) {
			logger.error("Unable to find config file", e);
			System.out.println("Error in application: " + e);
		} catch (IOException e) {
			logger.error("Unable to read config file", e);
			System.out.println("Error in application: " + e);
		}
		USER_AGENT = prop.getProperty("user.agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
		USERNAME = prop.getProperty("username", "");
		PASSWORD = prop.getProperty("password", "");
		BROWSER = prop.getProperty("browser", "FireFox");
		URL_PARAMETER_1 = prop.getProperty("url.parameter1");
		URL_PARAMETER_2 = prop.getProperty("url.parameter2");

		REPORT_NAME = prop.getProperty("report.name", "Web Analytics Report");
		if (null != prop.getProperty("query.string.parameter1.key")
				&& !prop.getProperty("query.string.parameter1.key").isEmpty()) {
			QUERY_MAP.put(prop.getProperty("query.string.parameter1.key"),
					prop.getProperty("query.string.parameter1.value"));
		}
		if (null != prop.getProperty("query.string.parameter2.key")
				&& !prop.getProperty("query.string.parameter2.key").isEmpty()) {
			QUERY_MAP.put(prop.getProperty("query.string.parameter2.key"),
					prop.getProperty("query.string.parameter2.value"));
		}
		if (null != prop.getProperty("query.string.parameter3.key")
				&& !prop.getProperty("query.string.parameter3.key").isEmpty()) {
			QUERY_MAP.put(prop.getProperty("query.string.parameter3.key"),
					prop.getProperty("query.string.parameter3.value"));
		}
		if (null != prop.getProperty("query.string.parameter4.key")
				&& !prop.getProperty("query.string.parameter4.key").isEmpty()) {
			QUERY_MAP.put(prop.getProperty("query.string.parameter4.key"),
					prop.getProperty("query.string.parameter4.value"));
		}
		if (null != prop.getProperty("query.string.parameter5.key")
				&& !prop.getProperty("query.string.parameter5.key").isEmpty()) {
			QUERY_MAP.put(prop.getProperty("query.string.parameter5.key"),
					prop.getProperty("query.string.parameter5.value"));
		}
		REPORT_FILE_LOCATION = new File("Reports" + File.separator + HelperUtils.getUniqueString()).getAbsolutePath();
		Constants.REPORT_PATH = REPORT_FILE_LOCATION + File.separator + "Report.html";
	}
}
