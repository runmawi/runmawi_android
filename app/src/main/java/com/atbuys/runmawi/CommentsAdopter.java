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

class CommentsAdopter extends RecyclerView.Adapter<CommentsAdopter.MyViewHolder> {



    private ArrayList<comment> usercommentlist;
    private Context context;

    private static int currentPosition = 0;

    public CommentsAdopter(ArrayList<comment> usercommentlist, Context context) {
        this.usercommentlist = usercommentlist;
        this.context = context;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView usercomment,username;
        public ImageView userimage;

        public MyViewHolder(View view) {
            super(view);

            userimage = (ImageView) view.findViewById(R.id.userimage);
             username= (TextView) view.findViewById(R.id.username);
             usercomment=(TextView) view.findViewById(R.id.usercomment);


        }
    }

    public CommentsAdopter(ArrayList<comment> usercommentlist) {
        this.usercommentlist = usercommentlist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_comments, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {



        holder.username.setText(usercommentlist.get(position).getUsername());
        holder.usercomment.setText(usercommentlist.get(position).getComment());


        if(usercommentlist.get(position).getUser_image_url().equalsIgnoreCase("https://runmawi.com/public/uploads/avatars/default.png"))
        {
            Picasso.get().load(R.drawable.profileicon);
        }
        else {
            Picasso.get().
                    load(usercommentlist.get(position).getUser_image_url())
                    .into(holder.userimage);
        }

    }


    @Override
    public int getItemCount() {
        return usercommentlist.size();
    }


}

