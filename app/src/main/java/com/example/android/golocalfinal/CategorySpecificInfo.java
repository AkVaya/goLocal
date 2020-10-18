package com.example.android.golocalfinal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    FirebaseUser user;
    Toolbar toolbar ;
    RecyclerView recyclerView;
    TextView textViewCategory;
    List<Product> productList;
    Button buttonAdd;
    Intent intent;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_specific_info);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

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
                ProductAdapter adapter = new ProductAdapter(getApplicationContext(), productList, new ProductAdapter.clickHandler() {
                    @Override
                    public void onLongClick(int position) {
                        showUpdateDialog(position);
                    }
                });
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showUpdateDialog(final int position){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_product , null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final EditText editTextQuantity = (EditText) dialogView.findViewById(R.id.editTextQuantity);
        final EditText editTextPrice = (EditText) dialogView.findViewById(R.id.editTextPrice);
        final EditText editTextDescription = (EditText) dialogView.findViewById(R.id.editTextDescription);
        final Button update = (Button) dialogView.findViewById(R.id.updateProduct);
        final Button delete = (Button) dialogView.findViewById(R.id.deleteProduct);

        String ProductName = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.textViewProduct)).getText().toString();
        String ProductQuantity = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.textViewQuantity)).getText().toString();
        String ProductPrice = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.textViewPrice)).getText().toString();
        String ProductDescription = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.textViewDescription)).getText().toString();

        editTextDescription.setText(ProductDescription);
        editTextPrice.setText(ProductPrice);
        editTextName.setText(ProductName);
        editTextQuantity.setText(ProductQuantity);

        final String email = user.getEmail();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextName.getText().toString().isEmpty()){
                    editTextName.setError("Can't be empty");
                    editTextName.requestFocus();
                }else if(editTextDescription.getText().toString().isEmpty()){
                    editTextDescription.setError("Can't be empty");
                    editTextDescription.requestFocus();
                }else if(editTextPrice.getText().toString().isEmpty()){
                    editTextPrice.setError("Can't be empty");
                    editTextPrice.requestFocus();
                }else if(editTextQuantity.getText().toString().isEmpty() || Integer.parseInt(editTextQuantity.getText().toString())<0){
                    editTextQuantity.setError("Can't be empty");
                    editTextQuantity.requestFocus();
                }
                else {
                    mRef.child(String.valueOf(position)).child("desc").setValue(editTextDescription.getText().toString());
                    mRef.child(String.valueOf(position)).child("quantity").setValue(editTextQuantity.getText().toString());
                    mRef.child(String.valueOf(position)).child("price").setValue(editTextPrice.getText().toString());
                    mRef.child(String.valueOf(position)).child("name").setValue(editTextName.getText().toString());
                    Toast.makeText(getApplicationContext(), "Product Updated!", Toast.LENGTH_SHORT).show();
                    recyclerView.getAdapter().notifyItemChanged(position);
                    alertDialog.dismiss();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog diaBox = AskOption(position);
                diaBox.show();
            }
        });


        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private AlertDialog AskOption(final int position)
    {
        final String ProductName = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.textViewProduct)).getText().toString();
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                .setTitle("CAUTION")
                .setMessage("Are you sure you want to delete the product " + ProductName + "?")
                .setIcon(R.drawable.ic_baseline_delete_24)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        productList.remove(position);
                        String email = user.getEmail().replace("." , ",");
                        mRef.setValue(productList);
                        recyclerView.getAdapter().notifyItemRemoved(position);
                        recyclerView.getAdapter().notifyItemRangeChanged(position, productList.size());
                        Toast.makeText(getApplicationContext(),ProductName + " Deleted!",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        alertDialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        return myQuittingDialogBox;
    }



}