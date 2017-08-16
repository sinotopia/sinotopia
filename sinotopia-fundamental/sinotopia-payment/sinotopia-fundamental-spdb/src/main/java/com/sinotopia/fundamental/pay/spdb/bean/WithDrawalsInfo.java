package com.sinotopia.fundamental.pay.spdb.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 代付参数对象
 * @author Administrator
 *
 */
public class WithDrawalsInfo {
	private String packetId;//报文号 赋值和订单编号一样  all required
	private Date timestamp;//订单创建时间 all required
	private String elecChequeNo;//订单编号 all required
	private Integer payeeType;//收款帐户类型,0-对公，1-卡  EG01  8801
	private String payeeAcctNo;//收款人账号 EG01  8801
	private String payeeName;//收款人名称 EG01  8801
	private String payeeBankName;//收款行名称
	private BigDecimal amount;//金额  EG01  8801
	private Integer sysflag;//本行他行标志 0-本行，1他行 8801
	private Integer remitLocation;//同城异地标志 0-同城 1-异地  8801
	private Integer payeeBankSelectFlag;//收款行速选标志 1-速选，当同城异地为异地时才生效  8801
	private String payeeBankNo;// EG01 银行行号

	private Integer transStatus;//交易状态 0-待处理 1-成功 2-失败 3-进行中  all required
	private String acceptNo;//受理编号/业务编号 EG30 8824
	private String note;//附言


	public String getPacketId() {
		return packetId;
	}
	public void setPacketId(String packetId) {
		this.packetId = packetId;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getElecChequeNo() {
		return elecChequeNo;
	}
	public void setElecChequeNo(String elecChequeNo) {
		this.elecChequeNo = elecChequeNo;
	}
	public Integer getPayeeType() {
		return payeeType;
	}
	public void setPayeeType(Integer payeeType) {
		this.payeeType = payeeType;
	}
	public String getPayeeAcctNo() {
		return payeeAcctNo;
	}
	public void setPayeeAcctNo(String payeeAcctNo) {
		this.payeeAcctNo = payeeAcctNo;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public String getPayeeBankName() {
		return payeeBankName;
	}
	public void setPayeeBankName(String payeeBankName) {
		this.payeeBankName = payeeBankName;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Integer getSysflag() {
		return sysflag;
	}
	public void setSysflag(Integer sysflag) {
		this.sysflag = sysflag;
	}
	public Integer getRemitLocation() {
		return remitLocation;
	}
	public void setRemitLocation(Integer remitLocation) {
		this.remitLocation = remitLocation;
	}
	public Integer getPayeeBankSelectFlag() {
		return payeeBankSelectFlag;
	}
	public void setPayeeBankSelectFlag(Integer payeeBankSelectFlag) {
		this.payeeBankSelectFlag = payeeBankSelectFlag;
	}
	public String getPayeeBankNo() {
		return payeeBankNo;
	}
	public void setPayeeBankNo(String payeeBankNo) {
		this.payeeBankNo = payeeBankNo;
	}
	public Integer getTransStatus() {
		return transStatus;
	}
	public void setTransStatus(Integer transStatus) {
		this.transStatus = transStatus;
	}
	public String getAcceptNo() {
		return acceptNo;
	}
	public void setAcceptNo(String acceptNo) {
		this.acceptNo = acceptNo;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

	
}
