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


class following_pageartistAdopter extends RecyclerView.Adapter<following_pageartistAdopter.MyViewHolder> {



    private ArrayList<followinglist> followinglists;
    private Context context;




    public following_pageartistAdopter(ArrayList<followinglist> followinglists, Context context) {
        this.followinglists = followinglists;
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

    public following_pageartistAdopter(ArrayList<followinglist> followinglists) {
        this.followinglists = followinglists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artist_liat, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.id.setText(followinglists.get(position).getId());
        holder.url.setText(followinglists.get(position).getDescription());
        holder.name.setText(followinglists.get(position).getArtist_name());

        Picasso.get()
                .load("https://runmawi.com/public/uploads/images/"+followinglists.get(position).getImage())
                .placeholder(R.drawable.avatar)
                .transform(new CircleTransform())
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
       return followinglists.size();

    }

}

