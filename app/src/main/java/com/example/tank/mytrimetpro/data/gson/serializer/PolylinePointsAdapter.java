package com.example.tank.mytrimetpro.data.gson.serializer;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.List;

/**
 * Created by jmiller on 8/4/2016.
 */

public class PolylinePointsAdapter extends TypeAdapter<List<LatLng>> {
    @Override
    public void write(JsonWriter out, List<LatLng> value) throws IOException {
        out.value(PolyUtil.encode(value));
    }

    @Override
    public List<LatLng> read(JsonReader in) throws IOException {
        return PolyUtil.decode(in.nextString());
    }
}
