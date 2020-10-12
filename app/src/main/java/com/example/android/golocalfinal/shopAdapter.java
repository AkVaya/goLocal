package com.example.android.golocalfinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class shopAdapter extends RecyclerView.Adapter<shopAdapter.ShopViewHolder> {
    Context mCtx;
    List<shop> shopList;
    private shopAdapter.onNoteListener monNoteListener;

    public shopAdapter(Context applicationContext, List<shop> shopList, onNoteListener onNoteListener) {
        this.mCtx = applicationContext;
        this.shopList = shopList;
        this.monNoteListener = (shopAdapter.onNoteListener) onNoteListener;
    }

    @NonNull
    @Override
    public shopAdapter.ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.view_shop,null);

        return new ShopViewHolder(view,monNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull shopAdapter.ShopViewHolder holder, int position){
        shop curr = shopList.get(position);
        holder.textViewShopName.setText(curr.getName());
        holder.textViewShopLocality.setText(curr.getLocality());
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    static class ShopViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener{
        TextView textViewShopName,textViewShopLocality;
        shopAdapter.onNoteListener onNoteListener;
        public ShopViewHolder(@NonNull View itemView,onNoteListener onNoteListener) {
            super(itemView);
            textViewShopName = (TextView) itemView.findViewById(R.id.textViewShopName);
            textViewShopLocality = (TextView) itemView.findViewById(R.id.textViewShopLocality);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) { onNoteListener.onNoteClick(getLayoutPosition()); }
    }

    public interface onNoteListener{
        void onNoteClick(int position);
    }
}
