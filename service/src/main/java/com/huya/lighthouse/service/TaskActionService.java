package com.huya.lighthouse.service;

import java.util.List;

import com.huya.lighthouse.model.po.instance.InstanceTaskKey;

/**
 * 
 */
public interface TaskActionService {

	public void run(String startDate, String endDate, String taskIds, Integer isSelfRun, Integer isForceRun) throws Exception;

	public void kill(List<InstanceTaskKey> instanceTaskKeyList) throws Exception;

	public void setSuccess(List<InstanceTaskKey> instanceTaskKeyList) throws Exception;
}
