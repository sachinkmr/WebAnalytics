package web.analytics.base;

import java.util.Map;

public class TestCase {
	private String testCaseName;
	private Map<String, String> objectRepo;
	private String projectName;
	private String analyticsSheetLocation;

	public TestCase(String testCaseName) {

		this.testCaseName = testCaseName;
	}

	public String getTestCaseName() {
		return testCaseName;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	public Map<String, String> getObjectRepo() {
		return objectRepo;
	}

	public void setObjectRepo(Map<String, String> objectRepo) {
		this.objectRepo = objectRepo;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getAnalyticsSheetLocation() {
		return analyticsSheetLocation;
	}

	public void setAnalyticsSheetLocation(String analyticsSheetLocation) {
		this.analyticsSheetLocation = analyticsSheetLocation;
	}

}
