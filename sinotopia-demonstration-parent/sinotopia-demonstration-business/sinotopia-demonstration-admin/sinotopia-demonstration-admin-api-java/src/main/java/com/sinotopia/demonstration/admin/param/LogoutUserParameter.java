package com.sinotopia.demonstration.admin.param;

import com.sinotopia.fundamental.api.params.BaseParameter;
import com.sinotopia.fundamental.api.params.SessionIdentity;

/**
 * 登录用户参数
 * Created by brucezee on 2017/3/1.
 */
public class LogoutUserParameter extends BaseParameter {
    /**
     * 登录信息
     */
    private SessionIdentity sessionIdentity;

    public SessionIdentity getSessionIdentity() {
        return sessionIdentity;
    }

    public void setSessionIdentity(SessionIdentity sessionIdentity) {
        this.sessionIdentity = sessionIdentity;
    }
}
