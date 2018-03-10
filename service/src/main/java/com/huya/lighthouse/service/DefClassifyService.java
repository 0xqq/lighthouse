package com.huya.lighthouse.service;

import java.util.List;

import com.huya.lighthouse.model.po.def.DefClassify;

/**
 * [DefClassify] 的业务操作
 * 
 */
public interface DefClassifyService {

	/**
	 * 创建DefClassify
	 **/
	public DefClassify create(DefClassify defClassify);

	/**
	 * 更新DefClassify
	 **/
	public DefClassify update(DefClassify defClassify);

	/**
	 * 删除DefClassify
	 **/
	public void removeById(String classifyType, String classifyCode, int taskId);

	/**
	 * 根据ID得到DefClassify
	 **/
	public DefClassify getById(String classifyType, String classifyCode, int taskId);

	public void batchCreate(List<DefClassify> defClassifyList);

	public void removeByTaskId(int taskId);

	public List<DefClassify> getByTaskId(int taskId, int isValid);

	public void updateIsValidById(int taskId, int isValid);

}
