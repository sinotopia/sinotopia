package com.hkfs.fundamental.zookeeper.callback;

import com.hkfs.fundamental.zookeeper.ZooKeeperClient;
import com.hkfs.fundamental.zookeeper.bean.ResultContainer;

/**
 * 在ZooKeeper里加锁后执行业务逻辑的回调接口。
 * @Author dzr
 * @Date 2016/04/25
 * @param <T> 业务返回结果的类型。
 */
public interface ZooKeeperLockCallback<T> {
    
    /**
     * 执行业务逻辑。
     * @param lockedPath 已锁定的路径。
     * @param resultContainer 结果容器，如果有结果要返回，请调用{@link ResultContainer#setResult(Object)}设置结果，该结果最后会由
     *            {@link ZooKeeperClient#lockAndWork(String, long, java.util.concurrent.TimeUnit, com.hkfs.fundamental.zookeeper.callback.ZooKeeperLockCallback)}
     *            返回。
     * @param client ZooKeeperClient，方便对ZooKeeper进行其他操作。
     */
    public void doWork(String lockedPath, ResultContainer<T> resultContainer, ZooKeeperClient client);
    
}
