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


class artistlit_artist_page_Adopter extends RecyclerView.Adapter<artistlit_artist_page_Adopter.MyViewHolder> {



    private ArrayList<artistlist> artistlists;
    private Context context;




    public artistlit_artist_page_Adopter(ArrayList<artistlist> artistlist, Context context) {
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

    public artistlit_artist_page_Adopter(ArrayList<artistlist> artistlists) {
        this.artistlists = artistlists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artist_liat, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.id.setText(artistlists.get(position).getId());
        holder.url.setText(artistlists.get(position).getDescription());
        holder.name.setText(artistlists.get(position).getArtist_name());

        Picasso.get()
                .load(artistlists.get(position).getImage_url())
                .placeholder(R.drawable.avatar)
                .transform(new CircleTransform())
                .into(holder.image);

    }


    @Override
    public int getItemCount() {
       return artistlists.size();

    }

}

