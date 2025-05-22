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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit2.Call;

public class ArtistPageActivity extends AppCompatActivity {


    RecyclerView artistsongrecycler;
    ProgressBar artistsongprogress;

    AlertDialog.Builder alert;
    List<artistsongs> artistsongsList;
    private ArrayList<artist> artist_list;
    private ArrayList<artist_audios> artist_audioslist;
    private ArrayList<audiodetail> movie_detaildata;

    String artist_id,user_id,user_role;
    ImageView artistimage;
    TextView artist_name;
    LinearLayout artist_layout;
    RelativeLayout progresslayout;
    LinearLayout favouriteartistlayout,favouriteaddedartistlayout;
    Button follow,following;
    TextView no_audio;
    Artist_AudioAdapter artist_audioAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_page);

        Intent in = getIntent();
        artist_id = in.getStringExtra("artist_id");

        artistsongsList = new ArrayList<>();
        artist_list = new ArrayList<>();
        artist_audioslist = new ArrayList<>();


        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);
        audio_id = prefs.getString(sharedpreferences.audioid,"");

        artistimage = (ImageView) findViewById(R.id.artistimage);
        artist_name = (TextView) findViewById(R.id.artist_name);
        artist_layout = (LinearLayout)  findViewById(R.id.artist_layout);
        progresslayout = (RelativeLayout) findViewById(R.id.progresslayout);
        favouriteartistlayout = (LinearLayout) findViewById(R.id.favouriteartistlayout);
        favouriteaddedartistlayout = (LinearLayout) findViewById(R.id.favouriteaddedartistlayout);
        follow = (Button) findViewById(R.id.follow);
        following = (Button) findViewById(R.id.following);
        no_audio = (TextView) findViewById(R.id.no_audios);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.backarrow);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             finish();
            }
        });

        artistsongrecycler = (RecyclerView) findViewById(R.id.artistsongsrecycler);
        artist_audioAdapter = new Artist_AudioAdapter(artist_audioslist, getApplicationContext());
        artistsongrecycler.setHasFixedSize(true);
        artistsongrecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        artistsongrecycler.setAdapter(artist_audioAdapter);


        Call<JSONResponse> callaudio = ApiClient.getInstance1().getApi().getArtistDetails(artist_id,user_id);
        callaudio.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();

                if (jsonResponse.getStatus().equalsIgnoreCase("false")) {

                    no_audio.setVisibility(View.VISIBLE);
                    artistsongrecycler.setVisibility(View.GONE);
                }
                else {
                    no_audio.setVisibility(View.GONE);
                    artistsongrecycler.setVisibility(View.VISIBLE);
                    artist_audioslist = new ArrayList<>(Arrays.asList(jsonResponse.getArtist_audios()));
                    artist_audioAdapter = new Artist_AudioAdapter(artist_audioslist);
                    artistsongrecycler.setAdapter(artist_audioAdapter);

                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });



        artistsongrecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (artist_audioslist.size() > position) {
                            if (artist_audioslist.get(position) != null) {

                                if(user_role == null && artist_audioslist.get(position).getAccess().equalsIgnoreCase("registered")
                                        || user_role == null && artist_audioslist.get(position).getAccess().equalsIgnoreCase("subscriber"))
                                {

                                    AlertDialog.Builder alert = new AlertDialog.Builder(ArtistPageActivity.this);
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

                                else if (user_role == null && artist_audioslist.get(position).getAccess().equalsIgnoreCase("guest") ) {

                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, artist_audioslist.get(position).getId());
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

                                            audio_id = prefs.getString(sharedpreferences.audioid, "");
                                        }

                                        @Override
                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                                            Log.d("Error", t.getMessage());
                                        }
                                    });

                                    Intent in = new Intent(getApplicationContext(), MediaPlayerPageActivity.class);
                                    in.putExtra("id", artist_audioslist.get(position).getId());
                                    startActivity(in);
                                    mediaplayer.reset();
                                    mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                                    try {
                                        mediaplayer.setDataSource(artist_audioslist.get(position).getMp3_url());
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

                                else if (user_role != null && artist_audioslist.get(position).getAccess().equalsIgnoreCase("guest") ) {

                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, artist_audioslist.get(position).getId());
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

                                            audio_id = prefs.getString(sharedpreferences.audioid, "");
                                        }

                                        @Override
                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                                            Log.d("Error", t.getMessage());
                                        }
                                    });

                                    Intent in = new Intent(getApplicationContext(), MediaPlayerPageActivity.class);
                                    in.putExtra("id", artist_audioslist.get(position).getId());
                                    startActivity(in);
                                    mediaplayer.reset();
                                    mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                                    try {
                                        mediaplayer.setDataSource(artist_audioslist.get(position).getMp3_url());
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


                                else if (user_role.equalsIgnoreCase("subscriber") || user_role.equalsIgnoreCase("admin") ) {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, artist_audioslist.get(position).getId());
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

                                            audio_id = prefs.getString(sharedpreferences.audioid, "");
                                        }

                                        @Override
                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                                            Log.d("Error", t.getMessage());
                                        }
                                    });

                                    Intent in = new Intent(getApplicationContext(), MediaPlayerPageActivity.class);
                                    in.putExtra("id", artist_audioslist.get(position).getId());
                                    startActivity(in);
                                    mediaplayer.reset();
                                    mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                                    try {
                                        mediaplayer.setDataSource(artist_audioslist.get(position).getMp3_url());
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

                                else if(user_role.equalsIgnoreCase("registered") && artist_audioslist.get(position).getAccess().equalsIgnoreCase("guest") || user_role.equalsIgnoreCase("registered") && artist_audioslist.get(position).getAccess().equalsIgnoreCase("registered"))
                                {

                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, artist_audioslist.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));

                                          /*  SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                            editor.putBoolean(sharedpreferences.login, true);
                                            editor.putString(sharedpreferences.audioid, movie_detaildata.get(0).getId());
                                            editor.apply();
                                            editor.commit();*/

                                            audio_id = prefs.getString(sharedpreferences.audioid, "");
                                        }

                                        @Override
                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                                            Log.d("Error", t.getMessage());
                                        }
                                    });

                                    Intent in = new Intent(getApplicationContext(), MediaPlayerPageActivity.class);
                                    in.putExtra("id", artist_audioslist.get(position).getId());
                                    startActivity(in);
                                    mediaplayer.reset();
                                    mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                                    try {
                                        mediaplayer.setDataSource(artist_audioslist.get(position).getMp3_url());
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
                                    AlertDialog.Builder alert = new AlertDialog.Builder(ArtistPageActivity.this);
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


        Call<JSONResponse> callser = ApiClient.getInstance1().getApi().getArtistDetails(artist_id,user_id);
        callser.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                progresslayout.setVisibility(View.GONE);
                artist_layout.setVisibility(View.VISIBLE);
                JSONResponse jsonResponse = response.body();

                if (jsonResponse.getArtist().length == 0) {




                } else {


                    artist_list = new ArrayList<>(Arrays.asList(jsonResponse.getArtist()));
                   Picasso.get().load(artist_list.get(0).getImage_url()).fit().into(artistimage);
                   artist_name .setText(artist_list.get(0).getArtist_name());

                    if (jsonResponse.getFavourites().equalsIgnoreCase("true")) {
                        favouriteartistlayout.setVisibility(View.GONE);
                        favouriteaddedartistlayout.setVisibility(View.VISIBLE);
                    } else {

                        favouriteartistlayout.setVisibility(View.VISIBLE);
                        favouriteaddedartistlayout.setVisibility(View.GONE);
                    }

                    if (jsonResponse.getFollowing().equalsIgnoreCase("true")) {
                        follow.setVisibility(View.GONE);
                        following.setVisibility(View.VISIBLE);

                    } else {

                        follow.setVisibility(View.VISIBLE);
                        following.setVisibility(View.GONE);

                    }


                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });



        favouriteartistlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Api.getClient().getAddorRemoveFavourite(user_id, artist_id, "1", new Callback<Addtowishlistmovie>() {

                    @Override
                    public void success(Addtowishlistmovie addwishmovie, retrofit.client.Response response) {

                       Toast.makeText(getApplicationContext(),""+addwishmovie.getMessage(), Toast.LENGTH_LONG).show();

                       favouriteartistlayout.setVisibility(View.GONE);
                       favouriteaddedartistlayout.setVisibility(View.VISIBLE);


                    }

                    @Override
                    public void failure(RetrofitError error) {

                        Toast.makeText(getApplicationContext(), "Your are Not registered user"+error, Toast.LENGTH_LONG).show();
                    }
                });
            }


        });


        favouriteaddedartistlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Api.getClient().getAddorRemoveFavourite(user_id, artist_id, "0", new Callback<Addtowishlistmovie>() {
                    @Override
                    public void success(Addtowishlistmovie addwishmovie, retrofit.client.Response response) {

                        Toast.makeText(getApplicationContext(),""+addwishmovie.getMessage(), Toast.LENGTH_LONG).show();

                        favouriteartistlayout.setVisibility(View.VISIBLE);
                        favouriteaddedartistlayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void failure(RetrofitError error) {

                        Toast.makeText(getApplicationContext(), "Your are Not registered user", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Api.getClient().getAddorRemoveFollowing(user_id, artist_id, "1", new Callback<Addtowishlistmovie>() {
                    @Override
                    public void success(Addtowishlistmovie addwishmovie, retrofit.client.Response response) {

                        Toast.makeText(getApplicationContext(),""+addwishmovie.getMessage(), Toast.LENGTH_LONG).show();

                        follow.setVisibility(View.GONE);
                        following.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void failure(RetrofitError error) {

                        Toast.makeText(getApplicationContext(), "Your are Not registered user", Toast.LENGTH_LONG).show();
                    }
                });
            }

        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Api.getClient().getAddorRemoveFollowing(user_id, artist_id, "0", new Callback<Addtowishlistmovie>() {
                    @Override
                    public void success(Addtowishlistmovie addwishmovie, retrofit.client.Response response) {

                        Toast.makeText(getApplicationContext(),""+addwishmovie.getMessage(), Toast.LENGTH_LONG).show();
                        follow.setVisibility(View.VISIBLE);
                        following.setVisibility(View.GONE);

                    }

                    @Override
                    public void failure(RetrofitError error) {

                        Toast.makeText(getApplicationContext(), "Your are Not registered user", Toast.LENGTH_LONG).show();
                    }
                });
            }

        });

    }
}