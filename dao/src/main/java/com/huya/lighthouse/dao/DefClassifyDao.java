package com.huya.lighthouse.dao;

import java.util.List;

import com.huya.lighthouse.model.po.def.DefClassify;

/**
 * tableName: def_classify [DefClassify] 的Dao操作
 * 
 */
public interface DefClassifyDao {

	public void insert(DefClassify entity);

	public int update(DefClassify entity);

	public int deleteById(String classifyType, String classifyCode, int taskId);

	public DefClassify getById(String classifyType, String classifyCode, int taskId);

	public int[] batchInsert(List<DefClassify> entityList);

	public int deleteByTaskId(Integer taskId);

	public List<DefClassify> getByTaskId(Integer taskId, int isValid);

	public void updateIsValidById(int taskId, int isValid);

}
