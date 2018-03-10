package com.huya.lighthouse.dao;

import java.util.List;

import com.huya.lighthouse.model.po.def.DefCatalog;
import com.huya.lighthouse.model.query.DefCatalogQuery;
import com.huya.lighthouse.model.query.page.Page;

/**
 * tableName: def_catalog [DefCatalog] 的Dao操作
 * 
 */
public interface DefCatalogDao {

	public void insert(DefCatalog entity);

	public int update(DefCatalog entity);

	public int deleteById(int catalogId);

	public DefCatalog getById(int catalogId);
	
	public List<DefCatalog> getAllValid();

	public Page<DefCatalog> findPage(DefCatalogQuery query);

}
