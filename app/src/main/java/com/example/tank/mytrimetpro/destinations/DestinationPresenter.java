package com.example.tank.mytrimetpro.destinations;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.tank.mytrimetpro.R;
import com.example.tank.mytrimetpro.data.Destination;
import com.example.tank.mytrimetpro.data.DestinationWrapper;
import com.example.tank.mytrimetpro.util.UserPhoneStatus;
import com.google.android.gms.location.places.Place;

import javax.inject.Inject;

/**
 * Created by tank on 8/28/16.
 */

public class DestinationPresenter implements DestinationContract.Presenter {

    private final DestinationWrapper mDestinationWrapper;
    private Destination mActiveDestination;
    private UserPhoneStatus mUserPhoneStatus;

    @NonNull
    private final DestinationContract.View mDestinationsView;


    @Inject
    DestinationPresenter(UserPhoneStatus userPhoneStatus,
                         DestinationWrapper destinationWrapper, DestinationContract.View destinationsView){
        this.mUserPhoneStatus = userPhoneStatus;
        mDestinationWrapper = destinationWrapper;
        this.mDestinationsView = destinationsView;
    }

    @Inject
    void setupListeners() {
        mDestinationsView.setPresenter(this);
    }



    @Override
    public void start() {
        if (mUserPhoneStatus.isNetworked()) {
            mDestinationsView.displayNetworkedRequiredUi(true);
        } else {
            mDestinationsView.displayNetworkedRequiredUi(false);
        }

        mActiveDestination = Destination.getActiveRoute();
        mDestinationsView.setActiveDestination(mActiveDestination);
        mDestinationsView.showDestinations(Destination.getDestinations());
    }

    @Override
    public void updateSelectedDestination(Place place){
        updateSelectedDestination(new Destination(place));
    }

    @Override
    public void updateSelectedDestination(Destination destination) {
        Destination.createOrUpdateDestination(destination);
        mDestinationWrapper.setDestination(destination);
        mDestinationsView.startMap();
    }

}
