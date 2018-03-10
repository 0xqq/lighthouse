package com.huya.lighthouse.dataswitch.util;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.Assert;

import com.huya.lighthouse.dataswitch.BaseObject;

public abstract class DataSourceProvider extends BaseObject {

	private DataSource dataSource;
	private String username;
	private String password;
	private String url;
	private String driverClass;

	public DataSource getDataSource() {
		if (dataSource == null) {
			Assert.notNull(url, "jdbc url must be not empty");
			this.dataSource = getDataSource(username, password, url, driverClass);
		}
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	private static Map<String, DataSource> dataSourceCache = new HashMap<String, DataSource>();

	private static DataSource getDataSource(String username, String password, String url, String driverClass) {
		String dataSourceKey = url + username + password;
		DataSource result = dataSourceCache.get(dataSourceKey);
		if (result == null) {
			DriverManagerDataSource ds = new DriverManagerDataSource();
			ds.setDriverClassName(driverClass);
			ds.setUsername(username);
			ds.setPassword(password);
			ds.setUrl(url);
			dataSourceCache.put(dataSourceKey, ds);
			result = ds;
		}
		return result;
	}

}
