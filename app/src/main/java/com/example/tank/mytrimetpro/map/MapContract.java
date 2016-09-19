package com.example.tank.mytrimetpro.map;

import android.view.View;

import com.example.tank.mytrimetpro.base.BasePresenter;
import com.example.tank.mytrimetpro.base.BaseView;
import com.example.tank.mytrimetpro.data.Destination;
import com.example.tank.mytrimetpro.destinations.DestinationContract;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by tank on 8/30/16.
 */

public interface MapContract {

    interface View extends BaseView<Presenter>{

        void displayInvalidDestinationMessage();

        void changeActiveRouteBtn(android.view.View view, int drawableId, int colorId, int tag);

        void displayRouteActiveToast();

        void displayRouteCancelledToast();

        void initFieldsRequiringPermissions();

        void requestUserPermissions();

        void displayPermissionDeniedToast();

        void displayDestinationOutOfBounds();

        void changeDisplayedDestination(Destination mSelectedDestination);

        void displayNoCurrentLocationToast();

        void displayNoRouteFoundToast();

        void addCircle(CircleOptions circleOptions);

        void addLine(PolylineOptions polylineOptions);

        void animateMap(CameraUpdate cameraUpdate);

        boolean clearVehicleMarkers();

        void updateVehicleMarker(MarkerOptions markerOptions);

        void rotateVechicleMarker(float x, float y, float bearing, float infoWindowOffSetX, float infoWindowOffSetY);

        void updateCurrentLocationMarker(MarkerOptions markerOptions);

        void requestLocationUpdates(LocationRequest locationRequest);

        void setActiveRouteBtnDisplay(int visiblityState, int buttonTag, Integer buttonImage, Integer buttonColor);
    }

    interface Presenter extends BasePresenter{
        void onActivateRouteBtnClick(android.view.View view);

        void onMapLoaded();

        void onGoogleApiConnection();
    }
}
