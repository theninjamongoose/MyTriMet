package com.example.tank.mytrimetpro.data.gson.serializer;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by Steffy
 */

public class LatLongAdapter extends TypeAdapter<LatLng> {

    private static final String LAT = "lat";
    private static final String LNG = "lng";

    @Override
    public void write(JsonWriter out, LatLng latLng) throws IOException {
        out.beginObject();
        out.name(LAT).value(latLng.latitude);
        out.name(LNG).value(latLng.longitude);
        out.endObject();
    }

    @Override
    public LatLng read(JsonReader in) throws IOException {
        in.beginObject();
        Double latitude = null;
        Double longitude = null;
        while (in.hasNext()) {
            switch (in.nextName()) {
                case LAT:
                    latitude = in.nextDouble();
                    break;
                case LNG:
                    longitude = in.nextDouble();
                    break;
            }
        }
        in.endObject();
        if (latitude != null && longitude != null) {
            return new LatLng(latitude, longitude);
        }
        return null;
    }
}
