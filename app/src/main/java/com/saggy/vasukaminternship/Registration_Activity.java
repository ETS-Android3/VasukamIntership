package com.saggy.vasukaminternship;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.Calendar;

public class Registration_Activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    ImageButton backButton;
    ImageButton calender;
    EditText username, name, phoneNumber, emailId, dateofbirth;
    AppCompatButton signup, login;
    TextView errorText;
    CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        backButton = findViewById(R.id.backButton);
        phoneNumber = findViewById(R.id.phoneNumber);
        emailId = findViewById(R.id.email);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        errorText = findViewById(R.id.errorText);
        dateofbirth = findViewById(R.id.dob);
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        ccp = findViewById(R.id.ccp);
        calender = findViewById(R.id.calender);

        dateofbirth.setFocusable(false);
        dateofbirth.setEnabled(false);
        errorText.setVisibility(View.GONE);

        backButton.setOnClickListener(view -> onBackPressed());

        login.setOnClickListener(view -> {
            startActivity(new Intent(Registration_Activity.this, Login_Activity.class));
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
            else if (phoneNumber.getText().toString().isEmpty()){
                errorText.setVisibility(View.VISIBLE);
                errorText.setText("Enter phone number to continue.");
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
//                FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.child("Users").child(username.getText().toString()).exists()){
//                            errorText.setVisibility(View.VISIBLE);
//                            errorText.setText("Username is already in use, try using different username");
//                        }
//                        else{
                            errorText.setVisibility(View.GONE);
                            Intent intent = new Intent(Registration_Activity.this,Otp_Verification_Activity.class);
                            intent.putExtra("name",name.getText().toString());
                            intent.putExtra("temp",2);
                            intent.putExtra("username",username.getText().toString());
                            intent.putExtra("emailId",emailId.getText().toString());
                            intent.putExtra("phoneNumber",ccp.getSelectedCountryCodeWithPlus()+phoneNumber.getText().toString());
                            intent.putExtra("dateOfBirth",dateofbirth.getText().toString());
                            startActivity(intent);
                            finish();
//                        }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                    }
//                });
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Registration_Activity.this,
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
