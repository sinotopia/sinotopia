package com.hkfs.fundamental.lock.redis;

/**
 * Created by zhoubing on 2017/1/4.
 */
public interface LockCallback {
    /**
     * 锁定成功
     * @param key 锁定的key
     * @param lockId 锁id，锁定和解锁只能是同一个lockId
     */
    public void onSuccess(String key, Object lockId);
}
