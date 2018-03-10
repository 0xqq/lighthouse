package com.huya.lighthouse.service;

import java.util.Date;
import java.util.List;

import com.huya.lighthouse.model.po.def.DefTask;
import com.huya.lighthouse.model.query.DefTaskQuery;
import com.huya.lighthouse.model.query.page.Page;

/**
 * [DefTask] 的业务操作
 * 
 */
public interface DefTaskService {

	/**
	 * 创建DefTask
	 **/
	public DefTask create(DefTask defTask);

	/**
	 * 更新DefTask
	 **/
	public DefTask update(DefTask defTask);

	/**
	 * 删除DefTask
	 **/
	public void removeById(int taskId);

	/**
	 * 根据ID得到DefTask
	 **/
	public DefTask getById(int taskId);
	
	/** 
	 * 分页查询: DefTask
	 **/      
	public Page<DefTask> findPage(DefTaskQuery query);

	public List<DefTask> getByDependPreId(Integer taskId, Integer isValid);

	public List<DefTask> getByDependPostId(Integer taskId, Integer isValid);

	public List<DefTask> getAllValidCronTask();

	public List<DefTask> getForInitByCatalogId(Integer catalogId);

	public void updateIsValidById(int taskId, int isValid);

	public List<DefTask> getByMonitorCronExp(String cronExp);

	public List<DefTask> getByOfflineTime(Date offlineTime);

	public List<DefTask> getOfflineValid();

}
