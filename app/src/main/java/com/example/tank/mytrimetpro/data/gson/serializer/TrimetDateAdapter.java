package com.example.tank.mytrimetpro.data.gson.serializer;

import com.example.tank.mytrimetpro.util.CalendarFactory;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by jmiller on 8/18/2016.
 */

public class TrimetDateAdapter extends TypeAdapter<Calendar> {
    @Override
    public void write(JsonWriter out, Calendar value) throws IOException {
        out.value(TimeUnit.MILLISECONDS.toSeconds(value.getTimeInMillis()));
    }

    @Override
    public Calendar read(JsonReader in) throws IOException {
        Calendar cal = CalendarFactory.getUTCCalendar();
        cal.setTimeInMillis(in.nextLong());
        return cal;
    }
}
