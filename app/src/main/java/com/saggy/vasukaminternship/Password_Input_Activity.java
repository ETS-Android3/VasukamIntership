package com.saggy.vasukaminternship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Password_Input_Activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    ImageButton backButton;
    ImageButton calender;
    EditText username, name, emailId, dateofbirth;
    AppCompatButton signup, login;
    TextView errorText;

    FirebaseUser firebaseUser;
    String uid;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_input);

        backButton = findViewById(R.id.backButton);
        emailId = findViewById(R.id.email);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        errorText = findViewById(R.id.errorText);
        dateofbirth = findViewById(R.id.dob);
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        calender = findViewById(R.id.calender);

        dateofbirth.setFocusable(false);
        dateofbirth.setEnabled(false);

        backButton.setOnClickListener(view -> onBackPressed());

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        uid = firebaseUser.getUid();
        phoneNumber = firebaseUser.getPhoneNumber();

        errorText.setVisibility(View.GONE);

        login.setOnClickListener(view -> {
            startActivity(new Intent(Password_Input_Activity.this, Login_Activity.class));
            finish();
        });

        calender.setOnClickListener(view -> showDatePickerDialog());

        signup.setOnClickListener(view -> {
            if (username.getText().toString().isEmpty()){
                errorText.setVisibility(View.VISIBLE);
                errorText.setText("Enter username to continue.");
            }
            else if (name.getText().toString().isEmpty()){
                errorText.setVisibility(View.VISIBLE);
                errorText.setText("Enter your name to continue.");
            }
            else if (emailId.getText().toString().isEmpty()){
                errorText.setVisibility(View.VISIBLE);
                errorText.setText("Enter email id to continue.");
            }
            else if (dateofbirth.getText().toString().isEmpty()){
                errorText.setVisibility(View.VISIBLE);
                errorText.setText("Enter date of birth to continue");
            }
            else{
                errorText.setVisibility(View.GONE);
                Intent intent = new Intent(Password_Input_Activity.this,Home_Activity.class);
                FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("name").setValue(name.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("uid").setValue(uid);
                FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("phoneNumber").setValue(phoneNumber);
                FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("emailId").setValue(emailId.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("username").setValue(username.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("dateOfBirth").setValue(dateofbirth.getText().toString());
                startActivity(intent);
                finish();
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Password_Input_Activity.this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        dateofbirth.setText(i2 + "/" + (i1+1) + "/" + i);
    }
}