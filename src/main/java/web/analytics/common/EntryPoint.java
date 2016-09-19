package web.analytics.common;

import java.util.List;

import web.analytics.excel.Controller;
import web.analytics.excel.Suite;

public class EntryPoint {

	public static void main(String[] args) {
		// TestNG testng = new TestNG();
		// List<String> files = new ArrayList<>();
		// files.add("testNG.xml");
		// // testng.addListener(new ExtentReporterNG());
		// testng.setUseDefaultListeners(false);
		// testng.setTestSuites(files);
		// testng.setVerbose(0);
		// testng.run();

		Controller controller = new Controller("input/Controller.xlsx");
		List<Suite> suites = controller.getSuites();
		Object[][] a = new Object[][] { suites.toArray() };
		System.out.println(a);
		controller.close();
	}

}
