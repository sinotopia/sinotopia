package com.sinotopia.fundamental.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.*;
import java.util.Properties;

/**
 * 默认本地配置文件加载器
 */
public class FilePropertiesLoader implements PropertiesLoader {

    public static final Logger logger = LoggerFactory.getLogger(FilePropertiesLoader.class);

    public static final String FILE_PREFIX = "file:";
    public static final String CLASSPATH_PREFIX = "classpath*:";

    private String configPath;
    private String charset;

    public FilePropertiesLoader(String configPath, String charset) {
        this.configPath = configPath;
        this.charset = charset;
    }

    @Override
    public void load(Properties properties) {
        FileInputStream fis = null;
        InputStream is = null;
        try {
            if (configPath.startsWith(FILE_PREFIX)) {
                File file = new File(configPath.substring(FILE_PREFIX.length()));
                logger.info("Load config from " + file.getName());
                loadFromFile(properties, file);
            } else if (configPath.startsWith(CLASSPATH_PREFIX)) {
//              classpath*:*.properties
                ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
                Resource[] resources = resolver.getResources(configPath);
                for (int i = 0; i < resources.length; i++) {
                    if (resources[i].isReadable()) {
                        logger.info("Load config from " + resources[i].getFilename());
                        PropertiesLoaderUtils.fillProperties(properties, resources[i]);
                    }
                }
            } else {
                throw new RuntimeException("Unknown configPath value " + configPath);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(fis);
            closeQuietly(is);
        }
    }

    private void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException var2) {
            ;
        }
    }

    private void loadFromFile(Properties properties, File file) {
        if (file != null) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File eachFile : files) {
                    loadFromFile(properties, eachFile);
                }
            } else if (file.isFile() && file.getName().endsWith(".properties")
                    && !file.getName().startsWith("log4j")
                    ) {
                try {
                    properties.load(new InputStreamReader(new FileInputStream(file), charset));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
