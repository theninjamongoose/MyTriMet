package com.example.tank.mytrimetpro.data.googleroute;

import com.example.tank.mytrimetpro.data.gson.serializer.PolylinePointsAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmiller on 8/1/2016.
 */
public class Polyline {
    @JsonAdapter(PolylinePointsAdapter.class)
    @SerializedName("points")
    private List<LatLng> mPoints = new ArrayList<>();

    public List<LatLng> getPoints() {
        return mPoints;
    }
    public void setPoints(List<LatLng> points) {
        this.mPoints = points;
    }
}
