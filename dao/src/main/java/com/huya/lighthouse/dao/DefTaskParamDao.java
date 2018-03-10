package com.huya.lighthouse.dao;

import java.util.List;

import com.huya.lighthouse.model.po.def.DefTaskParam;
import com.huya.lighthouse.model.query.DefTaskParamQuery;
import com.huya.lighthouse.model.query.page.Page;

/**
 * tableName: def_task_param [DefTaskParam] 的Dao操作
 * 
 */
public interface DefTaskParamDao {

	public void insert(DefTaskParam entity);

	public int update(DefTaskParam entity);

	public int deleteById(Integer catalogId, String paramCode);

	public DefTaskParam getById(Integer catalogId, String paramCode);

	public List<DefTaskParam> getAllValid();

	public Page<DefTaskParam> findPage(DefTaskParamQuery query);

}
