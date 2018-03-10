package com.huya.lighthouse.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huya.lighthouse.dao.InstanceTaskDependDao;
import com.huya.lighthouse.model.po.instance.InstanceTaskDepend;
import com.huya.lighthouse.service.InstanceTaskDependService;

/**
 * [InstanceTaskDepend] 的业务操作实现类
 * 
 */
@Service("instanceTaskDependService")
@Transactional(rollbackFor = Throwable.class)
public class InstanceTaskDependServiceImpl implements InstanceTaskDependService {

	protected static final Logger log = LoggerFactory.getLogger(InstanceTaskDependServiceImpl.class);

	@Autowired
	private InstanceTaskDependDao instanceTaskDependDao;

	/**
	 * 创建InstanceTaskDepend
	 **/
	public InstanceTaskDepend create(InstanceTaskDepend instanceTaskDepend) {
		Assert.notNull(instanceTaskDepend, "'instanceTaskDepend' must be not null");
		instanceTaskDependDao.insert(instanceTaskDepend);
		return instanceTaskDepend;
	}

	/**
	 * 更新InstanceTaskDepend
	 **/
	public InstanceTaskDepend update(InstanceTaskDepend instanceTaskDepend) {
		Assert.notNull(instanceTaskDepend, "'instanceTaskDepend' must be not null");
		instanceTaskDependDao.update(instanceTaskDepend);
		return instanceTaskDepend;
	}

	/**
	 * 删除InstanceTaskDepend
	 **/
	public void removeById(int taskId, Date taskDate, String instanceId, int preTaskId, Date preTaskDate) {
		instanceTaskDependDao.deleteById(taskId, taskDate, instanceId, preTaskId, preTaskDate);
	}

	/**
	 * 根据ID得到InstanceTaskDepend
	 **/
	public InstanceTaskDepend getById(int taskId, Date taskDate, String instanceId, int preTaskId, Date preTaskDate) {
		return instanceTaskDependDao.getById(taskId, taskDate, instanceId, preTaskId, preTaskDate);
	}

	@Override
	public List<InstanceTaskDepend> getByPostId(int taskId, Date taskDate, String instanceId) {
		return instanceTaskDependDao.getByPostId(taskId, taskDate, instanceId);
	}

	@Override
	public List<InstanceTaskDepend> getByPreId(int preTaskId, Date preTaskDate, String instanceId) {
		return instanceTaskDependDao.getByPreId(preTaskId, preTaskDate, instanceId);
	}
	
}
