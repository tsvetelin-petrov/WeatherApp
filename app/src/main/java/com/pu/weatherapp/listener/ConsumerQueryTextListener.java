package com.pu.weatherapp.listener;

import android.widget.SearchView;

import java.util.function.Predicate;

public class ConsumerQueryTextListener implements SearchView.OnQueryTextListener {
    private final Predicate<String> onTextSubmit;

    public ConsumerQueryTextListener(Predicate<String> onTextSubmit) {
        this.onTextSubmit = onTextSubmit;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return onTextSubmit.test(query);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
