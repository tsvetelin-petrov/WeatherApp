package com.pu.weatherapp.client;

import android.graphics.Bitmap;

import com.pu.weatherapp.model.WeatherResponse;
import com.pu.weatherapp.parser.JSONWeatherParser;
import com.pu.weatherapp.util.Constant;
import com.pu.weatherapp.util.HttpUtils;
import com.pu.weatherapp.util.WeatherApiUrl;

import org.json.JSONException;

public class WeatherHttpClient implements WeatherClient {
    private final JSONWeatherParser weatherParser;
    private final HttpUtils httpUtils;

    public WeatherHttpClient(JSONWeatherParser weatherParser, HttpUtils httpUtils) {
        this.weatherParser = weatherParser;
        this.httpUtils = httpUtils;
    }

    @Override
    public WeatherResponse get(double lat, double lon) {
        String url = WeatherApiUrl.WEATHER.getUrlByCoordinates(lat, lon);
        return getWeatherObject(url);
    }

    @Override
    public WeatherResponse get(String location) {
        String url = WeatherApiUrl.WEATHER.getUrlByTownName(location);
        return this.getWeatherObject(url);
    }

    @Override
    public Bitmap getImageFromCode(String code) {
        String url = Constant.IMG_URL_PREFIX + code + Constant.IMG_URL_POSTFIX;
        return httpUtils.getImageFromUrl(url);
    }

    private WeatherResponse getWeatherObject(String url) {
        String data = httpUtils.getData(url);

        try {
            return weatherParser.get(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}