package com.huya.lighthouse.server.scheduler;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.quartz.CronExpression;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.model.po.def.DefCatalog;
import com.huya.lighthouse.model.po.def.DefTask;
import com.huya.lighthouse.model.type.TaskType;
import com.huya.lighthouse.server.factory.ServiceBeanFactory;
import com.huya.lighthouse.server.factory.ThreadPublicFactory;
import com.huya.lighthouse.server.instance.initiator.InstanceTaskDependInitiator;
import com.huya.lighthouse.server.instance.initiator.InstanceTaskInitiator;
import com.huya.lighthouse.server.util.CronExpUtils;

/**
 * quartz的Job实现 : 定时任务触发
 * 
 */
public class TaskInitAllJob implements Job {

	private static Logger logger = LoggerFactory.getLogger(TaskInitAllJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("TaskInitAllJob start");
		try {
			Date scheduleTime = context.getScheduledFireTime();
			final Date date = DateUtils.addDays(scheduleTime, -1);
			List<DefCatalog> defCatalogList = ServiceBeanFactory.getDefCatalogService().getAllValid();
			if (defCatalogList == null) {
				return;
			}
			for (DefCatalog defCatalog : defCatalogList) {
				final Integer catalogId = defCatalog.getCatalogId();
				ThreadPublicFactory.cachedThreadPool.submit(new Runnable() {
					@Override
					public void run() {
						Thread.currentThread().setName("TaskInitAllJob, catalogId=" + catalogId);
						List<DefTask> defTaskList = ServiceBeanFactory.getDefTaskService().getForInitByCatalogId(catalogId);
						if (defTaskList == null) {
							return;
						}
						for (DefTask defTask : defTaskList) {
							try {
								if (StringUtils.equals(defTask.getTaskType(), TaskType.CRON.name())) {
									continue;
								}
								CronExpression cronExp = InstanceTaskDependInitiator.getCronExp(defTask.getExecCronExp(), defTask.getTaskId());
								List<Date> taskDateList = CronExpUtils.getTimeInDate(cronExp, date);
								if (taskDateList == null) {
									continue;
								}
								for (Date taskDate : taskDateList) {
									InstanceTaskInitiator.init(defTask.getTaskId(), taskDate, TaskSchedulerJob.CRON_INSTANCE_ID, 0, 0);
								}
							} catch (Exception e) {
								logger.error("init all job: " + defTask, e);
							}
						}
					}
				});
			}
		} catch (Exception e) {
			logger.error("TaskInitAllJob error", e);
		}
	}

	public static void initTrigger() throws SchedulerException {
		QuartzScheduler.addSimpleTrigger(TaskInitAllJob.class, "taskInitAllJob", "0 0 0 * * ?");
	}
}
