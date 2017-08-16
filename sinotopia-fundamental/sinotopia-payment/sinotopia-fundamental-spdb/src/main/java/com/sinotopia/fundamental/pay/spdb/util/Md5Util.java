package com.sinotopia.fundamental.pay.spdb.util;

import java.security.MessageDigest;

import org.apache.commons.lang.StringUtils;

/**
 * 32位MD5摘要算法
 * 
 */
public class Md5Util {

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 转换字节数组为高位字符串
	 * 
	 * @param b
	 *            字节数组
	 * @return
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * 检查签名
	 * 
	 * @param sign
	 * @param signData
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public static boolean checkSign(String sign, String signData, String charset)
			throws Exception {
		return StringUtils.equals(sign, digest(signData, charset));
	}

	/**
	 * MD5 摘要计算(byte[]).
	 * 
	 * @param src
	 *            byte[]
	 * @throws Exception
	 * @return String
	 */
	public static String digest(String signData, String charset) {
		try {
			MessageDigest alg = MessageDigest.getInstance(SignTypeEnum.MD5
					.getCode());
			StringBuffer result = new StringBuffer();
			byte[] bytes = alg.digest(signData.getBytes(charset));
			for (byte b : bytes) {
				result.append(byteToHexString(b));
			}
			return result.toString();
		} catch (Exception e) {
			return null;
		}

	}
}
