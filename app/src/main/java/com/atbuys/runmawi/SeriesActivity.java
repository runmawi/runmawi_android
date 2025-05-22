package com.atbuys.runmawi;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;

public class SeriesActivity extends AppCompatActivity {

    private ArrayList<series> serieslist;
    RecyclerView seriesRecycler;
    ProgressBar seriesProgress;
    SeriesAdapter seriesAdapter;
    private RecyclerView.LayoutManager seriesManeger;

     SeasonAdapter seasonAdapter;
     EpisodesAdapter episodeAdapter;
     RecyclerView seasonrecycler,episoderecycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series);

        serieslist =new ArrayList<series>();
        seriesRecycler = (RecyclerView) findViewById(R.id.seriesrecycler);
        seriesProgress=(ProgressBar)findViewById(R.id.seriesprogress);




        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        seriesAdapter = new SeriesAdapter(serieslist, this);
        seriesManeger = new GridLayoutManager(this, 3);

        seriesRecycler.setHasFixedSize(true);
        seriesRecycler.setLayoutManager(seriesManeger);
        seriesRecycler.setAdapter(seriesAdapter);


        Call<JSONResponse> call = ApiClient.getInstance1().getApi().getSeriesList();
        call.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                seriesProgress.setVisibility(View.GONE);
                seriesRecycler.setVisibility(View.VISIBLE);
                JSONResponse jsonResponse = response.body();
                serieslist = new ArrayList<>(Arrays.asList(jsonResponse.getSeries()));
                seriesAdapter = new SeriesAdapter(serieslist);
                seriesRecycler.setAdapter(seriesAdapter);


            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });


        seriesRecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(SeriesActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (serieslist.size() > position) {
                            if (serieslist.get(position) != null) {

                                Intent in=new Intent(SeriesActivity.this, SeasonEpisodeCopyActivity.class);
                                in.putExtra("id",serieslist.get(position).getId());
                                in.putExtra("image",serieslist.get(position).getImage_url());
                                in.putExtra("desc",serieslist.get(position).getDescription());
                                in.putExtra("title",serieslist.get(position).getTitle());
                                startActivity(in);
                            }
                        }
                    }
                }));

    }
}
