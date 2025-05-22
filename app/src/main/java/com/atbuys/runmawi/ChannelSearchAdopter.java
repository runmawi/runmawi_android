package com.atbuys.runmawi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class ChannelSearchAdopter extends RecyclerView.Adapter<ChannelSearchAdopter.MyViewHolder> {



    private ArrayList<categorylist> channellistdata;
    private DashboardFragment context;

    private int lastPosition = -1;
    int row_index = -1;


    public ChannelSearchAdopter(ArrayList<categorylist> channellists, DashboardFragment context) {
        this.channellistdata = channellists;
        this.context = context;
    }

    public ChannelSearchAdopter(ArrayList<categorylist> channelListData, NotificationsFragment notificationsFragment) {
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, url,id,ppvstatus;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.newuploadid);
             name = (TextView) view.findViewById(R.id.newuploadname);
            url = (TextView) view.findViewById(R.id.newuploadurl);
            ppvstatus=(TextView)view.findViewById(R.id.ppvstatus);

        }
    }

    public ChannelSearchAdopter(ArrayList<categorylist> channellists) {
        this.channellistdata = channellists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.id.setText(channellistdata.get(position).getId());
        holder.name.setText(channellistdata.get(position).getName());

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (row_index == position)
                {

                }
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
        return channellistdata.size();
    }

}