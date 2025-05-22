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

class castandcrewadapter extends RecyclerView.Adapter<castandcrewadapter.MyViewHolder> {



    private ArrayList<video_cast> castandcrewpojos;
    private Context context;

    private static int currentPosition = 0;

    public castandcrewadapter(ArrayList<video_cast> castandcrewpojos, Context context) {
        this.castandcrewpojos = castandcrewpojos;
        this.context = context;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cast_id, cast;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            //cast_id = (TextView) view.findViewById(R.id.newuploadid);
            image = (ImageView) view.findViewById(R.id.newuploadimg);
            cast = (TextView) view.findViewById(R.id.newuploadname);



        }
    }

    public castandcrewadapter(ArrayList<video_cast> castandcrewpojos) {
        this.castandcrewpojos = castandcrewpojos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.castandcrew, parent, false);

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
                load(castandcrewpojos.get(position).getImage_url())
                .into(holder.image);

        StringBuilder split_name = new StringBuilder("");
        String name = castandcrewpojos.get(position).getArtist_name();
        String[] splited = name.split("\\s+");
        for (int i=0;i< splited.length;i++){
            split_name.append(splited[i]+"\n");
        }

        holder.cast.setText(split_name);

    }


    @Override
    public int getItemCount() {
        return castandcrewpojos.size();
    }



}

