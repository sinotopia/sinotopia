package com.hkfs.fundamental.httpclient.http;

import org.apache.http.cookie.*;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.protocol.HttpContext;

/**
 * Cookie
 * Created by zhoubing on 2016/6/12.
 */
public class CoreCookieSpecProvider implements CookieSpecProvider {
    /**
     * 不验证cookie的有效性
     * @param context
     * @return
     */
    public CookieSpec create(HttpContext context) {
        return new BrowserCompatSpec() {
            @Override
            public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
                // Oh, I am easy
            }
        };
    }
}
