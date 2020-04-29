package com.pu.weatherapp.parser;

import com.pu.weatherapp.model.LocationResponse;
import com.pu.weatherapp.model.WeatherResponse;

import org.json.JSONException;
import org.json.JSONObject;

import static com.pu.weatherapp.util.ParserUtils.getFloat;
import static com.pu.weatherapp.util.ParserUtils.getInt;
import static com.pu.weatherapp.util.ParserUtils.getObject;
import static com.pu.weatherapp.util.ParserUtils.getString;

public class JSONWeatherParser implements JSONParser<WeatherResponse> {
    public WeatherResponse get(String data) throws JSONException {
        WeatherResponse weather = new WeatherResponse();
        JSONObject jObj = new JSONObject(data);

        weather.location = getLocation(jObj);

        JSONObject JSONWeather = jObj.getJSONArray("weather").getJSONObject(0);

        weather.currentCondition.setWeatherId(getInt("id", JSONWeather));
        weather.currentCondition.setDescr(getString("description", JSONWeather));
        weather.currentCondition.setCondition(getString("main", JSONWeather));
        weather.currentCondition.setIcon(getString("icon", JSONWeather));

        JSONObject mainObj = getObject("main", jObj);
        weather.currentCondition.setHumidity(getInt("humidity", mainObj));
        weather.currentCondition.setPressure(getInt("pressure", mainObj));
        weather.temperature.setMaxTemp(getFloat("temp_max", mainObj));
        weather.temperature.setMinTemp(getFloat("temp_min", mainObj));
        weather.temperature.setTemp(getFloat("temp", mainObj));

        JSONObject wObj = getObject("wind", jObj);
        weather.wind.setSpeed(getFloat("speed", wObj));

        JSONObject cObj = getObject("clouds", jObj);
        weather.clouds.setPerc(getInt("all", cObj));

        return weather;
    }

    private LocationResponse getLocation(JSONObject jObj) throws JSONException {
        LocationResponse loc = new LocationResponse();

        JSONObject coordObj = getObject("coord", jObj);
        loc.setLatitude(getFloat("lat", coordObj));
        loc.setLongitude(getFloat("lon", coordObj));

        JSONObject sysObj = getObject("sys", jObj);
        loc.setCountry(getString("country", sysObj));
        loc.setCity(getString("name", jObj));

        return loc;
    }
}
