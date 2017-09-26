package com.sinotopia.fundamental.dubbo.start;

import com.sinotopia.fundamental.common.utils.StrUtils;
import com.sinotopia.fundamental.config.FundamentalConfigProvider;
import com.sinotopia.fundamental.dubbo.resolver.AppNameResolver;
import com.sinotopia.fundamental.dubbo.resolver.DefaultAppNameResolver;
import com.sinotopia.fundamental.spring.utils.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JndiConnectionFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

// Spring Java Config的标识
@Configuration
@ComponentScan(basePackages = {"com.sinotopia"}, excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = QuickDubboBootstrap.class)})
// Spring Boot的AutoConfig和载入外部properties文件的 标识
//@ImportResource(value = {"classpath*:applicationContext-*.xml","classpath*:spring/applicationContext-*.xml","classpath*:spring/dubbo-*.xml"})
@EnableConfigurationProperties
@EnableAutoConfiguration(exclude = {JmsAutoConfiguration.class, JndiConnectionFactoryAutoConfiguration.class, ActiveMQAutoConfiguration.class, ArtemisAutoConfiguration.class})
public class QuickDubboBootstrap implements ApplicationContextAware {

    public static final Logger LOGGER = LoggerFactory.getLogger(QuickDubboBootstrap.class);

    private static final String APP_SPRING_EXCLUDE_XML_KEY = "app.spring.exclude.xml";
    private static final String APP_DUBBO_API_XML_TPL_KEY = "app.dubbo.api.xml.tpl";
    private static final String APP_NAME_RESOLVER_CLASS_KEY = "app.name.resolver.class";
    private static AppNameResolver appNameResolver = new DefaultAppNameResolver();
    private static volatile boolean running = true;
    private static String APP_DUBBO_API_XML_TPL = "applicationContext-{}-api.xml";
    private static String[] excludeLoadXmlArr = null;
    private static String[] apiXmls = null;
    //自动扫包配置
    private static final String APP_SCAN_PACKAGE = "app.scan.package";
    //自动扫包配置项间隔符
    private static final String APP_SCAN_PACKAGE_DELIMITERS = ",; \t\n";

    public static void main(String[] args) throws Exception {
        LOGGER.info("dubbo begin start ……");

        //如果配置文件中自定一些不需要加载的xml文件
        String excludeLoadXml = FundamentalConfigProvider.getString(APP_SPRING_EXCLUDE_XML_KEY);
        if (!StrUtils.isEmpty(excludeLoadXml)) {
            excludeLoadXmlArr = excludeLoadXml.split(",");
            LOGGER.info("current dubbo excludeXml:{}", excludeLoadXmlArr);
        }
        //获取api xml 模板
        String dubboApiXmlTpl = FundamentalConfigProvider.getString(APP_DUBBO_API_XML_TPL_KEY);
        if (!StrUtils.isEmpty(dubboApiXmlTpl)) {
            APP_DUBBO_API_XML_TPL = dubboApiXmlTpl;
        }
        //获取app解析名称
        String appNameResolverClass = FundamentalConfigProvider.getString(APP_NAME_RESOLVER_CLASS_KEY);
        if (!StrUtils.isEmpty(appNameResolverClass)) {
            LOGGER.info("current dubbo app name resolver:{}", appNameResolverClass);
            Class<?> appNameResolverClz = ClassUtils.forName(appNameResolverClass, ClassUtils.getDefaultClassLoader());
            appNameResolver = (AppNameResolver) appNameResolverClz.newInstance();
        }
        LOGGER.info("current dubbo app name resolver:{}", appNameResolver.getClass().getName());
        LOGGER.info("current dubbo api xml tpl:{}", APP_DUBBO_API_XML_TPL);
        //检查是否配置appName的解析器

        //dubbo 服务要去掉当前dubbo服务api里面
        List<Object> sourceList = new ArrayList<Object>();
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

        //1.加载spring目录下面的dubbo-*.xml,获取appname，获取api定义的xml文件名
        loadDubboResources(resourcePatternResolver, sourceList);
        //2.加载spring目录下面的其他applicationContext-*.xml
        loadResources(resourcePatternResolver, sourceList, "classpath*:spring/applicationContext-*.xml");
        //3.加载classpath下面的applicationContext-*.xml
        loadResources(resourcePatternResolver, sourceList, "classpath*:applicationContext-*.xml");

        //5.加载启动类
        sourceList.add(QuickDubboBootstrap.class);
        // 对于fundamental中的默认的applicationContext-*.xml，考虑到具体项目可能需要对fundamental中的xml进行覆盖

        Object[] sources = sourceList.toArray(new Object[sourceList.size()]);

        SpringApplication springApplication = new SpringApplication(sources);
        springApplication.addInitializers(new DubboApplicationContextInitializer());
        //启动
        springApplication.run(args);

        //添加关闭钩子
        addShutdownHook();
    }

    private static boolean isFundamentalResource(Resource resource) {
        return resource.toString().contains("fundamental");
    }

    private static void loadDubboResources(ResourcePatternResolver resourcePatternResolver, List<Object> sourceList) throws IOException {
        //支持多个dubbo-xxxx.xml的服务提供者定义
        Resource[] resources = resourcePatternResolver.getResources("classpath*:spring/dubbo-*.xml");
        List<String> appNameList = new ArrayList<String>();
        List<String> apiXmlList = new ArrayList<String>();

        if (resources != null && resources.length > 0) {
            for (Resource resource : resources) {
                String resourceName = resource.getFilename();
                LOGGER.info("Under classPath of {} ,the xml:{} is added!", "classpath*:spring/dubbo-*.xml", resourceName);
                sourceList.add(resource);
                String appName = getAppName(resourceName);
                if (StrUtils.notEmpty(appName)) {
                    appNameList.add(appName);
                }
            }

            for (String appName : appNameList) {
                LOGGER.info("current dubbo appName:{}", appName);
                String apiXml = getApiXml(appName);
                LOGGER.info("current dubbo apiXml:{}", apiXml);
                apiXmlList.add(apiXml);
            }
        }

        apiXmls = apiXmlList.toArray(new String[apiXmlList.size()]);
    }

    private static int loadResources(ResourcePatternResolver resourcePatternResolver, List<Object> sourceList, String locationPattern) throws IOException {
        int loadCount = 0;
        Resource[] resources = resourcePatternResolver.getResources(locationPattern);
        if (resources != null && resources.length > 0) {
            //这里特殊处理，满足具体项目覆盖fundamental中的xml定义
            HashMap<String, Resource> resourceMap = new HashMap<String, Resource>();
            String xmlFileName = null;
            for (Resource resource : resources) {
                if (isExcludedResource(resource)) {
                    continue;
                }

                xmlFileName = resource.getFilename();
                Resource existingResource = resourceMap.get(xmlFileName);
                if (existingResource != null) {
                    boolean isExistingResourceFundamental = isFundamentalResource(existingResource);
                    boolean isCurrentResourceFundamental = isFundamentalResource(resource);

                    if (isExistingResourceFundamental && !isCurrentResourceFundamental) {
                        resourceMap.put(xmlFileName, resource);

                        LOGGER.info("fundamental xml :{} will be overrided by {}!", xmlFileName, resource.getURI());
                    } else if (!isExistingResourceFundamental && isCurrentResourceFundamental) {
                        resourceMap.put(xmlFileName, existingResource);

                        LOGGER.info("fundamental xml :{} will be overrided by {}!", xmlFileName, existingResource.getURI());
                    } else {
                        //不允许xml名字相同
                        throw new IllegalArgumentException("resource name conflict!" + existingResource.toString() + " " + resource.toString());
                    }
                } else {
                    resourceMap.put(xmlFileName, resource);
                }
            }

            Iterator<Map.Entry<String, Resource>> it = resourceMap.entrySet().iterator();
            Map.Entry<String, Resource> entry;
            while (it.hasNext()) {
                entry = it.next();

                LOGGER.info("Under classPath of {}, the xml:{} is added!", locationPattern, entry.getValue().getFilename());
                sourceList.add(entry.getValue());
                loadCount++;
            }
        }
        return loadCount;
    }

    /**
     * 程序关闭通知
     */
    private static void addShutdownHook() {
        Thread sysnotifyShutdownHook = new Thread(new Runnable() {
            public void run() {
                synchronized (QuickDubboBootstrap.class) {
                    running = false;
                    QuickDubboBootstrap.class.notify();
                }
            }
        }, "sysnotify-service-shutdown-hook");
        sysnotifyShutdownHook.setPriority(Thread.MAX_PRIORITY);
        Runtime.getRuntime().addShutdownHook(sysnotifyShutdownHook);
        synchronized (QuickDubboBootstrap.class) {
            while (running) {
                try {
                    QuickDubboBootstrap.class.wait();
                } catch (Throwable e) {
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.setApplicationContext(applicationContext);
    }

    /**
     * 获取应用的名称
     *
     * @param fileName
     * @return
     */
    private static String getAppName(String fileName) {
        if (appNameResolver != null) {
            return appNameResolver.resolver(fileName);
        }
        return null;
    }

    /**
     * 获取dubbo服务API定义xml
     *
     * @param appName
     * @return
     */
    private static String getApiXml(String appName) {
        return APP_DUBBO_API_XML_TPL.replace("{}", appName);
    }

    /**
     * 校验是否匹配
     *
     * @param resource
     * @return
     */
    private static boolean isExcludedResource(Resource resource) {
        String xmlName = resource.getFilename();
        LOGGER.info("check xml:{}", resource.getFilename());
        if (apiXmls != null && apiXmls.length > 0) {
            for (String apiXml : apiXmls) {
                if (xmlName.endsWith(apiXml)) {
                    LOGGER.info("current xml name:{} matched with api xml:{} will be excluded !", xmlName, apiXml);
                    return true;
                }
            }
        }
        if (excludeLoadXmlArr != null && excludeLoadXmlArr.length > 0) {
            for (String excludeXml : excludeLoadXmlArr) {
                if (xmlName.endsWith(excludeXml)) {
                    LOGGER.info("current xml name:{} matched with exclude xml:{} will be excluded !", xmlName, excludeXml);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 负责springboot容器初始化
     */
    public static class DubboApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            String scanPackage = FundamentalConfigProvider.getString(APP_SCAN_PACKAGE);
            if (!StringUtils.isEmpty(scanPackage)) {
                BeanDefinitionRegistry beanDefinitionRegistry = null;
                //这里扫描包
                if (configurableApplicationContext instanceof BeanDefinitionRegistry) {
                    beanDefinitionRegistry = (BeanDefinitionRegistry) configurableApplicationContext;
                } else if (configurableApplicationContext instanceof AbstractApplicationContext) {
                    beanDefinitionRegistry = (BeanDefinitionRegistry) configurableApplicationContext.getBeanFactory();
                } else {
                    throw new IllegalStateException("===================Could not locate BeanDefinitionRegistry");
                }

                String[] packages = StringUtils.tokenizeToStringArray(scanPackage, APP_SCAN_PACKAGE_DELIMITERS);
                if (packages != null && packages.length > 0) {
                    ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanDefinitionRegistry);
                    int loadCount = scanner.scan(packages);
                    LOGGER.info("===================Load bean from custom scan package:{},loadcount:{}", packages, loadCount);
                }
            }
        }
    }
}
