package com.example.yeshu.weatherapp;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class WeatherActivity extends AppCompatActivity implements GetWeather.Response{

    TextView city, weather, temper, humid, press;

    GetWeather.weatherTask obj = new GetWeather.weatherTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        city = (TextView) findViewById(R.id.CityName);
        weather = (TextView) findViewById(R.id.WeatherCondition);
        temper = (TextView) findViewById(R.id.Temperature);
        humid= (TextView) findViewById(R.id.Humidity);
        press= (TextView) findViewById(R.id.Pressure);

        obj.delegate=this;//to revert back to this class otherwise it was showing app has stopped error

        obj.execute("28.6692", "77.4538");//to pass parameters to asynctask
    }
            public void processFinish(String weather_city, String weather_condition, String weather_temperature, String weather_humidity, String weather_pressure)
            {
                city.setText(weather_city);
                weather.setText(weather_condition);
                temper.setText(weather_temperature);
                humid.setText("Humidity: "+weather_humidity);
                press.setText("Pressure: "+weather_pressure);

            }
        }