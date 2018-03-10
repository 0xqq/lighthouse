package com.huya.lighthouse.server.executor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang.time.DateUtils;
import org.quartz.CronExpression;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.model.bo.def.DefTaskCron;
import com.huya.lighthouse.model.po.def.DefTask;
import com.huya.lighthouse.model.po.instance.InstanceTask;
import com.huya.lighthouse.model.util.ModelConverters;
import com.huya.lighthouse.server.TaskAction;
import com.huya.lighthouse.server.factory.DefAgentGroupFactory;
import com.huya.lighthouse.server.factory.DefParamFactory;
import com.huya.lighthouse.server.factory.InstanceTaskCacheFactory;
import com.huya.lighthouse.server.factory.InstanceTaskLockFactory;
import com.huya.lighthouse.server.factory.ServiceBeanFactory;
import com.huya.lighthouse.server.factory.TaskRunningFactory;
import com.huya.lighthouse.server.factory.ThreadPublicFactory;
import com.huya.lighthouse.server.instance.initiator.InstanceTaskDependInitiator;
import com.huya.lighthouse.server.scheduler.AgentCleanJob;
import com.huya.lighthouse.server.scheduler.AgentMonitorJob;
import com.huya.lighthouse.server.scheduler.DefMonitorBeginJob;
import com.huya.lighthouse.server.scheduler.FactoryLoaderJob;
import com.huya.lighthouse.server.scheduler.OfflineMonitorJob;
import com.huya.lighthouse.server.scheduler.QuartzScheduler;
import com.huya.lighthouse.server.scheduler.TaskInitAllJob;
import com.huya.lighthouse.server.scheduler.TaskSchedulerJob;
import com.huya.lighthouse.server.util.CronExpUtils;
import com.huya.lighthouse.util.DateUtils2;

/**
 *
 */
public class SceneRestorer {

	private static Logger logger = LoggerFactory.getLogger(SceneRestorer.class);

	public static void restore() throws Exception {
		logger.info("=========================Start Scene restore");
		long startTime = System.currentTimeMillis();
		DefAgentGroupFactory.load();
		PluginLoader.load();
		DefParamFactory.load();

		ThreadPublicFactory.clear();
		TaskRunningFactory.clear();
		InstanceTaskLockFactory.clear();
		InstanceTaskCacheFactory.clear();

		QuartzScheduler.start();
		TaskInitAllJob.initTrigger();
		FactoryLoaderJob.initTrigger();
		AgentMonitorJob.initTrigger();
		AgentCleanJob.initTrigger();
		OfflineMonitorJob.initTrigger();
		cronMonitorBeginRestore();

		unDoneTaskRestore();
		cronTaskRestore();
		logger.info("=========================End Scene restore, time=" + (System.currentTimeMillis() - startTime));
	}

	private static void cronMonitorBeginRestore() {
		logger.info("=========================cronMonitorBeginRestore");
		List<String> cronExpList = ServiceBeanFactory.getDefMonitorBeginService().getAllValidCronExp();
		if (cronExpList == null) {
			return;
		}
		for (String cronExp : cronExpList) {
			try {
				QuartzScheduler.addSimpleTrigger(DefMonitorBeginJob.class, cronExp, cronExp);
			} catch (SchedulerException e) {
				logger.error(cronExp, e);
			}
		}
	}

	private static void unDoneTaskRestore() {
		long startTime = System.currentTimeMillis();
		List<InstanceTask> unDoneInstanceTaskList = ServiceBeanFactory.getInstanceTaskService().getAllValidUnDoneInstance();
		if (unDoneInstanceTaskList == null) {
			return;
		}
		ExecutorService es = Executors.newFixedThreadPool(100);
		List<Future<?>> futureList = new ArrayList<Future<?>>();
		for (final InstanceTask unDoneInstanceTask : unDoneInstanceTaskList) {
			if (unDoneInstanceTask == null) {
				continue;
			}
			Future<?> futureTask = es.submit(new Runnable() {
				@Override
				public void run() {
					for (int i=0; i<5; i++) {
						try {
							TaskAction.run(unDoneInstanceTask.getTaskId(), unDoneInstanceTask.getTaskDate(), unDoneInstanceTask.getInstanceId(), unDoneInstanceTask.getIsSelfRun(),
									unDoneInstanceTask.getIsForceRun());
							break;
						} catch (Exception e) {
							logger.error("unDoneTaskRestore: " + i + ", " + unDoneInstanceTask.getTaskId() + ", " + unDoneInstanceTask.getTaskDate() + ", " + unDoneInstanceTask.getInstanceId(), e);
						}
					}
				}
			});
			futureList.add(futureTask);
		}
		for (Future<?> futureTask : futureList) {
			try {
				futureTask.get();
			} catch (Exception e) {
				logger.error("unDoneTaskRestore future task", e);
			}
		}
		es.shutdown();
		logger.info("=========================unDoneTaskRestore: " + (System.currentTimeMillis() - startTime));
	}

	private static void cronTaskRestore() {
		logger.info("=========================cronTaskRestore");
		List<DefTask> cronDefTaskList = ServiceBeanFactory.getDefTaskService().getAllValidCronTask();
		if (cronDefTaskList == null) {
			return;
		}
		for (DefTask cronDefTask : cronDefTaskList) {
			DefTaskCron cronTask = null;
			try {
				cronTask = (DefTaskCron) ModelConverters.defPO2BO(cronDefTask);
			} catch (Exception e) {
				logger.error("po2bo error: " + cronDefTask, e);
				continue;
			}
			if (cronTask == null) {
				continue;
			}
			try {
				QuartzScheduler.addSimpleTrigger(TaskSchedulerJob.class, cronTask.getTaskId().toString(), cronTask.getCronExp());
				Date date = new Date();
				InstanceTask lastCronInstanceTask = ServiceBeanFactory.getInstanceTaskService().getLastValidInstance(cronTask.getTaskId());
				if (lastCronInstanceTask == null) {
					continue;
				}

				CronExpression actionCronExp = InstanceTaskDependInitiator.getCronExp(cronTask.getCronExp(), cronTask.getTaskId());
				Date recentlyActionDate = CronExpUtils.getTimeBeforeN(actionCronExp, date, 1).get(0);
				Date recentlyTaskDate = DateUtils2.add(recentlyActionDate, 0, cronTask.getMoveMonth(), cronTask.getMoveDay(), cronTask.getMoveHour(), cronTask.getMoveMinute());
				recentlyTaskDate = DateUtils.addMilliseconds(recentlyTaskDate, 1);

				CronExpression taskCronExp = InstanceTaskDependInitiator.getCronExp(cronTask.getExecCronExp(), cronTask.getTaskId());
				List<Date> unHappenDateList = CronExpUtils.getTimeBetween(taskCronExp, lastCronInstanceTask.getTaskDate(), recentlyTaskDate, 0);
				if (unHappenDateList == null || unHappenDateList.size() == 0) {
					continue;
				}
				for (Date unHappenDate : unHappenDateList) {
					try {
						TaskAction.run(cronTask.getTaskId(), unHappenDate, "cron", 0, 0);
					} catch (Exception e) {
						logger.error("cronTaskRestore: " + cronTask.getTaskId() + ", " + unHappenDate, e);
					}
				}
			} catch (Exception e) {
				logger.error(cronTask.getCronExp(), e);
			}
		}
	}
}
