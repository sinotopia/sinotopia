package com.hkfs.fundamental.redis;

/**
 * 结果容器。
 * @Author dzr
 * @Date 2016/04/25
 * @param <T> 结果类型。
 */
public interface ResultContainer<T> {

    /**
     * 向容器中设置结果对象。
     * @param result 结果对象。
     */
    public void setResult(T result);

    /**
     * 从容器中获取结果。
     * @return 容器中的结果对象。
     */
    public T getResult();

}
