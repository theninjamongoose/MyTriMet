package com.example.tank.mytrimetpro.data.googleroute;

import com.example.tank.mytrimetpro.data.gson.serializer.GoogleDateAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

public class Time {

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