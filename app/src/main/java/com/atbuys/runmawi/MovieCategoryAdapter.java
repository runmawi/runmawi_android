package com.atbuys.runmawi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieCategoryAdapter extends RecyclerView.Adapter<MovieCategoryAdapter.MovieViewHolder> {

    ArrayList<categoryVideos> movieCategoryList;
    Context context;

    public MovieCategoryAdapter(ArrayList<categoryVideos> movieCategoryList, Context context) {
        this.movieCategoryList = movieCategoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieCategoryAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_uploads1, parent, false);
        return new MovieCategoryAdapter.MovieViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull MovieCategoryAdapter.MovieViewHolder holder, int position) {
        Picasso.get().load( movieCategoryList.get(position).getImage_url()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return movieCategoryList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{

        public ImageView image;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.newuploadimg);
        }
    }
}
