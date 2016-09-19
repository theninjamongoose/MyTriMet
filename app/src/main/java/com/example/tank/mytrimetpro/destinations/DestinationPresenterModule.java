package com.example.tank.mytrimetpro.destinations;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tank on 8/29/16.
 */

@Module
public class DestinationPresenterModule {

    private final DestinationContract.View mView;

    public DestinationPresenterModule(DestinationContract.View view) {
        mView = view;
    }

    @Provides
    DestinationContract.View provideStatisticsContractView() {
        return mView;
    }

}
