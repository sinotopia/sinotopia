package com.hkfs.fundamental.tomcat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>tomcat自定义的bean后置处理器</p>
 * @Author dzr
 * @Date 2016/7/11
 */
public class HkfsTomcatCustomizerBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private final static Logger LOGGER = LoggerFactory.getLogger(HkfsTomcatCustomizerBeanPostProcessor.class);

    private ApplicationContext applicationContext;

    private List<TomcatConnectorCustomizer> tomcatConnectorCustomizers;

    private List<TomcatContextCustomizer> tomcatContextCustomizers;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        if (bean instanceof TomcatEmbeddedServletContainerFactory) {
            postProcessBeforeInitialization((TomcatEmbeddedServletContainerFactory) bean);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    private void postProcessBeforeInitialization(
            TomcatEmbeddedServletContainerFactory bean) {

        //这里处理自动添加一下TomcatConnectorCustomizer
        LOGGER.info("**********************HkfsTomcatConnectorCustomizers length = {} ,begin to set Tomcat Connector Customizer …… ",getTomcatConnectorCustomizers().size());
        bean.setTomcatConnectorCustomizers(getTomcatConnectorCustomizers());

        //这里处理自动添加一下TomcatContextCustomizer
        LOGGER.info("**********************HkfsTomcatContextCustomizers length = {} ,begin to set Tomcat Context Customizer …… ",getTomcatContextCustomizers().size());
        bean.setTomcatContextCustomizers(getTomcatContextCustomizers());

        /**ssl info ****
        Ssl ssl = new Ssl();
        bean.setSsl(ssl);
        ****/
    }

    /**
     * 获取所有的tomcatConnector的连接对象
     * @return
     */
    private Collection<TomcatConnectorCustomizer> getTomcatConnectorCustomizers() {
        if (this.tomcatConnectorCustomizers == null) {
            this.tomcatConnectorCustomizers = new ArrayList<TomcatConnectorCustomizer>(
                    this.applicationContext
                            .getBeansOfType(TomcatConnectorCustomizer.class,
                                    false, false)
                            .values());
            Collections.sort(this.tomcatConnectorCustomizers, AnnotationAwareOrderComparator.INSTANCE);
            this.tomcatConnectorCustomizers = Collections.unmodifiableList(this.tomcatConnectorCustomizers);
        }
        return this.tomcatConnectorCustomizers;
    }

    /**
     * 获取所有的tomcatContext的自定义对象
     * @return
     */
    private Collection<TomcatContextCustomizer> getTomcatContextCustomizers() {
        if (this.tomcatContextCustomizers == null) {
            this.tomcatContextCustomizers = new ArrayList<TomcatContextCustomizer>(
                    this.applicationContext
                            .getBeansOfType(TomcatContextCustomizer.class,
                                    false, false)
                            .values());
            Collections.sort(this.tomcatContextCustomizers, AnnotationAwareOrderComparator.INSTANCE);
            this.tomcatContextCustomizers = Collections.unmodifiableList(this.tomcatContextCustomizers);
        }
        return this.tomcatContextCustomizers;
    }
}
