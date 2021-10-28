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

public class Item_Horizontal_Adapter extends RecyclerView.Adapter<Item_Horizontal_Adapter.ViewHolder> {
    Context context;
    List<String> optionList;

    public Item_Horizontal_Adapter(Context context, List<String> optionList){
        this.context = context;
        this.optionList = optionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.listitem_options, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Item_Horizontal_Adapter.ViewHolder holder, int position) {
        TextView textView = holder.textView;
        textView.setText(optionList.get(position));
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.option_text);
        }
    }
}