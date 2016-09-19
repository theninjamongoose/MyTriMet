package com.example.tank.mytrimetpro.destinations;

import android.support.annotation.Nullable;

import com.example.tank.mytrimetpro.data.Destination;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tank on 8/30/16.
 */

@Module
public class DestinationModule {

    private Destination mDestination;

    public DestinationModule(Destination destination){
        this.mDestination = destination;
    }

    @Nullable
    @Provides
    Destination provideDestination() {
        return mDestination;
    }

}
