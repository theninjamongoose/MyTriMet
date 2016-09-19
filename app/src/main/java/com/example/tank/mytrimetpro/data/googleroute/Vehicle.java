package com.example.tank.mytrimetpro.data.googleroute;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jmiller on 8/2/2016.
 */

public class Vehicle {
    @SerializedName("name")
    private String mName;
    @SerializedName("type")
    private String mType;

    public String getName() {
        return mName;
    }
    public void setName(String name) {
        this.mName = name;
    }
    public String getType() {
        return mType;
    }
    public void setType(String type) {
        this.mType = type;
    }
}
