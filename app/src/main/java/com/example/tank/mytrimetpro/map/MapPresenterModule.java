package com.example.tank.mytrimetpro.map;

import com.example.tank.mytrimetpro.destinations.DestinationContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tank on 9/1/16.
 */

@Module
public class MapPresenterModule {

    private final MapContract.View mView;

    public MapPresenterModule(MapContract.View view) {
        mView = view;
    }

    @Provides
    MapContract.View provideStatisticsContractView() {
        return mView;
    }

}
