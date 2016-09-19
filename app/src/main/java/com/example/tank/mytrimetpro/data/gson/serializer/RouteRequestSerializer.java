package com.example.tank.mytrimetpro.data.gson.serializer;

import com.activeandroid.serializer.TypeSerializer;
import com.example.tank.mytrimetpro.data.googleroute.RouteRequest;
import com.google.gson.Gson;

/**
 * Created by jmiller on 8/9/2016.
 * The purpose of this class is to convert
 * between a RouteRequest object and a Json String.
 *
 * The RouteRequest object is saved to the database
 * as a String because ActiveAndroid doesn't support
 * saving lists to the database.
 */
public class RouteRequestSerializer extends TypeSerializer {
    private static final Gson gson = new Gson();

    @Override
    public Class<?> getDeserializedType() {
        return RouteRequest.class;
    }

    @Override
    public Class<?> getSerializedType() {
        return String.class;
    }

    @Override
    public String serialize(Object data) {
        if(null == data){
            return null;
        }
        final String json = gson.toJson(data);
        return json;
    }

    @Override
    public RouteRequest deserialize(Object data) {
        if(null == data){
            return null;
        }
        final RouteRequest route = gson.fromJson(data.toString(), RouteRequest.class);
        return route;
    }
}
