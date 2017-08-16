package com.sinotopia.fundamental.lock;

import com.sinotopia.fundamental.lock.redis.LockService;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 *  <p>基于Redis的分布式锁</p>
 * @Author dzr
 * @Date 2016/04/27
 */
public class RedisLock1 implements Lock{

    public static final long LOCK_EXPIRATION_INTERVAL_SECONDS = 30*1000;

    private LockService lockService;

    private String lockKey;

    public void setLockKey(String lockKey) {
        this.lockKey = lockKey;
    }

    public void setLockService(LockService lockService) {
        this.lockService = lockService;
    }

    public RedisLock1(String lockKey ,LockService lockService) {
        this.lockService = lockService;
        this.lockKey = lockKey;
    }

    @Override
    public void lock() {
        boolean locked = this.lockService.lock(this.lockKey,0L,null);
        if(!locked){
            throw new RuntimeException("get lock failed!");
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean tryLock() {
        boolean locked = false;
        try {
            locked = this.tryLock(LOCK_EXPIRATION_INTERVAL_SECONDS, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return locked;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        long milliseconds = TimeUnit.MILLISECONDS.convert(time, unit);
        return this.lockService.lock(this.lockKey,milliseconds,null);
    }

    @Override
    public void unlock() {
        this.lockService.unlock(this.lockKey);
    }

    @Override
    public Condition newCondition() {
        //暂时不支持
        throw new UnsupportedOperationException();
    }
}
