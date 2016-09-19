package web.analytics.common;

import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;

import web.analytics.reporter.ExtentReporterNG;

public class EntryPoint {

	public static void main(String[] args) {
		TestNG testng = new TestNG();
		List<String> files = new ArrayList<>();
		files.add("testNG.xml");
		testng.addListener(new ExtentReporterNG());
		testng.setUseDefaultListeners(false);
		testng.setTestSuites(files);
		testng.setVerbose(0);
		testng.run();
	}

}
