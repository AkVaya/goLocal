package com.example.android.golocalfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BuyerBasicInfo extends AppCompatActivity {
    Intent intent;
    EditText userName,userAddress,userCity,userNumber;
    Button buttonConfirm;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    BuyerInformation buyerInformation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyer_basic_info_final);
        userAddress = (EditText) findViewById(R.id.userAddress);
        userName = (EditText) findViewById(R.id.userName);
        userCity = (EditText) findViewById(R.id.userCity);
        userNumber = (EditText) findViewById(R.id.userNumber);
        buttonConfirm = (Button) findViewById(R.id.buttonConfirm);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();
        intent = getIntent();
        final String Email = intent.getExtras().getString(SignUpActivity.EMAIL_ID);
        final String Password = intent.getExtras().getString(SignUpActivity.PASSWORD);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo(Email,Password);
            }
        });
    }

    public void saveInfo(final String Email, String Password){
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    finish();
                    mRef = FirebaseDatabase.getInstance().getReference();
                    String email2 = Email.replace('.', ',');
                    mRef.child("TYPE").child(email2).setValue("BUYER");

                    buyerInformation = new BuyerInformation(Email,userName.getText().toString().trim(),userAddress.getText().toString().trim(),userCity.getText().toString().trim(),
                            userNumber.getText().toString().trim());
                    mRef.child("BUYERS").child(email2).setValue(buyerInformation);
                    Intent intent = new Intent(BuyerBasicInfo.this, AfterLoginBuyer.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_SHORT).show();
                    startActivity(intent);

                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
