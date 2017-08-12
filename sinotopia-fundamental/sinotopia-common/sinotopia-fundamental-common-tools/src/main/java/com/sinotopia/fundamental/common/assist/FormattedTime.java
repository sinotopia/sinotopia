package com.hkfs.fundamental.common.assist;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 用于获取某个时间区间的开始时间和结束时间
 * 如：某一天的开始时间和结束时间
 */
public class FormattedTime {
	private String yearCode;
	private String monthCode;
	private String dayCode;
	private String hourCode;
	private String weekCode;
	
	private Calendar calendar;
	private int year;
	private int month;
	private int day;
	private int hour;
	private int week;

	private boolean logEnabled = false;
	private long endTimeOffset = 1;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static final int YEAR_START_TIME = 1;
	public static final int YEAR_END_TIME = 2;
	public static final int MONTH_START_TIME = 3;
	public static final int MONTH_END_TIME = 4;
	public static final int DAY_START_TIME = 5;
	public static final int DAY_END_TIME = 6;
	public static final int HOUR_START_TIME = 7;
	public static final int HOUR_END_TIME = 8;
	public static final int WEEK_START_TIME = 9;
	public static final int WEEK_END_TIME = 10;
	
	public FormattedTime() {
		init(null);
	}
	public FormattedTime(Date date) {
		init(date.getTime());
	}
	public FormattedTime(Long millis) {
		init(millis);
	}

	public Date getTime(int filed) {
		if (filed == YEAR_START_TIME) {
			return getYearStartTime();
		}
		else if (filed == YEAR_END_TIME) {
			return getYearEndTime();
		}
		else if (filed == MONTH_START_TIME) {
			return getMonthStartTime();
		}
		else if (filed == MONTH_END_TIME) {
			return getMonthEndTime();
		}
		else if (filed == DAY_START_TIME) {
			return getDayStartTime();
		}
		else if (filed == DAY_END_TIME) {
			return getDayEndTime();
		}
		else if (filed == HOUR_START_TIME) {
			return getHourStartTime();
		}
		else if (filed == HOUR_END_TIME) {
			return getHourEndTime();
		}
		else if (filed == WEEK_START_TIME) {
			return getWeekStartTime();
		}
		else if (filed == WEEK_END_TIME) {
			return getWeekEndTime();
		}
		return null;
	}
	
	public Date getYearStartTime() {
		//年开始时间
		calendar.set(year, 0, 1, 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date yearStartTime = calendar.getTime();
		
		if (logEnabled) {
			System.out.println("year:"+dateFormat.format(yearStartTime));
		}
		return yearStartTime;
	}
	
	public Date getYearEndTime() {
		//年结束时间
		calendar.set(year+1, 0, 1, 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.setTimeInMillis(calendar.getTimeInMillis()-endTimeOffset);
		Date yearEndTime = calendar.getTime();
		
		if (logEnabled) {
			System.out.println("year:"+dateFormat.format(yearEndTime));
		}
		
		return yearEndTime;
	}
	
	public Date getMonthStartTime() {
		//月开始时间
		calendar.set(year, month-1, 1, 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date monthStartTime = calendar.getTime();
		
		if (logEnabled) {
			System.out.println("month:"+dateFormat.format(monthStartTime));
		}
		
		return monthStartTime;
	}
	
	public Date getMonthEndTime() {
		//月结束时间
		calendar.set(year, month, 1, 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.setTimeInMillis(calendar.getTimeInMillis()-endTimeOffset);
		Date monthEndTime = calendar.getTime();
		
		if (logEnabled) {
			System.out.println("month:"+dateFormat.format(monthEndTime));
		}
		
		return monthEndTime;
	}
	
	public Date getDayStartTime() {
		//日开始时间
		calendar.set(year, month-1, day, 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date dayStartTime = calendar.getTime();
		
		if (logEnabled) {
			System.out.println("day:"+dateFormat.format(dayStartTime));
		}
		
		return dayStartTime;
	}
	
	public Date getDayEndTime() {
		//日结束时间
		calendar.setTimeInMillis(getDayStartTime().getTime()+MILLIS_OF_DAY-endTimeOffset);
		Date dayEndTime = calendar.getTime();
		
		if (logEnabled) {
			System.out.println("day:"+dateFormat.format(dayEndTime));
		}
		
		return dayEndTime;
	}
	
	public Date getHourStartTime() {
		//时开始时间
		calendar.set(year, month-1, day, hour, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date hourStartTime = calendar.getTime();
		
		if (logEnabled) {
			System.out.println("hour:"+dateFormat.format(hourStartTime));
		}
		
		return hourStartTime;
	}
	
	public Date getHourEndTime() {
		//时结束时间
		calendar.setTimeInMillis(getHourStartTime().getTime()+MILLIS_OF_HOUR-endTimeOffset);
		Date hourEndTime = calendar.getTime();
		
		if (logEnabled) {
			System.out.println("hour:"+dateFormat.format(hourEndTime));
		}
		
		return hourEndTime;
	}
	
	public Date getWeekStartTime() {
		//周开始时间
		calendar.set(year, month-1, day+1, 0, 0, 0);//这里day加1后又减1毫秒主要是为了获取当天23:59:59
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.setTimeInMillis(calendar.getTimeInMillis()-1);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 1) {
			dayOfWeek = 7;
		}
		else {
			dayOfWeek = dayOfWeek-1;
		}
		long weekStartTimeMillis = calendar.getTimeInMillis() - (dayOfWeek*MILLIS_OF_DAY) + 1;
		calendar.setTimeInMillis(weekStartTimeMillis);
		Date weekStartTime = calendar.getTime();
		
		if (logEnabled) {
			System.out.println("week:"+calendar.get(Calendar.WEEK_OF_YEAR)+" "+dateFormat.format(weekStartTime));
		}
		
		return weekStartTime;
	}
	
	public Date getWeekEndTime() {
		//周结束时间
		calendar.setTimeInMillis(getWeekStartTime().getTime()+MILLIS_OF_WEEK-endTimeOffset);
		Date weekEndTime = calendar.getTime();
		
		if (logEnabled) {
			System.out.println("week:"+dateFormat.format(weekEndTime));
		}
		
		return weekEndTime;
	}

	public void setLogEnabled(boolean logEnabled) {
		this.logEnabled = logEnabled;
	}

	public void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public void setEndTimeOffset(long endTimeOffset) {
		this.endTimeOffset = endTimeOffset;
	}

	private void init(Long millis) {
//		时间段类型:1,total,累计;2,year,年;3,月,month;4,周,week;5,day,日;6,hour,小时;
		Locale.setDefault(Locale.CHINA);
		calendar = Calendar.getInstance();
		if (millis != null && millis > 0) {
			calendar.setTimeInMillis(millis);
		}

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH)+1;
		day = calendar.get(Calendar.DAY_OF_MONTH);
		hour = calendar.get(Calendar.HOUR_OF_DAY);
		week = calendar.get(Calendar.WEEK_OF_YEAR);
		
		yearCode = String.valueOf(year);
		monthCode = year+addZero(month);
		dayCode = monthCode+addZero(day);
		hourCode = dayCode+addZero(hour);
		weekCode = year+addZero(week);
	}
	
	private String addZero(int value) {
		StringBuilder sb = new StringBuilder();
		if (value < 10) {
			sb.append("0");
		}
		sb.append(value);
		return sb.toString();
	}
	
	public static final long MILLIS_OF_SECOND = 1000;
	public static final long MILLIS_OF_MINUTE = 60 * MILLIS_OF_SECOND;
	public static final long MILLIS_OF_HOUR = 60 * MILLIS_OF_MINUTE;
	public static final long MILLIS_OF_DAY = 24 * MILLIS_OF_HOUR;
	public static final long MILLIS_OF_WEEK = 7 * MILLIS_OF_DAY;

	public String getYearCode() {
		return yearCode;
	}

	public String getMonthCode() {
		return monthCode;
	}

	public String getDayCode() {
		return dayCode;
	}

	public String getHourCode() {
		return hourCode;
	}

	public String getWeekCode() {
		return weekCode;
	}
	
	public static void main(String[] args) {
		FormattedTime formattedTime = new FormattedTime();
		formattedTime.setLogEnabled(true);
		formattedTime.setEndTimeOffset(10000);
		System.out.println(formattedTime.getDayCode());
//		formattedTime.getDayStartTime();
		formattedTime.getDayEndTime();
		
		System.out.println(formattedTime.getHourCode());
		formattedTime.getHourEndTime();
		
		System.out.println(formattedTime.getMonthCode());
		formattedTime.getMonthStartTime();
		formattedTime.getMonthEndTime();
		
		System.out.println(formattedTime.getYearCode());
		formattedTime.getYearStartTime();
		formattedTime.getYearEndTime();
		
		System.out.println(formattedTime.getWeekCode());
		formattedTime.getWeekEndTime();
	}
}
