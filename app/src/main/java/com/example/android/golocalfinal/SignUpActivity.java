package com.example.android.golocalfinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class SignUpActivity extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    ProgressBar progressBar;
    Button buyer,seller;
    TextView login;
    static final public String EMAIL_ID="EMAIL_ID",PASSWORD="PASSWORD";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();
        buyer = (Button) findViewById(R.id.buttonSignUpBuyer);
        seller = (Button) findViewById(R.id.buttonSignUpSeller);
        login = (TextView) findViewById(R.id.login);
        buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyerLogin();
            }
        });
        seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellerLogin();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

    }

    private void BuyerLogin() {
        final String Email = editTextEmail.getText().toString(), Password = editTextPassword.getText().toString();
        if (Email.isEmpty()) {
            editTextEmail.setError("Email should be entered");
        }
        if (Password.length() < 6) {
            editTextPassword.setError("The length of Password should at least be 6");
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            editTextEmail.setError("Enter a Valid Email Address");
        }
        if(Password.length() >= 6 && Patterns.EMAIL_ADDRESS.matcher(Email).matches()){

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
            });
            Intent intent = new Intent(this,BuyerBasicInfo.class);
            intent.putExtra(EMAIL_ID,Email);
            intent.putExtra(PASSWORD,Password);
            startActivity(intent);
        }
    }

    private void SellerLogin() {
        final String Email = editTextEmail.getText().toString().trim(), Password = editTextPassword.getText().toString().trim();
        if (Email.isEmpty()) {
            editTextEmail.setError("Email should be entered");
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            editTextEmail.setError("Enter a Valid Email Address");
        }
        if (Password.length() < 6) {
            editTextPassword.setError("The length of Password should at least be 6");
        }
        if(Password.length() >= 6 && Patterns.EMAIL_ADDRESS.matcher(Email).matches()){

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
            });
            Intent intent = new Intent(this,SellerBasicInfo.class);
            intent.putExtra(EMAIL_ID,Email);
            intent.putExtra(PASSWORD,Password);
            finish();
            startActivity(intent);
        }
    }
}