package com.example.tank.mytrimetpro.util;

import android.content.Context;

import com.example.tank.mytrimetpro.data.googleroute.Leg;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Calculation {
    private static final double MILLISEC_TO_MINUTES_TO_DOUBLE = 60000.0;
    private final Context mContext;

    Calculation(Context context){
        mContext = context;
    }

//    public static Calendar getNotifcationTime(Context context, Leg leg, VehicleData vehicleData){
//        Calendar lastTimeToLeave;
//        int notifyMinBefore;
//        if(vehicleData != null && vehicleData.getEstimatedArivalTime() != null){
//            lastTimeToLeave = vehicleData.getEstimatedArivalTime();
//            notifyMinBefore = getMinToFirstStop(context, leg);
//        } else {
//            lastTimeToLeave  = leg.getDepartureTime().getValue();
//            notifyMinBefore = UserSettingsUtil.INSTANCE.getFirstStopNotificationStoredTime(context);
//        }
//        lastTimeToLeave.add(Calendar.MINUTE, - notifyMinBefore);
//        return lastTimeToLeave;
//    }
//
    public int getMinToFirstStop(Leg leg){
//        int userBufferTimeMin = UserSettingsUtil.INSTANCE.getFirstStopNotificationStoredTime(context);
        int walkDuration = (int) Math.ceil(leg.getSteps().get(0).getDuration().getValue().getTimeInMillis()/MILLISEC_TO_MINUTES_TO_DOUBLE);
//        return userBufferTimeMin + walkDuration;
        return walkDuration;
    }

    public Calendar getMinArivalTimeMilliSec(Leg leg){
        long millisecToFirstStop = TimeUnit.MINUTES.toMillis(getMinToFirstStop(leg));
        Calendar calendar = CalendarFactory.getUTCCalendar();
        calendar.setTimeInMillis(calendar.getTimeInMillis() + millisecToFirstStop);
        return calendar;
    }
}

