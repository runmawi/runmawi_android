package com.atbuys.runmawi.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.atbuys.runmawi.R;
import com.atbuys.runmawi.pages;

import java.util.ArrayList;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {

    ArrayList<pages> genreList;
    Context context;
    int pre_position;

    public GenreAdapter(ArrayList<pages> genreList, Context context) {
        this.genreList = genreList;
        this.context = context;
    }

    @NonNull
    @Override
    public GenreAdapter.GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GenreAdapter.GenreViewHolder(LayoutInflater.from(context).inflate(R.layout.genre_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GenreAdapter.GenreViewHolder holder, int position) {

        holder.genre_name.setText(genreList.get(position).getGenre_name());

        if (holder.genre_name.getText().toString().equalsIgnoreCase("Categories")){
            holder.genre_name.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_keyboard_arrow_down_24, 0);
        }

        if (genreList.get(position).isVisible()){
            pre_position=position;
            holder.genre_layout.setCardBackgroundColor(Color.parseColor("#ff0000"));
            holder.genre_name.setTextColor(Color.parseColor("#FFFFFF"));
        }else{
            holder.genre_layout.setCardBackgroundColor(Color.parseColor("#FF000000"));
            holder.genre_name.setTextColor(Color.parseColor("#FFFFFF"));
        }

        holder.genre_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genreList.get(pre_position).setVisible(false);
                genreList.get(position).setVisible(true);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    public class GenreViewHolder extends RecyclerView.ViewHolder {

        TextView genre_name;
        CardView genre_layout;
        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);

            genre_name=itemView.findViewById(R.id.genre_name);
            genre_layout=itemView.findViewById(R.id.genre_layout);
            genre_layout.setCardBackgroundColor(Color.parseColor("#FF000000"));
        }
    }
}
