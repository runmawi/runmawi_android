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

class FavouriteAdopter extends RecyclerView.Adapter<FavouriteAdopter.MyViewHolder> {



    private ArrayList<channel_videos> wishlistlilst;
    private Context context;

    private static int currentPosition = 0;

    public FavouriteAdopter(ArrayList<channel_videos> wishlistlilst, Context context) {
        this.wishlistlilst = wishlistlilst;
        this.context = context;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, url,id,ppvstatus;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.id);
            image = (ImageView) view.findViewById(R.id.newuploadimg);
             name = (TextView) view.findViewById(R.id.newuploadname);
            url = (TextView) view.findViewById(R.id.views);
            ppvstatus=(TextView)view.findViewById(R.id.ppvstatus);



        }
    }

    public FavouriteAdopter(ArrayList<channel_videos> wishlistlilst) {
        this.wishlistlilst = wishlistlilst;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wish_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {




        holder.id.setText(wishlistlilst.get(position).getId());
      /*  holder.url.setText(wishlistlilst.get(position).getMp4_url());
        holder.name.setText(wishlistlilst.get(position).getTitle());
        holder.ppvstatus.setText(wishlistlilst.get(position).getPpv_status());*/


        Picasso.get().
                load(wishlistlilst.get(position).getImage_url())
                .fit()
                .into(holder.image);
    }


    @Override
    public int getItemCount() {
        return wishlistlilst.size();
    }



}

