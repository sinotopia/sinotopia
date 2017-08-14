package com.sinotopia.fundamental.common.async;

import java.util.concurrent.CountDownLatch;

/**
 * 
 * @author brucezee 2013-5-25 下午1:51:10
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
