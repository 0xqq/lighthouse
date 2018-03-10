package com.huya.lighthouse.dao;

import java.util.List;

import com.huya.lighthouse.model.po.def.DefObjectDepend;

/**
 * tableName: def_object_depend [DefObjectDepend] 的Dao操作
 * 
 */
public interface DefObjectDependDao {

	public void insert(DefObjectDepend entity);

	public int update(DefObjectDepend entity);

	public int deleteById(int rwFlag, int taskId, String objectId);

	public DefObjectDepend getById(int rwFlag, int taskId, String objectId);

	public int[] batchInsert(List<DefObjectDepend> defObjectDependList);

	public int deleteById(Integer taskId);

	public List<DefObjectDepend> getByTaskId(int taskId, int isValid);

	public void updateIsValidById(int taskId, int isValid);

}
