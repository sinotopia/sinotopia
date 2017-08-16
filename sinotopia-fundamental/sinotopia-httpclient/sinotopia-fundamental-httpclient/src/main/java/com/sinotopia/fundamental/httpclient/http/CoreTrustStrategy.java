package com.sinotopia.fundamental.httpclient.http;

import org.apache.http.conn.ssl.TrustStrategy;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 信任所有证书
 * Created by zhoubing on 2016/6/12.
 */
public class CoreTrustStrategy implements TrustStrategy {
    /**
     * 信任所有
     * @param chain
     * @param authType
     * @return
     * @throws CertificateException
     */
    @Override
    public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        return true;
    }
}
