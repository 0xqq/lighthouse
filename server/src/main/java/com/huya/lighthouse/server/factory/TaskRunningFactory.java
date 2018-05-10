package com.huya.lighthouse.server.factory;

import com.huya.lighthouse.model.po.instance.InstanceTask;
import com.huya.lighthouse.server.executor.InstanceTaskExecutor;
import com.huya.lighthouse.server.model.InstanceTaskKeyDetail;
import com.huya.lighthouse.server.model.TaskRunInfo;
import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

public class TaskRunningFactory {

	protected static Logger logger = LoggerFactory.getLogger(TaskRunningFactory.class);

	private static ConcurrentHashMap<Integer, TaskRunInfo> taskRunInfoMap = new ConcurrentHashMap<Integer, TaskRunInfo>();
	
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
		TaskRunInfo taskRunInfo = taskRunInfoMap.get(taskId);
		if (taskRunInfo == null) {
			taskRunInfo = new TaskRunInfo();
			taskRunInfoMap.put(taskId, taskRunInfo);
		}

		if (taskRunInfo.isSubmitted(instanceTaskKeyDetail)) {
			return true;
		}
		if (taskRunInfo.getRunningSize() >= instanceTask.getMaxRunNum()) {
			taskRunInfo.putQueue(instanceTaskExecutor);
			return true;
		} else {
			taskRunInfo.addRunning(instanceTaskKeyDetail);
			return false;
		}
	}

	public static synchronized void delInstatnceTask(InstanceTaskKeyDetail instanceTaskKeyDetail) {
		Integer taskId = instanceTaskKeyDetail.getTaskId();
		TaskRunInfo taskRunInfo = taskRunInfoMap.get(taskId);
		if (taskRunInfo == null) {
			logger.error("taskRunInfo is null for instanceTaskKeyDetail={}", instanceTaskKeyDetail);
			return;
		}

		if (taskRunInfo.removeRunning(instanceTaskKeyDetail)) {
			InstanceTaskExecutor instanceTaskExecutor = taskRunInfo.pollQueue();
			while (instanceTaskExecutor != null && instanceTaskExecutor.getInstanceTask() != null && (instanceTaskExecutor.getInstanceTask().getIsValid() == null || instanceTaskExecutor.getInstanceTask().getIsValid() == 0)) {
                instanceTaskExecutor = taskRunInfo.pollQueue();
            }
            if (instanceTaskExecutor != null) {
                submit(instanceTaskExecutor);
            }
		}
		if (taskRunInfo.isEmpty()) {
			taskRunInfoMap.remove(taskId);
		}
	}

	public static synchronized void submit(InstanceTaskExecutor instanceTaskExecutor) {
	    if (addInstatnceTask(instanceTaskExecutor)) {
	        return;
        }
        InstanceTask instanceTask = instanceTaskExecutor.getInstanceTask();
        ThreadPoolExecutor threadPoolExecutor = ThreadPublicFactory.getThreadPoolExecutor(instanceTask.getQueueId());
        threadPoolExecutor.submit(instanceTaskExecutor);
	}

	public static synchronized void clear() {
		taskRunInfoMap.clear();
	}

	public static synchronized ConcurrentHashMap<Integer, TaskRunInfo> cloneMap() {
		return (ConcurrentHashMap<Integer, TaskRunInfo>) SerializationUtils.clone(taskRunInfoMap);
	}
}
