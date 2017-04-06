package com.example.yeshu.weathercast.activity;

/**
 * Created by yeshu on 20-09-2016.
 */
import android.app.Activity;
        import android.net.Uri;
        import android.os.Bundle;
        import android.widget.MediaController;
        import android.widget.VideoView;

import com.example.yeshu.weathercast1.R;

/**
 * Created by Lenovo on 20-Sep-16.
 */
public class Vid extends Activity {
    private static final String TAG = "PRANJAL";
    private boolean isImage = false;
    private String reviewImageLink;
    private MediaController mc;
    VideoView vd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vide);
        vd=(VideoView)findViewById(R.id.videoView2);
        Uri u=Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.cloud);
        mc=new MediaController(this);
        vd.setMediaController(mc);
        vd.setVideoURI(u);
        vd.start();

    }
}

