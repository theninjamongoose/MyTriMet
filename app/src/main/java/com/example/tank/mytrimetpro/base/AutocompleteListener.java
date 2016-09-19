package com.example.tank.mytrimetpro.base;

import android.util.Log;

import com.example.tank.mytrimetpro.data.Destination;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import org.greenrobot.eventbus.EventBus;


public class AutocompleteListener implements PlaceSelectionListener {
    private static final String TAG = AutocompleteListener.class.getSimpleName();
    private static final String ERROR_MESSAGE = "An error occurred:";
    private final BasePresenter mBasePresenter;

    public AutocompleteListener(BasePresenter basePresenter){
        mBasePresenter = basePresenter;
    }

    @Override
    public void onPlaceSelected(Place place) {
        mBasePresenter.updateSelectedDestination(place);
    }

    @Override
    public void onError(Status status) {
        Log.e(TAG, ERROR_MESSAGE + status);
    }

}
