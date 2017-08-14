package com.sinotopia.fundamental.common.hanger.loop;

import com.sinotopia.fundamental.common.hanger.Chain;

import java.util.List;

/**
 * 基于列表的循环执行器
 * Created by zhoubing on 2016/12/5.
 */
public abstract class ListLoop<T> extends BoxLoop<T> {
    protected List<T> list;

    @Override
    protected final void init(Chain chain) {
        this.list = getList(chain);
        super.init(chain);
    }

    @Override
    protected final T getObject(Chain chain, int i) {
        return list != null ? list.get(i) : null;
    }

    @Override
    protected final int getSize(Chain chain) {
        return list != null ? list.size() : 0;
    }

    /**
     * 获取列表
     * @param chain
     * @return
     */
    protected abstract List<T> getList(Chain chain);
}
