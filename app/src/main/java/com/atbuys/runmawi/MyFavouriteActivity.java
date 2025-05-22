package com.atbuys.runmawi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;

public class MyFavouriteActivity extends AppCompatActivity {


    FavouriteAdopter favouriteAdopter;
    RecyclerView favouriteRecycle;
    ProgressBar favouriteProgress;
    private ArrayList<channel_videos> favoritedata;
    TextView novideos;
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
        setContentView(R.layout.activity_favourite);

        favoritedata = new ArrayList<channel_videos>();

        favouriteAdopter = new FavouriteAdopter(favoritedata, this);
        novideos=(TextView)findViewById(R.id.novideos);



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

        favouriteProgress=(ProgressBar)findViewById(R.id.favouriteprogress);
        favouriteRecycle = (RecyclerView) findViewById(R.id.favouriterecycle);
        favouriteRecycle.setLayoutManager(new GridLayoutManager(this, 3));

        favouriteRecycle.setHasFixedSize(true);
        favouriteRecycle.setAdapter(favouriteAdopter);

        movie_detaildata = new ArrayList<videodetail>();

        ImageView backarrow = (ImageView) findViewById(R.id.backarrow);

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getMyFavorites(user_id);
        movieres.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                favouriteProgress.setVisibility(View.GONE);

                if (jsonResponse.getChannel_videos().length == 0) {

                    novideos.setVisibility(View.VISIBLE);
                    favouriteRecycle.setVisibility(View.GONE);
                } else {
                    novideos.setVisibility(View.GONE);
                    favouriteRecycle.setVisibility(View.VISIBLE);
                    favoritedata = new ArrayList<>(Arrays.asList(jsonResponse.getChannel_videos()));
                    //Collections.reverse(favoritedata);
                    favouriteAdopter = new FavouriteAdopter(favoritedata);
                    favouriteRecycle.setAdapter(favouriteAdopter);
                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
        favouriteRecycle.addOnItemTouchListener(
                new RecyclerItemClickListener(MyFavouriteActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if(favoritedata.size()>position) {
                            if (favoritedata.get(position) != null) {

                                //Toast.makeText(MyFavouriteActivity.this, "id -->"+favoritedata.get(position).getId() , Toast.LENGTH_SHORT).show();

                                if(favoritedata.get(position).getType() == null)
                                {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, favoritedata.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                                            if (user_id == null) {


                                                Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                in.putExtra("id", favoritedata.get(position).getId());
                                                in.putExtra("url", favoritedata.get(position).getTrailer());
                                                startActivity(in);

                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (favoritedata.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", favoritedata.get(position).getId());
                                                    in.putExtra("url", favoritedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", favoritedata.get(position).getId());
                                                    in.putExtra("url", favoritedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((favoritedata.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", favoritedata.get(position).getId());
                                                    in.putExtra("url", favoritedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = favoritedata.get(position).getVideo_url()+favoritedata.get(position).getPath()+".m3u8";

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", favoritedata.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((favoritedata.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (favoritedata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", favoritedata.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", favoritedata.get(position).getVideo_url()+favoritedata.get(position).getPath()+".m3u8");
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    }
                                                } else if (favoritedata.get(position).getAccess().equalsIgnoreCase("ppv") || favoritedata.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", favoritedata.get(position).getId());
                                                    in.putExtra("url", favoritedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    startActivity(in);
                                                } else {

                                                    if (favoritedata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", favoritedata.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", favoritedata.get(position).getVideo_url()+favoritedata.get(position).getPath()+".m3u8");
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


                                else if (favoritedata.get(position).getType().equalsIgnoreCase("embed")) {

                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, favoritedata.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                                            if (user_id == null) {

                                                Intent in = new Intent(getApplicationContext(), TrailerPlayerActivity.class);
                                                in.putExtra("id", favoritedata.get(position).getId());
                                                in.putExtra("url", favoritedata.get(position).getTrailer());
                                                startActivity(in);

                                            }

                                            if (user_id != null && user_role.equalsIgnoreCase("registered")) {

                                                if (response.body().getPpv_video_status().equalsIgnoreCase("pay_now")) {
//                                                    Intent in = new Intent(getApplicationContext(), YoutubeVideoHomepageActivity.class);
//                                                    in.putExtra("id", favoritedata.get(position).getId());
//                                                    startActivity(in);

                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

//                                                    Intent in = new Intent(getApplicationContext(), YoutubeVideoHomepageActivity.class);
//                                                    in.putExtra("id", favoritedata.get(position).getId());
//                                                    startActivity(in);

                                                } else {

//
//                                                    Intent in = new Intent(getApplicationContext(), YoutubeVideoHomepageActivity.class);
//                                                    in.putExtra("id", favoritedata.get(position).getId());
//                                                    startActivity(in);

                                                }

                                            } else {

//                                                Intent in = new Intent(getApplicationContext(), YoutubeVideoHomepageActivity.class);
//                                                in.putExtra("id", favoritedata.get(position).getId());
//                                                startActivity(in);
//

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                                            Log.d("Error", t.getMessage());
                                        }
                                    });

                                }
                                else if (favoritedata.get(position).getType().equalsIgnoreCase("mp4_url")) {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, favoritedata.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));

                                            if (user_id == null) {

                                                Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                in.putExtra("id", favoritedata.get(position).getId());
                                                in.putExtra("url", favoritedata.get(position).getTrailer());
                                                startActivity(in);

                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (favoritedata.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", favoritedata.get(position).getId());
                                                    in.putExtra("url", favoritedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", favoritedata.get(position).getId());
                                                    in.putExtra("url", favoritedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((favoritedata.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", favoritedata.get(position).getId());
                                                    in.putExtra("url", favoritedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = favoritedata.get(position).getMp4_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", favoritedata.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((favoritedata.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (favoritedata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", favoritedata.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", favoritedata.get(position).getMp4_url());
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    }
                                                } else if (favoritedata.get(position).getAccess().equalsIgnoreCase("ppv") || favoritedata.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", favoritedata.get(position).getId());
                                                    in.putExtra("url", favoritedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    startActivity(in);
                                                } else {

                                                    if (favoritedata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", favoritedata.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", favoritedata.get(position).getMp4_url());
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



                                else if (favoritedata.get(position).getType().equalsIgnoreCase("m3u8_url")) {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, favoritedata.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                                            if (user_id == null) {


                                                if(favoritedata.get(position).getAccess().equalsIgnoreCase("guest"))
                                                {
                                                    String videourl = favoritedata.get(position).getM3u8_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", favoritedata.get(position).getTrailer());
                                                        in.putExtra("xtra", "guest");
                                                        //     in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "guest");
                                                        //in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    }
                                                }


                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (favoritedata.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", favoritedata.get(position).getId());
                                                    in.putExtra("url", favoritedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    in.putExtra("continuee","0");
                                                    startActivity(in);

                                                }

                                                else if(favoritedata.get(position).getAccess().equalsIgnoreCase("guest"))
                                                {
                                                    String videourl = favoritedata.get(position).getM3u8_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", favoritedata.get(position).getTrailer());
                                                        in.putExtra("xtra", "guest");
                                                        //     in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "guest");
                                                        //in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    }
                                                }
                                                else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", favoritedata.get(position).getId());
                                                    in.putExtra("url", favoritedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    in.putExtra("continuee","0");
                                                    startActivity(in);

                                                } else if ((favoritedata.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", favoritedata.get(position).getId());
                                                    in.putExtra("url", favoritedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    in.putExtra("continuee","0");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = favoritedata.get(position).getM3u8_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", favoritedata.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((favoritedata.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (favoritedata.get(position).getM3u8_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", favoritedata.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", favoritedata.get(position).getM3u8_url());
                                                        in.putExtra("xtra", "subscriberented");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    }
                                                } else if (favoritedata.get(position).getAccess().equalsIgnoreCase("ppv") || favoritedata.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", favoritedata.get(position).getId());
                                                    in.putExtra("url", favoritedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    in.putExtra("continuee","0");
                                                    startActivity(in);
                                                } else {

                                                    if (favoritedata.get(position).getM3u8_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", favoritedata.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", favoritedata.get(position).getM3u8_url());
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
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, favoritedata.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                                            if (user_id == null) {


                                                Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                in.putExtra("id", favoritedata.get(position).getId());
                                                in.putExtra("url", favoritedata.get(position).getTrailer());
                                                startActivity(in);

                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (favoritedata.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", favoritedata.get(position).getId());
                                                    in.putExtra("url", favoritedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", favoritedata.get(position).getId());
                                                    in.putExtra("url", favoritedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((favoritedata.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", favoritedata.get(position).getId());
                                                    in.putExtra("url", favoritedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = favoritedata.get(position).getVideo_url() + favoritedata.get(position).getPath() + ".m3u8";

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", favoritedata.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((favoritedata.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (favoritedata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", favoritedata.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", favoritedata.get(position).getVideo_url() + favoritedata.get(position).getPath() + ".m3u8");
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    }
                                                } else if (favoritedata.get(position).getAccess().equalsIgnoreCase("ppv") || favoritedata.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", favoritedata.get(position).getId());
                                                    in.putExtra("url", favoritedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    startActivity(in);
                                                } else {

                                                    if (favoritedata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", favoritedata.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", favoritedata.get(position).getId());
                                                        in.putExtra("url", favoritedata.get(position).getVideo_url() + favoritedata.get(position).getPath() + ".m3u8");
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
