package com.example.android.golocalfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewBuyerProfile extends AppCompatActivity {

    TextView name,contact,address,city;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Button editProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_buyer_profile);

        name = findViewById(R.id.buyerName);
        contact = findViewById(R.id.buyerContact);
        address = findViewById(R.id.buyerAddress);
        city = findViewById(R.id.buyerCity);
        editProfile = findViewById(R.id.editProfileBuyer);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        setDetails();
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewBuyerProfile.this,EditBuyerProfile.class));
            }
        });

    }

    private void setDetails(){
        final String email = mUser.getEmail().toString().replace(".",",");
        FirebaseDatabase.getInstance().getReference().child("BUYERS").child(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.child("userName").getValue().toString());
                city.setText(snapshot.child("userCity").getValue().toString());
                address.setText(snapshot.child("userAddress").getValue().toString());
                contact.setText(snapshot.child("userNumber").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}