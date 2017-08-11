package com.hkfs.fundamental.pay.spdb.exception;

class ErrorCode {
	
	public String ERROR_EYY8989 = "上传签名标志signFlag需为1";
	public String ERROR_EYY0001 = "通讯报文格式错误（原因：报文的长度(报文的前4个字节)错误）";
	public String ERROR_EYY8990 = "报文流水号当天必须唯一";
	public String ERROR_EYY8994 = "结束日期不能在开始日期之前";
	public String ERROR_EYY8993 = "查询日期范围超过约定，请缩小日期范围 ";
	public String ERROR_EYY8986 = "帐户{0}的预约日期不能在当天以前";
	public String ERROR_EYY8979 = "客户号不能为空";
	public String ERROR_10 = "跨行转帐标志错误";
	public String ERROR_EYY9995 = "该账号({0})没有对此收款账号的付款权限";
	public String ERROR_EYY9000 = "帐户{0}已经销户";
	public String ERROR_09 = "跨行转帐标志不能为空";
	public String ERROR_EYY9991 = "此企业没有正常签约";
	public String ERROR_EYY9990 = "还未到此签约开通日期";
	public String ERROR_EYY0011 = "非工作时间,系统暂停服务";
	public String ERROR_EYY8992 = "帐户{0}没有签约开通此交易";
	public String ERROR_EYY8999 = "帐号{0}没有签约开通";
	public String ERROR_EYY8961 = "交易客户号({0})已关闭";
	public String ERROR_EYY8960 = "交易客户号({0})未开通此交易";
	public String ERROR_EYY9996 = "报文转换错误";
	public String ERROR_EYY9994 = "签名校验错误";
	public String ERROR_EYY8949 = "付款账号和网银授权客户号不匹配";
	public String ERROR_EYY8910 = "转账金额不能超过人民币5万元";
	public String ERROR_EYY8917 = "[{0}]和[{1}]不能同时为空";
	public String ERROR_EYY8953 = "支付记录账号[{0}]金额[{1}]的支付号不能为空";
	public String ERROR_EYY8952 = "支付记录账号[{0}]金额[{1}]的付款行号[{2}]输入有误";
	public String ERROR_EYY9993 = "签名错误";
	public String ERROR_EYY8998 = "超过帐号{0}的交易限额";
	public String ERROR_EYY8895 = "业务编号和电子凭证号不匹配";
	public String ERROR_EYY8991 = "帐户域不能为空";
	public String ERROR_EYY8970 = "输入字段({0})不能为空";
	public String ERROR_EYY8896 = "字段[{0}]格式错误";
	public String ERROR_EYY8968 = "输入字段({0})校验失败";
	public String ERROR_EYY9997 = "通讯错误";
	
	public String ERROR_EYY0010 = "系统忙,请稍候再查";
	public String ERROR_EYY8962 = "交易客户号({0})未签约";
	public String ERROR_EYY9999 = "系统内部错误，请查询";
	public String ERROR_EYY8901 = "电子凭证号[{0}]已存在";
	public String ETS0555 = "该笔交易不存在";
}
