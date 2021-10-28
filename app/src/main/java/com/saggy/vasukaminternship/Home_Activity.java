package com.saggy.vasukaminternship;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.saggy.vasukaminternship.fragments.Home_Fragment;
import com.saggy.vasukaminternship.fragments.Live_Nav_Fragment;

public class Home_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        BottomNavigationView bottomNavigationView = findViewById(R.id.navbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(navlistner);
        bottomNavigationView.setItemIconTintList(null);

        loadFragment(new Home_Fragment() );

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlistner = item -> {
        Fragment selectedFragment = null;
        switch (item.getItemId()){
            case R.id.home :
                selectedFragment = new Home_Fragment();
                break;
            case R.id.live:
                selectedFragment = new Live_Nav_Fragment();
                break;
            case R.id.add:
                selectedFragment = new Home_Fragment();
                break;
            case R.id.lightning:
                selectedFragment = new Home_Fragment();
                break;
            case R.id.store:
                selectedFragment = new Home_Fragment();
                break;
        }
        loadFragment(selectedFragment);
        return true;
    };

    public  void loadFragment(Fragment fragment){
        //create a fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();
        //create Fragment transaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //replace framelayout with new fragment
        fragmentTransaction.replace(R.id.nav_host_fragment,fragment);
        //save changes
        fragmentTransaction.commit();
    }


}



