package web.analytics.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import web.analytics.driver.DriverBuilder;
import web.analytics.excel.Controller;
import web.analytics.excel.Suite;
import web.analytics.excel.TestCase;
import web.analytics.excel.TestStep;
import web.analytics.reporter.ComplexReportFactory;
import web.analytics.selenium.TestStepExecutor;

public class TestExecutor {
	List<TestCase> testCases = new ArrayList<>();
	DriverBuilder builder;
	protected static final Logger logger = LoggerFactory.getLogger(TestExecutor.class);

	@BeforeSuite(alwaysRun = true)
	public void setUp() {
		Controller controller = new Controller("input/Controller.xlsx");
		List<Suite> suites = controller.getSuites();
		controller.close();
		builder = new DriverBuilder();
		builder.getChromeDriver();
		TestStepExecutor executor = new TestStepExecutor();
		try {
			for (Suite suite : suites) {
				for (TestCase testCase : suite.getTestCases()) {
					testCases.add(testCase);
					System.out.println("Executing Test Case: " + testCase.getTestCaseName());
					for (TestStep step : testCase.getTestSteps()) {
						System.out.println(" - Executing Test Step: " + step.getStepNumber());
						executor.executeTestStep(builder, step);
					}
					System.out.println("-------------------------------------------------------------- ");
				}
			}
		} catch (Exception ex) {
			logger.error("Error :", ex);
		}
		builder.close();
	}

	@DataProvider(name = "TestCases")
	public Object[][] getTestCases() {
		Object[][] obj = new Object[testCases.size()][];
		for (int i = 0; i < testCases.size(); i++) {
			obj[i] = new Object[] { testCases.get(i) };
		}
		return obj;
	}

	@Test(dataProvider = "TestCases")
	public void executeTestCases(TestCase testCase) {
		ExtentTest test = ComplexReportFactory.getTest(testCase.getTestCaseName());
		test.assignCategory(testCase.getSuiteName());
		for (TestStep step : testCase.getTestSteps()) {
			if (step.getStatus().name().equalsIgnoreCase("PASS")) {
				test.log(LogStatus.PASS, step.getTestStepID() + " ### " + step.getAction() + " ### "
						+ step.getObjectLocator() + " ### " + step.getData() + "### ..");
			} else if (step.getStatus().name().equalsIgnoreCase("FAIL")) {
				test.log(LogStatus.FAIL, step.getTestStepID() + " ### " + step.getAction() + " ### "
						+ step.getObjectLocator() + " ### " + step.getData() + " ### " + step.getEx());
			} else if (step.getStatus().name().equalsIgnoreCase("SKIP")) {
				test.log(LogStatus.SKIP,
						step.getTestStepID() + " ### " + step.getAction() + " ### " + step.getObjectLocator() + " ### "
								+ step.getData()
								+ " ### Skipping test step as stop execution on error is set to true in previous step");
			} else {
				test.log(LogStatus.UNKNOWN, step.getTestStepID() + " ### " + step.getAction() + " ### "
						+ step.getObjectLocator() + " ### " + step.getData() + " ### Unknown step execution state");
			}
		}
		ComplexReportFactory.closeTest(test);
	}

	@AfterSuite(alwaysRun = true)
	public void tearDown() {
		try {
			Thread.sleep(1000);
			builder.close();
		} catch (Exception e) {
			logger.error("Error :", e);
		}
		FileUtils.deleteQuietly(new File(Constants.TEMP));
	}
}
