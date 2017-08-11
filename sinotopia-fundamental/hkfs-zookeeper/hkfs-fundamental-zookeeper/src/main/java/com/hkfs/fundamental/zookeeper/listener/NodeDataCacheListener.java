package com.hkfs.fundamental.zookeeper.listener;

/**
 * 节点数据缓存监听器。
 * @Author dzr
 * @Date 2016/04/25
 */
public interface NodeDataCacheListener {
    
    /**
     * 节点有变化，节点增删或数据更新。
     * @param path 节点路径。
     * @param data 当前数据，如果为null表示节点不存在（或获取数据发生异常）。
     */
    public void nodeChanged(String path, byte[] data);
    
}
