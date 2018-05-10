package com.huya.lighthouse.server.model;

import com.huya.lighthouse.server.executor.InstanceTaskExecutor;
import com.huya.lighthouse.util.DateUtils2;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskRunInfo implements Serializable {

    // for concurrent submit task
    private ConcurrentHashMap<InstanceTaskKeyDetail, String> submittedSet = new ConcurrentHashMap<InstanceTaskKeyDetail, String>();
    // for rerun running task
    private ConcurrentHashMap<String, AtomicInteger> runningTaskMap = new ConcurrentHashMap<String, AtomicInteger>();
    private PriorityBlockingQueue<InstanceTaskExecutor> waitingTaskQueue = new PriorityBlockingQueue<InstanceTaskExecutor>();

    public void addRunning(InstanceTaskKeyDetail instanceTaskKeyDetail) {
        submittedSet.put(instanceTaskKeyDetail, "");
        String taskDateStr = DateUtils2.dateStr(instanceTaskKeyDetail.getTaskDate());
        AtomicInteger zero = new AtomicInteger();
        AtomicInteger curVal = runningTaskMap.putIfAbsent(taskDateStr, zero);
        if (curVal == null) {
            curVal = runningTaskMap.putIfAbsent(taskDateStr, zero);
        }
        curVal.incrementAndGet();
    }

    public boolean removeRunning(InstanceTaskKeyDetail instanceTaskKeyDetail) {
        submittedSet.remove(instanceTaskKeyDetail);
        String taskDateStr = DateUtils2.dateStr(instanceTaskKeyDetail.getTaskDate());
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

    public void putQueue(InstanceTaskExecutor instanceTaskExecutor) {
        submittedSet.put(instanceTaskExecutor.getInstanceTaskKeyDetail(), "");
        waitingTaskQueue.put(instanceTaskExecutor);
    }

    public InstanceTaskExecutor pollQueue() {
        InstanceTaskExecutor instanceTaskExecutor = waitingTaskQueue.poll();
        if (instanceTaskExecutor != null && instanceTaskExecutor.getInstanceTaskKeyDetail() != null) {
            submittedSet.remove(instanceTaskExecutor.getInstanceTaskKeyDetail());
        }
        return instanceTaskExecutor;
    }

    public int getRunningSize() {
        return runningTaskMap.size();
    }

    public boolean isEmpty() {
        return runningTaskMap.isEmpty() && waitingTaskQueue.isEmpty();
    }

    public boolean isSubmitted(InstanceTaskKeyDetail instanceTaskKeyDetail) {
        return submittedSet.contains(instanceTaskKeyDetail);
    }

    public ConcurrentHashMap<String, AtomicInteger> getRunningTaskMap() {
        return runningTaskMap;
    }

    public PriorityBlockingQueue<InstanceTaskExecutor> getWaitingTaskQueue() {
        return waitingTaskQueue;
    }

    @Override
    public String toString() {
        return "runningTaskMap=" + runningTaskMap + ", waitingTaskQueueSize=" + waitingTaskQueue.size();
    }
}
