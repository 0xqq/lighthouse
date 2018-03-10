package com.huya.lighthouse.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huya.lighthouse.dao.InstanceTaskDao;
import com.huya.lighthouse.dao.InstanceTaskDependDao;
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
import com.huya.lighthouse.service.InstanceTaskService;

/**
 * [InstanceTask] 的业务操作实现类
 * 
 */
@Service("instanceTaskService")
@Transactional(rollbackFor = Throwable.class)
public class InstanceTaskServiceImpl implements InstanceTaskService {

	protected static final Logger log = LoggerFactory.getLogger(InstanceTaskServiceImpl.class);

	@Autowired
	private InstanceTaskDao instanceTaskDao;

	@Autowired
	private InstanceTaskDependDao instanceTaskDependDao;

	/**
	 * 创建InstanceTask
	 **/
	public InstanceTask create(InstanceTask instanceTask) {
		Assert.notNull(instanceTask, "'instanceTask' must be not null");
		instanceTaskDao.insert(instanceTask);
		return instanceTask;
	}

	/**
	 * 更新InstanceTask
	 **/
	public InstanceTask update(InstanceTask instanceTask) {
		Assert.notNull(instanceTask, "'instanceTask' must be not null");
		instanceTaskDao.update(instanceTask);
		return instanceTask;
	}

	/**
	 * 删除InstanceTask
	 **/
	public void removeById(int taskId, Date taskDate, String instanceId) {
		instanceTaskDao.deleteById(taskId, taskDate, instanceId);
	}

	/**
	 * 根据ID得到InstanceTask
	 **/
	public InstanceTask getById(Integer taskId, Date taskDate, String instanceId) {
		return instanceTaskDao.getById(taskId, taskDate, instanceId);
	}

	@Override
	public InstanceTask getValidInstance(Integer taskId, Date taskDate) {
		return instanceTaskDao.getValidInstance(taskId, taskDate);
	}

	@Override
	public boolean isSuccessPreInstance(Integer taskId, Date taskDate) {
		return instanceTaskDao.isSuccessPreInstance(taskId, taskDate);
	}

	@Override
	public void create(InstanceTask instanceTask, List<InstanceTaskDepend> instanceTaskDependList) {
		instanceTaskDao.updateInvalid(instanceTask.getTaskId(), instanceTask.getTaskDate());
		instanceTaskDao.insert(instanceTask);
		instanceTaskDependDao.updateInvalid(instanceTask.getTaskId(), instanceTask.getTaskDate());
		if (instanceTaskDependList != null && instanceTaskDependList.size() > 0) {
			instanceTaskDependDao.batchInsert(instanceTaskDependList);
		}
	}

	@Override
	public InstanceTask getUnDoneTask(Integer taskId, Date taskDate) {
		return instanceTaskDao.getUnDoneTask(taskId, taskDate);
	}

	@Override
	public List<InstanceTask> getValidInitInstance(Integer taskId) {
		return instanceTaskDao.getValidInitInstance(taskId);
	}

	@Override
	public InstanceTask getSameAgentRun(Integer taskId, Date taskDate) {
		return instanceTaskDao.getSameAgentRun(taskId, taskDate);
	}

	@Override
	public int[] batchInsert(List<InstanceTask> instanceTaskList) {
		return instanceTaskDao.batchInsert(instanceTaskList);
	}

	@Override
	public InstanceTask getLastValidInstance(Integer taskId) {
		return instanceTaskDao.getLastValidInstance(taskId);
	}

	@Override
	public List<InstanceTask> getAllValidUnDoneInstance() {
		return instanceTaskDao.getAllValidUnDoneInstance();
	}

	@Override
	public void updateInvalid(Integer taskId, Date taskDate) {
		instanceTaskDao.updateInvalid(taskId, taskDate);
		instanceTaskDependDao.updateInvalid(taskId, taskDate);
	}

	@Override
	public List<String> getPreContexts(Integer taskId, Date taskDate, String instanceId) {
		return instanceTaskDao.getPreContexts(taskId, taskDate, instanceId);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<InstanceTask> findPage(InstanceTaskQuery query) {
		return instanceTaskDao.findPage(query);
	}

	@Override
	public StatusCnt getCurStatusCountAll(List<Integer> catalogIds) {
		return instanceTaskDao.getCurStatusCountAll(catalogIds);
	}

	@Override
	public Page<InstanceTask> pageQueryCurInstances(StatusCntQuery query) {
		return instanceTaskDao.pageQueryCurInstances(query);
	}

	@Override
	public List<StatusCntCatalog> getCurStatusCountCatalog(List<Integer> catalogIds) {
		return instanceTaskDao.getCurStatusCountCatalog(catalogIds);
	}

	@Override
	public Page<StatusCntCatalogDay> pageQueryStatusCountCatalogDay(StatusCntCatalogDayQuery query) {
		return instanceTaskDao.pageQueryStatusCountCatalogDay(query);
	}

	@Override
	public List<InstanceStatus> getInstanceStatuss(int taskId, Date startTaskDate, Date endTaskDate) {
		return instanceTaskDao.getInstanceStatuss(taskId, startTaskDate, endTaskDate);
	}

	@Override
	public List<InstanceTask> getInstancesById(int taskId, Date taskDate) {
		return instanceTaskDao.getInstancesById(taskId, taskDate);
	}

	@Override
	public String getLastStatus(Integer taskId, Date taskDate) {
		InstanceTask instanceTask = instanceTaskDao.getValidInstance(taskId, taskDate);
		if (instanceTask == null) {
			return "";
		}
		return instanceTask.getStatus();
	}

	@Override
	public List<InstanceTask> getByDependPreId(Integer taskId, Date taskDate) {
		return instanceTaskDao.getByDependPreId(taskId, taskDate);
	}

	@Override
	public List<InstanceTask> getByDependPostId(Integer taskId, Date taskDate) {
		return instanceTaskDao.getByDependPostId(taskId, taskDate);
	}
}
