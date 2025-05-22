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

class sidemennu3Adopter extends RecyclerView.Adapter<sidemennu3Adopter.MyViewHolder> {



    private List<sidemenu3> sidemenulist3;
    private Context context;

    private static int currentPosition = 0;

    public sidemennu3Adopter(List<sidemenu3> sidemenulist3, Context context) {
        this.sidemenulist3 = sidemenulist3;
        this.context = context;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name1;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.sidemenuimg);
            name1 = (TextView) view.findViewById(R.id.name1);

        }
    }

    public sidemennu3Adopter(List<sidemenu3> sidemenulist3) {
        this.sidemenulist3 = sidemenulist3;
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



        sidemenu3 sidemenu3 = sidemenulist3.get(position);
        holder.name1.setText(sidemenu3.getName1());
        Picasso.get().load(sidemenu3.getImage()).into(holder.image);


//         Picasso.get().load(sidemenu3.getImage()).into(holder.image);
      //  holder.image.setImageDrawable(context.getResources().getDrawable(sidemenu3.getImage()));

    }


    @Override
    public int getItemCount() {
        return sidemenulist3.size();
    }



}

