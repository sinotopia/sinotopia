package com.sinotopia.fundamental.zookeeper;

import com.sinotopia.fundamental.zookeeper.callback.ZooKeeperLockCallback;
import com.sinotopia.fundamental.zookeeper.listener.LeaderLatchEventListener;
import com.sinotopia.fundamental.zookeeper.listener.NodeDataCacheListener;
import com.sinotopia.fundamental.zookeeper.listener.PathChildrenCacheEventListener;
import com.sinotopia.fundamental.zookeeper.listener.ZooKeeperClientConnectionStateListener;
import org.apache.zookeeper.CreateMode;

import java.io.EOFException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 静态单例的ZooKeeper客户端。
 * @author zhaohc
 * 
 */
public class ZooKeeperSingleClient {
    
    private static ZooKeeperClient client;
    
    /**
     * 启动该ZooKeeper客户端。
     * @param connectString 服务器连接串。
     */
    public static void start(String connectString) {
        if (null == client) {
            client = new ZooKeeperClient(connectString);
        }
        client.start();
    }
    
    /**
     * 启动该ZooKeeper客户端。
     * @param connectString 服务器连接串。
     * @param baseSleepTimeMs 重试间隔时间。
     * @param maxRetries 最大重试次数。
     */
    public static void start(String connectString, int baseSleepTimeMs, int maxRetries) {
        if (null == client) {
            client = new ZooKeeperClient(connectString, baseSleepTimeMs, maxRetries);
        }
        client.start();
    }
    
    /**
     * 关闭该ZooKeeper客户端。
     */
    public static void close() {
        client.close();
    }
    
    /**
     * 获取当前底层客户端，如果没有调用start，将返回null。
     * @return 当前底层客户端。
     */
    public static ZooKeeperClient getClient() {
        return client;
    }
    
    /**
     * 添加客户端连接状态监听
     * @param listener 客户端连接状态监听器
     */
    public void addClientConnectionStateListener(ZooKeeperClientConnectionStateListener listener) {
        client.addClientConnectionStateListener(listener);
    }
    
    /**
     * 删除客户端连接状态监听
     * @param listener 客户端连接状态监听器
     */
    public void removeClientConnectionStateListener(ZooKeeperClientConnectionStateListener listener) {
        client.removeClientConnectionStateListener(listener);
    }
    
    /**
     * 创建指定路径，如果父路径不存在则抛出异常。
     * @param path 路径。
     */
    public static void create(String path) {
        client.create(path);
    }
    
    /**
     * 创建指定路径，如果父路径不存在则抛出异常。
     * @param path 路径。
     * @param createMode 要创建的路径的类型。
     */
    public static void create(String path, CreateMode createMode) {
        client.create(path, createMode);
    }
    
    /**
     * 创建路径，如果父路径不存在，会自动创建。
     * @param path 路径。
     */
    public static void mkdirs(String path) {
        client.mkdirs(path);
    }
    
    /**
     * 创建路径，如果父路径不存在，会自动创建。
     * @param path 路径。
     * @param createMode 要创建的路径的类型。
     */
    public static void mkdirs(String path, CreateMode createMode) {
        client.mkdirs(path, createMode);
    }
    
    /**
     * 创建路径，如果父路径不存在，会自动创建。
     * @param parentPath 父路径。
     * @param childPath 子路径名。
     */
    public static void mkdirs(String parentPath, String childPath) {
        client.mkdirs(parentPath, childPath);
    }
    
    /**
     * 创建路径，如果父路径不存在，会自动创建。
     * @param parentPath 父路径。
     * @param childPath 子路径名。
     * @param createMode 要创建的路径的类型。
     */
    public static void mkdirs(String parentPath, String childPath, CreateMode createMode) {
        client.mkdirs(parentPath, childPath, createMode);
    }
    
    /**
     * 删除指定路径，会递归删除其下子路径，如果路径不存在则不操作。
     * @param path 路径。
     */
    public static void delete(String path) {
        client.delete(path);
    }
    
    /**
     * 删除指定路径，如果路径不存在，则抛出异常。
     * @param path 路径。
     * @param clearChildren 如果为true，则递归删除子路径，否则只删除指定路径，如果路径非空，则抛出异常。
     */
    public static void delete(String path, boolean clearChildren) {
        client.delete(path, clearChildren);
    }
    
    /**
     * 检查指定路径是否存在。
     * @param path 路径。
     * @return 如果存在则返回true，否则返回false。
     */
    public static boolean exists(String path) {
        return client.exists(path);
    }
    
    /**
     * 检查指定路径是否存在。
     * @param path 路径。
     * @return 如果存在则返回true，否则返回false。
     */
    public static List<String> getChildren(String path) {
        return client.getChildren(path);
    }
    
    /**
     * 获取指定路径上的数据。
     * @param path 路径。
     * @return 路径上的数据，如果路径不存在，则返回null。
     */
    public static String getStringData(String path) {
        return client.getStringData(path);
    }
    
    /**
     * 获取指定路径上的数据。
     * @param path 路径。
     * @param decompress 是否需要进行解压缩。
     * @param giveRawIfDecompressFailed 如果解压失败，true返回原始数据，false返回空白。不确定数据是否被压缩时可设为true，确定被压缩过一般应设为false。
     * @return 路径上的数据，如果路径不存在，则返回null。
     */
    public static String getStringData(String path, boolean decompress, boolean giveRawIfDecompressFailed) {
        return client.getStringData(path, decompress, giveRawIfDecompressFailed);
    }
    
    /**
     * 获取指定路径上的数据。
     * @param path 路径。
     * @return 路径上的数据，如果路径不存在，则返回null。
     */
    public static byte[] getData(String path) {
        return client.getData(path);
    }
    
    /**
     * 获取指定路径上的数据。
     * @param path 路径。
     * @param decompress 是否需要进行解压缩。
     * @param giveRawIfDecompressFailed 如果解压失败，true返回原始数据，false返回空白。不确定数据是否被压缩时可设为true，确定被压缩过一般应设为false。
     * @return 路径上的数据，如果路径不存在，则返回null。
     */
    public static byte[] getData(String path, boolean decompress, boolean giveRawIfDecompressFailed) {
        return client.getData(path, decompress, giveRawIfDecompressFailed);
    }
    
    /**
     * 从本地缓存中获取指定路径上的数据，调用该方法前应该先调用{@link #watchData(String, NodeDataCacheListener...)}。
     * @param path 路径。
     * @return 路径上的数据，如果没有调用{@link #watchData(String, NodeDataCacheListener...)}开始监视，则返回null。
     */
    public static String getDataFromCache(String path) {
        return client.getDataFromCache(path);
    }
    
    /**
     * 在指定路径上设置数据，如果路径不存在，会创建路径。
     * @param path 路径。
     * @param data 数据，如果传入null，则设置空白数据""。
     */
    public static void setData(String path, String data) {
        client.setData(path, data);
    }
    
    /**
     * 在指定路径上设置数据，如果路径不存在，会创建路径。
     * @param path 路径。
     * @param data 数据，如果传入null，则设置空白数据""。
     * @param compress 是否需要进行压缩。
     */
    public static void setData(String path, String data, boolean compress) {
        client.setData(path, data, compress);
    }
    
    /**
     * 在指定路径上设置数据，如果路径不存在，会创建路径。
     * @param path 路径。
     * @param data 数据，如果传入null，则设置空白数据byte[0]。
     */
    public static void setData(String path, byte[] data) {
        client.setData(path, data);
    }
    
    /**
     * 在指定路径上设置数据，如果路径不存在，会创建路径。
     * @param path 路径。
     * @param data 数据，如果传入null，则设置空白数据byte[0]。
     * @param compress 是否需要进行压缩。
     */
    public static void setData(String path, byte[] data, boolean compress) {
        client.setData(path, data, compress);
    }
    
    /**
     * 在指定路径上设置数据，如果路径不存在，会创建路径。
     * @param path 路径。
     * @param data 数据，如果传入null，则设置空白数据""。
     * @param createMode 要创建的路径的类型。
     */
    public static void setData(String path, String data, CreateMode createMode) {
        client.setData(path, data, createMode);
    }
    
    /**
     * 在指定路径上设置数据，如果路径不存在，会创建路径。
     * @param path 路径。
     * @param data 数据，如果传入null，则设置空白数据""。
     * @param compress 是否需要进行压缩。
     * @param createMode 要创建的路径的类型。
     */
    public static void setData(String path, String data, boolean compress, CreateMode createMode) {
        client.setData(path, data, compress, createMode);
    }
    
    /**
     * 在指定路径上设置数据，如果路径不存在，会创建路径。
     * @param path 路径。
     * @param data 数据，如果传入null，则设置空白数据byte[0]。
     * @param createMode 要创建的路径的类型。
     */
    public static void setData(String path, byte[] data, CreateMode createMode) {
        client.setData(path, data, createMode);
    }
    
    /**
     * 在指定路径上设置数据，如果路径不存在，会创建路径。
     * @param path 路径。
     * @param data 数据，如果传入null，则设置空白数据byte[0]。
     * @param createMode 要创建的路径的类型。
     * @param compress 是否需要进行压缩。
     */
    public static void setData(String path, byte[] data, boolean compress, CreateMode createMode) {
        client.setData(path, data, compress, createMode);
    }
    
    /**
     * 锁定一个路径，并在持有锁的过程中执行业务逻辑，完成后释放锁。如果获取锁失败，则抛出异常。
     * @param path 要锁定的路径。
     * @param time 等待锁的最长时间。
     * @param unit 等待锁的时间的单位。
     * @param lockCallback 业务逻辑回调。
     * @return 业务执行的返回结果，业务的该结果可以通过{@link ResultContainer#setResult(Object)}设置在{@code lockCallback}中。
     */
    public static <T> T lockAndWork(String path, long time, TimeUnit unit, ZooKeeperLockCallback<T> lockCallback) {
        return client.lockAndWork(path, time, unit, lockCallback);
    }
    
    /**
     * 监视指定路径上的数据，添加业务的监视器。
     * @param path 路径。
     * @param listeners 业务实现的监视器。
     */
    public static void watchData(String path, NodeDataCacheListener... listeners) {
        client.watchData(path, listeners);
    }
    
    /**
     * 移除对指定路径上数据的监视器。
     * @param path 路径。
     * @param listeners 要移除的监视器。
     */
    public static void removeWatchDataListener(String path, NodeDataCacheListener... listeners) {
        client.removeWatchDataListener(path, listeners);
    }
    
    /**
     * 取消对指定路径上数据的监视。
     * @param path 路径。
     */
    public static void unregisterDataWatch(String path) {
        client.unregisterDataWatch(path);
    }
    
    /**
     * 监视指定路径上的子路径，添加业务的监视器。
     * @param path 路径。
     * @param listeners 业务实现的监视器。
     * @return 当前的数据。
     */
    public static void watchPathChildren(String path, PathChildrenCacheEventListener... listeners) {
        client.watchPathChildren(path, listeners);
    }
    
    /**
     * 移除对指定路径上对子路径的监视器。
     * @param path 路径。
     * @param listeners 要移除的监视器。
     */
    public static void removeWatchPathChildrenListener(String path, PathChildrenCacheEventListener... listeners) {
        client.removeWatchPathChildrenListener(path, listeners);
    }
    
    /**
     * 取消对指定路径上子路径的监视。
     * @param path 路径。
     */
    public static void unregisterPathChildrenWatch(String path) {
        client.unregisterPathChildrenWatch(path);
    }
    
    /**
     * 监视指定路径上的本节点是否为主导节点，添加业务的监视器。
     * @param path 路径。
     * @param listeners 业务实现的监视器。
     */
    public static void addLeaderLatchEventListener(String path, LeaderLatchEventListener... listeners) {
        client.addLeaderLatchEventListener(path, listeners);
    }
    
    /**
     * 移除对指定路径上本节点是否为主导节点的监视器。
     * @param path 路径。
     * @param listeners 要移除的监视器。
     */
    public static void removeLeaderLatchEventListener(String path, LeaderLatchEventListener... listeners) {
        client.removeLeaderLatchEventListener(path, listeners);
    }
    
    /**
     * 指定路径上本节点是否为主导节点，如果尚未参与主导节点竞争则会加入竞争。
     * @param path 路径。
     * @return true表示为主导节点。
     */
    public static boolean thisNodeIsLeader(String path) {
        return client.thisNodeIsLeader(path);
    }
    
    /**
     * 指定路径上本节点是否为主导节点。
     * @param path 路径。
     * @param joinCompetitionIfNecessary 为true将会参与主导节点竞争，false只做检查，没有竞争即返回false。
     * @return true表示为主导节点。
     */
    public static boolean thisNodeIsLeader(String path, boolean joinCompetitionIfNecessary) {
        return client.thisNodeIsLeader(path, joinCompetitionIfNecessary);
    }
    
    /**
     * 取消对指定路径上本节点是否为主导节点的监视。
     * @param path 路径。
     */
    public static void closeLeaderLatch(String path) {
        client.closeLeaderLatch(path);
    }
    
    /**
     * 当作为主导节点时执行任务。
     * <b>注意：只在作为主导节点时执行一次。</b>
     * @param path 竞争主导节点的路径。
     * @param runnable 任务。
     * @throws java.io.EOFException 等待过程中竞争器被关闭了则会抛出该异常。
     * @throws InterruptedException 等待被打断则会抛出该异常。
     */
    public static void executeAsLeadership(String path, Runnable runnable) throws EOFException,
            InterruptedException {
        client.executeAsLeadership(path, runnable);
    }
    
    /**
     * 当作为主导节点时执行任务。
     * <b>注意：只在作为主导节点时执行一次。</b>
     * @param path 竞争主导节点的路径。
     * @param runnable 任务。
     * @param timeUnit 等待时间。
     * @throws java.io.EOFException 等待过程中竞争器被关闭了则会抛出该异常。
     * @throws InterruptedException 等待被打断则会抛出该异常。
     */
    public static void executeAsLeadership(String path, Runnable runnable, long timeout, TimeUnit timeUnit)
            throws EOFException, InterruptedException {
        client.executeAsLeadership(path, runnable, timeout, timeUnit);
    }
    
}
