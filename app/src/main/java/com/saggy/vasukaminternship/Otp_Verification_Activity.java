package com.saggy.vasukaminternship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Otp_Verification_Activity extends AppCompatActivity {
    AppCompatButton done, login;
    EditText otp;
    ImageButton backButton;
    TextView errorText, resend;
    FirebaseAuth mAuth;

    String phoneNumber;
    int temp = 0;
    String otpId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        otp = findViewById(R.id.otp);
        done = findViewById(R.id.done);
        backButton = findViewById(R.id.backButton);
        login = findViewById(R.id.login);
        errorText = findViewById(R.id.errorText);
        resend = findViewById(R.id.resend);
        mAuth = FirebaseAuth.getInstance();

        temp = getIntent().getExtras().getInt("temp");
        phoneNumber = getIntent().getExtras().getString("phoneNumber");
        errorText.setText("Sending OTP");
        errorText.setVisibility(View.VISIBLE);

        sendOTP();

        resend.setOnClickListener(view -> {
                errorText.setVisibility(View.GONE);
                sendOTP();
        });

        done.setOnClickListener(view -> {
            if(otp.getText().toString().isEmpty()){
                Toast.makeText(this, "Enter your OTP", Toast.LENGTH_SHORT).show();
            }
            else if(otp.getText().toString().length() != 6){
                Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
            }
            else{
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpId,otp.getText().toString());
                signInWithPhoneAuthCredential(credential);
            }
        });

    }

    private void sendOTP() {
        //method to send otp to user
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                errorText.setText("OTP sent");
                                errorText.setVisibility(View.VISIBLE);
                                otpId = s;
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                String code = phoneAuthCredential.getSmsCode();
                                Toast.makeText(Otp_Verification_Activity.this, code+" yes", Toast.LENGTH_SHORT).show();
                                if(code != null){
                                    otp.setText(code.trim());
                                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpId, code.trim());
                                    signInWithPhoneAuthCredential(credential);
                                }
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                errorText.setVisibility(View.VISIBLE);
                                errorText.setText("something went wrong");
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        //method to verify otp and sign in user
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                errorText.setText("Sign in successful");
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = task.getResult().getUser();
                                // Update UI
                                assert user != null;
                                String uid = user.getUid();
                                if(temp == 1){
                                    //login
                                    FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.child("Users").child(uid).exists()){
                                                startActivity(new Intent(Otp_Verification_Activity.this,Home_Activity.class));
                                                finish();
                                            }
                                            else{
                                                startActivity(new Intent(Otp_Verification_Activity.this, Password_Input_Activity.class));
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                                else if(temp == 2){
                                    //register
                                    String name, emailId, DOB, username;

                                    name = getIntent().getExtras().getString("name");
                                    emailId = getIntent().getExtras().getString("emailId");
                                    DOB = getIntent().getExtras().getString("DOB");
                                    username = getIntent().getExtras().getString("username");
                                    phoneNumber = user.getPhoneNumber();

                                    FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("name").setValue(name);
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("uid").setValue(uid);
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("phoneNumber").setValue(phoneNumber);
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("emailId").setValue(emailId);
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("username").setValue(username);
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("DOB").setValue(DOB);
                                    startActivity(new Intent(Otp_Verification_Activity.this, Home_Activity.class));
                                    finish();
                                }
                            } else {
                              //failed
                               errorText.setVisibility(View.VISIBLE);
                               errorText.setText("OTP is incorrect");
                            }
                        }
                    });
        }



}