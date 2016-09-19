package com.example.tank.mytrimetpro.data.trimet.vehicle;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jmiller on 8/16/2016.
 */

public class VehicleResult {

    @SerializedName("vehicle")
    private List<VehicleData> mVehicleDataList = new ArrayList<>();

    public List<VehicleData> getVehicleDataList() {
        return mVehicleDataList;
    }
    public void setVehicleDataList(List<VehicleData> mVehicleDataList) {
        this.mVehicleDataList = mVehicleDataList;
    }
}
