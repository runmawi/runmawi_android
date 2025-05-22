package com.atbuys.runmawi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

class RecommandedAdopter extends RecyclerView.Adapter<RecommandedAdopter.MyViewHolder> {

    private List<recomendedaudios> recomendedaudioslist;
    private Context context;

    private static int currentPosition = 0;

    public RecommandedAdopter(List<recomendedaudios> recomendedaudioslist, Context context) {
        this.recomendedaudioslist = recomendedaudioslist;
        this.context = context;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name1, name2;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.newuploadimg);
             name1 = (TextView) view.findViewById(R.id.newuploadname);
            name2 = (TextView) view.findViewById(R.id.newuploadurl);

        }
    }

    public RecommandedAdopter(List<recomendedaudios> recomendedaudioslist) {
        this.recomendedaudioslist = recomendedaudioslist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recommened_play, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        //Picasso.get().load(videos.get(position).getGenre).into(holder.genre);

        recomendedaudios recomendedaudios = recomendedaudioslist.get(position);
        holder.name1.setText(recomendedaudios.getTitle());
        holder.name2.setText(recomendedaudios.getTitle());

        Picasso.get().load(recomendedaudios.getImage_url()).into(holder.image);
/*
        holder.image.setImageDrawable(context.getResources().getDrawable(sidemenu.getImage()));
*/

    }


    @Override
    public int getItemCount() {
        return recomendedaudioslist.size();
    }



}

