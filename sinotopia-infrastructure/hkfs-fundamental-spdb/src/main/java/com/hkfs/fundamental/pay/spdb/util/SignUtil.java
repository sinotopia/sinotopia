package com.hkfs.fundamental.pay.spdb.util;

import com.hkfs.fundamental.config.FundamentalConfigProvider;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * LLpay 签名等工具函数
 * 
 *
 */
public final class SignUtil {
	private static final Logger logger = LoggerFactory.getLogger(SignUtil.class);

	private static final String UTF_8 = "UTF-8";

	/**
	 * 生成待签名串
	 * 
	 * @param map
	 * @return
	 */
	public static String genSignData(Map<String, Object> map) {
		// 按照key做首字母升序排列
		List<String> keys = new ArrayList<String>();
		Iterator<String> iKeys = map.keySet().iterator();
		while (iKeys.hasNext()) {
			keys.add(iKeys.next());
		}
		Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
		StringBuffer content = new StringBuffer();
		for (String key : keys) {

			// sign字段不参与签名
			if ("sign".equals(key)) {
				continue;
			}

			Object value = map.get(key);

			// 空串不参与签名
			if (value == null || "".equals(value.toString())) {
				continue;
			}
			content.append("&").append(key).append("=").append(value);
		}
		String result = content.toString();
		if (result.startsWith("&")) {
			result = content.substring(1);
		}
		return result;
	}

	/**
	 * 加签
	 * 
	 * @return
	 */
	public static String getSign(String signData) {
		try {
			signData += "&key=" + FundamentalConfigProvider.getString("spdb.md5Key");
			return Md5Util.digest(signData, UTF_8);
		} catch (Exception e) {
			logger.error("获取签名出错", e);
			return null;
		}
	}

	/**
	 * 签名验证
	 * 
	 * @param map
	 * @return
	 */
	public static boolean checkSign(Map<String, Object> map) {
		try {
			String sign = ObjectUtils.toString(map.get("sign"));
			String signData = genSignData(map);
			signData += "&key=" + FundamentalConfigProvider.getString("spdb.md5Key");
			return Md5Util.checkSign(sign, signData, UTF_8);
		} catch (Exception e) {
			logger.error("签名校验出错", e);
			return false;
		}
	}

}
