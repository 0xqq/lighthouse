package com.huya.lighthouse.service;

import java.util.List;

import com.huya.lighthouse.model.po.def.DefMonitorDur;

/**
 * [DefMonitorDur] 的业务操作
 * 
 */
public interface DefMonitorDurService {

	/**
	 * 创建DefMonitorDur
	 **/
	public DefMonitorDur create(DefMonitorDur defMonitorDur);

	/**
	 * 更新DefMonitorDur
	 **/
	public DefMonitorDur update(DefMonitorDur defMonitorDur);

	/**
	 * 删除DefMonitorDur
	 **/
	public void removeById(int taskId, int durSec);

	/**
	 * 根据ID得到DefMonitorDur
	 **/
	public DefMonitorDur getById(int taskId, int durSec);

	public List<DefMonitorDur> getByTaskId(Integer taskId, int isValid);

	public void batchCreate(List<DefMonitorDur> defMonitorDurList);

	public void removeByTaskId(Integer taskId);

	public void updateIsValidById(int taskId, int isValid);

}
