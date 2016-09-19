package com.example.tank.mytrimetpro.map;

import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;

import com.example.tank.mytrimetpro.R;
import com.example.tank.mytrimetpro.base.DrawerActivity;
import com.example.tank.mytrimetpro.data.Destination;
import com.example.tank.mytrimetpro.data.DestinationWrapper;
import com.example.tank.mytrimetpro.data.MapPresenterCallback;
import com.example.tank.mytrimetpro.data.NetworkHandler;
import com.example.tank.mytrimetpro.data.googleroute.RouteRequest;
import com.example.tank.mytrimetpro.data.googleroute.Step;
import com.example.tank.mytrimetpro.data.trimet.vehicle.VehicleData;
import com.example.tank.mytrimetpro.util.Calculation;
import com.example.tank.mytrimetpro.util.CalendarFactory;
import com.example.tank.mytrimetpro.util.MapUtil;
import com.example.tank.mytrimetpro.util.UserPhoneStatus;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by tank on 8/30/16.
 */

public class MapPresenter implements MapContract.Presenter, MapPresenterCallback {

    private static final int NOTIFY_BTN_GONE = 100;
    private static final int ROUTE_BTN_ACTIVE = 101;
    private static final int ROUTE_BTN_CANCEL = 102;
    private static final float VIEW_MIDDLE = 0.5f;
    private final Calculation mCalculation;
    private NetworkHandler mNetworkHandler;
    private MapUtil mMapUtil;
    private UserPhoneStatus mUserPhoneStatus;
    private LatLng mCurrentLocation;
    private static final int TRIMET_CALL_MILLI_SEC = 10000;

    private Destination mSelectedDestination;
    @NonNull
    private final MapContract.View mMapView;
    private Step mVehicleStep;
    private Handler mTrimetVehicleHandler;
    private Runnable mTrimetVehicleRunnable;
    private VehicleData mVehicleData;

    @Inject
    public MapPresenter(DestinationWrapper destinationWrapper, MapContract.View view, NetworkHandler networkHandler,
                        MapUtil mapUtil, UserPhoneStatus userPhoneStatus, Calculation calculation){
        this.mSelectedDestination = destinationWrapper.getDestination();
        mCalculation = calculation;
        mMapView = view;
        mMapUtil = mapUtil;
        mNetworkHandler = networkHandler;
        mUserPhoneStatus = userPhoneStatus;
    }

    @Override
    public void onActivateRouteBtnClick(View view) {
        //On no route, display toast, return
        if (!mSelectedDestination.hasRoute()) {
            mMapView.displayInvalidDestinationMessage();
            return;
        }
        //todo implement notifications
        //On notifications set to false, display toast, return
//        if (!UserSettingsUtil.INSTANCE.getFirstStopNotificationState(MapActivity.this)) {
//
//            Toast.makeText(MapActivity.this, R.string.notifications_are_off, Toast.LENGTH_LONG).show();
//            return;
//        }
        switch ((int) view.getTag()) {
            case ROUTE_BTN_ACTIVE:
                mMapView.changeActiveRouteBtn(view, R.drawable.ic_close_box_black_48dp, R.color.darkRed, ROUTE_BTN_CANCEL);
                updateActiveDestination(true);
                //todo handle notifications
//                setNotification();
                mMapView.displayRouteActiveToast();
                break;
            case ROUTE_BTN_CANCEL:
                mMapView.changeActiveRouteBtn(view, R.drawable.ic_directions_black_48dp, R.color.darkGreen, ROUTE_BTN_ACTIVE);
                updateActiveDestination(false);
                mMapView.displayRouteCancelledToast();
                break;
            default:
                break;
        }
    }

    @Override
    public void onMapLoaded() {
        updateDestination();
    }

    @Override
    public void onGoogleApiConnection() {
        if(mUserPhoneStatus.haveUserPermission()){
            mMapView.requestLocationUpdates(mUserPhoneStatus.buildLocationRequest());
        }
    }

    private boolean isInBounds(LatLng point) {
        return DrawerActivity.PLACE_BOUNDS_LIMIT.contains(point);
    }

    public void handlePermissions() {
        if (mUserPhoneStatus.haveUserPermission()) {
            mMapView.initFieldsRequiringPermissions();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mMapView.requestUserPermissions();
            } else {
                mMapView.displayPermissionDeniedToast();
            }
        }
    }


    private void updateActiveDestination(boolean activeState) {
        Destination.stopCurrentlyActiveRoute();
        mSelectedDestination.setActive(activeState);
        Destination.createOrUpdateDestination(mSelectedDestination);
    }

    @Override
    public void start() {
        handlePermissions();
        if(mUserPhoneStatus.haveUserPermission()){

        }
    }

    @Override
    public void updateSelectedDestination(Place place) {
        updateSelectedDestination(new Destination(place));
    }

    public void updateDestination(){
        //return if no destination
        if(mSelectedDestination == null){
            return;
        }
        setActivateRouteBtnDisplay();
        //return if out of bounds
        if (!isInBounds(mSelectedDestination.destinationLatLng())) {
            unassignHandler(mTrimetVehicleHandler, mTrimetVehicleRunnable);
            mMapView.displayDestinationOutOfBounds();
            return;
        }

        mMapView.changeDisplayedDestination(mSelectedDestination);
        if(mSelectedDestination.isActive()){
            updateRoute(mSelectedDestination.getRoute());
        } else if (mCurrentLocation == null) {
            mMapView.displayNoCurrentLocationToast();
        } else {
            mNetworkHandler.directionPointsGson(mCurrentLocation, mSelectedDestination.destinationLatLng(), this);
        }
    }

    private void setActivateRouteBtnDisplay() {
        if (mSelectedDestination == null || mSelectedDestination.getPlaceName() == null
                || mSelectedDestination.getPlaceName().length() == 0) {
            mMapView.setActiveRouteBtnDisplay(View.GONE, NOTIFY_BTN_GONE, null, null);
        } else {
            if (mSelectedDestination.isActive()) {
                mMapView.setActiveRouteBtnDisplay(View.VISIBLE, ROUTE_BTN_CANCEL, R.drawable.ic_close_box_black_48dp, R.color.darkRed);

            } else {
                mMapView.setActiveRouteBtnDisplay(View.VISIBLE, ROUTE_BTN_ACTIVE, R.drawable.ic_directions_black_48dp, R.color.darkGreen);
            }
        }
    }

    @Override
    public void updateSelectedDestination(Destination destination) {
        Destination.createOrUpdateDestination(destination);
        mSelectedDestination = destination;
    }

    @Inject
    void setupListeners() {
        mMapView.setPresenter(this);
    }

    @Override
    public void updateRoute(RouteRequest response) {
        if(response.getRoutes().size() == 0){
            mMapView.displayNoRouteFoundToast();
        }
        mSelectedDestination.setRoute(response);
        Destination.createOrUpdateDestination(mSelectedDestination);

        //draw route
        List<Step> routeSteps = response.getLeg().getSteps();
        drawRoute(routeSteps);
        mMapView.animateMap(mMapUtil.getZoomLvl(routeSteps.get(0).getPolyline().getPoints()));

        //draw vehicle
        mVehicleStep = getNextStepWithVehicle(routeSteps, CalendarFactory.getUTCCalendar());
        if(mVehicleStep.getVehicleId() == 0 && mVehicleStep.getTrimetLocationId() == 0) {
            mNetworkHandler.getStopLocationId(mVehicleStep, mCalculation.getMinArivalTimeMilliSec(response.getLeg()), this);
        } else {
            updateVehiclePosition();
        }
    }

    public Step getNextStepWithVehicle(List<Step> steps, Calendar earliestTime) {
        for (Step step : steps) {
            if (step.getTravelMode().equals(NetworkHandler.TRANSIT)
                    && step.getTransitDetails().getDepartureTime().getValue().after(earliestTime)) {
                return step;
            }
        }
        return null;
    }

    private void drawRoute(List<Step> routeSteps) {
        createRouteLines(routeSteps);
        createRoutePoints(routeSteps);
    }

    private void createRoutePoints(List<Step> routeSteps) {
        List<List<CircleOptions>> circleOptionsListList = mMapUtil.getRoutePoints(routeSteps);
        if(circleOptionsListList != null) {
            for (List<CircleOptions> circleOptionsList : circleOptionsListList) {
                if(circleOptionsList != null) {
                    for (CircleOptions circleOptions : circleOptionsList) {
                        mMapView.addCircle(circleOptions);
                    }
                }
            }
        }
    }

    private void createRouteLines(List<Step> routeSteps) {
        List<List<PolylineOptions>> polylineOptionsListList = mMapUtil.getRouteLines(routeSteps);
        if(polylineOptionsListList != null) {
            for (List<PolylineOptions> polylineOptionsList : polylineOptionsListList) {
                if(polylineOptionsList != null) {
                    for (PolylineOptions polylineOptions : polylineOptionsList) {
                        mMapView.addLine(polylineOptions);
                    }
                }
            }
        }
    }

    @Override
    public void queryForNextIncomingtransitVehicle(int id) {
        updateVehiclePosition();
    }

    private void updateVehiclePosition() {
        initTrimetVeicleHandler();
        mTrimetVehicleRunnable = new Runnable() {
            @Override
            public void run() {
                queryForTrimetVehiclePostition();
                mTrimetVehicleHandler.postDelayed(mTrimetVehicleRunnable, TRIMET_CALL_MILLI_SEC);
            }
        };
        mTrimetVehicleHandler.post(mTrimetVehicleRunnable);
    }

    private void queryForTrimetVehiclePostition() {
        if(mVehicleStep.getTrimetLocationId() > 0 && mVehicleStep.getVehicleId() > 0) {
            mNetworkHandler.getUpdatedVehicleDetails(mVehicleStep.getTrimetLocationId(), mVehicleStep.getVehicleId(), this);
        }
    }

    private Handler initTrimetVeicleHandler() {
        if(mTrimetVehicleHandler == null){
            mTrimetVehicleHandler = new Handler();
        } else {
            unassignHandler(mTrimetVehicleHandler, mTrimetVehicleRunnable);
        }
        return mTrimetVehicleHandler;
    }

    private void unassignHandler(Handler handler, Runnable thread) {
        if(handler != null && thread != null) {
            handler.removeCallbacks(thread);
        }
    }

    @Override
    public void noVehicleAssigned() {

    }

    @Override
    public void trimetLocationUpdate(VehicleData vehicleData) {
        mVehicleData = vehicleData;
        LatLng locationVehicle = new LatLng(vehicleData.getLatitude(), vehicleData.getLongitude());

        //if we don't remove markers then it must be the first load.  Set the zoom.
        if(!mMapView.clearVehicleMarkers()){
            List<LatLng> focusPoints = new ArrayList<LatLng>();
            focusPoints.add(mCurrentLocation);
            focusPoints.add(locationVehicle);
            focusPoints.add(mVehicleStep.getStartLocation());
            mMapView.animateMap(mMapUtil.getZoomLvl(focusPoints));
        }

        mMapView.updateVehicleMarker(
                mMapUtil.buildMarkerOptions(
                        locationVehicle,
                        vehicleData,
                        mVehicleStep.getTransitDetails().getLine().getVehicle().getType()
                )
        );

        float vehicleBearing = vehicleData.getBearing();

        //pin info box to the top of the trimet marker
        float infoWindowRotateOffsetX = (float)(Math.sin(-vehicleBearing * Math.PI / 180) * 0.5 + 0.5);
        float infoWindowRotateOffsetY = (float)-(Math.cos(-vehicleBearing * Math.PI / 180) * 0.5 - 0.5);
        mMapView.rotateVechicleMarker(VIEW_MIDDLE, VIEW_MIDDLE,
                vehicleBearing, infoWindowRotateOffsetX, infoWindowRotateOffsetY);
    }

    public void updateLocation(Location location) {
        mCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());
//        mCurrentLocation = new LatLng(45.523058, -122.671795);
        mMapView.updateCurrentLocationMarker(
                mMapUtil.buildMarkerOptions(
                        mCurrentLocation, R.string.current_location,
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
    }
}
