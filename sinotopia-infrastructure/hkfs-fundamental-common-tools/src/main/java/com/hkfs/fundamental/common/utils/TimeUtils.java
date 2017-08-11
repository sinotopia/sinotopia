package com.hkfs.fundamental.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author brucezee 2013-3-1 下午8:56:40
 */
public class TimeUtils {
	public static final long MILLIS_OF_SECOND = 1000L;
	public static final long MILLIS_OF_MINUTE = 60*MILLIS_OF_SECOND;
	public static final long MILLIS_OF_HOUR = 60*MILLIS_OF_MINUTE;
	public static final long MILLIS_OF_DAY = 24*MILLIS_OF_HOUR;
	public static final long MILLIS_OF_WEEK = 7*MILLIS_OF_DAY;
	public static final long ROUGH_MILLIS_OF_MONTH = 30*MILLIS_OF_DAY;


	public static final String FORMAT_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_YYYYMMDDHHMMSS2 = "yyyy/MM/dd HH:mm:ss";
	public static final String FORMAT_YYYYMMDDHHMMSS3 = "yy-MM-dd HH:mm:ss";

	public static final String FORMAT_YYYYMMDD = "yyyy-MM-dd";
	public static final String FORMAT_YYYYMMDD2 = "yyyy/MM/dd";
	public static final String FORMAT_YYYYMMDD3 = "yyyy年MM月dd日";

	public static final String FORMAT_YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
	public static final String FORMAT_YYYYMMDDHHMM2 = "yyyy/MM/dd HH:mm";

	public static final String FORMAT_YYYYMM = "yyyy-MM";
	public static final String FORMAT_YYYYMM2 = "yyyy/MM";
	public static final String FORMAT_YYYYMM3 = "yyyy年MM月";

	public static final String FORMAT_HHMMSS = "HH:mm:ss";

	public static final String DEFAULT_MONTH_CODE_FORMAT = "yyyyMM";
	/**
	 * 获取一个毫秒数对应的天数的整数值
	 */
	public static int getDayCount(long millis) {
		return (int) ((millis+MILLIS_OF_HOUR*8) / MILLIS_OF_DAY)+1;
	}
	
	/**
	 * 获取当前时间毫秒数对应的天数的整数值
	 */
	public static int getTodayDayCount() {
		return getDayCount(System.currentTimeMillis());
	}

	/**
	 * 获取时间的初略描述
	 */
	public static String getRoughTimeText(Long millisTime) {
		long millis = System.currentTimeMillis() - NumberUtils.valueOf(millisTime);
		if (millis < MILLIS_OF_MINUTE) {
			return "刚刚";
		}
		StringBuilder sb = new StringBuilder();
		if (millis < MILLIS_OF_HOUR) {
			sb.append((int) (millis/MILLIS_OF_MINUTE)).append("分钟");
		}
		else if (millis < MILLIS_OF_DAY) {
			sb.append((int) (millis/MILLIS_OF_HOUR)).append("小时");
		}
		else if (millis < MILLIS_OF_WEEK) {
			sb.append((int) (millis/MILLIS_OF_DAY)).append("天");
		}
		else if (millis < ROUGH_MILLIS_OF_MONTH) {
			sb.append((int) (millis/MILLIS_OF_WEEK)).append("周");
		}
		else {
			sb.append((int) (millis/ROUGH_MILLIS_OF_MONTH)).append("月");
		}
		sb.append("前");
		return sb.toString();
	}
	
	/**
	 * 获取毫秒的时间描述如：1天23小时15分4秒
	 */
	public static String formatMilliseconds(long millis) {
		StringBuilder sb = new StringBuilder();
		if (millis < 1000) {
			sb.append(millis).append("毫秒");
		}
		else if (millis < MILLIS_OF_MINUTE) {
			sb.append((int) (millis/1000)).append("秒");
			if (millis % 1000 != 0) {
				sb.append(formatMilliseconds(millis % 1000));
			}
		}
		else if (millis < MILLIS_OF_HOUR) {
			sb.append((int) (millis/MILLIS_OF_MINUTE)).append("分");
			if (millis % MILLIS_OF_MINUTE != 0) {
				sb.append(formatMilliseconds(millis % MILLIS_OF_MINUTE));
			}
		}
		else if (millis < MILLIS_OF_DAY) {
			sb.append((int) (millis/MILLIS_OF_HOUR)).append("小时");
			if (millis % MILLIS_OF_HOUR != 0) {
				sb.append(formatMilliseconds(millis % MILLIS_OF_HOUR));
			}
		}
		else if (millis < MILLIS_OF_WEEK) {
			sb.append((int) (millis/MILLIS_OF_DAY)).append("天");
			if (millis % MILLIS_OF_DAY != 0) {
				sb.append(formatMilliseconds(millis % MILLIS_OF_DAY));
			}
		}
		else if (millis < ROUGH_MILLIS_OF_MONTH) {
			sb.append((int) (millis/MILLIS_OF_WEEK)).append("周");
			if (millis % MILLIS_OF_WEEK != 0) {
				sb.append(formatMilliseconds(millis % MILLIS_OF_WEEK));
			}
		}
		else {
			sb.append((int) (millis/ROUGH_MILLIS_OF_MONTH)).append("月");
			if (millis % ROUGH_MILLIS_OF_MONTH != 0) {
				sb.append(formatMilliseconds(millis % ROUGH_MILLIS_OF_MONTH));
			}
		}
		return sb.toString();
	}
	
	/**
	 * 获取距离指定时间指定毫秒数的时间
	 */
	public static Date getNewDate(Date fromDate, long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(fromDate.getTime() + millis);
		return calendar.getTime();
	}
	
	/**
	 * 获取当前月的格式化字符串如：201309
	 */
	public static String getCurrentMonthCode() {
		return getCurrentMonthCode(DEFAULT_MONTH_CODE_FORMAT);
	}

	/**
	 * 获取当前月的格式化字符串如：201309,2013-09
	 */
	public static String getCurrentMonthCode(String format) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(Calendar.getInstance().getTime());
	}

	/**
	 * 获取上n个月的格式化字符串如：201309
	 */
	public static String getLastMonthCode(int n) {
		return getLastMonthCode(n, DEFAULT_MONTH_CODE_FORMAT);
	}

	/**
	 * 获取上n个月的格式化字符串如：201309,2013-09
	 */
	public static String getLastMonthCode(int n, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-n);
		return formatTime(calendar.getTime(), format);
	}

	/**
	 * 格式化时间
	 * @param timeMillis 时间毫秒数
	 * @param pattern 时间格式字符串 如：yyyy-MM-dd HH:mm:ss
	 * @return 指定格式的时间字符串
	 */
	public static String formatTime(Long timeMillis, String pattern) {
		if (timeMillis == null) timeMillis = 0l;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeMillis);
		return formatTime(calendar.getTime(), pattern);
	}
	
	/**
	 * 格式化时间
	 * @param time 日期
	 * @param pattern 时间格式字符串 如：yyyy-MM-dd HH:mm:ss
	 * @return 指定格式的时间字符串
	 */
	public static String formatTime(Date time, String pattern) {
		if (time != null && StrUtils.notEmpty(pattern)) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			return simpleDateFormat.format(time);
		}
		return null;
	}
	
	/**
	 * 获取当前时间
	 */
	public static Date getTime() {
		return new Date();
	}
	
	/**
	 * 转换日期字符串为日期对象
	 * @param timeText 日期字符串
	 * @param pattern 日期格式（如yyyy-MM-dd HH:mm:ss）
	 * @return 成功返回日期对象，失败返回null。
	 */
	public static Date parseDate(String timeText, String pattern) {
		if (StrUtils.notEmpty(timeText)) {
			try {
				return new SimpleDateFormat(pattern).parse(timeText);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 格式化日期字符串，兼容部分通用日期格式
	 * @param timeText
	 * @return
	 */
	public static Date parseDate(String timeText) {
		if (StrUtils.isEmpty(timeText)) {
			return null;
		}
		timeText = StrUtils.trim(timeText);
		if (timeText.contains("-")) {
			//常用的放在前面
			if (timeText.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
				return parseDate(timeText, FORMAT_YYYYMMDDHHMMSS);
			}
			if (timeText.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
				return parseDate(timeText, FORMAT_YYYYMMDD);
			}
			if (timeText.matches("^\\d{2}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
				return parseDate(timeText, FORMAT_YYYYMMDDHHMMSS3);
			}
			if (timeText.matches("^\\d{4}-\\d{1,2}$")) {
				return parseDate(timeText, FORMAT_YYYYMM);
			}
			if (timeText.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
				return parseDate(timeText, FORMAT_YYYYMMDDHHMM);
			}
		}

		if (timeText.contains("/")) {
			//常用的放在前面
			if (timeText.matches("^\\d{4}/\\d{1,2}/\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
				return parseDate(timeText, FORMAT_YYYYMMDDHHMMSS2);
			}
			if (timeText.matches("^\\d{4}/\\d{1,2}/\\d{1,2}$")) {
				return parseDate(timeText, FORMAT_YYYYMMDD2);
			}

			if (timeText.matches("^\\d{4}/\\d{1,2}$")) {
				return parseDate(timeText, FORMAT_YYYYMM2);
			}
			if (timeText.matches("^\\d{4}/\\d{1,2}/\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
				return parseDate(timeText, FORMAT_YYYYMMDDHHMM2);
			}
		}

		if (timeText.contains("年")) {
			if (timeText.matches("^\\d{4}年\\d{1,2}月\\d{1,2}日$")) {
				return parseDate(timeText, FORMAT_YYYYMMDD3);
			}

			if (timeText.matches("^\\d{4}年\\d{1,2}月$")) {
				return parseDate(timeText, FORMAT_YYYYMM3);
			}
		}

		throw new IllegalArgumentException("Invalid date format :" + timeText + "");
	}

	public static void main(String[] args) {
		System.out.println("yyyy-MM-dd HH:mm".length());
	}

	/**
	 * 将日期字符串转换成另外一种格式的日期字符串
	 * @param timeText 日期字符串
	 * @param pattern 原字符串的日期格式
	 * @param resultPattern 新日期的格式
	 * @return
	 */
	public static String parseDateString(String timeText, String pattern, String resultPattern) {
		Date date = parseDate(timeText, pattern);
		if (date != null) {
			return formatTime(date, resultPattern);
		}
		return null;
	}
	
	/**
	 * 转换日期字符串为时间毫秒数
	 * @param timeText 日期字符串
	 * @param pattern 日期格式（如yyyy-MM-dd HH:mm:ss）
	 * @return 成功返回时间毫秒数，失败返回null。
	 */
	public static Long parseTime(String timeText, String pattern) {
		Date date = parseDate(timeText, pattern);
		if (date != null) {
			return date.getTime();
		}
		return null;
	}
	
	/**
	 * 获取一个日期的“日”
	 */
	public static int getDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 获取一个日期的当月的天数
	 */
	public static int getDayCountOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

}
