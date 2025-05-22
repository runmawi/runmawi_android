package com.atbuys.runmawi;

import static com.atbuys.runmawi.UserHomeFragment.audio_id;
import static com.atbuys.runmawi.UserHomeFragment.mediaplayer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;

public class FavoriteActivity extends AppCompatActivity {


    MyFavouriteAdopter wishlistAdopter;


    RecyclerView watchlistrecycle,watchlistaudiorecycle;
    ProgressBar watchlistprogress,watchlistaudioprogress;
    TextView novideos;
    private ArrayList<channel_audios> mywishlistdata;
    private ArrayList<audiodetail> movie_detaildata;
    String user_id,user_role;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlater);

        mywishlistdata = new ArrayList<channel_audios>();

        wishlistAdopter = new MyFavouriteAdopter(mywishlistdata, this);

        movie_detaildata = new ArrayList<audiodetail>();



        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id,null );
        user_role =prefs.getString(sharedpreferences.role,null);
        audio_id = prefs.getString(sharedpreferences.audioid,null);

        watchlistprogress=(ProgressBar)findViewById(R.id.watchlistprogress1);
        watchlistrecycle = (RecyclerView) findViewById(R.id.watchlilstrecycle);
        novideos=(TextView) findViewById(R.id.novideos);
        watchlistrecycle.setLayoutManager(new GridLayoutManager(this, 3));

        watchlistrecycle.setHasFixedSize(true);
        watchlistrecycle.setAdapter(wishlistAdopter);




        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getMyWatchlater(user_id);
        movieres.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();

                watchlistprogress.setVisibility(View.GONE);
                if (jsonResponse.getChannel_audios().length == 0) {

                    novideos.setVisibility(View.VISIBLE);
                    watchlistrecycle.setVisibility(View.GONE);

                } else {

                    novideos.setVisibility(View.GONE);
                    watchlistrecycle.setVisibility(View.VISIBLE);
                    mywishlistdata = new ArrayList<>(Arrays.asList(jsonResponse.getChannel_audios()));
                    wishlistAdopter = new MyFavouriteAdopter(mywishlistdata);
                    watchlistrecycle.setAdapter(wishlistAdopter);

                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });


        watchlistrecycle.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (mywishlistdata.size() > position) {
                            if (mywishlistdata.get(position) != null)

                            {
                                if(user_role == null)
                                {

                                    AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                                    alert.setTitle("Your Not Subscribed user Kindly Subscribe and access the contents");

                                    alert.setPositiveButton("Subscribe", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent in = new Intent(getApplicationContext(), SubscribeActivity.class);
                                            startActivity(in);
                                        }
                                    });
                                    alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });
                                    alert.show();
                                }


                                else if (user_role.equalsIgnoreCase("subscriber")  || user_role.equalsIgnoreCase("admin")) {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, mywishlistdata.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));

                                            SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                            editor.putBoolean(sharedpreferences.login, true);
                                            editor.putString(sharedpreferences.audioid, movie_detaildata.get(0).getId());
                                            editor.apply();
                                            editor.commit();

                                            audio_id = prefs.getString(sharedpreferences.audioid, null);
                                        }

                                        @Override
                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                                            Log.d("Error", t.getMessage());
                                        }
                                    });

                                    Intent in = new Intent(getApplicationContext(), MediaPlayerPageActivity.class);
                                    in.putExtra("id", audio_id);
                                    startActivity(in);
                                    mediaplayer.reset();
                                    mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                                    try {
                                        mediaplayer.setDataSource(mywishlistdata.get(position).getMp3_url());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        mediaplayer.prepare();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    mediaplayer.start();
                                }

                                else if(user_role.equalsIgnoreCase("registered") && mywishlistdata.get(position).getAccess().equalsIgnoreCase("guest") || user_role.equalsIgnoreCase("registered") && mywishlistdata.get(position).getAccess().equalsIgnoreCase("registered"))
                                {

                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, mywishlistdata.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));

                                            SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                            editor.putBoolean(sharedpreferences.login, true);
                                            editor.putString(sharedpreferences.audioid, movie_detaildata.get(0).getId());
                                            editor.apply();
                                            editor.commit();

                                            audio_id = prefs.getString(sharedpreferences.audioid, null);
                                        }

                                        @Override
                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                                            Log.d("Error", t.getMessage());
                                        }
                                    });

                                    Intent in = new Intent(getApplicationContext(), MediaPlayerPageActivity.class);
                                    in.putExtra("id", audio_id);
                                    startActivity(in);
                                    mediaplayer.reset();
                                    mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                                    try {
                                        mediaplayer.setDataSource(mywishlistdata.get(position).getMp3_url());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        mediaplayer.prepare();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    mediaplayer.start();

                                }
                                else {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                                    alert.setTitle("Your Not Subscribed user Kindly Subscribe and access the contents");

                                    alert.setPositiveButton("Subscribe", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent in = new Intent(getApplicationContext(), SubscribeActivity.class);
                                            startActivity(in);
                                        }
                                    });
                                    alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });
                                    alert.show();

                                }
                            }
                        }

                    }
                }));
    }



    @Override
    public void onBackPressed() {
        finish();
    }
}
