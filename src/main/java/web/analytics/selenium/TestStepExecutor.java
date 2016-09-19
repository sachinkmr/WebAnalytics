package web.analytics.selenium;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import web.analytics.excel.TestStep;

public class TestStepExecutor {
	Keywords key = new Keywords();
	protected static final Logger logger = LoggerFactory.getLogger(TestStepExecutor.class);

	public void executeTestStep(WebDriver driver, TestStep testStep) {
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
		case "VERIFYCLICKEVENT":
			key.verifyClickEvent(driver, testStep);
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
		case "MOUSEHOVER":
			key.mouseHover(driver, testStep);
			break;
		case "VERIFYPAGEEVENT":
			key.verifypageEvent(driver, testStep);
			break;
		default:
			try {
				throw new Exception("'" + testStep.getAction() + "'Keyword not found");
			} catch (Exception e) {
				logger.error("Error in Keyword: ", e);
			}
		}
	}
}
