package com.like.common.util;

import java.util.Calendar;

/**
 * 日历相关的工具类
 */
public class CalendarUtils {
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
     * 得到某月有多少天数
     *
     * @param isLeapyear
     * @param month
     * @return
     */
    public static int getDaysOfMonth(boolean isLeapyear, int month) {
        int daysOfMonth = 0;      //某月的天数
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                daysOfMonth = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                daysOfMonth = 30;
                break;
            case 2:
                if (isLeapyear) {
                    daysOfMonth = 29;
                } else {
                    daysOfMonth = 28;
                }

        }
        return daysOfMonth;
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

}
