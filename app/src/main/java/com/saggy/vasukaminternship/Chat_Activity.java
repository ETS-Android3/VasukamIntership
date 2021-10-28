package com.saggy.vasukaminternship;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saggy.vasukaminternship.adapters.Chat_Contact_Adapter;
import com.saggy.vasukaminternship.adapters.Message_Adapter;
import com.saggy.vasukaminternship.models.Chat_Model;
import com.saggy.vasukaminternship.models.Messages;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat_Activity extends AppCompatActivity {
    ImageButton backButton, message_send_button, video_call, voice_call;
    ImageButton photo_send, camera,addButton;
    TextView usernameText;
    RecyclerView userMessagesList;
    EditText messageBox;
    CircleImageView profile_imageR;
    RelativeLayout bottom_layout;
    CardView sender_cardView, receiver_cardView;
    TextView sent_text , received_text;
    CardView request_dialog;
    AppCompatButton accept_request , cancel_dialog;

    private DatabaseReference RootRef;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    String currentUserUid;
    String usernameR, nameR, uidR, phoneNumberR;
    int val;

    String sender_key, receiver_key;

    private List<Messages> messagesList = new ArrayList<>();
    Message_Adapter message_adapter;

    Messages request_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        currentUserUid = firebaseUser.getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        usernameText = findViewById(R.id.username);
        backButton = findViewById(R.id.backButton);
        userMessagesList = findViewById(R.id.chat_list_messages);
        messageBox = findViewById(R.id.messageBox);
        message_send_button = findViewById(R.id.mic_chat_button);
        video_call = findViewById(R.id.video_call);
        voice_call = findViewById(R.id.mic_button);
        photo_send = findViewById(R.id.image_button);
        camera = findViewById(R.id.camera_button);
        addButton = findViewById(R.id.add_button);
        profile_imageR = findViewById(R.id.profile_picture);
        bottom_layout = findViewById(R.id.bottom_layout);
        sender_cardView = findViewById(R.id.message_cardS);
        received_text = findViewById(R.id.receive_message);
        sent_text = findViewById(R.id.send_message);
        receiver_cardView = findViewById(R.id.message_cardR);
        cancel_dialog = findViewById(R.id.cancel_dialog);
        accept_request = findViewById(R.id.accept_request);
        request_dialog = findViewById(R.id.request_dialog);

        receiver_cardView.setVisibility(View.GONE);
        sender_cardView.setVisibility(View.GONE);
        request_dialog.setVisibility(View.GONE);

        val = getIntent().getExtras().getInt("val");
        usernameR = getIntent().getExtras().getString("username");
        nameR = getIntent().getExtras().getString("name");
        uidR = getIntent().getExtras().getString("uid");
        phoneNumberR = getIntent().getExtras().getString("phoneNumber");

        usernameText.setText(usernameR);

        message_adapter = new Message_Adapter(messagesList);
        userMessagesList.setLayoutManager(new LinearLayoutManager(this));
        userMessagesList.setAdapter(message_adapter);
        if(val == 0 || messagesList.size()>0) {
            FirebaseDatabase.getInstance().getReference().child("Messages").child(currentUserUid)
                    .child(uidR).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child("keycode").exists())
                        sender_key = snapshot.child("keycode").getValue(String.class);
                    else
                        val = 1;
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            FirebaseDatabase.getInstance().getReference().child("Messages").child(uidR)
                    .child(currentUserUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child("keycode").exists())
                        receiver_key = snapshot.child("keycode").getValue(String.class);
                    else
                        val = 1;

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        else if(val == 2){
            //request
            bottom_layout.setVisibility(View.GONE);
            request_message = (Messages) getIntent().getSerializableExtra("message");
            request_dialog.setVisibility(View.VISIBLE);
            receiver_cardView.setVisibility(View.VISIBLE);
            received_text.setText(request_message.getMessage());
        }
        else{
            //search
            FirebaseDatabase.getInstance().getReference().child("Request").child(uidR).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(currentUserUid).exists()) {
                        bottom_layout.setVisibility(View.GONE);
                        sender_cardView.setVisibility(View.VISIBLE);
                        sent_text.setText(snapshot.child(currentUserUid).child("message").getValue(String.class));
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        }

        backButton.setOnClickListener(view -> onBackPressed());

        video_call.setOnClickListener(view -> {
            if(val == 0) {
                Intent intent = new Intent(Chat_Activity.this, Video_Call.class);
                intent.putExtra("uid", uidR);
//                intent.putExtra("username", usernameR);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(this, "you can't video call "+usernameR, Toast.LENGTH_SHORT).show();
            }
        });

        voice_call.setOnClickListener(view -> {
            if(val == 0) {
                Intent intent = new Intent(Chat_Activity.this, Voice_Call.class);
                intent.putExtra("uid", uidR);
//                intent.putExtra("username", usernameR);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(this, "you can't voice call "+usernameR, Toast.LENGTH_SHORT).show();
            }
        });

        messageBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 0) {
                    message_send_button.setImageDrawable(getResources().getDrawable(R.drawable.messenger_right_figma));
                }
                else {
                    message_send_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_mic_none_24));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        message_send_button.setOnClickListener(view -> {

            if(messageBox.getText().toString().length()>0) {
                //want to send message
                if(val == 0) {
                    // already in his chat
                    String message = messageBox.getText().toString();
//                    String messageSenderRef = "Messages/" + currentUserUid + "/" + uidR + "/messages" ;
//                    String messageReceiverRef = "Messages/" + uidR + "/" + currentUserUid + "/messages" ;

                    DatabaseReference userMessageKeyRef = RootRef.child("Messages")
                            .child(currentUserUid).child(uidR).child("messages").push();

                    String messagePushID = userMessageKeyRef.getKey();
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
                    String saveCurrentDate = currentDate.format(calendar.getTime());
                    SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                    String saveCurrentTime = currentTime.format(calendar.getTime());

                    Messages messages = new Messages(currentUserUid, message, "text", uidR, messagePushID,
                            saveCurrentTime, saveCurrentDate);

                    assert messagePushID != null;
                    FirebaseDatabase.getInstance().getReference().child("Messages").child(currentUserUid)
                            .child(uidR).child("messages").child(messagePushID).setValue(messages);
                    FirebaseDatabase.getInstance().getReference().child("Messages").child(uidR)
                            .child(currentUserUid).child("messages").child(messagePushID).setValue(messages);

                    FirebaseDatabase.getInstance().getReference().child("Chat").child(currentUserUid).child(sender_key).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Chat").child(uidR).child(receiver_key).removeValue();

                    DatabaseReference sref1 = RootRef.child("Chat").child(currentUserUid).push();
                    DatabaseReference rref2 = RootRef.child("Chat").child(uidR).push();

                    String new_sender_key = sref1.getKey();
                    String new_receiver_key = rref2.getKey();

                    Chat_Model senderChat = new Chat_Model(new_sender_key,uidR);
                    Chat_Model receiverChat = new Chat_Model(new_receiver_key,currentUserUid);

                    assert new_sender_key != null;
                    FirebaseDatabase.getInstance().getReference().child("Chat").child(currentUserUid)
                            .child(new_sender_key).setValue(senderChat);
                    assert new_receiver_key != null;
                    FirebaseDatabase.getInstance().getReference().child("Chat").child(uidR)
                            .child(new_receiver_key).setValue(receiverChat);

                    FirebaseDatabase.getInstance().getReference().child("Messages").child(currentUserUid)
                            .child(uidR).child("keycode").setValue(new_sender_key);
                    FirebaseDatabase.getInstance().getReference().child("Messages").child(uidR)
                            .child(currentUserUid).child("keycode").setValue(new_receiver_key);

                    sender_key = new_sender_key;
                    receiver_key = new_receiver_key;

                    messageBox.setText("");
                }
                else {
                    // want to give request
                    FirebaseDatabase.getInstance().getReference().child("Request").child(uidR).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.child(currentUserUid).exists()) {
                                Toast.makeText(Chat_Activity.this, "you already sent the request to "+usernameR, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                String message = messageBox.getText().toString();

                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
                                String saveCurrentDate = currentDate.format(calendar.getTime());
                                SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                                String saveCurrentTime = currentTime.format(calendar.getTime());

                                Messages messages = new Messages(currentUserUid, message, "text", uidR, "new",
                                        saveCurrentTime, saveCurrentDate);

                                FirebaseDatabase.getInstance().getReference().child("Request").child(uidR).child(currentUserUid).setValue(messages);

                                Toast.makeText(Chat_Activity.this, "Request sent successfully", Toast.LENGTH_SHORT).show();
                                sender_cardView.setVisibility(View.VISIBLE);
                                sent_text.setText(message);
                                bottom_layout.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
            else {
                //want to send audio
            }
        });


        cancel_dialog.setOnClickListener(view -> {
            if(val == 2){
                request_dialog.setVisibility(View.GONE);
            }
        });

        accept_request.setOnClickListener(view -> {
            if(val == 2){
                if(!request_message.getMessage().isEmpty()){
                    DatabaseReference userMessageKeyRef = RootRef.child("Messages")
                            .child(currentUserUid).child(uidR).child("messages").push();

                    String messagePushId = userMessageKeyRef.getKey();

                    Messages messages = new Messages(request_message.getFrom(), request_message.getMessage(), "text",request_message.getTo() , messagePushId,
                            request_message.getTime(), request_message.getDate());

                    assert messagePushId != null;
                    FirebaseDatabase.getInstance().getReference().child("Messages").child(currentUserUid)
                            .child(uidR).child("messages").child(messagePushId).setValue(messages);
                    FirebaseDatabase.getInstance().getReference().child("Messages").child(uidR)
                            .child(currentUserUid).child("messages").child(messagePushId).setValue(messages);

                    DatabaseReference sref1 = RootRef.child("Chat").child(currentUserUid).push();
                    DatabaseReference rref2 = RootRef.child("Chat").child(uidR).push();

                    String new_sender_key = sref1.getKey();
                    String new_receiver_key = rref2.getKey();

                    Chat_Model senderChat = new Chat_Model(new_sender_key,uidR);
                    Chat_Model receiverChat = new Chat_Model(new_receiver_key,currentUserUid);

                    assert new_sender_key != null;
                    FirebaseDatabase.getInstance().getReference().child("Chat").child(currentUserUid)
                            .child(new_sender_key).setValue(senderChat);
                    assert new_receiver_key != null;
                    FirebaseDatabase.getInstance().getReference().child("Chat").child(uidR)
                            .child(new_receiver_key).setValue(receiverChat);

                    FirebaseDatabase.getInstance().getReference().child("Messages").child(currentUserUid)
                            .child(uidR).child("keycode").setValue(new_sender_key);
                    FirebaseDatabase.getInstance().getReference().child("Messages").child(uidR)
                            .child(currentUserUid).child("keycode").setValue(new_receiver_key);

                    FirebaseDatabase.getInstance().getReference().child("Request").child(uidR).child(currentUserUid).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Request").child(currentUserUid).child(uidR).removeValue();

                    Toast.makeText(this, "request accepted successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Chat_Activity.this, Messenger_Activity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*sendfilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence options[]=new CharSequence[]
                        {
                                "Images",
                                "PDF Files",
                                "Ms Word Files"
                        };
                AlertDialog.Builder builder=new AlertDialog.Builder(ChatActivity.this);
                builder.setTitle("Select the file");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==0){
                            checker="image";
                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent,"Select Image"),438);
                        }
                        if(i==1){
                            checker="pdf";
                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/pdf");
                            startActivityForResult(intent.createChooser(intent,"Select Pdf file"),438);
                        }
                        if(i==2){
                            checker="docx";
                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/msword");
                            startActivityForResult(intent.createChooser(intent,"Select MS Word file"),438);
                        }
                    }
                });
                builder.show();

            }
        });*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        userRef.child(currentUserUid).child("Ringing").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("ringing")) {
                    String valuer=snapshot.child("ringing").getValue().toString();
                    userRef.child(snapshot.child("ringing").getValue().toString()).child("calling").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild("calling")) {
                                Intent intent = new Intent(Chat_Activity.this, Video_Call.class);
                                intent.putExtra("uid",valuer);
                                // intent.putExtra("name",  messageReceiverName);
                                startActivity(intent);
                            }
                            else{
                                userRef.child(currentUserUid).child("Ringing").removeValue()
                                        .addOnCompleteListener(task -> {
                                        });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference userRef1= FirebaseDatabase.getInstance().getReference().child("user1");
        userRef1.child(currentUserUid).child("Ringing").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("ringing")) {
                    String valuer=snapshot.child("ringing").getValue().toString();
                    userRef1.child(snapshot.child("ringing").getValue().toString()).child("calling").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild("calling")) {
                                Intent intent = new Intent(Chat_Activity.this, Voice_Call.class);
                                intent.putExtra("uid",valuer);
                                // intent.putExtra("name",  messageReceiverName);
                                startActivity(intent);
                            }
                            else{
                                userRef1.child(currentUserUid).child("Ringing").removeValue()
                                        .addOnCompleteListener(task -> {
                                        });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        RootRef.child("Messages").child(currentUserUid).child(uidR).child("messages")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s)
                    {
                        Messages messages = dataSnapshot.getValue(Messages.class);
                        messagesList.add(messages);
                        message_adapter.notifyDataSetChanged();
                        userMessagesList.smoothScrollToPosition(Objects.requireNonNull(userMessagesList.getAdapter()).getItemCount());
                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    }
                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                    }
                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}







    /*

     private ProgressDialog  loadingBar;
    private String checker="";
    private Uri fileuri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==438&&resultCode==RESULT_OK&&data!=null){
            loadingBar.setTitle("Send File");
            loadingBar.setMessage("Please wait, we are send file..");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            fileuri=data.getData();
            if(!checker.equals("image")){
                StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Document file");
                String messageSenderRef = "Messages/" + messageSenderID + "/" + messageReceiverID;
                String messageReceiverRef = "Messages/" + messageReceiverID + "/" + messageSenderID;

                DatabaseReference userMessageKeyRef = RootRef.child("Messages")
                        .child(messageSenderID).child(messageReceiverID).push();

                String messagePushID = userMessageKeyRef.getKey();
                StorageReference filepath=storageReference.child(messagePushID+"."+checker);
                filepath.putFile(fileuri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                                firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        final String downloadUri= uri.toString();
                                        // complete the rest of your code
                                        Map messageTextBody = new HashMap();
                                        messageTextBody.put("message", downloadUri);
                                        messageTextBody.put("name", fileuri.getLastPathSegment());
                                        messageTextBody.put("type", checker);
                                        messageTextBody.put("from", messageSenderID);
                                        messageTextBody.put("to", messageReceiverID);
                                        messageTextBody.put("messageID", messagePushID);
                                        messageTextBody.put("time", saveCurrentTime);
                                        messageTextBody.put("date", saveCurrentDate);

                                        Map messageBodyDetails = new HashMap();
                                        messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
                                        messageBodyDetails.put( messageReceiverRef + "/" + messagePushID, messageTextBody);

                                        RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task)
                                            {
                                                if (task.isSuccessful())
                                                {
                                                    loadingBar.dismiss();
                                                    Toast.makeText(ChatActivity.this, "Message Sent Successfully...", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    loadingBar.dismiss();
                                                    Toast.makeText(ChatActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                                }
                                                MessageInputText.setText("");
                                            }
                                        });

                                    }
                                });

                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double p=(100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                        loadingBar.setMessage((int)p+"  % Uploading...");
                    }
                });

            }
            else if(checker.equals("image")){
                StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("image file");
                String messageSenderRef = "Messages/" + messageSenderID + "/" + messageReceiverID;
                String messageReceiverRef = "Messages/" + messageReceiverID + "/" + messageSenderID;

                DatabaseReference userMessageKeyRef = RootRef.child("Messages")
                        .child(messageSenderID).child(messageReceiverID).push();

                String messagePushID = userMessageKeyRef.getKey();



                StorageReference filepath=storageReference.child(messagePushID+".jpg");
                filepath.putFile(fileuri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                                firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        final String downloadUri= uri.toString();
                                        // complete the rest of your code
                                        Map messageTextBody = new HashMap();
                                        messageTextBody.put("message", downloadUri);
                                        messageTextBody.put("name", fileuri.getLastPathSegment());
                                        messageTextBody.put("type", checker);
                                        messageTextBody.put("from", messageSenderID);
                                        messageTextBody.put("to", messageReceiverID);
                                        messageTextBody.put("messageID", messagePushID);
                                        messageTextBody.put("time", saveCurrentTime);
                                        messageTextBody.put("date", saveCurrentDate);

                                        Map messageBodyDetails = new HashMap();
                                        messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
                                        messageBodyDetails.put( messageReceiverRef + "/" + messagePushID, messageTextBody);

                                        RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task)
                                            {
                                                if (task.isSuccessful())
                                                {
                                                    loadingBar.dismiss();
                                                    Toast.makeText(ChatActivity.this, "Message Sent Successfully...", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    loadingBar.dismiss();
                                                    Toast.makeText(ChatActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                                }
                                                MessageInputText.setText("");
                                            }
                                        });

                                    }
                                });

                            }
                        });

            }
        }
    }*/



    /*
    private void DisplayLastSeen()
    {
        RootRef.child("Users").child(messageReceiverID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.child("userState").hasChild("state"))
                        {
                            String state = dataSnapshot.child("userState").child("state").getValue().toString();
                            String date = dataSnapshot.child("userState").child("date").getValue().toString();
                            String time = dataSnapshot.child("userState").child("time").getValue().toString();

                            if (state.equals("online"))
                            {
                                userLastSeen.setText("online");
                            }
                            else if (state.equals("offline"))
                            {
                                userLastSeen.setText("Last Seen:" + date + " " + time);
                            }
                        }
                        else
                        {
                            userLastSeen.setText("offline");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }*/
