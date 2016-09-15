package web.analytics.base;

import java.util.List;

public class Suite {
	private final String suiteName;
	private List<TestCase> testCases;

	public List<TestCase> getTestCases() {
		return testCases;
	}

	public void setTestCases(List<TestCase> testCases) {
		this.testCases = testCases;
	}

	public Suite(String suiteName) {
		this.suiteName = suiteName;
	}

	public String getSuiteName() {
		return suiteName;
	}

}
