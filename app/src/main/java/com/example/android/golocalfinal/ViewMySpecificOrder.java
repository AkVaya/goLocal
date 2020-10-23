package com.example.android.golocalfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewMySpecificOrder extends AppCompatActivity {
    RecyclerView recyclerViewSpecificOrder;
    Button buttonConfirmOrderDelivery;
    TextView textViewTotalPriceCart;
    List<ProductCart> shoppingOrder;
    DatabaseReference mRef,mRefSeller;
    Integer totalCost;
    String email,orderId,status;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_specific_order);
        buttonConfirmOrderDelivery = findViewById(R.id.buttonConfirmOrderDelivery);
        recyclerViewSpecificOrder = findViewById(R.id.recyclerViewSpecificOrder);
        recyclerViewSpecificOrder.setHasFixedSize(true);
        recyclerViewSpecificOrder.setLayoutManager(new LinearLayoutManager(this));
        textViewTotalPriceCart = findViewById(R.id.textViewTotalPriceCart);
        shoppingOrder = new ArrayList<>();
        orderId = getIntent().getExtras().getString(BuyerOrders.ORDER_ID);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("BUYERS").child(mUser.getEmail().replace('.',',')).child("yourOrders").child(orderId);
        mRefSeller = FirebaseDatabase.getInstance().getReference().child("SELLERS");

        buttonConfirmOrderDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDelivered();
            }
        });
    }

    private void OrderDelivered() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewMySpecificOrder.this, R.style.AlertDialog);
        builder.setTitle("Pls Enter 'CONFIRM' to confirm delivery");
        final EditText editTextConfirm = new EditText(ViewMySpecificOrder.this);
        builder.setView(editTextConfirm);

        builder.setPositiveButton("Confirm Delivery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String confirm = editTextConfirm.getText().toString();
                if(confirm.equals("CONFIRM")){
                    mRefSeller.child(email).child("Orders").child(orderId).child("status").setValue("complete");
                    mRef.child("status").setValue("complete");
                    Toast.makeText(getApplicationContext(),"Congrats, Your Order has been Delivered", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ViewMySpecificOrder.this,BuyerOrders.class));
                } else{
                    Toast.makeText(getApplicationContext(), "Order Delivery Not Confirmed", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onStart() {
        super.onStart();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shoppingOrder.clear();
                totalCost = 0;
                status = "incomplete";
                email = snapshot.child("email").getValue().toString();
                status = snapshot.child("status").getValue().toString();
                for(DataSnapshot items : snapshot.child("list").getChildren() ){
                    Boolean check = false;
                    String itemCategoryIndex = items.child("itemCategoryIndex").getValue().toString();
                    String itemIndex = items.child("itemIndex").getValue().toString();
                    String itemName = items.child("itemName").getValue().toString();
                    String itemPrice = items.child("itemPrice").getValue().toString();
                    String itemQuantity = items.child("itemQuantity").getValue().toString();
                    totalCost += Integer.parseInt(itemPrice)*Integer.parseInt(itemQuantity);
                    shoppingOrder.add(new ProductCart(itemCategoryIndex,itemIndex,itemName,itemPrice,itemQuantity,check));
                }
                if(status.equals("complete")){
                    buttonConfirmOrderDelivery.setText("Order Delivered");
                    buttonConfirmOrderDelivery.setClickable(false);
                }
                CartAdapter adapter = new CartAdapter(getApplicationContext(), shoppingOrder);
                recyclerViewSpecificOrder.setAdapter(adapter);
                textViewTotalPriceCart.setText("The total cost is " + totalCost.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}