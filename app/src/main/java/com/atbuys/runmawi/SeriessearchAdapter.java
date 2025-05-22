package com.atbuys.runmawi;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


class SeriessearchAdapter extends RecyclerView.Adapter<SeriessearchAdapter.MyViewHolder> {



    private ArrayList<data> latestvideoslist;
    private Context context;

    private static SharedPreferences prefs;
    String seriestitleshared = "";



    public SeriessearchAdapter(ArrayList<data> latestvideoslist, Context context1) {
        this.latestvideoslist = latestvideoslist;
        this.context = context1;


        prefs = context.getSharedPreferences(sharedpreferences.My_preference_name, 0);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name, url,id,ppvstatus,seriesid;
        public ImageView image;

        public MyViewHolder(View view) {

            super(view);
            id = (TextView) view.findViewById(R.id.newuploadid);
            image = (ImageView) view.findViewById(R.id.newuploadimg);
             name = (TextView) view.findViewById(R.id.newuploadname);
            url = (TextView) view.findViewById(R.id.newuploadurl);
            ppvstatus=(TextView)view.findViewById(R.id.ppvstatus);
            seriesid=(TextView)view.findViewById(R.id.seriesid);

        }
    }

    public SeriessearchAdapter(ArrayList<data> latestvideoslist) {
        this.latestvideoslist = latestvideoslist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        seriestitleshared = prefs.getString(sharedpreferences.seriestitle,null);



        holder.name.setText(latestvideoslist.get(position).getTitle());
      /*  holder.url.setText(latestvideoslist.get(position).getMp4_url());
        holder.name.setText(latestvideoslist.get(position).getId());
        holder.ppvstatus.setText(latestvideoslist.get(position).getPpv_status());
        holder.seriesid.setText(seriestitleshared);
*/


/*
        Picasso.get().
                load( latestvideoslist.get(position).getImage_url())
                .into(holder.image);
*/


     /*   if(holder.seriesid.getText().toString().equalsIgnoreCase("1"))
        {

            holder.id.setVisibility(View.VISIBLE);
        }

        else
        {

            holder.id.setVisibility(View.GONE);
        }
*/
    }


    @Override
    public int getItemCount() {
        return latestvideoslist.size();
    }

}

