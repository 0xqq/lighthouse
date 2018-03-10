package com.huya.lighthouse.service;

import java.util.Date;
import java.util.List;

import com.huya.lighthouse.model.po.instance.InstanceTaskDepend;

/**
 * [InstanceTaskDepend] 的业务操作
 * 
 */
public interface InstanceTaskDependService {

	/**
	 * 创建InstanceTaskDepend
	 **/
	public InstanceTaskDepend create(InstanceTaskDepend instanceTaskDepend);

	/**
	 * 更新InstanceTaskDepend
	 **/
	public InstanceTaskDepend update(InstanceTaskDepend instanceTaskDepend);

	/**
	 * 删除InstanceTaskDepend
	 **/
	public void removeById(int taskId, Date taskDate, String instanceId, int preTaskId, Date preTaskDate);

	/**
	 * 根据ID得到InstanceTaskDepend
	 **/
	public InstanceTaskDepend getById(int taskId, Date taskDate, String instanceId, int preTaskId, Date preTaskDate);

	public List<InstanceTaskDepend> getByPostId(int taskId, Date taskDate, String instanceId);
	
	public List<InstanceTaskDepend> getByPreId(int preTaskId, Date preTaskDate, String instanceId);
}
