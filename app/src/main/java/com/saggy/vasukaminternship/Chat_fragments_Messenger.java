package com.saggy.vasukaminternship;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saggy.vasukaminternship.adapters.Chat_Contact_Adapter;
import com.saggy.vasukaminternship.models.ProfileInfo;


import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class Chat_fragments_Messenger extends Fragment {
    View view;
    public Messenger_Activity activity;

    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    String currentUserId;
    private Chat_Contact_Adapter chat_contact_adapter;
    List<ProfileInfo> resultlist;
    List<ProfileInfo> chat_user_list;
    int temp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        resultlist = (List<ProfileInfo>) bundle.getSerializable("result");
        chat_user_list = (List<ProfileInfo>) bundle.getSerializable("chat");
        temp = bundle.getInt("val");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chat_messenger_activity, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        recyclerView = view.findViewById(R.id.chat_messages_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 0 for chat
        // 1 for search for request user
        if(temp == 0){
            //chat
            chat_contact_adapter  = new Chat_Contact_Adapter(getContext(), chat_user_list, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    ProfileInfo pos = chat_user_list.get(position);
//                // Toast.makeText(getApplicationContext(),  pos.getUid()+ " is selected!", Toast.LENGTH_SHORT).show();
                    if (!currentUserId.equals(pos.getUid())) {
                        Intent intent = new Intent(getContext(), Chat_Activity.class);
                        intent.putExtra("uid", pos.getUid());
                        intent.putExtra("name", pos.getName());
                        intent.putExtra("username", pos.getUsername());
                        intent.putExtra("phoneNumber", pos.getPhoneNumber());
                        intent.putExtra("val" , 0);
                        startActivity(intent);
                        chat_contact_adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "your click your account", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onLongClick(View view, int position) {
                }
            });
            recyclerView.setAdapter(chat_contact_adapter);

        }
        else{
           //search
            chat_contact_adapter  = new Chat_Contact_Adapter(getContext(), resultlist, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    ProfileInfo pos = resultlist.get(position);
                    int flag =1;
                    //check whether this profile is new to user or not
                    for(ProfileInfo profileInfo: chat_user_list){
                        if(profileInfo.getUid().equals(pos.getUid())){
                            flag = 0;
                            break;
                        }
                    }
                    if(flag == 0){
                        //chat
                        if (!currentUserId.equals(pos.getUid())) {
                            Intent intent = new Intent(getContext(), Chat_Activity.class);
                            intent.putExtra("uid", pos.getUid());
                            intent.putExtra("name", pos.getName());
                            intent.putExtra("username", pos.getUsername());
                            intent.putExtra("phoneNumber", pos.getPhoneNumber());
                            intent.putExtra("val",0);
                            startActivity(intent);
                            chat_contact_adapter.notifyDataSetChanged();
                        } else
                            Toast.makeText(getContext(), "your click your account", Toast.LENGTH_LONG).show();
                    }
                    else{
                        // new user
                        if (!currentUserId.equals(pos.getUid())) {
                            Intent intent = new Intent(getContext(), Chat_Activity.class);
                            intent.putExtra("uid", pos.getUid());
                            intent.putExtra("name", pos.getName());
                            intent.putExtra("username", pos.getUsername());
                            intent.putExtra("phoneNumber", pos.getPhoneNumber());
                            intent.putExtra("val",1);
                            startActivity(intent);
                            chat_contact_adapter.notifyDataSetChanged();
                        } else
                            Toast.makeText(getContext(), "your click your account", Toast.LENGTH_LONG).show();
                    }

                }
                @Override
                public void onLongClick(View view, int position) {
                }
            });
            recyclerView.setAdapter(chat_contact_adapter);
        }



        //video call
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userRef.child(currentUserId).child("Ringing").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("ringing")) {
                    String valuer = snapshot.child("ringing").getValue().toString();
                    userRef.child(snapshot.child("ringing").getValue().toString()).child("calling").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild("calling")) {
                                Intent intent = new Intent(activity, Video_Call.class);
                                intent.putExtra("uid", valuer);
                                // intent.putExtra("name",  messageReceiverName);
                                startActivity(intent);
                            } else {
                                userRef.child(currentUserId).child("Ringing").removeValue();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });


        //voice call
        DatabaseReference userRef1 = FirebaseDatabase.getInstance().getReference().child("user1");
        userRef1.child(currentUserId).child("Ringing").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("ringing")) {
                    String valuer = snapshot.child("ringing").getValue().toString();
                    userRef1.child(snapshot.child("ringing").getValue().toString()).child("calling").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild("calling")) {
                                Intent intent = new Intent(activity, Voice_Call.class);//voice call
                                intent.putExtra("uid",valuer);
                                // intent.putExtra("name",  messageReceiverName);
                                startActivity(intent);
                            } else {
                                userRef1.child(currentUserId).child("Ringing").removeValue();
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
        return view;
    }

    @Override
    public void onAttach(@NonNull @NotNull Activity activity) {
        super.onAttach(activity);
        this.activity = (Messenger_Activity) activity;
    }
}
