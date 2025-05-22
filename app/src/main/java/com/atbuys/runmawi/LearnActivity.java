package com.atbuys.runmawi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;
import com.atbuys.runmawi.Adapter.LearnPageAdapter;
import com.atbuys.runmawi.Api1.RetrofitSingleton;
import com.atbuys.runmawi.Model.HomeBodyResponse;
import com.atbuys.runmawi.Model.LearnData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

public class LearnActivity extends AppCompatActivity {


    ProgressBar allChannelProgress,bannerprogress;
    private ArrayList<LearnData> dataList;

    private RecyclerView allChannelRecycler;
    private RecyclerView.Adapter adapter;
    CirclePageIndicator indicator;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<series_banner> bannersdata;

    ImageView notification;
    private int currentPage = 0;
    private int NUM_PAGES = 0;
    CircleImageView profiledp;
    String user_id;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    private ArrayList<user_details> user_detailsdata;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(  AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {

            setTheme(R.style.darktheme);

        }

        else {


            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        dataList = new ArrayList<LearnData>();
        bannersdata = new ArrayList<series_banner>();

        allChannelRecycler = (RecyclerView) findViewById(R.id.channelistrecycler);
        allChannelProgress=(ProgressBar)findViewById(R.id.catoryprogress);
        bannerprogress = (ProgressBar) findViewById(R.id.bannerprogress);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);

        adapter = new LearnPageAdapter(dataList, this);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;

                }

                viewPager.setCurrentItem(currentPage++, true);

                if (currentPage == bannersdata.size()) {

                    currentPage = 0;

                }

            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 1000, 2500);

        allChannelRecycler.setHasFixedSize(true);
        allChannelRecycler.setLayoutManager(layoutManager);
        allChannelRecycler.setAdapter(adapter);

        notification = (ImageView) findViewById(R.id.notification);
        profiledp = (CircleImageView) findViewById(R.id.profiledp);

        SharedPreferences prefs = this.getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);


        notification.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {



            }
        });


        final Call<JSONResponse> bannerreq = ApiClient.getInstance1().getApi().getBanners();
        bannerreq.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();


                bannersdata = new ArrayList<>(Arrays.asList(jsonResponse.getSeries_banner()));

                viewPagerAdapter = new ViewPagerAdapter(bannersdata, getApplicationContext());
                viewPager.setAdapter(viewPagerAdapter);
                bannerprogress.setVisibility(View.GONE);

                indicator.setViewPager(viewPager);
                indicator.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });



        profiledp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Fragment newCase = new UserprofilFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newCase); // give your fragment container id in first parameter
                transaction.commit();*/
                Intent in = new Intent(getApplicationContext(), MyAccountActivity.class);
                startActivity(in);
            }
        });

        Call<JSONResponse> profileres = ApiClient.getInstance1().getApi().getUserprofile(user_id);
        profileres.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        JSONResponse jsonResponse = response.body();
                        user_detailsdata = new ArrayList<>(Arrays.asList(jsonResponse.getUser_details()));

                        for (int k = 0; k < user_detailsdata.size(); k++) {


                            String userprofile = user_detailsdata.get(k).getProfile_url();
                            Picasso.get().load(userprofile).into(profiledp);


                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

                //  Toast.makeText(getContext(), "check your internet connection", Toast.LENGTH_LONG).show();
            }
        });




        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });


        Call<HomeBodyResponse> responseCall = RetrofitSingleton.getInstance().getApi().getLearn();
        responseCall.enqueue(new retrofit2.Callback<HomeBodyResponse>() {
            @Override
            public void onResponse(Call<HomeBodyResponse> call, retrofit2.Response<HomeBodyResponse> response) {

                allChannelProgress.setVisibility(View.GONE);


                allChannelRecycler.setVisibility(View.VISIBLE);


                if (response.isSuccessful()) {
                    for (LearnData data : response.body().getEpisode_videos()) {

                        dataList.add(data);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<HomeBodyResponse> call, Throwable t) {

                //  progressBar.setVisibility(View.GONE);

            }
        });

    }
}
