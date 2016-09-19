package com.example.tank.mytrimetpro.data.trimet.arrival;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jmiller on 8/16/2016.
 */

public class ArrivalQueryResult {
    @SerializedName("arrival")
    private List<ArrivalData> mArrivals = new ArrayList<>();

    public List<ArrivalData> getArrivals() {
        return mArrivals;
    }
    public void setArrivals(List<ArrivalData> mArrivals) {
        this.mArrivals = mArrivals;
    }
}
