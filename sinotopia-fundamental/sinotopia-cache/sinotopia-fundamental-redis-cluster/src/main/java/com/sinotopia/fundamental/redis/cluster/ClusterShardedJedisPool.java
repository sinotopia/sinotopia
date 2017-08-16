package com.sinotopia.fundamental.redis.cluster;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 支持集群的redis pool
 * Created by zhoubing on 2016/8/4.
 */
public class ClusterShardedJedisPool extends ShardedJedisPool {
    public ClusterShardedJedisPool(final GenericObjectPoolConfig poolConfig, ClusterShardedJedisPoolConfig clusterConfig) {
        this(poolConfig, clusterConfig.getShards());
    }

    public ClusterShardedJedisPool(final GenericObjectPoolConfig poolConfig, List<JedisShardInfo> shards) {
        this(poolConfig, shards, Hashing.MURMUR_HASH);
    }

    public ClusterShardedJedisPool(final GenericObjectPoolConfig poolConfig, List<JedisShardInfo> shards,
                            Hashing algo) {
        this(poolConfig, shards, algo, null);
    }

    public ClusterShardedJedisPool(final GenericObjectPoolConfig poolConfig, List<JedisShardInfo> shards,
                            Pattern keyTagPattern) {
        this(poolConfig, shards, Hashing.MURMUR_HASH, keyTagPattern);
    }

    public ClusterShardedJedisPool(final GenericObjectPoolConfig poolConfig, List<JedisShardInfo> shards,
                            Hashing algo, Pattern keyTagPattern) {
        super(poolConfig, shards, algo, keyTagPattern);
    }
}
