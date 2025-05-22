package com.atbuys.runmawi;

import static com.atbuys.runmawi.SignupActivityNew.dialog;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


class ContinueEpisodeWatchingAdopter extends RecyclerView.Adapter<ContinueEpisodeWatchingAdopter.MyViewHolder> {



    private ArrayList<episodes> continuevideolist;
    private android.content.Context context;
    private Object Context;
    View itemView;
    ImageView hoverimg;


    HomePageActivitywithFragments homePageActivitywithFragments;

    public ContinueEpisodeWatchingAdopter(ArrayList<episodes> continuevideolist, android.content.Context context) {
        this.continuevideolist = continuevideolist;
        this.context = context;
    }


    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, url,id,ppvstatus;
        public ImageView image,ig;
        ProgressBar seekbarr;


        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.newuploadid);
            image = (ImageView) view.findViewById(R.id.newuploadimg);
             name = (TextView) view.findViewById(R.id.newuploadname);
            url = (TextView) view.findViewById(R.id.newuploadurl);
            ppvstatus=(TextView)view.findViewById(R.id.ppvstatus);

            seekbarr =(ProgressBar) view.findViewById(R.id.seekbarr);

        }
    }

    public ContinueEpisodeWatchingAdopter(ArrayList<episodes> continuevideolist) {
        this.continuevideolist = continuevideolist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.continue_watching, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.id.setText(continuevideolist.get(position).getId());
        holder.url.setText(continuevideolist.get(position).getMp4_url());
        holder.name.setText(continuevideolist.get(position).getTitle());
        holder.seekbarr.setProgress(Integer.parseInt(continuevideolist.get(position).getWatch_percentage()));
      //  holder.ppvstatus.setText(continuevideolist.get(position).getPpv_status());


        Picasso.get().
                load( continuevideolist.get(position).getImage_url())
                .into(holder.image);



      //  Glide.with(holder.itemView.getContext()).load("https://res.cloudinary.com/demo/image/upload/kitten_fighting.gif").into(holder.image);


        holder.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                showDialog(this);



                return true;
            }
        });

     /*   holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                homePageActivitywithFragments.OpenBottom();

            }
        });
*/


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
        return continuevideolist.size();
    }

}

