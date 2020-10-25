package com.example.android.golocalfinal;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


public class PendingOrdersSeller extends Fragment {

    FirebaseAuth mAuth;
    DatabaseReference mRef;
    FirebaseUser mUser;
    Intent intent;
    List<ProductBuyer> pendingList;
    RecyclerView recyclerView;
    final static String ORDER_ID = "ORDER_ID";
    HashMap<Integer,String> ids;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pending_orders_seller,container,false);
        recyclerView = view.findViewById(R.id.pendingOrders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pendingList = new ArrayList<>();
        ids = new HashMap<>();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String email = mUser.getEmail().toString().replace(".",",");
        mRef = FirebaseDatabase.getInstance().getReference().child("SELLERS").child(email).child("Orders");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pendingList.clear();

                Integer i=0;
                for(DataSnapshot order : snapshot.getChildren()){

                    String id = order.child("key").getValue().toString();
                    String address = order.child("buyerAddress").getValue().toString();
                    String name = order.child("buyerName").getValue().toString();
                    String cost = order.child("totalCost").getValue().toString();
                    String status = order.child("status").getValue().toString();
                    String email = order.child("buyerEmail").getValue().toString();
                    String number = order.child("buyerEmail").getValue().toString();
                    String image="";

                    if(status.equals("incomplete")){
                        pendingList.add(new ProductBuyer(name,cost,address,image));
                    }
                    ids.put(i,id);
                    i++;

                }
                recyclerView.setAdapter(new SellerOrdersAdapter(getContext(), pendingList, new SellerOrdersAdapter.onNoteListener() {
                    @Override
                    public void onNoteClick(int position) {
                        Intent intent = new Intent(getContext(), SellerSpecificOrderPending.class);
                        intent.putExtra(ORDER_ID,ids.get(position));
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