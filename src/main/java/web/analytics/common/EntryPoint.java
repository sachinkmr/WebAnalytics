package web.analytics.common;

import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;

import web.analytics.helper.HelperUtils;
import web.analytics.reporter.ExtentReporterNG;

public class EntryPoint {

	public static void main(String[] args) {
		// BasicConfigurator.configure();
		TestNG testng = new TestNG();
		List<String> files = new ArrayList<>();
		files.add(HelperUtils.getResourceFile("testNG.xml"));
		testng.addListener(new ExtentReporterNG());
		testng.setUseDefaultListeners(false);
		testng.setTestSuites(files);
		testng.setVerbose(0);
		testng.run();
	}

}
