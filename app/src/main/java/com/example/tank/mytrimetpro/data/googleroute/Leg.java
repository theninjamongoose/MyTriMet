package com.example.tank.mytrimetpro.data.googleroute;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmiller on 8/1/2016.
 */

public class Leg {
    @SerializedName("departure_time")
    public Time mDepartureTime;

    @SerializedName("steps")
    private List<Step> mSteps = new ArrayList<>();

    @SerializedName("arrival_time")
    private Time mDestinationArrivalTime;

    public Time getDepartureTime() {
        return mDepartureTime;
    }
    public void setDepartureTime(Time departureTime) {
        this.mDepartureTime = departureTime;
    }
    public Time getDestinationArrivalTime() {
        return mDestinationArrivalTime;
    }
    public void setDestinationArrivalTime(Time destinationArrivalTime) {
        this.mDestinationArrivalTime = destinationArrivalTime;
    }
    public List<Step> getSteps() {
        return mSteps;
    }
    public void setSteps(List<Step> steps) {
        this.mSteps = steps;
    }

}
