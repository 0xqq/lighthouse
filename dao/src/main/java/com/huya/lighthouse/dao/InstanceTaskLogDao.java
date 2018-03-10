package com.huya.lighthouse.dao;

import java.util.Date;
import java.util.List;

import com.huya.lighthouse.model.po.instance.InstanceTaskLog;

/**
 * tableName: instance_task_log
*/
public interface InstanceTaskLogDao {
	
	public void insert(InstanceTaskLog entity);
	
	public int update(InstanceTaskLog entity);

	public int deleteById(int taskId, Date taskDate, String instanceId, long logId);
	
	public InstanceTaskLog getById(int taskId, Date taskDate, String instanceId, long logId);
	
	public List<InstanceTaskLog> getInstanceTaskLogs(int taskId, Date taskDate, String instanceId);
	
}
