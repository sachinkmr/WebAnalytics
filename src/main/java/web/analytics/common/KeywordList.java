package web.analytics.common;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import web.analytics.helper.HelperUtils;
import web.analytics.selenium.KeywordInfo;
import web.analytics.selenium.Keywords;

public class KeywordList {
	public static void main(String[] args) {
		Map<String, KeywordInfo> map = new TreeMap<String, KeywordInfo>();
		Method[] methods = Keywords.class.getMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(KeywordInfo.class)) {
				Annotation annotation = method.getAnnotation(KeywordInfo.class);
				KeywordInfo keywordInfo = (KeywordInfo) annotation;
				map.put(Character.toUpperCase(method.getName().charAt(0)) + method.getName().substring(1), keywordInfo);
			}
		}
		File file = new File("KeywordsList.html");
		try {
			Document doc = Jsoup.parse(
					IOUtils.toString(HelperUtils.class.getClassLoader().getResourceAsStream("KeywordsList.html")),
					"utf-8");
			Element element = doc.select("table tbody").first();
			int i = 1;
			for (String key : map.keySet()) {
				element.append("<tr><td>" + i++ + "</td><td>" + key + "</td><td>" + map.get(key).objectName()
						+ "</td><td>" + map.get(key).data() + "</td><td>" + map.get(key).description() + "</td></tr>");
			}
			FileUtils.write(file, doc.toString(), "utf-8");
		} catch (IOException e) {
			System.out.println("Unable to read report");
		}
		System.out.println("Keyword List is generated at: " + file.getAbsolutePath());

	}

}
