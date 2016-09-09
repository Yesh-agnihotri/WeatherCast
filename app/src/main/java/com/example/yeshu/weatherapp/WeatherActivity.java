package com.example.yeshu.weatherapp;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;



public class WeatherActivity extends AppCompatActivity implements GetWeather.Response,AdapterView.OnItemClickListener
{

    TextView city, weather, temper, humid, press;

    GetWeather.weatherTask obj = new GetWeather.weatherTask();

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";


    private static final String API_KEY ="AIzaSyDQh56saUf-nDW70sK5CzgeBB2vLWJg9-8";
    String str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));

        autoCompView.setOnItemClickListener(this);

        city = (TextView) findViewById(R.id.CityName);
        weather = (TextView) findViewById(R.id.WeatherCondition);
        temper = (TextView) findViewById(R.id.Temperature);
        humid= (TextView) findViewById(R.id.Humidity);
        press= (TextView) findViewById(R.id.Pressure);

        //to revert back to this class otherwise it was showing app has stopped error

        //to pass parameters to asynctask
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
    {
        String str1="";
        str = (String) adapterView.getItemAtPosition(position);
        str1="";
        for(int i=0;i<str.length();i++)
        {


            if (str.charAt(i)==',')
            {
                break;
            }
            else
            {
                str1=str1+str.charAt(i);
            }

            }
        obj.delegate=this;
        obj.execute(str1);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);

            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());


            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {

            return resultList;
        } catch (IOException e) {

            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {


            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");


            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("=");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {

        }

        return resultList;
    }


    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }


        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {

                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }

                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
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