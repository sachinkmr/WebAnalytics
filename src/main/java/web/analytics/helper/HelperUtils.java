package web.analytics.helper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		return new Date().toString();
	}
}
