package com.example.tank.mytrimetpro.data.googleroute;

import com.example.tank.mytrimetpro.data.gson.serializer.GoogleDateAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

/**
 * Created by jmiller on 8/10/2016.
 */
public class Duration {

    @SerializedName("text")
    private String mText;

    @SerializedName("value")
    @JsonAdapter(GoogleDateAdapter.class)
    private Calendar mValue;

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public Calendar getValue() {
        return mValue;
    }

    public void setValue(Calendar value) {
        this.mValue = value;
    }
}



