package com.example.tank.mytrimetpro.util;

import android.content.Context;
import android.util.DisplayMetrics;

import javax.inject.Singleton;

/**
 * Created by Jmiller on 8/5/2016.
 */
@Singleton
public class UiUtil {

    private Context mContext;

    public UiUtil(Context context){
        this.mContext = context;
    }

    public float dpToPx(float dp) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
