package web.analytics.selenium;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarRequest;
import web.analytics.common.Constants;
import web.analytics.driver.DriverBuilder;
import web.analytics.excel.TestStatus;
import web.analytics.excel.TestStep;
import web.analytics.helper.HelperUtils;

public class Keywords {
	protected static final Logger logger = LoggerFactory.getLogger(Keywords.class);
	boolean stopOnError = false;

	@KeywordInfo(description = "This Keyword is used to launch browser and opens web page", data = "web page url", objectName = "..")
	public void launch(WebDriver driver, TestStep testStep) {
		if (stopOnError) {
			testStep.setStatus(TestStatus.SKIP);
			logger.error("Skipping test step as stop execution on error is set to true");
			return;
		}
		try {
			driver.get(testStep.getData());
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx(ex.getLocalizedMessage());
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();

		}
	}

	@KeywordInfo(description = "This is used to click on web Element", data = "..", objectName = "Web Element Locator name form object repository")
	public void click(WebDriver driver, TestStep testStep) {
		if (stopOnError) {
			testStep.setStatus(TestStatus.SKIP);
			logger.error("Skipping test step as stop execution on error is set to true");
			return;
		}
		try {
			By property = testStep.getTestCase().getObjectRepo().get(testStep.getObjectLocator());
			driver.findElement(property).click();
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx(ex.getLocalizedMessage());
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This Keyword is used to submit form data", data = "", objectName = "Web Element Locator name form object repository")
	public void submit(WebDriver driver, TestStep testStep) {
		if (stopOnError) {
			testStep.setStatus(TestStatus.SKIP);
			logger.error("Skipping test step as stop execution on error is set to true");
			return;
		}
		try {
			By property = testStep.getTestCase().getObjectRepo().get(testStep.getObjectLocator());
			driver.findElement(property).submit();
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx(ex.getLocalizedMessage());
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to enter text in text field or text area", data = "text to be entered", objectName = "Web Element Locator name form object repository")
	public void enter(WebDriver driver, TestStep testStep) {
		if (stopOnError) {
			testStep.setStatus(TestStatus.SKIP);
			logger.error("Skipping test step as stop execution on error is set to true");
			return;
		}
		try {
			By property = testStep.getTestCase().getObjectRepo().get(testStep.getObjectLocator());
			WebElement element = driver.findElement(property);
			element.clear();
			element.sendKeys(testStep.getData());
			element.sendKeys(Keys.TAB);
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx(ex.getLocalizedMessage());
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to select a value from drop down", data = "text to be selected", objectName = "Web Element Locator name form object repository")
	public void select(WebDriver driver, TestStep testStep) {
		if (stopOnError) {
			testStep.setStatus(TestStatus.SKIP);
			logger.error("Skipping test step as stop execution on error is set to true");
			return;
		}
		try {
			By property = testStep.getTestCase().getObjectRepo().get(testStep.getObjectLocator());
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
			testStep.setEx(ex.getLocalizedMessage());
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to wait for page to load completly.", data = "..", objectName = "..")
	public void waitForPageLoad(WebDriver driver, TestStep testStep) {
		if (stopOnError) {
			testStep.setStatus(TestStatus.SKIP);
			logger.error("Skipping test step as stop execution on error is set to true");
			return;
		}
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
			testStep.setEx(ex.getLocalizedMessage());
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to close the browser current tab/windows", data = "..", objectName = "..")
	public void close(WebDriver driver, TestStep testStep) {
		if (stopOnError) {
			testStep.setStatus(TestStatus.SKIP);
			logger.error("Skipping test step as stop execution on error is set to true");
			return;
		}
		try {
			driver.close();
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx(ex.getLocalizedMessage());
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to delete cookies for current session", data = "..", objectName = "..")
	public void deleteCookies(WebDriver driver, TestStep testStep) {
		if (stopOnError) {
			testStep.setStatus(TestStatus.SKIP);
			logger.error("Skipping test step as stop execution on error is set to true");
			return;
		}
		try {
			driver.manage().deleteAllCookies();
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx(ex.getLocalizedMessage());
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to reload the page", data = "..", objectName = "..")
	public void refresh(WebDriver driver, TestStep testStep) {
		if (stopOnError) {
			testStep.setStatus(TestStatus.SKIP);
			logger.error("Skipping test step as stop execution on error is set to true");
			return;
		}
		try {
			driver.navigate().refresh();
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx(ex.getLocalizedMessage());
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to douple click on web element", data = "..", objectName = "Web Element Locator name form object repository")
	public void doubleClick(WebDriver driver, TestStep testStep) {
		if (stopOnError) {
			testStep.setStatus(TestStatus.SKIP);
			logger.error("Skipping test step as stop execution on error is set to true");
			return;
		}
		try {
			Actions action = new Actions(driver);
			WebElement element = driver
					.findElement(testStep.getTestCase().getObjectRepo().get(testStep.getObjectLocator()));
			action.doubleClick(element).build().perform();
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx(ex.getLocalizedMessage());
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to select check box if not selected already", data = "", objectName = "Web Element Locator name form object repository")
	public void clickCheckBox(WebDriver driver, TestStep testStep) {
		if (stopOnError) {
			testStep.setStatus(TestStatus.SKIP);
			logger.error("Skipping test step as stop execution on error is set to true");
			return;
		}
		try {
			WebElement element = driver
					.findElement(testStep.getTestCase().getObjectRepo().get(testStep.getObjectLocator()));
			if (!element.isSelected())
				element.click();
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx(ex.getLocalizedMessage());
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	// @KeywordInfo(description = "This method is used to select radio box",
	// data = "", objectName = "Web Element Locator name form object
	// repository")
	// public void selectRadioButton(WebDriver driver, TestStep testStep) {
	// if (stopOnError) {
	// testStep.setStatus(TestStatus.SKIP);
	// logger.error("Skipping test step as stop execution on error is set to
	// true");
	// return;
	// }
	// try {
	// WebElement element = driver
	// .findElement(testStep.getTestCase().getObjectRepo().get(testStep.getObjectLocator()));
	// if (!element.isSelected())
	// element.click();
	// testStep.setStatus(TestStatus.PASS);
	// } catch (Exception ex) {
	// testStep.setStatus(TestStatus.FAIL);
	// testStep.setEx(ex.getLocalizedMessage());
	// logger.error("Error in executing test step. ", ex);
	// stopOnError = testStep.onError();
	// }
	// }

	@KeywordInfo(description = "This is used to clear text field or text area", data = "", objectName = "Web Element Locator name form object repository")
	public void clear(WebDriver driver, TestStep testStep) {
		if (stopOnError) {
			testStep.setStatus(TestStatus.SKIP);
			logger.error("Skipping test step as stop execution on error is set to true");
			return;
		}
		try {
			driver.findElement(testStep.getTestCase().getObjectRepo().get(testStep.getObjectLocator())).clear();
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx(ex.getLocalizedMessage());
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This places mouse on web element", data = "", objectName = "Web Element Locator name form object repository")
	public void mouseHover(WebDriver driver, TestStep testStep) {
		if (stopOnError) {
			testStep.setStatus(TestStatus.SKIP);
			logger.error("Skipping test step as stop execution on error is set to true");
			return;
		}
		try {
			Actions action = new Actions(driver);
			action.moveToElement(
					driver.findElement(testStep.getTestCase().getObjectRepo().get(testStep.getObjectLocator()))).build()
					.perform();
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx(ex.getLocalizedMessage());
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to wait for given time", data = "wait time in seconds", objectName = "Web Element Locator name form object repository")
	public void wait(WebDriver driver, TestStep testStep) {
		if (stopOnError) {
			testStep.setStatus(TestStatus.SKIP);
			logger.error("Skipping test step as stop execution on error is set to true");
			return;
		}
		try {
			Thread.sleep(Integer.parseInt(testStep.getData()) * 1000);
			testStep.setStatus(TestStatus.PASS);
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx(ex.getLocalizedMessage());
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	@KeywordInfo(description = "This is used to verify analytics event", data = "", objectName = "Analytics event name provided in sheet")
	public void verifyEvent(DriverBuilder builder, TestStep testStep) {
		if (stopOnError) {
			testStep.setStatus(TestStatus.SKIP);
			logger.error("Skipping test step as stop execution on error is set to true");
			return;
		}
		try {
			this.waitForPageLoad(builder.getDriver(), testStep);
			for (HarEntry entry : builder.getProxy().getHar().getLog().getEntries()) {
				HarRequest request = entry.getRequest();
				if (request.getUrl().contains(Constants.URL_PARAMETER_1)
						&& request.getUrl().contains(Constants.URL_PARAMETER_2)) {
					Map<String, String> query = HelperUtils.getQueryParameterMap(request.getQueryString());
					if (query.keySet().containsAll(Constants.QUERY_MAP.keySet())) {
						Map<String, String> map = HelperUtils
								.getAnalyticsData(testStep.getAction(), testStep.getTestCase()).getData();
						for (String key : map.keySet()) {
							if (!(query.containsKey(key) && map.get(key).equals(query.get(key)))) {
								builder.getProxy().newHar(HelperUtils.getUniqueString());
								throw new Exception(
										"Key or value not matched. Key: " + key + "\nValue: " + query.get(key));
							}
						}
					}
				}
			}
			testStep.setStatus(TestStatus.PASS);
			builder.getProxy().newHar(HelperUtils.getUniqueString());
		} catch (Exception ex) {
			testStep.setStatus(TestStatus.FAIL);
			testStep.setEx(ex.getLocalizedMessage());
			logger.error("Error in executing test step. ", ex);
			stopOnError = testStep.onError();
		}
	}

	// @KeywordInfo(description = "", data = "", objectName = "")
	// public void verifyClickEvent(WebDriver driver, TestStep testStep) {
	// if (stopOnError) {
	// testStep.setStatus(TestStatus.SKIP);
	// logger.error("Skipping test step as stop execution on error is set to
	// true");
	// return;
	// }
	// try {
	//
	// testStep.setStatus(TestStatus.PASS);
	// } catch (Exception ex) {
	// testStep.setStatus(TestStatus.FAIL);
	// testStep.setEx(ex.getLocalizedMessage());
	// logger.error("Error in executing test step. ", ex);
	// stopOnError = testStep.onError();
	// }
	// }

}