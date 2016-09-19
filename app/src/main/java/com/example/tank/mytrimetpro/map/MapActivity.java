package com.example.tank.mytrimetpro.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.tank.mytrimetpro.MyTrimetApplication;
import com.example.tank.mytrimetpro.R;
import com.example.tank.mytrimetpro.base.AutocompleteListener;
import com.example.tank.mytrimetpro.base.DrawerActivity;
import com.example.tank.mytrimetpro.data.Destination;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by tank on 8/30/16.
 */

public class MapActivity extends DrawerActivity implements MapContract.View,
        AnimationUpdateImageViewCallback, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    @Inject MapPresenter mPresenter;
    private ImageButton mActivateRouteBtn;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;

    private static final int USER_PERMISSIONS_REQUEST = 1001;
    public static final int ZERO_BOUNDS_PADDING = 0;
    private Marker mVehicleMarker;
    private Marker mCurrentLocationMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        init();

        // init presenter
        DaggerMapComponent.builder()
                .appComponent(((MyTrimetApplication) getApplication()).getAppComponent())
                .mapPresenterModule(new MapPresenterModule(this))
                .build().inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onPause() {
        if(mGoogleApiClient != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    private void init() {
        initActivateRouteBtn();
        drawerSetUp(R.id.activity_map);

    }

    private void initActivateRouteBtn() {
        mActivateRouteBtn = (ImageButton) findViewById(R.id.notification_status_image_btn);
        mActivateRouteBtn.setVisibility(View.GONE);
        mActivateRouteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onActivateRouteBtnClick(view);
            }
        });
    }


    @Override
    public void setPresenter(MapContract.Presenter presenter) {
        mPresenter = (MapPresenter) presenter;
        setUpAutocomplete(new AutocompleteListener(mPresenter));
    }

    @Override
    public void displayInvalidDestinationMessage() {
        Toast.makeText(this, getResources()
                .getString(R.string.invalid_destination), Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void changeActiveRouteBtn(View view, int drawableId, int colorId, int tag) {
        Animation imageClickAnimation = AnimationUtils.loadAnimation(this, R.anim.image_click_shrink);
        imageClickAnimation.setAnimationListener(
                new ImageClickedAnimation(this, this, view, drawableId, colorId, tag));
        view.startAnimation(imageClickAnimation);
    }

    @Override
    public void displayRouteActiveToast() {
        Toast.makeText(MapActivity.this, R.string.route_active, Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayRouteCancelledToast() {
        Toast.makeText(MapActivity.this, R.string.route_cancelled, Toast.LENGTH_LONG).show();
    }

    @Override
    public void initFieldsRequiringPermissions() {
        initMap();
        initGoogleLocationApi();
    }

    @Override
    public void requestUserPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                USER_PERMISSIONS_REQUEST);
    }

    @Override
    public void displayPermissionDeniedToast() {
        Toast.makeText(this, getResources().getString(R.string.permission_denied_toast),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayDestinationOutOfBounds() {
        clearMap();
        Toast.makeText(this, getResources().getString(R.string.out_of_trimet_bounds),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void changeDisplayedDestination(Destination destination) {
        clearMap();
        updateAutocompleteDestinationText(destination);
        markDestination(destination);
    }

    @Override
    public void displayNoCurrentLocationToast() {
        Toast.makeText(this, getResources().getString(R.string.no_current_location_for_route),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayNoRouteFoundToast() {
        Toast.makeText(this, getResources().getString(R.string.empty_route_response),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void addCircle(CircleOptions circleOptions) {
        mMap.addCircle(circleOptions);
    }

    @Override
    public void addLine(PolylineOptions polylineOptions) {
        mMap.addPolyline(polylineOptions);
    }

    @Override
    public void animateMap(CameraUpdate cameraUpdate) {
        mMap.animateCamera(cameraUpdate);
    }

    @Override
    public boolean clearVehicleMarkers() {
        if(mVehicleMarker != null){
            mVehicleMarker.remove();
            return true;
        }
        return false;
    }

    @Override
    public void updateVehicleMarker(MarkerOptions markerOptions) {
        mVehicleMarker = mMap.addMarker(markerOptions);
        mVehicleMarker.showInfoWindow();
    }

    @Override
    public void rotateVechicleMarker(float x, float y, float bearing, float infoWindowOffSetX, float infoWindowOffSetY) {
        mVehicleMarker.setAnchor(x, y);
        mVehicleMarker.setRotation(bearing);
        mVehicleMarker.setInfoWindowAnchor(infoWindowOffSetX, infoWindowOffSetY);
    }

    @Override
    public void updateCurrentLocationMarker(MarkerOptions markerOptions) {
        if (mCurrentLocationMarker != null) {
            mCurrentLocationMarker.remove();
        }
        mCurrentLocationMarker = mMap.addMarker(markerOptions);
    }

    @Override
    @SuppressWarnings("MissingPermission")
    public void requestLocationUpdates(LocationRequest locationRequest) {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    }

    @Override
    public void setActiveRouteBtnDisplay(int visiblityState, int buttonTag, Integer buttonImage, Integer buttonColor) {
        mActivateRouteBtn.setVisibility(visiblityState);
        mActivateRouteBtn.setTag(buttonTag);
        if(buttonImage != null) {
            mActivateRouteBtn.setBackgroundResource(buttonImage);
        }
        if(buttonColor != null) {
            mActivateRouteBtn.getBackground().setColorFilter(ContextCompat.getColor(MapActivity.this, buttonColor),
                    PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void markDestination(Destination destination) {
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(destination.getLatitude(), destination.getLongitude()))
                .title(destination.getPlaceName()));
    }

    private void updateAutocompleteDestinationText(Destination destination) {
        mAutoCompleteFragment.setText(destination.getPlaceName());
    }

    private void clearMap() {
        mMap.clear();
        mVehicleMarker = null;
    }

    private void initGoogleLocationApi() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_Fragment);
        mapFragment.getMapAsync(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateImageView(int drawableId, int colorId, int tag) {
        mActivateRouteBtn.setBackgroundResource(drawableId);
        mActivateRouteBtn.getBackground().setColorFilter(ContextCompat.getColor(MapActivity.this, colorId),
                PorterDuff.Mode.SRC_ATOP);
        mActivateRouteBtn.setTag(tag);
    }

    @Override
    @SuppressWarnings("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mMap.moveCamera(CameraUpdateFactory
                .newLatLngBounds(DrawerActivity.PLACE_BOUNDS_LIMIT, displayMetrics.widthPixels,
                        displayMetrics.heightPixels, ZERO_BOUNDS_PADDING));
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mPresenter.onMapLoaded();
            }
        });
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPresenter.onGoogleApiConnection();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, getResources().getString(R.string.google_api_failed_connection),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == USER_PERMISSIONS_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, getResources().getString(R.string.permission_denied_toast),
                        Toast.LENGTH_LONG).show();
            } else if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initFieldsRequiringPermissions();
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mPresenter.updateLocation(location);
    }
}
