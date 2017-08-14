package com.hkfs.fundamental.common.async;

import java.util.concurrent.ExecutorService;





/**
 * 
 * @author brucezee 2013-3-24 上午10:12:54
 */
public class AsyncTaskPoolImpl implements AsyncTaskPool {
	private ExecutorService executorService;

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	@Override
	public void sendTask(SafeRunnable runnable) {
		if (executorService == null) {
			throw new IllegalArgumentException("A executorService should be supplied.");
		}
		if (runnable != null) {
			executorService.submit(runnable);
		}
	}

	@Override
	public void sendTask(AsyncTask asyncTask) {
		if (asyncTask != null) {
			SafeRunnable runnable = asyncTask.getTask();
			if (runnable != null) {
				sendTask(runnable);
			}
		}
	}

//	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
//
//	public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
//		return threadPoolTaskExecutor;
//	}
//
//	public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
//		this.threadPoolTaskExecutor = threadPoolTaskExecutor;
//	}
//
//	@Override
//	public void sendTask(SafeRunnable runnable) {
//		if (threadPoolTaskExecutor == null) {
//			throw new IllegalArgumentException("A executorService should be supplied.");
//		}
//		if (runnable != null) {
//			threadPoolTaskExecutor.submit(runnable);
//		}
//	}
//
//	@Override
//	public void sendTask(AsyncTask asyncTask) {
//		if (asyncTask != null) {
//			SafeRunnable runnable = asyncTask.getTask();
//			if (runnable != null) {
//				sendTask(runnable);
//			}
//		}
//	}
}
