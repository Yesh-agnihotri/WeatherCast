package com.example.yeshu.weathercast1.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.yeshu.weathercast1.application.WeatherCast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;



public class NetworkUtils {
    public static boolean isNetConnected() {
        NetworkInfo netInfo = ((ConnectivityManager) WeatherCast.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public static boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability gApi = GoogleApiAvailability.getInstance();
        int resultCode = gApi.isGooglePlayServicesAvailable(WeatherCast.getInstance());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (gApi.isUserResolvableError(resultCode)) {
                return false;
            }
        }
        return true;
    }

}
