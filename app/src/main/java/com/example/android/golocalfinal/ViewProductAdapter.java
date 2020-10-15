package com.example.android.golocalfinal;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewProductAdapter extends RecyclerView.Adapter<ViewProductAdapter.ProductViewHolder> {
    Context mCTx;
    List<ProductBuyer> productList;
    private ViewProductAdapter.onNoteListener monNoteListener;


    public ViewProductAdapter(Context mCTx, List<ProductBuyer> productList,onNoteListener monNoteListener) {
        this.mCTx = mCTx;
        this.productList = productList;
        this.monNoteListener = monNoteListener;
    }

    @NonNull
    @Override
    public ViewProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCTx);

        View view  = inflater.inflate(R.layout.view_product_buyer,null);
        return new ViewProductAdapter.ProductViewHolder(view,monNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        final ProductBuyer curr = productList.get(position);

        holder.textViewProduct.setText(curr.getName());
        holder.textViewPrice.setText(curr.getPrice());
        holder.textViewDescription.setText(curr.getDesc());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewProduct,textViewPrice,textViewDescription;
        Button buttonAddToCart;
        ViewProductAdapter.onNoteListener onNoteListener;
        public ProductViewHolder(@NonNull View itemView,ViewProductAdapter.onNoteListener onNoteListener) {
            super(itemView);
            textViewProduct = (TextView) itemView.findViewById(R.id.textViewProductBuyer);
            textViewPrice = (TextView) itemView.findViewById(R.id.textViewPriceBuyer);
            textViewDescription =  itemView.findViewById(R.id.textViewDescriptionBuyer);
            buttonAddToCart = itemView.findViewById(R.id.buttonAddToCart);
            this.onNoteListener = onNoteListener;
            buttonAddToCart.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) { onNoteListener.onNoteClick(getLayoutPosition()); }
    }

    public interface onNoteListener{
        void onNoteClick(int position);
    }
}

