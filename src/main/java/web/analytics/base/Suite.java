package web.analytics.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Suite {
	private final String suiteName;
	private Map<String, String> objectRepo;
	private String projectName;
	private String analyticsSheetLocation;

	public Suite(String suiteName) {
		this.suiteName = suiteName;
	}

	public List<TestCase> getTestCases() {
		List<TestCase> testCases = new ArrayList<>();
		
		return testCases;
	}

	public String getSuiteName() {
		return suiteName;
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

	@Override
	public String toString() {
		return "Suite [suiteName=" + suiteName + "]";
	}

}
