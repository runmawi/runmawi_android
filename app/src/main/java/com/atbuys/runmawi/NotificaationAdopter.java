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

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


class NotificaationAdopter extends RecyclerView.Adapter<NotificaationAdopter.MyViewHolder> {



    private ArrayList<notifications> notificationdata;
    private Context context;




    public NotificaationAdopter(ArrayList<notifications> notificationdata, Context context) {
        this.notificationdata = notificationdata;
        this.context = context;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView notify_title, notify_status,notify_id,notify_message;
        public ImageView notify_image;
        public LinearLayout card;

        public MyViewHolder(View view) {
            super(view);
            notify_id = (TextView) view.findViewById(R.id.notificationid);
            notify_image = (ImageView) view.findViewById(R.id.notificationimg);
             notify_title = (TextView) view.findViewById(R.id.notificationtitle);
            notify_status = (TextView) view.findViewById(R.id.notificationstatus);
            notify_message = (TextView) view.findViewById(R.id.notificationmsg);
            card =(LinearLayout) view.findViewById(R.id.card);

        }
    }

    public NotificaationAdopter(ArrayList<notifications> notificationdata) {
        this.notificationdata = notificationdata;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        if(notificationdata.get(position).getRead_status().equalsIgnoreCase(String.valueOf(0))) {
            holder.notify_id.setText(notificationdata.get(position).getId());
            holder.notify_status.setText(notificationdata.get(position).getRead_status());
            holder.notify_title.setText(notificationdata.get(position).getTitle());
            holder.notify_message.setText(notificationdata.get(position).getMessage());

        }

        else {

            holder.card.setBackgroundColor(Color.DKGRAY);
            holder.notify_id.setText(notificationdata.get(position).getId());
            holder.notify_status.setText(notificationdata.get(position).getRead_status());
            holder.notify_title.setText(notificationdata.get(position).getTitle());
            holder.notify_message.setText(notificationdata.get(position).getMessage());

        }
        /*Picasso.get().
                load( )
                .into(holder.notify_image);*/

    }


    @Override
    public int getItemCount() {
        return notificationdata.size();
    }

}

