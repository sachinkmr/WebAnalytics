package web.analytics.reporter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

import web.analytics.common.Constants;

public class ExtentReporterNG implements IReporter {

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
	try {
	    Thread.sleep(2000);
	    ComplexReportFactory.closeReport();
	    File file = new File(Constants.REPORT_PATH);
	    Document doc = Jsoup.parse(file, "utf-8");
	    doc.select("div.report-headline").first().text("Report: " + Constants.REPORT_NAME);
	    FileUtils.write(file, doc.toString(), "utf-8");
	} catch (IOException | InterruptedException e) {
	    System.out.println("Unable to read report");
	}
	System.out.println("Report Generated: " + Constants.REPORT_PATH);
    }

}
