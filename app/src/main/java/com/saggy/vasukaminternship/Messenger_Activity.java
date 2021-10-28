package com.saggy.vasukaminternship;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saggy.vasukaminternship.adapters.Mesenger_Pager_Adapter;
import com.saggy.vasukaminternship.models.Chat_Model;
import com.saggy.vasukaminternship.models.Messages;
import com.saggy.vasukaminternship.models.ProfileInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

public class Messenger_Activity extends AppCompatActivity {
    ImageButton backButton;
    TabLayout tb;
    ViewPager vp;
    EditText search_bar;
    FirebaseUser firebaseUser;
    String currentUserUid;
    List<ProfileInfo> userList =  new ArrayList<>();
    List<ProfileInfo> resultList = new ArrayList<>();
    List<ProfileInfo> chat_user_list = new ArrayList<>();
    List<ProfileInfo> requestlist = new ArrayList<>();
    List<Messages> request_message_list = new ArrayList<>();
    List<Messages> temp_message_list = new ArrayList<>();
    int temp = 0;
    Stack<Chat_Model> chat_list = new Stack<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messenger_activity);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        currentUserUid = firebaseUser.getUid();

        tb = findViewById(R.id.tab_layout);
        vp = findViewById(R.id.viewPager);
        backButton = findViewById(R.id.backButton);
        search_bar = findViewById(R.id.searchBar);

        tb.addTab(tb.newTab().setText("Chat"));
        tb.addTab(tb.newTab().setText("Request"));

        tb.setTabGravity(TabLayout.GRAVITY_FILL);

        // to get all the users
        FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (final DataSnapshot idSnapshot : dataSnapshot.getChildren()) {
                    ProfileInfo poi = idSnapshot.getValue(ProfileInfo.class);
                    assert poi != null;
                    if (!poi.getUid().equals(currentUserUid))
                        userList.add(poi);
                }

                //get chats of the current user
                FirebaseDatabase.getInstance().getReference().child("Chat").child(currentUserUid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (final DataSnapshot idSnapshot : dataSnapshot.getChildren()) {
                            Chat_Model chat = idSnapshot.getValue(Chat_Model.class);
                            if (chat != null)
                                chat_list.push(chat);
                        }
                        for(Chat_Model chat_model:chat_list){
                            for(ProfileInfo profileInfo: userList){
                                if(chat_model.getUid().equals(profileInfo.getUid())){
                                    chat_user_list.add(profileInfo);
                                    break;
                                }
                            }
                        }

                        FirebaseDatabase.getInstance().getReference().child("Request").child(currentUserUid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    Messages messages = dataSnapshot.getValue(Messages.class);
                                    temp_message_list.add(messages);
                                }
                                int count = 0;
                                for(Messages msg : temp_message_list){
                                    if(msg.getMessageID().equals("new")) {
                                        request_message_list.add(msg);
                                        count++;
                                    }
                                }
                                for(Messages msg : temp_message_list){
                                    if(!msg.getMessageID().equals("new"))
                                        request_message_list.add(msg);
                                }
                                 for(Messages message  : request_message_list){
                                    for(ProfileInfo profileInfo : userList){
                                        if(profileInfo.getUid().equals(message.getFrom())){
                                            requestlist.add(profileInfo);
                                            break;
                                        }
                                    }
                                }
                                 if(count != 0)
                                 Objects.requireNonNull(tb.getTabAt(1)).getOrCreateBadge().setNumber(count);
                                Mesenger_Pager_Adapter mesenger_pager_adapter = new Mesenger_Pager_Adapter(Messenger_Activity.this,
                                        getSupportFragmentManager(), tb.getTabCount(), 0 , chat_user_list,resultList, requestlist , request_message_list);
                                vp.setAdapter(mesenger_pager_adapter);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tb));

        tb.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        backButton.setOnClickListener(view -> {
            onBackPressed();
        });


        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() >2){
                    resultList.clear();
                    for(ProfileInfo profileInfo : userList){
                        if(profileInfo.getUsername().toLowerCase().contains(charSequence.toString().toLowerCase()) ||
                        profileInfo.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            resultList.add(profileInfo);
                        }
                    }
                    if(resultList.size() > 0){
                        temp = 1;
                        Mesenger_Pager_Adapter mesenger_pager_adapter = new Mesenger_Pager_Adapter(Messenger_Activity.this,
                                getSupportFragmentManager(), tb.getTabCount(), 1 , chat_user_list,resultList,requestlist,request_message_list);
                        vp.setAdapter(mesenger_pager_adapter);
                        Objects.requireNonNull(vp.getAdapter()).notifyDataSetChanged();
                    }
                    else{
                        if(temp ==1){
                            Mesenger_Pager_Adapter mesenger_pager_adapter = new Mesenger_Pager_Adapter(Messenger_Activity.this,
                                    getSupportFragmentManager(), tb.getTabCount(), 0 , chat_user_list,resultList,requestlist,request_message_list);
                            vp.setAdapter(mesenger_pager_adapter);
                            Objects.requireNonNull(vp.getAdapter()).notifyDataSetChanged();
                            temp = 0;
                        }

                    }
                }
                else{
                    if(temp == 1){
                        Mesenger_Pager_Adapter mesenger_pager_adapter = new Mesenger_Pager_Adapter(Messenger_Activity.this,
                                getSupportFragmentManager(), tb.getTabCount(), 0 , chat_user_list,resultList,requestlist,request_message_list);
                        vp.setAdapter(mesenger_pager_adapter);
                        Objects.requireNonNull(vp.getAdapter()).notifyDataSetChanged();
                        temp = 0;
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}


