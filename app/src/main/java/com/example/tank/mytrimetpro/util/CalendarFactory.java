package com.example.tank.mytrimetpro.util;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by tank on 8/29/16.
 */

public class CalendarFactory {

    private static final String DEFAULT_TIME_ZONE = "UTC";

    public static Calendar getUTCCalendar(){
        return Calendar.getInstance(TimeZone.getTimeZone(DEFAULT_TIME_ZONE));
    }
}
