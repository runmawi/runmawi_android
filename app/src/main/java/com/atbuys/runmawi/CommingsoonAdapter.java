package com.atbuys.runmawi;

import static com.atbuys.runmawi.SignupActivityNew.dialog;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


class CommingsoonAdapter extends RecyclerView.Adapter<CommingsoonAdapter.MyViewHolder> {



    private ArrayList<latestvideos> latestvideoslist;
    private android.content.Context context;
    private Object Context;
    View itemView;
    ImageView hoverimg;


    public CommingsoonAdapter(ArrayList<latestvideos> latestvideoslist, android.content.Context context) {
        this.latestvideoslist = latestvideoslist;
        this.context = context;
    }


    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,id,ppvstatus;
        public ImageView image;
        public TextView url;

        public MyViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.newuploadimg);
             name = (TextView) view.findViewById(R.id.newuploadname);
            url = (TextView) view.findViewById(R.id.newuploadurl);



        }
    }

    public CommingsoonAdapter(ArrayList<latestvideos> latestvideoslist) {
        this.latestvideoslist = latestvideoslist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comingsoon, parent, false);
        return new MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.url.setText(latestvideoslist.get(position).getDescription());
        holder.name.setText(latestvideoslist.get(position).getTitle());


        Picasso.get().
                load( latestvideoslist.get(position).getImage_url())
                .into(holder.image);
/*

        holder.url.setShowingLine(2);

        holder.url.addShowMoreText("show More");
        holder.url.addShowLessText("show Less");

        holder.url.setShowMoreColor(Color.WHITE); // or other color
        holder.url.setShowLessTextColor(Color.WHITE);
*/

        holder.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                showDialog(this);



                return true;
            }
        });



    }

    private void showDialog(View.OnLongClickListener onLongClickListener) {

        dialog = new Dialog(itemView.getContext());
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.imagehovering);

        dialog.show();

         hoverimg = (ImageView) dialog.findViewById(R.id.hoverimage);

          Glide.with(itemView.getContext()).load("https://res.cloudinary.com/demo/image/upload/kitten_fighting.gif").into(hoverimg);


    }


    @Override
    public int getItemCount() {
        return latestvideoslist.size();
    }

}

