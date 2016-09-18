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

public class Constants {
    public static final String USER_AGENT; // default is Chrome 52
    public static final String USERNAME;
    public static final String PASSWORD;
    public static final String BROWSER; // default is Chrome
    public static final String URL_PARAMETER_1;;
    public static final String URL_PARAMETER_2;
    protected static final Logger logger = LoggerFactory.getLogger(Constants.class);
    public static final Map<String, String> QUERY_MAP = new HashMap<>();

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
		"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
	USERNAME = prop.getProperty("username", "");
	PASSWORD = prop.getProperty("password", "");
	BROWSER = prop.getProperty("browser", "FireFox");
	URL_PARAMETER_1 = prop.getProperty("url.parameter1", "collect");
	URL_PARAMETER_2 = prop.getProperty("url.parameter2", "tid");
	if (null != prop.getProperty("query.string.parameter1.key")
		|| !prop.getProperty("query.string.parameter1.key").isEmpty()) {
	    QUERY_MAP.put(prop.getProperty("query.string.parameter1.key"),
		    prop.getProperty("query.string.parameter1.value"));
	}
	if (null != prop.getProperty("query.string.parameter2.key")
		|| !prop.getProperty("query.string.parameter2.key").isEmpty()) {
	    QUERY_MAP.put(prop.getProperty("query.string.parameter2.key"),
		    prop.getProperty("query.string.parameter2.value"));
	}
	if (null != prop.getProperty("query.string.parameter3.key")
		|| !prop.getProperty("query.string.parameter3.key").isEmpty()) {
	    QUERY_MAP.put(prop.getProperty("query.string.parameter3.key"),
		    prop.getProperty("query.string.parameter3.value"));
	}
	if (null != prop.getProperty("query.string.parameter4.key")
		|| !prop.getProperty("query.string.parameter4.key").isEmpty()) {
	    QUERY_MAP.put(prop.getProperty("query.string.parameter4.key"),
		    prop.getProperty("query.string.parameter4.value"));
	}
	if (null != prop.getProperty("query.string.parameter5.key")
		|| !prop.getProperty("query.string.parameter5.key").isEmpty()) {
	    QUERY_MAP.put(prop.getProperty("query.string.parameter5.key"),
		    prop.getProperty("query.string.parameter5.value"));
	}
    }
}
