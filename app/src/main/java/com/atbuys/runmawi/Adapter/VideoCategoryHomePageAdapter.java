package com.atbuys.runmawi.Adapter;

import static com.atbuys.runmawi.UserHomeFragment.mediaplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.atbuys.runmawi.HomePageVideoActivityNew;
import com.squareup.picasso.Picasso;
import com.atbuys.runmawi.AlbumAudioPageActivity;
import com.atbuys.runmawi.ApiClient;
import com.atbuys.runmawi.ChannalPageActivity1;
import com.atbuys.runmawi.ChannelpartnerlistActivity;
import com.atbuys.runmawi.ContentpartnerlistActivity;
import com.atbuys.runmawi.HomePageLiveActivity;
import com.atbuys.runmawi.HomePageVideoActivity;
import com.atbuys.runmawi.JSONResponse;
import com.atbuys.runmawi.MediaPlayerPageActivity;
import com.atbuys.runmawi.Model.data;
import com.atbuys.runmawi.R;
import com.atbuys.runmawi.SeasonEpisodeCopyActivity;
import com.atbuys.runmawi.audiodetail;
import com.atbuys.runmawi.livedetail;
import com.atbuys.runmawi.sharedpreferences;
import com.atbuys.runmawi.videodetail;
import com.atbuys.runmawi.videossubtitles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;


public class VideoCategoryHomePageAdapter extends RecyclerView.Adapter<VideoCategoryHomePageAdapter.MovieViewHolder> {


    private List<data> movieList;
    private Context context;
    boolean status;
    data movie1;

    private static SharedPreferences prefs;
    String user_id, user_role;
    private ArrayList<videodetail> movie_detaildata;
    private ArrayList<audiodetail> audiodetail;
    private ArrayList<livedetail> livemovie_detaildata;
    private ArrayList<videossubtitles> videossubtitles;

    String channal_image, channal_banner, channel_slug, channel_name, tittle, mp3;

    public VideoCategoryHomePageAdapter(List<data> list, Context context,boolean status) {
        this.movieList = list;
        this.context = context;
        this.status=status;

        prefs = context.getSharedPreferences(sharedpreferences.My_preference_name, 0);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);
        movie_detaildata = new ArrayList<videodetail>();
        audiodetail = new ArrayList<>();
        videossubtitles = new ArrayList<videossubtitles>();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (status){
            return new MovieViewHolder(LayoutInflater.from(context).inflate(R.layout.landscape_layout, parent, false));
        }else {
            return new MovieViewHolder(LayoutInflater.from(context).inflate(R.layout.row_layout_channel2, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        movie1 = movieList.get(position);


        //  holder.textsub.setText(moviesubtitles.get(position).getUrl());

        holder.textViewTitle.setText(movie1.getId1());
        holder.textsub.setText(movie1.getSource());
        channal_banner = movie1.getChannel_image();
        channal_image = movie1.getImage_url();
        channel_slug = movie1.getChannel_slug();
        channel_name = movie1.getChannel_name();
        tittle = movie1.getName();
        mp3 = movie1.getMp3_url();
        // holder..setImageAlpha(movie1.getImage_url());

        // Toast.makeText(context, ""+holder.textViewTitle, Toast.LENGTH_SHORT).show();

        holder.trailerurl.setText(movie1.getTitle());

        if (movie1.getRating() == null || movie1.getRating() == "" || movie1.getRating() == "0") {
            holder.ratingcard.setVisibility(View.GONE);
        } else {
            holder.rating.setText(movie1.getRating() + ". 0");
        }

        //String txt=movie.getId();


        holder.textViewGenre.setText(movie1.getPpv_status() + movie1.getId());

        Picasso.get().
                load(movie1.getImage_url())
                .into(holder.imageViewMovie);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            private boolean isClicked = false;
            @Override
            public void onClick(View view) {


                if (holder.textsub.getText().toString().equalsIgnoreCase("Videos")) {

                    //Toast.makeText(context,  ""+user_id+"---"+movieList.get(position).getId1(), Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(context, HomePageVideoActivity.class);
                    in.putExtra("id", movieList.get(position).getId1());
                    in.putExtra("url", "videourl");
                    in.putExtra("xtra", "rentted");
                    in.putExtra("data", "videos");
                    in.putExtra("ads", "");
                    context.startActivity(in);
                   /* Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, movieList.get(position).getId1());
                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                            JSONResponse jsonResponse = response.body();
                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));

                            if (user_id == null) {


                                Intent in = new Intent(context, HomePageVideoActivity.class);
                                in.putExtra("id", movieList.get(position).getId1());
                                //in.putExtra("url", holder.trailerurl.getText().toString());
                                in.putExtra("data", "videos");
                                in.putExtra("ads", "");
                                context.startActivity(in);

                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {

                                if (holder.textsub.getText().toString().equalsIgnoreCase("ppv")) {

                                    Intent in = new Intent(context, HomePageVideoActivity.class);
                                    in.putExtra("id", movieList.get(position).getId1());
                                    //in.putExtra("url", holder.trailerurl.getText().toString());
                                    in.putExtra("xtra", "norent");
                                    in.putExtra("ads", "");
                                    in.putExtra("data", "videos");
                                    context.startActivity(in);

                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                    Log.d("nbdncbdbdncbx", "yyy");
                                    Intent in = new Intent(context, HomePageVideoActivity.class);
                                    in.putExtra("id", movieList.get(position).getId1());
                                    //in.putExtra("url", holder.trailerurl.getText().toString());
                                    in.putExtra("xtra", "norent");
                                    in.putExtra("ads", "");
                                    in.putExtra("data", "videos");
                                    context.startActivity(in);
                                } else if (holder.textsub.getText().toString().equalsIgnoreCase("subscriber") || holder.textsub.getText().toString().equalsIgnoreCase("admin")) {

                                    Intent in = new Intent(context, HomePageVideoActivity.class);
                                    in.putExtra("id", movieList.get(position).getId1());
                                    //in.putExtra("url", holder.trailerurl.getText().toString());
                                    in.putExtra("xtra", "subscriber_content");
                                    in.putExtra("ads", "");
                                    in.putExtra("data", "videos");
                                    context.startActivity(in);
                                } else {

                                    String videourl = holder.textViewGenre.getText().toString();

                                                *//*Intent in = new Intent(getContext(), OnlinePlayerActivity.class);
                                                in.putExtra("id", movie_detaildata.get(position).getId());
                                                in.putExtra("url",videourl);
                                                startActivity(in);*//*
                                    Intent in = new Intent(context, HomePageVideoActivity.class);
                                    in.putExtra("id", movieList.get(position).getId1());
                                    in.putExtra("url", videourl);
                                    in.putExtra("xtra", "rentted");
                                    in.putExtra("data", "videos");
                                    in.putExtra("ads", "");
                                    context.startActivity(in);
                                }

                            } else {

                                if (holder.textsub.getText().toString().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view")) {

                                    Intent in = new Intent(context, HomePageVideoActivity.class);
                                    in.putExtra("id", movieList.get(position).getId1());
                                    //in.putExtra("url", holder.trailerurl.getText().toString());
                                    in.putExtra("xtra", "subscriberented");
                                    in.putExtra("data", "videos");
                                    in.putExtra("ads", "");
                                    context.startActivity(in);

                                } else if (holder.textsub.getText().toString().equalsIgnoreCase("ppv") || holder.textsub.getText().toString().equalsIgnoreCase("expired")) {

                                    Intent in = new Intent(context, HomePageVideoActivity.class);
                                    in.putExtra("id", movieList.get(position).getId1());
                                    //in.putExtra("url", holder.trailerurl.getText().toString());
                                    in.putExtra("xtra", "subscriberrent");
                                    in.putExtra("data", "videos");
                                    in.putExtra("ads", "");
                                    context.startActivity(in);
                                } else {

                                    Intent in = new Intent(context, HomePageVideoActivity.class);
                                    in.putExtra("id", movieList.get(position).getId1());
                                    //in.putExtra("url", holder.trailerurl.getText().toString());
                                    in.putExtra("xtra", "Norent");
                                    in.putExtra("data", "videos");
                                    in.putExtra("ads", "");
                                    context.startActivity(in);
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Log.d("Error", t.getMessage());
                        }
                    });*/

                } else if (holder.textsub.getText().toString().equalsIgnoreCase("Livestream")) {


                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getLiveDetail(user_id, movieList.get(position).getId1());
                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                            JSONResponse jsonResponse = response.body();
                            livemovie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getLivedetail()));
                            if (user_id == null) {

                                Intent in = new Intent(context, HomePageLiveActivity.class);
                                in.putExtra("id", movieList.get(position).getId1());
                                //in.putExtra("url", holder.trailerurl.getText().toString());
                                in.putExtra("data", "videos");
                                in.putExtra("ads", "");
                                context.startActivity(in);

                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {

                                if (holder.textsub.getText().toString().equalsIgnoreCase("ppv")) {

                                    Intent in = new Intent(context, HomePageLiveActivity.class);
                                    in.putExtra("id", movieList.get(position).getId1());
                                    //in.putExtra("url", holder.trailerurl.getText().toString());
                                    in.putExtra("xtra", "norent");
                                    in.putExtra("ads", "");
                                    in.putExtra("data", "videos");
                                    context.startActivity(in);

                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                    Log.d("nbdncbdbdncbx", "yyy");
                                    Intent in = new Intent(context, HomePageLiveActivity.class);
                                    in.putExtra("id", movieList.get(position).getId1());
                                    //in.putExtra("url", holder.trailerurl.getText().toString());
                                    in.putExtra("xtra", "norent");
                                    in.putExtra("ads", "");
                                    in.putExtra("data", "videos");
                                    context.startActivity(in);
                                } else if (holder.textsub.getText().toString().equalsIgnoreCase("subscriber") || holder.textsub.getText().toString().equalsIgnoreCase("admin")) {

                                    Intent in = new Intent(context, HomePageLiveActivity.class);
                                    in.putExtra("id", movieList.get(position).getId1());
                                    //.putExtra("url", holder.trailerurl.getText().toString());
                                    in.putExtra("xtra", "subscriber_content");
                                    in.putExtra("ads", "");
                                    in.putExtra("data", "videos");
                                    context.startActivity(in);
                                } else {

                                    String videourl = holder.textViewGenre.getText().toString();

                                                /*Intent in = new Intent(getContext(), OnlinePlayerActivity.class);
                                                in.putExtra("id", movie_detaildata.get(position).getId());
                                                in.putExtra("url",videourl);
                                                startActivity(in);*/
                                    Intent in = new Intent(context, HomePageLiveActivity.class);
                                    in.putExtra("id", movieList.get(position).getId1());
                                    in.putExtra("url", videourl);
                                    in.putExtra("xtra", "rentted");
                                    in.putExtra("data", "videos");
                                    in.putExtra("ads", "");
                                    context.startActivity(in);
                                }

                            } else {

                                if (holder.textsub.getText().toString().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view")) {

                                    Intent in = new Intent(context, HomePageLiveActivity.class);
                                    in.putExtra("id", movieList.get(position).getId1());
                                    //in.putExtra("url", holder.trailerurl.getText().toString());
                                    in.putExtra("xtra", "subscriberented");
                                    in.putExtra("data", "videos");
                                    in.putExtra("ads", "");
                                    context.startActivity(in);

                                } else if (holder.textsub.getText().toString().equalsIgnoreCase("ppv") || holder.textsub.getText().toString().equalsIgnoreCase("expired")) {
                                    Intent in = new Intent(context, HomePageLiveActivity.class);
                                    in.putExtra("id", movieList.get(position).getId1());
                                   // in.putExtra("url", holder.trailerurl.getText().toString());
                                    in.putExtra("xtra", "subscriberrent");
                                    in.putExtra("data", "videos");
                                    in.putExtra("ads", "");
                                    context.startActivity(in);
                                } else {
                                    Intent in = new Intent(context, HomePageLiveActivity.class);
                                    in.putExtra("id", movieList.get(position).getId1());
                                    //in.putExtra("url", holder.trailerurl.getText().toString());
                                    in.putExtra("xtra", "Norent");
                                    in.putExtra("data", "videos");
                                    in.putExtra("ads", "");
                                    context.startActivity(in);
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Log.d("Error", t.getMessage());
                        }
                    });

                } else if (holder.textsub.getText().toString().equalsIgnoreCase("Series")) {
                    Intent in = new Intent(context, SeasonEpisodeCopyActivity.class);
                    in.putExtra("id", movieList.get(position).getId1());
                    in.putExtra("image", movieList.get(position).getImage_url());
                    in.putExtra("desc", movieList.get(position).getDescription());
                    in.putExtra("title", movieList.get(position).getTitle());
                    context.startActivity(in);
                } else if (holder.textsub.getText().toString().equalsIgnoreCase("SeriesGenre")) {
                    Intent in = new Intent(context, SeasonEpisodeCopyActivity.class);
                    in.putExtra("id", movieList.get(position).getId1());
                    in.putExtra("image", movieList.get(position).getImage_url());
                    in.putExtra("desc", movieList.get(position).getDescription());
                    in.putExtra("title", movieList.get(position).getTitle());
                    context.startActivity(in);
                } else if (holder.textsub.getText().toString().equalsIgnoreCase("Audios_album")) {

                    Intent in = new Intent(context, AlbumAudioPageActivity.class);
                    in.putExtra("album_id", movieList.get(position).getId1());
                    context.startActivity(in);

                } else if (holder.textsub.getText().toString().equalsIgnoreCase("Audios")) {

                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, movieList.get(position).getId1());
                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                            JSONResponse jsonResponse = response.body();

                            if (jsonResponse.getAudiodetail().length == 0 || jsonResponse.getAudiodetail() == null) {
                                Toast.makeText(context, "Audio Not Available ", Toast.LENGTH_LONG).show();
                            } else {
                                audiodetail = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));

                                SharedPreferences.Editor editor = context.getSharedPreferences(sharedpreferences.My_preference_name, context.MODE_PRIVATE).edit();
                                editor.putBoolean(sharedpreferences.login, true);
                                editor.putString(sharedpreferences.audioid, audiodetail.get(0).getId());
                                editor.apply();
                                editor.commit();


                                Intent in = new Intent(context, MediaPlayerPageActivity.class);
                                in.putExtra("id", audiodetail.get(0).getId());
                                context.startActivity(in);
                                mediaplayer.reset();
                                mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                try {
                                    mediaplayer.setDataSource(audiodetail.get(0).getMp3_url());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    mediaplayer.prepare();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                mediaplayer.start();
                            }
                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Log.d("Error", t.getMessage());
                        }
                    });


                } else if (holder.textsub.getText().toString().equalsIgnoreCase("Channel_Partner")) {

                    Intent in = new Intent(context, ChannelpartnerlistActivity.class);
                    in.putExtra("slug", movieList.get(position).getChannel_slug());
                    in.putExtra("image", movieList.get(position).getImage_url());
                    in.putExtra("name", movieList.get(position).getChannel_name());
                    in.putExtra("channal_image", movieList.get(position).getChannel_image());
                    context.startActivity(in);

                } else if (holder.textsub.getText().toString().equalsIgnoreCase("Content_Partner")) {

                    Intent in = new Intent(context, ContentpartnerlistActivity.class);
                    in.putExtra("slug",  movieList.get(position).getSlug());
                    in.putExtra("image",  movieList.get(position).getImage_url());
                    in.putExtra("name",  movieList.get(position).getChannel_name());
                    in.putExtra("channal_image", movieList.get(position).getChannel_image());
                    context.startActivity(in);
                } else if (holder.textsub.getText().toString().equalsIgnoreCase("LiveCategory")) {

                    //check this line
                    Intent in = new Intent(view.getContext(), ChannalPageActivity1.class);
                    in.putExtra("id", movieList.get(position).getId1());
                    in.putExtra("name", "live_category");
                    in.putExtra("name1",  movieList.get(position).getName());
                    in.putExtra("number", "z");
                    view.getContext().startActivity(in);
                }else{
                    Intent in = new Intent(view.getContext(), ChannalPageActivity1.class);
                    in.putExtra("id", movieList.get(position).getId1());
                    in.putExtra("name", "category_videos");
                    in.putExtra("name1",  movieList.get(position).getName());
                    in.putExtra("number", "z");
                    view.getContext().startActivity(in);
                }

            }
        });


    }

    @Override
    public int getItemCount() {

        return movieList.size();

    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle, rating;
        private TextView textViewGenre, textsub, trailerurl;
        private ImageView imageViewMovie;
        private CardView ratingcard;


        public MovieViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_title);
            textViewGenre = itemView.findViewById(R.id.tv_genre);
            imageViewMovie = itemView.findViewById(R.id.image_view_movie);
            textsub = itemView.findViewById(R.id.tv_sub);
            trailerurl = itemView.findViewById(R.id.tv_trailerurl);
            rating = (TextView) itemView.findViewById(R.id.rating);
            ratingcard = (CardView) itemView.findViewById(R.id.ratingcard);
            
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (textsub.getText().toString().equalsIgnoreCase("Videos")) {
                        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, textViewTitle.getText().toString());
                        res.enqueue(new retrofit2.Callback<JSONResponse>() {
                            @Override
                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                JSONResponse jsonResponse = response.body();
                                movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));

                                if (user_id == null) {


                                    Intent in = new Intent(context, HomePageVideoActivity.class);
                                    in.putExtra("id", textViewTitle.getText().toString());
                                    in.putExtra("url", trailerurl.getText().toString());
                                    in.putExtra("data", "videos");
                                    in.putExtra("ads", "");
                                    // context.startActivity(in);

                                } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {

                                    if (textsub.getText().toString().equalsIgnoreCase("ppv")) {

                                        Intent in = new Intent(context, HomePageVideoActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url", trailerurl.getText().toString());
                                        in.putExtra("xtra", "norent");
                                        in.putExtra("ads", "");
                                        in.putExtra("data", "videos");
                                        context.startActivity(in);

                                    } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                        Log.d("nbdncbdbdncbx", "yyy");
                                        Intent in = new Intent(context, HomePageVideoActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url", trailerurl.getText().toString());
                                        in.putExtra("xtra", "norent");
                                        in.putExtra("ads", "");
                                        in.putExtra("data", "videos");
                                        context.startActivity(in);
                                    } else if (textsub.getText().toString().equalsIgnoreCase("subscriber") || textsub.getText().toString().equalsIgnoreCase("admin")) {

                                        Intent in = new Intent(context, HomePageVideoActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url", trailerurl.getText().toString());
                                        in.putExtra("xtra", "subscriber_content");
                                        in.putExtra("ads", "");
                                        in.putExtra("data", "videos");
                                        context.startActivity(in);
                                    } else {

                                        String videourl = textViewGenre.getText().toString();

                                                *//*Intent in = new Intent(getContext(), OnlinePlayerActivity.class);
                                                in.putExtra("id", movie_detaildata.get(position).getId());
                                                in.putExtra("url",videourl);
                                                startActivity(in);*//*
                                        Intent in = new Intent(context, HomePageVideoActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url", videourl);
                                        in.putExtra("xtra", "rentted");
                                        in.putExtra("data", "videos");
                                        in.putExtra("ads", "");
                                        context.startActivity(in);
                                    }

                                } else {

                                    if (textsub.getText().toString().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view")) {

                                        Intent in = new Intent(context, HomePageVideoActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url", trailerurl.getText().toString());
                                        in.putExtra("xtra", "subscriberented");
                                        in.putExtra("data", "videos");
                                        in.putExtra("ads", "");
                                        context.startActivity(in);

                                    } else if (textsub.getText().toString().equalsIgnoreCase("ppv") || textsub.getText().toString().equalsIgnoreCase("expired")) {

                                        Intent in = new Intent(context, HomePageVideoActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url", trailerurl.getText().toString());
                                        in.putExtra("xtra", "subscriberrent");
                                        in.putExtra("data", "videos");
                                        in.putExtra("ads", "");
                                        context.startActivity(in);
                                    } else {

                                        Intent in = new Intent(context, HomePageVideoActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url", trailerurl.getText().toString());
                                        in.putExtra("xtra", "Norent");
                                        in.putExtra("data", "videos");
                                        in.putExtra("ads", "");
                                        context.startActivity(in);
                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                Log.d("Error", t.getMessage());
                            }
                        });

                    } else if (textsub.getText().toString().equalsIgnoreCase("Livestream")) {


                        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getLiveDetail(user_id, textViewTitle.getText().toString());
                        res.enqueue(new retrofit2.Callback<JSONResponse>() {
                            @Override
                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                JSONResponse jsonResponse = response.body();
                                livemovie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getLivedetail()));
                                if (user_id == null) {

                                    Intent in = new Intent(context, HomePageLiveActivity.class);
                                    in.putExtra("id", textViewTitle.getText().toString());
                                    in.putExtra("url", trailerurl.getText().toString());
                                    in.putExtra("data", "videos");
                                    in.putExtra("ads", "");
                                    context.startActivity(in);

                                } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {

                                    if (textsub.getText().toString().equalsIgnoreCase("ppv")) {

                                        Intent in = new Intent(context, HomePageLiveActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url", trailerurl.getText().toString());
                                        in.putExtra("xtra", "norent");
                                        in.putExtra("ads", "");
                                        in.putExtra("data", "videos");
                                        context.startActivity(in);

                                    } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                        Log.d("nbdncbdbdncbx", "yyy");
                                        Intent in = new Intent(context, HomePageLiveActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url", trailerurl.getText().toString());
                                        in.putExtra("xtra", "norent");
                                        in.putExtra("ads", "");
                                        in.putExtra("data", "videos");
                                        context.startActivity(in);
                                    } else if (textsub.getText().toString().equalsIgnoreCase("subscriber") || textsub.getText().toString().equalsIgnoreCase("admin")) {

                                        Intent in = new Intent(context, HomePageLiveActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url", trailerurl.getText().toString());
                                        in.putExtra("xtra", "subscriber_content");
                                        in.putExtra("ads", "");
                                        in.putExtra("data", "videos");
                                        context.startActivity(in);
                                    } else {

                                        String videourl = textViewGenre.getText().toString();

                                                *//*Intent in = new Intent(getContext(), OnlinePlayerActivity.class);
                                                in.putExtra("id", movie_detaildata.get(position).getId());
                                                in.putExtra("url",videourl);
                                                startActivity(in);*//*
                                        Intent in = new Intent(context, HomePageLiveActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url", videourl);
                                        in.putExtra("xtra", "rentted");
                                        in.putExtra("data", "videos");
                                        in.putExtra("ads", "");
                                        context.startActivity(in);
                                    }

                                } else {

                                    if (textsub.getText().toString().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view")) {

                                        Intent in = new Intent(context, HomePageLiveActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url", trailerurl.getText().toString());
                                        in.putExtra("xtra", "subscriberented");
                                        in.putExtra("data", "videos");
                                        in.putExtra("ads", "");
                                        context.startActivity(in);

                                    } else if (textsub.getText().toString().equalsIgnoreCase("ppv") || textsub.getText().toString().equalsIgnoreCase("expired")) {
                                        Intent in = new Intent(context, HomePageLiveActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url", trailerurl.getText().toString());
                                        in.putExtra("xtra", "subscriberrent");
                                        in.putExtra("data", "videos");
                                        in.putExtra("ads", "");
                                        context.startActivity(in);
                                    } else {
                                        Intent in = new Intent(context, HomePageLiveActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url", trailerurl.getText().toString());
                                        in.putExtra("xtra", "Norent");
                                        in.putExtra("data", "videos");
                                        in.putExtra("ads", "");
                                        context.startActivity(in);
                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                Log.d("Error", t.getMessage());
                            }
                        });

                    } else if (textsub.getText().toString().equalsIgnoreCase("Series")) {
                        Intent in = new Intent(context, SeasonEpisodeCopyActivity.class);
                        in.putExtra("id", textViewTitle.getText().toString());
                        in.putExtra("image", movie1.getImage_url());
                        in.putExtra("desc", movie1.getDescription());
                        context.startActivity(in);
                    } else if (textsub.getText().toString().equalsIgnoreCase("SeriesGenre")) {
                        Intent in = new Intent(context, SeasonEpisodeCopyActivity.class);
                        in.putExtra("id", textViewTitle.getText().toString());
                        in.putExtra("image", movie1.getImage_url());
                        in.putExtra("desc", movie1.getDescription());
                        context.startActivity(in);
                    } else if (textsub.getText().toString().equalsIgnoreCase("Audios_album")) {


                        Intent in = new Intent(context, AlbumAudioPageActivity.class);
                        in.putExtra("album_id", textViewTitle.getText().toString());
                        context.startActivity(in);


                    } else if (textsub.getText().toString().equalsIgnoreCase("Audios")) {

                        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, textViewTitle.getText().toString());
                        res.enqueue(new retrofit2.Callback<JSONResponse>() {
                            @Override
                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                JSONResponse jsonResponse = response.body();

                                if (jsonResponse.getAudiodetail().length == 0 || jsonResponse.getAudiodetail() == null) {
                                    Toast.makeText(context, "Audio Not Available ", Toast.LENGTH_LONG).show();
                                } else {
                                    audiodetail = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));

                                    SharedPreferences.Editor editor = context.getSharedPreferences(sharedpreferences.My_preference_name, context.MODE_PRIVATE).edit();
                                    editor.putBoolean(sharedpreferences.login, true);
                                    editor.putString(sharedpreferences.audioid, audiodetail.get(0).getId());
                                    editor.apply();
                                    editor.commit();


                                    Intent in = new Intent(context, MediaPlayerPageActivity.class);
                                    in.putExtra("id", audiodetail.get(0).getId());
                                    context.startActivity(in);
                                    mediaplayer.reset();
                                    mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                    try {
                                        mediaplayer.setDataSource(audiodetail.get(0).getMp3_url());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        mediaplayer.prepare();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    mediaplayer.start();
                                }
                            }

                            @Override
                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                Log.d("Error", t.getMessage());
                            }
                        });


                    } else if (textsub.getText().toString().equalsIgnoreCase("Channel_Partner")) {

                        Intent in = new Intent(context, ChannelpartnerlistActivity.class);
                        in.putExtra("slug", channel_slug);
                        in.putExtra("image", channal_image);
                        in.putExtra("name", channel_name);
                        in.putExtra("channal_image", channal_banner);
                        context.startActivity(in);

                    } else if (textsub.getText().toString().equalsIgnoreCase("Content_Partner")) {

                        Intent in = new Intent(context, ContentpartnerlistActivity.class);
                        in.putExtra("slug", channel_slug);
                        in.putExtra("image", channal_image);
                        in.putExtra("name", channel_name);
                        in.putExtra("channal_image", channal_banner);
                        context.startActivity(in);
                    } else if (textsub.getText().toString().equalsIgnoreCase("LiveCategory")) {

                        //check this line
                        Intent in = new Intent(view.getContext(), ChannalPageActivity1.class);
                        in.putExtra("id", textViewTitle.getText().toString());
                        in.putExtra("name", "live_category");
                        in.putExtra("name1", tittle);
                        in.putExtra("number", "z");
                        view.getContext().startActivity(in);
                    }


                }
            });*/

        }
    }
}