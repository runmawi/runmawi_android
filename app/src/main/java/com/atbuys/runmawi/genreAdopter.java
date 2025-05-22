package com.atbuys.runmawi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class genreAdopter extends RecyclerView.Adapter<genreAdopter.MyViewHolder> {



    private ArrayList<movie_categories> genrelist;
    private Context context;

    private static int currentPosition = 0;

    public genreAdopter(ArrayList<movie_categories> genrelist, Context context) {
        this.genrelist = genrelist;
        this.context = context;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, url,id;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.newuploadid);
            image = (ImageView) view.findViewById(R.id.newuploadimg);
             name = (TextView) view.findViewById(R.id.newuploadname);
            url = (TextView) view.findViewById(R.id.newuploadurl);

        }
    }

    public genreAdopter(ArrayList<movie_categories> genrelist) {
        this.genrelist = genrelist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.genre_search, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.id.setText(genrelist.get(position).getId());
        holder.name.setText(genrelist.get(position).getName());


    }


    @Override
    public int getItemCount() {
        return genrelist.size();
    }



}

