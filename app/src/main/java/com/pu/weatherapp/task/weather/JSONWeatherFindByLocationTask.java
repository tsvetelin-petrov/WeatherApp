package com.pu.weatherapp.task.weather;

import com.pu.weatherapp.client.WeatherClient;
import com.pu.weatherapp.model.WeatherResponse;

import java.util.function.Consumer;

public class JSONWeatherFindByLocationTask extends JSONWeatherBaseTask {

    public JSONWeatherFindByLocationTask(WeatherClient weatherClient, Consumer<WeatherResponse> weatherConsumer) {
        super(weatherClient, weatherConsumer);
    }

    @Override
    protected WeatherResponse getWeather(String... params) {
        double lat = Double.parseDouble(params[0]);
        double lon = Double.parseDouble(params[1]);
        return weatherClient.get(lat, lon);
    }
}
