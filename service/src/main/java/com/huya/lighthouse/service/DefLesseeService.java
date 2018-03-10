package com.huya.lighthouse.service;

import java.util.List;

import com.huya.lighthouse.model.po.def.DefLessee;

/**
 * [DefLessee] 的业务操作
 * 
 */
public interface DefLesseeService {

	/**
	 * 创建DefLessee
	 **/
	public DefLessee create(DefLessee defLessee);

	/**
	 * 更新DefLessee
	 **/
	public DefLessee update(DefLessee defLessee);

	/**
	 * 删除DefLessee
	 **/
	public void removeById(int lesseeId);

	/**
	 * 根据ID得到DefLessee
	 **/
	public DefLessee getById(int lesseeId);

	public List<DefLessee> getAllValid();

	public DefLessee getByCatalogId(Integer catalogId);

}
