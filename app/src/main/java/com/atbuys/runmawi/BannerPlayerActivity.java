package com.atbuys.runmawi;

import static androidx.media3.exoplayer.offline.Download.STATE_COMPLETED;
import static androidx.media3.exoplayer.offline.Download.STATE_DOWNLOADING;
import static androidx.media3.exoplayer.offline.Download.STATE_FAILED;
import static androidx.media3.exoplayer.offline.Download.STATE_QUEUED;
import static androidx.media3.exoplayer.offline.Download.STATE_REMOVING;
import static androidx.media3.exoplayer.offline.Download.STATE_RESTARTING;
import static androidx.media3.exoplayer.offline.Download.STATE_STOPPED;
import static com.atbuys.runmawi.Connectivity.getNetworkInfo;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.util.Pair;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.media3.common.C;
import androidx.media3.common.ErrorMessageProvider;
import androidx.media3.common.Format;
import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.exoplayer.ExoPlaybackException;
import androidx.media3.exoplayer.SimpleExoPlayer;
import androidx.media3.exoplayer.ima.ImaServerSideAdInsertionMediaSource;
import androidx.media3.exoplayer.mediacodec.MediaCodecRenderer;
import androidx.media3.exoplayer.mediacodec.MediaCodecUtil;
import androidx.media3.exoplayer.offline.Download;
import androidx.media3.exoplayer.offline.DownloadHelper;
import androidx.media3.exoplayer.offline.DownloadManager;
import androidx.media3.exoplayer.offline.DownloadRequest;
import androidx.media3.exoplayer.source.BehindLiveWindowException;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.MergingMediaSource;
import androidx.media3.exoplayer.source.TrackGroupArray;
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector;
import androidx.media3.exoplayer.trackselection.MappingTrackSelector;
import androidx.media3.exoplayer.upstream.BandwidthMeter;
import androidx.media3.extractor.ExtractorsFactory;
import androidx.media3.ui.AspectRatioFrameLayout;
import androidx.media3.ui.DefaultTimeBar;
import androidx.media3.ui.PlayerControlView;
import androidx.media3.ui.PlayerView;
import androidx.media3.ui.SubtitleView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

@UnstableApi
public class BannerPlayerActivity extends AppCompatActivity implements View.OnClickListener, PlayerControlView.VisibilityListener, GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {


    private static final int playerHeight = 100;
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

    skiptime skiptime;
    long skipduration, introduration;
    Uri subUri;

    MediaSource subsource;
    MergingMediaSource mergedSource;
    BandwidthMeter bandwidthMeter;

    ImageView cast;
    TextView cast1;
    List<Countrycode> movieList;


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
    List<TrackKey> trackKeys = new ArrayList<>();
    List<String> optionsToDownload = new ArrayList<String>();
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
    private SubtitleView subtitle;
    private DataSource.Factory dataSourceFactory;
    private SimpleExoPlayer player;
    private MediaSource mediaSource;
    private DefaultTrackSelector trackSelector;
    private boolean isShowingTrackSelectionDialog;
    private DefaultTrackSelector.Parameters trackSelectorParameters;
    private TrackGroupArray lastSeenTrackGroupArray;
    private TextView tvPlaybackSpeed, tvPlaybackSpeedSymbol;
    private boolean startAutoPlay;
    private int startWindow;
    // Fields used only for ad playback. The ads loader is loaded via reflection.
    private long startPosition;
    private ImaServerSideAdInsertionMediaSource.AdsLoader adsLoader;
    private Uri loadedAdTagUri;
    private FrameLayout frameLayoutMain;
    private ImageView imgBwd;
    private ImageView exoPlay;
    private Switch autoplayswitch;
    private ImageView exoPause;
    private ImageView exoreply;
    private ImageView imgFwd,imgBackPlayer;
    private TextView skipintro;
    private TextView tvPlayerCurrentTime;
    private DefaultTimeBar exoTimebar;
    private ProgressBar exoProgressbar;
    private TextView tvPlayerEndTime;
    private ImageView imgSetting;
    private ImageView imgFullScreenEnterExit;

    private ImageView subtitle_off,subtitle_on;

    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    //private DownloadTracker downloadTracker;
    private DownloadManager downloadManager;
    private DownloadHelper myDownloadHelper;
    private LinearLayout llDownloadContainer;
    private LinearLayout llDownloadVideo;
    private ImageView imgDownloadState;
    private TextView tvDownloadState;
    private ProgressBar progressBarPercentage;
    private String videoName;

    /*private TextView cast_name;*/
    private long videoDurationInSeconds;
    private Runnable runnableCode;
    private Handler handler;
    Handler ha;
    private Runnable has;
    private TextView comments;
    private EditText commenttext;
    private ImageView commentsend;
    private LinearLayout commentlayout;
    private RecyclerView usercommentrecycler;

    private int currentWindow = 0;
    private long playbackPosition = 0;


    private AccessibilityService context;
    public static Dialog dialog,dialogsubformat;
    private ImageView like,liked,dislikee,dislikeed;
    LinearLayout unlikelayout,undislikelayout;
    AddComment addComment;
    ContinuWatching continuWatching;


    private float mScaleFactor = 1.0f;
    String nxtvidid,nextvidurl;

    LinearLayout watchlist,watchlater,hidelayout1,likee,dislike;
    ImageView watchlistimg,watchlistaddedimg,watchlaterimg,watchlateraddedimg;
    TextView videotext;
    LinearLayout share;
    TextView videotitle1,language1,genre2,duration,year,getCast1;
    String shareurl,user_id,autoplay,subtitles_text,subchecked1,subchecked2,subchecked3,subchecked4;
    String continuee;

    Long curr_time;


    private RecyclerView.LayoutManager layoutManager,layoutManager1,manager;
    private ArrayList<movies> moviesdata;
    private ArrayList<channelrecomended> recomendeddatalist;
    private ArrayList<genre> genredata;
    private ArrayList<videodetail> movie_detaildata;
    private ArrayList<videossubtitles> moviesubtitlesdata;
    private ArrayList<user_comments> usercommentslist;
    showwishlist showwish;
    private ArrayList<movieresolution> movieresolutiondata;
    private ArrayList<video_cast> castdetails;

    private List<videossubtitles> statelistdata;
    public String urlll ="";
    public String suburl = "";


    TextView norecommanded;

    RecyclerView thismaylikerecycler;
    RecyclerView castandcrewrecycler;
    ExtractorsFactory extractorsFactory;
    ThismaylikeAdopter thismaylikeadopter;
    castandcrewadapter castandcrewadapter;
    subtitleLangAdopter subtitleLangadopter;

    UserCommentsAdopter userCommentsAdopter;


    private long position;
    // private Object cast_details;.

    GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;

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

    @SuppressLint("WrongViewCast")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        if(  AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {

            setTheme(R.style.darktheme);

        }

        else {

            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        gestureDetector = new GestureDetector(BannerPlayerActivity.this, BannerPlayerActivity.this);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        dataSourceFactory = buildDataSourceFactory();

        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }
        hideStatusBar();

        setContentView(R.layout.activity_online_player);

        Intent in=getIntent();

        urlll=in.getStringExtra("url");




        // suburl = in.getStringExtra("suburl");
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        if (savedInstanceState != null) {

            trackSelectorParameters = savedInstanceState.getParcelable(KEY_TRACK_SELECTOR_PARAMETERS);
            startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
            startWindow = savedInstanceState.getInt(KEY_WINDOW);
            startPosition = savedInstanceState.getLong(KEY_POSITION);
        }
        else {
            trackSelectorParameters = new DefaultTrackSelector.ParametersBuilder().build();
            clearStartPosition();
        }

      /*  AdaptiveExoplayer application = (AdaptiveExoplayer) getApplication();
        downloadTracker = application.getDownloadTracker();
        downloadManager = application.getDownloadManager();*/

        // Start the download service if it should be running but it's not currently.
        // Starting the service in the foreground causes notification flicker if there is no scheduled
        // action. Starting it in the background throws an exception if the app is in the background too
        // (e.g. if device screen is locked).

        try {
           // DownloadService.start(this, DemoDownloadService.class);
        } catch (IllegalStateException e) {
         //   DownloadService.startForeground(this, DemoDownloadService.class);
        }

//        Requirements requirements = new Requirements(Requirements.NETWORK_UNMETERED);
//        DownloadService.sendSetRequirements(
//                OnlinePlayerActivity.this,
//                DemoDownloadService.class,
//                requirements,
//                /* foreground= */ false);


        createView();
        prepareView();


        runnableCode = new Runnable() {
            @Override
            public void run() {
           //     observerVideoStatus();
                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnableCode);


        SharedPreferences prefs =getApplicationContext(). getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id,null );
        final String user_role = prefs.getString(sharedpreferences.role,null );
        autoplay = prefs.getString(sharedpreferences.autoplay,null);
        subtitles_text  = prefs.getString(sharedpreferences.subtitles,null);
        subchecked1  = prefs.getString(sharedpreferences.subcheck1,null);
        subchecked2  = prefs.getString(sharedpreferences.subcheck2,null);
        subchecked3  = prefs.getString(sharedpreferences.subcheck3,null);
        subchecked4  = prefs.getString(sharedpreferences.subcheck4,null);




        videotext=(TextView)findViewById(R.id.videotext);
        watchlist=(LinearLayout)findViewById(R.id.watchlist);
        watchlater=(LinearLayout)findViewById(R.id.watchlater);
        watchlistimg=(ImageView)findViewById(R.id.watchlistimg);
        comments = (TextView) findViewById(R.id.comments);
        commentsend = (ImageView) findViewById(R.id.sendcomment);
        commenttext = (EditText) findViewById(R.id.commenttext);
        commentlayout = (LinearLayout)  findViewById(R.id.commentlayout);
        usercommentrecycler = (RecyclerView) findViewById(R.id.usercomments);


        likee = (LinearLayout) findViewById(R.id.likeed);
        dislike = (LinearLayout) findViewById(R.id.dislike);
        unlikelayout = (LinearLayout) findViewById(R.id.unlikelayout);
        undislikelayout = (LinearLayout) findViewById(R.id.undislikelayout);

        like = (ImageView) findViewById(R.id.like);
        liked = (ImageView) findViewById(R.id.liked);
        dislikee = (ImageView) findViewById(R.id.dislikee);
        dislikeed = (ImageView) findViewById(R.id.dislikeed);

        watchlistaddedimg=(ImageView)findViewById(R.id.watchlistaddimg);
        watchlaterimg=(ImageView)findViewById(R.id.watchlateradd);
        watchlateraddedimg=(ImageView)findViewById(R.id.watchlateraddedimg);
        share=(LinearLayout)findViewById(R.id.share);
        norecommanded=(TextView) findViewById(R.id.norecommanded);
        cast =(ImageView)findViewById(R.id.cast);
        genre2=(TextView)findViewById(R.id.genre);
        /*     cast_name=(TextView)findViewById(R.id.cast_name);*/
        language1=(TextView)findViewById(R.id.language);
        year=(TextView)findViewById(R.id.movieyear);
        duration=(TextView)findViewById(R.id.videoduration);
        videotitle1=(TextView)findViewById(R.id.videotitle);
        moviesdata = new ArrayList<movies>();
        genredata = new ArrayList<genre>();
        movieresolutiondata = new ArrayList<movieresolution>();
        movie_detaildata = new ArrayList<videodetail>();
        moviesubtitlesdata = new ArrayList<videossubtitles>();
        recomendeddatalist=new ArrayList<channelrecomended>();
        usercommentslist = new ArrayList<user_comments>();
        castdetails=new ArrayList<video_cast>();
        movieList = new ArrayList<>();

        thismaylikeadopter = new ThismaylikeAdopter(recomendeddatalist, this);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        thismaylikerecycler = (RecyclerView) findViewById(R.id.thismayalsolike);

        userCommentsAdopter = new UserCommentsAdopter(usercommentslist, this);
        layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);

        thismaylikerecycler.setHasFixedSize(true);
        thismaylikerecycler.setLayoutManager(layoutManager);
        thismaylikerecycler.setAdapter(thismaylikeadopter);

        castandcrewadapter = new castandcrewadapter(castdetails, this);
        manager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        castandcrewrecycler = (RecyclerView) findViewById(R.id.castandcrew);
        castandcrewrecycler.setHasFixedSize(true);
        castandcrewrecycler.setLayoutManager(manager);
        castandcrewrecycler.setAdapter(castandcrewadapter);


        usercommentrecycler.setHasFixedSize(true);
        usercommentrecycler.setLayoutManager(layoutManager1);
        usercommentrecycler.setAdapter(thismaylikeadopter);









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
                player.setPlayWhenReady(true);
                imgBwd.setVisibility(View.VISIBLE);
                imgFwd.setVisibility(View.VISIBLE);

            }
        });

        exoPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                exoPause.setVisibility(View.GONE);
                exoPlay.setVisibility(View.VISIBLE);
                player.setPlayWhenReady(false);
            }
        });







     //   CastContext castContext = CastContext.getSharedInstance(this);





    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
    {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            /*mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 10.0f));
*/

            if (mScaleFactor > 1) {
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
            } else {
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            }




            return true;
        }
    }



    private MediaSource buildMediaSource1(Uri urlll) {

        return buildMediaSource(urlll, null);
    }




/*
    private void observerVideoStatus() {
        if (downloadManager.getCurrentDownloads().size() > 0) {
            for (int i = 0; i < downloadManager.getCurrentDownloads().size(); i++) {
                Download currentDownload = downloadManager.getCurrentDownloads().get(i);
                if (!urlll.isEmpty() && currentDownload.request.uri.equals(parse(urlll))) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (downloadTracker.downloads.size() > 0) {
                                if (currentDownload.request.uri.equals(parse(urlll))) {

                                    Download downloadFromTracker = downloadTracker.downloads.get(parse(urlll));
                                    if (downloadFromTracker != null) {
                                        switch (downloadFromTracker.state) {
                                            case STATE_QUEUED:
                                                setCommonDownloadButton(ExoDownloadState.DOWNLOAD_QUEUE);
                                                break;

                                            case STATE_STOPPED:
                                                setCommonDownloadButton(ExoDownloadState.DOWNLOAD_RESUME);
                                                setCommonDownloadButton(ExoDownloadState.DOWNLOAD_RESUME);
                                                break;

                                            case STATE_DOWNLOADING:

                                                setCommonDownloadButton(ExoDownloadState.DOWNLOAD_PAUSE);

                                                if(downloadFromTracker.getPercentDownloaded() != -1){
                                                    progressBarPercentage.setVisibility(View.VISIBLE);
                                                    progressBarPercentage.setProgress(Integer.parseInt(AppUtil.floatToPercentage(downloadFromTracker.getPercentDownloaded()).replace("%","")));
//                                                                                 tvProgressPercentage.setText(AppUtil.floatToPercentage(downloadFromTracker.getPercentDownloaded()));
                                                }

                                                Log.d("EXO STATE_DOWNLOADING ", +downloadFromTracker.getBytesDownloaded() + " " + downloadFromTracker.contentLength);
                                                Log.d("EXO  STATE_DOWNLOADING ", "" + downloadFromTracker.getPercentDownloaded());

                                                break;
                                            case STATE_COMPLETED:


                                                setCommonDownloadButton(ExoDownloadState.DOWNLOAD_COMPLETED);
                                                progressBarPercentage.setVisibility(View.GONE);

                                                Log.d("EXO STATE_COMPLETED ", +downloadFromTracker.getBytesDownloaded() + " " + downloadFromTracker.contentLength);
                                                Log.d("EXO  STATE_COMPLETED ", "" + downloadFromTracker.getPercentDownloaded());

                                                progressBarPercentage.setVisibility(View.GONE);


                                                break;

                                            case STATE_FAILED:
                                                setCommonDownloadButton(ExoDownloadState.DOWNLOAD_RETRY);

                                                break;

                                            case STATE_REMOVING:


                                                break;

                                            case STATE_RESTARTING:


                                                break;
                                        }
                                    }
                                }

                            }
                        }
                    });
                }
            }
        }

    }
*/



    protected void createView() {
        handler = new Handler();
        tvPlaybackSpeed = findViewById(R.id.tv_play_back_speed);
        tvPlaybackSpeed.setOnClickListener(this);
        tvPlaybackSpeed.setText("" + tapCount);
        tvPlaybackSpeedSymbol = findViewById(R.id.tv_play_back_speed_symbol);
        tvPlaybackSpeedSymbol.setOnClickListener(this);
        imgBwd = findViewById(R.id.img_bwd);
        exoPlay = findViewById(R.id.exo_play1);
        autoplayswitch = (Switch) findViewById(R.id.autoplayswitch);
        exoPause = findViewById(R.id.exo_pause);
        subtitle_on = findViewById(R.id.img_subtitle);
        subtitle_off = findViewById(R.id.img_subtitle_off);

        exoreply = findViewById(R.id.exo_reply12);
        imgFwd = findViewById(R.id.img_fwd);
        tvPlayerCurrentTime = findViewById(R.id.tv_player_current_time);
        exoTimebar = findViewById(R.id.exo_progress);
        exoProgressbar = findViewById(R.id.loading_exoplayer);
        tvPlayerEndTime = findViewById(R.id.tv_player_end_time);
        imgSetting = findViewById(R.id.img_setting);
        imgFullScreenEnterExit = findViewById(R.id.img_full_screen_enter_exit);
        imgFullScreenEnterExit.setOnClickListener(this);
        imgBackPlayer = findViewById(R.id.img_back_player);
        playerView = findViewById(R.id.player_view);
        imgSetting.setOnClickListener(this);
        playerView.setControllerVisibilityListener(this);
      //  playerView.setErrorMessageProvider(new PlayerErrorMessageProvider());
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
        skipintro = (TextView) findViewById(R.id.img_bwd_skip1);

        playerView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);

            }
        });

        setProgress();
        FullScreencall();





    }



    private void subtitleDialog(BannerPlayerActivity onlineplayerActivity) {


        dialog = new Dialog(BannerPlayerActivity.this);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.subtitle_recycler);

        RadioButton subOff = (RadioButton) dialog.findViewById(R.id.subtitle_off);
        RadioButton subon = (RadioButton)dialog.findViewById(R.id.subtitle_on_radio);
        TextView sub_settings = (TextView) dialog.findViewById(R.id.subtitle_settings);
        RecyclerView subtitlelang = (RecyclerView) dialog.findViewById(R.id.subtitlelang);
         RecyclerView.LayoutManager subtiitleManeger;



        subtitleLangadopter = new subtitleLangAdopter(moviesubtitlesdata, this);
        subtiitleManeger = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);




        subtitlelang.setHasFixedSize(true);
        subtitlelang.setLayoutManager(subtiitleManeger);
        subtitlelang.setAdapter(subtitleLangadopter);

















    }



    public void prepareView() {
        playerView.setLayoutParams(
                new PlayerView.LayoutParams(
                        // or ViewGroup.LayoutParams.WRAP_CONTENT
                        PlayerView.LayoutParams.MATCH_PARENT,
                        // or ViewGroup.LayoutParams.WRAP_CONTENT,
                        ScreenUtils.convertDIPToPixels(BannerPlayerActivity.this, PlayerView.LayoutParams.MATCH_PARENT)));


        frameLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));


    }

 /*   @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        releasePlayer();
        clearStartPosition();
        setIntent(intent);

        Bundle bundle = getIntent().getExtras();


        if (bundle != null) {
            videoId = bundle.getString("video_id");
            videoName = bundle.getString("video_name");
            urlll = bundle.getString("video_url");
            videoDurationInSeconds =bundle.getLong("video_duration");
        }


    }*/

    @Override
    public void onStart() {
        super.onStart();

      //  downloadTracker.addListener(this);


        if (Util.SDK_INT > 23) {
           // initializePlayer();
            setProgress();
            if (playerView != null) {
                playerView.onResume();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || player == null) {
          //  initializePlayer();
            setProgress();

            if (playerView != null) {
                playerView.onResume();
            }
        }

        FullScreencall();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();


        }

    }

    @Override
    public void onStop() {
        super.onStop();
     //   downloadTracker.removeListener(this);
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

    }


    // OnClickListener methods
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        updateTrackSelectorParameters();
        updateStartPosition();
     //   outState.putParcelable(KEY_TRACK_SELECTOR_PARAMETERS, trackSelectorParameters);
        outState.putBoolean(KEY_AUTO_PLAY, startAutoPlay);
        outState.putInt(KEY_WINDOW, startWindow);
        outState.putLong(KEY_POSITION, startPosition);
    }

// PlaybackControlView.PlaybackPreparer implementation

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // See whether the player view wants to handle media or DPAD keys events.
        return playerView.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_setting:
/*
                MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null) {
                    if (!isShowingTrackSelectionDialog && TrackSelectionDialog.willHaveContent(trackSelector)) {
                        isShowingTrackSelectionDialog = true;
                        TrackSelectionDialog trackSelectionDialog = TrackSelectionDialog.createForTrackSelector(trackSelector,*//* onDismissListener= *//* dismissedDialog -> isShowingTrackSelectionDialog = false);
                        trackSelectionDialog.show(getSupportFragmentManager(), *//* tag= *//* null);
                    }
                }*/

                break;

            case R.id.img_full_screen_enter_exit:
                Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
                int orientation = display.getOrientation();

                if (orientation == Surface.ROTATION_90 || orientation == Surface.ROTATION_270) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


                    playerView.setLayoutParams(
                            new PlayerView.LayoutParams(
                                    // or ViewGroup.LayoutParams.WRAP_CONTENT
                                    PlayerView.LayoutParams.MATCH_PARENT,
                                    // or ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ScreenUtils.convertDIPToPixels(BannerPlayerActivity.this, PlayerView.LayoutParams.MATCH_PARENT)));


                    frameLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                    //imgFullScreenEnterExit.setImageResource(R.drawable.exo_controls_fullscreen_enter);
                    isScreenLandscape = false;
                    FullScreencall();

                    hide();
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    FullScreencall();

                    playerView.setLayoutParams(
                            new PlayerView.LayoutParams(
                                    // or ViewGroup.LayoutParams.WRAP_CONTENT
                                    PlayerView.LayoutParams.MATCH_PARENT,
                                    // or ViewGroup.LayoutParams.WRAP_CONTENT,
                                    PlayerView.LayoutParams.MATCH_PARENT));


                    frameLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                 //   imgFullScreenEnterExit.setImageResource(R.drawable.exo_controls_fullscreen_exit);
                    isScreenLandscape = true;
                    hide();

                }

                break;

            case R.id.tv_play_back_speed:
            case R.id.tv_play_back_speed_symbol:

                if (tvPlaybackSpeed.getText().equals("1")) {
                    tapCount++;
                    PlaybackParameters param = new PlaybackParameters(2f);
                    player.setPlaybackParameters(param);
                    tvPlaybackSpeed.setText("" + 2);
                } else if (tvPlaybackSpeed.getText().equals("2")) {
                    tapCount++;
                    PlaybackParameters param = new PlaybackParameters(4f);
                    player.setPlaybackParameters(param);
                    tvPlaybackSpeed.setText("" + 4);

                } else if (tvPlaybackSpeed.getText().equals("4")) {
                    tapCount++;
                    PlaybackParameters param = new PlaybackParameters(6f);
                    player.setPlaybackParameters(param);
                    tvPlaybackSpeed.setText("" + 6);
                } else if (tvPlaybackSpeed.getText().equals("6")) {
                    tapCount++;
                    PlaybackParameters param = new PlaybackParameters(8f);
                    player.setPlaybackParameters(param);
                    tvPlaybackSpeed.setText("" + 8);
                } else {
                    tapCount = 0;
                    player.setPlaybackParameters(null);
                    tvPlaybackSpeed.setText("" + 1);

                }

                break;

            case R.id.img_back_player:
                onBackPressed();
                break;
            case R.id.ll_download_video:

                ExoDownloadState exoDownloadState = (ExoDownloadState) llDownloadVideo.getTag();
                exoVideoDownloadDecision(exoDownloadState);

                break;

        }


    }

    private void exoVideoDownloadDecision(ExoDownloadState exoDownloadState){
        if(exoDownloadState == null || urlll.isEmpty()) {
            Toast.makeText(this, "Please, Tap Again", Toast.LENGTH_SHORT).show();

            return;
        }

        switch (exoDownloadState) {

            case DOWNLOAD_START:
                fetchDownloadOptions();

                break;

            case DOWNLOAD_PAUSE:

              //  downloadManager.addDownload(downloadTracker.getDownloadRequest(Uri.parse(urlll)), Download.STATE_STOPPED);

//                DownloadService.sendSetStopReason(
//                        OnlinePlayerActivity.this,
//                        DemoDownloadService.class,
//                        downloadTracker.getDownloadRequest(Uri.parse(urlll)).id,
//                        Download.STATE_STOPPED,
//                        /* foreground= */ false);

                break;

            case DOWNLOAD_RESUME:

             //   downloadManager.addDownload(downloadTracker.getDownloadRequest(Uri.parse(urlll)), Download.STOP_REASON_NONE);
//                DownloadService.sendSetStopReason(
//                        OnlinePlayerActivity.this,
//                        DemoDownloadService.class,
//                        downloadTracker.getDownloadRequest(Uri.parse(urlll)).id,
//                        Download.STOP_REASON_NONE,
//                        /* foreground= */ false);

                break;

            case DOWNLOAD_RETRY:

                break;

            case DOWNLOAD_COMPLETED:
                Toast.makeText(this, "Already Downloaded, Delete from Downloaded video ", Toast.LENGTH_SHORT).show();

                break;
        }
    }

    private void exoButtonPrepareDecision(){
//        if (downloadTracker.downloads.size() > 0) {
//            Download download = downloadTracker.downloads.get(Uri.parse(urlll));
//
//            if (download != null) {
//                if (download.getPercentDownloaded() > 99.0) {
//                    setCommonDownloadButton(ExoDownloadState.DOWNLOAD_COMPLETED);
//
//                } else {
//                    //Resume Download Not 100 % Downloaded
//                    //So, resume download
//                    setCommonDownloadButton(ExoDownloadState.DOWNLOAD_RESUME);
////                    String contentId = download.request.id;
////
////                    DownloadService.sendSetStopReason(
////                            OnlinePlayerActivity.this,
////                            DemoDownloadService.class,
////                            contentId,
////                            Download.STOP_REASON_NONE,
////                            /* foreground= */ false);
//
//                }
//            } else {
//                // New Download
//                setCommonDownloadButton(ExoDownloadState.DOWNLOAD_START);
//
////                DownloadRequest myDownloadRequest = downloadRequestt;
////                downloadManager.addDownload(myDownloadRequest);
//
//            }
//
//        }else {
//            setCommonDownloadButton(ExoDownloadState.DOWNLOAD_START);
//        }
    }



    private void fetchDownloadOptions() {
        trackKeys.clear();

        if (pDialog == null || !pDialog.isShowing()) {
            pDialog = new ProgressDialog(BannerPlayerActivity.this);
            pDialog.setTitle(null);
            pDialog.setCancelable(false);
            pDialog.setMessage("Preparing Download Options...");
            pDialog.show();
        }


       // DownloadHelper downloadHelper = DownloadHelper.forDash(BannerPlayerActivity.this, Uri.parse(urlll), dataSourceFactory, new DefaultRenderersFactory(BannerPlayerActivity.this));


/*
        downloadHelper.prepare(new DownloadHelper.Callback() {
            @Override
            public void onPrepared(DownloadHelper helper) {
                // Preparation completes. Now other DownloadHelper methods can be called.
                myDownloadHelper = helper;
                for (int i = 0; i < helper.getPeriodCount(); i++) {
                    TrackGroupArray trackGroups = helper.getTrackGroups(i);
                    for (int j = 0; j < trackGroups.length; j++) {
                        TrackGroup trackGroup = trackGroups.get(j);
                        for (int k = 0; k < trackGroup.length; k++) {
                            Format track = trackGroup.getFormat(k);
                            if (shouldDownload(track)) {
                                trackKeys.add(new TrackKey(trackGroups, trackGroup, track));
                            }
                        }
                    }
                }

                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }

                else {

                    //Toast.makeText(getApplicationContext(),"d", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }


                optionsToDownload.clear();
                showDownloadOptionsDialog(myDownloadHelper, trackKeys);
            }

            @Override
            public void onPrepareError(DownloadHelper helper, IOException e) {


                Log.d("sdsdd", String.valueOf(e));
                //Toast.makeText(getApplicationContext(),"failed"+e, Toast.LENGTH_LONG).show();
                pDialog.dismiss();

            }
        });
*/
    }

    private void showDownloadOptionsDialog(DownloadHelper helper, List<TrackKey> trackKeyss) {

        if (helper == null) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(BannerPlayerActivity.this);
        builder.setTitle("Select Download Format");
        int checkedItem = 1;


        for (int i = 0; i < trackKeyss.size(); i++) {
            TrackKey trackKey = trackKeyss.get(i);
            long bitrate = trackKey.getTrackFormat().bitrate;
            long getInBytes =  (bitrate * videoDurationInSeconds)/8;
            String getInMb = AppUtil.formatFileSize(getInBytes);
            String videoResoultionDashSize =  " "+trackKey.getTrackFormat().height +"      ("+getInMb+")";
            optionsToDownload.add(i, videoResoultionDashSize);
        }

        // Initialize a new array adapter instance
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(
                BannerPlayerActivity.this, // Context
                android.R.layout.simple_list_item_single_choice, // Layout
                optionsToDownload // List
        );

        TrackKey trackKey = trackKeyss.get(0);
        qualityParams = ((DefaultTrackSelector) trackSelector).getParameters().buildUpon()
                .setMaxVideoSize(trackKey.getTrackFormat().width, trackKey.getTrackFormat().height)
                .setMaxVideoBitrate(trackKey.getTrackFormat().bitrate)
                .build();

        builder.setSingleChoiceItems(arrayAdapter, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TrackKey trackKey = trackKeyss.get(i);

                qualityParams = ((DefaultTrackSelector) trackSelector).getParameters().buildUpon()
                        .setMaxVideoSize(trackKey.getTrackFormat().width, trackKey.getTrackFormat().height)
                        .setMaxVideoBitrate(trackKey.getTrackFormat().bitrate)
                        .build();



            }
        });
        // Set the a;ert dialog positive button
        builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {


                for (int periodIndex = 0; periodIndex < helper.getPeriodCount(); periodIndex++) {
                    MappingTrackSelector.MappedTrackInfo mappedTrackInfo = helper.getMappedTrackInfo(/* periodIndex= */ periodIndex);
                    helper.clearTrackSelections(periodIndex);
                    for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
//                        TrackGroupArray rendererTrackGroups = mappedTrackInfo.getTrackGroups(i);
                        helper.addTrackSelection(
                                periodIndex,
                                qualityParams);
                    }

                }



                DownloadRequest downloadRequest = helper.getDownloadRequest(Util.getUtf8Bytes(urlll));
                if (downloadRequest.streamKeys.isEmpty()) {
                    // All tracks were deselected in the dialog. Don't start the download.
                    return;
                }


                startDownload(downloadRequest);

                dialogInterface.dismiss();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();
    }

    private void startDownload(DownloadRequest downloadRequestt) {

        DownloadRequest myDownloadRequest = downloadRequestt;

        //       downloadManager.addDownload(downloadRequestt);

        if (myDownloadRequest.uri.toString().isEmpty()) {
            Toast.makeText(this, "Try Again!!", Toast.LENGTH_SHORT).show();

            return;
        } else {


//            DownloadRequest downloadRequest = new DownloadRequest(
//                    statusId,
//                    DownloadRequest.TYPE_PROGRESSIVE,
//                    Uri.parse(urlll),
//                    /* streamKeys= */ Collections.emptyList(),
//                    /* customCacheKey= */ null,
//                    null);


            downloadManager.addDownload(myDownloadRequest);

        }


    }

 /*   @Override
    public void preparePlayback() {
        initializePlayer();
    }


    private void initializePlayer() {



        TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory();
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(this, null, DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER);

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


        player = ExoPlayerFactory.newSimpleInstance(*//* context= *//* BannerPlayerActivity.this, renderersFactory, trackSelector, defaultLoadControl);
        player.addListener(new PlayerEventListener());
        player.setPlayWhenReady(startAutoPlay);
        player.addAnalyticsListener(new EventLogger(trackSelector));
        playerView.setPlayer(player);
        playerView.setPlaybackPreparer(this);

        mediaSource = buildMediaSource(Uri.parse(urlll));
        if(player != null){
            player.prepare(mediaSource, false, true);
        }


        exoButtonPrepareDecision();

        updateButtonVisibilities();
        initBwd();
        initFwd();

    }*/

    private boolean shouldDownload(Format track) {
        return track.height != 240 && track.sampleMimeType.equalsIgnoreCase("video/avc");
    }

    private MediaSource buildMediaSource(Uri uri) {
        return buildMediaSource(uri, null);
    }

    @SuppressWarnings("unchecked")
    private MediaSource buildMediaSource(Uri uri, @Nullable String overrideExtension) {
        @C.ContentType int type = Util.inferContentType(uri, overrideExtension);
        switch (type) {
            case C.TYPE_DASH:
              //  return new DashMediaSource.Factory(dataSourceFactory)
                      //  .createMediaSource(uri);
            case C.TYPE_SS:
              //  return new SsMediaSource.Factory(dataSourceFactory)
                       // .createMediaSource(uri);
            case C.TYPE_HLS:
               // return new HlsMediaSource.Factory(dataSourceFactory)
                        //.createMediaSource(uri);
            case C.TYPE_OTHER:
              //  return new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }


    /**
     * Returns a new DataSource factory.
     */
    private DataSource.Factory buildDataSourceFactory() {
       // return ((AdaptiveExoplayer) getApplication()).buildDataSourceFactory();
        return null;
    }


    private void updateButtonVisibilities() {
        if (player == null) {
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
                switch (player.getRendererType(i)) {
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


    private void showToast(int messageId) {
        showToast(getString(messageId));
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onVisibilityChange(int visibility) {

    }

    private void setProgress() {


        handler = new Handler();
        //Make sure you update Seekbar on UI thread
        handler.post(new Runnable() {

            @Override
            public void run() {
                if (player != null) {

                    tvPlayerCurrentTime.setText(stringForTime((int) player.getCurrentPosition()));
                    tvPlayerEndTime.setText(stringForTime((int) player.getDuration()));

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
                player.seekTo(player.getCurrentPosition() - 10000);
            }
        });
    }

    private void initFwd() {
        imgFwd.requestFocus();
        imgFwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.seekTo(player.getCurrentPosition() + 10000);
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

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.


        isConnectedFast(getApplicationContext());


        urlll = savedInstanceState.getString("url");
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

       /* if (isScreenLandscape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            playerView.setLayoutParams(
                    new PlayerView.LayoutParams(
                            // or ViewGroup.LayoutParams.WRAP_CONTENT
                            PlayerView.LayoutParams.MATCH_PARENT,
                            // or ViewGroup.LayoutParams.WRAP_CONTENT,
                            ScreenUtils.convertDIPToPixels(OnlinePlayerActivity.this, PlayerView.LayoutParams.MATCH_PARENT)));


            frameLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

            imgFullScreenEnterExit.setImageResource(R.drawable.exo_controls_fullscreen_enter);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            isScreenLandscape = false;
            hide();


        } else {
            finish();

        }*/



    }

    public void onDownloadsChanged(Download download) {
        switch (download.state) {
            case STATE_QUEUED:

                break;

            case STATE_STOPPED:


                break;
            case STATE_DOWNLOADING:



                Log.d("EXO DOWNLOADING ", +download.getBytesDownloaded() + " " + download.contentLength);
                Log.d("EXO  DOWNLOADING ", "" + download.getPercentDownloaded());


                break;
            case STATE_COMPLETED:

                setCommonDownloadButton(ExoDownloadState.DOWNLOAD_COMPLETED);
                progressBarPercentage.setVisibility(View.GONE);

                Log.d("EXO COMPLETED ", +download.getBytesDownloaded() + " " + download.contentLength);
                Log.d("EXO  COMPLETED ", "" + download.getPercentDownloaded());


                if(download.request.uri.toString().equals(urlll)){

                    if(download.getPercentDownloaded() != -1){
                        progressBarPercentage.setVisibility(View.VISIBLE);
                        progressBarPercentage.setProgress(Integer.parseInt(AppUtil.floatToPercentage(download.getPercentDownloaded()).replace("%","")));
//                        tvDownloadProgressMb.setText(AppUtil.getProgressDisplayLine(download.getBytesDownloaded(),download.contentLength)+" MB");
//                        tvProgressPercentage.setText(AppUtil.floatToPercentage(download.getPercentDownloaded()));
                    }
                }

                progressBarPercentage.setVisibility(View.GONE);
                break;

            case STATE_FAILED:

                break;

            case STATE_REMOVING:

                break;

            case STATE_RESTARTING:

                break;

        }

    }

    private void releasePlayer() {
        if (player != null) {





            updateTrackSelectorParameters();
            updateStartPosition();
            player.release();
            player = null;
            mediaSource = null;
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
        if (player != null) {
            startAutoPlay = player.getPlayWhenReady();
            startWindow = player.getCurrentWindowIndex();
            startPosition = Math.max(0, player.getContentPosition());
        }
    }

    private void clearStartPosition() {
        startAutoPlay = true;
        startWindow = C.INDEX_UNSET;
        startPosition = C.TIME_UNSET;
    }



    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.gestureDetector.onTouchEvent(event);
        this.scaleGestureDetector.onTouchEvent(event);

        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if(playerView.getResizeMode()== AspectRatioFrameLayout.RESIZE_MODE_FILL)
        {

            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);

        }
        else
        {
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);

        }


        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {


       return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }


/*
    private class PlayerEventListener implements Player.EventListener {

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
                    player.stop();



                    updateButtonVisibilities();
            }

        }
        @Override
        public void onPositionDiscontinuity(@Player.DiscontinuityReason int reason) {
            if (player.getPlaybackError() != null) {
                // The user has performed a seek whilst in the error state. Update the resume position so
                // that if the user then retries, playback resumes from the position to which they seeked.
                updateStartPosition();
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException e) {
            if (isBehindLiveWindow(e)) {
                clearStartPosition();
              //  initializePlayer();
            } else {
                updateStartPosition();
                updateButtonVisibilities();
//                showControls();
            }
        }

        @Override
        @SuppressWarnings("ReferenceEquality")
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            updateButtonVisibilities();
            if (trackGroups != lastSeenTrackGroupArray) {
                MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null) {
                    if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
                            == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                        showToast(R.string.error_unsupported_video);
                    }
                    if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_AUDIO)
                            == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                        showToast(R.string.error_unsupported_audio);
                    }
                }
                lastSeenTrackGroupArray = trackGroups;
            }
        }
    }
*/

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


    public void setCommonDownloadButton(ExoDownloadState exoDownloadState) {
        switch (exoDownloadState) {

            case DOWNLOAD_START:
                llDownloadVideo.setTag(exoDownloadState);
                tvDownloadState.setText(exoDownloadState.getValue());
                imgDownloadState.setImageDrawable(getResources().getDrawable(R.drawable.ic_download_cloud));
                break;

            case DOWNLOAD_PAUSE:
                llDownloadVideo.setTag(exoDownloadState);
                tvDownloadState.setText(exoDownloadState.getValue());
                imgDownloadState.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
                break;

            case DOWNLOAD_RESUME:
                llDownloadVideo.setTag(exoDownloadState);
                tvDownloadState.setText(exoDownloadState.getValue());
                imgDownloadState.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
                break;

            case DOWNLOAD_RETRY:
                llDownloadVideo.setTag(exoDownloadState);
                tvDownloadState.setText(exoDownloadState.getValue());
                imgDownloadState.setImageDrawable(getResources().getDrawable(R.drawable.ic_retry));
                break;

            case DOWNLOAD_COMPLETED:
                llDownloadVideo.setTag(exoDownloadState);
                tvDownloadState.setText(exoDownloadState.getValue());
                imgDownloadState.setImageDrawable(getResources().getDrawable(R.drawable.ic_download_complete));
                break;

            case DOWNLOAD_QUEUE:
                llDownloadVideo.setTag(exoDownloadState);
                tvDownloadState.setText(exoDownloadState.getValue());
                imgDownloadState.setImageDrawable(getResources().getDrawable(R.drawable.ic_queue));
                break;
        }
    }



}
