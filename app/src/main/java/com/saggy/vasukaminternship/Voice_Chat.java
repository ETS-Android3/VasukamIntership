package com.saggy.vasukaminternship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.util.List;

public class Voice_Chat extends AppCompatActivity {
    private static final String APP_KEY = "4bb38533-8728-4c77-a455-4acf2c729447";
    private static final String APP_SECRET = "RZNvr3U6mk2o/dwLuCrcfQ==";
    private static final String ENVIRONMENT = "clientapi.sinch.com";

    private Call call;
    private TextView username;
    private SinchClient sinchClient;
    private ImageButton cancel_call;
    private String callerId;
    private String recipientId;

    private DatabaseReference userRef ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_chat);

        cancel_call = findViewById(R.id.close);
        username = findViewById(R.id.video_username);

        userRef= FirebaseDatabase.getInstance().getReference().child("user1");

        Intent intent = getIntent();
        callerId = intent.getStringExtra("callerId");
        recipientId = intent.getStringExtra("recipientId");

        sinchClient = Sinch.getSinchClientBuilder()
                .context(Voice_Chat.this)
                .userId(recipientId)
                .applicationKey(APP_KEY)
                .environmentHost(ENVIRONMENT)
                .build();

        //sinchClient.setSupportCalling(true);

        sinchClient.addSinchClientListener(new SinchClientListener() {
            @Override
            public void onClientStarted(SinchClient sinchClient) {
            }
            @Override
            public void onClientFailed(SinchClient sinchClient, SinchError sinchError) {
            }
            @Override
            public void onLogMessage(int i, String s, String s1) {
            }
            @Override
            public void onPushTokenRegistered() {
            }
            @Override
            public void onPushTokenRegistrationFailed(SinchError sinchError) {
            }
            @Override
            public void onCredentialsRequired(ClientRegistration clientRegistration) {
                //need to implement
            }
            @Override
            public void onUserRegistered() {
            }
            @Override
            public void onUserRegistrationFailed(SinchError sinchError) {
            }
        });

        sinchClient.startListeningOnActiveConnection();
        sinchClient.start();

        sinchClient.getCallClient().addCallClientListener(new Voice_Chat.SinchCallClientListener());

        cancel_call.setOnClickListener(view -> {
            call.hangup();
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Ringing").removeValue();
                    userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("calling").removeValue();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            onBackPressed();
        });


        if (call == null) {
            call = sinchClient.getCallClient().callUser(recipientId);
            call.addCallListener((CallListener) new SinchCallListener());
            //button.setText("Hang Up");
        } else {
            call.hangup();
        }
    }

    private class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {
            call = null;
            SinchError a = endedCall.getDetails().getError();
            //  button.setText("Call");
            username.setText("");
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
        }

        @Override
        public void onCallEstablished(Call establishedCall) {
            username.setText("connected");
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        }

        @Override
        public void onCallProgressing(Call progressingCall) {
            username.setText("ringing");
        }
    }

    private class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
            call = incomingCall;
            Toast.makeText(Voice_Chat.this, "incoming call", Toast.LENGTH_SHORT).show();
            call.answer();
            call.addCallListener(new SinchCallListener());
            // button.setText("Hang Up");
        }
    }
}
