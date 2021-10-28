package com.saggy.vasukaminternship.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.saggy.vasukaminternship.R;
import com.saggy.vasukaminternship.RecyclerTouchListener;
import com.saggy.vasukaminternship.models.ProfileInfo;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat_Contact_Adapter extends RecyclerView.Adapter<Chat_Contact_Adapter.ViewHolder> {
    private Context context;
    private List<ProfileInfo> profileList;
    public RecyclerTouchListener.ClickListener  onhi;


    public Chat_Contact_Adapter(Context context, List<ProfileInfo> profileList, RecyclerTouchListener.ClickListener  onhi1) {
        this.profileList = profileList ;
        this.context = context;
        this.onhi = onhi1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_chat_messeges, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Chat_Contact_Adapter.ViewHolder holder, int position) {
        ProfileInfo profileInfo = profileList.get(position);
        holder.username.setText(profileInfo.getUsername());
        holder.status.setText(profileInfo.getEmailId());
        /*

    Picasso.get().load(po.getImage()).placeholder(R.drawable.profile).into(holder.circleImageView);
       holder.status.setText(po.getStatus());
         */
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username, status;
        public CircleImageView userImage;
        public CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.person_name);
            status = itemView.findViewById(R.id.status);
            userImage = itemView.findViewById(R.id.profile_picture);
            cardView = itemView.findViewById(R.id.cardView);

            itemView.setOnClickListener(v -> {
                onhi.onClick(v, getAdapterPosition());
                //  Toast.makeText(v.getContext(), "ROW PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            });

        }
//        @Override
//        public void onClick(View v) {
//            int position = this.getAdapterPosition();
//            Intent intent = new Intent(context, Chat_Activity.class);
//            intent.putExtra("username", profileList.get(position).getUsername());
//            context.startActivity(intent);
//        }




    }

}
