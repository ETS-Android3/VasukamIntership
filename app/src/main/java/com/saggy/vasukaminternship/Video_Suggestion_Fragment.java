package com.saggy.vasukaminternship;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saggy.vasukaminternship.adapters.Video_Verticle_Adapter;

import java.util.ArrayList;
import java.util.List;

public class Video_Suggestion_Fragment extends Fragment {
    View view;

    RecyclerView recyclerView;
    List<String> options = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.video_list_fragment, container, false);

        recyclerView = view.findViewById(R.id.videoListRecycleView);
        addValuesToList();

        Video_Verticle_Adapter video_verticle_adapter = new Video_Verticle_Adapter(getContext(),options);
        recyclerView.setAdapter(video_verticle_adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }

    private void addValuesToList() {
        options.add("Item 1");
        options.add("Item 2");
        options.add("Item 3");
        options.add("Item 4");
        options.add("Item 5");
        options.add("Item 6");
        options.add("Item 7");
    }
}
