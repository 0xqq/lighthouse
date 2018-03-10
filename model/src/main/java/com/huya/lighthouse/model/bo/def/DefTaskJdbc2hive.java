package com.huya.lighthouse.model.bo.def;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang.StringUtils;

import com.huya.lighthouse.model.type.TaskType;
import com.huya.lighthouse.util.AssertUtils;

/**
 * 
 *
 */
public class DefTaskJdbc2hive extends AbstractBODefTask {

	private static final long serialVersionUID = -7215848289912890356L;

	List<FromJdbcSep> fromJdbcSepList = new ArrayList<FromJdbcSep>();

	/**
	 * 数据落地至库名。hive表名
	 */
	private String hiveDBDotTable;

	private String partition;

	private String hiveCmd;

	private String hadoopQueue;
	
	private int sumFileRow = 0;

	public List<FromJdbcSep> getFromJdbcSepList() {
		return fromJdbcSepList;
	}

	public void setFromJdbcSepList(List<FromJdbcSep> fromJdbcSepList) {
		this.fromJdbcSepList = fromJdbcSepList;
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

	public int getSumFileRow() {
		return sumFileRow;
	}

	public void setSumFileRow(int sumFileRow) {
		this.sumFileRow = sumFileRow;
	}

	public DefTaskJdbc2hive() {
		this.setTaskType(TaskType.JDBC2HIVE.name());
		this.setTaskPlugin("java -Xms128m -Xmx128m -cp $pluginDir/lighthouse-plugin-jdbc2hive-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.huya.lighthouse.plugin.jdbc2hive.Jdbc2hiveMain");
	}

	@Override
	public void doAssert() throws Exception {
		super.doAssert();
		AssertUtils.assertTrue(fromJdbcSepList != null && fromJdbcSepList.size() > 0, "fromJdbcSepList cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(hiveDBDotTable), "fromJdbcSepList cannot be blank!");
		for (FromJdbcSep fromJdbcSep : fromJdbcSepList) {
			fromJdbcSep.doAssert();
		}
	}

	@Override
	public void doTrim() {
		super.doTrim();
		this.hiveDBDotTable = StringUtils.trim(this.hiveDBDotTable);
		this.partition = StringUtils.trim(this.partition);
		if (fromJdbcSepList == null) {
			return;
		}
		for (FromJdbcSep fromJdbcSep : fromJdbcSepList) {
			fromJdbcSep.doTrim();
		}
	}

	@Override
	public void registerConverter() {
		super.registerConverter();
		ConvertUtils.register(new Converter() {
			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(Class type, Object value) {
				@SuppressWarnings("unchecked")
				List<Map> mapList = (List<Map>) value;
				List<FromJdbcSep> fromJdbcSepList = new ArrayList<FromJdbcSep>();
				for (Map map : mapList) {
					FromJdbcSep fromJdbcSep = new FromJdbcSep();
					fromJdbcSepList.add(fromJdbcSep);
					try {
						BeanUtils.populate(fromJdbcSep, map);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return fromJdbcSepList;
			}
		}, List.class);
	}
}
