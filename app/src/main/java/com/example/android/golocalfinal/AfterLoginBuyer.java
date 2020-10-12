package com.example.android.golocalfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AfterLoginBuyer extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference mRefShops,mRef;
    Toolbar toolbar ;
    RecyclerView recyclerView;
    List<shop> shopList;
    String email,city="";
    static final public String EMAIL_ID = "EMAIL_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_login_buyer);
        mAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbarForLogout);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recyclerViewShop);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        shopList = new ArrayList<>();
        mRefShops = FirebaseDatabase.getInstance().getReference().child("SELLERS");
        email = mAuth.getCurrentUser().getEmail();
        email = email.replace('.',',');
        mRef = FirebaseDatabase.getInstance().getReference().child("BUYERS").child(email).child("userCity");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuLogout :
                mAuth.signOut();
                finish();
                startActivity(new Intent(AfterLoginBuyer.this,MainActivity.class));
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu,menu);
        return true;
    }
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() == null){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    city = snapshot.getValue().toString();
                    city = city.toLowerCase();
                mRefShops.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        shopList.clear();
                        for (DataSnapshot shops : snapshot.getChildren()) {
                            boolean check = false;
                            for (DataSnapshot temp : shops.getChildren()) {
                                if (temp.getKey().equals("outletCity")) {
                                    String outletCity = temp.getValue().toString();
                                    outletCity = outletCity.toLowerCase();
                                    if (outletCity.equals(city)) {
                                        check = true;
                                    }
                                    break;
                                }
                            }
                            if (!check)
                                continue;
                            String outletName = "", outletLocality = "";
                            for (DataSnapshot temp : shops.getChildren()) {
                                if (temp.getKey().equals("outletLocality")) {
                                    outletLocality = temp.getValue().toString();
                                    continue;
                                }
                                if (temp.getKey().equals("outletName")) {
                                    outletName = temp.getValue().toString();
                                    continue;
                                }
                            }
                            shopList.add(new shop(outletName, outletLocality,shops.getKey().toString()));
                        }
                        shopAdapter shopAdapter = new shopAdapter(getApplicationContext(), shopList,new shopAdapter.onNoteListener(){
                            @Override
                            public void onNoteClick(int position) {
                                shop curr = shopList.get(position);
                                Intent intent = new Intent(AfterLoginBuyer.this,ShopSpecificInfo.class);
                                intent.putExtra(EMAIL_ID,curr.getEmail());
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(shopAdapter);
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


    }
}
