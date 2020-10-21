package com.example.android.golocalfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ViewCart extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference mRef,mRefSellerProducts,mRefSeller;
    List<ProductCart> shoppingCart,temp;
    Button buttonPlaceOrder;
    String email;
    Boolean check;
    HashMap<String,Boolean> store;
    Integer index;
    FirebaseUser mUser ;
    String Name,PhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);
        recyclerView = findViewById(R.id.recyclerViewCart);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        shoppingCart = new ArrayList<>();
        temp = new ArrayList<>();
        buttonPlaceOrder = findViewById(R.id.buttonPlaceOrder);
        email = getIntent().getExtras().getString(ShopSpecificInfo.EMAIL_ID);
        String mail = email;
        mRef = FirebaseDatabase.getInstance().getReference().child("BUYERS").child((FirebaseAuth.getInstance().getCurrentUser().getEmail().replace('.',',')));
        mRefSellerProducts = FirebaseDatabase.getInstance().getReference().child("SELLERS").child(email).child("CATEGORIES");
        mRefSeller = FirebaseDatabase.getInstance().getReference().child("SELLERS").child(email);
        check = true;
        index = 0;
        store = new HashMap<>();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });
    }

    private void placeOrder() {
        if(check.equals(false)){
            AlertDialog.Builder builder = new AlertDialog.Builder(ViewCart.this, R.style.AlertDialog);
            builder.setTitle("Sorry!");
            final TextView textViewMessage = new TextView(ViewCart.this);
            
            textViewMessage.setText("Can't Place Order : Quantity Entered is more than Available");
            builder.setView(textViewMessage);
            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.show();
        }
        else{
            String key = mRefSeller.push().getKey();
            mRefSeller.child("pendingOrders").child(key).child("list").setValue(shoppingCart);
            mRefSeller.child("pendingOrders").child(key).child("name").setValue(Name);
            mRefSeller.child("pendingOrders").child(key).child("number").setValue(PhoneNumber);
            mRef.child("yourOrders").child(key).setValue(shoppingCart);
            shoppingCart.clear();
            mRef.child("CART").child(email).setValue(shoppingCart);
            Toast.makeText(getApplicationContext(),"Order Place",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ViewCart.this,AfterLoginBuyer.class));
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mRef.child("CART").child(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                temp.clear();
                shoppingCart.clear();
                for (DataSnapshot single : snapshot.getChildren()) {
                    final String categoryIndex, index, name, price, quantity;
                    categoryIndex = single.child("itemCategoryIndex").getValue().toString();
                    index = single.child("itemIndex").getValue().toString();
                    name = single.child("itemName").getValue().toString();
                    quantity = single.child("itemQuantity").getValue().toString();
                    price = single.child("itemPrice").getValue().toString();
                    temp.add(new ProductCart(categoryIndex, index, name, price, quantity, false));
                }
                mRefSellerProducts.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (ProductCart x : temp) {
                            final String categoryIndex, index, name, price, quantity;
                            categoryIndex = x.getItemCategoryIndex();
                            index = x.getItemIndex();
                            name = x.getItemName();
                            quantity = x.getItemQuantity();
                            price = x.getItemPrice();
                            String available = snapshot.child(categoryIndex).child("PRODUCTS").child(index).child("quantity").getValue().toString();
                            if (Integer.parseInt(available) < Integer.parseInt(quantity)) {
                                shoppingCart.add(new ProductCart(categoryIndex, index, name, price, quantity, true));
                                check = false;
                            } else
                                shoppingCart.add(new ProductCart(categoryIndex, index, name, price, quantity, false));
                        }
                        CartAdapter adapter = new CartAdapter(getApplicationContext(), shoppingCart);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Name = snapshot.child("userName").getValue().toString();
                PhoneNumber = snapshot.child("userNumber").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}