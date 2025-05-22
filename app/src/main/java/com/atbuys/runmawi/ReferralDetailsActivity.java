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

import retrofit2.Call;

public class ReferralDetailsActivity extends AppCompatActivity {


    LinearLayout novideoscat;
    String user_id,user_role;
    RelativeLayout progresslayout;


    private ArrayList<myrefferals> channelvideosdata;
    RefercodeDetailsAdopter refercodeDetailsAdopter;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(  AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {


            setTheme(R.style.AppTheme);

        }

        else {


            setTheme(R.style.darktheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral_details);

   //     getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);


        novideoscat =(LinearLayout) findViewById(R.id.novideoscat);
        progresslayout =(RelativeLayout) findViewById(R.id.progresslayout);



        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in=new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(in);
            }
        });


        channelvideosdata = new ArrayList<myrefferals>();


        refercodeDetailsAdopter = new RefercodeDetailsAdopter(channelvideosdata, this);
        layoutManager = new GridLayoutManager(this, 3);


        recyclerView = findViewById(R.id.rv_main);
        progressBar = findViewById(R.id.pb_home);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);


        Call<JSONResponse> channel = ApiClient.getInstance1().getApi().getCoupons(user_id);
        channel.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                progresslayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                JSONResponse jsonResponse = response.body();

                if (jsonResponse.getMyrefferals().length ==0) {


                    progresslayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);

                    novideoscat.setVisibility(View.VISIBLE);

                }
                else
                {

                    recyclerView.setVisibility(View.VISIBLE);

                    channelvideosdata = new ArrayList<>(Arrays.asList(jsonResponse.getMyrefferals()));
                    refercodeDetailsAdopter = new RefercodeDetailsAdopter(channelvideosdata);
                    recyclerView.setAdapter(refercodeDetailsAdopter);

                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });

    }
}
