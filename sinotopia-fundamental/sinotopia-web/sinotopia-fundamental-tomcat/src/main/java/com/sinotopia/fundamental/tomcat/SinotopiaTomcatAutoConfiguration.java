package com.sinotopia.fundamental.tomcat;

import org.apache.catalina.startup.Tomcat;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ObjectUtils;

import javax.servlet.Servlet;

/**
 * <p>tomcat自定义配置</p>
 *
 * @Author dzr
 * @Date 2016/7/8
 */
@Configuration
@ConditionalOnWebApplication
//@ConditionalOnBean(TomcatEmbeddedServletContainerFactory.class)
@AutoConfigureAfter(EmbeddedServletContainerAutoConfiguration.class)
@Import(SinotopiaTomcatAutoConfiguration.TomcatCustomizerBeanPostProcessorRegistrar.class)
public class SinotopiaTomcatAutoConfiguration {

    @Configuration
    @ConditionalOnClass({ Servlet.class, Tomcat.class })
    static class sinotopiaTomcatConnectorCustomizerrConfiguration {

        @Bean
        public SinotopiaTomcatConnectorCustomizer sinotopiaTomcatConnectorCustomizer() {
            return new SinotopiaTomcatConnectorCustomizer();
        }

        @Bean
        public SinotopiaTomcatContextCustomizer sinotopiaTomcatContextCustomizer() {
            return new SinotopiaTomcatContextCustomizer();
        }
    }

    public static class TomcatCustomizerBeanPostProcessorRegistrar
            implements ImportBeanDefinitionRegistrar, BeanFactoryAware {

        private ConfigurableListableBeanFactory beanFactory;

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            if (beanFactory instanceof ConfigurableListableBeanFactory) {
                this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
            }
        }

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                            BeanDefinitionRegistry registry) {
            if (this.beanFactory == null) {
                return;
            }
            if (ObjectUtils.isEmpty(this.beanFactory.getBeanNamesForType(
                    SinotopiaTomcatCustomizerBeanPostProcessor.class, true,
                    false))) {
                //这里手动注册
                registry.registerBeanDefinition(
                        "sinotopiaTomcatCustomizerBeanPostProcessor",
                        new RootBeanDefinition(
                                SinotopiaTomcatCustomizerBeanPostProcessor.class));

            }
        }
    }
}
