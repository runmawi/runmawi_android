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


class RecentAudioAdapter extends RecyclerView.Adapter<RecentAudioAdapter.MyViewHolder> {



    private ArrayList<recent_audios> recent_audioslist;
    private Context context;




    public RecentAudioAdapter(ArrayList<recent_audios> recent_audioslist, Context context) {
        this.recent_audioslist = recent_audioslist;
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

    public RecentAudioAdapter(ArrayList<recent_audios> recent_audioslist) {
        this.recent_audioslist = recent_audioslist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_relses, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.id.setText(recent_audioslist.get(position).getId());
        holder.url.setText(recent_audioslist.get(position).getMp3_url());
        holder.name.setText(recent_audioslist.get(position).getTitle());

        Picasso.get().
                load( "https://runmawi.com/public/uploads/images/"+recent_audioslist.get(position).getImage())
                .into(holder.image);

    }


    @Override
    public int getItemCount() {
       return recent_audioslist.size();

    }

}

