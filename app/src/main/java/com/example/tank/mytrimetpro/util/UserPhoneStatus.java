package com.example.tank.mytrimetpro.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.LocationRequest;

import javax.inject.Singleton;

/**
 * Created by tank on 8/30/16.
 */
@Singleton
public class UserPhoneStatus {

    private static final int LOCATION_CHECK_INTERVAL = 5000; //5 seconds
    private static final int LOCATION_CHECK_FASTEST_INTERVAL = 3000; //3 seconds
    private final Context mContext;

    public UserPhoneStatus(Context context){
        mContext = context;
    }

    public boolean isNetworked() {
        NetworkInfo networkinfo = getNetworkInfo();
        return isNetworkAvailable(networkinfo);
    }

    private boolean isNetworkAvailable(NetworkInfo networkinfo) {
        boolean isAvailable = false;
        if (networkinfo != null && networkinfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private NetworkInfo getNetworkInfo() {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo();
    }

    public boolean haveUserPermission() {
        return ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public LocationRequest buildLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(LOCATION_CHECK_INTERVAL);
        locationRequest.setFastestInterval(LOCATION_CHECK_FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        return locationRequest;
    }
}
