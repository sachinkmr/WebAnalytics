package web.analytics.driver;

import java.util.Arrays;
import java.util.HashSet;
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

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.proxy.CaptureType;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;
import web.analytics.common.Constants;
import web.analytics.helper.HelperUtils;

public class DriverBuilder implements AutoCloseable {
    protected static final Logger logger = LoggerFactory.getLogger(DriverBuilder.class);
    private BrowserMobProxy proxy;
    private WebDriver driver;

    static {
	System.setProperty("webdriver.gecko.driver", "input/servers/geckodriver.exe");
	System.setProperty("webdriver.chrome.driver", "input/servers/chromedriver.exe");
    }

    public WebDriver getDriver() {
	return driver;
    }

    public DriverBuilder() {
	proxy = new BrowserMobProxyServer();
	// proxy.setHostNameResolver(ClientUtil.createDnsJavaResolver());
	// proxy.setHostNameResolver(ClientUtil.createNativeCacheManipulatingResolver());
	proxy.setTrustAllServers(true);
	proxy.setConnectTimeout(1, TimeUnit.MINUTES);
	proxy.start(0);
	proxy.addRequestFilter(new RequestFilter() {
	    @Override
	    public HttpResponse filterRequest(HttpRequest request, HttpMessageContents arg1, HttpMessageInfo arg2) {
		if (!Constants.USERNAME.isEmpty()) {
		    final String login = Constants.USERNAME + ":" + Constants.PASSWORD;
		    final String base64login = new String(Base64.encodeBase64(login.getBytes()));
		    request.headers().add("Authorization", "Basic " + base64login);
		}
		request.headers().add("user-agent", Constants.USER_AGENT);
		return null;
	    }
	});
	proxy.newHar(HelperUtils.getUniqueString());
	HashSet<CaptureType> enable = new HashSet<CaptureType>();
	HashSet<CaptureType> disable = new HashSet<CaptureType>();
	enable.add(CaptureType.REQUEST_CONTENT);
	enable.add(CaptureType.REQUEST_BINARY_CONTENT);
	proxy.enableHarCaptureTypes(enable);
	disable.add(CaptureType.RESPONSE_BINARY_CONTENT);
	disable.add(CaptureType.RESPONSE_CONTENT);
	disable.add(CaptureType.REQUEST_HEADERS);
	disable.add(CaptureType.RESPONSE_HEADERS);
	disable.add(CaptureType.REQUEST_COOKIES);
	disable.add(CaptureType.RESPONSE_COOKIES);
	proxy.disableHarCaptureTypes(disable);
    }

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

    public BrowserMobProxy getProxy() {
	return this.proxy;
    }

    /**
     * This method is used to kill all the processes running chrome server if
     * any is running as system processes.
     *
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
