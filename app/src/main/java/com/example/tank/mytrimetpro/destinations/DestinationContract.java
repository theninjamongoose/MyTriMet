package com.example.tank.mytrimetpro.destinations;

import com.example.tank.mytrimetpro.base.BasePresenter;
import com.example.tank.mytrimetpro.base.BaseView;
import com.example.tank.mytrimetpro.data.Destination;

import java.util.List;

/**
 * Created by tank on 8/29/16.
 */

public interface DestinationContract {

    interface View extends BaseView<Presenter> {

        void setActiveDestination(Destination activeRoute);

        void showDestinations(List<Destination> destinations);

        void displayNetworkedRequiredUi(boolean updateDb);

        void startMap();
    }

    interface  Presenter extends BasePresenter {

    }

}
