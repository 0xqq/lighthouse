package com.huya.lighthouse.model.bo.def;

import org.apache.commons.lang.StringUtils;

import com.huya.lighthouse.model.type.TaskType;
import com.huya.lighthouse.util.AssertUtils;
import com.huya.lighthouse.util.FileSizeUtils;

/**
 * 
 *
 */
public class DefTaskDataPathCheck extends AbstractBODefTask {

	private static final long serialVersionUID = -74636414949469006L;

	private String dataPath;

	private int fileCount = 0;

	/**
	 * 检查成功判断条件：文件总大小，大于等于 example : 20KB or 20MB or 20GB
	 */
	private String sumFileSize;

	private int sumFileRow = 0;

	public DefTaskDataPathCheck() {
		init();
	}

	private void init() {
		this.setTaskType(TaskType.DATAPATHCHECK.name());
		this.setTaskPlugin("java -Xms128m -Xmx128m -cp $pluginDir/lighthouse-plugin-dataPathCheck-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.huya.lighthouse.plugin.dataPathCheck.DataPathCheckMain");
	}
	
	public DefTaskDataPathCheck(String dataPath, int fileCount, String sumFileSize, int sumFileRow) {
		super();
		init();
		this.dataPath = dataPath;
		this.fileCount = fileCount;
		this.sumFileSize = sumFileSize;
		this.sumFileRow = sumFileRow;
	}

	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
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
	
	public long getLongSumFileSize() {
		return FileSizeUtils.parseHumanReadableFileSize(sumFileSize);
	}

	public int getSumFileRow() {
		return sumFileRow;
	}

	public void setSumFileRow(int sumFileRow) {
		this.sumFileRow = sumFileRow;
	}

	@Override
	public void doAssert() throws Exception {
		super.doAssert();
		AssertUtils.assertTrue(StringUtils.isNotBlank(this.dataPath), "dataPath cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(this.sumFileSize), "sumFileSize cannot be blank!");
		getLongSumFileSize();
	}

	@Override
	public void doTrim() {
		super.doTrim();
		this.dataPath = StringUtils.trim(this.dataPath);
		this.sumFileSize = StringUtils.trim(this.sumFileSize);
	}
}
