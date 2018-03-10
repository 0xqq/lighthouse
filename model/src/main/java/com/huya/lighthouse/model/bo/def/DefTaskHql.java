package com.huya.lighthouse.model.bo.def;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.huya.lighthouse.model.type.TaskType;
import com.huya.lighthouse.util.AssertUtils;

/**
 * 
 *
 */
public class DefTaskHql extends AbstractBODefTask {

	private static final long serialVersionUID = -6664233575375570519L;

	private String sql;
	
	private String hiveCmd;
	
	private String sparkCmd;
	
	private int sparkTimeOutSec;
	
	private String hadoopQueue;
	
	public DefTaskHql() {
		this.setTaskType(TaskType.HQL.name());
		this.setTaskPlugin("java -Xms128m -Xmx128m -cp $pluginDir/lighthouse-plugin-hql-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.huya.lighthouse.plugin.hql.HqlMain");
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getHiveCmd() {
		return hiveCmd;
	}

	public void setHiveCmd(String hiveCmd) {
		this.hiveCmd = hiveCmd;
	}

	public String getSparkCmd() {
		return sparkCmd;
	}

	public void setSparkCmd(String sparkCmd) {
		this.sparkCmd = sparkCmd;
	}

	public int getSparkTimeOutSec() {
		return sparkTimeOutSec;
	}

	public void setSparkTimeOutSec(int sparkTimeOutSec) {
		this.sparkTimeOutSec = sparkTimeOutSec;
	}

	public String getHadoopQueue() {
		return hadoopQueue;
	}

	public void setHadoopQueue(String hadoopQueue) {
		this.hadoopQueue = hadoopQueue;
	}

	@Override
	public void doAssert() throws Exception {
		super.doAssert();
		AssertUtils.assertTrue(StringUtils.isNotBlank(this.sql), "sql cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(this.hiveCmd) || StringUtils.isNotBlank(this.sparkCmd), "hiveCmd and sparkCmd cannot together be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(this.hadoopQueue), "hadoopQueue cannot be blank!");
		
		String localDataRoot = "${local_data_root}";
		String hdfsDataRoot = "/tmp/lighthouse/analyse-result/";
		Pattern p = Pattern.compile("(?i)INSERT\\s+OVERWRITE\\s+(LOCAL\\s+)?DIRECTORY\\s+(\'|\")([^;|\'|\"]*)(\'|\")");
		Matcher m = p.matcher(this.sql);
		while(m.find()) {
			if ( StringUtils.equalsIgnoreCase("LOCAL", StringUtils.trim(m.group(1))) ) {
				AssertUtils.assertTrue(StringUtils.startsWith(m.group(3), localDataRoot), "当前数据目录为: " + m.group(3) + ", 本地数据目录开头必须为: " + localDataRoot);
			} 
			else {
				AssertUtils.assertTrue(StringUtils.startsWith(m.group(3), hdfsDataRoot), "当前数据目录为: " + m.group(3) + ", hdfs数据目录开头必须为: " + hdfsDataRoot);
			}
		}
	}

	@Override
	public void doTrim() {
		super.doTrim();
		this.sql = StringUtils.trim(this.sql);
		this.hiveCmd = StringUtils.trim(this.hiveCmd);
		this.sparkCmd = StringUtils.trim(this.sparkCmd);
		this.hadoopQueue = StringUtils.trim(this.hadoopQueue);
	}
}
