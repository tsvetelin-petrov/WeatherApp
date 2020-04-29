package com.pu.weatherapp.task.weather;


import com.pu.weatherapp.client.WeatherClient;
import com.pu.weatherapp.model.WeatherResponse;

import java.util.function.Consumer;

public class JSONWeatherFindByNameTask extends JSONWeatherBaseTask {

    public JSONWeatherFindByNameTask(WeatherClient weatherClient, Consumer<WeatherResponse> weatherConsumer) {
        super(weatherClient, weatherConsumer);
    }

    @Override
    protected WeatherResponse getWeather(String... params) {
        String location = params[0];
        return weatherClient.get(location);
    }
}
