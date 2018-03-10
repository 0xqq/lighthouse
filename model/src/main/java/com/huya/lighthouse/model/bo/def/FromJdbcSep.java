package com.huya.lighthouse.model.bo.def;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.huya.lighthouse.util.AssertUtils;

public class FromJdbcSep {

	/**
	 * 数据库连接驱动
	 */
	private String jdbcDriver;

	/**
	 * 数据库连接url
	 */
	private String jdbcUrl;

	/**
	 * 数据库连接用户
	 */
	private String jdbcUsername;

	/**
	 * 数据库连接
	 */
	private String jdbcPassword;

	/**
	 * 从数据库中抽取数据的sql
	 */
	private String sql;

	/**
	 * 分库分表开始ID
	 */
	private int startIndex = 0;

	/**
	 * 分库分表结束ID
	 */
	private int endIndex = 0;

	public FromJdbcSep() {

	}

	public FromJdbcSep(String jdbcDriver, String jdbcUrl, String jdbcUsername, String jdbcPassword, String sql, int startIndex, int endIndex) {
		super();
		this.jdbcDriver = jdbcDriver;
		this.jdbcUrl = jdbcUrl;
		this.jdbcUsername = jdbcUsername;
		this.jdbcPassword = jdbcPassword;
		this.sql = sql;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getJdbcUsername() {
		return jdbcUsername;
	}

	public void setJdbcUsername(String jdbcUsername) {
		this.jdbcUsername = jdbcUsername;
	}

	public String getJdbcPassword() {
		return jdbcPassword;
	}

	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public void doAssert() throws Exception {
		AssertUtils.assertTrue(StringUtils.isNotBlank(jdbcDriver), "jdbcDriver cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(jdbcUrl), "jdbcUrl cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(jdbcUsername), "jdbcUsername cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(jdbcPassword), "jdbcPassword cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(sql), "sql cannot be blank!");
		AssertUtils.assertTrue((endIndex>0 && endIndex>startIndex) || (endIndex==0 && startIndex==0), "startIndex and endIndex is invalid!");
	}

	public void doTrim() {
		this.jdbcDriver = StringUtils.trim(this.jdbcDriver);
		this.jdbcUrl = StringUtils.trim(this.jdbcUrl);
		this.jdbcUsername = StringUtils.trim(this.jdbcUsername);
		this.jdbcPassword = StringUtils.trim(this.jdbcPassword);
		this.sql = StringUtils.trim(this.sql);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
