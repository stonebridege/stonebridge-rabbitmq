package com.stonebridge.springbootrabbitmq.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getDateStr(Date date) {
        return formatter.format(date);
    }
}
