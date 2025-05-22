package com.atbuys.runmawi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class channelvideosadaptersearch extends RecyclerView.Adapter<channelvideosadaptersearch.MyViewHolder> {

    private ArrayList<data> thismaylikelilst;
    private Context context;

    private static int currentPosition = 0;

    public channelvideosadaptersearch(ArrayList<data> thismaylikelilst, Context context) {
        this.thismaylikelilst = thismaylikelilst;
        this.context = context;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, url,id,ppvstatus,rating;
        public ImageView image;
        CardView ratingcard;

        public MyViewHolder(View view) {
            super(view);
            //id = (TextView) view.findViewById(R.id.newuploadid);
            image = (ImageView) view.findViewById(R.id.newuploadimg);
            rating=(TextView)view.findViewById(R.id.rating);
            ratingcard=(CardView)view.findViewById(R.id.ratingcard);
           /* name = (TextView) view.findViewById(R.id.newuploadname);
            url = (TextView) view.findViewById(R.id.id);
            ppvstatus=(TextView)view.findViewById(R.id.ppvstatus);*/


        }
    }

    public channelvideosadaptersearch(ArrayList<data> thismaylikelilst) {
        this.thismaylikelilst = thismaylikelilst;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.latest_videos, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        //Picasso.get().load(videos.get(position).getGenre).into(holder.genre);

       /* holder.id.setText(thismaylikelilst.get(position).getDuration() + " Seconds");
        holder.url.setText(thismaylikelilst.get(position).getYear());
        holder.name.setText(thismaylikelilst.get(position).getTitle());
        holder.ppvstatus.setText(thismaylikelilst.get(position).getAccess());*/


        Picasso.get().
                load( thismaylikelilst.get(position).getImage_url())
                .into(holder.image);
        if (thismaylikelilst.get(position).getRating()==null ||thismaylikelilst.get(position).getRating()=="" ||thismaylikelilst.get(position).getRating()=="0"){
            holder.ratingcard.setVisibility(View.GONE);
        }else {
            holder.rating.setText(thismaylikelilst.get(position).getRating()+". 0");
        }



    }


    @Override
    public int getItemCount() {
        return thismaylikelilst.size();
    }



}

