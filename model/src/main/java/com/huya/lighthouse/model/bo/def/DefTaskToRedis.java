package com.huya.lighthouse.model.bo.def;

import org.apache.commons.lang.StringUtils;

import com.huya.lighthouse.model.type.TaskType;
import com.huya.lighthouse.util.AssertUtils;

/**
 * 
 * 
 */
public class DefTaskToRedis extends AbstractBODefTask {

	private static final long serialVersionUID = 369369075317025603L;

	/**
	 * 连接redis服务器的host
	 */
	private String host;

	/**
	 * 连接redis服务器的port
	 */
	private int port;

	/**
	 * 连接redis服务器的密码
	 */
	private String password;

	private int timeout = 2000;

	private int database = 0;

	/**
	 * 数据所在目录
	 */
	private String dataPaths;

	/**
	 * 为各列数据定义别名（请用英文逗号分隔,且中间不允许有空格）
	 */
	private String columns;

	/**
	 * 分隔符
	 **/
	private String columnSplit = "\001";

	/**
	 * hive 的null 值特殊转义字符
	 */
	private String nullValue = "\\N";

	/**
	 * 执行命令
	 */
	private String script;

	/**
	 * 批量导入的大小
	 */
	private int batchUpdateSize = 3000;

	public DefTaskToRedis() {
		this.setTaskType(TaskType.TOREDIS.name());
		this.setTaskPlugin("java -Xms512m -Xmx512m -cp $pluginDir/lighthouse-plugin-toRedis-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.huya.lighthouse.plugin.toRedis.ToRedisMain");
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDataPaths() {
		return dataPaths;
	}

	public void setDataPaths(String dataPaths) {
		this.dataPaths = dataPaths;
	}

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
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

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getDatabase() {
		return database;
	}

	public void setDatabase(int database) {
		this.database = database;
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
		AssertUtils.assertTrue(StringUtils.isNotBlank(host), "host cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(columns), "columns cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(columnSplit), "columnSplit cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(nullValue), "nullValue cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(script), "script cannot be blank!");
	}

	@Override
	public void doTrim() {
		super.doTrim();
		this.host = StringUtils.trim(this.host);
		this.dataPaths = StringUtils.trim(this.dataPaths);
		this.columns = StringUtils.trim(this.columns);
		this.nullValue = StringUtils.trim(this.nullValue);
		this.script = StringUtils.trim(this.script);
	}
}
