package web.analytics.common;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import net.lightbody.bmp.core.har.Har;
import web.analytics.selenium.DriverBuilder;

public class Sachin {

    public static void main(String[] args) {
	DriverBuilder builder = new DriverBuilder();
	WebDriver driver = builder.getChromeDriver();
	driver.get("http://tresemme-stg.unileversolutions.com/us/en/hair-product.html");
	Har har = builder.getProxy().getHar();
	try {
	    har.writeTo(new File("d://asd.html"));
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	builder.getProxy().newHar(new Date().toString());
	driver.findElement(By.cssSelector(".c-global-footer-v2-links-list li")).click();
	try {
	    Thread.sleep(10000);
	} catch (InterruptedException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
	har = builder.getProxy().getHar();
	try {
	    har.writeTo(new File("d://asdas.html"));
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	driver.close();
	builder.close();

    }

}
