package com.hkfs.fundamental.exception;


/**
 * @description 业务异常
 * @author dzr
 * @date 2015年10月16日 下午4:50:09
 *
 */
public class HkfsBizException extends RuntimeException {
	
	private static final long serialVersionUID = -2678203134198782909L;

	private Integer retCode;
	private String retMsg;

	public HkfsBizException(Integer retCode, String retMsg) {
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
