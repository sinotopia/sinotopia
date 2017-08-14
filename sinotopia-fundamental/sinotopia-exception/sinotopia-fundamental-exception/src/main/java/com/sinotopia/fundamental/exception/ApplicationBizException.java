package com.sinotopia.fundamental.exception;


/**
 * @description 业务异常
 */
public class ApplicationBizException extends RuntimeException {

    private static final long serialVersionUID = -2678203134198782909L;

    private Integer retCode;

    private String retMsg;

    public ApplicationBizException(Integer retCode, String retMsg) {
        super(retMsg);
        this.retCode = retCode;
        this.retMsg = retMsg;
    }

    public Integer getRetCode() {
        return retCode;
    }

    public void setRetCode(Integer retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }
}
