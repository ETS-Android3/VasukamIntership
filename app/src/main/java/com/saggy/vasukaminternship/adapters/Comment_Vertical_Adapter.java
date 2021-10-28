package com.saggy.vasukaminternship.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saggy.vasukaminternship.R;

import java.util.List;

public class Comment_Vertical_Adapter extends RecyclerView.Adapter<Comment_Vertical_Adapter.ViewHolder> {
    Context context;
    List<String> commentList;

    public Comment_Vertical_Adapter(Context context, List<String> commentList){
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.listitem_video_comment, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Comment_Vertical_Adapter.ViewHolder holder, int position) {
        TextView textView = holder.textView;
        textView.setText(commentList.get(position));
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.commentText);
        }
    }
}
