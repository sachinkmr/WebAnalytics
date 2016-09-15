package web.analytics.selenium;

import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.CaptureType;
import web.analytics.common.Constants;

public class DriverBuilder implements AutoCloseable {
	protected static final Logger logger = LoggerFactory.getLogger(DriverBuilder.class);
	private BrowserMobProxy proxy;
	private WebDriver driver;
	private Proxy seleniumProxy;

	public DriverBuilder() {
		proxy = new BrowserMobProxyServer();
		proxy.setHostNameResolver(ClientUtil.createDnsJavaResolver());
		proxy.setHostNameResolver(ClientUtil.createNativeCacheManipulatingResolver());
		proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT);
		proxy.addHeader("user-agent", Constants.USER_AGENT);
		if (!Constants.USERNAME.isEmpty()) {
			final String login = Constants.USERNAME + ":" + Constants.PASSWORD;
			final String base64login = new String(Base64.encodeBase64(login.getBytes()));
			proxy.addHeader("Authorization", "Basic " + base64login);
			// proxy.addRequestFilter(new RequestFilter() {
			// @Override
			// public HttpResponse filterRequest(HttpRequest request,
			// HttpMessageContents arg1, HttpMessageInfo arg2) {
			// request.headers().add("Authorization", "Basic " + base64login);
			// return null;
			// }
			// });
		}

		proxy.start();
		seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
	}

	public WebDriver getFireFoxDriver() {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);

		return driver;
	}

	@Override
	public void close() {
		proxy.stop();
	}
}
