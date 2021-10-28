package com.saggy.vasukaminternship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class First_Activity extends AppCompatActivity {
    AppCompatButton login, signup;
    ViewPager viewPager;
    private int[] layouts;
    View v1, v2, v3, v4, v5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        viewPager = findViewById(R.id.view_pager);
        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        v3 = findViewById(R.id.v3);
        v4 = findViewById(R.id.v4);
        v5 = findViewById(R.id.v5);

        login.setOnClickListener(view -> {
            startActivity(new Intent(First_Activity.this, Login_Activity.class));
            finish();
        });
        signup.setOnClickListener(view -> {
            startActivity(new Intent(First_Activity.this, Registration_Activity.class));
            finish();
        });

        layouts = new int[]{
                R.layout.slide_one,
                R.layout.slide_two,
                R.layout.slide_three,
                R.layout.slide_four,
                R.layout.slide_five
        };

        changeViewColor(0);
        My_Page_Adapter adapter = new My_Page_Adapter();
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

    }

    
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            changeViewColor(position);
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    @SuppressLint("UseCompatLoadingForDrawables")
    private void changeViewColor(int position) {
        switch (position){
            case 0: v1.setBackground(getResources().getDrawable(R.drawable.white_slider_dot));
                v2.setBackground(getResources().getDrawable(R.drawable.light_grey_slider_dot));
                v3.setBackground(getResources().getDrawable(R.drawable.light_grey_slider_dot));
                v4.setBackground(getResources().getDrawable(R.drawable.light_grey_slider_dot));
                v5.setBackground(getResources().getDrawable(R.drawable.light_grey_slider_dot));
                    break;
            case 1: v2.setBackground(getResources().getDrawable(R.drawable.white_slider_dot));
                v1.setBackground(getResources().getDrawable(R.drawable.light_grey_slider_dot));
                v3.setBackground(getResources().getDrawable(R.drawable.light_grey_slider_dot));
                v4.setBackground(getResources().getDrawable(R.drawable.light_grey_slider_dot));
                v5.setBackground(getResources().getDrawable(R.drawable.light_grey_slider_dot));
                break;
            case 2: v3.setBackground(getResources().getDrawable(R.drawable.white_slider_dot));
                v2.setBackground(getResources().getDrawable(R.drawable.light_grey_slider_dot));
                v1.setBackground(getResources().getDrawable(R.drawable.light_grey_slider_dot));
                v4.setBackground(getResources().getDrawable(R.drawable.light_grey_slider_dot));
                v5.setBackground(getResources().getDrawable(R.drawable.light_grey_slider_dot));
                break;
            case 3: v4.setBackground(getResources().getDrawable(R.drawable.white_slider_dot));
                v2.setBackground(getResources().getDrawable(R.drawable.light_grey_slider_dot));
                v3.setBackground(getResources().getDrawable(R.drawable.light_grey_slider_dot));
                v1.setBackground(getResources().getDrawable(R.drawable.light_grey_slider_dot));
                v5.setBackground(getResources().getDrawable(R.drawable.light_grey_slider_dot));
                break;
            case 4: v5.setBackground(getResources().getDrawable(R.drawable.white_slider_dot));
                v2.setBackground(getResources().getDrawable(R.drawable.light_grey_slider_dot));
                v3.setBackground(getResources().getDrawable(R.drawable.light_grey_slider_dot));
                v4.setBackground(getResources().getDrawable(R.drawable.light_grey_slider_dot));
                v1.setBackground(getResources().getDrawable(R.drawable.light_grey_slider_dot));
        }
    }


    public class My_Page_Adapter extends PagerAdapter{
        private LayoutInflater layoutInflater;
        public My_Page_Adapter(){
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            return view;
        }
        @Override
        public int getCount() {
            return layouts.length;
        }
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}