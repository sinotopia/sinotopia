package com.hkfs.fundamental.database;

import org.apache.ibatis.cache.decorators.FifoCache;
import org.apache.ibatis.cache.decorators.LruCache;
import org.apache.ibatis.cache.decorators.SoftCache;
import org.apache.ibatis.cache.decorators.WeakCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.apache.ibatis.datasource.jndi.JndiDataSourceFactory;
import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;
import org.apache.ibatis.executor.loader.cglib.CglibProxyFactory;
import org.apache.ibatis.executor.loader.javassist.JavassistProxyFactory;
import org.apache.ibatis.logging.commons.JakartaCommonsLoggingImpl;
import org.apache.ibatis.logging.jdk14.Jdk14LoggingImpl;
import org.apache.ibatis.logging.log4j.Log4jImpl;
import org.apache.ibatis.logging.log4j2.Log4j2Impl;
import org.apache.ibatis.logging.nologging.NoLoggingImpl;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.scripting.defaults.RawLanguageDriver;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;

/**
 * @author Clinton Begin
 */
public class FundamentalConfiguration extends Configuration {
    protected FundamentalTypeAliasRegistry fundamentalTypeAliasRegistry = new FundamentalTypeAliasRegistry();

    public FundamentalConfiguration() {
        super();
        fundamentalTypeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
        fundamentalTypeAliasRegistry.registerAlias("MANAGED", ManagedTransactionFactory.class);

        fundamentalTypeAliasRegistry.registerAlias("JNDI", JndiDataSourceFactory.class);
        fundamentalTypeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);
        fundamentalTypeAliasRegistry.registerAlias("UNPOOLED", UnpooledDataSourceFactory.class);

        fundamentalTypeAliasRegistry.registerAlias("PERPETUAL", PerpetualCache.class);
        fundamentalTypeAliasRegistry.registerAlias("FIFO", FifoCache.class);
        fundamentalTypeAliasRegistry.registerAlias("LRU", LruCache.class);
        fundamentalTypeAliasRegistry.registerAlias("SOFT", SoftCache.class);
        fundamentalTypeAliasRegistry.registerAlias("WEAK", WeakCache.class);

        fundamentalTypeAliasRegistry.registerAlias("DB_VENDOR", VendorDatabaseIdProvider.class);

        fundamentalTypeAliasRegistry.registerAlias("XML", XMLLanguageDriver.class);
        fundamentalTypeAliasRegistry.registerAlias("RAW", RawLanguageDriver.class);

        fundamentalTypeAliasRegistry.registerAlias("SLF4J", Slf4jImpl.class);
        fundamentalTypeAliasRegistry.registerAlias("COMMONS_LOGGING", JakartaCommonsLoggingImpl.class);
        fundamentalTypeAliasRegistry.registerAlias("LOG4J", Log4jImpl.class);
        fundamentalTypeAliasRegistry.registerAlias("LOG4J2", Log4j2Impl.class);
        fundamentalTypeAliasRegistry.registerAlias("JDK_LOGGING", Jdk14LoggingImpl.class);
        fundamentalTypeAliasRegistry.registerAlias("STDOUT_LOGGING", StdOutImpl.class);
        fundamentalTypeAliasRegistry.registerAlias("NO_LOGGING", NoLoggingImpl.class);

        fundamentalTypeAliasRegistry.registerAlias("CGLIB", CglibProxyFactory.class);
        fundamentalTypeAliasRegistry.registerAlias("JAVASSIST", JavassistProxyFactory.class);
    }

    @Override
    public FundamentalTypeAliasRegistry getTypeAliasRegistry() {
        return fundamentalTypeAliasRegistry;
    }
}