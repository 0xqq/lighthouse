package com.huya.lighthouse.server.factory;

import com.huya.lighthouse.model.po.def.DefAgentGroup;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefAgentGroupFactory {

	private static Logger logger = LoggerFactory.getLogger(DefAgentGroupFactory.class);
	private static Map<String, Map<String, DefAgentGroup>> defAgentGroupMap = new HashMap<String, Map<String, DefAgentGroup>>();

	public static synchronized void load() {
		long startTime = System.currentTimeMillis();
		Map<String, Map<String, DefAgentGroup>> curDefAgentGroupMap = new HashMap<String, Map<String, DefAgentGroup>>();
		List<DefAgentGroup> DefAgentGroupList = ServiceBeanFactory.getDefAgentGroupService().getAllValid();
		if (DefAgentGroupList == null) {
			logger.info("None DefAgentGroup");
		}
		for (DefAgentGroup defAgentGroup : DefAgentGroupList) {
			String agentHostGroup = defAgentGroup.getAgentHostGroup();
			Map<String, DefAgentGroup> agentHostMap = curDefAgentGroupMap.get(agentHostGroup);
			if (agentHostMap == null) {
				agentHostMap = new HashMap<String, DefAgentGroup>();
				curDefAgentGroupMap.put(agentHostGroup, agentHostMap);
			}
			agentHostMap.put(defAgentGroup.getAgentHost(), defAgentGroup);
		}
		defAgentGroupMap = curDefAgentGroupMap;
		logger.info("=========================DefAgentGroupFactory load: " + (System.currentTimeMillis()-startTime));
	}

	public static Map<String, DefAgentGroup> getByAgentHostGroup(String agentHostGroup) {
		return defAgentGroupMap.get(agentHostGroup);
	}
	
	public static DefAgentGroup getAgentHost(String agentHostGroup, String agentHost) {
		Map<String, DefAgentGroup> agentHostMap = defAgentGroupMap.get(agentHostGroup);
		if(agentHostMap == null) {
			return null;
		}
		DefAgentGroup result =  agentHostMap.get(agentHost);
		if (result == null) {
			for (Map.Entry<String, Map<String, DefAgentGroup>> entryOut : defAgentGroupMap.entrySet()) {
				Map<String, DefAgentGroup> agentHostMapTmp = entryOut.getValue();
				for (Map.Entry<String, DefAgentGroup> entryIn : agentHostMapTmp.entrySet()) {
					DefAgentGroup defAgentGroupTmp = entryIn.getValue();
					if (StringUtils.equals(agentHost, defAgentGroupTmp.getAgentHost())) {
						return defAgentGroupTmp;
					}
				}
			}
		}
		return result;
	}

	public static Map<String, Map<String, DefAgentGroup>> getAll() {
		return defAgentGroupMap;
	}
}
