package com.saggy.vasukaminternship.adapters;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.saggy.vasukaminternship.Award_Fragment;
import com.saggy.vasukaminternship.Chat_fragments_Messenger;
import com.saggy.vasukaminternship.Currency_Fragment;
import com.saggy.vasukaminternship.Request_Fragment_Messenger;
import com.saggy.vasukaminternship.models.Messages;
import com.saggy.vasukaminternship.models.ProfileInfo;

import java.io.Serializable;
import java.util.List;

public class Mesenger_Pager_Adapter extends FragmentStatePagerAdapter {
    int totalTabs;
    Context context;
    List<ProfileInfo> resultList;
    List<ProfileInfo> chat_user_list;
    List<ProfileInfo> request_list;
    List<Messages> request_message_list;
    int val;

    public Mesenger_Pager_Adapter(Context context,@NonNull FragmentManager fm, int totalTabs, int val,
                                  List<ProfileInfo> chat_user_list, List<ProfileInfo> resultList,
                                  List<ProfileInfo> request_list, List<Messages> request_message_list) {
        super(fm);
        this.val =val;
        this.context = context;
        this.resultList = resultList;
        this.totalTabs = totalTabs;
        this.chat_user_list = chat_user_list;
        this.request_list = request_list;
        this.request_message_list = request_message_list;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: Chat_fragments_Messenger chat_fragments_messenger = new Chat_fragments_Messenger();
                Bundle bundle = new Bundle();
                bundle.putSerializable("result",(Serializable) resultList);
                bundle.putSerializable("chat",(Serializable) chat_user_list);
                bundle.putInt("val",val);
                chat_fragments_messenger.setArguments(bundle);
                return chat_fragments_messenger;
            case 1: Request_Fragment_Messenger request_fragment_messenger = new Request_Fragment_Messenger();
                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable("result", (Serializable) resultList);
                    bundle1.putSerializable("request", (Serializable) request_list);
                    bundle1.putSerializable("message", (Serializable) request_message_list);
                    bundle1.putSerializable("chat", (Serializable) chat_user_list);
                    bundle1.putInt("val",val);
                    request_fragment_messenger.setArguments(bundle1);
                    return request_fragment_messenger;
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
