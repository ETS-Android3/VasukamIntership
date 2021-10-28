package com.saggy.vasukaminternship;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.InCallService;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Video_Call extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username_text;
    ImageButton cancel_call, make_call;

    String callingid="",ringid="", receiverUserid="" ,receiverUserimage="null",receiverUsername;
    String senderUserid="",senderUserimage="",checker="";
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_call_activity);

        profile_image = findViewById(R.id.image);
        username_text = findViewById(R.id.video_username);
        cancel_call = findViewById(R.id.cancel_video);
        make_call = findViewById(R.id.make_call);

        receiverUserid = getIntent().getExtras().getString("uid");
//        receiverUsername = getIntent().getExtras().getString("username");

        senderUserid= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

//        username_text.setText(receiverUsername);
        userRef= FirebaseDatabase.getInstance().getReference().child("Users");


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(senderUserid).hasChild("Ringing")&&!snapshot.child(senderUserid).hasChild("calling")){
                    make_call.setVisibility(View.VISIBLE);
                }
                if(snapshot.child(receiverUserid).child("Ringing").hasChild("picked")){
                    Intent intent=new Intent(Video_Call.this,VideoChat.class);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(receiverUserid).exists()){
                    //if(snapshot.child(receiverUserid).child("image").exists())  receiverUserimage=snapshot.child(receiverUserid).child("image").getValue().toString();
                    receiverUsername= Objects.requireNonNull(snapshot.child(receiverUserid).child("username").getValue()).toString();
                    username_text.setText(receiverUsername);
                    //  Picasso.get().load(rec).placeholder(R.drawable.profile).into(messageViewHolder.receiverProfileImage);
                    //if(receiverUserimage!=null)  Picasso.get().load(receiverUserimage).placeholder(R.drawable.profile).into(profilcimage);
                }
//                if(snapshot.child(senderUserid).exists()){
//                    if(snapshot.child(senderUserid).child("image").exists()) senderUserimage=snapshot.child(senderUserid).child("image").getValue().toString();
//                    senderrUsername=snapshot.child(senderUserid).child("phone").getValue().toString();
//                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        cancel_call.setOnClickListener(view -> {
            checker="clicked";
            Toast.makeText(getApplication(), "data"+checker, Toast.LENGTH_SHORT).show();
            hello();
        });

        make_call.setOnClickListener(view -> {
            final HashMap<String,Object> calling_pickup_map = new HashMap<>();
            calling_pickup_map.put("picked","picked");
            userRef.child(senderUserid).child("Ringing").updateChildren(calling_pickup_map).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Intent intent=new Intent(Video_Call.this,VideoChat.class);
                    startActivity(intent);
                    finish();
                }
            });
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        userRef.child(receiverUserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(checker.equals("clicked") || snapshot.hasChild("calling") || snapshot.hasChild("Ringing"))){
                    //receiver not calling anyone and checker is not clicked
                    userRef.child(senderUserid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!(checker.equals("clicked") || snapshot.hasChild("calling") || snapshot.hasChild("Ringing"))){
                                // user also don't calling previously
                                final HashMap<String,Object> callingInfo=new HashMap<>();
                                callingInfo.put("calling",receiverUserid);
                                userRef.child(senderUserid).child("calling").updateChildren(callingInfo).addOnCompleteListener(task -> {
                                    if(task.isSuccessful()){
                                        final HashMap<String,Object> ringingInfo = new HashMap<>();
                                        ringingInfo.put("ringing",senderUserid);
                                        userRef.child(receiverUserid).child("Ringing").updateChildren(ringingInfo);
                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


        //when user come if other user calling this user
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(senderUserid).hasChild("Ringing") && !snapshot.child(senderUserid).hasChild("calling")){
                    make_call.setVisibility(View.VISIBLE);
                }
                if(snapshot.child(receiverUserid).child("Ringing").hasChild("picked")){
                    Intent intent=new Intent(Video_Call.this,VideoChat.class);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void  hello(){
        userRef.child(senderUserid).child("calling")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()&&snapshot.hasChild("calling")){
                            callingid = Objects.requireNonNull(snapshot.child("calling").getValue()).toString();
                            userRef.child(callingid).child(("Ringing")).removeValue()
                                    .addOnCompleteListener(task -> {
                                        if(task.isSuccessful()){
                                            userRef.child(senderUserid).child("calling").removeValue()
                                                    .addOnCompleteListener(task1 -> onBackPressed());
                                        }
                                    });
                        }
                        else
                           onBackPressed();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
        userRef.child(senderUserid).child("Ringing")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()&&snapshot.hasChild("ringing")){
                            ringid= Objects.requireNonNull(snapshot.child("ringing").getValue()).toString();
                            userRef.child(ringid)
                                    .child(("calling")).removeValue()
                                    .addOnCompleteListener(task -> {
                                        if(task.isSuccessful()){
                                            userRef.child(senderUserid).child("Ringing").removeValue()
                                                    .addOnCompleteListener(task12 -> onBackPressed());
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