package com.hkfs.fundamental.zookeeper.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by zhoubing on 2016/4/28.
 */
public class ZookeeperModuleUtils {
    private static final int BUFFER_LENGTH = 2048;
    public static final int DEFAULT_POOL_SIZE = 10;
    public static final byte[] EMPTY_BYTES = new byte[0];
    public static Charset CHARSET_UTF8 = Charset.forName("utf-8");

    private static Map<String, ExecutorService> executorServiceMap = new HashMap<String, ExecutorService>();

    private static Lock executorServiceMapLock = new ReentrantLock();

    static {
        init();
    }

    /**
     * <p>压缩</p>
     * @param ungzipped
     * @return
     */
    public static byte[] gzip(byte[] ungzipped) {
        GZIPOutputStream gos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            gos = new GZIPOutputStream(baos);
            gos.write(ungzipped, 0, ungzipped.length);
            gos.finish();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Gzip data failed.", e);
        } finally {
            close(gos);
            close(baos);
        }
    }

    /**
     * <p>解压缩</p>
     * @param gzipped
     * @return
     */
    public static byte[] ungzip(byte[] gzipped) {
        return ungzip(gzipped, false);
    }

    /**
     * <p>解压缩</p>
     * @param gzipped
     * @param giveRawIfFailed
     * @return
     */
    public static byte[] ungzip(byte[] gzipped, boolean giveRawIfFailed) {
        GZIPInputStream gis = null;
        ByteArrayInputStream bais = null;
        ByteArrayOutputStream baos = null;
        try {
            byte[] buffer = new byte[BUFFER_LENGTH];
            bais = new ByteArrayInputStream(gzipped);
            gis = new GZIPInputStream(bais, BUFFER_LENGTH);
            baos = new ByteArrayOutputStream(gzipped.length);
            int readedLength = 0;
            while (-1 != (readedLength = gis.read(buffer))) {
                baos.write(buffer, 0, readedLength);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            if (giveRawIfFailed) {
                throw new RuntimeException("Ungzip data failed, return the raw data.", e);
            } else {
                throw new RuntimeException("Ungzip data failed, return empty data.", e);
            }
        } finally {
            close(bais);
            close(gis);
            close(baos);
        }
    }

    public static void close(Object object) {
        if (object != null) {
            try {
                if (object instanceof InputStream) {
                    ((InputStream)object).close();
                }
                else if (object instanceof OutputStream) {
                    ((OutputStream)object).close();
                }
                else if (object instanceof Reader) {
                    ((Reader)object).close();
                }
                else if (object instanceof Writer) {
                    ((Writer)object).close();
                }
            }
            catch (Exception e) {}
        }
    }

    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

    private static void init() {
        // 注册关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                executorServiceMapLock.lock();
                try {
                    for (ExecutorService executorService : executorServiceMap.values()) {
                        executorService.shutdown();
                    }
                } finally {
                    executorServiceMapLock.unlock();
                }
            }
        });
    }

    private static ExecutorService getThreadPool(int corePoolSize, String name,
                                                 ExecutorServiceFactory executorServiceFactory) {
        ExecutorService executorService = executorServiceMap.get(name);
        executorServiceMapLock.lock();
        try {
            executorService = executorServiceMap.get(name);
            if (null == executorService) {
                // 创建执行服务
                executorService = executorServiceFactory.createExecutorService(corePoolSize, name);
                executorServiceMap.put(name, executorService);
            }
        } finally {
            executorServiceMapLock.unlock();
        }
        return executorService;
    }

    private static abstract class ExecutorServiceFactory {

        public ExecutorService createExecutorService(int corePoolSize, String threadGroupName) {
            return createExecutorService(corePoolSize, createThreadFactory(threadGroupName));
        }

        public abstract ExecutorService createExecutorService(int corePoolSize, ThreadFactory threadFactory);

    }

    private static ExecutorServiceFactory cachedThreadPoolFactory = new ExecutorServiceFactory() {
        @Override
        public ExecutorService createExecutorService(int corePoolSize, ThreadFactory threadFactory) {
            return Executors.newCachedThreadPool(threadFactory);
        };
    };

    private static ExecutorServiceFactory scheduledThreadPoolFactory = new ExecutorServiceFactory() {
        @Override
        public ExecutorService createExecutorService(int corePoolSize, ThreadFactory threadFactory) {
            return Executors.newScheduledThreadPool(corePoolSize, threadFactory);
        }
    };

    private static ExecutorServiceFactory fixedThreadPoolFactory = new ExecutorServiceFactory() {
        @Override
        public ExecutorService createExecutorService(int corePoolSize, ThreadFactory threadFactory) {
            return Executors.newFixedThreadPool(corePoolSize, threadFactory);
        }
    };

    private static ExecutorServiceFactory singleThreadPoolFactory = new ExecutorServiceFactory() {
        @Override
        public ExecutorService createExecutorService(int corePoolSize, ThreadFactory threadFactory) {
            return Executors.newSingleThreadExecutor(threadFactory);
        }
    };

    private static ExecutorServiceFactory singleScheduledThreadPoolFactory = new ExecutorServiceFactory() {
        @Override
        public ExecutorService createExecutorService(int corePoolSize, ThreadFactory threadFactory) {
            return Executors.newSingleThreadScheduledExecutor(threadFactory);
        }
    };

    public static ExecutorService getCachedThreadPool(String name) {
        return getThreadPool(0, name, cachedThreadPoolFactory);
    }

    public static ScheduledExecutorService getScheduledThreadPool(String name) {
        return getScheduledThreadPool(DEFAULT_POOL_SIZE, name);
    }

    public static ScheduledExecutorService getScheduledThreadPool(int corePoolSize, String name) {
        return (ScheduledExecutorService) getThreadPool(corePoolSize, name, scheduledThreadPoolFactory);
    }

    public static ExecutorService getFixedThreadPool(int nThreads, String name) {
        return getThreadPool(nThreads, name, fixedThreadPoolFactory);
    }

    public static ExecutorService getSingleThreadPool(String name) {
        return getThreadPool(0, name, singleThreadPoolFactory);
    }

    public static ScheduledExecutorService getSingleScheduledThreadPool(String name) {
        return (ScheduledExecutorService) getThreadPool(0, name, singleScheduledThreadPoolFactory);
    }

    private static ThreadFactory createThreadFactory(final String threadGroupName) {
        return new ThreadFactory() {
            private String threadNamePrefix = threadGroupName + "Thread-";
            private AtomicInteger threadIndex = new AtomicInteger();

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName(threadNamePrefix + (threadIndex.incrementAndGet()));
                t.setDaemon(true);
                return t;
            }
        };
    }

    public static <T> Future<T> submit(ExecutorService executorService, final Callable<T> task) {
        // 判断是否需要复制上下文变量
        return executorService.submit(new Callable<T>() {
            @SuppressWarnings("unchecked")
            @Override
            public T call() throws Exception {
                // 无需复制上下文变量
                return task.call();
            }
        });
    }

    public static Future<?> submit(ExecutorService executorService, Runnable task) {
        return submit(executorService, task, null);
    }

    public static <T> Future<T> submit(ExecutorService executorService, final Runnable task, T result) {
        // 判断是否需要复制上下文变量
        return executorService.submit(new Runnable() {
            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                // 无需复制上下文变量
                task.run();
            }
        }, result);
    }

    /**
     * 判断给定字符串是否空白串。
     * 空白串是指由空格、制表符、回车符、换行符组成的字符串
     * 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        for (int i=0;i<str.length();i++) {
            char c = str.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 不为空返回true，为空返回false。
     */
    public static boolean notEmpty(String str) {
        return !isEmpty(str);
    }
}
