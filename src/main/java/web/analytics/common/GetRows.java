package web.analytics.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GetRows {
	private static List<AnalyticsData> analyticsDatalist = null;

	public static void main(String[] args) throws IOException {
		FileInputStream f = new FileInputStream(new File("D:\\Analytics.xlsx"));
		XSSFWorkbook workbook = new XSSFWorkbook(f);
		DataFormatter df = new DataFormatter();
		f.close();
		XSSFSheet sheet = workbook.getSheetAt(0);
		analyticsDatalist = new ArrayList<>();
		for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
			String cellValue = df.formatCellValue(sheet.getRow(i).getCell(1));
			if (null != cellValue && !cellValue.isEmpty()) {

				AnalyticsData analyticsData = new AnalyticsData(cellValue);

				for (int j = 3; j < sheet.getRow(i).getPhysicalNumberOfCells(); j++) {
					// System.out.println(sheet.getRow(i).getLastCellNum());
					if (null != df.formatCellValue(sheet.getRow(i).getCell(j))
							&& !df.formatCellValue(sheet.getRow(i).getCell(j)).isEmpty())
						analyticsData.setData(df.formatCellValue(sheet.getRow(i).getCell(j)),
								df.formatCellValue(sheet.getRow(i + 1).getCell(j)));

				}
				analyticsDatalist.add(analyticsData);
				i++;
			}

		}
		workbook.close();
		System.out.println(analyticsDatalist.size());
		for (int i = 0; i < analyticsDatalist.size(); i++) {

			System.out.println(analyticsDatalist.get(i));
			Map<String, String> dataMap = analyticsDatalist.get(i).getData();
			System.out.println(dataMap.values());
			System.out.println(dataMap.keySet());
		}

	}

}
