package com.pu.weatherapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.pu.weatherapp.R;
import com.pu.weatherapp.adapter.ForecastAdapter;
import com.pu.weatherapp.client.ForecastClient;
import com.pu.weatherapp.client.ForecastHttpClient;
import com.pu.weatherapp.listener.ConsumerLocationListener;
import com.pu.weatherapp.listener.ConsumerQueryTextListener;
import com.pu.weatherapp.model.ForecastResponse;
import com.pu.weatherapp.parser.JSONForecastParser;
import com.pu.weatherapp.task.forecast.JSONForecastFindByLocationTask;
import com.pu.weatherapp.task.forecast.JSONForecastFindByNameTask;
import com.pu.weatherapp.util.DbUtils;
import com.pu.weatherapp.util.HttpUtils;

import java.util.ArrayList;
import java.util.List;

public class ForecastActivity extends Activity {
    private ForecastClient forecastClient;

    private SearchView searchTownView;
    private TextView cityText;
    private Button changeView;

    private ForecastAdapter adapter;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        JSONForecastParser parser = new JSONForecastParser();
        String path = getFilesDir().getPath();
        DbUtils dbUtils = new DbUtils(path);
        HttpUtils httpUtils = new HttpUtils(dbUtils);
        forecastClient = new ForecastHttpClient(parser, httpUtils);

        initComponents();
        executeTaskForCurrentLocation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void executeTaskForCurrentLocation() {
        LocationListener locationListener = new ConsumerLocationListener(this::onLocationChanged);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000,
                    100f, locationListener);
        } else {
            Toast.makeText(this, "Map is not allowed", Toast.LENGTH_LONG).show();
        }
    }

    private void initComponents() {
        List<ForecastResponse.DailyForecast> dailyForecastList = new ArrayList<>();
        adapter = new ForecastAdapter(this, dailyForecastList);
        ListView weatherList = findViewById(R.id.weatherList);
        weatherList.setAdapter(adapter);

        searchTownView = findViewById(R.id.searchNameForecast);
        searchTownView.setOnQueryTextListener(new ConsumerQueryTextListener(this::onQueryTextSubmit));

        cityText = findViewById(R.id.cityTextForecast);

        changeView = findViewById(R.id.changeToWeather);
        changeView.setOnClickListener(this::switchToWeather);
    }

    private boolean onQueryTextSubmit(String query) {
        JSONForecastFindByNameTask task = new JSONForecastFindByNameTask(forecastClient, this::showForecast);
        task.execute(query);
        return true;
    }

    private void showForecast(ForecastResponse forecast) {
        adapter.clear();

        cityText.setText(String.format("%s,%s", forecast.location.getCity(), forecast.location.getCountry()));
        List<ForecastResponse.DailyForecast> dailyForecasts = forecast.forecasts;
        adapter.addAll(dailyForecasts);
    }

    private void onLocationChanged(Location location) {
        if (location != null) {
            String lat = "" + location.getLatitude();
            String lon = "" + location.getLongitude();

            JSONForecastFindByLocationTask task = new JSONForecastFindByLocationTask(forecastClient, this::showForecast);
            task.execute(lat, lon);
        }
    }

    private void switchToWeather(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
