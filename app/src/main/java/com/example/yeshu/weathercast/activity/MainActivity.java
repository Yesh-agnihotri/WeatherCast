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

import com.example.yeshu.weathercast.utils.GPSTracker;
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

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


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

public class MainActivity extends AppCompatActivity implements GetWeatherMap.ResponseMap, AdapterView.OnItemClickListener, GetWeather.Response, forecast.ResponseForecast {

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
    ConnectivityManager connectivityManager;
    GetWeatherMap.MapWeatherTask obj1;
    GPSTracker gps;
    FloatingActionButton menu1,menu2,menu3,menu4,menu5 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);




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
                city();
                result1 = result;


            } else {


                Toast.makeText(this,"Location not available",Toast.LENGTH_LONG).show();


            }
        }
        else
        {gps.showSettingsAlert();

        }

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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





    }
    public void change(View v) {
        Intent intent=new Intent(getApplicationContext(),MapsActivity.class);
        startActivity(intent);

        Toast.makeText(MainActivity.this , "Map view", Toast.LENGTH_LONG).show();

    }
    public void chang(View v) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Weather App");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Now Share this exclusive weather app http//googleplaystroe.com");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));

        Toast.makeText(MainActivity.this , "Share App", Toast.LENGTH_LONG).show();


    }

    public void cha(View v) {
        Intent i =new Intent(getApplicationContext(),Disaster.class);
        startActivity(i);

        Toast.makeText(MainActivity.this , "Disaster", Toast.LENGTH_LONG).show();




    }
    public void ch(View v) {
        Intent i =new Intent(this,Preference.class);
        startActivity(i);

        Toast.makeText(MainActivity.this , "Last search", Toast.LENGTH_LONG).show();




    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();

        String  lai=String.format("%f",latitude);
        String  lni=String.format("%f",longitude);
        Toast.makeText(this,lai,Toast.LENGTH_LONG).show();
        obj1=new GetWeatherMap.MapWeatherTask();
        obj1.delegateMap=this;
        Toast.makeText(this,lai,Toast.LENGTH_LONG).show();
        obj1.execute(lai,lni);
        city();
        result1=result;

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



    public  ArrayList<String> autocomplete(String input)
    {
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
        }
        else
        {
            alertDialog("Error", "Sorry, your device doesn't connect to internet!");
        }
        progressBar.setVisibility(View.VISIBLE);
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
    {     progressBar.setVisibility(View.GONE);

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
    { connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
        }
        else
        {
            alertDialog("Error", "Sorry, your device doesn't connect to internet!");
        }
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
    String weath;
    String cond;
    @TargetApi(Build.VERSION_CODES.M)
    public void processFinish(double lat, String weather_city, String weather_condition, String weather_temperature, String weather_humidity, String weather_pressure, String lastUpdate, String weather_iconText, String speed, String tempmax, String tempmin, String sun_rise, String sun_set)
    {
        progressBar.setVisibility(View.GONE);
        this.weath=weather_city;
        this.cond=weather_condition;
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


        showNotification();


    }
    private static final int notificationId=1;
    public void showNotification() { // Use NotificationCompat.Builder to set up our notification.
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
        notificationManager.notify( notificationId, builder.build());

    }



    public void chan(View v) {
        Intent i =new Intent(getApplicationContext(),Notify.class);



        startActivity(i);

        Toast.makeText(MainActivity.this , "Setting", Toast.LENGTH_LONG).show();


    }


    //method for alert Dialog
    public void alertDialog(String title , String message){

        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_WIFI_SETTINGS);
                        startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

    }


}
