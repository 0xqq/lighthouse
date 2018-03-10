package com.huya.lighthouse.service;

import java.util.List;

import com.huya.lighthouse.model.po.def.DefTaskDepend;

/**
 * [DefTaskDepend] 的业务操作
 * 
 */
public interface DefTaskDependService {

	/**
	 * 创建DefTaskDepend
	 **/
	public DefTaskDepend create(DefTaskDepend defTaskDepend);

	/**
	 * 更新DefTaskDepend
	 **/
	public DefTaskDepend update(DefTaskDepend defTaskDepend);

	/**
	 * 删除DefTaskDepend
	 **/
	public void removeById(int taskId, int preTaskId);

	/**
	 * 根据ID得到DefTaskDepend
	 **/
	public DefTaskDepend getById(int taskId, int preTaskId);

	public List<DefTaskDepend> getByPreId(Integer preTaskId, int isValid);

	public List<DefTaskDepend> getByPostId(Integer taskId, int isValid);

	public void batchCreate(List<DefTaskDepend> defTaskDependList);

	public void removeByTaskId(Integer taskId);

	public void updateIsValidById(int taskId, int isValid);

}
