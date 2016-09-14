package web.analytics.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {
	public static final String USER_AGENT; // default is FF v47
	public static final String USERNAME;
	public static final String PASSWORD;
	public static final String BROWSER; // default is FireFox
	protected static final Logger logger = LoggerFactory.getLogger(Constants.class);

	static {
		Properties prop = new Properties();
		try {
			FileReader reader = new FileReader(new File("input/Config.properties"));
			prop.load(reader);
			reader.close();
		} catch (FileNotFoundException e) {
			logger.error("Unable to find config file", e);
			System.exit(1);
		} catch (IOException e) {
			logger.error("Unable to read config file", e);
			System.exit(1);
		}
		USER_AGENT = prop.getProperty("user.agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0");
		USERNAME = prop.getProperty("user.agent", "");
		PASSWORD = prop.getProperty("user.agent", "");
		BROWSER = prop.getProperty("user.agent", "FireFox");
	}
}
