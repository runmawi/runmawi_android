package com.atbuys.runmawi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


class Favourite_artistAdopter extends RecyclerView.Adapter<Favourite_artistAdopter.MyViewHolder> {


    private ArrayList<favoriteslist> favoriteslists;
    private Context context;

    public Favourite_artistAdopter(ArrayList<favoriteslist> favoriteslists, Context context) {
        this.favoriteslists = favoriteslists;
        this.context = context;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, url,id,ppvstatus;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.newuploadid);
            image = (ImageView) view.findViewById(R.id.newuploadimg);
             name = (TextView) view.findViewById(R.id.newuploadname);
            url = (TextView) view.findViewById(R.id.newuploadurl);
            ppvstatus=(TextView)view.findViewById(R.id.ppvstatus);

        }
    }

    public Favourite_artistAdopter(ArrayList<favoriteslist> favoriteslists) {
        this.favoriteslists = favoriteslists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_relses, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.id.setText(favoriteslists.get(position).getId());
        holder.url.setText(favoriteslists.get(position).getDescription());
        holder.name.setText(favoriteslists.get(position).getArtist_name());

        Picasso.get()
                .load("https://runmawi.com/public/uploads/artists/"+favoriteslists.get(position).getImage())
                .placeholder(R.drawable.avatar)
                .transform(new CircleTransform())
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
       return favoriteslists.size();

    }

}

