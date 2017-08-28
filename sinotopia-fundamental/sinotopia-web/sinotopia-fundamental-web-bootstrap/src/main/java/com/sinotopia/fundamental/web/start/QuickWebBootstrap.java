package com.sinotopia.fundamental.web.start;

import com.sinotopia.fundamental.common.utils.StrUtils;
import com.sinotopia.fundamental.config.FundamentalConfigProvider;
import com.sinotopia.fundamental.spring.utils.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableConfigurationProperties
@ImportResource(value = {"classpath*:applicationContext-*.xml", "classpath*:spring/applicationContext-*.xml", "classpath*:spring/dubbo-*.xml"})
@ComponentScan(
        basePackages = {"com.sinotopia"},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = QuickWebBootstrap.class)})
@EnableAutoConfiguration
public class QuickWebBootstrap extends SpringBootServletInitializer implements EmbeddedServletContainerCustomizer, ApplicationContextAware {

    private final static Logger LOGGER = LoggerFactory.getLogger(QuickWebBootstrap.class);

    public static void main(String[] args) {
        List sourceList = new ArrayList();
        sourceList.add(QuickWebBootstrap.class);
        Object[] sources = sourceList.toArray(new Object[sourceList.size()]);

        SpringApplication springApplication = new SpringApplication(sources);
        springApplication.addInitializers(new WebApplicationContextInitializer());
        springApplication.run(args);
    }

    //端口号
    private static final String WEB_PORT_KEY = "web.port";
    //项目根路径
    private static final String WEB_CONTEXT_PATH_KEY = "web.context.path";
    //静态文件根路径
    private static final String WEB_DOCUMENT_ROOT_KEY = "web.document.root";
    //项目扫包配置
    private static final String WEB_SCAN_PACKAGE = "web.scan.package";
    //项目扫包配置分隔符
    private static final String WEB_SCAN_PACKAGE_DELIMITERS = ",; \t\n";

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setContextPath(FundamentalConfigProvider.getString(WEB_CONTEXT_PATH_KEY));
        container.setPort(FundamentalConfigProvider.getInt(WEB_PORT_KEY));

        String documentRootPath = FundamentalConfigProvider.getString(WEB_DOCUMENT_ROOT_KEY);
        if (StrUtils.notEmpty(documentRootPath)) {
            container.setDocumentRoot(new File(documentRootPath));
        }
    }

    /**
     * 负责springboot容器初始化
     */
    public static class WebApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            String scanPackage = FundamentalConfigProvider.getString(WEB_SCAN_PACKAGE);
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

                String[] packages = StringUtils.tokenizeToStringArray(scanPackage, WEB_SCAN_PACKAGE_DELIMITERS);
                if (packages != null && packages.length > 0) {
                    ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanDefinitionRegistry);
                    int loadCount = scanner.scan(packages);
                    LOGGER.info("===================Load bean from custom scan package:{},loadcount:{}", packages, loadCount);
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.setApplicationContext(applicationContext);
    }
}
