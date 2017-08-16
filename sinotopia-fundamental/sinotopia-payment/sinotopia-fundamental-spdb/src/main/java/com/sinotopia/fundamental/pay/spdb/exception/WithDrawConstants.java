package com.sinotopia.fundamental.pay.spdb.exception;


import com.sinotopia.fundamental.config.FundamentalConfigProvider;

public class WithDrawConstants {
	
	public final static String SIGNIP = FundamentalConfigProvider.getString("spdb.signIp");// 签名服务IP地址
	public final static String SENDDOCIP = FundamentalConfigProvider.getString("spdb.sendDocIp");// http服务IP地址
	public final static String MASTERID = FundamentalConfigProvider.getString("spdb.masterId");// 企业客户号
	public final static String ACCTNO = FundamentalConfigProvider.getString("spdb.acctNo");// 付款账号
	public final static String ACCTNAME = FundamentalConfigProvider.getString("spdb.acctName");// 付款人账户名称
	
	public static final String SDF_SHORT = "yyyy-MM-dd";
	public static final String SDF_LONG = "yyyy-MM-dd HH:mm:ss";
	public static final String SDF_LONG_NOSECOND = "yyyy-MM-dd HH:mm";
	public static final String SDF_LONG_VARCHAR = "yyyyMMddHHmmss";
	public static final String SDF_SHORT_VARCHAR = "yyyyMMdd";
	
	public final static String CHARSET_GB2312 = "gb2312";
	
	public final static String CHARSET_UTF8 = "UTF-8";
	
	public static String SPDB_SEND_SIGN = "INFOSEC_SIGN/1.0;charset=gb2312;";//签名http Content-Type
	public static String SPDB_SEND_VALIDATESIGN = "INFOSEC_VERIFY_SIGN/1.0;charset=gb2312;";//验签http Content-Type
	
	public static String TRANSSTATUS="transStatus";//8801交易状态
	public static String TRANSCODE="transCode"; //交易码
	public static String TOTALCOUNT="totalCount"; //交易数目
	public static String BUSINESSNO="businessNo";//EG01受理编号 
	public static String BUSINESSSTATUS="businessStatus";//EG30受理编号 
	public static String ACCEPTNO="acceptNo"; //8801业务编号
	public static String CANUSEBALANCE="canUseBalance"; //可用余额
	public static String HEAD="head"; //内容头部
	public static String BODY="body"; //内容主体
	public static String SIGNATURE="signature"; //发送报文后需要验签的内容
	public static String RESULT="result"; //结果
	public static String SIGN="sign"; //签名
	public static String RETURNCODE="returnCode";//返回编码
	public static String RETURNMSG="returnMsg";//错误时返回的错误信息
	
	//如<lists><list></list><list></list></lists>
	public static String LISTS="lists"; //多条数据
	public static String LIST="list"; //单条数据
	
	public static String SENDDOC_SUCCESS = "AAAAAAA";//报文成功
	public static String SIGN_SUCCESS = "0";//签名或验签成功
	

}
