package com.atbuys.runmawi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import retrofit2.Call;

public class ContinueWatchingSeeall extends AppCompatActivity {

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    NewContinueWatchingAdopter continueWatchingAdopter;
    private RecyclerView.LayoutManager layoutManager;
    String user_id,user_role;
    LinearLayout novideoscat;
    RelativeLayout progresslayout;
    private ArrayList<videos> continuwatchilinglist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(  AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        }
        else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_watching_seeall);

        layoutManager = new GridLayoutManager(this, 3);

        recyclerView = findViewById(R.id.rv_main);
        progressBar = findViewById(R.id.pb_home);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(continueWatchingAdopter);
        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);
        novideoscat =(LinearLayout) findViewById(R.id.novideoscat);
        progresslayout =(RelativeLayout) findViewById(R.id.progresslayout);
        continuwatchilinglist = new ArrayList<videos>();


        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        Call<JSONResponse> callelist = ApiClient.getInstance1().getApi().getContinelisting(user_id);
        callelist.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                progresslayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                JSONResponse jsonResponse = response.body();

                if (jsonResponse.getVideos().length == 0) {
                    progresslayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    novideoscat.setVisibility(View.VISIBLE);
                } else {
                    novideoscat.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    continuwatchilinglist = new ArrayList<>(Arrays.asList(jsonResponse.getVideos()));
                    Collections.reverse(continuwatchilinglist);
                    continueWatchingAdopter = new NewContinueWatchingAdopter(continuwatchilinglist);
                    recyclerView.setAdapter(continueWatchingAdopter);
                    continueWatchingAdopter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(ContinueWatchingSeeall.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (continuwatchilinglist.size() > position) {
                            if (continuwatchilinglist.get(position) != null) {

                                String videourl;

                                if (continuwatchilinglist.get(position).getMp4_url() == null) {
                                    videourl = continuwatchilinglist.get(position).getTrailer();
                                } else {
                                    videourl = continuwatchilinglist.get(position).getMp4_url();
                                }

                                Intent in = new Intent(ContinueWatchingSeeall.this, HomePageVideoActivity.class);
                                in.putExtra("id", continuwatchilinglist.get(position).getId());
                                in.putExtra("url", videourl);
                                in.putExtra("xtra", "");
                                in.putExtra("ads", "");
                                startActivity(in);
                            }
                        }
                    }
                }));


    }
}