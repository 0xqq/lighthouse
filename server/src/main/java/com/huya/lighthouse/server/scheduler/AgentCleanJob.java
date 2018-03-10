package com.huya.lighthouse.server.scheduler;

import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.model.po.def.DefAgentGroup;
import com.huya.lighthouse.server.executor.InstanceTaskExecutor;
import com.huya.lighthouse.server.factory.DefAgentGroupFactory;
import com.huya.lighthouse.util.SSHAsynExecUtils;

/**
 * quartz的Job实现 : 定时任务触发
 * 
 */
public class AgentCleanJob implements Job {

	private static Logger logger = LoggerFactory.getLogger(AgentCleanJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("AgentCleanJob start");
		try {
			Map<String, Map<String, DefAgentGroup>> defAgentGroupMap = DefAgentGroupFactory.getAll();
			if (defAgentGroupMap == null) {
				return;
			}
			for (Map<String, DefAgentGroup> hostMap : defAgentGroupMap.values()) {
				if (hostMap == null) {
					continue;
				}
				for (DefAgentGroup defAgentGroup : hostMap.values()) {
					if (defAgentGroup == null) {
						continue;
					}
					String instanceBaseDir = InstanceTaskExecutor.getInstanceBaseDir(defAgentGroup.getWorkBaseDir());
					SSHAsynExecUtils.clean(defAgentGroup.getAgentHost(), defAgentGroup.getAgentPort(), defAgentGroup.getAgentUser(), defAgentGroup.getAgentPrivateKey(),
							defAgentGroup.getAgentPassword(), instanceBaseDir, 92);
				}
			}
		} catch (Exception e) {
			logger.error("AgentCleanJob error", e);
		}
	}

	public static void initTrigger() throws SchedulerException {
		QuartzScheduler.addSimpleTrigger(AgentCleanJob.class, "AgentCleanJob", "0 0 12 * * ?");
	}
}
