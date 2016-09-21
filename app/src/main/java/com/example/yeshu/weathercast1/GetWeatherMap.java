package com.example.yeshu.weathercast1;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yeshu on 14-09-2016.
 */
public class GetWeatherMap
{

    static final String weather_url ="http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric";
    static final String api_id = "025134d1ae0ad7f1d8fda20c8c965a46";

    public interface ResponseMap //to pass the values from asynctask to processfinish
    {
        void processFinish(double output0,String output1, String output2, String output3, String output4, String output5, String output6, String output7, String output8, String output9, String output10, String output11, String output12);
    }

    public static class MapWeatherTask extends AsyncTask<String, Void, JSONObject>
    {
        public ResponseMap delegateMap = null; //as a listener

        @Override
        protected JSONObject doInBackground(String... params) {

            JSONObject jsonWeather = null;
            try
            {
                jsonWeather = access_api(params[0],params[1]);
            } catch (Exception e) {
                Log.d("Error", "Cannot process JSON results", e);
            }
            return jsonWeather;
        }

        @Override
        protected void onPostExecute(JSONObject jsonWeather) {
            try {
                if(jsonWeather != null)

                {

                    JSONObject weatherArray = jsonWeather.getJSONArray("weather").getJSONObject(0);//as the jason is in array form
                    JSONObject main = jsonWeather.getJSONObject("main");
                    DateFormat df = DateFormat.getDateTimeInstance();
                    JSONObject wind=jsonWeather.getJSONObject("wind");

                    String speed=wind.getString("speed")+"km/h";
                    String temp_max=main.getString("temp_max");
                    String temp_min=main.getString("temp_min");
                    String cityname = jsonWeather.getString("name")+ ", " + jsonWeather.getJSONObject("sys").getString("country");
                    String condition = weatherArray.getString("description").toUpperCase(Locale.US);
                    String temperature = String.format("%.2f", main.getDouble("temp"))+ "°";//to covert to 2 decimal place
                    String humidity = main.getString("humidity") + "%";
                    String pressure = main.getString("pressure") + " hPa";
                    String lastUpdate = df.format(new Date(jsonWeather.getLong("dt")*1000));
                    String iconText=weatherArray.getString("icon");
                    Long Sunrise=jsonWeather.getJSONObject("sys").getLong("sunrise")*1000;
                    Long Sunset=jsonWeather.getJSONObject("sys").getLong("sunset")*1000;
                    String sun_rise=df.format(new Date(Sunrise));
                    String sun_set=df.format(new Date(Sunset));
                    JSONObject pos=jsonWeather.getJSONObject("coord");
                    double lat=pos.getDouble("lat");
                    double lon=pos.getDouble("lon");
                    delegateMap.processFinish(lat,cityname,condition, temperature, humidity, pressure,lastUpdate,iconText,speed,temp_max,temp_min,sun_rise,sun_set);

                }
            }
            catch (JSONException e)
            {
                return;
            }
        }

    }
    public static JSONObject access_api(String lat,String lon){
        try
        {
            URL url = new URL(String.format(weather_url,lat,lon));
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            conn.addRequestProperty("x-api-key", api_id);//add my api key to url

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));//to get input from url
            StringBuffer json = new StringBuffer(1024);
            String readData="";
            while((readData=reader.readLine())!=null)
                json.append(readData).append("\n");
            reader.close();
            JSONObject data = new JSONObject(json.toString());
            if(data.getInt("cod") == 200)//an internal parameter for this weather jason
            {
                return data;
            }
            else
            {
                return null;
            }

        }
        catch(Exception e)
        {
            return null;
        }
    }
}
