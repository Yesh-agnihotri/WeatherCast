package com.example.yeshu.weathercast1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener,GoogleMap.OnMarkerClickListener,GetWeatherMap.ResponseMap,GetWeather.Response {

    private GoogleMap mMap;
    Bitmap bmp;
    Marker marker;
    Text cityname;
    LatLng point;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    int i=0;
    String citynamelist[]={"Shanghai","Karachi","Beijing","Delhi","Lagos","Tianjin","Istanbul","Tokyo","Guangzhou","Mumbai","Moscow","São Paulo","Shenzhen","Jakarta","Lahore","Seoul","Kinshasa","Cairo","Mexico City","Lima","London","NewYork City","Bengaluru","Bangkok","Ho Chi Minh City","Dongguan","Chongqing","Nanjing","Tehran","Shenyang","Bogotá","Ningbo","Hong Kong","Hanoi","Baghdad","Changsha","Dhaka","Wuhan","Hyderabad","Chennai","Rio de Janeiro","Faisalabad","Foshan","Zunyi","Santiago","Santiago","Ahmedabad","Singapore","Shantou","Yangon"};
    @Override
    public void onMapReady(GoogleMap googleMap)
    {

        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false); //to disable navigation buttons
        mMap.setOnMapClickListener(this);
        GetWeather.weatherTask object=new GetWeather.weatherTask();
        object.delegate=this;
        object.execute(citynamelist[i]);

    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (marker != null) {
            marker.remove();
        }

        point = new LatLng(latLng.latitude, latLng.longitude);
        marker = mMap.addMarker(new MarkerOptions().position(point));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(point));

        GetWeatherMap.MapWeatherTask obj1 = new GetWeatherMap.MapWeatherTask();
        obj1.delegateMap = this;
        LatLng latlng = point;
        Double l1 = latlng.latitude;
        Double l2 = latlng.longitude;
        String coordl1 = l1.toString();
        String coordl2 = l2.toString();
        obj1.execute(coordl1, coordl2);

        mMap.setOnMarkerClickListener(this);
    }



    String city;
    String weather;

    @Override
    public boolean onMarkerClick(Marker marker) {

        marker.setTitle(city);
        marker.setSnippet(weather);
        return false;

    }

    @Override
    public void processFinish(double lat, String weather_city, String weather_condition, String weather_temperature, String weather_humidity, String weather_pressure, String lastUpdate, String weather_iconText, String speed, String tempmax, String tempmin, String sun_rise, String sun_set) {
        city = weather_city;
        weather = weather_condition;
        final String src = "http://openweathermap.org/img/w/" + weather_iconText + ".png";
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InputStream in = new URL(src).openStream();
                    bmp = BitmapFactory.decodeStream(in);
                } catch (Exception e)
                {
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (bmp != null)
                {
                    marker.remove();
                    Bitmap rbmp = Bitmap.createScaledBitmap(bmp, 80, 80, false);
                    marker=mMap.addMarker(new MarkerOptions().position(point)
                            .icon(BitmapDescriptorFactory.fromBitmap(rbmp)));
                }
            }

        }.execute();

    }

    double latcity,longcity;
    @Override
    public void processFinish(double lat, double lon, final String weather_city, final String weather_condition, String weather_temperature, String weather_humidity, String weather_pressure, String lastUpdate, String weather_iconText, String speed, String tempmax, String tempmin, String sun_rise, String sun_set)
    {

        city = weather_city;
        final LatLng loc=new LatLng(lat,lon);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        final String src = "http://openweathermap.org/img/w/" + weather_iconText + ".png";
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... params) {
                try
                {
                    InputStream in = new URL(src).openStream();
                    bmp = BitmapFactory.decodeStream(in);
                }
                catch (Exception e)
                {
                }
                return null;
            }
            Marker city_marker;
            @Override
            protected void onPostExecute(Void result)
            {
                if (bmp != null)
                {
                    Bitmap rbmp = Bitmap.createScaledBitmap(bmp, 60, 60, false);
                    city_marker=mMap.addMarker(new MarkerOptions().position(loc)
                            .icon(BitmapDescriptorFactory.fromBitmap(rbmp)));
                    city_marker.setTitle(weather_city);
                    city_marker.setSnippet(weather_condition);
                }
            }

        }.execute();
        i++;
        GetWeather.weatherTask object=new GetWeather.weatherTask();
        object.delegate=this;
        object.execute(citynamelist[i]);
    }
}