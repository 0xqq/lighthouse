package com.huya.lighthouse.service.impl;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huya.lighthouse.model.bo.instance.BOInstanceTask;
import com.huya.lighthouse.model.po.instance.InstanceTask;
import com.huya.lighthouse.model.po.instance.InstanceTaskDepend;
import com.huya.lighthouse.model.util.ModelConverters;
import com.huya.lighthouse.service.BOInstanceTaskService;
import com.huya.lighthouse.service.InstanceTaskDependService;
import com.huya.lighthouse.service.InstanceTaskLogService;
import com.huya.lighthouse.service.InstanceTaskService;

/**
 * [InstanceTask] 的业务操作实现类
 * 
 */
@Service("bOInstanceTaskService")
public class BOInstanceTaskServiceImpl implements BOInstanceTaskService {

	protected static final Logger log = LoggerFactory.getLogger(BOInstanceTaskServiceImpl.class);

	@Autowired
	private InstanceTaskService instanceTaskService;
	@Autowired
	private InstanceTaskDependService instanceTaskDependService;
	@Autowired
	private InstanceTaskLogService instanceTaskLogService;

	@Override
	public BOInstanceTask getById(int taskId, Date taskDate, String instanceId) throws Exception {
		InstanceTask instanceTask = instanceTaskService.getById(taskId, taskDate, instanceId);
		BOInstanceTask boInstanceTask = ModelConverters.instancePO2BO(instanceTask);
		List<InstanceTaskDepend> preDepends = instanceTaskDependService.getByPostId(instanceTask.getTaskId(), instanceTask.getTaskDate(), instanceTask.getInstanceId());
		List<InstanceTaskDepend> postDepends = instanceTaskDependService.getByPreId(instanceTask.getTaskId(), instanceTask.getTaskDate(), instanceTask.getInstanceId());
		boInstanceTask.setPreDepends(preDepends);
		boInstanceTask.setPostDepends(postDepends);
		return boInstanceTask;
	}

	@Override
	public BOInstanceTask getValidById(int taskId, Date taskDate) throws Exception {
		InstanceTask instanceTask = instanceTaskService.getValidInstance(taskId, taskDate);
		BOInstanceTask boInstanceTask = ModelConverters.instancePO2BO(instanceTask);
		List<InstanceTaskDepend> preDepends = instanceTaskDependService.getByPostId(instanceTask.getTaskId(), instanceTask.getTaskDate(), instanceTask.getInstanceId());
		List<InstanceTaskDepend> postDepends = instanceTaskDependService.getByPreId(instanceTask.getTaskId(), instanceTask.getTaskDate(), instanceTask.getInstanceId());
		boInstanceTask.setPreDepends(preDepends);
		boInstanceTask.setPostDepends(postDepends);
		return boInstanceTask;
	}

	@Override
	public void getLog(OutputStream outputStream, Integer taskId, Date taskDate, String instanceId) throws Exception {
		instanceTaskLogService.getLog(outputStream, taskId, taskDate, instanceId);
	}
	
	@Override
	public void getLastLog(OutputStream outputStream, Integer taskId, Date taskDate) throws Exception {
		InstanceTask instanceTask = instanceTaskService.getValidInstance(taskId, taskDate);
		if (instanceTask == null) {
			IOUtils.write("实例不存在。", outputStream);
			return;
		}
		instanceTaskLogService.getLog(outputStream, taskId, taskDate, instanceTask.getInstanceId());
	}

	@Override
	public List<BOInstanceTask> getInstancesById(int taskId, Date taskDate) throws Exception {
		List<BOInstanceTask> result = new ArrayList<BOInstanceTask>();
		List<InstanceTask> instanceTaskList = instanceTaskService.getInstancesById(taskId, taskDate);
		if (instanceTaskList == null) {
			return null;
		}
		for (InstanceTask instanceTask : instanceTaskList) {
			BOInstanceTask boInstanceTask = ModelConverters.instancePO2BO(instanceTask);
			result.add(boInstanceTask);
		}
		return result;
	}

}
