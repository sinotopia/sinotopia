package com.hkfs.fundamental.redis;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhoubing on 2016/4/8.
 */
public class RedisConnector {
    private static ShardedJedisPool shardedJedisPool;
    private static JdkSerializationRedisSerializer redisSerializer = new JdkSerializationRedisSerializer();
    private static Long MAX_EXPIRE_TIME = Long.parseLong(String.valueOf(Integer.MAX_VALUE)) * 1000;
    public static ShardedJedisPool getShardedJedisPool() {
        return shardedJedisPool;
    }

    public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
        RedisConnector.shardedJedisPool = shardedJedisPool;
    }

    public static ShardedJedis getResource() {
        return getShardedJedisPool().getResource();
    }

    public static void close(ShardedJedis jedis) {
        getShardedJedisPool().returnBrokenResource(jedis);
    }

    public static Long incrBy(String key, long integer) {
        ShardedJedis jedis = getResource();
        try{
            return jedis.incrBy(key, integer);
        }
        finally {
            close(jedis);
        }
    }

    public static boolean set(String key, String value) {
        return set(key, MAX_EXPIRE_TIME, value);
    }

    public static boolean set(String key, long milliseconds, String value) {
        ShardedJedis jedis = getResource();
        try {
            //redis默认的过期时间是seconds 即秒
            String result = jedis.setex(key, ((Long)(milliseconds/1000)).intValue(), value);
            return "OK".equals(result);
        } finally {
            close(jedis);
        }
    }

    public static boolean setObject(Object key, Object value) {
        return setObject(key, MAX_EXPIRE_TIME, value);
    }

    public static boolean setObject(Object key, long milliseconds, Object value) {
        byte[] keyBytes = serializeValue(key);
        byte[] valueBytes = serializeValue(value);
        ShardedJedis jedis = getResource();
        try {
            //redis默认的过期时间是seconds 即秒
            String result = jedis.setex(keyBytes, ((Long)(milliseconds/1000)).intValue(), valueBytes);
            return "OK".equals(result);
        } finally {
            close(jedis);
        }
    }

    public static byte[] serializeValue(Object value) {
        if (redisSerializer == null) {
            if (value instanceof byte[]) {
                return (byte[]) value;
            }
            throw new IllegalStateException("redisSerializer is not initialized.");
        }
        return redisSerializer.serialize(value);
    }
    public static Object deserializeValue(byte[] value) {
        return redisSerializer == null?value:redisSerializer.deserialize(value);
    }

    public static String get(String key) {
        ShardedJedis jedis = getResource();
        try {
            return jedis.get(key);
        } finally {
            close(jedis);
        }
    }

    public static Object getObject(Object key) {
        byte[] keyBytes = serializeValue(key);
        ShardedJedis jedis = getResource();
        try {
            byte[] valueBytes = jedis.get(keyBytes);
            if (valueBytes == null) {
                return null;
            }
            return deserializeValue(valueBytes);
        } finally {
            close(jedis);
        }
    }

    public static Long del(String key) {
        ShardedJedis jedis = getResource();
        try {
            return jedis.del(key);
        } finally {
            close(jedis);
        }
    }

    public static Long delObject(Object key) {
        byte[] keyBytes = serializeValue(key);
        ShardedJedis jedis = getResource();
        try {
            return jedis.del(keyBytes);
        } finally {
            close(jedis);
        }
    }

    public static Boolean exists(String key) {
        ShardedJedis jedis = getResource();
        try {
            return jedis.exists(key);
        } finally {
            close(jedis);
        }
    }

    public static Boolean existsObject(Object key) {
        byte[] keyBytes = serializeValue(key);
        ShardedJedis jedis = getResource();
        try {
            return jedis.exists(keyBytes);
        } finally {
            close(jedis);
        }
    }

    /**
     * 头入队列
     *
     * @param key
     * @param value
     * @return
     */
    public static Long lpush(String key, String value) {
        ShardedJedis jedis = getResource();
        try {
            return jedis.lpush(key, value);
        } finally {
            close(jedis);
        }
    }

    /**
     * 尾入队列
     *
     * @param key
     * @param value
     * @return
     */
    public static Long rpush(String key, String value) {
        ShardedJedis jedis = getResource();
        try {
            return jedis.rpush(key, value);
        } finally {
            close(jedis);
        }
    }

    /**
     * 从左出队列
     *
     * @param key
     * @return
     */
    public static String lpop(String key) {
        ShardedJedis jedis = getResource();
        try {
            return jedis.lpop(key);
        } finally {
            close(jedis);
        }
    }

    /**
     * 从右出队列
     *
     * @param key
     * @return
     */
    public static String rpop(String key) {
        ShardedJedis jedis = getResource();
        try {
            return jedis.rpop(key);
        } finally {
            close(jedis);
        }
    }

    /**
     * lrange quue length
     *
     * @param key
     * @return
     */
    public static Long llen(String key) {
        ShardedJedis jedis = getResource();
        try {
            return jedis.llen(key);
        } finally {
            close(jedis);
        }
    }

    /**
     * 有序集合
     *
     * @param key
     * @param value
     * @return
     */
    public static Long addSet(String key, String value) {
        ShardedJedis jedis = getResource();
        try {
            Long score = jedis.zcard(key);
            return jedis.zadd(key, ++score, value);
        } finally {
            close(jedis);
        }
    }

    /**
     * 得到有序集合
     *
     * @param key
     * @return
     */
    public static Set<String> getSet(String key) {
        ShardedJedis jedis = getResource();
        try {
            Set<String> sets = jedis.zrange(key, 0, -1);
            return sets;
        } finally {
            close(jedis);
        }
    }

    /**
     * 分页得到有序集合
     *
     * @param key
     * @param size
     * @return
     */
    public static Set<String> zrange(String key, int size) {
        ShardedJedis jedis = getResource();
        try {
            Set<String> sets = jedis.zrange(key, 0, size);
            return sets;
        } finally {
            close(jedis);
        }
    }

    /**
     * 删除指定的元素
     *
     * @param key
     * @param values
     * @return
     */
    public static Long zrem(String key, String...values) {
        ShardedJedis jedis = getResource();
        try {
            Long count = jedis.zrem(key, values);
            return count;
        } finally {
            close(jedis);
        }
    }

    public static ShardedJedis getResource(ShardedJedisPool shardedJedisPool) {
        return shardedJedisPool.getResource();
    }

    public static void close(ShardedJedisPool shardedJedisPool, ShardedJedis jedis) {
        shardedJedisPool.returnBrokenResource(jedis);
    }

    public static Long incrBy(ShardedJedisPool shardedJedisPool, String key, long integer) {
        ShardedJedis jedis = getResource(shardedJedisPool);
        try{
            return jedis.incrBy(key, integer);
        }
        finally {
            close(shardedJedisPool, jedis);
        }
    }

    public static boolean set(ShardedJedisPool shardedJedisPool, String key, String value) {
        return set(shardedJedisPool, key, MAX_EXPIRE_TIME, value);
    }

    public static boolean set(ShardedJedisPool shardedJedisPool, String key, long milliseconds, String value) {
        ShardedJedis jedis = getResource(shardedJedisPool);
        try {
            //redis默认的过期时间是seconds 即秒
            String result = jedis.setex(key, ((Long)(milliseconds/1000)).intValue(), value);
            return "OK".equals(result);
        } finally {
            close(shardedJedisPool, jedis);
        }
    }

    public static boolean setObject(ShardedJedisPool shardedJedisPool, Object key, Object value) {
        return setObject(shardedJedisPool, key, MAX_EXPIRE_TIME, value);
    }

    public static boolean setObject(ShardedJedisPool shardedJedisPool, Object key, long milliseconds, Object value) {
        byte[] keyBytes = serializeValue(key);
        byte[] valueBytes = serializeValue(value);
        ShardedJedis jedis = getResource(shardedJedisPool);
        try {
            //redis默认的过期时间是seconds 即秒
            String result = jedis.setex(keyBytes, ((Long)(milliseconds/1000)).intValue(), valueBytes);
            return "OK".equals(result);
        } finally {
            close(shardedJedisPool, jedis);
        }
    }

    public static String get(ShardedJedisPool shardedJedisPool, String key) {
        ShardedJedis jedis = getResource(shardedJedisPool);
        try {
            return jedis.get(key);
        } finally {
            close(shardedJedisPool, jedis);
        }
    }

    public static Object getObject(ShardedJedisPool shardedJedisPool, Object key) {
        byte[] keyBytes = serializeValue(key);
        ShardedJedis jedis = getResource(shardedJedisPool);
        try {
            byte[] valueBytes = jedis.get(keyBytes);
            if (valueBytes == null) {
                return null;
            }
            return deserializeValue(valueBytes);
        } finally {
            close(shardedJedisPool, jedis);
        }
    }

    public static Long del(ShardedJedisPool shardedJedisPool, String key) {
        ShardedJedis jedis = getResource(shardedJedisPool);
        try {
            return jedis.del(key);
        } finally {
            close(shardedJedisPool, jedis);
        }
    }

    public static Long delObject(ShardedJedisPool shardedJedisPool, Object key) {
        byte[] keyBytes = serializeValue(key);
        ShardedJedis jedis = getResource(shardedJedisPool);
        try {
            return jedis.del(keyBytes);
        } finally {
            close(shardedJedisPool, jedis);
        }
    }

    public static Boolean exists(ShardedJedisPool shardedJedisPool, String key) {
        ShardedJedis jedis = getResource(shardedJedisPool);
        try {
            return jedis.exists(key);
        } finally {
            close(shardedJedisPool, jedis);
        }
    }

    public static Boolean existsObject(ShardedJedisPool shardedJedisPool, Object key) {
        byte[] keyBytes = serializeValue(key);
        ShardedJedis jedis = getResource(shardedJedisPool);
        try {
            return jedis.exists(keyBytes);
        } finally {
            close(shardedJedisPool, jedis);
        }
    }

    /**
     * 头入队列
     *
     * @param key
     * @param value
     * @return
     */
    public static Long lpush(ShardedJedisPool shardedJedisPool, String key, String value) {
        ShardedJedis jedis = getResource(shardedJedisPool);
        try {
            return jedis.lpush(key, value);
        } finally {
            close(shardedJedisPool, jedis);
        }
    }

    /**
     * 尾入队列
     *
     * @param key
     * @param value
     * @return
     */
    public static Long rpush(ShardedJedisPool shardedJedisPool, String key, String value) {
        ShardedJedis jedis = getResource(shardedJedisPool);
        try {
            return jedis.rpush(key, value);
        } finally {
            close(shardedJedisPool, jedis);
        }
    }

    /**
     * 从左出队列
     *
     * @param key
     * @return
     */
    public static String lpop(ShardedJedisPool shardedJedisPool, String key) {
        ShardedJedis jedis = getResource(shardedJedisPool);
        try {
            return jedis.lpop(key);
        } finally {
            close(shardedJedisPool, jedis);
        }
    }

    /**
     * 从右出队列
     *
     * @param key
     * @return
     */
    public static String rpop(ShardedJedisPool shardedJedisPool, String key) {
        ShardedJedis jedis = getResource(shardedJedisPool);
        try {
            return jedis.rpop(key);
        } finally {
            close(shardedJedisPool, jedis);
        }
    }

    /**
     * lrange quue length
     *
     * @param key
     * @return
     */
    public static Long llen(ShardedJedisPool shardedJedisPool, String key) {
        ShardedJedis jedis = getResource(shardedJedisPool);
        try {
            return jedis.llen(key);
        } finally {
            close(shardedJedisPool, jedis);
        }
    }

    /**
     * 有序集合
     *
     * @param key
     * @param value
     * @return
     */
    public static Long addSet(ShardedJedisPool shardedJedisPool, String key, String value) {
        ShardedJedis jedis = getResource(shardedJedisPool);
        try {
            Long score = jedis.zcard(key);
            return jedis.zadd(key, ++score, value);
        } finally {
            close(shardedJedisPool, jedis);
        }
    }

    /**
     * 得到有序集合
     *
     * @param key
     * @return
     */
    public static Set<String> getSet(ShardedJedisPool shardedJedisPool, String key) {
        ShardedJedis jedis = getResource(shardedJedisPool);
        try {
            Set<String> sets = jedis.zrange(key, 0, -1);
            return sets;
        } finally {
            close(shardedJedisPool, jedis);
        }
    }

    /**
     * 分页得到有序集合
     *
     * @param key
     * @param size
     * @return
     */
    public static Set<String> zrange(ShardedJedisPool shardedJedisPool, String key, int size) {
        ShardedJedis jedis = getResource(shardedJedisPool);
        try {
            Set<String> sets = jedis.zrange(key, 0, size);
            return sets;
        } finally {
            close(shardedJedisPool, jedis);
        }
    }

    /**
     * 删除指定的元素
     *
     * @param key
     * @param values
     * @return
     */
    public static Long zrem(ShardedJedisPool shardedJedisPool, String key, String...values) {
        ShardedJedis jedis = getResource(shardedJedisPool);
        try {
            Long count = jedis.zrem(key, values);
            return count;
        } finally {
            close(shardedJedisPool, jedis);
        }
    }

    public static class JdkSerializationRedisSerializer {
        private Converter<Object, byte[]> serializer = new SerializingConverter();
        private Converter<byte[], Object> deserializer = new DeserializingConverter();

        public JdkSerializationRedisSerializer() {
        }

        public Object deserialize(byte[] bytes) {
            if(bytes == null || bytes.length == 0) {
                return null;
            } else {
                return this.deserializer.convert(bytes);
            }
        }

        public byte[] serialize(Object object) {
            if(object == null) {
                return new byte[0];
            } else {
                return (byte[])this.serializer.convert(object);
            }
        }
    }

    public static <T> T lockAndWork(String key, long time, TimeUnit unit, RedisLockCallback<T> lockCallback) {
        //乐观锁
        ShardedJedis shardedJedis = getResource();
        Jedis jedis = shardedJedis.getShard(key);
        // 准备一个结果容器
        ResultContainer<T> resultContainer = new DefaultResultContainer<T>();
        long start = System.currentTimeMillis();
        long timeout = unit.convert(time,TimeUnit.MILLISECONDS);
        boolean flag = false;
        try {
//            while ((System.currentTimeMillis() - start) < timeout){
                if("OK".equals(jedis.watch(key))){
                    String oldData = jedis.get(key);
                    Transaction tx = jedis.multi();
                    lockCallback.doWork(key, resultContainer,oldData, tx, jedis);
                    List<Object> results = tx.exec();
                    if (results != null) {
                        flag = true;
                        long end = System.currentTimeMillis();
                        System.out.println("获得乐观锁，耗时" + ((end - start) / 1000.0) + "秒");
                    }else {
                        jedis.unwatch();
                    }
                }
//            }
        } catch (Exception e) {
            // 执行错误，抛出异常
            String message = "获取乐观锁出现异常, key=" + key;
            throw new RuntimeException(message, e);
        } finally {
            close(shardedJedis);
        }
        if(!flag){
            String message = "获取乐观锁失败：" + key;
            throw new RuntimeException(message);
        }
        return resultContainer.getResult();
    }
}
