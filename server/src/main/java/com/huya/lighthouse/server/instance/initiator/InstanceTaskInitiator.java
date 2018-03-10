package com.huya.lighthouse.server.instance.initiator;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.model.po.def.DefTask;
import com.huya.lighthouse.model.po.instance.InstanceTask;
import com.huya.lighthouse.model.po.instance.InstanceTaskDepend;
import com.huya.lighthouse.model.po.instance.InstanceTaskLog;
import com.huya.lighthouse.model.type.Status;
import com.huya.lighthouse.server.TaskAction;
import com.huya.lighthouse.server.executor.LeaderSelector;
import com.huya.lighthouse.server.factory.DefParamFactory;
import com.huya.lighthouse.server.factory.InstanceTaskCacheFactory;
import com.huya.lighthouse.server.factory.InstanceTaskLockFactory;
import com.huya.lighthouse.server.factory.ServiceBeanFactory;
import com.huya.lighthouse.server.factory.ThreadPublicFactory;
import com.huya.lighthouse.server.model.InstanceTaskKey;
import com.huya.lighthouse.server.model.InstanceTaskLock;
import com.huya.lighthouse.server.scheduler.TaskSchedulerJob;
import com.huya.lighthouse.util.DateUtils2;
import com.huya.lighthouse.util.FreemarkerUtils;

public class InstanceTaskInitiator {

	private static Logger logger = LoggerFactory.getLogger(InstanceTaskInitiator.class);

	public static InstanceTask init(Integer taskId, Date taskDate, String instanceId, Integer isSelfRun, Integer isForceRun) {
		if (!LeaderSelector.isLeader) {
			return null;
		}

		InstanceTask instanceTask = null;
		InstanceTaskKey instanceTaskKey = new InstanceTaskKey(taskId, taskDate);
		InstanceTaskLock instanceTaskLock = InstanceTaskLockFactory.borrowLock(instanceTaskKey);
		instanceTaskLock.getLock().lock();
		try {
			instanceTask = InstanceTaskCacheFactory.get(taskId, taskDate, instanceId);
			if (instanceTask != null) {
				if (instanceTask.getIsValid().intValue() != 1) {
					instanceTask = ServiceBeanFactory.getInstanceTaskService().getValidInstance(taskId, taskDate);
				}
				return instanceTask;
			}

			InstanceTask instanceTaskValid = ServiceBeanFactory.getInstanceTaskService().getValidInstance(taskId, taskDate);
			if (instanceTaskValid != null && StringUtils.equals(instanceId, TaskSchedulerJob.CRON_INSTANCE_ID)) {
				return instanceTaskValid;
			}

			if (instanceTaskValid != null) {
				killUnDoneTask(instanceTaskValid);
			}
			
			instanceTask = initSelf(taskId, taskDate, instanceId, isSelfRun, isForceRun);
		} finally {
			InstanceTaskLockFactory.returnLock(instanceTaskLock);
		}

		initNext(taskId, taskDate, instanceId, isSelfRun);
		return instanceTask;
	}

	public static InstanceTask initSelf(Integer taskId, Date taskDate, String instanceId, Integer isSelfRun, Integer isForceRun) {
		logger.info("init taskId={}, taskDate={}, instanceId={}, isSelfRun={}, isForceRun={}", new Object[] { taskId, DateUtils2.dateStr(taskDate), instanceId, isSelfRun,
				isForceRun });
		DefTask defTask = ServiceBeanFactory.getDefTaskService().getById(taskId);
		if (defTask == null) {
			logger.error("taskId={} does not exists.", taskId);
			return null;
		}

		if (defTask.getIsValid() == 0 || defTask.getOfflineTime().getTime() <= System.currentTimeMillis()) {
			logger.info("taskId={} has been invalid.", taskId);
			return null;
		}

		String instanceTaskMsg = String.format("taskId=%s, execCronExp=%s, taskDate=%s", defTask.getTaskId().toString(), defTask.getExecCronExp(), DateUtils2.dateStr(taskDate));
		CronExpression cronExp = null;
		try {
			cronExp = new CronExpression(defTask.getExecCronExp());
		} catch (Exception e) {
			logger.error(instanceTaskMsg, e);
			return null;
		}
		if (!cronExp.isSatisfiedBy(taskDate)) {
			logger.error(instanceTaskMsg + " is not satisfy.");
			return null;
		}

		InstanceTask instanceTask = initInstanceTask(defTask, taskDate, instanceId, isSelfRun, isForceRun);
		List<InstanceTaskDepend> instanceTaskPreDependList = InstanceTaskDependInitiator.initPre(taskId, taskDate, instanceId);
		ServiceBeanFactory.getInstanceTaskService().create(instanceTask, instanceTaskPreDependList);
		InstanceTaskCacheFactory.put(instanceTask);
		return instanceTask;
	}

	private static void initNext(Integer taskId, Date taskDate, String instanceId, Integer isSelfRun) {
		if (isSelfRun != 1) {
			List<InstanceTaskDepend> instanceTaskPostDependList = InstanceTaskDependInitiator.initPost(taskId, taskDate, instanceId);
			if (instanceTaskPostDependList == null) {
				return;
			}
			for (final InstanceTaskDepend instanceTaskDepend : instanceTaskPostDependList) {
				if (instanceTaskDepend == null) {
					continue;
				}
				if (!DateUtils.isSameDay(instanceTaskDepend.getTaskDate(), taskDate)) {
					continue;
				}
				ThreadPublicFactory.cachedThreadPool.submit(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.currentThread().setName("init next to: " + instanceTaskDepend.getTaskId() + ", " + instanceTaskDepend.getTaskDate() + ", " + instanceTaskDepend.getInstanceId());
							init(instanceTaskDepend.getTaskId(), instanceTaskDepend.getTaskDate(), instanceTaskDepend.getInstanceId(), 0, 0);
						} catch (Exception e) {
							logger.error("init next to: " + instanceTaskDepend.getTaskId() + ", " + DateUtils2.dateStr(instanceTaskDepend.getTaskDate()) + ", " + instanceTaskDepend.getInstanceId(), e);
						}
					}
				});
			}
		}
	}

	private static void killUnDoneTask(InstanceTask instanceTask) {
		try {
			TaskAction.kill(instanceTask.getTaskId(), instanceTask.getTaskDate(), instanceTask.getInstanceId(), 0);
		} catch (Exception e) {
			logger.error("init kill: " + instanceTask.getTaskId() + ", " + instanceTask.getTaskDate(), e);
		}
	}

	private static InstanceTask initInstanceTask(DefTask defTask, Date taskDate, String instanceId, Integer isSelfRun, Integer isForceRun) {
		InstanceTask instanceTask = new InstanceTask();
		instanceTask.setTaskDate(taskDate);
		instanceTask.setTaskId(defTask.getTaskId());
		instanceTask.setInstanceId(instanceId);
		instanceTask.setCatalogId(defTask.getCatalogId());
		instanceTask.setTaskName(defTask.getTaskName());
		instanceTask.setTaskType(defTask.getTaskType());
		instanceTask.setTaskPlugin(defTask.getTaskPlugin());
		instanceTask.setTaskBody(defTask.getTaskBody());
		instanceTask.setExecCronExp(defTask.getExecCronExp());
		instanceTask.setAgentHostGroup(defTask.getAgentHostGroup());
		instanceTask.setLinuxRunUser(defTask.getLinuxRunUser());
		instanceTask.setQueueId(defTask.getQueueId());
		instanceTask.setPriority(defTask.getPriority());
		instanceTask.setMaxRunNum(defTask.getMaxRunNum());
		instanceTask.setMaxRunSec(defTask.getMaxRunSec());
		instanceTask.setMaxRetryNum(defTask.getMaxRetryNum());
		instanceTask.setRetryInterval(defTask.getRetryInterval());
		instanceTask.setIsIgnoreError(defTask.getIsIgnoreError());
		instanceTask.setIsOneTimes(defTask.getIsOneTimes());
		instanceTask.setOfflineTime(defTask.getOfflineTime());
		instanceTask.setStatus(Status.INIT.name());
		instanceTask.setIsSelfRun(isSelfRun);
		instanceTask.setIsForceRun(isForceRun);
		instanceTask.setRemarks(defTask.getRemarks());
		instanceTask.setIsValid(defTask.getIsValid());
		instanceTask.setCreateTime(new Date());
		instanceTask.setUpdateTime(new Date());
		instanceTask.setCreateUser(defTask.getCreateUser());
		instanceTask.setUpdateUser(defTask.getUpdateUser());

		Map<String, String> projectParamMap = DefParamFactory.getByCatalogId(instanceTask.getCatalogId());
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (projectParamMap != null) {
			paramMap.putAll(projectParamMap);
		}
		paramMap.put("tdate", taskDate);
		String taskBody = defTask.getTaskBody();
		try {
			taskBody = FreemarkerUtils.parse2Time(taskBody, paramMap);
		} catch (Exception e) {
			String errorLog = ExceptionUtils.getFullStackTrace(e);
			instanceTask.setStatus(Status.FAIL.name());
			instanceTask.setRetriedNum(defTask.getMaxRetryNum());
			InstanceTaskLog instanceTaskLog = new InstanceTaskLog(instanceTask.getTaskId(), instanceTask.getTaskDate(), instanceTask.getInstanceId(), System.currentTimeMillis());
			instanceTaskLog.setContent(errorLog);
			ServiceBeanFactory.getInstanceTaskLogService().create(instanceTaskLog);
		}
		instanceTask.setTaskBody(taskBody);

		return instanceTask;
	}

}
