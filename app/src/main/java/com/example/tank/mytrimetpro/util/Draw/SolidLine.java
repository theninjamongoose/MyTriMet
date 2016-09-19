package com.example.tank.mytrimetpro.util.Draw;

import com.example.tank.mytrimetpro.util.UiUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

/**
 * Created by Jmiller on 8/5/2016.
 */

@Singleton
public class SolidLine implements DrawLine {
    private final float mSolidLineWidth;
    private final float mCircleRadius;

    public SolidLine(UiUtil uiUtil){
        mSolidLineWidth = uiUtil.dpToPx(3);
        mCircleRadius = uiUtil.dpToPx(2);
    }

    @Override
    public List<PolylineOptions> createLine(List<LatLng> points, int lineColor) {
         /* Add a red circle on the beggining and end of each transit section */
        List<PolylineOptions> polylineOptionsList = new ArrayList<>();
        for (int i = 0; i < points.size() - 1; i++) {
            polylineOptionsList.add(new PolylineOptions()
                    .add(points.get(i))
                    .add(points.get(i + 1))
                    .width(mSolidLineWidth)
                    .color(lineColor));
        }
        return polylineOptionsList;
    }

    /* Circle Options for transit line/s */
    public List<CircleOptions> createPoints(List<LatLng> points, int lineColor) {
        List<CircleOptions> circleOptionsList = new ArrayList<>();
        circleOptionsList.add(createCircleOption(points.get(0), lineColor));
        circleOptionsList.add(createCircleOption(points.get(points.size() - 1), lineColor));
        return circleOptionsList;
    }

    private CircleOptions createCircleOption(LatLng point, int color){
        return new CircleOptions().center(point).clickable(false)
                .radius(mCircleRadius).fillColor(color).strokeColor(color);
    }
}
