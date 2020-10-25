package com.example.android.golocalfinal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SellerSpecificOrderDelivered extends AppCompatActivity {

    Intent intent;
    RecyclerView recyclerView;
    TextView buyerName,buyerAddress,totalCost;
    ImageView callButton, emailButton;
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    List<ProductCart> items;

    FirebaseUser mUser;
    String email,contactNumber,buyerEmail,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_seller_specific_order);
        intent = getIntent();
        items = new ArrayList<>();
        buyerAddress = findViewById(R.id.buyerAddress2);
        buyerName = findViewById(R.id.buyerName2);
        totalCost = findViewById(R.id.buyerTotal2);
        callButton = findViewById(R.id.callShopBuyer);
        emailButton = findViewById(R.id.mailShopBuyer);
        contactNumber = intent.getExtras().getString(DeliveredOrdersSeller.NUMBER);
        buyerEmail = intent.getExtras().getString(DeliveredOrdersSeller.EMAIL_ID);
        id = intent.getExtras().getString(DeliveredOrdersSeller.ORDER_ID);
        recyclerView = findViewById(R.id.recyclerViewSpecificOrderSeller);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        email = mUser.getEmail().replace(".",",");
        mRef = FirebaseDatabase.getInstance().getReference().child("SELLERS").child(email).child("Orders").child(id);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                buyerAddress.setText(snapshot.child("buyerAddress").getValue().toString());
                buyerName.setText(snapshot.child("buyerName").getValue().toString());
                totalCost.setText(snapshot.child("totalCost").getValue().toString());

                callButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uri = "tel:" + snapshot.child("buyerNumber").getValue().toString().trim() ;
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse(uri));
                        startActivity(intent);
                    }
                });
                emailButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {snapshot.child("buyerEmail").getValue().toString().replace(",",".")});
                        intent.putExtra(Intent.EXTRA_SUBJECT,"contact buyer for delivery details");
                        startActivity(Intent.createChooser(intent, "CONTACT BUYER"));

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        mRef.child("buyerList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for(DataSnapshot item : snapshot.getChildren()) {
                    final String productName, productQuant, productPrice;
                    productName = item.child("itemName").getValue().toString();
                    productQuant = item.child("itemQuantity").getValue().toString();
                    productPrice = item.child("itemPrice").getValue().toString();

                    items.add(new ProductCart(null,null,productName,productPrice,productQuant,false));
                }
                recyclerView.setAdapter(new CartAdapter(getApplicationContext(),items));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
