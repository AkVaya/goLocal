package com.example.android.golocalfinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SellerOrdersAdapter extends RecyclerView.Adapter<SellerOrdersAdapter.SellerOrdersViewHolder> {

    Context mCTx;
    List<ProductBuyer> orderList;
    onNoteListener monNoteListener;

    public SellerOrdersAdapter(Context mCTx, List<ProductBuyer> orderList, onNoteListener onNoteListener) {
        this.mCTx = mCTx;
        this.orderList = orderList;
        this.monNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public SellerOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCTx);
        View view = inflater.inflate(R.layout.view_seller_order, null);
        return new SellerOrdersViewHolder(view,monNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SellerOrdersAdapter.SellerOrdersViewHolder holder, int position) {
        ProductBuyer curr = orderList.get(position);
        String buyerName = curr.getName();
        String totalCost = curr.getPrice();
        String address = curr.getDesc();
        holder.name.setText(buyerName);
        holder.price.setText(totalCost);
        holder.address.setText(address);

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class SellerOrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name,address,price;
        onNoteListener onNoteListener;
        public SellerOrdersViewHolder(@NonNull View itemView,SellerOrdersAdapter.onNoteListener onNoteListener) {
            super(itemView);
            this.name = itemView.findViewById(R.id.textViewBuyerName);
            this.price = itemView.findViewById(R.id.totalPrice);
            this.address = itemView.findViewById(R.id.textViewBuyerAddress);
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

