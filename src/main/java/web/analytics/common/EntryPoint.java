package web.analytics.common;

import java.util.List;

import web.analytics.base.Controller;
import web.analytics.base.Suite;

public class EntryPoint {

	public static void main(String[] args) {
		Controller controller = new Controller("input/Controller.xlsx");
		List<Suite> suites = controller.getSuites();
		controller.close();
	}

}
