package com.huya.lighthouse.dao;

import java.util.List;

import com.huya.lighthouse.model.po.def.DefMonitorRetry;

/**
 * tableName: def_monitor_retry [DefMonitorRetry] 的Dao操作
 * 
 */
public interface DefMonitorRetryDao {

	public void insert(DefMonitorRetry entity);

	public int update(DefMonitorRetry entity);

	public int deleteById(int taskId, int failRetryN);

	public DefMonitorRetry getById(int taskId, int failRetryN);

	public List<DefMonitorRetry> getByTaskId(Integer taskId, int isValid);

	public int[] batchInsert(List<DefMonitorRetry> defMonitorRetryList);

	public int deleteById(Integer taskId);

	public void updateIsValidById(int taskId, int isValid);

}
