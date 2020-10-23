package com.example.android.golocalfinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.MyOrdersViewHolder> {
    Context mCTx;
    List<ProductBuyer> orderList;
    onNoteListener monNoteListener;


    public MyOrdersAdapter(Context mCTx, List<ProductBuyer> orderList,onNoteListener onNoteListener) {
        this.mCTx = mCTx;
        this.orderList = orderList;
        this.monNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public MyOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCTx);
        View view = inflater.inflate(R.layout.view_cart_product, null);
        return new MyOrdersViewHolder(view,monNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrdersViewHolder holder, int position) {
        ProductBuyer curr = orderList.get(position);
        holder.textViewQty.setVisibility(View.INVISIBLE);
        String shopName = curr.getName();
        String totalCost = curr.getPrice();
        String isDelivered = curr.getDesc();
        holder.textViewName.setText(shopName);
        holder.textViewPrice.setText(totalCost);
        if(isDelivered.equals("complete")){
            holder.textViewDelivered.setVisibility(View.VISIBLE);
        }
        else{
            holder.textViewUnavailable.setText("Not Delivered Yet");
            holder.textViewUnavailable.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class MyOrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textViewName, textViewPrice, textViewQty, textViewUnavailable,textViewDelivered;
        onNoteListener onNoteListener;

        public MyOrdersViewHolder(@NonNull View itemView,MyOrdersAdapter.onNoteListener onNoteListener) {
            super(itemView);
            this.textViewName = itemView.findViewById(R.id.textViewNameCart);
            this.textViewPrice = itemView.findViewById(R.id.textViewPriceCart);
            this.textViewQty = itemView.findViewById(R.id.textViewQuantityCart);
            this.textViewUnavailable = itemView.findViewById(R.id.textViewUnavailable);
            this.textViewDelivered = itemView.findViewById(R.id.textViewDelivered);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getLayoutPosition());
        }
    }

    public interface onNoteListener{
        void onNoteClick(int position);
    }
}

