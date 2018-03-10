package com.huya.lighthouse.service;

import java.util.List;

import com.huya.lighthouse.model.po.def.DefTaskParam;
import com.huya.lighthouse.model.query.DefTaskParamQuery;
import com.huya.lighthouse.model.query.page.Page;

/**
 * [DefTaskParam] 的业务操作
 * 
 */
public interface DefTaskParamService {

	/**
	 * 创建DefTaskParam
	 **/
	public DefTaskParam create(DefTaskParam defTaskParam) throws Exception;

	/**
	 * 更新DefTaskParam
	 **/
	public DefTaskParam update(DefTaskParam defTaskParam) throws Exception;

	/**
	 * 删除DefTaskParam
	 **/
	public void removeById(Integer catalogId, String paramCode) throws Exception;

	/**
	 * 根据ID得到DefTaskParam
	 **/
	public DefTaskParam getById(Integer catalogId, String paramCode);

	/**
	 * 
	 */
	public List<DefTaskParam> getAllValid();
	
	/** 
	 * 分页查询: DefTaskParam
	 **/      
	public Page<DefTaskParam> findPage(DefTaskParamQuery query);

}
