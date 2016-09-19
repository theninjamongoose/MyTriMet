package com.example.tank.mytrimetpro.data.googleroute;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmiller on 8/1/2016.
 */

public class Route {

    @SerializedName("legs")
    public List<Leg> mLegs = new ArrayList<>();

    public List<Leg> getLegs() {
        return mLegs;
    }

    public void setLegs(List<Leg> legs) {
        this.mLegs = legs;
    }
}
