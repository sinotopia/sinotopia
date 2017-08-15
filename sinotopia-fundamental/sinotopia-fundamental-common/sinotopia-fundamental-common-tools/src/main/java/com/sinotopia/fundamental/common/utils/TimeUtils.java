package com.sinotopia.fundamental.common.utils;

import com.sinotopia.fundamental.common.assist.CodeMocker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author brucezee 2013-3-1 下午8:56:40
 */
public class TimeUtils {

    private static Logger logger = LoggerFactory.getLogger(CodeMocker.class);
    public static final long MILLIS_OF_SECOND = 1000L;
    public static final long MILLIS_OF_MINUTE = 60 * MILLIS_OF_SECOND;
    public static final long MILLIS_OF_HOUR = 60 * MILLIS_OF_MINUTE;
    public static final long MILLIS_OF_DAY = 24 * MILLIS_OF_HOUR;
    public static final long MILLIS_OF_WEEK = 7 * MILLIS_OF_DAY;
    public static final long ROUGH_MILLIS_OF_MONTH = 30 * MILLIS_OF_DAY;

    public static final String FORMAT_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YYYYMMDDHHMMSS2 = "yyyy/MM/dd HH:mm:ss";
    public static final String FORMAT_YYYYMMDDHHMMSS3 = "yy-MM-dd HH:mm:ss";
    public static final String FORMAT_YYYYMMDDHHMMSS4 = "yyyyMMddHHmmss";

    public static final String FORMAT_YYYYMMDD = "yyyy-MM-dd";
    public static final String FORMAT_YYYYMMDD2 = "yyyy/MM/dd";
    public static final String FORMAT_YYYYMMDD3 = "yyyy年MM月dd日";
    public static final String FORMAT_YYYYMMDD4 = "yyyyMMdd";

    public static final String FORMAT_YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_YYYYMMDDHHMM2 = "yyyy/MM/dd HH:mm";

    public static final String FORMAT_YYYYMM = "yyyy-MM";
    public static final String FORMAT_YYYYMM2 = "yyyy/MM";
    public static final String FORMAT_YYYYMM3 = "yyyy年MM月";
    public static final String FORMAT_YYYYMM4 = "yyyyMM";

    public static final String FORMAT_YYYY = "yyyy";

    public static final String FORMAT_HHMMSS = "HH:mm:ss";

    /**
     * 获取一个毫秒数对应的天数的整数值
     */
    public static int getDayCount(long millis) {
        return (int) ((millis + MILLIS_OF_HOUR * 8) / MILLIS_OF_DAY) + 1;
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
            sb.append((int) (millis / MILLIS_OF_MINUTE)).append("分钟");
        } else if (millis < MILLIS_OF_DAY) {
            sb.append((int) (millis / MILLIS_OF_HOUR)).append("小时");
        } else if (millis < MILLIS_OF_WEEK) {
            sb.append((int) (millis / MILLIS_OF_DAY)).append("天");
        } else if (millis < ROUGH_MILLIS_OF_MONTH) {
            sb.append((int) (millis / MILLIS_OF_WEEK)).append("周");
        } else {
            sb.append((int) (millis / ROUGH_MILLIS_OF_MONTH)).append("月");
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
        } else if (millis < MILLIS_OF_MINUTE) {
            sb.append((int) (millis / 1000)).append("秒");
            if (millis % 1000 != 0) {
                sb.append(formatMilliseconds(millis % 1000));
            }
        } else if (millis < MILLIS_OF_HOUR) {
            sb.append((int) (millis / MILLIS_OF_MINUTE)).append("分");
            if (millis % MILLIS_OF_MINUTE != 0) {
                sb.append(formatMilliseconds(millis % MILLIS_OF_MINUTE));
            }
        } else if (millis < MILLIS_OF_DAY) {
            sb.append((int) (millis / MILLIS_OF_HOUR)).append("小时");
            if (millis % MILLIS_OF_HOUR != 0) {
                sb.append(formatMilliseconds(millis % MILLIS_OF_HOUR));
            }
        } else if (millis < MILLIS_OF_WEEK) {
            sb.append((int) (millis / MILLIS_OF_DAY)).append("天");
            if (millis % MILLIS_OF_DAY != 0) {
                sb.append(formatMilliseconds(millis % MILLIS_OF_DAY));
            }
        } else if (millis < ROUGH_MILLIS_OF_MONTH) {
            sb.append((int) (millis / MILLIS_OF_WEEK)).append("周");
            if (millis % MILLIS_OF_WEEK != 0) {
                sb.append(formatMilliseconds(millis % MILLIS_OF_WEEK));
            }
        } else {
            sb.append((int) (millis / ROUGH_MILLIS_OF_MONTH)).append("月");
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
        return getCurrentMonthCode(FORMAT_YYYYMM4);
    }

    /**
     * 获取当前月的格式化字符串如：201309,2013-09
     */
    public static String getCurrentMonthCode(String format) {
        return getLastMonthCode(0, format);
    }

    /**
     * 获取上n个月的格式化字符串如：201309
     */
    public static String getLastMonthCode(int n) {
        return getLastMonthCode(n, FORMAT_YYYYMM4);
    }

    /**
     * 获取上n个月的格式化字符串如：201309,2013-09
     */
    public static String getLastMonthCode(int n, String format) {
        return getLastMonthCode(new Date(), n, format);
    }

    /**
     * 获取某个时间点上n个月的格式化字符串如：201309,2013-09
     */
    public static String getLastMonthCode(Date date, int n, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - n);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return formatTime(calendar.getTime(), format);
    }

    /**
     * 格式化时间
     *
     * @param timeMillis 时间毫秒数
     * @param pattern    时间格式字符串 如：yyyy-MM-dd HH:mm:ss
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
     *
     * @param time    日期
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
     * 获取当前格式化时间字符串 yyyy-MM-dd HH:mm:ss
     */
    public static String getFormattedTime() {
        return formatTime(new Date(), FORMAT_YYYYMMDDHHMMSS);
    }

    /**
     * 转换日期字符串为日期对象
     *
     * @param timeText 日期字符串
     * @param pattern  日期格式（如yyyy-MM-dd HH:mm:ss）
     * @return 成功返回日期对象，失败返回null。
     */
    public static Date parseDate(String timeText, String pattern) {
        if (StrUtils.notEmpty(timeText)) {
            if (StrUtils.notEmpty(pattern)) {
                try {
                    return new SimpleDateFormat(pattern).parse(timeText);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            } else {
                return parseDate(timeText);
            }
        }
        return null;
    }

    /**
     * 格式化日期字符串，兼容部分通用日期格式
     *
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

        //常用的放在前面
        if (timeText.matches("\\d{6}")) {
            return parseDate(timeText, FORMAT_YYYYMM4);
        }
        if (timeText.matches("\\d{8}")) {
            return parseDate(timeText, FORMAT_YYYYMMDD4);
        }
        if (timeText.matches("\\d{14}")) {
            return parseDate(timeText, FORMAT_YYYYMMDDHHMMSS4);
        }

        throw new IllegalArgumentException("Invalid date format :" + timeText + "");
    }

    /**
     * 将日期字符串转换成另外一种格式的日期字符串
     *
     * @param timeText      日期字符串
     * @param pattern       原字符串的日期格式
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
     * 将日期字符串转换成另外一种格式的日期字符串
     *
     * @param timeText      日期字符串
     * @param resultPattern 新日期的格式
     * @return
     */
    public static String parseDateString(String timeText, String resultPattern) {
        return parseDateString(timeText, null, resultPattern);
    }

    /**
     * 转换日期字符串为时间毫秒数
     *
     * @param timeText 日期字符串
     * @param pattern  日期格式（如yyyy-MM-dd HH:mm:ss）
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

    /**
     * 加上天数
     *
     * @param date 日期
     * @param day  天数
     * @return
     */
    public static Date addDays(Date date, int day) {
        return date == null ? null : new Date(date.getTime() + TimeUtils.MILLIS_OF_DAY * day);
    }

    /**
     * 判断两个日期是否是同一天
     *
     * @param d1
     * @param d2
     * @return
     */
    public static boolean isSameDay(Date d1, Date d2) {
        if (d1 != null && d2 != null) {
            return getDaysDate(d1).equals(getDaysDate(d2));
        }
        return false;
    }

    /**
     * 获取一个Date的去除时分秒毫秒的日期Date
     *
     * @param d
     * @return
     */
    public static Date getDaysDate(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取两个时间点相差的天数
     *
     * @param d1
     * @param d2
     * @return
     */
    public static int getDaysCount(Date d1, Date d2) {
        long time1 = getDaysDate(d1).getTime();
        long time2 = getDaysDate(d2).getTime();
        return ((Long) Math.abs((time2 - time1) / TimeUtils.MILLIS_OF_DAY)).intValue();
    }

    /**
     * 根据两个月份的代号生成其中间的月份代号
     *
     * @param startMonthCode  开始月份代号，如201601
     * @param endMonthCode    结束月份代号，如201608
     * @param monthCodeFormat 开始和结束月份代号的格式，如yyyyMM
     * @return 两个月份代号之间的月份代号
     */
    public static List<String> getMonthCodeList(String startMonthCode, String endMonthCode, String monthCodeFormat) {
        return getMonthCodeList(startMonthCode, endMonthCode, monthCodeFormat, monthCodeFormat);
    }

    /**
     * 根据两个月份的代号生成其中间的月份代号
     *
     * @param startMonthCode     开始月份代号，如201601
     * @param endMonthCode       结束月份代号，如201608
     * @param monthCodeFormat    开始和结束月份代号的格式，如yyyyMM
     * @param newMonthCodeFormat 新月份代号的格式，如yyyy-MM
     * @return 两个月份代号之间的月份代号
     */
    public static List<String> getMonthCodeList(String startMonthCode, String endMonthCode, String monthCodeFormat, String newMonthCodeFormat) {
        Date startTime = TimeUtils.parseDate(startMonthCode, monthCodeFormat);
        Date endTime = TimeUtils.parseDate(endMonthCode, monthCodeFormat);
        if (startTime.after(endTime)) {
            throw new IllegalArgumentException("illegal start month and end month");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        List<String> list = new LinkedList<String>();
        String newStartMonthCode = TimeUtils.formatTime(calendar.getTime(), newMonthCodeFormat);//这里的时间处理必须与getLastMonthCode里的时间处理一致，否则死循环
        int i = 0;
        String monthCode = null;
        while (true) {
            monthCode = TimeUtils.getLastMonthCode(endTime, i, newMonthCodeFormat);
            list.add(0, monthCode);
            if (newStartMonthCode.equals(monthCode)) {
                break;
            }
            i++;

            if (i > 100) {
                throw new IllegalArgumentException("dead loop");
            }
        }

        return list;
    }
}
