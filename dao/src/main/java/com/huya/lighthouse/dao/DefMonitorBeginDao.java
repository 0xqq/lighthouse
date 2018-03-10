package com.huya.lighthouse.dao;

import java.util.List;

import com.huya.lighthouse.model.po.def.DefMonitorBegin;

/**
 * tableName: def_monitor_begin [DefMonitorBegin] 的Dao操作
 * 
 */
public interface DefMonitorBeginDao {

	public void insert(DefMonitorBegin entity);

	public int update(DefMonitorBegin entity);

	public int deleteById(String cronExp, int taskId);

	public DefMonitorBegin getById(String cronExp, int taskId);

	public List<String> getAllValidCronExp();

	public int[] batchInsert(List<DefMonitorBegin> defMonitorBeginList);

	public int deleteById(Integer taskId);

	public List<DefMonitorBegin> getByTaskId(int taskId, int isValid);

	public void updateIsValidById(int taskId, int isValid);

	public List<DefMonitorBegin> getByCronExp(String cronExp);

}
