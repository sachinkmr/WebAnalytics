package web.analytics.reporter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.NetworkMode;

import web.analytics.common.Constants;
import web.analytics.helper.HelperUtils;

public class ComplexReportFactory {

	public static ExtentReports reporter;
	public static Map<Long, String> threadToExtentTestMap = new HashMap<>();
	public static Map<String, ExtentTest> nameToTestMap = new HashMap<>();

	public synchronized static ExtentTest getTest(String testName, String className, String testDescription) {
		String name = className + "." + testName;
		if (!nameToTestMap.containsKey(name)) {
			ExtentTest test = getExtentReport().startTest(testName, testDescription);
			nameToTestMap.put(name, test);
			Long threadID = Thread.currentThread().getId();
			threadToExtentTestMap.put(threadID, name);
		}
		return nameToTestMap.get(name);
	}

	public synchronized static ExtentReports getExtentReport() {
		if (reporter == null) {
			String outputDirectory = new File("output" + File.separator + "Reports").getAbsolutePath();
			Constants.REPORT_PATH = outputDirectory + File.separator + "Report" + HelperUtils.getUniqueString()
					+ ".html";
			reporter = new ExtentReports(Constants.REPORT_PATH, true, DisplayOrder.OLDEST_FIRST, NetworkMode.ONLINE);
			reporter.loadConfig(new File(HelperUtils.getResourceFile("extent-config.xml")));
		}
		return reporter;
	}

	/*
	 * At any given instance there will be only one test running in a thread. We
	 * are already maintaining a thread to extentest map. This method should be
	 * used after some part of the code has already called getTest with proper
	 * testcase name and/or description.
	 *
	 * Reason: This method is not for creating test but getting an existing test
	 * reporter. sometime you are in a utility function and want to log
	 * something from there. Utility function or similar code sections are not
	 * bound to a test hence we cannot get a reporter via test name, unless we
	 * pass test name in all method calls. Which will be an overhead and a rigid
	 * design. However, there is one common association which is the thread ID.
	 * When testng executes a test it puts it launches a new thread, assuming
	 * test level threading, all tests will have a unique thread id hence call
	 * to this function will return the right extent test to use
	 */
	public synchronized static ExtentTest getTest() {
		Long threadID = Thread.currentThread().getId();
		if (threadToExtentTestMap.containsKey(threadID)) {
			String testName = threadToExtentTestMap.get(threadID);
			return nameToTestMap.get(testName);
		}
		// system log, this shouldnt happen but in this crazy times if it did
		// happen log it.
		return null;
	}

	public synchronized static ExtentTest getTest(String testName, String testDescription) {
		if (!nameToTestMap.containsKey(testName)) {
			ExtentTest test = getExtentReport().startTest(testName, testDescription);
			nameToTestMap.put(testName, test);
			Long threadID = Thread.currentThread().getId();
			threadToExtentTestMap.put(threadID, testName);
		}
		return nameToTestMap.get(testName);
	}

	public synchronized static ExtentTest getTest(String testName) {
		return getTest(testName, "");
	}

	public synchronized static void closeTest(String testName) {
		if (!testName.isEmpty()) {
			ExtentTest test = getTest(testName);
			getExtentReport().endTest(test);
		}
	}

	public synchronized static void closeTest(ExtentTest test) {
		if (test != null) {
			getExtentReport().endTest(test);
		}
	}

	public synchronized static void closeTest() {
		ExtentTest test = getTest();
		closeTest(test);
	}

	public synchronized static void closeReport() {
		if (reporter != null) {
			reporter.flush();
			reporter.close();
			reporter = null;
		}
	}
}