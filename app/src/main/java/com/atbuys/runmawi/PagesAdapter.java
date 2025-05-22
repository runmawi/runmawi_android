package com.atbuys.runmawi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class PagesAdapter extends RecyclerView.Adapter<PagesAdapter.MyViewHolder> {

    private ArrayList<pages> pageslist;
    private Context context;


    public PagesAdapter(ArrayList<pages> pageslist, Context context) {
        this.pageslist = pageslist;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, url,id,ppvstatus;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.name1);


            name = (TextView) view.findViewById(R.id.url);

        }
    }

    public PagesAdapter(ArrayList<pages> pageslist) {
        this.pageslist = pageslist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.side_mennu3, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.id.setText(pageslist.get(position).getTitle());
     holder.name.setText(pageslist.get(position).getPage_url());

    }


    @Override
    public int getItemCount() {
        return pageslist.size();
    }

}

