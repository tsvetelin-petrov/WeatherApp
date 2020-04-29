package com.pu.weatherapp.client;

import android.graphics.Bitmap;

public interface BaseClient<T> {
    T get(double lat, double lon);

    T get(String location);

    Bitmap getImageFromCode(String code);
}
