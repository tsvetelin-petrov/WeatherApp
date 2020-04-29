package com.pu.weatherapp.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ForecastResponse {
    public LocationResponse location;
    public List<DailyForecast> forecasts;

    public ForecastResponse() {
        this.forecasts = new ArrayList<>();
    }

    public static class DailyForecast {
        public float temp;
        public float temp_min;
        public float temp_max;
        public float pressure;
        public float humidity;

        public Bitmap iconData;

        public Weather weather;
        public Clouds clouds;
        public Wind wind;
        public Date date;

        public DailyForecast() {
            this.weather = new Weather();
            this.clouds = new Clouds();
            this.wind = new Wind();
        }
    }

    public static class Weather {
        public int weatherId;
        public String condition;
        public String description;
        public String icon;
    }

    public static class Clouds {
        public float all;
    }

    public static class Wind {
        public float speed;
        public float deg;
    }
}
