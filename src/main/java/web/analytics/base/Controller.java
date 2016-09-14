package web.analytics.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import web.analytics.helper.HelperUtils;

public class Controller {
	private final String filePath;
	private HSSFWorkbook workbook;
	protected static final Logger logger = LoggerFactory.getLogger(Controller.class);

	public Controller(String filePath) {
		this.filePath = filePath;
	}

	public List<Suite> getSuites() {
		List<Suite> suites = new ArrayList<>();
		try {
			FileInputStream f = new FileInputStream(new File(filePath));
			workbook = new HSSFWorkbook(f);
			HSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.rowIterator();
			DataFormatter df = new DataFormatter();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				String name = df.formatCellValue(row.getCell(1));
				if (null != name && !name.isEmpty() && df.formatCellValue(row.getCell(3)).equalsIgnoreCase("yes")) {
					Suite suite = new Suite(name);
					suite.setObjectRepo(HelperUtils.getObjectRepository(df.formatCellValue(row.getCell(2))));
					suite.setProjectName(df.formatCellValue(row.getCell(4)));
					suite.setAnalyticsSheetLocation(df.formatCellValue(row.getCell(5)));
					suite.setScenario(getScenario());
				}
			}

		} catch (FileNotFoundException e) {
			logger.error("Unable to find Controller file", e);
			System.exit(1);
		} catch (IOException e) {
			logger.error("Unable to read Controller file", e);
			System.exit(1);
		}
		return suites;
	}

	private TestScenario getScenario() {
		// TODO Auto-generated method stub
		return null;
	}

	public void close() {
		try {
			workbook.close();
		} catch (IOException e) {
			logger.error("Unable to close Controller file", e);
		}
	}

}
