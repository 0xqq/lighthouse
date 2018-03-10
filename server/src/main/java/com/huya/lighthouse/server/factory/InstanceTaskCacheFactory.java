package com.huya.lighthouse.server.factory;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.huya.lighthouse.model.po.instance.InstanceTask;
import com.huya.lighthouse.server.model.InstanceTaskKeyDetail;

public class InstanceTaskCacheFactory {

	private static Cache<InstanceTaskKeyDetail, InstanceTask> cache = CacheBuilder.newBuilder().maximumSize(100000).expireAfterAccess(2, TimeUnit.DAYS).expireAfterWrite(2, TimeUnit.DAYS).build();

	public static InstanceTask get(Integer taskId, Date taskDate, String instanceId) {
		InstanceTaskKeyDetail instanceTaskKeyDetail = new InstanceTaskKeyDetail(taskId, taskDate, instanceId);
		return get(instanceTaskKeyDetail);
	}
	
	public static InstanceTask get(InstanceTaskKeyDetail instanceTaskKeyDetail) {
		InstanceTask instanceTask = cache.getIfPresent(instanceTaskKeyDetail);
		if (instanceTask == null) {
			instanceTask = ServiceBeanFactory.getInstanceTaskService().getById(instanceTaskKeyDetail.getTaskId(), instanceTaskKeyDetail.getTaskDate(), instanceTaskKeyDetail.getInstanceId());
			if (instanceTask != null) {
				put(instanceTask);
			}
		}
		return instanceTask;
	}

	public static void put(InstanceTask instanceTask) {
		InstanceTaskKeyDetail instanceTaskKeyDetail = new InstanceTaskKeyDetail(instanceTask.getTaskId(), instanceTask.getTaskDate(), instanceTask.getInstanceId());
		cache.put(instanceTaskKeyDetail, instanceTask);
	}
	
	public static void remove(InstanceTaskKeyDetail instanceTaskKeyDetail) {
		cache.invalidate(instanceTaskKeyDetail);
	}
	
	public static synchronized void clear() {
		cache.cleanUp();
	}
}
