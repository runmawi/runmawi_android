package com.atbuys.runmawi;

import static com.atbuys.runmawi.UserHomeFragment.audio_id;
import static com.atbuys.runmawi.UserHomeFragment.mediaplayer;

import android.animation.ValueAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit2.Call;


public class HomePageActivitywithFragments extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    private ArrayList<audio> audiolist;
    private ArrayList<audiodetail> movie_detaildata;
    private ArrayList<recomendedaudios> recomendedaudioslist;
    private ArrayList<allaudios> allaudioslist;

    SeekBar seekbar;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();
    private LinearLayout equalizerlayot, wishlistlayout, favouritelayout;
    private ImageView wishlist, wishlistadd, favourite, favouriteadd, cross, songimage;
    BottomSheetDialog dialog;
    BottomNavigationView navigation;


    TextView tx1, tx2, audio_title, audio_description;
    LinearLayout playerconrollayout;

    ImageView buttonpre, buttonnext, buttonpause1, buttonrepeat, buttonShuffle, buttonStart1, repeatonce, repeatmany, greenshuffle, buttonStart, downarrow;
    ImageView playbutton1, pausebutton1;

    ImageView playhome, pausehome, favhome, queuehome;
    SharedPreferences prefs;
    SeekBar seekBarhome;
    private ArrayList<user_details> user_detailsdata;
    static DrawerLayout drawer_layout;
    String user_id, user_role;
    Runnable test;
    String x, nexturl, preurl;
    NotificationManager notificationManager;
    List<Track> tracks;
    int position = -1;
    boolean isPlaying = false;
    Toast toast;

    boolean doubleBackToExitPressedOnce = false;
    LinearLayout playerviewlayout;


    final Fragment homeFragment = new HomeFragment();
    final Fragment gridFragment = new DashboardFragment();
    final Fragment seriesFragment = new SeriesFragment();
    final Fragment circleFragment = new LatestvideosPageFragment();
    final Fragment userprofilefragment = new UserprofilFragment();
    final FragmentManager fm = getSupportFragmentManager();
    public static Fragment active;

    public static TextView audio_title_layout;
    public static TextView audio_desc_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_home_fragment);

        active = homeFragment;
        prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);
        audio_id = prefs.getString(sharedpreferences.audioid, null);


        movie_detaildata = new ArrayList<audiodetail>();
        recomendedaudioslist = new ArrayList<recomendedaudios>();
        allaudioslist = new ArrayList<allaudios>();

        popluateTracks();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
            registerReceiver(broadcastReceiver, new IntentFilter("TRACKS_TRACKS"), Context.RECEIVER_NOT_EXPORTED);
            //    startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
        }


        audiolist = new ArrayList<audio>();

        playerconrollayout = (LinearLayout) findViewById(R.id.playerconrollayout);
        playhome = (ImageView) findViewById(R.id.playhome);
        pausehome = (ImageView) findViewById(R.id.pausehome);
        favhome = (ImageView) findViewById(R.id.favhome);
        queuehome = (ImageView) findViewById(R.id.queuehome);
        audio_title_layout = (TextView) findViewById(R.id.audio_title_layout);
        audio_desc_layout = (TextView) findViewById(R.id.audio_desc_layout);
        playerviewlayout = (LinearLayout) findViewById(R.id.playerviewlayout);


        //loading the default fragment
        //   loadFragment(new HomeFragment());

        fm.beginTransaction().add(R.id.fragment_container, seriesFragment, "5").hide(seriesFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, userprofilefragment, "4").hide(userprofilefragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, circleFragment, "3").hide(circleFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, gridFragment, "2").hide(gridFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, homeFragment, "1").commit();

        //getting bottom navigation view and attaching the listener
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        mediaplayer = new MediaPlayer();

       /* final Handler handler1 = new Handler();
        final Runnable Update2= new Runnable() {
            public void run() {
                if(mediaplayer.isPlaying())
                {
                    audio_id = prefs.getString(sharedpreferences.audioid,"");

                    playhome.setVisibility(View.GONE);
                    pausehome.setVisibility(View.VISIBLE);
                    playerviewlayout.setVisibility(View.VISIBLE);



                    if(user_id == null)

                    {
                        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail("", audio_id);
                        res.enqueue(new retrofit2.Callback<JSONResponse>() {
                            @Override
                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                                //  Toast.makeText(getApplicationContext(),""+audio_id+"\n"+response.body().toString(),Toast.LENGTH_LONG).show();
                                JSONResponse jsonResponse = response.body();

                                movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));

                                if (jsonResponse.getAudiodetail().length == 0)
                                {

                                }

                                else
                                {
                                    audio_title_layout.setText(movie_detaildata.get(0).getTitle());
                                    audio_desc_layout.setText(movie_detaildata.get(0).getDescription());

                                }

                            }

                            @Override
                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                Log.d("Error", t.getMessage());
                            }
                        });


                    }

                    else
                    {
                        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, audio_id);
                        res.enqueue(new retrofit2.Callback<JSONResponse>() {
                            @Override
                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                                //  Toast.makeText(getApplicationContext(),""+audio_id+"\n"+response.body().toString(),Toast.LENGTH_LONG).show();
                                JSONResponse jsonResponse = response.body();
                                movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));

                                if (jsonResponse.getAudiodetail().length == 0)
                                {

                                }

                                else
                                {
                                    audio_title_layout.setText(movie_detaildata.get(0).getTitle());
                                    audio_desc_layout.setText(movie_detaildata.get(0).getDuration());

                                }

                            }

                            @Override
                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                Log.d("Error", t.getMessage());
                            }
                        });

                    }


                }

                else
                {
                    playhome.setVisibility(View.VISIBLE);
                    pausehome.setVisibility(View.GONE);

                }

            }

        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler1.post(Update2);
            }
        }, 100, 100);*/

        final Handler handler1 = new Handler();
        final Runnable Update2 = new Runnable() {
            public void run() {

                if (mediaplayer.isPlaying()) {

                    onTrackPlay();
                    playhome.setVisibility(View.GONE);
                    pausehome.setVisibility(View.VISIBLE);
                    playerviewlayout.setVisibility(View.GONE);


                }
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler1.post(Update2);
            }
        }, 1000, 1000);


        playhome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mediaplayer.isPlaying()) {
                    onTrackPause();
                } else {
                    onTrackPlay();
                }
                pausehome.setVisibility(View.VISIBLE);
                playhome.setVisibility(View.GONE);

                mediaplayer.start();

            }
        });

        pausehome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mediaplayer.isPlaying()) {
                    onTrackPause();
                } else {
                    onTrackPlay();
                }
                pausehome.setVisibility(View.GONE);
                playhome.setVisibility(View.VISIBLE);
                mediaplayer.pause();


            }
        });




     /*   seekBarhome.setMax((int) finalTime);


        Runnable mUpdateSeekbar = new Runnable() {
            @Override
            public void run() {
                seekBarhome.setProgress(mediaplayer.getCurrentPosition());
                myHandler.postDelayed(this, 50);
            }
        };


        myHandler.postDelayed(mUpdateSeekbar, 0);

        long xxy = mediaplayer.getCurrentPosition();

        seekBarhome.setProgress((int) xxy);
        myHandler.postDelayed(UpdateSongTime, 1000);


        seekBarhome.setClickable(true);
        seekBarhome.setFocusable(true);
        seekBarhome.setEnabled(true);

        seekBarhome.getThumb().mutate().setAlpha(0);*/

        playerconrollayout.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {

                                                      final Handler handler1 = new Handler();
                                                      final Runnable Update = new Runnable() {
                                                          public void run() {


                                                              if (mediaplayer.isPlaying()) {
                                                                  playbutton1.setVisibility(View.GONE);
                                                                  pausebutton1.setVisibility(View.VISIBLE);

                                                              } else {
                                                                  playbutton1.setVisibility(View.VISIBLE);
                                                                  pausebutton1.setVisibility(View.GONE);
                                                              }


                                                          }
                                                      };
                                                      Timer swipeTimer = new Timer();
                                                      swipeTimer.schedule(new TimerTask() {
                                                          @Override
                                                          public void run() {
                                                              handler1.post(Update);
                                                          }
                                                      }, 1000, 1000);


                                                      View view1 = getLayoutInflater().inflate(R.layout.activity_player_page_audio, null);
                                                      seekbar = (SeekBar) view1.findViewById(R.id.seekBar);
                                                      tx1 = (TextView) view1.findViewById(R.id.first);
                                                      tx2 = (TextView) view1.findViewById(R.id.last);
                                                      audio_title = (TextView) view1.findViewById(R.id.Audio_title);
                                                      audio_description = (TextView) view1.findViewById(R.id.audio_description);
                                                      favouritelayout = (LinearLayout) view1.findViewById(R.id.favoritelayout);
                                                      wishlistlayout = (LinearLayout) view1.findViewById(R.id.wishlistlayout);
                                                      wishlist = (ImageView) view1.findViewById(R.id.wishlist);
                                                      wishlistadd = (ImageView) view1.findViewById(R.id.wishlisted);
                                                      favourite = (ImageView) view1.findViewById(R.id.favourite);
                                                      favouriteadd = (ImageView) view1.findViewById(R.id.favoriteadd);
                                                      cross = (ImageView) view1.findViewById(R.id.cross);
                                                      songimage = (ImageView) view1.findViewById(R.id.songimage);


                                                      cross.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View v) {
                                                              mediaplayer.stop();
                                                              playerviewlayout.setVisibility(View.GONE);
                                                              dialog.cancel();


                                                          }
                                                      });


                                                      equalizerlayot = (LinearLayout) view1.findViewById(R.id.equalizer_layout);

                                                      view1.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View v) {
                                                              Intent in = new Intent(getApplicationContext(), MediaPlayerPageActivity.class);
                                                              in.putExtra("id", audio_id);
                                                              startActivity(in);
                                                          }
                                                      });


                                                      equalizerlayot.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View v) {
                                                              Intent intent = new Intent();
                                                              intent.setAction("android.media.action.DISPLAY_AUDIO_EFFECT_CONTROL_PANEL");
                                                              startActivityForResult(intent, 1);
                                                          }
                                                      });


 /*               final Handler handler22 = new Handler();
                final Runnable Update22 = new Runnable() {
                    public void run() {

                        Toast.makeText(getApplicationContext(),"cmd",Toast.LENGTH_LONG).show();*/

                                                      Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, audio_id);
                                                      res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                          @Override
                                                          public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                              JSONResponse jsonResponse = response.body();
                                                              movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));


                                                              if (jsonResponse.getAudiodetail().length == 0) {

                                                              } else {
                                                                  audio_title.setText(movie_detaildata.get(0).getTitle());
                                                                  audio_description.setText(movie_detaildata.get(0).getDescription());
                                                                  audio_title_layout.setText(movie_detaildata.get(0).getTitle());
                                                                  audio_desc_layout.setText(movie_detaildata.get(0).getDescription());
                                                                  Picasso.get().load(movie_detaildata.get(0).getImage()).into(songimage);
                                                              }

                                                              if (jsonResponse.getWishlist().equalsIgnoreCase("true")) {

                                                                  wishlistadd.setVisibility(View.VISIBLE);
                                                                  wishlist.setVisibility(View.GONE);
                                                              } else {

                                                                  wishlistadd.setVisibility(View.GONE);
                                                                  wishlist.setVisibility(View.VISIBLE);

                                                              }

                                                              if (jsonResponse.getFavorite().equalsIgnoreCase("true")) {

                                                                  favouriteadd.setVisibility(View.VISIBLE);
                                                                  favourite.setVisibility(View.GONE);
                                                              } else {
                                                                  favouriteadd.setVisibility(View.GONE);
                                                                  favourite.setVisibility(View.VISIBLE);

                                                              }
                                                          }

                                                          @Override
                                                          public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                              Log.d("Error", t.getMessage());
                                                          }
                                                      });

                                                      //    }
             /*   };
                Timer swipeTimerrr = new Timer();
                swipeTimerrr.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler22.post(Update22);
                    }
                }, 1000, 1000);*/


                                                      dialog = new BottomSheetDialog(HomePageActivitywithFragments.this);
                                                      dialog.setContentView(view1);
                                                      dialog.show();

                                                      finalTime = mediaplayer.getDuration();
                                                      startTime = mediaplayer.getCurrentPosition();

                                                      tx2.setText(String.format("%02d : %02d ",
                                                              TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                                              TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                                                      TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                                              finalTime)))
                                                      );


                                                      seekbar.setMax((int) finalTime);

                                                      Runnable mUpdateSeekbar = new Runnable() {
                                                          @Override
                                                          public void run() {
                                                              seekbar.setProgress(mediaplayer.getCurrentPosition());
                                                              myHandler.postDelayed(this, 50);
                                                          }
                                                      };


                                                      myHandler.postDelayed(mUpdateSeekbar, 0);

                                                      long xxy = mediaplayer.getCurrentPosition();

                                                      seekbar.setProgress((int) xxy);
                                                      myHandler.postDelayed(UpdateSongTime, 1000);


                                                      seekbar.setClickable(true);
                                                      seekbar.setFocusable(true);
                                                      seekbar.setEnabled(true);


                                                      seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                                          int progressChangedValue = 0;

                                                          public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                                              progressChangedValue = progress;
                                                          }

                                                          public void onStartTrackingTouch(SeekBar seekBar) {

                                                          }

                                                          public void onStopTrackingTouch(SeekBar seekBar) {

                                                              mediaplayer.seekTo(progressChangedValue);
                                                          }
                                                      });


                                                      final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
                                                      animator.setRepeatCount(ValueAnimator.INFINITE);
                                                      animator.setInterpolator(new LinearInterpolator());
                                                      animator.setDuration(9000L);
                                                      animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                                          @Override
                                                          public void onAnimationUpdate(ValueAnimator animation) {
                                                              final float progress = (float) animation.getAnimatedValue();
                                                              final float width = audio_description.getWidth();
                                                              final float translationX = width * progress;
                                                              audio_description.setTranslationX(translationX);

                                                          }
                                                      });
                                                      animator.start();


                                                      buttonpre = (ImageView) view1.findViewById(R.id.previous);
                                                      buttonnext = (ImageView) view1.findViewById(R.id.next);
                                                      repeatonce = view1.findViewById(R.id.repeatonce);
                                                      repeatmany = view1.findViewById(R.id.repeatmany);
                                                      buttonShuffle = (ImageView) view1.findViewById(R.id.shuffle);
                                                      greenshuffle = (ImageView) view1.findViewById(R.id.shufflevisible);
                                                      buttonrepeat = (ImageView) view1.findViewById(R.id.repeat);


                                                      playbutton1 = view1.findViewById(R.id.play);
                                                      pausebutton1 = view1.findViewById(R.id.pause);

                                                      playbutton1.setOnClickListener(new View.OnClickListener() {

                                                          @Override
                                                          public void onClick(View v) {

                                                              pausebutton1.setVisibility(View.VISIBLE);
                                                              playbutton1.setVisibility(View.GONE);

                                                              mediaplayer.start();


                                                          }
                                                      });

                                                      pausebutton1.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View view) {


                                                              pausebutton1.setVisibility(View.GONE);
                                                              playbutton1.setVisibility(View.VISIBLE);
                                                              mediaplayer.pause();

                                                          }
                                                      });

                                                      buttonnext.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View view) {


                                                              Call<JSONResponse> res = ApiClient.getInstance1().getApi().getNextAudioDetail(user_id, audio_id);
                                                              res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                                  @Override
                                                                  public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                                      JSONResponse jsonResponse = response.body();

                                                                      if (jsonResponse.getStatus().equalsIgnoreCase("true")) {
                                                                          audiolist = new ArrayList<>(Arrays.asList(jsonResponse.getAudio()));


                                                                          SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                                                          editor.putBoolean(sharedpreferences.login, true);
                                                                          editor.putString(sharedpreferences.audioid, audiolist.get(0).getId());
                                                                          editor.apply();
                                                                          editor.commit();

                                                                          audio_id = prefs.getString(sharedpreferences.audioid, "");

                                                                          nexturl = audiolist.get(0).getMp3_url();


                                                                          AudioDetails(audio_id);
                                                                          mediaplayer.reset();

                                                                          mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                                                          try {
                                                                              mediaplayer.setDataSource(nexturl);
                                                                          } catch (IOException e) {
                                                                              e.printStackTrace();
                                                                          }
                                                                          try {
                                                                              mediaplayer.prepare();
                                                                          } catch (IOException e) {
                                                                              e.printStackTrace();
                                                                          }


                                                                          finalTime = mediaplayer.getDuration();
                                                                          startTime = mediaplayer.getCurrentPosition();


                                                                          seekbar.setMax((int) finalTime);

                                                                          tx2.setText(String.format("%02d : %02d ",
                                                                                  TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                                                                  TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                                                                          TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                                                                  finalTime)))
                                                                          );


                                                                          seekbar.setProgress((int) startTime);
                                                                          myHandler.postDelayed(UpdateSongTime, 1000);

                                                                          mediaplayer.start();
                                                                          //onTrackNext();

                                                                      } else {

                                                                          Toast.makeText(getApplicationContext(), "" + jsonResponse.getMessage(), Toast.LENGTH_LONG).show();
                                                                          mediaplayer.stop();

                                                                          dialog.cancel();
                                                                      }
                                                                  }

                                                                  @Override
                                                                  public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                                      Log.d("Error", t.getMessage());
                                                                  }
                                                              });


                                                          }
                                                      });


                                                      buttonpre.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View view) {


                                                              Call<JSONResponse> res = ApiClient.getInstance1().getApi().getPrevAudioDetail(user_id, audio_id);
                                                              res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                                  @Override
                                                                  public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                                      JSONResponse jsonResponse = response.body();

                                                                      if (jsonResponse.getStatus().equalsIgnoreCase("true")) {


                                                                          audiolist = new ArrayList<>(Arrays.asList(jsonResponse.getAudio()));


                                                                          preurl = audiolist.get(position).getMp3_url();


                                                                          SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                                                          editor.putBoolean(sharedpreferences.login, true);
                                                                          editor.putString(sharedpreferences.audioid, audiolist.get(0).getId());
                                                                          editor.apply();
                                                                          editor.commit();

                                                                          AudioDetails(audiolist.get(0).getId());

                                                                          mediaplayer.reset();
                                                                          mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                                                          try {
                                                                              mediaplayer.setDataSource(preurl);
                                                                          } catch (IOException e) {
                                                                              e.printStackTrace();
                                                                          }
                                                                          try {
                                                                              mediaplayer.prepare();
                                                                          } catch (IOException e) {
                                                                              e.printStackTrace();
                                                                          }


                                                                          finalTime = mediaplayer.getDuration();
                                                                          startTime = mediaplayer.getCurrentPosition();


                                                                          seekbar.setMax((int) finalTime);

                                                                          tx2.setText(String.format("%02d : %02d ",
                                                                                  TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                                                                  TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                                                                          TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                                                                  finalTime)))
                                                                          );


                                                                          seekbar.setProgress((int) startTime);
                                                                          myHandler.postDelayed(UpdateSongTime, 1000);

                                                                          mediaplayer.start();
                                                                          //  onTrackPrevious();
                                                                      } else {
                                                                          Toast.makeText(getApplicationContext(), "" + jsonResponse.getMessage(), Toast.LENGTH_LONG).show();
                                                                          mediaplayer.stop();
                                                                          dialog.cancel();

                                                                      }
                                                                  }


                                                                  @Override
                                                                  public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                                      Log.d("Error", t.getMessage());
                                                                  }
                                                              });


                                                          }
                                                      });

                                                      buttonrepeat.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View view) {


                                                              repeatonce.setVisibility(View.VISIBLE);
                                                              buttonrepeat.setVisibility(View.GONE);

                                                              mediaplayer.setLooping(true);


                                                          }
                                                      });


                                                      repeatonce.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View view) {


                                                              repeatonce.setVisibility(View.GONE);
                                                              buttonrepeat.setVisibility(View.VISIBLE);

                                                              mediaplayer.setLooping(false);
                                                          }
                                                      });

                                                      greenshuffle.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View view) {


                                                              greenshuffle.setVisibility(View.GONE);
                                                              buttonShuffle.setVisibility(View.VISIBLE);

                                                              mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                                  public void onCompletion(MediaPlayer mp) {

                                                                      mediaplayer.stop();
                                                                      mediaplayer.reset();
                                                                      tx1.setText("00:00");


                                                                  }
                                                              });

                                                          }
                                                      });


                                                      buttonShuffle.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View view) {


                                                              greenshuffle.setVisibility(View.VISIBLE);
                                                              buttonShuffle.setVisibility(View.GONE);


                                                              mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                                  public void onCompletion(final MediaPlayer theMediaPlayer) {


                                                                      Call<JSONResponse> res = ApiClient.getInstance1().getApi().getNextAudioDetail(user_id, audio_id);
                                                                      res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                                          @Override
                                                                          public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                                              JSONResponse jsonResponse = response.body();

                                                                              if (jsonResponse.getStatus().equalsIgnoreCase("true")) {
                                                                                  audiolist = new ArrayList<>(Arrays.asList(jsonResponse.getAudio()));


                                                                                  SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                                                                  editor.putBoolean(sharedpreferences.login, true);
                                                                                  editor.putString(sharedpreferences.audioid, audiolist.get(0).getId());
                                                                                  editor.apply();
                                                                                  editor.commit();


                                                                                  AudioDetails(audiolist.get(0).getId());

                                                                                  mediaplayer.reset();
                                                                                  mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                                                                  try {
                                                                                      mediaplayer.setDataSource(audiolist.get(0).getMp3_url());
                                                                                  } catch (IOException e) {
                                                                                      e.printStackTrace();
                                                                                  }
                                                                                  try {
                                                                                      mediaplayer.prepare();
                                                                                  } catch (IOException e) {
                                                                                      e.printStackTrace();
                                                                                  }


                                                                                  finalTime = mediaplayer.getDuration();
                                                                                  startTime = mediaplayer.getCurrentPosition();


                                                                                  seekbar.setMax((int) finalTime);

                                                                                  tx2.setText(String.format("%02d : %02d ",
                                                                                          TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                                                                          TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                                                                                  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                                                                          finalTime)))
                                                                                  );


                                                                                  seekbar.setProgress((int) startTime);
                                                                                  myHandler.postDelayed(UpdateSongTime, 1000);

                                                                                  mediaplayer.start();
                                                                              } else {
                                                                                  Toast.makeText(getApplicationContext(), "" + jsonResponse.getMessage(), Toast.LENGTH_LONG).show();
                                                                                  mediaplayer.stop();
                                                                                  dialog.cancel();
                                                                              }
                                                                          }

                                                                          @Override
                                                                          public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                                              Log.d("Error", t.getMessage());
                                                                          }
                                                                      });


                                                                  }


                                                              });


                                                          }
                                                      });

                                                      favouritelayout.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View view) {

                                                              {

                                                                  Api.getClient().getAddFavouriteAudio(user_id, audio_id, new Callback<Addtowishlistmovie>() {

                                                                      @Override
                                                                      public void success(Addtowishlistmovie addwishmovie, retrofit.client.Response response) {

                                                                          addwishmovie = addwishmovie;
                                                                          if (addwishmovie.getStatus().equalsIgnoreCase("true")) {

                                                                              favouriteadd.setVisibility(View.VISIBLE);
                                                                              favourite.setVisibility(View.GONE);

                                                                              Toast.makeText(getApplicationContext(), "" + addwishmovie.getMessage(), Toast.LENGTH_LONG).show();
                                                                          } else if (addwishmovie.getStatus().equalsIgnoreCase("false")) {

                                                                              favouriteadd.setVisibility(View.GONE);
                                                                              favourite.setVisibility(View.VISIBLE);

                                                                              Toast.makeText(getApplicationContext(), "" + addwishmovie.getMessage(), Toast.LENGTH_LONG).show();
                                                                          } else {
                                                                              Toast.makeText(getApplicationContext(), "You are not registered user", Toast.LENGTH_LONG).show();

                                                                          }

                                                                      }

                                                                      @Override
                                                                      public void failure(RetrofitError error) {

                                                                          Toast.makeText(getApplicationContext(), "check your internet connection", Toast.LENGTH_LONG).show();
                                                                      }
                                                                  });
                                                              }


                                                          }
                                                      });


                                                      mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                          public void onCompletion(final MediaPlayer theMediaPlayer) {


                                                              Call<JSONResponse> res = ApiClient.getInstance1().getApi().getNextAudioDetail(user_id, audio_id);
                                                              res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                                  @Override
                                                                  public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                                      JSONResponse jsonResponse = response.body();

                                                                      if (jsonResponse.getStatus().equalsIgnoreCase("true")) {
                                                                          audiolist = new ArrayList<>(Arrays.asList(jsonResponse.getAudio()));


                                                                          SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                                                          editor.putBoolean(sharedpreferences.login, true);
                                                                          editor.putString(sharedpreferences.audioid, audiolist.get(0).getId());
                                                                          editor.apply();
                                                                          editor.commit();


                                                                          AudioDetails(audiolist.get(0).getId());

                                                                          mediaplayer.reset();
                                                                          mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                                                          try {
                                                                              mediaplayer.setDataSource(audiolist.get(0).getMp3_url());
                                                                          } catch (IOException e) {
                                                                              e.printStackTrace();
                                                                          }
                                                                          try {
                                                                              mediaplayer.prepare();
                                                                          } catch (IOException e) {
                                                                              e.printStackTrace();
                                                                          }


                                                                          finalTime = mediaplayer.getDuration();
                                                                          startTime = mediaplayer.getCurrentPosition();


                                                                          seekbar.setMax((int) finalTime);

                                                                          tx2.setText(String.format("%02d : %02d ",
                                                                                  TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                                                                  TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                                                                          TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                                                                  finalTime)))
                                                                          );


                                                                          seekbar.setProgress((int) startTime);
                                                                          myHandler.postDelayed(UpdateSongTime, 1000);

                                                                          mediaplayer.start();
                                                                      } else {
                                                                          Toast.makeText(getApplicationContext(), "" + jsonResponse.getMessage(), Toast.LENGTH_LONG).show();
                                                                          mediaplayer.stop();
                                                                          dialog.cancel();
                                                                      }
                                                                  }

                                                                  @Override
                                                                  public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                                      Log.d("Error", t.getMessage());
                                                                  }
                                                              });

                                                          }
                                                      });

                                                      wishlistlayout.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View view) {

                                                              {

                                                                  Api.getClient().getAddWishlistAudio(user_id, audio_id, new Callback<Addtowishlistmovie>() {

                                                                      @Override
                                                                      public void success(Addtowishlistmovie addwishmovie, retrofit.client.Response response) {

                                                                          addwishmovie = addwishmovie;
                                                                          if (addwishmovie.getStatus().equalsIgnoreCase("true")) {

                                                                              wishlistadd.setVisibility(View.VISIBLE);
                                                                              wishlist.setVisibility(View.GONE);

                                                                              Toast.makeText(getApplicationContext(), "" + addwishmovie.getMessage(), Toast.LENGTH_LONG).show();
                                                                          } else if (addwishmovie.getStatus().equalsIgnoreCase("false")) {

                                                                              wishlistadd.setVisibility(View.GONE);
                                                                              wishlist.setVisibility(View.VISIBLE);

                                                                              Toast.makeText(getApplicationContext(), "" + addwishmovie.getMessage(), Toast.LENGTH_LONG).show();
                                                                          } else {
                                                                              Toast.makeText(getApplicationContext(), "You are not registered user", Toast.LENGTH_LONG).show();

                                                                          }

                                                                      }

                                                                      @Override
                                                                      public void failure(RetrofitError error) {
                                                                          Toast.makeText(getApplicationContext(), "check your internet connection", Toast.LENGTH_LONG).show();
                                                                      }
                                                                  });
                                                              }


                                                          }
                                                      });

                                                  }
                                              }
        );
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                //   fragment = new HomeFragment();

                fm.beginTransaction().hide(active).show(homeFragment).commit();
                active = homeFragment;
                return true;

            case R.id.navigation_search:
               /* fragment = new DashboardFragment();
                break;*/

                fm.beginTransaction().hide(active).show(gridFragment).commit();
                active = gridFragment;
                return true;

            case R.id.navigation_comming:

              /*  fragment = new UserHomeFragment();
                break;
*/

                fm.beginTransaction().hide(active).show(circleFragment).commit();
                active = circleFragment;
                return true;


            case R.id.series:

              /*  fragment = new UserHomeFragment();
                break;
*/

                fm.beginTransaction().hide(active).show(seriesFragment).commit();
                active = seriesFragment;
                return true;








          /*  case R.id.navigation_download:
                fragment = new ProfileFragment();
                break;*/

            case R.id.navigation_more:

                if (user_id == null) {
                    Intent in = new Intent(getApplicationContext(), MailandOtpLoginActivity.class);
                    startActivity(in);
                } else {
                    fm.beginTransaction().hide(active).show(userprofilefragment).commit();
                    active = userprofilefragment;
                    return true;
                }

        }
        return false;
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)

                    .commit();
            return true;
        }
        return false;
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CreateNotification.CHANNEL_ID,
                    "Bauu", NotificationManager.IMPORTANCE_HIGH);


            notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {

                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void popluateTracks() {

        tracks = new ArrayList<>();


        if (audio_id != null) {

            Call<JSONResponse> callser = ApiClient.getInstance1().getApi().getAllAudios();
            callser.enqueue(new retrofit2.Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                    JSONResponse jsonResponse = response.body();

                    if (jsonResponse.getAllaudios().length == 0) {


                    } else {


                        allaudioslist = new ArrayList<>(Arrays.asList(jsonResponse.getAllaudios()));

                        for (int i = 0; i < allaudioslist.size(); i++) {
                            tracks.add(new Track(allaudioslist.get(i).getTitle(), "", allaudioslist.get(i).getImage_url(), allaudioslist.get(i).getMp3_url(), allaudioslist.get(i).getId()));

                        }


                    }
                }

                @Override
                public void onFailure(Call<JSONResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });

        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getExtras().getString("actionname");

            switch (action) {
                case CreateNotification.ACTION_PREVIUOS:
                    onTrackPrevious();
                    break;
                case CreateNotification.ACTION_PLAY:
                    if (mediaplayer.isPlaying()) {
                        onTrackPause();
                    } else {
                        onTrackPlay();
                    }
                    break;
                case CreateNotification.ACTION_NEXT:
                    onTrackNext();
                    break;
            }
        }
    };

    public void onTrackPrevious() {

        position--;
       /* CreateNotification.createNotification(HomePageActivitywithFragments.this, tracks.get(position),
                R.drawable.play, position, tracks.size()-1);*/
        // title.setText(tracks.get(position).getTitle());
        SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
        editor.putBoolean(sharedpreferences.login, true);
        editor.putString(sharedpreferences.audioid, tracks.get(position).getId());
        editor.apply();
        editor.commit();

        audio_id = prefs.getString(sharedpreferences.audioid, "");
        mediaplayer.reset();
        mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaplayer.setDataSource(tracks.get(position).getUrl());
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

    public void onTrackPlay() {

        for (int i = 0; i < tracks.size(); i++) {
            if (tracks.get(i).getId().equals(audio_id)) {
                position = i;
                /*CreateNotification.createNotification(HomePageActivitywithFragments.this, tracks.get(position),
                        R.drawable.pause, position, tracks.size() - 1);*/
                //   play.setImageResource(R.drawable.ic_pause_black_24dp);
                //  title.setText(tracks.get(position).getTitle());

                mediaplayer.start();
                isPlaying = true;
                break;  // uncomment to get the first instance
            }
        }
        // position = Integer.parseInt(tracks.get(position).getId());

    }


    public void onTrackPause() {

        for (int i = 0; i < tracks.size(); i++) {
            if (tracks.get(i).getId().equals(audio_id)) {
                position = i;

                /*CreateNotification.createNotification(HomePageActivitywithFragments.this, tracks.get(position),
                        R.drawable.play, position, tracks.size()-1);*/
                //    play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                //   title.setText(tracks.get(position).getTitle());


                mediaplayer.pause();
                isPlaying = false;
                break;  // uncomment to get the first instance
            }
        }


    }


    public void onTrackNext() {

        position++;
        /*CreateNotification.createNotification(HomePageActivitywithFragments.this, tracks.get(position),
                R.drawable.pause, position, tracks.size()-1);*/
        // title.setText(tracks.get(position).getTitle());

        SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
        //  editor.putBoolean(sharedpreferences.login, true);
        editor.putString(sharedpreferences.audioid, tracks.get(position).getId());
        editor.apply();
        editor.commit();

        audio_id = prefs.getString(sharedpreferences.audioid, null);

        mediaplayer.reset();
        mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaplayer.setDataSource(tracks.get(position).getUrl());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.cancelAll();
        }

        unregisterReceiver(broadcastReceiver);
    }


    @Override
    public void onBackPressed() {

        if (navigation.getSelectedItemId() == R.id.navigation_home) {

            int count = getSupportFragmentManager().getBackStackEntryCount();

            if (count == 0) {
                //additional code
                if (doubleBackToExitPressedOnce) {

                    toast.cancel();
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);

                } else {
                    toast = Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT);
                    toast.show();
                }

                this.doubleBackToExitPressedOnce = true;
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            } else {

                getSupportFragmentManager().popBackStack();
            }

        } else {

            navigation.setSelectedItemId(R.id.navigation_home);


        }
    }


    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaplayer.getCurrentPosition();
            tx1.setText(String.format("%02d : %02d ",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int) startTime);
            myHandler.postDelayed(this, 100);
        }
    };


    public void AudioDetails(String id) {

        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, id);
        res.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                JSONResponse jsonResponse = response.body();
                movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));

                if (jsonResponse.getAudiodetail().length == 0) {

                } else {
                    audio_title_layout.setText(movie_detaildata.get(0).getTitle());
                    audio_desc_layout.setText(movie_detaildata.get(0).getDuration());
                    audio_title.setText(movie_detaildata.get(0).getTitle());
                    audio_description.setText(movie_detaildata.get(0).getDuration());
                    Picasso.get().load(movie_detaildata.get(0).getImage()).into(songimage);
                }

                if (jsonResponse.getWishlist().equalsIgnoreCase("true")) {

                    wishlistadd.setVisibility(View.VISIBLE);
                    wishlist.setVisibility(View.GONE);
                } else {
                    wishlistadd.setVisibility(View.GONE);
                    wishlist.setVisibility(View.VISIBLE);

                }

                if (jsonResponse.getFavorite().equalsIgnoreCase("true")) {
                    favouriteadd.setVisibility(View.VISIBLE);
                    favourite.setVisibility(View.GONE);
                } else {
                    favouriteadd.setVisibility(View.GONE);
                    favourite.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

}

