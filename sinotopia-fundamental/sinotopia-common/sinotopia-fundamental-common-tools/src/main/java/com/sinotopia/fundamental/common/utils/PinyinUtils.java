package com.hkfs.fundamental.common.utils;


import com.hkfs.fundamental.common.assist.CodeMocker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PinyinUtils {
	private static Logger logger = LoggerFactory.getLogger(CodeMocker.class);
	// 简体中文的编码范围从B0A1（45217）一直到F7FE（63486）
	private static int BEGIN = 45217;
	private static int END = 63486;
	// 按照声母表示，这个表是在GB2312中的出现的第一个汉字，也就是说“啊”是代表首字母a的第一个汉字。
	// i, u, v都不做声母, 自定规则跟随前面的字母
	private static char[] chartable = { '啊', '芭', '擦', '搭', '蛾', '发', '噶', '哈',
			'哈', '击', '喀', '垃', '妈', '拿', '哦', '啪', '期', '然', '撒', '塌', '塌',
			'塌', '挖', '昔', '压', '匝', };

	// 二十六个字母区间对应二十七个端点
	// GB2312码汉字区间十进制表示
	private static int[] table = new int[27];

	// 对应首字母区间表
	private static char[] initialtable = { 'A', 'B', 'C', 'D', 'E', 'F', 'G',
			'H', 'H', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
			'T', 'T', 'W', 'X', 'Y', 'Z', };
	private static char defaultChar = "#".charAt(0);
	
	// 初始化
	static {
		for (int i = 0; i < 26; i++) {
			table[i] = gbValue(chartable[i]);// 得到GB2312码的首字母区间端点表，十进制。
		}
		table[26] = END;// 区间表结尾
	}
	
	/**
	 * 获取中文字符的拼音的首字母（大写）
	 */
	public static String getFirstPinyinLetters(String source) {
		return getFirstPinyinLetters(source, false);
	}
	
	/**
	 * 获取中文字符的拼音的首字母
	 * @param lowerCase 是否返回小写
	 */
	public static String getFirstPinyinLetters(String source, boolean lowerCase) {
		StringBuilder sb = new StringBuilder("");
		int length = source.length();
		int i;
		try {
			for (i = 0; i < length; i++) {
				sb.append(char2Initial(source.charAt(i)));
			}
		}
		catch (Throwable e) {
			logger.error(e.getMessage(), e);
		}
		String pinyin = sb.toString();
		if (pinyin.length() == 0) {
			pinyin = "#";
		}
		if (lowerCase) {
			return pinyin.toLowerCase();
		}
		return pinyin;
	}

	/**
	 * 输入字符,得到他的声母,英文字母返回对应的大写字母,其他非简体汉字返回 '0'
	 * 
	 */
	private static char char2Initial(char ch) {
		// 对英文字母的处理：大写字母直接返回，小写字母转换为大写字母返回
		if (ch >= 'a' && ch <= 'z') {
			return (char) (ch - 'a' + 'A');
		}
		if (ch >= 'A' && ch <= 'Z') {
			return ch;
		}
		
		// 对非英文字母的处理：转化为首字母，然后判断是否在码表范围内，
		// 若不是，则直接返回。
		// 若是，则在码表内的进行判断。
		int gb = gbValue(ch);// 汉字转换首字母
		if ((gb < BEGIN) || (gb > END))// 不在码表区间直接返回#
			return defaultChar;
		
		int i;
		for (i = 0; i < 26; i++) {// 判断匹配码表区间，匹配到就break,判断区间形如“[,)”
			if ((gb >= table[i]) && (gb < table[i + 1])) {
				break;
			}
		}
		
		if (gb == END) {//不在码表区间直接返回3
			return defaultChar;
		}
		return initialtable[i]; // 在码表区间中，返回首字母
	}

	/**
	 * 取出汉字的编码 
	 */
	private static int gbValue(char ch) {// 将一个汉字（GB2312）转换为十进制表示。
		String str = String.valueOf(ch);
		try {
			byte[] bytes = str.getBytes("GB2312");
			if (bytes.length < 2)
				return 0;
			return (bytes[0] << 8 & 0xff00) + (bytes[1] & 0xff);
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
			return 0;
		}
	}
}
