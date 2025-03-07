package com.TheLa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.TheLa.models.Category;
import com.example.TheLa.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
        private List<Category> categoryList;
        private Context context;
        private IOnItemClickListener listener;

        public CategoryAdapter(List<Category> categoryList, Context context, IOnItemClickListener listener) {
            this.categoryList = categoryList;
            this.context = context;
            this.listener = listener;
        }

        @NonNull
        @Override
        public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_category, parent, false);
            return new CategoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
            Category category = categoryList.get(position);
            holder.categoryName.setText(category.getName());

            //xử lý ảnh

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            });

        }

        @Override
        public int getItemCount() {
            return categoryList.size();
        }

        static class CategoryViewHolder extends RecyclerView.ViewHolder {
            TextView categoryName;

            public CategoryViewHolder(@NonNull View itemView) {
                super(itemView);
                categoryName = itemView.findViewById(R.id.categoryName);
            }
        }
    }
