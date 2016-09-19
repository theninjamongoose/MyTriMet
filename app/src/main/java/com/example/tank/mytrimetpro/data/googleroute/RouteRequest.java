package com.example.tank.mytrimetpro.data.googleroute;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmiller on 8/1/2016.
 */

public class RouteRequest {

    @SerializedName("routes")
    private List<Route> mRoutes = new ArrayList<>();

    public List<Route> getRoutes() {
        return mRoutes;
    }
    public Leg getLeg(){
        return mRoutes.get(0).getLegs().get(0);
    }
    public void setRoutes(List<Route> routes) {
        this.mRoutes = routes;
    }
}
