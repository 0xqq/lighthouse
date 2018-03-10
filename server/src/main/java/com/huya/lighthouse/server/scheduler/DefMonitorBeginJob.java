package com.huya.lighthouse.server.scheduler;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.model.po.def.DefMonitorBegin;
import com.huya.lighthouse.model.po.def.DefTask;
import com.huya.lighthouse.model.po.instance.InstanceTask;
import com.huya.lighthouse.model.type.Status;
import com.huya.lighthouse.server.factory.ServiceBeanFactory;
import com.huya.lighthouse.server.util.AlertUtils;
import com.huya.lighthouse.util.DateUtils2;

/**
 * quartz的Job实现 : 定时任务触发
 * 
 */
public class DefMonitorBeginJob implements Job {

	private static Logger logger = LoggerFactory.getLogger(DefMonitorBeginJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			String jobName = context.getJobDetail().getKey().getName();
			logger.info("DefMonitorBeginJob, cronExp=" + jobName);

			List<DefMonitorBegin> defMonitorBeginList = ServiceBeanFactory.getDefMonitorBeginService().getByCronExp(jobName);
			if (defMonitorBeginList == null) {
				return;
			}
			Date scheduleTime = context.getScheduledFireTime();
			scheduleTime = DateUtils2.clear2SecondLevel(scheduleTime);

			for (DefMonitorBegin defMonitorBegin : defMonitorBeginList) {
				Date taskDate = DateUtils2.add(scheduleTime, 0, defMonitorBegin.getMoveMonth(), defMonitorBegin.getMoveDay(), defMonitorBegin.getMoveHour(), defMonitorBegin.getMoveMinute());
				try {
					InstanceTask instanceTask = ServiceBeanFactory.getInstanceTaskService().getValidInstance(defMonitorBegin.getTaskId(), taskDate);
					if (instanceTask == null) {
						DefTask defTask = ServiceBeanFactory.getDefTaskService().getById(defMonitorBegin.getTaskId());
						AlertUtils.alertMonitorBegin(defTask.getCatalogId(), defTask.getTaskId(), defTask.getTaskName(), taskDate, defMonitorBegin.getCronExp(), defMonitorBegin.getMoveMonth(), defMonitorBegin.getMoveDay(), defMonitorBegin.getMoveHour(), defMonitorBegin.getMoveMinute());
						continue;
					}
					boolean isInit = StringUtils.equals(Status.INIT.name(), instanceTask.getStatus());
					boolean isReady = StringUtils.equals(Status.READY.name(), instanceTask.getStatus());
					boolean isRetry = instanceTask.getRetriedNum() > 0;
					if ((isInit || isReady) && !isRetry) {
						AlertUtils.alertMonitorBegin(instanceTask.getCatalogId(), instanceTask.getTaskId(), instanceTask.getTaskName(), taskDate, defMonitorBegin.getCronExp(), defMonitorBegin.getMoveMonth(), defMonitorBegin.getMoveDay(), defMonitorBegin.getMoveHour(), defMonitorBegin.getMoveMinute());
					}
				} catch (Exception e) {
					String msg = String.format("taskId=%s, execCronExp=%s", defMonitorBegin.getTaskId(), defMonitorBegin.getCronExp());
					logger.error(msg, e);
				}
			}
		} catch (Exception e) {
			logger.error("DefMonitorBeginJob error", e);
		}
	}

}
