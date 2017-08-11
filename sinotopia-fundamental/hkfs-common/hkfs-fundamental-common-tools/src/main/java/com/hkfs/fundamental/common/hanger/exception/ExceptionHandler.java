package com.hkfs.fundamental.common.hanger.exception;

/**
 * 异常处理
 * Created by zhoubing on 2016/12/5.
 */
public abstract class ExceptionHandler {
    /**
     * 处理异常
     * @param name
     * @param e
     */
    public abstract void handle(String name, Exception e);

    /**
     * 判断是否
     * @param name
     * @param e
     * @return 如果需要终止全部执行，则返回true，否则返回false。
     */
    public boolean isTerminated(String name, Exception e) {
        return e instanceof ChainBreakException;//默认抛出 ChainBreakException 跳出执行链条
    }
}
