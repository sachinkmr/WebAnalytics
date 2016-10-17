package web.analytics.driver;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import web.analytics.common.Constants;

/**
 * <p>
 * This class is used to handle proxy and webdriver instances and their types
 * </p>
 * 
 * @author Sachin
 * 
 **/
public class DriverBuilder implements AutoCloseable {
	protected static final Logger logger = LoggerFactory.getLogger(DriverBuilder.class);
	private BrowserMobProxy proxy;
	private WebDriver driver;

	static {
		System.setProperty("webdriver.gecko.driver", "input/Servers/geckodriver.exe");
		System.setProperty("webdriver.chrome.driver", "input/Servers/chromedriver.exe");
	}

	public WebDriver getDriver() {
		return driver;
	}

	public DriverBuilder() {
		proxy = new BrowserMobProxyServer();
		proxy.setTrustAllServers(true);
		proxy.setConnectTimeout(30, TimeUnit.SECONDS);
		if (!Constants.USERNAME.isEmpty())
			proxy.addRequestFilter((request, contents, messageInfo) -> {
				final String login = Constants.USERNAME + ":" + Constants.PASSWORD;
				final String base64login = new String(Base64.encodeBase64(login.getBytes()));
				request.headers().add("Authorization", "Basic " + base64login);
				return null;
			});
		proxy.start(0);

	}

	/***
	 * @author Sachin
	 *         <p>
	 *         This method returns instance of chrome driver
	 *         </p>
	 * 
	 * @return WebDriver
	 * 
	 ***/
	public WebDriver getChromeDriver() {
		ChromeOptions options = new ChromeOptions();
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setJavascriptEnabled(true);
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		capabilities.setCapability(CapabilityType.PROXY, ClientUtil.createSeleniumProxy(proxy));
		capabilities.setCapability("chrome.switches", Arrays.asList("--ignore-certificate-errors"));
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		driver = new ChromeDriver(capabilities);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		return driver;
	}

	/**
	 * @author Sachin
	 *         <p>
	 *         This method returns instance of Firefox driver
	 *         </p>
	 * 
	 * @return WebDriver
	 * 
	 **/
	public WebDriver getFireFoxDriver() {
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		capabilities.setCapability(CapabilityType.PROXY, ClientUtil.createSeleniumProxy(proxy));
		capabilities.setJavascriptEnabled(true);
		capabilities.setCapability("takesScreenshot", true);
		driver = new FirefoxDriver(capabilities);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return driver;
	}

	/**
	 * @author Sachin
	 *         <p>
	 *         Method returns instance of proxy driver
	 *         </p>
	 * 
	 * @return BrowserMobProxy
	 * 
	 **/

	public BrowserMobProxy getProxy() {
		return this.proxy;
	}

	/**
	 * This method is used to kill all the processes running chrome server if
	 * any is running as system processes.
	 *
	 * @author Sachin
	 **/
	private void killChromeService() {
		String serviceName = "chromedriver.exe";
		try {
			if (ProcessKiller.isProcessRunning(serviceName)) {
				ProcessKiller.killProcess(serviceName);
			}
		} catch (Exception ex) {
			logger.warn("Unable to close chrome server.", ex);
		}
	}

	@Override
	public void close() {
		if (null != proxy) {
			proxy.stop();
			proxy = null;
		}

		if (null != driver) {
			driver.quit();
		}
		killChromeService();
	}
}
