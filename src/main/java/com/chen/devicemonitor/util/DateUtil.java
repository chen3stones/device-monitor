package com.chen.devicemonitor.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateUtil {
    static String number_time = "yyyy-MM-dd HH:mm:ss";
    static String number_date = "yyyy.MM.dd";
    public static String getNumberTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat(number_time);
        return format.format(time);
    }
    public static String getYMD(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(number_date);
        return simpleDateFormat.format(time);
    }
}
