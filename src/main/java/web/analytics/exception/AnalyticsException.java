package web.analytics.exception;

public class AnalyticsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 11L;

	public AnalyticsException() {
		super();
	}

	public AnalyticsException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public AnalyticsException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public AnalyticsException(String arg0) {
		super(arg0);
	}

	public AnalyticsException(Throwable arg0) {
		super(arg0);
	}

}
