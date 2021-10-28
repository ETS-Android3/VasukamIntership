package com.saggy.vasukaminternship.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.saggy.vasukaminternship.R;
import com.saggy.vasukaminternship.models.Messages;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Message_Adapter extends RecyclerView.Adapter<Message_Adapter.MessageViewHolder> {

    private List<Messages> userMessagesList;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    public Message_Adapter (List<Messages> userMessagesList)
    {
        this.userMessagesList = userMessagesList;
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_list_messenges_item, parent, false);
        mAuth = FirebaseAuth.getInstance();
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Message_Adapter.MessageViewHolder holder, int position) {
        String messageSenderId = mAuth.getCurrentUser().getUid();

        Messages messages = userMessagesList.get(position);

        String fromUserID = messages.getFrom();
        String fromMessageType = messages.getType();

        holder.senderView.setVisibility(View.GONE);
        holder.receiverView.setVisibility(View.GONE);
        holder.messageSenderPicture.setVisibility(View.GONE);
        holder.messageReceiverPicture.setVisibility(View.GONE);



        if(fromMessageType.equals("text")) {
            if (fromUserID.equals(messageSenderId))
            {
                //user send to his known
                holder.senderView.setVisibility(View.VISIBLE);
                //holder.senderMessageText.setText(messages.getMessage() + "\n \n" + messages.getTime() + " - " + messages.getDate());
                holder.senderMessageText.setText(messages.getMessage());
            }
            else
            {
                holder.receiverView.setVisibility(View.VISIBLE);
                //holder.receiverMessageText.setText(messages.getMessage() + "\n \n" + messages.getTime() + " - " + messages.getDate());
                holder.receiverMessageText.setText(messages.getMessage());
            }
        }
        else if(fromMessageType.equals("image")) {
            if (fromUserID.equals(messageSenderId)) {
                holder.messageSenderPicture.setVisibility(View.VISIBLE);
                //Picasso.get().load(messages.getMessage()).placeholder(R.drawable.profile).into(holder.messageSenderPicture);
            }
            else{
                holder.messageReceiverPicture.setVisibility(View.VISIBLE);
                //Picasso.get().load(messages.getMessage()).placeholder(R.drawable.profile).into(holder.messageReceiverPicture);
            }
        }
        else {
            if (fromUserID.equals(messageSenderId)) {
                holder.messageSenderPicture.setVisibility(View.VISIBLE);
                holder.messageSenderPicture.setBackgroundResource(R.drawable.ic_baseline_insert_drive_file_24);
                holder.itemView.setOnClickListener(view -> {
                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                    holder.itemView.getContext().startActivity(intent);
                });
            }
            else{
                holder.messageReceiverPicture.setVisibility(View.VISIBLE);
                holder.messageReceiverPicture.setBackgroundResource(R.drawable.ic_baseline_insert_drive_file_24);
                holder.itemView.setOnClickListener(view -> {
                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                    holder.itemView.getContext().startActivity(intent);
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder
    {
        public TextView senderMessageText, receiverMessageText;
        public ImageView messageSenderPicture, messageReceiverPicture;
        public CardView senderView , receiverView;


        public MessageViewHolder(@NonNull View itemView)
        {
            super(itemView);
            senderMessageText = itemView.findViewById(R.id.sender_message_text);
            receiverMessageText = itemView.findViewById(R.id.receiver_message_text);
            messageReceiverPicture = itemView.findViewById(R.id.message_receiver_image_view);
            messageSenderPicture = itemView.findViewById(R.id.message_sender_image_view);
            senderView = itemView.findViewById(R.id.sender_card);
            receiverView = itemView.findViewById(R.id.receiver_card);
        }
    }

}

