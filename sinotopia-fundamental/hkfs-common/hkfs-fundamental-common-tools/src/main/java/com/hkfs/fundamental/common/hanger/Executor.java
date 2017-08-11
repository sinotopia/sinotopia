package com.hkfs.fundamental.common.hanger;

/**
 * 执行器
 * Created by zhoubing on 2016/12/5.
 */
public interface Executor {
    /**
     * 执行过程
     * @param chain
     * @return
     */
    public boolean execute(Chain chain);
}
