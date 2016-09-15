package web.analytics.base;

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

import web.analytics.helper.HelperUtils;

public class Controller {
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
			System.exit(1);
		} catch (IOException e) {
			logger.error("Unable to read Controller file", e);
			System.exit(1);
		}
	}

	/**
	 * 
	 * asdasdasdasdas asd asd
	 **/
	public List<Suite> getSuites() {
		List<Suite> suites = new ArrayList<>();
		XSSFSheet sheet = workbook.getSheet("Suites");
		Iterator<Row> rowIterator = sheet.rowIterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			String name = df.formatCellValue(row.getCell(1));
			if (null != name && !name.isEmpty()
					&& !df.formatCellValue(row.getCell(1)).equalsIgnoreCase("Project_Suite_Name")
					&& df.formatCellValue(row.getCell(2)).equalsIgnoreCase("yes")) {
				suites.add(getSuite(name));
			}
		}
		return suites;
	}

	public Suite getSuite(String name) {
		Suite suite = new Suite(name);
		XSSFSheet sheet = workbook.getSheet(name);
		Iterator<Row> rowIterator = sheet.rowIterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			String testCaseName = df.formatCellValue(row.getCell(1));
			// adding test cases name and oother properttiees
			if (null != testCaseName && !testCaseName.isEmpty()
					&& !df.formatCellValue(row.getCell(1)).equalsIgnoreCase("Test_Scenario_Name")
					&& df.formatCellValue(row.getCell(3)).equalsIgnoreCase("yes")) {
				suite.setObjectRepo(HelperUtils.getObjectRepository(df.formatCellValue(row.getCell(2))));
				suite.setProjectName(df.formatCellValue(row.getCell(4)));
				suite.setAnalyticsSheetLocation(df.formatCellValue(row.getCell(5)));
			}
		}
		return suite;
	}

	public void close() {
		try {
			workbook.close();
		} catch (IOException e) {
			logger.error("Unable to close Controller file", e);
		}
	}

}
