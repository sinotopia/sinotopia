package com.hkfs.fundamental.zookeeper.listener;

public interface ZooKeeperClientConnectionStateListener {
    
    public void notifyConnected();
    
    public void notifyDisconnected();
    
}
