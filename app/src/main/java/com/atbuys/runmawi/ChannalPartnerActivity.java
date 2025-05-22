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


public class ChannalPartnerActivity extends AppCompatActivity {



    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<channels> channelvideosdata;
    chennalparnerAdapter channelvideoAdapter;
    String id,name;
    TextView channelid;
    LinearLayout novideoscat;
    String user_id,user_role;
    private ArrayList<videodetail> movie_detaildata;

    RelativeLayout progresslayout;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(  AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {

            setTheme(R.style.darktheme);
        }

        else {


            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channal_partner);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

         Intent in=getIntent();
         id=in.getStringExtra("id");
      //   name=in.getStringExtra("name");

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);

        channelid =(TextView) findViewById(R.id.namee);
        novideoscat =(LinearLayout) findViewById(R.id.novideoscat);
        progresslayout =(RelativeLayout) findViewById(R.id.progresslayout);

        channelid.setText("Partner Content");

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               finish();
            }
        });



        movie_detaildata = new ArrayList<videodetail>();
        channelvideosdata = new ArrayList<channels>();

        adapter = new chennalparnerAdapter(channelvideosdata, this);
        layoutManager = new GridLayoutManager(this, 3);


        recyclerView = findViewById(R.id.rv_main);
        progressBar = findViewById(R.id.pb_home);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);


        Call<JSONResponse> channel = ApiClient.getInstance1().getApi().getChannel();
        channel.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                progresslayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                JSONResponse jsonResponse = response.body();



                if (jsonResponse.getChannels().length == 0) {

                    progresslayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    novideoscat.setVisibility(View.VISIBLE);

                }

                else
                {
                    novideoscat.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    channelvideosdata = new ArrayList<>(Arrays.asList(jsonResponse.getChannels()));
                    channelvideoAdapter = new chennalparnerAdapter(channelvideosdata);
                    recyclerView.setAdapter(channelvideoAdapter);

                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });



        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(ChannalPartnerActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (channelvideosdata.size() > position) {
                            if (channelvideosdata.get(position) != null)

                            {

                                Intent in = new Intent(getApplicationContext(),ChannelpartnerlistActivity.class);
                                in.putExtra("slug",channelvideosdata.get(position).getChannel_slug());
                                in.putExtra("image",channelvideosdata.get(position).getChannel_banner());
                                in.putExtra("name",channelvideosdata.get(position).getChannel_name());
                                in.putExtra("channal_image",channelvideosdata.get(position).getChannel_image());
                                startActivity(in);

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
