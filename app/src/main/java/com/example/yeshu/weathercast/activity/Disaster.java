package com.example.yeshu.weathercast.activity;

import android.support.v7.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.yeshu.weathercast1.R;

import java.util.Random;
/**
 * Created by yeshu on 20-09-2016.
 */
public class Disaster extends AppCompatActivity {
    private Camera camera;
    ImageButton flashlightSwitchImg;
    private boolean isFlashlightOn;
    Parameters params;
    private Handler imageHandler = new Handler() {
    };
    //array containing drawables ids
    int[] myarray = new int[]{R.drawable.back,R.drawable.backe,R.drawable.backee,R.drawable.backeee};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disaster);

        getSupportActionBar().hide();
        imageHandler.post(handle);


        // flashlight on off Image
        flashlightSwitchImg = (ImageButton) findViewById(R.id.imageButton);

        boolean isCameraFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isCameraFlash) {
            showNoCameraAlert();
        } else {
            camera = Camera.open();
            params = camera.getParameters();
        }

        flashlightSwitchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFlashlightOn) {
                    setFlashlightOff();
                } else {
                    setFlashlightOn();
                }
            }
        });
    }
    private final Runnable handle = new Runnable(){
        public void run(){
            try {
                Random r = new Random();
                int i = r.nextInt(myarray.length);
                Disaster.this.getWindow().setBackgroundDrawableResource(myarray[i]);
                imageHandler.postDelayed(this,5000);
            }
            catch (Exception e) {
                Log.d("Test", e.toString());
            }
        }
    };



    private void showNoCameraAlert(){
        new AlertDialog.Builder(this)
                .setTitle("Error: No Camera Flash!")
                .setMessage("Camera flashlight not available in this Android device!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish(); // close the Android app
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        return;
    }
    private void setFlashlightOn() {
        params = camera.getParameters();
        params.setFlashMode(Parameters.FLASH_MODE_TORCH);
        camera.setParameters(params);
        camera.startPreview();
        isFlashlightOn = true;
        flashlightSwitchImg.setImageResource(R.drawable.on);
    }

    private void setFlashlightOff() {
        params.setFlashMode(Parameters.FLASH_MODE_OFF);
        camera.setParameters(params);
        camera.stopPreview();
        isFlashlightOn = false;
        flashlightSwitchImg.setImageResource(R.drawable.off);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
    public void change(View v)
    {
        Intent i=new Intent(this,Cloud.class);
        startActivity(i);

    }
    public void cha(View v)
    {
        Intent i=new Intent(this,Flood1.class);
        startActivity(i);

    }

}
