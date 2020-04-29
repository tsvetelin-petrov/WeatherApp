package com.pu.weatherapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pu.weatherapp.R;
import com.pu.weatherapp.model.ForecastResponse;

import java.text.SimpleDateFormat;
import java.util.List;

public class ForecastAdapter extends ArrayAdapter<ForecastResponse.DailyForecast> {
    public ForecastAdapter(@NonNull Context context, @NonNull List<ForecastResponse.DailyForecast> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ForecastResponse.DailyForecast dailyForecast = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.weatherlist, parent, false);
        }
        ImageView imgView = convertView.findViewById(R.id.list_condIcon);
        TextView condDescr = convertView.findViewById(R.id.list_condDescr);
        TextView temp = convertView.findViewById(R.id.list_temp);
        TextView hum = convertView.findViewById(R.id.list_hum);
        TextView press = convertView.findViewById(R.id.list_press);
        TextView windSpeed = convertView.findViewById(R.id.list_windSpeed);
        TextView dateTime = convertView.findViewById(R.id.weatherDateTime);

        assert dailyForecast != null;
        imgView.setImageBitmap(dailyForecast.iconData);
        condDescr.setText(String.format("%s(%s)", dailyForecast.weather.condition, dailyForecast.weather.description));
        temp.setText(String.format("%d°C", Math.round((dailyForecast.temp - 273.15))));
        hum.setText(String.format("%s%%", dailyForecast.humidity));
        press.setText(String.format("%s hPa", dailyForecast.pressure));
        windSpeed.setText(String.format("%s mps", dailyForecast.wind.speed));

        SimpleDateFormat format = new SimpleDateFormat("EEEE HH:mm");
        String dateString = format.format(dailyForecast.date);
        dateTime.setText(dateString);

        return convertView;
    }
}
