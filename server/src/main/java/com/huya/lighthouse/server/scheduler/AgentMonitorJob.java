package com.huya.lighthouse.server.scheduler;

import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.model.po.def.DefAgentGroup;
import com.huya.lighthouse.server.executor.AgentSelector;
import com.huya.lighthouse.server.factory.DefAgentGroupFactory;
import com.huya.lighthouse.server.factory.PropertiesFactory;

/**
 * quartz的Job实现 : 定时任务触发
 * 
 */
public class AgentMonitorJob implements Job {

	private static Logger logger = LoggerFactory.getLogger(AgentMonitorJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("AgentMonitorJob start");
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
					boolean isDfOk = AgentSelector.isDiskOK(defAgentGroup);
					boolean isFreeOk = AgentSelector.isMemoryOK(defAgentGroup);
					if (!(isDfOk && isFreeOk)) {
						alert(PropertiesFactory.admins, "灯塔调度系统Agent异常", "灯塔调度系统Agent异常" + defAgentGroup.getAgentHostGroup() + "," + defAgentGroup.getAgentHost(), "", PropertiesFactory.adminsAlerts);
					}
				}
			}
		} catch (Exception e) {
			logger.error("AgentMonitorJob error", e);
		}
	}

	public static void initTrigger() throws SchedulerException {
		QuartzScheduler.addSimpleTrigger(AgentMonitorJob.class, "AgentMonitorJob", "0 0/10 * * * ?");
	}

	public static void alert(String[] receivers, String title, String msg, String url, String[] sendTypes) {
		logger.info("alert receivers={}, sendTypes={}, msg={}", new Object[] { receivers, sendTypes, msg });
		try {
			//TODO send msg
		} catch (Exception e) {
			logger.error("alert error", e);
		}
	}
}
