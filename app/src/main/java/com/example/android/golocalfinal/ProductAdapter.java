package com.example.android.golocalfinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    Context mCTx;
    List<Product> productList;
    private clickHandler mClickHandler;

    public ProductAdapter(Context mCTx, List<Product> productList,clickHandler clickHandler) {
        this.mCTx = mCTx;
        this.productList = productList;
        this.mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCTx);

        View view  = inflater.inflate(R.layout.view_product,null);
      //  LayoutInflater.from(parent.getContext()).inflate(R.layout.category_specific_info, parent, false);
        ProductViewHolder productViewHolder = new ProductViewHolder(view, mClickHandler);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product curr = productList.get(position);

        holder.textViewProduct.setText(curr.getName());
        holder.textViewQuantity.setText((curr.getQuantity()));
        holder.textViewPrice.setText(curr.getPrice());
        holder.textViewDescription.setText(curr.getDesc());
        Glide.with(holder.productImage.getContext())
                .load(curr.getImageURL())
                .into(holder.productImage);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        TextView textViewProduct,textViewQuantity,textViewPrice,textViewDescription;
        ImageView productImage;
        ProductAdapter.clickHandler clickHandler;
        public ProductViewHolder(@NonNull View itemView,clickHandler clickHandler) {
            super(itemView);
            textViewProduct = (TextView) itemView.findViewById(R.id.textViewProduct);
            textViewQuantity = (TextView) itemView.findViewById(R.id.textViewQuantity);
            textViewPrice = (TextView) itemView.findViewById(R.id.textViewPrice);
            textViewDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            this.clickHandler = clickHandler;
            itemView.setOnLongClickListener(this);
        }
        @Override
        public boolean onLongClick(View view) {
            clickHandler.onLongClick(getLayoutPosition());
            return false;
        }

    }


    public interface clickHandler{
        void onLongClick(int position);
    }
}
