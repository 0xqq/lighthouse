package com.huya.lighthouse.server.executor;

import java.util.Date;

import com.huya.lighthouse.server.factory.InstanceTaskLockFactory;
import com.huya.lighthouse.server.model.InstanceTaskKey;
import com.huya.lighthouse.server.model.InstanceTaskLock;

public abstract class InstanceTaskLockExecutor {

	private Integer taskId;
	private Date taskDate;

	public InstanceTaskLockExecutor(Integer taskId, Date taskDate) {
		this.taskId = taskId;
		this.taskDate = taskDate;
	}

	public void exec() throws Exception {
		InstanceTaskKey instanceTaskKey = new InstanceTaskKey(taskId, taskDate);
		InstanceTaskLock instanceTaskLock = InstanceTaskLockFactory.borrowLock(instanceTaskKey);
		instanceTaskLock.getLock().lock();
		try {
			handle();
		} finally {
			InstanceTaskLockFactory.returnLock(instanceTaskLock);
		}
	}

	protected abstract void handle() throws Exception;
}
