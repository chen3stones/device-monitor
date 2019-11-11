package com.chen.devicemonitor.util;

public class StringUtil {
    public static boolean isEmpty(String s) {
        if(s == null || "".equals(s)) {
            return true;
        }
        return false;
    }
}
