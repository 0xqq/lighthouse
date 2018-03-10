package com.huya.lighthouse.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huya.lighthouse.dao.DefTaskDependDao;
import com.huya.lighthouse.model.po.def.DefTaskDepend;
import com.huya.lighthouse.service.DefTaskDependService;

/**
 * [DefTaskDepend] 的业务操作实现类
 * 
 */
@Service("defTaskDependService")
@Transactional(rollbackFor = Throwable.class)
public class DefTaskDependServiceImpl implements DefTaskDependService {

	protected static final Logger log = LoggerFactory.getLogger(DefTaskDependServiceImpl.class);

	@Autowired
	private DefTaskDependDao defTaskDependDao;

	/**
	 * 创建DefTaskDepend
	 **/
	public DefTaskDepend create(DefTaskDepend defTaskDepend) {
		Assert.notNull(defTaskDepend, "'defTaskDepend' must be not null");
		defTaskDependDao.insert(defTaskDepend);
		return defTaskDepend;
	}

	/**
	 * 更新DefTaskDepend
	 **/
	public DefTaskDepend update(DefTaskDepend defTaskDepend) {
		Assert.notNull(defTaskDepend, "'defTaskDepend' must be not null");
		defTaskDependDao.update(defTaskDepend);
		return defTaskDepend;
	}

	/**
	 * 删除DefTaskDepend
	 **/
	public void removeById(int taskId, int preTaskId) {
		defTaskDependDao.deleteById(taskId, preTaskId);
	}

	/**
	 * 根据ID得到DefTaskDepend
	 **/
	public DefTaskDepend getById(int taskId, int preTaskId) {
		return defTaskDependDao.getById(taskId, preTaskId);
	}

	@Override
	public List<DefTaskDepend> getByPreId(Integer preTaskId, int isValid) {
		return defTaskDependDao.getByPreId(preTaskId, isValid);
	}

	@Override
	public List<DefTaskDepend> getByPostId(Integer taskId, int isValid) {
		return defTaskDependDao.getByPostId(taskId, isValid);
	}

	@Override
	public void batchCreate(List<DefTaskDepend> defTaskDependList) {
		defTaskDependDao.batchInsert(defTaskDependList);
	}

	@Override
	public void removeByTaskId(Integer taskId) {
		defTaskDependDao.deleteById(taskId);
	}

	@Override
	public void updateIsValidById(int taskId, int isValid) {
		defTaskDependDao.updateIsValidById(taskId, isValid);
	}
}
