package com.example.yeshu.weathercast.activity;

/**
 * Created by yeshu on 17-09-2016.
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

public class forecast
{
    static final String weather_url ="http://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric";
    static final String api_id = "025134d1ae0ad7f1d8fda20c8c965a46";

    public interface ResponseForecast //to pass the values from asynctask to processfinish
    {

        void processFinishforecast(String output1, String output2, String output3,String output4);
    }

    public static class weatherTask extends AsyncTask<String, Void, JSONObject> {

        public ResponseForecast delegateforcast = null; //as a listener

        @Override
        protected JSONObject doInBackground(String... params) {

            JSONObject jsonWeather =null;
            try {

                jsonWeather = access_api(params[0]);
            } catch (Exception e) {
                Log.d("Error", "Cannot process JSON results", e);
            }
            return jsonWeather;
        }
        String cityname="",condition="",temperature="",humidity="",pressure="",date="",icon="";
        @Override
        protected void onPostExecute(JSONObject jsonWeather) {
            try {
                if(jsonWeather != null)
                {
                    for(int i=0;i<3;i++) {
                        JSONObject weatherArray = jsonWeather.getJSONArray("list").getJSONObject(i);

                        JSONObject w = weatherArray.getJSONArray("weather").getJSONObject(0);
                        JSONObject main = weatherArray.getJSONObject("main");

                        condition= w.getString("description").toUpperCase(Locale.US);
                        icon=w.getString("icon");
                        temperature= String.format("%.2f", main.getDouble("temp")) + "°";//to covert to 2 decimal place
                        date= weatherArray.getString("dt_txt");

                        delegateforcast.processFinishforecast(condition,temperature,date,icon);
                    }
                }
            } catch (JSONException e)
            {
                return;
            }
        }
    }
    public static JSONObject access_api(String city){
        try
        {
            URL url = new URL(String.format(weather_url, city));
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
