package com.saggy.vasukaminternship.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.saggy.vasukaminternship.Award_Fragment;
import com.saggy.vasukaminternship.Currency_Fragment;
import com.saggy.vasukaminternship.Trending_Fragment;

public class Tab_Layout_Adapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;

    public Tab_Layout_Adapter(Context context, int totalTabs,FragmentManager fm){
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new Currency_Fragment();
            case 1: return new Award_Fragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}

