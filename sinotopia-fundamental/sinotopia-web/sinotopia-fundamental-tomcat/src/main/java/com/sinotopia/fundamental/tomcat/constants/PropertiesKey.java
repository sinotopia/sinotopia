package com.sinotopia.fundamental.tomcat.constants;

/**
 * Created by pc on 2016/4/22.
 */
public interface PropertiesKey {

    public final static String TOMCAT_MAX_CONNECTIONS_KEY  = "sinotopia.tomcat.protocol.maxconnections";

    public final static String TOMCAT_MAX_THREADS_KEY = "sinotopia.tomcat.protocol.maxthreads";

    public final static String TOMCAT_CONNECTION_TIMEOUT_KEY  = "sinotopia.tomcat.protocol.connection.timeout";

    public final static String TOMCAT_MIN_SPARE_THREADS_KEY = "sinotopia.tomcat.protocol.minsparethreads";

    public final static String TOMCAT_MAX_KEEPALIVEREQUESTS_KEY = "sinotopia.tomcat.protocol.maxkeepalive.requests";

    public final static String TOMCAT_MAXHEADERCOUNT_KEY = "sinotopia.tomcat.protocol.maxheadercount";

    public final static String TOMCAT_COMPRESSION_KEY = "sinotopia.tomcat.protocol.compression";

    public final static String TOMCAT_COMPRESSIONMINSIZE_KEY = "sinotopia.tomcat.protocol.compression.minsize";

    public final static String TOMCAT_NOCOMPRESSIONUSERAGENTS_KEY = "sinotopia.tomcat.protocol.nocompressionuseragents";

}
