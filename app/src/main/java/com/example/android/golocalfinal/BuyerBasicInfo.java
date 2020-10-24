package com.example.android.golocalfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BuyerBasicInfo extends AppCompatActivity {
    Intent intent;
    EditText userName,userAddress,userNumber;
    Spinner userCity;
    Button buttonConfirm;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    List<String> cityList;
    BuyerInformation buyerInformation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyer_basic_info_final);
        userAddress =  findViewById(R.id.userAddress);
        userName =  findViewById(R.id.userName);
        userCity =  findViewById(R.id.spinnerUserCity);
        userCity.setPrompt("Select City");
        userNumber =  findViewById(R.id.userNumber);
        buttonConfirm =  findViewById(R.id.buttonConfirm);
        progressBar =  findViewById(R.id.progressbar);
        mRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        intent = getIntent();
        cityList = new ArrayList<>();
        final String Email = intent.getExtras().getString(SignUpActivity.EMAIL_ID);
        final String Password = intent.getExtras().getString(SignUpActivity.PASSWORD);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo(Email,Password);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRef.child("SELLERS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cityList.clear();
                for(DataSnapshot shops : snapshot.getChildren()){
                    String city = shops.child("outletCity").getValue().toString();
                    cityList.add(city);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, cityList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                userCity.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void saveInfo(final String Email, String Password){
        if(userName.getText().toString().isEmpty()){
            userName.setError("Name can't be empty");
            userName.requestFocus();
        }
        else if(userAddress.getText().toString().isEmpty()){
            userAddress.setError("Address can't be empty");
            userAddress.requestFocus();
        }
        else if(userNumber.getText().toString().isEmpty() || userNumber.getText().toString().length()!=10){
            userNumber.setError("Enter a valid number");
            userAddress.requestFocus();
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        finish();
                        String email2 = Email.replace('.', ',');
                        mRef.child("TYPE").child(email2).setValue("BUYER");
                        buyerInformation = new BuyerInformation(Email, userName.getText().toString().trim(), userAddress.getText().toString().trim(), userCity.getSelectedItem().toString(),
                                userNumber.getText().toString().trim());
                        mRef.child("BUYERS").child(email2).setValue(buyerInformation);
                        Intent intent = new Intent(BuyerBasicInfo.this, AfterLoginBuyer.class);
                        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
