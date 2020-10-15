package com.example.android.golocalfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditSellerProfile extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mRef;
    FirebaseUser mUser;
    Button confirmChanges;
    EditText shopName,shopLocality,shopAddress,shopCity,ownerName,ownerNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_seller_profile);

        shopAddress = (EditText) findViewById(R.id.outletAddress);
        shopName = (EditText) findViewById(R.id.outletName);
        shopLocality = (EditText) findViewById(R.id.outletLocality);
        shopCity = (EditText) findViewById(R.id.outletCity);
        ownerName = (EditText) findViewById(R.id.outletContactName);
        ownerNumber = (EditText) findViewById(R.id.outletContact);
        confirmChanges = (Button) findViewById(R.id.button);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        ConfirmChanges();


    }

    public void ConfirmChanges(){

        confirmChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = shopName.getText().toString().trim();
                String address = shopAddress.getText().toString().trim();
                String city = shopCity.getText().toString().trim();
                String locality = shopLocality.getText().toString().trim();
                String owner = ownerName.getText().toString().trim();
                String number = ownerNumber.getText().toString().trim();

                if(name.isEmpty())
                    shopName.setError("This field cannot remain empty");
                if(address.isEmpty())
                    shopAddress.setError("This field cannot remain empty");
                if(city.isEmpty())
                    shopCity.setError("This field cannot remain empty");
                if(locality.isEmpty())
                    shopLocality.setError("This field cannot remain empty");
                if(owner.isEmpty())
                    ownerName.setError("This field cannot remain empty");
                if(number.isEmpty())
                    ownerNumber.setError("This field cannot remain empty");

                String Email = mUser.getEmail();
                String email2 = Email.replace('.', ',');

                mRef = FirebaseDatabase.getInstance().getReference().child("SELLERS").child(email2);
                mRef.child("outletName").setValue(name);
                mRef.child("outletAddress").setValue(address);
                mRef.child("outletLocality").setValue(locality);
                mRef.child("outletCity").setValue(city);
                mRef.child("outletNumber").setValue(number);
                mRef.child("outletContactName").setValue(owner);
                Toast.makeText(getApplicationContext(),"Details updated successfully!",Toast.LENGTH_SHORT).show();

            }
        });



    }



}