package com.atbuys.runmawi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;


public class YoutubeActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;
    String id,user_id,user_role;
    private ArrayList<videodetail> movie_detaildata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube);

        movie_detaildata = new ArrayList<videodetail>();


        Intent in = getIntent();
        id = in.getStringExtra("id");

        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);

        youTubePlayerView = findViewById(R.id.activity_main_youtubePlayerView);
        getLifecycle().addObserver(youTubePlayerView);


        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id,id);
        res.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                if (user_id == null) {

                    Toast.makeText(YoutubeActivity.this, "player"+movie_detaildata.get(0).getEmbed_code() , Toast.LENGTH_SHORT).show();


                }

                if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                    if (response.body().getPpv_video_status().equalsIgnoreCase("pay_now")) {


                    } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {



                    } else {

                        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                            @Override
                            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                                String videoId =movie_detaildata.get(0).getEmbed_code() ;
                                youTubePlayer.loadVideo(videoId, 0);
                            }
                        });
                    }

                } else {

                    youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                            String videoId =movie_detaildata.get(0).getEmbed_code();
                            youTubePlayer.loadVideo(videoId, 0);

                        }
                    });



                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });





    }
}