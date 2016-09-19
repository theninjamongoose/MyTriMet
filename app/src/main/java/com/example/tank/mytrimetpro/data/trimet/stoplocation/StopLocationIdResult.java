package com.example.tank.mytrimetpro.data.trimet.stoplocation;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jmiller on 8/17/2016.
 */

public class StopLocationIdResult {

    @SerializedName("location")
    List<StopLocationId> mLocationDetails = new ArrayList<>();

    public List<StopLocationId> getLocationDetails() {
        return mLocationDetails;
    }
    public void setLocationDetails(List<StopLocationId> locationDetails) {
        this.mLocationDetails = locationDetails;
    }
}
