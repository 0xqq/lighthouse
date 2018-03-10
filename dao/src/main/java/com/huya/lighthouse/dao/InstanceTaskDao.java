package com.huya.lighthouse.dao;

import java.util.Date;
import java.util.List;

import com.huya.lighthouse.model.bo.misc.InstanceStatus;
import com.huya.lighthouse.model.bo.misc.StatusCnt;
import com.huya.lighthouse.model.bo.misc.StatusCntCatalog;
import com.huya.lighthouse.model.bo.misc.StatusCntCatalogDay;
import com.huya.lighthouse.model.po.instance.InstanceTask;
import com.huya.lighthouse.model.query.InstanceTaskQuery;
import com.huya.lighthouse.model.query.misc.StatusCntCatalogDayQuery;
import com.huya.lighthouse.model.query.misc.StatusCntQuery;
import com.huya.lighthouse.model.query.page.Page;

/**
 * tableName: instance_task [InstanceTask] 的Dao操作
 * 
 */
public interface InstanceTaskDao {

	public void insert(InstanceTask entity);

	public int[] batchInsert(List<InstanceTask> entityList);

	public int update(InstanceTask entity);

	public int deleteById(int taskId, Date taskDate, String instanceId);

	public InstanceTask getById(Integer taskId, Date taskDate, String instanceId);

	public InstanceTask getValidInstance(Integer taskId, Date taskDate);

	public boolean isSuccessPreInstance(Integer taskId, Date taskDate);

	public int updateInvalid(Integer taskId, Date taskDate);

	public InstanceTask getUnDoneTask(Integer taskId, Date taskDate);

	public List<InstanceTask> getValidInitInstance(Integer taskId);

	public InstanceTask getSameAgentRun(Integer taskId, Date taskDate);

	public InstanceTask getLastValidInstance(Integer taskId);

	public List<InstanceTask> getAllValidUnDoneInstance();

	public List<String> getPreContexts(Integer taskId, Date taskDate, String instanceId);

	public Page<InstanceTask> findPage(InstanceTaskQuery query);

	public StatusCnt getCurStatusCountAll(List<Integer> catalogIds);
	
	public Page<InstanceTask> pageQueryCurInstances(StatusCntQuery query);

	public List<StatusCntCatalog> getCurStatusCountCatalog(List<Integer> catalogIds);

	public Page<StatusCntCatalogDay> pageQueryStatusCountCatalogDay(StatusCntCatalogDayQuery query);

	public List<InstanceStatus> getInstanceStatuss(int taskId, Date startTaskDate, Date endTaskDate);

	public List<InstanceTask> getInstancesById(int taskId, Date taskDate);

	public List<InstanceTask> getByDependPreId(Integer taskId, Date taskDate);

	public List<InstanceTask> getByDependPostId(Integer taskId, Date taskDate);

}
