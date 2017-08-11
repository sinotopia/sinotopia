package com.hkfs.fundamental.redis.cluster;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import redis.clients.jedis.JedisCluster;

import java.util.Set;

/**
 * Created by zhoubing on 2016/4/8.
 */
public class RedisClusterConnector {
    private static JedisCluster jedisCluster;
    private static JdkSerializationRedisSerializer redisSerializer = new JdkSerializationRedisSerializer();
    private static Long MAX_EXPIRE_TIME = Long.parseLong(String.valueOf(Integer.MAX_VALUE)) * 1000;
    public static JedisCluster getJedisCluster() {
        return jedisCluster;
    }

    public void setJedisCluster(JedisCluster jedisCluster) {
        RedisClusterConnector.jedisCluster = jedisCluster;
    }

    public static Long incrBy(String key, long integer) {
        return getJedisCluster().incrBy(key, integer);
    }

    public static boolean set(String key, String value) {
        return set(key, MAX_EXPIRE_TIME, value);
    }

    public static boolean set(String key, long milliseconds, String value) {
        //redis默认的过期时间是seconds 即秒
        String result = getJedisCluster().setex(key, ((Long)(milliseconds/1000)).intValue(), value);
        return "OK".equals(result);
    }

//    public static boolean setObject(Object key, Object value) {
//        return setObject(key, MAX_EXPIRE_TIME, value);
//    }
//
//    public static boolean setObject(Object key, long milliseconds, Object value) {
//        byte[] keyBytes = serializeValue(key);
//        byte[] valueBytes = serializeValue(value);
//        //redis默认的过期时间是seconds 即秒
//        String result = getJedisCluster().setex(keyBytes, ((Long)(milliseconds/1000)).intValue(), valueBytes);
//        return "OK".equals(result);
//    }

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
        return getJedisCluster().get(key);
    }

//    public static Object getObject(Object key) {
//        byte[] keyBytes = serializeValue(key);
//        ShardedJedis jedis = getResource();
//        try {
//            byte[] valueBytes = jedis.get(keyBytes);
//            if (valueBytes == null) {
//                return null;
//            }
//            return deserializeValue(valueBytes);
//        } finally {
//            close(jedis);
//        }
//    }

    public static Long del(String key) {
        return getJedisCluster().del(key);
    }

//    public static Long delObject(Object key) {
//        byte[] keyBytes = serializeValue(key);
//        ShardedJedis jedis = getResource();
//        try {
//            return jedis.del(keyBytes);
//        } finally {
//            close(jedis);
//        }
//    }

    public static Boolean exists(String key) {
        return getJedisCluster().exists(key);
    }

//    public static Boolean existsObject(Object key) {
//        byte[] keyBytes = serializeValue(key);
//        ShardedJedis jedis = getResource();
//        try {
//            return jedis.exists(keyBytes);
//        } finally {
//            close(jedis);
//        }
//    }

    /**
     * 头入队列
     *
     * @param key
     * @param value
     * @return
     */
    public static Long lpush(String key, String value) {
        return getJedisCluster().lpush(key, value);
    }

    /**
     * 尾入队列
     *
     * @param key
     * @param value
     * @return
     */
    public static Long rpush(String key, String value) {
        return getJedisCluster().rpush(key, value);
    }

    /**
     * 从左出队列
     *
     * @param key
     * @return
     */
    public static String lpop(String key) {
        return getJedisCluster().lpop(key);
    }

    /**
     * 从右出队列
     *
     * @param key
     * @return
     */
    public static String rpop(String key) {
        return getJedisCluster().rpop(key);
    }

    /**
     * lrange quue length
     *
     * @param key
     * @return
     */
    public static Long llen(String key) {
        return getJedisCluster().llen(key);
    }

    /**
     * 有序集合
     *
     * @param key
     * @param value
     * @return
     */
    public static Long addSet(String key, String value) {
        Long score = getJedisCluster().zcard(key);
        return getJedisCluster().zadd(key, ++score, value);
    }

    /**
     * 得到有序集合
     *
     * @param key
     * @return
     */
    public static Set<String> getSet(String key) {
        Set<String> sets = getJedisCluster().zrange(key, 0, -1);
        return sets;
    }

    /**
     * 分页得到有序集合
     *
     * @param key
     * @param size
     * @return
     */
    public static Set<String> zrange(String key, int size) {
        Set<String> sets = getJedisCluster().zrange(key, 0, size);
        return sets;
    }

    /**
     * 删除指定的元素
     *
     * @param key
     * @param values
     * @return
     */
    public static Long zrem(String key, String...values) {
        Long count = getJedisCluster().zrem(key, values);
        return count;
    }


    public static Long incrBy(JedisCluster jedisCluster, String key, long integer) {
        return getJedisCluster().incrBy(key, integer);
    }

    public static boolean set(JedisCluster jedisCluster, String key, String value) {
        return set(jedisCluster, key, MAX_EXPIRE_TIME, value);
    }

    public static boolean set(JedisCluster jedisCluster, String key, long milliseconds, String value) {
        //redis默认的过期时间是seconds 即秒
        String result = getJedisCluster().setex(key, ((Long) (milliseconds / 1000)).intValue(), value);
        return "OK".equals(result);
    }

//    public static boolean setObject(JedisCluster jedisCluster, Object key, Object value) {
//        return setObject(jedisCluster, key, MAX_EXPIRE_TIME, value);
//    }
//
//    public static boolean setObject(JedisCluster jedisCluster, Object key, long milliseconds, Object value) {
//        byte[] keyBytes = serializeValue(key);
//        byte[] valueBytes = serializeValue(value);
//        ShardedJedis jedis = getResource(jedisCluster);
//        try {
//            //redis默认的过期时间是seconds 即秒
//            String result = jedis.setex(keyBytes, ((Long)(milliseconds/1000)).intValue(), valueBytes);
//            return "OK".equals(result);
//        } finally {
//            close(jedisCluster, jedis);
//        }
//    }

    public static String get(JedisCluster jedisCluster, String key) {
        return jedisCluster.get(key);
    }

//    public static Object getObject(JedisCluster jedisCluster, Object key) {
//        byte[] keyBytes = serializeValue(key);
//        ShardedJedis jedis = getResource(jedisCluster);
//        try {
//            byte[] valueBytes = jedis.get(keyBytes);
//            if (valueBytes == null) {
//                return null;
//            }
//            return deserializeValue(valueBytes);
//        } finally {
//            close(jedisCluster, jedis);
//        }
//    }

    public static Long del(JedisCluster jedisCluster, String key) {
        return jedisCluster.del(key);
    }

//    public static Long delObject(JedisCluster jedisCluster, Object key) {
//        byte[] keyBytes = serializeValue(key);
//        ShardedJedis jedis = getResource(jedisCluster);
//        try {
//            return jedis.del(keyBytes);
//        } finally {
//            close(jedisCluster, jedis);
//        }
//    }

    public static Boolean exists(JedisCluster jedisCluster, String key) {
        return jedisCluster.exists(key);
    }

//    public static Boolean existsObject(JedisCluster jedisCluster, Object key) {
//        byte[] keyBytes = serializeValue(key);
//        ShardedJedis jedis = getResource(jedisCluster);
//        try {
//            return jedis.exists(keyBytes);
//        } finally {
//            close(jedisCluster, jedis);
//        }
//    }

    /**
     * 头入队列
     *
     * @param key
     * @param value
     * @return
     */
    public static Long lpush(JedisCluster jedisCluster, String key, String value) {
        return jedisCluster.lpush(key, value);
    }

    /**
     * 尾入队列
     *
     * @param key
     * @param value
     * @return
     */
    public static Long rpush(JedisCluster jedisCluster, String key, String value) {
        return jedisCluster.rpush(key, value);
    }

    /**
     * 从左出队列
     *
     * @param key
     * @return
     */
    public static String lpop(JedisCluster jedisCluster, String key) {
        return jedisCluster.lpop(key);
    }

    /**
     * 从右出队列
     *
     * @param key
     * @return
     */
    public static String rpop(JedisCluster jedisCluster, String key) {
        return jedisCluster.rpop(key);
    }

    /**
     * lrange quue length
     *
     * @param key
     * @return
     */
    public static Long llen(JedisCluster jedisCluster, String key) {
        return jedisCluster.llen(key);
    }

    /**
     * 有序集合
     *
     * @param key
     * @param value
     * @return
     */
    public static Long addSet(JedisCluster jedisCluster, String key, String value) {
        Long score = jedisCluster.zcard(key);
        return jedisCluster.zadd(key, ++score, value);
    }

    /**
     * 得到有序集合
     *
     * @param key
     * @return
     */
    public static Set<String> getSet(JedisCluster jedisCluster, String key) {
        Set<String> sets = jedisCluster.zrange(key, 0, -1);
        return sets;
    }

    /**
     * 分页得到有序集合
     *
     * @param key
     * @param size
     * @return
     */
    public static Set<String> zrange(JedisCluster jedisCluster, String key, int size) {
        Set<String> sets = jedisCluster.zrange(key, 0, size);
        return sets;
    }

    /**
     * 删除指定的元素
     *
     * @param key
     * @param values
     * @return
     */
    public static Long zrem(JedisCluster jedisCluster, String key, String...values) {
        Long count = jedisCluster.zrem(key, values);
        return count;
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

}
