package com.atbuys.runmawi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


class ArtistSearchAdopter extends RecyclerView.Adapter<ArtistSearchAdopter.MyViewHolder> {



    private ArrayList<artistlist> artistlists;
    private Context context;

    private int lastPosition = -1;
    int row_index = 0;



    public ArtistSearchAdopter(ArrayList<artistlist> artistlist, Context context) {
        this.artistlists = artistlist;
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

    public ArtistSearchAdopter(ArrayList<artistlist> artistlists) {
        this.artistlists = artistlists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.id.setText(artistlists.get(position).getId());
        holder.url.setText(artistlists.get(position).getDescription());
        holder.name.setText(artistlists.get(position).getArtist_name());

/*
        Picasso.get()
                .load(artistlists.get(position).getImage_url())
                .placeholder(R.drawable.avatar)
                .transform(new CircleTransform())
                .into(holder.image);
*/

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index= position;
                notifyDataSetChanged();
            }
        });


        if (row_index== position)
        {
            holder.name.setBackgroundResource(R.drawable.plansborder);
            // viewHolder.id.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else {
            holder.name.setBackgroundResource(R.drawable.borderstyle);
            //viewHolder.id.setTextColor(Color.parseColor("#FFFFFF"));
        }

    }


    @Override
    public int getItemCount() {
       return artistlists.size();

    }

}

