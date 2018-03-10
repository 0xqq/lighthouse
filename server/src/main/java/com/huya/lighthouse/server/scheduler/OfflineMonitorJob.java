package com.huya.lighthouse.server.scheduler;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.model.po.def.DefTask;
import com.huya.lighthouse.server.factory.ServiceBeanFactory;
import com.huya.lighthouse.server.util.AlertUtils;

/**
 * quartz的Job实现 : 下线任务报警
 * 
 */
public class OfflineMonitorJob implements Job {

	private static Logger logger = LoggerFactory.getLogger(OfflineMonitorJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("OfflineMonitorJob start");
		try {
			Date offlineTime = DateUtils.addDays(new Date(), 7);
			List<DefTask> defTaskList = ServiceBeanFactory.getDefTaskService().getByOfflineTime(offlineTime);
			if (defTaskList == null) {
				return;
			}
			for (DefTask defTask : defTaskList) {
				AlertUtils.alertOffline(defTask.getCatalogId(), defTask.getTaskId(), defTask.getTaskName(), defTask.getOfflineTime());
			}
			
			List<DefTask> offlineValidDefTaskList = ServiceBeanFactory.getDefTaskService().getOfflineValid();
			if (offlineValidDefTaskList == null) {
				return;
			}
			for (DefTask defTask : offlineValidDefTaskList) {
				ServiceBeanFactory.getbODefTaskService().offlineByIdServer(defTask.getTaskId());
			}
		} catch (Exception e) {
			logger.error("OfflineMonitorJob error", e);
		}
	}

	public static void initTrigger() throws SchedulerException {
		QuartzScheduler.addSimpleTrigger(OfflineMonitorJob.class, "OfflineMonitorJob", "0 0 8 * * ?");
	}
}
