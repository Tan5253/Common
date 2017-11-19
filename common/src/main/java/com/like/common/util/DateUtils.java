package com.like.common.util;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期工具类
 */
public class DateUtils {
    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
    private static final SimpleDateFormat sdf4 = new SimpleDateFormat("MM月dd日 HH:mm");
    private static final SimpleDateFormat sdf5 = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat sdf6 = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat sdf7 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取当前日期时间星期
     *
     * @return "yyyy-MM-dd HH:mm:ss 星期几"
     */
    public static String getCurrentDateTimeWeek() {
        StringBuilder sb = new StringBuilder();
        Date date = new Date(System.currentTimeMillis());
        sb.append(sdf7.format(date));
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String week = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(week)) {
            sb.append(" 星期日");
        } else if ("2".equals(week)) {
            sb.append(" 星期一");
        } else if ("3".equals(week)) {
            sb.append(" 星期二");
        } else if ("4".equals(week)) {
            sb.append(" 星期三");
        } else if ("5".equals(week)) {
            sb.append(" 星期四");
        } else if ("6".equals(week)) {
            sb.append(" 星期五");
        } else if ("7".equals(week)) {
            sb.append(" 星期六");
        }
        return sb.toString();
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Time getCurrentTime() {
        return new Time(System.currentTimeMillis());
    }

    /**
     * 获取日期字符串。
     *
     * @param time
     * @return yyyy年MM月dd日
     */
    public static String getDateString1(long time) {
        Date date = new Date(time);
        return sdf1.format(date);
    }

    /**
     * 获取日期字符串。
     *
     * @param time
     * @return yyyy-MM-dd
     */
    public static String getDateString2(long time) {
        Date date = new Date(time);
        return sdf2.format(date);
    }

    /**
     * 获取日期时间字符串。
     *
     * @param time
     * @return yyyy年MM月dd日 HH:mm:ss
     */
    public static String getDateString3(long time) {
        Date date = new Date(time);
        return sdf3.format(date);
    }

    /**
     * 获取日期时间字符串。
     *
     * @param time
     * @return MM月dd日 HH:mm
     */
    public static String getDateString4(long time) {
        Date date = new Date(time);
        return sdf4.format(date);
    }

    /**
     * 获取日期时间字符串。
     *
     * @param time
     * @return HH:mm
     */
    public static String getDateString5(long time) {
        Date date = new Date(time);
        return sdf5.format(date);
    }

    /**
     * 获取日期时间字符串。
     *
     * @param time
     * @return "yyyy-MM-dd HH:mm:ss"
     */
    public static String getDateString7(long time) {
        Date date = new Date(time);
        return sdf6.format(date);
    }

    /**
     * 获取日期时间字符串。
     *
     * @param time
     * @return HH:mm:ss
     */
    public static String getDateString6(long time) {
        Date date = new Date(time);
        return sdf6.format(date);
    }

    /**
     * 获取当前年份
     *
     * @return
     */
    public static int getCurYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public static int getCurMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前天
     *
     * @return
     */
    public static int getCurDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取指定偏移月份的年月
     *
     * @param baseDate    基准日期，传空就以当前日期为准
     * @param offsetMonth 偏移月份，可以正负
     * @return baseDate为"2016-9"，offsetMonth为-3，则返回"2016-6"--"2016-8"
     */
    public static DateInfo getDateInfoByOffset(String baseDate, int offsetMonth) {
        DateInfo dateInfo = new DateInfo();
        Calendar c = Calendar.getInstance();
        // 设置基准日期
        Date date = parseDate(baseDate);
        if (date != null) {
            c.setTime(date);//设置日历时间
        }
        dateInfo.baseYear = c.get(Calendar.YEAR);
        dateInfo.baseMonth = c.get(Calendar.MONTH);

        c.add(Calendar.MONTH, offsetMonth);
        dateInfo.otherYear = c.get(Calendar.YEAR);
        dateInfo.otherMonth = c.get(Calendar.MONTH) + 1;
        return dateInfo;
    }

    public static Date parseDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M");
        // 设置基准日期
        Date date = null;
        try {
            date = (Date) sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 用于分页的日期数据
     */
    public static class DateInfo {
        public int otherYear;
        public int otherMonth;
        public int baseYear;
        public int baseMonth;

        @Override
        public String toString() {
            return "DateInfo{" +
                    "otherYear=" + otherYear +
                    ", otherMonth=" + otherMonth +
                    ", baseYear=" + baseYear +
                    ", baseMonth=" + baseMonth +
                    '}';
        }
    }

    /**
     * 获取星期几
     *
     * @param time
     * @return
     */
    public static String getWeek(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        String result = "";
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                result = "周日";
                break;
            case Calendar.MONDAY:
                result = "周一";
                break;
            case Calendar.TUESDAY:
                result = "周二";
                break;
            case Calendar.WEDNESDAY:
                result = "周三";
                break;
            case Calendar.THURSDAY:
                result = "周四";
                break;
            case Calendar.FRIDAY:
                result = "周五";
                break;
            case Calendar.SATURDAY:
                result = "周六";
                break;
        }
        return result;
    }

    /**
     * 把今天、昨天、前天转换成时间
     *
     * @param custom
     * @return
     */
    public static String parseCustom2Date(String custom) {
        return null;
    }

    /**
     * 把时间转换成今天、昨天、前天
     *
     * @param time
     * @return
     */
    public static String parseDate2Custom(long time) {
        String result = null;
        try {
            Calendar now = Calendar.getInstance();
            long oneDayMillis = 24 * 60 * 60 * 1000;
            long todayPassMillis = 1000 * (now.get(Calendar.HOUR_OF_DAY) * 3600 + now.get(Calendar.MINUTE) * 60 + now.get(Calendar.SECOND));//毫秒数
            long nowMillis = now.getTimeInMillis();
            if (nowMillis - time < todayPassMillis) {
                result = "今天";
            } else if (nowMillis - time < (todayPassMillis + oneDayMillis)) {
                result = "昨天";
            } else if (nowMillis - time < (todayPassMillis + oneDayMillis * 2)) {
                result = "前天";
            } else {
                result = new SimpleDateFormat("yyyy-MM-dd").format(time);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 比较两个日期的大小
     *
     * @param date1
     * @param date2
     * @param format
     * @return
     */
    public static int compareDate(String date1, String date2, String format) {
        DateFormat df = new SimpleDateFormat(format);
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 是否今天
     *
     * @param date
     * @return
     */
    public static boolean isToday(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Calendar now = Calendar.getInstance();
        if (c.get(Calendar.MONTH) == now.get(Calendar.MONTH) && c.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH)) {
            return true;
        }
        return false;
    }

    /**
     * 是否今年
     *
     * @param date
     * @return
     */
    public static boolean isThisYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Calendar now = Calendar.getInstance();
        if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
            return true;
        }
        return false;
    }

    /**
     * 一年前就返回相隔的年数
     *
     * @param date
     * @return
     */
    public static int isPreYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.YEAR) - c.get(Calendar.YEAR);
    }

    /**
     * 判断是否为闰年
     */
    public static boolean isLeapYear(int year) {
        if (year % 100 == 0 && year % 400 == 0) {
            return true;
        } else if (year % 100 != 0 && year % 4 == 0) {
            return true;
        }
        return false;
    }

    /**
     * 指定某年中的某月的第一天是星期几
     *
     * @param year
     * @param month
     * @return
     */
    public static int getWeekdayOfMonth(int year, int month) {
        int dayOfWeek = 0;        //具体某一天是星期几
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return dayOfWeek;
    }

    /**
     * 获取某一天属于某个月的第几个星期，即第几排，用于改变背景
     *
     * @return
     */
    public static int getWeekOfMonth(int year, int mouth, int date) {
        String[] weeks = {"日", "一", "二", "三", "四", "五", "六"};
        Calendar c = Calendar.getInstance();
        c.set(year, mouth - 1, date);
        int week = c.get(Calendar.WEEK_OF_MONTH);//获取是本月的第几周
        int day = c.get(Calendar.DAY_OF_WEEK);//获致是本周的第几天地, 1代表星期天...7代表星期六
//        Logger.i("本月的第" + week + "周");
//        Logger.i("星期" + weeks[day - 1]);
        return week;
    }

    /**
     * 获取指定年、月的某月份的天数
     */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当月的天数
     */
    public static int getDaysOfCurrentMonth() {
        return getDaysByYearMonth(getCurYear(), getCurMonth());
    }

}
