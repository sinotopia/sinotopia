package com.sinotopia.fundamental.redis.cluster;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.Set;

/**
 */
public class FundamentalJedisCluster extends JedisCluster {

    public FundamentalJedisCluster(final GenericObjectPoolConfig poolConfig, final FundamentalJedisClusterConfig clusterConfig) {
        super(clusterConfig.getHostAndPorts(), clusterConfig.getTimeout(), clusterConfig.getTimeout(), clusterConfig.getMaxRedirections(), poolConfig);
    }

    public FundamentalJedisCluster(Set<HostAndPort> nodes, int timeout) {
        super(nodes, timeout);
    }

    public FundamentalJedisCluster(Set<HostAndPort> nodes) {
        super(nodes);
    }

    public FundamentalJedisCluster(Set<HostAndPort> nodes, int timeout, int maxRedirections) {
        super(nodes, timeout, maxRedirections);
    }

    public FundamentalJedisCluster(Set<HostAndPort> nodes, final GenericObjectPoolConfig poolConfig) {
        super(nodes, poolConfig);
    }

    public FundamentalJedisCluster(Set<HostAndPort> nodes, int timeout, final GenericObjectPoolConfig poolConfig) {
        super(nodes, timeout, poolConfig);
    }

    public FundamentalJedisCluster(Set<HostAndPort> jedisClusterNode, int timeout, int maxRedirections,
                                   final GenericObjectPoolConfig poolConfig) {
        super(jedisClusterNode, timeout, timeout, maxRedirections, poolConfig);
    }

}
