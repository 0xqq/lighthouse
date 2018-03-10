package com.huya.lighthouse.server.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.server.factory.DefAgentGroupFactory;
import com.huya.lighthouse.server.factory.DefParamFactory;

/**
 * quartz的Job实现 : 定时任务触发
 * 
 */
public class FactoryLoaderJob implements Job {

	private static Logger logger = LoggerFactory.getLogger(FactoryLoaderJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("FactoryLoaderJob start");
		try {
			DefAgentGroupFactory.load();
			DefParamFactory.load();
		} catch (Exception e) {
			logger.error("FactoryLoaderJob error", e);
		}
	}

	public static void initTrigger() throws SchedulerException {
		QuartzScheduler.addSimpleTrigger(FactoryLoaderJob.class, "factoryLoaderJob", "0 0/30 * * * ?");
	}
}
