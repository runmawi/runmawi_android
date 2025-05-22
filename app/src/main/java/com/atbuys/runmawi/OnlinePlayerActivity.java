package com.atbuys.runmawi;

import static androidx.media3.exoplayer.offline.Download.STATE_COMPLETED;
import static androidx.media3.exoplayer.offline.Download.STATE_DOWNLOADING;
import static androidx.media3.exoplayer.offline.Download.STATE_QUEUED;
import static androidx.media3.exoplayer.offline.Download.STATE_STOPPED;
import static com.atbuys.runmawi.Connectivity.getNetworkInfo;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.util.TypedValue;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.media3.common.C;
import androidx.media3.common.ErrorMessageProvider;
import androidx.media3.common.Format;
import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.exoplayer.ExoPlaybackException;
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
import androidx.media3.exoplayer.source.ads.AdsLoader;
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector;
import androidx.media3.exoplayer.trackselection.MappingTrackSelector;
import androidx.media3.ui.AspectRatioFrameLayout;
import androidx.media3.ui.DefaultTimeBar;
import androidx.media3.ui.PlayerControlView;
import androidx.media3.ui.PlayerView;
import androidx.media3.ui.SubtitleView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.google.android.exoplayer2.ext.cast.CastPlayer;
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardMultilineWidget;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;

@UnstableApi
public class OnlinePlayerActivity extends AppCompatActivity implements View.OnClickListener, PlayerControlView.VisibilityListener, PaymentResultListener, GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {
    private BroadcastReceiver MyReceiver = null;
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

    skiptime skiptime;
    long skipduration, introduration;
    Uri subUri;

    MediaSource subsource;
    MergingMediaSource mergedSource;
  //  BandwidthMeter bandwidthMeter;

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
    private Player player;
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
    private AdsLoader adsLoader;
    private Uri loadedAdTagUri;
    private FrameLayout frameLayoutMain;
    private ImageView imgBwd;
    private ImageView exoPlay;
    private Switch autoplayswitch;
    private ImageView exoPause;
    private ImageView exoreply;
    private ImageView imgFwd, imgBackPlayer;
    private TextView skipintro;
    private TextView tvPlayerCurrentTime, free_time;
    private DefaultTimeBar exoTimebar;
    private ProgressBar exoProgressbar;
    private TextView tvPlayerEndTime;
    private ImageView imgSetting;
    private ImageView imgFullScreenEnterExit, resize_icon;
    long current_time_update;

    private ImageView subtitle_off, subtitle_on;

    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
  //  private DownloadTracker downloadTracker;
    private DownloadManager downloadManager;
    private DownloadHelper myDownloadHelper;
    private LinearLayout llDownloadContainer;
    private LinearLayout llDownloadVideo;
    private ImageView imgDownloadState;
    private TextView tvDownloadState;
    private ProgressBar progressBarPercentage;
    private String videoId;
    private String videoName;
    private int skip_intro;
    private int skip_recap;
    private int skip_intro_start;
    private int skip_recap_end;
    private int skip_recap_start;
    private int skip_intro_end;
    /*private TextView cast_name;*/
    private long videoDurationInSeconds;
    private Runnable runnableCode;
    private Handler handler;
    Handler ha;
    //private CastPlayer castPlayer;
    private Runnable has;
    private TextView comments;
    private EditText commenttext;
    private ImageView commentsend;
    private LinearLayout commentlayout;
    private RecyclerView usercommentrecycler;
    private String videoads;
    LinearLayout reelslayout, epaper_layout;
    ImageView reels_image, e_paper_image;


    private LinearLayout playnow, paynow, userfeatuerlayout, rent, play_begin, resume;

    private ImaAdsLoader imaAdsLoader;

    private int currentWindow = 0;
    ImageView info;
    LinearLayout infolayout;
    TextView infotext;

    private long playbackPosition = 0;


    private AccessibilityService context;
    public static Dialog dialog, dialogsubformat;
    View view;
    AlertDialog.Builder alert;
    AlertDialog custom_dialog;
    private ArrayList<active_payment_settings> active_payment_settingsList;
    Active_Payment_settingsAdopter active_payment_settingsAdopter;
    Addpayperview addpayperview;
    ProgressDialog progressDialog;

    private ArrayList<payment_settings> payment_settingslist;
    String key, py_id;
    private String ppv_price;
    private Stripe stripe;
    PaymentMethodCreateParams params;
    private ImageView like, liked, dislikee, dislikeed;
    LinearLayout unlikelayout, undislikelayout;
    AddComment addComment;
    ContinuWatching continuWatching;


    private float mScaleFactor = 1.0f;
    String nxtvidid, nextvidurl;

    LinearLayout watchlist, watchlater, hidelayout1, likee, dislike;
    ImageView watchlistimg, watchlistaddedimg, watchlaterimg, watchlateraddedimg;
    TextView videotext, views;
    LinearLayout share;
    TextView videotitle1, language1, genre2, duration, year, getCast1;
    String shareurl, user_id, autoplay, subtitles_text, subchecked1, subchecked2, subchecked3, subchecked4;
    String continuee;

    Long curr_time;

    Handler handlerskip = new Handler();
    Runnable runnable;
    int delay = 1000;


    private RecyclerView.LayoutManager layoutManager, layoutManager1, manager;
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
    public String urlll = "";
    public String suburl = "";
    public String rented = "";
    public int count = 1;
    String type = "channel";
    String dataa;
    TextView norecommanded;
    String reels_url;
    ImageView xnext, xprevious;

    RecyclerView thismaylikerecycler;
    RecyclerView castandcrewrecycler;
   // ExtractorsFactory extractorsFactory;
    ThismaylikeAdopter thismaylikeadopter;
    castandcrewadapter castandcrewadapter;
    subtitleLangAdopter subtitleLangadopter;

    UserCommentsAdopter userCommentsAdopter;


    LinearLayout trailerLayout;
    ImageView trailerImage, back;
    AlertDialog.Builder resolution_dialog;
    AlertDialog resolution_alert;
    View res_view;
    String resolution;


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

    @androidx.annotation.OptIn(markerClass = androidx.media3.common.util.UnstableApi.class)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("WrongViewCast")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {

            setTheme(R.style.darktheme);

        } else {

            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        gestureDetector = new GestureDetector(OnlinePlayerActivity.this, OnlinePlayerActivity.this);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
     //   dataSourceFactory = buildDataSourceFactory();

        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }
        hideStatusBar();
        MyReceiver = new MyReceiver();
        broadcastIntent();

        setContentView(R.layout.activity_online_player);

        Intent in = getIntent();
        videoId = in.getStringExtra("id");
        urlll = in.getStringExtra("url");
        dataa = in.getStringExtra("data");
        continuee = in.getStringExtra("continuee");
        videoads = in.getStringExtra("ads");
        rented = in.getStringExtra("xtra");


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //   Toast.makeText(getApplicationContext(),""+dataa, Toast.LENGTH_SHORT).show();

   //     imaAdsLoader = new ImaAdsLoader(this, getAdTagUri());


        // suburl = in.getStringExtra("suburl");
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        if (savedInstanceState != null) {

            trackSelectorParameters = savedInstanceState.getParcelable(KEY_TRACK_SELECTOR_PARAMETERS);
            startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
            startWindow = savedInstanceState.getInt(KEY_WINDOW);
            startPosition = savedInstanceState.getLong(KEY_POSITION);
        } else {
            trackSelectorParameters = new DefaultTrackSelector.ParametersBuilder().build();
            clearStartPosition();
        }

    //    AdaptiveExoplayer application = (AdaptiveExoplayer) getApplication();
       // downloadTracker = application.getDownloadTracker();
      //  downloadManager = application.getDownloadManager();

        // Start the download service if it should be running but it's not currently.
        // Starting the service in the foreground causes notification flicker if there is no scheduled
        // action. Starting it in the background throws an exception if the app is in the background too
        // (e.g. if device screen is locked).

        try {
          //  DownloadService.start(this, DemoDownloadService.class);
        } catch (IllegalStateException e) {
           // DownloadService.startForeground(this, DemoDownloadService.class);
        }

//        Requirements requirements = new Requirements(Requirements.NETWORK_UNMETERED);
//        DownloadService.sendSetRequirements(
//                OnlinePlayerActivity.this,
//                DemoDownloadService.class,
//                requirements,
//                /* foreground= */ false);


        createView();
        prepareView();

        //  Toast.makeText(application, "cast", Toast.LENGTH_SHORT).show();
        runnableCode = new Runnable() {
            @Override
            public void run() {
               // observerVideoStatus();
                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnableCode);


        SharedPreferences prefs = getApplicationContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        final String user_role = prefs.getString(sharedpreferences.role, null);
        autoplay = prefs.getString(sharedpreferences.autoplay, null);
        subtitles_text = prefs.getString(sharedpreferences.subtitles, null);
        subchecked1 = prefs.getString(sharedpreferences.subcheck1, null);
        subchecked2 = prefs.getString(sharedpreferences.subcheck2, null);
        subchecked3 = prefs.getString(sharedpreferences.subcheck3, null);
        subchecked4 = prefs.getString(sharedpreferences.subcheck4, null);


        reelslayout = (LinearLayout) findViewById(R.id.reelslayout);
        reels_image = (ImageView) findViewById(R.id.reels_image);

        epaper_layout = (LinearLayout) findViewById(R.id.e_paperlayout);
        e_paper_image = (ImageView) findViewById(R.id.e_paper_image);

        videotext = (TextView) findViewById(R.id.videotext);
        watchlist = (LinearLayout) findViewById(R.id.watchlist);
        watchlater = (LinearLayout) findViewById(R.id.watchlater);
        watchlistimg = (ImageView) findViewById(R.id.watchlistimg);
        comments = (TextView) findViewById(R.id.comments);
        commentsend = (ImageView) findViewById(R.id.sendcomment);
        commenttext = (EditText) findViewById(R.id.commenttext);
        commentlayout = (LinearLayout) findViewById(R.id.commentlayout);
        usercommentrecycler = (RecyclerView) findViewById(R.id.usercomments);
        xnext = (ImageView) findViewById(R.id.xnext);
        xprevious = (ImageView) findViewById(R.id.xprevious);


        trailerLayout = (LinearLayout) findViewById(R.id.trailerlayout);
        // ll_download_video_progress = (LinearLayout) findViewById(R.id.ll_download_video_progress);
        trailerImage = (ImageView) findViewById(R.id.trailerimage);
        back = (ImageView) findViewById(R.id.back);


        likee = (LinearLayout) findViewById(R.id.likeed);
        dislike = (LinearLayout) findViewById(R.id.dislike);
        unlikelayout = (LinearLayout) findViewById(R.id.unlikelayout);
        undislikelayout = (LinearLayout) findViewById(R.id.undislikelayout);

        playnow = (LinearLayout) findViewById(R.id.play_now);
        paynow = (LinearLayout) findViewById(R.id.paynow);
        play_begin = (LinearLayout) findViewById(R.id.play_begin);
        resume = (LinearLayout) findViewById(R.id.resumee);

        userfeatuerlayout = (LinearLayout) findViewById(R.id.userfeatuerlayout);
        autoplayswitch = (Switch) findViewById(R.id.autoplay);
        views = (TextView) findViewById(R.id.view);
        rent = (LinearLayout) findViewById(R.id.rent_now);

        like = (ImageView) findViewById(R.id.like);
        liked = (ImageView) findViewById(R.id.liked);
        dislikee = (ImageView) findViewById(R.id.dislikee);
        dislikeed = (ImageView) findViewById(R.id.dislikeed);

        watchlistaddedimg = (ImageView) findViewById(R.id.watchlistaddimg);
        watchlaterimg = (ImageView) findViewById(R.id.watchlateradd);
        watchlateraddedimg = (ImageView) findViewById(R.id.watchlateraddedimg);
        share = (LinearLayout) findViewById(R.id.share);
        norecommanded = (TextView) findViewById(R.id.norecommanded);
        cast = (ImageView) findViewById(R.id.cast);
        genre2 = (TextView) findViewById(R.id.genre);
        /*     cast_name=(TextView)findViewById(R.id.cast_name);*/
        language1 = (TextView) findViewById(R.id.language);
        year = (TextView) findViewById(R.id.movieyear);
        duration = (TextView) findViewById(R.id.videoduration);
        videotitle1 = (TextView) findViewById(R.id.videotitle);
        moviesdata = new ArrayList<movies>();
        genredata = new ArrayList<genre>();
        movieresolutiondata = new ArrayList<movieresolution>();
        movie_detaildata = new ArrayList<videodetail>();
        moviesubtitlesdata = new ArrayList<videossubtitles>();
        recomendeddatalist = new ArrayList<channelrecomended>();
        usercommentslist = new ArrayList<user_comments>();
        castdetails = new ArrayList<video_cast>();
        movieList = new ArrayList<>();

        thismaylikeadopter = new ThismaylikeAdopter(recomendeddatalist, this);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        cast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


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

        xprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        Api.getClient().getContinueexist(user_id, videoId, new Callback<ReadNotification>() {
            @Override
            public void success(ReadNotification readNotification, Response response) {

                readNotification = readNotification;


                if (readNotification.getStatus().equalsIgnoreCase("true")) {
                    continuee = "1";

                } else {
                    continuee = "0";
                }

                if (user_role == null) {

                    paynow.setVisibility(View.GONE);
                    resume.setVisibility(View.GONE);
                    play_begin.setVisibility(View.GONE);
                    rent.setVisibility(View.GONE);
                    playnow.setVisibility(View.GONE);
                    llDownloadVideo.setVisibility(View.GONE);
                    comments.setVisibility(View.GONE);
                    userfeatuerlayout.setVisibility(View.GONE);
                    trailerLayout.setVisibility(View.GONE);

                } else if (user_role != null && user_role.equalsIgnoreCase("registered")) {


                    if (continuee.equalsIgnoreCase("1")) {

                        resume.setVisibility(View.GONE);
                        play_begin.setVisibility(View.GONE);
                        paynow.setVisibility(View.GONE);
                        rent.setVisibility(View.GONE);
                        playnow.setVisibility(View.GONE);
                        llDownloadVideo.setVisibility(View.GONE);
                        comments.setVisibility(View.GONE);
                        userfeatuerlayout.setVisibility(View.GONE);
                        trailerLayout.setVisibility(View.GONE);

                    } else {


                        if (rented.equalsIgnoreCase("rentted")) {
                            paynow.setVisibility(View.GONE);
                            rent.setVisibility(View.GONE);
                            resume.setVisibility(View.GONE);
                            play_begin.setVisibility(View.GONE);
                            playnow.setVisibility(View.GONE);
                            llDownloadVideo.setVisibility(View.GONE);
                            comments.setVisibility(View.GONE);
                            userfeatuerlayout.setVisibility(View.GONE);
                            trailerLayout.setVisibility(View.GONE);

                        } else if (rented.equalsIgnoreCase("subscriber_content")) {
                            paynow.setVisibility(View.GONE);
                            rent.setVisibility(View.GONE);
                            playnow.setVisibility(View.GONE);
                            llDownloadVideo.setVisibility(View.GONE);
                            comments.setVisibility(View.GONE);
                            trailerLayout.setVisibility(View.GONE);
                            resume.setVisibility(View.GONE);
                            play_begin.setVisibility(View.GONE);
                        } else {

                            paynow.setVisibility(View.GONE);
                            rent.setVisibility(View.GONE);
                            resume.setVisibility(View.GONE);
                            play_begin.setVisibility(View.GONE);
                            playnow.setVisibility(View.GONE);
                            llDownloadVideo.setVisibility(View.GONE);
                            comments.setVisibility(View.GONE);
                            userfeatuerlayout.setVisibility(View.GONE);
                            trailerLayout.setVisibility(View.GONE);


                        }
                    }
                } else if (continuee.equalsIgnoreCase("1")) {

                    resume.setVisibility(View.GONE);
                    play_begin.setVisibility(View.GONE);
                    paynow.setVisibility(View.GONE);
                    rent.setVisibility(View.GONE);
                    playnow.setVisibility(View.GONE);
                    llDownloadVideo.setVisibility(View.GONE);
                    comments.setVisibility(View.GONE);
                    userfeatuerlayout.setVisibility(View.GONE);
                    trailerLayout.setVisibility(View.GONE);

                } else {
                    if (rented.equalsIgnoreCase("subscriberrent")) {
                        paynow.setVisibility(View.GONE);
                        resume.setVisibility(View.GONE);
                        play_begin.setVisibility(View.GONE);
                        rent.setVisibility(View.VISIBLE);
                        playnow.setVisibility(View.GONE);
                        llDownloadVideo.setVisibility(View.GONE);
                        comments.setVisibility(View.GONE);
                        userfeatuerlayout.setVisibility(View.GONE);
                        trailerLayout.setVisibility(View.GONE);
                    } else if (rented.equalsIgnoreCase("subscriberented")) {
                        paynow.setVisibility(View.GONE);
                        rent.setVisibility(View.GONE);
                        playnow.setVisibility(View.GONE);
                        llDownloadVideo.setVisibility(View.GONE);
                        userfeatuerlayout.setVisibility(View.GONE);
                        comments.setVisibility(View.VISIBLE);
                        trailerLayout.setVisibility(View.VISIBLE);
                    } else {
                        paynow.setVisibility(View.GONE);
                        rent.setVisibility(View.GONE);
                        playnow.setVisibility(View.GONE);
                        llDownloadVideo.setVisibility(View.GONE);
                        userfeatuerlayout.setVisibility(View.GONE);
                        comments.setVisibility(View.VISIBLE);
                        trailerLayout.setVisibility(View.VISIBLE);
                    }

                }

            }

            @Override
            public void failure(RetrofitError error) {

                Toast.makeText(OnlinePlayerActivity.this, "Check Your Internet Connection", Toast.LENGTH_LONG).show();
            }
        });

        final Handler handler223 = new Handler();
        final Runnable Update223 = new Runnable() {
            public void run() {//check this line

                Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, videoId);
                res.enqueue(new retrofit2.Callback<JSONResponse>() {
                    @Override
                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                        JSONResponse jsonResponse = response.body();
                        movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));

                        if (jsonResponse.getUserrole().equalsIgnoreCase("admin")) {

                        } else if (movie_detaildata.get(0).getAccess().equalsIgnoreCase("ppv") && !(jsonResponse.getPpv_video_status().equalsIgnoreCase("can_view"))) {
                            if (false) {

                                try {

                                    int playerTime = (int) player.getCurrentPosition() / 1000;//10
                                    int free_duration = Integer.parseInt(movie_detaildata.get(0).getFree_duration());//59
                                    int ending_time = free_duration - playerTime;//59-10=49
                                    String timer = TimeFormat(ending_time);
                                    //Toast.makeText(OnlinePlayerActivity.this, "pt-> "+playerTime+"ft-> "+free_duration+"et-> "+ending_time+"ti-> "+timer, Toast.LENGTH_SHORT).show();

                                    free_time.setText("end in: " + timer);

                                    if (playerTime > free_duration) {
                                        player.setPlayWhenReady(false);
                                        free_time.setVisibility(View.GONE);
                                        exoPlay.setVisibility(View.GONE);
                                        exoPause.setVisibility(View.GONE);
                                        showBottom(count);

                                        if (exoPause.getVisibility() == View.VISIBLE) {
                                            player.setPlayWhenReady(true);
                                        } else {
                                            player.setPlayWhenReady(false);
                                        }
                                    } else {
                                        player.setPlayWhenReady(true);
                                        free_time.setVisibility(View.VISIBLE);
                                        if (exoPause.getVisibility() == View.VISIBLE) {
                                            player.setPlayWhenReady(true);
                                            exoPlay.setVisibility(View.GONE);
                                            exoPause.setVisibility(View.VISIBLE);

                                        } else {
                                            player.setPlayWhenReady(false);
                                            exoPlay.setVisibility(View.VISIBLE);
                                            exoPause.setVisibility(View.GONE);
                                        }
                                    }
                                } catch (Exception e) {

                                }

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                        Log.d("Error", t.getMessage());
                    }
                });
            }
        };
        Timer swipeTime223 = new Timer();
        swipeTime223.schedule(new TimerTask() {
            @Override
            public void run() {
                handler223.post(Update223);
            }
        }, 1000, 1000);


        handlerskip.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);


                if (player == null) {

                } else if (player.getCurrentPosition() > 5000 && player.getCurrentPosition() < 10000) {

                    infolayout.setVisibility(View.GONE);
                } else {
                    infolayout.setVisibility(View.GONE);
                }


                if (player == null) {

                } else if (player.getCurrentPosition() > 1000 && player.getCurrentPosition() < 5000) {

                    skipintro.setVisibility(View.VISIBLE);
                    skipintro.setText("Skip Intro");


                } else {

                    skipintro.setVisibility(View.GONE);

                }


                if (player == null) {

                } else if (player.getCurrentPosition() > skip_recap_start && player.getCurrentPosition() < skip_recap_end) {

                    skipintro.setText("Skip Recap");
                    skipintro.setVisibility(View.VISIBLE);


                } else {


                    skipintro.setVisibility(View.GONE);


                }

                if (player == null) {
                } else if (player.getCurrentPosition() > skip_recap_end || player.getCurrentPosition() > skip_intro_end) {


//                    ha.removeCallbacks(has);
                    skipintro.setVisibility(View.GONE);

                } else {

                }

            }

        }, delay);


        xprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataa.equalsIgnoreCase("watchlater")) {
                    Call<JSONResponse> alsolikeres = ApiClient.getInstance1().getApi().getWatchlatertPrev(user_id, videoId);
                    alsolikeres.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                            JSONResponse jsonResponse = response.body();

                            if (jsonResponse.getStatus().equalsIgnoreCase("true")) {

                                videoId = jsonResponse.getNext_videoid();
                                // Toast.makeText(getApplicationContext(), "" + jsonResponse.getNext_videoid() + "\n" + videoId, Toast.LENGTH_LONG).show();
                                VideoDetails();
                                ThisMayAlsoLikeVideos();

                            } else {
                               /* Toast.makeText(getApplicationContext(), "Auto play turned off", Toast.LENGTH_LONG).show();
                                SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                editor.putString(sharedpreferences.autoplay, String.valueOf(0));
                                editor.apply();
                                editor.commit();
                                SharedPreferences prefs =getApplicationContext(). getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
                                autoplay = prefs.getString(sharedpreferences.autoplay,null);*/

                            }

                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Log.d("Error", t.getMessage());

                        }
                    });

                } else if (dataa.equalsIgnoreCase("favorite")) {
                    Call<JSONResponse> alsolikeres = ApiClient.getInstance1().getApi().getfavouritePrev(user_id, videoId);
                    alsolikeres.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                            JSONResponse jsonResponse = response.body();

                            if (jsonResponse.getStatus().equalsIgnoreCase("true")) {
                                videoId = jsonResponse.getNext_videoid();
                                VideoDetails();
                                ThisMayAlsoLikeVideos();
                            } else {
                              /*  Toast.makeText(getApplicationContext(), "Auto play turned off", Toast.LENGTH_LONG).show();
                                SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                editor.putString(sharedpreferences.autoplay, String.valueOf(0));
                                editor.apply();
                                editor.commit();
                                SharedPreferences prefs =getApplicationContext(). getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
                                autoplay = prefs.getString(sharedpreferences.autoplay,null);*/

                            }
                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Log.d("Error", t.getMessage());

                        }
                    });
                } else if (dataa.equalsIgnoreCase("wishlist")) {
                    Call<JSONResponse> alsolikeres = ApiClient.getInstance1().getApi().getWishlistPrev(user_id, videoId);
                    alsolikeres.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                            JSONResponse jsonResponse = response.body();

                            if (jsonResponse.getStatus().equalsIgnoreCase("true")) {
                                videoId = jsonResponse.getNext_videoid();
                                VideoDetails();
                                ThisMayAlsoLikeVideos();
                            } else {
                               /* Toast.makeText(getApplicationContext(), "Auto play turned off", Toast.LENGTH_LONG).show();
                                SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                editor.putString(sharedpreferences.autoplay, String.valueOf(0));
                                editor.apply();
                                editor.commit();
                                SharedPreferences prefs =getApplicationContext(). getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
                                autoplay = prefs.getString(sharedpreferences.autoplay,null);*/

                            }
                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Log.d("Error", t.getMessage());

                        }
                    });
                } else if (dataa.equalsIgnoreCase("video")) {
                    Call<JSONResponse> alsolikeres = ApiClient.getInstance1().getApi().getPrev_video(videoId);
                    alsolikeres.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                            JSONResponse jsonResponse = response.body();

                            if (jsonResponse.getStatus().equalsIgnoreCase("true")) {
                                videoId = jsonResponse.getNext_videoid();
                                VideoDetails();
                                ThisMayAlsoLikeVideos();
                            } else {
                               /* Toast.makeText(getApplicationContext(), "Auto play turned off", Toast.LENGTH_LONG).show();
                                SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                editor.putString(sharedpreferences.autoplay, String.valueOf(0));
                                editor.apply();
                                editor.commit();
                                SharedPreferences prefs =getApplicationContext(). getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
                                autoplay = prefs.getString(sharedpreferences.autoplay,null);*/

                            }

                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Log.d("Error", t.getMessage());

                        }
                    });
                }
            }
        });

        xnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataa.equalsIgnoreCase("watchlater")) {
                    Call<JSONResponse> alsolikeres = ApiClient.getInstance1().getApi().getWatchlatertNext(user_id, videoId);
                    alsolikeres.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                            JSONResponse jsonResponse = response.body();

                            if (jsonResponse.getStatus().equalsIgnoreCase("true")) {

                                videoId = jsonResponse.getNext_videoid();
                                Toast.makeText(getApplicationContext(), "" + jsonResponse.getNext_videoid() + "\n" + videoId, Toast.LENGTH_LONG).show();
                                VideoDetails();
                                ThisMayAlsoLikeVideos();

                            } else {
                               /* Toast.makeText(getApplicationContext(), "Auto play turned off", Toast.LENGTH_LONG).show();
                                SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                editor.putString(sharedpreferences.autoplay, String.valueOf(0));
                                editor.apply();
                                editor.commit();
                                SharedPreferences prefs =getApplicationContext(). getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
                                autoplay = prefs.getString(sharedpreferences.autoplay,null);*/

                            }

                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Log.d("Error", t.getMessage());

                        }
                    });

                } else if (dataa.equalsIgnoreCase("favorite")) {
                    Call<JSONResponse> alsolikeres = ApiClient.getInstance1().getApi().getfavouriteNext(user_id, videoId);
                    alsolikeres.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                            JSONResponse jsonResponse = response.body();

                            if (jsonResponse.getStatus().equalsIgnoreCase("true")) {
                                videoId = jsonResponse.getNext_videoid();
                                VideoDetails();
                                ThisMayAlsoLikeVideos();
                            } else {
                               /* Toast.makeText(getApplicationContext(), "Auto play turned off", Toast.LENGTH_LONG).show();
                                SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                editor.putString(sharedpreferences.autoplay, String.valueOf(0));
                                editor.apply();
                                editor.commit();
                                SharedPreferences prefs =getApplicationContext(). getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
                                autoplay = prefs.getString(sharedpreferences.autoplay,null);*/

                            }
                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Log.d("Error", t.getMessage());

                        }
                    });
                } else if (dataa.equalsIgnoreCase("wishlist")) {
                    Call<JSONResponse> alsolikeres = ApiClient.getInstance1().getApi().getWishlistNext(user_id, videoId);
                    alsolikeres.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                            JSONResponse jsonResponse = response.body();

                            if (jsonResponse.getStatus().equalsIgnoreCase("true")) {
                                videoId = jsonResponse.getNext_videoid();
                                VideoDetails();
                                ThisMayAlsoLikeVideos();
                            } else {
                               /* Toast.makeText(getApplicationContext(), "Auto play turned off", Toast.LENGTH_LONG).show();
                                SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                editor.putString(sharedpreferences.autoplay, String.valueOf(0));
                                editor.apply();
                                editor.commit();
                                SharedPreferences prefs =getApplicationContext(). getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
                                autoplay = prefs.getString(sharedpreferences.autoplay,null);*/

                            }
                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Log.d("Error", t.getMessage());

                        }
                    });
                } else if (dataa.equalsIgnoreCase("video")) {
                    Call<JSONResponse> alsolikeres = ApiClient.getInstance1().getApi().getNext_video(videoId);
                    alsolikeres.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                            JSONResponse jsonResponse = response.body();

                            if (jsonResponse.getStatus().equalsIgnoreCase("true")) {
                                videoId = jsonResponse.getNext_videoid();
                                VideoDetails();
                                ThisMayAlsoLikeVideos();
                            } else {
                                /*Toast.makeText(getApplicationContext(), "Auto play turned off", Toast.LENGTH_LONG).show();
                                SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                editor.putString(sharedpreferences.autoplay, String.valueOf(0));
                                editor.apply();
                                editor.commit();
                                SharedPreferences prefs =getApplicationContext(). getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
                                autoplay = prefs.getString(sharedpreferences.autoplay,null);*/

                            }

                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Log.d("Error", t.getMessage());

                        }
                    });
                }
            }
        });

        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (commentlayout.getVisibility() == View.VISIBLE) {

                    commentlayout.setVisibility(View.GONE);
                } else {

                    commentlayout.setVisibility(View.VISIBLE);

                    Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getUsercomments(videoId);
                    movieres.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                            JSONResponse jsonResponse = response.body();

                            if (jsonResponse.getUser_comments().length == 0) {


                            } else {

                                usercommentslist = new ArrayList<>(Arrays.asList(jsonResponse.getUser_comments()));
                                userCommentsAdopter = new UserCommentsAdopter(usercommentslist);
                                usercommentrecycler.setAdapter(userCommentsAdopter);

                            }

                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Log.d("Error", t.getMessage());
                        }
                    });

                }

            }
        });


        autoplayswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {


                    Toast.makeText(getApplicationContext(), "Auto play turned On", Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                    editor.putString(sharedpreferences.autoplay, String.valueOf(1));
                    editor.apply();

                    SharedPreferences prefs = getApplicationContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
                    autoplay = prefs.getString(sharedpreferences.autoplay, null);


                } else {

                    Toast.makeText(getApplicationContext(), "Auto play turned off", Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                    editor.putString(sharedpreferences.autoplay, String.valueOf(0));
                    editor.apply();
                    editor.commit();

                    SharedPreferences prefs = getApplicationContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
                    autoplay = prefs.getString(sharedpreferences.autoplay, null);


                }
            }
        });


        if (autoplay == null) {

            autoplayswitch.setChecked(false);

        } else if (autoplay.equalsIgnoreCase("0")) {
            autoplayswitch.setChecked(false);
        } else {
            autoplayswitch.setChecked(true);
        }


        commentsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comment = commenttext.getText().toString();


                Api.getClient().getAddcomment(user_id, videoId, comment, new Callback<AddComment>() {

                    @Override
                    public void success(AddComment addComment, Response response) {

                        addComment = addComment;

                        if (addComment.getStatus().equalsIgnoreCase("true")) {

                            Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getUsercomments(videoId);
                            movieres.enqueue(new retrofit2.Callback<JSONResponse>() {
                                @Override
                                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                    JSONResponse jsonResponse = response.body();

                                    commenttext.setText("");

                                    usercommentslist = new ArrayList<>(Arrays.asList(jsonResponse.getUser_comments()));
                                    userCommentsAdopter = new UserCommentsAdopter(usercommentslist);
                                    usercommentrecycler.setAdapter(userCommentsAdopter);


                                }

                                @Override
                                public void onFailure(Call<JSONResponse> call, Throwable t) {
                                    Log.d("Error", t.getMessage());
                                }
                            });

                        } else {

                            Toast.makeText(getApplicationContext(), "" + addComment.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });


            }
        });


        exoreply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                exoPause.setVisibility(View.VISIBLE);
                exoreply.setVisibility(View.GONE);
                exoPlay.setVisibility(View.GONE);
                VideoDetails();
                ThisMayAlsoLikeVideos();
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


        //   CastContext castContext = CastContext.getSharedInstance(this);


        watchlater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                {

                    Api.getClient().getAddwatchlater(user_id, videoId, type, new Callback<Addtowishlistmovie>() {

                        @Override
                        public void success(Addtowishlistmovie addwishmovie, Response response) {

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

                            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
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
                        public void success(Addtowishlistmovie addwishmovie, Response response) {

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


        likee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                {

                    Api.getClient().getlikeandunlike(user_id, videoId, "1", new Callback<likeandunlike>() {

                        @Override
                        public void success(likeandunlike addwishmovie, Response response) {

                            addwishmovie = addwishmovie;
                            if (addwishmovie.getLiked().equalsIgnoreCase("1")) {

                                unlikelayout.setVisibility(View.VISIBLE);
                                likee.setVisibility(View.GONE);

                                Toast.makeText(getApplicationContext(), "You liked video", Toast.LENGTH_LONG).show();
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


        unlikelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                {

                    Api.getClient().getlikeandunlike(user_id, videoId, "0", new Callback<likeandunlike>() {

                        @Override
                        public void success(likeandunlike addwishmovie, Response response) {

                            addwishmovie = addwishmovie;
                            if (addwishmovie.getLiked().equalsIgnoreCase("0")) {

                                likee.setVisibility(View.VISIBLE);
                                unlikelayout.setVisibility(View.GONE);

                                Toast.makeText(getApplicationContext(), "You un liked video", Toast.LENGTH_LONG).show();
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


        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                {

                    Api.getClient().getDislikecount(user_id, videoId, "1", new Callback<likeandunlike>() {

                        @Override
                        public void success(likeandunlike addwishmovie, Response response) {

                            addwishmovie = addwishmovie;

                            if (addwishmovie.getLiked().equalsIgnoreCase("1")) {

                                undislikelayout.setVisibility(View.VISIBLE);
                                dislike.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "You disliked video", Toast.LENGTH_LONG).show();
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


        undislikelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                {

                    Api.getClient().getDislikecount(user_id, videoId, "0", new Callback<likeandunlike>() {

                        @Override
                        public void success(likeandunlike addwishmovie, Response response) {

                            addwishmovie = addwishmovie;
                            if (addwishmovie.getLiked().equalsIgnoreCase("0")) {

                                dislike.setVisibility(View.VISIBLE);
                                undislikelayout.setVisibility(View.GONE);

                                Toast.makeText(getApplicationContext(), "You disliked video", Toast.LENGTH_LONG).show();
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
                        public void success(Addtowishlistmovie addwishmovie, Response response) {

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

                            Toast.makeText(getApplicationContext(), "You are not a registered user ", Toast.LENGTH_LONG).show();
                        }
                    });
                }


            }
        });


        VideoDetails();
        ThisMayAlsoLikeVideos();


        reelslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  Intent in = new Intent(getApplicationContext(), ReelsplayActivity.class);
                in.putExtra("url", reels_url);
                startActivity(in);
*/

            }
        });


        view = getLayoutInflater().inflate(R.layout.show_video_acess, null);
        alert = new AlertDialog.Builder(OnlinePlayerActivity.this);
        custom_dialog = alert.create();
        custom_dialog.setView(view);

        CardView access = view.findViewById(R.id.access);
        TextView access_content = view.findViewById(R.id.access_content);

        access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                custom_dialog.cancel();
                Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, videoId);
                res.enqueue(new retrofit2.Callback<JSONResponse>() {
                    @Override
                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                        JSONResponse jsonResponse = response.body();
                        movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));
                        ppv_price = movie_detaildata.get(0).getPpv_price();
                    }

                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                        Log.d("Error", t.getMessage());
                    }
                });

                dialog = new Dialog(OnlinePlayerActivity.this);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialogbox);
                dialog.show();


                RadioButton radioButton = (RadioButton) dialog.findViewById(R.id.termsandcon);
                RadioButton radioButton1 = (RadioButton) dialog.findViewById(R.id.radiorazor);
                RadioButton radioButton2 = (RadioButton) dialog.findViewById(R.id.radiopaypal);
                RadioButton radioButton3 = (RadioButton) dialog.findViewById(R.id.radiostripe);
                RadioButton radioButton4 = (RadioButton) dialog.findViewById(R.id.termsandcons);
                Button btndialog = (Button) dialog.findViewById(R.id.proceed);
                RecyclerView wes = (RecyclerView) dialog.findViewById(R.id.wes);
                TextView pays = (TextView) dialog.findViewById(R.id.pay);


                Call<JSONResponse> callimgg = ApiClient.getInstance1().getApi().getPaymentDetails();
                callimgg.enqueue(new retrofit2.Callback<JSONResponse>() {
                    @Override
                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                        JSONResponse jsonResponse = response.body();


                        wes.setHasFixedSize(true);
                        wes.setLayoutManager(new GridLayoutManager(OnlinePlayerActivity.this, 1));

                        active_payment_settingsList = new ArrayList<>(Arrays.asList(jsonResponse.getActive_payment_settings()));
                        active_payment_settingsAdopter = new Active_Payment_settingsAdopter(active_payment_settingsList);
                        wes.setAdapter(active_payment_settingsAdopter);

                    }

                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                        Log.d("Error1", t.getMessage());
                    }
                });


                wes.addOnItemTouchListener(
                        new RecyclerItemClickListener(OnlinePlayerActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                if (active_payment_settingsList.size() > position) {
                                    if (active_payment_settingsList.get(position) != null) {

                                        pays.setText(active_payment_settingsList.get(position).getPayment_type());
                                    }

                                }
                            }
                        }));


                btndialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.cancel();
                        if (pays.getText().toString().equalsIgnoreCase("stripe")) {

                            View view = getLayoutInflater().inflate(R.layout.activity_payperview_payment_page, null);
                            BottomSheetDialog dialog1 = new BottomSheetDialog(OnlinePlayerActivity.this);
                            dialog1.setContentView(view);

                            CardMultilineWidget cardMultilineWidget = view.findViewById(R.id.card_input_widget);
                            Button paynow1 = (Button) view.findViewById(R.id.save_payment);
                            ProgressBar payment_progress = (ProgressBar) view.findViewById(R.id.payment_progress);
                            CheckBox check = (CheckBox) view.findViewById(R.id.check);
                            paynow1.setText("Pay " + ppv_price + ".00");

                            Call<JSONResponse> call = ApiClient.getInstance1().getApi().getPaymentDetails();
                            call.enqueue(new retrofit2.Callback<JSONResponse>() {
                                @Override
                                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                    JSONResponse jsonResponse = response.body();
                                    payment_settingslist = new ArrayList<>(Arrays.asList(jsonResponse.getPayment_settings()));
                                    if (payment_settingslist.get(0).getLive_mode().equalsIgnoreCase(String.valueOf(0))) {
                                        key = payment_settingslist.get(0).getTest_publishable_key();
                                    } else {
                                        key = payment_settingslist.get(0).getLive_publishable_key();
                                    }
                                }

                                @Override
                                public void onFailure(Call<JSONResponse> call, Throwable t) {
                                }
                            });
                            paynow1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    paynow1.setVisibility(View.GONE);
                                    payment_progress.setVisibility(View.VISIBLE);

                                    if (!check.isChecked()) {
                                        Toast.makeText(getApplicationContext(), "Accept Terms & Condition", Toast.LENGTH_LONG).show();
                                        paynow1.setVisibility(View.VISIBLE);
                                        payment_progress.setVisibility(View.GONE);
                                    } else {

                                        params = cardMultilineWidget.getPaymentMethodCreateParams();

                                        if (params == null) {
                                            return;
                                        }
                                        stripe = new Stripe(getApplicationContext(), key);
                                        stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
                                            @Override
                                            public void onSuccess(@NotNull PaymentMethod paymentMethod) {
                                                py_id = paymentMethod.id;
                                                Api.getClient().getAddPayperView1(user_id, videoId, py_id, "stripe", ppv_price, "Android", new Callback<Addpayperview>() {

                                                    @Override
                                                    public void success(Addpayperview addpayperview1, Response response) {

                                                        dialog1.cancel();
                                                        addpayperview = addpayperview1;
                                                        Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                                                        player.setPlayWhenReady(true);

                                                    }

                                                    @Override
                                                    public void failure(RetrofitError error) {

                                                        Toast.makeText(getApplicationContext(), "check your internet connection", Toast.LENGTH_LONG).show();
                                                        paynow1.setVisibility(View.VISIBLE);
                                                        payment_progress.setVisibility(View.GONE);
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onError(@NotNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "false", Toast.LENGTH_LONG).show();
                                                paynow1.setVisibility(View.VISIBLE);
                                                payment_progress.setVisibility(View.GONE);

                                            }
                                        });
                                    }
                                }
                            });

                            dialog1.show();
                            dialog.dismiss();
                        } else if (pays.getText().toString().equalsIgnoreCase("razorpay")) {

                            startPayment();
                            dialog.dismiss();

                        }

                    }

                });

            }
        });

    }

    private void showBottom(int count1) {
        if (count1 == 1) {
            if (isScreenLandscape) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                frameLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                isScreenLandscape = false;
                hide();
            }
            custom_dialog.show();
        }
        count++;
    }

    private void startPayment() {

        final Activity activity = this;
        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Runmawi");
            options.put("description", "Video On Rent");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            String payment = ppv_price;
            // amount is in paise so please multiple it by 100
            //Payment failed Invalid amount (should be passed in integer paise. Minimum value is 100 paise, i.e. ₹ 1)
            double total = Double.parseDouble(payment);
            total = total * 100;
            options.put("amount", total);
            JSONObject preFill = new JSONObject();
            preFill.put("email", "");
            preFill.put("contact", "");
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }


    @Override
    public void onPaymentSuccess(String s) {
        // payment successfull pay_DGU19rDsInjcF2

        Api.getClient().getAddPayperView1(user_id, videoId, "", "razorpay", ppv_price, "Android", new Callback<Addpayperview>() {

            @Override
            public void success(Addpayperview addpayperview1, Response response) {

                addpayperview = addpayperview1;
                Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                player.setPlayWhenReady(true);

            }

            @Override
            public void failure(RetrofitError error) {

                Toast.makeText(getApplicationContext(), "check your internet connction", Toast.LENGTH_LONG).show();

            }
        });


    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            Toast.makeText(this, "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
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

    private void ThisMayAlsoLikeVideos() {


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

    private void updateVideoQuality(String res) {

        Call<JSONResponse> updateurl = ApiClient.getInstance1().getApi().getMovieDetails(user_id, videoId);
        updateurl.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));
                if (movie_detaildata.get(0).getType() == null || movie_detaildata.get(0).getType().isEmpty()) {
                    if (res.equalsIgnoreCase("240")) {
                        urlll = movie_detaildata.get(0).getVideo_url() + movie_detaildata.get(0).getPath() + "_0_250" + ".m3u8";
                    } else if (res.equalsIgnoreCase("360")) {
                        urlll = movie_detaildata.get(0).getVideo_url() + movie_detaildata.get(0).getPath() + "_1_600" + ".m3u8";
                    } else if (res.equalsIgnoreCase("480")) {
                        urlll = movie_detaildata.get(0).getVideo_url() + movie_detaildata.get(0).getPath() + "_2_1000" + ".m3u8";
                    } else if (res.equalsIgnoreCase("720")) {
                        urlll = movie_detaildata.get(0).getVideo_url() + movie_detaildata.get(0).getPath() + "_3_2500" + ".m3u8";
                    } else {
                        urlll = movie_detaildata.get(0).getVideo_url() + movie_detaildata.get(0).getPath() + ".m3u8";
                    }

                } else if (movie_detaildata.get(0).getType().equalsIgnoreCase("m3u8_url")) {
                    urlll = movie_detaildata.get(0).getM3u8_url();
                } else {
                    if (movie_detaildata.get(0).getMp4_url() == null || movie_detaildata.get(0).getMp4_url().isEmpty()) {
                        urlll = movie_detaildata.get(0).getTrailer();
                    } else {
                        urlll = movie_detaildata.get(0).getMp4_url();
                    }
                }

                mediaSource = buildMediaSource(Uri.parse(urlll));
               // player.prepare(mediaSource);
               // player.addListener(new PlayerEventListener());
                player.setPlayWhenReady(startAutoPlay);
            //    player.addAnalyticsListener(new EventLogger(trackSelector));
                playerView.setPlayer(player);
                player.seekTo(curr_time);
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

            }
        });

    }

    private void VideoDetails() {

        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, videoId);
        res.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));
                moviesubtitlesdata = new ArrayList<>(Arrays.asList(jsonResponse.getmoviesubtitles()));

                if (jsonResponse.getmoviesubtitles().length == 0) {

                    //Toast.makeText(getApplicationContext(), "No subtitles available", Toast.LENGTH_LONG).show();
                }

                if (movie_detaildata.get(0).getType() == null || movie_detaildata.get(0).getType().isEmpty()) {
                    urlll = movie_detaildata.get(0).getVideo_url() + movie_detaildata.get(0).getPath() + ".m3u8";
                } else if (movie_detaildata.get(0).getType().equalsIgnoreCase("m3u8_url")) {
                    urlll = movie_detaildata.get(0).getM3u8_url();
                } else {
                    if (movie_detaildata.get(0).getMp4_url() == null || movie_detaildata.get(0).getMp4_url().isEmpty()) {
                        urlll = movie_detaildata.get(0).getTrailer();
                    } else {
                        urlll = movie_detaildata.get(0).getMp4_url();
                    }
                }

                shareurl = jsonResponse.getShareurl();
                shareurl = jsonResponse.getShareurl();

                videoads = jsonResponse.getVideoads();
                genre2.setText(jsonResponse.getMain_genre());

                nxtvidid = jsonResponse.getVideonext();
                nextvidurl = jsonResponse.getNext_url();

                String userprofile = movie_detaildata.get(0).getImage_url();
                Picasso.get().load(userprofile).into(trailerImage);
                Picasso.get().load(userprofile).into(reels_image);
                //  skip_intro = Integer.parseInt(movie_detaildata.get(0).getSkip_intro()) *1000;
                // skip_recap = Integer.parseInt(movie_detaildata.get(0).getSkip_recap()) * 1000;

                videotext.setText(movie_detaildata.get(0).getDescription());

                if (movie_detaildata.get(0).getDescription() == null || movie_detaildata.get(0).getDescription().equalsIgnoreCase("") || movie_detaildata.get(0).getDescription().equalsIgnoreCase("0")) {
                    videotext.setVisibility(View.GONE);
                } else {
                    videotext.setVisibility(View.VISIBLE);

                }


             /*   if(movie_detaildata.get(0).getIntro_start_time() == null || movie_detaildata.get(0).getIntro_end_time() == null)
                {
                   skip_intro_end = 0;
                   skip_intro_start = 0;

                }
                else
                {
                    skip_intro_start = Integer.parseInt(movie_detaildata.get(0).getIntro_start_time())* 1000;
                    skip_intro_end = Integer.parseInt(movie_detaildata.get(0).getIntro_end_time()) *1000;
                }

                if(movie_detaildata.get(0).getRecap_start_time() == null || movie_detaildata.get(0).getRecap_end_time() == null) {

                    skip_recap_start = 0;
                    skip_recap_end = 0;
                }
                else
                {
                    skip_recap_start = Integer.parseInt(movie_detaildata.get(0).getRecap_start_time()) * 1000;
                    skip_recap_end = Integer.parseInt(movie_detaildata.get(0).getIntro_end_time()) * 1000;

                }
*/

                if (jsonResponse.getWishlist().equalsIgnoreCase("true")) {

                    watchlistaddedimg.setVisibility(View.VISIBLE);
                    watchlistimg.setVisibility(View.GONE);
                } else {
                    watchlistaddedimg.setVisibility(View.GONE);
                    watchlistimg.setVisibility(View.VISIBLE);

                }

                if (jsonResponse.getWatchlater().equalsIgnoreCase("true")) {
                    watchlateraddedimg.setVisibility(View.VISIBLE);
                    watchlaterimg.setVisibility(View.GONE);

                } else {

                    watchlateraddedimg.setVisibility(View.GONE);
                    watchlaterimg.setVisibility(View.VISIBLE);

                }


                language1.setText(jsonResponse.getLanguages());
                year.setText(movie_detaildata.get(0).getYear());
                videotitle1.setText(movie_detaildata.get(0).getTitle());
                //    urlll = movie_detaildata.get(0).getVideo_url() + movie_detaildata.get(0).getPath();


                if (jsonResponse.getCurr_time().contains(".")) {


                    String xx = jsonResponse.getCurr_time();
                    String[] splited = xx.split("\\.");
                    String xxy = splited[0];
                    curr_time = Long.valueOf(xxy) * 1000;

                } else {
                    curr_time = Long.valueOf(jsonResponse.getCurr_time());

                }


                //urlll = movie_detaildata.get(0).getMp4_url();


                if (jsonResponse.getmoviesubtitles().length == 0) {
                    //  Toast.makeText(getApplicationContext(),"No subtitle found",Toast.LENGTH_LONG).show();
                } else {

                    suburl = moviesubtitlesdata.get(0).getUrl();

                    subUri = Uri.parse(suburl);


                }
                if (subtitle_on.getVisibility() == View.VISIBLE) {

                    if (jsonResponse.getmoviesubtitles().length == 0) {

                        Toast.makeText(getApplicationContext(), "No subtitles available", Toast.LENGTH_LONG).show();
                    } else {
                        MediaSource contentMediaSource = buildMediaSource1(Uri.parse(urlll));
                        MediaSource[] mediaSources = new MediaSource[2]; //The Size must change depending on the Uris
                        mediaSources[0] = contentMediaSource; // uri


                      /*  SingleSampleMediaSource subtitleSource = new SingleSampleMediaSource(subUri, dataSourceFactory,
                                Format.createTextSampleFormat(null, MimeTypes.APPLICATION_TTML, Format.NO_VALUE, "en", null),
                                C.TIME_UNSET);

                        mediaSources[1] = subtitleSource;*/

                        MediaSource mediaSource = new MergingMediaSource(mediaSources);

                        player.prepare();
                        player.setPlayWhenReady(startAutoPlay);


                    }

                } else {

                    if (current_time_update != 0) {
                        mediaSource = buildMediaSource(Uri.parse(urlll));
                      //  player.prepare(mediaSource);
                   //     player.addListener(new PlayerEventListener());
                        player.setPlayWhenReady(startAutoPlay);
                    //    player.addAnalyticsListener(new EventLogger(trackSelector));
                        playerView.setPlayer(player);
                        player.seekTo(current_time_update);
                    } else {
                        if (continuee == null) {
                            mediaSource = buildMediaSource(Uri.parse(urlll));
                          //  player.prepare(mediaSource);
                       //     player.addListener(new PlayerEventListener());
                            player.setPlayWhenReady(startAutoPlay);
                           // player.addAnalyticsListener(new EventLogger(trackSelector));
                            playerView.setPlayer(player);
                        } else if (continuee.equalsIgnoreCase("1")) {
                            mediaSource = buildMediaSource(Uri.parse(urlll));
                           // player.prepare(mediaSource);
                          //  player.addListener(new PlayerEventListener());
                            player.setPlayWhenReady(startAutoPlay);
                           // player.addAnalyticsListener(new EventLogger(trackSelector));
                            playerView.setPlayer(player);
                            player.seekTo(curr_time);
                        } else {
                            mediaSource = buildMediaSource(Uri.parse(urlll));
                          //  player.prepare(mediaSource);
                        //    player.addListener(new PlayerEventListener());
                            player.setPlayWhenReady(startAutoPlay);
                           // player.addAnalyticsListener(new EventLogger(trackSelector));
                            playerView.setPlayer(player);
                        }
                    }

                }


                //               ha   = new Handler();
/*
                 has = new Runnable() {

                    @Override
                    public void run() {


                        if(player == null)
                        {

                        }

                        else if (player.getCurrentPosition() > skip_intro_start && player.getCurrentPosition() < skip_intro_end) {

                            skipintro.setVisibility(View.VISIBLE);
                            skipintro.setText("Skip Intro");



                        }

                        else
                         {

                                skipintro.setVisibility(View.GONE);

                            }


                        if(player == null)
                        {

                        }

                            else if (player.getCurrentPosition() > skip_recap_start  && player.getCurrentPosition() < skip_recap_end) {

                                skipintro.setText("Skip Recap");
                                skipintro.setVisibility(View.VISIBLE);



                            }

                            else
                            {


                                skipintro.setVisibility(View.GONE);


                            }

                            if (player.getCurrentPosition() > skip_recap_end || player.getCurrentPosition() > skip_intro_end) {


                               ha.removeCallbacks(has);
                                skipintro.setVisibility(View.GONE);




                            }

                            else
                            {

                            }

                            ha.postDelayed(has,1000);

                    }
                };
*/

                //              ha.post(has);
//                ha.removeCallbacks(has);








/*
                    ha.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            //call function

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
                                            Toast.makeText(OnlinePlayerActivity.this, error.toString(), Toast.LENGTH_LONG).show();

                                        }
                                    });





                            if(player == null)
                            {

                            }

                            else if (player.getCurrentPosition() > 5000 && player.getCurrentPosition() < 10000) {

                                skipintro.setVisibility(View.VISIBLE);
                                exoPlay.setVisibility(View.GONE);
                                exoPause.setVisibility(View.GONE);
                                imgBwd.setVisibility(View.GONE);
                                imgFwd.setVisibility(View.GONE);
                                player.getPlaybackState();


                            }

                            else
                            {

/*     skipintro.setVisibility(View.GONE);
                                exoPlay.setVisibility(View.VISIBLE);
                                exoPause.setVisibility(View.VISIBLE);
                                imgBwd.setVisibility(View.VISIBLE);
                                imgFwd.setVisibility(View.VISIBLE);
                                player.getPlaybackState();*//*


                            }


                            if(player == null)
                            {

                            }


                           else if (player.getCurrentPosition() > 10000) {


                                skipintro.setVisibility(View.GONE);
                                exoPlay.setVisibility(View.VISIBLE);
                                exoPause.setVisibility(View.VISIBLE);
                                imgBwd.setVisibility(View.VISIBLE);
                                imgFwd.setVisibility(View.VISIBLE);
                                player.getPlaybackState();



                            }

                            else
                            {

                            }



                        }
                    }, 1000);
*/


/*

                if(player.getCurrentPosition() > 3000 ) {

                    skipintro.setVisibility(View.VISIBLE);
                    imgBwd.setVisibility(View.GONE);
                    imgFwd.setVisibility(View.GONE);
                    exoPause.setVisibility(View.GONE);
                    exoPlay.setVisibility(View.GONE);

                }

*/


            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });


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

                                                if (downloadFromTracker.getPercentDownloaded() != -1) {
                                                    progressBarPercentage.setVisibility(View.VISIBLE);
                                                    progressBarPercentage.setProgress(Integer.parseInt(AppUtil.floatToPercentage(downloadFromTracker.getPercentDownloaded()).replace("%", "")));
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
        info = (ImageView) findViewById(R.id.infobutton);
        infolayout = (LinearLayout) findViewById(R.id.infolayout);
        infotext = (TextView) findViewById(R.id.infotext);
        autoplayswitch = (Switch) findViewById(R.id.autoplayswitch);
        exoPause = findViewById(R.id.exo_pause);
        subtitle_on = findViewById(R.id.img_subtitle);
        subtitle_off = findViewById(R.id.img_subtitle_off);

        exoreply = findViewById(R.id.exo_reply12);
        imgFwd = findViewById(R.id.img_fwd);
        tvPlayerCurrentTime = findViewById(R.id.tv_player_current_time);
        free_time = findViewById(R.id.free_timer);
        exoTimebar = findViewById(R.id.exo_progress);
        exoProgressbar = findViewById(R.id.loading_exoplayer);
        tvPlayerEndTime = findViewById(R.id.tv_player_end_time);
        imgSetting = findViewById(R.id.img_setting);
        imgFullScreenEnterExit = findViewById(R.id.img_full_screen_enter_exit);
        imgFullScreenEnterExit.setOnClickListener(this);
        resize_icon = findViewById(R.id.resize_icon);
        resize_icon.setOnClickListener(this);
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


        skipintro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                skipintro.setVisibility(View.GONE);
                exoPlay.setVisibility(View.VISIBLE);
                exoPause.setVisibility(View.VISIBLE);
                imgBwd.setVisibility(View.VISIBLE);
                imgFwd.setVisibility(View.VISIBLE);
                player.seekTo(25000);


            }
        });

   /*     if(player.isPlaying())
        {
         mediaplayer.pause();
        }
        else
        {

        }*/

        subtitle_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, videoId);
                res.enqueue(new retrofit2.Callback<JSONResponse>() {
                    @Override
                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                        JSONResponse jsonResponse = response.body();
                        moviesubtitlesdata = new ArrayList<>(Arrays.asList(jsonResponse.getmoviesubtitles()));


                        if (jsonResponse.getmoviesubtitles().length == 0) {
                            Toast.makeText(getApplicationContext(), "No subtitle found", Toast.LENGTH_LONG).show();
                        } else {
                            subtitleDialog(OnlinePlayerActivity.this);

                        }
                    }

                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable t) {

                    }
                });


            }
        });

        subtitle_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, videoId);
                res.enqueue(new retrofit2.Callback<JSONResponse>() {
                    @Override
                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                        JSONResponse jsonResponse = response.body();
                        moviesubtitlesdata = new ArrayList<>(Arrays.asList(jsonResponse.getmoviesubtitles()));


                        if (jsonResponse.getmoviesubtitles().length == 0) {
                            Toast.makeText(getApplicationContext(), "No subtitle found", Toast.LENGTH_LONG).show();
                        } else {
                            subtitleDialog(OnlinePlayerActivity.this);

                        }
                    }

                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable t) {

                    }
                });


            }
        });
    }


    private void subtitleDialog(OnlinePlayerActivity onlineplayerActivity) {


        dialog = new Dialog(OnlinePlayerActivity.this);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.subtitle_recycler);

        RadioButton subOff = (RadioButton) dialog.findViewById(R.id.subtitle_off);
        RadioButton subon = (RadioButton) dialog.findViewById(R.id.subtitle_on_radio);
        TextView sub_settings = (TextView) dialog.findViewById(R.id.subtitle_settings);
        RecyclerView subtitlelang = (RecyclerView) dialog.findViewById(R.id.subtitlelang);
        RecyclerView.LayoutManager subtiitleManeger;


        subtitleLangadopter = new subtitleLangAdopter(moviesubtitlesdata, this);
        subtiitleManeger = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        subtitlelang.setHasFixedSize(true);
        subtitlelang.setLayoutManager(subtiitleManeger);
        subtitlelang.setAdapter(subtitleLangadopter);


        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, videoId);
        res.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                moviesubtitlesdata = new ArrayList<>(Arrays.asList(jsonResponse.getmoviesubtitles()));
                subtitleLangadopter = new subtitleLangAdopter(moviesubtitlesdata);
                subtitlelang.setAdapter(subtitleLangadopter);

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });


        subtitlelang.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (moviesubtitlesdata.size() > position) {
                            if (moviesubtitlesdata.get(position) != null) {

                                double position2 = player.getCurrentPosition();

                                subUri = Uri.parse(moviesubtitlesdata.get(position).getUrl());

                                MediaSource contentMediaSource = buildMediaSource1(Uri.parse(urlll));
                                MediaSource[] mediaSources = new MediaSource[2]; //The Size must change depending on the Uris
                                mediaSources[0] = contentMediaSource; // uri
                                //Add subtitles
                              /*  SingleSampleMediaSource subtitleSource = new SingleSampleMediaSource(subUri, dataSourceFactory,
                                        Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP, Format.NO_VALUE, "en", null),
                                        C.TIME_UNSET);

                                mediaSources[1] = subtitleSource;*/

                                MediaSource mediaSource = new MergingMediaSource(mediaSources);

                              //  player.prepare(mediaSource);
                                player.setPlayWhenReady(startAutoPlay);
                                player.seekTo((long) position2);

                            }
                        }
                    }
                }));


        if (subon.isChecked()) {
            sub_settings.setVisibility(View.VISIBLE);
            subtitlelang.setVisibility(View.VISIBLE);
        } else {
            sub_settings.setVisibility(View.GONE);
            subtitlelang.setVisibility(View.GONE);
        }


        if (subtitle_on.getVisibility() == View.VISIBLE) {

            subon.setChecked(true);
            sub_settings.setVisibility(View.VISIBLE);
            subtitlelang.setVisibility(View.VISIBLE);
            SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
            editor.putString(sharedpreferences.subtitles, String.valueOf(1));
            editor.apply();
            editor.commit();

        } else {
            subOff.setChecked(true);
            SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
            editor.putString(sharedpreferences.subtitles, String.valueOf(0));
            editor.apply();
            editor.commit();

        }


        sub_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showsubtitlestyles();
                dialog.cancel();


            }
        });


        RadioGroup radgrp = (RadioGroup) dialog.findViewById(R.id.radiogroup);


        radgrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {


                    case R.id.subtitle_off:


                        double position1 = player.getCurrentPosition();
                        subtitle_off.setVisibility(View.VISIBLE);
                        subtitle_on.setVisibility(View.GONE);
                        sub_settings.setVisibility(View.GONE);
                        subtitlelang.setVisibility(View.GONE);


                        mediaSource = buildMediaSource(Uri.parse(urlll));
                       // player.prepare(mediaSource);
                      //  player.addListener(new PlayerEventListener());
                        player.setPlayWhenReady(startAutoPlay);
                    //    player.addAnalyticsListener(new EventLogger(trackSelector));
                        playerView.setPlayer(player);
                        player.seekTo((long) position1);


                        break;

                    case R.id.subtitle_on_radio:


                        subtitle_off.setVisibility(View.GONE);
                        subtitle_on.setVisibility(View.VISIBLE);
                        sub_settings.setVisibility(View.VISIBLE);
                        subtitlelang.setVisibility(View.VISIBLE);

                        double position2 = player.getCurrentPosition();

                        MediaSource contentMediaSource = buildMediaSource1(Uri.parse(urlll));
                        MediaSource[] mediaSources = new MediaSource[2]; //The Size must change depending on the Uris
                        mediaSources[0] = contentMediaSource; // uri
                        //Add subtitles
//                        SingleSampleMediaSource subtitleSource = new SingleSampleMediaSource(subUri, dataSourceFactory,
//                                Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP, Format.NO_VALUE, "en", null),
//                                C.TIME_UNSET);
//
//                        mediaSources[1] = subtitleSource;

                        MediaSource mediaSource = new MergingMediaSource(mediaSources);

                       // player.prepare(mediaSource);
                        player.setPlayWhenReady(startAutoPlay);
                        player.seekTo((long) position2);


                        break;


                }
            }
        });


        dialog.show();

    }

    private void showsubtitlestyles() {


        dialogsubformat = new Dialog(OnlinePlayerActivity.this);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogsubformat.setCancelable(true);
        dialogsubformat.setContentView(R.layout.subtitle_format);


        LinearLayout font1 = (LinearLayout) dialogsubformat.findViewById(R.id.font1);
        LinearLayout font2 = (LinearLayout) dialogsubformat.findViewById(R.id.font2);
        LinearLayout font3 = (LinearLayout) dialogsubformat.findViewById(R.id.font3);
        LinearLayout font4 = (LinearLayout) dialogsubformat.findViewById(R.id.font4);

        CheckBox fontcheck1 = (CheckBox) dialogsubformat.findViewById(R.id.fontcheck1);
        CheckBox fontcheck2 = (CheckBox) dialogsubformat.findViewById(R.id.fontcheck2);
        CheckBox fontcheck3 = (CheckBox) dialogsubformat.findViewById(R.id.fontcheck3);
        CheckBox fontcheck4 = (CheckBox) dialogsubformat.findViewById(R.id.fontcheck4);


        LinearLayout fontback1 = (LinearLayout) dialogsubformat.findViewById(R.id.fontback1);
        LinearLayout fontback2 = (LinearLayout) dialogsubformat.findViewById(R.id.fontback2);
        LinearLayout fontback3 = (LinearLayout) dialogsubformat.findViewById(R.id.fontback3);
        LinearLayout fontback4 = (LinearLayout) dialogsubformat.findViewById(R.id.fontback4);


        LinearLayout format1 = (LinearLayout) dialogsubformat.findViewById(R.id.format1);
        LinearLayout format2 = (LinearLayout) dialogsubformat.findViewById(R.id.format2);
        LinearLayout format3 = (LinearLayout) dialogsubformat.findViewById(R.id.format3);
        LinearLayout format4 = (LinearLayout) dialogsubformat.findViewById(R.id.format4);


        if (subchecked1 == null || subchecked2 == null || subchecked3 == null || subchecked4 == null) {

        } else if (subchecked1.equalsIgnoreCase("1")) {

            fontback1.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.subback));
            fontback2.setBackgroundResource(0);
            fontback3.setBackgroundResource(0);
            fontback4.setBackgroundResource(0);

            playerView.getSubtitleView().setApplyEmbeddedStyles(false);
            playerView.getSubtitleView().setApplyEmbeddedFontSizes(false);
            // playerView.getSubtitleView().setStyle(new CaptionStyleCompat(Color.WHITE,Color.BLACK , Color.YELLOW, CaptionStyleCompat.EDGE_TYPE_OUTLINE,Color.BLACK,null));
            playerView.getSubtitleView().setFixedTextSize(TypedValue.COMPLEX_UNIT_PX, 20);


        } else if (subchecked2.equalsIgnoreCase("1")) {
            fontback2.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.subback));
            fontback1.setBackgroundResource(0);
            fontback3.setBackgroundResource(0);
            fontback4.setBackgroundResource(0);

            playerView.getSubtitleView().setApplyEmbeddedStyles(false);
            playerView.getSubtitleView().setApplyEmbeddedFontSizes(false);
            // playerView.getSubtitleView().setStyle(new CaptionStyleCompat(Color.WHITE,Color.BLACK , Color.YELLOW, CaptionStyleCompat.EDGE_TYPE_OUTLINE,Color.BLACK,null));
            playerView.getSubtitleView().setFixedTextSize(TypedValue.COMPLEX_UNIT_PX, 40);

        } else if (subchecked3.equalsIgnoreCase("1")) {

            fontback3.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.subback));
            fontback2.setBackgroundResource(0);
            fontback4.setBackgroundResource(0);
            fontback1.setBackgroundResource(0);

            playerView.getSubtitleView().setApplyEmbeddedStyles(false);
            playerView.getSubtitleView().setApplyEmbeddedFontSizes(false);
            // playerView.getSubtitleView().setStyle(new CaptionStyleCompat(Color.WHITE, Color.BLACK , Color.YELLOW, CaptionStyleCompat.EDGE_TYPE_OUTLINE,Color.BLACK,null));
            playerView.getSubtitleView().setFixedTextSize(TypedValue.COMPLEX_UNIT_PX, 60);

        } else if (subchecked4.equalsIgnoreCase("1")) {

            fontback4.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.subback));
            fontback1.setBackgroundResource(0);
            fontback2.setBackgroundResource(0);
            fontback3.setBackgroundResource(0);

            playerView.getSubtitleView().setApplyEmbeddedStyles(false);
            playerView.getSubtitleView().setApplyEmbeddedFontSizes(false);
            // playerView.getSubtitleView().setStyle(new CaptionStyleCompat(Color.WHITE,Color.BLACK , Color.YELLOW, CaptionStyleCompat.EDGE_TYPE_OUTLINE,Color.BLACK,null));
            playerView.getSubtitleView().setFixedTextSize(TypedValue.COMPLEX_UNIT_PX, 80);
        } else {


        }

        font1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(getApplicationContext(), "" + subchecked1, Toast.LENGTH_LONG).show();

                if (fontcheck1.isChecked()) {
                    fontcheck1.setChecked(false);
                    fontcheck2.setChecked(false);
                    fontcheck3.setChecked(false);
                    fontcheck4.setChecked(false);


                    SharedPreferences.Editor editor1 = getApplicationContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                    editor1.putString(sharedpreferences.subcheck1, "1");
                    editor1.apply();
                    editor1.commit();


                } else {
                    fontcheck1.setChecked(true);
                    fontcheck2.setChecked(false);
                    fontcheck3.setChecked(false);
                    fontcheck4.setChecked(false);

                    SharedPreferences.Editor editor1 = getApplicationContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                    editor1.putString(sharedpreferences.subcheck1, "0");
                    editor1.apply();
                    editor1.commit();

                }


                fontback1.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.subback));
                fontback2.setBackgroundResource(0);
                fontback3.setBackgroundResource(0);
                fontback4.setBackgroundResource(0);


                playerView.getSubtitleView().setApplyEmbeddedStyles(false);
                playerView.getSubtitleView().setApplyEmbeddedFontSizes(false);
                // playerView.getSubtitleView().setStyle(new CaptionStyleCompat(Color.WHITE,Color.BLACK , Color.YELLOW, CaptionStyleCompat.EDGE_TYPE_OUTLINE,Color.BLACK,null));
                playerView.getSubtitleView().setFixedTextSize(TypedValue.COMPLEX_UNIT_PX, 20);

                //   dialogsubformat.hide();

            }
        });

        font2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (fontcheck2.isChecked()) {
                    fontcheck1.setChecked(false);
                    fontcheck2.setChecked(false);
                    fontcheck3.setChecked(false);
                    fontcheck4.setChecked(false);

                    SharedPreferences.Editor editor2 = getApplicationContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                    editor2.putString(sharedpreferences.subcheck2, "1");
                    editor2.apply();
                    editor2.commit();


                } else {
                    fontcheck2.setChecked(true);
                    fontcheck1.setChecked(false);
                    fontcheck3.setChecked(false);
                    fontcheck4.setChecked(false);

                    SharedPreferences.Editor editor2 = getApplicationContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                    editor2.putString(sharedpreferences.subcheck2, "0");
                    editor2.apply();
                    editor2.commit();

                }


                fontback2.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.subback));
                fontback1.setBackgroundResource(0);
                fontback3.setBackgroundResource(0);
                fontback4.setBackgroundResource(0);

                playerView.getSubtitleView().setApplyEmbeddedStyles(false);
                playerView.getSubtitleView().setApplyEmbeddedFontSizes(false);
                // playerView.getSubtitleView().setStyle(new CaptionStyleCompat(Color.WHITE,Color.BLACK , Color.YELLOW, CaptionStyleCompat.EDGE_TYPE_OUTLINE,Color.BLACK,null));
                playerView.getSubtitleView().setFixedTextSize(TypedValue.COMPLEX_UNIT_PX, 40);

                //      dialogsubformat.hide();

            }
        });

        font3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (fontcheck3.isChecked()) {
                    fontcheck1.setChecked(false);
                    fontcheck2.setChecked(false);
                    fontcheck3.setChecked(false);
                    fontcheck4.setChecked(false);

                    SharedPreferences.Editor editor3 = getApplicationContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                    editor3.putString(sharedpreferences.subcheck3, "1");
                    editor3.apply();
                    editor3.commit();


                } else {
                    fontcheck1.setChecked(false);
                    fontcheck2.setChecked(false);
                    fontcheck3.setChecked(true);
                    fontcheck4.setChecked(false);

                    SharedPreferences.Editor editor3 = getApplicationContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                    editor3.putString(sharedpreferences.subcheck3, "0");
                    editor3.apply();
                    editor3.commit();

                }


                fontback3.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.subback));
                fontback2.setBackgroundResource(0);
                fontback4.setBackgroundResource(0);
                fontback1.setBackgroundResource(0);

                playerView.getSubtitleView().setApplyEmbeddedStyles(false);
                playerView.getSubtitleView().setApplyEmbeddedFontSizes(false);
                // playerView.getSubtitleView().setStyle(new CaptionStyleCompat(Color.WHITE,Color.BLACK , Color.YELLOW, CaptionStyleCompat.EDGE_TYPE_OUTLINE,Color.BLACK,null));
                playerView.getSubtitleView().setFixedTextSize(TypedValue.COMPLEX_UNIT_PX, 60);

                ///   dialogsubformat.hide();

            }
        });

        font4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fontcheck4.isChecked()) {
                    fontcheck1.setChecked(false);
                    fontcheck2.setChecked(false);
                    fontcheck3.setChecked(false);
                    fontcheck4.setChecked(false);

                    SharedPreferences.Editor editor4 = getApplicationContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                    editor4.putString(sharedpreferences.subcheck4, "1");
                    editor4.apply();
                    editor4.commit();


                } else {
                    fontcheck1.setChecked(false);
                    fontcheck2.setChecked(false);
                    fontcheck3.setChecked(false);
                    fontcheck4.setChecked(true);

                    SharedPreferences.Editor editor4 = getApplicationContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                    editor4.putString(sharedpreferences.subcheck4, "0");
                    editor4.apply();
                    editor4.commit();

                }

                fontback4.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.subback));
                fontback1.setBackgroundResource(0);
                fontback2.setBackgroundResource(0);
                fontback3.setBackgroundResource(0);

                playerView.getSubtitleView().setApplyEmbeddedStyles(false);
                playerView.getSubtitleView().setApplyEmbeddedFontSizes(false);
                // playerView.getSubtitleView().setStyle(new CaptionStyleCompat(Color.WHITE,Color.BLACK , Color.YELLOW, CaptionStyleCompat.EDGE_TYPE_OUTLINE,Color.BLACK,null));
                playerView.getSubtitleView().setFixedTextSize(TypedValue.COMPLEX_UNIT_PX, 80);

                //     dialogsubformat.hide();

            }
        });


        format1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                playerView.getSubtitleView().setApplyEmbeddedStyles(false);
                playerView.getSubtitleView().setApplyEmbeddedFontSizes(false);
             //   playerView.getSubtitleView().setStyle(new CaptionStyleCompat(Color.WHITE, Color.BLACK, Color.TRANSPARENT, CaptionStyleCompat.EDGE_TYPE_OUTLINE, Color.BLACK, null));
                //  playerView.getSubtitleView().setFixedTextSize(TypedValue.COMPLEX_UNIT_PX, 20);

                //  dialogsubformat.hide();

            }
        });


        format2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                playerView.getSubtitleView().setApplyEmbeddedStyles(false);
                playerView.getSubtitleView().setApplyEmbeddedFontSizes(false);
//                playerView.getSubtitleView().setStyle(new CaptionStyleCompat(Color.BLACK, Color.TRANSPARENT, Color.TRANSPARENT, CaptionStyleCompat.EDGE_TYPE_OUTLINE, Color.BLACK, null));
                //   playerView.getSubtitleView().setFixedTextSize(TypedValue.COMPLEX_UNIT_PX, 20);

                // dialogsubformat.hide();

            }
        });


        format3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                playerView.getSubtitleView().setApplyEmbeddedStyles(false);
                playerView.getSubtitleView().setApplyEmbeddedFontSizes(false);
//                playerView.getSubtitleView().setStyle(new CaptionStyleCompat(Color.YELLOW, Color.BLACK, Color.TRANSPARENT, CaptionStyleCompat.EDGE_TYPE_OUTLINE, Color.BLACK, null));
                //  playerView.getSubtitleView().setFixedTextSize(TypedValue.COMPLEX_UNIT_PX, 20);

                //  dialogsubformat.hide();

            }
        });


        format4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                playerView.getSubtitleView().setApplyEmbeddedStyles(false);
                playerView.getSubtitleView().setApplyEmbeddedFontSizes(false);
//                playerView.getSubtitleView().setStyle(new CaptionStyleCompat(Color.WHITE, Color.TRANSPARENT, Color.TRANSPARENT, CaptionStyleCompat.EDGE_TYPE_OUTLINE, Color.BLACK, null));
                // playerView.getSubtitleView().setFixedTextSize(TypedValue.COMPLEX_UNIT_PX, 20);

                // dialogsubformat.hide();

            }
        });


        dialogsubformat.show();

    }


    public void prepareView() {
     /*   playerView.setLayoutParams(
                new PlayerView.LayoutParams(
                        // or ViewGroup.LayoutParams.WRAP_CONTENT
                        PlayerView.LayoutParams.MATCH_PARENT,
                        // or ViewGroup.LayoutParams.WRAP_CONTENT,
                        ScreenUtils.convertDIPToPixels(OnlinePlayerActivity.this, PlayerView.LayoutParams.MATCH_PARENT)));
        */
        frameLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));


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

//        downloadTracker.addListener(this);


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
           // initializePlayer();
            setProgress();

            if (playerView != null) {
                playerView.onResume();
            }
        }
        VideoDetails();
        FullScreencall();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            if (playerView != null) {
                playerView.onPause();
            }
        }
        releasePlayer();
        try {
            current_time_update = player.getCurrentPosition();
        }catch (Exception e){

        }
    }

    @Override
    public void onStop() {
        super.onStop();
      //  downloadTracker.removeListener(this);
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
        //    imaAdsLoader.release();

    }


    // OnClickListener methods
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        updateTrackSelectorParameters();
        updateStartPosition();
//        outState.putParcelable(KEY_TRACK_SELECTOR_PARAMETERS, trackSelectorParameters);
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
            case R.id.img_setting://check resolution

                /*res_view = getLayoutInflater().inflate(R.layout.show_video_resolution, null);
                resolution_dialog = new AlertDialog.Builder(OnlinePlayerActivity.this);
                resolution_alert = resolution_dialog.create();
                resolution_alert.setView(res_view);


                RecyclerView res_recyclerview = res_view.findViewById(R.id.res_recyclerview);
                TextView auto = res_view.findViewById(R.id.auto);
                res_recyclerview.setLayoutManager(new LinearLayoutManager(OnlinePlayerActivity.this, RecyclerView.VERTICAL, false));
                res_recyclerview.setHasFixedSize(true);
                ArrayList<String> resList = new ArrayList<>();

                Call<JSONResponse> profileres = ApiClient.getInstance1().getApi().getUserprofile(user_id);
                profileres.enqueue(new retrofit2.Callback<JSONResponse>() {
                    @Override
                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> profile_response) {

                        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, videoId);
                        res.enqueue(new retrofit2.Callback<JSONResponse>() {
                            @Override
                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                                JSONResponse jsonResponse1 = response.body();
                                movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse1.getVideodetail()));

                                if (movie_detaildata.get(0).getAccess().equalsIgnoreCase("ppv")) {

                                    if (jsonResponse1.getPpv_video_status().equalsIgnoreCase("can_view")) {
                                        resList.add("240p");
                                        resList.add("360p");
                                        resList.add("480p");
                                        resList.add("720p");
                                    } else {
                                        resList.add("240p");
                                    }
                                } else {
                                    JSONResponse jsonResponse = profile_response.body();
                                    if (jsonResponse.getCurren_stripe_plan().equalsIgnoreCase("SVOD Basic")) {
                                        resList.add("240p");
                                        resList.add("360p");
                                        resList.add("480p");
                                    } else if (jsonResponse.getCurren_stripe_plan().equalsIgnoreCase("SVOD Premium")) {
                                        resList.add("240p");
                                        resList.add("360p");
                                        resList.add("480p");
                                        resList.add("720p");
                                    } else if (jsonResponse.getCurren_stripe_plan().equalsIgnoreCase("No Plan Found") || jsonResponse.getCurren_stripe_plan().isEmpty() || jsonResponse.getCurren_stripe_plan() == null) {
                                        resList.add("240p");
                                    }

                                    resolution_alert.show();
                                    resolution_alert.getWindow().setGravity(Gravity.RIGHT | Gravity.BOTTOM);
                                    resolution_alert.getWindow().setLayout(300, 530);
                                    ResolutionAdapter resolutionAdapter = new ResolutionAdapter(resList, OnlinePlayerActivity.this);
                                    res_recyclerview.setAdapter(resolutionAdapter);
                                    auto.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onFailure(Call<JSONResponse> call, Throwable t) {

                            }
                        });


                    }

                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable t) {

                    }
                });

                res_recyclerview.addOnItemTouchListener(
                        new RecyclerItemClickListener(OnlinePlayerActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {
                                if (resList.size() > position) {
                                    if (resList.get(position) != null) {

                                        resolution_alert.cancel();
                                        if (resList.get(position).equalsIgnoreCase("240p")) {
                                            resolution = "240";
                                        } else if (resList.get(position).equalsIgnoreCase("360p")) {//4
                                            resolution = "360";
                                        } else if (resList.get(position).equalsIgnoreCase("480p")) {
                                            resolution = "480";
                                        } else if (resList.get(position).equalsIgnoreCase("720p")) {
                                            resolution = "720";
                                        } else if (resList.get(position).equalsIgnoreCase("1080p")) {
                                            resolution = "1080";
                                        }
                                        //VideoDetails();
                                        curr_time = player.getCurrentPosition();
                                        updateVideoQuality(resolution);

                                    }
                                }
                            }
                        }));
                auto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateVideoQuality("240");
                        resolution_alert.cancel();
                    }
                });*/

//                MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
//                if (mappedTrackInfo != null) {
//                    if (!isShowingTrackSelectionDialog && TrackSelectionDialog.willHaveContent(trackSelector)) {
//                        isShowingTrackSelectionDialog = true;
//                      //  TrackSelectionDialog trackSelectionDialog = TrackSelectionDialog.createForTrackSelector(trackSelector, /*onDismissListener =*/  dismissedDialog -> isShowingTrackSelectionDialog = false);
//                        trackSelectionDialog.show(getSupportFragmentManager(),  /*tag=*/  null);
//                    }
//                }

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

    private void exoVideoDownloadDecision(ExoDownloadState exoDownloadState) {
        if (exoDownloadState == null || urlll.isEmpty()) {
            Toast.makeText(this, "Please, Tap Again", Toast.LENGTH_SHORT).show();

            return;
        }

        switch (exoDownloadState) {

            case DOWNLOAD_START:
                fetchDownloadOptions();

                break;

            case DOWNLOAD_PAUSE:

           //     downloadManager.addDownload(downloadTracker.getDownloadRequest(Uri.parse(urlll)), Download.STATE_STOPPED);

//                DownloadService.sendSetStopReason(
//                        OnlinePlayerActivity.this,
//                        DemoDownloadService.class,
//                        downloadTracker.getDownloadRequest(Uri.parse(urlll)).id,
//                        Download.STATE_STOPPED,
//                        /* foreground= */ false);

                break;

            case DOWNLOAD_RESUME:

            //    downloadManager.addDownload(downloadTracker.getDownloadRequest(Uri.parse(urlll)), Download.STOP_REASON_NONE);
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

//    private void exoButtonPrepareDecision() {
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
//        } else {
//            setCommonDownloadButton(ExoDownloadState.DOWNLOAD_START);
//        }
//    }


    private void fetchDownloadOptions() {
        trackKeys.clear();

        if (pDialog == null || !pDialog.isShowing()) {
            pDialog = new ProgressDialog(OnlinePlayerActivity.this);
            pDialog.setTitle(null);
            pDialog.setCancelable(false);
            pDialog.setMessage("Preparing Download Options...");
            pDialog.show();
        }

//
//        DownloadHelper downloadHelper = DownloadHelper.forDash(OnlinePlayerActivity.this, Uri.parse(urlll), dataSourceFactory, new DefaultRenderersFactory(OnlinePlayerActivity.this));
//
//
//        downloadHelper.prepare(new DownloadHelper.Callback() {
//            @Override
//            public void onPrepared(DownloadHelper helper) {
//                // Preparation completes. Now other DownloadHelper methods can be called.
//                myDownloadHelper = helper;
//                for (int i = 0; i < helper.getPeriodCount(); i++) {
//                    TrackGroupArray trackGroups = helper.getTrackGroups(i);
//                    for (int j = 0; j < trackGroups.length; j++) {
//                        TrackGroup trackGroup = trackGroups.get(j);
//                        for (int k = 0; k < trackGroup.length; k++) {
//                            Format track = trackGroup.getFormat(k);
//                            if (shouldDownload(track)) {
//                                trackKeys.add(new TrackKey(trackGroups, trackGroup, track));
//                            }
//                        }
//                    }
//                }
//
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.dismiss();
//                } else {
//
//                    Toast.makeText(getApplicationContext(), "d", Toast.LENGTH_SHORT).show();
//                    pDialog.dismiss();
//                }
//
//
//                optionsToDownload.clear();
//                showDownloadOptionsDialog(myDownloadHelper, trackKeys);
//            }
//
//            @Override
//            public void onPrepareError(DownloadHelper helper, IOException e) {
//
//
//                Log.d("sdsdd", String.valueOf(e));
//                Toast.makeText(getApplicationContext(), "failed" + e, Toast.LENGTH_LONG).show();
//                pDialog.dismiss();
//
//            }
//        });
    }

    private void showDownloadOptionsDialog(DownloadHelper helper, List<TrackKey> trackKeyss) {

        if (helper == null) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(OnlinePlayerActivity.this);
        builder.setTitle("Select Download Format");
        int checkedItem = 1;


        for (int i = 0; i < trackKeyss.size(); i++) {
            TrackKey trackKey = trackKeyss.get(i);
            long bitrate = trackKey.getTrackFormat().bitrate;
            long getInBytes = (bitrate * videoDurationInSeconds) / 8;
            String getInMb = AppUtil.formatFileSize(getInBytes);
            String videoResoultionDashSize = " " + trackKey.getTrackFormat().height + "      (" + getInMb + ")";
            optionsToDownload.add(i, videoResoultionDashSize);
        }

        // Initialize a new array adapter instance
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(
                OnlinePlayerActivity.this, // Context
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

//    @Override
//    public void preparePlayback() {
//        initializePlayer();
//    }
//
//
//    private void initializePlayer() {
//
//
//        TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory();
//        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(this, null, DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER);
//
//        trackSelector = new DefaultTrackSelector(trackSelectionFactory);
//        trackSelector.setParameters(trackSelectorParameters);
//        lastSeenTrackGroupArray = null;
//
//        DefaultAllocator defaultAllocator = new DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE);
//        DefaultLoadControl defaultLoadControl = new DefaultLoadControl(defaultAllocator,
//                DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
//                DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
//                DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS,
//                DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS,
//                DefaultLoadControl.DEFAULT_TARGET_BUFFER_BYTES,
//                DefaultLoadControl.DEFAULT_PRIORITIZE_TIME_OVER_SIZE_THRESHOLDS
//        );
//
//
//        player = ExoPlayerFactory.newSimpleInstance(/* context= */ OnlinePlayerActivity.this, renderersFactory, trackSelector, defaultLoadControl);
//        player.addListener(new PlayerEventListener());
//        player.setPlayWhenReady(startAutoPlay);
//        player.addAnalyticsListener(new EventLogger(trackSelector));
//        playerView.setPlayer(player);
//        playerView.setPlaybackPreparer(this);
//
//        /*if(videoads.equalsIgnoreCase(""))
//        {
//            mediaSource = buildMediaSource2(Uri.parse(urlll));
//        }
//        else
//        {
//            mediaSource = buildMediaSource(Uri.parse(urlll));
//        }
//
//        if(player != null){
//            player.prepare(mediaSource, false, true);
//        }*/
//
//
//        exoButtonPrepareDecision();
//
//        updateButtonVisibilities();
//        initBwd();
//        initFwd();
//
//    }

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
              //  imaAdsLoader.setPlayer(player);
               // DashMediaSource dashMediaSource = new DashMediaSource.Factory(dataSourceFactory)
                 //       .createMediaSource(uri);
//
//                return new AdsMediaSource(
//                        dashMediaSource,
//                        dataSourceFactory,
//                        imaAdsLoader,
//                        playerView);


            case C.TYPE_SS:

             //   imaAdsLoader.setPlayer(player);
             //   SsMediaSource ssMediaSource = new SsMediaSource.Factory(dataSourceFactory)
                  //      .createMediaSource(uri);

//                return new AdsMediaSource(
//                        ssMediaSource,
//                        dataSourceFactory,
//                        imaAdsLoader,
//                        playerView);

            case C.TYPE_HLS:

              //  imaAdsLoader.setPlayer(player);
//
//                HlsMediaSource hlsMediaSource = new HlsMediaSource.Factory(dataSourceFactory)
//                        .createMediaSource(uri);
//
//                return new AdsMediaSource(
//                        hlsMediaSource,
//                        dataSourceFactory,
//                        imaAdsLoader,
//                        playerView);


            case C.TYPE_OTHER:

               // imaAdsLoader.setPlayer(player);
              //  MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);

                //  return new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
//                return new AdsMediaSource(
//                        mediaSource,
//                        dataSourceFactory,
//                        imaAdsLoader,
//                        playerView);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    private MediaSource buildMediaSource2(Uri uri) {
        return buildMediaSource2(uri, null);
    }


    @SuppressWarnings("unchecked")
    private MediaSource buildMediaSource2(Uri uri, @Nullable String overrideExtension) {
        @C.ContentType int type = Util.inferContentType(uri, overrideExtension);
        switch (type) {
            case C.TYPE_DASH:
              //  imaAdsLoader.setPlayer(player);
//                DashMediaSource dashMediaSource = new DashMediaSource.Factory(dataSourceFactory)
//                        .createMediaSource(uri);
//
//                return new AdsMediaSource(
//                        dashMediaSource,
//                        dataSourceFactory,
//                        imaAdsLoader,
//                        playerView);


            case C.TYPE_SS:

             //   imaAdsLoader.setPlayer(player);
//                SsMediaSource ssMediaSource = new SsMediaSource.Factory(dataSourceFactory)
//                        .createMediaSource(uri);
//
//                return new AdsMediaSource(
//                        ssMediaSource,
//                        dataSourceFactory,
//                        imaAdsLoader,
//                        playerView);

            case C.TYPE_HLS:

              //  imaAdsLoader.setPlayer(player);

//                HlsMediaSource hlsMediaSource = new HlsMediaSource.Factory(dataSourceFactory)
//                        .createMediaSource(uri);
//
//                return new AdsMediaSource(
//                        hlsMediaSource,
//                        dataSourceFactory,
//                        imaAdsLoader,
//                        playerView);


            case C.TYPE_OTHER:

                //  imaAdsLoader.setPlayer(player);
                //   MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);

//                return new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
           /* return new AdsMediaSource(
                    mediaSource,
                    dataSourceFactory,
                    imaAdsLoader,
                    playerView);*/
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }


    /**
     * Returns a new DataSource factory.
     */
//    private DataSource.Factory buildDataSourceFactory() {
//        return ((AdaptiveExoplayer) getApplication()).buildDataSourceFactory();
//
//    }
//

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
                switch (player.getRepeatMode()) {
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

    private String TimeFormat(int timeMs) {
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = timeMs;

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


        videoId = savedInstanceState.getString("id");
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
        /*if (isScreenLandscape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


            frameLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            //   imgFullScreenEnterExit.setImageResource(R.drawable.exo_controls_fullscreen_enter);
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


                if (download.request.uri.toString().equals(urlll)) {

                    if (download.getPercentDownloaded() != -1) {
                        progressBarPercentage.setVisibility(View.VISIBLE);
                        progressBarPercentage.setProgress(Integer.parseInt(AppUtil.floatToPercentage(download.getPercentDownloaded()).replace("%", "")));
//                        tvDownloadProgressMb.setText(AppUtil.getProgressDisplayLine(download.getBytesDownloaded(),download.contentLength)+" MB");
//                        tvProgressPercentage.setText(AppUtil.floatToPercentage(download.getPercentDownloaded()));
                    }
                }

                progressBarPercentage.setVisibility(View.GONE);
                break;



        }

    }

    private void releasePlayer() {
        if (player != null) {

            long x = player.getDuration();
            long y = player.getCurrentPosition();


            if (x < 0) {
                finish();

            } else {


                int a = Integer.parseInt(String.valueOf(x));
                int b = Integer.parseInt(String.valueOf(y));


                int w = (b * 100 / a);


                Api.getClient().getContinueWatching(user_id, videoId, String.valueOf(player.getContentPosition()), String.valueOf(w), new Callback<ContinuWatching>() {

                    @Override
                    public void success(ContinuWatching continuWatching, Response response) {

                        continuWatching = continuWatching;

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });

            }
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
    public boolean onTouchEvent(MotionEvent event) {
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

        float centreX = playerView.getX() + playerView.getWidth() / 2;
        float centreY = playerView.getY() + playerView.getHeight() / 2;


        if (centreX > e.getX()) {
            player.seekTo(player.getCurrentPosition() - 10000);

        } else {
            player.seekTo(player.getCurrentPosition() + 10000);
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


//    private class PlayerEventListener implements Player.EventListener {
//
//        @Override
//        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//            switch (playbackState) {
//
//                case ExoPlayer.STATE_READY:
//
//                    exoProgressbar.setVisibility(View.GONE);
//                    break;
//
//                case ExoPlayer.STATE_BUFFERING:
//
//                    exoProgressbar.setVisibility(View.VISIBLE);
//                    break;
//
//                case ExoPlayer.STATE_ENDED:
//                    player.stop();
//
//
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            if (autoplay == null) {
//                                exoreply.setVisibility(View.VISIBLE);
//                                exoPause.setVisibility(View.GONE);
//                                exoPlay.setVisibility(View.GONE);
//                                imgBwd.setVisibility(View.GONE);
//                                imgFwd.setVisibility(View.GONE);
//                                cast.setVisibility(View.GONE);
//                            } else if (autoplay.equalsIgnoreCase("1")) {
//
//                                if (dataa.equalsIgnoreCase("watchlater")) {
//                                    Call<JSONResponse> alsolikeres = ApiClient.getInstance1().getApi().getWatchlatertNext(user_id, videoId);
//                                    alsolikeres.enqueue(new retrofit2.Callback<JSONResponse>() {
//                                        @Override
//                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
//
//
//                                            JSONResponse jsonResponse = response.body();
//
//                                            if (jsonResponse.getStatus().equalsIgnoreCase("true")) {
//
//                                                videoId = jsonResponse.getNext_videoid();
//                                                Toast.makeText(getApplicationContext(), "" + jsonResponse.getNext_videoid() + "\n" + videoId, Toast.LENGTH_LONG).show();
//                                                VideoDetails();
//                                                ThisMayAlsoLikeVideos();
//
//                                            } else {
//                                                Toast.makeText(getApplicationContext(), "Auto play turned off", Toast.LENGTH_LONG).show();
//                                                SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
//                                                editor.putString(sharedpreferences.autoplay, String.valueOf(0));
//                                                editor.apply();
//                                                editor.commit();
//                                                SharedPreferences prefs = getApplicationContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
//                                                autoplay = prefs.getString(sharedpreferences.autoplay, null);
//
//                                            }
//
//                                        }
//
//                                        @Override
//                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
//                                            Log.d("Error", t.getMessage());
//
//                                        }
//                                    });
//
//                                } else if (dataa.equalsIgnoreCase("favorite")) {
//                                    Call<JSONResponse> alsolikeres = ApiClient.getInstance1().getApi().getfavouriteNext(user_id, videoId);
//                                    alsolikeres.enqueue(new retrofit2.Callback<JSONResponse>() {
//                                        @Override
//                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
//
//
//                                            JSONResponse jsonResponse = response.body();
//
//                                            if (jsonResponse.getStatus().equalsIgnoreCase("true")) {
//                                                videoId = jsonResponse.getNext_videoid();
//                                                VideoDetails();
//                                                ThisMayAlsoLikeVideos();
//                                            } else {
//                                                Toast.makeText(getApplicationContext(), "Auto play turned off", Toast.LENGTH_LONG).show();
//                                                SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
//                                                editor.putString(sharedpreferences.autoplay, String.valueOf(0));
//                                                editor.apply();
//                                                editor.commit();
//                                                SharedPreferences prefs = getApplicationContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
//                                                autoplay = prefs.getString(sharedpreferences.autoplay, null);
//
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
//                                            Log.d("Error", t.getMessage());
//
//                                        }
//                                    });
//                                } else if (dataa.equalsIgnoreCase("wishlist")) {
//                                    Call<JSONResponse> alsolikeres = ApiClient.getInstance1().getApi().getWishlistNext(user_id, videoId);
//                                    alsolikeres.enqueue(new retrofit2.Callback<JSONResponse>() {
//                                        @Override
//                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
//
//
//                                            JSONResponse jsonResponse = response.body();
//
//                                            if (jsonResponse.getStatus().equalsIgnoreCase("true")) {
//                                                videoId = jsonResponse.getNext_videoid();
//                                                VideoDetails();
//                                                ThisMayAlsoLikeVideos();
//                                            } else {
//                                                Toast.makeText(getApplicationContext(), "Auto play turned off", Toast.LENGTH_LONG).show();
//                                                SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
//                                                editor.putString(sharedpreferences.autoplay, String.valueOf(0));
//                                                editor.apply();
//                                                editor.commit();
//                                                SharedPreferences prefs = getApplicationContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
//                                                autoplay = prefs.getString(sharedpreferences.autoplay, null);
//
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
//                                            Log.d("Error", t.getMessage());
//
//                                        }
//                                    });
//                                } else if (dataa.equalsIgnoreCase("video")) {
//                                    Call<JSONResponse> alsolikeres = ApiClient.getInstance1().getApi().getNext_video(videoId);
//                                    alsolikeres.enqueue(new retrofit2.Callback<JSONResponse>() {
//                                        @Override
//                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
//
//
//                                            JSONResponse jsonResponse = response.body();
//
//                                            if (jsonResponse.getStatus().equalsIgnoreCase("true")) {
//                                                videoId = jsonResponse.getNext_videoid();
//                                                VideoDetails();
//                                                ThisMayAlsoLikeVideos();
//                                            } else {
//                                                Toast.makeText(getApplicationContext(), "Auto play turned off", Toast.LENGTH_LONG).show();
//                                                SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
//                                                editor.putString(sharedpreferences.autoplay, String.valueOf(0));
//                                                editor.apply();
//                                                editor.commit();
//                                                SharedPreferences prefs = getApplicationContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
//                                                autoplay = prefs.getString(sharedpreferences.autoplay, null);
//
//                                            }
//
//                                        }
//
//                                        @Override
//                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
//                                            Log.d("Error", t.getMessage());
//
//                                        }
//                                    });
//                                }
//
//
//                            } else {
//                                exoreply.setVisibility(View.VISIBLE);
//                                exoPause.setVisibility(View.GONE);
//                                exoPlay.setVisibility(View.GONE);
//                                imgBwd.setVisibility(View.GONE);
//                                imgFwd.setVisibility(View.GONE);
//                                cast.setVisibility(View.GONE);
//
//                            }
//
//                            //  Toast.makeText(getApplicationContext(),"method called", Toast.LENGTH_LONG).show();
//                        }
//                    }, 1000);
//
//                    updateButtonVisibilities();
//            }
//
//        }
//
//        @Override
//        public void onPositionDiscontinuity(@Player.DiscontinuityReason int reason) {
//            if (player.getPlaybackError() != null) {
//                // The user has performed a seek whilst in the error state. Update the resume position so
//                // that if the user then retries, playback resumes from the position to which they seeked.
//                updateStartPosition();
//            }
//        }
//
//        @Override
//        public void onPlayerError(ExoPlaybackException e) {
//            if (isBehindLiveWindow(e)) {
//                clearStartPosition();
//                initializePlayer();
//            } else {
//                updateStartPosition();
//                updateButtonVisibilities();
////                showControls();
//            }
//        }
//
//        @Override
//        @SuppressWarnings("ReferenceEquality")
//        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
//            updateButtonVisibilities();
//            if (trackGroups != lastSeenTrackGroupArray) {
//                MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
//                if (mappedTrackInfo != null) {
//                    if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
//                            == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
//                        showToast(R.string.error_unsupported_video);
//                    }
//                    if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_AUDIO)
//                            == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
//                        showToast(R.string.error_unsupported_audio);
//                    }
//                }
//                lastSeenTrackGroupArray = trackGroups;
//            }
//        }
//    }

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


    private Uri getAdTagUri() {

        String ads;
        if (videoads == null) {
            ads = "https://developers.google.com/interactive-media-ads/docs/sdks/html5/client-side/vastinspector";
        } else {

            ads = videoads;

        }
        return Uri.parse(ads);
    }

    public void broadcastIntent() {
        registerReceiver(MyReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }


}
