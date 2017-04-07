package com.example.yeshu.weathercast.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.yeshu.weathercast.utils.GPSTracker;
import com.example.yeshu.weathercast1.R;

import java.util.Random;

/**
 * Created by Lenovo on 18-Sep-16.
 */
public class Notify extends AppCompatActivity implements GetWeatherMap.ResponseMap{
    String weath,cond;
    GPSTracker gps;
    GetWeatherMap.MapWeatherTask obj1;


    private Handler imageHandler = new Handler() {
    };
    //array containing drawables ids
    int[] myarray = new int[]{R.drawable.back,R.drawable.backe,R.drawable.backee,R.drawable.backeee};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        Location location;
        boolean l;
        gps = new GPSTracker(this);
        l=gps.canGetLocation();
        if(l==true) {
            location = gps.getLocation();
            // Initialize the location fields
            if (location != null) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                String lai = String.format("%f", latitude);
                String lni = String.format("%f", longitude);
                Toast.makeText(this, lai, Toast.LENGTH_LONG).show();
                obj1 = new GetWeatherMap.MapWeatherTask();
                obj1.delegateMap = this;
                Toast.makeText(this, lai, Toast.LENGTH_LONG).show();
                obj1.execute(lai, lni);
            }
        }

        imageHandler.post(handle);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }
    private final Runnable handle = new Runnable(){
        public void run(){
            try {
                Random r = new Random();
                int i = r.nextInt(myarray.length);
                Notify.this.getWindow().setBackgroundDrawableResource(myarray[i]);
                imageHandler.postDelayed(this,5000);
            }
            catch (Exception e) {
                Log.d("Test", e.toString());
            }
        }
    };
    public void processFinish(double lat, String weather_city, String weather_condition, String weather_temperature, String weather_humidity, String weather_pressure, String lastUpdate, String weather_iconText, String speed, String tempmax, String tempmin, String sun_rise, String sun_set)
    {

        this.weath=weather_city;
        this.cond=weather_condition;

    }
    public  void  showNotification(View v)
    {
        showNotificatio();
    }


    public void showNotificatio() { // Use NotificationCompat.Builder to set up our notification.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        //icon appears in device notification bar and right hand corner of notification
        builder.setSmallIcon(R.drawable.icon);

        // This intent is fired when notification is clicked
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Set the intent that will fire when the user taps the notification.
        builder.setContentIntent(pendingIntent);

        // Large icon appears on the left of the notification
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.icon));

        // Content title, which appears in large type at the top of the notification
        builder.setContentTitle(this.weath);

        // Content text, which appears in smaller text below the title
        builder.setContentText(this.weath+this.cond);

        // The subtext, which appears under the text on newer devices.
        // This will show-up in the devices with Android 4.2 and above only


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        notificationManager.notify(1, builder.build());

    }
    public  void cancelNotification(View v)
    {
        cancelNotificatio(1);
    }


    public void cancelNotificatio(int notificationId) {

        if (Context.NOTIFICATION_SERVICE != null) {
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
            nMgr.cancel(notificationId);
        }
    }

}