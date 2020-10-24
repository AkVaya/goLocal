package com.example.android.golocalfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AfterLoginBuyer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth mAuth;
    DatabaseReference mRefShops,mRef;
    Toolbar toolbar ;
    RecyclerView recyclerView;
    List<shop> shopList;
    String email,city="";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FloatingActionButton support;
    static final public String EMAIL_ID = "EMAIL_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_login_buyer);

        support = findViewById(R.id.floatingActionButton);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recyclerViewShop);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        shopList = new ArrayList<>();
        mRefShops = FirebaseDatabase.getInstance().getReference().child("SELLERS");
        email = mAuth.getCurrentUser().getEmail();
        email = email.replace('.',',');
        mRef = FirebaseDatabase.getInstance().getReference().child("BUYERS").child(email).child("userCity");

        //NavigationDrawer
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbarForLogout);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"golocalsupport@googlegroups.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT,"REPORT A BUG/ NEED HELP");
                startActivity(Intent.createChooser(intent, "CONTACT US"));
            }
        });

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

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            super.onBackPressed();

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.nav_home :
                drawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_fav :
                startActivity(new Intent(AfterLoginBuyer.this, Favourites.class));
                break;
            case R.id.nav_orders :
                startActivity(new Intent(AfterLoginBuyer.this, BuyerOrders.class));
                break;


            case R.id.nav_profile :
                startActivity(new Intent(AfterLoginBuyer.this, ViewBuyerProfile.class));
                break;

            case R.id.nav_logout :
                mAuth.signOut();
                finish();
                startActivity(new Intent(AfterLoginBuyer.this,MainActivity.class));
                break;
    /*
            case R.id.nav_share :
                startActivity(new Intent(AfterLoginBuyer.this, Developers.class));
                break;
            case R.id.nav_rate :
                startActivity(new Intent(AfterLoginSeller.this, OrdersReceived.class));
                break;
      */
            case R.id.nav_password :
                startActivity(new Intent(AfterLoginBuyer.this, ForgotPassword.class));
                break;

            case R.id.nav_developers :
                startActivity(new Intent(AfterLoginBuyer.this, Developers.class));
                break;

            case R.id.nav_about :
                startActivity(new Intent(AfterLoginBuyer.this, About.class));
                break;

        }
        return true;
    }
}
