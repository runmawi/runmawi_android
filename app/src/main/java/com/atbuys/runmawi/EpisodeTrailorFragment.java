package com.atbuys.runmawi;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Response;

public class EpisodeTrailorFragment extends Fragment {

    ImageView trailorimage;
    TextView trailoename,trailorduration;
    String videoId,user_id;
    private ArrayList<episode> movie_detaildata;
    private ArrayList<season> season;

    public EpisodeTrailorFragment() {
    }

    public EpisodeTrailorFragment(String videoId, String user_id) {
        this.videoId=videoId;
        this.user_id=user_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root=(ViewGroup) inflater.inflate(R.layout.fragment_episode_trailor, container, false);

        trailorimage=(ImageView) root.findViewById(R.id.trailerimage);
        trailoename=(TextView)root.findViewById(R.id.trailorname) ;
        trailorduration=(TextView) root.findViewById(R.id.trailorduration);
        movie_detaildata=new ArrayList<>();
        season=new ArrayList<>();
        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getEpisodesDetail(videoId,user_id );
        res.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                JSONResponse jsonResponse = response.body();
                movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getEpisode()));
                season=new ArrayList<>(Arrays.asList(jsonResponse.getSeason()));

                Picasso.get().load(season.get(0).getImage()).into(trailorimage);
                trailoename.setText(movie_detaildata.get(0).getSeries_name());
                //trailorduration.setText(movie_detaildata.get(0).getDuration());
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

            }
        });

        trailorimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (season.get(0).getTrailer() == null || season.get(0).getTrailer().isEmpty()) {

                    Toast.makeText(getActivity(),"No Trailer Found", Toast.LENGTH_LONG).show();

                } else {
                    Intent in = new Intent(getActivity(), TrailerPlayerActivity.class);
                    in.putExtra("id", movie_detaildata.get(0).getId());
                    in.putExtra("url", season.get(0).getTrailer());
                    in.putExtra("type","episode");
                    startActivity(in);
                }
            }
        });

        return root;
    }
}