package com.atbuys.runmawi;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;

public class SeriesFragment extends Fragment {

    private ArrayList<series> serieslist;
    RecyclerView seriesRecycler;
    ProgressBar seriesProgress;
    SeriesAdapter seriesAdapter;
    private RecyclerView.LayoutManager seriesManeger;

     SeasonAdapter seasonAdapter;
     EpisodesAdapter episodeAdapter;
     RecyclerView seasonrecycler,episoderecycler;




        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_series, container, false);



            serieslist =new ArrayList<series>();


            seriesRecycler = (RecyclerView) root.findViewById(R.id.seriesrecycler);
        seriesProgress=(ProgressBar)root.findViewById(R.id.seriesprogress);




        Toolbar mToolbar = (Toolbar)root. findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().finish();
            }
        });
        seriesAdapter = new SeriesAdapter(serieslist, getContext());
        seriesManeger = new GridLayoutManager(getContext(), 3);

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
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (serieslist.size() > position) {
                            if (serieslist.get(position) != null) {

                                Intent in=new Intent(getContext(), SeasonEpisodeCopyActivity.class);
                                in.putExtra("id",serieslist.get(position).getId());
                                in.putExtra("image",serieslist.get(position).getImage_url());
                                in.putExtra("desc",serieslist.get(position).getDescription());
                                in.putExtra("title",serieslist.get(position).getTitle());
                                startActivity(in);
                            }
                        }
                    }
                }));

            return root;
        }
}
