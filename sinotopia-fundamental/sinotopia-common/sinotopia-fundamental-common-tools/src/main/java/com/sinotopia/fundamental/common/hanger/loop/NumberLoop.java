package com.hkfs.fundamental.common.hanger.loop;

import com.hkfs.fundamental.common.hanger.Chain;

/**
 * 基于数字的循环执行器
 * Created by zhoubing on 2016/12/5.
 */
public abstract class NumberLoop extends Loop {
    protected int i;
    protected int limit;
    protected int step;
    protected OptType optType;

    @Override
    protected void init(Chain chain) {
        this.optType = getOptType(chain);
        this.i = getInitialValue(chain);
        this.limit = getLimit(chain);
        this.step = getStep(chain);
        super.init(chain);
    }

    @Override
    protected final boolean isLoop(Chain chain) {
        if (optType == OptType.LESS_THAN) {
            return i < limit;
        }
        if (optType == OptType.LESS_THAN_OR_EQUAL_TO) {
            return i <= limit;
        }
        if (optType == OptType.GREATER_THAN) {
            return i > limit;
        }
        if (optType == OptType.GREATER_THAN_OR_EQUAL_TO) {
            return i >= limit;
        }

        throw new IllegalArgumentException("Unknown opt type value "+optType);
    }

    @Override
    protected final boolean doLoop(Chain chain) {
        if (doLoop(chain, i)) {
            i += getStep(chain);
            return true;
        }
        return false;
    }

    /**
     * 获取逻辑运算符
     * @return
     */
    protected OptType getOptType(Chain chain) {
        return OptType.LESS_THAN;
    }

    /**
     * 获取循环初始值，默认0
     * @return
     */
    protected int getInitialValue(Chain chain) {
        return 0;
    }

    /**
     * 获取每次循环跳跃次数，默认1
     * @return
     */
    protected int getStep(Chain chain) {
        return 1;
    }

    /**
     * 执行循环
     * @param chain
     * @param i
     * @return
     */
    protected abstract boolean doLoop(Chain chain, int i);

    /**
     * 获取循环边界
     * @return
     */
    protected abstract int getLimit(Chain chain);
}
