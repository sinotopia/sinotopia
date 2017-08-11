package com.hkfs.fundamental.redis;

/**
 * <p>输入注释</p>
 *
 * @Author dzr
 * @Date 2016/8/25
 */
public class DefaultResultContainer<T> implements ResultContainer<T>{

    private T result;

    @Override
    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public T getResult() {
        return result;
    }
}
