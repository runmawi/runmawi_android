package com.atbuys.runmawi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class cmsAdopter extends RecyclerView.Adapter<cmsAdopter.MyViewHolder> {



    private List<cmspage> sidemenulist;
    private Context context;

    private static int currentPosition = 0;

    public cmsAdopter(List<cmspage> sidemenulist, Context context) {
        this.sidemenulist = sidemenulist;
        this.context = context;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name1, name2;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.sidemenuimg);
             name1 = (TextView) view.findViewById(R.id.name1);
            name2 = (TextView) view.findViewById(R.id.name2);

        }
    }

    public cmsAdopter(List<cmspage> sidemenulist) {
        this.sidemenulist = sidemenulist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.side_mennu3, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        //Picasso.get().load(videos.get(position).getGenre).into(holder.genre);


        cmspage sidemenu = sidemenulist.get(position);
        holder.name1.setText(sidemenu.getName1());

/*
        holder.image.setImageDrawable(context.getResources().getDrawable(sidemenu.getImage()));
*/

    }


    @Override
    public int getItemCount() {
        return sidemenulist.size();
    }



}

