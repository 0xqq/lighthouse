package com.huya.lighthouse.dao;

import java.util.List;

import com.huya.lighthouse.model.po.def.DefTaskDepend;

/**
 * tableName: def_task_depend [DefTaskDepend] 的Dao操作
 * 
 */
public interface DefTaskDependDao {

	public void insert(DefTaskDepend entity);

	public int update(DefTaskDepend entity);

	public int deleteById(int taskId, int preTaskId);

	public DefTaskDepend getById(int taskId, int preTaskId);

	public List<DefTaskDepend> getByPreId(Integer preTaskId, int isValid);

	public List<DefTaskDepend> getByPostId(Integer taskId, int isValid);

	public int[] batchInsert(List<DefTaskDepend> defTaskDependList);

	public int deleteById(Integer taskId);

	public void updateIsValidById(int taskId, int isValid);

}
