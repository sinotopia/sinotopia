package com.hkfs.fundamental.api.params;

/**
 * 需要登录的参数基类
 * Created by zhoubing on 2016/4/11.
 */
public class SessionParameter extends BaseParameter {
    private static final long serialVersionUID = 1L;

    private SessionIdentity sessionIdentity;

    public SessionIdentity getSessionIdentity() {
        return sessionIdentity;
    }

    public void setSessionIdentity(SessionIdentity sessionIdentity) {
        this.sessionIdentity = sessionIdentity;
    }
}
