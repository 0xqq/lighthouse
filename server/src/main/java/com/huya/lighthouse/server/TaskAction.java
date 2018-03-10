package com.huya.lighthouse.server;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.model.po.instance.InstanceTask;
import com.huya.lighthouse.model.type.Status;
import com.huya.lighthouse.server.executor.InstanceTaskExecutor;
import com.huya.lighthouse.server.executor.LeaderSelector;
import com.huya.lighthouse.server.factory.DefAgentGroupFactory;
import com.huya.lighthouse.server.factory.DefParamFactory;
import com.huya.lighthouse.server.factory.InstanceTaskCacheFactory;
import com.huya.lighthouse.server.factory.InstanceTaskLockFactory;
import com.huya.lighthouse.server.factory.ServiceBeanFactory;
import com.huya.lighthouse.server.factory.TaskRunningFactory;
import com.huya.lighthouse.server.factory.ThreadPublicFactory;
import com.huya.lighthouse.server.instance.initiator.InstanceTaskInitiator;
import com.huya.lighthouse.server.model.InstanceTaskKey;
import com.huya.lighthouse.server.model.InstanceTaskKeyDetail;
import com.huya.lighthouse.server.model.InstanceTaskLock;
import com.huya.lighthouse.server.scheduler.DefMonitorBeginJob;
import com.huya.lighthouse.server.scheduler.QuartzScheduler;
import com.huya.lighthouse.server.scheduler.TaskSchedulerJob;
import com.huya.lighthouse.util.DateUtils2;

public class TaskAction {

	private static Logger logger = LoggerFactory.getLogger(TaskAction.class);

	public static void run(Integer taskId, Date taskDate, String instanceId, Integer isSelfRun, Integer isForceRun) throws Exception {
		if (!LeaderSelector.isLeader) {
			return;
		}
		logger.info("run: {}, {}, {}, {}, {}", new Object[] { taskId, DateUtils2.dateStr(taskDate), instanceId, isSelfRun, isForceRun });
		InstanceTaskKey instanceTaskKey = new InstanceTaskKey(taskId, taskDate);
		InstanceTaskLock instanceTaskLock = InstanceTaskLockFactory.borrowLock(instanceTaskKey);
		instanceTaskLock.getLock().lock();
		try {
			InstanceTask instanceTask = InstanceTaskInitiator.init(taskId, taskDate, instanceId, isSelfRun, isForceRun);
			if (instanceTask == null) {
				return;
			}
			//instanceId可能会变
			InstanceTaskKeyDetail instanceTaskKeyDetail = new InstanceTaskKeyDetail(taskId, taskDate, instanceTask.getInstanceId());
			InstanceTaskExecutor instanceTaskExecutor = new InstanceTaskExecutor(instanceTaskKeyDetail);
			boolean isRunnable = instanceTaskExecutor.isRunnable(instanceTask, isForceRun);
			if (isRunnable) {
				instanceTaskExecutor.setReadyStatus(instanceTask);
				TaskRunningFactory.submit(instanceTaskExecutor);
			}
		} finally {
			InstanceTaskLockFactory.returnLock(instanceTaskLock);
		}
	}

	public static void kill(Integer taskId, Date taskDate, String instanceId, Integer isValid) throws Exception {
		logger.info("kill: {}, {}, {}", new Object[] { taskId, DateUtils2.dateStr(taskDate), instanceId });
		if (!LeaderSelector.isLeader) {
			return;
		}
		InstanceTaskKey instanceTaskKey = new InstanceTaskKey(taskId, taskDate);
		InstanceTaskLock instanceTaskLock = InstanceTaskLockFactory.borrowLock(instanceTaskKey);
		instanceTaskLock.getLock().lock();
		try {
			InstanceTask instanceTask = InstanceTaskCacheFactory.get(taskId, taskDate, instanceId);
			boolean result = true;
			if (instanceTask == null) {
				logger.info("kill null: {}, {}, {}", new Object[] { taskId, DateUtils2.dateStr(taskDate), instanceId });
				return;
			}
			if (instanceTask.getIsValid().intValue() == 0) {
				logger.info("kill invalid: {}, {}, {}", new Object[] { taskId, DateUtils2.dateStr(taskDate), instanceId });
				return;
			}
			if (InstanceTaskExecutor.isDone(instanceTask)) {
				return;
			}
			if (InstanceTaskExecutor.isRunning(instanceTask)) {
				result = InstanceTaskExecutor.kill(instanceTask);
			}
			if (result) {
				InstanceTaskExecutor.setEndStatus(instanceTask, Status.KILL.name(), isValid);
			}
		} finally {
			InstanceTaskLockFactory.returnLock(instanceTaskLock);
		}
	}

	public static void setSuccess(Integer taskId, Date taskDate, String instanceId) throws Exception {
		logger.info("setSuccess: {}, {}, {}", new Object[] { taskId, DateUtils2.dateStr(taskDate), instanceId });
		if (!LeaderSelector.isLeader) {
			return;
		}
		InstanceTaskKey instanceTaskKey = new InstanceTaskKey(taskId, taskDate);
		InstanceTaskLock instanceTaskLock = InstanceTaskLockFactory.borrowLock(instanceTaskKey);
		instanceTaskLock.getLock().lock();
		InstanceTask instanceTask = InstanceTaskCacheFactory.get(taskId, taskDate, instanceId);
		try {
			if (instanceTask == null) {
				return;
			}
			if (instanceTask.getIsValid().intValue() == 0 || StringUtils.equals(instanceTask.getStatus(), Status.SUCCESS.name())) {
				return;
			}
			InstanceTaskExecutor.setEndStatus(instanceTask, Status.SUCCESS.name(), 1);
		} finally {
			InstanceTaskLockFactory.returnLock(instanceTaskLock);
		}
		InstanceTaskExecutor.doNext(instanceTask);
	}

	public static void rebuildInit(Integer taskId) throws Exception {
		logger.info("rebuildInit: " + taskId);
		if (!LeaderSelector.isLeader) {
			return;
		}
		List<InstanceTask> instanceTaskList = ServiceBeanFactory.getInstanceTaskService().getValidInitInstance(taskId);
		if (instanceTaskList == null) {
			return;
		}
		for (InstanceTask sInstanceTask : instanceTaskList) {
			InstanceTaskKey instanceTaskKey = new InstanceTaskKey(taskId, sInstanceTask.getTaskDate());
			InstanceTaskLock instanceTaskLock = InstanceTaskLockFactory.borrowLock(instanceTaskKey);
			instanceTaskLock.getLock().lock();
			try {
				InstanceTask instanceTask = InstanceTaskCacheFactory.get(taskId, sInstanceTask.getTaskDate(), sInstanceTask.getInstanceId());
				if (instanceTask.getIsValid().intValue() == 1 && StringUtils.equals(instanceTask.getStatus(), Status.INIT.name())) {
					instanceTask = InstanceTaskInitiator.initSelf(taskId, instanceTask.getTaskDate(), instanceTask.getInstanceId(), instanceTask.getIsSelfRun(), instanceTask.getIsForceRun());
					//改后，满足条件运行，可以马上跑
					if (instanceTask != null) {
						TaskAction.run(instanceTask.getTaskId(), instanceTask.getTaskDate(), instanceTask.getInstanceId(), instanceTask.getIsSelfRun(), instanceTask.getIsForceRun());
					}
				}
			} finally {
				InstanceTaskLockFactory.returnLock(instanceTaskLock);
			}
		}
	}

	public static void invalidInit(Integer taskId) throws Exception {
		logger.info("invalidInit: " + taskId);
		if (!LeaderSelector.isLeader) {
			return;
		}
		List<InstanceTask> instanceTaskList = ServiceBeanFactory.getInstanceTaskService().getValidInitInstance(taskId);
		if (instanceTaskList == null) {
			return;
		}
		for (InstanceTask sInstanceTask : instanceTaskList) {
			InstanceTaskKey instanceTaskKey = new InstanceTaskKey(taskId, sInstanceTask.getTaskDate());
			InstanceTaskLock instanceTaskLock = InstanceTaskLockFactory.borrowLock(instanceTaskKey);
			instanceTaskLock.getLock().lock();
			try {
				InstanceTask instanceTask = InstanceTaskCacheFactory.get(taskId, sInstanceTask.getTaskDate(), sInstanceTask.getInstanceId());
				if (instanceTask.getIsValid().intValue() == 1 && StringUtils.equals(instanceTask.getStatus(), Status.INIT.name())) {
					ServiceBeanFactory.getInstanceTaskService().updateInvalid(taskId, instanceTask.getTaskDate());
					instanceTask.setIsValid(0);
				}
			} finally {
				InstanceTaskLockFactory.returnLock(instanceTaskLock);
			}
		}
	}

	public static void addCronTask(Integer taskId, String cronExp) throws Exception {
		logger.info("addCronTask: {}, {}", taskId, cronExp);
		if (!LeaderSelector.isLeader) {
			return;
		}
		QuartzScheduler.addSimpleTrigger(TaskSchedulerJob.class, taskId.toString(), cronExp);
	}

	public static void addCronMonitorBegin(String cronExp) throws Exception {
		logger.info("addCronMonitorBegin: " + cronExp);
		if (!LeaderSelector.isLeader) {
			return;
		}
		QuartzScheduler.addSimpleTrigger(DefMonitorBeginJob.class, cronExp, cronExp);
	}

	public static void reBuildDefAgentGroupFactory() {
		logger.info("addCronMonitorBegin");
		if (!LeaderSelector.isLeader) {
			return;
		}
		DefAgentGroupFactory.load();
	}

	public static void reBuildDefParamFactory() {
		logger.info("reBuildDefParamFactory");
		if (!LeaderSelector.isLeader) {
			return;
		}
		DefParamFactory.load();
	}

	public static void updateQueueSize(int queueId, int queueSize) {
		logger.info("updateQueueSize: {}, {}", queueId, queueSize);
		if (!LeaderSelector.isLeader) {
			return;
		}
		ThreadPublicFactory.updateQueueSize(queueId, queueSize);
	}
}
