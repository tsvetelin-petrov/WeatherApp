package com.pu.weatherapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {
    private final DbUtils dbUtils;

    public HttpUtils(DbUtils dbUtils) {
        this.dbUtils = dbUtils;
    }

    public String getData(String url) {
        HttpURLConnection con = null;
        InputStream is = null;

        try {
            con = getConnection(url);

            StringBuilder buffer = new StringBuilder();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null)
                buffer.append(line).append("\r\n");

            is.close();
            con.disconnect();
            return buffer.toString();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                assert is != null;
                is.close();
            } catch (Throwable ignored) {
            }
            try {
                assert con != null;
                con.disconnect();
            } catch (Throwable ignored) {
            }
        }

        return null;
    }

    public Bitmap getImageFromUrl(String url) {
        try {
            HttpURLConnection connection = getConnection(url);
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private HttpURLConnection getConnection(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) (new URL(url)).openConnection();
        con.setRequestMethod("GET");
        con.connect();

        dbUtils.addCall(url);
        return con;
    }
}
