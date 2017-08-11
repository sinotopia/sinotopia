package com.hkfs.fundamental.common.async;

import com.hkfs.fundamental.common.assist.CodeMocker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author brucezee 2013-3-24 上午10:39:28
 */
public abstract class SafeRunnable implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(CodeMocker.class);
	@Override
	public void run() {
		onStart();
		try {
			runSafe();
		}
		catch (Exception e) {
			onException(e);
		}
		finally {
			onFinish();
		}
	}
	
	/**
	 * 当run方法执行开始会调用的方法
	 */
	protected void onStart() {
		//to be override
	}
	
	/**
	 * 当run方法执行完毕后会调用的方法
	 */
	protected void onFinish() {
		//to be override
	}
	
	/**
	 * 当run方法执行异常后会调用的方法
	 */
	protected void onException(Exception e) {
		//to be override
		logger.error(e.getMessage(), e);
	}
	
	public abstract void runSafe();
}
