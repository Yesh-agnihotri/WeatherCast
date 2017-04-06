package com.example.yeshu.weathercast.activity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yeshu.weathercast1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener, GetWeatherMap.ResponseMap, AdapterView.OnItemClickListener, GetWeather.Response, forecast.ResponseForecast {

    forecast.weatherTask obj2;
    ProgressBar progressBar, progressBarForecast;
    double lat, lng;
    private Bitmap bmp;
    TextView city, weather, temper, humid, press, updatedOn, sunrise, sunset, wind, temprange;
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    public static final String mypreference = "mypref";
    public static final String City = "cityKey";
    private static final String API_KEY = "AIzaSyDQh56saUf-nDW70sK5CzgeBB2vLWJg9-8";
    String str;
    ImageView imageView;
    private LocationManager locationManager;
    private String provider;
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this,"Location not available. Enter it manually",Toast.LENGTH_LONG).show();
        }
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null)
        {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
        }
        else
        {
            alertDialog("Error", "Sorry, your device doesn't connect to internet!");
        }



        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        GetWeatherMap.MapWeatherTask obj1;
        obj1=new GetWeatherMap.MapWeatherTask();
        obj1.delegateMap=this;
        Toast.makeText(this,la,Toast.LENGTH_LONG).show();
        obj1.execute(la,ln);

        obj2=new forecast.weatherTask();
        obj2.delegateforcast=this;
        obj2.execute(result1);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

       progressBar=(ProgressBar)findViewById(R.id.progressBar);
       progressBar.setVisibility(View.VISIBLE);
        progressBarForecast=(ProgressBar)findViewById(R.id.progressBarForeCast);
        progressBarForecast.setVisibility(View.VISIBLE);
        AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));

        autoCompView.setOnItemClickListener(this);

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


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
               Intent intent=new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton prefence = (FloatingActionButton) findViewById(R.id.preference);
        prefence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getApplicationContext(),Preference.class);
                startActivity(intent);
            }
        });
        FloatingActionButton fabshare = (FloatingActionButton) findViewById(R.id.fabshare);
        fabshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Weather App");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Now Share this exclusive weather app http//googleplaystroe.com");
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
        FloatingActionButton fabdisaster = (FloatingActionButton) findViewById(R.id.fabdisaster);
        fabdisaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getApplicationContext(),Disaster.class);
                startActivity(intent);

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
    {
        String str1="";
        str="";
        str =(String) adapterView.getItemAtPosition(position);

        for(int i=0;i<str.length();i++)
        {

            if (str.charAt(i)==',')
            {
                break;
            }
            if(str.charAt(i)!=' ')
            {
                str1=str1+str.charAt(i);
            }
        }
         flag=0;
        GetWeather.weatherTask obj = new GetWeather.weatherTask();
        obj.delegate=this;
        obj.execute(str1);
        forecast.weatherTask obj1=new forecast.weatherTask();
        obj1.delegateforcast=this;
        obj1.execute(str1);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(City, str1);
        editor.apply();

        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
    public static ArrayList<String> autocomplete(String input)
    {
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
        } catch (IOException e)
        {

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

    String la,ln;
    @Override
    public void onLocationChanged(Location location)
    {
        lat = location.getLatitude();
        lng = location.getLongitude();
        la=String.format("%f",lat);
        ln=String.format("%f",lng);
        city();
        result1=result;
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


    public String result;
    public String result1;
    public void city()
    {
        Geocoder gcd =new Geocoder(this,Locale.getDefault());
        List<Address>addresses=null;
        try
        {
            addresses=gcd.getFromLocation(lat,lng,1);

        }
        catch (IOException e)
        {
        }
            Toast.makeText(this,result,Toast.LENGTH_LONG).show();
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

    public void processFinish(double lat,double lon,String weather_city, String weather_condition, String weather_temperature, String weather_humidity, String weather_pressure,String lastUpdate,String weather_iconText,String speed,String tempmax,String tempmin,String sun_rise,String sun_set)
    {

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
            protected void onPostExecute(Void result) {
                if (bmp != null)
                    imageView.setImageBitmap(bmp);
            }

        }.execute();

    }
    int flag=0;
    ImageView imageView1,imageView2,imageView3;
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
                protected void onPostExecute(Void result) {
                    if (bmp != null)
                        imageView3.setImageBitmap(bmp);
                }

            }.execute();
        }
        flag++;
        }
    PendingIntent pIntent;
    Intent intent;
    public void processFinish(double lat,String weather_city, String weather_condition, String weather_temperature, String weather_humidity, String weather_pressure,String lastUpdate,String weather_iconText,String speed,String tempmax,String tempmin,String sun_rise,String sun_set)
    {
        progressBar.setVisibility(View.GONE);
        String weath=weather_city;
        String cond=weather_condition;
        wind.setText(speed);
        temprange.setText(tempmin+"/"+tempmax);
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
                try
                {
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
            protected void onPostExecute(Void result)
            {
                if (bmp != null)
                    imageView.setImageBitmap(bmp);
            }

        }.execute();

        Notification noti = new Notification.Builder(this)
                .setContentTitle(weath+cond)
                .setContentText(weather_condition).setSmallIcon(R.drawable.not_icon).build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);
    }

    //method for alert Dialog
    public void alertDialog(String title , String message){

        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(MainActivity.this);
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
