package com.saggy.vasukaminternship;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

public class Login_Activity extends AppCompatActivity {
    ImageButton backButton;
    CountryCodePicker ccp;
    EditText phoneNumber;
    AppCompatButton login;
    AppCompatButton signup;
    TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        errorText = findViewById(R.id.errorText);
        backButton = findViewById(R.id.backButton);
        ccp = findViewById(R.id.ccp);
        phoneNumber = findViewById(R.id.phoneNumber);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);



        signup.setOnClickListener(view -> {
            startActivity(new Intent(Login_Activity.this, Registration_Activity.class));
            finish();
        });

        login.setOnClickListener(view -> {
            if (phoneNumber.getText().toString().isEmpty()){
                errorText.setVisibility(View.VISIBLE);
                errorText.setText("Please enter mobile number");
            }
            else{
                errorText.setVisibility(View.GONE);
                Intent intent = new Intent(Login_Activity.this, Otp_Verification_Activity.class);
                intent.putExtra("phoneNumber",ccp.getSelectedCountryCodeWithPlus()+phoneNumber.getText().toString());
                intent.putExtra("temp",1);
                startActivity(intent);
                finish();
            }
        });

        backButton.setOnClickListener(view -> onBackPressed());
    }
}