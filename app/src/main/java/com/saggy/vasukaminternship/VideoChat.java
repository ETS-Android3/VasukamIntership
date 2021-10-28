package com.saggy.vasukaminternship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class VideoChat extends AppCompatActivity implements Session.SessionListener , PublisherKit.PublisherListener {


    private static String API_KEY = "47351701";
    private static String SESSION_ID = "1_MX40NzM1MTcwMX5-MTYzMzU1NDAwNjk4MH4zWlNjZ3VHRjZLOXpZalg1MldVNGUvWGJ-fg";
    private static String TOKEN = "T1==cGFydG5lcl9pZD00NzM1MTcwMSZzaWc9Y2QzYjI3YTA0OTFhNGY0N2FlYjMzZmIzMjQ1OWFiZDk" +
            "xNDIxOTFiODpzZXNzaW9uX2lkPTFfTVg0ME56TTFNVGN3TVg1LU1UWXpNelUxTkRBd05qazRNSDR6V2xOalozVkhSalpMT1hwWmFsZz" +
            "FNbGRWTkdVdldHSi1mZyZjcmVhdGVfdGltZT0xNjMzNTU0MDg2Jm5vbmNlPTAuMjg3NDUyNzE5NTM3MTkwNTQmcm9sZT1wdWJsaXNoZXIm" +
            "ZXhwaXJlX3RpbWU9MTYzNjE0NjA4NSZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";
    //validity 30 days
    private static final int RC_VIDEO_APP_PERM = 124;

    private static final String LOG_TAG = VideoChat.class.getSimpleName();
    private Subscriber mSubscriber;
    private Publisher mPublisher;
    String userid;

    private Session mSession;
    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer;

    private DatabaseReference userRef;
    private ImageButton close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat);
        userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        userid= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        close = findViewById(R.id.close);
        requestPermissions();

        close.setOnClickListener(view -> {

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(userid).hasChild("Ringing")){
                        userRef.child(userid).child("Ringing")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()&&snapshot.hasChild("picked")){
                                            String   ringid=snapshot.child("picked").getValue().toString();
                                            userRef.child(ringid)
                                                    .child(("calling")).removeValue()
                                                    .addOnCompleteListener(task -> {
                                                        if(task.isSuccessful()){
                                                            userRef.child(userid).child("Ringing").removeValue();
                                                        }
                                                    });
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) { }
                                });
                        userRef.child(userid).child("Ringing").removeValue();
                        if(mPublisher!=null)
                            mPublisher.destroy();
                        if(mSubscriber!=null)
                            mSubscriber.destroy();
                        onBackPressed();
                    }
                    if(snapshot.child(userid).hasChild("calling")){
                        userRef.child(userid).child("calling")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()&&snapshot.hasChild("calling")){
                                            String  callingid=snapshot.child("calling").getValue().toString();
                                            userRef.child(callingid)
                                                    .child(("Ringing")).removeValue()
                                                    .addOnCompleteListener(task -> {
                                                        if(task.isSuccessful()){
                                                            userRef.child(userid).child("calling").removeValue();
                                                        }
                                                    });
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) { }
                                });
                        userRef.child(userid).child("calling").removeValue();
                        if(mPublisher!=null)
                            mPublisher.destroy();
                        if(mSubscriber!=null)
                            mSubscriber.destroy();
                        onBackPressed();
                    }
                    else{
                        if(mPublisher!=null)
                            mPublisher.destroy();
                        if(mSubscriber!=null)
                            mSubscriber.destroy();
                        onBackPressed();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
    });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
        String[] perms = { Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO };
        if (EasyPermissions.hasPermissions(this, perms)) {
            // initialize view objects from your layout

            mPublisherViewContainer = (FrameLayout) findViewById(R.id.publisher_container);
            mSubscriberViewContainer = (FrameLayout) findViewById(R.id.subscriber_container);

            // initialize and connect to the session
            mSession = new Session.Builder(this, API_KEY, SESSION_ID).build();
            mSession.setSessionListener(this);
            mSession.connect(TOKEN);
        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);
        }
    }

    //publisher listener's method
    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG, "Publisher onStreamCreated");
    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG, "Publisher onStreamDestroyed");
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
        Log.e(LOG_TAG, "Publisher error: " + opentokError.getMessage());
    }

    //Session Listener's method
    @Override
    public void onConnected(Session session) {
        Log.i(LOG_TAG, "Session Connected");
        mPublisher = new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(this);

        mPublisherViewContainer.addView(mPublisher.getView());

        if (mPublisher.getView() instanceof GLSurfaceView){
            ((GLSurfaceView) mPublisher.getView()).setZOrderOnTop(true);
        }

        mSession.publish(mPublisher);
    }

    @Override
    public void onDisconnected(Session session) {
        Log.i(LOG_TAG, "Session Disconnected");
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Received");
        if (mSubscriber == null) {
            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSession.subscribe(mSubscriber);
            mSubscriberViewContainer.addView(mSubscriber.getView());
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Dropped");
        if (mSubscriber != null) {
            mSubscriber = null;
            mSubscriberViewContainer.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "Session error: " + opentokError.getMessage());
    }
}




