package com.hkfs.fundamental.common.async;


/**
 * 
 * @author brucezee 2013-3-24 上午10:12:54
 */
public interface AsyncTaskPool {
	public void sendTask(SafeRunnable runnable);
	public void sendTask(AsyncTask asyncTask);
}
