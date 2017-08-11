package com.hkfs.fundamental.common.hanger.loop;

import com.hkfs.fundamental.common.hanger.Chain;
import com.hkfs.fundamental.common.hanger.Executor;

/**
 * 循环执行器
 * Created by zhoubing on 2016/12/5.
 */
public abstract class Loop implements Executor {
    @Override
    public boolean execute(Chain chain) {
        init(chain);

        while(isLoop(chain)) {
            if (!doLoop(chain)) {
                break;
            }
        }
        return true;
    }

    /**
     * 初始化
     * @param chain
     */
    protected void init(Chain chain) {
        //子类按需重写
    }

    /**
     * 判断循环条件
     * @param chain
     * @return
     */
    protected abstract boolean isLoop(Chain chain);

    /**
     * 执行循环
     * @param chain
     * @return
     */
    protected abstract boolean doLoop(Chain chain);

}
