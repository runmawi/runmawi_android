package com.atbuys.runmawi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;

public class WatchlaterActivity extends AppCompatActivity {


    WishlistAdopter wishlistAdopter;
    WishlistEpisodeAdopter wishlistEpisodeAdopter;

    RecyclerView watchlistrecycle,watchlistepisoderecycle;
    ProgressBar watchlistprogress,watchlistepisodeprogress;
    TextView novideos,noepisodes;
    private ArrayList<channel_videos> mywishlistdata;
    private ArrayList<episode> myepisodelist;
    private ArrayList<videodetail> movie_detaildata;
    String user_id,user_role,theme;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(  AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {


            setTheme(R.style.darktheme);

        }

        else {


            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlater);

        mywishlistdata = new ArrayList<channel_videos>();
        myepisodelist = new ArrayList<episode>();

        wishlistAdopter = new WishlistAdopter(mywishlistdata, this);
        wishlistEpisodeAdopter = new WishlistEpisodeAdopter(myepisodelist,this);

        movie_detaildata = new ArrayList<videodetail>();



        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id,null );
        user_role =prefs.getString(sharedpreferences.role,null);
        theme = prefs.getString(sharedpreferences.theme,"0");

        if( theme.equalsIgnoreCase("1")) {
            setTheme(R.style.AppTheme);
        }

        else {
            setTheme(R.style.darktheme);
        }


        watchlistprogress=(ProgressBar)findViewById(R.id.watchlistprogress1);
        watchlistrecycle = (RecyclerView) findViewById(R.id.watchlilstrecycle);
        novideos=(TextView) findViewById(R.id.novideos);

        watchlistepisodeprogress=(ProgressBar)findViewById(R.id.watchlistepisodeprogress);
        watchlistepisoderecycle = (RecyclerView) findViewById(R.id.watchlilstepisoderecycle);
        noepisodes=(TextView) findViewById(R.id.noepisode);



        watchlistrecycle.setLayoutManager(new GridLayoutManager(this, 3));
        watchlistrecycle.setHasFixedSize(true);
        watchlistrecycle.setAdapter(wishlistAdopter);


        watchlistepisoderecycle.setLayoutManager(new GridLayoutManager(this, 3));
        watchlistepisoderecycle.setHasFixedSize(true);
        watchlistepisoderecycle.setAdapter(wishlistEpisodeAdopter);





        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getMyWatchlater(user_id);
        movieres.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();

                watchlistprogress.setVisibility(View.GONE);
                if (jsonResponse.getChannel_videos().length == 0) {

                    novideos.setVisibility(View.VISIBLE);
                    watchlistrecycle.setVisibility(View.GONE);

                } else {

                    novideos.setVisibility(View.GONE);
                    watchlistrecycle.setVisibility(View.VISIBLE);
                    mywishlistdata = new ArrayList<>(Arrays.asList(jsonResponse.getChannel_videos()));
                    wishlistAdopter = new WishlistAdopter(mywishlistdata);
                    watchlistrecycle.setAdapter(wishlistAdopter);

                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });


        Call<JSONResponse> movieepis = ApiClient.getInstance1().getApi().getMyWatchlaterEpisode(user_id);
        movieepis.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();

                watchlistepisodeprogress.setVisibility(View.GONE);
                if (jsonResponse.getChannel_videos().length == 0) {

                    noepisodes.setVisibility(View.VISIBLE);
                    watchlistepisoderecycle.setVisibility(View.GONE);

                } else {

                    noepisodes.setVisibility(View.GONE);
                    watchlistepisoderecycle.setVisibility(View.VISIBLE);
                    myepisodelist = new ArrayList<>(Arrays.asList(jsonResponse.getEpisode()));
                    wishlistEpisodeAdopter = new WishlistEpisodeAdopter(myepisodelist);
                    watchlistepisoderecycle.setAdapter(wishlistEpisodeAdopter);

                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });


        watchlistrecycle.addOnItemTouchListener(
                new RecyclerItemClickListener(WatchlaterActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if(mywishlistdata.size()>position){
                            if (mywishlistdata.get(position)!= null){

                                if(mywishlistdata.get(position).getType() == null){
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, mywishlistdata.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                                            if (user_id == null) {


                                                Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                in.putExtra("id", mywishlistdata.get(position).getId());
                                                in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                startActivity(in);

                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (mywishlistdata.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", mywishlistdata.get(position).getId());
                                                    in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", mywishlistdata.get(position).getId());
                                                    in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((mywishlistdata.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", mywishlistdata.get(position).getId());
                                                    in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = mywishlistdata.get(position).getVideo_url()+mywishlistdata.get(position).getPath()+".m3u8";

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((mywishlistdata.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (mywishlistdata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", mywishlistdata.get(position).getVideo_url()+mywishlistdata.get(position).getPath()+".m3u8");
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    }
                                                } else if (mywishlistdata.get(position).getAccess().equalsIgnoreCase("ppv") || mywishlistdata.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", mywishlistdata.get(position).getId());
                                                    in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    startActivity(in);
                                                } else {

                                                    if (mywishlistdata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", mywishlistdata.get(position).getVideo_url()+mywishlistdata.get(position).getPath()+".m3u8");
                                                        in.putExtra("xtra", "Norent");
                                                        startActivity(in);
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

                                else if (mywishlistdata.get(position).getType().equalsIgnoreCase("embed")) {

                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, mywishlistdata.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                                            if (user_id == null) {

                                                Intent in = new Intent(getApplicationContext(), TrailerPlayerActivity.class);
                                                in.putExtra("id", mywishlistdata.get(position).getId());
                                                in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                startActivity(in);

                                            }

                                            if (user_id != null && user_role.equalsIgnoreCase("registered")) {

                                                if (response.body().getPpv_video_status().equalsIgnoreCase("pay_now")) {

//                                                    Intent in = new Intent(getApplicationContext(), YoutubeVideoHomepageActivity.class);
//                                                    in.putExtra("id", mywishlistdata.get(position).getId());
//                                                    startActivity(in);

                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {
//
//                                                    Intent in = new Intent(getApplicationContext(), YoutubeVideoHomepageActivity.class);
//                                                    in.putExtra("id", mywishlistdata.get(position).getId());
//                                                    startActivity(in);

                                                } else {


//                                                    Intent in = new Intent(getApplicationContext(), YoutubeVideoHomepageActivity.class);
//                                                    in.putExtra("id", mywishlistdata.get(position).getId());
//                                                    startActivity(in);

                                                }

                                            } else {
//
//                                                Intent in = new Intent(getApplicationContext(), YoutubeVideoHomepageActivity.class);
//                                                in.putExtra("id", mywishlistdata.get(position).getId());
//                                                startActivity(in);


                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                                            Log.d("Error", t.getMessage());
                                        }
                                    });

                                }
                                else if (mywishlistdata.get(position).getType().equalsIgnoreCase("mp4_url")) {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, mywishlistdata.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                                            if (user_id == null) {


                                                Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                in.putExtra("id", mywishlistdata.get(position).getId());
                                                in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                startActivity(in);

                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (mywishlistdata.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", mywishlistdata.get(position).getId());
                                                    in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", mywishlistdata.get(position).getId());
                                                    in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((mywishlistdata.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", mywishlistdata.get(position).getId());
                                                    in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = mywishlistdata.get(position).getMp4_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((mywishlistdata.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (mywishlistdata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", mywishlistdata.get(position).getMp4_url());
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    }
                                                } else if (mywishlistdata.get(position).getAccess().equalsIgnoreCase("ppv") || mywishlistdata.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", mywishlistdata.get(position).getId());
                                                    in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    startActivity(in);
                                                } else {

                                                    if (mywishlistdata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", mywishlistdata.get(position).getMp4_url());
                                                        in.putExtra("xtra", "Norent");
                                                        startActivity(in);
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


                                else if (mywishlistdata.get(position).getType().equalsIgnoreCase("m3u8_url")) {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, mywishlistdata.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                                            if (user_id == null) {


                                                if(mywishlistdata.get(position).getAccess().equalsIgnoreCase("guest"))
                                                {
                                                    String videourl = mywishlistdata.get(position).getM3u8_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "guest");
                                                        //     in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "guest");
                                                        //in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    }
                                                }


                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (mywishlistdata.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", mywishlistdata.get(position).getId());
                                                    in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    in.putExtra("continuee","0");
                                                    startActivity(in);

                                                }

                                                else if(mywishlistdata.get(position).getAccess().equalsIgnoreCase("guest"))
                                                {
                                                    String videourl = mywishlistdata.get(position).getM3u8_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "guest");
                                                        //     in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "guest");
                                                        //in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    }
                                                }
                                                else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", mywishlistdata.get(position).getId());
                                                    in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    in.putExtra("continuee","0");
                                                    startActivity(in);

                                                } else if ((mywishlistdata.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", mywishlistdata.get(position).getId());
                                                    in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    in.putExtra("continuee","0");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = mywishlistdata.get(position).getM3u8_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((mywishlistdata.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (mywishlistdata.get(position).getM3u8_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", mywishlistdata.get(position).getM3u8_url());
                                                        in.putExtra("xtra", "subscriberented");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    }
                                                } else if (mywishlistdata.get(position).getAccess().equalsIgnoreCase("ppv") || mywishlistdata.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", mywishlistdata.get(position).getId());
                                                    in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    in.putExtra("continuee","0");
                                                    startActivity(in);
                                                } else {

                                                    if (mywishlistdata.get(position).getM3u8_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", mywishlistdata.get(position).getM3u8_url());
                                                        in.putExtra("xtra", "Norent");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);
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



                                else {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, mywishlistdata.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                                            if (user_id == null) {


                                                Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                in.putExtra("id", mywishlistdata.get(position).getId());
                                                in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                startActivity(in);

                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (mywishlistdata.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", mywishlistdata.get(position).getId());
                                                    in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", mywishlistdata.get(position).getId());
                                                    in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((mywishlistdata.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", mywishlistdata.get(position).getId());
                                                    in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = mywishlistdata.get(position).getVideo_url() + mywishlistdata.get(position).getPath() + ".m3u8";

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((mywishlistdata.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (mywishlistdata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", mywishlistdata.get(position).getVideo_url() + mywishlistdata.get(position).getPath() + ".m3u8");
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    }
                                                } else if (mywishlistdata.get(position).getAccess().equalsIgnoreCase("ppv") || mywishlistdata.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", mywishlistdata.get(position).getId());
                                                    in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    startActivity(in);
                                                } else {

                                                    if (mywishlistdata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", mywishlistdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", mywishlistdata.get(position).getId());
                                                        in.putExtra("url", mywishlistdata.get(position).getVideo_url() + mywishlistdata.get(position).getPath() + ".m3u8");
                                                        in.putExtra("xtra", "Norent");
                                                        startActivity(in);
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
                            }

                            }

                    }
                })
        );

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
