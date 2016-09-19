package com.example.tank.mytrimetpro.data.trimet.arrival;

import com.example.tank.mytrimetpro.data.gson.serializer.TrimetDateAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

/**
 * Created by Jmiller on 8/16/2016.
 */

public class ArrivalData {

    @SerializedName("feet")
    private Integer mFeet;

    @SerializedName("inCongestion")
    private Boolean mInCongestion;

    @SerializedName("scheduled")
    @JsonAdapter(TrimetDateAdapter.class)
    private Calendar mScheduledArrivalTime;

    @SerializedName("loadPercentage")
    private int mLoadPercentage;

    @SerializedName("estimated")
    @JsonAdapter(TrimetDateAdapter.class)
    private Calendar mEstimatedArrivalTime;

    @SerializedName("locid")
    private int mLocationId;

    @SerializedName("vehicleID")
    private int mVehicleId;

    public Integer getFeet() {
        return mFeet;
    }
    public void setFeet(Integer mFeet) {
        this.mFeet = mFeet;
    }
    public Calendar getEstimatedArrivalTime() {
        return mEstimatedArrivalTime;
    }
    public void setEstimatedArrivalTime(Calendar mEstimatedArrivalTime) {
        this.mEstimatedArrivalTime = mEstimatedArrivalTime;
    }
    public int getVehicleId() {
        return mVehicleId;
    }
    public void setVehicleId(int mVehicleId) {
        this.mVehicleId = mVehicleId;
    }
    public int getLoadPercentage() {
        return mLoadPercentage;
    }
    public void setLoadPercentage(int mLoadPercentage) {
        this.mLoadPercentage = mLoadPercentage;
    }
    public Boolean getInCongestion() {
        return mInCongestion;
    }
    public void setInCongestion(Boolean mInCongestion) {
        this.mInCongestion = mInCongestion;
    }
    public int getLocationId() {
        return mLocationId;
    }
    public void setLocationId(int mLocationId) {
        this.mLocationId = mLocationId;
    }
    public Calendar getScheduledArrivalTime() {
        return mScheduledArrivalTime;
    }
    public void setScheduledArrivalTime(Calendar mScheduledArrivalTime) {
        this.mScheduledArrivalTime = mScheduledArrivalTime;
    }
}
