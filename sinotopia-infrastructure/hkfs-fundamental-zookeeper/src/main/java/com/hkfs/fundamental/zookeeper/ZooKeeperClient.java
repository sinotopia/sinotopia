package com.hkfs.fundamental.zookeeper;

import com.hkfs.fundamental.zookeeper.bean.ConcurrentHashSet;
import com.hkfs.fundamental.zookeeper.bean.ResultContainer;
import com.hkfs.fundamental.zookeeper.callback.ZooKeeperLockCallback;
import com.hkfs.fundamental.zookeeper.listener.LeaderLatchEventListener;
import com.hkfs.fundamental.zookeeper.listener.NodeDataCacheListener;
import com.hkfs.fundamental.zookeeper.listener.PathChildrenCacheEventListener;
import com.hkfs.fundamental.zookeeper.listener.ZooKeeperClientConnectionStateListener;
import com.hkfs.fundamental.zookeeper.utils.ZookeeperModuleUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.framework.recipes.leader.Participant;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>ZooKeeper客户端。</p>
 * @author dzr
 * @Date 2016/04/25
 */
public class ZooKeeperClient {
    
    /**
     * 路径分割字符。
     */
    public static final char PATH_SEPARATOR_CHAR = '/';
    
    public static final int DEFAULT_BASE_SLEEP_TIME_MS = 1000;
    
    public static final int DEFAULT_MAX_RETRIES = 0;
    
    private static final Logger LOG = LoggerFactory.getLogger(ZooKeeperClient.class);
    
    private ExecutorService zkNodeEventNotifierExecutor = ZookeeperModuleUtils
            .getCachedThreadPool("ZooKeeperNodeEventNotifier");

    private ExecutorService pathChildrenCacheHandlerExecutor = ZookeeperModuleUtils
            .getCachedThreadPool("pathChildrenCacheHandler");
    
    private CuratorFramework client;
    
    private ConnectionState connectionState;
    
    private Set<ZooKeeperClientConnectionStateListener> connectionStateListeners = new ConcurrentHashSet<ZooKeeperClientConnectionStateListener>();
    
    private Map<String, NodeCacheHandler> nodeCacheHandlerMap = new HashMap<String, NodeCacheHandler>();
    
    private Lock nodeCacheHandlerMapLock = new ReentrantLock();
    
    private Map<String, PathChildrenCacheHandler> pathChildrenCacheHandlerMap = new HashMap<String, PathChildrenCacheHandler>();
    
    private Lock pathChildrenCacheHandlerMapLock = new ReentrantLock();
    
    private Map<String, LeaderLatchHandler> leaderLatchHandlerMap = new HashMap<String, LeaderLatchHandler>();
    
    private Lock leaderLatchHandlerMapLock = new ReentrantLock();
    
    /**
     * 创建ZooKeeper客户端。
     * @param connectString 服务器连接串。
     * @param retryPolicy 重试策略。
     */
    public ZooKeeperClient(String connectString, RetryPolicy retryPolicy) {
        client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
    }
    
    /**
     * 创建ZooKeeper客户端。
     * @param connectString 服务器连接串。
     * @param baseSleepTimeMs 重试间隔时间。
     * @param maxRetries 最大重试次数。
     */
    public ZooKeeperClient(String connectString, int baseSleepTimeMs, int maxRetries) {
        client = CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(baseSleepTimeMs,
                maxRetries));
    }
    
    /**
     * 创建ZooKeeper客户端。
     * @param connectString 服务器连接串。
     */
    public ZooKeeperClient(String connectString) {
        client = CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(
                DEFAULT_BASE_SLEEP_TIME_MS, DEFAULT_MAX_RETRIES));
    }
    
    /**
     * 启动该ZooKeeper客户端。
     */
    public void start() {
        // 先确认状态，如果已经启动或关闭时调用start会抛异常
        if (CuratorFrameworkState.LATENT == client.getState()) {
            client.getConnectionStateListenable().addListener(new ClientConnectionStateListener());
            client.start();
        }
    }
    
    /**
     * 关闭该ZooKeeper客户端。
     */
    public void close() {
        LOG.info("Close Zookeeper Client Instance ...");
        client.close();
    }


    private class ClientConnectionStateListener implements ConnectionStateListener {
        
        @Override
        public void stateChanged(CuratorFramework client, ConnectionState newState) {
            connectionState = newState;
            notifyConnectionStateListener(newState);
        }
        
    }
    
    private void notifyConnectionStateListener(ConnectionState state) {
        if (state.isConnected()) {
            for (ZooKeeperClientConnectionStateListener listener : connectionStateListeners) {
                listener.notifyConnected();
            }
        } else {
            for (ZooKeeperClientConnectionStateListener listener : connectionStateListeners) {
                listener.notifyDisconnected();
            }
        }
    }
    
    private void notifyConnectionStateListener(ConnectionState state,
            ZooKeeperClientConnectionStateListener listener) {
        if (state.isConnected()) {
            listener.notifyConnected();
        } else {
            listener.notifyDisconnected();
        }
    }
    
    /**
     * 添加客户端连接状态监听
     * @param listener 客户端连接状态监听器
     */
    public void addClientConnectionStateListener(ZooKeeperClientConnectionStateListener listener) {
        // 任何情况下都添加，启动后才通知
        if (connectionStateListeners.add(listener) && CuratorFrameworkState.STARTED == client.getState()) {
            notifyConnectionStateListener(connectionState, listener);
        }
    }
    
    /**
     * 删除客户端连接状态监听
     * @param listener 客户端连接状态监听器
     */
    public void removeClientConnectionStateListener(ZooKeeperClientConnectionStateListener listener) {
        connectionStateListeners.remove(listener);
    }
    
    public CuratorFramework getClient() {
        return client;
    }
    
    /**
     * 创建指定路径，如果父路径不存在则抛出异常。
     * @param path 路径。
     */
    public void create(String path) {
        create(path, CreateMode.PERSISTENT);
    }
    
    /**
     * 创建指定路径，如果父路径不存在则抛出异常。
     * @param path 路径。
     * @param createMode 要创建的路径的类型。
     */
    public void create(String path, CreateMode createMode) {
        // 先检查路径是否存在，如果已存在则不做操作
        if (!exists(path)) {
            try {
                client.create().withMode(createMode).forPath(path, ZookeeperModuleUtils.EMPTY_BYTES);
            } catch (Exception e) {
                String message = "create path failed, path=" + path;
                LOG.error(message, e);
                throw new RuntimeException(message, e);
            }
        }
    }
    
    /**
     * 创建路径，如果父路径不存在，会自动创建。
     * @param path 路径。
     */
    public void mkdirs(String path) {
        mkdirs(path, CreateMode.PERSISTENT);
    }
    
    /**
     * 创建路径，如果父路径不存在，会自动创建。
     * @param path 路径。
     * @param createMode 要创建的路径的类型。
     */
    public void mkdirs(String path, CreateMode createMode) {
        // 先检查路径是否存在，如果已存在则不做操作
        if (!exists(path)) {
            try {
                client.create().creatingParentsIfNeeded().withMode(createMode).forPath(path, ZookeeperModuleUtils.EMPTY_BYTES);
            } catch (Exception e) {
                String message = "create path failed, path=" + path;
                LOG.error(message, e);
                throw new RuntimeException(message, e);
            }
        }
    }
    
    public static String buildFullPath(String... pathSegs) {
        // 创建builder
        StringBuilder sBuilder = new StringBuilder();
        if (null != pathSegs && 0 < pathSegs.length) {
            for (String pathSeg : pathSegs) {
                if (!ZookeeperModuleUtils.isEmpty(pathSeg)) {
                    // 去除开头的路径分隔符
                    pathSeg = ZooKeeperClient.ensureChildPathRelative(pathSeg);
                    // 去除结尾的路径分隔符
                    pathSeg = ZooKeeperClient.ensurePathNotEndWithPathSeparator(pathSeg);
                    // 处理后仍然非空的则组装
                    if (!pathSeg.isEmpty()) {
                        sBuilder.append(ZooKeeperClient.PATH_SEPARATOR_CHAR).append(pathSeg);
                    }
                }
            }
        }
        // 如果没有任何路径被组装，则返回根路径
        if (0 == sBuilder.length()) {
            sBuilder.append(ZooKeeperClient.PATH_SEPARATOR_CHAR);
        }
        return sBuilder.toString();
    }
    
    public static String appendPath(String basePath, String followingPath) {
        if (null != followingPath && !followingPath.isEmpty()) {
            // 去除开头的路径分隔符
            followingPath = ensureChildPathRelative(followingPath);
            // 去除结尾的路径分隔符
            followingPath = ensurePathNotEndWithPathSeparator(followingPath);
            // 处理后仍然非空的则组装
            if (!followingPath.isEmpty()) {
                StringBuilder sBuilder = new StringBuilder(basePath.length() + 1 + followingPath.length())
                        .append(basePath);
                if (!basePath.isEmpty() && PATH_SEPARATOR_CHAR != basePath.charAt(basePath.length() - 1)) {
                    sBuilder.append(PATH_SEPARATOR_CHAR);
                }
                sBuilder.append(followingPath);
                return sBuilder.toString();
            }
        }
        return basePath;
    }
    
    public static String ensureChildPathRelative(String path) {
        if (null != path && !path.isEmpty()) {
            int len = path.length();
            int idx = 0;
            while (len > idx && PATH_SEPARATOR_CHAR == path.charAt(idx)) {
                ++idx;
            }
            if (idx == len) {
                path = "";
            } else if (idx != 0) {
                path = path.substring(idx);
            }
        }
        return path;
    }
    
    public static String ensurePathNotEndWithPathSeparator(String path) {
        if (null != path && !path.isEmpty()) {
            int idx = path.length() - 1;
            while (0 <= idx && PATH_SEPARATOR_CHAR == path.charAt(idx)) {
                idx = idx - 1;
            }
            if (idx < 0) {
                return "";
            } else if (idx != path.length() - 1) {
                path = path.substring(0, idx + 1);
            }
        }
        return path;
    }
    
    /**
     * 创建路径，如果父路径不存在，会自动创建。
     * @param parentPath 父路径。
     * @param childPath 子路径名。
     */
    public void mkdirs(String parentPath, String childPath) {
        mkdirs(parentPath, childPath, CreateMode.PERSISTENT);
    }
    
    /**
     * 创建路径，如果父路径不存在，会自动创建。
     * @param parentPath 父路径。
     * @param childPath 子路径名。
     * @param createMode 要创建的路径的类型。
     */
    public void mkdirs(String parentPath, String childPath, CreateMode createMode) {
        String path = parentPath + PATH_SEPARATOR_CHAR + ensureChildPathRelative(childPath);
        mkdirs(path, createMode);
    }
    
    /**
     * 删除指定路径，会递归删除其下子路径，如果路径不存在则不操作。
     * @param path 路径。
     */
    public void delete(String path) {
        // 先检查路径是否存在，如果不存在则不做操作
        if (exists(path)) {
            // 递归删除子路径
            delete(path, true);
        }
    }
    
    /**
     * 删除指定路径，如果路径不存在，则抛出异常。
     * @param path 路径。
     * @param clearChildren 如果为true，则递归删除子路径，否则只删除指定路径，如果路径非空，则抛出异常。
     */
    public void delete(String path, boolean clearChildren) {
        try {
            if (clearChildren) {
                // 列出所有子路径
                List<String> children = client.getChildren().forPath(path);
                if (!children.isEmpty()) {
                    // 遍历子路径操作
                    for (String childPath : children) {
                        // 递归调用删除子路径
                        delete(path + PATH_SEPARATOR_CHAR + childPath, true);
                    }
                }
            }
            // 删除本路径
            client.delete().forPath(path);
        } catch (Exception e) {
            String message = "delete path failed, path=" + path;
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        }
    }
    
    /**
     * 检查指定路径是否存在。
     * @param path 路径。
     * @return 如果存在则返回true，否则返回false。
     */
    public boolean exists(String path) {
        try {
            // 如果返回的Stat不为null则表示该路径存在
            return null != client.checkExists().forPath(path);
        } catch (Exception e) {
            String message = "check exists failed, path=" + path;
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        }
    }
    
    /**
     * 获取指定路径中的子路径名。
     * @param path 要获取子路径的父路径。
     * @return 子路径名，如果父路径不存在，则返回空白列表。
     */
    public List<String> getChildren(String path) {
        try {
            // 先检查路径是否存在，如果存在则获取子路径名
            if (exists(path)) {
                return client.getChildren().forPath(path);
            } else {// 如果该路径不存在则返回空列表
                return new ArrayList<String>();
            }
        } catch (Exception e) {
            String message = "get children failed, path=" + path;
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        }
    }
    
    /**
     * 获取指定路径上的数据。
     * @param path 路径。
     * @return 路径上的数据，如果路径不存在，则返回null。
     */
    public String getStringData(String path) {
        return getStringData(path, false, true);
    }
    
    /**
     * 获取指定路径上的数据。
     * @param path 路径。
     * @param decompress 是否需要进行解压缩。
     * @param giveRawIfDecompressFailed 如果解压失败，true返回原始数据，false返回空白。不确定数据是否被压缩时可设为true，确定被压缩过一般应设为false。
     * @return 路径上的数据，如果路径不存在，则返回null。
     */
    public String getStringData(String path, boolean decompress, boolean giveRawIfDecompressFailed) {
        byte[] bytes = getData(path, decompress, giveRawIfDecompressFailed);
        return null == bytes ? null : new String(bytes, ZookeeperModuleUtils.CHARSET_UTF8);
    }
    
    /**
     * 获取指定路径上的数据。
     * @param path 路径。
     * @return 路径上的数据，如果路径不存在，则返回null。
     */
    public byte[] getData(String path) {
        return getData(path, false, true);
    }
    
    /**
     * 获取指定路径上的数据。
     * @param path 路径。
     * @param decompress 是否需要进行解压缩。
     * @param giveRawIfDecompressFailed 如果解压失败，true返回原始数据，false返回空白。不确定数据是否被压缩时可设为true，确定被压缩过一般应设为false。
     * @return 路径上的数据，如果路径不存在，则返回null。
     */
    public byte[] getData(String path, boolean decompress, boolean giveRawIfDecompressFailed) {
        try {
            // 先检查路径是否存在，如果存在则获取节点数据
            if (exists(path)) {
                byte[] bytes = client.getData().forPath(path);
                if (null == bytes) {
                    return null;
                }
                if (decompress && 0 < bytes.length) {
                    bytes = ZookeeperModuleUtils.ungzip(bytes, giveRawIfDecompressFailed);
                }
                return bytes;
            } else {// 如果路径不存在则返回null
                return null;
            }
        } catch (Exception e) {
            String message = "get data failed, path=" + path;
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        }
    }
    
    /**
     * 从本地缓存中获取指定路径上的数据，调用该方法前应该先调用{@link #watchData(String, NodeDataCacheListener...)}。
     * @param path 路径。
     * @return 路径上的数据，如果没有调用{@link #watchData(String, NodeDataCacheListener...)}开始监视，则返回null。
     */
    public String getDataFromCache(String path) {
        // 先确认是否已注册监视
        NodeCacheHandler nodeCacheHandler = nodeCacheHandlerMap.get(path);
        if (null != nodeCacheHandler) {// 有注册监视则返回当前数据
            return nodeCacheHandler.getStringData();
        } else {// 没有注册监视则返回null
            return null;
        }
    }
    
    /**
     * 在指定路径上设置数据，如果路径不存在，会创建路径。
     * @param path 路径。
     * @param data 数据，如果传入null，则设置空白数据""。
     */
    public void setData(String path, String data) {
        setData(path, data, false);
    }
    
    /**
     * 在指定路径上设置数据，如果路径不存在，会创建路径。
     * @param path 路径。
     * @param data 数据，如果传入null，则设置空白数据""。
     * @param compress 是否需要进行压缩。
     */
    public void setData(String path, String data, boolean compress) {
        setData(path, data, compress, CreateMode.PERSISTENT);
    }
    
    /**
     * 在指定路径上设置数据，如果路径不存在，会创建路径。
     * @param path 路径。
     * @param data 数据，如果传入null，则设置空白数据byte[0]。
     */
    public void setData(String path, byte[] data) {
        setData(path, data, CreateMode.PERSISTENT);
    }
    
    /**
     * 在指定路径上设置数据，如果路径不存在，会创建路径。
     * @param path 路径。
     * @param data 数据，如果传入null，则设置空白数据byte[0]。
     * @param compress 是否需要进行压缩。
     */
    public void setData(String path, byte[] data, boolean compress) {
        setData(path, data, compress, CreateMode.PERSISTENT);
    }
    
    /**
     * 在指定路径上设置数据，如果路径不存在，会创建路径。
     * @param path 路径。
     * @param data 数据，如果传入null，则设置空白数据""。
     * @param createMode 要创建的路径的类型。
     */
    public void setData(String path, String data, CreateMode createMode) {
        setData(path, data, false, createMode);
    }
    
    /**
     * 在指定路径上设置数据，如果路径不存在，会创建路径。
     * @param path 路径。
     * @param data 数据，如果传入null，则设置空白数据""。
     * @param compress 是否需要进行压缩。
     * @param createMode 要创建的路径的类型。
     */
    public void setData(String path, String data, boolean compress, CreateMode createMode) {
        setData(path, null == data ? ZookeeperModuleUtils.EMPTY_BYTES : data.getBytes(ZookeeperModuleUtils.CHARSET_UTF8), compress, createMode);
    }
    
    /**
     * 在指定路径上设置数据，如果路径不存在，会创建路径。
     * @param path 路径。
     * @param data 数据，如果传入null，则设置空白数据byte[0]。
     * @param createMode 要创建的路径的类型。
     */
    public void setData(String path, byte[] data, CreateMode createMode) {
        setData(path, data, false, createMode);
    }
    
    /**
     * 在指定路径上设置数据，如果路径不存在，会创建路径。
     * @param path 路径。
     * @param data 数据，如果传入null，则设置空白数据byte[0]。
     * @param createMode 要创建的路径的类型。
     * @param compress 是否需要进行压缩。
     */
    public void setData(String path, byte[] data, boolean compress, CreateMode createMode) {
        try {
            // 先确认路径是否存在，如果路径不存在则创建路径
            long s = 0;
            if (LOG.isTraceEnabled()) {
                s = System.currentTimeMillis();
            }
            if (!exists(path)) {
                mkdirs(path, createMode);
            }
            if (LOG.isTraceEnabled()) {
                LOG.trace("check spend " + (System.currentTimeMillis() - s));
                s = System.currentTimeMillis();
            }
            // 向节点设置数据，如果数据为null，则设置空白数据byte[0]
            if (null == data) {
                data = ZookeeperModuleUtils.EMPTY_BYTES;
            }
            if (compress) {
                data = ZookeeperModuleUtils.gzip(data);
            }
            client.setData().forPath(path, data);
            if (LOG.isTraceEnabled()) {
                LOG.trace("set spend " + (System.currentTimeMillis() - s));
            }
        } catch (Exception e) {
            String message = "set data failed, path=" + path;
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        }
    }
    
    /**
     * 锁定一个路径，并在持有锁的过程中执行业务逻辑，完成后释放锁。如果获取锁失败，则抛出异常。
     * @param path 要锁定的路径。
     * @param time 等待锁的最长时间。
     * @param unit 等待锁的时间的单位。
     * @param lockCallback 业务逻辑回调。
     * @return 业务执行的返回结果，业务的该结果可以通过{@link ResultContainer#setResult(Object)}设置在{@code lockCallback}中。
     */
    public <T> T lockAndWork(String path, long time, TimeUnit unit, ZooKeeperLockCallback<T> lockCallback) {
        // 准备一个结果容器
        ResultContainer<T> resultContainer = new DefaultResultContainer<T>();
        // 创建锁
        InterProcessMutex mutex = new InterProcessMutex(client, path);
        boolean locked = false;
        try {
            // 尝试锁定
            if (locked = mutex.acquire(time, unit)) {
                // 获取锁成功
                lockCallback.doWork(path, resultContainer, this);
            }
        } catch (Exception e) {
            // 执行错误，抛出异常
            String message = "acquire lock failed, path=" + path;
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        } finally {
            // 如果锁定成功，则解锁
            if (locked) {
                try {
                    mutex.release();
                } catch (Exception e) {
                    String message = "release lock failed, path=" + path;
                    LOG.error(message, e);
                    throw new RuntimeException(message, e);
                }
            }
        }
        // 获取锁失败，抛出异常
        if (!locked) {
            String message = "acquire lock failed, path=" + path;
            LOG.error(message);
            throw new RuntimeException(message);
        }
        return resultContainer.getResult();
    }
    
    private static class DefaultResultContainer<T> implements ResultContainer<T> {
        
        private T result;
        
        @Override
        public void setResult(T result) {
            this.result = result;
        }
        
        @Override
        public T getResult() {
            return result;
        }
        
    }
    
    private class NodeCacheHandler implements NodeCacheListener {
        
        private String path;
        
        private NodeCache nodeCache;
        
        private boolean started;
        
        private Set<NodeDataCacheListener> registeredListeners = new ConcurrentHashSet<NodeDataCacheListener>();
        
        public NodeCacheHandler(String path, NodeCache nodeCache) {
            this.path = path;
            this.nodeCache = nodeCache;
            this.nodeCache.getListenable().addListener(this);
            if (LOG.isDebugEnabled()) {
                LOG.debug("NodeCacheHandler created, path: " + path);
            }
        }
        
        public String getStringData() {
            byte[] bytes = getData();
            String data = null == bytes ? null : new String(bytes, ZookeeperModuleUtils.CHARSET_UTF8);
            return data;
        }
        
        public byte[] getData() {
            ChildData dataObj = nodeCache.getCurrentData();
            return null == dataObj ? null : dataObj.getData();
        }
        
        public void startWatch() {
            if (!started) {
                try {
                    nodeCache.start(true);
                    started = true;
                } catch (Exception e) {
                    LOG.error("start watch failed", e);
                }
            }
        }
        
        public void stopWatch() {
            if (started) {
                try {
                    nodeCache.close();
                    started = false;
                } catch (IOException e) {
                    LOG.error("stop watch failed", e);
                }
            }
        }
        
        public void destroy() {
            stopWatch();
            clearListener();
        }
        
        @Override
        public void nodeChanged() throws Exception {
            if (registeredListeners.isEmpty()) {
                return;
            }
            final byte[] data = getData();
            zkNodeEventNotifierExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (NodeDataCacheListener listener : registeredListeners) {
                            listener.nodeChanged(path, data);
                        }
                    } catch (Exception e) {
                        LOG.error("notify nodeChanged failed", e);
                    }
                }
            });
        }
        
        public void addListener(NodeDataCacheListener... listeners) {
            if (null != listeners && 0 < listeners.length) {
                byte[] bytes = getData();
                for (NodeDataCacheListener listener : listeners) {
                    if (registeredListeners.add(listener)) {
                        if (LOG.isInfoEnabled()) {
                            LOG.info(listener + " added to nodeWatching " + path);
                        }
                        listener.nodeChanged(path, bytes);
                    }
                }
            }
        }
        
        public void removeListener(NodeDataCacheListener... listeners) {
            if (null != listeners && 0 < listeners.length) {
                for (NodeDataCacheListener listener : listeners) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info(listener + " removed from nodeWatching " + path);
                    }
                    registeredListeners.remove(listener);
                }
            }
        }
        
        public void clearListener() {
            registeredListeners.clear();
            LOG.info("listeners cleared from nodeWatching " + path);
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((path == null) ? 0 : path.hashCode());
            return result;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof NodeCacheHandler)) {
                return false;
            }
            NodeCacheHandler other = (NodeCacheHandler) obj;
            if (!getOuterType().equals(other.getOuterType())) {
                return false;
            }
            if (path == null) {
                if (other.path != null) {
                    return false;
                }
            } else if (!path.equals(other.path)) {
                return false;
            }
            return true;
        }
        
        private ZooKeeperClient getOuterType() {
            return ZooKeeperClient.this;
        }
        
        @Override
        public String toString() {
            return "NodeCacheHandler [path=" + path + "]";
        }
        
    }
    
    /**
     * 监视指定路径上的数据，添加业务的监视器。
     * @param path 路径。
     * @param listeners 业务实现的监视器。
     */
    public void watchData(String path, NodeDataCacheListener... listeners) {
        if (null != listeners && 0 < listeners.length) {
            // 获取节点数据缓存处理器，如果之前没有就创建并且启动
            NodeCacheHandler nodeCacheHandler = getNodeCacheHandler(path);
            // 注册业务监视器
            nodeCacheHandler.addListener(listeners);
        }
    }
    
    /**
     * 移除对指定路径上数据的监视器。
     * @param path 路径。
     * @param listeners 要移除的监视器。
     */
    public void removeWatchDataListener(String path, NodeDataCacheListener... listeners) {
        if (null != listeners && 0 < listeners.length) {
            NodeCacheHandler nodeCacheHandler = nodeCacheHandlerMap.get(path);
            if (null != nodeCacheHandler) {
                nodeCacheHandler.removeListener(listeners);
            }
        }
    }
    
    /**
     * 取消对指定路径上数据的监视。
     * @param path 路径。
     */
    public void unregisterDataWatch(String path) {
        LOG.info("unregister data watch at path: " + path);
        NodeCacheHandler nodeCacheHandler = nodeCacheHandlerMap.get(path);
        if (null != nodeCacheHandler) {
            nodeCacheHandler.destroy();
            nodeCacheHandlerMap.remove(path);
        }
    }
    
    private NodeCacheHandler getNodeCacheHandler(String path) {
        NodeCacheHandler nodeCacheHandler = nodeCacheHandlerMap.get(path);
        boolean newCreated = false;
        if (null == nodeCacheHandler) {
            nodeCacheHandlerMapLock.lock();
            try {
                nodeCacheHandler = nodeCacheHandlerMap.get(path);
                if (null == nodeCacheHandler) {
                    // 创建节点数据缓存对象
                    NodeCache nodeCache = new NodeCache(client, path);
                    // 创建节点数据缓存处理器
                    nodeCacheHandler = new NodeCacheHandler(path, nodeCache);
                    // 启动监视
                    nodeCacheHandler.startWatch();
                    nodeCacheHandlerMap.put(path, nodeCacheHandler);
                    newCreated = true;
                }
            } finally {
                nodeCacheHandlerMapLock.unlock();
            }
        }
        if (newCreated && LOG.isDebugEnabled()) {
            LOG.debug("NodeCacheHandler created for path " + path);
        }
        return nodeCacheHandler;
    }
    
    private static final AtomicReference<PathChildrenCacheHandler> DUMMY_PATH_CHILD_CACHE_HANDLER = new AtomicReference<PathChildrenCacheHandler>();
    
    private PathChildrenCacheHandler getDummyPathChildrenCacheHandler() {
        PathChildrenCacheHandler handler = DUMMY_PATH_CHILD_CACHE_HANDLER.get();
        if (null == handler) {
            handler = new PathChildrenCacheHandler();
            DUMMY_PATH_CHILD_CACHE_HANDLER.set(handler);
        }
        return handler;
    }
    
    private class PathChildrenCacheHandler implements PathChildrenCacheListener {
        
        private String path;
        
        private PathChildrenCache pathChildrenCache;
        
        private boolean started;
        
        private Set<PathChildrenCacheEventListener> registeredListeners = new ConcurrentHashSet<PathChildrenCacheEventListener>();
        
        private Set<PathChildrenCacheEventListener> recursionWatchListeners = new ConcurrentHashSet<PathChildrenCacheEventListener>();
        
        private Map<String, PathChildrenCacheHandler> childHandlers = new ConcurrentHashMap<String, PathChildrenCacheHandler>();
        
        public PathChildrenCacheHandler(String path, PathChildrenCache pathChildrenCache) {
            this.path = path;
            this.pathChildrenCache = pathChildrenCache;
            this.pathChildrenCache.getListenable().addListener(this);
            if (LOG.isDebugEnabled()) {
                LOG.debug("PathChildrenCacheHandler created, path: " + path);
            }
        }
        
        protected PathChildrenCacheHandler() {
            path = "";
        }
        
        public void startWatch() {
            if (!started) {
                try {
                    pathChildrenCache.start(StartMode.BUILD_INITIAL_CACHE);
                    started = true;
                } catch (Exception e) {
                    LOG.error("start watch failed", e);
                }
            }
        }
        
        public void stopWatch() {
            if (started) {
                try {
                    pathChildrenCache.close();
                    started = false;
                } catch (IOException e) {
                    LOG.error("stop watch failed", e);
                }
            }
        }
        
        public void destroy() {
            stopWatch();
            clearListener();
        }
        
        private void addChildPath(String childPath) {
            // 如果路径没有添加过，那么处理，如果已经添加了，那么往子handler上注册listener的操作由本层的addRecursionWatchListener完成
            if (!childHandlers.containsKey(childPath)) {
                PathChildrenCacheHandler childHandler = null;
                // 当前有recursionWatchListener才需要获取到PathChildrenCacheHandler，否则只需记录路径
                if (!recursionWatchListeners.isEmpty()) {
                    childHandler = getPathChildrenCacheHandler(childPath);
                    // 把recursionWatchListener注册到子handler上
                    childHandler.addListener(recursionWatchListeners
                            .toArray(new PathChildrenCacheEventListener[recursionWatchListeners.size()]));
                } else {
                    childHandler = getDummyPathChildrenCacheHandler();
                }
                childHandlers.put(childPath, childHandler);
            }
        }
        
        private void addRecursionWatchListener(PathChildrenCacheEventListener listener) {
            if (recursionWatchListeners.add(listener)) {
                // 遍历当前的子路径
                for (Entry<String, PathChildrenCacheHandler> entry : childHandlers.entrySet()) {
                    // 查看当前是否已获取handler
                    PathChildrenCacheHandler childHandler = entry.getValue();
                    // 如果没有获取就使用该子路径获取并设置到map中
                    if (getDummyPathChildrenCacheHandler() == childHandler) {
                        childHandler = getPathChildrenCacheHandler(entry.getKey());
                        entry.setValue(childHandler);
                    }
                    // 向子路径handler中注册监听器
                    childHandler.addListener(listener);
                }
            }
        }
        
        private void removeRecursionWatchListener(PathChildrenCacheEventListener listener) {
            if (recursionWatchListeners.remove(listener)) {
                // 遍历当前的子路径
                for (Entry<String, PathChildrenCacheHandler> entry : childHandlers.entrySet()) {
                    // 查看当前是否已获取handler
                    PathChildrenCacheHandler childHandler = entry.getValue();
                    // 如果有handler才移除监听器
                    if (getDummyPathChildrenCacheHandler() != childHandler) {
                        // 从子路径handler中移除监听器
                        childHandler.removeListener(listener);
                    }
                }
            }
        }
        
        private void removeChildHandler(String childPath) {
            PathChildrenCacheHandler childHandler = childHandlers.remove(childPath);
            if (null != childHandler) {
                childHandler.destroy();
            }
        }
        
        private void notifyAdded(final String path, final byte[] data, final Stat stat) {
            if (registeredListeners.isEmpty()) {
                return;
            }
            zkNodeEventNotifierExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (PathChildrenCacheEventListener listener : registeredListeners) {
                            listener.childPathAdded(path, data, stat);
                        }
                    } catch (Exception e) {
                        LOG.error("notify added failed", e);
                    }
                }
            });
        }
        
        private void notifyUpdated(final String path, final byte[] data, final Stat stat) {
            if (registeredListeners.isEmpty()) {
                return;
            }
            zkNodeEventNotifierExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (PathChildrenCacheEventListener listener : registeredListeners) {
                            listener.childPathUpdated(path, data, stat);
                        }
                    } catch (Exception e) {
                        LOG.error("notify updated failed", e);
                    }
                }
            });
        }
        
        private void notifyRemoved(final String path, final Stat stat) {
            if (registeredListeners.isEmpty()) {
                return;
            }
            zkNodeEventNotifierExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (PathChildrenCacheEventListener listener : registeredListeners) {
                            listener.childPathRemoved(path, stat);
                        }
                    } catch (Exception e) {
                        LOG.error("notify removed failed", e);
                    }
                }
            });
        }
        
        @Override
        public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
            try {
                ChildData dataObj = event.getData();
                // 对于无ChildData的情况不通知
                if (null == dataObj) {
                    return;
                }
                String path = dataObj.getPath();
                byte[] data = dataObj.getData();
                Stat stat = dataObj.getStat();
                Type type = event.getType();
                switch (type) {
                    case CHILD_ADDED:
                        notifyAdded(path, data, stat);
                        addChildPath(path);
                        break;
                    case CHILD_UPDATED:
                        notifyUpdated(path, data, stat);
                        break;
                    case CHILD_REMOVED:
                        notifyRemoved(path, stat);
                        removeChildHandler(path);
                        break;
                    case CONNECTION_SUSPENDED:
                        if (LOG.isWarnEnabled()) {
                            LOG.warn("connection suspended, watching path: " + path);
                        }
                        // omit current
                        break;
                    case CONNECTION_RECONNECTED:
                        if (LOG.isWarnEnabled()) {
                            LOG.warn("connection reconnected, watching path: " + path);
                        }
                        // omit current
                        break;
                    case CONNECTION_LOST:
                        if (LOG.isWarnEnabled()) {
                            LOG.warn("connection lost, watching path: " + path);
                        }
                        // omit current
                        break;
                    case INITIALIZED:
                        if (LOG.isWarnEnabled()) {
                            LOG.warn("connection initialized, watching path: " + path);
                        }
                        // omit current
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                LOG.error("process childEvent failed", e);
            }
        }
        
        private void showCurrentData(PathChildrenCacheEventListener listener) {
            List<ChildData> dataObjs = pathChildrenCache.getCurrentData();
            for (ChildData dataObj : dataObjs) {
                // 对于无ChildData的情况不通知
                if (null == dataObj) {
                    continue;
                }
                String path = dataObj.getPath();
                byte[] data = dataObj.getData();
                Stat stat = dataObj.getStat();
                listener.childPathAdded(path, data, stat);
            }
        }
        
        public void addListener(PathChildrenCacheEventListener... listeners) {
            if (null != listeners && 0 < listeners.length) {
                for (PathChildrenCacheEventListener listener : listeners) {
                    if (registeredListeners.add(listener)) {
                        if (LOG.isInfoEnabled()) {
                            LOG.info(listener + " added to pathWatching " + path);
                        }
                        showCurrentData(listener);
                    }
                    if (listener.isNeedRecursionWatch()) {
                        addRecursionWatchListener(listener);
                    }
                }
            }
        }
        
        public void removeListener(PathChildrenCacheEventListener... listeners) {
            if (null != listeners && 0 < listeners.length) {
                for (PathChildrenCacheEventListener listener : listeners) {
                    if (registeredListeners.remove(listener)) {
                        if (LOG.isInfoEnabled()) {
                            LOG.info(listener + " removed from pathWatching " + path);
                        }
                    }
                    if (listener.isNeedRecursionWatch()) {
                        removeRecursionWatchListener(listener);
                    }
                }
            }
        }
        
        public void clearListener() {
            registeredListeners.clear();
            for (PathChildrenCacheEventListener listener : recursionWatchListeners) {
                removeRecursionWatchListener(listener);
            }
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((path == null) ? 0 : path.hashCode());
            return result;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof PathChildrenCacheHandler)) {
                return false;
            }
            PathChildrenCacheHandler other = (PathChildrenCacheHandler) obj;
            if (!getOuterType().equals(other.getOuterType())) {
                return false;
            }
            if (path == null) {
                if (other.path != null) {
                    return false;
                }
            } else if (!path.equals(other.path)) {
                return false;
            }
            return true;
        }
        
        private ZooKeeperClient getOuterType() {
            return ZooKeeperClient.this;
        }
        
        @Override
        public String toString() {
            return "PathChildrenCacheHandler [path=" + path + "]";
        }
        
    }
    
    /**
     * 监视指定路径上的子路径，添加业务的监视器。
     * @param path 路径。
     * @param listeners 业务实现的监视器。
     * @return 当前的数据。
     */
    public void watchPathChildren(String path, PathChildrenCacheEventListener... listeners) {
        if (null != listeners && 0 < listeners.length) {
            // 获取节点子路径缓存处理器，如果之前没有就创建并且启动
            PathChildrenCacheHandler pathChildrenCacheHandler = getPathChildrenCacheHandler(path);
            // 注册业务监视器
            pathChildrenCacheHandler.addListener(listeners);
        }
    }
    
    /**
     * 移除对指定路径上对子路径的监视器。
     * @param path 路径。
     * @param listeners 要移除的监视器。
     */
    public void removeWatchPathChildrenListener(String path, PathChildrenCacheEventListener... listeners) {
        if (null != listeners && 0 != listeners.length) {
            PathChildrenCacheHandler pathChildrenCacheHandler = pathChildrenCacheHandlerMap.get(path);
            if (null != pathChildrenCacheHandler) {
                pathChildrenCacheHandler.removeListener(listeners);
            }
        }
    }
    
    /**
     * 取消对指定路径上子路径的监视。
     * @param path 路径。
     */
    public void unregisterPathChildrenWatch(String path) {
        LOG.info("unregister data path children at path: " + path);
        PathChildrenCacheHandler pathChildrenCacheHandler = pathChildrenCacheHandlerMap.get(path);
        if (null != pathChildrenCacheHandler) {
            pathChildrenCacheHandler.destroy();
            pathChildrenCacheHandlerMap.remove(path);
        }
    }
    
    private PathChildrenCacheHandler getPathChildrenCacheHandler(String path) {
        PathChildrenCacheHandler pathChildrenCacheHandler = pathChildrenCacheHandlerMap.get(path);
        boolean newCreated = false;
        if (null == pathChildrenCacheHandler) {
            pathChildrenCacheHandlerMapLock.lock();
            try {
                pathChildrenCacheHandler = pathChildrenCacheHandlerMap.get(path);
                if (null == pathChildrenCacheHandler) {
                    // 创建节点数据缓存对象
                    PathChildrenCache pathChildrenCache = new PathChildrenCache(client, path, true, false,
                            pathChildrenCacheHandlerExecutor);
                    // 创建节点子路径缓存处理器
                    pathChildrenCacheHandler = new PathChildrenCacheHandler(path, pathChildrenCache);
                    // 启动监视
                    pathChildrenCacheHandler.startWatch();
                    pathChildrenCacheHandlerMap.put(path, pathChildrenCacheHandler);
                }
            } finally {
                pathChildrenCacheHandlerMapLock.unlock();
            }
        }
        if (newCreated && LOG.isDebugEnabled()) {
            LOG.debug("PathChildrenCacheHandler created for path " + path);
        }
        return pathChildrenCacheHandler;
    }
    
    private class LeaderLatchHandler implements LeaderLatchListener {
        
        private String path;
        
        private LeaderLatch leaderLatch;
        
        private boolean started;
        
        private Set<LeaderLatchEventListener> registeredListeners = new ConcurrentHashSet<LeaderLatchEventListener>();
        
        public LeaderLatchHandler(String path, LeaderLatch leaderLatch) {
            this.path = path;
            this.leaderLatch = leaderLatch;
            this.leaderLatch.addListener(this);
            if (LOG.isDebugEnabled()) {
                LOG.debug("LeaderLatchHandler created, path: " + path);
            }
        }
        
        public void start() {
            if (!started) {
                try {
                    leaderLatch.start();
                    started = true;
                } catch (Exception e) {
                    LOG.error("start leaderLatch failed", e);
                }
            }
        }
        
        public void stop() {
            if (started) {
                try {
                    leaderLatch.close();
                    started = false;
                } catch (IOException e) {
                    LOG.error("stop leaderLatch failed", e);
                }
            }
        }
        
        public void destroy() {
            stop();
            clearListener();
        }
        
        public boolean hasLeadership() {
            return leaderLatch.hasLeadership();
        }
        
        public void awaitForLeadership() throws EOFException, InterruptedException {
            leaderLatch.await();
        }
        
        public void awaitForLeadership(long timeout, TimeUnit timeUnit) throws EOFException, InterruptedException {
            leaderLatch.await(timeout, timeUnit);
        }
        
        @Override
        public void isLeader() {
            if (registeredListeners.isEmpty()) {
                return;
            }
            zkNodeEventNotifierExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (LeaderLatchEventListener listener : registeredListeners) {
                            listener.isLeader();
                        }
                    } catch (Exception e) {
                        LOG.error("notify isLeader failed", e);
                    }
                }
            });
        }
        
        @Override
        public void notLeader() {
            if (registeredListeners.isEmpty()) {
                return;
            }
            zkNodeEventNotifierExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (LeaderLatchEventListener listener : registeredListeners) {
                            listener.notLeader();
                        }
                    } catch (Exception e) {
                        LOG.error("notify notLeader failed", e);
                    }
                }
            });
        }
        
        public void addListener(LeaderLatchEventListener... listeners) {
            if (null != listeners && 0 < listeners.length) {
                boolean hasLeadership = hasLeadership();
                for (LeaderLatchEventListener listener : listeners) {
                    if (registeredListeners.add(listener)) {
                        if (LOG.isInfoEnabled()) {
                            LOG.info(listener + " added to leaderLatchWatching " + path);
                        }
                        if (hasLeadership) {
                            listener.isLeader();
                        } else {
                            listener.notLeader();
                        }
                    }
                }
            }
        }
        
        public void removeListener(LeaderLatchEventListener... listeners) {
            if (null != listeners && 0 < listeners.length) {
                for (LeaderLatchEventListener listener : listeners) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info(listener + " removed from leaderLatchWatching " + path);
                    }
                    registeredListeners.remove(listener);
                }
            }
        }
        
        public void clearListener() {
            registeredListeners.clear();
            LOG.info("listeners cleared from leaderLatchWatching " + path);
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((path == null) ? 0 : path.hashCode());
            return result;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof LeaderLatchHandler)) {
                return false;
            }
            LeaderLatchHandler other = (LeaderLatchHandler) obj;
            if (!getOuterType().equals(other.getOuterType())) {
                return false;
            }
            if (path == null) {
                if (other.path != null) {
                    return false;
                }
            } else if (!path.equals(other.path)) {
                return false;
            }
            return true;
        }
        
        private ZooKeeperClient getOuterType() {
            return ZooKeeperClient.this;
        }
        
        @Override
        public String toString() {
            return "LeaderLatchHandler [path=" + path + "]";
        }

        public LeaderLatch getLeaderLatch() {
            return leaderLatch;
        }

        public String getPath() {
            return path;
        }
    }
    
    /**
     * 监视指定路径上的本节点是否为主导节点，添加业务的监视器。
     * @param path 路径。
     * @param listeners 业务实现的监视器。
     */
    public void addLeaderLatchEventListener(String path, LeaderLatchEventListener... listeners) {
        if (null != listeners && 0 < listeners.length) {
            // 获取主导节点竞争处理器，如果之前没有就创建并且启动
            LeaderLatchHandler leaderLatchHandler = getLeaderLatchHandler(path);
            // 注册业务监视器
            leaderLatchHandler.addListener(listeners);
        }
    }
    
    /**
     * 移除对指定路径上本节点是否为主导节点的监视器。
     * @param path 路径。
     * @param listeners 要移除的监视器。
     */
    public void removeLeaderLatchEventListener(String path, LeaderLatchEventListener... listeners) {
        if (null != listeners && 0 < listeners.length) {
            LeaderLatchHandler leaderLatchHandler = leaderLatchHandlerMap.get(path);
            if (null != leaderLatchHandler) {
                leaderLatchHandler.removeListener(listeners);
            }
        }
    }
    
    /**
     * 指定路径上本节点是否为主导节点，如果尚未参与主导节点竞争则会加入竞争。
     * @param path 路径。
     * @return true表示为主导节点。
     */
    public boolean thisNodeIsLeader(String path) {
        return thisNodeIsLeader(path, true);
    }
    
    public boolean thisNodeIsLeader(String path, String id) {
        return thisNodeIsLeader(path, id, true);
    }
    
    /**
     * 指定路径上本节点是否为主导节点。
     * @param path 路径。
     * @param joinCompetitionIfNecessary 为true将会参与主导节点竞争，false只做检查，没有竞争即返回false。
     * @return true表示为主导节点。
     */
    public boolean thisNodeIsLeader(String path, boolean joinCompetitionIfNecessary) {
        return thisNodeIsLeader(path, null, joinCompetitionIfNecessary);
    }
    
    public boolean thisNodeIsLeader(String path, String id, boolean joinCompetitionIfNecessary) {
    	 if (joinCompetitionIfNecessary) {
             LeaderLatchHandler leaderLatchHandler = getLeaderLatchHandler(path, id);
             return leaderLatchHandler.hasLeadership();
         } else {
             LeaderLatchHandler leaderLatchHandler = leaderLatchHandlerMap.get(path);
             if (null == leaderLatchHandler) {
                 return false;
             } else {
                 return leaderLatchHandler.hasLeadership();
             }
         }
    }

    public Participant getLeaderOfThisNode(String path, String id){
        LeaderLatchHandler leaderLatchHandler = getLeaderLatchHandler(path, id);
        if (null == leaderLatchHandler) {
            return null;
        } else {
            try {
                return leaderLatchHandler.getLeaderLatch().getLeader();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    /**
     * 取消对指定路径上本节点是否为主导节点的监视。
     * @param path 路径。
     */
    public void closeLeaderLatch(String path) {
        LOG.info("close leader latch at path: " + path);
        LeaderLatchHandler leaderLatchHandler = leaderLatchHandlerMap.get(path);
        if (null != leaderLatchHandler) {
            leaderLatchHandlerMap.remove(path);
            leaderLatchHandler.destroy();
        }
    }
    
    /**
     * 当作为主导节点时执行任务。
     * <b>注意：只在作为主导节点时执行一次。</b>
     * @param path 竞争主导节点的路径。
     * @param runnable 任务。
     * @throws java.io.EOFException 等待过程中竞争器被关闭了则会抛出该异常。
     * @throws InterruptedException 等待被打断则会抛出该异常。
     */
    public void executeAsLeadership(String path, Runnable runnable) throws EOFException, InterruptedException {
        LeaderLatchHandler leaderLatchHandler = getLeaderLatchHandler(path);
        leaderLatchHandler.awaitForLeadership();
        zkNodeEventNotifierExecutor.submit(runnable);
    }
    
    /**
     * 当作为主导节点时执行任务。
     * <b>注意：只在作为主导节点时执行一次。</b>
     * @param path 竞争主导节点的路径。
     * @param runnable 任务。
     * @param timeout 等待时间。
     * @param timeUnit 等待时间单位。
     * @throws java.io.EOFException 等待过程中竞争器被关闭了则会抛出该异常。
     * @throws InterruptedException 等待被打断则会抛出该异常。
     */
    public void executeAsLeadership(String path, Runnable runnable, long timeout, TimeUnit timeUnit)
            throws EOFException, InterruptedException {
        LeaderLatchHandler leaderLatchHandler = getLeaderLatchHandler(path);
        leaderLatchHandler.awaitForLeadership(timeout, timeUnit);
        zkNodeEventNotifierExecutor.submit(runnable);
    }
    
    private LeaderLatchHandler getLeaderLatchHandler(String path) {
    	return getLeaderLatchHandler(path, null);
    }
    
    
    private LeaderLatchHandler getLeaderLatchHandler(String path, String id) {
    	 LeaderLatchHandler leaderLatchHandler = leaderLatchHandlerMap.get(path);
         boolean newCreated = false;
         if (null == leaderLatchHandler) {
             leaderLatchHandlerMapLock.lock();
             try {
                 leaderLatchHandler = leaderLatchHandlerMap.get(path);
                 if (null == leaderLatchHandler) {
                     // 创建主导节点竞争器
                	 LeaderLatch leaderLatch = null;
                	 if (id == null) {
                		 leaderLatch = new LeaderLatch(client, path);
                	 }else{
                		 leaderLatch = new LeaderLatch(client, path, id);
                	 }
                     // 创建主导节点竞争处理器
                     leaderLatchHandler = new LeaderLatchHandler(path, leaderLatch);
                     // 启动监视
                     leaderLatchHandler.start();
                     leaderLatchHandlerMap.put(path, leaderLatchHandler);
                     newCreated = true;
                 }
             } finally {
                 leaderLatchHandlerMapLock.unlock();
             }
         }
         if (newCreated && LOG.isDebugEnabled()) {
             LOG.debug("LeaderLatchHandler created for path " + path);
         }
         return leaderLatchHandler;
    }
    
    
}
