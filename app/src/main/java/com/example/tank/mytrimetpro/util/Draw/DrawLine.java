package com.example.tank.mytrimetpro.util.Draw;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * Created by jmiller on 8/4/2016.
 */

public interface DrawLine {
    List<PolylineOptions> createLine(List<LatLng> points, int lineColor);
    List<CircleOptions> createPoints(List<LatLng> points, int lineColor);
}
