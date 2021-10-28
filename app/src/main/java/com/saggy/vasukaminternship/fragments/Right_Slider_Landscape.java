package com.saggy.vasukaminternship.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.saggy.vasukaminternship.R;
import com.saggy.vasukaminternship.adapters.Tab_Layout_Adapter;

import eu.okatrych.rightsheet.RightSheetBehavior;

public class Right_Slider_Landscape extends Fragment {
    View view;
    ImageButton close;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.right_sheet_landscape, container, false);

        close = view.findViewById(R.id.close_btn);
        TabLayout tb = view.findViewById(R.id.tab_layout);
        ViewPager vp = view.findViewById(R.id.popupViewPager);

        tb.addTab(tb.newTab().setText("Currency"));
        tb.addTab(tb.newTab().setText("Awards"));

        tb.getTabAt(0).setIcon(R.drawable.money_figma_1);
        tb.getTabAt(1).setIcon(R.drawable.trophy_black_figma);

        for(int j=0; j<tb.getTabCount(); j++){
            View tab = ((ViewGroup) tb.getChildAt(0)).getChildAt(j);
            tab.requestLayout();
            if(j == 0){
                ViewCompat.setBackground(tab,getResources().getDrawable(R.drawable.white_rounded));
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
                p.setMargins(0, 0, 20, 0);
            }
            else{
                ViewCompat.setBackground(tab,getResources().getDrawable(R.drawable.yellow_rounded_stroke));
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
                p.setMargins(20, 0, 0, 0);
            }
        }

        close.setOnClickListener(view1 -> {
            RightSheetBehavior rightSheetBehavior = RightSheetBehavior.from(getActivity().findViewById(R.id.right_sheet));
            rightSheetBehavior.setPeekWidth(0);
            rightSheetBehavior.setState(RightSheetBehavior.STATE_COLLAPSED);
        });

        tb.setTabGravity(TabLayout.GRAVITY_FILL);

        Tab_Layout_Adapter tab_layout_adapter = new Tab_Layout_Adapter(getContext(), tb.getTabCount(), getChildFragmentManager());
        vp.setAdapter(tab_layout_adapter);

        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tb));

        tb.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());
                for(int j=0; j<tb.getTabCount(); j++){
                    View tabl = ((ViewGroup) tb.getChildAt(0)).getChildAt(j);
                    tabl.requestLayout();
                    if(j == tab.getPosition()){
                        ViewCompat.setBackground(tabl,getResources().getDrawable(R.drawable.white_rounded));
                    }
                    else{
                        ViewCompat.setBackground(tabl,getResources().getDrawable(R.drawable.yellow_rounded_stroke));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }
}
