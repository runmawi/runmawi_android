package com.atbuys.runmawi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.atbuys.runmawi.Adapter.EpisodehomeAdopter;
import com.atbuys.runmawi.Adapter.SeriesImage;
import com.atbuys.runmawi.Adapter.series_imgae;
import com.atbuys.runmawi.Api1.RetrofitSingleton;
import com.atbuys.runmawi.Model.EpisodeHomeData;
import com.atbuys.runmawi.Model.HomeBodyResponse;
import com.atbuys.runmawi.Model.episodes;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;

public class SeasonEpisodeCopyActivity extends AppCompatActivity {

    ProgressBar allChannelProgress;
    private ArrayList<EpisodeHomeData> dataList;
    private ArrayList<episodes> episodes;
    ArrayList<series> series;


    private RecyclerView allChannelRecycler;
    public SeriesImage imagesereies;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    ImageView imageView,back;
    String x, y, z,value,title;
    TextView desc,view_more,videotitle;
    CardView rent_now,pay_now;
    String user_role,user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_season_episode_copy);

        imageView = (ImageView) findViewById(R.id.videoimage);
        back=(ImageView)findViewById(R.id.back);
        videotitle=(TextView)findViewById(R.id.videotitle);
        rent_now=(CardView)findViewById(R.id.rent_now);
        pay_now=(CardView)findViewById(R.id.paynow);
        series=new ArrayList<>();

        Intent in = getIntent();
        x = in.getStringExtra("id");
        y = in.getStringExtra("image");
        z = in.getStringExtra("desc");
        title = in.getStringExtra("title");

        videotitle.setText(title);
        desc = (TextView) findViewById(R.id.decsription);
        view_more = (TextView) findViewById(R.id.view_more);
        String next = "<font color='#ff0000'>  View Less</font>";

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final Handler handler223 = new Handler();
        final Runnable Update223 = new Runnable() {
            public void run() {

                if (z=="" ||  z == null) {
                }else {

                    if (desc.getMaxLines() == 2) {
                        desc.setText(Html.fromHtml(z));

                        if (z.length() <= 160) {
                            view_more.setVisibility(View.GONE);
                        } else {
                            view_more.setVisibility(View.VISIBLE);
                        }
                    } else {
                        desc.setText(Html.fromHtml(z + next));
                        view_more.setVisibility(View.INVISIBLE);
                        desc.setMaxLines(Integer.MAX_VALUE);
                    }
                }
            }
        };
        Timer swipeTime223 = new Timer();
        swipeTime223.schedule(new TimerTask() {
            @Override
            public void run() {
                handler223.post(Update223);
            }
        }, 1000, 1000);

        if (z == null || z=="") {
            view_more.setVisibility(View.GONE);
            desc.setVisibility(View.GONE);
        }

        view_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_more.setVisibility(View.INVISIBLE);
                desc.setMaxLines(Integer.MAX_VALUE);
            }
        });
        desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (z.length() <= 160) {
                    view_more.setVisibility(View.GONE);
                } else {
                    view_more.setVisibility(View.VISIBLE);
                }
                desc.setMaxLines(2);
            }
        });

        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);





     /*   Call<JSONResponse> channela = ApiClient.getInstance1().getApi().getSeriesDetail(x);
        channela.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();

                if (jsonResponse.getSeries().length!=0||jsonResponse.getSeries()!=null){
                    series=new ArrayList<>(Arrays.asList(jsonResponse.getSeries()));

                    if (user_role.equalsIgnoreCase("admin")) {
                        pay_now.setVisibility(View.GONE);
                        rent_now.setVisibility(View.GONE);
                    }else if (series.get(0).getAccess().equalsIgnoreCase("subscriber")) {
                        if (user_role.equalsIgnoreCase("subscriber")) {
                            pay_now.setVisibility(View.GONE);
                            rent_now.setVisibility(View.GONE);
                        } else {
                            pay_now.setVisibility(View.VISIBLE);
                            rent_now.setVisibility(View.GONE);
                        }
                    }else if (series.get(0).getAccess().equalsIgnoreCase("ppv") && jsonResponse.getPpv_video_status().equalsIgnoreCase("can_view")) {

                        pay_now.setVisibility(View.GONE);
                        rent_now.setVisibility(View.GONE);
                    } else if (series.get(0).getAccess().equalsIgnoreCase("ppv")) {
                        pay_now.setVisibility(View.GONE);
                        rent_now.setVisibility(View.VISIBLE);
                    }
                }

            }
            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error: "+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });*/

        //desc.setText(z);

        dataList = new ArrayList<EpisodeHomeData>();

        allChannelRecycler = (RecyclerView) findViewById(R.id.channelistrecycler);
        allChannelProgress = (ProgressBar) findViewById(R.id.catoryprogress);

        adapter = new EpisodehomeAdopter(dataList, this);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        allChannelRecycler.setHasFixedSize(true);
        allChannelRecycler.setLayoutManager(layoutManager);
        allChannelRecycler.setAdapter(adapter);

        Call<HomeBodyResponse> responseCall = RetrofitSingleton.getInstance().getApi().getSeasonEpisodes(x);
        responseCall.enqueue(new retrofit2.Callback<HomeBodyResponse>() {
            @Override
            public void onResponse(Call<HomeBodyResponse> call, retrofit2.Response<HomeBodyResponse> response) {

                allChannelProgress.setVisibility(View.GONE);
                allChannelRecycler.setVisibility(View.VISIBLE);

                if (response.isSuccessful()) {
                    for (EpisodeHomeData data : response.body().getSeasonsEpisodes()) {
                        dataList.add(data);
                        data.getEpisodes();
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<HomeBodyResponse> call, Throwable t) {

                //  progressBar.setVisibility(View.GONE);

            }
        });

        Call<series_imgae> channel = ApiClient.getInstance1().getApi().getseason_image(x);
        channel.enqueue(new retrofit2.Callback<series_imgae>() {
            @Override
            public void onResponse(Call<series_imgae> call, retrofit2.Response<series_imgae> response) {

                if (response.code() == 200) {
                    assert response.body() != null;
                    series_imgae item = response.body();
                    imagesereies = item.getSeries_image();
                    value =imagesereies.getBanner_image_url();
                    Log.w("ert",value);
                    Picasso.get().load(value).into(imageView);

                }
            }

            @Override
            public void onFailure(Call<series_imgae> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });


    }
}