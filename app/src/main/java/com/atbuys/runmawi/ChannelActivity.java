package com.atbuys.runmawi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.atbuys.runmawi.Adapter.HomeCategoryPageAdapter;
import com.atbuys.runmawi.Api1.RetrofitSingleton;
import com.atbuys.runmawi.Model.HomeBodyResponse;
import com.atbuys.runmawi.Model.HomeData;

import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

public class ChannelActivity extends AppCompatActivity {


    ProgressBar allChannelProgress;
    private ArrayList<HomeData> dataList;

    private RecyclerView allChannelRecycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    ImageView notification;
    CircleImageView profiledp;
    String user_id;

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
        setContentView(R.layout.activity_channel);

       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        dataList = new ArrayList<HomeData>();

        allChannelRecycler = (RecyclerView) findViewById(R.id.channelistrecycler);
        allChannelProgress=(ProgressBar)findViewById(R.id.catoryprogress);

        adapter = new HomeCategoryPageAdapter(dataList, this);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


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


        Call<HomeBodyResponse> responseCall = RetrofitSingleton.getInstance().getApi().getMovieByCategory();
        responseCall.enqueue(new retrofit2.Callback<HomeBodyResponse>() {
            @Override
            public void onResponse(Call<HomeBodyResponse> call, retrofit2.Response<HomeBodyResponse> response) {

                allChannelProgress.setVisibility(View.GONE);


                allChannelRecycler.setVisibility(View.VISIBLE);

                if (response.isSuccessful()) {
                    for (HomeData data : response.body().getGenreMovies()) {

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
