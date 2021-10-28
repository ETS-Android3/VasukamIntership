package com.saggy.vasukaminternship.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saggy.vasukaminternship.R;

import java.util.List;

public class Video_Verticle_Adapter extends RecyclerView.Adapter<Video_Verticle_Adapter.ViewHolder> {
    Context context;
    List<String> videoLink;

    public Video_Verticle_Adapter(Context context, List<String> videoLink){
        this.context = context;
        this.videoLink = videoLink;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.listitem_video, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Video_Verticle_Adapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return videoLink.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
