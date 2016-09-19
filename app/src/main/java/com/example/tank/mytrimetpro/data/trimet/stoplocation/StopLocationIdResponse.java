package com.example.tank.mytrimetpro.data.trimet.stoplocation;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jmiller on 8/17/2016.
 */

public class StopLocationIdResponse {

    @SerializedName("resultSet")
    StopLocationIdResult mStopLocationIdDetails;

    public StopLocationIdResult getStopLocationIdDetails() {
        return mStopLocationIdDetails;
    }
    public void setStopLocationIdDetails(StopLocationIdResult mStopLocationIdDetails) {
        this.mStopLocationIdDetails = mStopLocationIdDetails;
    }

    /* Shortcut method */
    public int getLocationId(){
        return mStopLocationIdDetails.getLocationDetails().get(0).getLocationId();
    }
}
