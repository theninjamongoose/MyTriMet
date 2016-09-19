package com.example.tank.mytrimetpro.data.googleroute;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jmiller on 8/1/2016.
 */

public class Line {
    
    @SerializedName("name")
    private String mName;

    @SerializedName("vehicle")
    private Vehicle mVehicle = new Vehicle();


    public Vehicle getVehicle() {
        return mVehicle;
    }
    public void setVehicle(Vehicle vehicle) {
        this.mVehicle = vehicle;
    }

    public String getName() {
        return mName;
    }
    public void setName(String name) {
        this.mName = name;
    }
}
