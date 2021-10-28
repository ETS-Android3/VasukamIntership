package com.saggy.vasukaminternship.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saggy.vasukaminternship.Explore_Fragment;
import com.saggy.vasukaminternship.Live_Fragment;
import com.saggy.vasukaminternship.Messenger_Activity;
import com.saggy.vasukaminternship.R;
import com.saggy.vasukaminternship.Trending_Fragment;
import com.saggy.vasukaminternship.adapters.Item_Horizontal_Adapter;

import java.util.ArrayList;
import java.util.List;

public class Live_Nav_Fragment extends Fragment {
    View view;
    AppCompatButton liveButton, trendingButton, exploreButton;
    ImageButton filterButton,messenger;
    TextView headText;
    View view1, view3;
    AppCompatButton v1,v2,v3;



    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.live_navbar_fragment, container, false);
        loadFragment2(new Live_Fragment());

        liveButton = view.findViewById(R.id.liveButton);
        exploreButton = view.findViewById(R.id.exploreButton);
        trendingButton = view.findViewById(R.id.trendingButton);
        filterButton = view.findViewById(R.id.filterButton);
        messenger = view.findViewById(R.id.messenger);
        headText = view.findViewById(R.id.headText);
        view1 = view.findViewById(R.id.view1);
        view3 = view.findViewById(R.id.view3);
        v3 = view.findViewById(R.id.btn_view3);
        v2 = view.findViewById(R.id.btn_view2);
        v1 = view.findViewById(R.id.btn_view1);




        messenger.setOnClickListener(view -> {
            Intent intent = new Intent(getContext() , Messenger_Activity.class);
            startActivity(intent);
        });

        v1.setOnClickListener(view -> {
            loadFragment2(new Live_Fragment());
            headText.setText("Live");

            view1.setBackground(getResources().getDrawable(R.drawable.view1_live));
            view3.setBackground(getResources().getDrawable(R.drawable.view3_live));
            liveButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_icon_figma));
            trendingButton.setBackgroundColor(getResources().getColor(R.color.transparent));
            exploreButton.setBackgroundColor(getResources().getColor(R.color.transparent));
            exploreButton.setTextColor(getResources().getColor(R.color.grey_tab));
            trendingButton.setTextColor(getResources().getColor(R.color.grey_tab));
            liveButton.setTextColor(getResources().getColor(R.color.white));

            liveButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            trendingButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            exploreButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        });

        v2.setOnClickListener(view -> {
            loadFragment2(new Trending_Fragment());
            headText.setText("Trending");

            view1.setBackground(getResources().getDrawable(R.drawable.view1_trending));
            view3.setBackground(getResources().getDrawable(R.drawable.view3_trending));

            liveButton.setBackgroundColor(getResources().getColor(R.color.transparent));
            exploreButton.setBackgroundColor(getResources().getColor(R.color.transparent));
            trendingButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_icon_figma));
            exploreButton.setTextColor(getResources().getColor(R.color.grey_tab));
            trendingButton.setTextColor(getResources().getColor(R.color.white));
            liveButton.setTextColor(getResources().getColor(R.color.grey_tab));

            liveButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            trendingButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            exploreButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        });

        v3.setOnClickListener(view -> {
            loadFragment2(new Explore_Fragment());
            headText.setText("Explore");

            view1.setBackground(getResources().getDrawable(R.drawable.view1_explore));
            view3.setBackground(getResources().getDrawable(R.drawable.view3_explore));

            liveButton.setBackgroundColor(getResources().getColor(R.color.transparent));
            trendingButton.setBackgroundColor(getResources().getColor(R.color.transparent));
            exploreButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_explore_figma));
            exploreButton.setTextColor(getResources().getColor(R.color.white));
            trendingButton.setTextColor(getResources().getColor(R.color.grey_tab));
            liveButton.setTextColor(getResources().getColor(R.color.grey_tab));

            liveButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            trendingButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            exploreButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        });

        return view;
    }

    public  void loadFragment2(Fragment fragment){
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
}





//        int margin1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());


        //live params view1
//        int width_live1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 267, getResources().getDisplayMetrics());
//        int height_live1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 203, getResources().getDisplayMetrics());
//        int start_live1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -80, getResources().getDisplayMetrics());
//        int top_live1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -9, getResources().getDisplayMetrics());
//        ConstraintLayout.LayoutParams live_params1 = new ConstraintLayout.LayoutParams(width_live1, height_live1);
//        live_params1.setMargins(start_live1,top_live1,margin1,margin1);

        //trending params view1
//        int width_trending1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 256, getResources().getDisplayMetrics());
//        int height_trending1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 248, getResources().getDisplayMetrics());
//        int start_trending1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -53, getResources().getDisplayMetrics());
//        int top_trending1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -71, getResources().getDisplayMetrics());
//        ConstraintLayout.LayoutParams trending_params1 = new ConstraintLayout.LayoutParams(width_trending1, height_trending1);
//        trending_params1.setMargins(start_trending1,top_trending1,margin1,margin1);

        //explore params view1
//        int width_explore1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 325, getResources().getDisplayMetrics());
//        int start_explore1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -38, getResources().getDisplayMetrics());
//        int top_explore1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -22, getResources().getDisplayMetrics());
//        ConstraintLayout.LayoutParams explore_params1 = new ConstraintLayout.LayoutParams(width_explore1, height_live1);
//        explore_params1.setMargins(start_explore1,top_explore1,margin1,margin1);

        //live params view3
//        int width_live3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 133, getResources().getDisplayMetrics());
//        int height_live3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 164, getResources().getDisplayMetrics());
//        int right_live3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -15, getResources().getDisplayMetrics());
//        int top_live3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 38, getResources().getDisplayMetrics());
//        ConstraintLayout.LayoutParams live_params3 = new ConstraintLayout.LayoutParams(width_live3, height_live3);
//        live_params3.setMargins(margin1,top_live3,right_live3,margin1);

        //trending params view3
//        int right_trending3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -5, getResources().getDisplayMetrics());
//        int top_trending3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 72, getResources().getDisplayMetrics());
//        ConstraintLayout.LayoutParams trending_params3 = new ConstraintLayout.LayoutParams(width_live3, height_live3);
//        trending_params3.setMargins(margin1,top_trending3,right_trending3,margin1);

        //explore params view3
//        int width_explore3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 194, getResources().getDisplayMetrics());
//        ConstraintLayout.LayoutParams explore_params3 = new ConstraintLayout.LayoutParams(width_explore3, height_live3);
//        explore_params3.setMargins(margin1,top_live3,margin1,margin1);

        //live button params
//        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -55, getResources().getDisplayMetrics());
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        params.setMarginStart(margin);

//        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        params1.setMarginStart(margin1);