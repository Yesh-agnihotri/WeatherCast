package com.example.yeshu.weatherapp;
/**
 * Created by yeshu on 03-09-2016.
 */
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class GetWeather
{
    static final String weather_url ="http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric";
    static final String api_id = "025134d1ae0ad7f1d8fda20c8c965a46";

    public interface Response //to pass the values from asynctask to processfinish
    {

        void processFinish(String output1, String output2, String output3, String output4, String output5);
    }

    public static class weatherTask extends AsyncTask<String, Void, JSONObject> {

        public Response delegate = null; //as a listener

        @Override
        protected JSONObject doInBackground(String... params) {

            JSONObject jsonWeather = null;
            try {
                jsonWeather = access_api(params[0], params[1]);
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
                    String cityname = jsonWeather.getString("name")+ ", " + jsonWeather.getJSONObject("sys").getString("country");
                    String condition = weatherArray.getString("description").toUpperCase(Locale.US);
                    String temperature = String.format("%.2f", main.getDouble("temp"))+ "°";//to covert to 2 decimal place
                    String humidity = main.getString("humidity") + "%";
                    String pressure = main.getString("pressure") + " hPa";

                    delegate.processFinish(cityname, condition, temperature, humidity, pressure);

                }
            } catch (JSONException e)
            {
                return;
            }
        }
    }
    public static JSONObject access_api(String lat, String lon){
        try
        {
            URL url = new URL(String.format(weather_url, lat, lon));
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
