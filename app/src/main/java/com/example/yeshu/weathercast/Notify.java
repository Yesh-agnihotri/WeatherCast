package com.example.yeshu.weathercast1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Lenovo on 18-Sep-16.
 */
public class Notify extends AppCompatActivity implements LocationListener,GetWeatherMap.ResponseMap
{
    GetWeatherMap.MapWeatherTask obj;
    private LocationManager locationManager;
    private String provider;
    Intent intent;
    PendingIntent pIntent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        }

        obj=new GetWeatherMap.MapWeatherTask();
        obj.delegateMap=this;
        Toast.makeText(this,la,Toast.LENGTH_LONG).show();
        obj.execute(la,ln);



        // Prepare intent which is triggered if the
        // notification is selected
        intent = new Intent(this,MainActivity.class);

         pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Actions are just fake



    }
    double lat,lng;
    String la,ln;

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        la=String.format("%f",lat);
        ln=String.format("%f",lng);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    public void processFinish(double lat,String weather_city, String weather_condition, String weather_temperature, String weather_humidity, String weather_pressure,String lastUpdate,String weather_iconText,String speed,String tempmax,String tempmin,String sun_rise,String sun_set)
    {

       String weath=weather_city;
        String cond=weather_condition;
        Notification noti = new Notification.Builder(this)
                .setContentTitle(weath+cond)
                .setContentText(weather_condition).setSmallIcon(R.drawable.ic_menu_camera)
                .setContentIntent(pIntent)
                .addAction(R.drawable.ic_menu_camera, "Call", pIntent)
                .addAction(R.drawable.ic_menu_camera, "More", pIntent)
                .addAction(R.drawable.ic_menu_camera, "And more", pIntent).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);

        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);


    }
}
