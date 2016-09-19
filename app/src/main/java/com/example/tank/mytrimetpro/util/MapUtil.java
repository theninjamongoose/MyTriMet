package com.example.tank.mytrimetpro.util;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.example.tank.mytrimetpro.R;
import com.example.tank.mytrimetpro.data.Destination;
import com.example.tank.mytrimetpro.data.NetworkHandler;
import com.example.tank.mytrimetpro.data.googleroute.Step;
import com.example.tank.mytrimetpro.data.trimet.vehicle.VehicleData;
import com.example.tank.mytrimetpro.util.Draw.DottedLine;
import com.example.tank.mytrimetpro.util.Draw.DrawLine;
import com.example.tank.mytrimetpro.util.Draw.SolidLine;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

/**
 * Created by jmiller on 7/21/2016.
 */
@Singleton
public class MapUtil {

    private static final float DEFAULT_NO_DESTINATION_ZOOM_LVL = 15.5f;
    private static final int MAP_PADDING = 130;
    private final int mBoundsPadding;
    private final Map<String, DrawLine> mDrawLineMap;
    private static final Map<String, Integer> LINE_COLORS;
    private static final Map<String, Integer> LINE_IMAGE;

    private static final String MAX_BLUE_LINE = "MAX Blue Line";
    private static final String MAX_GREEN_LINE = "MAX Green Line";
    private static final String MAX_ORAGE_LINE = "MAX Orange Line";
    private static final String MAX_RED_LINE = "MAX Red Line";
    private static final String MAX_YELLOW_LINE = "MAX Yellow Line";
    private static final String BUS_LINE = "bus";
    private static final String HEAVY_RAIL = "HEAVEY_RAIL";
    private static final String TRAM = "TRAM";

    static {
        Map<String, Integer> lineColors = new HashMap<>();
        lineColors.put(MAX_BLUE_LINE, R.color.blue);
        lineColors.put(MAX_GREEN_LINE, R.color.green);
        lineColors.put(MAX_ORAGE_LINE, R.color.orange);
        lineColors.put(MAX_RED_LINE, R.color.red);
        lineColors.put(MAX_YELLOW_LINE, R.color.yellow);
        lineColors.put(BUS_LINE, R.color.bus_color);
        LINE_COLORS = Collections.unmodifiableMap(lineColors);
    }

    static {
        Map<String, Integer> lineType = new HashMap<>();
        lineType.put(BUS_LINE.toUpperCase(), R.drawable.ic_bus_black_24dp);
        lineType.put(TRAM, R.drawable.ic_tram_black_24dp);
        lineType.put(HEAVY_RAIL, R.drawable.ic_train_black_24dp);
        LINE_IMAGE = Collections.unmodifiableMap(lineType);
    }

    private final Context mContext;

    public MapUtil(Context context, UiUtil uiUtil, DottedLine dottedLine, SolidLine solidLine) {
        mContext = context;
        mBoundsPadding = (int) uiUtil.dpToPx(MAP_PADDING);
        Map<String, DrawLine> drawLineMap = new HashMap<>();
        drawLineMap.put(NetworkHandler.WALKING, dottedLine);
        drawLineMap.put(NetworkHandler.TRANSIT, solidLine);
        this.mDrawLineMap = Collections.unmodifiableMap(drawLineMap);
    }

    private LatLngBounds boundsBuilder(List<LatLng> points) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng point : points) {
            builder.include(point);
        }
        return builder.build();
    }

    public List<List<PolylineOptions>> getRouteLines(List<Step> steps) {
        List<List<PolylineOptions>> routeLines = new ArrayList<>();
        for (Step step : steps) {
            int color = step.getTransitDetails().getLineColor(mContext);
            routeLines.add(mDrawLineMap.get(step.getTravelMode())
                    .createLine(step.getPolyline().getPoints(), color));
        }
        return routeLines;
    }

    public List<List<CircleOptions>> getRoutePoints(List<Step> steps){
        List<List<CircleOptions>> routePoints = new ArrayList<>();
        for (Step step : steps) {
            int color = step.getTransitDetails().getLineColor(mContext);
            routePoints.add(mDrawLineMap.get(step.getTravelMode())
                    .createPoints(step.getPolyline().getPoints(), color));
        }
        return routePoints;
    }

    public CameraUpdate getZoomLvl(List<LatLng> points) {
        return CameraUpdateFactory
                .newLatLngBounds(boundsBuilder(points), mBoundsPadding);
    }

    public MarkerOptions buildMarkerOptions(LatLng latLng, VehicleData vehicleData, String vehicleType) {
        return buildMarkerOptions(latLng, vehicleData.getTitle(mContext),
                vehicleData.getStatusMessages(mContext), BitmapDescriptorFactory.fromResource(LINE_IMAGE.get(vehicleType)));
    }


    public MarkerOptions buildMarkerOptions(LatLng latLng, String title, String statusMessages, BitmapDescriptor icon) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(icon);
        if (!TextUtils.isEmpty(title)) {
            markerOptions.title(title);
        }
        if (!TextUtils.isEmpty(statusMessages)) {
            markerOptions.snippet(statusMessages);
        }
        return markerOptions;
    }

    public MarkerOptions buildMarkerOptions(LatLng mCurrentLocation, int current_location, BitmapDescriptor bitmapDescriptor) {
        return buildMarkerOptions(mCurrentLocation, mContext.getResources().getString(R.string.current_location), null, bitmapDescriptor);
    }
}