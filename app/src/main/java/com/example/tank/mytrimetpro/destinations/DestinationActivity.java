package com.example.tank.mytrimetpro.destinations;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tank.mytrimetpro.base.AutocompleteListener;
import com.example.tank.mytrimetpro.base.DrawerActivity;
import com.example.tank.mytrimetpro.MyTrimetApplication;
import com.example.tank.mytrimetpro.R;
import com.example.tank.mytrimetpro.data.Destination;
import com.example.tank.mytrimetpro.data.googleroute.Leg;
import com.example.tank.mytrimetpro.map.MapActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static dagger.internal.Preconditions.checkNotNull;

public class DestinationActivity extends DrawerActivity implements DestinationContract.View, AdapterItemInteractionCallback {

    private RecyclerView mDestinationHistoryListView;
    private LinearLayoutManager mLayoutManager;

    @Inject DestinationPresenter mPresenter;

    private RelativeLayout mActiveRouteWrapper;
    private TextView mActiveRoutePlaceName;
    private TextView mActiveRouteArrivalTime;
    private TextView mActiveRouteDisplay;
    private DestinationAdapter mDestinationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);
        drawerSetUp(R.id.activity_destination);
        initVariables();
        setUpDestinationHistoryListView();

        // init presenter
        DaggerDestinationComponent.builder()
                .appComponent(((MyTrimetApplication) getApplication()).getAppComponent())
                .destinationPresenterModule(new DestinationPresenterModule(this))
                .build().inject(this);
    }


    private void initVariables() {
        mDestinationHistoryListView = (RecyclerView) findViewById(R.id.destination_history);
        mLayoutManager = new LinearLayoutManager(this);
        /* Active route ui components */
        mActiveRouteWrapper = (RelativeLayout) findViewById(R.id.active_route_display_info_wrapper);
        mActiveRouteWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo grab active route somehow?
                mPresenter.updateSelectedDestination(new Destination());
            }
        });
        mActiveRoutePlaceName = (TextView) findViewById(R.id.active_route_place_name);
        mActiveRouteArrivalTime = (TextView) findViewById(R.id.active_route_arrival_time);
        mActiveRouteDisplay = (TextView) findViewById(R.id.active_route_display);

        mDestinationAdapter =
                new DestinationAdapter(new ArrayList<Destination>(0), this);
        mDestinationHistoryListView.setAdapter(mDestinationAdapter);
    }

    /* set up listview to display destination history */
    public void setUpDestinationHistoryListView() {
        mDestinationHistoryListView.setLayoutManager(mLayoutManager);
        // add horizontal lines between each item in the list
        mDestinationHistoryListView.addItemDecoration(new DividerItemDecoration(this));
        mDestinationHistoryListView.setHasFixedSize(true);
    }

    @Override
    public void setActiveDestination(Destination destination){
        if(destination == null){
            mActiveRouteWrapper.setVisibility(View.GONE);
            mActiveRouteDisplay.setVisibility(View.GONE);
        } else {
            mActiveRouteWrapper.setVisibility(View.VISIBLE);
            mActiveRouteDisplay.setVisibility(View.VISIBLE);
            mActiveRoutePlaceName.setText(destination.getPlaceName());
            Leg leg = destination.getRoute().getLeg();
            if(destination.getRoute() != null && leg.getDestinationArrivalTime() != null) {
                mActiveRouteArrivalTime
                        .setText(getString(R.string.arrival_time, leg.getDestinationArrivalTime().getText()));
            }else if(leg.getDepartureTime().getText()!=null){
                mActiveRouteArrivalTime
                        .setText(getString(R.string.from_departure_time, leg.getDepartureTime().getText()));
            }else{
                mActiveRouteArrivalTime.setText(getString(R.string.no_arrival_time));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void showDestinations(List<Destination> destinations) {
        mDestinationAdapter.replaceData(destinations);
    }

    @Override
    public void displayNetworkedRequiredUi(boolean isNetworked) {
        if(isNetworked){
            setUpAutocomplete(new AutocompleteListener(mPresenter));
        } else {
            Toast.makeText(this, R.string.no_network_message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void startMap() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    //todo get rid of casting, do we need this method?
    @Override
    public void setPresenter(DestinationContract.Presenter presenter) {
        mPresenter = checkNotNull((DestinationPresenter) presenter);
    }

    @Override
    public void onAdapterItemClicked(Destination destination) {
        mPresenter.updateSelectedDestination(destination);
    }
}
