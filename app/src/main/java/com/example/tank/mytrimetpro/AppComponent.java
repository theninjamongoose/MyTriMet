package com.example.tank.mytrimetpro;

import com.example.tank.mytrimetpro.data.DestinationWrapper;
import com.example.tank.mytrimetpro.data.NetworkHandler;
import com.example.tank.mytrimetpro.util.Calculation;
import com.example.tank.mytrimetpro.util.MapUtil;
import com.example.tank.mytrimetpro.util.MyTrimetUtilModule;
import com.example.tank.mytrimetpro.util.UserPhoneStatus;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by tank on 8/30/16.
 */

@Singleton
@Component(modules = {ApplicationModule.class, MyTrimetUtilModule.class})
public interface AppComponent {
    UserPhoneStatus getUserPhoneStatus();
    NetworkHandler getNetworkHandler();
    DestinationWrapper getDestinationWrapper();
    MapUtil getMapUtil();
    Calculation getCalcualtion();
}
