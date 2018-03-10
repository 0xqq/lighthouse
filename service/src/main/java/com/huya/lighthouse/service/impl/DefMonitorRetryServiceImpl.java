package com.huya.lighthouse.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huya.lighthouse.dao.DefMonitorRetryDao;
import com.huya.lighthouse.model.po.def.DefMonitorRetry;
import com.huya.lighthouse.service.DefMonitorRetryService;

/**
 * [DefMonitorRetry] 的业务操作实现类
 * 
 */
@Service("defMonitorRetryService")
@Transactional(rollbackFor = Throwable.class)
public class DefMonitorRetryServiceImpl implements DefMonitorRetryService {

	protected static final Logger log = LoggerFactory.getLogger(DefMonitorRetryServiceImpl.class);

	@Autowired
	private DefMonitorRetryDao defMonitorRetryDao;

	/**
	 * 创建DefMonitorRetry
	 **/
	public DefMonitorRetry create(DefMonitorRetry defMonitorRetry) {
		Assert.notNull(defMonitorRetry, "'defMonitorRetry' must be not null");
		defMonitorRetryDao.insert(defMonitorRetry);
		return defMonitorRetry;
	}

	/**
	 * 更新DefMonitorRetry
	 **/
	public DefMonitorRetry update(DefMonitorRetry defMonitorRetry) {
		Assert.notNull(defMonitorRetry, "'defMonitorRetry' must be not null");
		defMonitorRetryDao.update(defMonitorRetry);
		return defMonitorRetry;
	}

	/**
	 * 删除DefMonitorRetry
	 **/
	public void removeById(int taskId, int failRetryN) {
		defMonitorRetryDao.deleteById(taskId, failRetryN);
	}

	/**
	 * 根据ID得到DefMonitorRetry
	 **/
	public DefMonitorRetry getById(int taskId, int failRetryN) {
		return defMonitorRetryDao.getById(taskId, failRetryN);
	}

	@Override
	public List<DefMonitorRetry> getByTaskId(Integer taskId, int isValid) {
		return defMonitorRetryDao.getByTaskId(taskId, isValid);
	}

	@Override
	public void batchCreate(List<DefMonitorRetry> defMonitorRetryList) {
		defMonitorRetryDao.batchInsert(defMonitorRetryList);
	}

	@Override
	public void removeByTaskId(Integer taskId) {
		defMonitorRetryDao.deleteById(taskId);
	}

	@Override
	public void updateIsValidById(int taskId, int isValid) {
		defMonitorRetryDao.updateIsValidById(taskId, isValid);
	}

}
