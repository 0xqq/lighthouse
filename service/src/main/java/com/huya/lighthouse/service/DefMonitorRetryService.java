package com.huya.lighthouse.service;

import java.util.List;

import com.huya.lighthouse.model.po.def.DefMonitorRetry;

/**
 * [DefMonitorRetry] 的业务操作
 * 
 */
public interface DefMonitorRetryService {

	/**
	 * 创建DefMonitorRetry
	 **/
	public DefMonitorRetry create(DefMonitorRetry defMonitorRetry);

	/**
	 * 更新DefMonitorRetry
	 **/
	public DefMonitorRetry update(DefMonitorRetry defMonitorRetry);

	/**
	 * 删除DefMonitorRetry
	 **/
	public void removeById(int taskId, int failRetryN);

	/**
	 * 根据ID得到DefMonitorRetry
	 **/
	public DefMonitorRetry getById(int taskId, int failRetryN);

	public List<DefMonitorRetry> getByTaskId(Integer taskId, int isValid);

	public void batchCreate(List<DefMonitorRetry> defMonitorRetryList);

	public void removeByTaskId(Integer taskId);

	public void updateIsValidById(int taskId, int isValid);

}
