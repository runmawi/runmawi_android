package com.atbuys.runmawi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;

public class EpisodeAlsoLikeFragment extends Fragment {

    RecyclerView thismayalsolike;
    String videoId, user_id;
    ThismaylikeEpisodeAdopter thismaylikeadopter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<related_episode> recomendeddatalist;

    public EpisodeAlsoLikeFragment() {
    }

    public EpisodeAlsoLikeFragment(String videoId, String user_id) {
        this.videoId = videoId;
        this.user_id = user_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_also_like, container, false);

        thismayalsolike = (RecyclerView) root.findViewById(R.id.thismayalsolike);
        recomendeddatalist = new ArrayList<related_episode>();


        thismaylikeadopter = new ThismaylikeEpisodeAdopter(recomendeddatalist, getActivity());
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);


        thismayalsolike.setHasFixedSize(true);
        thismayalsolike.setLayoutManager(layoutManager);
        thismayalsolike.setAdapter(thismaylikeadopter);


        Call<JSONResponse> alsolikeres = ApiClient.getInstance1().getApi().getAlsolikeEpisodes(videoId);
        alsolikeres.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();

                thismayalsolike.setVisibility(View.VISIBLE);
                recomendeddatalist = new ArrayList<>(Arrays.asList(jsonResponse.getRelated_episode()));
                thismaylikeadopter = new ThismaylikeEpisodeAdopter(recomendeddatalist);
                thismayalsolike.setAdapter(thismaylikeadopter);

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());

            }
        });

        thismayalsolike.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (recomendeddatalist.size() > position) {
                            if (recomendeddatalist.get(position) != null) {

                                Intent in = new Intent(getActivity(), HomePageEpisodeActivity.class);
                                in.putExtra("id", recomendeddatalist.get(position).getId());
                                in.putExtra("url", recomendeddatalist.get(position).getMp4_url());
                                in.putExtra("seasonid", recomendeddatalist.get(position).getSeason_id());
                                in.putExtra("series_id", recomendeddatalist.get(position).getSeries_id());
                                startActivity(in);
                            }
                        }


                    }
                })
        );


        return root;
    }
}