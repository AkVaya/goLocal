package com.example.android.golocalfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BuyerOrders extends AppCompatActivity {
    RecyclerView recyclerViewMyOrders;
    DatabaseReference mRef;
    FirebaseUser mUser;
    List<ProductBuyer> orderList,deliveredList;
    final static String ORDER_ID = "ORDER_ID", SHOP_NUMBER = "SHOP_NUMBER";
    HashMap<Integer,String> orderIds;
    HashMap<String, String> numbersId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyer_orders);
        recyclerViewMyOrders = findViewById(R.id.recyclerViewMyOrders);
        recyclerViewMyOrders.setHasFixedSize(true);
        recyclerViewMyOrders.setLayoutManager(new LinearLayoutManager(this));
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("BUYERS").child(mUser.getEmail().replace('.',',')).child("yourOrders");
        orderList = new ArrayList<>();
        deliveredList = new ArrayList<>();
        orderIds = new HashMap<>();
        numbersId = new HashMap<String, String>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                orderList.clear();
                deliveredList.clear();
                Integer i = 0;
                for(DataSnapshot order : snapshot.getChildren()){

                        String shopName = order.child("name").getValue().toString();
                        String totalCost = order.child("cost").getValue().toString();
                        String status = order.child("status").getValue().toString();


                        String id = order.child("key").getValue().toString();
                        String number = order.child("contact").getValue().toString();
                        if (status.equals("incomplete")) {
                            orderList.add(new ProductBuyer(shopName, totalCost, status, id));
                        } else {
                            deliveredList.add(new ProductBuyer(shopName, totalCost, status, id));
                        }

                        numbersId.put(id, number);


                }
                orderList.addAll(deliveredList);
                recyclerViewMyOrders.setAdapter(new MyOrdersAdapter(getApplicationContext(), orderList, new MyOrdersAdapter.onNoteListener() {
                    @Override
                    public void onNoteClick(int position) {
                        Intent intent = new Intent(getApplicationContext(),ViewMySpecificOrder.class);
                        intent.putExtra(ORDER_ID,orderList.get(position).getImageURL());
                        intent.putExtra(SHOP_NUMBER,numbersId.get(orderList.get(position).getImageURL()));
                        startActivity(intent);
                    }
                }));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}