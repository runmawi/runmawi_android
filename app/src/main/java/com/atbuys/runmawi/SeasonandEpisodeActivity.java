package com.atbuys.runmawi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;

public class SeasonandEpisodeActivity extends AppCompatActivity {

    RecyclerView seasonrecycler,episoderecycler, morerecycler;
    TextView no_series;
    SeasonAdapter seasonAdapter;
    EpisodesAdapter episodeAdapter;
    Series_list_Adapter series_list_adapter;
    String x,y,season_id,episode_id;
    TextView watch;
    private ArrayList<season> seasonlisit;
    private ArrayList<episodes> episodelist;
    private ArrayList<Series_list> seriesList;
    private RecyclerView.LayoutManager seriesManeger,episodeManeger,moreManeger;
    ImageView imageView;
    String firstseason;
    String theme,user_id,user_role;
    TextView seriesname;
    private ArrayList<episodes> movie_detaildata;
    private ArrayList<episode> movie_detaildata1;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        if(  AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {


            setTheme(R.style.darktheme);

        }

        else {

            setTheme(R.style.AppTheme);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seasonand_episode);
        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        theme = prefs.getString(sharedpreferences.theme,null);
        user_id = prefs.getString(sharedpreferences.user_id,null );
        user_role= prefs.getString(sharedpreferences.role,null );

        movie_detaildata = new ArrayList<episodes>();
        movie_detaildata1 = new ArrayList<episode>();

        if (theme == null)

        {
            setTheme(R.style.AppTheme);

        }
        else if( theme.equalsIgnoreCase("1")) {

            setTheme(R.style.darktheme);


        }

        else {

            setTheme(R.style.AppTheme);

        }



        Intent in=getIntent();
        x=in.getStringExtra("series_id");
        y=in.getStringExtra("image");
        episode_id = in.getStringExtra("episode_id");
        season_id = in.getStringExtra("seasonid");

        seasonlisit =new ArrayList<season>();
        episodelist =new ArrayList<episodes>();
        seriesList = new ArrayList<Series_list>();

        seasonrecycler = (RecyclerView) findViewById(R.id.seasonrecycler);
        imageView=(ImageView)findViewById(R.id.videoimage);


        morerecycler = (RecyclerView)  findViewById(R.id.moreseriesrecycler);
        no_series = (TextView) findViewById(R.id.no_series);
        seriesname = (TextView) findViewById(R.id.seriesname);


        watch = (TextView) findViewById(R.id.watch);


        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getEpisodesDetail(episode_id, user_id);
        res.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                movie_detaildata1 = new ArrayList<>(Arrays.asList(jsonResponse.getEpisode()));
                seriesname.setText(movie_detaildata1.get(0).getSeries_name());

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });

                watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Call<JSONResponse> res = ApiClient.getInstance1().getApi().getEpisodesDetail(episode_id, user_id);
                res.enqueue(new retrofit2.Callback<JSONResponse>() {
                    @Override
                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                        JSONResponse jsonResponse = response.body();
                        movie_detaildata1 = new ArrayList<>(Arrays.asList(jsonResponse.getEpisode()));


                        if (user_id == null) {

                            Intent in = new Intent(getApplicationContext(), HomePageEpisodeActivity.class);
                            in.putExtra("id",movie_detaildata1.get(0).getId());
                            in.putExtra("url", movie_detaildata1.get(0).getMp4_url());
                            startActivity(in);


                        } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                            if (response.body().getPpv_video_status().equalsIgnoreCase("pay_now")) {
                                Intent in = new Intent(getApplicationContext(), HomePageEpisodeActivity.class);
                                in.putExtra("id", movie_detaildata1.get(0).getId());
                                in.putExtra("url", movie_detaildata1.get(0).getMp4_url());
                                in.putExtra("xtra", "norent");
                                //   in.putExtra("continuee","0");
                                startActivity(in);

                            } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                Intent in = new Intent(getApplicationContext(), HomePageEpisodeActivity.class);
                                in.putExtra("id", movie_detaildata1.get(0).getId());
                                in.putExtra("url", movie_detaildata1.get(0).getMp4_url());
                                in.putExtra("xtra", "norent");
                                // in.putExtra("continuee","0");
                                startActivity(in);

                            } else {

                                String videourl = movie_detaildata1.get(0).getMp4_url();

                                Intent in = new Intent(getApplicationContext(), HomePageEpisodeActivity.class);
                                in.putExtra("id", movie_detaildata1.get(0).getId());
                                in.putExtra("url", videourl);
                                in.putExtra("xtra", "rentted");
                                // in.putExtra("continuee","0");
                                startActivity(in);


                            }

                        } else {

                            String videourl = movie_detaildata1.get(0).getMp4_url();
                            Intent in = new Intent(getApplicationContext(), HomePageEpisodeActivity.class);
                            in.putExtra("id", movie_detaildata1.get(0).getId());
                            in.putExtra("url", videourl);
                            in.putExtra("xtra", "Norent");
                            //in.putExtra("continuee","0");
                            startActivity(in);

                        }

                    }

                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                        Log.d("Error", t.getMessage());
                    }
                });



            }
        });

        Picasso.get().load(y).into(imageView);


        episoderecycler = (RecyclerView) findViewById(R.id.episoderecycler);

        seasonAdapter = new SeasonAdapter(seasonlisit, this);
        seriesManeger = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);


        series_list_adapter = new Series_list_Adapter(seriesList, this);
        moreManeger = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);


        seasonrecycler.setHasFixedSize(true);
        seasonrecycler.setLayoutManager(seriesManeger);
        seasonrecycler.setAdapter(seasonAdapter);


        morerecycler.setHasFixedSize(true);
        morerecycler.setLayoutManager(moreManeger);
        morerecycler.setAdapter(series_list_adapter);




        episodeAdapter = new EpisodesAdapter(episodelist, this);
        episodeManeger = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);


        episoderecycler.setHasFixedSize(true);
        episoderecycler.setLayoutManager(episodeManeger);
        episoderecycler.setAdapter(episodeAdapter);


        Call<JSONResponse> call = ApiClient.getInstance1().getApi().getSeasonlist(x);
        call.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

             //   seriesProgress.setVisibility(View.GONE);
               // seriesRecycler.setVisibility(View.VISIBLE);
                JSONResponse jsonResponse = response.body();
             //  firstseason = jsonResponse.getFirst_season_id();

                seasonlisit = new ArrayList<>(Arrays.asList(jsonResponse.getSeason()));

                    seasonAdapter = new SeasonAdapter(seasonlisit);
                    seasonrecycler.setAdapter(seasonAdapter);


                Call<JSONResponse> calle = ApiClient.getInstance1().getApi().getRemaingEpisode(season_id,x);
                calle.enqueue(new retrofit2.Callback<JSONResponse>() {
                    @Override
                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                        //  Toast.makeText(getApplicationCorntext(),"df",Toast.LENGTH_LONG).show();
                        JSONResponse jsonResponse = response.body();

                        episodelist = new ArrayList<>(Arrays.asList(jsonResponse.getEpisodes()));
                        episodeAdapter = new EpisodesAdapter(episodelist);
                        episoderecycler.setAdapter(episodeAdapter);


                    }

                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                        Log.d("Error15444", t.getMessage());
                    }
                });

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });



        Call<JSONResponse> calls = ApiClient.getInstance1().getApi().getRelatedSeries(x);
        calls.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                //   seriesProgress.setVisibility(View.GONE);
                // seriesRecycler.setVisibility(View.VISIBLE);
                JSONResponse jsonResponse = response.body();


                if(jsonResponse.getSeries_list().length == 0)
                {

                    no_series.setVisibility(View.VISIBLE);
                    morerecycler.setVisibility(View.GONE);
                }
                else {

                    no_series.setVisibility(View.GONE);
                    morerecycler.setVisibility(View.VISIBLE);
                    seriesList = new ArrayList<>(Arrays.asList(jsonResponse.getSeries_list()));
                    series_list_adapter = new Series_list_Adapter(seriesList);
                    morerecycler.setAdapter(series_list_adapter);


                }

            }
            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });








        seasonrecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(SeasonandEpisodeActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {



                        if (seasonlisit.size() > position) {
                            if (seasonlisit.get(position) != null) {

                                Call<JSONResponse> call = ApiClient.getInstance1().getApi().getEpisodeList(seasonlisit.get(position).getId());
                                call.enqueue(new retrofit2.Callback<JSONResponse>() {
                                    @Override
                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                                        JSONResponse jsonResponse = response.body();
                                        episodelist = new ArrayList<>(Arrays.asList(jsonResponse.getEpisodes()));
                                        episodeAdapter = new EpisodesAdapter(episodelist);
                                        episoderecycler.setAdapter(episodeAdapter);


                                    }

                                    @Override
                                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                                        Log.d("Error15444", t.getMessage());
                                    }
                                });
                            }
                        }
                    }
                }));


        episoderecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(SeasonandEpisodeActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (episodelist.size() > position) {
                            if (episodelist.get(position) != null) {


                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getEpisodesDetail(episode_id, user_id);
                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                            JSONResponse jsonResponse = response.body();
                            movie_detaildata1 = new ArrayList<>(Arrays.asList(jsonResponse.getEpisode()));



                            if (user_id == null) {

                                Intent in = new Intent(getApplicationContext(), HomePageEpisodeActivity.class);
                                in.putExtra("id",movie_detaildata1.get(position).getId());
                                in.putExtra("url", movie_detaildata1.get(position).getMp4_url());
                                startActivity(in);


                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                if (response.body().getPpv_video_status().equalsIgnoreCase("pay_now")) {
                                    Intent in = new Intent(getApplicationContext(), HomePageEpisodeActivity.class);
                                    in.putExtra("id", movie_detaildata1.get(position).getId());
                                    in.putExtra("url", movie_detaildata1.get(position).getMp4_url());
                                    in.putExtra("xtra", "norent");
                                 //   in.putExtra("continuee","0");
                                    startActivity(in);

                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                    Intent in = new Intent(getApplicationContext(), HomePageEpisodeActivity.class);
                                    in.putExtra("id", movie_detaildata1.get(position).getId());
                                    in.putExtra("url", movie_detaildata1.get(position).getMp4_url());
                                    in.putExtra("xtra", "norent");
                                   // in.putExtra("continuee","0");
                                    startActivity(in);

                                } else {

                                    String videourl = movie_detaildata1.get(position).getMp4_url();

                                    Intent in = new Intent(getApplicationContext(), HomePageEpisodeActivity.class);
                                    in.putExtra("id", movie_detaildata1.get(position).getId());
                                    in.putExtra("url", videourl);
                                    in.putExtra("xtra", "rentted");
                                   // in.putExtra("continuee","0");
                                    startActivity(in);


                                }

                            } else {

                                String videourl = movie_detaildata1.get(position).getMp4_url();
                                Intent in = new Intent(getApplicationContext(), HomePageEpisodeActivity.class);
                                in.putExtra("id", movie_detaildata1.get(position).getId());
                                in.putExtra("url", videourl);
                                in.putExtra("xtra", "Norent");
                                //in.putExtra("continuee","0");
                                startActivity(in);

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
                }));

    }
}
