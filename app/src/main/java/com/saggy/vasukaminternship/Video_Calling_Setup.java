package com.saggy.vasukaminternship;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ComponentName;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import com.saggy.vasukaminternship.utils.SinchServer;
import com.sinch.android.rtc.SinchError;

public class Video_Calling_Setup extends Base_Activity implements SinchServer.StartFailedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_calling_setup);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA
                    , Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE},100);
        }

        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient("userName");//need usernmae of current user
        } else {
            openPlaceCallActivity();
        }
    }

    private void openPlaceCallActivity() {

    }


    //when connection establish with sinch
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    public void onStartFailed(SinchError error) {

    }

    @Override
    public void onStarted() {
        //invoked when server started
        openPlaceCallActivity();
        //go to place activity
    }
}
