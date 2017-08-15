package com.sinotopia.fundamental.common.async;


/**
 * @author brucezee 2013-3-24 上午10:12:54
 */
public interface AsyncTaskPool {

    void sendTask(SafeRunnable runnable);

    void sendTask(AsyncTask asyncTask);
}
