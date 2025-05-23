package com.atbuys.runmawi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.atbuys.runmawi.ChannalPageActivity1;
import com.atbuys.runmawi.Model.data;
import com.atbuys.runmawi.R;
import java.util.ArrayList;

/**
 * Simple adapter for displaying live categories similar to GenreCategoriesAdapter
 * This provides a consistent UI between movie categories and live categories
 */
public class LiveCategoriesSimpleAdapter extends RecyclerView.Adapter<LiveCategoriesSimpleAdapter.LiveCateViewHolder> {
    
    /**
     * Interface for handling category item clicks
     */
    public interface OnCategoryClickListener {
        void onCategoryClick(data category);
    }

    private ArrayList<data> liveCategoryList;
    private Context context;
    private OnCategoryClickListener listener;

    /**
     * Constructor with click listener for in-place filtering
     */
    public LiveCategoriesSimpleAdapter(ArrayList<data> liveCategoryList, Context context, OnCategoryClickListener listener) {
        this.liveCategoryList = liveCategoryList;
        this.context = context;
        this.listener = listener;
        
        // Log the categories received
        android.util.Log.d("LIVE_ADAPTER_DEBUG", "Adapter created with " + 
                (liveCategoryList != null ? liveCategoryList.size() : 0) + " categories");
        
        if (liveCategoryList != null) {
            for (int i = 0; i < liveCategoryList.size(); i++) {
                data category = liveCategoryList.get(i);
                android.util.Log.d("LIVE_ADAPTER_DEBUG", "Category " + i + ": " + 
                        "Name=" + category.getName() + ", " +
                        "ID=" + category.getId() + ", " + 
                        "ID1=" + category.getId1());
            }
        }
    }
    
    /**
     * Constructor without click listener (for backward compatibility)
     * This will launch a new activity when a category is clicked
     */
    public LiveCategoriesSimpleAdapter(ArrayList<data> liveCategoryList, Context context) {
        this(liveCategoryList, context, null);
    }

    @NonNull
    @Override
    public LiveCateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Use the same layout as movie categories for consistency
        return new LiveCateViewHolder(LayoutInflater.from(context).inflate(R.layout.categorylist_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LiveCateViewHolder holder, int position) {
        final data category = liveCategoryList.get(position);
        
        // Log binding data for debugging
        android.util.Log.d("LIVE_ADAPTER_DEBUG", "Binding position " + position + ": " + 
                "Name=" + category.getName());
        
        // Set the category name
        holder.category_name.setText(category.getName());
        
        // Add click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.util.Log.d("LIVE_ADAPTER_DEBUG", "Category clicked: " + category.getName());
                
                // If we have a listener, use it for in-place filtering
                if (listener != null) {
                    android.util.Log.d("LIVE_ADAPTER_DEBUG", "Using in-place filtering for: " + category.getName());
                    listener.onCategoryClick(category);
                } else {
                    // Otherwise, launch a new activity (legacy behavior)
                    android.util.Log.d("LIVE_ADAPTER_DEBUG", "Launching activity for: " + category.getName());
                    Intent intent = new Intent(context, ChannalPageActivity1.class);
                    
                    // Handle case where ID is null by using the category name
                    if (category.getId() != null) {
                        intent.putExtra("id", category.getId());
                        android.util.Log.d("LIVE_ADAPTER_DEBUG", "Using ID: " + category.getId());
                    } else if (category.getId1() != null) {
                        intent.putExtra("id", category.getId1());
                        android.util.Log.d("LIVE_ADAPTER_DEBUG", "Using ID1: " + category.getId1());
                    } else {
                        // Use category name as identifier when ID is null
                        intent.putExtra("id", "category_" + category.getName().toLowerCase().replace(" ", "_"));
                        intent.putExtra("category_name", category.getName());
                        android.util.Log.d("LIVE_ADAPTER_DEBUG", "Using name as ID: category_" + 
                                category.getName().toLowerCase().replace(" ", "_"));
                    }
                    
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return liveCategoryList != null ? liveCategoryList.size() : 0;
    }

    public class LiveCateViewHolder extends RecyclerView.ViewHolder {
        TextView category_name;

        public LiveCateViewHolder(@NonNull View itemView) {
            super(itemView);
            category_name = itemView.findViewById(R.id.category_name);
        }
    }
}
