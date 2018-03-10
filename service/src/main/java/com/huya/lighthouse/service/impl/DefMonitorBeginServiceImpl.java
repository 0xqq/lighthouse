package com.huya.lighthouse.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huya.lighthouse.dao.DefMonitorBeginDao;
import com.huya.lighthouse.model.po.def.DefMonitorBegin;
import com.huya.lighthouse.service.DefMonitorBeginService;

/**
 * [DefMonitorBegin] 的业务操作实现类
 * 
 */
@Service("defMonitorBeginService")
@Transactional(rollbackFor = Throwable.class)
public class DefMonitorBeginServiceImpl implements DefMonitorBeginService {

	protected static final Logger log = LoggerFactory.getLogger(DefMonitorBeginServiceImpl.class);

	@Autowired
	private DefMonitorBeginDao defMonitorBeginDao;


	/**
	 * 创建DefMonitorBegin
	 **/
	public DefMonitorBegin create(DefMonitorBegin defMonitorBegin) {
		Assert.notNull(defMonitorBegin, "'defMonitorBegin' must be not null");
		defMonitorBeginDao.insert(defMonitorBegin);
		return defMonitorBegin;
	}

	/**
	 * 更新DefMonitorBegin
	 **/
	public DefMonitorBegin update(DefMonitorBegin defMonitorBegin) {
		Assert.notNull(defMonitorBegin, "'defMonitorBegin' must be not null");
		defMonitorBeginDao.update(defMonitorBegin);
		return defMonitorBegin;
	}

	/**
	 * 删除DefMonitorBegin
	 **/
	public void removeById(String cronExp, int taskId) {
		defMonitorBeginDao.deleteById(cronExp, taskId);
	}

	/**
	 * 根据ID得到DefMonitorBegin
	 **/
	public DefMonitorBegin getById(String cronExp, int taskId) {
		return defMonitorBeginDao.getById(cronExp, taskId);
	}

	@Override
	public List<String> getAllValidCronExp() {
		return defMonitorBeginDao.getAllValidCronExp();
	}

	@Override
	public void batchCreate(List<DefMonitorBegin> defMonitorBeginList) {
		defMonitorBeginDao.batchInsert(defMonitorBeginList);
	}

	@Override
	public void removeByTaskId(Integer taskId) {
		defMonitorBeginDao.deleteById(taskId);
	}

	@Override
	public List<DefMonitorBegin> getByTaskId(int taskId, int isValid) {
		return defMonitorBeginDao.getByTaskId(taskId, isValid);
	}

	@Override
	public void updateIsValidById(int taskId, int isValid) {
		defMonitorBeginDao.updateIsValidById(taskId, isValid);
	}

	@Override
	public List<DefMonitorBegin> getByCronExp(String cronExp) {
		return defMonitorBeginDao.getByCronExp(cronExp);
	}

	
}
