package com.hkfs.fundamental.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * <p>输入注释</p>
 *
 * @Author dzr
 * @Date 2016/8/25
 */
public interface RedisLockCallback<T> {

    public void doWork(String key, ResultContainer<T> resultContainer, String oldData, Transaction tx, Jedis jedis);
}
