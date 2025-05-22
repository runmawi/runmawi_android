package com.atbuys.runmawi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


class ContinueWatchingAdopter extends RecyclerView.Adapter<ContinueWatchingAdopter.MyViewHolder>  {

    private ArrayList<videos> continuevideolist;
    View itemView;
    ImageView hoverimg;
    private Context context1;
    private static SharedPreferences prefs;
    String user_id,user_role;

    private MediaPlayer mediaPlayer;
    String fileDescriptor;
    BottomSheetDialog bt;
    View view;

    public ContinueWatchingAdopter(ArrayList<videos> continuevideolist, Context context) {
        this.continuevideolist = continuevideolist;
        this.context1 = context;

    }

    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, url,id,ppvstatus,rating;
        public ImageView image;
        ProgressBar seekbarr;
        CardView ratingcard;

        public MyViewHolder(View view) {
            super(view);
           // id = (TextView) view.findViewById(R.id.newuploadid);
            id = (TextView) view.findViewById(R.id.newuploadid);
            image = (ImageView) view.findViewById(R.id.newuploadimg);
            name = (TextView) view.findViewById(R.id.newuploadname);
            url = (TextView) view.findViewById(R.id.newuploadurl);
            ppvstatus=(TextView)view.findViewById(R.id.ppvstatus);
            rating=(TextView)view.findViewById(R.id.rating);
            ratingcard=(CardView)view.findViewById(R.id.ratingcard);

            seekbarr =(ProgressBar) view.findViewById(R.id.seekbarr);

        }
    }
    public ContinueWatchingAdopter(ArrayList<videos> continuevideolist) {
        this.continuevideolist = continuevideolist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.continue_watching, parent, false);

          bt=new BottomSheetDialog(parent.getContext(),R.style.BottomSheetDialogTheme);
         view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_player_page,null);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.id.setText(continuevideolist.get(position).getId());
        holder.url.setText(continuevideolist.get(position).getVideo_url());
        holder.name.setText(continuevideolist.get(position).getTitle());
        if (continuevideolist.get(position).getRating()==null ||continuevideolist.get(position).getRating()=="" ||continuevideolist.get(position).getRating()=="0"){
            holder.ratingcard.setVisibility(View.GONE);
        }else {
            holder.rating.setText(continuevideolist.get(position).getRating()+". 0");
        }

        String xx= continuevideolist.get(position).getWatch_percentage();
        String[] splited = xx.split("\\.");

           holder.seekbarr.setProgress(Integer.parseInt(splited[0]));
        //  holder.ppvstatus.setText(continuevideolist.get(position).getPpv_status());



        Picasso.get().
                load(continuevideolist.get(position).getImage_url())
                .fit()
                .into(holder.image);


        /*holder.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                bt.setContentView(view);
                bt.show();
                return false;
            }
        });*/


        TextView play_trailer = (TextView) view.findViewById(R.id.play_trailer);
        TextView start_over = (TextView) view.findViewById(R.id.start);
        TextView resume_play = (TextView) view.findViewById(R.id.resume);
        TextView wishlist = (TextView) view.findViewById(R.id.wishlists);
        TextView download = (TextView) view.findViewById(R.id.downloaded);
        TextView remove = (TextView) view.findViewById(R.id.remove);
        TextView share = (TextView) view.findViewById(R.id.share);
        TextView details = (TextView) view.findViewById(R.id.view_details);

        play_trailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(view.getContext(), TrailerPlayerActivity.class);
                in.putExtra("id", continuevideolist.get(position).getId());
                in.putExtra("url", continuevideolist.get(position).getTrailer());
                view.getContext().startActivity(in);
            }
        });

        start_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent in = new Intent(view.getContext(), OnlinePlayerActivity.class);
                in.putExtra("id", continuevideolist.get(position).getId());
                in.putExtra("url", continuevideolist.get(position).getMp4_url());
                in.putExtra("suburl", "");
                in.putExtra("data","video");
                in.putExtra("ads","");
                view.getContext().startActivity(in);

            }
        });

        resume_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Api.getClient().getAddWishlistMovie(user_id, continuevideolist.get(position).getId(), "Channel", new Callback<Addtowishlistmovie>() {

                    @Override
                    public void success(Addtowishlistmovie addwishmovie, Response response) {

                        addwishmovie = addwishmovie;
                        if (addwishmovie.getStatus().equalsIgnoreCase("true")) {

                            wishlist.setCompoundDrawablesWithIntrinsicBounds(R.drawable.added, 0, 0, 0);
                            wishlist.setText("Remove From Wishlist");

                            Toast.makeText(view.getContext(), "" + addwishmovie.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        else if (addwishmovie.getStatus().equalsIgnoreCase("false")) {

                            wishlist.setCompoundDrawablesWithIntrinsicBounds(R.drawable.add, 0, 0, 0);
                            wishlist.setText("Add to  Wishlist");

                            Toast.makeText(view.getContext(), "" + addwishmovie.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(view.getContext(), "You are not registered user", Toast.LENGTH_LONG).show();

                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                        Toast.makeText(view.getContext(), "sd", Toast.LENGTH_LONG).show();
                    }
                });


            }
        });

    /*    download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });*/

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


/*
                Api.getClient().getRemoveContinueWatching(user_id, continuevideolist.get(position).getId(), new Callback<RemoveContinuWatching>() {
                    @Override
                    public void success(ContinuWatching continuWatching, Response response) {

                        continuWatching = continuWatching;
                        bt.hide();
                        Toast.makeText(view.getContext(),""+continuWatching.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {

                        Toast.makeText(view.getContext(),""+error, Toast.LENGTH_SHORT).show();

                    }
                });
*/

/*
                Api.getClient().getRemoveContinueWatching(user_id,continuevideolist.get(position).getId(), new Callback<ContinuWatching>() {

                    @Override
                    public void success(ContinuWatching continuWatching, Response response) {

                        continuWatching = continuWatching;
                        bt.hide();
                        Toast.makeText(getActivity(),""+continuWatching.getMessage(),Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
*/

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(view.getContext(), HomePageVideoActivity.class);
                in.putExtra("id", continuevideolist.get(position).getId());
                in.putExtra("url", continuevideolist.get(position).getTrailer());
                view.getContext().startActivity(in);

            }
        });

    }
    @Override
    public int getItemCount() {
        return continuevideolist.size();
    }

}

