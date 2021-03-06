package web.analytics.suite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import web.analytics.helpers.HelperUtils;

public class Controller implements AutoCloseable {
    private XSSFWorkbook workbook;
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    private DataFormatter df = new DataFormatter();

    public Controller(String filePath) {
	try {
	    FileInputStream f = new FileInputStream(new File(filePath));
	    workbook = new XSSFWorkbook(f);
	    f.close();
	} catch (FileNotFoundException e) {
	    logger.error("Unable to find Controller file", e);
	    System.out.println("Error in application: "+ e);
	} catch (IOException e) {
	    logger.error("Unable to read Controller file", e);
	    System.out.println("Error in application: "+ e);
	}
    }

    /**
     * Method to read all suites and their data from excel files
     * 
     * @return List<Suite> returns list of suites
     * 
     **/
    public List<TestSuite> getSuites() {
	List<TestSuite> testSuites = new ArrayList<>();
	XSSFSheet sheet = workbook.getSheet("Suites");
	Iterator<Row> rowIterator = sheet.rowIterator();
	while (rowIterator.hasNext()) {
	    Row row = rowIterator.next();
	    String name = df.formatCellValue(row.getCell(1));
	    if (null != name && !name.isEmpty()
		    && !df.formatCellValue(row.getCell(1)).equalsIgnoreCase("Project_Suite_Name")
		    && df.formatCellValue(row.getCell(2)).equalsIgnoreCase("yes")) {
		TestSuite testSuite = new TestSuite(name);
		testSuite.setTestCases(getTestCases(testSuite));
		testSuites.add(testSuite);
	    }
	}
	return testSuites;
    }

    /**
     * Method to read all test cases and their data
     * 
     * @param testSuite
     *            name of the suite.
     * 
     * @return List<TestCase> returns list of test cases for test suite
     * 
     **/
    public List<TestCase> getTestCases(TestSuite testSuite) {
	List<TestCase> testCases = new ArrayList<>();
	XSSFSheet sheet = workbook.getSheet(testSuite.getSuiteName());
	Iterator<Row> rowIterator = sheet.rowIterator();
	while (rowIterator.hasNext()) {
	    Row row = rowIterator.next();
	    String testCaseName = df.formatCellValue(row.getCell(1));
	    // adding test cases name and oother properttiees
	    if (null != testCaseName && !testCaseName.isEmpty()
		    && !df.formatCellValue(row.getCell(1)).equalsIgnoreCase("Test_Scenario_Name")
		    && df.formatCellValue(row.getCell(3)).equalsIgnoreCase("yes")) {
		TestCase testCase = new TestCase(testCaseName);
		testCase.setObjectRepo(HelperUtils.getObjectRepository(df.formatCellValue(row.getCell(2))));
		testCase.setProjectName(df.formatCellValue(row.getCell(4)));
		testCase.setAnalyticsSheetLocation(df.formatCellValue(row.getCell(5)));
		testCase.setSuiteName(testSuite.getSuiteName());
		testCases.add(testCase);
	    }
	}
	return testCases;
    }

    /**
     * Method to close controller resources.
     * 
     * 
     **/
    @Override
    public void close() {
	try {
	    workbook.close();
	} catch (Exception e) {
	    logger.error("Unable to close Controller file", e);
	}
    }

}
