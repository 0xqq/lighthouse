package com.huya.lighthouse.service;

import java.util.List;

import com.huya.lighthouse.model.po.def.DefObjectDepend;

/**
 * [DefObjectDepend] 的业务操作
 * 
 */
public interface DefObjectDependService {

	/**
	 * 创建DefObjectDepend
	 **/
	public DefObjectDepend create(DefObjectDepend defObjectDepend);

	/**
	 * 更新DefObjectDepend
	 **/
	public DefObjectDepend update(DefObjectDepend defObjectDepend);

	/**
	 * 删除DefObjectDepend
	 **/
	public void removeById(int rwFlag, int taskId, String objectId);

	/**
	 * 根据ID得到DefObjectDepend
	 **/
	public DefObjectDepend getById(int rwFlag, int taskId, String objectId);

	public void batchCreate(List<DefObjectDepend> defObjectDependList);

	public void removeByTaskId(Integer taskId);

	public List<DefObjectDepend> getByTaskId(int taskId, int isValid);

	public void updateIsValidById(int taskId, int isValid);

}
