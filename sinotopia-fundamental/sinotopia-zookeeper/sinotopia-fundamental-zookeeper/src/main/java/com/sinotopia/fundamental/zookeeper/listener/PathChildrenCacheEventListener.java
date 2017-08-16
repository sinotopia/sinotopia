package com.sinotopia.fundamental.zookeeper.listener;

import org.apache.zookeeper.data.Stat;

public interface PathChildrenCacheEventListener {
    
    public void childPathAdded(String fullPath, byte[] data, Stat stat);
    
    public void childPathUpdated(String fullPath, byte[] data, Stat stat);
    
    public void childPathRemoved(String fullPath, Stat stat);
    
    public boolean isNeedRecursionWatch();
    
}
