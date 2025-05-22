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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;


public class LivePageActivity extends AppCompatActivity {



    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<LiveCategory> channelvideosdata;
    livepageAdopter channelvideoAdapter;
    String id,name;
    TextView channelid;
    LinearLayout novideoscat;
    String user_id,user_role;
    private ArrayList<videodetail> movie_detaildata;
    private ArrayList<livedetail> livedetailsdata;
    RelativeLayout progresslayout;
    String Urll;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(  AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {


            setTheme(R.style.darktheme);

        }

        else {


            setTheme(R.style.AppTheme);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviecategorylist);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

         Intent in=getIntent();
         id=in.getStringExtra("id");
         name=in.getStringExtra("name");

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);

        channelid =(TextView) findViewById(R.id.namee);
        novideoscat =(LinearLayout) findViewById(R.id.novideoscat);
        progresslayout =(RelativeLayout) findViewById(R.id.progresslayout);

        channelid.setText(name);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               finish();
            }
        });



        movie_detaildata = new ArrayList<videodetail>();
        channelvideosdata = new ArrayList<LiveCategory>();
        livedetailsdata = new ArrayList<livedetail>();

        channelvideoAdapter = new livepageAdopter(channelvideosdata, this);
        layoutManager = new GridLayoutManager(this, 3);


        recyclerView = findViewById(R.id.rv_main);
        progressBar = findViewById(R.id.pb_home);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(channelvideoAdapter);

        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);


        Call<JSONResponse> channel = ApiClient.getInstance1().getApi().getLiveCat(id);
        channel.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                progresslayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                JSONResponse jsonResponse = response.body();



                if (jsonResponse.getLiveCategory().length == 0) {

                    progresslayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    novideoscat.setVisibility(View.VISIBLE);

                }

                else
                {
                    novideoscat.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    channelvideosdata = new ArrayList<>(Arrays.asList(jsonResponse.getLiveCategory()));
                    channelvideoAdapter = new livepageAdopter(channelvideosdata);
                    recyclerView.setAdapter(channelvideoAdapter);

                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });



        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(LivePageActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (channelvideosdata.size() > position) {
                            if (channelvideosdata.get(position) != null) {
                                Call<JSONResponse> res = ApiClient.getInstance1().getApi().getLiveDetail(user_id, channelvideosdata.get(position).getId());
                                res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                    @Override
                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                        JSONResponse jsonResponse = response.body();
                                        livedetailsdata = new ArrayList<>(Arrays.asList(jsonResponse.getLivedetail()));




                                        if (user_id == null ) {

                                            Intent in = new Intent(getApplicationContext(), HomePageLiveActivity.class);
                                            in.putExtra("id", channelvideosdata.get(position).getId());
                                            in.putExtra("url",   channelvideosdata.get(position).getHls_url());
                                            in.putExtra("data","videos");
                                            in.putExtra("ads","");
                                            startActivity(in);

                                        }

                                        else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                            if (channelvideosdata.get(position).getAccess().equalsIgnoreCase("ppv"))
                                            {




                                                Intent in = new Intent(getApplicationContext(), HomePageLiveActivity.class);
                                                in.putExtra("id", channelvideosdata.get(position).getId());
                                                in.putExtra("url",   channelvideosdata.get(position).getHls_url());
                                                in.putExtra("xtra", "norent");
                                                in.putExtra("ads","");
                                                in.putExtra("data","videos");
                                                startActivity(in);

                                            } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {


                                                Intent in = new Intent(getApplicationContext(), HomePageLiveActivity.class);
                                                in.putExtra("id", channelvideosdata.get(position).getId());
                                                in.putExtra("url",   channelvideosdata.get(position).getHls_url());
                                                in.putExtra("xtra", "norent");
                                                in.putExtra("ads","");
                                                in.putExtra("data","videos");
                                                getApplicationContext().startActivity(in);
                                            }
                                            else if (channelvideosdata.get(position).getAccess().equalsIgnoreCase("subscriber") || channelvideosdata.get(position).getAccess().equalsIgnoreCase("admin")) {

                                                Intent in = new Intent(getApplicationContext(), HomePageLiveActivity.class);
                                                in.putExtra("id", channelvideosdata.get(position).getId());
                                                in.putExtra("url",   channelvideosdata.get(position).getHls_url());
                                                in.putExtra("xtra", "subscriber_content");
                                                in.putExtra("ads","");
                                                in.putExtra("data","videos");
                                                startActivity(in);
                                            }

                                            else {

                                                String videourl =  channelvideosdata.get(position).getHls_url() ;

                                                /*Intent in = new Intent(getContext(), OnlinePlayerActivity.class);
                                                in.putExtra("id", movie_detaildata.get(position).getId());
                                                in.putExtra("url",videourl);
                                                startActivity(in);*/
                                                Intent in = new Intent(getApplicationContext(), HomePageLiveActivity.class);
                                                in.putExtra("id", channelvideosdata.get(position).getId());
                                                in.putExtra("url", videourl);
                                                in.putExtra("xtra", "rentted");
                                                in.putExtra("data","videos");
                                                in.putExtra("ads","");
                                                startActivity(in);
                                            }

                                        } else {

                                            if (channelvideosdata.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view")) {

                                                Intent in = new Intent(getApplicationContext(), HomePageLiveActivity.class);
                                                in.putExtra("id", channelvideosdata.get(position).getId());
                                                in.putExtra("url",  channelvideosdata.get(position).getHls_url());
                                                in.putExtra("xtra", "subscriberented");
                                                in.putExtra("data","videos");
                                                in.putExtra("ads","");
                                                startActivity(in);

                                            }

                                            else if(channelvideosdata.get(position).getAccess().equalsIgnoreCase("ppv") ||channelvideosdata.get(position).getAccess().equalsIgnoreCase("expired"))
                                            {
                                                Intent in = new Intent(getApplicationContext(), HomePageLiveActivity.class);
                                                in.putExtra("id", channelvideosdata.get(position).getId());
                                                in.putExtra("url",   channelvideosdata.get(position).getHls_url());
                                                in.putExtra("xtra", "subscriberrent");
                                                in.putExtra("data","videos");
                                                in.putExtra("ads","");
                                                startActivity(in);
                                            }

                                            else {
                                                Intent in = new Intent(getApplicationContext(), HomePageLiveActivity.class);
                                                in.putExtra("id", channelvideosdata.get(position).getId());
                                                in.putExtra("url",   channelvideosdata.get(position).getHls_url());
                                                in.putExtra("xtra", "Norent");
                                                in.putExtra("data","videos");
                                                in.putExtra("ads","");
                                                startActivity(in);
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



                })
        );



    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
