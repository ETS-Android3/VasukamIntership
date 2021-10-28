package com.saggy.vasukaminternship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.saggy.vasukaminternship.utils.SinchServer;

public class Base_Activity extends AppCompatActivity implements ServiceConnection {

    private SinchServer.SinchServiceInterface mSinchServiceInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationContext().bindService(new Intent(this, SinchServer.class), this,
                BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (SinchServer.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = (SinchServer.SinchServiceInterface) iBinder;
            onServiceConnected();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        if (SinchServer.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = null;
            onServiceDisconnected();
        }
    }

    protected void onServiceConnected() {
        // for subclasses
    }

    protected void onServiceDisconnected() {
        // for subclasses
    }

    protected SinchServer.SinchServiceInterface getSinchServiceInterface() {
        return mSinchServiceInterface;
    }
}




//    androidTestImplementation 'androidx.test.ext:junit:1.1.1'
//            androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'
//            implementation 'com.google.firebase:firebase-core:17.2.1'
//            implementation 'id.zelory:compressor:2.0.0'
//            implementation 'com.google.firebase:firebase-messaging:20.0.1'
//            implementation 'androidx.multidex:multidex:2.0.1'
//            implementation 'com.google.firebase:firebase-auth:19.2.0'
//            implementation 'com.firebaseui:firebase-ui-database:1.2.0'
//            implementation 'com.squareup.picasso:picasso:2.71828'
//            implementation 'com.theartofdev.edmodo:android-image-cropper:2.5.1'
//            implementation 'com.google.android.material:material:1.0.0'
//            implementation 'com.microsoft.projectoxford:vision:1.0.394'
//            implementation 'com.google.android.gms:play-services-ads:18.3.0'
//            implementation 'com.google.firebase:firebase-analytics:17.2.1'