package com.hkfs.fundamental.httpclient.http;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolException;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.protocol.HttpContext;

/**
 * 默认爬虫跳转策略
 * Created by zhoubing on 2016/5/30.
 */
public class CoreRedirectStrategy extends DefaultRedirectStrategy {
    /**
     * 跳转策略，301,302进行跳转
     * @param request
     * @param response
     * @param context
     * @return
     * @throws ProtocolException
     */
    public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
        if (response != null) {
            int responseCode = response.getStatusLine().getStatusCode();
            return (responseCode == HttpStatus.SC_MOVED_PERMANENTLY
                    || responseCode == HttpStatus.SC_MOVED_TEMPORARILY);
        }
        return false;
    }
}
