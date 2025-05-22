package com.atbuys.runmawi;

import static com.atbuys.runmawi.UserHomeFragment.mediaplayer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;


public class ContentpartnerlistActivity extends AppCompatActivity {

    private ArrayList<latest_video> featured_videoslist;
    private ArrayList<latest_series> series_list;
    private ArrayList<audios> audioslist;
    private ArrayList<livetream> livestreamsList;
    private ArrayList<videodetail> movie_detaildata;
    private ArrayList<audiodetail> audio_detaildata;
    LinearLayout feature_layout,series_layout,audios_layout,livestream_layout;
    private  ImageView base,center;

    RecyclerView featurevideoRecycler,seriesRecycler,audioRecycler,livestreamRecycler;
    ProgressBar featurevideoprogress,seriesProgress,audioProgress,livestreamProgress;
    latest_channal_videosAdapter latest_Channal_videosAdapter;
    latest_seriesAdapter latest_SeriesAdapter;
    AudiosAdapter audiosAdapter;
    LivestreamAdapter livestreamAdapter;
    private RecyclerView.LayoutManager featuredManager,seriesManager,audioManager,liveStreamManager;
    LinearLayout no_feature,no_series,no_audios,no_livestream,sharelayuot;
    String user_id, user_role,slug,namee,media_url;
    public static String audio_id;
    TextView channal_name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(  AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {


            setTheme(R.style.darktheme);

        }

        else {


            setTheme(R.style.AppTheme);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channeli_partner_list);

        Intent in = getIntent();

        slug= in.getStringExtra("slug");
        namee = in.getStringExtra("name");
        channal_name = (TextView)findViewById(R.id.channel_name) ;

        channal_name.setText(namee);



        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);

        base = (ImageView) findViewById(R.id.base);
        center = (ImageView) findViewById(R.id.center);

        Picasso.get().load(in.getStringExtra("image")).into(base);
        Picasso.get().load(in.getStringExtra("channal_image")).into(center);



        featured_videoslist = new ArrayList<latest_video>();
        series_list =  new ArrayList<latest_series>();
        audioslist = new ArrayList<audios>();
        livestreamsList = new ArrayList<livetream>();
        movie_detaildata = new ArrayList<videodetail>();

        sharelayuot =(LinearLayout) findViewById(R.id.sharelayuot);

        feature_layout = (LinearLayout) findViewById(R.id.featredvideos);
        featurevideoRecycler = (RecyclerView) findViewById(R.id.featurevideosRecycler);


        series_layout = (LinearLayout) findViewById(R.id.series);
        seriesRecycler = (RecyclerView) findViewById(R.id.seriesRecycler);


        audios_layout = (LinearLayout) findViewById(R.id.audios);
        audioRecycler = (RecyclerView) findViewById(R.id.audiosRecycler);


        livestream_layout = (LinearLayout) findViewById(R.id.livestream);
        livestreamRecycler = (RecyclerView) findViewById(R.id.livestreamRecycler);


        latest_Channal_videosAdapter = new latest_channal_videosAdapter(featured_videoslist,getApplicationContext());
        featuredManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        latest_SeriesAdapter = new latest_seriesAdapter(series_list,getApplicationContext());
        seriesManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        audiosAdapter = new AudiosAdapter(audioslist,getApplicationContext());
        audioManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        livestreamAdapter = new LivestreamAdapter(livestreamsList,getApplicationContext());
        liveStreamManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);


        featurevideoRecycler.setHasFixedSize(true);
        featurevideoRecycler.setLayoutManager(featuredManager);
        featurevideoRecycler.setAdapter(latest_Channal_videosAdapter);


        seriesRecycler.setHasFixedSize(true);
        seriesRecycler.setLayoutManager(seriesManager);
        seriesRecycler.setAdapter(latest_SeriesAdapter);


        audioRecycler.setHasFixedSize(true);
        audioRecycler.setLayoutManager(audioManager);
        audioRecycler.setAdapter(audiosAdapter);


        livestreamRecycler.setHasFixedSize(true);
        livestreamRecycler.setLayoutManager(liveStreamManager);
        livestreamRecycler.setAdapter(livestreamAdapter);


        Call<JSONResponse> callfetaure = ApiClient.getInstance1().getApi().getContentllist(slug);
        callfetaure.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();

                //media_url = jsonResponse.getMedia_url();

                if (jsonResponse.getLatest_videos().length == 0) {

                    feature_layout.setVisibility(View.GONE);
                }

                else {
                    feature_layout.setVisibility(View.VISIBLE);
                    featured_videoslist = new ArrayList<>(Arrays.asList(jsonResponse.getLatest_videos()));
                    latest_Channal_videosAdapter = new latest_channal_videosAdapter(featured_videoslist);
                    featurevideoRecycler.setAdapter(latest_Channal_videosAdapter);


                }


                if (jsonResponse.getLatest_series().length == 0) {

                    series_layout.setVisibility(View.GONE);
                }

                else {
                    series_layout.setVisibility(View.VISIBLE);
                    series_list = new ArrayList<>(Arrays.asList(jsonResponse.getLatest_series()));
                    latest_SeriesAdapter = new latest_seriesAdapter(series_list);
                    seriesRecycler.setAdapter(latest_SeriesAdapter);

                }

                if (jsonResponse.getAudios().length == 0) {

                    audios_layout.setVisibility(View.GONE);
                }

                else {
                    audios_layout.setVisibility(View.VISIBLE);
                    audioslist = new ArrayList<>(Arrays.asList(jsonResponse.getAudios()));
                    audiosAdapter = new AudiosAdapter(audioslist);
                    audioRecycler.setAdapter(audiosAdapter);
                }

                if (jsonResponse.getLivestream().length == 0) {

                    livestream_layout.setVisibility(View.GONE);
                }

                else {
                    livestream_layout.setVisibility(View.VISIBLE);
                    livestreamsList = new ArrayList<>(Arrays.asList(jsonResponse.getLivestream()));
                    livestreamAdapter = new LivestreamAdapter(livestreamsList);
                    livestreamRecycler.setAdapter(livestreamAdapter);

                }




            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
                Toast.makeText(getApplicationContext(),"hgh"+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



        sharelayuot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "I am watching videos";
                String shareSub =media_url ;
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareSub);
                startActivity(Intent.createChooser(myIntent, "Share using"));



            }
        });

        featurevideoRecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (featured_videoslist.size() > position) {
                            if (featured_videoslist.get(position) != null) {

                                if(featured_videoslist.get(position).getType().equalsIgnoreCase(""))
                                {
                                    {
                                        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, featured_videoslist.get(position).getId());
                                        res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                            @Override
                                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                JSONResponse jsonResponse = response.body();
                                                movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                                                if (user_id == null && user_role == null) {

                                                    if(featured_videoslist.get(position).getAccess().equalsIgnoreCase("guest"))
                                                    {
                                                        String videourl = featured_videoslist.get(position).getVideo_url() + featured_videoslist.get(position).getPath() + ".m3u8";

                                                        if (videourl == null) {
                                                            Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                            in.putExtra("id", featured_videoslist.get(position).getId());
                                                            in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                            in.putExtra("xtra", "guest");
                                                            //     in.putExtra("continuee","0");
                                                            startActivity(in);

                                                        } else {

                                                            Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                            in.putExtra("id", featured_videoslist.get(position).getId());
                                                            in.putExtra("url", videourl);
                                                            in.putExtra("xtra", "guest");
                                                            //in.putExtra("continuee","0");
                                                            startActivity(in);
                                                        }
                                                    }

                                                } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                    if (featured_videoslist.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                        in.putExtra("xtra", "norent");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    }


                                                    else if(featured_videoslist.get(position).getAccess().equalsIgnoreCase("guest"))
                                                    {
                                                        String videourl = featured_videoslist.get(position).getVideo_url() + featured_videoslist.get(position).getPath() + ".m3u8";

                                                        if (videourl == null) {
                                                            Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                            in.putExtra("id", featured_videoslist.get(position).getId());
                                                            in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                            in.putExtra("xtra", "guest");
                                                            //     in.putExtra("continuee","0");
                                                            startActivity(in);

                                                        } else {

                                                            Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                            in.putExtra("id", featured_videoslist.get(position).getId());
                                                            in.putExtra("url", videourl);
                                                            in.putExtra("xtra", "guest");
                                                            //in.putExtra("continuee","0");
                                                            startActivity(in);
                                                        }
                                                    }
                                                    else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                        in.putExtra("xtra", "norent");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    } else if ((featured_videoslist.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriber_content");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    } else {

                                                        String videourl = featured_videoslist.get(position).getVideo_url()+featured_videoslist.get(position).getPath()+".m3u8";

                                                        if (videourl == null) {
                                                            Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                            in.putExtra("id", featured_videoslist.get(position).getId());
                                                            in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                            in.putExtra("xtra", "rentted");
                                                            in.putExtra("continuee","0");
                                                            startActivity(in);
                                                        } else {

                                                            Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                            in.putExtra("id", featured_videoslist.get(position).getId());
                                                            in.putExtra("url", videourl);
                                                            in.putExtra("xtra", "rentted");
                                                            in.putExtra("continuee","0");
                                                            startActivity(in);
                                                        }
                                                    }

                                                } else {

                                                    if ((featured_videoslist.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                        if (featured_videoslist.get(position).getMp4_url() == null) {
                                                            Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                            in.putExtra("id", featured_videoslist.get(position).getId());
                                                            in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                            in.putExtra("xtra", "subscriberented");
                                                            in.putExtra("continuee","0");
                                                            startActivity(in);

                                                        } else {
                                                            Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                            in.putExtra("id", featured_videoslist.get(position).getId());
                                                            in.putExtra("url", featured_videoslist.get(position).getVideo_url()+featured_videoslist.get(position).getPath()+".m3u8");
                                                            in.putExtra("xtra", "subscriberented");
                                                            in.putExtra("continuee","0");
                                                            startActivity(in);

                                                        }
                                                    } else if (featured_videoslist.get(position).getAccess().equalsIgnoreCase("ppv") || featured_videoslist.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberrent");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    } else {

                                                        if (featured_videoslist.get(position).getMp4_url() == null) {
                                                            Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                            in.putExtra("id", featured_videoslist.get(position).getId());
                                                            in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                            in.putExtra("xtra", "Norent");
                                                            in.putExtra("continuee","0");
                                                            startActivity(in);
                                                        } else {
                                                            Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                            in.putExtra("id", featured_videoslist.get(position).getId());
                                                            in.putExtra("url", featured_videoslist.get(position).getVideo_url()+featured_videoslist.get(position).getPath()+".m3u8");
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
                                }


                                else if(featured_videoslist.get(position).getType().equalsIgnoreCase("aws_m3u8"))
                                {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, featured_videoslist.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                                            if (user_id == null) {

                                                if(featured_videoslist.get(position).getAccess().equalsIgnoreCase("guest"))
                                                {
                                                    String videourl = featured_videoslist.get(position).getM3u8_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                        in.putExtra("xtra", "guest");
                                                        //     in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "guest");
                                                        //in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    }
                                                }
                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {

                                                if (featured_videoslist.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", featured_videoslist.get(position).getId());
                                                    in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    //      in.putExtra("continuee","0");
                                                    startActivity(in);

                                                }

                                                else if(featured_videoslist.get(position).getAccess().equalsIgnoreCase("guest"))
                                                {
                                                    String videourl = featured_videoslist.get(position).getM3u8_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                        in.putExtra("xtra", "guest");
                                                        //     in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "guest");
                                                        //in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    }
                                                }
                                                else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", featured_videoslist.get(position).getId());
                                                    in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    //     in.putExtra("continuee","0");
                                                    startActivity(in);

                                                } else if ((featured_videoslist.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", featured_videoslist.get(position).getId());
                                                    in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    //   in.putExtra("continuee","0");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = featured_videoslist.get(position).getM3u8_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        //     in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        //in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((featured_videoslist.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (featured_videoslist.get(position).getM3u8_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        //     in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getM3u8_url());
                                                        in.putExtra("xtra", "subscriberented");
                                                        //   in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    }
                                                } else if (featured_videoslist.get(position).getAccess().equalsIgnoreCase("ppv") || featured_videoslist.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", featured_videoslist.get(position).getId());
                                                    in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    //    in.putExtra("continuee","0");
                                                    startActivity(in);
                                                } else {

                                                    if (featured_videoslist.get(position).getM3u8_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        //   in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getM3u8_url());
                                                        in.putExtra("xtra", "Norent");
                                                        //     in.putExtra("continuee","0");
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


                                else if (featured_videoslist.get(position).getType().equalsIgnoreCase("embed")) {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, featured_videoslist.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));

                                            if (user_id == null) {
                                                Intent in = new Intent(getApplicationContext(), TrailerPlayerActivity.class);
                                                in.putExtra("id", featured_videoslist.get(position).getId());
                                                in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                startActivity(in);
                                            }

                                            if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (response.body().getPpv_video_status().equalsIgnoreCase("pay_now")) {
//                                                    Intent in = new Intent(getApplicationContext(), YoutubeVideoHomepageActivity.class);
//                                                    in.putExtra("id", featured_videoslist.get(position).getId());
//                                                    startActivity(in);

                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

//                                                    Intent in = new Intent(getApplicationContext(), YoutubeVideoHomepageActivity.class);
//                                                    in.putExtra("id", featured_videoslist.get(position).getId());
//                                                    startActivity(in);

                                                } else {

//                                                    Intent in = new Intent(getApplicationContext(), YoutubeVideoHomepageActivity.class);
//                                                    in.putExtra("id", featured_videoslist.get(position).getId());
//                                                    startActivity(in);

                                                }

                                            } else {

//                                                Intent in = new Intent(getApplicationContext(), YoutubeVideoHomepageActivity.class);
//                                                in.putExtra("id", featured_videoslist.get(position).getId());
//                                                startActivity(in);


                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                                            Log.d("Error", t.getMessage());
                                        }
                                    });

                                }

                                else if (featured_videoslist.get(position).getType().equalsIgnoreCase("mp4_url")) {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, featured_videoslist.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                                            if (user_id == null) {


                                                if(featured_videoslist.get(position).getAccess().equalsIgnoreCase("guest"))
                                                {
                                                    String videourl = featured_videoslist.get(position).getMp4_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                        in.putExtra("xtra", "guest");
                                                        //     in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "guest");
                                                        //in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    }
                                                }


                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (featured_videoslist.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", featured_videoslist.get(position).getId());
                                                    in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    in.putExtra("continuee","0");
                                                    startActivity(in);

                                                }

                                                else if(featured_videoslist.get(position).getAccess().equalsIgnoreCase("guest"))
                                                {
                                                    String videourl = featured_videoslist.get(position).getMp4_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                        in.putExtra("xtra", "guest");
                                                        //     in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "guest");
                                                        //in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    }
                                                }
                                                else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", featured_videoslist.get(position).getId());
                                                    in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    in.putExtra("continuee","0");
                                                    startActivity(in);

                                                } else if ((featured_videoslist.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", featured_videoslist.get(position).getId());
                                                    in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    in.putExtra("continuee","0");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = featured_videoslist.get(position).getMp4_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((featured_videoslist.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (featured_videoslist.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getMp4_url());
                                                        in.putExtra("xtra", "subscriberented");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    }
                                                } else if (featured_videoslist.get(position).getAccess().equalsIgnoreCase("ppv") || featured_videoslist.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", featured_videoslist.get(position).getId());
                                                    in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    in.putExtra("continuee","0");
                                                    startActivity(in);
                                                } else {

                                                    if (featured_videoslist.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getMp4_url());
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


                                else if (featured_videoslist.get(position).getType().equalsIgnoreCase("m3u8_url")) {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, featured_videoslist.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                                            if (user_id == null) {


                                                if(featured_videoslist.get(position).getAccess().equalsIgnoreCase("guest"))
                                                {
                                                    String videourl = featured_videoslist.get(position).getM3u8_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                        in.putExtra("xtra", "guest");
                                                        //     in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "guest");
                                                        //in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    }
                                                }


                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (featured_videoslist.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", featured_videoslist.get(position).getId());
                                                    in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    in.putExtra("continuee","0");
                                                    startActivity(in);

                                                }

                                                else if(featured_videoslist.get(position).getAccess().equalsIgnoreCase("guest"))
                                                {
                                                    String videourl = featured_videoslist.get(position).getM3u8_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                        in.putExtra("xtra", "guest");
                                                        //     in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "guest");
                                                        //in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    }
                                                }
                                                else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", featured_videoslist.get(position).getId());
                                                    in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    in.putExtra("continuee","0");
                                                    startActivity(in);

                                                } else if ((featured_videoslist.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", featured_videoslist.get(position).getId());
                                                    in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    in.putExtra("continuee","0");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = featured_videoslist.get(position).getM3u8_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((featured_videoslist.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (featured_videoslist.get(position).getM3u8_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getM3u8_url());
                                                        in.putExtra("xtra", "subscriberented");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    }
                                                } else if (featured_videoslist.get(position).getAccess().equalsIgnoreCase("ppv") || featured_videoslist.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", featured_videoslist.get(position).getId());
                                                    in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    in.putExtra("continuee","0");
                                                    startActivity(in);
                                                } else {

                                                    if (featured_videoslist.get(position).getM3u8_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getM3u8_url());
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
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, featured_videoslist.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));

                                            if (user_id == null) {

                                                if(featured_videoslist.get(position).getAccess().equalsIgnoreCase("guest"))
                                                {
                                                    String videourl = featured_videoslist.get(position).getM3u8_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                        in.putExtra("xtra", "guest");
                                                        //     in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "guest");
                                                        //in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    }
                                                }


                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (featured_videoslist.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", featured_videoslist.get(position).getId());
                                                    in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    in.putExtra("continuee","0");
                                                    startActivity(in);

                                                }

                                                else if(featured_videoslist.get(position).getAccess().equalsIgnoreCase("guest"))
                                                {
                                                    String videourl = featured_videoslist.get(position).getMp4_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                        in.putExtra("xtra", "guest");
                                                        //     in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "guest");
                                                        //in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    }
                                                }

                                                else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", featured_videoslist.get(position).getId());
                                                    in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    in.putExtra("continuee","0");
                                                    startActivity(in);

                                                } else if ((featured_videoslist.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", featured_videoslist.get(position).getId());
                                                    in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    in.putExtra("continuee","0");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = featured_videoslist.get(position).getVideo_url()+featured_videoslist.get(position).getPath()+".m3u8";

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((featured_videoslist.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (featured_videoslist.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getVideo_url()+featured_videoslist.get(position).getPath()+".m3u8");
                                                        in.putExtra("xtra", "subscriberented");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    }
                                                } else if (featured_videoslist.get(position).getAccess().equalsIgnoreCase("ppv") || featured_videoslist.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", featured_videoslist.get(position).getId());
                                                    in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    in.putExtra("continuee","0");
                                                    startActivity(in);
                                                } else {

                                                    if (featured_videoslist.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", featured_videoslist.get(position).getId());
                                                        in.putExtra("url", featured_videoslist.get(position).getVideo_url()+featured_videoslist.get(position).getPath()+".m3u8");
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
                            }
                        }
                    }

                })
        );


        seriesRecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (series_list.size() > position) {
                            if (series_list.get(position) != null) {

                                //   Toast.makeText(getActivity(),""+serieslist.get(position).getId(),Toast.LENGTH_LONG).show();
                                Intent in = new Intent(getApplicationContext(), SeasonEpisodeCopyActivity.class);
                                in.putExtra("id", series_list.get(position).getId());
                                in.putExtra("image", series_list.get(position).getImage_url());
                                in.putExtra("desc", series_list.get(position).getDescription());
                                startActivity(in);
                            }
                        }
                    }
                }));



        audioRecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (audioslist.size() > position) {
                            if (audioslist.get(position) != null)
                            {
                                if(user_role == null)
                                {

                                    AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                                    alert.setTitle("Your Not Subscribed user Kindly Subscribe and access the contents");

                                    alert.setPositiveButton("Subscribe", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent in = new Intent(getApplicationContext(), SubscribeActivity.class);
                                            startActivity(in);
                                        }
                                    });
                                    alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });
                                    alert.show();
                                }


                                else if (user_role.equalsIgnoreCase("subscriber") || user_role.equalsIgnoreCase("admin") ) {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, audioslist.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            audio_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));

                                            SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                            editor.putBoolean(sharedpreferences.login, true);
                                            editor.putString(sharedpreferences.audioid, audio_detaildata.get(0).getId());
                                            editor.apply();
                                            editor.commit();

                                            audio_id = prefs.getString(sharedpreferences.audioid, null);
                                        }

                                        @Override
                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                                            Log.d("Error", t.getMessage());
                                        }
                                    });

                                    Intent in = new Intent(getApplicationContext(), MediaPlayerPageActivity.class);
                                    in.putExtra("id", audioslist.get(position).getId());
                                    startActivity(in);
                                    mediaplayer.reset();
                                    mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                                    try {
                                        mediaplayer.setDataSource(audioslist.get(position).getMp3_url());
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

                                else if(user_role.equalsIgnoreCase("registered") && audioslist.get(position).getAccess().equalsIgnoreCase("guest") || user_role.equalsIgnoreCase("registered") && audioslist.get(position).getAccess().equalsIgnoreCase("registered"))
                                {

                                    Toast.makeText(getApplicationContext(),""+audioslist.get(position).getId(), Toast.LENGTH_LONG).show();
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, audioslist.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();

                                            audio_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));

                                            SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                            editor.putBoolean(sharedpreferences.login, true);
                                            editor.putString(sharedpreferences.audioid, audio_detaildata.get(0).getId());
                                            editor.apply();
                                            editor.commit();

                                            audio_id = prefs.getString(sharedpreferences.audioid, null);
                                        }

                                        @Override
                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                                            Log.d("Error", t.getMessage());
                                        }
                                    });

                                    Intent in = new Intent(getApplicationContext(), MediaPlayerPageActivity.class);
                                    in.putExtra("id", audioslist.get(position).getId());
                                    startActivity(in);
                                    mediaplayer.reset();
                                    mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                                    try {
                                        mediaplayer.setDataSource(audioslist.get(position).getMp3_url());
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
                                else {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                                    alert.setTitle("Your Not Subscribed user Kindly Subscribe and access the contents");

                                    alert.setPositiveButton("Subscribe", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent in = new Intent(getApplicationContext(), SubscribeActivity.class);
                                            startActivity(in);
                                        }
                                    });
                                    alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });
                                    alert.show();

                                }
                            }


                        }

                    }
                }));




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
