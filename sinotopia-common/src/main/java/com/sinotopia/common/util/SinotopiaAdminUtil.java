package com.sinotopia.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * sinotopia-x.x.x.jar到resources目录
 * Created by sinotopia on 2016/12/18.
 */
public class SinotopiaAdminUtil implements InitializingBean, ServletContextAware {

    private static Logger _log = LoggerFactory.getLogger(SinotopiaAdminUtil.class);

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        _log.info("===== sinotopia-admin =====");
        String version = PropertiesFileUtil.getInstance().get("sinotopia-admin.version");
        _log.info("sinotopia-admin.jar 版本: {}", version);
        String jarPath = servletContext.getRealPath("/WEB-INF/lib/sinotopia-admin-" + version + ".jar");
        _log.info("sinotopia-admin.jar 包路径: {}", jarPath);
        String resources = servletContext.getRealPath("/") + "/resources/sinotopia-admin";
        _log.info("sinotopia-admin.jar 解压到: {}", resources);
        JarUtil.decompress(jarPath, resources);
        _log.info("===== 解压zheng-admin完成 =====");
    }

}
