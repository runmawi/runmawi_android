package com.atbuys.runmawi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class ResolutionAdapter extends RecyclerView.Adapter<ResolutionAdapter.MyViewHolder> {

    private ArrayList<String> resList;
    private OnlinePlayerActivity context;
    private static int currentPosition = 0;

    public ResolutionAdapter(ArrayList<String> resList, OnlinePlayerActivity onlinePlayerActivity) {
        this.context=onlinePlayerActivity;
        this.resList=resList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.newuploadname);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.resolution_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.name.setText(resList.get(position));

    }


    @Override
    public int getItemCount() {
        return resList.size();
    }



}

