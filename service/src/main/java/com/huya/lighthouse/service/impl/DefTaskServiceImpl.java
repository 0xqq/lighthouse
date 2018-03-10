package com.huya.lighthouse.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huya.lighthouse.dao.DefTaskDao;
import com.huya.lighthouse.dao.DefTaskDependDao;
import com.huya.lighthouse.model.po.def.DefTask;
import com.huya.lighthouse.model.query.DefTaskQuery;
import com.huya.lighthouse.model.query.page.Page;
import com.huya.lighthouse.service.DefTaskService;

/**
 * [DefTask] 的业务操作实现类
 * 
 */
@Service("defTaskService")
@Transactional(rollbackFor = Throwable.class)
public class DefTaskServiceImpl implements DefTaskService {

	protected static final Logger log = LoggerFactory.getLogger(DefTaskServiceImpl.class);

	@Autowired
	private DefTaskDao defTaskDao;
	@Autowired
	private DefTaskDependDao defTaskDependDao;

	/**
	 * 创建DefTask
	 **/
	public DefTask create(DefTask defTask) {
		Assert.notNull(defTask, "'defTask' must be not null");
		defTask.setTaskId(null);
		defTaskDao.insert(defTask);
		return defTask;
	}

	/**
	 * 更新DefTask
	 **/
	public DefTask update(DefTask defTask) {
		Assert.notNull(defTask, "'defTask' must be not null");
		defTaskDao.update(defTask);
		return defTask;
	}

	/**
	 * 删除DefTask
	 **/
	public void removeById(int taskId) {
		defTaskDao.deleteById(taskId);
	}

	/**
	 * 根据ID得到DefTask
	 **/
	public DefTask getById(int taskId) {
		return defTaskDao.getById(taskId);
	}

	@Override
	public List<DefTask> getByDependPreId(Integer taskId, Integer isValid) {
		return defTaskDao.getByDependPreId(taskId, isValid);
	}

	@Override
	public List<DefTask> getByDependPostId(Integer taskId, Integer isValid) {
		return defTaskDao.getByDependPostId(taskId, isValid);
	}

	@Override
	public List<DefTask> getAllValidCronTask() {
		return defTaskDao.getAllValidCronTask();
	}

	@Override
	public List<DefTask> getForInitByCatalogId(Integer catalogId) {
		return defTaskDao.getForInitByCatalogId(catalogId);
	}

	@Override
	public void updateIsValidById(int taskId, int isValid) {
		defTaskDao.updateIsValidById(taskId, isValid);
		defTaskDependDao.updateIsValidById(taskId, isValid);
	}

	@Override
	public List<DefTask> getByMonitorCronExp(String cronExp) {
		return defTaskDao.getByMonitorCronExp(cronExp);
	}

	@Override
	public List<DefTask> getByOfflineTime(Date offlineTime) {
		return defTaskDao.getByOfflineTime(offlineTime);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<DefTask> findPage(DefTaskQuery query) {
		return defTaskDao.findPage(query);
	}

	@Override
	public List<DefTask> getOfflineValid() {
		return defTaskDao.getOfflineValid();
	}
}
