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

class UserCommentsAdopter extends RecyclerView.Adapter<UserCommentsAdopter.MyViewHolder> {



    private ArrayList<user_comments> usercommentlist;
    private Context context;

    private static int currentPosition = 0;

    public UserCommentsAdopter(ArrayList<user_comments> usercommentlist, Context context) {
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

    public UserCommentsAdopter(ArrayList<user_comments> usercommentlist) {
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

        //Picasso.get().load(videos.get(position).getGenre).into(holder.genre);

        holder.username.setText(usercommentlist.get(position).getBody());
        holder.usercomment.setText(usercommentlist.get(position).getBody());


        if(usercommentlist.get(position).getUser_profile().equalsIgnoreCase("https://runmawi.com/public/uploads/avatars/default.png"))
        {
            Picasso.get().load(R.drawable.profileicon);
        }
        else {
            Picasso.get().
                    load(usercommentlist.get(position).getUser_profile())
                    .into(holder.userimage);
        }


    }


    @Override
    public int getItemCount() {
        return usercommentlist.size();
    }



}

