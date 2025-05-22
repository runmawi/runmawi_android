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

class paypervideoAdopter extends RecyclerView.Adapter<paypervideoAdopter.MyViewHolder> {


    private ArrayList<payperview_video> paypervideolist;
    private Context context;

    private static int currentPosition = 0;

    public paypervideoAdopter(ArrayList<payperview_video> paypervideolist, Context context) {
        this.paypervideolist = paypervideolist;
        this.context = context;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, url,id,expire;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.id);
            image = (ImageView) view.findViewById(R.id.newuploadimg123);
             name = (TextView) view.findViewById(R.id.newuploadname);
            url = (TextView) view.findViewById(R.id.views);
            expire=(TextView)view.findViewById(R.id.newuploadurl);

        }
    }

    public paypervideoAdopter(ArrayList<payperview_video> paypervideolist) {
        this.paypervideolist = paypervideolist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payperview_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {



        holder.id.setText(paypervideolist.get(position).getId());
     //   holder.url.setText(paypervideolist.get(position).getMovie().getPpv_status());
   //    holder.name.setText(paypervideolist.get(position).getMovie().getId());
       holder.expire.setText(paypervideolist.get(position).getPpv_videos_status());


        Picasso.get().
                load( paypervideolist.get(position).getImage_url())
                .into(holder.image);



    }


    @Override
    public int getItemCount() {
        return paypervideolist.size();
    }



}

