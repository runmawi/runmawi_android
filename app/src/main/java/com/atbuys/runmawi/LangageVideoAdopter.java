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

class LangageVideoAdopter extends RecyclerView.Adapter<LangageVideoAdopter.MyViewHolder> {



    private ArrayList<language_videos> thismaylikelilst;
    private Context context;

    private static int currentPosition = 0;

    public LangageVideoAdopter(ArrayList<language_videos> thismaylikelilst, Context context) {
        this.thismaylikelilst = thismaylikelilst;
        this.context = context;
    }

    public LangageVideoAdopter(ArrayList<categoryVideos> channelvideosdata, NotificationsFragment notificationsFragment) {
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

    public LangageVideoAdopter(ArrayList<language_videos> thismaylikelilst) {
        this.thismaylikelilst = thismaylikelilst;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_uploads1, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        //Picasso.get().load(videos.get(position).getGenre).into(holder.genre);

      //  holder.id.setText(thismaylikelilst.get(position).getId());
       // holder.url.setText(thismaylikelilst.get(position).getMp4_url());
       // holder.name.setText(thismaylikelilst.get(position).getTitle());
        //holder.ppvstatus.setText(thismaylikelilst.get(position).getPpv_status());


        Picasso.get().
                load( "https://runmawi.com/public/uploads/images/"+thismaylikelilst.get(position).getImage())
                .into(holder.image);



    }


    @Override
    public int getItemCount() {
        return thismaylikelilst.size();
    }



}

