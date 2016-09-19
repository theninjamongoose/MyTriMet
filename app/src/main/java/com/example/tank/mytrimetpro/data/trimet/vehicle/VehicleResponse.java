package com.example.tank.mytrimetpro.data.trimet.vehicle;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jmiller on 8/16/2016.
 */

public class VehicleResponse {

    @SerializedName("resultSet")
    private VehicleResult mVehicleResult;

    public VehicleResult getVehicleResult() {
        return mVehicleResult;
    }
    public void setVehicleResult(VehicleResult mVehicleResult) {
        this.mVehicleResult = mVehicleResult;
    }
}
