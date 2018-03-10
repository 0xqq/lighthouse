package com.huya.lighthouse.dao;

import java.util.List;

import com.huya.lighthouse.model.po.def.DefLessee;

/**
 * tableName: def_lessee [DefLessee] 的Dao操作
 * 
 */
public interface DefLesseeDao {

	public void insert(DefLessee entity);

	public int update(DefLessee entity);

	public int deleteById(int lesseeId);

	public DefLessee getById(int lesseeId);

	public List<DefLessee> getAllValid();

	public DefLessee getByCatalogId(Integer catalogId);

}
