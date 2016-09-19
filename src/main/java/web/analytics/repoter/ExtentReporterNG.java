package web.analytics.repoter;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import web.analytics.common.Constants;

public class ExtentReporterNG implements IReporter {

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		for (ISuite suite : suites) {
			Map<String, ISuiteResult> result = suite.getResults();
			for (ISuiteResult r : result.values()) {
				ITestContext context = r.getTestContext();
				buildTestNodes(context.getPassedTests(), LogStatus.PASS);
				buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
				buildTestNodes(context.getSkippedTests(), LogStatus.SKIP);
			}
		}
		ComplexReportFactory.closeReport();
		try {
			File file = new File(Constants.REPORT_PATH);
			Document doc = Jsoup.parse(file, "utf-8");
			doc.select("div.report-headline").first().text("Site: " + Constants.REPORT_NAME);
			FileUtils.write(file, doc.toString(), "utf-8");
		} catch (IOException e) {
			System.out.println("Unable to read report");
		}
		System.out.println("Report Generated: " + Constants.REPORT_PATH);
	}

	private void buildTestNodes(IResultMap tests, LogStatus status) {
		ExtentTest test;
		if (tests.size() > 0) {
			for (ITestResult result : tests.getAllResults()) {
				test = ComplexReportFactory.getTest(result.getMethod().getMethodName(),
						result.getMethod().getRealClass().getCanonicalName(), result.getMethod().getDescription());
				test.setDescription("<b>Test Case Description: </b>" + result.getMethod().getDescription());
				test.setStartedTime(getTime(result.getStartMillis()));
				test.setEndedTime(getTime(result.getEndMillis()));
				for (String group : result.getMethod().getGroups()) {
					test.assignCategory(group);
				}
				if (result.getThrowable() != null) {
					test.log(status, "Failed", "<br/>" + result.getThrowable().getLocalizedMessage());
				} else {
					test.log(status, "END", "Test Case Completed.");
				}
				ComplexReportFactory.closeTest(test);
			}
		}
	}

	private Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

}
