package com.sinotopia.fundamental.common.async;

import java.util.concurrent.CountDownLatch;

/**
 */
public abstract class GroupSafeRunnable extends SafeRunnable {

    private CountDownLatch countDownLatch;

    @Override
    protected void onFinish() {
        super.onFinish();
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }
}
