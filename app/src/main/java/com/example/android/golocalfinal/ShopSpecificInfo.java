package com.example.android.golocalfinal;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.example.android.golocalfinal.AfterLoginBuyer.EMAIL_ID;

public class ShopSpecificInfo extends AppCompatActivity {
    ImageView callShop;
    TextView shopName;
    RecyclerView recyclerView;
    Spinner spinnerCategories;
    Button viewCart;
    DatabaseReference mRef,mRef2,mRefCart,mRefSellerProducts;
    Intent intent;
    HashMap<String,Integer> categoryCorrespondence;
    List<String> categoryList;
    List<ProductBuyer> productList;
    List<ProductCart> shoppingCart;
    public static final String EMAIL_ID = "EMAIL_ID";
    Set<String> all;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_specific_info);

        callShop = (ImageView) findViewById(R.id.callShop);
        shopName = (TextView) findViewById(R.id.textViewShop);
        viewCart = findViewById(R.id.buttonViewCart);
        recyclerView = findViewById(R.id.recyclerViewDisplayProducts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        spinnerCategories = findViewById(R.id.spinner_categories);

        intent = getIntent();
        categoryCorrespondence = new HashMap<>();
        String Email = intent.getExtras().getString(EMAIL_ID);
        Email = Email.replace('.',',');

        FirebaseDatabase.getInstance().getReference().child("SELLERS").child(Email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Name = snapshot.child("outletName").getValue().toString();
                shopName.setText(Name);
                final String PhoneNumber = snapshot.child("outletNumber").getValue().toString();
                callShop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uri = "tel:" + PhoneNumber.trim() ;
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse(uri));
                        startActivity(intent);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        mRef = FirebaseDatabase.getInstance().getReference().child("SELLERS").child(Email).child("CATEGORIES");
        mRefSellerProducts = FirebaseDatabase.getInstance().getReference().child("SELLERS").child(Email).child("CATEGORIES");
        mRefCart = FirebaseDatabase.getInstance().getReference().child("BUYERS").child((FirebaseAuth.getInstance().getCurrentUser().getEmail().replace('.',','))).child("CART").child(Email);
        categoryList = new ArrayList<>();
        productList = new ArrayList<>();
        shoppingCart = new ArrayList<>();
        all = new HashSet<>();

        final String finalEmail1 = Email;
        viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopSpecificInfo.this,ViewCart.class);
                intent.putExtra(EMAIL_ID, finalEmail1);
                startActivity(intent);
            }
        });
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
                    name = dataSnapshot.child("categoryName").getValue().toString();
                    if( name==null)
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
        mRefCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shoppingCart.clear();
                for(DataSnapshot single : snapshot.getChildren()){
                    String categoryIndex,index,name,price,quantity;
                    categoryIndex = single.child("itemCategoryIndex").getValue().toString();
                    index = single.child("itemIndex").getValue().toString();
                    name = single.child("itemName").getValue().toString();
                    price = single.child("itemPrice").getValue().toString();
                    quantity = single.child("itemQuantity").getValue().toString();
                    shoppingCart.add(new ProductCart(categoryIndex,index,name,price,quantity,false));
                    if(!name.isEmpty()){
                        boolean temp = all.add(name);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                final HashMap<String,String> productCorrespondence = new HashMap<>();
                final String selectedItem = parent.getItemAtPosition(position).toString();
                mRef2 = mRef.child(String.valueOf(categoryCorrespondence.get(selectedItem))).child("PRODUCTS");
                mRef2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        productList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String index = dataSnapshot.getKey();
                            String name = dataSnapshot.child("name").getValue().toString(),
                                    price = dataSnapshot.child("price").getValue().toString(),
                                    description = dataSnapshot.child("desc").getValue().toString(),
                                    image = dataSnapshot.child("imageURL").getValue().toString();

                            productList.add(new ProductBuyer(name,price,description,image));
                            productCorrespondence.put(name,index);
                        }
                        ViewProductAdapter adapter = new ViewProductAdapter(getApplicationContext(), productList, new ViewProductAdapter.onNoteListener() {
                            @Override
                            public void onNoteClick(int position) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ShopSpecificInfo.this, R.style.AlertDialog);
                                final ProductBuyer productBuyer = productList.get(position);
                                builder.setTitle("Enter Quantity for " + productBuyer.getName() );
                                final EditText editTextQuantity = new EditText(ShopSpecificInfo.this);
                                editTextQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
                                if(!all.contains(productBuyer.getName()))
                                    editTextQuantity.setHint("Enter Quantity");
                                else{
                                    for(ProductCart curr : shoppingCart){
                                        if(curr.getItemName().equals(productBuyer.getName())) {
                                            editTextQuantity.setText(curr.getItemQuantity());
                                            break;
                                        }
                                    }
                                }
                                builder.setView(editTextQuantity);

                                builder.setPositiveButton("Add To cart", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String qty = editTextQuantity.getText().toString();
                                        if(qty.isEmpty()){
                                            editTextQuantity.setError("Enter A valid quantity");
                                            editTextQuantity.requestFocus();
                                        } else{
                                            if(!all.contains(productBuyer.getName())) {
                                                shoppingCart.add(new ProductCart(String.valueOf(categoryCorrespondence.get(selectedItem)), productCorrespondence.get(productBuyer.getName()), productBuyer.getName(), productBuyer.getPrice(), qty,false));
                                            }
                                            else{
                                                for(ProductCart curr : shoppingCart){
                                                    if(curr.getItemName().equals(productBuyer.getName())) {
                                                        curr.setItemQuantity(qty);
                                                        break;
                                                    }
                                                }
                                            }
                                            mRefCart.setValue(shoppingCart);
                                        }
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
