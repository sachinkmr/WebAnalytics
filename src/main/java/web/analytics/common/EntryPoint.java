package web.analytics.common;

import java.util.List;

import web.analytics.excel.Controller;
import web.analytics.excel.Suite;

public class EntryPoint {

    public static void main(String[] args) {
	Controller controller = new Controller("input/Controller.xlsx");
	List<Suite> suites = controller.getSuites();
	Object[][] a = new Object[][] { suites.toArray() };
	controller.close();
    }

}
