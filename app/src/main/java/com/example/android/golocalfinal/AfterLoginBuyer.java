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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AfterLoginBuyer extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    Toolbar toolbar ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_login_buyer);
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();
        toolbar = findViewById(R.id.toolbarForLogout);
        setSupportActionBar(toolbar);
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
    }
}

// thanks for contribution
