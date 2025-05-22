package com.atbuys.runmawi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

class SeasonAdapter extends RecyclerView.Adapter<SeasonAdapter.MyViewHolder> {



    private List<season> thismaylikelilst;
    Context context;

    View itemView;



    private int lastPosition = -1;
    int row_index = 0;


    public SeasonAdapter(List<season> thismaylikelilst, Context context) {
        this.thismaylikelilst = thismaylikelilst;
        this.context = context;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, url,id,ppvstatus;
        public ImageView image;
        public CardView cardView;
        LinearLayout lin;

        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.newuploadid);
            image = (ImageView) view.findViewById(R.id.newuploadimg);
            name = (TextView) view.findViewById(R.id.newuploadname);
            url = (TextView) view.findViewById(R.id.newuploadurl);
            ppvstatus = (TextView) view.findViewById(R.id.ppvstatus);
            cardView=(CardView) view.findViewById(R.id.cardview);
            lin=(LinearLayout) view.findViewById(R.id.lin);


         //     holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimaryLight));

        }
    }

    public SeasonAdapter(ArrayList<season> thismaylikelilst) {
        this.thismaylikelilst = thismaylikelilst;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.season_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        //Picasso.get().load(videos.get(position).getGenre).into(holder.genre);

        for (int i=0; i<=position;i++) {

            int j=i+1;
            holder.id.setText("season " + j);

            holder.url.setText(thismaylikelilst.get(position).getId());

        }


        holder.name.setText(thismaylikelilst.get(position).getTitle());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=position;
                notifyDataSetChanged();
            }
        });


        if (row_index==position)
        {
            holder.lin.setBackgroundColor(R.attr.colorPrimary);
            holder.id.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else {
            holder.lin.setBackgroundColor(Color.parseColor("#45c5e1"));
            holder.id.setTextColor(Color.parseColor("#FFFFFF"));
        }


    }

    @Override
    public int getItemCount() {
        return thismaylikelilst.size();
    }

}

