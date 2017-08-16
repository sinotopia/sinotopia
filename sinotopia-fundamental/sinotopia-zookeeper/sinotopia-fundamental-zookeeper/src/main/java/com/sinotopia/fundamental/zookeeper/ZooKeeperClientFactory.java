package com.sinotopia.fundamental.zookeeper;

import com.sinotopia.fundamental.config.FundamentalConfigProvider;
import com.sinotopia.fundamental.zookeeper.constants.PropertiesKey;
import com.sinotopia.fundamental.zookeeper.utils.ZookeeperModuleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Zookeeper客户端工厂
 */
public class ZooKeeperClientFactory {
    
    private static final Logger LOG = LoggerFactory.getLogger(ZooKeeperClientFactory.class);
    
    public static final String ZOOKEEPER_CONNECTSTRING = FundamentalConfigProvider.getString(PropertiesKey.PROP_NAME_PREFIX,"project")
            + "zookeeper.connectString";

    private static ZooKeeperClient zkClient;
    
    private static ZooKeeperClient createZooKeeperClient() {
        if (zkClient != null) {
            return zkClient;
        }
        String zookeeperConnectString = System.getProperty(ZOOKEEPER_CONNECTSTRING);
        if (ZookeeperModuleUtils.isEmpty(zookeeperConnectString)) {
            zookeeperConnectString = FundamentalConfigProvider.getString(ZOOKEEPER_CONNECTSTRING);
        }
        if (ZookeeperModuleUtils.notEmpty(zookeeperConnectString)) {
            zkClient = new ZooKeeperClient(zookeeperConnectString);
            LOG.info("create ZooKeeperClient of: " + zookeeperConnectString);
            zkClient.start();
            return zkClient;
        } else {
            LOG.error(ZOOKEEPER_CONNECTSTRING + " not configured yet, create ZooKeeperClient failed!!!");
            return null;
        }
    }

    public static void close(){
         if (zkClient != null) {
             zkClient.close();
         }
    }
    
}
