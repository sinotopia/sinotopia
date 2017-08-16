package com.sinotopia.fundamental.zookeeper.callback;

import com.sinotopia.fundamental.zookeeper.ZooKeeperClient;
import com.sinotopia.fundamental.zookeeper.bean.ResultContainer;
import com.sinotopia.fundamental.zookeeper.utils.ZookeeperModuleUtils;

/**
 * 在ZooKeeper里加锁后使用节点上的数据执行业务逻辑的回调接口。
 * @Author dzr
 * @Date 2016/04/25
 * @param <T> 业务返回结果的类型。
 */
public abstract class ZooKeeperLockUseDataCallback<T> implements ZooKeeperLockCallback<T> {
    
    protected boolean dataCompressed;
    
    protected boolean useOriginalDataWhenDecompressFailed;
    
    public void setDataCompressed(boolean dataCompressed) {
        this.dataCompressed = dataCompressed;
    }
    
    private String toString(byte[] bytes) {
        if (null == bytes) {
            return null;
        }
        if (dataCompressed) {
            bytes = ZookeeperModuleUtils.ungzip(bytes, useOriginalDataWhenDecompressFailed);
        }
        return new String(bytes, ZookeeperModuleUtils.CHARSET_UTF8);
    }



    @Override
    public void doWork(String lockedPath, ResultContainer<T> resultContainer, ZooKeeperClient client) {
        // 从服务器获取数据
        String data = toString(client.getData(lockedPath));
        // 将数据提供给回调使用
        String newData = useData(lockedPath, data, resultContainer, client);
        // 如果返回的数据与提供的不同，则更新到服务器
        if (!ZookeeperModuleUtils.equals(data, newData)) {
            client.setData(lockedPath, newData, dataCompressed);
        }
    }



    /**
     * 使用节点上的数据执行业务逻辑。
     * @param lockedPath 已锁定的路径。
     * @param data 当前在节点上的数据。
     * @param resultContainer 结果容器，如果有结果要返回，请调用{@link ResultContainer#setResult(Object)}设置结果，该结果最后会由
     *            {@link ZooKeeperClient#lockAndWork(String, long, java.util.concurrent.TimeUnit, ZooKeeperLockCallback)}
     *            返回。
     * @param client ZooKeeperClient，方便对ZooKeeper进行其他操作。
     * @return 需要设置在节点上的新数据，如果不需要更新节点数据，则返回原数据。
     */
    public abstract String useData(String lockedPath, String data, ResultContainer<T> resultContainer,
            ZooKeeperClient client);
    
}
