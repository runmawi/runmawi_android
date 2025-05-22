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
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.atbuys.runmawi.Adapter.LibraryPageAdapter;
import com.atbuys.runmawi.Api1.RetrofitSingleton;
import com.atbuys.runmawi.Model.HomeBodyResponse;
import com.atbuys.runmawi.Model.LibraryData;

import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

public class LibraryActivity extends AppCompatActivity {


    ProgressBar allChannelProgress;
    private ArrayList<LibraryData> dataList;
    private int currentPage = 1; // Current page number
    private boolean isLoading = false; // Flag to indicate if data is being loaded
    private boolean isLastPage = false; // Flag to indicate if it's the last page of data
    private int totalPages = 0; // Total number of pages
    private int visibleThreshold = 5; // Number of items remaining from the end before loading more data
    private RecyclerView allChannelRecycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private NestedScrollView nestedSV;
    int page = 0, limit = 1;


    ImageView notification;
    CircleImageView profiledp;
    String user_id;

    private ArrayList<user_details> user_detailsdata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {

            setTheme(R.style.darktheme);

        } else {


            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        dataList = new ArrayList<LibraryData>();

        allChannelRecycler = (RecyclerView) findViewById(R.id.idRVUsers);
        allChannelProgress = (ProgressBar) findViewById(R.id.idPBLoading);


        adapter = new LibraryPageAdapter(dataList, this);
        layoutManager = new GridLayoutManager(this, 1);


        allChannelRecycler.setHasFixedSize(true);
        allChannelRecycler.setLayoutManager(layoutManager);
        allChannelRecycler.setAdapter(adapter);

        notification = (ImageView) findViewById(R.id.notification);
        profiledp = (CircleImageView) findViewById(R.id.profiledp);
        nestedSV = (NestedScrollView) findViewById(R.id.idNestedSV);

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


        Call<HomeBodyResponse> responseCall = RetrofitSingleton.getInstance().getApi().getLibrary();
        responseCall.enqueue(new retrofit2.Callback<HomeBodyResponse>() {
            @Override
            public void onResponse(Call<HomeBodyResponse> call, retrofit2.Response<HomeBodyResponse> response) {

                allChannelProgress.setVisibility(View.GONE);


                allChannelRecycler.setVisibility(View.VISIBLE);


                if (response.isSuccessful()) {
                    for (LibraryData data : response.body().getVideos()) {


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

