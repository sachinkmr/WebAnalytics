package web.analytics.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestCase {
	private String testCaseName;
	private Map<String, String> objectRepo;
	private String projectName;
	private String analyticsSheetLocation;
	private List<TestStep> testSteps = null;
	private static final Logger logger = LoggerFactory.getLogger(TestCase.class);

	public void setTestSteps(String testCaseName) {
		testSteps = new ArrayList<TestStep>();
		DataFormatter df = new DataFormatter();
		try {
			FileInputStream f = new FileInputStream(new File("input/TestCases/" + testCaseName + ".xlsx"));
			XSSFWorkbook workbook = new XSSFWorkbook(f);
			f.close();
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.rowIterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				String action = df.formatCellValue(row.getCell(3));
				if (null != action && !action.isEmpty()
						&& !df.formatCellValue(row.getCell(3)).equalsIgnoreCase("action")) {
					TestStep testStep = new TestStep();
					testStep.setStepNumber(df.formatCellValue(row.getCell(1)));
					testStep.setTestCaseName(df.formatCellValue(row.getCell(2)));
					testStep.setAction(df.formatCellValue(row.getCell(3)));
					testStep.setPageName(df.formatCellValue(row.getCell(4)));
					testStep.setObjectLocator(df.formatCellValue(row.getCell(5)));
					testStep.setOnError(df.formatCellValue(row.getCell(6)));
					testStep.setData(df.formatCellValue(row.getCell(7)));
					testSteps.add(testStep);
				}
			}
			workbook.close();
		} catch (FileNotFoundException e) {
			logger.error("Unable to find Test Case file", e);
			System.exit(1);
		} catch (IOException e) {
			logger.error("Unable to read Test Case file or unable to close Test Case file", e);
			System.exit(1);
		}

	}

	public List<TestStep> getTestSteps() {
		return this.testSteps;
	}

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
