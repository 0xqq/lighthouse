package com.huya.lighthouse.server.factory;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.server.model.InstanceTaskKey;
import com.huya.lighthouse.server.model.InstanceTaskLock;

public class InstanceTaskLockFactory {

	private static Logger logger = LoggerFactory.getLogger(InstanceTaskLockFactory.class);

	private static Map<InstanceTaskKey, InstanceTaskLock> instanceTaskLockMap = new HashMap<InstanceTaskKey, InstanceTaskLock>();

	public static synchronized InstanceTaskLock borrowLock(InstanceTaskKey instanceTaskKey) {
		if (instanceTaskKey == null) {
			logger.error("InstanceTaskKey is null");
			return null;
		}
		InstanceTaskLock instanceTaskLock = instanceTaskLockMap.get(instanceTaskKey);
		if (instanceTaskLock == null) {
			instanceTaskLock = new InstanceTaskLock(instanceTaskKey);
			instanceTaskLockMap.put(instanceTaskKey, instanceTaskLock);
		}
		instanceTaskLock.add1();
		return instanceTaskLock;
	}

	public static synchronized void returnLock(InstanceTaskLock instanceTaskLock) {
		if (instanceTaskLock == null) {
			logger.error("InstanceTaskLock is null");
			return;
		}
		instanceTaskLock.getLock().unlock();
		if (instanceTaskLock.sub1() == 0) {
			instanceTaskLockMap.remove(instanceTaskLock.getInstanceTaskKey());
		}
	}
	
	public static synchronized void clear() {
		instanceTaskLockMap.clear();
	}
}
