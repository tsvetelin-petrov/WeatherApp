package com.pu.weatherapp.task.weather;

import android.os.AsyncTask;

import com.pu.weatherapp.client.WeatherClient;
import com.pu.weatherapp.model.WeatherResponse;

import java.util.function.Consumer;

public abstract class JSONWeatherBaseTask extends AsyncTask<String, Void, WeatherResponse> {
    final WeatherClient weatherClient;
    private final Consumer<WeatherResponse> weatherConsumer;

    JSONWeatherBaseTask(WeatherClient weatherClient, Consumer<WeatherResponse> weatherConsumer) {
        this.weatherClient = weatherClient;
        this.weatherConsumer = weatherConsumer;
    }

    protected abstract WeatherResponse getWeather(String... params) throws Exception;

    @Override
    protected final WeatherResponse doInBackground(String... params) {
        WeatherResponse weather = new WeatherResponse();

        try {
            weather = getWeather(params);
            weather.iconData = weatherClient.getImageFromCode(weather.currentCondition.getIcon());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weather;

    }

    @Override
    protected final void onPostExecute(WeatherResponse weather) {
        super.onPostExecute(weather);
        weatherConsumer.accept(weather);
    }
}
