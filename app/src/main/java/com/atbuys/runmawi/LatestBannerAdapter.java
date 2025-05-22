package com.atbuys.runmawi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Response;


class LatestBannerAdapter extends RecyclerView.Adapter<LatestBannerAdapter.MyViewHolder>  {

    private ArrayList<latestvideos> latestvideoslist;
    private ArrayList<videodetail> movie_detaildata;
    private Context context;
    View itemView;
    ImageView hoverimg;
    private static SharedPreferences prefs;
    String user_id,user_role;

    private MediaPlayer mediaPlayer;
    String fileDescriptor;
    String main_genre;

    public LatestBannerAdapter(ArrayList<latestvideos> latestvideoslist, Context context) {
        this.latestvideoslist = latestvideoslist;
        this.context = context;
        prefs = LatestBannerAdapter.this.context.getSharedPreferences(sharedpreferences.My_preference_name, 0);
        user_id=prefs.getString(sharedpreferences.user_id,null);
        user_role=prefs.getString(sharedpreferences.role,null);
        movie_detaildata = new ArrayList<videodetail>();
    }

    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, id,ppvstatus, title_desc;
        public ImageView image;
        public VideoView videoView;
        public Button url;
        public ProgressBar progress;

        public MyViewHolder(View view) {
            super(view);
           // id = (TextView) view.findViewById(R.id.newuploadid);
            image = (ImageView) view.findViewById(R.id.image);
            videoView = (VideoView) view.findViewById(R.id.textureView);
             name = (TextView) view.findViewById(R.id.title);
            url = (Button) view.findViewById(R.id.watch);
            ppvstatus=(TextView)view.findViewById(R.id.close);
            progress = (ProgressBar) view.findViewById(R.id.progress);
            title_desc = (TextView) view.findViewById(R.id.title_desc);
        }
    }
    public LatestBannerAdapter(ArrayList<latestvideos> latestvideoslist) {
        this.latestvideoslist = latestvideoslist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.focus_list, parent, false);
        return new MyViewHolder(itemView);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

      /*  holder.id.setText(latestvideoslist.get(position).getId());
        holder.url.setText(latestvideoslist.get(position).getVideo_url());
        holder.name.setText(latestvideoslist.get(position).getTitle());
        holder.ppvstatus.setText(latestvideoslist.get(position).getPpv_status());*/



        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, latestvideoslist.get(position).getId());
        res.enqueue(new retrofit2.Callback<JSONResponse>() {

                        @Override
                        public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                            JSONResponse jsonResponse  = response.body();
                            main_genre = jsonResponse.getMain_genre();

                            holder.title_desc.setText(main_genre+". "+latestvideoslist.get(position).getYear()+". "+latestvideoslist.get(position).getRating());

                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {

                            Toast.makeText(context,""+t.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });



        Picasso.get().
                load( latestvideoslist.get(position).getImage_url())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.image);

        holder.name.setText(latestvideoslist.get(position).getTitle());
        holder.image.setVisibility(View.VISIBLE);


        try {

            final String videopath = latestvideoslist.get(position).getTrailer();
            holder.videoView.setVideoPath(videopath);
            holder.videoView.requestFocus();
            holder.videoView.start();


            holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @SuppressLint("Range")
                @Override
                public void onPrepared(MediaPlayer mp) {
                    holder.videoView.seekTo(0);
                    holder.videoView.setVisibility(View.VISIBLE);

                    mp.start();

                    holder.videoView.setAlpha((float) 100.0);
                    holder.image.setVisibility(View.GONE);
                    holder.progress.setVisibility(View.GONE);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        holder.url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


/*                if (latestvideoslist.get(position).getType().equalsIgnoreCase("embed")) {
                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, latestvideoslist.get(position).getId());
                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                            JSONResponse jsonResponse = response.body();
                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));

                            if (user_id == null) {
                                Intent in = new Intent(context, TrailerPlayerActivity.class);
                                in.putExtra("id", latestvideoslist.get(position).getId());
                                in.putExtra("url", latestvideoslist.get(position).getTrailer());
                                context.startActivity(in);
                            }

                            if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                if (response.body().getPpv_video_status().equalsIgnoreCase("pay_now")) {
                                    Intent in = new Intent(context, YoutubeVideoHomepageActivity.class);
                                    in.putExtra("id", latestvideoslist.get(position).getId());
                                    context.startActivity(in);

                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                    Intent in = new Intent(context, YoutubeVideoHomepageActivity.class);
                                    in.putExtra("id", latestvideoslist.get(position).getId());
                                    context.startActivity(in);

                                } else {

                                    Intent in = new Intent(context, YoutubeVideoHomepageActivity.class);
                                    in.putExtra("id", latestvideoslist.get(position).getId());
                                    context.startActivity(in);
                                }

                            } else {

                                Intent in = new Intent(context, YoutubeVideoHomepageActivity.class);
                                in.putExtra("id", latestvideoslist.get(position).getId());
                                context.startActivity(in);


                            }
                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Log.d("Error", t.getMessage());
                        }
                    });

                }*/
                //else {
                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, latestvideoslist.get(position).getId());
                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                            JSONResponse jsonResponse = response.body();
                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));

                            if (user_id == null) {

                                Intent in = new Intent(context, HomePageVideoActivity.class);
                                in.putExtra("id", latestvideoslist.get(position).getId());
                                in.putExtra("url", latestvideoslist.get(position).getTrailer());
                                context.startActivity(in);

                            }

                            else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                 if (latestvideoslist.get(position).getAccess().equalsIgnoreCase("ppv"))
                                 {

                                    Intent in = new Intent(context, HomePageVideoActivity.class);
                                    in.putExtra("id", latestvideoslist.get(position).getId());
                                    in.putExtra("url", latestvideoslist.get(position).getTrailer());
                                    in.putExtra("xtra", "norent");
                                    context.startActivity(in);

                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                    Intent in = new Intent(context, HomePageVideoActivity.class);
                                    in.putExtra("id", latestvideoslist.get(position).getId());
                                    in.putExtra("url", latestvideoslist.get(position).getTrailer());
                                    in.putExtra("xtra", "norent");
                                    context.startActivity(in);

                                }
                                 else if ((latestvideoslist.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                     Intent in = new Intent(context, HomePageVideoActivity.class);
                                     in.putExtra("id", latestvideoslist.get(position).getId());
                                     in.putExtra("url", latestvideoslist.get(position).getTrailer());
                                     in.putExtra("xtra", "subscriber_content");
                                     context.startActivity(in);

                                 }
                                 else {

                                    String videourl = latestvideoslist.get(position).getMp4_url() ;

                                    if (videourl == null)
                                    {
                                        Intent in = new Intent(context, HomePageVideoActivity.class);
                                        in.putExtra("id", latestvideoslist.get(position).getId());
                                        in.putExtra("url", latestvideoslist.get(position).getTrailer());
                                        in.putExtra("xtra", "rentted");
                                        context.startActivity(in);
                                    }


                                     else {
                                        Intent in = new Intent(context, HomePageVideoActivity.class);
                                        in.putExtra("id", latestvideoslist.get(position).getId());
                                        in.putExtra("url", videourl);
                                        in.putExtra("xtra", "rentted");
                                        context.startActivity(in);
                                    }
                                }

                            }
                            else {

                                if ((latestvideoslist.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                    if(latestvideoslist.get(position).getMp4_url() == null)
                                    {
                                        Intent in = new Intent(context, HomePageVideoActivity.class);
                                        in.putExtra("id", latestvideoslist.get(position).getId());
                                        in.putExtra("url", latestvideoslist.get(position).getTrailer());
                                        in.putExtra("xtra", "subscriberented");
                                        context.startActivity(in);
                                    }
                                    else {
                                        Intent in = new Intent(context, HomePageVideoActivity.class);
                                        in.putExtra("id", latestvideoslist.get(position).getId());
                                        in.putExtra("url", latestvideoslist.get(position).getMp4_url());
                                        in.putExtra("xtra", "subscriberented");
                                        context.startActivity(in);
                                    }
                                }

                               else if(latestvideoslist.get(position).getAccess().equalsIgnoreCase("ppv") || latestvideoslist.get(position).getAccess().equalsIgnoreCase("expired"))
                                {
                                    Intent in = new Intent(context, HomePageVideoActivity.class);
                                    in.putExtra("id", latestvideoslist.get(position).getId());
                                    in.putExtra("url", latestvideoslist.get(position).getTrailer());
                                    in.putExtra("xtra", "subscriberrent");
                                    context.startActivity(in);
                                }

                                else {

                                    if(latestvideoslist.get(position).getMp4_url() == null)
                                    {
                                        Intent in = new Intent(context, HomePageVideoActivity.class);
                                        in.putExtra("id", latestvideoslist.get(position).getId());
                                        in.putExtra("url", latestvideoslist.get(position).getTrailer());
                                        in.putExtra("xtra", "Norent");
                                        context.startActivity(in);
                                    }
                                    else
                                        {

                                        Intent in = new Intent(context, HomePageVideoActivity.class);
                                        in.putExtra("id", latestvideoslist.get(position).getId());
                                        in.putExtra("url", latestvideoslist.get(position).getMp4_url());
                                        in.putExtra("xtra", "Norent");
                                        context.startActivity(in);
                                    }
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Log.d("Error", t.getMessage());
                        }
                    });

                }

        });

        }

    @Override
    public int getItemCount() {
        return latestvideoslist.size();
    }

}

