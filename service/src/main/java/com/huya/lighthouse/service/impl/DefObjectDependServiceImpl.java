package com.huya.lighthouse.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huya.lighthouse.dao.DefObjectDependDao;
import com.huya.lighthouse.model.po.def.DefObjectDepend;
import com.huya.lighthouse.service.DefObjectDependService;

/**
 * [DefObjectDepend] 的业务操作实现类
 * 
 */
@Service("defObjectDependService")
@Transactional(rollbackFor = Throwable.class)
public class DefObjectDependServiceImpl implements DefObjectDependService {

	protected static final Logger log = LoggerFactory.getLogger(DefObjectDependServiceImpl.class);

	@Autowired
	private DefObjectDependDao defObjectDependDao;

	/**
	 * 创建DefObjectDepend
	 **/
	public DefObjectDepend create(DefObjectDepend defObjectDepend) {
		Assert.notNull(defObjectDepend, "'defObjectDepend' must be not null");
		defObjectDependDao.insert(defObjectDepend);
		return defObjectDepend;
	}

	/**
	 * 更新DefObjectDepend
	 **/
	public DefObjectDepend update(DefObjectDepend defObjectDepend) {
		Assert.notNull(defObjectDepend, "'defObjectDepend' must be not null");
		defObjectDependDao.update(defObjectDepend);
		return defObjectDepend;
	}

	/**
	 * 删除DefObjectDepend
	 **/
	public void removeById(int rwFlag, int taskId, String objectId) {
		defObjectDependDao.deleteById(rwFlag, taskId, objectId);
	}

	/**
	 * 根据ID得到DefObjectDepend
	 **/
	public DefObjectDepend getById(int rwFlag, int taskId, String objectId) {
		return defObjectDependDao.getById(rwFlag, taskId, objectId);
	}

	@Override
	public void batchCreate(List<DefObjectDepend> defObjectDependList) {
		defObjectDependDao.batchInsert(defObjectDependList);
	}

	@Override
	public void removeByTaskId(Integer taskId) {
		defObjectDependDao.deleteById(taskId);
	}

	@Override
	public List<DefObjectDepend> getByTaskId(int taskId, int isValid) {
		return defObjectDependDao.getByTaskId(taskId, isValid);
	}

	@Override
	public void updateIsValidById(int taskId, int isValid) {
		defObjectDependDao.updateIsValidById(taskId, isValid);
	}

}
