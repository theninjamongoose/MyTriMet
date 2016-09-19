package com.example.tank.mytrimetpro.util;

import android.content.Context;

import com.example.tank.mytrimetpro.data.DataRequestQueue;
import com.example.tank.mytrimetpro.data.DestinationWrapper;
import com.example.tank.mytrimetpro.data.NetworkHandler;
import com.example.tank.mytrimetpro.util.Draw.DottedLine;
import com.example.tank.mytrimetpro.util.Draw.SolidLine;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tank on 8/30/16.
 */
@Module
public class MyTrimetUtilModule {

    @Singleton
    @Provides
    DestinationWrapper provideDestinationWrapper() {
        return new DestinationWrapper();
    }

    @Singleton
    @Provides
    UserPhoneStatus provideUserPhoneStatus(Context context) {
        return new UserPhoneStatus(context);
    }

    @Singleton
    @Provides
    DataRequestQueue provideDataRequestQueue(Context context) { return new DataRequestQueue(context); }

    @Singleton
    @Provides
    NetworkHandler provideNetworkHandler(Context context, DataRequestQueue dataRequestQueue) {
        return new NetworkHandler(context, dataRequestQueue);
    }

    @Singleton
    @Provides
    UiUtil provideUiUtill(Context context) {
        return new UiUtil(context);
    }

    @Singleton
    @Provides
    DottedLine provideDottedLine(UiUtil uiUtil) {
        return new DottedLine(uiUtil);
    }

    @Singleton
    @Provides
    SolidLine provideSolidLine(UiUtil uiUtil) {
        return new SolidLine(uiUtil);
    }

    @Singleton
    @Provides
    MapUtil provideMapUtil(Context context, UiUtil uiUtil, DottedLine dottedLine, SolidLine solidLine) {
        return new MapUtil(context, uiUtil, dottedLine, solidLine);
    }

    @Singleton
    @Provides
    Calculation provideCalculation(Context context) {
        return new Calculation(context);
    }

}
