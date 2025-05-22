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
import android.app.Activity;
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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Base64;
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
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.atbuys.runmawi.Vdocipher.Videoplayer_cipher;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.atbuys.runmawi.Config.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@UnstableApi
public class HomePageEpisodeActivity extends AppCompatActivity implements View.OnClickListener, PlayerControlView.VisibilityListener, PaymentResultListener, GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {
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
    private static final int PAYPAL_REQUEST_CODE = 7777;
    private static HomePageEpisodeActivity mInstancee;
    protected String useragent1;
    Set<String> usersSet = new HashSet<>();

    GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private TextView no_cast;

    MediaSource subsource;
    MergingMediaSource mergedSource;

    BandwidthMeter bandwidthMeter;

    ImageView cast;
    TextView cast1;
    List<Countrycode> movieList;

    private ProgressBar commentprogress;

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
    LinearLayout llParentContainer, genere_layout, castlayout;
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

    private DataSource.Factory dataSourceFactory;
    private SimpleExoPlayer player;
    private MediaSource mediaSource;
    private DefaultTrackSelector trackSelector;
    private boolean isShowingTrackSelectionDialog;
    private DefaultTrackSelector.Parameters trackSelectorParameters;
    private TrackGroupArray lastSeenTrackGroupArray;
    private TextView tvPlaybackSpeed, tvPlaybackSpeedSymbol, progress_text;
    private boolean startAutoPlay;
    private int startWindow;
    // Fields used only for ad playback. The ads loader is loaded via reflection.
    private long startPosition;
    private ImaServerSideAdInsertionMediaSource.AdsLoader adsLoader;
    private Uri loadedAdTagUri;
    private FrameLayout frameLayoutMain;
    private ImageView imgBwd;
    private ImageView exoPlay;
    private ImageView exoPause, backarrow;
    QualityAdapter qualityAdapter;

    private ImageView exoreply;
    private ImageView imgFwd, imgBackPlayer;
    private TextView skipintro;
    private TextView tvPlayerCurrentTime;
    String ppv_price, currency;
    private DefaultTimeBar exoTimebar;
    private ProgressBar exoProgressbar;
    private TextView tvPlayerEndTime;
    private ImageView imgSetting;
    private ImageView imgFullScreenEnterExit;
    private LinearLayout userfeatuerlayout;
    private ImageView coverimage;
    private CardView playnow, play_begin, resume, paynow, rent;
    private ImageView videoThumb;
    String quality_name, quality_price, quality_resolution;

    private ImageView subtitle_off, subtitle_on;

    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    //   private DownloadTracker downloadTracker;
    private DownloadManager downloadManager;
    private DownloadHelper myDownloadHelper;
    private LinearLayout llDownloadContainer;
    private LinearLayout llDownloadVideo;
    private ImageView imgDownloadState;
    private TextView tvDownloadState;
    private ProgressBar progressBarPercentage;
    private String videoId, seasonid;
    private String videoName, video_access;
    private String videoUrl;
    RelativeLayout progress_layout;

    /*private TextView cast_name;*/
    private long videoDurationInSeconds;
    private Runnable runnableCode;
    private Handler handler;
    private TextView comments;
    private EditText commenttext;
    private ImageView commentsend;
    private LinearLayout commentlayout;
    private RecyclerView usercommentrecycler;
    CommentsAdopter commentsAdopter;
    private TextView description;

    Addpayperview addpayperview;


    private int currentWindow = 0;
    private long playbackPosition = 0;

    private Switch autoplayswitch;
    String paypalamount;

    private AccessibilityService context;
    public static Dialog dialog;
    BottomSheetDialog paydunya_dialog;
    CardView paydunya_pay_now;
    EditText paydunya_email_address;
    PaydunyaSetup setup;
    PaydunyaCheckoutStore store;
    PaydunyaCheckoutInvoice invoice;
    private ArrayList<active_payment_settings> active_payment_settingsList;
    public static TextView pays;
    private ArrayList<payment_settings> payment_settingslist;
    String key, py_id;
    private Stripe stripe;
    PaymentMethodCreateParams params;
    Active_Payment_settingsAdopter active_payment_settingsAdopter;
    private ImageView like, liked, dislikee, dislikeed;
    LinearLayout unlikelayout, undislikelayout;
    AddComment addComment;
    TabLayout tableLayout;
    ViewPager viewPager;

    String nxtvidid, nextvidurl;

    LinearLayout watchlist, watchlater, hidelayout1, likee, dislike, favouriteLayout;
    ImageView watchlistimg, watchlistaddedimg, watchlaterimg, watchlateraddedimg, favlistimg, favlistaddimg;
    TextView videotext, view_more, views;
    ImageView share;
    TextView videotitle1, language1, genre2, duration, year, getCast1, imdb, durationandyear, genre_text;
    String shareurl, series_id, user_id, autoplay, subtitles_text, subchecked1, subchecked2, subchecked3, subchecked4;
    String video_like, video_dislike;


    private RecyclerView.LayoutManager layoutManager, layoutManager1, manager;
    private ArrayList<movies> moviesdata;
    private ArrayList<related_episode> recomendeddatalist;
    private ArrayList<genre> genredata;

    private ArrayList<quality> qualities;
    private ArrayList<quality_free> qualities1;

    private ArrayList<episode> movie_detaildata;
    private ArrayList<season> movie_detaildata2;
    private ArrayList<episode> movie_detaildata1;

    private ArrayList<videossubtitles> moviesubtitlesdata;
    private ArrayList<comment> usercommentslist;
    showwishlist showwish;
    private ArrayList<movieresolution> movieresolutiondata;
    private ArrayList<video_cast> castdetails;

    public static ArrayList<VideoModel> videoModels = new ArrayList<>();
    public ArrayList<String> arrPackage;
    QualityPlansAdapter qualityPlansAdapter;

    //   DownloadActivity downloadActivity;

    private List<videossubtitles> statelistdata;
    public String urlll;
    public String suburl = "";
    String type = "channel";
    TextView norecommanded, idd, urll, downimgurl, downname, downurl, downloadtext;

    RecyclerView thismaylikerecycler;
    RecyclerView castandcrewrecycler;
    ExtractorsFactory extractorsFactory;
    ThismaylikeEpisodeAdopter thismaylikeadopter;
    castandcrewadapter castandcrewadapter;
    subtitleLangAdopter subtitleLangadopter;
    String user_role, auto_play;
    //ShowMoreTextView textView;
    UserCommentsAdopter userCommentsAdopter;
    String renturl, dataa;
    private long position;
    // private Object cast_details;.

    String RAZORPAY_KEY_ID;
    String RAZORPAY_KEY_SECRET;
    String orderId;


    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
            .clientId(Config.PAYPAL_CLIENT_ID);


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


        gestureDetector = new GestureDetector(HomePageEpisodeActivity.this, HomePageEpisodeActivity.this);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        arrPackage = new ArrayList<>();

        dataSourceFactory = buildDataSourceFactory();

        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }
        hideStatusBar();
        checkmenu();

        setContentView(R.layout.episode_home_player);

        Intent in = getIntent();
        videoId = in.getStringExtra("id");
        urlll = in.getStringExtra("url");
        seasonid = in.getStringExtra("seasonid");
        series_id = in.getStringExtra("series_id");

        SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
        editor.putString(sharedpreferences.episode_id, videoId);
        editor.putString(sharedpreferences.season_id, seasonid);
        editor.putString(sharedpreferences.series_id, series_id);
        editor.apply();
        editor.commit();


        // suburl = in.getStringExtra("suburl");
        // dataa = in.getStringExtra("data");

        idd = (TextView) findViewById(R.id.idd);
        urll = (TextView) findViewById(R.id.urll);
        downurl = (TextView) findViewById(R.id.downurl);
        downimgurl = (TextView) findViewById(R.id.downimgurl);
        downname = (TextView) findViewById(R.id.downname);
        downloadtext = (TextView) findViewById(R.id.download);
        progress_text = (TextView) findViewById(R.id.progress_text);
        progress_layout = (RelativeLayout) findViewById(R.id.progress_layout);
        //textView = findViewById(R.id.text_view_show_more);
        imdb = (TextView) findViewById(R.id.imdb);
        durationandyear = (TextView) findViewById(R.id.durationandyear);
        genre_text = (TextView) findViewById(R.id.genre_text);
        genere_layout = (LinearLayout) findViewById(R.id.genere_layout);
        castlayout = (LinearLayout) findViewById(R.id.castlayout);
        tableLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        backarrow = (ImageView) findViewById(R.id.back);

        // ll_download_video_progress = (LinearLayout) findViewById(R.id.ll_download_video_progress);

        idd.setText(videoId);
        urll.setText(urlll);

        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);
        auto_play = prefs.getString(sharedpreferences.autoplay, null);

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

      /*  AdaptiveExoplayer application = (AdaptiveExoplayer) getApplication();
        downloadTracker = application.getDownloadTracker();
        downloadManager = application.getDownloadManager();*/


        // Start the download service if it should be running but it's not currently.
        // Starting the service in the foreground causes notification flicker if there is no scheduled
        // action. Starting it in the background throws an exception if the app is in the background too
        // (e.g. if device screen is locked).

        try {
            //  DownloadService.start(this, DemoDownloadService.class);
        } catch (IllegalStateException e) {
            //  DownloadService.startForeground(this, DemoDownloadService.class);
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
                //      observerVideoStatus();
                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnableCode);


        videotext = (TextView) findViewById(R.id.videotext);
        view_more = (TextView) findViewById(R.id.view_more);
        watchlist = (LinearLayout) findViewById(R.id.watchlist);
        watchlater = (LinearLayout) findViewById(R.id.watchlater);
        watchlistimg = (ImageView) findViewById(R.id.watchlistimg);
        favouriteLayout = (LinearLayout) findViewById(R.id.favlist);
        favlistimg = (ImageView) findViewById(R.id.favlistimg);
        favlistaddimg = (ImageView) findViewById(R.id.favlistaddimg);
        comments = (TextView) findViewById(R.id.comments);
        commentsend = (ImageView) findViewById(R.id.sendcomment);
        commentprogress = (ProgressBar) findViewById(R.id.comment_progress);
        commenttext = (EditText) findViewById(R.id.commenttext);
        commentlayout = (LinearLayout) findViewById(R.id.commentlayout);
        usercommentrecycler = (RecyclerView) findViewById(R.id.usercomments);
        playnow = (CardView) findViewById(R.id.play_now);
        paynow = (CardView) findViewById(R.id.paynow);
        play_begin = (CardView) findViewById(R.id.play_begin);
        resume = (CardView) findViewById(R.id.resume);
        rent = (CardView) findViewById(R.id.rent_now);
        userfeatuerlayout = (LinearLayout) findViewById(R.id.userfeatuerlayout);
        autoplayswitch = (Switch) findViewById(R.id.autoplay);
        views = (TextView) findViewById(R.id.view);

        likee = (LinearLayout) findViewById(R.id.likeed);
        dislike = (LinearLayout) findViewById(R.id.dislike);
        unlikelayout = (LinearLayout) findViewById(R.id.unlikelayout);
        undislikelayout = (LinearLayout) findViewById(R.id.undislikelayout);

        like = (ImageView) findViewById(R.id.like);
        liked = (ImageView) findViewById(R.id.liked);
        dislikee = (ImageView) findViewById(R.id.dislikee);
        dislikeed = (ImageView) findViewById(R.id.dislikeed);

        watchlistaddedimg = (ImageView) findViewById(R.id.watchlistaddimg);
        watchlaterimg = (ImageView) findViewById(R.id.watchlateradd);
        watchlateraddedimg = (ImageView) findViewById(R.id.watchlateraddedimg);
        share = (ImageView) findViewById(R.id.share);
        norecommanded = (TextView) findViewById(R.id.norecommanded);
        cast = (ImageView) findViewById(R.id.cast);
        genre2 = (TextView) findViewById(R.id.genre);
        /*     cast_name=(TextView)findViewById(R.id.cast_name);*/
        language1 = (TextView) findViewById(R.id.language);
        year = (TextView) findViewById(R.id.movieyear);
        duration = (TextView) findViewById(R.id.videoduration);
        videotitle1 = (TextView) findViewById(R.id.videotitle);
        genredata = new ArrayList<genre>();
        movieresolutiondata = new ArrayList<movieresolution>();

        qualities = new ArrayList<>();
        qualities1 = new ArrayList<>();
        movie_detaildata = new ArrayList<episode>();


        moviesubtitlesdata = new ArrayList<videossubtitles>();
        recomendeddatalist = new ArrayList<related_episode>();
        usercommentslist = new ArrayList<comment>();
        castdetails = new ArrayList<video_cast>();
        active_payment_settingsList = new ArrayList<active_payment_settings>();
        pays = (TextView) findViewById(R.id.pays);

        Call<JSONResponse> id_call = ApiClient.getInstance1().getApi().getPaymentDetails();
        id_call.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JSONResponse jsonResponse = response.body();
                    payment_settingslist = new ArrayList<>(Arrays.asList(jsonResponse.getPayment_settings()));
                    for (int i = 0; i < payment_settingslist.size(); i++) {
                        if (payment_settingslist.get(i).getPayment_type().equalsIgnoreCase("Razorpay")) {

                            if (payment_settingslist.get(i).getLive_mode().equalsIgnoreCase(String.valueOf(0))) {
                                RAZORPAY_KEY_ID = payment_settingslist.get(i).getTest_publishable_key();
                                RAZORPAY_KEY_SECRET = payment_settingslist.get(i).getTest_secret_key();
                            } else {
                                RAZORPAY_KEY_ID = payment_settingslist.get(i).getLive_publishable_key();
                                RAZORPAY_KEY_SECRET = payment_settingslist.get(i).getLive_secret_key();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable throwable) {
            }
        });

        videoId = prefs.getString(sharedpreferences.episode_id, null);
        seasonid = prefs.getString(sharedpreferences.season_id, null);
        series_id = prefs.getString(sharedpreferences.series_id, null);


        Call<JSONResponse> channela = ApiClient.getInstance1().getApi().getSeriesDetail(series_id);
        channela.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse_ser = response.body();
                ArrayList<series> series = new ArrayList<>(Arrays.asList(jsonResponse_ser.getSeries()));

                if (series.get(0).getAccess().equalsIgnoreCase("subscriber")) {


                    if (user_role.equalsIgnoreCase("admin") || user_role.equalsIgnoreCase("subscriber")) {
                        resume.setVisibility(View.GONE);
                        play_begin.setVisibility(View.GONE);
                        paynow.setVisibility(View.GONE);
                        rent.setVisibility(View.GONE);
                        playnow.setVisibility(View.VISIBLE);
                    } else {
                        resume.setVisibility(View.GONE);
                        play_begin.setVisibility(View.GONE);
                        paynow.setVisibility(View.VISIBLE);
                        rent.setVisibility(View.GONE);
                        playnow.setVisibility(View.GONE);
                    }
                } else {

                    Call<JSONResponse> ac_api = ApiClient.getInstance1().getApi().getSeasonPpv(seasonid, videoId, user_id);
                    ac_api.enqueue(new retrofit2.Callback<JSONResponse>() {//working
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                            JSONResponse jsonResponse = response.body();


                            if (user_role.equalsIgnoreCase("admin") || user_role.equalsIgnoreCase("subscriber")) {

                                resume.setVisibility(View.GONE);
                                play_begin.setVisibility(View.GONE);
                                paynow.setVisibility(View.GONE);
                                rent.setVisibility(View.GONE);
                                playnow.setVisibility(View.VISIBLE);
                            } else if (jsonResponse.getAccess().equalsIgnoreCase("PPV")) {

                                resume.setVisibility(View.GONE);
                                play_begin.setVisibility(View.GONE);
                                paynow.setVisibility(View.GONE);
                                rent.setVisibility(View.VISIBLE);
                                playnow.setVisibility(View.GONE);
                            } else if (jsonResponse.getAccess().equalsIgnoreCase("subscriber")) {


                                resume.setVisibility(View.GONE);
                                play_begin.setVisibility(View.GONE);
                                paynow.setVisibility(View.VISIBLE);
                                rent.setVisibility(View.GONE);
                                playnow.setVisibility(View.GONE);
                            } else if (jsonResponse.getAccess().equalsIgnoreCase("free")) {
                                resume.setVisibility(View.GONE);
                                play_begin.setVisibility(View.GONE);
                                paynow.setVisibility(View.GONE);
                                rent.setVisibility(View.GONE);
                                playnow.setVisibility(View.VISIBLE);
                            } else if (jsonResponse.getAccess().equalsIgnoreCase("guest")) {


                                resume.setVisibility(View.GONE);
                                play_begin.setVisibility(View.GONE);
                                paynow.setVisibility(View.GONE);
                                rent.setVisibility(View.GONE);
                                playnow.setVisibility(View.VISIBLE);
                            } else {

                                resume.setVisibility(View.GONE);
                                play_begin.setVisibility(View.GONE);
                                paynow.setVisibility(View.GONE);
                                rent.setVisibility(View.GONE);
                                playnow.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Log.d("Error:1", t.getMessage());


                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://runmawi.com/api/auth/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponse> call = request.getStripeOnetime();
        call.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                currency = response.body().getCurrency_Symbol();
            }
            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
            }
        });


       /* playnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                qualities1.clear();
                Call<JSONResponse> ac_api = ApiClient.getInstance1().getApi().getSeasonPpv(seasonid, videoId, user_id);
                ac_api.enqueue(new retrofit2.Callback<JSONResponse>() {//working

                    @Override
                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                        if (response.isSuccessful() && response.body() != null) {


                            JSONResponse jsonResponse = response.body();
                            movie_detaildata2 = new ArrayList<>(Arrays.asList(jsonResponse.getSeason()));

                            video_access = jsonResponse.getAccess();


                            Log.e("TAG", "onResponse: \n" + movie_detaildata.get(0).getOtp() + "\n" + movie_detaildata.get(0).getPlaybackInfo());

                            Intent in = new Intent(getApplicationContext(), Videoplayer_cipher.class);
                            in.putExtra("id", videoId);
                            in.putExtra("otp", movie_detaildata.get(0).getOtp());
                            in.putExtra("playback", movie_detaildata.get(0).getPlaybackInfo());

                            //  Toast.makeText(getApplicationContext(), "" + movie_detaildata.get(0).getOtp(), Toast.LENGTH_SHORT).show();
                            //  Toast.makeText(getApplicationContext(), "" + movie_detaildata.get(0).getPlaybackInfo(), Toast.LENGTH_SHORT).show();

                            startActivity(in);

//                            if ((video_access.equalsIgnoreCase("registered")) || (video_access.equalsIgnoreCase("guest"))) {
//
//                                {
//                                    View view1 = getLayoutInflater().inflate(R.layout.quality_plan_page, null);
//                                    BottomSheetDialog dialog1 = new BottomSheetDialog(HomePageEpisodeActivity.this);
//                                    dialog1.setContentView(view1);
//
//                                    RecyclerView plans_recyclerview = view1.findViewById(R.id.plans_recyclerview);
//                                    CardView watch_free = view1.findViewById(R.id.watch_free);
//                                    TextView text_name = view1.findViewById(R.id.text_name);
//                                    //text_name.setText(videotitle1.getText().toString());
//
//                                    ItemClickListener itemClickListener;
//                                    itemClickListener = new ItemClickListener() {
//                                        @Override
//                                        public void onClick(String s) {
//                                            // Notify adapter
//                                            plans_recyclerview.post(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    qualityAdapter.notifyDataSetChanged();
//                                                }
//                                            });
//                                        }
//                                    };
//
//
//                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getSeasonPpv(seasonid, videoId, user_id);
//                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
//                                        @Override
//                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
//
//                                            JSONResponse jsonResponse = response.body();
//                                            movie_detaildata2 = new ArrayList<>(Arrays.asList(jsonResponse.getSeason()));
//                                            String s = "";
//                                            String p = "";
//                                            String r = "";
//
//                                            for (int i = 0; i < 3; i++) {
//
//                                                if (i == 0) {
//                                                    s = "Low Quality (480p)";
//                                                    r = "480p";
//                                                } else if (i == 1) {
//                                                    s = "Medium Quality (720p)";
//                                                    r = "720p";
//                                                } else if (i == 2) {
//                                                    s = "High Quality (1080p)";
//                                                    r = "1080p";
//                                                }
//                                                quality_free qualityList = new quality_free(s, r);
//                                                qualities1.add(qualityList);
//                                            }
//
//                                            plans_recyclerview.setHasFixedSize(true);
//                                            plans_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//                                            qualityAdapter = new QualityAdapter(qualities1, itemClickListener);
//                                            plans_recyclerview.setAdapter(qualityAdapter);
//                                            dialog1.show();
//
//                                        }
//
//                                        @Override
//                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
//                                        }
//                                    });
//
//
//                                    plans_recyclerview.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
//                                        @Override
//                                        public void onItemClick(View view, final int position) {
//
//                                            quality_name = qualities1.get(position).getQuality();
//                                            quality_resolution = qualities1.get(position).getResolution();
//
//                                            if (watch_free.getVisibility() == View.GONE) {
//                                                watch_free.setVisibility(View.VISIBLE);
//                                            }
//
//
//                                        }
//                                    }));
//
//                                    watch_free.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetailsepisodes(user_id,seasonid, videoId,quality_resolution);
//                                            res.enqueue(new retrofit2.Callback<JSONResponse>() {
//                                                @Override
//                                                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
//
//                                                    JSONResponse jsonResponse = response.body();
//                                                    movie_detaildata1 = new ArrayList<>(Arrays.asList(jsonResponse.getEpisode()));
//
//                                                    Intent in = new Intent(getApplicationContext(), Videoplayer_cipher.class);
//                                                    in.putExtra("id", videoId);
//                                                    in.putExtra("otp", movie_detaildata1.get(0).getOtp());
//                                                    in.putExtra("playback", movie_detaildata1.get(0).getPlaybackInfo());
//
//                                                    startActivity(in);
//
//                                                }
//                                                @Override
//                                                public void onFailure(Call<JSONResponse> call, Throwable t) {
//                                                }
//                                            });
//                                        }
//                                    });
//                                }
//                            }
//
//
//                            else {
//
//                          //Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();
//                                Intent in = new Intent(getApplicationContext(), Videoplayer_cipher.class);
//                                in.putExtra("id", videoId);
//                                in.putExtra("otp", movie_detaildata.get(0).getOtp());
//                                in.putExtra("playback", movie_detaildata.get(0).getPlaybackInfo());
//
//                                //  Toast.makeText(getApplicationContext(), "" + movie_detaildata.get(0).getOtp(), Toast.LENGTH_SHORT).show();
//                                //  Toast.makeText(getApplicationContext(), "" + movie_detaildata.get(0).getPlaybackInfo(), Toast.LENGTH_SHORT).show();
//
//                                startActivity(in);
//                            }

                        }


                    }


                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                    }
                });


            }
        });*/

        playnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qualities1.clear();
                Call<JSONResponse> ac_api = ApiClient.getInstance1().getApi().getSeasonPpv(seasonid, videoId, user_id);
                ac_api.enqueue(new retrofit2.Callback<JSONResponse>() {
                    @Override
                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                        JSONResponse jsonResponse = response.body();
                        movie_detaildata2 = new ArrayList<>(Arrays.asList(jsonResponse.getSeason()));

                        View view1 = getLayoutInflater().inflate(R.layout.quality_plan_page, null);
                        BottomSheetDialog dialog1 = new BottomSheetDialog(HomePageEpisodeActivity.this);
                        dialog1.setContentView(view1);

                        RecyclerView plans_recyclerview = view1.findViewById(R.id.plans_recyclerview);
                        CardView watch_free = view1.findViewById(R.id.watch_free);
                        CardView pay = view1.findViewById(R.id.pay);
                        TextView text_name = view1.findViewById(R.id.text_name);

                        ItemClickListener itemClickListener;
                        itemClickListener = new ItemClickListener() {
                            @Override
                            public void onClick(String s) {
                                // Notify adapter
                                plans_recyclerview.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        qualityAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        };

                        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getSeasonPpv(seasonid, videoId, user_id);
                        res.enqueue(new retrofit2.Callback<JSONResponse>() {
                            @Override
                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                JSONResponse jsonResponse = response.body();
                                movie_detaildata2 = new ArrayList<>(Arrays.asList(jsonResponse.getSeason()));

                                String s = "";
                                String p = "";
                                String r = "";

                                for (int i = 0; i < 3; i++) {

                                    if (i == 0) {
                                        s = "Low Quality (480p)";
                                        r = "480p";
                                        p = movie_detaildata2.get(0).getPpv_price_480p();
                                    } else if (i == 1) {
                                        s = "Medium Quality (720p)";
                                        r = "720p";
                                        p = movie_detaildata2.get(0).getPpv_price_720p();
                                    } else if (i == 2) {
                                        s = "High Quality (1080p)";
                                        r = "1080p";
                                        p = movie_detaildata2.get(0).getPpv_price_1080p();
                                    }
                                    quality_free qualityList = new quality_free(s,p,r);
                                    qualities1.add(qualityList);
                                }

                                plans_recyclerview.setHasFixedSize(true);
                                plans_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                qualityAdapter = new QualityAdapter(qualities1, itemClickListener);
                                plans_recyclerview.setAdapter(qualityAdapter);
                                dialog1.show();

                            }

                            @Override
                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                            }
                        });


                        plans_recyclerview.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {

                                quality_name = qualities1.get(position).getQuality();
                                quality_resolution = qualities1.get(position).getResolution();
                                quality_price = qualities.get(position).getPrice();

                                if (movie_detaildata2.get(0).getAccess().equalsIgnoreCase("ppv")) {
                                    pay.setVisibility(View.VISIBLE);
                                    watch_free.setVisibility(View.GONE);
                                    /*if (quality_resolution.equalsIgnoreCase(movie_detaildata2.get(0).getPPV_Plan())) {
                                        watch_free.setVisibility(View.VISIBLE);
                                        pay.setVisibility(View.GONE);
                                    } else {
                                        pay.setVisibility(View.VISIBLE);
                                        watch_free.setVisibility(View.GONE);
                                    }*/
                                } else {
                                    watch_free.setVisibility(View.VISIBLE);
                                    pay.setVisibility(View.GONE);
                                }
                            }
                        }));

                        watch_free.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog1.cancel();
                                Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetailsepisodes(user_id, seasonid, videoId, quality_resolution);
                                res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                    @Override
                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                        JSONResponse jsonResponse = response.body();
                                        movie_detaildata1 = new ArrayList<>(Arrays.asList(jsonResponse.getEpisode()));

                                        Intent in = new Intent(getApplicationContext(), Videoplayer_cipher.class);
                                        in.putExtra("id", videoId);
                                        in.putExtra("otp", movie_detaildata1.get(0).getOtp());
                                        in.putExtra("playback", movie_detaildata1.get(0).getPlaybackInfo());

                                        startActivity(in);

                                    }

                                    @Override
                                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                                    }
                                });
                            }
                        });
                        pay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.cancel();
                                createOrder();
                            }

                        });

                    }
                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable throwable) {
                    }
                });
            }
        });


        paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user_id == null) {
                    Intent in = new Intent(getApplicationContext(), SigninActivity.class);
                    startActivity(in);
                } else {
                    Intent in = new Intent(getApplicationContext(), SubscribeActivity.class);
                    startActivity(in);
                }
            }
        });


        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qualities.clear();
                View view1 = getLayoutInflater().inflate(R.layout.quality_plan_page, null);
                BottomSheetDialog dialog1 = new BottomSheetDialog(HomePageEpisodeActivity.this);
                dialog1.setContentView(view1);

                RecyclerView plans_recyclerview = view1.findViewById(R.id.plans_recyclerview);
                CardView pay = view1.findViewById(R.id.pay);
                TextView text_name = view1.findViewById(R.id.text_name);

                ItemClickListener itemClickListener;
                itemClickListener = new ItemClickListener() {
                    @Override
                    public void onClick(String s) {
                        // Notify adapter
                        plans_recyclerview.post(new Runnable() {
                            @Override
                            public void run() {
                                qualityPlansAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                };


                Call<JSONResponse> res = ApiClient.getInstance1().getApi().getSeasonPpv(seasonid, videoId, user_id);
                res.enqueue(new retrofit2.Callback<JSONResponse>() {
                    @Override
                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                        JSONResponse jsonResponse = response.body();
                        ArrayList<season> seasonArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getSeason()));
                        ppv_price = seasonArrayList.get(0).getPpv_price();


                        String s = "";
                        String p = "";
                        String r = "";

                        for (int i = 0; i < 3; i++) {

                            if (i == 0) {
                                s = "Low Quality (480p)";
                                r = "480p";
                                p = seasonArrayList.get(0).getPpv_price_480p();
                            } else if (i == 1) {
                                s = "Medium Quality (720p)";
                                r = "720p";
                                p = seasonArrayList.get(0).getPpv_price_720p();
                            } else if (i == 2) {
                                s = "High Quality (1080p)";
                                r = "1080p";
                                p = seasonArrayList.get(0).getPpv_price_1080p();
                            }
                            quality qualityList = new quality(s, p, r);
                            qualities.add(qualityList);
                        }

                        plans_recyclerview.setHasFixedSize(true);
                        plans_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        qualityPlansAdapter = new QualityPlansAdapter(qualities, itemClickListener);
                        plans_recyclerview.setAdapter(qualityPlansAdapter);
                        dialog1.show();

                    }

                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                    }
                });


                plans_recyclerview.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        quality_name = qualities.get(position).getQuality();
                        quality_price = qualities.get(position).getPrice();
                        quality_resolution = qualities.get(position).getResolution();

                        if (pay.getVisibility() == View.GONE) {
                            pay.setVisibility(View.VISIBLE);
                        }
                    }
                }));


                pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.cancel();
                        createOrder();
                    }

                });
            }

        });

        exoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exoPause.setVisibility(View.VISIBLE);
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


        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        thismaylikeadopter = new ThismaylikeEpisodeAdopter(recomendeddatalist, this);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        thismaylikerecycler = (RecyclerView) findViewById(R.id.thismayalsolike);

        commentsAdopter = new CommentsAdopter(usercommentslist, this);
        layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);

        thismaylikerecycler.setHasFixedSize(true);
        thismaylikerecycler.setLayoutManager(layoutManager);
        thismaylikerecycler.setAdapter(thismaylikeadopter);

        castandcrewadapter = new castandcrewadapter(castdetails, this);
        manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        castandcrewrecycler = (RecyclerView) findViewById(R.id.castandcrew);
        no_cast = findViewById(R.id.no_cast);

        castandcrewrecycler.setHasFixedSize(true);
        castandcrewrecycler.setLayoutManager(manager);
        castandcrewrecycler.setAdapter(castandcrewadapter);


        usercommentrecycler.setHasFixedSize(true);
        usercommentrecycler.setLayoutManager(layoutManager1);
        usercommentrecycler.setAdapter(commentsAdopter);


/*
        autoplayswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {


                    SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                    editor.putString(sharedpreferences.autoplay, String.valueOf(1));
                    editor.apply();
                    editor.commit();

                } else {

                    SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                    editor.putString(sharedpreferences.autoplay, String.valueOf(0));
                    editor.apply();
                    editor.commit();


                }
            }
        });
*/


       /* if (auto_play == null) {
            autoplayswitch.setChecked(false);
        } else if (auto_play.equalsIgnoreCase("1")) {

            autoplayswitch.setChecked(true);
        } else {
            autoplayswitch.setChecked(false);
        }
*/

        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (commentlayout.getVisibility() == View.VISIBLE) {

                    commentlayout.setVisibility(View.GONE);
                } else {

                    commentlayout.setVisibility(View.VISIBLE);

                    Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getUsercommentslive(videoId, "play_episode");
                    movieres.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                            JSONResponse jsonResponse = response.body();

                            if (jsonResponse.getComment().length == 0) {


                            } else {

                                usercommentslist = new ArrayList<>(Arrays.asList(jsonResponse.getComment()));

                                //  Toast.makeText(getApplicationContext(), ""+usercommentslist.get(0).getUsername(), Toast.LENGTH_SHORT).show();
                                commentsAdopter = new CommentsAdopter(usercommentslist);
                                usercommentrecycler.setAdapter(commentsAdopter);

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


        commentsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                commentprogress.setVisibility(View.VISIBLE);
                commentsend.setVisibility(View.GONE);

                String comment = commenttext.getText().toString();

                if (commenttext.getText().toString().isEmpty()) {
                    commentprogress.setVisibility(View.GONE);
                    commentsend.setVisibility(View.VISIBLE);

                    Toast.makeText(getApplicationContext(), "Please enter your Comment", Toast.LENGTH_LONG).show();
                } else {


                    Api.getClient().getAddLivecomment(user_id, videoId, comment, "play_episode", new Callback<AddComment>() {

                        @Override
                        public void success(AddComment addComment, Response response) {

                            addComment = addComment;

                            if (addComment.getStatus().equalsIgnoreCase("true")) {

                                Toast.makeText(getApplicationContext(), "" + addComment.getMessage(), Toast.LENGTH_LONG).show();

                                Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getUsercommentslive(videoId, "play_episode");
                                movieres.enqueue(new retrofit2.Callback<JSONResponse>() {
                                    @Override
                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                        JSONResponse jsonResponse = response.body();

                                        commentprogress.setVisibility(View.GONE);
                                        commentsend.setVisibility(View.VISIBLE);
                                        commenttext.setText("");

                                        usercommentslist = new ArrayList<>(Arrays.asList(jsonResponse.getComment()));
                                        commentsAdopter = new CommentsAdopter(usercommentslist);
                                        usercommentrecycler.setAdapter(commentsAdopter);


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
            }
        });


        usercommentrecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(HomePageEpisodeActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (usercommentslist.size() > position) {
                            if (usercommentslist.get(position) != null) {


                                View view1 = getLayoutInflater().inflate(R.layout.comments_edit, null);
                                BottomSheetDialog dialog1 = new BottomSheetDialog(HomePageEpisodeActivity.this);
                                dialog1.setContentView(view1);
                                dialog1.show();


                                TextView tx1 = (TextView) view1.findViewById(R.id.edit);
                                TextView tx2 = (TextView) view1.findViewById(R.id.delete);
                                LinearLayout txt1 = (LinearLayout) view1.findViewById(R.id.edittxt1);
                                EditText edit1 = (EditText) view1.findViewById(R.id.edittextcomment);
                                ImageView img = (ImageView) view1.findViewById(R.id.editecoment);
                                ProgressBar pro = (ProgressBar) view1.findViewById(R.id.comment_progress1);


                                tx1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        txt1.setVisibility(View.VISIBLE);
                                        edit1.setText(usercommentslist.get(position).getBody());

                                        img.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {


                                                pro.setVisibility(View.VISIBLE);
                                                img.setVisibility(View.GONE);

                                                String comment = edit1.getText().toString();

                                                if (edit1.getText().toString().isEmpty()) {
                                                    pro.setVisibility(View.GONE);
                                                    img.setVisibility(View.VISIBLE);

                                                    Toast.makeText(getApplicationContext(), "Please enter your Comment", Toast.LENGTH_LONG).show();
                                                } else {


                                                    Api.getClient().getupdateLivecomment(user_id, videoId, comment, "play_episode", usercommentslist.get(position).getId(), new Callback<AddComment>() {

                                                        @Override
                                                        public void success(AddComment addComment, Response response) {

                                                            addComment = addComment;

                                                            if (addComment.getStatus().equalsIgnoreCase("true")) {

                                                                Toast.makeText(getApplicationContext(), "" + addComment.getMessage(), Toast.LENGTH_LONG).show();

                                                                dialog1.cancel();
                                                                Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getUsercommentslive(videoId, "play_episode");
                                                                movieres.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                                    @Override
                                                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                                        JSONResponse jsonResponse = response.body();

                                                                        pro.setVisibility(View.GONE);
                                                                        img.setVisibility(View.VISIBLE);
                                                                        edit1.setText("");

                                                                        usercommentslist = new ArrayList<>(Arrays.asList(jsonResponse.getComment()));
                                                                        commentsAdopter = new CommentsAdopter(usercommentslist);
                                                                        usercommentrecycler.setAdapter(commentsAdopter);


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
                                            }
                                        });

                                    }
                                });


                                tx2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        Api.getClient().getDeleteComment(usercommentslist.get(position).getId(), new Callback<AddComment>() {

                                            @Override
                                            public void success(AddComment addComment, Response response) {

                                                addComment = addComment;
                                                Toast.makeText(getApplicationContext(), "" + addComment.getMessage(), Toast.LENGTH_LONG).show();
                                                dialog1.cancel();


                                                Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getUsercommentslive(videoId, "play_episode");
                                                movieres.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                    @Override
                                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                        JSONResponse jsonResponse = response.body();


                                                        usercommentslist = new ArrayList<>(Arrays.asList(jsonResponse.getComment()));
                                                        commentsAdopter = new CommentsAdopter(usercommentslist);
                                                        usercommentrecycler.setAdapter(commentsAdopter);


                                                    }

                                                    @Override
                                                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                        Log.d("Error", t.getMessage());
                                                    }
                                                });

                                            }

                                            @Override
                                            public void failure(RetrofitError error) {

                                            }
                                        });
                                    }
                                });


                            }
                        }
                    }
                }));


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "I am watching videos";
                String shareSub = shareurl;
                if (shareSub == null || shareSub.isEmpty() || shareSub.equalsIgnoreCase(" ")) {

                    myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                    myIntent.putExtra(Intent.EXTRA_TEXT, "Install Runmawi App and enjoy to watch videos");
                    startActivity(Intent.createChooser(myIntent, "Share using"));
                } else {
                    myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                    myIntent.putExtra(Intent.EXTRA_TEXT, shareSub);
                    startActivity(Intent.createChooser(myIntent, "Share using"));
                }


            }
        });


        cast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent("android.settings.CAST_SETTINGS"));

            }
        });


        favouriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Api.getClient().getFaourite(user_id, idd.getText().toString(), new Callback<Addtofavouritemovie>() {

                    @Override
                    public void success(Addtofavouritemovie addwishmovie, Response response) {

                        addwishmovie = addwishmovie;
                        if (addwishmovie.getStatus().equalsIgnoreCase("true")) {

                            favlistaddimg.setVisibility(View.VISIBLE);
                            favlistimg.setVisibility(View.GONE);

                            Toast.makeText(getApplicationContext(), "" + addwishmovie.getMessage(), Toast.LENGTH_LONG).show();
                        } else if (addwishmovie.getStatus().equalsIgnoreCase("false")) {

                            favlistaddimg.setVisibility(View.GONE);
                            favlistimg.setVisibility(View.VISIBLE);

                            Toast.makeText(getApplicationContext(), "" + addwishmovie.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "You are not registered user", Toast.LENGTH_LONG).show();

                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                        //   Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });


        watchlater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Api.getClient().getAddwatchlaterEpisode(user_id, videoId, new Callback<Addtowishlistmovie>() {
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

                        //   Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                    }
                });
            }


        });


        watchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Api.getClient().getAddWishlistEpisode(user_id, videoId, new Callback<Addtowishlistmovie>() {

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

                        // Toast.makeText(getApplicationContext(), "sd", Toast.LENGTH_LONG).show();
                    }
                });
            }


        });


        likee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Api.getClient().getLikecountEpisode(user_id, videoId, "1", new Callback<likeandunlike>() {

                    @Override
                    public void success(likeandunlike addwishmovie, Response response) {

                        addwishmovie = addwishmovie;
                        if (addwishmovie.getLiked().equalsIgnoreCase("1")) {

                            unlikelayout.setVisibility(View.VISIBLE);
                            likee.setVisibility(View.GONE);

                            Toast.makeText(getApplicationContext(), "You liked video", Toast.LENGTH_LONG).show();

                            if (addwishmovie.getDisliked().equalsIgnoreCase("0")) {

                                undislikelayout.setVisibility(View.GONE);
                                dislike.setVisibility(View.VISIBLE);
                            }
                        }


                    }

                    @Override
                    public void failure(RetrofitError error) {

                        Toast.makeText(getApplicationContext(), "Your are Not registered user", Toast.LENGTH_LONG).show();
                    }
                });
            }


        });


        unlikelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Api.getClient().getLikecountEpisode(user_id, videoId, "0", new Callback<likeandunlike>() {

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

                        Toast.makeText(getApplicationContext(), "Your not registered user ", Toast.LENGTH_LONG).show();
                    }
                });
            }


        });


        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Api.getClient().getDislikecountEpisode(user_id, videoId, "1", new Callback<likeandunlike>() {

                    @Override
                    public void success(likeandunlike addwishmovie, Response response) {
                        Toast.makeText(getApplicationContext(), "" + addwishmovie.getDisliked(), Toast.LENGTH_SHORT).show();

                        addwishmovie = addwishmovie;

                        if (addwishmovie.getDisliked().equalsIgnoreCase("1")) {

                            undislikelayout.setVisibility(View.VISIBLE);
                            dislike.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "You disliked video", Toast.LENGTH_LONG).show();

                            if (addwishmovie.getLiked().equalsIgnoreCase("0")) {

                                likee.setVisibility(View.VISIBLE);
                                unlikelayout.setVisibility(View.GONE);
                            }
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                        Toast.makeText(getApplicationContext(), "Your Not Registered user ", Toast.LENGTH_LONG).show();
                    }
                });
            }


        });


        undislikelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Api.getClient().getDislikecountEpisode(user_id, videoId, "0", new Callback<likeandunlike>() {

                    @Override
                    public void success(likeandunlike addwishmovie, Response response) {

                        addwishmovie = addwishmovie;
                        if (addwishmovie.getDisliked().equalsIgnoreCase("0")) {

                            dislike.setVisibility(View.VISIBLE);
                            undislikelayout.setVisibility(View.GONE);

                            Toast.makeText(getApplicationContext(), "Your dislike removed", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                        Toast.makeText(getApplicationContext(), "Your not a registered user ", Toast.LENGTH_LONG).show();
                    }
                });
            }

        });

        watchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Api.getClient().getAddWishlistEpisode(user_id, videoId, new Callback<Addtowishlistmovie>() {

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


        });

        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getEpisodesDetail(videoId, user_id);
        res.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getEpisode()));


                shareurl = movie_detaildata.get(0).getImage_url();
                //    Toast.makeText(application, ""+shareurl, Toast.LENGTH_SHORT).show();

                video_like = jsonResponse.getLike();
                video_dislike = jsonResponse.getDislike();

                series_id = movie_detaildata.get(0).getSeries_id();
                genre2.setText(jsonResponse.getMain_genre());
                imdb.setText(movie_detaildata.get(0).getRating());
                durationandyear.setText(movie_detaildata.get(0).getDuration() + " " + movie_detaildata.get(0).getYear());

                if (jsonResponse.getMain_genre() == null || jsonResponse.getMain_genre().isEmpty()) {
                    genere_layout.setVisibility(View.GONE);
                } else {
                    genere_layout.setVisibility(View.VISIBLE);
                    genre_text.setText(jsonResponse.getMain_genre());
                }
                nxtvidid = jsonResponse.getVideonext();
                nextvidurl = jsonResponse.getNext_url();


                String userprofile = movie_detaildata.get(0).getPlayer_image_url();
                Log.d("userprofile:", userprofile);
                Picasso.get().load(userprofile).fit().into(videoThumb);

                String next = "<font color='#ff0000'>  View Less</font>";
                //videotext.setText(Html.fromHtml(movie_detaildata.get(0).getEpisode_description()));

                final Handler handler223 = new Handler();
                final Runnable Update223 = new Runnable() {
                    public void run() {

                        if (movie_detaildata.get(0).getEpisode_description() == "" || movie_detaildata.get(0).getEpisode_description() == null) {
                        } else {

                            if (videotext.getMaxLines() == 2) {
                                videotext.setText(Html.fromHtml(movie_detaildata.get(0).getEpisode_description()));

                                if (movie_detaildata.get(0).getEpisode_description().length() <= 160) {
                                    view_more.setVisibility(View.GONE);
                                } else {
                                    view_more.setVisibility(View.VISIBLE);
                                }
                            } else {
                                videotext.setText(Html.fromHtml(movie_detaildata.get(0).getEpisode_description() + next));
                                view_more.setVisibility(View.INVISIBLE);
                                videotext.setMaxLines(Integer.MAX_VALUE);
                            }
                        }
                    }
                };
                Timer swipeTime223 = new Timer();
                swipeTime223.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler223.post(Update223);
                    }
                }, 1000, 1000);

                if (movie_detaildata.get(0).getEpisode_description() == null || movie_detaildata.get(0).getEpisode_description() == "") {
                    view_more.setVisibility(View.GONE);
                    videotext.setVisibility(View.GONE);
                }

                view_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view_more.setVisibility(View.INVISIBLE);
                        videotext.setMaxLines(Integer.MAX_VALUE);
                    }
                });
                videotext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (movie_detaildata.get(0).getEpisode_description().length() <= 160) {
                            view_more.setVisibility(View.GONE);
                        } else {
                            view_more.setVisibility(View.VISIBLE);
                        }
                        videotext.setMaxLines(2);
                    }
                });

                language1.setText(jsonResponse.getLanguages());


                //  year.setText(movie_detaildata.get(0).getYear());
                //    views.setText(movie_detaildata.get(0).getViews() + " Views");
                videotitle1.setText(movie_detaildata.get(0).getTitle());
                //textView.setText(movie_detaildata.get(0).getEpisode_description());
                //textView.setShowingChar(200);
                //number of line you want to short
               /* textView.setShowingLine(2);

                textView.addShowMoreText("show More");
                textView.addShowLessText("show Less");

                textView.setShowMoreColor(R.color.colorAccent); // or other color
                textView.setShowLessTextColor(R.color.colorAccent);*/


                //   urlll = "https://inthesky.in/public/uploads/videos/VENOM - Official Trailer (HD).mp4";

                urlll = movie_detaildata.get(0).getMp4_url();


                // urlll = movie_detaildata.get(0).getMp4_url();
                String x1 = movie_detaildata.get(0).getTitle();
                String x2 = movie_detaildata.get(0).getMp4_url();

                downurl.setText(x2);
                downname.setText(x1);
                downimgurl.setText(userprofile);

                getDetails();


                // mInstancee =this;

                if (player == null) {

                } else {
                    videoDurationInSeconds = player.getDuration();


                }


                //   urlll = "https://bitmovin-a.akamaihd.net/content/playhouse-vr/mpds/105560.mpd";
                // Uri dashUri = Uri.parse(urlll);
                // Uri subUri =Uri.parse(suburl);


                /*   if(subtitle_on.getVisibility() == View.GONE) {

                 *//*  MediaSource contentMediaSource = buildMediaSource1(dashUri);
                    MediaSource[] mediaSources = new MediaSource[2]; //The Size must change depending on the Uris
                    mediaSources[0] = contentMediaSource; // uri

                    //Add subtitles
                    SingleSampleMediaSource subtitleSource = new SingleSampleMediaSource(subUri, dataSourceFactory,
                            Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP, Format.NO_VALUE, "en", null),
                            C.TIME_UNSET);

                    mediaSources[1] = subtitleSource;

                    MediaSource mediaSource = new MergingMediaSource(mediaSources);

                    player.prepare(mediaSource);
                    player.setPlayWhenReady(true);*//*

                }*/




           /*     mediaSource = buildMediaSource(Uri.parse(urlll));
                player.prepare(mediaSource);
                player.addListener(new PlayerEventListener());
                player.setPlayWhenReady(true);
                player.addAnalyticsListener(new EventLogger(trackSelector));
                playerView.setPlayer(player);
*/

           /*     if(player.getCurrentPosition() == 15245) {

                    skipintro.setVisibility(View.VISIBLE);
                    imgBwd.setVisibility(View.GONE);
                    imgFwd.setVisibility(View.GONE);
                    exoPause.setVisibility(View.GONE);
                    exoPlay.setVisibility(View.GONE);

                }*/


            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
        thismaylikerecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(HomePageEpisodeActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (recomendeddatalist.size() > position) {
                            if (recomendeddatalist.get(position) != null) {

                                Call<JSONResponse> res = ApiClient.getInstance1().getApi().getEpisodesDetail(recomendeddatalist.get(position).getId(), user_id);
                                res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                    @Override
                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                        JSONResponse jsonResponse = response.body();
                                        movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getEpisode()));

                                        if (user_id == null) {

                                            Intent in = new Intent(getApplicationContext(), HomePageEpisodeActivity.class);
                                            in.putExtra("id", recomendeddatalist.get(position).getId());
                                            in.putExtra("url", recomendeddatalist.get(position).getMp4_url());
                                            startActivity(in);


                                        } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                            if (response.body().getPpv_video_status().equalsIgnoreCase("pay_now")) {
                                                Intent in = new Intent(getApplicationContext(), HomePageEpisodeActivity.class);
                                                in.putExtra("id", recomendeddatalist.get(position).getId());
                                                in.putExtra("url", recomendeddatalist.get(position).getMp4_url());
                                                in.putExtra("xtra", "norent");
                                                startActivity(in);

                                            } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                Intent in = new Intent(getApplicationContext(), HomePageEpisodeActivity.class);
                                                in.putExtra("id", recomendeddatalist.get(position).getId());
                                                in.putExtra("url", recomendeddatalist.get(position).getMp4_url());
                                                in.putExtra("xtra", "norent");
                                                startActivity(in);

                                            } else {

                                                String videourl = recomendeddatalist.get(position).getMp4_url();

                                                Intent in = new Intent(getApplicationContext(), HomePageEpisodeActivity.class);
                                                in.putExtra("id", recomendeddatalist.get(position).getId());
                                                in.putExtra("url", videourl);
                                                in.putExtra("xtra", "rentted");
                                                startActivity(in);


                                            }

                                        } else {

                                            String videourl = recomendeddatalist.get(position).getMp4_url();
                                            Intent in = new Intent(getApplicationContext(), HomePageEpisodeActivity.class);
                                            in.putExtra("id", recomendeddatalist.get(position).getId());
                                            in.putExtra("url", videourl);
                                            in.putExtra("xtra", "Norent");
                                            startActivity(in);

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                                        Log.d("Error", t.getMessage());
                                    }
                                });
                            }
                        }


                    }
                })
        );

        //VideoDetails();
        // ThisMayAlsoLikeVideos();


        Call<JSONResponse> alsolikeres = ApiClient.getInstance1().getApi().getAlsolikeEpisodes(videoId);
        alsolikeres.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();

                if (jsonResponse.getRelated_episode().length == 0) {

                    norecommanded.setVisibility(View.VISIBLE);
                    thismaylikerecycler.setVisibility(View.GONE);
                } else {

                    norecommanded.setVisibility(View.GONE);
                    thismaylikerecycler.setVisibility(View.VISIBLE);
                    recomendeddatalist = new ArrayList<>(Arrays.asList(jsonResponse.getRelated_episode()));
                    thismaylikeadopter = new ThismaylikeEpisodeAdopter(recomendeddatalist);
                    thismaylikerecycler.setAdapter(thismaylikeadopter);

                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());

            }
        });

        Call<JSONResponse> videocast = ApiClient.getInstance1().getApi().getVideoCast(videoId);
        videocast.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();


                if (jsonResponse.getCast_details().length == 0) {


                    castandcrewrecycler.setVisibility(View.GONE);
                    no_cast.setVisibility(View.VISIBLE);
                    castlayout.setVisibility(View.GONE);

                } else {

                    castandcrewrecycler.setVisibility(View.VISIBLE);
                    no_cast.setVisibility(View.GONE);
                    castdetails = new ArrayList<>(Arrays.asList(jsonResponse.getCast_details()));
                    castandcrewadapter = new castandcrewadapter(castdetails);
                    castandcrewrecycler.setAdapter(castandcrewadapter);
                    //    cast_name.setText(jsonResponse.getCast());

                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());

            }
        });


       /* Call<JSONResponse> videocast = ApiClient.getInstance1().getApi().getVideoCast("39");
        videocast.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();



                if(jsonResponse.getCast_details().length ==0) {


                }
                else {


                    castdetails = new ArrayList<>(Arrays.asList(jsonResponse.getCast_details()));
                    castandcrewadapter = new castandcrewadapter(castdetails);
                    castandcrewrecycler.setAdapter(castandcrewadapter);
                   *//* cast_name.setText(jsonResponse.getCast());*//*

                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());

            }
        });*/


    }

    private void checkmenu() {
    }

    private void getDetails() {

        // Toast.makeText(getApplicationContext(),"method called"+"\n"+idd.getText().toString()+"\n"+urll.getText().toString(),Toast.LENGTH_LONG).show();

        videoModels.add(new VideoModel(downimgurl.getText().toString(), downname.getText().toString(), downurl.getText().toString(), 15));
        mInstancee = this;
        useragent1 = Util.getUserAgent(this, "AdaptiveExoplayer");

        SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(videoModels);
        editor.putString("key", json);
        editor.apply();
        editor.commit();


    }


    private void processPayment() {
        paypalamount = "1";
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(paypalamount)), "XOF",
                "Video on rent ", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    public void getPayment() {
        Call<JSONResponse> callimg = ApiClient.getInstance1().getApi().currencyConverter("United States of America", ppv_price);
        callimg.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                if (!(jsonResponse.getCurrency_Converted().isEmpty()) || jsonResponse.getCurrency_Converted() != null) {

                    String[] curencySplit = jsonResponse.getCurrency_Converted().split(" ");
                    String[] remCurr = curencySplit[1].split("");
                    String totalAmount = remCurr[0] + remCurr[1] + remCurr[2] + remCurr[3];
                    Double addTax = Double.parseDouble(totalAmount) + 0.2;
                    paypalamount = String.valueOf(addTax);
                    PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paypalamount)), "USD", "Season on rent", PayPalPayment.PAYMENT_INTENT_SALE);
                    Intent intent = new Intent(HomePageEpisodeActivity.this, PaymentActivity.class);
                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                    startActivityForResult(intent, PAYPAL_REQUEST_CODE);
                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error: ", t.getMessage());
            }
        });
    }

/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            PaymentConfirmation confirm = null;
            try {

            } catch (Exception e) {
                confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            }

            if (confirm != null) {

                try {
                    String paymentDetail = confirm.toJSONObject().toString();
                    JSONObject payObj = new JSONObject(paymentDetail);
                    String payID = payObj.getJSONObject("response").getString("id");
                    String state = payObj.getJSONObject("response").getString("state");
                    //Toast.makeText(HomePageVideoActivity.this, "Payment " + state + "\n with payment id is " + payID, Toast.LENGTH_LONG).show();
                    Api.getClient().getAddPayperViewSeason(user_id, series_id, seasonid, payID, "PayPal", ppv_price,"Android", new Callback<Addpayperview>() {
                        @Override
                        public void success(Addpayperview addpayperview1, Response response) {

                            addpayperview = addpayperview1;
                            if (addpayperview.getStatus().equalsIgnoreCase("true")) {
                                Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                                changeAccess();

                            } else if (addpayperview.getStatus().equalsIgnoreCase("false")) {
                                Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });


                } catch (JSONException e) {
                    Toast.makeText(HomePageEpisodeActivity.this, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == Activity.RESULT_CANCELED) {
                Toast.makeText(HomePageEpisodeActivity.this, "Payment Canceled", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(HomePageEpisodeActivity.this, "Invalid Payment", Toast.LENGTH_LONG).show();
        } else if (requestCode == Activity.RESULT_CANCELED) {
            Toast.makeText(HomePageEpisodeActivity.this, "Payment Canceled", Toast.LENGTH_LONG).show();
        }
    }*/

    private void transactionLog(){

        Log.w("runmawwi","ui: "+user_id+" price: "+quality_price+" vi: "+videoId+" sea: "+seasonid+" series: "+series_id);
        Call<JSONResponse1> trac_api=ApiClient.getInstance1().getApi().transactioLog(user_id,quality_price,"ppv", "razoray","android",quality_resolution,"","",seasonid,series_id);
        trac_api.enqueue(new retrofit2.Callback<JSONResponse1>() {
            @Override
            public void onResponse(Call<JSONResponse1> call, retrofit2.Response<JSONResponse1> response) {
                Log.w("runmawwi"," success: "+response.body().getMessage());
            }
            @Override
            public void onFailure(Call<JSONResponse1> call, Throwable throwable) {
                Log.w("runmawwi"," err: "+throwable.getMessage());
            }
        });
    }

    private void createOrder() {
        transactionLog();
        String credentials = RAZORPAY_KEY_ID + ":" + RAZORPAY_KEY_SECRET;
        String authHeader = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        int amt= Integer.parseInt(quality_price);
        String priceTotal = String.valueOf(amt * 100);

        Order order = new Order(priceTotal, "INR", "order_rcptid_12", "1");
        Call<JSONResponse> list = ApiClient.getInstance1().getApi2().createOrder("Authorization" + authHeader, order);
        list.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                if (response.code() == 200) {
                    orderId=response.body().getId();
                    startPayment(response.body().getId());
                    Log.w("runmawii", "amt: " + quality_price);
                    Log.w("runmawii", "id: " + response.body().getId());
                } else {
                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable throwable) {
                Log.w("runmawii", "api failed: " + throwable.getMessage());
            }
        });

    }

    private void startPayment(String id) {

        final Activity activity = this;
        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Runmawi");
            options.put("description", id);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            options.put("order_id", id);
            String payment = quality_price;
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
            //Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onPaymentSuccess(String s) {

        fetchPaymentDetails(s);

    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            Api.getClient().getAddPayperViewSeason(user_id, series_id, seasonid, orderId, "failed", "razorpay", quality_resolution, quality_price, "Android", new Callback<Addpayperview>() {
                @Override
                public void success(Addpayperview addpayperview1, Response response) {

                    addpayperview = addpayperview1;
                    if (addpayperview.getStatus().equalsIgnoreCase("true")) {
                        //Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();

                        changeAccess();
                    } else if (addpayperview.getStatus().equalsIgnoreCase("false")) {
                        //Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                }
            });
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }

    private void fetchPaymentDetails(String paymentId) {

        //Generate Basic Auth header
        String credentials = RAZORPAY_KEY_ID + ":" + RAZORPAY_KEY_SECRET;
        String authHeader = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        Call<JSONResponse> list = ApiClient.getInstance1().getApi1().dynamicGetRequest("Authorization" + authHeader, paymentId);
        list.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                Log.w("runmawii", "api success: " + response);
                Log.w("runmawii", "api success: " + response.body().getStatus() + " daa: " + response.body().getAmount());

                Api.getClient().getAddPayperViewSeason(user_id, series_id, seasonid, orderId, response.body().getStatus(), "razorpay", quality_resolution, quality_price, "Android", new Callback<Addpayperview>() {
                    @Override
                    public void success(Addpayperview addpayperview1, Response response) {

                        addpayperview = addpayperview1;
                        if (addpayperview.getStatus().equalsIgnoreCase("true")) {
                            Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();

                            changeAccess();
                        } else if (addpayperview.getStatus().equalsIgnoreCase("false")) {
                            Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                    }
                });
            }
            @Override
            public void onFailure(Call<JSONResponse> call, Throwable throwable) {
                Log.w("runmawii", "api failed: " + throwable.getMessage());
            }
        });
    }


    private MediaSource buildMediaSource1(Uri urlll) {

        return buildMediaSource(urlll, null);
    }


//    private void observerVideoStatus() {
//        if (downloadManager.getCurrentDownloads().size() > 0) {
//            for (int i = 0; i < downloadManager.getCurrentDownloads().size(); i++) {
//                Download currentDownload = downloadManager.getCurrentDownloads().get(i);
//                if (!urlll.isEmpty() && currentDownload.request.uri.equals(parse(urlll))) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (downloadTracker.downloads.size() > 0) {
//                                if (currentDownload.request.uri.equals(parse(urlll))) {
//
//                                    Download downloadFromTracker = downloadTracker.downloads.get(parse(urlll));
//                                    if (downloadFromTracker != null) {
//                                        switch (downloadFromTracker.state) {
//                                            case STATE_QUEUED:
//                                                setCommonDownloadButton(ExoDownloadState.DOWNLOAD_QUEUE);
//                                                break;
//
//                                            case STATE_STOPPED:
//                                                setCommonDownloadButton(ExoDownloadState.DOWNLOAD_RESUME);
//                                                setCommonDownloadButton(ExoDownloadState.DOWNLOAD_RESUME);
//                                                break;
//
//                                            case STATE_DOWNLOADING:
//
//                                                setCommonDownloadButton(ExoDownloadState.DOWNLOAD_PAUSE);
//
//                                                if (downloadFromTracker.getPercentDownloaded() != -1) {
//
//
//                                                    progressBarPercentage.setVisibility(View.VISIBLE);
//                                                    progress_layout.setVisibility(View.VISIBLE);
//
//                                                    progress_text.setText(AppUtil.floatToPercentage(downloadFromTracker.getPercentDownloaded()));
//
//
//                                                    progressBarPercentage.setProgress(Integer.parseInt(AppUtil.floatToPercentage(downloadFromTracker.getPercentDownloaded()).replace("%", "")));
////                                                                                 tvProgressPercentage.setText(AppUtil.floatToPercentage(downloadFromTracker.getPercentDownloaded()));
//                                                }
//
//                                                Log.d("EXO STATE_DOWNLOADING ", +downloadFromTracker.getBytesDownloaded() + " " + downloadFromTracker.contentLength);
//                                                Log.d("EXO  STATE_DOWNLOADING ", "" + downloadFromTracker.getPercentDownloaded());
//
//                                                break;
//                                            case STATE_COMPLETED:
//
//
//                                                setCommonDownloadButton(ExoDownloadState.DOWNLOAD_COMPLETED);
//                                                progressBarPercentage.setVisibility(View.GONE);
//                                                progress_layout.setVisibility(View.GONE);
//
//
//                                                Log.d("EXO STATE_COMPLETED ", +downloadFromTracker.getBytesDownloaded() + " " + downloadFromTracker.contentLength);
//                                                Log.d("EXO  STATE_COMPLETED ", "" + downloadFromTracker.getPercentDownloaded());
//
//                                                progressBarPercentage.setVisibility(View.GONE);
//
//
//                                                break;
//
//                                            case STATE_FAILED:
//                                                setCommonDownloadButton(ExoDownloadState.DOWNLOAD_RETRY);
//
//                                                break;
//
//                                            case STATE_REMOVING:
//
//
//                                                break;
//
//                                            case STATE_RESTARTING:
//
//
//                                                break;
//                                        }
//                                    }
//                                }
//
//                            }
//                        }
//                    });
//                }
//            }
//        }
//
//    }


    protected void createView() {
        handler = new Handler();
        // tvPlaybackSpeed = findViewById(R.id.tv_play_back_speed);
        //  tvPlaybackSpeed.setOnClickListener(this);
        //  tvPlaybackSpeed.setText("" + tapCount);
        //  tvPlaybackSpeedSymbol = findViewById(R.id.tv_play_back_speed_symbol);
        //tvPlaybackSpeedSymbol.setOnClickListener(this);
        imgBwd = findViewById(R.id.img_bwd);
        exoPlay = findViewById(R.id.exo_play1);
        exoPause = findViewById(R.id.exo_pause);

        videoThumb = (ImageView) findViewById(R.id.videothumb);

        imgFwd = findViewById(R.id.img_fwd);
        tvPlayerCurrentTime = findViewById(R.id.tv_player_current_time);
        exoTimebar = findViewById(R.id.exo_progress);
        exoProgressbar = findViewById(R.id.loading_exoplayer);
        tvPlayerEndTime = findViewById(R.id.tv_player_end_time);
        imgSetting = findViewById(R.id.img_setting);
        //  imgFullScreenEnterExit = findViewById(R.id.img_full_screen_enter_exit);
        // imgFullScreenEnterExit.setOnClickListener(this);
        imgBackPlayer = findViewById(R.id.img_back_player);
        playerView = findViewById(R.id.player_view);
        // imgSetting.setOnClickListener(this);
        playerView.setControllerVisibilityListener(this);
        //     playerView.setErrorMessageProvider(new PlayerErrorMessageProvider());
        playerView.requestFocus();
        llParentContainer = (LinearLayout) findViewById(R.id.ll_parent_container);
        frameLayoutMain = findViewById(R.id.frame_layout_main);
        findViewById(R.id.img_back_player).setOnClickListener(this);
        progressBarPercentage = findViewById(R.id.progress_horizontal_percentage);
        progressBarPercentage.setVisibility(View.GONE);
        progress_layout.setVisibility(View.GONE);

        llDownloadContainer = (LinearLayout) findViewById(R.id.ll_download_container);
        llDownloadVideo = (LinearLayout) findViewById(R.id.ll_download_video);
        imgDownloadState = (ImageView) findViewById(R.id.img_download_state);
        tvDownloadState = (TextView) findViewById(R.id.tv_download_state);
        llDownloadVideo.setOnClickListener(this);


        playerView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        setProgress();


    }


    public void prepareView() {
        playerView.setLayoutParams(
                new PlayerView.LayoutParams(
                        // or ViewGroup.LayoutParams.WRAP_CONTENT
                        PlayerView.LayoutParams.MATCH_PARENT,
                        // or ViewGroup.LayoutParams.WRAP_CONTENT,
                        ScreenUtils.convertDIPToPixels(HomePageEpisodeActivity.this, playerHeight)));


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
        ///  downloadTracker.addListener(this);


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
        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        videoId = prefs.getString(sharedpreferences.episode_id, null);
        seasonid = prefs.getString(sharedpreferences.season_id, null);
        series_id = prefs.getString(sharedpreferences.series_id, null);
        updateTrailerMoreLikeThis();
        changeAccess();
        if (Util.SDK_INT <= 23 || player == null) {
            // initializePlayer();
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
        // downloadTracker.removeListener(this);
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
        //  outState.putParcelable(KEY_TRACK_SELECTOR_PARAMETERS, trackSelectorParameters);
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
                                    ScreenUtils.convertDIPToPixels(HomePageEpisodeActivity.this, playerHeight)));


                    frameLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                    //  imgFullScreenEnterExit.setImageResource(R.drawable.exo_controls_fullscreen_enter);
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
                    //imgFullScreenEnterExit.setImageResource(R.drawable.exo_controls_fullscreen_exit);
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

                // downloadManager.addDownload(downloadTracker.getDownloadRequest(Uri.parse(urlll)), Download.STATE_STOPPED);

//                DownloadService.sendSetStopReason(
//                        HomePageEpisodeActivity.this,
//                        DemoDownloadService.class,
//                        downloadTracker.getDownloadRequest(Uri.parse(urlll)).id,
//                        Download.STATE_STOPPED,
//                        /* foreground= */ false);

                break;

            case DOWNLOAD_RESUME:

                // downloadManager.addDownload(downloadTracker.getDownloadRequest(Uri.parse(urlll)), Download.STOP_REASON_NONE);
//                DownloadService.sendSetStopReason(
//                        HomePageEpisodeActivity.this,
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
////                            HomePageEpisodeActivity.this,
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
            pDialog = new ProgressDialog(HomePageEpisodeActivity.this);
            pDialog.setTitle(null);
            pDialog.setCancelable(false);
            pDialog.setMessage("Preparing Download Options...");
            pDialog.show();
        }


      /*  DownloadHelper downloadHelper = DownloadHelper.forDash(HomePageEpisodeActivity.this, Uri.parse(urlll), dataSourceFactory, new DefaultRenderersFactory(HomePageEpisodeActivity.this));


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
                } else {

                    //   Toast.makeText(getApplicationContext(),"d", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }


                optionsToDownload.clear();
                showDownloadOptionsDialog(myDownloadHelper, trackKeys);
            }

            @Override
            public void onPrepareError(DownloadHelper helper, IOException e) {


                Log.d("sdsdd", String.valueOf(e));
                //  Toast.makeText(getApplicationContext(),"failed"+e, Toast.LENGTH_LONG).show();
                pDialog.dismiss();

            }
        });*/
    }

    private void showDownloadOptionsDialog(DownloadHelper helper, List<TrackKey> trackKeyss) {

        if (helper == null) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(HomePageEpisodeActivity.this);
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
                HomePageEpisodeActivity.this, // Context
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

   /* @Override
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


        if (user_id == null || user_role == null) {

            player = ExoPlayerFactory.newSimpleInstance(*//* context= *//* HomePageEpisodeActivity.this, renderersFactory, trackSelector, defaultLoadControl);
            player.addListener(new PlayerEventListener());
            player.setPlayWhenReady(false);
            player.addAnalyticsListener(new EventLogger(trackSelector));
            playerView.setPlayer(player);
            playerView.setPlaybackPreparer(this);


            exoButtonPrepareDecision();
            updateButtonVisibilities();

            initBwd();
            initFwd();

        } else if (user_role != null && user_role.equalsIgnoreCase("registered")) {

            playerView.setVisibility(View.GONE);
            videoThumb.setVisibility(View.VISIBLE);
            player = ExoPlayerFactory.newSimpleInstance(*//* context= *//* HomePageEpisodeActivity.this, renderersFactory, trackSelector, defaultLoadControl);
            player.addListener(new PlayerEventListener());
            player.setPlayWhenReady(false);
            player.addAnalyticsListener(new EventLogger(trackSelector));
            playerView.setPlayer(player);
            playerView.setPlaybackPreparer(this);


            exoButtonPrepareDecision();
            updateButtonVisibilities();

            initBwd();
            initFwd();
        } else {

            playerView.setVisibility(View.GONE);
            videoThumb.setVisibility(View.VISIBLE);
            player = ExoPlayerFactory.newSimpleInstance(*//* context= *//* HomePageEpisodeActivity.this, renderersFactory, trackSelector, defaultLoadControl);
            player.addListener(new PlayerEventListener());
            player.setPlayWhenReady(false);
            player.addAnalyticsListener(new EventLogger(trackSelector));
            playerView.setPlayer(player);
            playerView.setPlaybackPreparer(this);


            exoButtonPrepareDecision();

            updateButtonVisibilities();
            initBwd();
            initFwd();

        }


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
                //   return new DashMediaSource.Factory(dataSourceFactory);
                // .createMediaSource(uri);
            case C.TYPE_SS:
                //return new SsMediaSource.Factory(dataSourceFactory)
                //       .createMediaSource(uri);
            case C.TYPE_HLS:
                //return new HlsMediaSource.Factory(dataSourceFactory)
                //  .createMediaSource(uri);
            case C.TYPE_OTHER:
                // return new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
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
        //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
        if (isScreenLandscape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            playerView.setLayoutParams(
                    new PlayerView.LayoutParams(
                            // or ViewGroup.LayoutParams.WRAP_CONTENT
                            PlayerView.LayoutParams.MATCH_PARENT,
                            // or ViewGroup.LayoutParams.WRAP_CONTENT,
                            ScreenUtils.convertDIPToPixels(HomePageEpisodeActivity.this, playerHeight)));


            frameLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

            // imgFullScreenEnterExit.setImageResource(R.drawable.exo_controls_fullscreen_enter);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            isScreenLandscape = false;
            hide();


        } else {
            finish();

        }

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
                progress_layout.setVisibility(View.GONE);


                Log.d("EXO COMPLETED ", +download.getBytesDownloaded() + " " + download.contentLength);
                Log.d("EXO  COMPLETED ", "" + download.getPercentDownloaded());


                if (download.request.uri.toString().equals(urlll)) {

                    if (download.getPercentDownloaded() != -1) {
                        progressBarPercentage.setVisibility(View.VISIBLE);
                        progress_layout.setVisibility(View.VISIBLE);

                        progress_text.setText(AppUtil.floatToPercentage(download.getPercentDownloaded()));

                        progressBarPercentage.setProgress(Integer.parseInt(AppUtil.floatToPercentage(download.getPercentDownloaded()).replace("%", "")));
//                        tvDownloadProgressMb.setText(AppUtil.getProgressDisplayLine(download.getBytesDownloaded(),download.contentLength)+" MB");
//                        tvProgressPercentage.setText(AppUtil.floatToPercentage(download.getPercentDownloaded()));
                    }
                }

                progressBarPercentage.setVisibility(View.GONE);

                progress_layout.setVisibility(View.VISIBLE);

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
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        this.scaleGestureDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
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


    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (playerView.getResizeMode() == AspectRatioFrameLayout.RESIZE_MODE_FILL) {

            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        } else {
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

            }
            updateButtonVisibilities();
        }

      //  @Override
        public void onPositionDiscontinuity(@Player.DiscontinuityReason int reason) {
        //    if (player.getPlaybackError() != null) {
                // The user has performed a seek whilst in the error state. Update the resume position so
                // that if the user then retries, playback resumes from the position to which they seeked.
            //    updateStartPosition();
          //  }
        }

       // @Override
        public void onPlayerError(ExoPlaybackException e) {
            if (isBehindLiveWindow(e)) {
                clearStartPosition();
             //   initializePlayer();
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


    @SuppressLint("UseCompatLoadingForDrawables")
    public void setCommonDownloadButton(ExoDownloadState exoDownloadState) {
        switch (exoDownloadState) {

            case DOWNLOAD_START:
                llDownloadVideo.setTag(exoDownloadState);
                tvDownloadState.setText(exoDownloadState.getValue());
                imgDownloadState.setImageDrawable(getResources().getDrawable(R.drawable.downloadimd));

                break;

            case DOWNLOAD_PAUSE:
                llDownloadVideo.setTag(exoDownloadState);
                tvDownloadState.setText(exoDownloadState.getValue());
                imgDownloadState.setImageDrawable(getResources().getDrawable(R.drawable.download_pause_light));
                break;

            case DOWNLOAD_RESUME:
                llDownloadVideo.setTag(exoDownloadState);
                tvDownloadState.setText(exoDownloadState.getValue());
                imgDownloadState.setImageDrawable(getResources().getDrawable(R.drawable.loading_light_download));
                break;

            case DOWNLOAD_RETRY:
                llDownloadVideo.setTag(exoDownloadState);
                tvDownloadState.setText(exoDownloadState.getValue());
                imgDownloadState.setImageDrawable(getResources().getDrawable(R.drawable.ic_retry));

                break;

            case DOWNLOAD_COMPLETED:
                llDownloadVideo.setTag(exoDownloadState);
                tvDownloadState.setText(exoDownloadState.getValue());
                imgDownloadState.setImageDrawable(getResources().getDrawable(R.drawable.downloaded));

                //   downloadtext.setCompoundDrawablesWithIntrinsicBounds(R.drawable.downloaded_24, 0, 0, 0);
                break;

            case DOWNLOAD_QUEUE:
                llDownloadVideo.setTag(exoDownloadState);
                tvDownloadState.setText(exoDownloadState.getValue());
                imgDownloadState.setImageDrawable(getResources().getDrawable(R.drawable.ic_queue));

                break;
        }

    }

    public static synchronized HomePageEpisodeActivity getInstance() {
        return mInstancee;
    }


    private void showDetails(JSONObject jsonDetails) throws JSONException {

        // Toast.makeText(getApplicationContext(),""+jsonDetails.getString("id"), Toast.LENGTH_LONG).show();
        Log.d("sdfdfdffer", jsonDetails.getString("id"));

    }

    private class MyTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        @Override
        protected String doInBackground(String... params) {

            String email_ou_numero_du_client_paydunya = params[0];
            Double montant_a_transferer = Double.valueOf(params[1]);
            try {
                PaydunyaDirectPay direct_pay = new PaydunyaDirectPay(setup);
                if (direct_pay.creditAccount(email_ou_numero_du_client_paydunya, montant_a_transferer)) {
                    Log.w("direct_pay", "if: " + direct_pay.getStatus() + " trac_ID: " + direct_pay.getTransactionId());
                    return direct_pay.getStatus() + ":" + direct_pay.getTransactionId();
                } else {
                    Log.w("direct_pay", "else: " + direct_pay.getStatus() + direct_pay.getResponseText());
                    return direct_pay.getStatus() + ":" + direct_pay.getResponseText();
                }
            } catch (Exception e) {
                e.printStackTrace();
                this.exception = e;
                Log.w("direct_pay", "catch: " + e.getMessage());
                return "Error" + ":" + e.getMessage();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            paydunyaResponse(result);
        }

    }

    public void paydunyaResponse(String result) {

        String[] splited = result.split(":");

        if (splited[0].equalsIgnoreCase("success")) {
            Call<JSONResponse> pay_api = ApiClient.getInstance1().getApi().paydunyaSeasonRent(user_id, seasonid, "Android");
            pay_api.enqueue(new retrofit2.Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                    paydunya_dialog.cancel();
                    JSONResponse jsonResponse = response.body();
                    if (jsonResponse.getStatus().equalsIgnoreCase("true")) {
                        Toast.makeText(HomePageEpisodeActivity.this, "" + jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        changeAccess();
                    } else {
                        Toast.makeText(HomePageEpisodeActivity.this, "" + jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JSONResponse> call, Throwable t) {
                }
            });
        } else if (splited[0].equalsIgnoreCase("fail")) {
            paydunya_email_address.setError(splited[1]);
        } else {
            paydunya_email_address.setError(result);
        }
    }

    public void updateTrailerMoreLikeThis() {
        tableLayout.removeAllTabs();
        tableLayout.addTab(tableLayout.newTab().setText("Trailers"));
        tableLayout.addTab(tableLayout.newTab().setText("More like this"));
        tableLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        EpisodeTabAdapter adapter = new EpisodeTabAdapter(this, getSupportFragmentManager(), tableLayout.getTabCount(), videoId, user_id);
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tableLayout));


        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public void changeAccess() {
        Call<JSONResponse> channela = ApiClient.getInstance1().getApi().getSeriesDetail(series_id);
        channela.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse_ser = response.body();
                ArrayList<series> series = new ArrayList<>(Arrays.asList(jsonResponse_ser.getSeries()));

                if (series.get(0).getAccess().equalsIgnoreCase("subscriber")) {


                    if (user_role.equalsIgnoreCase("admin") || user_role.equalsIgnoreCase("subscriber")) {
                        resume.setVisibility(View.GONE);
                        play_begin.setVisibility(View.GONE);
                        paynow.setVisibility(View.GONE);
                        rent.setVisibility(View.GONE);
                        playnow.setVisibility(View.VISIBLE);
                    } else {
                        resume.setVisibility(View.GONE);
                        play_begin.setVisibility(View.GONE);
                        paynow.setVisibility(View.VISIBLE);
                        rent.setVisibility(View.GONE);
                        playnow.setVisibility(View.GONE);
                    }
                } else {
                    Call<JSONResponse> ac_api = ApiClient.getInstance1().getApi().getSeasonPpv(seasonid, videoId, user_id);
                    ac_api.enqueue(new retrofit2.Callback<JSONResponse>() {//working
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                            JSONResponse jsonResponse = response.body();

                            if (user_role.equalsIgnoreCase("admin") || user_role.equalsIgnoreCase("subscriber")) {
                                resume.setVisibility(View.GONE);
                                play_begin.setVisibility(View.GONE);
                                paynow.setVisibility(View.GONE);
                                rent.setVisibility(View.GONE);
                                playnow.setVisibility(View.VISIBLE);
                            } else if (jsonResponse.getAccess().equalsIgnoreCase("ppv")) {
                                resume.setVisibility(View.GONE);
                                play_begin.setVisibility(View.GONE);
                                paynow.setVisibility(View.GONE);
                                rent.setVisibility(View.VISIBLE);
                                playnow.setVisibility(View.GONE);
                            } else if (jsonResponse.getAccess().equalsIgnoreCase("subscriber")) {
                                resume.setVisibility(View.GONE);
                                play_begin.setVisibility(View.GONE);
                                paynow.setVisibility(View.VISIBLE);
                                rent.setVisibility(View.GONE);
                                playnow.setVisibility(View.GONE);
                            } else if (jsonResponse.getAccess().equalsIgnoreCase("free")) {
                                resume.setVisibility(View.GONE);
                                play_begin.setVisibility(View.GONE);
                                paynow.setVisibility(View.GONE);
                                rent.setVisibility(View.GONE);
                                playnow.setVisibility(View.VISIBLE);
                            } else if (jsonResponse.getAccess().equalsIgnoreCase("guest")) {
                                resume.setVisibility(View.GONE);
                                play_begin.setVisibility(View.GONE);
                                paynow.setVisibility(View.GONE);
                                rent.setVisibility(View.GONE);
                                playnow.setVisibility(View.VISIBLE);
                            } else {
                                resume.setVisibility(View.GONE);
                                play_begin.setVisibility(View.GONE);
                                paynow.setVisibility(View.GONE);
                                rent.setVisibility(View.GONE);
                                playnow.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
            }
        });
    }

}