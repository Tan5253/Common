package com.like.common.util;

/**
 * 中文相关的工具类
 *
 * @author like
 * @version 1.0
 * @created at 2017/1/2 16:31
 */
public class ChineseUtils {
    public static String parseNumber2Chinese(int number) {
        String result = "";
        switch (number) {
            case 1:
                result = "一";
                break;
            case 2:
                result = "二";
                break;
            case 3:
                result = "三";
                break;
            case 4:
                result = "四";
                break;
            case 5:
                result = "五";
                break;
            case 6:
                result = "六";
                break;
            case 7:
                result = "七";
                break;
            case 8:
                result = "八";
                break;
            case 9:
                result = "九";
                break;
        }
        return result;
    }
}
