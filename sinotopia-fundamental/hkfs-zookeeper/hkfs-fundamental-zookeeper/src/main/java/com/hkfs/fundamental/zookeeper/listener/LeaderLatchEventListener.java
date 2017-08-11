package com.hkfs.fundamental.zookeeper.listener;

/**
 * 主导节点竞争事件监听器。
 * @Author dzr
 * @Date 2016/04/25
 */
public interface LeaderLatchEventListener {
    
    /**
     * 当前是主导节点。
     */
    public void isLeader();
    
    /**
     * 当前不是主导节点。
     */
    public void notLeader();
    
}
