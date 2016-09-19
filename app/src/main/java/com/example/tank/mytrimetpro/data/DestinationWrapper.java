package com.example.tank.mytrimetpro.data;

import javax.inject.Singleton;

/**
 * Created by tank on 9/1/16.
 */

@Singleton
public class DestinationWrapper {

    private Destination mDestination;

    public DestinationWrapper(){
        mDestination = new Destination();
    }

    public Destination getDestination() {
        return mDestination;
    }

    public void setDestination(Destination mDestination) {
        this.mDestination = mDestination;
    }


}
