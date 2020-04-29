package com.pu.weatherapp.task.forecast;

import android.os.AsyncTask;

import com.pu.weatherapp.client.ForecastClient;
import com.pu.weatherapp.model.ForecastResponse;

import java.util.function.Consumer;

public abstract class JSONForecastBaseTask extends AsyncTask<String, Void, ForecastResponse> {
    final ForecastClient forecastClient;
    private final Consumer<ForecastResponse> forecastConsumer;

    JSONForecastBaseTask(ForecastClient forecastClient, Consumer<ForecastResponse> forecastConsumer) {
        this.forecastClient = forecastClient;
        this.forecastConsumer = forecastConsumer;
    }

    protected abstract ForecastResponse getForecast(String... params) throws Exception;

    @Override
    protected final ForecastResponse doInBackground(String... params) {
        ForecastResponse forecast = new ForecastResponse();

        try {
            forecast = getForecast(params);
            forecast.forecasts.forEach(this::getImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return forecast;
    }

    @Override
    protected final void onPostExecute(ForecastResponse forecast) {
        super.onPostExecute(forecast);
        forecastConsumer.accept(forecast);
    }

    private void getImage(ForecastResponse.DailyForecast dailyForecast) {
        dailyForecast.iconData = forecastClient.getImageFromCode(dailyForecast.weather.icon);
    }
}
