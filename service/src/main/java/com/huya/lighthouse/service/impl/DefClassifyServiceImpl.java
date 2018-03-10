package com.huya.lighthouse.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huya.lighthouse.dao.DefClassifyDao;
import com.huya.lighthouse.model.po.def.DefClassify;
import com.huya.lighthouse.service.DefClassifyService;

/**
 * [DefClassify] 的业务操作实现类
 * 
 */
@Service("defClassifyService")
@Transactional(rollbackFor = Throwable.class)
public class DefClassifyServiceImpl implements DefClassifyService {

	protected static final Logger log = LoggerFactory.getLogger(DefClassifyServiceImpl.class);

	@Autowired
	private DefClassifyDao defClassifyDao;

	/**
	 * 创建DefClassify
	 **/
	public DefClassify create(DefClassify defClassify) {
		Assert.notNull(defClassify, "'defClassify' must be not null");
		defClassifyDao.insert(defClassify);
		return defClassify;
	}

	/**
	 * 更新DefClassify
	 **/
	public DefClassify update(DefClassify defClassify) {
		Assert.notNull(defClassify, "'defClassify' must be not null");
		defClassifyDao.update(defClassify);
		return defClassify;
	}

	/**
	 * 删除DefClassify
	 **/
	public void removeById(String classifyType, String classifyCode, int taskId) {
		defClassifyDao.deleteById(classifyType, classifyCode, taskId);
	}

	/**
	 * 根据ID得到DefClassify
	 **/
	public DefClassify getById(String classifyType, String classifyCode, int taskId) {
		return defClassifyDao.getById(classifyType, classifyCode, taskId);
	}

	@Override
	public void batchCreate(List<DefClassify> defClassifyList) {
		defClassifyDao.batchInsert(defClassifyList);
	}

	@Override
	public void removeByTaskId(int taskId) {
		defClassifyDao.deleteByTaskId(taskId);
	}

	@Override
	public List<DefClassify> getByTaskId(int taskId, int isValid) {
		return defClassifyDao.getByTaskId(taskId, isValid);
	}

	@Override
	public void updateIsValidById(int taskId, int isValid) {
		defClassifyDao.updateIsValidById(taskId, isValid);
	}
}
