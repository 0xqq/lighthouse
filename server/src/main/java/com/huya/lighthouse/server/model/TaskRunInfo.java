package com.huya.lighthouse.server.model;

import com.huya.lighthouse.server.executor.InstanceTaskExecutor;
import com.huya.lighthouse.util.DateUtils2;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskRunInfo implements Serializable {

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

    public ConcurrentHashMap<String, AtomicInteger> getRunningTaskMap() {
        return runningTaskMap;
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
