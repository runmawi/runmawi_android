package com.atbuys.runmawi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

class recentAdopter extends RecyclerView.Adapter<recentAdopter.MyViewHolder> {



    private List<recent> recentList;
    private Context context;

    private static int currentPosition = 0;

    public recentAdopter(List<recent> recentList, Context context) {
        this.recentList = recentList;
        this.context = context;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name1, name2;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.newuploadimg);
             name1 = (TextView) view.findViewById(R.id.newuploadname);
            name2 = (TextView) view.findViewById(R.id.newuploadurl);

        }
    }

    public recentAdopter(List<recent> recentList) {
        this.recentList = recentList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_play, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        //Picasso.get().load(videos.get(position).getGenre).into(holder.genre);


        recent sidemenu = recentList.get(position);
        holder.name1.setText(sidemenu.getName1());
        holder.name2.setText(sidemenu.getName2());


        Picasso.get().load(sidemenu.getImage()).into(holder.image);
/*
        holder.image.setImageDrawable(context.getResources().getDrawable(sidemenu.getImage()));
*/

    }


    @Override
    public int getItemCount() {
        return recentList.size();
    }



}

