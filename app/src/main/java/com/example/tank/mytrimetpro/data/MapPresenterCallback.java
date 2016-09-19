package com.example.tank.mytrimetpro.data;

import com.example.tank.mytrimetpro.data.googleroute.RouteRequest;
import com.example.tank.mytrimetpro.data.trimet.vehicle.VehicleData;

/**
 * Created by tank on 8/31/16.
 */
public interface MapPresenterCallback {
    void updateRoute(RouteRequest response);

    void queryForNextIncomingtransitVehicle(int id);

    void noVehicleAssigned();

    void trimetLocationUpdate(VehicleData vehicleData);
}
