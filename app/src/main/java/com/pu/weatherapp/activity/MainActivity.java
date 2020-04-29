package com.pu.weatherapp.activity;

import android.Manifest;
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
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.pu.weatherapp.R;
import com.pu.weatherapp.client.WeatherClient;
import com.pu.weatherapp.client.WeatherHttpClient;
import com.pu.weatherapp.listener.ConsumerLocationListener;
import com.pu.weatherapp.listener.ConsumerQueryTextListener;
import com.pu.weatherapp.model.WeatherResponse;
import com.pu.weatherapp.parser.JSONWeatherParser;
import com.pu.weatherapp.task.weather.JSONWeatherFindByLocationTask;
import com.pu.weatherapp.task.weather.JSONWeatherFindByNameTask;
import com.pu.weatherapp.util.DbUtils;
import com.pu.weatherapp.util.HttpUtils;

public class MainActivity extends Activity {
    private static final int PERMISSION_ID = 44;

    private DbUtils dbUtils;
    private WeatherClient weatherClient;
    private LocationManager locationManager;

    private SearchView searchTownView;
    private TextView cityText;
    private TextView condDescr;
    private TextView temp;
    private TextView press;
    private TextView windSpeed;
    private TextView hum;
    private ImageView imgView;
    private Button changeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();

        String path = getFilesDir().getPath();

        initDb(path);
        initFields();

        JSONWeatherParser weatherParser = new JSONWeatherParser();
        HttpUtils httpUtils = new HttpUtils(dbUtils);
        weatherClient = new WeatherHttpClient(weatherParser, httpUtils);

        executeTaskForCurrentLocation();
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
            Toast.makeText(this, "GPS isn't allowed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void initDb(String path) {
        this.dbUtils = new DbUtils(path);
        this.dbUtils.initDb();
    }

    private void initFields() {
        searchTownView = findViewById(R.id.searchName);
        searchTownView.setOnQueryTextListener(new ConsumerQueryTextListener(this::onQueryTextSubmit));

        cityText = findViewById(R.id.cityText);
        condDescr = findViewById(R.id.condDescr);
        temp = findViewById(R.id.temp);
        hum = findViewById(R.id.hum);
        press = findViewById(R.id.press);
        windSpeed = findViewById(R.id.windSpeed);
        imgView = findViewById(R.id.condIcon);
        changeView = findViewById(R.id.changeView);

        changeView.setOnClickListener(this::switchToForecast);
    }

    private void showWeather(WeatherResponse weather) {
        if (weather.iconData != null) {
            imgView.setImageBitmap(weather.iconData);
        }

        cityText.setText(String.format("%s,%s", weather.location.getCity(), weather.location.getCountry()));
        condDescr.setText(String.format("%s(%s)", weather.currentCondition.getCondition(), weather.currentCondition.getDescr()));
        temp.setText(String.format("%d°C", Math.round((weather.temperature.getTemp() - 273.15))));
        hum.setText(String.format("%s%%", weather.currentCondition.getHumidity()));
        press.setText(String.format("%s hPa", weather.currentCondition.getPressure()));
        windSpeed.setText(String.format("%s mps", weather.wind.getSpeed()));
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private void onLocationChanged(Location location) {
        if (location != null) {
            String lat = "" + location.getLatitude();
            String lon = "" + location.getLongitude();

            JSONWeatherFindByLocationTask task = new JSONWeatherFindByLocationTask(weatherClient, this::showWeather);
            task.execute(lat, lon);
        }
    }

    private boolean onQueryTextSubmit(String query) {
        JSONWeatherFindByNameTask task = new JSONWeatherFindByNameTask(weatherClient, this::showWeather);
        task.execute(query);
        return true;
    }

    private void switchToForecast(View view) {
        Intent intent = new Intent(this, ForecastActivity.class);
        startActivity(intent);
    }
}
