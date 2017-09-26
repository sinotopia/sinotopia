package com.sinotopia.cms.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 服务启动类
 * Created by sinotopia on 2017/2/3.
 */
public class SinotopiaCmsRpcServiceApplication {

    private static Logger _log = LoggerFactory.getLogger(SinotopiaCmsRpcServiceApplication.class);

    public static void main(String[] args) {
        _log.info(">>>>> zheng-cms-rpc-service 正在启动 <<<<<");
        new ClassPathXmlApplicationContext("classpath:META-INF/spring/*.xml");
        _log.info(">>>>> zheng-cms-rpc-service 启动完成 <<<<<");
    }

}
