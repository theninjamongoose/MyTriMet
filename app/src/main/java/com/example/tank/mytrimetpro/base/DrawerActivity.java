package com.example.tank.mytrimetpro.base;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;

import com.example.tank.mytrimetpro.R;
import com.example.tank.mytrimetpro.destinations.DestinationActivity;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tank on 8/29/16.
 */

public class DrawerActivity extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener, DrawerCallback {

    private static final  Map<Integer, Class> DRAWER_OPTION_MAP;
    static  {
        Map<Integer, Class > drawOptionMap = new HashMap<>();
        drawOptionMap.put( R.id.nav_home, DestinationActivity.class);
        //todo add other activities
        DRAWER_OPTION_MAP = Collections.unmodifiableMap(drawOptionMap);
    }


    public static final LatLngBounds PLACE_BOUNDS_LIMIT =
            new LatLngBounds(new LatLng(45.23396470, -123.153528597), new LatLng(45.70263875, -122.367487422));
    protected PlaceAutocompleteFragment mAutoCompleteFragment;
    private DrawerLayout mDrawerLayout;

    public void setUpAutocomplete(PlaceSelectionListener placeSelectionListener) {
        mAutoCompleteFragment = new CustomPlaceAutocompleteFragment();
        mAutoCompleteFragment.setBoundsBias(PLACE_BOUNDS_LIMIT);
        mAutoCompleteFragment.setOnPlaceSelectedListener(placeSelectionListener);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.autocomplete, mAutoCompleteFragment).commit();
    }

    protected void drawerSetUp(int drawerLayoutId) {
        NavigationView navigationView;
        mDrawerLayout = (DrawerLayout) findViewById(drawerLayoutId);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        mDrawerLayout.closeDrawer(GravityCompat.START);
        Class clazzName = DRAWER_OPTION_MAP.get(item.getItemId());
        Intent intent = new Intent(this, clazzName);
        startActivity(intent);
        return true;
    }

    @Override
    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }
}
