package com.saggy.vasukaminternship;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saggy.vasukaminternship.adapters.Item_Horizontal_Adapter;
import com.saggy.vasukaminternship.adapters.Video_Horizontal_Adapter;
import com.saggy.vasukaminternship.adapters.Video_Verticle_Adapter;

import java.util.ArrayList;
import java.util.List;

public class Live_Fragment extends Fragment {
    View view;

    NestedScrollView nestedScrollView;

    RecyclerView horVideoRecycleView;
//    List<String> videolinks = new ArrayList<>();
    List<String> options = new ArrayList<>();

    RecyclerView itemRecycleView;

    RecyclerView verVideoRecycleView;

    VideoView videoTop;
    RelativeLayout rl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.live_tab_activity, container, false);
        horVideoRecycleView = view.findViewById(R.id.hor_video_recycle_view);
        verVideoRecycleView = view.findViewById(R.id.ver_video_recycle_view);
        rl = view.findViewById(R.id.rl);
        nestedScrollView = view.findViewById(R.id.nested_scroll_view);
        videoTop = view.findViewById(R.id.videotop);
        itemRecycleView = view.findViewById(R.id.optionRecycleView);
        addValuesToList();
        Item_Horizontal_Adapter item_horizontal_adapter = new Item_Horizontal_Adapter(getContext(),options);
        itemRecycleView.setAdapter(item_horizontal_adapter);
        itemRecycleView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        Video_Horizontal_Adapter video_horizontal_adapter = new Video_Horizontal_Adapter(getContext(),options);
        horVideoRecycleView.setAdapter(video_horizontal_adapter);
        horVideoRecycleView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        Video_Verticle_Adapter video_verticle_adapter = new Video_Verticle_Adapter(getContext(),options);
        verVideoRecycleView.setAdapter(video_verticle_adapter);
        verVideoRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        rl.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(),Video_Activity.class);
            startActivity(intent);
        });
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
