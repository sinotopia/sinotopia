package com.hkfs.fundamental.common.utils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MatcherUtils {
	private static Matcher numberMatcher = Pattern.compile("\\d+").matcher("");
	
	/**
	 * 获取字符串中的数字连续字符串并使用指定的分隔符连接成字符串
	 * @param text 字符串文本
	 * @param separator 找到数字连续字符串后连接的分隔符
	 * @return 使用指定的分隔符连接各个数字连续字符串的字符串
	 */
	public static String getNumbersFromText(String text, String separator) {
		if (separator == null) {
			separator = "";
		}
		StringBuilder sb = new StringBuilder();
		if (StrUtils.notEmpty(text)) {
			synchronized (numberMatcher) {
				numberMatcher.reset(text);
				while (numberMatcher.find()) {
					sb.append(separator+numberMatcher.group());
				}
			}
		}
		if (sb.length() > 0) {
			return sb.substring(separator.length());
		}
		return sb.toString();
	}
	
	/**
	 * 获取一个字符串中所有的数字连续字符串，返回各个数字的数组
	 */
	public static String[] getNumbersFromText(String text) {
		String separator = "#";
		String result = getNumbersFromText(text, separator);
		return result.split(separator);
	}
	
	/**
	 * 获取一个字符串中第一次出现的数字连续字符串
	 */
	public static String getFirstNumbersFromText(String text) {
		if (StrUtils.notEmpty(text)) {
			synchronized (numberMatcher) {
				numberMatcher.reset(text);
				if (numberMatcher.find()) {
					return numberMatcher.group();
				}
			}
		}
		return "";
	}
	
	public static void main(String[] args) {
//		String x = "aaaaaaaa32fffffff3aaaaaaaa2fffffff8";
		String x = "";
		System.out.println(Arrays.toString(getNumbersFromText(x)));
		System.out.println(getFirstNumbersFromText(x));
	}
}
