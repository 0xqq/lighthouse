package com.huya.lighthouse.server.scheduler;

import java.util.Collection;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时器
 * 
 */
public class QuartzScheduler {

	private static Logger logger = LoggerFactory.getLogger(QuartzScheduler.class);

	private static SchedulerFactory factory;
	private static Scheduler scheduler;

	public static void start() throws SchedulerException {
		logger.info("Start scheduler!");
		factory = new StdSchedulerFactory();
		scheduler = factory.getScheduler();
		scheduler.start();
	}

	public static void addSimpleTrigger(Class<? extends Job> jobClass, String cronId, String cronExp) throws SchedulerException {
		try {
			addTrigger(jobClass, cronId, Scheduler.DEFAULT_GROUP, cronId, Scheduler.DEFAULT_GROUP, cronExp);
		} catch (ObjectAlreadyExistsException e) {
			logger.info("cronId={}, cronExp={} already exists", cronId, cronExp);
		}
	}

	public static void delSimpleTrigger(String cronId, String cronExp) throws SchedulerException {
		delTrigger(cronId, Scheduler.DEFAULT_GROUP, cronId, Scheduler.DEFAULT_GROUP);
	}

	/**
	 * 增加定时任务
	 * 
	 * @param jobClass
	 * @param jobName
	 * @param jobGroup
	 * @param cronTriggerName
	 * @param cronTriggerGroup
	 * @param cronExp
	 * @throws SchedulerException
	 */
	public static void addTrigger(Class<? extends Job> jobClass, String jobName, String jobGroup, String cronTriggerName, String cronTriggerGroup, String cronExp) throws SchedulerException {
		logger.info("Add scheduler: jobName={}, jobGroup={}, cronTriggerName={}, cronTriggerGroup={}, cronExp={}", new Object[] { jobName, jobGroup, cronTriggerName, cronTriggerGroup, cronExp });
		JobDetail job = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup).build();
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(cronTriggerName, cronTriggerGroup).withSchedule(CronScheduleBuilder.cronSchedule(cronExp)).build();
		scheduler.scheduleJob(job, trigger);
	}

	/**
	 * 删除定时任务
	 * 
	 * @param jobName
	 * @param jobGroup
	 * @param cronTriggerName
	 * @param cronTriggerGroup
	 * @throws SchedulerException
	 */
	public static void delTrigger(String jobName, String jobGroup, String cronTriggerName, String cronTriggerGroup) throws SchedulerException {
		logger.info("Delete scheduler: jobName={}, jobGroup={}, cronTriggerName={}, cronTriggerGroup={}", new Object[] { jobName, jobGroup, cronTriggerName, cronTriggerGroup });
		TriggerKey triggerKey = new TriggerKey(cronTriggerName, cronTriggerGroup);
		JobKey jobKey = new JobKey(jobName, jobGroup);
		scheduler.pauseTrigger(triggerKey);
		scheduler.unscheduleJob(triggerKey);
		scheduler.deleteJob(jobKey);
	}

	/**
	 * 销毁定时器
	 * 
	 * @throws SchedulerException
	 */
	public static void shutdownAll() throws SchedulerException {
		logger.info("Shutdown all scheduler!");
		Collection<Scheduler> schedulerList = factory.getAllSchedulers();
		for (Scheduler scheduler : schedulerList) {
			scheduler.shutdown(true);
		}
	}

}
