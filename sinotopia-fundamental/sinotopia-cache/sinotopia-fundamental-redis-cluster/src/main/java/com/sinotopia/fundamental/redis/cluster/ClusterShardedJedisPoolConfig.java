package com.sinotopia.fundamental.redis.cluster;

import redis.clients.jedis.JedisShardInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * redis集群配置
 * Created by zhoubing on 2016/8/4.
 */
public class ClusterShardedJedisPoolConfig {
    private String connections;
    private int timeout;
    private String passwords;

    public ClusterShardedJedisPoolConfig() {

    }

    public ClusterShardedJedisPoolConfig(String connections, int timeout) {
        this(connections, timeout, null);
    }
    public ClusterShardedJedisPoolConfig(String connections, int timeout, String passwords) {
        this.connections = connections;
        this.timeout = timeout;
        this.passwords = passwords;
    }

    public List<JedisShardInfo> getShards() {
        String[] connectionArray = connections.split(",");
        String[] passwordArray = (passwords!=null&&passwords.length()>0)?passwords.split(","):null;
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>(connectionArray.length);
        for (int i=0;i<connectionArray.length;i++) {
            String[] array = connectionArray[i].split(":");
            if (array.length != 2) {
                throw new IllegalArgumentException("Illegal redis connection config value "+connectionArray[i]);
            }

            JedisShardInfo jedisShardInfo = new JedisShardInfo(array[0], Integer.parseInt(array[1]), timeout);
            if (passwordArray != null && passwordArray.length > 0) {
                if (i < passwordArray.length) {
                    jedisShardInfo.setPassword(passwordArray[i]);
                }
                else {
                    //其他的全部用第一个
                    jedisShardInfo.setPassword(passwordArray[0]);
                }
            }
            shards.add(jedisShardInfo);
        }
        return shards;
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

    public String getPasswords() {
        return passwords;
    }

    public void setPasswords(String passwords) {
        this.passwords = passwords;
    }
}
