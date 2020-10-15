package com.example.android.golocalfinal;

import androidx.annotation.NonNull;
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

import java.lang.ref.Reference;

public class ViewSellerProfile extends AppCompatActivity {

    DatabaseReference mRef;
   FirebaseAuth mAuth;
   TextView outletName, outletAddress, outletLocality, outletCity, ownerName, ownerContact;
   Button editProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_seller_profile);
        mAuth = FirebaseAuth.getInstance();
        outletName = (TextView) findViewById(R.id.shopName);
        outletCity = (TextView) findViewById(R.id.shopCity);
        outletAddress = (TextView) findViewById(R.id.shopAddress);
        outletLocality = (TextView) findViewById(R.id.shopLocality);
        ownerName = (TextView) findViewById(R.id.ownerName);
        ownerContact = (TextView) findViewById(R.id.ownerContact);
        editProfile = (Button) findViewById(R.id.editProfile);
        setDetails();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewSellerProfile.this, EditSellerProfile.class));
            }
        });

    }

    public void setDetails(){

        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();
        final String email2 = email.replace(".",",");
        FirebaseDatabase.getInstance().getReference().child("SELLERS").child(email2).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Name = snapshot.child("outletName").getValue().toString();
                String City = snapshot.child("outletCity").getValue().toString();
                String Address = snapshot.child("outletAddress").getValue().toString();
                String Locality = snapshot.child("outletLocality").getValue().toString();
                String outletNumber = snapshot.child("outletNumber").getValue().toString();
                String outletContactName = snapshot.child("outletContactName").getValue().toString();

                outletName.setText(Name);
                outletCity.setText(City);
                outletAddress.setText(Address);
                outletLocality.setText(Locality);
                ownerContact.setText(outletNumber);
                ownerName.setText(outletContactName);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}