package com.atbuys.runmawi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;


public class LangageVideoActivity extends AppCompatActivity {



    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<language_videos> channelvideosdata;
    LangageVideoAdopter channelvideoAdapter;
    String id,name;
    TextView channelid;
    LinearLayout novideoscat;
    String user_id,user_role;
    private ArrayList<videodetail> movie_detaildata;

    RelativeLayout progresslayout;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(  AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {


            setTheme(R.style.darktheme);

        }

        else {


            setTheme(R.style.AppTheme);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviecategorylist);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

         Intent in=getIntent();
         id=in.getStringExtra("id");
         name=in.getStringExtra("name");

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);

        channelid =(TextView) findViewById(R.id.namee);
        novideoscat =(LinearLayout) findViewById(R.id.novideoscat);
        progresslayout =(RelativeLayout) findViewById(R.id.progresslayout);

        channelid.setText(name);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               finish();
            }
        });



        movie_detaildata = new ArrayList<videodetail>();
        channelvideosdata = new ArrayList<language_videos>();

        adapter = new LangageVideoAdopter(channelvideosdata, this);
        layoutManager = new GridLayoutManager(this, 3);


        recyclerView = findViewById(R.id.rv_main);
        progressBar = findViewById(R.id.pb_home);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);


        Call<JSONResponse> channel = ApiClient.getInstance1().getApi().getVideoLangage(id,user_id);
        channel.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                progresslayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                JSONResponse jsonResponse = response.body();

                if (jsonResponse.getLanguage_videos().length ==0) {


                    progresslayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    novideoscat.setVisibility(View.VISIBLE);

                }
                else
                {
                    novideoscat.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    channelvideosdata = new ArrayList<>(Arrays.asList(jsonResponse.getLanguage_videos()));
                    channelvideoAdapter = new LangageVideoAdopter(channelvideosdata);
                    recyclerView.setAdapter(channelvideoAdapter);

                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });



        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(LangageVideoActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (channelvideosdata.size() > position) {
                            if (channelvideosdata.get(position) != null)

                            {

                                if(channelvideosdata.get(0).getType() == null)
                                {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, channelvideosdata.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                                            if (user_id == null) {


                                                Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                in.putExtra("id", channelvideosdata.get(position).getId());
                                                in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                startActivity(in);

                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (channelvideosdata.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((channelvideosdata.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = channelvideosdata.get(position).getVideo_url()+channelvideosdata.get(position).getPath()+".m3u8";

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((channelvideosdata.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (channelvideosdata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getVideo_url()+channelvideosdata.get(position).getPath()+".m3u8");
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    }
                                                } else if (channelvideosdata.get(position).getAccess().equalsIgnoreCase("ppv") || channelvideosdata.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    startActivity(in);
                                                } else {

                                                    if (channelvideosdata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getVideo_url()+channelvideosdata.get(position).getPath()+".m3u8");
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


                                else if (channelvideosdata.get(position).getType().equalsIgnoreCase("m3u8_url")) {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, channelvideosdata.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                                            if (user_id == null) {


                                                if(channelvideosdata.get(position).getAccess().equalsIgnoreCase("guest"))
                                                {
                                                    String videourl = channelvideosdata.get(position).getM3u8_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "guest");
                                                        //     in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "guest");
                                                        //in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    }
                                                }


                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (channelvideosdata.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    in.putExtra("continuee","0");
                                                    startActivity(in);

                                                }

                                                else if(channelvideosdata.get(position).getAccess().equalsIgnoreCase("guest"))
                                                {
                                                    String videourl = channelvideosdata.get(position).getM3u8_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "guest");
                                                        //     in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "guest");
                                                        //in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    }
                                                }
                                                else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    in.putExtra("continuee","0");
                                                    startActivity(in);

                                                } else if ((channelvideosdata.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    in.putExtra("continuee","0");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = channelvideosdata.get(position).getM3u8_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((channelvideosdata.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (channelvideosdata.get(position).getM3u8_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getM3u8_url());
                                                        in.putExtra("xtra", "subscriberented");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    }
                                                } else if (channelvideosdata.get(position).getAccess().equalsIgnoreCase("ppv") || channelvideosdata.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    in.putExtra("continuee","0");
                                                    startActivity(in);
                                                } else {

                                                    if (channelvideosdata.get(position).getM3u8_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getM3u8_url());
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


                                else if (channelvideosdata.get(position).getType().equalsIgnoreCase("embed")) {

                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, channelvideosdata.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                                            if (user_id == null) {

                                                Intent in = new Intent(getApplicationContext(), TrailerPlayerActivity.class);
                                                in.putExtra("id", channelvideosdata.get(position).getId());
                                                in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                startActivity(in);

                                            }

                                            if (user_id != null && user_role.equalsIgnoreCase("registered")) {

                                                if (response.body().getPpv_video_status().equalsIgnoreCase("pay_now")) {
//                                                    Intent in = new Intent(getApplicationContext(), YoutubeVideoHomepageActivity.class);
//                                                    in.putExtra("id", channelvideosdata.get(position).getId());
//                                                    startActivity(in);

                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {
//
//                                                    Intent in = new Intent(getApplicationContext(), YoutubeVideoHomepageActivity.class);
//                                                    in.putExtra("id", channelvideosdata.get(position).getId());
//                                                    startActivity(in);

                                                } else {


//                                                    Intent in = new Intent(getApplicationContext(), YoutubeVideoHomepageActivity.class);
//                                                    in.putExtra("id", channelvideosdata.get(position).getId());
//                                                    startActivity(in);

                                                }

                                            } else {
//
//                                                Intent in = new Intent(getApplicationContext(), YoutubeVideoHomepageActivity.class);
//                                                in.putExtra("id", channelvideosdata.get(position).getId());
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
                                else if (channelvideosdata.get(position).getType().equalsIgnoreCase("mp4_url")) {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, channelvideosdata.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                                            if (user_id == null) {


                                                Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                in.putExtra("id", channelvideosdata.get(position).getId());
                                                in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                startActivity(in);

                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (channelvideosdata.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((channelvideosdata.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = channelvideosdata.get(position).getMp4_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((channelvideosdata.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (channelvideosdata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getMp4_url());
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    }
                                                } else if (channelvideosdata.get(position).getAccess().equalsIgnoreCase("ppv") || channelvideosdata.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    startActivity(in);
                                                } else {

                                                    if (channelvideosdata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getMp4_url());
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
                                } else {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, channelvideosdata.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                                            if (user_id == null) {


                                                Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                in.putExtra("id", channelvideosdata.get(position).getId());
                                                in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                startActivity(in);

                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (channelvideosdata.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((channelvideosdata.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = channelvideosdata.get(position).getVideo_url() + channelvideosdata.get(position).getPath() + ".m3u8";

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((channelvideosdata.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (channelvideosdata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getVideo_url() + channelvideosdata.get(position).getPath() + ".m3u8");
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    }
                                                } else if (channelvideosdata.get(position).getAccess().equalsIgnoreCase("ppv") || channelvideosdata.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    startActivity(in);
                                                } else {

                                                    if (channelvideosdata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getVideo_url() + channelvideosdata.get(position).getPath() + ".m3u8");
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
