package com.sinotopia.fundamental.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.util.Properties;

public class FundamentalConfigProvider extends PropertyPlaceholderConfigurer {

    public static final Logger logger = LoggerFactory.getLogger(FundamentalConfigProvider.class);

    private static FundamentalConfigProvider instance;
    private static Properties prop = null;
    public static final String CONFIG_PATH_NAME = "config.path";
    private static final String DEFAULT_CLASSPATH_PROPERTIES = "classpath*:config/*.properties";
    private static String charset = "UTF-8";

    public static final String REMOTE_PROPERTIES_LOADER_CLASS_NAME = "com.sinotopia.fundamental.config.RemotePropertiesLoader";

    public FundamentalConfigProvider() {
        setProperties(FundamentalConfigProvider.getProp());
        instance = this;
    }

    public static Properties getProp() {
        if (prop == null) {
            init();
        }
        return prop;
    }

    /**
     * 返回所有的properties
     * @return
     */
    public Properties getProperties() {
        return getProp();
    }

    /**
     * 返回String类型的配置结果，默认返回null
     * @param key
     * @return
     */
    public static String getString(String key) {
        return getString(key, null);
    }
    /**
     * 返回String类型的配置结果
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public static String getString(String key, String defaultValue) {
        return getProp().getProperty(key, defaultValue);
    }

    /**
     * 返回Integer类型的配置结果，默认返回null
     * @param key
     * @return
     */
    public static Integer getInt(String key) {
        return getInt(key, null);
    }

    /**
     * 返回Integer类型的配置结果
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public static Integer getInt(String key, Integer defaultValue) {
        String value = getString(key);
        if (value != null && value.length() > 0) {
            return Integer.parseInt(value);
        }
        return defaultValue;
    }

    /**
     * 返回Long类型的配置结果，默认返回null
     * @param key
     * @return
     */
    public static Long getLong(String key) {
        return getLong(key, null);
    }

    /**
     * 返回Long类型的配置结果
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public static Long getLong(String key, Long defaultValue) {
        String value = getString(key);
        if (value != null && value.length() > 0) {
            return Long.parseLong(value);
        }
        return defaultValue;
    }

    /**
     * 返回Double类型的配置结果，默认返回null
     * @param key
     * @return
     */
    public static Double getDouble(String key) {
        return getDouble(key, null);
    }

    /**
     * 返回Double类型的配置结果
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public static Double getDouble(String key, Double defaultValue) {
        String value = getString(key);
        if (value != null && value.length() > 0) {
            return Double.parseDouble(value);
        }
        return defaultValue;
    }

    /**
     * 返回Boolean类型的配置结果，默认返回false
     * @param key
     * @return
     */
    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * 返回Boolean类型的配置结果
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = getString(key);
        if (value != null && value.length() > 0) {
            return Boolean.parseBoolean(value);
        }
        return defaultValue;
    }

    /**
     * 设置读取配置文件时使用的字符集编码，默认UTF-8
     * @param charset
     */
    public static void setCharset(String charset) {
        FundamentalConfigProvider.charset = charset;
    }

    /**
     * 获取配置文件字符集编码
     * @return
     */
    public static String getCharset() {
        return FundamentalConfigProvider.charset;
    }

    private static void init() {
        loadProperties(false);
    }

    public static void reload() {
        loadProperties(true);
        if (instance != null) {
            instance.setProperties(FundamentalConfigProvider.getProp());
        }
    }

    private static synchronized void loadProperties(boolean isReload) {
        if (!isReload && FundamentalConfigProvider.prop != null) {
            return;
        }

        Properties properties = new Properties();

        loadLocalProperties(properties);

        loadRemoteProperties(properties);

        FundamentalConfigProvider.prop = properties;

        logger.info((isReload?"Reload":"Load")+" FundamentalConfigProvider.prop: " + FundamentalConfigProvider.prop);
    }

    private static void loadRemoteProperties(Properties properties) {
        PropertiesLoader propertiesLoader = null;
        try {
            Class<?> clazz = Class.forName(REMOTE_PROPERTIES_LOADER_CLASS_NAME);
            if (clazz != null) {
                Object object = clazz.newInstance();
                if (object instanceof PropertiesLoader) {
                    propertiesLoader = (PropertiesLoader) object;
                }
            }
        } catch (Exception e) {
            //跳过
        }

        //加载异常需要抛出
        if (propertiesLoader != null) {
            propertiesLoader.load(properties);
        }
    }

    private static void loadLocalProperties(Properties properties) {
        String configPath = getConfigPath();
        if (configPath != null) {
            String[] configPathArray = configPath.split(";");
            for (String path : configPathArray) {
                loadFromConfigPath(properties, path);
            }
        }

        //加载默认/resources/config目录下的配置文件
        loadFromConfigPath(properties, DEFAULT_CLASSPATH_PROPERTIES);
    }

    private static String getConfigPath() {
        String configPath = System.getProperty(CONFIG_PATH_NAME);
        if (configPath == null || configPath.length() == 0) {
            configPath = System.getenv(CONFIG_PATH_NAME);
        }
        return configPath;
    }

    /**
     * 获取资源文件
     * @param filePath 文件路径
     * @return
     */
    public static Resource getResource(String filePath) {
        Resource[] resources = getResources(filePath);
        if (resources != null && resources.length > 0) {
            return resources[0];
        }
        return null;
    }

    /**
     * 获取资源文件
     * @param filePath 文件路径
     * @return
     */
    public static File getResourceFile(String filePath) {
        Resource resource = getResource(filePath);
        if (resource != null) {
            try {
                return resource.getFile();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    /**
     * 获取资源文件数组
     * @param filePath 文件路径
     * @return
     */
    public static Resource[] getResources(String filePath) {
        if (!filePath.startsWith(FilePropertiesLoader.CLASSPATH_PREFIX)) {
            filePath = FilePropertiesLoader.CLASSPATH_PREFIX+filePath;
        }
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            return resolver.getResources(filePath);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从配置文件中加载配置
     * @param configPath
     */
    private static void loadFromConfigPath(Properties properties, String configPath){
        PropertiesLoader propertiesLoader = new FilePropertiesLoader(configPath, charset);
        propertiesLoader.load(properties);
    }
}
