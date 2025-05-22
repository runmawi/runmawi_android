package com.atbuys.runmawi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.atbuys.runmawi.R;
import com.atbuys.runmawi.categorylist;
import java.util.ArrayList;

public class GenreCategoriesAdapter extends RecyclerView.Adapter<GenreCategoriesAdapter.CateViewHolder> {

    ArrayList<categorylist> cateList;
    Context context;

    public GenreCategoriesAdapter(ArrayList<categorylist> cateList, Context context) {
        this.cateList = cateList;
        this.context = context;
    }

    @NonNull
    @Override
    public GenreCategoriesAdapter.CateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GenreCategoriesAdapter.CateViewHolder(LayoutInflater.from(context).inflate(R.layout.categorylist_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GenreCategoriesAdapter.CateViewHolder holder, int position) {

        holder.category_name.setText(cateList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return cateList.size();
    }

    public class CateViewHolder extends RecyclerView.ViewHolder{

        TextView category_name;

        public CateViewHolder(@NonNull View itemView) {
            super(itemView);

            category_name=itemView.findViewById(R.id.category_name);
        }
    }
}
