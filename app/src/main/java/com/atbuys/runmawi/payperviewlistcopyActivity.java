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

public class payperviewlistcopyActivity extends AppCompatActivity {

    paypervideoAdopter wishlistAdopter;

    RecyclerView watchlistrecycle;


    private ArrayList<payperview_video> payperviewmoviedata;
    private ArrayList<videodetail> movie_detaildata;
    private RecyclerView.LayoutManager layoutManager;
    TextView novideos;
    ProgressBar ppvprogress;
    String user_id, user_role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payperviewcopy);

        //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);


        movie_detaildata = new ArrayList<videodetail>();
        payperviewmoviedata = new ArrayList<payperview_video>();


        wishlistAdopter = new paypervideoAdopter(payperviewmoviedata, this);
        layoutManager = new GridLayoutManager(this, 3);
        novideos = (TextView) findViewById(R.id.novideos);

        watchlistrecycle = (RecyclerView) findViewById(R.id.ppvrecycle);
        ppvprogress = (ProgressBar) findViewById(R.id.ppvprogress);


        watchlistrecycle.setHasFixedSize(true);
        watchlistrecycle.setLayoutManager(layoutManager);
        watchlistrecycle.setAdapter(wishlistAdopter);


        ImageView backarrow = (ImageView) findViewById(R.id.backarrow);

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Call<JSONResponse> audiores = ApiClient.getInstance1().getApi().getPPv(user_id);
        audiores.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();

                ppvprogress.setVisibility(View.GONE);

                if (jsonResponse.getpayperview_movie().length == 0 || jsonResponse.getpayperview_movie()==null) {
                    novideos.setVisibility(View.VISIBLE);
                    watchlistrecycle.setVisibility(View.GONE);

                } else {

                    watchlistrecycle.setVisibility(View.VISIBLE);
                    novideos.setVisibility(View.GONE);
                    payperviewmoviedata = new ArrayList<>(Arrays.asList(jsonResponse.getpayperview_movie()));
                    wishlistAdopter = new paypervideoAdopter(payperviewmoviedata);
                    watchlistrecycle.setAdapter(wishlistAdopter);


                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });


        watchlistrecycle.addOnItemTouchListener(
                (RecyclerView.OnItemTouchListener) new RecyclerItemClickListener(payperviewlistcopyActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (payperviewmoviedata.size() > position) {
                            if (payperviewmoviedata.get(position) != null) {

                                if (payperviewmoviedata.get(position).getType() == null) {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, payperviewmoviedata.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                                            if (user_id == null) {


                                                Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                startActivity(in);

                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (payperviewmoviedata.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                    in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    in.putExtra("continuee", "0");
                                                    startActivity(in);

                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                    in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    in.putExtra("continuee", "0");
                                                    startActivity(in);

                                                } else if ((payperviewmoviedata.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                    in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    in.putExtra("continuee", "0");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = payperviewmoviedata.get(position).getVideo_url() + payperviewmoviedata.get(position).getPath() + ".m3u8";

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                        in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        in.putExtra("continuee", "0");
                                                        startActivity(in);
                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        in.putExtra("continuee", "0");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((payperviewmoviedata.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (payperviewmoviedata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                        in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        in.putExtra("continuee", "0");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                        in.putExtra("url", payperviewmoviedata.get(position).getVideo_url() + payperviewmoviedata.get(position).getPath() + ".m3u8");
                                                        in.putExtra("xtra", "subscriberented");
                                                        in.putExtra("continuee", "0");
                                                        startActivity(in);

                                                    }
                                                } else if (payperviewmoviedata.get(position).getAccess().equalsIgnoreCase("ppv") || payperviewmoviedata.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                    in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    in.putExtra("continuee", "0");
                                                    startActivity(in);
                                                } else {

                                                    if (payperviewmoviedata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                        in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        in.putExtra("continuee", "0");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                        in.putExtra("url", payperviewmoviedata.get(position).getVideo_url() + payperviewmoviedata.get(position).getPath() + ".m3u8");
                                                        in.putExtra("xtra", "Norent");
                                                        in.putExtra("continuee", "0");
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

                                } else if (payperviewmoviedata.get(position).getType().equalsIgnoreCase("embed")) {

                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, payperviewmoviedata.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                                            if (user_id == null) {

                                                Intent in = new Intent(getApplicationContext(), TrailerPlayerActivity.class);
                                                in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                startActivity(in);

                                            }

                                            if (user_id != null && user_role.equalsIgnoreCase("registered")) {

                                                if (response.body().getPpv_video_status().equalsIgnoreCase("pay_now")) {
//                                                    Intent in = new Intent(getApplicationContext(), YoutubeVideoHomepageActivity.class);
//                                                    in.putExtra("id", payperviewmoviedata.get(position).getId());
//                                                    startActivity(in);

                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

//                                                    Intent in = new Intent(getApplicationContext(), YoutubeVideoHomepageActivity.class);
//                                                    in.putExtra("id", payperviewmoviedata.get(position).getId());
//                                                    startActivity(in);

                                                } else {


//                                                    Intent in = new Intent(getApplicationContext(), YoutubeVideoHomepageActivity.class);
//                                                    in.putExtra("id", payperviewmoviedata.get(position).getId());
//                                                    startActivity(in);

                                                }

                                            } else {
//
//                                                Intent in = new Intent(getApplicationContext(), YoutubeVideoHomepageActivity.class);
//                                                in.putExtra("id", payperviewmoviedata.get(position).getId());
//                                                startActivity(in);


                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                                            Log.d("Error", t.getMessage());
                                        }
                                    });

                                } else if (payperviewmoviedata.get(position).getType().equalsIgnoreCase("mp4_url")) {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, payperviewmoviedata.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                                            if (user_id == null) {


                                                Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                startActivity(in);

                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (payperviewmoviedata.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                    in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    in.putExtra("continuee", "0");
                                                    startActivity(in);

                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                    in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    in.putExtra("continuee", "0");
                                                    startActivity(in);

                                                } else if ((payperviewmoviedata.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                    in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    in.putExtra("continuee", "0");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = payperviewmoviedata.get(position).getMp4_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                        in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        in.putExtra("continuee", "0");
                                                        startActivity(in);
                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        in.putExtra("continuee", "0");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((payperviewmoviedata.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (payperviewmoviedata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                        in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        in.putExtra("continuee", "0");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                        in.putExtra("url", payperviewmoviedata.get(position).getMp4_url());
                                                        in.putExtra("xtra", "subscriberented");
                                                        in.putExtra("continuee", "0");
                                                        startActivity(in);

                                                    }
                                                } else if (payperviewmoviedata.get(position).getAccess().equalsIgnoreCase("ppv") || payperviewmoviedata.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                    in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    in.putExtra("continuee", "0");
                                                    startActivity(in);
                                                } else {

                                                    if (payperviewmoviedata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                        in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        in.putExtra("continuee", "0");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                        in.putExtra("url", payperviewmoviedata.get(position).getMp4_url());
                                                        in.putExtra("xtra", "Norent");
                                                        in.putExtra("continuee", "0");
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
                                } else if (payperviewmoviedata.get(position).getType().equalsIgnoreCase("m3u8_url")) {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, payperviewmoviedata.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                                            if (user_id == null) {


                                                Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                startActivity(in);

                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (payperviewmoviedata.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                    in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    in.putExtra("continuee", "0");
                                                    startActivity(in);

                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                    in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    in.putExtra("continuee", "0");
                                                    startActivity(in);

                                                } else if ((payperviewmoviedata.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                    in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    in.putExtra("continuee", "0");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = payperviewmoviedata.get(position).getM3u8_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                        in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        in.putExtra("continuee", "0");
                                                        startActivity(in);
                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        in.putExtra("continuee", "0");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((payperviewmoviedata.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (payperviewmoviedata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                        in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        in.putExtra("continuee", "0");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                        in.putExtra("url", payperviewmoviedata.get(position).getM3u8_url());
                                                        in.putExtra("xtra", "subscriberented");
                                                        in.putExtra("continuee", "0");
                                                        startActivity(in);

                                                    }
                                                } else if (payperviewmoviedata.get(position).getAccess().equalsIgnoreCase("ppv") || payperviewmoviedata.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                    in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    in.putExtra("continuee", "0");
                                                    startActivity(in);
                                                } else {

                                                    if (payperviewmoviedata.get(position).getM3u8_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                        in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        in.putExtra("continuee", "0");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                        in.putExtra("url", payperviewmoviedata.get(position).getM3u8_url());
                                                        in.putExtra("xtra", "Norent");
                                                        in.putExtra("continuee", "0");
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
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, payperviewmoviedata.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                                            if (user_id == null) {


                                                Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                startActivity(in);

                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (payperviewmoviedata.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                    in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    in.putExtra("continuee", "0");
                                                    startActivity(in);

                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                    in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    in.putExtra("continuee", "0");
                                                    startActivity(in);

                                                } else if ((payperviewmoviedata.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                    in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    in.putExtra("continuee", "0");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = payperviewmoviedata.get(position).getVideo_url() + payperviewmoviedata.get(position).getPath() + ".m3u8";

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                        in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        in.putExtra("continuee", "0");
                                                        startActivity(in);
                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        in.putExtra("continuee", "0");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((payperviewmoviedata.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (payperviewmoviedata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                        in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        in.putExtra("continuee", "0");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                        in.putExtra("url", payperviewmoviedata.get(position).getVideo_url() + payperviewmoviedata.get(position).getPath() + ".m3u8");
                                                        in.putExtra("xtra", "subscriberented");
                                                        in.putExtra("continuee", "0");
                                                        startActivity(in);

                                                    }
                                                } else if (payperviewmoviedata.get(position).getAccess().equalsIgnoreCase("ppv") || payperviewmoviedata.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                    in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    in.putExtra("continuee", "0");
                                                    startActivity(in);
                                                } else {

                                                    if (payperviewmoviedata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                        in.putExtra("url", payperviewmoviedata.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        in.putExtra("continuee", "0");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", payperviewmoviedata.get(position).getId());
                                                        in.putExtra("url", payperviewmoviedata.get(position).getVideo_url() + payperviewmoviedata.get(position).getPath() + ".m3u8");
                                                        in.putExtra("xtra", "Norent");
                                                        in.putExtra("continuee", "0");
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
                        //                    }


                    }
                })
        );


    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
