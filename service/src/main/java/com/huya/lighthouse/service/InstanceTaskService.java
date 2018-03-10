package com.huya.lighthouse.service;

import java.util.Date;
import java.util.List;

import com.huya.lighthouse.model.bo.misc.InstanceStatus;
import com.huya.lighthouse.model.bo.misc.StatusCnt;
import com.huya.lighthouse.model.bo.misc.StatusCntCatalog;
import com.huya.lighthouse.model.bo.misc.StatusCntCatalogDay;
import com.huya.lighthouse.model.po.instance.InstanceTask;
import com.huya.lighthouse.model.po.instance.InstanceTaskDepend;
import com.huya.lighthouse.model.query.InstanceTaskQuery;
import com.huya.lighthouse.model.query.misc.StatusCntCatalogDayQuery;
import com.huya.lighthouse.model.query.misc.StatusCntQuery;
import com.huya.lighthouse.model.query.page.Page;

/**
 * [InstanceTask] 的业务操作
 * 
 */
public interface InstanceTaskService {

	/**
	 * 创建InstanceTask
	 **/
	public InstanceTask create(InstanceTask instanceTask);

	/**
	 * 更新InstanceTask
	 **/
	public InstanceTask update(InstanceTask instanceTask);

	/**
	 * 删除InstanceTask
	 **/
	public void removeById(int taskId, Date taskDate, String instanceId);

	/**
	 * 根据ID得到InstanceTask
	 **/
	public InstanceTask getById(Integer taskId, Date taskDate, String instanceId);

	public InstanceTask getValidInstance(Integer taskId, Date taskDate);

	public boolean isSuccessPreInstance(Integer taskId, Date taskDate);

	public void create(InstanceTask instanceTask, List<InstanceTaskDepend> instanceTaskDependList);

	public InstanceTask getUnDoneTask(Integer taskId, Date taskDate);

	public List<InstanceTask> getValidInitInstance(Integer taskId);

	public InstanceTask getSameAgentRun(Integer taskId, Date taskDate);

	public int[] batchInsert(List<InstanceTask> instanceTaskList);

	public InstanceTask getLastValidInstance(Integer taskId);

	public List<InstanceTask> getAllValidUnDoneInstance();

	public void updateInvalid(Integer taskId, Date taskDate);

	public List<String> getPreContexts(Integer taskId, Date taskDate, String instanceId);

	/**
	 * 分页查询: InstanceTask
	 **/  
	public Page<InstanceTask> findPage(InstanceTaskQuery query);
	
	public StatusCnt getCurStatusCountAll(List<Integer> catalogIds);
	
	public Page<InstanceTask> pageQueryCurInstances(StatusCntQuery query);

	public List<StatusCntCatalog> getCurStatusCountCatalog(List<Integer> catalogIds);

	public Page<StatusCntCatalogDay> pageQueryStatusCountCatalogDay(StatusCntCatalogDayQuery query);

	public List<InstanceStatus> getInstanceStatuss(int taskId, Date startTaskDate, Date endTaskDate);

	public List<InstanceTask> getInstancesById(int taskId, Date taskDate);

	public String getLastStatus(Integer taskId, Date taskDate);

	public List<InstanceTask> getByDependPreId(Integer taskId, Date taskDate);

	public List<InstanceTask> getByDependPostId(Integer taskId, Date taskDate);
}
