package com.hkfs.fundamental.common.hanger.loop;

import com.hkfs.fundamental.common.hanger.Chain;

/**
 * 基于数组的循环执行器
 * Created by zhoubing on 2016/12/5.
 */
public abstract class ArrayLoop<T> extends BoxLoop<T> {
    protected T[] array;

    @Override
    protected final void init(Chain chain) {
        this.array = getArray(chain);
        super.init(chain);
    }

    @Override
    protected final T getObject(Chain chain, int i) {
        return array != null ? array[i] : null;
    }

    @Override
    protected final int getSize(Chain chain) {
        return array != null ? array.length : 0;
    }

    /**
     * 获取数组
     * @param chain
     * @return
     */
    protected abstract T[] getArray(Chain chain);
}
