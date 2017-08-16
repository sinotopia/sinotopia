package com.sinotopia.fundamental.pay.spdb.bean;

import java.util.Date;

/**
 * 请求记录表
 * @author Administrator
 *
 */
public class MessageInfo {

	private String orderNo;//订单号
	private String url;//请求地址
	private String content;//参数
	private String messageType;//类型，2为发起方，1为接收方
	private String ip;//IP地址
	private String result;//请求返回结果
	private String orderType;//订单类型
	private Date createTime;//创建时间
	private Integer status;//状态,0请求出错,1请求成功
	private String responseTime;//请求响应时间

	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}

	public String toString(){
		return orderNo + "#" + url + "#" + content + "#" + messageType + "#" +ip + "#" + result + "#" + orderType + "#" + createTime + "#" + status + "#" + responseTime;
	}
	
	
}
