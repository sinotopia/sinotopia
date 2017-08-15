package com.sinotopia.fundamental.common.utils;

import com.sinotopia.fundamental.common.assist.CodeMocker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadUtils {
	private static Logger logger = LoggerFactory.getLogger(CodeMocker.class);
	
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
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
