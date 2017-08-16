package com.sinotopia.fundamental.zookeeper.listener;

public interface ZooKeeperClientConnectionStateListener {
    
    public void notifyConnected();
    
    public void notifyDisconnected();
    
}
