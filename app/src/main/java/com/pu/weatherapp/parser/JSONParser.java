package com.pu.weatherapp.parser;

import org.json.JSONException;

public interface JSONParser<T> {
    T get(String data) throws JSONException;
}
