package com.saggy.vasukaminternship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Voice_Call extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username_text;
    ImageButton cancel_call, make_call;

    private  String callingid="", ringid="", receiverUserid="", receiverUserimage="null", receiverUsername="";
    private  String senderUserid="", senderUserimage="", senderrUsername="", checker="";
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_call);

        profile_image = findViewById(R.id.image);
        username_text = findViewById(R.id.video_username);
        cancel_call = findViewById(R.id.cancel_video);
        make_call = findViewById(R.id.make_call);

        receiverUserid = getIntent().getExtras().get("uid").toString();
        userRef= FirebaseDatabase.getInstance().getReference().child("user1");
        senderUserid= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        cancel_call.setOnClickListener(view -> {
            checker="clicked";
            Toast.makeText(getApplication(), "data"+checker, Toast.LENGTH_SHORT).show();
            hello();
        });

        make_call.setOnClickListener(view -> {
            final HashMap<String,Object> callingpickupmap=new HashMap<>();
            callingpickupmap.put("picked","picked");
            userRef.child(senderUserid).child("Ringing").updateChildren(callingpickupmap).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Intent intent=new Intent(Voice_Call.this,Voice_Chat.class);
                    intent.putExtra("callerId", senderUserid);
                    intent.putExtra("recipientId",receiverUserid);
                    startActivity(intent);
                    finish();
                }
            });
        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(senderUserid).hasChild("Ringing")&&!snapshot.child(senderUserid).hasChild("calling")){
                    make_call.setVisibility(View.VISIBLE);
                }
                if(snapshot.child(receiverUserid).child("Ringing").hasChild("picked")){
                    ///   mediaPlayer.stop();
                    Intent intent=new Intent(Voice_Call.this, Voice_Chat.class);
                    intent.putExtra("callerId", senderUserid);
                    intent.putExtra("recipientId", receiverUserid);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(receiverUserid).exists()){
                    //if(snapshot.child(receiverUserid).child("image").exists())  receiverUserimage=snapshot.child(receiverUserid).child("image").getValue().toString();
                    receiverUsername= Objects.requireNonNull(snapshot.child(receiverUserid).child("username").getValue()).toString();
                    username_text.setText(receiverUsername);
                    //  Picasso.get().load(rec).placeholder(R.drawable.profile).into(messageViewHolder.receiverProfileImage);
                    //if(receiverUserimage!=null)  Picasso.get().load(receiverUserimage).placeholder(R.drawable.profile).into(profilcimage);
                }
                if(snapshot.child(senderUserid).exists()){
                    //if(snapshot.child(senderUserid).child("image").exists()) senderUserimage=snapshot.child(senderUserid).child("image").getValue().toString();
                    senderrUsername= Objects.requireNonNull(snapshot.child(senderUserid).child("username").getValue()).toString();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    @Override
    protected void onStart() {
        if (ContextCompat.checkSelfPermission(Voice_Call.this,
                android.Manifest.permission.RECORD_AUDIO) !=
                PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission
                (Voice_Call.this, android.Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(Voice_Call.this,
                    new String[]{android.Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE},
                    1);
        }
        super.onStart();
        userRef.child(receiverUserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(checker.equals("clicked") || snapshot.hasChild("calling") || snapshot.hasChild("Ringing"))){
                    userRef.child(senderUserid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!(checker.equals("clicked") || snapshot.hasChild("calling") || snapshot.hasChild("Ringing"))){
                                final HashMap<String,Object> callingInfo=new HashMap<>();
                                callingInfo.put("calling",receiverUserid);
                                userRef.child(senderUserid).child("calling").updateChildren(callingInfo).addOnCompleteListener(task -> {
                                    if(task.isSuccessful()){
                                        final HashMap<String,Object>ringingInfo=new HashMap<>();
                                        ringingInfo.put("ringing",senderUserid);
                                        userRef.child(receiverUserid).child("Ringing").updateChildren(ringingInfo);
                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(senderUserid).hasChild("Ringing")&&!snapshot.child(senderUserid).hasChild("calling")){
                    make_call.setVisibility(View.VISIBLE);
                }
                if(snapshot.child(receiverUserid).child("Ringing").hasChild("picked")){
                    Intent intent=new Intent(Voice_Call.this, Voice_Chat.class);
                    intent.putExtra("callerId", senderUserid);
                    intent.putExtra("recipientId", receiverUserid);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public void  hello(){
        userRef.child(senderUserid).child("calling")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()&&snapshot.hasChild("calling")){
                            callingid=snapshot.child("calling").getValue().toString();
                            userRef.child(callingid)
                                    .child(("Ringing")).removeValue()
                                    .addOnCompleteListener(task -> {
                                        if(task.isSuccessful()){
                                            userRef.child(senderUserid).child("calling").removeValue()
                                                    .addOnCompleteListener(task1 -> {
                                                        onBackPressed();
                                                    });
                                        }
                                    });
                        }
                        else
                           onBackPressed();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        userRef.child(senderUserid).child("Ringing")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()&&snapshot.hasChild("ringing")){
                            ringid=snapshot.child("ringing").getValue().toString();
                            userRef.child(ringid)
                                    .child(("calling")).removeValue()
                                    .addOnCompleteListener(task -> {
                                        if(task.isSuccessful()){
                                            userRef.child(senderUserid).child("Ringing").removeValue()
                                                    .addOnCompleteListener(task12 -> {
                                                        onBackPressed();
                                                    });
                                        }
                                    });
                        }
                        else
                            onBackPressed();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }
}

