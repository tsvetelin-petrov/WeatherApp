package com.pu.weatherapp.util;

public enum WeatherApiUrl {
    FORECAST("forecast"),
    WEATHER("weather");

    public static final String API_KEY = "b377a8f336806af64ab377bf15db4486";
    public static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    private final String type;

    WeatherApiUrl(String type) {
        this.type = type;
    }

    public String getUrlByTownName(String name) {
        return BASE_URL + type + "?q=" + name + "&APPID=" + API_KEY;
    }

    public String getUrlByCoordinates(double lat, double lon) {
        return BASE_URL + type + "?lat=" + Double.valueOf(lat).intValue() + "&lon=" + Double.valueOf(lon).intValue() + "&APPID=" + API_KEY;
    }
}
