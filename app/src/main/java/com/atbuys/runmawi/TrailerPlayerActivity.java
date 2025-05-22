package com.atbuys.runmawi;

import static com.atbuys.runmawi.Connectivity.getNetworkInfo;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DefaultDataSourceFactory;
import androidx.media3.exoplayer.ExoPlaybackException;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.dash.DashMediaSource;
import androidx.media3.exoplayer.hls.HlsMediaSource;
import androidx.media3.exoplayer.offline.DownloadHelper;
import androidx.media3.exoplayer.offline.DownloadManager;
import androidx.media3.exoplayer.smoothstreaming.SsMediaSource;
import androidx.media3.exoplayer.source.BehindLiveWindowException;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.MergingMediaSource;
import androidx.media3.exoplayer.source.ProgressiveMediaSource;
import androidx.media3.exoplayer.source.TrackGroupArray;
import androidx.media3.exoplayer.source.ads.AdsLoader;
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector;
import androidx.media3.exoplayer.trackselection.MappingTrackSelector;
import androidx.media3.exoplayer.trackselection.TrackSelectionArray;
import androidx.media3.exoplayer.upstream.BandwidthMeter;
import androidx.media3.exoplayer.util.EventLogger;
import androidx.media3.extractor.DefaultExtractorsFactory;
import androidx.media3.extractor.ExtractorsFactory;
import androidx.media3.ui.AspectRatioFrameLayout;
import androidx.media3.ui.DefaultTimeBar;
import androidx.media3.ui.PlayerControlView;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.atbuys.runmawi.Config.Config;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit2.Call;

/**
 * Created by Mayur Solanki (mayursolanki120@gmail.com) on 22/03/19, 1:20 PM.
 */
@UnstableApi
public class TrailerPlayerActivity extends AppCompatActivity implements View.OnClickListener, PlayerControlView.VisibilityListener {

    private static final int playerHeight = 250;
    ProgressDialog pDialog;
    protected static final CookieManager DEFAULT_COOKIE_MANAGER;
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 2000;
    private static final int UI_ANIMATION_DELAY = 300;
    // Saved instance state keys.
    private static final String KEY_TRACK_SELECTOR_PARAMETERS = "track_selector_parameters";
    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "position";
    private static final String KEY_AUTO_PLAY = "auto_play";
    MediaSource mediaSource;

    skiptime skiptime;
    long skipduration, introduration;

    private ArrayList<settings> settings;

    BandwidthMeter bandwidthMeter;


    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            .clientId(Config.PAYPAL_CLIENT_ID);

    static {
        DEFAULT_COOKIE_MANAGER = new CookieManager();
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }

    private final Handler mHideHandler = new Handler();
    private final Runnable mShowRunnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }

        }
    };


    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    int tapCount = 1;
    LinearLayout llParentContainer;
    Boolean isScreenLandscape = false;

    //    TrackKey trackKeyDownload;
    DefaultTrackSelector.Parameters qualityParams;
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    private PlayerView playerView;
    private DataSource.Factory dataSourceFactory;

//    private SimpleExoPlayer player;

    private ExoPlayer exoPlayer;
    private MediaItem mediaItem;

    private DefaultTrackSelector trackSelector;
    private boolean isShowingTrackSelectionDialog;
    private DefaultTrackSelector.Parameters trackSelectorParameters;
    private TrackGroupArray lastSeenTrackGroupArray;
    private TextView tvPlaybackSpeed, tvPlaybackSpeedSymbol;
    private boolean startAutoPlay;
    private int startWindow;
    // Fields used only for ad playback. The ads loader is loaded via reflection.
    private long startPosition;
    private AdsLoader adsLoader;
    private Uri loadedAdTagUri;
    private FrameLayout frameLayoutMain;
    private ImageView imgBwd;
    private ImageView exoPlay,exoreply;
    private ImageView exoPause;
    private ImageView imgFwd, imgBackPlayer, skipintro;
    private TextView tvPlayerCurrentTime;
    private DefaultTimeBar exoTimebar;
    private ProgressBar exoProgressbar;
    private TextView tvPlayerEndTime;
    private ImageView imgSetting;
    private ImageView imgFullScreenEnterExit,resize_icon;
    long current_time_update;

    private ImageView cast;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private DownloadManager downloadManager;
    private DownloadHelper myDownloadHelper;
    private LinearLayout llDownloadContainer;
    private LinearLayout llDownloadVideo;
    private ImageView imgDownloadState;
    private TextView tvDownloadState;
    private ProgressBar progressBarPercentage;
    private String videoId, videoUrl, videoName, source_type;
    private long videoDurationInSeconds;
    private Runnable runnableCode;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private Handler handler;
    private AccessibilityService context;
    Button rent, subscribe;
    private static final int PAYPAL_REQUEST_CODE = 7777;


    LinearLayout watchlist, watchlater, hidelayout1;
    ImageView watchlistimg, watchlistaddedimg, watchlaterimg, watchlateraddedimg;
    TextView videotext;
    LinearLayout share;
    TextView videotitle1, language1, genre2, duration, year;
    private String shareurl, user_id, theme;


    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<movies> moviesdata;
    private ArrayList<channelrecomended> recomendeddatalist;
    private ArrayList<genre> genredata;
    private ArrayList<videodetail> movie_detaildata;
    private ArrayList<livedetail> live_detaildata;
    private ArrayList<episode> episode_detaildata;
    private ArrayList<season> season;
    showwishlist showwish;
    private ArrayList<movieresolution> movieresolutiondata;

    public String urlll = "";
    String type = "channel";
    TextView norecommanded;
    String amount = "";


    RecyclerView thismaylikerecycler;
    ExtractorsFactory extractorsFactory;
    ThismaylikeAdopter thismaylikeadopter;
    private long position;
    String videomp4url;

    Paypalresponse paypalresponse;
    private ImageView subtitle_off, subtitle_on;

    private static boolean isBehindLiveWindow(ExoPlaybackException e) {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
            return false;
        }
        Throwable cause = e.getSourceException();
        while (cause != null) {
            if (cause instanceof BehindLiveWindowException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    @OptIn(markerClass = UnstableApi.class)
    @SuppressLint("WrongViewCast")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {


            setTheme(R.style.AppTheme);

        } else {


            setTheme(R.style.darktheme);
        }


        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        settings = new ArrayList<settings>();

        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        dataSourceFactory = new DefaultDataSourceFactory(this, this.getPackageName());

        hideStatusBar();

        setContentView(R.layout.activity_trailer_player);

        Intent in = getIntent();
        videoId = in.getStringExtra("id");
        urlll = in.getStringExtra("url");
        source_type = in.getStringExtra("type");


        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);


     /*   Call<JSONResponse> req = ApiClient.getInstance1().getApi().getSettings();
        req.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                JSONResponse jsonResponse = response.body();

                amount=response.body().getPpv_price();

                Toast.makeText(getApplicationContext(),"we"+amount,Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });*/

        if (savedInstanceState != null) {
            trackSelectorParameters = savedInstanceState.getParcelable(KEY_TRACK_SELECTOR_PARAMETERS);
            startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
            startWindow = savedInstanceState.getInt(KEY_WINDOW);
            startPosition = savedInstanceState.getLong(KEY_POSITION);
        } else {
            trackSelectorParameters = new DefaultTrackSelector.ParametersBuilder().build();
            clearStartPosition();
        }


        createView();
        prepareView();


        runnableCode = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnableCode);

        videotext = (TextView) findViewById(R.id.videotext);
        watchlist = (LinearLayout) findViewById(R.id.watchlist);
        watchlater = (LinearLayout) findViewById(R.id.watchlater);
        watchlistimg = (ImageView) findViewById(R.id.watchlistimg);
        watchlistaddedimg = (ImageView) findViewById(R.id.watchlistaddimg);
        watchlaterimg = (ImageView) findViewById(R.id.watchlateradd);
        watchlateraddedimg = (ImageView) findViewById(R.id.watchlateraddedimg);
        share = (LinearLayout) findViewById(R.id.share);
        norecommanded = (TextView) findViewById(R.id.norecommanded);
        rent = (Button) findViewById(R.id.rented);
        subscribe = (Button) findViewById(R.id.subscribe);

        genre2 = (TextView) findViewById(R.id.genre);
        language1 = (TextView) findViewById(R.id.language);
        year = (TextView) findViewById(R.id.movieyear);
        duration = (TextView) findViewById(R.id.videoduration);
        videotitle1 = (TextView) findViewById(R.id.videotitle);

        cast = (ImageView) findViewById(R.id.cast);


        moviesdata = new ArrayList<movies>();
        genredata = new ArrayList<genre>();
        movieresolutiondata = new ArrayList<movieresolution>();
        movie_detaildata = new ArrayList<videodetail>();
        live_detaildata=new ArrayList<>();
        episode_detaildata=new ArrayList<>();
        season=new ArrayList<>();
        recomendeddatalist = new ArrayList<channelrecomended>();

        thismaylikeadopter = new ThismaylikeAdopter(recomendeddatalist, this);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        thismaylikerecycler = (RecyclerView) findViewById(R.id.thismayalsolike);

        thismaylikerecycler.setHasFixedSize(true);
        thismaylikerecycler.setLayoutManager(layoutManager);
        thismaylikerecycler.setAdapter(thismaylikeadopter);

        trackSelector = new DefaultTrackSelector(this);
        exoPlayer = new ExoPlayer.Builder(this)
                .setTrackSelector(trackSelector)
                .build();


        cast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent("android.settings.CAST_SETTINGS"));
            }
        });
        exoreply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                exoPause.setVisibility(View.VISIBLE);
                exoreply.setVisibility(View.GONE);
                exoPlay.setVisibility(View.GONE);
                imgBwd.setVisibility(View.VISIBLE);
                imgFwd.setVisibility(View.VISIBLE);

            }
        });

        exoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exoPause.setVisibility(View.VISIBLE);
                exoreply.setVisibility(View.GONE);
                exoPlay.setVisibility(View.GONE);
                exoPlayer.setPlayWhenReady(true);
                imgBwd.setVisibility(View.VISIBLE);
                imgFwd.setVisibility(View.VISIBLE);

            }
        });

        exoPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                exoPause.setVisibility(View.GONE);
                exoPlay.setVisibility(View.VISIBLE);
                exoPlayer.setPlayWhenReady(false);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "I am watching videos";
                String shareSub = shareurl;
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareSub);
                startActivity(Intent.createChooser(myIntent, "Share using"));


            }
        });

        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (user_id != null) {


                    String[] listItems = {"Continue with Paypal", "Continue With Stripe"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(TrailerPlayerActivity.this);
                    builder.setTitle("Select Payment Method");

                    builder.setItems(listItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            if (which == 0) {


                                processPayment();


                            }
                            if (which == 1) {

                                Intent in = new Intent(getApplicationContext(), PayPerViewPaymentActivity.class);
                                in.putExtra("id", videoId);
                                startActivity(in);


                            }

                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                } else {

                    Intent in = new Intent(getApplicationContext(), SigninActivity.class);
                    startActivity(in);

                }


            }
        });

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user_id != null) {

                    Intent in = new Intent(getApplicationContext(), SubscribeActivity.class);
                    in.putExtra("id", videoId);
                    startActivity(in);

                } else {

                    Intent in = new Intent(getApplicationContext(), SigninActivity.class);
                    startActivity(in);
                }
            }
        });

        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        final String user_role = prefs.getString(sharedpreferences.role, null);

        theme = prefs.getString(sharedpreferences.theme, null);


        if (theme == null) {

        }

        if (theme.equalsIgnoreCase("1")) {


            setTheme(R.style.AppTheme);


        } else {


            setTheme(R.style.darktheme);

        }


        extractorsFactory = new DefaultExtractorsFactory();


        watchlater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                {

                    Api.getClient().getAddwatchlater(user_id, videoId, type, new Callback<Addtowishlistmovie>() {

                        @Override
                        public void success(Addtowishlistmovie addwishmovie, retrofit.client.Response response) {

                            addwishmovie = addwishmovie;
                            if (addwishmovie.getStatus().equalsIgnoreCase("true")) {

                                watchlateraddedimg.setVisibility(View.VISIBLE);
                                watchlaterimg.setVisibility(View.GONE);

                                Toast.makeText(getApplicationContext(), "" + addwishmovie.getMessage(), Toast.LENGTH_LONG).show();
                            } else if (addwishmovie.getStatus().equalsIgnoreCase("false")) {

                                watchlateraddedimg.setVisibility(View.GONE);
                                watchlaterimg.setVisibility(View.VISIBLE);

                                Toast.makeText(getApplicationContext(), "" + addwishmovie.getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "You are not registered user", Toast.LENGTH_LONG).show();

                            }

                        }

                        @Override
                        public void failure(RetrofitError error) {

                            Toast.makeText(getApplicationContext(), "sd", Toast.LENGTH_LONG).show();
                        }
                    });
                }


            }
        });

        watchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                {

                    Api.getClient().getAddWishlistMovie(user_id, videoId, type, new Callback<Addtowishlistmovie>() {

                        @Override
                        public void success(Addtowishlistmovie addwishmovie, retrofit.client.Response response) {

                            addwishmovie = addwishmovie;
                            if (addwishmovie.getStatus().equalsIgnoreCase("true")) {

                                watchlistaddedimg.setVisibility(View.VISIBLE);
                                watchlistimg.setVisibility(View.GONE);

                                Toast.makeText(getApplicationContext(), "" + addwishmovie.getMessage(), Toast.LENGTH_LONG).show();
                            } else if (addwishmovie.getStatus().equalsIgnoreCase("false")) {

                                watchlistaddedimg.setVisibility(View.GONE);
                                watchlistimg.setVisibility(View.VISIBLE);

                                Toast.makeText(getApplicationContext(), "" + addwishmovie.getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "You are not registered user", Toast.LENGTH_LONG).show();

                            }

                        }

                        @Override
                        public void failure(RetrofitError error) {

                            Toast.makeText(getApplicationContext(), "sd", Toast.LENGTH_LONG).show();
                        }
                    });
                }


            }
        });


        if (source_type.equalsIgnoreCase("video")){
            Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, videoId);
            res.enqueue(new retrofit2.Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                    JSONResponse jsonResponse = response.body();
                    movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));

                    shareurl = jsonResponse.getShareurl();

                    videomp4url = movie_detaildata.get(0).getVideo_url() + movie_detaildata.get(0).getPath();

                  urlll = movie_detaildata.get(0).getTrailer();
                    videotitle1.setText(movie_detaildata.get(0).getTitle());

                    mediaItem = buildMediaSource(Uri.parse(urlll)).getMediaItem();
                    exoPlayer.setPlayWhenReady(true);
                    playerView.setPlayer(exoPlayer);
                    exoPlayer.setMediaItem(mediaItem);
                    exoPlayer.addListener(new PlayerEventListener());
                    exoPlayer.prepare();
                    exoPlayer.play();


                    final Handler ha = new Handler();
                    ha.postDelayed(new Runnable() {

                        @Override
                        public void run() {

                            ha.postDelayed(this, 1000);


                            Api.getClient().getskiptime(
                                    new Callback<skiptime>() {
                                        @Override
                                        public void success(skiptime skiptime1, retrofit.client.Response response) {
                                            skiptime = skiptime1;


                                            skipduration = Long.parseLong(skiptime.getSkip_time());
                                            introduration = Long.parseLong(skiptime.getIntro_time());

                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            Toast.makeText(TrailerPlayerActivity.this, error.toString(), Toast.LENGTH_LONG).show();

                                        }

                                    });

                        }
                    }, 1000);


                }

                @Override
                public void onFailure(Call<JSONResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });
        }
        else if (source_type.equalsIgnoreCase("live")){
            Call<JSONResponse> res = ApiClient.getInstance1().getApi().getLiveDetail(user_id, videoId);
            res.enqueue(new retrofit2.Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                    JSONResponse jsonResponse = response.body();
                    live_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getLivedetail()));

                    shareurl = jsonResponse.getShareurl();

                    videomp4url = live_detaildata.get(0).getVideo_url() + live_detaildata.get(0).getPath();

                    urlll = live_detaildata.get(0).getTrailer();
                    videotitle1.setText(live_detaildata.get(0).getTitle());

                    Uri dashUri = Uri.parse(urlll);

                    mediaSource = buildMediaSource(Uri.parse(urlll));
                    exoPlayer.prepare(mediaSource);
                    exoPlayer.setPlayWhenReady(startAutoPlay);
                    exoPlayer.addAnalyticsListener(new EventLogger(trackSelector));
                    playerView.setPlayer(exoPlayer);
                    exoPlayer.seekTo(current_time_update);


                    final Handler ha = new Handler();
                    ha.postDelayed(new Runnable() {

                        @Override
                        public void run() {

                            ha.postDelayed(this, 1000);


                            Api.getClient().getskiptime(
                                    new Callback<skiptime>() {
                                        @Override
                                        public void success(skiptime skiptime1, retrofit.client.Response response) {
                                            skiptime = skiptime1;


                                            skipduration = Long.parseLong(skiptime.getSkip_time());
                                            introduration = Long.parseLong(skiptime.getIntro_time());

                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            Toast.makeText(TrailerPlayerActivity.this, error.toString(), Toast.LENGTH_LONG).show();

                                        }

                                    });

                        }
                    }, 1000);


                }

                @Override
                public void onFailure(Call<JSONResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });
        }
        else if (source_type.equalsIgnoreCase("episode")){
            Call<JSONResponse> res = ApiClient.getInstance1().getApi().getEpisodesDetail(videoId, user_id);
            res.enqueue(new retrofit2.Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                    JSONResponse jsonResponse = response.body();
                    episode_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getEpisode()));
                    season=new ArrayList<>(Arrays.asList(jsonResponse.getSeason()));

                    shareurl = jsonResponse.getShareurl();

                    videomp4url = episode_detaildata.get(0).getMp4_url() + episode_detaildata.get(0).getPath();

                    urlll = season.get(0).getTrailer();
                    videotitle1.setText(season.get(0).getTitle());

                    Uri dashUri = Uri.parse(urlll);

                    mediaSource = buildMediaSource(Uri.parse(urlll));
                    exoPlayer.prepare(mediaSource);
                    exoPlayer.setPlayWhenReady(startAutoPlay);
                    exoPlayer.addAnalyticsListener(new EventLogger(trackSelector));
                    playerView.setPlayer(exoPlayer);
                    exoPlayer.seekTo(current_time_update);


                    final Handler ha = new Handler();
                    ha.postDelayed(new Runnable() {

                        @Override
                        public void run() {

                            ha.postDelayed(this, 1000);


                            Api.getClient().getskiptime(
                                    new Callback<skiptime>() {
                                        @Override
                                        public void success(skiptime skiptime1, retrofit.client.Response response) {
                                            skiptime = skiptime1;


                                            skipduration = Long.parseLong(skiptime.getSkip_time());
                                            introduration = Long.parseLong(skiptime.getIntro_time());

                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            Toast.makeText(TrailerPlayerActivity.this, error.toString(), Toast.LENGTH_LONG).show();

                                        }

                                    });

                        }
                    }, 1000);


                }

                @Override
                public void onFailure(Call<JSONResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });
        }


        Call<JSONResponse> alsolikeres = ApiClient.getInstance1().getApi().getAlsolikeVideo(videoId);
        alsolikeres.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                JSONResponse jsonResponse = response.body();

                if (jsonResponse.getChannelrecomended().length == 0) {

                    norecommanded.setVisibility(View.VISIBLE);
                    thismaylikerecycler.setVisibility(View.GONE);
                } else {

                    norecommanded.setVisibility(View.GONE);
                    thismaylikerecycler.setVisibility(View.VISIBLE);
                    recomendeddatalist = new ArrayList<>(Arrays.asList(jsonResponse.getChannelrecomended()));

                    thismaylikeadopter = new ThismaylikeAdopter(recomendeddatalist);
                    thismaylikerecycler.setAdapter(thismaylikeadopter);


                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());

            }
        });

    }

    private void processPayment() {


        Call<JSONResponse> req = ApiClient.getInstance1().getApi().getSettings();
        req.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                JSONResponse jsonResponse = response.body();

                settings = new ArrayList<>(Arrays.asList(jsonResponse.getSettings()));


                amount = settings.get(0).getImage_url();
                PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "USD",
                        "Purchase Goods", PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
                startActivityForResult(intent, PAYPAL_REQUEST_CODE);


            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });


    }


    protected void createView() {
        handler = new Handler();
        tvPlaybackSpeed = findViewById(R.id.tv_play_back_speed);
        tvPlaybackSpeed.setOnClickListener(this);
        tvPlaybackSpeed.setText("" + tapCount);
        tvPlaybackSpeedSymbol = findViewById(R.id.tv_play_back_speed_symbol);
        tvPlaybackSpeedSymbol.setOnClickListener(this);
        imgBwd = findViewById(R.id.img_bwd);
        exoPlay = findViewById(R.id.exo_play1);
        exoreply = findViewById(R.id.exo_reply12);
        exoPause = findViewById(R.id.exo_pause);
        imgFwd = findViewById(R.id.img_fwd);
        /*skipintro = findViewById(R.id.img_bwd_skip1);*/
        tvPlayerCurrentTime = findViewById(R.id.tv_player_current_time);
        exoTimebar = findViewById(R.id.exo_progress);
        exoProgressbar = findViewById(R.id.loading_exoplayer);
        tvPlayerEndTime = findViewById(R.id.tv_player_end_time);
        imgSetting = findViewById(R.id.img_setting);
        imgFullScreenEnterExit = findViewById(R.id.img_full_screen_enter_exit);
        imgFullScreenEnterExit.setOnClickListener(this);
        resize_icon = findViewById(R.id.resize_icon);
        resize_icon.setOnClickListener(this);
        imgBackPlayer = findViewById(R.id.img_back_player);
        imgBackPlayer.setOnClickListener(this);
        playerView = findViewById(R.id.player_view);
        hidelayout1 = (LinearLayout) findViewById(R.id.hidelayout);
        imgSetting.setOnClickListener(this);
        //playerView.setControllerVisibilityListener(this);
        //   playerView.setErrorMessageProvider(new PlayerErrorMessageProvider());
        playerView.requestFocus();
        llParentContainer = (LinearLayout) findViewById(R.id.ll_parent_container);
        frameLayoutMain = findViewById(R.id.frame_layout_main);
        findViewById(R.id.img_back_player).setOnClickListener(this);
        progressBarPercentage = findViewById(R.id.progress_horizontal_percentage);
        progressBarPercentage.setVisibility(View.GONE);
        llDownloadContainer = (LinearLayout) findViewById(R.id.ll_download_container);
        llDownloadVideo = (LinearLayout) findViewById(R.id.ll_download_video);
        imgDownloadState = (ImageView) findViewById(R.id.img_download_state);
        tvDownloadState = (TextView) findViewById(R.id.tv_download_state);
        llDownloadVideo.setOnClickListener(this);
        subtitle_on = findViewById(R.id.img_subtitle);
        subtitle_off = findViewById(R.id.img_subtitle_off);
        subtitle_off.setVisibility(View.GONE);
        subtitle_on.setVisibility(View.GONE);

        setProgress();
        FullScreencall();


    }


    public void prepareView() {
        /*playerView.setLayoutParams(
                new PlayerView.LayoutParams(
                        // or ViewGroup.LayoutParams.WRAP_CONTENT
                        PlayerView.LayoutParams.MATCH_PARENT,
                        // or ViewGroup.LayoutParams.WRAP_CONTENT,
                        ScreenUtils.convertDIPToPixels(TrailerPlayerActivity.this, FrameLayout.LayoutParams.MATCH_PARENT)));*/


        frameLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));


    }

    private void VideoDetails() {
        trackSelector = new DefaultTrackSelector(this);
        exoPlayer = new ExoPlayer.Builder(this)
                .setTrackSelector(trackSelector)
                .build();

        mediaItem = buildMediaSource(Uri.parse(urlll)).getMediaItem();
        exoPlayer.setPlayWhenReady(true);
        playerView.setPlayer(exoPlayer);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.addListener(new PlayerEventListener());
        exoPlayer.prepare();
        exoPlayer.play();

    }

    private class PlayerEventListener implements Player.Listener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState) {

                case ExoPlayer.STATE_READY:

                    exoProgressbar.setVisibility(View.GONE);
                    break;

                case ExoPlayer.STATE_BUFFERING:

                    exoProgressbar.setVisibility(View.VISIBLE);
                    break;

                case ExoPlayer.STATE_ENDED:
                    exoPlayer.stop();
                    //player.stop();


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {



                            //  Toast.makeText(getApplicationContext(),"method called", Toast.LENGTH_LONG).show();
                        }
                    }, 1000);

                    updateButtonVisibilities();
            }

        }

        @Override
        public void onPositionDiscontinuity(@Player.DiscontinuityReason int reason) {
            if (exoPlayer.getPlayerError() != null) {
                // The user has performed a seek whilst in the error state. Update the resume position so
                // that if the user then retries, playback resumes from the position to which they seeked.
                updateStartPosition();
            }
        }


        public void onPlayerError(ExoPlaybackException e) {
            if (isBehindLiveWindow(e)) {
                clearStartPosition();
                //initializePlayer();
            } else {
                updateStartPosition();
                updateButtonVisibilities();
//                showControls();
            }
        }


        @SuppressWarnings("ReferenceEquality")
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            updateButtonVisibilities();
            if (trackGroups != lastSeenTrackGroupArray) {
                MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null) {
                    if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
                            == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                        showToast(String.valueOf(R.string.error_unsupported_video));
                    }
                    if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_AUDIO)
                            == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                        showToast(String.valueOf(R.string.error_unsupported_audio));
                    }
                }
                lastSeenTrackGroupArray = trackGroups;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                String status = "true";

                Api.getClient().getPaypalRent(user_id, videoId, status, new Callback<Paypalresponse>() {
                    @Override
                    public void success(Paypalresponse regresponse, retrofit.client.Response response) {
                        paypalresponse = regresponse;

                        if (regresponse.getStatus().equalsIgnoreCase("true")) {

                            Intent in = new Intent(getApplicationContext(), Main3Activity.class);
                            in.putExtra("id", videoId);
                            in.putExtra("url", videomp4url);
                            startActivity(in);

                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                        Toast.makeText(getApplicationContext(), "sd" + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


            } else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
    }


/*

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        releasePlayer();
        clearStartPosition();
        setIntent(intent);

        Bundle bundle = getIntent().getExtras();


        if (bundle != null) {
            videoId = bundle.getString("video_id");
            videoName = bundle.getString("video_name");
            videoUrl = bundle.getString("video_url");
            videoDurationInSeconds =bundle.getLong("video_duration");
        }


    }
*/

    @Override
    public void onStart() {
        super.onStart();


        if (Util.SDK_INT > 23) {
           // initializePlayer();
            setProgress();
            if (playerView != null) {
                playerView.onResume();
            }
        }
    }

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public void onResume() {
        super.onResume();
//
//        Uri dashUri = Uri.parse(urlll);
//        /*mediaSource = new ExtractorMediaSource(dashUri,
//                dataSourceFactory, extractorsFactory, null, null);*/
//        mediaSource = buildMediaSource(Uri.parse(urlll));
//        exoPlayer.setPlayWhenReady(true);
//        exoPlayer.seekTo(position);
//        exoPlayer.addAnalyticsListener(new EventLogger(trackSelector));
//        playerView.setPlayer(exoPlayer);

        if (Util.SDK_INT <= 23 || exoPlayer == null) {
           // initializePlayer();
            setProgress();

            if (playerView != null) {
                playerView.onResume();
            }
        }

        FullScreencall();
    }

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public void onPause() {
        super.onPause();

        position = exoPlayer.getCurrentPosition();
        handler.removeCallbacks(runnableCode);


        if (Util.SDK_INT <= 23) {
            if (playerView != null) {
                playerView.onPause();
            }
           // releasePlayer();

        }
        try {
            current_time_update = exoPlayer.getCurrentPosition();
        }catch (Exception e){

        }

    }

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public void onStop() {
        super.onStop();

        handler.removeCallbacks(runnableCode);

        if (Util.SDK_INT > 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        releasePlayer();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        updateTrackSelectorParameters();
        updateStartPosition();
      //  outState.putParcelable(KEY_TRACK_SELECTOR_PARAMETERS, trackSelectorParameters);
        outState.putBoolean(KEY_AUTO_PLAY, startAutoPlay);
        outState.putInt(KEY_WINDOW, startWindow);
        outState.putLong(KEY_POSITION, startPosition);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        return playerView.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
    }


    @OptIn(markerClass = UnstableApi.class)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_setting:

              /*  MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null) {
                    if (!isShowingTrackSelectionDialog && TrackSelectionDialog.willHaveContent(trackSelector)) {
                        isShowingTrackSelectionDialog = true;
                        TrackSelectionDialog trackSelectionDialog = TrackSelectionDialog.createForTrackSelector(trackSelector,*//* onDismissListener= *//* dismissedDialog -> isShowingTrackSelectionDialog = false);
                        trackSelectionDialog.show(getSupportFragmentManager(), *//* tag= *//* null);
                    }
                }*/

                break;
            case R.id.tv_play_back_speed:

            case R.id.tv_play_back_speed_symbol:

                if (tvPlaybackSpeed.getText().equals("1")) {
                    tapCount++;
                    PlaybackParameters param = new PlaybackParameters(2f);
                    exoPlayer.setPlaybackParameters(param);
                    tvPlaybackSpeed.setText("" + 2);
                } else if (tvPlaybackSpeed.getText().equals("2")) {
                    tapCount++;
                    PlaybackParameters param = new PlaybackParameters(4f);
                    exoPlayer.setPlaybackParameters(param);
                    tvPlaybackSpeed.setText("" + 4);

                } else if (tvPlaybackSpeed.getText().equals("4")) {
                    tapCount++;
                    PlaybackParameters param = new PlaybackParameters(6f);
                    exoPlayer.setPlaybackParameters(param);
                    tvPlaybackSpeed.setText("" + 6);
                } else if (tvPlaybackSpeed.getText().equals("6")) {
                    tapCount++;
                    PlaybackParameters param = new PlaybackParameters(8f);
                    exoPlayer.setPlaybackParameters(param);
                    tvPlaybackSpeed.setText("" + 8);
                } else {
                    tapCount = 0;
                    exoPlayer.setPlaybackParameters(null);
                    tvPlaybackSpeed.setText("" + 1);

                }

                break;

            case R.id.img_full_screen_enter_exit:
                resize_icon.setVisibility(View.VISIBLE);
                imgFullScreenEnterExit.setVisibility(View.GONE);
                Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
                int orientation = display.getOrientation();

                if (orientation == Surface.ROTATION_90 || orientation == Surface.ROTATION_270) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                    frameLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                    isScreenLandscape = false;
                    FullScreencall();
                    hide();

                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    FullScreencall();
                    playerView.setLayoutParams(new PlayerView.LayoutParams(PlayerView.LayoutParams.MATCH_PARENT, PlayerView.LayoutParams.MATCH_PARENT));
                    frameLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                    isScreenLandscape = true;
                    hide();

                }
                break;
            case R.id.resize_icon:
                resize_icon.setVisibility(View.GONE);
                imgFullScreenEnterExit.setVisibility(View.VISIBLE);
                Display display1 = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
                int orientation1 = display1.getOrientation();

                if (orientation1 == Surface.ROTATION_90 || orientation1 == Surface.ROTATION_270) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                    frameLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                    isScreenLandscape = false;
                    FullScreencall();
                    hide();

                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    FullScreencall();
                    playerView.setLayoutParams(new PlayerView.LayoutParams(PlayerView.LayoutParams.MATCH_PARENT, PlayerView.LayoutParams.MATCH_PARENT));
                    frameLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                    isScreenLandscape = true;
                    hide();

                }

                break;

            case R.id.img_back_player:
                onBackPressed();
                break;
        }
    }

  /*  @Override
    public void preparePlayback() {
        initializePlayer();
    }


    private void initializePlayer() {


        TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory();

        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(this, null, DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER);

        TrackSelection.Factory adaptiveTrackSelection = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());

        trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        trackSelector.setParameters(trackSelectorParameters);
        lastSeenTrackGroupArray = null;

        DefaultAllocator defaultAllocator = new DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE);
        DefaultLoadControl defaultLoadControl = new DefaultLoadControl(defaultAllocator,
                DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
                DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
                DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS,
                DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS,
                DefaultLoadControl.DEFAULT_TARGET_BUFFER_BYTES,
                DefaultLoadControl.DEFAULT_PRIORITIZE_TIME_OVER_SIZE_THRESHOLDS
        );

        bandwidthMeter = new DefaultBandwidthMeter();
        String userAgent = "XYZPLAYER";
        DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent, (TransferListener) bandwidthMeter, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS, DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true);
        // DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, (TransferListener) bandwidthMeter, httpDataSourceFactory);

        player = ExoPlayerFactory.newSimpleInstance(*/
    /*
    context=
    */
    /* TrailerPlayerActivity.this, renderersFactory, trackSelector, defaultLoadControl);


        playerView.setPlaybackPreparer(this);


        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }


            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case ExoPlayer.STATE_READY:

                        exoProgressbar.setVisibility(View.GONE);
                        break;

                    case ExoPlayer.STATE_BUFFERING:

                        exoProgressbar.setVisibility(View.VISIBLE);
                        break;

                    case ExoPlayer.STATE_IDLE:

                        player.prepare(mediaSource);
                        player.setPlayWhenReady(true);
                        player.getPlaybackState();
                        break;
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            }

            @Override
            public void onSeekProcessed() {
            }
        });
        player.seekTo(currentWindow, playbackPosition);
        // player.prepare(mediaSource, true, false);
        updateButtonVisibilities();
        initBwd();
        initFwd();

    }*/


    private MediaSource buildMediaSource(Uri uri) {
        return buildMediaSource(uri, null);
    }

    @OptIn(markerClass = UnstableApi.class)
    @SuppressWarnings("unchecked")
    private MediaSource buildMediaSource(Uri uri, @Nullable String overrideExtension) {
        @C.ContentType int type = Util.inferContentType(uri, overrideExtension);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(uri));
            case C.TYPE_SS:
                return new SsMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(uri));
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(uri));
            case C.TYPE_OTHER:
                return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(uri));
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    /**
     * Returns a new DataSource factory.
     */
  /*  private DataSource.Factory buildDataSourceFactory() {
        return ((AdaptiveExoplayer) getApplication()).buildDataSourceFactory();
    }
*/
    @OptIn(markerClass = UnstableApi.class)
    private void updateButtonVisibilities() {
        if (exoPlayer == null) {
            return;
        }

        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
        if (mappedTrackInfo == null) {
            return;
        }

        for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
            TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
            if (trackGroups.length != 0) {
                int label;
                switch (exoPlayer.getRendererType(i)) {
                    case C.TRACK_TYPE_AUDIO:
                        label = R.string.exo_track_selection_title_audio;
                        break;
                    case C.TRACK_TYPE_VIDEO:
                        label = R.string.exo_track_selection_title_video;
                        break;
                    case C.TRACK_TYPE_TEXT:
                        label = R.string.exo_track_selection_title_text;
                        break;
                    default:
                        continue;
                }
            }
        }
    }


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }


    private void setProgress() {

        handler = new Handler();
        //Make sure you update Seekbar on UI thread
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (exoPlayer != null) {
                    tvPlayerCurrentTime.setText(stringForTime((int) exoPlayer.getCurrentPosition()));
                    tvPlayerEndTime.setText(stringForTime((int) exoPlayer.getDuration()));
                    handler.postDelayed(this, 1000);
                }
            }
        });
    }

    private void initBwd() {
        imgBwd.requestFocus();
        imgBwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exoPlayer.seekTo(exoPlayer.getCurrentPosition() - 10000);
            }
        });
    }

    private void initFwd() {
        imgFwd.requestFocus();
        imgFwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exoPlayer.seekTo(exoPlayer.getCurrentPosition() + 10000);
            }
        });

    }

    private String stringForTime(int timeMs) {
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    public void FullScreencall() {


        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);
        }


    }

    public void hideStatusBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        llParentContainer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, UI_ANIMATION_DELAY);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.

        isConnectedFast(getApplicationContext());
        //    videoId = savedInstanceState.getString("video_id");
        //    videoUrl = savedInstanceState.getString("video_url");
        startPosition = savedInstanceState.getInt(KEY_POSITION);
        trackSelectorParameters = savedInstanceState.getParcelable(KEY_TRACK_SELECTOR_PARAMETERS);
        startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
        startWindow = savedInstanceState.getInt(KEY_WINDOW);
        savedInstanceState.getString("");
    }


    public static boolean isConnectedFast(final Context context) {
        final NetworkInfo info = getNetworkInfo(context);
        return (info != null) && info.isConnected() && isConnectionFast(info.getType(), info.getSubtype());
    }

    public static boolean isConnectionFast(final int type, final int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return true;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {

                case TelephonyManager.NETWORK_TYPE_CDMA:
                    Log.e("logg", String.valueOf(12));
                    return true; // ~ 14-64 kbps

                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    Log.e("logg", String.valueOf(60));
                    return true; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    Log.e("logg", String.valueOf(50));
                    return true; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    Log.e("logg", String.valueOf(400));
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    Log.e("logg", String.valueOf(600));
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    Log.e("logg", String.valueOf(100));
                    return true; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    Log.e("logg", "2mb");
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    Log.e("logg", String.valueOf(700));
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    Log.e("logg", "23mb");
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    Log.e("logg", String.valueOf(4007000));
                    return true; // ~ 400-7000 kbps
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    Log.e("logg", "1-2mb");
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    Log.e("logg", "5mb");
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    Log.e("logg", "10mb");
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    Log.e("logg", "25kb");
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    Log.e("logg", "10+mb");
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return true;
            }
        } else {
            return false;
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
        /*if (isScreenLandscape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            frameLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            isScreenLandscape = false;
            hide();
        } else {
            finish();

        }*/


    }


    private void releasePlayer() {
        if (exoPlayer != null) {
            updateTrackSelectorParameters();
            updateStartPosition();
            exoPlayer.release();
            exoPlayer = null;
            // mediaSource = null;
            trackSelector = null;
        }
        if (adsLoader != null) {
            adsLoader.setPlayer(null);
        }
    }

    private void updateTrackSelectorParameters() {
        if (trackSelector != null) {
            trackSelectorParameters = trackSelector.getParameters();
        }
    }

    private void updateStartPosition() {
        if (exoPlayer != null) {
            startAutoPlay = exoPlayer.getPlayWhenReady();
            startWindow = exoPlayer.getCurrentWindowIndex();
            startPosition = Math.max(0, exoPlayer.getContentPosition());
        }
    }

    private void clearStartPosition() {
        startAutoPlay = true;
        startWindow = C.INDEX_UNSET;
        startPosition = C.TIME_UNSET;
    }

    @Override
    public void onVisibilityChange(int visibility) {

    }




/*
    private class PlayerErrorMessageProvider implements ErrorMessageProvider<ExoPlaybackException> {

        @Override
        public Pair<Integer, String> getErrorMessage(ExoPlaybackException e) {
            String errorString = getString(R.string.error_generic);
            if (e.type == ExoPlaybackException.TYPE_RENDERER) {
                Exception cause = e.getRendererException();
                if (cause instanceof MediaCodecRenderer.DecoderInitializationException) {
                    // Special case for decoder initialization failures.
                    MediaCodecRenderer.DecoderInitializationException decoderInitializationException =
                            (MediaCodecRenderer.DecoderInitializationException) cause;
                    if (decoderInitializationException.codecInfo == null) {
                        if (decoderInitializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException) {
                            errorString = getString(R.string.error_querying_decoders);
                        } else if (decoderInitializationException.secureDecoderRequired) {
                            errorString =
                                    getString(
                                            R.string.error_no_secure_decoder, decoderInitializationException.mimeType);
                        } else {
                            errorString =
                                    getString(R.string.error_no_decoder, decoderInitializationException.mimeType);
                        }
                    } else {
                        errorString =
                                getString(
                                        R.string.error_instantiating_decoder,
                                        decoderInitializationException.codecInfo);
                    }
                }
            }
            return Pair.create(0, errorString);
        }
    }
*/


}