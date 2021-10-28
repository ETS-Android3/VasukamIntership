package com.saggy.vasukaminternship;

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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saggy.vasukaminternship.adapters.Chat_Contact_Adapter;
import com.saggy.vasukaminternship.models.Messages;
import com.saggy.vasukaminternship.models.ProfileInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Request_Fragment_Messenger extends Fragment {
    View view;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    String currentUserId;

    private Chat_Contact_Adapter chat_contact_adapter;
    List<ProfileInfo> resultlist;
    List<ProfileInfo> requestlist;
    List<ProfileInfo> chat_user_list;
    List<Messages> request_message_list;


    int temp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        resultlist = (List<ProfileInfo>) bundle.getSerializable("result");
        requestlist = (List<ProfileInfo>) bundle.getSerializable("request");
        request_message_list = (List<Messages>) bundle.getSerializable("message");
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

        if(temp == 0){
            // request
            chat_contact_adapter  = new Chat_Contact_Adapter(getContext(), requestlist, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    ProfileInfo pos = requestlist.get(position);
//                // Toast.makeText(getApplicationContext(),  pos.getUid()+ " is selected!", Toast.LENGTH_SHORT).show();
                    if (!currentUserId.equals(pos.getUid())) {
                        FirebaseDatabase.getInstance().getReference().child("Request").child(currentUserId)
                                .child(pos.getUid()).child("messageID").setValue("seen");
                        if(request_message_list.size() == requestlist.size()){
                            Messages messages = request_message_list.get(position);
                            Intent intent = new Intent(getContext(), Chat_Activity.class);
                            intent.putExtra("uid", pos.getUid());
                            intent.putExtra("name", pos.getName());
                            intent.putExtra("username", pos.getUsername());
                            intent.putExtra("phoneNumber", pos.getPhoneNumber());
                            intent.putExtra("message",(Serializable) messages);
                            intent.putExtra("val" , 2);
                            startActivity(intent);
                            chat_contact_adapter.notifyDataSetChanged();
                        }
                        else{
                            FirebaseDatabase.getInstance().getReference().child("Request").child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Messages messages = snapshot.child(requestlist.get(position).getUid()).getValue(Messages.class);
                                    Intent intent = new Intent(getContext(), Chat_Activity.class);
                                    intent.putExtra("uid", pos.getUid());
                                    intent.putExtra("name", pos.getName());
                                    intent.putExtra("username", pos.getUsername());
                                    intent.putExtra("phoneNumber", pos.getPhoneNumber());
                                    intent.putExtra("message",(Serializable) messages);
                                    intent.putExtra("val" , 2);
                                    startActivity(intent);
                                    chat_contact_adapter.notifyDataSetChanged();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
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
            // search
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


        return view;
    }
}
