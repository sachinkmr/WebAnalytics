package web.analytics.selenium;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarRequest;
import web.analytics.common.Constants;
import web.analytics.driver.DriverBuilder;
import web.analytics.exception.AnalyticsException;
import web.analytics.helpers.HelperUtils;
import web.analytics.suite.AnalyticsData;
import web.analytics.suite.TestStatus;
import web.analytics.suite.TestStep;

public class Keywords {
	protected static final Logger logger = LoggerFactory.getLogger(Keywords.class);
	public boolean stopOnError = false;
	WebDriverWait wait;

	@KeywordInfo(description = "This Keyword is used to launch browser and opens web page", data = "web page url", objectName = "..")
	public void launch(DriverBuilder builder, TestStep testStep) {
		WebDriver driver = builder.getDriver();
		wait = new WebDriverWait(driver, 30);
		testStep.getTestCase().getDataMap().put("SITE_URL", HelperUtils.getSiteURL(testStep.getData()));
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			driver.navigate().to(testStep.getData());
			testStep.setStatus(TestStatus.PASS);
			Thread.sleep(3000);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to click on web Element", data = "..", objectName = "Web Element Locator name form object repository")
	public void click(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			By property = testStep.getTestCase().getObjectRepo().get(testStep.getObjectLocator());
			wait.until(ExpectedConditions.visibilityOfElementLocated(property));
			driver.findElement(property).click();
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This Keyword is used to submit form data", data = "..", objectName = "Web Element Locator name form object repository")
	public void submit(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			By property = testStep.getTestCase().getObjectRepo().get(testStep.getObjectLocator());
			wait.until(ExpectedConditions.visibilityOfElementLocated(property));
			driver.findElement(property).submit();
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to enter text in text field or text area", data = "text to be entered", objectName = "Web Element Locator name form object repository")
	public void enter(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			By property = testStep.getTestCase().getObjectRepo().get(testStep.getObjectLocator());
			wait.until(ExpectedConditions.visibilityOfElementLocated(property));
			WebElement element = driver.findElement(property);
			element.clear();
			element.sendKeys(testStep.getData());
			element.sendKeys(Keys.TAB);
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to select a value from drop down", data = "text to be selected", objectName = "Web Element Locator name form object repository")
	public void select(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			By property = testStep.getTestCase().getObjectRepo().get(testStep.getObjectLocator());
			wait.until(ExpectedConditions.visibilityOfElementLocated(property));
			WebElement element = driver.findElement(property);
			if (element.getTagName().equalsIgnoreCase("UL")) {
				element.findElement(By.xpath("//descendant-or-self::*[text()='" + testStep.getData() + "']")).click();
			} else if (element.getTagName().equalsIgnoreCase("SELECT")) {
				new Select(element).selectByVisibleText(testStep.getData());
			} else {
				throw new Exception("Error in selecting element.");
			}
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to wait for page to load completly.", data = "..", objectName = "..")
	public void waitForPageLoad(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			boolean flag = true;
			do {
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				String value = (String) executor.executeScript("return document.readyState");
				if (value.equalsIgnoreCase("complete")) {
					flag = false;
				}
				Thread.sleep(1000);
			} while (flag);
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to close the browser current tab/windows", data = "..", objectName = "..")
	public void close(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			if (driver.getWindowHandles().size() > 1)
				driver.close();
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to delete cookies for current session", data = "..", objectName = "..")
	public void deleteCookies(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			driver.manage().deleteAllCookies();
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to reload the page", data = "..", objectName = "..")
	public void refresh(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			driver.navigate().refresh();
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to douple click on web element", data = "..", objectName = "Web Element Locator name form object repository")
	public void doubleClick(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			By property = testStep.getTestCase().getObjectRepo().get(testStep.getObjectLocator());
			wait.until(ExpectedConditions.visibilityOfElementLocated(property));
			Actions action = new Actions(driver);
			WebElement element = driver.findElement(property);
			action.doubleClick(element).build().perform();
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to select check box if not selected already", data = "..", objectName = "Web Element Locator name form object repository")
	public void clickCheckBox(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			By property = testStep.getTestCase().getObjectRepo().get(testStep.getObjectLocator());
			wait.until(ExpectedConditions.visibilityOfElementLocated(property));
			WebElement element = driver.findElement(property);
			if (!element.isSelected())
				element.click();
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to clear text field or text area", data = "..", objectName = "Web Element Locator name form object repository")
	public void clear(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			By property = testStep.getTestCase().getObjectRepo().get(testStep.getObjectLocator());
			wait.until(ExpectedConditions.visibilityOfElementLocated(property));
			driver.findElement(property).clear();
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This places mouse on web element", data = "..", objectName = "Web Element Locator name form object repository")
	public void mouseHover(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			By property = testStep.getTestCase().getObjectRepo().get(testStep.getObjectLocator());
			wait.until(ExpectedConditions.visibilityOfElementLocated(property));
			Actions action = new Actions(driver);
			action.moveToElement(driver.findElement(property)).build().perform();
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to wait for given time", data = "wait time in seconds", objectName = "Web Element Locator name form object repository")
	public void wait(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			Thread.sleep(Integer.parseInt(testStep.getData()) * 1000);
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to save the text for and web element.", data = "Name of the variable in which data is saved", objectName = "Web Element Locator name form object repository")
	public void saveText(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			By property = testStep.getTestCase().getObjectRepo().get(testStep.getObjectLocator());
			wait.until(ExpectedConditions.visibilityOfElementLocated(property));
			String text = driver.findElement(property).getText();
			testStep.getTestCase().getDataMap().put(testStep.getData(), text);
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to wait for a web element to appear on the webpage", data = "Maximum time to wait in seconds", objectName = "Web Element Locator name form object repository")
	public void waitForElementAppearance(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			By property = testStep.getTestCase().getObjectRepo().get(testStep.getObjectLocator());
			WebDriverWait wait = new WebDriverWait(driver, Integer.parseInt(testStep.getData()));
			wait.until(ExpectedConditions.visibilityOfElementLocated(property));
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to wait for a web element to disappear from the webpage", data = "Maximum time to wait in seconds", objectName = "Web Element Locator name form object repository")
	public void waitForElementDisappearance(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			By property = testStep.getTestCase().getObjectRepo().get(testStep.getObjectLocator());
			WebDriverWait wait = new WebDriverWait(driver, Integer.parseInt(testStep.getData()));
			wait.until(ExpectedConditions.invisibilityOfElementLocated(property));
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to clear networks calls or analytics calls", data = "..", objectName = "..")
	public void clearEventCalls(DriverBuilder builder, TestStep testStep) {
		if (!executeBeforeKeyword(builder.getDriver(), testStep))
			return;
		try {
			builder.getProxy().newHar(HelperUtils.getUniqueString());
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to verify analytics event", data = "..", objectName = "Analytics event name provided in sheet")
	public void verifyEvent(DriverBuilder builder, TestStep testStep) {
		if (!executeBeforeKeyword(builder.getDriver(), testStep))
			return;
		try {
			AnalyticsData data = HelperUtils.getAnalyticsData(testStep.getObjectLocator(), testStep.getTestCase());
			if (data == null)
				throw new Exception("Event name in analytics sheet not found ");
			Map<String, String> map = data.getData();
			if (!getEventStatus(builder, testStep, map)) {
				throw new Exception("Event Name: " + testStep.getObjectLocator() + "<br/>Best Matched Event: <br/>"
						+ HelperUtils.getBestMatchingEvent(new TreeMap<>(map), builder));
			}
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	private boolean executeBeforeKeyword(WebDriver driver, TestStep testStep) {
		testStep.getTestCase().getDataMap().put("CURRENT_URL", driver.getCurrentUrl());
		if (stopOnError) {
			stopOnError = testStep.onError();
			testStep.setStatus(TestStatus.SKIP);
			logger.error("Skipping test step as stop execution on error is set to true");
			return false;
		}
		return true;
	}

	private boolean getEventStatus(DriverBuilder builder, TestStep testStep, Map<String, String> map) {
		try {
			this.waitForPageLoad(builder.getDriver(), testStep);
			Thread.sleep(2000);
			for (HarEntry entry : builder.getProxy().getHar().getLog().getEntries()) {
				HarRequest request = entry.getRequest();
				if (request.getUrl().contains(Constants.URL_PARAMETER_1)
						&& request.getUrl().contains(Constants.URL_PARAMETER_2)) {
					Map<String, String> query = HelperUtils.getQueryParameterMap(request.getQueryString());
					if (query.entrySet().containsAll(Constants.QUERY_MAP.entrySet())) {
						testStep.setData(map.toString());
						if (query.entrySet().containsAll(map.entrySet())) {
							return true;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.debug("Error in event. ", e);
		}
		return false;
	}

	@KeywordInfo(description = "This is used to current url of the page", data = "URL to be matched. Its case sensitive", objectName = "..")
	public void verifyURL(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			if (driver.getCurrentUrl().equals(testStep.getData())) {
				testStep.setStatus(TestStatus.PASS);
			} else {
				throw new AnalyticsException("URL are not same");
			}
		} catch (AnalyticsException ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}

	}


	@KeywordInfo(description = "This is used to get attribute value and stores it in a given variable.", data = "[attribute name=variable name] attribute name to fetch the value, varibale name to store attribute value. thease are without brackets. Attribute name and variable name must separated by '=' respectively.", objectName = "Element locator")
	public void saveElementAttributeValue(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			By property = testStep.getTestCase().getObjectRepo().get(testStep.getObjectLocator());
			wait.until(ExpectedConditions.visibilityOfElementLocated(property));
			WebElement list = driver.findElement(property);
			String attr = testStep.getData().substring(0, testStep.getData().indexOf("="));
			String var = testStep.getData().replace(attr + "=", "");
			testStep.getTestCase().getDataMap().put(var, list.getAttribute(attr));
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to switch to the iframe. if data column can have iFrame order as digit/iFrame name, or user can also provide iFrame element locator object. Only one is mandatory (iFrame Name/iFrame order or iFrame element locator)", data = "Iframe name or order number.", objectName = "Element locator")
	public void switchToframe(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			if (testStep.getData() != null && !testStep.getData().isEmpty()
					&& !testStep.getData().equalsIgnoreCase("..")) {
				if (HelperUtils.isDigit(testStep.getData())) {
					driver.switchTo().frame(Integer.parseInt(testStep.getData()));
				} else {
					driver.switchTo().frame(testStep.getData());
				}
			} else if (testStep.getObjectLocator() != null && !testStep.getObjectLocator().isEmpty()) {
				WebElement element = driver
						.findElement(testStep.getTestCase().getObjectRepo().get(testStep.getObjectLocator()));
				driver.switchTo().frame(element);
			}
			testStep.setStatus(TestStatus.PASS);
			// System.out.println(driver.getPageSource());
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to switch to the iframe. if data column can have iFrame order as digit/iFrame name, or user can also provide iFrame element locator object. Only one is mandatory (iFrame Name/iFrame order or iFrame element locator)", data = "Iframe name or order number.", objectName = "Element locator")
	public void switchToMainWindows(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			driver.switchTo().defaultContent();
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to click back button and load previous page from brawser history ", data = "..", objectName = "..")
	public void brawserBack(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			driver.navigate().back();
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to click back button and load previous page from browser history ", data = "..", objectName = "..")
	public void browserBack(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			driver.navigate().back();
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to click forword button and load next page from browser history ", data = "..", objectName = "..")
	public void browserForword(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			driver.navigate().forward();
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to accept alert on page.", data = "..", objectName = "..")
	public void acceptAlert(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			driver.switchTo().alert().accept();
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to close alert on page.", data = "..", objectName = "..")
	public void closeAlert(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			driver.switchTo().alert().dismiss();
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to enter text in alert on page.", data = "text to be inserted", objectName = "..")
	public void enterTextInAlert(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			driver.switchTo().alert().sendKeys(testStep.getData());
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used verify page title.", data = "text to be compared", objectName = "..")
	public void verifyPageTitle(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			if (driver.getTitle().equals(testStep.getData())) {
				testStep.setStatus(TestStatus.PASS);
			} else {
				throw new AnalyticsException("Title is not same");
			}
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to resize browser windows.", data = "diemension to be entered as [width,height]. Without brackets and must be sapreated by comma", objectName = "..")
	public void resizeWindow(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			int height = Integer.parseInt(testStep.getData().split(",")[1]);
			int width = Integer.parseInt(testStep.getData().split(",")[0]);
			driver.manage().window().setSize(new Dimension(width, height));
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to switch parent windows", data = "..", objectName = "..")
	public void switchParentWindow(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			driver.switchTo().parentFrame();
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to verify css property value", data = "[property name:value of css property] without  brackets must be sapreated by colon", objectName = "Element locator")
	public void verifyCSSValue(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			By property = testStep.getTestCase().getObjectRepo().get(testStep.getObjectLocator());
			wait.until(ExpectedConditions.visibilityOfElementLocated(property));
			WebElement element = driver.findElement(property);
			String name = testStep.getData().substring(0, testStep.getData().indexOf(":"));
			String value = testStep.getData().replace(name + ":", "");
			if (!element.getCssValue(name).equals(value))
				throw new AnalyticsException("Values are not same");
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to save css property value", data = "[property name:varable name] without brackets. Variable name is reuired to store the property value. must be sapreated by colon", objectName = "Element locator")
	public void saveCSSValue(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			By property = testStep.getTestCase().getObjectRepo().get(testStep.getObjectLocator());

			wait.until(ExpectedConditions.visibilityOfElementLocated(property));
			WebElement element = driver.findElement(property);
			String name = testStep.getData().substring(0, testStep.getData().indexOf(":"));
			String var = testStep.getData().replace(name + ":", "");
			String value = element.getCssValue(var);
			testStep.getTestCase().getDataMap().put(var, value);
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to save user defined value in variable", data = "variable name=user value", objectName = "..")
	public void saveUserValue(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			String name = testStep.getData().substring(0, testStep.getData().indexOf("="));
			String var = testStep.getData().replace(name + "=", "");
			testStep.getTestCase().getDataMap().put(name, var);
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}

	}

	@KeywordInfo(description = "This is used to execute java", data = "..", objectName = "css property for element")
	public void executeJSQuery(WebDriver driver, TestStep testStep) {
		if (!executeBeforeKeyword(driver, testStep))
			return;
		try {
			System.out.println(driver.getPageSource());
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(testStep.getData());
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx("<code>" + ex.getLocalizedMessage() + "</code>");
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}
}
