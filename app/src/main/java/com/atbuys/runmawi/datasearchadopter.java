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

class datasearchadopter extends RecyclerView.Adapter<datasearchadopter.MyViewHolder> {



    private ArrayList<data> thismaylikelilst;
    private Context context;

    private static int currentPosition = 0;

    public datasearchadopter(ArrayList<data> thismaylikelilst, Context context) {
        this.thismaylikelilst = thismaylikelilst;
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
            url = (TextView) view.findViewById(R.id.id);
            ppvstatus=(TextView)view.findViewById(R.id.ppvstatus);


        }
    }

    public datasearchadopter(ArrayList<data> thismaylikelilst) {
        this.thismaylikelilst = thismaylikelilst;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.searchchannel, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        //Picasso.get().load(videos.get(position).getGenre).into(holder.genre);

       holder.id.setText(thismaylikelilst.get(position).getDuration() + " Seconds");
        holder.url.setText(thismaylikelilst.get(position).getYear());
        holder.name.setText(thismaylikelilst.get(position).getTitle());
        holder.ppvstatus.setText(thismaylikelilst.get(position).getAccess());


        Picasso.get().
                load( thismaylikelilst.get(position).getImage_url())
                .into(holder.image);



    }


    @Override
    public int getItemCount() {
        return thismaylikelilst.size();
    }



}

