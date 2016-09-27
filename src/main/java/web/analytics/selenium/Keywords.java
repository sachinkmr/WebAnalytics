package web.analytics.selenium;

import java.util.Map;

import org.openqa.selenium.By;
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
import web.analytics.helpers.HelperUtils;
import web.analytics.suite.TestStatus;
import web.analytics.suite.TestStep;

public class Keywords {
    protected static final Logger logger = LoggerFactory.getLogger(Keywords.class);
    public boolean stopOnError = false;
    WebDriverWait wait;

    @KeywordInfo(description = "This Keyword is used to launch browser and opens web page", data = "web page url", objectName = "..")
    public void launch(WebDriver driver, TestStep testStep) {
	wait = new WebDriverWait(driver, 30);
	testStep.getTestCase().getDataMap().put("SITE_URL", HelperUtils.getSiteURL(testStep.getData()));
	if (!executeBeforeKeyword(driver, testStep))
	    return;
	try {
	    driver.get(testStep.getData());
	    testStep.setStatus(TestStatus.PASS);
	    Thread.sleep(3000);
	} catch (Exception ex) {
	    testStep.setStatus(TestStatus.FAIL);
	    testStep.setEx(ex.getLocalizedMessage());
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
	    testStep.setEx(ex.getLocalizedMessage());
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
	    testStep.setEx(ex.getLocalizedMessage());
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
	    testStep.setEx(ex.getLocalizedMessage());
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
	    testStep.setEx(ex.getLocalizedMessage());
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
	    testStep.setEx(ex.getLocalizedMessage());
	    logger.error("Error in executing test step. ", ex);
	    stopOnError = testStep.onError();
	}
    }

    @KeywordInfo(description = "This is used to close the browser current tab/windows", data = "..", objectName = "..")
    public void close(WebDriver driver, TestStep testStep) {
	if (!executeBeforeKeyword(driver, testStep))
	    return;
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
	if (!executeBeforeKeyword(driver, testStep))
	    return;
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
	if (!executeBeforeKeyword(driver, testStep))
	    return;
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
	    testStep.setEx(ex.getLocalizedMessage());
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
	    testStep.setEx(ex.getLocalizedMessage());
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
	    testStep.setEx(ex.getLocalizedMessage());
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
	    testStep.setEx(ex.getLocalizedMessage());
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
	    testStep.setEx(ex.getLocalizedMessage());
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
	    testStep.setEx(ex.getLocalizedMessage());
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
	    testStep.setEx(ex.getLocalizedMessage());
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
	    testStep.setEx(ex.getLocalizedMessage());
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
	    testStep.setEx(ex.getLocalizedMessage());
	    logger.error("Error in executing test step. ", ex);
	    stopOnError = testStep.onError();
	}
    }

    @KeywordInfo(description = "This is used to verify analytics event", data = "..", objectName = "Analytics event name provided in sheet")
    public void verifyEvent(DriverBuilder builder, TestStep testStep) {
	if (!executeBeforeKeyword(builder.getDriver(), testStep))
	    return;
	try {
	    boolean fired = false;
	    Thread.sleep(3000);
	    this.waitForPageLoad(builder.getDriver(), testStep);
	    for (HarEntry entry : builder.getProxy().getHar().getLog().getEntries()) {
		HarRequest request = entry.getRequest();
		if (request.getUrl().contains(Constants.URL_PARAMETER_1)
			&& request.getUrl().contains(Constants.URL_PARAMETER_2)) {
		    Map<String, String> query = HelperUtils.getQueryParameterMap(request.getQueryString());
		    if (query.entrySet().containsAll(Constants.QUERY_MAP.entrySet())) {
			Map<String, String> map = HelperUtils
				.getAnalyticsData(testStep.getObjectLocator(), testStep.getTestCase()).getData();
			testStep.setData(map.toString());
			if (query.entrySet().containsAll(map.entrySet())) {
			    fired = true;
			    break;
			}
		    }
		}
	    }
	    if (!fired) {
		throw new Exception("Event not fired. Event: " + testStep.getObjectLocator());
	    }
	    testStep.setStatus(TestStatus.PASS);
	} catch (Exception ex) {
	    testStep.setStatus(TestStatus.FAIL);
	    testStep.setEx(ex.getLocalizedMessage());
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
}
