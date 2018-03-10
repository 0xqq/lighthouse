package com.huya.lighthouse.model.bo.def;

import org.apache.commons.lang.StringUtils;

import com.huya.lighthouse.model.type.TaskType;
import com.huya.lighthouse.util.AssertUtils;

/**
 * 
 *
 */
public class DefTaskRsync2hive extends AbstractBODefTask {

	private static final long serialVersionUID = 2319160093668637965L;

	private String rsyncIp;

	private int rsyncPort = 873;

	private String rsyncUserName;

	private String rsyncPassword;

	private String rsyncModuleName;

	private String rsyncPathUnderModule;

	private String hiveDBDotTable;

	private String partition;

	private String hiveCmd;

	private String hadoopQueue;

	private int fileCount = 0;

	/**
	 * 检查成功判断条件：文件总大小，大于等于 example : 20KB or 20MB or 20GB
	 */
	private String sumFileSize;

	private int sumFileRow = 0;

	public String getRsyncIp() {
		return rsyncIp;
	}

	public void setRsyncIp(String rsyncIp) {
		this.rsyncIp = rsyncIp;
	}

	public int getRsyncPort() {
		return rsyncPort;
	}

	public void setRsyncPort(int rsyncPort) {
		this.rsyncPort = rsyncPort;
	}

	public String getRsyncUserName() {
		return rsyncUserName;
	}

	public void setRsyncUserName(String rsyncUserName) {
		this.rsyncUserName = rsyncUserName;
	}

	public String getRsyncPassword() {
		return rsyncPassword;
	}

	public void setRsyncPassword(String rsyncPassword) {
		this.rsyncPassword = rsyncPassword;
	}

	public String getRsyncModuleName() {
		return rsyncModuleName;
	}

	public void setRsyncModuleName(String rsyncModuleName) {
		this.rsyncModuleName = rsyncModuleName;
	}

	public String getRsyncPathUnderModule() {
		return rsyncPathUnderModule;
	}

	public void setRsyncPathUnderModule(String rsyncPathUnderModule) {
		this.rsyncPathUnderModule = rsyncPathUnderModule;
	}

	public String getHiveDBDotTable() {
		return hiveDBDotTable;
	}

	public void setHiveDBDotTable(String hiveDBDotTable) {
		this.hiveDBDotTable = hiveDBDotTable;
	}

	public String getPartition() {
		return partition;
	}

	public void setPartition(String partition) {
		this.partition = partition;
	}

	public String getHiveCmd() {
		return hiveCmd;
	}

	public void setHiveCmd(String hiveCmd) {
		this.hiveCmd = hiveCmd;
	}

	public String getHadoopQueue() {
		return hadoopQueue;
	}

	public void setHadoopQueue(String hadoopQueue) {
		this.hadoopQueue = hadoopQueue;
	}

	public int getFileCount() {
		return fileCount;
	}

	public void setFileCount(int fileCount) {
		this.fileCount = fileCount;
	}

	public String getSumFileSize() {
		return sumFileSize;
	}

	public void setSumFileSize(String sumFileSize) {
		this.sumFileSize = sumFileSize;
	}

	public int getSumFileRow() {
		return sumFileRow;
	}

	public void setSumFileRow(int sumFileRow) {
		this.sumFileRow = sumFileRow;
	}

	public DefTaskRsync2hive() {
		this.setTaskType(TaskType.RSYNC2HIVE.name());
		this.setTaskPlugin("java -Xms128m -Xmx128m -cp $pluginDir/lighthouse-plugin-rsync2hive-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.huya.lighthouse.plugin.rsync2hive.Rsync2hiveMain");
	}

	@Override
	public void doAssert() throws Exception {
		super.doAssert();
		AssertUtils.assertTrue(StringUtils.isNotBlank(rsyncIp), "rsyncIp cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(rsyncUserName), "rsyncUserName cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(rsyncPassword), "rsyncPassword cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(rsyncModuleName), "rsyncModuleName cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(hiveDBDotTable), "hiveDBDotTable cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(hiveCmd), "hiveCmd cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(hadoopQueue), "hadoopQueue cannot be blank!");

	}

	@Override
	public void doTrim() {
		super.doTrim();
		this.rsyncIp = StringUtils.trim(this.rsyncIp);
		this.rsyncUserName = StringUtils.trim(this.rsyncUserName);
		this.rsyncPassword = StringUtils.trim(this.rsyncPassword);
		this.rsyncModuleName = StringUtils.trim(this.rsyncModuleName);
		this.rsyncPathUnderModule = StringUtils.trim(this.rsyncPathUnderModule);
		this.hiveDBDotTable = StringUtils.trim(this.hiveDBDotTable);
		this.partition = StringUtils.trim(this.partition);
		this.hiveCmd = StringUtils.trim(this.hiveCmd);
		this.hadoopQueue = StringUtils.trim(this.hadoopQueue);
	}

}
