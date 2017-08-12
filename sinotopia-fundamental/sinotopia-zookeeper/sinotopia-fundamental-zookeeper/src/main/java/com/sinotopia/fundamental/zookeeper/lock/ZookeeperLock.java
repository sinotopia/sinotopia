package com.hkfs.fundamental.zookeeper.lock;

import com.hkfs.fundamental.zookeeper.ZooKeeperClient;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 *
 * <p>基于zookeeper的分布式锁</p>
 * @Author dzr
 * @Date  2016/4/25.
 */
public class ZookeeperLock implements Lock{

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperLock.class);

    public static final long LOCK_EXPIRATION_INTERVAL_SECONDS = 30;

    /**
     * 锁定path
     */
    private String path;

    /**
     * 是否加锁
     */
    private boolean locked = false;

    private ZooKeeperClient zooKeeperClient;

    private InterProcessMutex interProcessMutex;

    public ZookeeperLock(String path, ZooKeeperClient zooKeeperClient) {
        if(zooKeeperClient == null){
            throw new RuntimeException("zooKeeperClient not allow null!");
        }
        this.path = path;
        this.zooKeeperClient = zooKeeperClient;
        interProcessMutex = new InterProcessMutex(zooKeeperClient.getClient(), path);
    }

    @Override
    public void lock() {
        try {
            if(!locked) {
                this.interProcessMutex.acquire();
                locked = true;
            }
            LOGGER.info("invoke lock method,lock path:" + this.path + " locked = " + locked);
        } catch (Exception e) {
            String message = "invoke lock method,acquire lock failed, path = " + path;
            LOGGER.error(message, e);
            throw new RuntimeException(message, e);
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        //暂时不支持
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean tryLock() {
        try {
            return tryLock(LOCK_EXPIRATION_INTERVAL_SECONDS,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        try {
            if(!locked) {
                locked = this.interProcessMutex.acquire(time, unit);
            }
            LOGGER.info("invoke tryLock method,lock path:" + this.path + " locked = " + locked);
        } catch (Exception e) {
            // 执行错误，抛出异常
            String message = "invoke tryLock method,acquire lock with time failed, path = " + path;
            LOGGER.error(message, e);
            throw new RuntimeException(message, e);
        }
        // 获取锁失败，抛出异常
        if (!locked) {
            String message = "invoke tryLock method,acquire lock with time failed, path = " + path +" locked = " + locked;
            LOGGER.error(message);
            throw new RuntimeException(message);
        }
        return locked;
    }

    @Override
    public void unlock() {
        // 如果锁定成功，则解锁
        if (locked) {
            try {
                interProcessMutex.release();
                locked = false;
                LOGGER.info("invoke unlock method,lock path:"+this.path +" success !");
            } catch (Exception e) {
                String message = "invoke unlock method,release lock failed, path = " + path;
                LOGGER.error(message, e);
                throw new RuntimeException(message, e);
            }
        }
    }

    @Override
    public Condition newCondition() {
        //暂时不支持
        throw new UnsupportedOperationException();
    }

    public ZooKeeperClient getZooKeeperClient() {
        return zooKeeperClient;
    }

    public void setZooKeeperClient(ZooKeeperClient zooKeeperClient) {
        this.zooKeeperClient = zooKeeperClient;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
