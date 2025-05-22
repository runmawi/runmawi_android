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

class livepageAdopter extends RecyclerView.Adapter<livepageAdopter.MyViewHolder> {



    private List<LiveCategory> sidemenulist2;
    private Context context;

    private static int currentPosition = 0;

    public livepageAdopter(List<LiveCategory> sidemenulist2, Context context) {
        this.sidemenulist2 = sidemenulist2;
        this.context = context;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name1;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.newuploadimg);
         //    name1 = (TextView) view.findViewById(R.id.name1);

        }
    }

    public livepageAdopter(List<LiveCategory> sidemenulist2) {
        this.sidemenulist2 = sidemenulist2;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_uploads5, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        //Picasso.get().load(videos.get(position).getGenre).into(holder.genre);



        LiveCategory sidemenu2 = sidemenulist2.get(position);
//        holder.name1.setText(sidemenu2.getName());


        Picasso.get().load("https://runmawi.com/public/uploads/images/"+sidemenu2.getImage()).into(holder.image);
/*
        holder.image.setImageDrawable(context.getResources().getDrawable(sidemenu.getImage()));
*/

    }


    @Override
    public int getItemCount() {
        return sidemenulist2.size();
    }



}

