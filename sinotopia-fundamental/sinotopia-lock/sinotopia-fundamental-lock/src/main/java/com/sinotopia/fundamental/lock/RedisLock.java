package com.sinotopia.fundamental.lock;

import com.sinotopia.fundamental.config.FundamentalConfigProvider;
import com.sinotopia.fundamental.lock.constants.PropertiesKey;
import org.redisson.Config;
import org.redisson.Redisson;
import org.redisson.connection.RandomLoadBalancer;
import org.redisson.core.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 *
 * <p>基于Redis的分布式锁</p>
 * @Author dzr
 * @Date  2016/4/25.
 */
public class RedisLock implements Lock{

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisLock.class);

    private static Redisson redisson;

    private String lockKey;

    static {
        /**
         * redis 初始化
         */
        int mode = FundamentalConfigProvider.getInt(PropertiesKey.REDIS_MODE, 1);
        String redisAddress = FundamentalConfigProvider.getString(PropertiesKey.REDIS_HOST,"127.0.0.1");
        String redisPort = FundamentalConfigProvider.getString(PropertiesKey.REDIS_PORT,"6379");
        String passowrd = FundamentalConfigProvider.getString(PropertiesKey.REDIS_PASSWORD,"123456");
        redisAddress = redisAddress+":" + redisPort;

       switch (mode){
           case 1:
               redisson = getSingleClient(redisAddress,passowrd);
               break;
           case 2:
               String masterAddress = "";
               String slaveAddress ="";
               redisson = getMasterSlaveClient(masterAddress,slaveAddress,passowrd);
               break;
           case 3:
//               String masterAddress = "";
//               String hosts = "";
//               redisson = getSentinelClient(masterAddress,hosts);
               break;
           case 4:
               redisson = getClusterClient(redisAddress,passowrd);
               break;
       }
    }

    public RedisLock(String lockKey) {
        this.lockKey = lockKey;
    }

    @Override
    public void lock() {
        RLock lock = redisson.getLock(lockKey);
        lock.lock();
        LOGGER.info("invoke lock method,lock key:"+this.lockKey);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        RLock lock = redisson.getLock(lockKey);
        lock.lockInterruptibly();
        LOGGER.info("invoke lockInterruptibly method,lock key:"+this.lockKey);
    }

    @Override
    public boolean tryLock() {
        LOGGER.info("invoke tryLock method,lock key:"+this.lockKey);
        RLock lock = redisson.getLock(lockKey);
        boolean isLocked = lock.tryLock();
        LOGGER.info("invoke tryLock method,lock key:"+this.lockKey+" is locked:"+isLocked);
        return isLocked;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        LOGGER.info("invoke tryLock method,lock key:"+this.lockKey);
        RLock lock = redisson.getLock(lockKey);
        boolean isLocked = lock.tryLock(time,unit);
        LOGGER.info("invoke tryLock method,lock key:"+this.lockKey+" is locked:"+isLocked);
        return isLocked;
    }

    @Override
    public void unlock() {
        LOGGER.info("invoke unlock method,lock key:"+this.lockKey);
        RLock lock = redisson.getLock(lockKey);
        lock.unlock();
        LOGGER.info("invoke unlock method,lock key:"+this.lockKey+" unlock success!");
    }

    @Override
    public Condition newCondition() {
        //暂时不支持
        throw new UnsupportedOperationException();
    }


    private static Redisson getSingleClient(String host, String password) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(host)
                .setPassword(password)
                .setConnectionPoolSize(1000);
        Redisson redisson = Redisson.create(config);
        return redisson;
    }

    private static Redisson getMasterSlaveClient(String masterAddress, String slaveAddress, String password) {
        Config config = new Config();
        String[] slaveAddresss = slaveAddress.split(",");
        config.useMasterSlaveConnection()
                .setMasterAddress(masterAddress)
                .setPassword(password)
                .setLoadBalancer(new RandomLoadBalancer())
                .addSlaveAddress(slaveAddresss)
                .setPassword(password)
                .setMasterConnectionPoolSize(10000)
                .setSlaveConnectionPoolSize(10000);
        Redisson redisson = Redisson.create(config);
        return redisson;
    }

    private static Redisson getClusterClient(String hosts, String password) {
        Config config = new Config();
        String[] hostArr = hosts.split(",");
        config.useClusterServers()
                .setScanInterval(2000)
                .addNodeAddress(hostArr)
                .setPassword(password)
                .setMasterConnectionPoolSize(10000)
                .setSlaveConnectionPoolSize(10000);

        Redisson redisson = Redisson.create(config);
        return redisson;
    }

}
