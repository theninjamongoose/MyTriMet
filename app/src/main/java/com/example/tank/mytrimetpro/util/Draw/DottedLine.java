package com.example.tank.mytrimetpro.util.Draw;

import android.content.Context;

import com.example.tank.mytrimetpro.util.UiUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmiller on 8/4/2016.
 */
public class DottedLine implements DrawLine {
    private final float mDottedLineWidth;

    /* Adjusting this variable adjusts the size of the line.
     * So far, its trial and error to find the ideal length. */
    protected static final float SEGMENT_LENGTH_MAXIMUM = 0.008f;
    protected static final float SEGMENT_LENGTH_MINIMUM = SEGMENT_LENGTH_MAXIMUM / 4;

    public DottedLine(UiUtil uiUtil){
        mDottedLineWidth = uiUtil.dpToPx(3);
    }

    @Override
    public List<PolylineOptions> createLine(List<LatLng> walkingPoints, int color) {
        List<PolylineOptions> polylineOptionsList = new ArrayList<>();
        /* Boolean to differentiate between a black dash and white space. */
        boolean added = false;
        /* This logic is need to walk through each point, and disern whether another point
         * should be generated, or not.  */
        for (int i = 0; i < walkingPoints.size() - 1; i++) {
            /* Get the distance between two geopoints */
            double distance = (SphericalUtil.computeDistanceBetween(walkingPoints.get(i),
                    walkingPoints.get(i + 1))) / 1000;

            if (distance <= SEGMENT_LENGTH_MAXIMUM && distance >= SEGMENT_LENGTH_MINIMUM) {
                if (!added) {
                    polylineOptionsList.add(new PolylineOptions()
                            .add(walkingPoints.get(i))
                            .add(walkingPoints.get(i + 1))
                            .width(mDottedLineWidth)
                            .color(color));
                    added = true;
                } else {
                    added = false;
                }
            } else if (distance > SEGMENT_LENGTH_MAXIMUM) {
                int divisionCount = (int) ((distance / SEGMENT_LENGTH_MAXIMUM));

                double latDiff = (walkingPoints.get(i + 1).latitude - walkingPoints.get(i).latitude) / divisionCount;
                double lngDiff = (walkingPoints.get(i + 1).longitude - walkingPoints.get(i).longitude) / divisionCount;

                LatLng lastKnownLatLng = new LatLng(walkingPoints.get(i).latitude, walkingPoints.get(i).longitude);

                for (int j = 0; j < divisionCount; j++) {
                    LatLng nextLatLng = new LatLng(lastKnownLatLng.latitude + latDiff,
                            lastKnownLatLng.longitude + lngDiff);
                    if (!added) {
                        polylineOptionsList.add(new PolylineOptions()
                                .add(lastKnownLatLng)
                                .add(nextLatLng)
                                .width(mDottedLineWidth)
                                .color(color));
                        added = true;
                    } else {
                        added = false;
                    }
                    lastKnownLatLng = nextLatLng;
                }
            }
        }
        return polylineOptionsList;
    }

    @Override
    public List<CircleOptions> createPoints(List<LatLng> points, int lineColor) {
        return null;
    }
}
