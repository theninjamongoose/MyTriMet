package com.example.tank.mytrimetpro.data.trimet.vehicle;

import android.content.Context;
import android.text.TextUtils;

import com.example.tank.mytrimetpro.R;
import com.example.tank.mytrimetpro.data.gson.serializer.TrimetDateAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jmiller on 8/16/2016.
 */

public class VehicleData {
    private static final int AT_CAPACITY_THRESHOLD = 98;
    private static final String STATUS_MESSAGE_CONTENT_BREAK = " | ";
    private static final int MIN_EARLY_STATUS = 1;
    @SerializedName("routeNumber")
    private int mRouteNumber;

    @SerializedName("loadPercentage")
    private Integer mLoadPercentage;

    @SerializedName("latitude")
    private double mLatitude;

    @SerializedName("longitude")
    private double mLongitude;

    @SerializedName("inCongestion")
    private Boolean mIsInCongestion;

    @SerializedName("vehicleID")
    private Integer mVehicleId;

    @SerializedName("bearing")
    private Float mBearing;

    @SerializedName("delay")
    private int mDelay;

    @SerializedName("signMessage")
    private String mSignMessage;

    @JsonAdapter(TrimetDateAdapter.class)
    private Calendar mEstimatedArivalTime;


    public int getRouteNumber() {
        return mRouteNumber;
    }
    public void setRouteNumber(int mRouteNumber) {
        this.mRouteNumber = mRouteNumber;
    }
    public Integer getVehicleId() {
        return mVehicleId;
    }
    public void setVehicleId(Integer mVehicleId) {
        this.mVehicleId = mVehicleId;
    }
    public Boolean getIsInCongestion() {
        return mIsInCongestion;
    }
    public void setIsInCongestion(Boolean mIsInCongestion) {
        this.mIsInCongestion = mIsInCongestion;
    }
    public double getLongitude() {
        return mLongitude;
    }
    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }
    public double getLatitude() {
        return mLatitude;
    }
    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }
    public int getLoadPercentage() {
        return mLoadPercentage;
    }
    public void setLoadPercentage(int mLoadPercentage) {
        this.mLoadPercentage = mLoadPercentage;
    }
    public Float getBearing() {
        return mBearing;
    }
    public void setBearing(Float bearing) {
        mBearing = bearing;
    }
    public String getSignMessage() {
        return mSignMessage;
    }
    public void setSignMessage(String mSignMessage) {
        this.mSignMessage = mSignMessage;
    }

    public String getStatusMessages(Context context) {
        List<String> statusMessages = new ArrayList<>();
        if(mDelay >= MIN_EARLY_STATUS){
            statusMessages.add(context.getString(R.string.early_vehicle));
        }
        if(mLoadPercentage != null && mLoadPercentage >= AT_CAPACITY_THRESHOLD){
            statusMessages.add(context.getString(R.string.at_vechicle_capacity));
        }
        if(Boolean.TRUE.equals(mIsInCongestion)){
            statusMessages.add(context.getString(R.string.vechicle_in_congestion));
        }
        return TextUtils.join(STATUS_MESSAGE_CONTENT_BREAK, statusMessages);
    }

    public int getmDelay() {
        return mDelay;
    }

    public void setmDelay(int mDelay) {
        this.mDelay = mDelay;
    }

    public Calendar getEstimatedArivalTime() {
        return mEstimatedArivalTime;
    }

    public void setEstimatedArivalTime(Calendar estimatedArivalTime) {
        this.mEstimatedArivalTime = estimatedArivalTime;
    }

    public String getTitle(Context context) {
        if(mEstimatedArivalTime != null) {
            return context.getString(R.string.minutes_abbreviated, getSignMessage(),
                    TimeUnit.MILLISECONDS.toMinutes(mEstimatedArivalTime.getTimeInMillis() - System.currentTimeMillis()));
        }
        return getSignMessage();
    }
}
