package com.chen.devicemonitor.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateUtil {
    static String number_time = "yyyy-MM-dd HH:mm:SS";
    public static String getNumberTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat(number_time);
        return format.format(time);
    }
}
