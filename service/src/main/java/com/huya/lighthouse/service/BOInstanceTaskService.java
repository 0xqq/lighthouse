package com.huya.lighthouse.service;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import com.huya.lighthouse.model.bo.instance.BOInstanceTask;

/**
 * 
 */
public interface BOInstanceTaskService {

	public BOInstanceTask getById(int taskId, Date taskDate, String instanceId) throws Exception;

	public BOInstanceTask getValidById(int taskId, Date taskDate) throws Exception;
	
	public void getLog(OutputStream outputStream, Integer taskId, Date taskDate, String instanceId) throws Exception;

	public List<BOInstanceTask> getInstancesById(int taskId, Date taskDate) throws Exception;

	public void getLastLog(OutputStream outputStream, Integer taskId, Date taskDate) throws Exception;
	
}
