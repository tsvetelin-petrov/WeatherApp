package com.pu.weatherapp.client;

import android.graphics.Bitmap;

import com.pu.weatherapp.model.ForecastResponse;
import com.pu.weatherapp.parser.JSONForecastParser;
import com.pu.weatherapp.util.Constant;
import com.pu.weatherapp.util.HttpUtils;
import com.pu.weatherapp.util.WeatherApiUrl;

import org.json.JSONException;

public class ForecastHttpClient implements ForecastClient {
    private final JSONForecastParser forecastParser;
    private final HttpUtils httpUtils;

    public ForecastHttpClient(JSONForecastParser forecastParser, HttpUtils httpUtils) {
        this.forecastParser = forecastParser;
        this.httpUtils = httpUtils;
    }

    @Override
    public ForecastResponse get(double lat, double lon) {
        String url = WeatherApiUrl.FORECAST.getUrlByCoordinates(lat, lon);
        return getForecastObject(url);
    }

    @Override
    public ForecastResponse get(String location) {
        String url = WeatherApiUrl.FORECAST.getUrlByTownName(location);
        return getForecastObject(url);
    }

    @Override
    public Bitmap getImageFromCode(String code) {
        String url = Constant.IMG_URL_PREFIX + code + Constant.IMG_URL_POSTFIX;
        return httpUtils.getImageFromUrl(url);
    }

    private ForecastResponse getForecastObject(String url) {
        String data = httpUtils.getData(url);

        try {
            return forecastParser.get(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}