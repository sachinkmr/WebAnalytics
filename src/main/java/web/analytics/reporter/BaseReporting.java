package web.analytics.reporter;

import java.lang.reflect.Method;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;

public class BaseReporting {
	public ExtentTest test;

	@BeforeMethod
	public void beforeMethod(Method caller) {
		test = ComplexReportFactory.getTest(caller.getName(), this.getClass().getCanonicalName(),
				caller.getAnnotationsByType(Test.class)[0].description());
		System.out.println("Executing: " + caller.getName());
	}

}
