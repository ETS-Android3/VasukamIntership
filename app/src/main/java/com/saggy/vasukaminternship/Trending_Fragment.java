package com.saggy.vasukaminternship;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saggy.vasukaminternship.adapters.Item_Horizontal_Adapter;

import java.util.ArrayList;
import java.util.List;

public class Trending_Fragment extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.trending_tab_activity, container, false);

        return view;
    }
}

