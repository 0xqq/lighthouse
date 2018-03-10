package com.huya.lighthouse.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huya.lighthouse.dao.DefMonitorDurDao;
import com.huya.lighthouse.model.po.def.DefMonitorDur;
import com.huya.lighthouse.service.DefMonitorDurService;

/**
 * [DefMonitorDur] 的业务操作实现类
 * 
 */
@Service("defMonitorDurService")
@Transactional(rollbackFor = Throwable.class)
public class DefMonitorDurServiceImpl implements DefMonitorDurService {

	protected static final Logger log = LoggerFactory.getLogger(DefMonitorDurServiceImpl.class);

	@Autowired
	private DefMonitorDurDao defMonitorDurDao;


	/**
	 * 创建DefMonitorDur
	 **/
	public DefMonitorDur create(DefMonitorDur defMonitorDur) {
		Assert.notNull(defMonitorDur, "'defMonitorDur' must be not null");
		defMonitorDurDao.insert(defMonitorDur);
		return defMonitorDur;
	}

	/**
	 * 更新DefMonitorDur
	 **/
	public DefMonitorDur update(DefMonitorDur defMonitorDur) {
		Assert.notNull(defMonitorDur, "'defMonitorDur' must be not null");
		defMonitorDurDao.update(defMonitorDur);
		return defMonitorDur;
	}

	/**
	 * 删除DefMonitorDur
	 **/
	public void removeById(int taskId, int durSec) {
		defMonitorDurDao.deleteById(taskId, durSec);
	}

	/**
	 * 根据ID得到DefMonitorDur
	 **/
	public DefMonitorDur getById(int taskId, int durSec) {
		return defMonitorDurDao.getById(taskId, durSec);
	}

	@Override
	public List<DefMonitorDur> getByTaskId(Integer taskId, int isValid) {
		return defMonitorDurDao.getByTaskId(taskId, isValid);
	}

	@Override
	public void batchCreate(List<DefMonitorDur> defMonitorDurList) {
		defMonitorDurDao.batchInsert(defMonitorDurList);
	}

	@Override
	public void removeByTaskId(Integer taskId) {
		defMonitorDurDao.deleteById(taskId);
	}

	@Override
	public void updateIsValidById(int taskId, int isValid) {
		defMonitorDurDao.updateIsValidById(taskId, isValid);
	}
}
