package com.example.android.golocalfinal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.android.golocalfinal.AfterLoginBuyer.EMAIL_ID;

public class ShopSpecificInfo extends AppCompatActivity {
    RecyclerView recyclerView;
    Spinner spinnerCategories;
    DatabaseReference mRef,mRef2;
    Intent intent;
    HashMap<String,Integer> categoryCorrespondence;
    List<String> categoryList;
    List<ProductBuyer> productList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_specific_info);

        recyclerView = findViewById(R.id.recyclerViewDisplayProducts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        spinnerCategories = findViewById(R.id.spinner_categories);

        intent = getIntent();
        categoryCorrespondence = new HashMap<>();
        String Email = intent.getExtras().getString(EMAIL_ID);
        Email = Email.replace('.',',');
        mRef = FirebaseDatabase.getInstance().getReference().child("SELLERS").child(Email).child("CATEGORIES");
        categoryList = new ArrayList<>();
        productList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String name = null;
                    Integer index = Integer.parseInt(dataSnapshot.getKey());
                    for(DataSnapshot temp : dataSnapshot.getChildren()){
                        if(temp.getKey().equals("categoryName")){
                            name = temp.getValue().toString();
                            break;
                        }
                    }
                    if(name==null)
                        continue;
                    categoryList.add(name);
                    categoryCorrespondence.put(name, index);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategories.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                mRef2 = mRef.child(String.valueOf(categoryCorrespondence.get(selectedItem))).child("PRODUCTS");
                mRef2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        productList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String name = dataSnapshot.child("name").getValue().toString(),
                                    price = dataSnapshot.child("price").getValue().toString(),
                                    description = dataSnapshot.child("desc").getValue().toString();

                            productList.add(new ProductBuyer(name,price,description));
                        }
                        ViewProductAdapter adapter = new ViewProductAdapter(getApplicationContext(), productList, new ViewProductAdapter.onNoteListener() {
                            @Override
                            public void onNoteClick(int position) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ShopSpecificInfo.this, R.style.AlertDialog);
                                ProductBuyer productBuyer = productList.get(position);
                                builder.setTitle("Enter Quantity for " + productBuyer.getName() );
                                final EditText editTextQuantity = new EditText(ShopSpecificInfo.this);
                                editTextQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
                                editTextQuantity.setHint("Enter Quantity");
                                builder.setView(editTextQuantity);
                                builder.setPositiveButton("Add To cart", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        
                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
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
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
