package com.huya.lighthouse.dao;

import java.util.Date;
import java.util.List;

import com.huya.lighthouse.model.po.instance.InstanceTaskDepend;

/**
 * tableName: instance_task_depend [InstanceTaskDepend] 的Dao操作
 * 
 */
public interface InstanceTaskDependDao {

	public void insert(InstanceTaskDepend entity);

	public int update(InstanceTaskDepend entity);

	public int deleteById(int taskId, Date taskDate, String instanceId, int preTaskId, Date preTaskDate);

	public InstanceTaskDepend getById(int taskId, Date taskDate, String instanceId, int preTaskId, Date preTaskDate);

	public int updateInvalid(Integer taskId, Date taskDate);

	public int[] batchInsert(List<InstanceTaskDepend> instanceTaskDependList);

	public List<InstanceTaskDepend> getByPostId(int taskId, Date taskDate, String instanceId);

	public List<InstanceTaskDepend> getByPreId(int preTaskId, Date preTaskDate, String instanceId);

}
