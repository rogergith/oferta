package com.weavx.web.utils;

import java.util.Properties;

public class UtilPropertiesLoader {
	
	private static UtilPropertiesLoader _instance = null;
	private Properties props = null;
	private Properties propsDashboard = null;
	
	
	private UtilPropertiesLoader() {
		super();
		try {
			props = new Properties();
			props.load(getClass().getResourceAsStream("/admin.properties"));
			
			propsDashboard = new Properties();
			propsDashboard.load(getClass().getResourceAsStream("/dashboard.properties"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static UtilPropertiesLoader getInstance() {
		if (_instance == null)
			_instance = new UtilPropertiesLoader();
		return _instance;
	}
	
	public String getProp(String name) {
		return (String)this.props.get(name);
	}
	
	public Properties getProps() {
		return this.props;
	}
	
	public String getPropDashboard(String name) {
		return (String)this.propsDashboard.get(name);
	}
	
	public Properties getPropsDashboard() {
		return this.propsDashboard;
	}
	
	
}
