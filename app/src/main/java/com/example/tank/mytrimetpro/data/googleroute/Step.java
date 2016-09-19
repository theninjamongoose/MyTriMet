package com.example.tank.mytrimetpro.data.googleroute;

import com.example.tank.mytrimetpro.data.gson.serializer.LatLongAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jmiller on 8/1/2016.
 */
public class Step {
    @SerializedName("travel_mode")
    private String mTravelMode;

    @SerializedName("polyline")
    private Polyline mPolyline;

    @SerializedName("transit_details")
    /* Initialized here to avoid a null pointer exception
     * in the drawRoute method in the MapUtil class */
    private TransitDetails mTransitDetails = new TransitDetails();

    @JsonAdapter(LatLongAdapter.class)
    @SerializedName("start_location")
    private LatLng mStartLocation;

    @SerializedName("duration")
    private Duration mDuration;

    @SerializedName("vehicleId")
    private int mVehicleId;

    private int mTrimetLocationId;

    public int getVehicleId() {
        return mVehicleId;
    }
    public void setVehicleId(int vehicleId) {
        mVehicleId = vehicleId;
    }

    public String getTravelMode() {
        return mTravelMode;
    }
    public LatLng getStartLocation() {
        return mStartLocation;
    }
    public void setStartLocation(LatLng mStartLocation) {
        this.mStartLocation = mStartLocation;
    }
    public void setTravelMode(String travelMode) {
        this.mTravelMode = travelMode;
    }
    public Polyline getPolyline() {
        return mPolyline;
    }
    public void setPolyline(Polyline polyline) {
        this.mPolyline = polyline;
    }
    public TransitDetails getTransitDetails() {
        return mTransitDetails;
    }
    public void setTransitDetails(TransitDetails transitDetails) {
        this.mTransitDetails = transitDetails;
    }
    public Duration getDuration() {return mDuration;}
    public void setDuration(Duration duration) {mDuration = duration;}

    public void setTrimetLocationId(int id) {
        this.mTrimetLocationId = id;
    }
    public int getTrimetLocationId() {return mTrimetLocationId;}
}