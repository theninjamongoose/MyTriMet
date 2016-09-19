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
 * Google returns it date value in seconds. A special deserializer is need to convert it to Millis
 * This will add consistency throughout the application.
 * While the {@link TrimetDateAdapter} converts milliseconds to a calendar object,
 * this deserializer coverts google's long values to milliseconds and then to a calendar object.
 */
public class GoogleDateAdapter extends TypeAdapter<Calendar> {



    @Override
    public void write(JsonWriter out, Calendar value) throws IOException {
        out.value(TimeUnit.MILLISECONDS.toSeconds(value.getTimeInMillis()));
    }

    @Override
    public Calendar read(JsonReader in) throws IOException {
        Calendar cal = CalendarFactory.getUTCCalendar();
        cal.setTimeInMillis(TimeUnit.SECONDS.toMillis(in.nextLong()));
        return cal;
    }
}
