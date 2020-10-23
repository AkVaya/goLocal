package com.example.android.golocalfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

public class EditBuyerProfile extends AppCompatActivity {

    EditText name, contact, city, address;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_buyer_profile);

        name = findViewById(R.id.editTextBuyerName);
        address = findViewById(R.id.editTextBuyerAddress);
        city = findViewById(R.id.editTextBuyerCity);
        contact = findViewById(R.id.editTextBuyerNumber);
        confirm = findViewById(R.id.button2);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        setDetails();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmChanges();
            }
        });

    }

    private void setDetails() {
        final String email = mUser.getEmail().replace(".", ",");
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

    public void ConfirmChanges() {


        String Name = name.getText().toString().trim();
        String Address = address.getText().toString().trim();
        String City = city.getText().toString().trim();
        String Number = contact.getText().toString().trim();

        if (Name.isEmpty()) {
            name.setError("This field cannot remain empty");
            name.requestFocus();
            return;
        }
        if (Address.isEmpty()) {
            address.setError("This field cannot remain empty");
            address.requestFocus();
            return;
        }
        if (City.isEmpty()) {
            city.setError("This field cannot remain empty");
            city.requestFocus();
            return;
        }
        if (Number.isEmpty()) {
            contact.setError("This field cannot remain empty");
            contact.requestFocus();
            return;
        }

        final String Email = mUser.getEmail().replace('.', ',');

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("BUYERS").child(Email);
        mRef.child("userName").setValue(Name);
        mRef.child("userAddress").setValue(Address);
        mRef.child("userCity").setValue(City);
        mRef.child("userNumber").setValue(Number);
        Toast.makeText(getApplicationContext(), "Details updated successfully!", Toast.LENGTH_SHORT).show();

        finish();
    }

}




