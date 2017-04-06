package com.example.yeshu.weathercast.activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.yeshu.weathercast1.R;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by yeshu on 17-09-2016.
 */

    public class Preference extends AppCompatActivity implements GetWeather.Response,forecast.ResponseForecast {
    TextView city, weather, temper, humid, press,updatedOn,sunrise,sunset,wind,temprange;
    ImageView imageView;
    private Bitmap bmp;
    MainActivity w;
    AutoCompleteTextView ac;
    Typeface weatherFont;
    GetWeather.weatherTask obj;
    int flag=0;
    ImageView imageView1,imageView2,imageView3;

    forecast.weatherTask obj2=new forecast.weatherTask();
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    ProgressBar progressBar2,progressBarForecast;
    String str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        weatherFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/weathericons-regular-webfont.ttf");
        city = (TextView) findViewById(R.id.CityName);
        weather = (TextView) findViewById(R.id.WeatherCondition);
        temper = (TextView) findViewById(R.id.Temperature);
        humid= (TextView) findViewById(R.id.Humidity);
        updatedOn=(TextView)findViewById(R.id.LastUpdate);
        imageView=(ImageView)findViewById(R.id.WeatherIcon);
        sunrise=(TextView)findViewById(R.id.Sunrise);
        sunset=(TextView)findViewById(R.id.Sunset);
        press=(TextView)findViewById(R.id.Pressure);
        wind=(TextView)findViewById(R.id.WindSpeed);
        temprange=(TextView)findViewById(R.id.TempRange);
        ac=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        sharedpreferences = getSharedPreferences(MainActivity.mypreference,Context.MODE_PRIVATE);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {

        }
        else {
            //showing alert dialog if there is no connectivity
            alertDialog("Error", "Sorry, your device doesn't connect to internet!");

        }


        progressBar2=(ProgressBar)findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.VISIBLE);
        progressBarForecast=(ProgressBar)findViewById(R.id.progressBarForeCast);
        progressBarForecast.setVisibility(View.VISIBLE);

        if (sharedpreferences.contains(MainActivity.City))
        {
            str=sharedpreferences.getString(MainActivity.City, "");
            ac.setText(sharedpreferences.getString(MainActivity.City, ""));
        }

        obj = new GetWeather.weatherTask();
        obj.delegate = this;
        obj.execute(str);
        obj2=new forecast.weatherTask();
        obj2.delegateforcast=this;
        obj2.execute(str);

    }



    @Override
    public void processFinish(double lat,double lon,String weather_city, String weather_condition, String weather_temperature, String weather_humidity, String weather_pressure,String lastUpdate,String weather_iconText,String speed,String tempmax,String tempmin,String sun_rise,String sun_set)
    {
        progressBar2.setVisibility(View.GONE);
        wind.setText(speed);
        temprange.setText(tempmin+"/"+tempmin);
        press.setText(weather_pressure);
        city.setText(weather_city);
        weather.setText(weather_condition);
        temper.setText(weather_temperature);
        humid.setText(weather_humidity);
        updatedOn.setText("Last Update On:"+lastUpdate);
        sunrise.setText("Sunrise: "+sun_rise);
        sunset.setText("Sunset:"+sun_set);
        final String src="http://openweathermap.org/img/w/"+weather_iconText+".png";
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... params)
            {
                try {
                    InputStream in = new URL(src).openStream();
                    bmp = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    // log error
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result)
            {
                if (bmp != null)
                    imageView.setImageBitmap(bmp);
            }

        }.execute();

    }

    @Override
    public void processFinishforecast(String condition,String temperature,String time,String weather_iconText)
    {
        progressBarForecast.setVisibility(View.GONE);

        TextView forecastcond,forecasttemp,forecasttime;
        if(flag==0)
        {
            forecastcond=(TextView)findViewById(R.id.forcast1);
            forecasttemp=(TextView)findViewById(R.id.forcast4);
            forecasttime=(TextView)findViewById(R.id.forcast7);
            forecasttemp.setText(temperature);
            forecastcond.setText(condition);
            forecasttime.setText(time);
            imageView1=(ImageView)findViewById(R.id.icon1);
            final String src="http://openweathermap.org/img/w/"+weather_iconText+".png";
            new AsyncTask<Void, Void, Void>()
            {
                @Override
                protected Void doInBackground(Void... params)
                {
                    try {
                        InputStream in = new URL(src).openStream();
                        bmp = BitmapFactory.decodeStream(in);
                    } catch (Exception e)
                    {
                        // log error
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    if (bmp != null)
                        imageView1.setImageBitmap(bmp);
                }

            }.execute();
        }
        if(flag==1)
        {
            forecastcond=(TextView)findViewById(R.id.forcast2);
            forecasttemp=(TextView)findViewById(R.id.forcast5) ;
            forecasttime=(TextView)findViewById(R.id.forcast8);
            forecasttemp.setText(temperature);
            forecastcond.setText(condition);
            forecasttime.setText(time);
            imageView2=(ImageView)findViewById(R.id.icon2);
            final String src="http://openweathermap.org/img/w/"+weather_iconText+".png";
            new AsyncTask<Void, Void, Void>()
            {
                @Override
                protected Void doInBackground(Void... params)
                {
                    try {
                        InputStream in = new URL(src).openStream();
                        bmp = BitmapFactory.decodeStream(in);
                    }
                    catch (Exception e)
                    {
                        // log error
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    if (bmp != null)
                        imageView2.setImageBitmap(bmp);
                }

            }.execute();

        }
        if(flag==2)
        {
            forecastcond=(TextView)findViewById(R.id.forcast3);
            forecasttemp=(TextView)findViewById(R.id.forcast6);
            forecasttime=(TextView)findViewById(R.id.forcast9);
            imageView3=(ImageView)findViewById(R.id.icon3);
            forecasttemp.setText(temperature);
            forecastcond.setText(condition);
            forecasttime.setText(time);

            final String src="http://openweathermap.org/img/w/"+weather_iconText+".png";
            new AsyncTask<Void, Void, Void>()
            {
                @Override
                protected Void doInBackground(Void... params)
                {
                    try {
                        InputStream in = new URL(src).openStream();
                        bmp = BitmapFactory.decodeStream(in);
                    } catch (Exception e) {
                        // log error
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result)
                {
                    if (bmp != null)
                        imageView3.setImageBitmap(bmp);
                }

            }.execute();
        }
        flag++;
    }

    public void alertDialog(String title , String message){

        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(Preference.this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alertDialog.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

    }
}
