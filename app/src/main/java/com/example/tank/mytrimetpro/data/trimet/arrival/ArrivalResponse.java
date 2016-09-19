package com.example.tank.mytrimetpro.data.trimet.arrival;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jmiller on 8/16/2016.
 */

public class ArrivalResponse {

    @SerializedName("resultSet")
    private ArrivalQueryResult mArrivalQueryResult;

    public ArrivalQueryResult getArrivalQueryResult() {
        return mArrivalQueryResult;
    }
    public void setArrivalQueryResult(ArrivalQueryResult mArrivalQueryResult) {
        this.mArrivalQueryResult = mArrivalQueryResult;
    }
}
