package com.hkfs.fundamental.config;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.*;
import java.util.Properties;

public class FundamentalConfigProvider extends PropertyPlaceholderConfigurer {
    private static Properties prop = null;
    public static final String CONFIG_PATH_NAME = "config.path";
    public static final String FILE_PREFIX = "file:";
    public static final String CLASSPATH_PREFIX = "classpath*:";

    private static final String DEFAULT_CLASSPATH_PROPERTIES = "classpath*:config/*.properties";

    public FundamentalConfigProvider() {
        setProperties(FundamentalConfigProvider.getProp());
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

    private static synchronized void init() {
        if (FundamentalConfigProvider.prop != null && FundamentalConfigProvider.prop.size() > 0) {
            return;
        }

        prop = new Properties();

        String configPath = getConfigPath();
        if (configPath != null) {
            String[] configPathArray = configPath.split(";");
            for (String path : configPathArray) {
                loadFromConfigPath(path);
            }
        }

        //加载默认/resources/config目录下的配置文件
        loadFromConfigPath(DEFAULT_CLASSPATH_PROPERTIES);

        System.out.println("FundamentalConfigProvider.prop: "+FundamentalConfigProvider.prop);
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
        if (!filePath.startsWith(CLASSPATH_PREFIX)) {
            filePath = CLASSPATH_PREFIX+filePath;
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
    private static void loadFromConfigPath(String configPath){
        FileInputStream fis = null;
        InputStream is = null;
        try {
            if (configPath.startsWith(FILE_PREFIX)) {
                File file = new File(configPath.substring(FILE_PREFIX.length()));
                System.out.println("Load config from "+file.getName());
                loadFromFile(file);
            }
            else if (configPath.startsWith(CLASSPATH_PREFIX)) {
//              classpath*:*.properties
                ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
                Resource[] resources = resolver.getResources(configPath);
                for (int i=0;i<resources.length;i++) {
                    if (resources[i].isReadable()) {
                        System.out.println("Load config from "+resources[i].getFilename());
                        PropertiesLoaderUtils.fillProperties(prop, resources[i]);
                    }
                }
            }
            else{
                throw new RuntimeException("Unknown configPath value "+configPath);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            closeQuietly(fis);
            closeQuietly(is);
        }
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if(closeable != null) {
                closeable.close();
            }
        } catch (IOException var2) {
            ;
        }
    }

    private static void loadFromFile(File file){
        if (file != null) {
            if(file.isDirectory()) {
                File[] files = file.listFiles();
                for(File eachFile: files){
                    loadFromFile(eachFile);
                }
            }
            else if (file.isFile() && file.getName().endsWith(".properties")
                    && !file.getName().startsWith("log4j")
                    ){
                try {
                    prop.load(new FileInputStream(file));
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
