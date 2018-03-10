package com.huya.lighthouse.dao;

import java.util.List;

import com.huya.lighthouse.model.po.def.DefMonitorDur;

/**
 * tableName: def_monitor_dur [DefMonitorDur] 的Dao操作
 * 
 */
public interface DefMonitorDurDao {

	public void insert(DefMonitorDur entity);

	public int update(DefMonitorDur entity);

	public int deleteById(int taskId, int durSec);

	public DefMonitorDur getById(int taskId, int durSec);

	public List<DefMonitorDur> getByTaskId(Integer taskId, int isValid);

	public int[] batchInsert(List<DefMonitorDur> defMonitorDurList);

	public int deleteById(Integer taskId);

	public void updateIsValidById(int taskId, int isValid);

}
