package com.huya.lighthouse.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.huya.lighthouse.dao.DefAgentGroupDao;
import com.huya.lighthouse.model.po.def.DefAgentGroup;
import com.huya.lighthouse.model.query.DefAgentGroupQuery;
import com.huya.lighthouse.model.query.page.Page;
import com.huya.lighthouse.service.DefAgentGroupService;
import com.huya.lighthouse.service.util.LeaderFinder;
import com.huya.lighthouse.util.HttpClientUtils2;

/**
 * [DefAgentGroup] 的业务操作实现类
 * 
 */
@Service("defAgentGroupService")
public class DefAgentGroupServiceImpl implements DefAgentGroupService {

	protected static final Logger log = LoggerFactory.getLogger(DefAgentGroupServiceImpl.class);

	@Autowired
	private DefAgentGroupDao defAgentGroupDao;
	private boolean isProd = StringUtils.equals(System.getenv("DWENV"), "prod");
	
	private void notifyServer() throws Exception {
		if (isProd) {
			String url = LeaderFinder.leaderURL + "/reBuildDefAgentGroupFactory.do";
			HttpClientUtils2.get(url);
		}
	}

	/**
	 * 创建DefAgentGroup
	 * @throws Exception 
	 **/
	public DefAgentGroup create(DefAgentGroup defAgentGroup) throws Exception {
		Assert.notNull(defAgentGroup, "'defAgentGroup' must be not null");
		defAgentGroupDao.insert(defAgentGroup);
		notifyServer();
		return defAgentGroup;
	}

	/**
	 * 更新DefAgentGroup
	 **/
	public DefAgentGroup update(DefAgentGroup defAgentGroup) throws Exception {
		Assert.notNull(defAgentGroup, "'defAgentGroup' must be not null");
		defAgentGroupDao.update(defAgentGroup);
		notifyServer();
		return defAgentGroup;
	}

	/**
	 * 删除DefAgentGroup
	 **/
	public void removeById(String agentHostGroup, String agentHost) throws Exception {
		defAgentGroupDao.deleteById(agentHostGroup, agentHost);
		notifyServer();
	}

	/**
	 * 根据ID得到DefAgentGroup
	 **/
	public DefAgentGroup getById(String agentHostGroup, String agentHost) {
		return defAgentGroupDao.getById(agentHostGroup, agentHost);
	}


	@Override
	public List<DefAgentGroup> getAllValid() {
		return defAgentGroupDao.getAllValid();
	}

	@Override
	public Page<DefAgentGroup> findPage(DefAgentGroupQuery query) {
		return defAgentGroupDao.findPage(query);
	}

	@Override
	public List<String> getAllAgentHostGroup() {
		List<DefAgentGroup> defAgentGroupList = defAgentGroupDao.getAllValid();
		if (defAgentGroupList == null) {
			return null;
		}
		List<String> agentHostGroupList = new ArrayList<String>();
		for (DefAgentGroup defAgentGroup : defAgentGroupList) {
			if (!agentHostGroupList.contains(defAgentGroup.getAgentHostGroup())) {
				agentHostGroupList.add(defAgentGroup.getAgentHostGroup());
			}
		}
		return agentHostGroupList;
	}
}
