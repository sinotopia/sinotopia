/*
 * Copyright (c) 2014, lingang.chen@gmail.com  All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.hkfs.fundamental.common.start;

import com.hkfs.fundamental.common.utils.StrUtils;
import com.hkfs.fundamental.config.FundamentalConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Reason: 授权服务器启动器.
 * 
 * @author chenlg
 * @version $Id: QuickServiceBootstrap.java, v 0.1 2014年10月27日 下午12:45:33 chenlg
 *          Exp $
 * @since JDK 1.7
 * @see
 */
// Spring Java Config的标识
@Configuration
@ComponentScan(basePackages={"com.djd", "com.hakim", "com.dyc"}, excludeFilters={@ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE,value=QuickWebBootstrap.class), @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE,value=QuickDubboBootstrap.class)})
// Spring Boot的AutoConfig和载入外部properties文件的 标识
@EnableConfigurationProperties
@ImportResource(value = {"classpath*:applicationContext-*.xml","classpath*:spring/applicationContext-*.xml","classpath*:spring/dubbo-*.xml"})
@EnableAutoConfiguration
public class QuickWebBootstrap extends SpringBootServletInitializer implements EmbeddedServletContainerCustomizer {

	private final static Logger LOGGER = LoggerFactory.getLogger(QuickWebBootstrap.class);

	public static void main(String[] args) throws Exception {

		List sourcesList = new ArrayList();
		sourcesList.add(QuickWebBootstrap.class);
		//SpringApplication.run(QuickWebBootstrap.class, args);
		Object[] sources = sourcesList.toArray(new Object[sourcesList.size()]);
		SpringApplication springApplication = new SpringApplication(sources);
		springApplication.addInitializers(new ApplicationContextInitializer<ConfigurableApplicationContext>() {
			@Override
			public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
				BeanDefinitionRegistry beanDefinitionRegistry = null;
				//这里扫描包
				if (configurableApplicationContext instanceof BeanDefinitionRegistry) {
					beanDefinitionRegistry = (BeanDefinitionRegistry) configurableApplicationContext;
				}else if (configurableApplicationContext instanceof AbstractApplicationContext) {
					beanDefinitionRegistry = (BeanDefinitionRegistry) ((AbstractApplicationContext) configurableApplicationContext)
							.getBeanFactory();
				}else {
					throw new IllegalStateException("===================Could not locate BeanDefinitionRegistry");
				}
				String scanPackage = FundamentalConfigProvider.getString(WEB_SCAN_PACKAGE);
				if(!StringUtils.isEmpty(scanPackage)) {
					String[] packages = StringUtils.tokenizeToStringArray(scanPackage, WEB_SCAN_PACKAGE_DELIMITERS);
					if(packages != null && packages.length > 0){
						ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanDefinitionRegistry);
						int loadCount = scanner.scan(packages);
						LOGGER.info("===================Load bean from custom scan package:{},loadcount:{}", packages,loadCount);
					}
				}

			}
		});
		springApplication.run(args);
//		SpringApplication.run(sources, args);
	}

	//端口号
	private static final String WEB_PORT_KEY = "web.port";
	//项目根路径
	private static final String WEB_CONTEXT_PATH_KEY = "web.context.path";
	//静态文件根路径
	private static final String WEB_DOCUMENT_ROOT_KEY = "web.document.root";

	private static final String WEB_SCAN_PACKAGE="web.scan.package";

	private static final String  WEB_SCAN_PACKAGE_DELIMITERS = ",; \t\n";

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		container.setContextPath(FundamentalConfigProvider.getString(WEB_CONTEXT_PATH_KEY));
		container.setPort(FundamentalConfigProvider.getInt(WEB_PORT_KEY));

		String documentRootPath = FundamentalConfigProvider.getString(WEB_DOCUMENT_ROOT_KEY);
		if (StrUtils.notEmpty(documentRootPath)) {
			container.setDocumentRoot(new File(documentRootPath));
		}
	}

}
