package com.example.android.golocalfinal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    Context mCTx;
    List<ProductCart> cartList;


    public CartAdapter(Context mCTx, List<ProductCart> cartList) {
        this.mCTx = mCTx;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCTx);
        View view = inflater.inflate(R.layout.view_cart_product, null);
        CartAdapter.CartViewHolder cartViewHolder = new CartAdapter.CartViewHolder(view);
        return cartViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        ProductCart curr = cartList.get(position);
        holder.textViewName.setText(curr.getItemName());
        holder.textViewQty.setText(curr.getItemQuantity());
        Integer qty = Integer.parseInt(curr.getItemQuantity());
        Integer price = Integer.parseInt(curr.getItemPrice());
        qty = qty*price;
        holder.textViewPrice.setText(qty.toString());
        if(curr.getCheck()){
            holder.textViewUnavailable.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPrice, textViewQty, textViewUnavailable;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewName = itemView.findViewById(R.id.textViewNameCart);
            this.textViewPrice = itemView.findViewById(R.id.textViewPriceCart);
            this.textViewQty = itemView.findViewById(R.id.textViewQuantityCart);
            this.textViewUnavailable = itemView.findViewById(R.id.textViewUnavailable);
        }
    }
}

