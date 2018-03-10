package com.huya.lighthouse.dao;

import java.util.List;

import com.huya.lighthouse.model.po.def.DefAgentGroup;
import com.huya.lighthouse.model.query.DefAgentGroupQuery;
import com.huya.lighthouse.model.query.page.Page;

/**
 * tableName: def_agent_group [DefAgentGroup] 的Dao操作
 * 
 */
public interface DefAgentGroupDao {

	public void insert(DefAgentGroup entity);

	public int update(DefAgentGroup entity);

	public int deleteById(String agentHostGroup, String agentHost);

	public DefAgentGroup getById(String agentHostGroup, String agentHost);

	public List<DefAgentGroup> getAllValid();

	public Page<DefAgentGroup> findPage(DefAgentGroupQuery query);

}
