package com.hkfs.fundamental.tomcat.constants;

/**
 * Created by pc on 2016/4/22.
 */
public interface PropertiesKey {

    public final static String TOMCAT_MAX_CONNECTIONS_KEY  = "hkfs.tomcat.protocol.maxconnections";

    public final static String TOMCAT_MAX_THREADS_KEY = "hkfs.tomcat.protocol.maxthreads";

    public final static String TOMCAT_CONNECTION_TIMEOUT_KEY  = "hkfs.tomcat.protocol.connection.timeout";

    public final static String TOMCAT_MIN_SPARE_THREADS_KEY = "hkfs.tomcat.protocol.minsparethreads";

    public final static String TOMCAT_MAX_KEEPALIVEREQUESTS_KEY = "hkfs.tomcat.protocol.maxkeepalive.requests";

    public final static String TOMCAT_MAXHEADERCOUNT_KEY = "hkfs.tomcat.protocol.maxheadercount";

    public final static String TOMCAT_COMPRESSION_KEY = "hkfs.tomcat.protocol.compression";

    public final static String TOMCAT_COMPRESSIONMINSIZE_KEY = "hkfs.tomcat.protocol.compression.minsize";

    public final static String TOMCAT_NOCOMPRESSIONUSERAGENTS_KEY = "hkfs.tomcat.protocol.nocompressionuseragents";

}
