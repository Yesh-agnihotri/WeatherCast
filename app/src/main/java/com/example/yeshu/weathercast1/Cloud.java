package com.example.yeshu.weathercast1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Lenovo on 17-Sep-16.
 */
public class Cloud extends AppCompatActivity  {
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cloud);
    }
    public void change(View v)
    {Intent i=new Intent(this,Vid.class);
        startActivity(i);

    }
}