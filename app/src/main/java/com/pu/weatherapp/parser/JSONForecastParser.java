package com.pu.weatherapp.parser;

import com.pu.weatherapp.model.ForecastResponse;
import com.pu.weatherapp.model.LocationResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static com.pu.weatherapp.util.ParserUtils.getFloat;
import static com.pu.weatherapp.util.ParserUtils.getObject;

public class JSONForecastParser implements JSONParser<ForecastResponse> {
    @Override
    public ForecastResponse get(String data) throws JSONException {
        ForecastResponse forecast = new ForecastResponse();
        JSONObject jsonObject = new JSONObject(data);

        setLocation(forecast, jsonObject);
        JSONArray forecastsArray = jsonObject.getJSONArray("list");
        for (int i = 0; i < forecastsArray.length(); i++) {
            JSONObject currentForecastObject = forecastsArray.getJSONObject(i);
            forecast.forecasts.add(getDailyForecast(currentForecastObject));
        }

        return forecast;
    }

    private void setLocation(ForecastResponse forecast, JSONObject jsonObject) throws JSONException {
        LocationResponse locationResponse = new LocationResponse();
        JSONObject cityObj = getObject("city", jsonObject);
        locationResponse.setCity(cityObj.getString("name"));
        locationResponse.setCountry(cityObj.getString("country"));

        JSONObject coordObject = cityObj.getJSONObject("coord");
        locationResponse.setLatitude(getFloat("lat", coordObject));
        locationResponse.setLongitude(getFloat("lon", coordObject));

        forecast.location = locationResponse;
    }

    private ForecastResponse.DailyForecast getDailyForecast(JSONObject currentForecastObject) throws JSONException {
        ForecastResponse.DailyForecast dailyForecast = new ForecastResponse.DailyForecast();

        JSONObject mainObject = currentForecastObject.getJSONObject("main");
        dailyForecast.temp = getFloat("temp", mainObject);
        dailyForecast.temp_min = getFloat("temp_min", mainObject);
        dailyForecast.temp_max = getFloat("temp_max", mainObject);
        dailyForecast.pressure = getFloat("pressure", mainObject);
        dailyForecast.humidity = getFloat("humidity", mainObject);

        setWeather(dailyForecast, currentForecastObject);
        setClouds(dailyForecast, currentForecastObject);
        setWind(dailyForecast, currentForecastObject);
        setDate(dailyForecast, currentForecastObject);

        return dailyForecast;
    }

    private void setWeather(ForecastResponse.DailyForecast dailyForecast, JSONObject currentForecastObject) throws JSONException {
        JSONObject weatherObject = currentForecastObject.getJSONArray("weather").getJSONObject(0);

        dailyForecast.weather.weatherId = weatherObject.getInt("id");
        dailyForecast.weather.condition = weatherObject.getString("main");
        dailyForecast.weather.description = weatherObject.getString("description");
        dailyForecast.weather.icon = weatherObject.getString("icon");
    }

    private void setClouds(ForecastResponse.DailyForecast dailyForecast, JSONObject currentForecastObject) throws JSONException {
        JSONObject cloudsObject = currentForecastObject.getJSONObject("clouds");

        dailyForecast.clouds.all = getFloat("all", cloudsObject);
    }

    private void setWind(ForecastResponse.DailyForecast dailyForecast, JSONObject currentForecastObject) throws JSONException {
        JSONObject windObject = currentForecastObject.getJSONObject("wind");
        dailyForecast.wind.speed = getFloat("speed", windObject);
        dailyForecast.wind.deg = getFloat("deg", windObject);
    }

    private void setDate(ForecastResponse.DailyForecast dailyForecast, JSONObject currentForecastObject) throws JSONException {
        String dateString = currentForecastObject.getString("dt_txt");
//        dateString = dateString.split(" ")[0];

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            dailyForecast.date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
