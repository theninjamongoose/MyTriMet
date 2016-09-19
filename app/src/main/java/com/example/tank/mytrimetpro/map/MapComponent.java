package com.example.tank.mytrimetpro.map;

import com.example.tank.mytrimetpro.AppComponent;
import com.example.tank.mytrimetpro.util.ActivityScoped;

import dagger.Component;

/**
 * Created by tank on 9/1/16.
 */

@ActivityScoped
@Component(dependencies = AppComponent.class,
        modules = {MapPresenterModule.class})
public interface MapComponent {
    void inject(MapActivity activity);
}
