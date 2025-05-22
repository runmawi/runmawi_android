package com.atbuys.runmawi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


class LivestreamAdapter extends RecyclerView.Adapter<LivestreamAdapter.MyViewHolder> {



    private ArrayList<livetream> latestvideoslist;
    private android.content.Context context;
    private Object Context;
    View itemView;
    ImageView hoverimg;


    public LivestreamAdapter(ArrayList<livetream> latestvideoslist, android.content.Context context) {
        this.latestvideoslist = latestvideoslist;
        this.context = context;
    }


    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
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

    public LivestreamAdapter(ArrayList<livetream> latestvideoslist) {
        this.latestvideoslist = latestvideoslist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.latest_videos, parent, false);
        return new MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.id.setText(latestvideoslist.get(position).getId());
        holder.url.setText(latestvideoslist.get(position).getVideo_url());
        holder.name.setText(latestvideoslist.get(position).getTitle());
        holder.ppvstatus.setText(latestvideoslist.get(position).getPpv_status());


        Picasso.get().
                load("https://runmawi.com/public/uploads/images/"+ latestvideoslist.get(position).getMobile_image())
                .fit()
                .into(holder.image);



      //  Glide.with(holder.itemView.getContext()).load("https://res.cloudinary.com/demo/image/upload/kitten_fighting.gif").into(holder.image);


/*
        holder.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                showDialog(this);



                return true;
            }
        });
*/



    }

/*
    private void showDialog(View.OnLongClickListener onLongClickListener) {

        dialog = new Dialog(itemView.getContext());
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.imagehovering);

        dialog.show();

         hoverimg = (ImageView) dialog.findViewById(R.id.hoverimage);

          Glide.with(itemView.getContext()).load("https://res.cloudinary.com/demo/image/upload/kitten_fighting.gif").into(hoverimg);


    }
*/


    @Override
    public int getItemCount() {
        return latestvideoslist.size();
    }

}

