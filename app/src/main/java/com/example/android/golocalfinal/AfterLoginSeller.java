package com.example.android.golocalfinal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.CheckedOutputStream;


public class AfterLoginSeller extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference mRef,mRef2;
    Toolbar toolbar ;
    RecyclerView recyclerView;
    EditText editTextCategory;
    List<Category>categoryList;
    ImageView buttonAdd;
    Intent intent;
    AlertDialog alertDialog;
    FloatingActionButton support;

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
        user = mAuth.getCurrentUser();
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
                    editTextCategory.setText(null);
                }
                else{
                    editTextCategory.setError("Name of category can't be empty");
                    editTextCategory.requestFocus();
                }
            }
        });

        support= findViewById(R.id.floatingActionButton);
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
                final CategoryAdapter adapter = new CategoryAdapter(getApplicationContext(), categoryList, new CategoryAdapter.onNoteListener() {
                    @Override
                    public void onNoteClick(int position) {
                        Intent intent2 = new Intent(getApplicationContext(), CategorySpecificInfo.class);
                        intent2.putExtra(POSITION, position);
                        intent2.putExtra(EMAIL_ID, intent.getExtras().getString(SellerBasicInfo.EMAIL_ID));
                        intent2.putExtra(CATEGORY_NAME, categoryList.get(position).getCategoryName());
                        startActivity(intent2);
                    }

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
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuLogout :
                mAuth.signOut();
                finish();
                startActivity(new Intent(AfterLoginSeller.this,MainActivity.class));
                break;

            case R.id.about :
                startActivity(new Intent(AfterLoginSeller.this, About.class));
                break;

            case R.id.myOrders :
                startActivity(new Intent(AfterLoginSeller.this, OrdersReceived.class));
                break;

            case R.id.developers :
                startActivity(new Intent(AfterLoginSeller.this, Developers.class));
                break;
            case R.id.viewProfile :
                startActivity(new Intent(AfterLoginSeller.this, ViewSellerProfile.class));

        }
        return true;
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu,menu);
        return true;
    }

    private void showUpdateDialog(final int position){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_category , null);
        dialogBuilder.setView(dialogView);

        final TextView title = (TextView) dialogView.findViewById(R.id.textViewCategory);
        final EditText editTextCategory1 = (EditText) dialogView.findViewById(R.id.editTextCategory1);
        final Button update = (Button) dialogView.findViewById(R.id.confirmButton);
        final Button delete = (Button) dialogView.findViewById(R.id.deleteCategory);

        String categoryName = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.textViewCategory)).getText().toString();
        title.setText("Update " + categoryName);

        final String email = user.getEmail().replace("." , ",");

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextCategory1.getText().toString().isEmpty()){
                    editTextCategory1.setError("Can't be empty");
                    editTextCategory1.requestFocus();
                }
                else {
                    FirebaseDatabase.getInstance().getReference().child("SELLERS").child(email).child("CATEGORIES").child(String.valueOf(position)).child("categoryName").setValue(editTextCategory1.getText().toString());
                    Toast.makeText(getApplicationContext(), "Category Name Updated!", Toast.LENGTH_SHORT).show();
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
        final String categoryName = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.textViewCategory)).getText().toString();
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                .setTitle("CAUTION")
                .setMessage("Are you sure you want to delete the category " + categoryName + "?")
                .setIcon(R.drawable.ic_baseline_delete_24)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        categoryList.remove(position);
                        String email = user.getEmail().replace("." , ",");
                        mRef2 = FirebaseDatabase.getInstance().getReference().child("SELLERS").child(email).child("CATEGORIES");
                        mRef2.setValue(categoryList);
                        recyclerView.getAdapter().notifyItemRemoved(position);
                        recyclerView.getAdapter().notifyItemRangeChanged(position, categoryList.size());
                        Toast.makeText(getApplicationContext(),categoryName + " Deleted!",Toast.LENGTH_SHORT).show();
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