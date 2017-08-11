package com.hkfs.fundamental.common.hanger.loop;

import com.hkfs.fundamental.common.hanger.Chain;

/**
 * 基于容器的循环执行器
 * Created by zhoubing on 2016/12/7.
 */
public abstract class BoxLoop<T> extends NumberLoop {
    protected SeqType seqType;

    @Override
    protected void init(Chain chain) {
        this.seqType = getSeqType(chain);
        super.init(chain);
    }

    @Override
    protected final boolean doLoop(Chain chain, int i) {
        return doLoop(chain, getObject(chain, i));
    }

    /**
     * 获取遍历类型
     * @param chain
     * @return
     */
    protected SeqType getSeqType(Chain chain) {
        return SeqType.ASC;//默认从前往后遍历
    }

    @Override
    protected final OptType getOptType(Chain chain) {
        return seqType == SeqType.DESC ? OptType.GREATER_THAN_OR_EQUAL_TO : OptType.LESS_THAN_OR_EQUAL_TO;
    }

    @Override
    protected final int getInitialValue(Chain chain) {
        return seqType == SeqType.DESC ? getSize(chain) - 1 : 0;
    }

    @Override
    protected final int getStep(Chain chain) {
        return seqType == SeqType.DESC ? -1 : 1;
    }

    @Override
    protected final int getLimit(Chain chain) {
        return seqType == SeqType.DESC ? 0 : getSize(chain) - 1;
    }

    /**
     * 获取某个索引项
     * @param i
     * @return
     */
    protected abstract T getObject(Chain chain, int i);

    /**
     * 获取数组或列表项目数量
     * @param chain
     * @return
     */
    protected abstract int getSize(Chain chain);

    /**
     * 执行循环
     * @param chain
     * @param object
     * @return
     */
    protected abstract boolean doLoop(Chain chain, T object);
}
