package com.huya.lighthouse.server.factory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.model.po.instance.InstanceTask;
import com.huya.lighthouse.server.executor.InstanceTaskExecutor;
import com.huya.lighthouse.server.model.InstanceTaskKeyDetail;
import com.huya.lighthouse.util.DateUtils2;

public class TaskRunningFactory {

	protected static Logger logger = LoggerFactory.getLogger(TaskRunningFactory.class);

	private static Map<Integer, TaskRunInfo> taskRunInfoMap = new HashMap<Integer, TaskRunInfo>();

	public static synchronized boolean addInstatnceTask(InstanceTaskExecutor instanceTaskExecutor) {
		if (instanceTaskExecutor == null) {
			logger.error("instanceTaskExecutor cannot be null");
			return true;
		}
		InstanceTaskKeyDetail instanceTaskKeyDetail = instanceTaskExecutor.getInstanceTaskKeyDetail();
		InstanceTask instanceTask = instanceTaskExecutor.getInstanceTask();
		if (instanceTask == null) {
			logger.error("instanceTask is null for " + instanceTaskKeyDetail);
			return true;
		}
		Integer taskId = instanceTaskKeyDetail.getTaskId();
		Date taskDate = instanceTaskKeyDetail.getTaskDate();
		TaskRunInfo taskRunInfo = taskRunInfoMap.get(taskId);
		if (taskRunInfo == null) {
			taskRunInfo = new TaskRunInfo();
			taskRunInfoMap.put(taskId, taskRunInfo);
		}
		if (taskRunInfo.getRunningSize() >= instanceTask.getMaxRunNum()) {
			taskRunInfo.getWaitingTaskQueue().put(instanceTaskExecutor);
			return true;
		} else {
			taskRunInfo.addRunning(taskDate);
			return false;
		}
	}

	public static synchronized void delInstatnceTask(Integer taskId, Date taskDate) {
		TaskRunInfo taskRunInfo = taskRunInfoMap.get(taskId);
		if (taskRunInfo == null) {
			logger.error("taskRunInfo is null for taskId={}, taskDate={}", taskId, DateUtils2.dateStr(taskDate));
			return;
		}

		if (taskRunInfo.removeRunning(taskDate)) {
			InstanceTaskExecutor instanceTaskExecutor = taskRunInfo.getWaitingTaskQueue().poll();
			if (instanceTaskExecutor != null) {
				InstanceTask instanceTask = instanceTaskExecutor.getInstanceTask();
				if (instanceTask != null) {
					ThreadPoolExecutor threadPoolExecutor = ThreadPublicFactory.getThreadPoolExecutor(instanceTask.getQueueId());
					threadPoolExecutor.submit(instanceTaskExecutor);
				} else {
					logger.error("instanceTask is null for taskId=" + taskId);
				}
			}
		}
		if (taskRunInfo.isEmpty()) {
			taskRunInfoMap.remove(taskId);
		}
	}

	public static synchronized void submit(InstanceTaskExecutor instanceTaskExecutor) {
		if (instanceTaskExecutor == null) {
			logger.error("instanceTaskExecutor cannot be null");
			return;
		}
		InstanceTaskKeyDetail instanceTaskKeyDetail = instanceTaskExecutor.getInstanceTaskKeyDetail();
		InstanceTask instanceTask = instanceTaskExecutor.getInstanceTask();
		if (instanceTask == null) {
			logger.error("instanceTask is null for " + instanceTaskKeyDetail);
			return;
		}
		Integer taskId = instanceTaskKeyDetail.getTaskId();
		TaskRunInfo taskRunInfo = taskRunInfoMap.get(taskId);
		if (taskRunInfo == null) {
			ThreadPoolExecutor threadPoolExecutor = ThreadPublicFactory.getThreadPoolExecutor(instanceTask.getQueueId());
			threadPoolExecutor.submit(instanceTaskExecutor);
			return;
		}
		if (taskRunInfo.getRunningSize() >= instanceTask.getMaxRunNum()) {
			taskRunInfo.getWaitingTaskQueue().put(instanceTaskExecutor);
		} else {
			ThreadPoolExecutor threadPoolExecutor = ThreadPublicFactory.getThreadPoolExecutor(instanceTask.getQueueId());
			threadPoolExecutor.submit(instanceTaskExecutor);
		}
	}

	public static synchronized void clear() {
		taskRunInfoMap.clear();
	}
}

class TaskRunInfo {

	// for rerun running task
	private ConcurrentHashMap<String, AtomicInteger> runningTaskMap = new ConcurrentHashMap<String, AtomicInteger>();
	private PriorityBlockingQueue<InstanceTaskExecutor> waitingTaskQueue = new PriorityBlockingQueue<InstanceTaskExecutor>();

	public void addRunning(Date taskDate) {
		String taskDateStr = DateUtils2.dateStr(taskDate);
		AtomicInteger zero = new AtomicInteger();
		AtomicInteger curVal = runningTaskMap.putIfAbsent(taskDateStr, zero);
		if (curVal == null) {
			curVal = runningTaskMap.putIfAbsent(taskDateStr, zero);
		}
		curVal.incrementAndGet();
	}
	
	public boolean removeRunning(Date taskDate) {
		String taskDateStr = DateUtils2.dateStr(taskDate);
		AtomicInteger zero = new AtomicInteger();
		AtomicInteger curVal = runningTaskMap.putIfAbsent(taskDateStr, zero);
		if (curVal == null) {
			curVal = runningTaskMap.putIfAbsent(taskDateStr, zero);
		}
		int result = curVal.decrementAndGet();
		if (result <= 0) {
			runningTaskMap.remove(taskDateStr);
			return true;
		}
		return false;
	}
	
	public int getRunningSize() {
		return runningTaskMap.size();
	}

	public PriorityBlockingQueue<InstanceTaskExecutor> getWaitingTaskQueue() {
		return waitingTaskQueue;
	}

	public boolean isEmpty() {
		return runningTaskMap.isEmpty() && waitingTaskQueue.isEmpty();
	}

	@Override
	public String toString() {
		return "runningTaskMap=" + runningTaskMap + ", waitingTaskQueueSize=" + waitingTaskQueue.size();
	}
}
