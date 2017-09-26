package com.sinotopia.fundamental.redis;

import redis.clients.jedis.HostAndPort;

import java.util.HashSet;
import java.util.Set;

/**
 */
public class FundamentalJedisClusterConfig {

    private String connections;
    private int timeout = 15000;
    private int maxRedirections = 5;

    public FundamentalJedisClusterConfig() {

    }

    public FundamentalJedisClusterConfig(String connections, int timeout) {
        this.connections = connections;
        this.timeout = timeout;
    }

    public Set<HostAndPort> getHostAndPorts() {
        String[] connectionArray = connections.split(",");
        Set<HostAndPort> set = new HashSet<HostAndPort>();
        for (int i = 0; i < connectionArray.length; i++) {
            String[] array = connectionArray[i].split(":");
            if (array.length != 2) {
                throw new IllegalArgumentException("Illegal redis connection config value " + connectionArray[i]);
            }

            HostAndPort hostAndPort = new HostAndPort(array[0], Integer.parseInt(array[1]));
            set.add(hostAndPort);
        }
        return set;
    }

    public String getConnections() {
        return connections;
    }

    public void setConnections(String connections) {
        this.connections = connections;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getMaxRedirections() {
        return maxRedirections;
    }

    public void setMaxRedirections(int maxRedirections) {
        this.maxRedirections = maxRedirections;
    }
}
