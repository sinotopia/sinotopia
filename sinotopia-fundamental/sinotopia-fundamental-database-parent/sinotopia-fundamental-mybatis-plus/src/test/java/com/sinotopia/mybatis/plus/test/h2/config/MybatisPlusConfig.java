package com.sinotopia.mybatis.plus.test.h2.config;

import javax.sql.DataSource;

import com.sinotopia.mybatis.plus.MybatisConfiguration;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import com.sinotopia.mybatis.plus.MybatisXMLLanguageDriver;
import com.sinotopia.mybatis.plus.entity.GlobalConfiguration;
import com.sinotopia.mybatis.plus.plugins.PaginationInterceptor;
import com.sinotopia.mybatis.plus.spring.MybatisSqlSessionFactoryBean;

/**
 * <p>
 * TODO class
 * </p>
 *
 * @author Caratacus
 * @date 2017/4/1
 */
@Configuration
@MapperScan("com.sinotopia.mybatis.plus.test.h2.entity.mapper")
public class MybatisPlusConfig {

    @Bean("mybatisSqlSession")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, ResourceLoader resourceLoader, GlobalConfiguration globalConfiguration) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
//        sqlSessionFactory.setConfigLocation(resourceLoader.getResource("classpath:mybatis-config.xml"));
        sqlSessionFactory.setTypeAliasesPackage("com.sinotopia.mybatis.plus.test.h2.entity.persistent");
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        sqlSessionFactory.setConfiguration(configuration);
        PaginationInterceptor pagination = new PaginationInterceptor();
        pagination.setDialectType("h2");
        sqlSessionFactory.setPlugins(new Interceptor[]{
                pagination
        });
        sqlSessionFactory.setGlobalConfig(globalConfiguration);
        return sqlSessionFactory.getObject();
    }

    @Bean
    public GlobalConfiguration globalConfiguration() {
        return new GlobalConfiguration();
    }
}
