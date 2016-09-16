package web.analytics.excel;

import java.util.HashMap;
import java.util.Map;

public class AnalyticsData {
	private String analyticsObjectname;
	private Map<String, String> data;

	public AnalyticsData(String analyticsObjectname) {
		super();
		this.analyticsObjectname = analyticsObjectname;
		this.data = new HashMap<>();
	}

	public void setData(String key, String value) {

		this.data.put(key, value);
	}

	public String getAnalyticsObjectname() {
		return analyticsObjectname;
	}

	public Map<String, String> getData() {
		return data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((analyticsObjectname == null) ? 0 : analyticsObjectname.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnalyticsData other = (AnalyticsData) obj;
		if (analyticsObjectname == null) {
			if (other.analyticsObjectname != null)
				return false;
		} else if (!analyticsObjectname.equals(other.analyticsObjectname))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return analyticsObjectname;
	}

}
