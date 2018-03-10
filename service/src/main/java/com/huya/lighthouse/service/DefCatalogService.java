package com.huya.lighthouse.service;

import java.util.List;

import com.huya.lighthouse.model.po.def.DefCatalog;
import com.huya.lighthouse.model.query.DefCatalogQuery;
import com.huya.lighthouse.model.query.page.Page;


/**
 * [DefCatalog] 的业务操作
 * 
 */
public interface DefCatalogService {

	/** 
	 * 创建DefCatalog
	 **/
	public DefCatalog create(DefCatalog defCatalog);
	
	/** 
	 * 更新DefCatalog
	 **/	
    public DefCatalog update(DefCatalog defCatalog);
    
	/** 
	 * 删除DefCatalog
	 **/
    public void removeById(int catalogId);
    
	/** 
	 * 根据ID得到DefCatalog
	 **/    
    public DefCatalog getById(int catalogId);
    
    /**
     * 
     * @return
     */
    public List<DefCatalog> getAllValid();
    
	/** 
	 * 分页查询: DefCatalog
	 **/      
	public Page<DefCatalog> findPage(DefCatalogQuery query);
	
    
}
