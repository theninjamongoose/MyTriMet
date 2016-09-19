package com.example.tank.mytrimetpro;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.example.tank.mytrimetpro.destinations.DestinationModule;
import com.example.tank.mytrimetpro.util.MyTrimetUtilModule;

/**
 * Created by tank on 8/29/16.
 * Even though Dagger2 allows annotating a {@link dagger.Component} as a singleton, the code itself
 * must ensure only one instance of the class is created.
 */

public class MyTrimetApplication extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);

        mAppComponent = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule((getApplicationContext())))
                .myTrimetUtilModule(new MyTrimetUtilModule()).build();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

}
