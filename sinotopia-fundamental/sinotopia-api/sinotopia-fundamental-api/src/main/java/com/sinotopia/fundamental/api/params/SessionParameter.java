package com.sinotopia.fundamental.api.params;

/**
 * 需要登录的参数基类
 * Created by zhoubing on 2016/4/11.
 */
public class SessionParameter<T extends SessionIdentity> extends BaseParameter {
    private static final long serialVersionUID = 1L;

    private T sessionIdentity;

    public T getSessionIdentity() {
        return sessionIdentity;
    }

    public void setSessionIdentity(T sessionIdentity) {
        this.sessionIdentity = sessionIdentity;
    }
}
