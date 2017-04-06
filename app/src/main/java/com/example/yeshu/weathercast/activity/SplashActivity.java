package com.example.yeshu.weathercast.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yeshu.weathercast.activity.MainActivity;
import com.example.yeshu.weathercast1.R;
import com.example.yeshu.weathercast1.utils.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by simran on 4/6/2017.
 */

public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener{
    @BindView(R.id.cloud)
    ImageView cloud;
    @BindView(R.id.sun)
    ImageView sun;
    @BindView(R.id.weathercast)
    TextView weatherCast;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        ButterKnife.bind(this);
        if (NetworkUtils.isGooglePlayServicesAvailable()) {
            if (NetworkUtils.isNetConnected()) {

                Animation animation = AnimationUtils.loadAnimation(this, R.anim.boatanim);
                animation.setFillAfter(true);
                animation.setAnimationListener(this);
                cloud.startAnimation(animation);
            } else {
                alertDialog("Error", "Sorry, your device isn't connected to internet");
            }
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Please install google play services in order to proceed...");
            alertDialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            onDestroy();
                        }
                    });
            alertDialog.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
        }

    }
    public void alertDialog(String title, String message) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
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
                        onDestroy();
                    }
                });
        alertDialog.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        AlphaAnimation fadeOut = new AlphaAnimation(0.0f , 1.0f ) ;
        AlphaAnimation fadeIn = new AlphaAnimation( 1.0f , 0.0f ) ;
        sun.startAnimation(fadeIn);
        weatherCast.startAnimation(fadeIn);
        sun.startAnimation(fadeOut);
        weatherCast.startAnimation(fadeOut);
        fadeIn.setDuration(500);
        fadeIn.setFillAfter(true);
        fadeOut.setDuration(1000);
        fadeOut.setFillAfter(true);
        fadeOut.setStartOffset(500+fadeIn.getStartOffset());
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
}
