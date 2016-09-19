package web.analytics.test;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import web.analytics.excel.Controller;
import web.analytics.excel.Suite;
import web.analytics.excel.TestCase;

public class TestNG {
	Controller controller;

	@BeforeSuite(alwaysRun = true)
	public void setUp() {
		controller = new Controller("input/Controller.xlsx");

	}

	@DataProvider(name = "TestCases")
	public Object[][] getTestCases() {
		List<Suite> suites = controller.getSuites();
		List<TestCase> testCases = new ArrayList<>();
		for (Suite suite : suites) {
			for (TestCase testCase : suite.getTestCases()) {
				testCases.add(testCase);
			}
		}
		Object[][] obj = new TestCase[testCases.size()][];
		for (int i = 0; i < testCases.size(); i++) {
			obj[i] = new Object[] { testCases.get(i), "" };
		}
		return obj;
	}

	@Test(dataProvider = "TestCases")
	public void executeTestCases(TestCase testCase) {
		System.out.println(testCase.getTestCaseName());
	}

	@AfterSuite(alwaysRun = true)
	public void tearDown() {
		controller.close();
	}
}
