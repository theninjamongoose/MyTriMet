package com.example.tank.mytrimetpro.base;

import com.example.tank.mytrimetpro.data.Destination;
import com.google.android.gms.location.places.Place;

/**
 * Created by tank on 8/29/16.
 */

public interface BasePresenter {

    void start();

    void updateSelectedDestination(Place place);

    void updateSelectedDestination(Destination destination);
}