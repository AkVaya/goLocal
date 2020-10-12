package com.example.android.golocalfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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


public class AfterLoginSeller extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference mRef,mRef2;
    Toolbar toolbar ;
    RecyclerView recyclerView;
    EditText editTextCategory;
    List<Category>categoryList;
    ImageView buttonAdd;
    Intent intent;

    static final public String CATEGORY_NAME = "CATEGORY_NAME", EMAIL_ID="EMAIL_ID",POSITION="POSITION";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_login_seller);
        mAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbarForLogout);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewCategories);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryList = new ArrayList<>();
        buttonAdd = (ImageView) findViewById(R.id.buttonAddNewCategory);
        intent = getIntent();
        mRef = FirebaseDatabase.getInstance().getReference().child("SELLERS").child(intent.getExtras().getString(EMAIL_ID).replace('.',',')).child("CATEGORIES");
        editTextCategory = (EditText) findViewById(R.id.editTextCategoryName);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = intent.getExtras().getString(SellerBasicInfo.EMAIL_ID);
                String email2 = email.replace('.',',');
                String category = editTextCategory.getText().toString().trim();
                if(category.length()!=0){
                    categoryList.add(new Category(category));
                    mRef.setValue(categoryList);
                }
                else{
                    editTextCategory.setError("Name of category can't be empty");
                    editTextCategory.requestFocus();
                }
            }
        });
    }
    protected void onStart() {
        super.onStart();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    for(DataSnapshot curr : dataSnapshot.getChildren()){
                        if(curr.getKey().equals("categoryName")){
                            String categoryName = curr.getValue().toString();
                            categoryList.add(new Category(categoryName));
                        }
                    }
                }
                CategoryAdapter  adapter = new CategoryAdapter(getApplicationContext(), categoryList, new CategoryAdapter.onNoteListener() {
                    @Override
                    public void onNoteClick(int position) {
                        Intent intent2 = new Intent(getApplicationContext(),CategorySpecificInfo.class);
                        intent2.putExtra(POSITION,position);
                        intent2.putExtra(EMAIL_ID,intent.getExtras().getString(SellerBasicInfo.EMAIL_ID));
                        intent2.putExtra(CATEGORY_NAME,categoryList.get(position).getCategoryName());
                        startActivity(intent2);
                    }
                });
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuLogout :
                mAuth.signOut();
                finish();
                startActivity(new Intent(AfterLoginSeller.this,MainActivity.class));
                break;
        }
        return true;
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu,menu);
        return true;
    }
}