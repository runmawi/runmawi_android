package com.atbuys.runmawi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atbuys.runmawi.Adapter.Home1Adapter;
import com.atbuys.runmawi.Api1.RetrofitSingleton;
import com.atbuys.runmawi.Model.HomeBodyResponse;
import com.atbuys.runmawi.Model.HomeData1;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LiveStreamPageActivity extends AppCompatActivity {



    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<HomeData1> dataList;
    String id;
    TextView channelid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lie_stream);

      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);


        Intent in=getIntent();
         id=in.getStringExtra("channelid");

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);

        channelid =(TextView) findViewById(R.id.channelid);



        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in=new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(in);
            }
        });



        dataList = new ArrayList<HomeData1>();

        adapter = new Home1Adapter(dataList, this);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        recyclerView = findViewById(R.id.rv_main);
        progressBar = findViewById(R.id.pb_home);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);



        Call<HomeBodyResponse> responseCall = RetrofitSingleton.getInstance().getApi().getLivestreams();

        responseCall.enqueue(new Callback<HomeBodyResponse>() {
            @Override
            public void onResponse(Call<HomeBodyResponse> call, Response<HomeBodyResponse> response) {

                progressBar.setVisibility(View.GONE);

                if(response.isSuccessful()){

                    for (HomeData1 data : response.body().getLive_streams()) {

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
