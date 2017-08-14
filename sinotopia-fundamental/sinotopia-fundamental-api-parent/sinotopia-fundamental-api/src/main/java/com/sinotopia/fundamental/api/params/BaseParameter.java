package com.sinotopia.fundamental.api.params;

import com.sinotopia.fundamental.api.data.DataObjectBase;

/**
 * 参数基类
 */
public class BaseParameter extends DataObjectBase {
    private static final long serialVersionUID = 1L;

    /**
     * 客户端的IP地址
     */
    private String clientIp;

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
}
