package com.pu.weatherapp.task.forecast;

import com.pu.weatherapp.client.ForecastClient;
import com.pu.weatherapp.model.ForecastResponse;

import java.util.function.Consumer;

public class JSONForecastFindByNameTask extends JSONForecastBaseTask {
    public JSONForecastFindByNameTask(ForecastClient forecastClient, Consumer<ForecastResponse> forecastConsumer) {
        super(forecastClient, forecastConsumer);
    }

    @Override
    protected ForecastResponse getForecast(String... params) {
        String location = params[0];
        return forecastClient.get(location);
    }
}
