package com.example.yeshu.weathercast1;

/**
 * Created by yeshu on 20-09-2016.
 */
import android.app.Activity;
        import android.net.Uri;
        import android.os.Bundle;
        import android.widget.MediaController;
        import android.widget.VideoView;

/**
 * Created by Lenovo on 20-Sep-16.
 */
public class Video extends Activity {
    private static final String TAG = "PRANJAL";
    private boolean isImage = false;
    private String reviewImageLink;
    private MediaController mc;
    VideoView vd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video);
        vd=(VideoView)findViewById(R.id.videoView);
        Uri u=Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.flood);
        mc=new MediaController(this);
        vd.setMediaController(mc);
        vd.setVideoURI(u);
        vd.start();

    }
}
