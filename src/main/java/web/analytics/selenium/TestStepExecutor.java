package web.analytics.selenium;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import web.analytics.driver.DriverBuilder;
import web.analytics.excel.TestStep;

public class TestStepExecutor {
	protected static final Logger logger = LoggerFactory.getLogger(TestStepExecutor.class);

	public void executeTestStep(DriverBuilder builder, TestStep testStep, Keywords key) {
		try {
			WebDriver driver = builder.getDriver();
			switch (testStep.getAction().toUpperCase()) {
			case "LAUNCH":
				key.launch(driver, testStep);
				break;
			case "CLICK":
				key.click(driver, testStep);
				break;
			case "SUBMIT":
				key.submit(driver, testStep);
				break;
			case "ENTER":
				key.enter(driver, testStep);
				break;
			case "VERIFYEVENT":
				key.verifyEvent(builder, testStep);
				break;
			case "SELECT":
				key.select(driver, testStep);
				break;
			case "WAITFORPAGELOAD":
				key.waitForPageLoad(driver, testStep);
				break;
			case "CLOSE":
				key.close(driver, testStep);
				break;
			case "DELETECOOKIES":
				key.deleteCookies(driver, testStep);
				break;
			case "REFRESH":
				key.refresh(driver, testStep);
				break;
			case "DOUBLECLICK":
				key.doubleClick(driver, testStep);
				break;
			case "CLICKCHECKBOX":
				key.clickCheckBox(driver, testStep);
				break;
			case "CLEAR":
				key.clear(driver, testStep);
				break;
			case "WAIT":
				key.wait(driver, testStep);
				break;
			case "MOUSEHOVER":
				key.mouseHover(driver, testStep);
				break;
			default:
				try {
					throw new Exception("'" + testStep.getAction() + "'Keyword not found");
				} catch (Exception e) {
					logger.error("Error in Keyword: ", e);
				}
			}
		} catch (Exception ex) {
			logger.error("Error in Step: ", ex);
		}

	}
}
