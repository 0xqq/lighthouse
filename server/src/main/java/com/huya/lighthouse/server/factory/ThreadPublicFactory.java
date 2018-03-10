package com.huya.lighthouse.server.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.model.po.def.DefQueue;
import com.huya.lighthouse.server.util.XThreadPoolExecutor;

public class ThreadPublicFactory {

	private static Logger logger = LoggerFactory.getLogger(ThreadPublicFactory.class);

	private static Map<Integer, ThreadPoolExecutor> threadPoolMap = new HashMap<Integer, ThreadPoolExecutor>();

	public static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

	public static ThreadPoolExecutor getThreadPoolExecutor(Integer queueId) {
		ThreadPoolExecutor threadPoolExecutor = threadPoolMap.get(queueId);
		if (threadPoolExecutor == null) {
			threadPoolExecutor = createThreadPoolExecutor(queueId);
		}
		return threadPoolExecutor;
	}

	private static synchronized ThreadPoolExecutor createThreadPoolExecutor(Integer queueId) {
		ThreadPoolExecutor threadPoolExecutor = threadPoolMap.get(queueId);
		if (threadPoolExecutor == null) {
			DefQueue defQueue = ServiceBeanFactory.getDefQueueService().getById(queueId);
			if (defQueue == null) {
				logger.error("createThreadPoolExecutor null for " + queueId);
				return null;
			}
			logger.info("createThreadPoolExecutor queueId={}, queueName={}, queueSize={}", new Object[] { queueId, defQueue.getQueueName(), defQueue.getQueueSize() });
			threadPoolExecutor = new XThreadPoolExecutor(defQueue.getQueueSize(), defQueue.getQueueSize(), 30L, TimeUnit.MINUTES, new PriorityBlockingQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());
			threadPoolMap.put(queueId, threadPoolExecutor);
		}
		return threadPoolExecutor;
	}

	public static void updateQueueSize(Integer queueId, int queueSize) {
		ThreadPoolExecutor threadPoolExecutor = threadPoolMap.get(queueId);
		if (threadPoolExecutor == null) {
			return;
		}
		threadPoolExecutor.setCorePoolSize(queueSize);
		threadPoolExecutor.setMaximumPoolSize(queueSize);
	}

	public static synchronized void clear() {
		threadPoolMap.clear();
	}
}
