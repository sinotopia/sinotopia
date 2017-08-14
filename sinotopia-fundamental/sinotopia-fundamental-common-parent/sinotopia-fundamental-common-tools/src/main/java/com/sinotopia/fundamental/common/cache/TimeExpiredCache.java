package com.sinotopia.fundamental.common.cache;

import com.sinotopia.fundamental.common.utils.TimeUtils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public abstract class TimeExpiredCache<T> {
    private long expiredTime;
    private long defaultCachedMillis = TimeUtils.MILLIS_OF_MINUTE * 5;//默认5分钟

    /**
     * 构造一个默认指定过期的时间分钟数的缓存对象
     */
    public TimeExpiredCache(int defaultCachedMinutes) {
        if (defaultCachedMinutes <= 0) {
            throw new IllegalArgumentException("illegal default cached miniutes value : " + defaultCachedMinutes);
        }
        this.defaultCachedMillis = TimeUtils.MILLIS_OF_MINUTE * defaultCachedMinutes;
    }

    /**
     * 构造一个默认5分钟过期的时间缓存对象
     */
    public TimeExpiredCache() {
    }

    private final Lock lock = new ReentrantLock();

    /**
     * 判断缓存的数据对象是否已经过期
     *
     * @return 过期返回true，否则返回false。
     */
    public boolean isExpired() {
        if (this.expiredTime > 0) {
            return System.currentTimeMillis() > this.expiredTime;
        }
        return true;
    }

    private T data;

    /**
     * 设置缓存的数据对象，并设置缓存的毫秒数
     *
     * @param data         需要数据对象
     * @param cachedMillis 缓存毫秒数
     */
    public void setData(T data, long cachedMillis) {
        try {
            lock.lock();
            this.expiredTime = System.currentTimeMillis() + cachedMillis;
            this.data = data;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 设置缓存的数据对象，缓存时间为默认
     *
     * @param data 需要数据对象
     */
    public void setData(T data) {
        try {
            lock.lock();
            this.data = data;
            this.expiredTime = System.currentTimeMillis() + defaultCachedMillis;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取缓存的对象
     *
     * @return 缓存的对象，如果对象已过期则重新获取数据。
     */
    public T getData() {
        try {
            lock.lock();
            if (isExpired()) {
                return refresh();
            }
            return this.data;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 设置默认的缓存时间，单位（分）
     *
     * @param defaultCacheMinutes 缓存时间（分）
     */
    public void setDefaultCacheMinutes(int defaultCacheMinutes) {
        this.defaultCachedMillis = TimeUtils.MILLIS_OF_MINUTE * defaultCacheMinutes;
    }

    /**
     * 设置默认的缓存时间，单位（毫秒）
     *
     * @param defaultCachedMillis 缓存时间（毫秒）
     */
    public void setDefaultCachedMillis(long defaultCachedMillis) {
        this.defaultCachedMillis = defaultCachedMillis;
    }

    /**
     * 设置默认的缓存时间，单位（小时）
     *
     * @param defaultCachedHour 缓存时间（小时）
     */
    public void setDefaultCachedHour(long defaultCachedHour) {
        this.defaultCachedMillis = TimeUtils.MILLIS_OF_HOUR * defaultCachedHour;
    }

    /**
     * 重新获取数据并缓存
     *
     * @return 新获取的数据
     */
    private T refresh() {
        T data = renewData();
        if (data != null) {
            setData(data);
        }
        return data;
    }

    /**
     * 清除缓存
     */
    public void clear() {
        //这里设置一下过期时间，使得在调用isExprired()方法时返回true，结果会从新renewData获取数据
//		setData(null);//不用
        this.data = null;
        this.expiredTime = System.currentTimeMillis() - 10000;
    }

    /**
     * 更新数据，子类可以重写该方法以实现当数据过期是重新获取数据
     */
    public abstract T renewData();
}
