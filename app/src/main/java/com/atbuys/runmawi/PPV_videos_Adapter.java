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

class PPV_videos_Adapter extends RecyclerView.Adapter<PPV_videos_Adapter.MyViewHolder> {



    private ArrayList<categorylist> channellistdata;
    private Context context;



    public PPV_videos_Adapter(ArrayList<categorylist> channellists, Context context) {
        this.channellistdata = channellists;
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

    public PPV_videos_Adapter(ArrayList<categorylist> channellists) {
        this.channellistdata = channellists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_channel, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.id.setText(channellistdata.get(position).getId());



        Picasso.get().
                load("https://runmawi.com/dev/public/uploads/videocategory/" + channellistdata.get(position).getImage())
                .into(holder.image);

    }


    @Override
    public int getItemCount() {
        return channellistdata.size();
    }

}

