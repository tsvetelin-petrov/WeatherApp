package com.pu.weatherapp.task.forecast;

import com.pu.weatherapp.client.ForecastClient;
import com.pu.weatherapp.model.ForecastResponse;

import java.util.function.Consumer;

public class JSONForecastFindByLocationTask extends JSONForecastBaseTask {
    public JSONForecastFindByLocationTask(ForecastClient forecastClient, Consumer<ForecastResponse> forecastConsumer) {
        super(forecastClient, forecastConsumer);
    }

    @Override
    protected ForecastResponse getForecast(String... params) {
        double lat = Double.parseDouble(params[0]);
        double lon = Double.parseDouble(params[1]);
        return forecastClient.get(lat, lon);
    }
}
