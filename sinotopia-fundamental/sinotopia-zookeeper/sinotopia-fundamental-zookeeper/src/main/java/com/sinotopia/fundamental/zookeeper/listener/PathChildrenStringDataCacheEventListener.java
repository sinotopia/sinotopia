package com.hkfs.fundamental.zookeeper.listener;

import com.hkfs.fundamental.zookeeper.utils.ZookeeperModuleUtils;
import org.apache.zookeeper.data.Stat;


public abstract class PathChildrenStringDataCacheEventListener implements PathChildrenCacheEventListener {
    
    protected boolean dataCompressed;
    
    protected boolean useOriginalDataWhenDecompressFailed;
    
    public void setDataCompressed(boolean dataCompressed) {
        this.dataCompressed = dataCompressed;
    }
    
    private String toString(byte[] bytes) {
        if (null == bytes) {
            return null;
        }
        if (dataCompressed && 0 < bytes.length) {
            bytes = ZookeeperModuleUtils.ungzip(bytes, useOriginalDataWhenDecompressFailed);
        }
        return new String(bytes, ZookeeperModuleUtils.CHARSET_UTF8);
    }
    
    @Override
    public void childPathAdded(String fullPath, byte[] data, Stat stat) {
        String stringData = toString(data);
        childPathAdded(fullPath, stringData, stat);
    }
    
    @Override
    public void childPathUpdated(String fullPath, byte[] data, Stat stat) {
        String stringData = toString(data);
        childPathUpdated(fullPath, stringData, stat);
    }
    
    public abstract void childPathAdded(String fullPath, String data, Stat stat);
    
    public abstract void childPathUpdated(String fullPath, String data, Stat stat);
    
    public abstract void childPathRemoved(String fullPath, Stat stat);
    
}
