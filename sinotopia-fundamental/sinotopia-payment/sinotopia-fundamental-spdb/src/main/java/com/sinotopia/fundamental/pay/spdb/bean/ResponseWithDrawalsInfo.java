package com.sinotopia.fundamental.pay.spdb.bean;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ResponseWithDrawalsInfo {
	private Integer status;//状态

	private String orderNo;//订单号
	
	private BigDecimal amount;//金额
	
	private String oidPaybill;//银行受理编号
	
	private Integer channelId;//通道编号
	
	private Map<String, Object> data = new HashMap<String, Object>();
	
	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getOidPaybill() {
		return oidPaybill;
	}

	public void setOidPaybill(String oidPaybill) {
		this.oidPaybill = oidPaybill;
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
	
}
