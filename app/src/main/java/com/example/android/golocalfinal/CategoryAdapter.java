package com.example.android.golocalfinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    Context mCTx;
    List<Category> categoryList;
    private onNoteListener monNoteListener;


    public CategoryAdapter(Context mCTx, List<Category> categoryList,onNoteListener onNoteListener) {
        this.mCTx = mCTx;
        this.categoryList = categoryList;
        this.monNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCTx);
        View view  = inflater.inflate(R.layout.text_view_category,null);
        CategoryViewHolder categoryViewHolder = new CategoryViewHolder(view,monNoteListener);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category curr = categoryList.get(position);
        holder.textViewCategory.setText(curr.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener  {
        TextView textViewCategory;
        onNoteListener onNoteListener;
        public CategoryViewHolder(@NonNull View itemView,onNoteListener onNoteListener) {
            super(itemView);
            this.textViewCategory = (TextView) itemView.findViewById(R.id.textViewCategory);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getLayoutPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            onNoteListener.onLongClick(getLayoutPosition());


            return false;
        }
    }

    public interface onNoteListener{
        void onNoteClick(int position);
        void onLongClick(int position);
    }





}