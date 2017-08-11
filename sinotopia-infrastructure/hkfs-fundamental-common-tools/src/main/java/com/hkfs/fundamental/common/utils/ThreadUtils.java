package com.hkfs.fundamental.common.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadUtils {
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ThreadPoolExecutor newThreadPoolExecutor(
			int corePoolSize,
			int maxPoolSize,
			int workQueueSize,
			long keepAliveTimeMillis) {
		BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(workQueueSize);
		RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
		return new ThreadPoolExecutor(
				corePoolSize,
				maxPoolSize,
				keepAliveTimeMillis, 
				TimeUnit.MILLISECONDS, 
				workQueue,
				rejectedExecutionHandler);
	}
	
	public static ThreadPoolExecutor newThreadPoolExecutor() {
		return newThreadPoolExecutor(5, 10, 100, 1000);
	}
}
