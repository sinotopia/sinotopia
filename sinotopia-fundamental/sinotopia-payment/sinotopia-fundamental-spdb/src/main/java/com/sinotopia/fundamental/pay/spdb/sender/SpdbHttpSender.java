package com.sinotopia.fundamental.pay.spdb.sender;

import com.sinotopia.fundamental.pay.spdb.bean.BankInfoResponse;
import com.sinotopia.fundamental.pay.spdb.bean.MessageInfo;
import com.sinotopia.fundamental.pay.spdb.bean.WithDrawRecordsDetail;
import com.sinotopia.fundamental.pay.spdb.bean.WithDrawalsInfo;
import com.sinotopia.fundamental.pay.spdb.enums.*;
import com.sinotopia.fundamental.pay.spdb.exception.WithDrawConstants;
import com.sinotopia.fundamental.pay.spdb.util.HelperUtil;
import com.sinotopia.fundamental.pay.spdb.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * 浦发银行转账
 * 
 * @author liliang
 *
 */
public class SpdbHttpSender {


	private static Logger logger = LoggerFactory.getLogger(SpdbHttpSender.class);

	private VelocityEngine velocityEngine;

	public void setVelocityEngine(VelocityEngine velocityEngine){
		this.velocityEngine = velocityEngine;
	}

	/**
	 * 单笔支付 8801
	 * @param withDrawalsInfo
	 * @return
	 */
	public void sendWithDraw( WithDrawalsInfo withDrawalsInfo) {

		// 与浦发接口对接，通过请求签名——报文——验签获取最终结果
		Map<String,Object> map = HelperUtil.getMap(TransCode.WITHDRAW.getCode(), withDrawalsInfo);
		String final_result = signAndSendDoc(TransCode.WITHDRAW.getCode(), withDrawalsInfo.getElecChequeNo(),
				map);

		if (StringUtils.isNotEmpty(final_result)) {
			String transStatus = JsonUtil.getValueByJson(final_result, WithDrawConstants.TRANSSTATUS);// 交易状态
			String acceptNo = JsonUtil.getValueByJson(final_result, WithDrawConstants.ACCEPTNO);// 受理编号
			
			//设置银行流水号
			withDrawalsInfo.setAcceptNo(acceptNo);
			
			if (SpdbSatus.BACKDATA_FOUR.getCode() == Integer.parseInt(transStatus)) {
				
				withDrawalsInfo.setTransStatus(PayStatus.SUCCESS.getCode());
				
			} else if (SpdbSatus.BACKDATA_EIGHT.getCode() == Integer.parseInt(transStatus) || SpdbSatus.BACKDATA_NINE.getCode() == Integer.parseInt(transStatus)) {
				
				withDrawalsInfo.setTransStatus(PayStatus.FAILURE.getCode());
				
			} else {
				
				withDrawalsInfo.setTransStatus(PayStatus.PROCESSING.getCode());
				
			}
		}

	}

	/**
	 * 网银互联跨行转账
	 * @param withDrawalsInfo
	 * @return
	 */
	public void sendWithDrawEG(WithDrawalsInfo withDrawalsInfo) {

		// 与浦发接口对接，通过请求签名——报文——验签获取最终结果
		Map<String,Object> map = HelperUtil.getMap(TransCode.WITHDRAW_EG.getCode(), withDrawalsInfo);
		String final_result = signAndSendDoc( TransCode.WITHDRAW_EG.getCode(), withDrawalsInfo.getElecChequeNo(),
				map);
		if (StringUtils.isNotEmpty(final_result)) {
			
			String businessNo = JsonUtil.getValueByJson(final_result, WithDrawConstants.BUSINESSNO);// 业务编号
			
			withDrawalsInfo.setAcceptNo(businessNo);
			withDrawalsInfo.setTransStatus(PayStatus.PROCESSING.getCode());
		}
	}

	/**
	 * 转账信息查询
	 *
	 * @param withDrawalsInfo
	 * @return
	 */
	public void queryWithDraw(WithDrawalsInfo withDrawalsInfo) {

		// 与浦发接口对接，通过请求签名——报文——验签获取最终结果
		Map<String,Object> map = HelperUtil.getMap(TransCode.WITHDRAW_QUERY.getCode(), withDrawalsInfo);
		map.put("beginDate",HelperUtil.formatDate(withDrawalsInfo.getTimestamp(), WithDrawConstants.SDF_SHORT_VARCHAR));
		map.put("endDate", HelperUtil.formatDate(new Date(), WithDrawConstants.SDF_SHORT_VARCHAR));
		String final_result = signAndSendDoc(TransCode.WITHDRAW_QUERY.getCode(), withDrawalsInfo.getElecChequeNo(), map);
		if (StringUtils.isNotEmpty(final_result)) {

			String totalCount = JsonUtil.getValueByJson(final_result, WithDrawConstants.TOTALCOUNT);//交易笔数

			if (Integer.parseInt(totalCount) == 1) {

				String listsStr = JsonUtil.getValueByJson(final_result, WithDrawConstants.LISTS);
				String listStr = JsonUtil.getValueByJson(listsStr, WithDrawConstants.LIST);
				String transStatus = JsonUtil.getValueByJson(listStr, WithDrawConstants.TRANSSTATUS);//交易状态
				
				//只有待处理和进行中的订单才更新状态
				if(PayStatus.PROCESSING.getCode() == withDrawalsInfo.getTransStatus().intValue() || PayStatus.PENDING.getCode() == withDrawalsInfo.getTransStatus().intValue()){
					
					if (SpdbSatus.BACKDATA_FOUR.getCode() == Integer.parseInt(transStatus)) {
						
						//交易成功
						withDrawalsInfo.setTransStatus(PayStatus.SUCCESS.getCode());
						
					} else if (SpdbSatus.BACKDATA_EIGHT.getCode() == Integer.parseInt(transStatus)
							|| SpdbSatus.BACKDATA_NINE.getCode() == Integer.parseInt(transStatus)) {
						
						//交易失败
						withDrawalsInfo.setTransStatus(PayStatus.FAILURE.getCode());
						
					} else {
						
						//交易进行中
						withDrawalsInfo.setTransStatus(PayStatus.PROCESSING.getCode());
						
					}
				}
			}
			
		}
	}

	/**
	 * 账户明细查询
	 * @return
	 */
	public List<WithDrawRecordsDetail> queryDetails(String beginDate, String endDate, int queryNumber,int beginNumber) {

		// 与浦发接口对接，通过请求签名——报文——验签获取最终结果
		Map<String, Object> map = HelperUtil.getMap(TransCode.WITHDRAW_DETAIL.getCode(), null);
		map.put("acctNo", WithDrawConstants.ACCTNO);
		map.put("beginDate", beginDate);
		map.put("endDate", endDate);
		map.put("queryNumber", queryNumber);
		map.put("beginNumber", beginNumber);
		String final_result = signAndSendDoc(TransCode.WITHDRAW_DETAIL.getCode(), "", map);
		
		List<WithDrawRecordsDetail> dataList = null;
		
		if (StringUtils.isNotEmpty(final_result)) {
			
			String totalCount = JsonUtil.getValueByJson(final_result, WithDrawConstants.TOTALCOUNT);//交易笔数
			
			if (Integer.parseInt(totalCount) != 0) {
				
				String listsStr = JsonUtil.getValueByJson(final_result, WithDrawConstants.LISTS);
				String listStr = JsonUtil.getValueByJson(listsStr, WithDrawConstants.LIST);
				
				if(listStr != null && listStr !=""){
					
					if(listStr.indexOf("[") != 0){
						//当就查询到一条数据时，需加上[]才能转换成对象，否则会报错
						listStr = "[" + listStr + "]";
					}
					
					dataList = JsonUtil.jsonToList(listStr, WithDrawRecordsDetail.class);
				}
			}
		}
		return dataList;
	}

	/**
	 * 帐户余额查询
	 * @return
	 */
	public String getBalance() {
		
		String canUseBalance = "";
		// 与浦发接口对接，通过请求签名——报文——验签获取最终结果
		Map<String, Object> map = HelperUtil.getMap(TransCode.WITHDRAW_BALANCE.getCode(), null);
		map.put("acctNo", WithDrawConstants.ACCTNO);
		String final_result = signAndSendDoc( TransCode.WITHDRAW_BALANCE.getCode(), "", map);
		
		if (StringUtils.isNotEmpty(final_result)) {
			
			String lists = JsonUtil.getValueByJson(final_result, WithDrawConstants.LISTS);
			String list = JsonUtil.getValueByJson(lists, WithDrawConstants.LIST);
			canUseBalance = JsonUtil.getValueByJson(list, WithDrawConstants.CANUSEBALANCE);//可用余额
			
		}
		
		return canUseBalance;
	}

	/**
	 * 网银互联交易结果信息查询
	 * @param withDrawalsInfo
	 * @return
	 */
	public void queryWithDrawEG(WithDrawalsInfo withDrawalsInfo) {

		// 与浦发接口对接，通过请求签名——报文——验签获取最终结果
		Map<String,Object> map = HelperUtil.getMap(TransCode.WITHDRAW_EG_QUERY.getCode(), withDrawalsInfo);
		String final_result = signAndSendDoc( TransCode.WITHDRAW_EG_QUERY.getCode(), withDrawalsInfo.getElecChequeNo(), map);

		if (StringUtils.isNotEmpty(final_result)) {

			// businessStatus 0-失败\1-成功\2-待认证\3-在途
			String businessStatus = JsonUtil.getValueByJson(final_result, WithDrawConstants.BUSINESSSTATUS);
			
			
			//只有待处理和进行中的订单才更新状态
			if(PayStatus.PROCESSING.getCode() == withDrawalsInfo.getTransStatus().intValue() || PayStatus.PENDING.getCode() == withDrawalsInfo.getTransStatus().intValue()){
				
				if (BusinessSatus.BACKDATA_ONE.getCode() == Integer.parseInt(businessStatus)) {
					
					//交易成功
					withDrawalsInfo.setTransStatus(PayStatus.SUCCESS.getCode());
					
				} else if (BusinessSatus.BACKDATA_ZERO.getCode() == Integer.parseInt(businessStatus)) {
					
					//交易失败
					withDrawalsInfo.setTransStatus(PayStatus.FAILURE.getCode());
					
				} else {
					
					//交易进行中
					withDrawalsInfo.setTransStatus(PayStatus.PROCESSING.getCode());
					
				}
			}


		}

	}

	/**
	 * 网银支付行名行号表查询
	 * @param bankName
	 * @return
	 */
	public List<BankInfoResponse> queryBankNo(String bankName) {

		// 与浦发接口对接，通过请求签名——报文——验签获取最终结果
		Map<String,Object> map = HelperUtil.getMap(TransCode.WITHDRAW_BANKNO_QUERY.getCode(), null);
		map.put("bankName", bankName);
		String final_result = signAndSendDoc( TransCode.WITHDRAW_BANKNO_QUERY.getCode(), "", map);
		
		List<BankInfoResponse> dataList = null;
		
		if (StringUtils.isNotEmpty(final_result)) {
			
			String listsStr = JsonUtil.getValueByJson(final_result, WithDrawConstants.LISTS);
			String listStr = JsonUtil.getValueByJson(listsStr, WithDrawConstants.LIST);
			
			if(StringUtils.isNotBlank(listStr)){
				
				if(listStr.indexOf("[") != 0){
					//当就查询到一条数据时，需加上[]才能转换成对象，否则会报错
					listStr = "[" + listStr + "]";
				}
				
				dataList = JsonUtil.jsonToList(listStr, BankInfoResponse.class);
			}

		}
		return dataList;
	}

	/**
	 * 与浦发接口对接，通过请求签名——报文——验签获取最终结果
	 * @param transCode
	 * @param elecChequeNo
	 * @param map
	 * @return
	 */
	private <T> String signAndSendDoc(String transCode, String elecChequeNo,
			Map<String, Object> map) {
		String final_result = "";
		String getResult = "";
		String head = "";
		String body = "";

		// 请求签名
		long sign_beginTime = System.currentTimeMillis();
		String strXml = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
				"spdbtemplate/spdb_" + transCode + ".vm", WithDrawConstants.CHARSET_UTF8, map);// 获取单笔支付主体xml
		logger.info("签名内容:"+strXml);
		byte[] content = null;
		try {
			content  =  strXml.getBytes(WithDrawConstants.CHARSET_GB2312);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		getResult = send(transCode,elecChequeNo, content, WithDrawConstants.SPDB_SEND_SIGN, WithDrawConstants.SIGNIP,strXml);// 请求签名

		head = JsonUtil.getValueByJson(JsonUtil.xml2JSON(getResult), WithDrawConstants.HEAD);
		body = JsonUtil.getValueByJson(JsonUtil.xml2JSON(getResult), WithDrawConstants.BODY);
		String result = JsonUtil.getValueByJson(head, WithDrawConstants.RESULT);// 获得返回结果
		// 请求签名结束

		//0表示签名成功
		if (WithDrawConstants.SIGN_SUCCESS.equals(result)) {
			String sign = JsonUtil.getValueByJson(body, WithDrawConstants.SIGN);// 签名内容

			map.put(WithDrawConstants.SIGN, sign);//保存签名

			String sendContent = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "spdbtemplate/send.vm", WithDrawConstants.CHARSET_UTF8, map);// 得到完整的报文内容
			logger.info("报文主体:"+sendContent);
			
			// 报文长度+6,然后保证是六位数
			sendContent = HelperUtil.fillSeats(sendContent.length() + 6, 6) + sendContent;

			// 发送报文
			long sendDoc_beginTime = System.currentTimeMillis();
			getResult = send(transCode,elecChequeNo, sendContent.getBytes(), "text/html;", WithDrawConstants.SENDDOCIP,sendContent);// 发送报文
			getResult = getResult.substring(getResult.indexOf("<?xml"));
			
			head = JsonUtil.getValueByJson(JsonUtil.xml2JSON(getResult), WithDrawConstants.HEAD);
			body = JsonUtil.getValueByJson(JsonUtil.xml2JSON(getResult), WithDrawConstants.BODY);
			String returnCode = JsonUtil.getValueByJson(head, WithDrawConstants.RETURNCODE);// 获取返回结果

			//AAAAAAA表示报文成功
			if (WithDrawConstants.SENDDOC_SUCCESS.equals(returnCode)) {
				String signature = JsonUtil.getValueByJson(body, WithDrawConstants.SIGNATURE);//验签内容
				if (StringUtils.isNotBlank(signature)) {
					
					// 验签
					long validateSign_beginTime = System.currentTimeMillis();
					getResult = send(transCode,elecChequeNo, signature.getBytes(), WithDrawConstants.SPDB_SEND_VALIDATESIGN, WithDrawConstants.SIGNIP,signature);
					logger.info("验签结果:"+getResult);
					
					head = JsonUtil.getValueByJson(JsonUtil.xml2JSON(getResult), WithDrawConstants.HEAD);
					body = JsonUtil.getValueByJson(JsonUtil.xml2JSON(getResult), WithDrawConstants.BODY);
					String str_result = JsonUtil.getValueByJson(head, WithDrawConstants.RESULT);
					logger.info("验签结果主体:"+str_result);
					// 验签结束
					
					//0表示验签成功
					if (WithDrawConstants.SIGN_SUCCESS.equals(str_result)) {
						
						String sic = JsonUtil.getValueByJson(body, "sic");
						final_result = JsonUtil.getValueByJson(sic, WithDrawConstants.BODY);
						logger.info("最终结果:"+final_result);
						
						return final_result;
					} else {
						logger.error("验签出错");
						saveMessageInfo(validateSign_beginTime,elecChequeNo,WithDrawConstants.SIGNIP,signature,TransCode.getByCode(transCode).getBundlekey(),MessageInfoStatus.SUCCESS.getCode(),"验签出错","2");
					}
				}
			} else {
				String returnMsg = JsonUtil.getValueByJson(body,WithDrawConstants.RETURNMSG);//错误信息
				logger.error("报文出错:" + returnMsg);
				saveMessageInfo(sendDoc_beginTime,elecChequeNo,WithDrawConstants.SENDDOCIP,sendContent,TransCode.getByCode(transCode).getBundlekey(),MessageInfoStatus.SUCCESS.getCode(),"报文出错:" + returnMsg,"2");
			}

		} else {
			logger.error("签名出错");
			saveMessageInfo(sign_beginTime,elecChequeNo,WithDrawConstants.SIGNIP,strXml,TransCode.getByCode(transCode).getBundlekey(),MessageInfoStatus.SUCCESS.getCode(),"签名出错","2");
		}


		return "";
	}

	/**
	 * 发送请求
	 * 
	 * @param elecChequeNo
	 * @param content
	 * @param contentType
	 * @param url
	 * @return
	 */
	private String send(String transCode,String elecChequeNo, byte[] content, String contentType, String url,String xmlStr) {
		StringBuilder ret = new StringBuilder();
		long beginTime = System.currentTimeMillis();
		InputStream in = null;
		BufferedReader br = null;
		try {
			URL urll = new URL(url);
			HttpURLConnection con1 = (HttpURLConnection) urll.openConnection();
			con1.setDoInput(true);
			con1.setDoOutput(true);
			con1.setRequestMethod("GET");
			con1.setRequestProperty("Content-Type", contentType);
			con1.setRequestProperty("contentType", "gb2312");
			con1.setRequestProperty("Content-Length", String.valueOf(content.length));con1.disconnect();
			con1.connect();
			con1.getOutputStream().write(content);
			con1.getOutputStream().flush();
			in = con1.getInputStream();

			br = new BufferedReader(new InputStreamReader(in, WithDrawConstants.CHARSET_GB2312));
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				ret = ret.append(line);
			}
			
			//记录请求
			saveMessageInfo(beginTime,elecChequeNo,url,xmlStr,TransCode.getByCode(transCode).getBundlekey(),1,ret.toString(),"2");
		} catch (MalformedURLException ce) {
			
			logger.error("http请求出错:", ce);
			throw new RuntimeException("[SpdbHttpSender]http请求出错", ce);
			
		} catch (IOException e) {
			
			logger.error("代理程序网络异常:", e);
			throw new RuntimeException("浦发代理程序网络异常", e);
			
		}finally{
			
			if(null != br){
				try {
					br.close();
				} catch (IOException e) {
					logger.error("close BufferedReader error", e);
				}
			}
			
			if(null != in){
				try {
					in.close();
				} catch (IOException e) {
					logger.error("close InputStream error", e);
				}
			}
			
		}
		return ret.toString();
	}

	/**
	 * 记录请求
	 * @param orderNo
	 * @param url
	 * @param content
	 * @param orderType
	 * @param status
	 */
	private void saveMessageInfo(long beginTime,String orderNo,String url,String content,String orderType,Integer status,String result,String messageType){

		//本地IP地址
		String localIp = HelperUtil.getLocalIp();

		Date now=new Date();

		MessageInfo messageInfo = new MessageInfo();
		messageInfo.setOrderNo(orderNo);
		messageInfo.setUrl(url);
		messageInfo.setContent(new String(content).replaceAll("[\n\r]", ""));
		messageInfo.setMessageType(messageType);//类型，2为发起方，1为接收方
		messageInfo.setIp(localIp);
		messageInfo.setOrderType(orderType);
		messageInfo.setCreateTime(now);
		messageInfo.setStatus(status);//状态,0请求出错,1请求成功
		messageInfo.setResult(result);
		long endTime = System.currentTimeMillis();
		long responseTime = endTime-beginTime;//响应时间
		messageInfo.setResponseTime(String.valueOf(responseTime)+"ms");

		logger.info(messageInfo.toString());

	}

	
}
