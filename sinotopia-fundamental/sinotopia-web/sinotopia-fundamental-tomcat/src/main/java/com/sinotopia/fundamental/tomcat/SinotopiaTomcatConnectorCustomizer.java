package com.sinotopia.fundamental.tomcat;

import com.sinotopia.fundamental.tomcat.constants.TomcatConstants;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;

/**
 * <p>tomcatConnector自定义器</p>
 *
 * @Author dzr
 * @Date 2016/7/8
 */
public class SinotopiaTomcatConnectorCustomizer implements TomcatConnectorCustomizer {

    private final static Logger LOGGER = LoggerFactory.getLogger(SinotopiaTomcatConnectorCustomizer.class);

    @Override
    public void customize(Connector connector) {
        LOGGER.info("**********************sinotopia Tomcat Connector config begin ......");
        //对tomcat的相关参数进行优化
        ProtocolHandler handler = connector.getProtocolHandler();
        if (handler instanceof AbstractHttp11Protocol) {

            AbstractHttp11Protocol<?> protocol = (AbstractHttp11Protocol<?>) handler;
            //设置最大连接数
            protocol.setMaxConnections(TomcatConstants.TOMCAT_MAX_CONNECTIONS);
            //设置最大线程数
            protocol.setMaxThreads(TomcatConstants.TOMCAT_MAX_THREADS);
            //设置连接超时时间
            protocol.setConnectionTimeout(TomcatConstants.TOMCAT_CONNECTION_TIMEOUT);
            protocol.setMinSpareThreads(TomcatConstants.TOMCAT_MIN_SPARE_THREADS);
            protocol.setCompression(TomcatConstants.TOMCAT_COMPRESSION);
            protocol.setCompressionMinSize(TomcatConstants.TOMCAT_COMPRESSIONMINSIZE);
            protocol.setMaxKeepAliveRequests(TomcatConstants.TOMCAT_MAX_KEEPALIVEREQUESTS);
            protocol.setMaxHeaderCount(TomcatConstants.TOMCAT_MAXHEADERCOUNT);
            protocol.setNoCompressionUserAgents(TomcatConstants.TOMCAT_NOCOMPRESSIONUSERAGENTS);
            LOGGER.info("[tomcat-connector] max.connections={}",TomcatConstants.TOMCAT_MAX_CONNECTIONS);
            LOGGER.info("[tomcat-connector] max.threads={}",TomcatConstants.TOMCAT_MAX_THREADS);
            LOGGER.info("[tomcat-connector] max.connection.timeout={}",TomcatConstants.TOMCAT_CONNECTION_TIMEOUT);
            LOGGER.info("[tomcat-connector] max.minsparethreads={}",TomcatConstants.TOMCAT_MIN_SPARE_THREADS);
            LOGGER.info("[tomcat-connector] max.compression={}",TomcatConstants.TOMCAT_COMPRESSION);
            LOGGER.info("[tomcat-connector] max.connectionsminsize={}",TomcatConstants.TOMCAT_COMPRESSIONMINSIZE);
            LOGGER.info("[tomcat-connector] max.maxkeepalive.requests={}",TomcatConstants.TOMCAT_MAX_KEEPALIVEREQUESTS);
            LOGGER.info("[tomcat-connector] max.maxheadercount={}",TomcatConstants.TOMCAT_MAXHEADERCOUNT);
            LOGGER.info("[tomcat-connector] max.nocompressionuseragents={}",TomcatConstants.TOMCAT_NOCOMPRESSIONUSERAGENTS);
            LOGGER.info("**********************sinotopia Tomcat Connector config finished!");
        }
    }
}
