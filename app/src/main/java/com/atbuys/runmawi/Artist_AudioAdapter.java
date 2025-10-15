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


class Artist_AudioAdapter extends RecyclerView.Adapter<Artist_AudioAdapter.MyViewHolder> {



    private ArrayList<artist_audios> artist_audioslist;
    private Context context;




    public Artist_AudioAdapter(ArrayList<artist_audios> artist_audioslist, Context context) {
        this.artist_audioslist = artist_audioslist;
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

        }
    }

    public Artist_AudioAdapter(ArrayList<artist_audios> artist_audioslist) {
        this.artist_audioslist = artist_audioslist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artist_songs, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.id.setText(artist_audioslist.get(position).getId());
        holder.url.setText(artist_audioslist.get(position).getMp3_url());
        holder.name.setText(artist_audioslist.get(position).getTitle());

        Picasso.get().
                load( "https://runmawi.com/public/uploads/images/"+artist_audioslist.get(position).getImage())
                .into(holder.image);

    }


    @Override
    public int getItemCount() {

       return artist_audioslist.size();

    }

}

