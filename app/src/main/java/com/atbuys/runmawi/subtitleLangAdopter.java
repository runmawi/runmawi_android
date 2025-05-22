package com.atbuys.runmawi;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


class subtitleLangAdopter extends RecyclerView.Adapter<subtitleLangAdopter.MyViewHolder> {


    public int mSelectedItem = -1;
    private ArrayList<videossubtitles> videossubtitles;
    private android.content.Context context;
    private Object Context;
    View itemView;
    ImageView hoverimg;



    int row_index = 0;


    public subtitleLangAdopter(ArrayList<videossubtitles> videossubtitles, android.content.Context context) {
        this.videossubtitles = videossubtitles;
        this.context = context;
    }


    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);

    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, url,id,ppvstatus;
        public ImageView image;
        private RadioGroup subradiogrp;
        private CardView cardView;
        private LinearLayout lin;

        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.subtitlelanguage);

            cardView=(CardView) view.findViewById(R.id.cardview);
            lin=(LinearLayout) view.findViewById(R.id.lin);




        }


    }

    public subtitleLangAdopter(ArrayList<videossubtitles> videossubtitles) {
        this.videossubtitles = videossubtitles;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fruit_item, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.id.setText(videossubtitles.get(position).getSub_language());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=position;
                notifyDataSetChanged();
            }
        });



        if (row_index==position) {
            holder.lin.setBackgroundColor(R.attr.colorAccent);
            holder.id.setTextColor(R.attr.editTextColor);
        } else {
            holder.lin.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.id.setTextColor(Color.parseColor("#000000"));
        }



    }



    @Override
    public int getItemCount() {
        return videossubtitles.size();
    }

}

