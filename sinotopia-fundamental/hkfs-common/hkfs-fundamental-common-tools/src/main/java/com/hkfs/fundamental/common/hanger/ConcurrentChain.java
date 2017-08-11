package com.hkfs.fundamental.common.hanger;

import com.hkfs.fundamental.common.async.GroupSafeRunnable;
import com.hkfs.fundamental.common.hanger.exception.ChainException;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 同步执行链条
 * Created by zhoubing on 2016/12/5.
 */
public class ConcurrentChain extends Chain {
    private ExecutorService service;
    private long timeout = 45000;
    private boolean terminated = false;

    public ConcurrentChain() {
    }

    public ConcurrentChain(ExecutorService service) {
        this.service = service;
    }

    public ExecutorService getService() {
        return service;
    }

    public void setService(ExecutorService service) {
        this.service = service;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public void execute() {
        Iterator<Map.Entry<String, Executor>> it = executors.entrySet().iterator();
        Map.Entry<String, Executor> entry = null;
        final CountDownLatch countDownLatch = new CountDownLatch(executors.size());

        while (it.hasNext()) {
            entry = it.next();
            final String name = entry.getKey();
            final Executor executor = entry.getValue();

            GroupSafeRunnable runnable = new GroupSafeRunnable() {
                @Override
                public void runSafe() {
                    if (!terminated) {
                        //执行
                        previousSuccess.set(executor.execute(ConcurrentChain.this));
                        success.set(success.get() && previousSuccess.get());
                    }
                }

                @Override
                protected void onException(Exception e) {
                    if (exceptionHandler != null
                            && exceptionHandler.isTerminated(name, e)) {
                        terminated = true;
                    }
                    success.set(false);
                    previousSuccess.set(false);

                    if (exceptionHandler != null) {
                        exceptionHandler.handle(name, e);
                    }
                }
            };

            //设置计数器
            runnable.setCountDownLatch(countDownLatch);
            //提交到线程池
            service.submit(runnable);
        }

        try {
            countDownLatch.await(getTimeout(), TimeUnit.MILLISECONDS);
        }
        catch (Exception e) {
            throw new ChainException(e);
        }
    }

    public void shutdown() {
        service.shutdown();
    }

    public List<Runnable> shutdownNow() {
        return service.shutdownNow();
    }
}
