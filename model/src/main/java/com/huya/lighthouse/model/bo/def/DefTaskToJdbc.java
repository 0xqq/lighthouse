package com.huya.lighthouse.model.bo.def;

import org.apache.commons.lang.StringUtils;

import com.huya.lighthouse.model.type.TaskType;
import com.huya.lighthouse.util.AssertUtils;

/**
 * 
 * 
 */
public class DefTaskToJdbc extends AbstractBODefTask {

	private static final long serialVersionUID = -5164333644851267262L;

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
	 * 数据所在目录
	 */
	private String dataPaths;

	/**
	 * 数据插入的表名
	 */
	private String tableName;

	/**
	 * 数据插入表中对应的列名（请用英文逗号分隔,且中间不允许有空格）
	 */
	private String columns;

	/**
	 * 导入前执行
	 */
	private String beforeSql;

	/**
	 * 导入后执行
	 */
	private String afterSql;

	/**
	 * 分隔符
	 **/
	private String columnSplit = "\001";

	/**
	 * hive 的null 值特殊转义字符
	 */
	private String nullValue = "\\N";

	/**
	 * 批量导入的大小
	 */
	private int batchUpdateSize = 3000;

	public DefTaskToJdbc() {
		this.setTaskType(TaskType.TOJDBC.name());
		this.setTaskPlugin("java -Xms512m -Xmx512m -cp $pluginDir/lighthouse-plugin-toJdbc-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.huya.lighthouse.plugin.toJdbc.ToJdbcMain");
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

	public String getDataPaths() {
		return dataPaths;
	}

	public void setDataPaths(String dataPaths) {
		this.dataPaths = dataPaths;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public String getBeforeSql() {
		return beforeSql;
	}

	public void setBeforeSql(String beforeSql) {
		this.beforeSql = beforeSql;
	}

	public String getAfterSql() {
		return afterSql;
	}

	public void setAfterSql(String afterSql) {
		this.afterSql = afterSql;
	}

	public String getColumnSplit() {
		return columnSplit;
	}

	public void setColumnSplit(String columnSplit) {
		this.columnSplit = columnSplit;
	}

	public String getNullValue() {
		return nullValue;
	}

	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}

	public int getBatchUpdateSize() {
		return batchUpdateSize;
	}

	public void setBatchUpdateSize(int batchUpdateSize) {
		this.batchUpdateSize = batchUpdateSize;
	}

	@Override
	public void doAssert() throws Exception {
		super.doAssert();
		AssertUtils.assertTrue(StringUtils.isNotBlank(jdbcDriver), "jdbcDriver cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(jdbcUrl), "jdbcUrl cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(jdbcUsername), "jdbcUsername cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(jdbcPassword), "jdbcPassword cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(tableName), "tableName cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(columns), "columns cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(columnSplit), "columnSplit cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(nullValue), "nullValue cannot be blank!");
	}

	@Override
	public void doTrim() {
		super.doTrim();
		this.jdbcDriver = StringUtils.trim(this.jdbcDriver);
		this.jdbcUrl = StringUtils.trim(this.jdbcUrl);
		this.jdbcUsername = StringUtils.trim(this.jdbcUsername);
		this.jdbcPassword = StringUtils.trim(this.jdbcPassword);
		this.dataPaths = StringUtils.trim(this.dataPaths);
		this.tableName = StringUtils.trim(this.tableName);
		this.columns = StringUtils.trim(this.columns);
		this.nullValue = StringUtils.trim(this.nullValue);
		this.beforeSql = StringUtils.trim(this.beforeSql);
		this.afterSql = StringUtils.trim(this.afterSql);
	}
}
