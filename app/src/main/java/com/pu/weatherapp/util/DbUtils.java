package com.pu.weatherapp.util;

import android.database.sqlite.SQLiteDatabase;

public class DbUtils {
    private static final String DB_NAME = "/weather.db";

    private final String path;

    public DbUtils(String path) {
        this.path = path;
    }

    public void initDb() {
        SQLiteDatabase db = getDatabase(path);
        String q = "CREATE TABLE if not exists api_call (" +
                "id INTEGER primary key AUTOINCREMENT, " +
                "call VARCHAR(100) not null)";
        db.execSQL(q);
        db.close();
    }

    void addCall(String call) {
        SQLiteDatabase db = getDatabase(path);
        String q = "INSERT INTO api_call (call) " +
                "VALUES(?)";
        db.execSQL(q, new Object[]{call});
        db.close();
    }

    private SQLiteDatabase getDatabase(String path) {
        return SQLiteDatabase.openOrCreateDatabase(
                path + DB_NAME,
                null
        );
    }
}
