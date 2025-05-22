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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;


public class CategoryAudiosPageActivity extends AppCompatActivity {



    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<categoryaudio> channelvideosdata;
    AudioCategoryvideoAdapter channelvideoAdapter;
    String id,name;
    TextView channelid;
    LinearLayout novideoscat;
    String user_id,user_role;
    private ArrayList<audiodetail> movie_detaildata;

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



        movie_detaildata = new ArrayList<audiodetail>();
        channelvideosdata = new ArrayList<categoryaudio>();

        adapter = new AudioCategoryvideoAdapter(channelvideosdata, this);
        layoutManager = new GridLayoutManager(this, 3);


        recyclerView = findViewById(R.id.rv_main);
        progressBar = findViewById(R.id.pb_home);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);


        Call<JSONResponse> channel = ApiClient.getInstance1().getApi().getCategoryAudios(id,user_id);
        channel.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                progresslayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                JSONResponse jsonResponse = response.body();



                if (jsonResponse.getCategoryaudio().length == 0) {

                    progresslayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    novideoscat.setVisibility(View.VISIBLE);

                }

                else
                {
                    novideoscat.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    channelvideosdata = new ArrayList<>(Arrays.asList(jsonResponse.getCategoryaudio()));
                    channelvideoAdapter = new AudioCategoryvideoAdapter(channelvideosdata);
                    recyclerView.setAdapter(channelvideoAdapter);

                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });



        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(CategoryAudiosPageActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (channelvideosdata.size() > position) {
                            if (channelvideosdata.get(position) != null) {

                                if(user_role == null && channelvideosdata.get(position).getAccess().equalsIgnoreCase("registered")
                                        || user_role == null && channelvideosdata.get(position).getAccess().equalsIgnoreCase("subscriber"))
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

                                else if (user_role == null && channelvideosdata.get(position).getAccess().equalsIgnoreCase("guest") ) {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, channelvideosdata.get(position).getId());

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
                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                    startActivity(in);
                                    mediaplayer.reset();
                                    mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                                    try {

                                        mediaplayer.setDataSource(channelvideosdata.get(position).getMp3_url());
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

                                else if (user_role.equalsIgnoreCase("subscriber") || user_role.equalsIgnoreCase("admin")) {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, channelvideosdata.get(position).getId());
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
                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                    startActivity(in);
                                    mediaplayer.reset();
                                    mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                                    try {
                                        mediaplayer.setDataSource(channelvideosdata.get(position).getMp3_url());
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

                                else if(user_role.equalsIgnoreCase("registered") && channelvideosdata.get(position).getAccess().equalsIgnoreCase("guest") || user_role.equalsIgnoreCase("registered") && channelvideosdata.get(position).getAccess().equalsIgnoreCase("registered"))
                                {

                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, channelvideosdata.get(position).getId());
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
                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                    startActivity(in);
                                    mediaplayer.reset();
                                    mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                                    try {
                                        mediaplayer.setDataSource(channelvideosdata.get(position).getMp3_url());
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



                })
        );



    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
