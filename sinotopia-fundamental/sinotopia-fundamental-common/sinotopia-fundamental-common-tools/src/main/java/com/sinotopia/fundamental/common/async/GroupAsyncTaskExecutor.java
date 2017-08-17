package com.sinotopia.fundamental.common.async;

import com.sinotopia.fundamental.common.assist.CodeMocker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 */
public class GroupAsyncTaskExecutor {

    private static Logger logger = LoggerFactory.getLogger(CodeMocker.class);

    private static final long TIMEOUT_MILLISECONDS = 15000;

    private List<GroupSafeRunnable> taskList = new LinkedList<GroupSafeRunnable>();

    private long timeout = 0;

    private AsyncTaskPool asyncTaskPool;

    public GroupAsyncTaskExecutor(AsyncTaskPool asyncTaskPool) {
        this.asyncTaskPool = asyncTaskPool;
    }

    public void addTask(GroupSafeRunnable task) {
        if (task != null) {
            taskList.add(task);
        }
    }

    public long getTimeout() {
        return timeout != 0 ? timeout : TIMEOUT_MILLISECONDS;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public void start() {
        if (taskList.size() > 0) {
            CountDownLatch countDownLatch = new CountDownLatch(taskList.size());
            for (GroupSafeRunnable task : taskList) {
                //注入
                task.setCountDownLatch(countDownLatch);
                sendAsyncTask(task);
            }
            try {
                countDownLatch.await(getTimeout(), TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    protected void sendAsyncTask(SafeRunnable runnable) {
        if (asyncTaskPool != null) {
            try {
                asyncTaskPool.sendTask(runnable);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
