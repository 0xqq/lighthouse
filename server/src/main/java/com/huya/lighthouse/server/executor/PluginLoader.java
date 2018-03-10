package com.huya.lighthouse.server.executor;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.model.po.def.DefAgentGroup;
import com.huya.lighthouse.server.factory.DefAgentGroupFactory;
import com.huya.lighthouse.server.factory.PropertiesFactory;
import com.huya.lighthouse.util.SSHUtils;

public class PluginLoader {

	private static Logger logger = LoggerFactory.getLogger(DefAgentGroupFactory.class);
	
	public static void load() throws Exception {
		String noLoadPlugin = System.getProperty("noLoadPlugin");
		logger.info("noLoadPlugin = " + noLoadPlugin);
		if (StringUtils.equals("true", noLoadPlugin)) {
			return;
		}
		long startTime = System.currentTimeMillis();
		Map<String, Map<String, DefAgentGroup>> defAgentGroupMap = DefAgentGroupFactory.getAll();
		for (Map<String, DefAgentGroup> defAgentHostMap : defAgentGroupMap.values()) {
			for (DefAgentGroup defAgentGroup : defAgentHostMap.values()) {
				String dstPluginDir = InstanceTaskExecutor.getTaskPluginDir(defAgentGroup.getWorkBaseDir());
				String cmd = "mkdir -p -m 777 " + dstPluginDir;
				SSHUtils.execRetry(defAgentGroup.getAgentHost(), defAgentGroup.getAgentPort(), defAgentGroup.getAgentUser(), defAgentGroup.getAgentPrivateKey(), defAgentGroup.getAgentPassword(), cmd);
				String srcPluginDir = PropertiesFactory.srcPluginDir + "/*";
				SSHUtils.sftpPutRetry(defAgentGroup.getAgentHost(), defAgentGroup.getAgentPort(), defAgentGroup.getAgentUser(), defAgentGroup.getAgentPrivateKey(), defAgentGroup.getAgentPassword(), srcPluginDir, dstPluginDir, 40, 3000l);
			}
		}
		logger.info("=========================PluginLoader load: " + (System.currentTimeMillis()-startTime));
	}
}
