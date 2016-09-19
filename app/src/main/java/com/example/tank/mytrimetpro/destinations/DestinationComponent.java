package com.example.tank.mytrimetpro.destinations;

import com.example.tank.mytrimetpro.AppComponent;
import com.example.tank.mytrimetpro.util.ActivityScoped;

import dagger.Component;

/**
 * Created by tank on 8/30/16.
 */
@ActivityScoped
@Component(dependencies = AppComponent.class,
        modules = {DestinationPresenterModule.class})
public interface DestinationComponent {
    void inject(DestinationActivity activity);
}
