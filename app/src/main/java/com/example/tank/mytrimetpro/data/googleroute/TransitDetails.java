package com.example.tank.mytrimetpro.data.googleroute;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.example.tank.mytrimetpro.R;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmiller on 8/1/2016.
 */
public class TransitDetails {

    private static final Map<String, Integer> LINE_COLORS;
    private static final String MAX_BLUE_LINE = "MAX Blue Line";
    private static final String MAX_GREEN_LINE = "MAX Green Line";
    private static final String MAX_ORAGE_LINE = "MAX Orange Line";
    private static final String MAX_RED_LINE = "MAX Red Line";
    private static final String MAX_YELLOW_LINE = "MAX Yellow Line";
    private static final String BUS_LINE = "bus";

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

    @SerializedName("line")
    /* Initialized here to avoid a null pointer exception
     * in the drawRoute method in the MapUtil class */
    private Line mLine = new Line();

    @SerializedName("departure_time")
    private Duration mDepartureTime;

    public Line getLine() {
        return mLine;
    }
    public void setLine(Line line) {
        this.mLine = line;
    }
    public Duration getDepartureTime() {
        return mDepartureTime;
    }
    public void setDepartureTime(Duration mDepartureTime) {
        this.mDepartureTime = mDepartureTime;
    }

    public int getLineColor(Context context) {
        String lineName = getLine().getName();
        Integer lineColor = LINE_COLORS.get(lineName);
        if(lineColor == null) {
            if (lineName == null) {
                lineColor = R.color.black;
            } else {
                lineColor = LINE_COLORS.get(BUS_LINE);
            }
        }
        return ContextCompat.getColor(context, lineColor);
    }


}
