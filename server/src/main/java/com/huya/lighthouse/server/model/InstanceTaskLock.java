package com.huya.lighthouse.server.model;

import java.util.concurrent.locks.ReentrantLock;

public class InstanceTaskLock {

	private InstanceTaskKey instanceTaskKey;

	private ReentrantLock lock = new ReentrantLock();

	private int activeSize = 0;

	public InstanceTaskLock(InstanceTaskKey instanceTaskKey) {
		super();
		this.instanceTaskKey = instanceTaskKey;
	}

	public InstanceTaskKey getInstanceTaskKey() {
		return instanceTaskKey;
	}

	public void setInstanceTaskKey(InstanceTaskKey instanceTaskKey) {
		this.instanceTaskKey = instanceTaskKey;
	}

	public ReentrantLock getLock() {
		return lock;
	}

	public void setLock(ReentrantLock lock) {
		this.lock = lock;
	}

	public int getActiveSize() {
		return activeSize;
	}

	public void setActiveSize(int activeSize) {
		this.activeSize = activeSize;
	}

	public int add1() {
		activeSize += 1;
		return activeSize;
	}

	public int sub1() {
		activeSize -= 1;
		return activeSize;
	}
}
