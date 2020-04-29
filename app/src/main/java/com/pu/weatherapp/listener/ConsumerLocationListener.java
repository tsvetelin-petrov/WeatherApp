package com.pu.weatherapp.listener;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import java.util.function.Consumer;

public class ConsumerLocationListener implements LocationListener {

    private final Consumer<Location> locationConsumer;

    public ConsumerLocationListener(Consumer<Location> locationConsumer) {
        this.locationConsumer = locationConsumer;
    }

    @Override
    public void onLocationChanged(Location location) {
        locationConsumer.accept(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
