package com.huya.lighthouse.server.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 使FutureTask实现Comparable，解决ThreadPoolExecutor的PriorityBlockingQueue支持问题
 * http://my.oschina.net/canghailan/blog/37006
 * 
 * @author chenwu
 * 
 */
public class XThreadPoolExecutor extends ThreadPoolExecutor {

	public XThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
	}

	@Override
	protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
		return new ComparableFutureTask<T>(callable);
	}

	protected class ComparableFutureTask<T> extends FutureTask<T> implements Comparable<ComparableFutureTask<T>> {
		private Object object;

		public ComparableFutureTask(Callable<T> callable) {
			super(callable);
			object = callable;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public int compareTo(ComparableFutureTask<T> o) {
			if (this == o) {
				return 0;
			}
			if (o == null) {
				return -1;
			}
			if (object != null && o.object != null) {
				if (object.getClass().equals(o.object.getClass())) {
					if (object instanceof Comparable) {
						return ((Comparable) object).compareTo(o.object);
					}
				}
			}
			return 0;
		}
	}
}
