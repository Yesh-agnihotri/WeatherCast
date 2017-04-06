package com.example.yeshu.weathercast1.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.yeshu.weathercast1.utils.Constants;

/**
 * Created by simran on 4/6/2017.
 */

public class WeatherCast extends Application {
    private static WeatherCast singleton = null;
    SharedPreferences sharedPrefs;
    public static WeatherCast getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        singleton = this;
        super.onCreate();
        sharedPrefs = getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
    }
}
