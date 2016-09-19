package web.analytics.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestCase {
    private String testCaseName;
    private String suiteName;
    private Map<String, By> objectRepo;
    private String projectName;
    private String analyticsSheetLocation;
    private List<TestStep> testSteps = null;
    private static final Logger logger = LoggerFactory.getLogger(TestCase.class);
    private List<AnalyticsData> analyticsDataList;
    private DataFormatter df = new DataFormatter();

    /**
     * @return the suiteName
     */
    public String getSuiteName() {
	return suiteName;
    }

    /**
     * @param suiteName
     *            the suiteName to set
     */
    public void setSuiteName(String suiteName) {
	this.suiteName = suiteName;
    }

    private void setTestSteps(String testCaseName) {
	testSteps = new ArrayList<>();
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
		    testStep.setTestCase(this);
		    testStep.setStepNumber(df.formatCellValue(row.getCell(1)).trim().isEmpty() ? ".."
			    : df.formatCellValue(row.getCell(1)));
		    testStep.setTestStepID(df.formatCellValue(row.getCell(2)).trim().isEmpty() ? ".."
			    : df.formatCellValue(row.getCell(2)));
		    testStep.setAction(df.formatCellValue(row.getCell(3)));
		    testStep.setObjectLocator(df.formatCellValue(row.getCell(4)).trim().isEmpty() ? ".."
			    : df.formatCellValue(row.getCell(4)));
		    testStep.setOnError(df.formatCellValue(row.getCell(5)).trim().isEmpty() ? "continue"
			    : df.formatCellValue(row.getCell(5)));
		    testStep.setData(df.formatCellValue(row.getCell(6)).trim().isEmpty() ? ".."
			    : df.formatCellValue(row.getCell(6)));
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
	this.setTestSteps(testCaseName);
    }

    public String getTestCaseName() {
	return testCaseName;
    }

    public void setTestCaseName(String testCaseName) {
	this.testCaseName = testCaseName;
    }

    public Map<String, By> getObjectRepo() {
	return objectRepo;
    }

    public void setObjectRepo(Map<String, By> objectRepo) {
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

    public void setAnalyticsSheetLocation(String analyticsSheetName) {
	this.analyticsSheetLocation = "input/AnalyticsSheets/" + analyticsSheetName + ".xlsx";
	readAnalyticsSheet(analyticsSheetLocation);
    }

    public List<AnalyticsData> getAnalyticsDataList() {
	return analyticsDataList;
    }

    private void readAnalyticsSheet(String analyticsSheetLocation) {
	analyticsDataList = new ArrayList<>();
	try {
	    FileInputStream f = new FileInputStream(new File(analyticsSheetLocation));
	    XSSFWorkbook workbook = new XSSFWorkbook(f);
	    f.close();
	    XSSFSheet sheet = workbook.getSheetAt(0);
	    Iterator<Row> rows = sheet.iterator();
	    List<String> keys = new ArrayList<>();
	    List<String> values = new ArrayList<>();
	    boolean keysRow = true;
	    while (rows.hasNext()) {
		Row row = rows.next();
		if (row.getPhysicalNumberOfCells() > 0 && isKeysRowEmpty(row, keysRow)) {
		    Iterator<Cell> cells = row.cellIterator();
		    while (cells.hasNext()) {
			Cell cell = cells.next();
			String cellValue = df.formatCellValue(cell);
			if (null != cellValue) {
			    if (keysRow) {
				keys.add(cellValue);
			    } else {
				values.add(cellValue);
			    }
			}
		    }
		    keysRow = false;
		    if (!values.isEmpty()) {
			keysRow = true;
			AnalyticsData analyticsData = new AnalyticsData(keys.get(0));
			for (int i = values.indexOf("Expected values") + 1; i < keys.size(); i++) {
			    analyticsData.setData(keys.get(i), values.get(i));
			}
			keys.clear();
			values.clear();
			analyticsDataList.add(analyticsData);
		    }
		}
	    }

	    workbook.close();
	} catch (FileNotFoundException e) {
	    logger.error("Unable to find Analytics file", e);
	    System.exit(1);
	} catch (IOException e) {
	    logger.error("Unable to read Analytics file or unable to close Analytics file", e);
	    System.exit(1);
	} catch (Exception e) {
	    logger.error("Unable to read Analytics file or unable to close Analytics file", e);
	    System.exit(1);
	}
    }

    private boolean isKeysRowEmpty(Row row, boolean keysRow) {
	if (!keysRow) {
	    return true;
	} else {
	    Iterator<Cell> cells = row.cellIterator();
	    while (cells.hasNext()) {
		Cell cell = cells.next();
		if (null != cell && !df.formatCellValue(cell).isEmpty()) {
		    return true;
		}
	    }
	}
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((testCaseName == null) ? 0 : testCaseName.hashCode());
	return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	TestCase other = (TestCase) obj;
	if (testCaseName == null) {
	    if (other.testCaseName != null)
		return false;
	} else if (!testCaseName.equals(other.testCaseName))
	    return false;
	return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return testCaseName;
    }

}
