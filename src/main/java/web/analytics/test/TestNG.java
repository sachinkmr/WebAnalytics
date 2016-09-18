package web.analytics.test;

import java.util.List;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import web.analytics.excel.Controller;
import web.analytics.excel.Suite;
import web.analytics.excel.TestCase;

public class TestNG {
    Controller controller;

    @BeforeSuite(alwaysRun = true)
    public void setUp() {
	Controller controller = new Controller("input/Controller.xlsx");

    }

    @DataProvider
    public Object[][] getTestCases() {
	List<Suite> suites = controller.getSuites();
	Object[][] obj = new Object[suites.size()][];
	for (int i = 0; i < suites.size(); i++) {
	    List<TestCase> testCases = suites.get(i).getTestCases();
	    for (int j = 0; j < testCases.size(); j++) {
		obj[i][j] = testCases.get(j);
	    }
	}
	return obj;
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() {
	controller.close();
    }
}
