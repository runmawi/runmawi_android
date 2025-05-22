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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;


public class ChannalPageActivity1 extends AppCompatActivity {

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<PageList> channelvideosdata;
    chennalCategoryvideoAdapter1 channelvideoAdapter;
    String id, name, name1, Source_name = "featured_videos", number;
    TextView channelid;
    LinearLayout novideoscat;
    String user_id, user_role;
    private ArrayList<videodetail> movie_detaildata;

    RelativeLayout progresslayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {


            setTheme(R.style.darktheme);

        } else {


            setTheme(R.style.AppTheme);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviecategorylist);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        Intent in = getIntent();
        id = in.getStringExtra("id");
        name = in.getStringExtra("name");
        name1 = in.getStringExtra("name1");
        number = in.getStringExtra("number");
        //Toast.makeText(getApplicationContext(), "id-> "+id+ " name-> "+name+" num-> "+user_id+" num"+number, Toast.LENGTH_LONG).show();//liveCategories

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);

        channelid = (TextView) findViewById(R.id.namee);
        novideoscat = (LinearLayout) findViewById(R.id.novideoscat);
        progresslayout = (RelativeLayout) findViewById(R.id.progresslayout);

        if (number.equalsIgnoreCase("z")) {
            channelid.setText(name1);

        } else {
            channelid.setText(name);

        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


        movie_detaildata = new ArrayList<videodetail>();
        channelvideosdata = new ArrayList<PageList>();
        adapter = new chennalCategoryvideoAdapter1(channelvideosdata, this);
        layoutManager = new GridLayoutManager(this, 3);
        recyclerView = findViewById(R.id.rv_main);
        progressBar = findViewById(R.id.pb_home);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);

        if (number.equalsIgnoreCase("y")) {
            Call<JSONResponse> channel = ApiClient.getInstance1().getApi().gethomelink1("Series_Genre_videos", id);
            channel.enqueue(new retrofit2.Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        progresslayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        JSONResponse jsonResponse = response.body();
                        if (jsonResponse.getPage_List() == null || jsonResponse.getPage_List().length == 0) {
                            progresslayout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            novideoscat.setVisibility(View.VISIBLE);
                        } else {
                            novideoscat.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            channelvideosdata = new ArrayList<>(Arrays.asList(jsonResponse.getPage_List()));
                            channelvideoAdapter = new chennalCategoryvideoAdapter1(channelvideosdata, false);
                            recyclerView.setAdapter(channelvideoAdapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<JSONResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });

        }
        if (number.equalsIgnoreCase("z")) {

            Log.w("param","name: "+name+" "+id);
            Call<JSONResponse> channel = ApiClient.getInstance1().getApi().gethomelink1(name, id);
            channel.enqueue(new retrofit2.Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                    if (response.isSuccessful() && response.body() != null) {

                        progresslayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        JSONResponse jsonResponse = response.body();
                        if (jsonResponse.getPage_List() == null || jsonResponse.getPage_List().length == 0) {
                            progresslayout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            novideoscat.setVisibility(View.VISIBLE);
                        } else {
                            novideoscat.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            channelvideosdata = new ArrayList<>(Arrays.asList(jsonResponse.getPage_List()));
                            channelvideoAdapter = new chennalCategoryvideoAdapter1(channelvideosdata, false);
                            recyclerView.setAdapter(channelvideoAdapter);
                        }
                    }else if (response.code() == 429) {
                        Toast.makeText(ChannalPageActivity1.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JSONResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });

        } else if (number.equalsIgnoreCase("a")) {
            Call<JSONResponse> channel = ApiClient.getInstance1().getApi().gethomelink(id, user_id);
            channel.enqueue(new retrofit2.Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        progresslayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        JSONResponse jsonResponse = response.body();
                        if (jsonResponse.getPage_List() == null || jsonResponse.getPage_List().length == 0) {
                            progresslayout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            novideoscat.setVisibility(View.VISIBLE);
                        } else {
                            novideoscat.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            GridLayoutManager layoutManager1 = new GridLayoutManager(getApplicationContext(), 2);
                            channelvideosdata = new ArrayList<>(Arrays.asList(jsonResponse.getPage_List()));
                            channelvideoAdapter = new chennalCategoryvideoAdapter1(channelvideosdata, true);
                            recyclerView.setLayoutManager(layoutManager1);
                            recyclerView.setAdapter(channelvideoAdapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<JSONResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();//liveCategories
                }
            });

        } else {
            Call<JSONResponse> channel = ApiClient.getInstance1().getApi().gethomelink(id, user_id);
            channel.enqueue(new retrofit2.Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        progresslayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        JSONResponse jsonResponse = response.body();
                        if (jsonResponse.getPage_List() == null || jsonResponse.getPage_List().length == 0) {
                            progresslayout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            novideoscat.setVisibility(View.VISIBLE);
                        } else {
                            novideoscat.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            channelvideosdata = new ArrayList<>(Arrays.asList(jsonResponse.getPage_List()));
                            channelvideoAdapter = new chennalCategoryvideoAdapter1(channelvideosdata, false);
                            recyclerView.setAdapter(channelvideoAdapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<JSONResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();//liveCategories
                }
            });

        }
    }
}