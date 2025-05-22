package com.atbuys.runmawi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class ChannelListSearchAdapter extends RecyclerView.Adapter<ChannelListSearchAdapter.MyViewHolder> {



    private ArrayList<categorylist> channellistdata;
    private DashboardFragment context;


    public ChannelListSearchAdapter(ArrayList<categorylist> channellists, DashboardFragment context) {
        this.channellistdata = channellists;
        this.context = context;
    }

    public ChannelListSearchAdapter(ArrayList<categorylist> channelListData, NotificationsFragment notificationsFragment) {
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

    public ChannelListSearchAdapter(ArrayList<categorylist> channellists) {
        this.channellistdata = channellists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_channel_search, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.id.setText(channellistdata.get(position).getId());
        holder.name.setText(channellistdata.get(position).getName());



    }


    @Override
    public int getItemCount() {
        return channellistdata.size();
    }

}