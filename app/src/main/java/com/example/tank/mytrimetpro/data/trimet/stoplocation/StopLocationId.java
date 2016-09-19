package com.example.tank.mytrimetpro.data.trimet.stoplocation;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jmiller on 8/16/2016.
 */

public class StopLocationId {
    @SerializedName("lng")
    private double mLongitude;
    @SerializedName("lat")
    private double mLatitude;
    @SerializedName("locid")
    private Integer mLocationId;

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
    public Integer getLocationId() {
        return mLocationId;
    }
    public void setLocationId(Integer mLocationId) {
        this.mLocationId = mLocationId;
    }
}
