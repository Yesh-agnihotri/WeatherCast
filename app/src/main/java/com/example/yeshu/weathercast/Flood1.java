package com.example.yeshu.weathercast1;

/**
 * Created by yeshu on 20-09-2016.
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Flood1 extends AppCompatActivity {
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flood);
    }
    public void change(View v)
    {Intent i=new Intent(this,Video.class);
        startActivity(i);

    }
}