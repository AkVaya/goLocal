package com.example.android.golocalfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

import static com.example.android.golocalfinal.AfterLoginSeller.CATEGORY_NAME;

public class CategorySpecificInfo extends AppCompatActivity {
    EditText editTextProductName,editTextQuantity,editTextPrice,editTextDescription;
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    Toolbar toolbar ;
    RecyclerView recyclerView;
    TextView textViewCategory;
    List<Product> productList;
    Button buttonAdd;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_specific_info);

        mAuth = FirebaseAuth.getInstance();

        editTextProductName = (EditText) findViewById(R.id.editTextProductName);
        editTextQuantity = (EditText) findViewById(R.id.editTextQuantityAvailable);
        editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        editTextDescription = (EditText) findViewById(R.id.editTextProductDescription);
        toolbar = findViewById(R.id.toolbarForLogout);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewProducts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        buttonAdd = (Button) findViewById(R.id.buttonAddNewProduct);
        intent = getIntent();
        textViewCategory = (TextView) findViewById(R.id.textViewCategoryName);
        textViewCategory.setText(intent.getExtras().getString(CATEGORY_NAME));
        String email = intent.getExtras().getString(SellerBasicInfo.EMAIL_ID);
        String email2 = email.replace('.',',');
        mRef = FirebaseDatabase.getInstance().getReference().child("SELLERS").child(email2).child("CATEGORIES").child(String.valueOf(intent.getExtras().get(AfterLoginSeller.POSITION))).child("PRODUCTS");

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qty = (editTextQuantity.getText().toString());
                String ProductName = editTextProductName.getText().toString().trim();
                String price = editTextPrice.getText().toString();
                String description = editTextDescription.getText().toString().trim();
                if(ProductName.length()==0){
                    editTextProductName.setError("Product Name can't be empty");
                    editTextProductName.requestFocus();
                }
                else if(Integer.parseInt(qty)<0 || qty.isEmpty()){
                    editTextQuantity.setError("Enter a valid Quantity");
                    editTextQuantity.requestFocus();
                }
                else{
                    productList.add(new Product(qty,ProductName,price,description));
                    mRef.setValue(productList);
                }
                editTextDescription.setText(null);
                editTextPrice.setText(null);
                editTextProductName.setText(null);
                editTextQuantity.setText(null);

            }
        });
    }

    protected void onStart() {

        super.onStart();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String name="",quantity="",price="",description="";
                    for(DataSnapshot temp : dataSnapshot.getChildren()){
                        if(temp.getKey().equals("name"))
                            name = temp.getValue().toString();
                        if(temp.getKey().equals("quantity"))
                            quantity = temp.getValue().toString();
                        if(temp.getKey().equals("price"))
                            price = temp.getValue().toString();
                        if(temp.getKey().equals("desc"))
                            description = temp.getValue().toString();
                    }
                    productList.add(new Product(quantity,name,price,description));
                }
                ProductAdapter adapter = new ProductAdapter(getApplicationContext(), productList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}