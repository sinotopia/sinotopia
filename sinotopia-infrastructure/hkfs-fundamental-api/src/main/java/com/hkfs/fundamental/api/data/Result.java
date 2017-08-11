package com.hkfs.fundamental.api.data;

/**
 * <p>通用返回类</p>
 * <p>2015-06-24 14:05</p>
 * @author brucezee zhoubing213@163.com
 */
public class Result extends DataObjectBase {
	public static final long serialVersionUID = 1L;
	
	/**
	 * 返回结果错误码
	 */
	public Integer retCode;
	/**
	 * 返回结果消息
	 */
	public String retMsg;
	/**
	 * 返回结果数据
	 */
	public Object data;

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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Result [retCode=" + retCode + ", retMsg=" + retMsg + ", data=" + data + "]";
	}
}