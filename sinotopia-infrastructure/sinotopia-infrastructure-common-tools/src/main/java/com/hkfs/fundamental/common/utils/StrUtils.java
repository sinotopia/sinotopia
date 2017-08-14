package com.hkfs.fundamental.common.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtils {
	public static final String UTF_8 = "UTF-8";
	public static final String GBK = "GBK";
	public static final String ISO_8859_1 = "ISO-8859-1";
	public static final String GB2312 = "GB2312";
	public static final String DOUBLE_QUOTATION = "\"";

	/**
	 * 判断给定字符串是否空白串。
	 * 空白串是指由空格、制表符、回车符、换行符组成的字符串
	 * 若输入字符串为null或空字符串，返回true
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str)) {
			return true;
		}
		for (int i=0;i<str.length();i++) {
			char c = str.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 不为空返回true，为空返回false。
	 */
	public static boolean notEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 清除字符两端的空格（包括html空格，该空格用string.trim()方法无法去除）
	 */
	public static String trim(String htmlText) {
		if (htmlText == null) {
			return "";
		}
		if (htmlText.indexOf(160) == -1) {
			//不包含html空格
			return htmlText.trim();
		}
		char[] cArray = htmlText.toCharArray();
		int startIndex = 0;
		for (int i=0;i<cArray.length;i++) {
			//160在ascii码表中代表html空格
			if (cArray[i] != 160) {
				startIndex = i;
				break;
			}
		}
		int endIndex = cArray.length-1;
		if (endIndex > startIndex) {
			for (int j=cArray.length-1;j>=0;j--) {
				if (cArray[j] != 160) {
					endIndex = j;
					break;
				}
			}
			if (endIndex > startIndex) {
				char[] newArray = new char[endIndex-startIndex+1];
				int x = 0;
				for (int i=startIndex;i<=endIndex;i++) {
					newArray[x++] = cArray[i];
				}
				return new String(newArray).trim();
			}
		}
		return "";
	}

	/**
	 * 去除字符串两端的空格及双引号
	 * @param text
	 * @return
	 */
	public static String trimQuotation(String text) {
		text = trim(text);
		if (text.startsWith(DOUBLE_QUOTATION)) {
			text = text.substring(1);
		}
		if (text.endsWith(DOUBLE_QUOTATION)) {
			text = text.substring(0, text.length()-1);
		}
		return trim(text);
	}

	public static String valueOf(String str) {
		return str != null ? str.trim() : "";
	}

	public static boolean isUpperCase(char ch) {
		return ch >= 65 && ch <= 90;
	}

	public static boolean isLowerCase(char ch) {
		return ch >= 97 && ch <= 122;
	}

	public static boolean isEqual(String str1, String str2) {
		if (str1 != null && str2 != null) {
			return str1.equals(str2);
		}
		return false;
	}

	public static boolean notEqual(String str1, String str2) {
		return !isEqual(str1, str2);
	}

	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 判断字符串是否是由相同不同的字符所构成
	 * 如果由不同字符组成返回true，否则返回false
	 */
	public static boolean isDifferentCharString(String str) {
		if (str != null && str.length() > 1) {
			char[] charArray = str.toCharArray();
			char firstChar = charArray[0];
			boolean different = false;
			for (char c : charArray) {
				if (firstChar != c) {
					different = true;
					break;
				}
			}
			return different;
		}
		return false;
	}

	/**
	 * 链接字符串
	 * @param args 子字符串参数
	 * @return 链接后的字符串
	 */
	public static StringBuilder append(Object ... args) {
		StringBuilder sb = new StringBuilder();
		if (args != null && args.length > 0) {
			for (Object obj : args) {
				sb.append(obj);
			}
		}
		return sb;
	}

	/**
	 * 将列表对象转换成字符串链接
	 * @param list 需要链接的对象列表
	 * @return 链接后的字符串
	 */
	public static <T extends Object> String append(List<T> list) {
		if (list != null && list.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (Object obj : list) {
				sb.append(obj);
			}
			return sb.toString();
		}
		return null;
	}

	/**
	 * 将列表对象转换成字符串用指定的分隔符链接
	 * @param divider 分隔符
	 * @param list 需要链接的对象列表
	 * @return 链接后的字符串
	 */
	public static <T extends Object> String appendWithDivider(String divider, List<T> list) {
		if (list != null && list.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (Object obj : list) {
				sb.append(obj).append(divider);
			}
			return sb.substring(0, sb.length() - divider.length());
		}
		return null;
	}

	/**
	 * 将多个对象用指定的分隔符连接成一个字符串
	 * @param divider 分隔符
	 * @param objects 需要连接的对象
	 * @return 链接后的字符串
	 */
	public static String appendWithDivider(String divider, Object...objects) {
		if (objects != null && objects.length > 0) {
			StringBuilder sb = new StringBuilder();
			for (Object obj : objects) {
				sb.append(obj).append(divider);
			}
			return sb.substring(0, sb.length() - divider.length());
		}
		return null;
	}

	/**
	 * 获取字符串的md5
	 */
	public static String getMd5(String str) {
		if (str != null) {
			try {
				byte[] hash = MessageDigest.getInstance("MD5").digest(str.getBytes("UTF-8"));
				StringBuilder sb = new StringBuilder(hash.length * 2);
				for (byte b : hash) {
					if ((b & 0xFF) < 0x10) {
						sb.append("0");
					}
					sb.append(Integer.toHexString(b & 0xFF));
				}
				return sb.toString();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * 获取字节数组串的md5
	 */
	public static String getMd5(byte[] bytes) {
		if (bytes != null) {
			try {
				MessageDigest digest = MessageDigest.getInstance("MD5");
				digest.update(bytes);
				byte[] hash = digest.digest();
				StringBuilder sb = new StringBuilder(hash.length * 2);
				for (byte b : hash) {
					if ((b & 0xFF) < 0x10) {
						sb.append("0");
					}
					sb.append(Integer.toHexString(b & 0xFF));
				}
				return sb.toString();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * 判断是否为手机号码
	 */
	public static boolean isPhoneNumber(String text) {
		if (text == null) {
			return false;
		}
		return text.matches("^1[3|4|5|7|8][0-9]\\d{8}$");
	}

	/**
	 * 替换指定前缀和后缀之间的内容
	 * @param content 内容字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @param replacement 需要替换成的内容
	 * @return 替换后的字符串，替换失败返回null
	 */
	public static String replaceMiddleText(String content, String prefix, String suffix, String replacement) {
		return replaceMiddleText(content, prefix, suffix, replacement, true, true);
	}

	/**
	 * 替换指定前缀和后缀之间的内容
	 * @param content 内容字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @param replacement 需要替换成的内容
	 * @Param lazyPrefix 是否使用第一次找到的前缀作为截取前缀
	 * @Param lazySuffix 是否使用第一次找到的后缀作为截取后缀
	 * @return 替换后的字符串，替换失败返回null
	 */
	public static String replaceMiddleText(String content, String prefix, String suffix, String replacement, boolean lazyPrefix, boolean lazySuffix) {
		int index = lazyPrefix ? content.indexOf(prefix) : content.lastIndexOf(prefix);
		if (index >= 0) {
			String head = content.substring(0, index);
			String tail = content.substring(index + prefix.length());
			index = lazySuffix ? tail.indexOf(suffix) : tail.lastIndexOf(suffix);
			if (index >= 0) {
				tail = tail.substring(index + suffix.length());
				StringBuilder sb = new StringBuilder();
				sb.append(head);
				sb.append(prefix);
				sb.append(replacement);
				sb.append(suffix);
				sb.append(tail);
				return sb.toString();
			}
		}
		return null;
	}

	/**
	 * 替换指定前缀加后缀以及之间的内容
	 * @param content 内容字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @param replacement 需要替换成的内容
	 * @return 替换后的字符串，失败返回null
	 */
	public static String replaceWholeText(String content, String prefix, String suffix, String replacement) {
		return replaceWholeText(content, prefix, suffix, replacement, true, true);
	}
	/**
	 * 替换指定前缀加后缀以及之间的内容
	 * @param content 内容字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @param replacement 需要替换成的内容
	 * @Param lazyPrefix 是否使用第一次找到的前缀作为截取前缀
	 * @Param lazySuffix 是否使用第一次找到的后缀作为截取后缀
	 * @return 替换后的字符串，替换失败返回null
	 */
	public static String replaceWholeText(String content, String prefix, String suffix, String replacement, boolean lazyPrefix, boolean lazySuffix) {
		String result = replaceMiddleText(content, prefix, suffix, "!0#9$", lazyPrefix, lazySuffix);
		if (result != null) {
			result = result.replace(prefix + "!0#9$" + suffix, replacement);
		}
		return result;
	}

	/**
	 * 替换模板字符串中指定的值
	 * @param template 模板字符串，如：xxx{parameter1}yyyyxxaaaa{parameter2}ccc
	 * @param replacement 包含需要替换的字符串的key，value的映射
	 * @return
	 */
	public static String replaceText(String template, Map<String, String> replacement) {
		Matcher matcher = Pattern.compile("\\{(\\w+)\\}").matcher(template);
		while (matcher.find()) {
			String key = matcher.group(1);
			String value = replacement.get(key);
			if (value != null) {
				template = template.replace("{" + key + "}", value);
			}
		}
		return template;
	}


	/**
	 * 获取子字符串在父字符串中的个数
	 * @param parent 父字符串
	 * @param child 子字符串
	 */
	public static int getChildTextCount(String parent, String child) {
		Pattern pattern = Pattern.compile(child);
		Matcher matcher = pattern.matcher(parent);
		int count = 0;
		while(matcher.find()) {
			count++;
		}
		return count;
	}

	/**
	 * 读取xml文件字符串，去除其中的注释内容
	 * @param xmlFilePath xml文件内容
	 */
	public static String loadPlainXmlContent2(String xmlFilePath) throws Exception {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(xmlFilePath), "UTF-8"));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line).append("\n");
		}
		br.close();
		String content = sb.toString();
		int anotationStartTagCount = getChildTextCount(content, "<!--");
		for (int i=0;i<anotationStartTagCount;i++) {
			content = replaceWholeText(content, "<!--", "-->", "");
		}
		return content;
	}

	/**
	 * 读取xml文件字符串，去除其中的注释内容
	 * @param xmlFilePath xml文件内容
	 */
	public static String loadPlainXmlContent(String xmlFilePath) throws Exception {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(
						new FileInputStream(xmlFilePath), "UTF-8"));
		String line = null;
		StringBuilder sb = new StringBuilder();
		boolean anotationStarted = false;
		String anotationStartTag = "<!--";
		String anotationEndTag = "-->";
		//读取Manifest.xml文件字符串，去除其中的注释内容
		while ((line = br.readLine()) != null) {
			int index = line.indexOf(anotationStartTag);
			if (index != -1) {
				sb.append(line.substring(0, index));
				index = line.indexOf(anotationEndTag);
				if (index != -1) {
					anotationStarted = false;
					sb.append(line.substring(index+anotationEndTag.length()));
					continue;
				}
				else {
					anotationStarted = true;
					sb.append("\n");
					continue;
				}
			}
			index = line.indexOf(anotationEndTag);
			if (index != -1) {
				anotationStarted = false;
				sb.append(line.substring(index+anotationEndTag.length()));
				continue;
			}
			if (anotationStarted) {
				sb.append("\n");
				continue;
			}
			sb.append(line).append("\n");
		}
		br.close();
		return sb.toString();
	}

	/**
	 * 将二进制数组转换成十六进制字符串
	 * @param bytes 二进制数组
	 * @return 十六进制字符串
	 */
	public static String toHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i=0;i<bytes.length;i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() <= 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	/**
	 * 将int类型的IP地址转换成通常看到的ip地址
	 */
	public static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}

	/**
	 * 从url中解析出参数的键值对
	 */
	public static HashMap<String, String> getParametersFromUrl(String url) {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			URL u = new URL(url);
			String query = u.getQuery();
			if (StrUtils.notEmpty(query)) {
				StringTokenizer tokenizer = new StringTokenizer(query, "&");
				while(tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();
					String[] tokens = token.split("=");
					if (tokens.length  == 2) {
						map.put(tokens[0], tokens[1]);
					}
					else if (tokens.length == 1) {
						map.put(tokens[0], "");
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 从url中解析出参数
	 */
	public static String getParameterFromUrl(String url, String parameterName) {
		return getParametersFromUrl(url).get(parameterName);
	}

	/**
	 * 判断是否为移动的手机号码
	 */
	public static boolean isChinaMobileNumber(String phoneNumber) {
		if (phoneNumber == null || phoneNumber.length() != 11) {
			return false;
		}
		//非移动手机号的号段
		HashSet<String> notChinaMobileSet = new HashSet<String>(17);
		notChinaMobileSet.add("130");
		notChinaMobileSet.add("131");
		notChinaMobileSet.add("132");
		notChinaMobileSet.add("145");
		notChinaMobileSet.add("155");
		notChinaMobileSet.add("156");
		notChinaMobileSet.add("185");
		notChinaMobileSet.add("186");
		notChinaMobileSet.add("145");
		notChinaMobileSet.add("133");
		notChinaMobileSet.add("153");
		notChinaMobileSet.add("180");
		notChinaMobileSet.add("181");
		notChinaMobileSet.add("189");

		//移动手机号的号段
//		HashSet<String> chinaMobileSet = new HashSet<String>(17);
//		chinaMobileSet.add("134");
//		chinaMobileSet.add("135");
//		chinaMobileSet.add("136");
//		chinaMobileSet.add("137");
//		chinaMobileSet.add("138");
//		chinaMobileSet.add("139");
//		chinaMobileSet.add("147");
//		chinaMobileSet.add("150");
//		chinaMobileSet.add("151");
//		chinaMobileSet.add("152");
//		chinaMobileSet.add("157");
//		chinaMobileSet.add("158");
//		chinaMobileSet.add("159");
//		chinaMobileSet.add("182");
//		chinaMobileSet.add("183");
//		chinaMobileSet.add("184");
//		chinaMobileSet.add("187");
//		chinaMobileSet.add("188");
		//这里通过排除法判断中国移动手机号码
		return !notChinaMobileSet.contains(phoneNumber.substring(0, 3));
	}

	/**
	 * 获取字符串中介于prefix和suffix之间的字符串
	 * @param content
	 * @param prefix
	 * @param suffix
	 * @return 没有找到返回null。
	 */
	public static String getMiddleText(String content, String prefix, String suffix) {
		return getMiddleText(content, prefix, suffix, true, true);
	}

	/**
	 * 获取字符串中介于prefix和suffix之间的字符串
	 * @param content
	 * @param prefix
	 * @param suffix
	 * @Param lazyPrefix 是否使用第一次找到的前缀作为截取前缀
	 * @Param lazySuffix 是否使用第一次找到的后缀作为截取后缀
	 * @return 没有找到返回null。
	 */
	public static String getMiddleText(String content, String prefix, String suffix, boolean lazyPrefix, boolean lazySuffix) {
		if (content != null) {
			if (prefix != null && suffix != null) {
				//截取前缀到后缀的字符串
				int prefixIndex = lazyPrefix ? content.indexOf(prefix) : content.lastIndexOf(prefix);
				if (prefixIndex >= 0) {
					String tail = content.substring(prefixIndex + prefix.length());
					int suffixIndex = lazySuffix ? tail.indexOf(suffix) : tail.lastIndexOf(suffix);
					if (suffixIndex > 0) {
						return tail.substring(0, suffixIndex);
					}
				}
			}
			else if (prefix == null && suffix != null) {
				//截取后缀到末尾的字符串
				int suffixIndex = lazySuffix ? content.indexOf(suffix) : content.lastIndexOf(suffix);
				if (suffixIndex > 0) {
					return content.substring(0, suffixIndex);
				}
			}
			else if (prefix != null && suffix == null) {
				//截取开头到前缀的字符串
				int prefixIndex = lazyPrefix ? content.indexOf(prefix) : content.lastIndexOf(prefix);
				if (prefixIndex >= 0) {
					return content.substring(prefixIndex + prefix.length(), content.length());
				}
			}
		}
		return null;
	}

	/**
	 * 获取字符串中介于prefix和suffix之间的字符串 然后拼接上prefix和suffix返回
	 * @param content
	 * @param prefix
	 * @param suffix
	 * @Param lazyPrefix 是否使用第一次找到的前缀作为截取前缀
	 * @Param lazySuffix 是否使用第一次找到的后缀作为截取后缀
	 * @return 没有找到返回null。
	 */
	public static String getWholeText(String content, String prefix, String suffix, boolean lazyPrefix, boolean lazySuffix) {
		String middleText = getMiddleText(content, prefix, suffix, lazyPrefix, lazySuffix);
		if (middleText != null) {
			return new StringBuilder().append(StrUtils.valueOf(prefix)).append(middleText).append(StrUtils.valueOf(suffix)).toString();
		}
		return null;
	}

	/**
	 * 获取字符串中介于prefix和suffix之间的字符串 然后拼接上prefix和suffix返回
	 * @param content
	 * @param prefix
	 * @param suffix
	 * @return 没有找到返回null。
	 */
	public static String getWholeText(String content, String prefix, String suffix) {
		return getWholeText(content, prefix, suffix, true, true);
	}

	/**
	 * 格式化金额
	 * @param amount 金额
	 * @param decimalDigits 小数点位数
	 * @return
	 */
	public static String formatAmount(Double amount, int decimalDigits) {
		return formatAmount(amount, decimalDigits, false);
	}

	/**
	 * 格式化金额
	 * @param amount 金额
	 * @param decimalDigits 小数点位数
	 * @param scientific 是否使用科学计数法，即在用逗号每三位隔开
	 * @return
	 */
	public static String formatAmount(Double amount, int decimalDigits, boolean scientific) {
		if (amount == null) amount = 0d;
		String result = String.format("%."+decimalDigits+"f", amount);
		if (!scientific) {
			return result;
		}
		return formatScientificAmount(result);
	}

	/**
	 * 格式化金额
	 * @param amount 金额
	 * @param decimalDigits 小数点位数
	 * @param scientific 是否使用科学计数法，即在用逗号每三位隔开
	 * @param clearZeroTail 是否清除尾部0
	 * @return
	 */
	public static String formatAmount(Double amount, int decimalDigits, boolean scientific, boolean clearZeroTail) {
		String amountText = formatAmount(amount, decimalDigits, scientific);
		if (clearZeroTail) {
			int index = amountText.indexOf(".");
			if (index > 0) {
				StringBuilder sb = new StringBuilder(amountText);
				char chr;
				while ((chr = sb.charAt(sb.length()-1)) == 48 || chr == 46) {
					sb.deleteCharAt(sb.length()-1);
					if (chr == 46) break;//去除小数点后结束
				}
				return sb.toString();
			}
		}
		return amountText;
	}

	private static String formatScientificAmount(String amount) {
		String[] arr = amount.split("\\.");
		String prefix = arr[0];
		int count = prefix.length();
		StringBuilder sb = new StringBuilder();
		for (int i=0;i<count;i++) {
			sb.append(prefix.charAt(i));
			int c = count - 1 - i;
			if (c % 3 == 0 && c != 0) {
				sb.append(",");
			}
		}
		if (arr.length > 1) {
			for (int i=1;i<arr.length;i++) {
				sb.append(".").append(arr[i]);
			}
		}
		return sb.toString();
	}

	/**
	 * 格式化金额
	 * @param amount 金额
	 * @param decimalDigits 小数点位数
	 * @return
	 */
	public static String formatAmount(Float amount, int decimalDigits) {
		return formatAmount(amount, decimalDigits, false);
	}

	/**
	 * 格式化金额
	 * @param amount 金额
	 * @param decimalDigits 小数点位数
	 * @param scientific 是否使用科学计数法，即在用逗号每三位隔开
	 * @return
	 */
	public static String formatAmount(Float amount, int decimalDigits, boolean scientific) {
		if (amount == null) amount = 0f;
		String result = String.format("%."+decimalDigits+"f", amount);
		if (!scientific) {
			return result;
		}
		return formatScientificAmount(result);
	}

	/**
	 * 格式化金额
	 * @param amount 金额
	 * @param decimalDigits 小数点位数
	 * @param scientific 是否使用科学计数法，即在用逗号每三位隔开
	 * @param clearZeroTail 是否清除尾部0
	 * @return
	 */
	public static String formatAmount(Float amount, int decimalDigits, boolean scientific, boolean clearZeroTail) {
		String amountText = formatAmount(amount, decimalDigits, scientific);
		if (clearZeroTail) {
			int index = amountText.indexOf(".");
			if (index > 0) {
				StringBuilder sb = new StringBuilder(amountText);
				char chr;
				while ((chr = sb.charAt(sb.length()-1)) == 48 || chr == 46) {
					sb.deleteCharAt(sb.length()-1);
					if (chr == 46) break;//去除小数点后结束
				}
				return sb.toString();
			}
		}
		return amountText;
	}

	public static String formatFileSize(Long size, int decimalDigits) {
		if (size != null) {
			if (size < 1024) {
				return size+"B";
			}

			double dsize = size/1024.0;
			if (dsize < 1024) {
				return formatAmount(dsize, decimalDigits)+"K";
			}

			dsize = dsize/1024.0;
			if (dsize < 1024) {
				return formatAmount(dsize, decimalDigits)+"M";
			}

			dsize = dsize/1024.0;
			return formatAmount(dsize, decimalDigits)+"G";
		}
		return "未知";
	}

	public static String getUrlHost(String url) {
		if (url != null) {
			try {
				URL u = new URL(url);
				return u.getHost();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String getUrlReferer(String url) {
		if (url != null) {
			try {
				URL u = new URL(url);
				return u.getProtocol() + "://" + u.getHost();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获取去除参数后的连接地址
	 * @param url 包含参数的连接地址
	 * @return 失败返回null。
	 */
	public static String getPlainUrl(String url) {
		try {
			URL u = new URL(url);
			String query = u.getQuery();
			if (StrUtils.notEmpty(query)) {
				int index = url.indexOf(query);
				if (index > 0) {
					return url.substring(0, index).replace("?", "");
				}
			}
			else  {
				return url;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 在超链接后面添加参数
	 * @param url 连接地址
	 * @param name 参数名称
	 * @param value 参数值
	 * @param cover 是否覆盖已经存在的参数
	 * @return 新的连接地址，失败返回null。
	 */
	public static String appendUrlParameter(String url, String name, Object value, boolean cover) {
		return appendUrlParameter(url, new NamedValue(name, value), cover);
	}
	/**
	 * 在超链接后面添加参数
	 * @param url 连接地址
	 * @param namedValue 键值对
	 * @param cover 是否覆盖已经存在的参数
	 * @return 新的连接地址，失败返回null。
	 */
	public static String appendUrlParameter(String url, NamedValue namedValue, boolean cover) {
		List<NamedValue> pairList = new ArrayList<NamedValue>(1);
		pairList.add(namedValue);
		return appendUrlParameters(url, pairList, cover);
	}

	/**
	 * 在超链接后面添加参数
	 * @param url 连接地址
	 * @param namedValueList 键值对列表
	 * @param cover 是否覆盖已经存在的参数
	 * @return 新的连接地址，失败返回null。
	 */
	public static String appendUrlParameters(String url, List<NamedValue> namedValueList, boolean cover) {
		HashMap<String, Object> params = new HashMap<String, Object>(namedValueList.size());
		for (NamedValue namedValue : namedValueList) {
			params.put(namedValue.name, namedValue.value);
		}
		return appendUrlParameters(url, params, cover);
	}
	/**
	 * 在超链接后面添加参数
	 * @param url 连接地址
	 * @param params 键值对map
	 * @param cover 是否覆盖已经存在的参数
	 * @return 新的连接地址，失败返回null。
	 */
	public static <T extends Object> String appendUrlParameters(String url, Map<String, T> params, boolean cover) {
		String plainUrl = getPlainUrl(url);
		if (StrUtils.isEmpty(plainUrl)) {
			return null;
		}
		if (params == null || params.isEmpty()) {
			return url;
		}
		HashMap<String, String> existingParamsMap = StrUtils.getParametersFromUrl(url);
		Iterator<Entry<String, T>> newParamsIterator = params.entrySet().iterator();
		while (newParamsIterator.hasNext()) {
			Entry<String, T> entry = newParamsIterator.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			if (!cover && existingParamsMap.containsKey(key)) {
				continue;
			}
			existingParamsMap.put(key, value != null ? value.toString() : "");
		}
		Iterator<Entry<String, String>> existingParamsIterator = existingParamsMap.entrySet().iterator();
		StringBuilder sb = new StringBuilder();
		while (existingParamsIterator.hasNext()) {
			Entry<String, String> entry = existingParamsIterator.next();
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append("&").append(key).append("=").append(StrUtils.valueOf(value));
		}
		return new StringBuilder().append(plainUrl).append("?").append(sb.substring(1)).toString();
	}

	/**
	 * 判断是否是数字字符串
	 * @param text 字符串
	 * @param justNumbers 是否要求纯数字（没有小数点）
	 * @return 匹配返回true，否则返回false。
	 */
	public static boolean isNumbers(String text, boolean justNumbers) {
		if (text != null) {
			if (justNumbers) {
				return text.matches("[\\d]+");
			}
			return text.matches("^\\d$|^\\d+[.]?\\d+$");
		}
		return false;
	}

	/**
	 * 判断是否是纯数字字符串
	 * @param text 字符串
	 * @return 匹配返回true，否则返回false。
	 */
	public static boolean isNumbers(String text) {
		return isNumbers(text, true);
	}
	/**
	 * 对字符串中的各个字符按照一定的方式进行调换位置组成新的字符串
	 * @param str 原始字符串
	 * @return
	 */
	public static String shiftEncode(String str) {
		return shiftEncode(Base64.encode(str), toIntArray(StrUtils.class.getSimpleName()));
	}
	/**
	 * 对字符串中的各个字符按照一定的方式进行调换位置组成新的字符串
	 * @param str 原始字符串
	 * @param key 调换方式
	 * @return
	 */
	public static String shiftEncode(String str, int[] key) {
		char[] chr = str.toCharArray();
		for (int i=0;i<chr.length;i++) {
			swap(chr, i, key[i%key.length]%chr.length);
		}
		return String.valueOf(chr);
	}
	/**
	 * 对字符串中的各个字符按照一定的方式进行调换位置组成新的字符串
	 * @param str 原始字符串
	 * @param key 调换方式
	 * @return
	 */
	public static String shiftEncode(String str, String key) {
		return shiftEncode(str, toIntArray(key));
	}
	/**
	 * 对已经通过一定方式调换位置的字符进行还原，还原成原始字符串
	 * @param str 调换后的字符串
	 * @param key 调换方式
	 * @return
	 */
	public static String shiftDecode(String str, int[] key) {
		char[] chr = str.toCharArray();
		for (int i=chr.length-1;i>=0;i--) {
			swap(chr, i, key[i%key.length]%chr.length);
		}
		return String.valueOf(chr);
	}
	/**
	 * 对已经通过一定方式调换位置的字符进行还原，还原成原始字符串
	 * @param str 调换后的字符串
	 * @param key 调换方式
	 * @return
	 */
	public static String shiftDecode(String str, String key) {
		return shiftDecode(str, toIntArray(key));
	}
	/**
	 * 字符串转的int数组
	 */
	public static int[] toIntArray(String str) {
		int[] arr = new int[str.length()];
		for (int i=0;i<str.length();i++) {
			arr[i] = str.charAt(i);
		}
		return arr;
	}
	/**
	 * 交换字符数组中两个元素的位置
	 * @param chr
	 * @param x
	 * @param y
	 */
	public static void swap(char[] chr, int x, int y) {
		if (x < chr.length && y < chr.length && x != y) {
			char e = chr[x];
			chr[x] = chr[y];
			chr[y] = e;
		}
	}

	/**
	 * 获取一个字符串中第一次出现的数字连续字符串
	 * @param text
	 * @return
	 */
	public static String getFirstNumberFromText(String text) {
		return getFirstNumberFromText(text, true);
	}
	/**
	 * 获取一个字符串中第一次出现的数字连续字符串
	 * @param text
	 * @param justNumbers 是否要求纯数字（没有小数点）
	 * @return
	 */
	public static String getFirstNumberFromText(String text, boolean justNumbers) {
		if (StrUtils.notEmpty(text)) {
			String regex = justNumbers?"\\d+":"-?[\\d]+,?[\\d]+\\.?[\\d]+";
			Matcher numberMatcher = Pattern.compile(regex).matcher(text);
			if(numberMatcher.find()) {
				String number = numberMatcher.group();
				return number.replace(",", "");
			}
		}
		return "";
	}

	/**
	 * 从字符串中获取所有出现的连续数字的列表
	 * @param text
	 */
	public static List<String> getNumberListFromText(String text) {
		return getNumberListFromText(text, true);
	}

	/**
	 * 从字符串中获取所有出现的连续数字的列表
	 * @param text
	 * @param justNumbers 是否要求纯数字（没有小数点）
	 */
	public static List<String> getNumberListFromText(String text, boolean justNumbers) {
		if (StrUtils.notEmpty(text)) {
			String regex = justNumbers ? "\\d+" : "[\\d.]+";
			Matcher numberMatcher = Pattern.compile(regex).matcher(text);
			List<String> list = new ArrayList<String>();
			while (numberMatcher.find()) {
				list.add(numberMatcher.group());
			}
			return list;
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public static void printList(List list) {
		if (list != null) {
			for (Object obj : list) {
				System.out.println(obj);
			}
		}
	}
	public static void printArray(Object[] arr) {
		if (arr != null) {
			for (Object obj : arr) {
				System.out.println(obj);
			}
		}
	}
	/**
	 * 获取指定位数的随机数字字符串
	 * @param count 字符串位数
	 * @return
	 */
	public static String getRandomCode(int count) {
		StringBuilder sb = new StringBuilder();
		for (int i=0;i<count;i++) {
			sb.append(NumberUtils.random(0, 9));
		}
		return sb.toString();
	}

	public static String getString(Object object, String defaultValue) {
		if (object == null) {
			return defaultValue;
		}
		return String.valueOf(object);
	}

	public static String getString(Object object) {
		return getString(object, "");
	}

	public static class NamedValue {
		public String name;
		public Object value;

		public NamedValue(String name, Object value) {
			this.name = name;
			this.value = value;
		}
	}

	public static void main(String[] args) {
//		System.out.println(formatAmount(100000.00141000, 8, false, true));
//		System.out.println(formatAmount(100000d, 8, false, true));
//		System.out.println(formatAmount(100000.001, 8, false, true));
//		int[] y = new int[]{2,0,1,5,12,3,13,31,48};
		String y = "鼎有财2016";
		String x = "1234567890abcdefghijklmnopq";
		String z = shiftEncode(x, y);
		String d = shiftDecode(z, y);
		System.out.println(x);
		System.out.println(z);
		System.out.println(d);

		String url = "http://www.baidu.com/index?aa=2424&f=";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("name", "zhoub");
		params.put("gender", 1);
		params.put("f", "hello");
		System.out.println(appendUrlParameters(url, params, false));
		System.out.println(appendUrlParameters(url, params, true));


		String template = "aa{hello}aaaaaa{ok},{hello}";
		Map<String, String> map = new HashMap<String, String>();
		map.put("hello", "yes");
		map.put("ok", "world");
		System.out.println(replaceText(template, map));

		System.out.println(replaceMiddleText(template, "{", "}", "yes", false, false));

		System.out.println(unicode2String("\\u9A8C\\u8BC1\\u7801国\\u9519\\u8BEFafiiff中国"));
	}

	public static String setNullIfEmpty(String text) {
		if (StrUtils.isEmpty(text)) {
			return null;
		}
		return text;
	}

	/**
	 * 将unicode的字符串转换成中文字符串，如果包含非unicode则抛异常
	 * @param s unicode字符串，如：\u9A8C\u8BC1\u7801\u9519\u8BEF
	 * @return
	 */
	public static String unicode2StringUnsafe(String s) {
		int n = s.length() / 6;
		StringBuilder sb = new StringBuilder(n);
		int start = 0;
		for (int i = start, j = start + 2; i < n; i++, j += 6) {
			String code = s.substring(j, j + 4);
			char ch = (char) Integer.parseInt(code, 16);
			sb.append(ch);
		}
		return sb.toString();
	}

	/**
	 * 将unicode的字符串转换成中文字符串
	 * @param s unicode字符串，如：\u9A8C\u8BC1\u7801\u9519\u8BEF
	 * @return
	 */
	public static String unicode2String(String s) {
		if (StrUtils.notEmpty(s)) {
			StringBuilder sb = new StringBuilder();
			char[] arr = s.toCharArray();
			for (int i = 0; i < arr.length; i++) {
				if (arr[i] == '\\' && (i + 1 <= arr.length - 5) && arr[i + 1] == 'u') {
					StringBuilder builder = new StringBuilder();
					for (int x = 0; x < 6; x++) {
						builder.append(arr[i + x]);
					}
					i += 5;
					sb.append(unicode2StringUnsafe(builder.toString()));
				} else {
					sb.append(arr[i]);
				}
			}
			return sb.toString();
		}
		return s;
	}
}
