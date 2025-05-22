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


class MyFavouriteAdopter extends RecyclerView.Adapter<MyFavouriteAdopter.MyViewHolder> {



    private ArrayList<channel_audios> channelaudiolist;
    private Context context;




    public MyFavouriteAdopter(ArrayList<channel_audios> channelaudiolist, Context context) {
        this.channelaudiolist = channelaudiolist;
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

    public MyFavouriteAdopter(ArrayList<channel_audios> channelaudiolist) {
        this.channelaudiolist = channelaudiolist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wish_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.id.setText(channelaudiolist.get(position).getId());
        holder.url.setText(channelaudiolist.get(position).getMp3_url());
        holder.name.setText(channelaudiolist.get(position).getTitle());

        Picasso.get().
                load( channelaudiolist.get(position).getImage_url())
                .into(holder.image);

    }


    @Override
    public int getItemCount() {
       return channelaudiolist.size();

    }

}

