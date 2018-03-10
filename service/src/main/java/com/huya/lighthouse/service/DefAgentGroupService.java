package com.huya.lighthouse.service;

import java.util.List;

import com.huya.lighthouse.model.po.def.DefAgentGroup;
import com.huya.lighthouse.model.query.DefAgentGroupQuery;
import com.huya.lighthouse.model.query.page.Page;

/**
 * [DefAgentGroup] 的业务操作
 * 
 */
public interface DefAgentGroupService {

	/**
	 * 创建DefAgentGroup
	 **/
	public DefAgentGroup create(DefAgentGroup defAgentGroup) throws Exception;

	/**
	 * 更新DefAgentGroup
	 **/
	public DefAgentGroup update(DefAgentGroup defAgentGroup) throws Exception;

	/**
	 * 删除DefAgentGroup
	 **/
	public void removeById(String agentHostGroup, String agentHost) throws Exception;

	/**
	 * 根据ID得到DefAgentGroup
	 **/
	public DefAgentGroup getById(String agentHostGroup, String agentHost);

	public List<DefAgentGroup> getAllValid();

	/** 
	 * 分页查询: DefAgentGroup
	 **/      
	public Page<DefAgentGroup> findPage(DefAgentGroupQuery query);

	public List<String> getAllAgentHostGroup();
}
