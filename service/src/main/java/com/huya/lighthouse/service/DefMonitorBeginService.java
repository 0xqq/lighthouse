package com.huya.lighthouse.service;

import java.util.List;

import com.huya.lighthouse.model.po.def.DefMonitorBegin;

/**
 * [DefMonitorBegin] 的业务操作
 * 
 */
public interface DefMonitorBeginService {
	
	/**
	 * 创建DefMonitorBegin
	 **/
	public DefMonitorBegin create(DefMonitorBegin defMonitorBegin);

	/**
	 * 更新DefMonitorBegin
	 **/
	public DefMonitorBegin update(DefMonitorBegin defMonitorBegin);

	/**
	 * 删除DefMonitorBegin
	 **/
	public void removeById(String cronExp, int taskId);

	/**
	 * 根据ID得到DefMonitorBegin
	 **/
	public DefMonitorBegin getById(String cronExp, int taskId);

	public List<String> getAllValidCronExp();

	public void batchCreate(List<DefMonitorBegin> defMonitorBeginList);

	public void removeByTaskId(Integer taskId);

	public List<DefMonitorBegin> getByTaskId(int taskId, int isValid);
	
	public void updateIsValidById(int taskId, int isValid);

	public List<DefMonitorBegin> getByCronExp(String cronExp);

}
