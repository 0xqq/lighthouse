package com.huya.lighthouse.server.scheduler;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.model.bo.def.DefTaskCron;
import com.huya.lighthouse.model.po.def.DefTask;
import com.huya.lighthouse.model.util.ModelConverters;
import com.huya.lighthouse.server.TaskAction;
import com.huya.lighthouse.server.factory.ServiceBeanFactory;
import com.huya.lighthouse.util.DateUtils2;

/**
 * quartz的Job实现 : 定时任务触发
 * 
 */
public class TaskSchedulerJob implements Job {

	private static Logger logger = LoggerFactory.getLogger(TaskSchedulerJob.class);

	public final static String CRON_INSTANCE_ID = "cron";

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			String jobName = context.getJobDetail().getKey().getName();
			logger.info("TaskSchedulerJob, taskId=" + jobName);

			Integer taskId = Integer.parseInt(jobName);
			DefTask defTask = ServiceBeanFactory.getDefTaskService().getById(taskId);
			DefTaskCron cronTask = (DefTaskCron) ModelConverters.defPO2BO(defTask);

			Date scheduleTime = context.getScheduledFireTime();
			scheduleTime = DateUtils2.clear2SecondLevel(scheduleTime);
			Date taskDate = DateUtils2.add(scheduleTime, 0, cronTask.getMoveMonth(), cronTask.getMoveDay(), cronTask.getMoveHour(), cronTask.getMoveMinute());

			TaskAction.run(taskId, taskDate, CRON_INSTANCE_ID, 0, 0);
		} catch (Exception e) {
			logger.error("", e);
		}

	}
}
