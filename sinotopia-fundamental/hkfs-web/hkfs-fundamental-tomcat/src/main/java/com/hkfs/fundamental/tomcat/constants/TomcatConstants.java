package com.hkfs.fundamental.tomcat.constants;

import com.hkfs.fundamental.config.FundamentalConfigProvider;

/**
 * <p>输入注释</p>
 *
 * @Author dzr
 * @Date 2016/7/12
 */
public interface TomcatConstants {

    public final static Integer TOMCAT_MAX_CONNECTIONS  = FundamentalConfigProvider.getInt(PropertiesKey.TOMCAT_MAX_CONNECTIONS_KEY,1000);

    public final static Integer TOMCAT_MAX_THREADS = FundamentalConfigProvider.getInt(PropertiesKey.TOMCAT_MAX_THREADS_KEY,1000);

    public final static Integer TOMCAT_CONNECTION_TIMEOUT  = FundamentalConfigProvider.getInt(PropertiesKey.TOMCAT_CONNECTION_TIMEOUT_KEY,60000);

    public final static Integer TOMCAT_MIN_SPARE_THREADS = FundamentalConfigProvider.getInt(PropertiesKey.TOMCAT_MIN_SPARE_THREADS_KEY,50);

    public final static Integer TOMCAT_MAX_KEEPALIVEREQUESTS = FundamentalConfigProvider.getInt(PropertiesKey.TOMCAT_MAX_KEEPALIVEREQUESTS_KEY,200);

    public final static Integer TOMCAT_MAXHEADERCOUNT = FundamentalConfigProvider.getInt(PropertiesKey.TOMCAT_MAXHEADERCOUNT_KEY,200);

    public final static String TOMCAT_COMPRESSION = FundamentalConfigProvider.getString(PropertiesKey.TOMCAT_COMPRESSION_KEY, "on");;

    public final static Integer TOMCAT_COMPRESSIONMINSIZE = FundamentalConfigProvider.getInt(PropertiesKey.TOMCAT_COMPRESSIONMINSIZE_KEY,2048);

    public final static String TOMCAT_NOCOMPRESSIONUSERAGENTS = FundamentalConfigProvider.getString(PropertiesKey.TOMCAT_NOCOMPRESSIONUSERAGENTS_KEY,"gozilla, traviata");
}
