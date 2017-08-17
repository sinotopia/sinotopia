package com.sinotopia.fundamental.common.async;


/**
 */
public interface AsyncTaskPool {

    void sendTask(SafeRunnable runnable);

    void sendTask(AsyncTask asyncTask);
}
