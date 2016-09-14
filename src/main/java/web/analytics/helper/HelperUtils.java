package web.analytics.helper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelperUtils {
	protected static final Logger logger = LoggerFactory.getLogger(HelperUtils.class);

	public static Map<String, String> getObjectRepository(String objectRepo) {
		Properties prop = new Properties();
		Map<String, String> map = new HashMap<>();
		try {
			FileReader reader = new FileReader(
					"input" + File.separator + "ObjectRepository" + File.separator + objectRepo + ".properties");
			prop.load(reader);
			reader.close();
		} catch (IOException e) {
			logger.error("Unable to find Controller file", e);
		}
		for (String key : prop.stringPropertyNames()) {
			map.put(key, prop.getProperty(key));
		}
		return map;
	}
}
