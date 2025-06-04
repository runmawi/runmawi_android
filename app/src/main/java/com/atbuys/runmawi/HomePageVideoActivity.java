package com.atbuys.runmawi;


import static androidx.media3.exoplayer.offline.Download.STATE_COMPLETED;
import static androidx.media3.exoplayer.offline.Download.STATE_DOWNLOADING;
import static androidx.media3.exoplayer.offline.Download.STATE_FAILED;
import static androidx.media3.exoplayer.offline.Download.STATE_QUEUED;
import static androidx.media3.exoplayer.offline.Download.STATE_REMOVING;
import static androidx.media3.exoplayer.offline.Download.STATE_RESTARTING;
import static androidx.media3.exoplayer.offline.Download.STATE_STOPPED;
import static com.atbuys.runmawi.Connectivity.getNetworkInfo;
import static com.atbuys.runmawi.UserHomeFragment.mediaplayer;

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
import org.json.JSONObject;
import org.json.JSONException;
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
import android.view.inputmethod.InputMethodManager;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
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

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.atbuys.runmawi.Vdocipher.Videoplayer_cipher;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.atbuys.runmawi.Adapter.RecommandeHomeAdapter;
import com.atbuys.runmawi.Api1.RetrofitSingleton;
import com.atbuys.runmawi.Config.Config;
import com.atbuys.runmawi.Model.HomeBodyResponse;
import com.atbuys.runmawi.Model.RecommandedHomeData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

import okhttp3.OkHttpClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@UnstableApi
public class HomePageVideoActivity extends AppCompatActivity implements View.OnClickListener, PlayerControlView.VisibilityListener, PaymentResultListener, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    private static final int playerHeight = 250;
    ProgressDialog pDialog;
    protected static final CookieManager DEFAULT_COOKIE_MANAGER;
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 2000;
    private static final int UI_ANIMATION_DELAY = 300;
    // private final CastPlayer castPlayer;
    // Saved instance state keys.
    private static final String KEY_TRACK_SELECTOR_PARAMETERS = "track_selector_parameters";
    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "position";
    private static final String KEY_AUTO_PLAY = "auto_play";
    private static HomePageVideoActivity mInstancee;
    protected String useragent1;
    Set<String> usersSet = new HashSet<>();
    String pdf_urll, reels_url;
    LinearLayout reelslayout, epaper_layout;
    ImageView reels_image, e_paper_image;

    TextView related, more;
    LinearLayout releatedlayout, morelayout, relatedlayoutview, morelayoutview;

    ViewPager viewPager;
    TabLayout tableLayout;

    GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;

    MediaSource subsource;
    MergingMediaSource mergedSource;

    BandwidthMeter bandwidthMeter;

    ImageView cast;
    TextView cast1;
    private ArrayList<latestvideos> recomendeddatalist;
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
    private ImageView exoPause;
    private ImageView exoreply;
    private ImageView imgFwd, imgBackPlayer;
    private TextView skipintro;
    private TextView tvPlayerCurrentTime;
    private DefaultTimeBar exoTimebar;
    private ProgressBar exoProgressbar;
    private TextView tvPlayerEndTime;
    private ImageView imgSetting;
    private ImageView imgFullScreenEnterExit;
    private LinearLayout userfeatuerlayout;
    private ImageView coverimage;
    private CardView playnow, play_begin, resume, paynow, rent;
    TextView rent_text;
    private ImageView videoThumb;
    private String ppv_price, currency;

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
    private String videoId, Image_url;
    private String videoName;
    private String videoUrl;
    RelativeLayout progress_layout;
    ReadNotification readNotification;
    Active_Payment_settingsAdopter active_payment_settingsAdopter;

    /*private TextView cast_name;*/
    private long videoDurationInSeconds;
    private Runnable runnableCode;
    private Handler handler;
    private TextView comments;
    private EditText commenttext;
    private ImageView commentsend;
    private ProgressBar commentprogress;
    private LinearLayout commentlayout;
    private RecyclerView usercommentrecycler;
    private TextView description;
    private String videoads, video_access;
    private TextView ratings;
    private TextView videocategory;
    Addpayperview addpayperview;
    ProgressDialog progressDialog;
    BottomSheetDialog paydunya_dialog;
    CardView paydunya_pay_now;
    EditText paydunya_email_address;
    PaydunyaSetup setup;
    PaydunyaCheckoutStore store;
    PaydunyaCheckoutInvoice invoice;
    private ArrayList<payment_settings> payment_settingslist;
    String key, py_id;
    private Stripe stripe;
    PaymentMethodCreateParams params;
    QualityPlansAdapter qualityPlansAdapter;
    QualityAdapter qualityAdapter;
    String quality_name, quality_price, quality_resolution;


    private int currentWindow = 0;
    private long playbackPosition = 0;

    private Switch autoplayswitch;
    String paypalamount;

    private AccessibilityService context;
    public static Dialog dialog;
    private ImageView like, liked, dislikee, dislikeed;
    LinearLayout unlikelayout, undislikelayout;
    AddComment addComment;

    TextView imdb, imdb_text, durationandyear, genre_text;
    View seperate_line;


    String nxtvidid, nextvidurl;

    LinearLayout watchlist, watchlater, hidelayout1, likee, dislike, favouriteLayout;
    ImageView watchlistimg, watchlistaddedimg, watchlaterimg, watchlateraddedimg, favlistimg, favlistaddimg;
    TextView videotext, view_more, views, viewsbottom;
    LinearLayout yearlayout;
    TextView videotitle1, language1, genre2, duration, year, getCast1;
    String shareurl, user_id, autoplay, subtitles_text, subchecked1, subchecked2, subchecked3, subchecked4;
    String video_like, video_dislike;

    LinearLayout trailerLayout, thismayalsolike_layout, share1;
    ImageView trailerImage, back, share;


    private RecyclerView.LayoutManager layoutManager1, manager;
    private ArrayList<movies> moviesdata;
    private ArrayList<RecommandedHomeData> dataList;
    private ArrayList<genre> genredata;
    private ArrayList<videodetail> movie_detaildata;
    private ArrayList<videodetail> movie_detaildata1;
    private ArrayList<quality> qualities;
    //private ArrayList<quality_free> qualities1;
    private ArrayList<quality_free> qualities1;
    private ArrayList<videossubtitles> moviesubtitlesdata;
    private ArrayList<active_payment_settings> active_payment_settingsList;
    private ArrayList<comment> usercommentslist;
    showwishlist showwish;
    private ArrayList<movieresolution> movieresolutiondata;
    private ArrayList<video_cast> castdetails;
    public static ArrayList<VideoModel> videoModels = new ArrayList<>();
    public ArrayList<String> arrPackage;

    //  DownloadActivity downloadActivity;


    private List<videossubtitles> statelistdata;
    public String urlll = "";
    public String rented = "";
    public String suburl = "";
    String type = "channel";

    TextView norecommanded, idd, urll, downimgurl, downname, downurl, downloadtext;

    private RecyclerView thismaylikerecycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    RecyclerView castandcrewrecycler;
    ExtractorsFactory extractorsFactory;
    RecommandeHomeAdapter thismaylikeadopter;
    castandcrewadapter castandcrewadapter;
    subtitleLangAdopter subtitleLangadopter;
    String user_role, auto_play;
    //ShowMoreTextView textView;
    UserCommentsAdopter userCommentsAdopter;
    CommentsAdopter commentsAdopter;
    String renturl, dataa;
    private long position;
    // private Object cast_details;.
    private TextView no_cast;
    public static String continuee;
    LinearLayout castlayout;
    public static TextView pays;
    int PAYPAL_REQUEST_CODE = 7777;
    public static PayPalConfiguration config;


    String RAZORPAY_KEY_ID;
    String RAZORPAY_KEY_SECRET;
    String orderId;
    /*String RAZORPAY_KEY_ID = "rzp_test_2YHmQefc5LWtAu";
    String RAZORPAY_KEY_SECRET = "vfMkD21soV9h3R0HZqrkh7nb";*/

    // Utility method to safely convert array to ArrayList with null check
    private static <T> ArrayList<T> safeArrayToList(T[] array) {
        if (array == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(array));
    }

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

    @SuppressLint({"WrongViewCast", "ForegroundServiceType"})
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

        gestureDetector = new GestureDetector(HomePageVideoActivity.this, HomePageVideoActivity.this);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        arrPackage = new ArrayList<>();

        dataSourceFactory = buildDataSourceFactory();
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }

        hideStatusBar();

        setContentView(R.layout.video_home_player);

        Intent in = getIntent();
        videoId = in.getStringExtra("id");
        urlll = in.getStringExtra("url");
        suburl = in.getStringExtra("suburl");
        dataa = in.getStringExtra("data");
        rented = in.getStringExtra("xtra");

        String paymentId = "pay_PdgNUQksVECYlt";
        // continuee = in.getStringExtra("continuee");

        config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION).clientId(Config.PAYPAL_CLIENT_ID);
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        thismayalsolike_layout = (LinearLayout) findViewById(R.id.thismayalsolike_layout);
        tableLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        idd = (TextView) findViewById(R.id.idd);
        urll = (TextView) findViewById(R.id.urll);
        downurl = (TextView) findViewById(R.id.downurl);
        downimgurl = (TextView) findViewById(R.id.downimgurl);
        downname = (TextView) findViewById(R.id.downname);
        //downloadtext = (TextView) findViewById(R.id.download);
        progress_text = (TextView) findViewById(R.id.progress_text);
        progress_layout = (RelativeLayout) findViewById(R.id.progress_layout);
        //textView = findViewById(R.id.text_view_show_more);
        pays = (TextView) findViewById(R.id.pays);
        castlayout = (LinearLayout) findViewById(R.id.castlayout);
        related = (TextView) findViewById(R.id.related);
        more = (TextView) findViewById(R.id.more);
        releatedlayout = (LinearLayout) findViewById(R.id.relatedlayout);
        morelayout = (LinearLayout) findViewById(R.id.morelayout);
        relatedlayoutview = (LinearLayout) findViewById(R.id.relatedlayoutview);
        morelayoutview = (LinearLayout) findViewById(R.id.morelayoutview);
        ratings = (TextView) findViewById(R.id.rating);
        videocategory = (TextView) findViewById(R.id.videocategory);
        imdb = (TextView) findViewById(R.id.imdb);
        imdb_text = (TextView) findViewById(R.id.imdb_text);
        seperate_line = (View) findViewById(R.id.seperate_line);
        durationandyear = (TextView) findViewById(R.id.durationandyear);
        genre_text = (TextView) findViewById(R.id.genre_text);


        reelslayout = (LinearLayout) findViewById(R.id.reelslayout);
        reels_image = (ImageView) findViewById(R.id.reels_image);

        epaper_layout = (LinearLayout) findViewById(R.id.e_paperlayout);
        e_paper_image = (ImageView) findViewById(R.id.e_paper_image);

        tableLayout.addTab(tableLayout.newTab().setText("Trailer"));
        tableLayout.addTab(tableLayout.newTab().setText("More like this"));
        tableLayout.addTab(tableLayout.newTab().setText("Comments"));
        tableLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);
        auto_play = prefs.getString(sharedpreferences.autoplay, null);


        final MyAdapter adapter = new MyAdapter(this, getSupportFragmentManager(), tableLayout.getTabCount(), videoId, user_id);
        viewPager.setAdapter(adapter);
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


        epaper_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent in = new Intent(getApplicationContext(), PdfActivity.class);
                // in.putExtra("pdf_name", pdf_urll);
                //  startActivity(in);
            }
        });


        reelslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_LONG).show();

                /*Intent in = new Intent(getApplicationContext(),ReelsplayActivity.class);
                in.putExtra("url",reels_url);
                startActivity(in);*/
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

                morelayout.setVisibility(View.VISIBLE);
                releatedlayout.setVisibility(View.GONE);

            }
        });


        related.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

                morelayout.setVisibility(View.GONE);
                releatedlayout.setVisibility(View.VISIBLE);

            }
        });


        trailerLayout = (LinearLayout) findViewById(R.id.trailerlayout);
        // ll_download_video_progress = (LinearLayout) findViewById(R.id.ll_download_video_progress);
        trailerImage = (ImageView) findViewById(R.id.trailerimage);
        back = (ImageView) findViewById(R.id.back);
        idd.setText(videoId);
        urll.setText(urlll);
        progressDialog = new ProgressDialog(this);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent in = new Intent(getApplicationContext(), HomePageActivitywithFragments.class);
                startActivity(in);*/
                finish();
            }
        });


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

        //AdaptiveExoplayer application = (AdaptiveExoplayer) getApplication();
        //  downloadTracker = application.getDownloadTracker();
        // downloadManager = application.getDownloadManager();


        // Start the download service if it should be running but it's not currently.
        // Starting the service in the foreground causes notification flicker if there is no scheduled
        // action. Starting it in the background throws an exception if the app is in the background too
        // (e.g. if device screen is locked).

        try {
            //  DownloadService.start(this, DemoDownloadService.class);
        } catch (IllegalStateException e) {
            //DownloadService.startForeground(this, DemoDownloadService.class);
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
                //   observerVideoStatus();
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
        resume = (CardView) findViewById(R.id.resumee);
        rent = (CardView) findViewById(R.id.rent_now);

        rent_text = (TextView) findViewById(R.id.rent_text);
        userfeatuerlayout = (LinearLayout) findViewById(R.id.userfeatuerlayout);
        autoplayswitch = (Switch) findViewById(R.id.autoplay);
        views = (TextView) findViewById(R.id.view);
        viewsbottom = (TextView) findViewById(R.id.viewsbottom);


        likee = (LinearLayout) findViewById(R.id.likeed);
        dislike = (LinearLayout) findViewById(R.id.dislike);
        unlikelayout = (LinearLayout) findViewById(R.id.unlikelayout);
        undislikelayout = (LinearLayout) findViewById(R.id.undislikelayout);

        no_cast = findViewById(R.id.no_cast);

        like = (ImageView) findViewById(R.id.like);
        liked = (ImageView) findViewById(R.id.liked);
        dislikee = (ImageView) findViewById(R.id.dislikee);
        dislikeed = (ImageView) findViewById(R.id.dislikeed);

        watchlistaddedimg = (ImageView) findViewById(R.id.watchlistaddimg);
        watchlaterimg = (ImageView) findViewById(R.id.watchlateradd);
        watchlateraddedimg = (ImageView) findViewById(R.id.watchlateraddedimg);
        share = (ImageView) findViewById(R.id.share);
        share1 = (LinearLayout) findViewById(R.id.share1);
        norecommanded = (TextView) findViewById(R.id.norecommanded);
        cast = (ImageView) findViewById(R.id.cast);
        genre2 = (TextView) findViewById(R.id.genre);
        /*     cast_name=(TextView)findViewById(R.id.cast_name);*/
        language1 = (TextView) findViewById(R.id.language);
        year = (TextView) findViewById(R.id.movieyear);
        yearlayout = (LinearLayout) findViewById(R.id.yearlayout);
        duration = (TextView) findViewById(R.id.videoduration);
        videotitle1 = (TextView) findViewById(R.id.videotitle);
        genredata = new ArrayList<genre>();
        movieresolutiondata = new ArrayList<movieresolution>();
        movie_detaildata = new ArrayList<videodetail>();
        qualities = new ArrayList<>();
        qualities1 = new ArrayList<>();
        active_payment_settingsList = new ArrayList<active_payment_settings>();
        moviesubtitlesdata = new ArrayList<videossubtitles>();
        dataList = new ArrayList<RecommandedHomeData>();
        usercommentslist = new ArrayList<comment>();
        castdetails = new ArrayList<video_cast>();

        Call<JSONResponse> id_call = ApiClient.getInstance1().getApi().getPaymentDetails();
        id_call.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JSONResponse jsonResponse = response.body();
                    payment_settingslist = safeArrayToList(jsonResponse.getPayment_settings());
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
                } else {
                    // Log error response or null body for the main getMovieDetails call
                    String errorBodyString = "null";
                    if (response.errorBody() != null) {
                        try {
                            errorBodyString = response.errorBody().string();
                        } catch (java.io.IOException e) {
                            android.util.Log.e("API_RESPONSE_IO_ERR", "IOException reading error body: " + e.getMessage());
                            errorBodyString = "Error reading error body.";
                        }
                    } else {
                        android.util.Log.w("Log_API_RESPONSE_WARN", "Error body was null for unsuccessful response.");
                    }
                    android.util.Log.e("API_RESPONSE_FAILURE", "Main getMovieDetails: Response not successful or body is null. Code: " + response.code() + " Message: " + response.message() + " ErrorBody: " + errorBodyString);
                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable throwable) {
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

                Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, videoId);
                res.enqueue(new retrofit2.Callback<JSONResponse>() {

                    @Override
                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                        if (response.isSuccessful() && response.body() != null) {


                            JSONResponse jsonResponse = response.body();
                            movie_detaildata = safeArrayToList(jsonResponse.getVideodetail());

                            // Check if video details are available
                            if (movie_detaildata == null || movie_detaildata.isEmpty()) {
                                android.util.Log.e("VIDEO_DETAILS_ERROR", "No video details found in first API response");
                                Toast.makeText(HomePageVideoActivity.this, "Video details not available. Please try again later.", Toast.LENGTH_LONG).show();
                                finish();
                                return;
                            }

                            if (user_role.equalsIgnoreCase("admin")) {
                                resume.setVisibility(View.GONE);
                                play_begin.setVisibility(View.GONE);
                                paynow.setVisibility(View.GONE);
                                rent.setVisibility(View.GONE);
                                playnow.setVisibility(View.VISIBLE);
                            } else if (movie_detaildata.get(0).getAccess().equalsIgnoreCase("ppv")) {

                                if (jsonResponse.getPpv_exist().equalsIgnoreCase("1")) {
                                    resume.setVisibility(View.GONE);
                                    play_begin.setVisibility(View.GONE);
                                    paynow.setVisibility(View.GONE);
                                    rent.setVisibility(View.GONE);
                                    playnow.setVisibility(View.VISIBLE);
                                } else {
                                    if (false) {
                                        int free_duration = Integer.parseInt(movie_detaildata.get(0).getFree_duration());//610
                                        int minutes = free_duration / 60;//10

                                        if (minutes < 1) {
                                            rent_text.setText("Play Video ( Free " + free_duration + " seconds )");
                                        } else {
                                            rent_text.setText("Play Video ( Free " + minutes + " minutes )");
                                        }
                                    } else {
                                        rent_text.setText("Rent");
                                    }

                                    resume.setVisibility(View.GONE);
                                    play_begin.setVisibility(View.GONE);
                                    paynow.setVisibility(View.GONE);
                                    rent.setVisibility(View.VISIBLE);
                                    playnow.setVisibility(View.GONE);
                                }

                            } else if (movie_detaildata.get(0).getAccess().equalsIgnoreCase("subscriber")) {

                                if (user_role.equalsIgnoreCase("subscriber")) {
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
                                resume.setVisibility(View.GONE);
                                play_begin.setVisibility(View.GONE);
                                paynow.setVisibility(View.GONE);
                                rent.setVisibility(View.GONE);
                                playnow.setVisibility(View.VISIBLE);
                            }


                        }


                    }


                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                    }
                });

            }

            @Override
            public void failure(RetrofitError error) {
            }
        });

        Log.w("Log_runmawi_test", videoId);
        Log.w("LOG_VIDEO_DEBUG", "Fetching video details for ID: " + videoId + ", User ID: " + user_id);
        
        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, videoId);
        res.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                Log.w("LOG_VIDEO_DEBUG", "API Response received. Code: " + response.code() + ", Successful: " + response.isSuccessful());
                
                if (response.isSuccessful() && response.body() != null) {

                    com.google.gson.Gson gson = new com.google.gson.Gson();
                    android.util.Log.d("API_RESPONSE_SUCCESS", "Raw: " + gson.toJson(response.body()));

                    JSONResponse jsonResponse = response.body();
                    
                    // Log the complete API response structure
                    Log.w("LOG_API_STRUCTURE", "=== Complete API Response Analysis ===");
                    Log.w("LOG_API_STRUCTURE", "Response Class: " + jsonResponse.getClass().getSimpleName());
                    try {
                        Log.w("LOG_API_STRUCTURE", "Status: " + jsonResponse.getStatus());
                        Log.w("LOG_API_STRUCTURE", "Message: " + jsonResponse.getMessage());
                        Log.w("LOG_API_STRUCTURE", "Wishlist: " + jsonResponse.getWishlist());
                        Log.w("LOG_API_STRUCTURE", "Watchlater: " + jsonResponse.getWatchlater());
                        Log.w("LOG_API_STRUCTURE", "Like: " + jsonResponse.getLike());
                        Log.w("LOG_API_STRUCTURE", "Dislike: " + jsonResponse.getDislike());
                        Log.w("LOG_API_STRUCTURE", "Userrole: " + jsonResponse.getUserrole());
                        Log.w("LOG_API_STRUCTURE", "Main Genre: " + jsonResponse.getMain_genre());
                        Log.w("LOG_API_STRUCTURE", "Languages: " + jsonResponse.getLanguages());
                        Log.w("LOG_API_STRUCTURE", "PPV Exist: " + jsonResponse.getPpv_exist());
                    } catch (Exception e) {
                        Log.e("LOG_API_STRUCTURE", "Error reading API response fields: " + e.getMessage());
                    }
                    
                    // Enhanced debugging
                    Log.w("LOG_VIDEO_DEBUG", "API Status: " + jsonResponse.getStatus());
                    Log.w("LOG_VIDEO_DEBUG", "API Message: " + jsonResponse.getMessage());
                    Log.w("LOG_VIDEO_DEBUG", "VideoDetail array null: " + (jsonResponse.getVideodetail() == null));
                    if (jsonResponse.getVideodetail() != null) {
                        Log.w("LOG_VIDEO_DEBUG", "VideoDetail array length: " + jsonResponse.getVideodetail().length);
                        
                        // Log detailed video details information
                        if (jsonResponse.getVideodetail().length > 0) {
                            videodetail firstVideo = jsonResponse.getVideodetail()[0];
                            Log.w("LOG_VIDEO_DETAIL", "=== First Video Details ===");
                            Log.w("LOG_VIDEO_DETAIL", "Video ID: " + firstVideo.getId());
                            Log.w("LOG_VIDEO_DETAIL", "Video Title: " + firstVideo.getTitle());
                            Log.w("LOG_VIDEO_DETAIL", "Video Type: " + firstVideo.getType());
                            Log.w("LOG_VIDEO_DETAIL", "Video Access: " + firstVideo.getAccess());
                            
                            // Check for VideoCipher specific fields
                            try {
                                String otp = firstVideo.getOtp();
                                String playbackInfo = firstVideo.getPlaybackInfo();
                                Log.w("LOG_VIDEO_DETAIL", "OTP Present: " + (otp != null));
                                Log.w("LOG_VIDEO_DETAIL", "OTP Value: " + (otp != null ? otp : "NULL"));
                                Log.w("LOG_VIDEO_DETAIL", "PlaybackInfo Present: " + (playbackInfo != null));
                                Log.w("LOG_VIDEO_DETAIL", "PlaybackInfo Value: " + (playbackInfo != null ? playbackInfo.substring(0, Math.min(100, playbackInfo.length())) + "..." : "NULL"));
                            } catch (Exception e) {
                                Log.e("LOG_VIDEO_DETAIL", "Error accessing OTP/PlaybackInfo: " + e.getMessage());
                            }
                            
                            // Check for video URLs
                            try {
                                Log.w("LOG_VIDEO_DETAIL", "MP4 URL: " + firstVideo.getMp4_url());
                                Log.w("LOG_VIDEO_DETAIL", "Video URL: " + firstVideo.getVideos_url());
                                Log.w("LOG_VIDEO_DETAIL", "Trailer URL: " + firstVideo.getTrailer());
                            } catch (Exception e) {
                                Log.e("LOG_VIDEO_DETAIL", "Error accessing video URLs: " + e.getMessage());
                            }
                            
                            // Check PPV related fields
                            try {
                                Log.w("LOG_VIDEO_DETAIL", "PPV Price: " + firstVideo.getPpv_price());
                                Log.w("LOG_VIDEO_DETAIL", "PPV Plan: " + firstVideo.getPPV_Plan());
                            } catch (Exception e) {
                                Log.e("LOG_VIDEO_DETAIL", "Error accessing PPV fields: " + e.getMessage());
                            }
                            
                            Log.w("LOG_VIDEO_DETAIL", "=== End Video Details ===");
                        }
                    }
                    
                    // Check if videodetail array is null to prevent NullPointerException
                    if (jsonResponse.getVideodetail() == null) {
                        android.util.Log.e("VIDEO_DETAILS_ERROR", "API returned null videodetail array");
                        Toast.makeText(HomePageVideoActivity.this, "Video details not available. Please try again later.", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                    
                    // Populate movie_detaildata from the current response
                    Log.w("LOG_VIDEO_DEBUG", "Populating movie_detaildata from API response...");
                    movie_detaildata = safeArrayToList(jsonResponse.getVideodetail());
                    Log.w("LOG_VIDEO_DEBUG", "movie_detaildata populated. Size: " + (movie_detaildata != null ? movie_detaildata.size() : "NULL"));
                    
                    // Check if the API request was successful and video details are available
                    if (!"true".equalsIgnoreCase(jsonResponse.getStatus()) || movie_detaildata == null || movie_detaildata.isEmpty()) {
                        android.util.Log.e("VIDEO_DETAILS_ERROR", "API returned error or empty video details. Status: " + jsonResponse.getStatus() + ", Message: " + jsonResponse.getMessage());
                        
                        // Show error message to user
                        if (jsonResponse.getMessage() != null && !jsonResponse.getMessage().isEmpty()) {
                            Toast.makeText(HomePageVideoActivity.this, jsonResponse.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(HomePageVideoActivity.this, "Video details not available. Please try again later.", Toast.LENGTH_LONG).show();
                        }
                        
                        // Don't finish the activity, just return to avoid crashes
                        return;
                    }

                    video_dislike = jsonResponse.getDislike();

                    genre2.setText(jsonResponse.getMain_genre());

                    nxtvidid = jsonResponse.getVideonext();
                    nextvidurl = jsonResponse.getNext_url();
                    videoads = jsonResponse.getVideoads();

                    String userprofile = "https://runmawi.com/public/uploads/images/" + movie_detaildata.get(0).getPlayer_image();
                    Picasso.get().load(userprofile).into(videoThumb);
                    Picasso.get().load(userprofile).into(trailerImage);
                    Picasso.get().load(userprofile).into(reels_image);

                    String next = "<font color='#ff0000'>  View Less</font>";
                    //videotext.setText(Html.fromHtml(movie_detaildata.get(0).getDescription()));

                    final Handler handler223 = new Handler();
                    final Runnable Update223 = new Runnable() {
                        public void run() {

                            if (movie_detaildata.get(0).getDescription() == "" || movie_detaildata.get(0).getDescription() == null) {
                            } else {

                                if (videotext.getMaxLines() == 2) {
                                    videotext.setText(Html.fromHtml(movie_detaildata.get(0).getDescription()));

                                    if (movie_detaildata.get(0).getDescription().length() <= 160) {
                                        view_more.setVisibility(View.GONE);
                                    } else {
                                        view_more.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    videotext.setText(Html.fromHtml(movie_detaildata.get(0).getDescription() + next));
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

                    if (movie_detaildata.get(0).getDescription() == null || movie_detaildata.get(0).getDescription() == "") {
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
                            if (movie_detaildata.get(0).getDescription().length() <= 160) {
                                view_more.setVisibility(View.GONE);
                            } else {
                                view_more.setVisibility(View.VISIBLE);
                            }
                            videotext.setMaxLines(2);
                        }
                    });


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

                    if (jsonResponse.getLike().equalsIgnoreCase("true")) {

                        likee.setVisibility(View.GONE);
                        unlikelayout.setVisibility(View.VISIBLE);
                    } else {
                        unlikelayout.setVisibility(View.GONE);
                        likee.setVisibility(View.VISIBLE);
                    }

                    if (jsonResponse.getDislike().equalsIgnoreCase("true")) {
                        dislike.setVisibility(View.GONE);
                        undislikelayout.setVisibility(View.VISIBLE);
                    } else {
                        dislike.setVisibility(View.VISIBLE);
                        undislikelayout.setVisibility(View.GONE);
                    }

                    if (jsonResponse.getFavorite().equalsIgnoreCase("true")) {
                        favlistaddimg.setVisibility(View.VISIBLE);
                        favlistimg.setVisibility(View.GONE);

                    } else {
                        favlistaddimg.setVisibility(View.GONE);
                        favlistimg.setVisibility(View.VISIBLE);
                    }

                    language1.setText(jsonResponse.getLanguages());

                    if (movie_detaildata.get(0).getYear() == null || movie_detaildata.get(0).getYear().equalsIgnoreCase("0")) {
                        yearlayout.setVisibility(View.GONE);
                    } else {
                        yearlayout.setVisibility(View.VISIBLE);
                    }
                    year.setText(movie_detaildata.get(0).getYear());
                    viewsbottom.setText(movie_detaildata.get(0).getViews() + " Views");
                    videotitle1.setText(movie_detaildata.get(0).getTitle());
                    duration.setText(movie_detaildata.get(0).getDuration() + " secons");
                    ratings.setText(movie_detaildata.get(0).getRating());
                    videocategory.setText(jsonResponse.getMain_genre());

                    if (movie_detaildata.get(0).getRating().isEmpty() || movie_detaildata.get(0).getRating() == null) {
                        imdb_text.setVisibility(View.GONE);
                        seperate_line.setVisibility(View.GONE);
                    } else {
                        imdb.setText(movie_detaildata.get(0).getRating());
                        imdb_text.setVisibility(View.VISIBLE);
                    }
                    if (movie_detaildata.get(0).getYear().isEmpty() || movie_detaildata.get(0).getYear() == null || movie_detaildata.get(0).getYear().equalsIgnoreCase("0")) {
                        durationandyear.setText(movie_detaildata.get(0).getMovie_duration());
                    } else {
                        durationandyear.setText(movie_detaildata.get(0).getMovie_duration() + " " + movie_detaildata.get(0).getYear());
                    }
                    genre_text.setText(jsonResponse.getMain_genre());

              /*  textView.setText(movie_detaildata.get(0).getDescription());
                //textView.setShowingChar(200);
                //number of line you want to short
                textView.setShowingLine(2);

                textView.addShowMoreText("show More");
                textView.addShowLessText("show Less");

                textView.setShowMoreColor(Color.WHITE); // or other color
                textView.setShowLessTextColor(Color.WHITE);*/


//                    if (movie_detaildata.get(0).getMp4_url() == null) {
//                        urlll = movie_detaildata.get(0).getTrailer();
//                    } else {
//                        urlll = movie_detaildata.get(0).getMp4_url();
//                    }

                    //    urlll = movie_detaildata.get(0).getVideo_url() + movie_detaildata.get(0).getPath();
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

                    if (movie_detaildata != null && !movie_detaildata.isEmpty()) {
                        String trailerUrl = movie_detaildata.get(0).getTrailer();

                        if (trailerUrl == null || trailerUrl.isEmpty()) {
                            // Hide the trailer layout if no trailer is available
                            trailerLayout.setVisibility(View.GONE);
                        } else {
                            // Show the trailer layout if a trailer is available
                            trailerLayout.setVisibility(View.GONE);

                            trailerImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Start the TrailerPlayerActivity if the trailer URL is valid
                                    Intent intent = new Intent(getApplicationContext(), TrailerPlayerActivity.class);
                                    intent.putExtra("id", movie_detaildata.get(0).getId());
                                    intent.putExtra("url", trailerUrl);
                                    intent.putExtra("type", "video");
                                    startActivity(intent);
                                }
                            });
                        }
                    } else {
                        // If movie_detaildata is null or empty, hide the layout as a safety measure
                        trailerLayout.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "No movie details found", Toast.LENGTH_SHORT).show();
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
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("runmawi_test", t.getMessage());
                Log.e("LOG_VIDEO_DEBUG", "=== API Call Failed ===");
                Log.e("LOG_VIDEO_DEBUG", "Error Message: " + t.getMessage());
                Log.e("LOG_VIDEO_DEBUG", "Error Type: " + t.getClass().getSimpleName());
                Log.e("LOG_VIDEO_DEBUG", "Request URL: " + call.request().url().toString());
                
                if (t instanceof java.net.SocketTimeoutException) {
                    Log.e("LOG_VIDEO_DEBUG", "Timeout Error - Server took too long to respond");
                } else if (t instanceof java.net.ConnectException) {
                    Log.e("LOG_VIDEO_DEBUG", "Connection Error - Unable to connect to server");
                } else if (t instanceof java.net.UnknownHostException) {
                    Log.e("LOG_VIDEO_DEBUG", "DNS Error - Unable to resolve server hostname");
                }
                
                Toast.makeText(HomePageVideoActivity.this, "Network error. Please check your connection and try again.", Toast.LENGTH_LONG).show();
            }
        });

        playnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playnow.setClickable(false);
                
                if (movie_detaildata.get(0).getAccess().equalsIgnoreCase("ppv")) {
                    
                    // ✅ NEW: Android-only auto quality selection logic
                    QualityAutoSelector.QualityChoice autoChoice = QualityAutoSelector.getAutoQualityChoice(movie_detaildata.get(0));
                    
                    Log.w("LOG_Android_AutoQuality", "=== PlayNow Button Clicked ===");
                    Log.w("LOG_Android_AutoQuality", "autoChoice.isAutoSelected: " + autoChoice.isAutoSelected);
                    Log.w("LOG_Android_AutoQuality", "autoChoice.name: " + autoChoice.name);
                    Log.w("LOG_Android_AutoQuality", "autoChoice.price: " + autoChoice.price);
                    Log.w("LOG_Android_AutoQuality", "autoChoice.resolution: " + autoChoice.resolution);
                    
                    if (autoChoice.isAutoSelected) {
                        // 🎯 Auto-selected quality - skip selection UI, go directly to purchase
                        Log.w("LOG_Android_AutoQuality", "Auto-selected quality: " + autoChoice.name + " at price: " + autoChoice.price);
                        
                        quality_name = autoChoice.name;
                        quality_price = autoChoice.price;
                        quality_resolution = autoChoice.resolution;
                        
                        Log.w("LOG_Android_AutoQuality", "Set quality variables - name: " + quality_name + ", price: " + quality_price + ", resolution: " + quality_resolution);
                        
                        // Check if user already purchased this quality
                        Call<JSONResponse> qualityCheck = ApiClient.getInstance1().getApi().getMovieDetails1(user_id, videoId, quality_resolution);
                        qualityCheck.enqueue(new retrofit2.Callback<JSONResponse>() {
                            @Override
                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    JSONResponse qualityResponse = response.body();
                                    
                                    if (qualityResponse.getPpv_exist().equalsIgnoreCase("1")) {
                                        // Already purchased - play directly
                                        Log.w("LOG_Android_AutoQuality", "User already owns " + quality_resolution + " - playing directly");
                                        playVideoDirectly();
                                    } else {
                                        // Need to purchase - show simplified purchase dialog
                                        Log.w("LOG_Android_AutoQuality", "User doesn't own " + quality_resolution + " - showing purchase dialog");
                                        showSimplifiedPurchaseDialog();
                                    }
                                } else {
                                    // Error - show purchase dialog to be safe
                                    Log.w("LOG_Android_AutoQuality", "API error - showing purchase dialog as fallback");
                                    showSimplifiedPurchaseDialog();
                                }
                                playnow.setClickable(true);
                            }
                            
                            @Override
                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                // Error - show purchase dialog to be safe
                                Log.w("LOG_Android_AutoQuality", "API failure - showing purchase dialog as fallback");
                                showSimplifiedPurchaseDialog();
                                playnow.setClickable(true);
                            }
                        });
                        
                    } else {
                        // 🔄 Different prices - show traditional quality selection UI
                        Log.w("LOG_Android_AutoQuality", "Different prices detected - showing quality selection");
                        showTraditionalQualitySelection();
                        playnow.setClickable(true);
                    }
                    
                } else {
                    // Free content - play directly
                    playVideoDirectly();
                    playnow.setClickable(true);
                }
            }
        });

        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rent.setClickable(false);
                
                // ✅ NEW: Use QualityAutoSelector for rent button too
                QualityAutoSelector.QualityChoice autoChoice = QualityAutoSelector.getAutoQualityChoice(movie_detaildata.get(0));
                
                Log.w("LOG_Android_AutoQuality_Rent", "=== Rent Button Clicked ===");
                Log.w("LOG_Android_AutoQuality_Rent", "autoChoice.isAutoSelected: " + autoChoice.isAutoSelected);
                Log.w("LOG_Android_AutoQuality_Rent", "autoChoice.name: " + autoChoice.name);
                Log.w("LOG_Android_AutoQuality_Rent", "autoChoice.price: " + autoChoice.price);
                Log.w("LOG_Android_AutoQuality_Rent", "autoChoice.resolution: " + autoChoice.resolution);
                
                if (autoChoice.isAutoSelected) {
                    // 🎯 Auto-selected quality - skip selection UI, go directly to purchase
                    Log.w("LOG_Android_AutoQuality_Rent", "Auto-selected quality: " + autoChoice.name + " at price: " + autoChoice.price);
                    
                    quality_name = autoChoice.name;
                    quality_price = autoChoice.price;
                    quality_resolution = autoChoice.resolution;
                    
                    Log.w("LOG_Android_AutoQuality_Rent", "Set quality variables - name: " + quality_name + ", price: " + quality_price + ", resolution: " + quality_resolution);
                    
                    // For rent button, always show simplified purchase dialog (no ownership check needed)
                    showSimplifiedPurchaseDialog();
                    rent.setClickable(true);
                    
                } else {
                    // 🔄 Different prices - show traditional quality selection UI
                    Log.w("LOG_Android_AutoQuality_Rent", "Different prices detected - showing quality selection");
                    showTraditionalQualitySelection();
                    rent.setClickable(true);
                }
            }
        });


        /*playnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaplayer.pause();
                qualities1.clear();
                if (movie_detaildata.get(0).getType() == null || movie_detaildata.get(0).getType().isEmpty()) {//movie_detaildata.get(0).getType().equalsIgnoreCase("") || movie_detaildata.get(0).getType().isEmpty() ||
                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, videoId);
                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                            if (response.isSuccessful() && response.body() != null) {


                                JSONResponse jsonResponse = response.body();
                                movie_detaildata = safeArrayToList(jsonResponse.getVideodetail());

                                video_access = movie_detaildata.get(0).getAccess();
                                if ((video_access.equalsIgnoreCase("registered")) || (video_access.equalsIgnoreCase("guest"))) {
                                    {
                                        View view1 = getLayoutInflater().inflate(R.layout.quality_plan_page, null);
                                        BottomSheetDialog dialog1 = new BottomSheetDialog(HomePageVideoActivity.this);
                                        dialog1.setContentView(view1);

                                        RecyclerView plans_recyclerview = view1.findViewById(R.id.plans_recyclerview);
                                        CardView watch_free = view1.findViewById(R.id.watch_free);
                                        TextView text_name = view1.findViewById(R.id.text_name);
                                        //text_name.setText(videotitle1.getText().toString());

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


                                        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, videoId);
                                        res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                            @Override
                                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                JSONResponse jsonResponse = response.body();
                                                movie_detaildata = safeArrayToList(jsonResponse.getVideodetail());
                                                String s = "";
                                                String p = "";
                                                String r = "";

                                                for (int i = 0; i < 3; i++) {

                                                    if (i == 0) {
                                                        s = "Low Quality (480p)";
                                                        r = "480p";
                                                    } else if (i == 1) {
                                                        s = "Medium Quality (720p)";
                                                        r = "720p";
                                                    } else if (i == 2) {
                                                        s = "High Quality (1080p)";
                                                        r = "1080p";
                                                    }
                                                    quality_free qualityList = new quality_free(s, r);
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

                                                if (watch_free.getVisibility() == View.GONE) {
                                                    watch_free.setVisibility(View.VISIBLE);
                                                }


                                            }
                                        }));

                                        watch_free.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Log.w("Log_runmawi_test", "u: " + user_id + " v: " + videoId + " q: " + quality_resolution);
                                                Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails1(user_id, videoId, quality_resolution);
                                                res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                    @Override
                                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                        JSONResponse jsonResponse = response.body();
                                                        movie_detaildata1 = safeArrayToList(jsonResponse.getVideodetail());

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
                                    }
                                } else {

                                    Intent in = new Intent(getApplicationContext(), Videoplayer_cipher.class);
                                    in.putExtra("id", videoId);
                                    in.putExtra("otp", movie_detaildata.get(0).getOtp());
                                    in.putExtra("playback", movie_detaildata.get(0).getPlaybackInfo());
                                    startActivity(in);

                                }

                            }

                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                        }
                    });
                } else {
                    if (movie_detaildata.get(0).getType().equalsIgnoreCase("embed")) {
                        Intent intent1 = new Intent(getApplicationContext(), EncodedWebViewActivity.class);
                        intent1.putExtra("url", movie_detaildata.get(0).getVideos_url());
                        startActivity(intent1);
                    } else {
                        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, videoId);
                        res.enqueue(new retrofit2.Callback<JSONResponse>() {
                            @Override
                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                JSONResponse jsonResponse = response.body();
                                movie_detaildata = safeArrayToList(jsonResponse.getVideodetail());

                                View view1 = getLayoutInflater().inflate(R.layout.quality_plan_page, null);
                                BottomSheetDialog dialog1 = new BottomSheetDialog(HomePageVideoActivity.this);
                                dialog1.setContentView(view1);

                                RecyclerView plans_recyclerview = view1.findViewById(R.id.plans_recyclerview);
                                CardView watch_free = view1.findViewById(R.id.watch_free);
                                CardView pay = view1.findViewById(R.id.pay);
                                TextView text_name = view1.findViewById(R.id.text_name);
                                TextView pay_text = view1.findViewById(R.id.pay_text);

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

                                Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, videoId);
                                res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                    @Override
                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                        JSONResponse jsonResponse = response.body();
                                        movie_detaildata = safeArrayToList(jsonResponse.getVideodetail());
                                        String s = "";
                                        String p = "";
                                        String r = "";

                                        for (int i = 0; i < 3; i++) {

                                            if (i == 0) {
                                                s = "Low Quality (480p)";
                                                r = "480p";
                                                p = movie_detaildata.get(0).getPpv_price_480p();
                                            } else if (i == 1) {
                                                s = "Medium Quality (720p)";
                                                r = "720p";
                                                p = movie_detaildata.get(0).getPpv_price_720p();
                                            } else if (i == 2) {
                                                s = "High Quality (1080p)";
                                                r = "1080p";
                                                p = movie_detaildata.get(0).getPpv_price_1080p();
                                            }

                                            quality_free qualityList = new quality_free(s, p, r);
                                            qualities1.add(qualityList);
                                        }

                                        plans_recyclerview.setHasFixedSize(true);
                                        plans_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                        qualityAdapter = new QualityAdapter(qualities1, itemClickListener);
                                        plans_recyclerview.setAdapter(qualityAdapter);
                                        dialog1.show();
                                        playnow.setClickable(true);

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
                                        quality_price = qualities1.get(position).getPrice();
                                        pay_text.setText("Upgrade Now");

                                        if (movie_detaildata.get(0).getAccess().equalsIgnoreCase("ppv")) {
                                            if (quality_resolution.equalsIgnoreCase(movie_detaildata.get(0).getPPV_Plan())) {
                                                watch_free.setVisibility(View.VISIBLE);
                                                pay.setVisibility(View.GONE);
                                            } else {
                                                pay.setVisibility(View.VISIBLE);
                                                watch_free.setVisibility(View.GONE);
                                            }
                                        } else {
                                            watch_free.setVisibility(View.VISIBLE);
                                            pay.setVisibility(View.GONE);
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
                                watch_free.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog1.cancel();
                                        Log.w("Log_runmawi_test", "u: " + user_id + " v: " + videoId + " q: " + quality_resolution);
                                        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails1(user_id, videoId, quality_resolution);
                                        res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                            @Override
                                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                JSONResponse jsonResponse = response.body();
                                                movie_detaildata1 = safeArrayToList(jsonResponse.getVideodetail());

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
                            }

                            @Override
                            public void onFailure(Call<JSONResponse> call, Throwable throwable) {

                            }
                        });
                    }
                }

            }
        });*/

        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(),""+urlll,Toast.LENGTH_SHORT).show();

                if (movie_detaildata.get(0).getType() == null || movie_detaildata.get(0).getType().isEmpty()) {//movie_detaildata.get(0).getType().equalsIgnoreCase("") || movie_detaildata.get(0).getType().isEmpty() ||
                    mediaplayer.pause();
                    Intent in = new Intent(getApplicationContext(), Videoplayer_cipher.class);
                    in.putExtra("id", videoId);
                    in.putExtra("url", urlll);
                    in.putExtra("xtra", "");
                    in.putExtra("ads", "");
                    in.putExtra("continuee", "1");
                    startActivity(in);
                } else {
                    if (movie_detaildata.get(0).getType().equalsIgnoreCase("embed")) {
                        Intent intent1 = new Intent(getApplicationContext(), EncodedWebViewActivity.class);
                        intent1.putExtra("url", movie_detaildata.get(0).getVideos_url());
                        startActivity(intent1);
                    } else {
                        mediaplayer.pause();
                        Intent in = new Intent(getApplicationContext(), Videoplayer_cipher.class);
                        in.putExtra("id", videoId);
                        in.putExtra("url", urlll);
                        in.putExtra("xtra", "");
                        in.putExtra("ads", "");
                        in.putExtra("continuee", "1");
                        startActivity(in);
                    }
                }
            }
        });

        play_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (movie_detaildata.get(0).getType() == null || movie_detaildata.get(0).getType().isEmpty()) {//movie_detaildata.get(0).getType().equalsIgnoreCase("") || movie_detaildata.get(0).getType().isEmpty() ||
                    mediaplayer.pause();
                    Intent in = new Intent(getApplicationContext(), Videoplayer_cipher.class);
                    in.putExtra("id", videoId);
                    in.putExtra("url", urlll);
                    in.putExtra("suburl", suburl);
                    in.putExtra("data", dataa);
                    in.putExtra("ads", videoads);
                    startActivity(in);
                } else {
                    if (movie_detaildata.get(0).getType().equalsIgnoreCase("embed")) {
                        Intent intent1 = new Intent(getApplicationContext(), EncodedWebViewActivity.class);
                        intent1.putExtra("url", movie_detaildata.get(0).getVideos_url());
                        startActivity(intent1);
                    } else {
                        mediaplayer.pause();
                        Intent in = new Intent(getApplicationContext(), Videoplayer_cipher.class);
                        in.putExtra("id", videoId);
                        in.putExtra("url", urlll);
                        in.putExtra("suburl", suburl);
                        in.putExtra("data", dataa);
                        in.putExtra("ads", videoads);
                        startActivity(in);
                    }
                }
                //   Toast.makeText(getApplicationContext(),""+urlll,Toast.LENGTH_SHORT).show();
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


        paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Toast.makeText(getApplicationContext(),"Coming soon",Toast.LENGTH_LONG).show();

                if (user_id == null) {
                    Intent in = new Intent(getApplicationContext(), SigninActivity.class);
                    startActivity(in);
                } else {
                    Intent in = new Intent(getApplicationContext(), SubscribeActivity.class);
                    startActivity(in);

                }
            }
        });
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://runmawi.com/api/auth/").addConverterFactory(GsonConverterFactory.create()).build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponse> call = request.getStripeOnetime();
        call.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currency = response.body().getCurrency_Symbol();
                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
            }
        });


        /*rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, videoId);
                res.enqueue(new retrofit2.Callback<JSONResponse>() {
                    @Override
                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            JSONResponse jsonResponse = response.body();
                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));

                            if (movie_detaildata.get(0).getMp4_url() == null) {
                                renturl = movie_detaildata.get(0).getTrailer();
                            } else {
                                renturl = movie_detaildata.get(0).getMp4_url();
                            }
                            ppv_price = movie_detaildata.get(0).getPpv_price();
                            if (renturl == null) {
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                        Log.d("Error", t.getMessage());
                    }
                });


                if (false) {

                    mediaplayer.pause();
                    Intent in = new Intent(getApplicationContext(), Videoplayer_cipher.class);
                    in.putExtra("id", videoId);
                    in.putExtra("url", urlll);
                    in.putExtra("suburl", suburl);
                    in.putExtra("data", dataa);
                    in.putExtra("ads", videoads);
                    in.putExtra("xtra", rented);
                    startActivity(in);

                } else {

                    if (user_id == null) {
                        Intent in = new Intent(getApplicationContext(), SigninActivity.class);
                        startActivity(in);
                    } else {

                        dialog = new Dialog(HomePageVideoActivity.this);
                        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
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


                        Call<JSONResponse> callimgg = ApiClient.getInstance1().getApi().getPaymentDetails();
                        callimgg.enqueue(new retrofit2.Callback<JSONResponse>() {
                            @Override
                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    JSONResponse jsonResponse = response.body();


                                    wes.setHasFixedSize(true);
                                    wes.setLayoutManager(new GridLayoutManager(HomePageVideoActivity.this, 1));

                                    active_payment_settingsList = safeArrayToList(jsonResponse.getActive_payment_settings());
                                    active_payment_settingsAdopter = new Active_Payment_settingsAdopter(active_payment_settingsList);
                                    wes.setAdapter(active_payment_settingsAdopter);

                                }
                            }

                            @Override
                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                Log.d("Error1", t.getMessage());
                            }
                        });


                        wes.addOnItemTouchListener(
                                new RecyclerItemClickListener(HomePageVideoActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
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


                                progressDialog.setMessage("Please Wait");
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.setCancelable(false);
                                progressDialog.setMax(100);
                                progressDialog.show();

                                if (pays.getText().toString().equalsIgnoreCase("stripe")) {

                                    View view = getLayoutInflater().inflate(R.layout.activity_payperview_payment_page, null);
                                    BottomSheetDialog dialog1 = new BottomSheetDialog(HomePageVideoActivity.this);
                                    dialog1.setContentView(view);

                                    CardMultilineWidget cardMultilineWidget = view.findViewById(R.id.card_input_widget);
                                    Button paynow1 = (Button) view.findViewById(R.id.save_payment);
                                    ProgressBar payment_progress = (ProgressBar) view.findViewById(R.id.payment_progress);
                                    CheckBox check = (CheckBox) view.findViewById(R.id.check);
                                    TextView terms = (TextView) view.findViewById(R.id.terms);
                                    TextView movie_name = (TextView) view.findViewById(R.id.movie_name);
                                    TextView movie_price = (TextView) view.findViewById(R.id.movie_price);

                                    terms.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            try {
                                                Intent i = new Intent(Intent.ACTION_VIEW);
                                                i.setData(Uri.parse("https://runmawi.com/page/terms-and-conditions"));
                                                startActivity(i);
                                            } catch (Exception e) {
                                                Toast.makeText(getApplicationContext(), "unable to open website", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    movie_price.setText(ppv_price + " " + currency);
                                    movie_name.setText(videotitle1.getText().toString());
                                    Call<JSONResponse> call = ApiClient.getInstance1().getApi().getPaymentDetails();
                                    call.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                                            if (response.isSuccessful() && response.body() != null) {
                                                JSONResponse jsonResponse = response.body();
                                                payment_settingslist = safeArrayToList(jsonResponse.getPayment_settings());
                                                if (payment_settingslist.get(0).getLive_mode().equalsIgnoreCase(String.valueOf(0))) {
                                                    key = payment_settingslist.get(0).getTest_publishable_key();
                                                } else {
                                                    key = payment_settingslist.get(0).getLive_publishable_key();
                                                }
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
                                                Toast.makeText(getApplicationContext(), "Accept Terms of Use and Privacy Policy", Toast.LENGTH_LONG).show();
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

                                                                dialog1.hide();
                                                                addpayperview = addpayperview1;
                                                                if (addpayperview.getStatus().equalsIgnoreCase("true")) {
                                                                    Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();

                                                                    changeAccess();
                                                                } else if (addpayperview.getStatus().equalsIgnoreCase("false")) {
                                                                    Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                                                                    paynow1.setVisibility(View.VISIBLE);
                                                                    payment_progress.setVisibility(View.GONE);
                                                                }
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
                                    progressDialog.hide();
                                    dialog.dismiss();
                                } else if (pays.getText().toString().equalsIgnoreCase("Paydunya")) {

                                    progressDialog.hide();
                                    dialog.dismiss();
                                    View view = getLayoutInflater().inflate(R.layout.paydunya_payment_page, null);
                                    paydunya_dialog = new BottomSheetDialog(HomePageVideoActivity.this);
                                    paydunya_dialog.setContentView(view);
                                    paydunya_dialog.show();

                                    ImageView cross = view.findViewById(R.id.cross);
                                    TextView plan_name = view.findViewById(R.id.plan_name);
                                    TextView plan_price = view.findViewById(R.id.plan_price);
                                    paydunya_email_address = view.findViewById(R.id.email_address);
                                    paydunya_pay_now = view.findViewById(R.id.pay_now);
                                    cross.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            paydunya_dialog.hide();
                                        }
                                    });
                                    plan_name.setText(videotitle1.getText().toString());
                                    plan_price.setText(ppv_price + " " + currency);
                                    paydunya_pay_now.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (paydunya_email_address.getText().toString().isEmpty() || paydunya_email_address.getText().toString().trim().isEmpty()) {
                                                Toast.makeText(HomePageVideoActivity.this, "EMAIL ADDRESS not entered", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Call<JSONResponse> call = ApiClient.getInstance1().getApi().getPaymentDetails();
                                                call.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                    @Override
                                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                                                        if (response.isSuccessful() && response.body() != null) {
                                                            JSONResponse jsonResponse = response.body();
                                                            ArrayList<payment_settings> payment_settingslist = new ArrayList<>(Arrays.asList(jsonResponse.getPayment_settings()));

                                                            for (int i = 0; i < payment_settingslist.size(); i++) {
                                                                if (payment_settingslist.get(i).getPayment_type().equalsIgnoreCase("Paydunya")) {

                                                                    if (payment_settingslist.get(i).getLive_mode().equalsIgnoreCase(String.valueOf(0))) {
                                                                        setup = new PaydunyaSetup();
                                                                        setup.setMasterKey(payment_settingslist.get(i).getPaydunya_masterkey());
                                                                        setup.setPrivateKey(payment_settingslist.get(i).getPaydunya_test_PrivateKey());
                                                                        setup.setPublicKey(payment_settingslist.get(i).getPaydunya_test_PublicKey());
                                                                        setup.setToken(payment_settingslist.get(i).getPaydunya_test_token());
                                                                        setup.setMode("test");
                                                                        store = new PaydunyaCheckoutStore();
                                                                        store.setName("Runmawi");
                                                                        invoice = new PaydunyaCheckoutInvoice(setup, store);
                                                                        invoice.addItem(videotitle1.getText().toString(), 1, Double.parseDouble(ppv_price), Double.parseDouble(ppv_price));
                                                                        invoice.setTotalAmount(Double.parseDouble(ppv_price));

                                                                        new MyTask().execute(paydunya_email_address.getText().toString(), ppv_price);

                                                                    } else {
                                                                        setup = new PaydunyaSetup();
                                                                        setup.setMasterKey(payment_settingslist.get(i).getPaydunya_masterkey());
                                                                        setup.setPrivateKey(payment_settingslist.get(i).getPaydunya_live_PrivateKey());
                                                                        setup.setPublicKey(payment_settingslist.get(i).getPaydunya_live_PublicKey());
                                                                        setup.setToken(payment_settingslist.get(i).getPaydunya_live_token());
                                                                        setup.setMode("live");
                                                                        store = new PaydunyaCheckoutStore();
                                                                        store.setName("Runmawi");
                                                                        invoice = new PaydunyaCheckoutInvoice(setup, store);
                                                                        invoice.addItem(videotitle1.getText().toString(), 1, Double.parseDouble(ppv_price), Double.parseDouble(ppv_price));
                                                                        invoice.setTotalAmount(Double.parseDouble(ppv_price));

                                                                        new MyTask().execute(paydunya_email_address.getText().toString(), ppv_price);
                                                                    }
                                                                }
                                                            }

                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<JSONResponse> call, Throwable t) {

                                                    }
                                                });
                                            }
                                        }
                                    });


                                } else if (pays.getText().toString().equalsIgnoreCase("razorpay")) {

                                    progressDialog.hide();

                                    startPayment();
                                    dialog.dismiss();

                                } else if (pays.getText().toString().equalsIgnoreCase("paystack")) {

                                    progressDialog.hide();
                                    dialog.dismiss();
                                    Intent intent = new Intent(HomePageVideoActivity.this, CheckoutActivity.class);
                                    intent.putExtra("price", ppv_price);
                                    intent.putExtra("id", videoId);
                                    intent.putExtra("url", urlll);
                                    intent.putExtra("type", "video_rent");
                                    startActivity(intent);

                                } else if (pays.getText().toString().equalsIgnoreCase("CinetPay")) {

                                    progressDialog.hide();
                                    dialog.dismiss();

                                    Call<JSONResponse> pay_api = ApiClient.getInstance1().getApi().getPaymentDetails();
                                    pay_api.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response2) {
                                            if (response2.isSuccessful() && response2.body() != null) {
                                                JSONResponse jsonResponse2 = response2.body();
                                                ArrayList<payment_settings> payment_settingslist = new ArrayList<>(Arrays.asList(jsonResponse2.getPayment_settings()));

                                                for (int j = 0; j < payment_settingslist.size(); j++) {
                                                    if (payment_settingslist.get(j).getPayment_type().equalsIgnoreCase("CinetPay")) {

                                                        //Toast.makeText(HomePageVideoActivity.this, "videoPage-> "+videoId+urlll, Toast.LENGTH_SHORT).show();
                                                        Intent in = new Intent(HomePageVideoActivity.this, MyCinetPayActivity.class);
                                                        //in.putExtra("amount", ppv_price);
                                                        in.putExtra("id", videoId);
                                                        in.putExtra("url", urlll);
                                                        in.putExtra("type", "video_rent");
                                                        in.putExtra(CinetPayActivity.KEY_API_KEY, payment_settingslist.get(j).getCinetPay_APIKEY());
                                                        in.putExtra(CinetPayActivity.KEY_SITE_ID, payment_settingslist.get(j).getCinetPay_SITE_ID());
                                                        in.putExtra(CinetPayActivity.KEY_TRANSACTION_ID, String.valueOf(new Date().getTime()));
                                                        in.putExtra(CinetPayActivity.KEY_AMOUNT, ppv_price);
                                                        in.putExtra(CinetPayActivity.KEY_CURRENCY, currency);
                                                        in.putExtra(CinetPayActivity.KEY_DESCRIPTION, "Video Purchase");
                                                        in.putExtra(CinetPayActivity.KEY_CHANNELS, "MOBILE_MONEY,WALLET,CREDIT_CARD");
                                                        startActivity(in);

                                                        //MyCinetPayActivity myCinetPayActivity=new MyCinetPayActivity(payment_settingslist.get(j).getCinetPay_APIKEY(),payment_settingslist.get(j).getCinetPay_SITE_ID(),String.valueOf(new Date().getTime()),"XOF","Video Purchase","MOBILE_MONEY,WALLET,CREDIT_CARD",ppv_price,"","video_rent","","","",videoId,urlll,"",HomePageVideoActivity.this);

                                                        //MyCinetPayActivity myCinetPayActivity=new MyCinetPayActivity(HomePageVideoActivity.this)
                                                        //myCinetPayActivity.receiveDta(videoId,urlll,"video_rent",ppv_price,payment_settingslist.get(j).getCinetPay_APIKEY(),payment_settingslist.get(j).getCinetPay_SITE_ID(),String.valueOf(new Date().getTime()),"XOF","Video Purchase","MOBILE_MONEY,WALLET,CREDIT_CARD","","","","","");

                                                    }
                                                }

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<JSONResponse> call, Throwable t) {

                                        }
                                    });
                                } else if (pays.getText().toString().equalsIgnoreCase("payPal")) {

                                    progressDialog.hide();
                                    dialog.dismiss();
                                    getPayment();
                                }

                            }

                        });


                    }
                }
            }
        });*/


        thismaylikeadopter = new RecommandeHomeAdapter(dataList, this);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        thismaylikerecycler = (RecyclerView) findViewById(R.id.thismayalsolike);

        commentsAdopter = new CommentsAdopter(usercommentslist, this);
        layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        thismaylikerecycler.setHasFixedSize(true);
        thismaylikerecycler.setLayoutManager(layoutManager);
        thismaylikerecycler.setAdapter(thismaylikeadopter);

        castandcrewadapter = new castandcrewadapter(castdetails, this);
        manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        castandcrewrecycler = (RecyclerView) findViewById(R.id.castandcrew);

        castandcrewrecycler.setHasFixedSize(true);
        castandcrewrecycler.setLayoutManager(manager);
        castandcrewrecycler.setAdapter(castandcrewadapter);

        // Debug: First check if video exists in database at all

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


/*
        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (commentlayout.getVisibility() == View.VISIBLE) {

                    commentlayout.setVisibility(View.GONE);
                } else {

                    commentlayout.setVisibility(View.VISIBLE);


                }

            }
        });
*/

        Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getUsercommentslive(videoId, "play_videos ");
        movieres.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JSONResponse jsonResponse = response.body();

                    if (jsonResponse.getComment() != null && jsonResponse.getComment().length != 0) {
                        usercommentslist = safeArrayToList(jsonResponse.getComment());
                        commentsAdopter = new CommentsAdopter(usercommentslist);
                        usercommentrecycler.setAdapter(commentsAdopter);
                    }

                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });


        usercommentrecycler.addOnItemTouchListener(new RecyclerItemClickListener(HomePageVideoActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

                if (usercommentslist.size() > position) {
                    if (usercommentslist.get(position) != null) {


                        if (!usercommentslist.get(position).getUser_id().equalsIgnoreCase(user_id)) {
                            Toast.makeText(getApplicationContext(), "Only your comments will be editable", Toast.LENGTH_LONG).show();

                        } else {


                            View view1 = getLayoutInflater().inflate(R.layout.comments_edit, null);
                            BottomSheetDialog dialog1 = new BottomSheetDialog(HomePageVideoActivity.this);
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
                                    edit1.setText(usercommentslist.get(position).getComment());

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


                                                Api.getClient().getupdateLivecomment(user_id, videoId, comment, "play_videos", usercommentslist.get(position).getId(), new Callback<AddComment>() {

                                                    @Override
                                                    public void success(AddComment addComment, Response response) {

                                                        addComment = addComment;

                                                        if (addComment.getStatus().equalsIgnoreCase("true")) {

                                                            Toast.makeText(getApplicationContext(), "" + addComment.getMessage(), Toast.LENGTH_LONG).show();

                                                            dialog1.cancel();
                                                            Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getUsercommentslive(videoId, "play_videos");
                                                            movieres.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                                @Override
                                                                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                                                                    if (response.isSuccessful() && response.body() != null) {
                                                                        JSONResponse jsonResponse = response.body();

                                                                        pro.setVisibility(View.GONE);
                                                                        img.setVisibility(View.VISIBLE);
                                                                        edit1.setText("");

                                                                        usercommentslist = new ArrayList<>(Arrays.asList(jsonResponse.getComment()));
                                                                        commentsAdopter = new CommentsAdopter(usercommentslist);
                                                                        usercommentrecycler.setAdapter(commentsAdopter);


                                                                    }
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


                                            Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getUsercommentslive(videoId, "play_videos");
                                            movieres.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                @Override
                                                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                                                    if (response.isSuccessful() && response.body() != null) {
                                                        JSONResponse jsonResponse = response.body();


                                                        usercommentslist = new ArrayList<>(Arrays.asList(jsonResponse.getComment()));
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

                                        @Override
                                        public void failure(RetrofitError error) {

                                        }
                                    });
                                }
                            });


                        }

                    }
                }
            }
        }));
        commenttext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // Clear the focus from the EditText
                view.clearFocus();
                // Hide the keyboard
                hideKeyboard(view);
                return false;
            }

            private void hideKeyboard(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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


                    Api.getClient().getAddLivecomment(user_id, videoId, comment, "play_videos ", new Callback<AddComment>() {

                        @Override
                        public void success(AddComment addComment, Response response) {

                            addComment = addComment;

                            if (addComment.getStatus().equalsIgnoreCase("true")) {

                                Toast.makeText(getApplicationContext(), "" + addComment.getMessage(), Toast.LENGTH_LONG).show();

                                Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getUsercommentslive(videoId, "play_videos");
                                movieres.enqueue(new retrofit2.Callback<JSONResponse>() {
                                    @Override
                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            JSONResponse jsonResponse = response.body();

                                            commentprogress.setVisibility(View.GONE);
                                            commentsend.setVisibility(View.VISIBLE);
                                            commenttext.setText("");

                                            usercommentslist = new ArrayList<>(Arrays.asList(jsonResponse.getComment()));
                                            commentsAdopter = new CommentsAdopter(usercommentslist);
                                            usercommentrecycler.setAdapter(commentsAdopter);


                                        }
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
        share1.setOnClickListener(new View.OnClickListener() {
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

                        Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
        watchlater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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


        });
        watchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        });
        likee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Api.getClient().getLikecount(user_id, videoId, "1", new Callback<likeandunlike>() {

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

                Api.getClient().getLikecount(user_id, videoId, "0", new Callback<likeandunlike>() {

                    @Override
                    public void success(likeandunlike addwishmovie, Response response) {

                        addwishmovie = addwishmovie;
                        if (addwishmovie.getLiked().equalsIgnoreCase("0")) {

                            likee.setVisibility(View.VISIBLE);
                            unlikelayout.setVisibility(View.GONE);

                            Toast.makeText(getApplicationContext(), "Your liked removed", Toast.LENGTH_LONG).show();
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

                Api.getClient().getDislikecount(user_id, videoId, "1", new Callback<likeandunlike>() {

                    @Override
                    public void success(likeandunlike addwishmovie, Response response) {

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


                Api.getClient().getDislikecount(user_id, videoId, "0", new Callback<likeandunlike>() {

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


        });



        Call<HomeBodyResponse> responseCall = RetrofitSingleton.getInstance().getApi().getRecommaned(videoId);
        responseCall.enqueue(new retrofit2.Callback<HomeBodyResponse>() {
            @Override
            public void onResponse(Call<HomeBodyResponse> call, retrofit2.Response<HomeBodyResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    thismaylikerecycler.setVisibility(View.VISIBLE);

                    if (response.body().equals(null)) {
                        thismayalsolike_layout.setVisibility(View.GONE);
                    }

                    if (response.isSuccessful()) {
                        for (RecommandedHomeData data : response.body().getChannelrecomended()) {

                            dataList.add(data);
                        }
                        thismaylikeadopter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onFailure(Call<HomeBodyResponse> call, Throwable t) {

                //  progressBar.setVisibility(View.GONE);

            }
        });


        Call<JSONResponse> videocast = ApiClient.getInstance1().getApi().getVideoCast(videoId);
        videocast.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    JSONResponse jsonResponse = response.body();


                    if (jsonResponse.getCast_details() != null && jsonResponse.getCast_details().length != 0) {


                        castandcrewrecycler.setVisibility(View.VISIBLE);
                        no_cast.setVisibility(View.GONE);
                        castdetails = safeArrayToList(jsonResponse.getCast_details());
                        castandcrewadapter = new castandcrewadapter(castdetails);
                        castandcrewrecycler.setAdapter(castandcrewadapter);


                    } else {

                        castandcrewrecycler.setVisibility(View.GONE);
                        no_cast.setVisibility(View.VISIBLE);
                        castlayout.setVisibility(View.GONE);
                        //    cast_name.setText(jsonResponse.getCast());

                    }

                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());

            }
        });
    }


    private void showSimplifiedPurchaseDialog() {
        // ✅ DEBUG: Log the values being used
        Log.w("LOG_SimplifiedDialog", "=== Simplified Purchase Dialog ===");
        Log.w("LOG_SimplifiedDialog", "quality_name: " + (quality_name != null ? quality_name : "NULL"));
        Log.w("LOG_SimplifiedDialog", "quality_price: " + (quality_price != null ? quality_price : "NULL"));
        Log.w("LOG_SimplifiedDialog", "quality_resolution: " + (quality_resolution != null ? quality_resolution : "NULL"));
        
        View view1 = getLayoutInflater().inflate(R.layout.simplified_purchase_dialog, null);
        BottomSheetDialog dialog1 = new BottomSheetDialog(HomePageVideoActivity.this);
        dialog1.setContentView(view1);
        
        TextView qualityText = view1.findViewById(R.id.quality_text);
        TextView priceText = view1.findViewById(R.id.price_text);
        TextView currencyText = view1.findViewById(R.id.currency_text);
        CardView purchaseButton = view1.findViewById(R.id.purchase_button);
        
        // ✅ NULL CHECKS: Set with fallback values
        if (quality_name != null && !quality_name.isEmpty()) {
            qualityText.setText(quality_name);
            Log.w("LOG_SimplifiedDialog", "Set quality_name: " + quality_name);
        } else {
            qualityText.setText("High Quality (1080p)"); // Fallback
            Log.w("LOG_SimplifiedDialog", "Used fallback quality name");
        }
        
        if (quality_price != null && !quality_price.isEmpty()) {
            priceText.setText(quality_price);
            Log.w("LOG_SimplifiedDialog", "Set quality_price: " + quality_price);
        } else {
            priceText.setText("0"); // Fallback
            Log.w("LOG_SimplifiedDialog", "Used fallback price: 0");
        }
        
        // Get currency symbol
        Call<JSONResponse> currencyCall = ApiClient.getInstance1().getApi().getStripeOnetime();
        currencyCall.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                if (response.body() != null && response.body().getCurrency_Symbol() != null) {
                    currencyText.setText(response.body().getCurrency_Symbol());
                    Log.w("LOG_SimplifiedDialog", "Set currency: " + response.body().getCurrency_Symbol());
                } else {
                    currencyText.setText("₹"); // Fallback
                    Log.w("LOG_SimplifiedDialog", "Used fallback currency: ₹");
                }
            }
            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                currencyText.setText("₹"); // Fallback on failure
                Log.w("LOG_SimplifiedDialog", "Currency API failed, using fallback: ₹");
            }
        });
        
        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                createOrder(); // Existing payment flow
            }
        });
        
        dialog1.show();
        Log.w("LOG_SimplifiedDialog", "Dialog shown successfully");
    }

    private void showTraditionalQualitySelection() {
        if (movie_detaildata == null || movie_detaildata.isEmpty()) {
            Log.e("HomePageVideoActivity", "showTraditionalQualitySelection: movie_detaildata is null or empty.");
            Toast.makeText(this, "Video details not available.", Toast.LENGTH_SHORT).show();
            if (playnow != null) playnow.setClickable(true); // Re-enable the main play button
            return;
        }
    
        final videodetail currentVideoDetails = movie_detaildata.get(0);
        final String videoAccessType = currentVideoDetails.getAccess();
    
        // ✅ ENHANCED DEBUG: Log all PPV-related fields from currentVideoDetails
        Log.w("LOG_TraditionalQuality", "=== Enhanced Video Details Debug ===");
        Log.w("LOG_TraditionalQuality", "Video ID: " + currentVideoDetails.getId());
        Log.w("LOG_TraditionalQuality", "Video Title: " + currentVideoDetails.getTitle());
        Log.w("LOG_TraditionalQuality", "Video Access Type: " + videoAccessType);
        Log.w("LOG_TraditionalQuality", "User's PPV_Plan: '" + currentVideoDetails.getPPV_Plan() + "'");
        Log.w("LOG_TraditionalQuality", "PPV_Plan is null: " + (currentVideoDetails.getPPV_Plan() == null));
        Log.w("LOG_TraditionalQuality", "PPV_Plan is empty: " + (currentVideoDetails.getPPV_Plan() != null && currentVideoDetails.getPPV_Plan().isEmpty()));
        Log.w("LOG_TraditionalQuality", "480p Price: " + currentVideoDetails.getPpv_price_480p());
        Log.w("LOG_TraditionalQuality", "720p Price: " + currentVideoDetails.getPpv_price_720p());
        Log.w("LOG_TraditionalQuality", "1080p Price: " + currentVideoDetails.getPpv_price_1080p());
        
        // Initialize or clear the list for quality options
        if (qualities1 == null) {
            qualities1 = new ArrayList<>();
        } else {
            qualities1.clear();
        }
    
        View view1 = getLayoutInflater().inflate(R.layout.quality_plan_page, null);
        final BottomSheetDialog dialog1 = new BottomSheetDialog(HomePageVideoActivity.this);
        dialog1.setContentView(view1);
    
        final RecyclerView plans_recyclerview = view1.findViewById(R.id.plans_recyclerview);
        final CardView payButton = view1.findViewById(R.id.pay);
        final TextView payButtonText = view1.findViewById(R.id.pay_text); // Text inside payButton
        final CardView watchFreeButton = view1.findViewById(R.id.watch_free);
        TextView dialogTitle = view1.findViewById(R.id.text_name);
    
        if (dialogTitle != null && currentVideoDetails.getTitle() != null) {
            dialogTitle.setText(currentVideoDetails.getTitle());
        }
    
        // Initially hide buttons
        payButton.setVisibility(View.GONE);
        watchFreeButton.setVisibility(View.GONE);
    
        // Populate qualities1 list (using quality_free objects)
        String name480p = "Low Quality (480p)";
        String res480p = "480p";
        String price480p = currentVideoDetails.getPpv_price_480p();
        if (price480p != null) { // Add if price info exists (even if "0" or empty)
            qualities1.add(new quality_free(name480p, price480p, res480p));
        }
    
        String name720p = "Medium Quality (720p)";
        String res720p = "720p";
        String price720p = currentVideoDetails.getPpv_price_720p();
        if (price720p != null) {
            qualities1.add(new quality_free(name720p, price720p, res720p));
        }
    
        String name1080p = "High Quality (1080p)";
        String res1080p = "1080p";
        String price1080p = currentVideoDetails.getPpv_price_1080p();
        if (price1080p != null) {
            qualities1.add(new quality_free(name1080p, price1080p, res1080p));
        }
        
        if (qualities1.isEmpty()) {
            Log.e("HomePageVideoActivity", "No qualities available in showTraditionalQualitySelection.");
            Toast.makeText(this, "No video quality options found.", Toast.LENGTH_SHORT).show();
            dialog1.dismiss(); // Dismiss if there's nothing to show
            if (playnow != null) playnow.setClickable(true);
            return;
        }
    
        ItemClickListener itemClickListener = s -> {
            if (plans_recyclerview.getAdapter() != null) {
                plans_recyclerview.post(() -> plans_recyclerview.getAdapter().notifyDataSetChanged());
            }
        };
    
        qualityAdapter = new QualityAdapter(qualities1, itemClickListener); // Assuming QualityAdapter takes ArrayList<quality_free>
        plans_recyclerview.setHasFixedSize(true);
        plans_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        plans_recyclerview.setAdapter(qualityAdapter);
    
        // ✅ NEW: Auto-select the purchased quality by default
        int purchasedQualityPosition = findPurchasedQualityPosition(currentVideoDetails, qualities1);
        Log.w("LOG_TraditionalQuality", "=== Auto-Selection ===");
        Log.w("LOG_TraditionalQuality", "Purchased quality position: " + purchasedQualityPosition);
        
        if (purchasedQualityPosition != -1) {
            // Pre-select the purchased quality
            quality_free purchasedQuality = qualities1.get(purchasedQualityPosition);
            quality_name = purchasedQuality.getQuality();
            quality_resolution = purchasedQuality.getResolution();
            quality_price = purchasedQuality.getPrice();
            
            Log.w("LOG_TraditionalQuality", "Auto-selected purchased quality: " + quality_name);
            
            // Show the appropriate button for the owned quality
            watchFreeButton.setVisibility(View.VISIBLE);
            payButton.setVisibility(View.GONE);
            
            // Notify adapter about selection and scroll to position
            plans_recyclerview.post(() -> {
                // Scroll to the purchased quality position and refresh the adapter
                qualityAdapter.notifyDataSetChanged();
                plans_recyclerview.scrollToPosition(purchasedQualityPosition);
            });
        }
    
        plans_recyclerview.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                if (position >= qualities1.size()) return;
    
                quality_free selectedQuality = qualities1.get(position);
                quality_name = selectedQuality.getQuality();
                quality_resolution = selectedQuality.getResolution();
                quality_price = selectedQuality.getPrice(); 
    
                boolean selectedPriceIsEffectivelyZero = (quality_price == null || quality_price.isEmpty() || quality_price.equals("0"));
    
                // ✅ COMPREHENSIVE OWNERSHIP DEBUG
                Log.w("LOG_TraditionalQuality", "=== Quality Item Clicked - COMPREHENSIVE DEBUG ===");
                Log.w("LOG_TraditionalQuality", "Selected quality_resolution: '" + quality_resolution + "'");
                Log.w("LOG_TraditionalQuality", "Selected quality_name: '" + quality_name + "'");
                Log.w("LOG_TraditionalQuality", "Selected quality_price: '" + quality_price + "'");
                Log.w("LOG_TraditionalQuality", "Video access type: '" + videoAccessType + "'");
                Log.w("LOG_TraditionalQuality", "User's owned PPV_Plan: '" + (currentVideoDetails.getPPV_Plan() != null ? currentVideoDetails.getPPV_Plan() : "NULL") + "'");
                Log.w("LOG_TraditionalQuality", "Price is effectively zero: " + selectedPriceIsEffectivelyZero);
                
                // ✅ DETAILED OWNERSHIP COMPARISON DEBUG
                String userOwnedPlan = currentVideoDetails.getPPV_Plan();
                Log.w("LOG_TraditionalQuality", "=== Ownership Comparison Details ===");
                Log.w("LOG_TraditionalQuality", "quality_resolution: '" + quality_resolution + "' (length: " + (quality_resolution != null ? quality_resolution.length() : "null") + ")");
                Log.w("LOG_TraditionalQuality", "userOwnedPlan: '" + userOwnedPlan + "' (length: " + (userOwnedPlan != null ? userOwnedPlan.length() : "null") + ")");
                
                if (quality_resolution != null && userOwnedPlan != null) {
                    Log.w("LOG_TraditionalQuality", "Case-sensitive equals: " + quality_resolution.equals(userOwnedPlan));
                    Log.w("LOG_TraditionalQuality", "Case-insensitive equals: " + quality_resolution.equalsIgnoreCase(userOwnedPlan));
                    Log.w("LOG_TraditionalQuality", "Trimmed case-insensitive equals: " + quality_resolution.trim().equalsIgnoreCase(userOwnedPlan.trim()));
                    
                    // Check for any hidden characters
                    Log.w("LOG_TraditionalQuality", "quality_resolution bytes: " + java.util.Arrays.toString(quality_resolution.getBytes()));
                    Log.w("LOG_TraditionalQuality", "userOwnedPlan bytes: " + java.util.Arrays.toString(userOwnedPlan.getBytes()));
                } else {
                    Log.w("LOG_TraditionalQuality", "One or both values are null - cannot compare");
                }
    
                if (videoAccessType.equalsIgnoreCase("ppv")) {
                    // ✅ ENHANCED OWNERSHIP CHECK with multiple fallback attempts
                    boolean isOwned = false;
                    String ownershipReason = "";
                    
                    if (userOwnedPlan != null && quality_resolution != null) {
                        // ✅ NEW: Extract resolution from full quality name
                        // The API returns "Medium Quality (720p)" but we need to match against "720p"
                        String extractedResolution = extractResolutionFromQualityName(userOwnedPlan);
                        
                        Log.w("LOG_TraditionalQuality", "=== Resolution Extraction ===");
                        Log.w("LOG_TraditionalQuality", "Original userOwnedPlan: '" + userOwnedPlan + "'");
                        Log.w("LOG_TraditionalQuality", "Extracted resolution: '" + extractedResolution + "'");
                        
                        // Try multiple comparison methods
                        if (quality_resolution.equalsIgnoreCase(extractedResolution)) {
                            isOwned = true;
                            ownershipReason = "Extracted resolution match: " + quality_resolution + " == " + extractedResolution;
                        } else if (quality_resolution.equalsIgnoreCase(userOwnedPlan)) {
                            isOwned = true;
                            ownershipReason = "Direct case-insensitive match";
                        } else if (quality_resolution.trim().equalsIgnoreCase(userOwnedPlan.trim())) {
                            isOwned = true;
                            ownershipReason = "Trimmed case-insensitive match";
                        } else {
                            // Try removing common suffixes/prefixes
                            String cleanResolution = quality_resolution.toLowerCase().replace("p", "");
                            String cleanOwned = userOwnedPlan.toLowerCase().replace("p", "");
                            if (cleanResolution.equals(cleanOwned)) {
                                isOwned = true;
                                ownershipReason = "Clean comparison match (removed 'p')";
                            }
                        }
                    }
                    
                    Log.w("LOG_TraditionalQuality", "=== Ownership Decision ===");
                    Log.w("LOG_TraditionalQuality", "Is quality owned: " + isOwned);
                    Log.w("LOG_TraditionalQuality", "Ownership reason: " + ownershipReason);
                    
                    if (isOwned) {
                        Log.w("LOG_TraditionalQuality", "✅ OWNED: User owns this quality - showing Watch Free button");
                        watchFreeButton.setVisibility(View.VISIBLE);
                        payButton.setVisibility(View.GONE);
                    } 
                    // If not owned, but the selected quality tier itself is free (price is "0")
                    else if (selectedPriceIsEffectivelyZero) {
                        Log.w("LOG_TraditionalQuality", "✅ FREE: Quality is free - showing Watch Free button");
                        watchFreeButton.setVisibility(View.VISIBLE);
                        payButton.setVisibility(View.GONE);
                    } 
                    // Otherwise, it's a PPV quality that needs to be purchased/upgraded
                    else {
                        Log.w("LOG_TraditionalQuality", "❌ PURCHASE: User doesn't own this quality - showing Pay button");
                        watchFreeButton.setVisibility(View.GONE);
                        payButton.setVisibility(View.VISIBLE);
                        if (payButtonText != null) payButtonText.setText("Upgrade Now"); // Or "Pay"
                    }
                } else {
                    // For non-PPV access types (e.g., "registered", "guest") that show quality selection
                    // it's always free to watch once selected.
                    Log.w("LOG_TraditionalQuality", "✅ NON-PPV: Always free for " + videoAccessType);
                    watchFreeButton.setVisibility(View.VISIBLE);
                    payButton.setVisibility(View.GONE);
                }
            }
        }));
    
        payButton.setOnClickListener(v -> {
            dialog1.dismiss();
            if (quality_price == null || quality_resolution == null) {
                Toast.makeText(HomePageVideoActivity.this, "Please select a quality to purchase.", Toast.LENGTH_SHORT).show();
                return;
            }
            // Ensure quality_name is also set if createOrder() depends on it.
            // quality_name is set in the item click listener.
            createOrder(); // Uses member variables quality_price, quality_name, quality_resolution
        });
    
        watchFreeButton.setOnClickListener(v -> {
            dialog1.dismiss();
            if (quality_resolution == null) {
                Toast.makeText(HomePageVideoActivity.this, "Please select a quality to watch.", Toast.LENGTH_SHORT).show();
                return;
            }
            playVideoDirectly(); // Uses member variable quality_resolution
        });
    
        dialog1.setOnDismissListener(dialogInterface -> {
            if (playnow != null) playnow.setClickable(true); // Re-enable the main "Play Now" button
        });
    
        dialog1.show();
        // Consider disabling playnow button while dialog is shown:
        // if (playnow != null) playnow.setClickable(false); 
    }

    private void playVideoDirectly() {
        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails1(user_id, videoId, quality_resolution);
        res.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                JSONResponse jsonResponse = response.body();
                movie_detaildata1 = safeArrayToList(jsonResponse.getVideodetail());
                
                Intent in = new Intent(getApplicationContext(), Videoplayer_cipher.class);
                in.putExtra("id", videoId);
                in.putExtra("otp", movie_detaildata1.get(0).getOtp());
                in.putExtra("playback", movie_detaildata1.get(0).getPlaybackInfo());
                startActivity(in);
            }
            
            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {}
        });
    }

    /**
     * Extracts resolution (e.g., "720p") from full quality names like "Medium Quality (720p)"
     * @param qualityName The full quality name from API
     * @return The extracted resolution or the original string if no pattern found
     */
    private String extractResolutionFromQualityName(String qualityName) {
        if (qualityName == null || qualityName.isEmpty()) {
            return qualityName;
        }
        
        // Pattern to match resolution in parentheses: "Medium Quality (720p)" -> "720p"
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\((\\d+p)\\)");
        java.util.regex.Matcher matcher = pattern.matcher(qualityName);
        
        if (matcher.find()) {
            String extracted = matcher.group(1); // Get the first capturing group (720p)
            Log.w("LOG_TraditionalQuality", "Pattern match found: " + extracted);
            return extracted;
        }
        
        // Fallback: Look for common resolution patterns without parentheses
        java.util.regex.Pattern fallbackPattern = java.util.regex.Pattern.compile("(\\d+p)");
        java.util.regex.Matcher fallbackMatcher = fallbackPattern.matcher(qualityName);
        
        if (fallbackMatcher.find()) {
            String extracted = fallbackMatcher.group(1);
            Log.w("LOG_TraditionalQuality", "Fallback pattern match found: " + extracted);
            return extracted;
        }
        
        Log.w("LOG_TraditionalQuality", "No resolution pattern found, returning original: " + qualityName);
        return qualityName; // Return original if no pattern found
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

    public void getPayment() {
        Call<JSONResponse> callimg = ApiClient.getInstance1().getApi().currencyConverter("United States of America", ppv_price);
        callimg.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JSONResponse jsonResponse = response.body();
                    if (!(jsonResponse.getCurrency_Converted().isEmpty()) || jsonResponse.getCurrency_Converted() != null) {

                        String[] curencySplit = jsonResponse.getCurrency_Converted().split(" ");
                        String[] remCurr = curencySplit[1].split("");
                        String totalAmount = remCurr[0] + remCurr[1] + remCurr[2] + remCurr[3];
                        Double addTax = Double.parseDouble(totalAmount) + 0.2;
                        paypalamount = String.valueOf(addTax);
                        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paypalamount)), "USD", "Video on rent", PayPalPayment.PAYMENT_INTENT_SALE);
                        Intent intent = new Intent(HomePageVideoActivity.this, PaymentActivity.class);
                        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
                    }
                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error: ", t.getMessage());
            }
        });
    }

    @Override
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
                    Api.getClient().getAddPayperView1(user_id, idd.getText().toString(), payID, "PayPal", ppv_price, "Android", new Callback<Addpayperview>() {
                        @Override
                        public void success(Addpayperview addpayperview1, Response response) {

                            addpayperview = addpayperview1;
                            if (addpayperview.getStatus().equalsIgnoreCase("true")) {
                                Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();

                                /*Intent in = new Intent(getApplicationContext(), Videoplayer_cipher.class);
                                in.putExtra("id", idd.getText().toString());
                                in.putExtra("url", urlll);
                                in.putExtra("suburl", suburl);
                                in.putExtra("data", dataa);
                                in.putExtra("ads", videoads);
                                in.putExtra("xtra", rented);
                                startActivity(in);*/
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
                    Toast.makeText(HomePageVideoActivity.this, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == Activity.RESULT_CANCELED) {
                Toast.makeText(HomePageVideoActivity.this, "Payment Canceled", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(HomePageVideoActivity.this, "Invalid Payment", Toast.LENGTH_LONG).show();
        } else if (requestCode == Activity.RESULT_CANCELED) {
            Toast.makeText(HomePageVideoActivity.this, "Payment Canceled", Toast.LENGTH_LONG).show();
        }
    }

    private void startPaymentWithQuality(String id) {
        Log.w("LOG_Runmawi_Payment", "startPaymentWithQuality called with orderId: " + id);
        final Activity activity = this;
        final Checkout co = new Checkout();

        try {
            // Validate critical parameters
            if (id == null || id.isEmpty()) {
                Log.e("LOG_Runmawi_Payment", "startPaymentWithQuality - Order ID is null or empty!");
                Toast.makeText(this, "Payment setup error: Invalid order ID", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (quality_price == null || quality_price.isEmpty()) {
                Log.e("LOG_Runmawi_Payment", "startPaymentWithQuality - Quality price is null or empty!");
                Toast.makeText(this, "Payment setup error: Invalid price", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (RAZORPAY_KEY_ID == null || RAZORPAY_KEY_ID.isEmpty()) {
                Log.e("LOG_Runmawi_Payment", "startPaymentWithQuality - Razorpay Key ID is null or empty!");
                Toast.makeText(this, "Payment setup error: Invalid Razorpay configuration", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject options = new JSONObject();
            options.put("name", "Runmawi");
            options.put("description", id);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            options.put("order_id", id);
            String payment = quality_price;
            Log.w("LOG_Runmawi_Payment", "startPaymentWithQuality - quality_price: " + payment);

            //amount is in paise so please multiple it by 100
            //Payment failed Invalid amount (should be passed in integer paise. Minimum value is 100 paise, i.e. ₹ 1)
            double total = Double.parseDouble(payment);
            Log.w("LOG_runmawii", "p: " + payment + " d: " + total + " t: " + (total * 100));
            total = total * 100;
            
            // Validate minimum amount (100 paise = ₹1)
            if (total < 100) {
                Log.e("LOG_Runmawi_Payment", "startPaymentWithQuality - Amount too low: " + total + " paise. Minimum is 100 paise (₹1)");
                Toast.makeText(this, "Payment error: Amount is too low (minimum ₹1)", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // p: 100 d: 100.0 t: 10000.0

            options.put("amount", (int) total);
            JSONObject preFill = new JSONObject();
            preFill.put("email", "");
            preFill.put("contact", "");
            options.put("prefill", preFill);

            // Set the Key ID for Razorpay Checkout
            co.setKeyID(RAZORPAY_KEY_ID); 
            Log.w("LOG_Runmawi_Payment", "startPaymentWithQuality - RAZORPAY_KEY_ID: " + RAZORPAY_KEY_ID);
            Log.w("LOG_Runmawi_Payment", "startPaymentWithQuality - Options before co.open: " + options.toString());

            co.open(activity, options);
        } catch (Exception e) {
            //Toast.makeText(activity, "Error in payment: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            Log.w("LOG_Runmawi_Payment", "startPaymentWithQuality - Exception: " + e.getMessage(), e);
            Log.w("LOG_runmawii", "Exception: " + e);
        }

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
            options.put("amount", (int) total);
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
    public void onPaymentSuccess(String razorpayPaymentId) {
        Log.w("LOG_Runmawi_Payment", "Payment successful. Payment ID: " + razorpayPaymentId);
        
        // Don't call add_payperview anymore - webhook handles it!
        // Just show success and refresh access
        Toast.makeText(this, "Payment successful! Processing...", Toast.LENGTH_SHORT).show();
        
        // Wait a moment for webhook to process, then refresh access
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                changeAccess(); // This will refresh the video access
            }
        }, 3000); // 3 second delay
    }

    @Override
    public void onPaymentError(int code, String response) {
        Log.e("LOG_Runmawi_Payment", "Payment failed. Code: " + code + ", Response: " + response);
        Toast.makeText(this, "Payment failed. Please try again.", Toast.LENGTH_SHORT).show();
        // Webhook will handle failed payments too
    }

    private void transactionLog(){

        Log.w("Log_runmawwi","ui: "+user_id+" price: "+quality_price+" vi: "+videoId);
        Call<JSONResponse1> trac_api=ApiClient.getInstance1().getApi().transactioLog(user_id,quality_price,"ppv", "razorpay","android",quality_resolution,videoId,"","","");
        trac_api.enqueue(new retrofit2.Callback<JSONResponse1>() {
            @Override
            public void onResponse(Call<JSONResponse1> call, retrofit2.Response<JSONResponse1> response) {
            }
            @Override
            public void onFailure(Call<JSONResponse1> call, Throwable throwable) {
                Log.w("Log_runmawwi"," err: "+throwable.getMessage());
            }
        });
    }

    private void createOrder() {
        transactionLog();
        Log.w("LOG_Runmawi_Payment", "createOrder called via Laravel API. quality_price: " + quality_price);
        
        // Call Laravel to create Razorpay order using consistent Retrofit 2.x
        Call<CreateRazorpayOrderResponse> orderCall = ApiClient.getInstance1().getApi().createRazorpayOrder(
            user_id, 
            idd.getText().toString(), // video_id
            quality_price,           // amount
            quality_name,            // ppv_plan (quality plan name)
            "Android"               // platform
        );

        orderCall.enqueue(new retrofit2.Callback<CreateRazorpayOrderResponse>() {
            @Override
            public void onResponse(Call<CreateRazorpayOrderResponse> call, retrofit2.Response<CreateRazorpayOrderResponse> response) {
                CreateRazorpayOrderResponse responseBody = response.body();
                if (responseBody != null && responseBody.getStatus().equalsIgnoreCase("true")) {
                    // Use the order created by Laravel
                    orderId = responseBody.getOrder_id();
                    RAZORPAY_KEY_ID = responseBody.getKey_id();
                    
                    Log.w("LOG_Runmawi_Payment", "Laravel order created successfully. Order ID: " + orderId);
                    
                    // Now start Razorpay payment with Laravel-created order
                    startRazorpayPayment(orderId, quality_price, RAZORPAY_KEY_ID);
                } else {
                    Log.e("LOG_Runmawi_Payment", "Failed to create order: " + (responseBody != null ? responseBody.getMessage() : "No response"));
                    Toast.makeText(HomePageVideoActivity.this, "Failed to create payment order", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreateRazorpayOrderResponse> call, Throwable t) {
                Log.e("LOG_Runmawi_Payment", "Error creating order: " + t.getMessage());
                Toast.makeText(HomePageVideoActivity.this, "Payment initialization failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startRazorpayPayment(String orderId, String amount, String keyId) {
        try {
            Checkout checkout = new Checkout();
            checkout.setKeyID(keyId);
            
            JSONObject options = new JSONObject();
            options.put("name", "RunMawi");
            options.put("description", "Video Purchase");
            options.put("order_id", orderId);
            options.put("currency", "INR");
            options.put("amount", Integer.parseInt(amount) * 100); // Convert to paise
            
            JSONObject prefill = new JSONObject();
            prefill.put("email", "user@example.com");
            prefill.put("contact", "9999999999");
            options.put("prefill", prefill);
            
            checkout.open(HomePageVideoActivity.this, options);
            
        } catch (Exception e) {
            Log.e("LOG_Runmawi_Payment", "Error starting Razorpay payment", e);
            Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
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

                                                if (downloadFromTracker.getPercentDownloaded() != -1) {


                                                    progressBarPercentage.setVisibility(View.VISIBLE);
                                                    progress_layout.setVisibility(View.VISIBLE);

                                                    progress_text.setText(AppUtil.floatToPercentage(downloadFromTracker.getPercentDownloaded()));


                                                    progressBarPercentage.setProgress(Integer.parseInt(AppUtil.floatToPercentage(downloadFromTracker.getPercentDownloaded()).replace("%", "")));
//                                                                                 tvProgressPercentage.setText(AppUtil.floatToPercentage(downloadFromTracker.getPercentDownloaded()));
                                                }

                                                Log.d("EXO STATE_DOWNLOADING ", +downloadFromTracker.getBytesDownloaded() + " " + downloadFromTracker.contentLength);
                                                Log.d("EXO  STATE_DOWNLOADING ", "" + downloadFromTracker.getPercentDownloaded());

                                                break;
                                            case STATE_COMPLETED:

                                                setCommonDownloadButton(ExoDownloadState.DOWNLOAD_COMPLETED);
                                                progressBarPercentage.setVisibility(View.GONE);
                                                progress_layout.setVisibility(View.GONE);

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
        exoPause = findViewById(R.id.exo_pause);

        videoThumb = (ImageView) findViewById(R.id.videothumb);


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
        // playerView.setErrorMessageProvider(new PlayerErrorMessageProvider());
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

        /*llDownloadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri Download_Uri = Uri.parse(url);
                DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setAllowedOverRoaming(false);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setTitle("Downloading");
                request.setDescription("Downloading File");
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES, "Shivam196.mp4"); // for public destination
                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                Long downloadID = downloadManager.enqueue(request);// enqueue puts the download request in the queue.
                Toast.makeText(HomePageVideoActivity.this, "down-> " + downloadID, Toast.LENGTH_SHORT).show();


            }
        });*/

        playerView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        setProgress();


    }


    public void prepareView() {
        playerView.setLayoutParams(new PlayerView.LayoutParams(
                // or ViewGroup.LayoutParams.WRAP_CONTENT
                PlayerView.LayoutParams.MATCH_PARENT,
                // or ViewGroup.LayoutParams.WRAP_CONTENT,
                ScreenUtils.convertDIPToPixels(HomePageVideoActivity.this, playerHeight)));


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
            //   initializePlayer();
            setProgress();

            if (playerView != null) {
                playerView.onResume();
            }
        }

        changeAccess();
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
        outState.putParcelable(KEY_TRACK_SELECTOR_PARAMETERS, getParentActivityIntent());
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

               /* MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
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


                    playerView.setLayoutParams(new PlayerView.LayoutParams(
                            // or ViewGroup.LayoutParams.WRAP_CONTENT
                            PlayerView.LayoutParams.MATCH_PARENT,
                            // or ViewGroup.LayoutParams.WRAP_CONTENT,
                            ScreenUtils.convertDIPToPixels(HomePageVideoActivity.this, playerHeight)));


                    frameLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                    //  imgFullScreenEnterExit.setImageResource(R.drawable.exo_controls_fullscreen_enter);
                    isScreenLandscape = false;
                    FullScreencall();

                    hide();
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    FullScreencall();

                    playerView.setLayoutParams(new PlayerView.LayoutParams(
                            // or ViewGroup.LayoutParams.WRAP_CONTENT
                            PlayerView.LayoutParams.MATCH_PARENT,
                            // or ViewGroup.LayoutParams.WRAP_CONTENT,
                            PlayerView.LayoutParams.MATCH_PARENT));


                    frameLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                    //     imgFullScreenEnterExit.setImageResource(R.drawable.exo_controls_fullscreen_exit);
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

                //    downloadManager.addDownload(downloadTracker.getDownloadRequest(parse(urlll)), STATE_STOPPED);

//                DownloadService.sendSetStopReason(
//                        HomePageVideoActivity.this,
//                        DemoDownloadService.class,
//                        downloadTracker.getDownloadRequest(Uri.parse(urlll)).id,
//                        Download.STATE_STOPPED,
//                        /* foreground= */ false);

                break;

            case DOWNLOAD_RESUME:

                //  downloadManager.addDownload(downloadTracker.getDownloadRequest(parse(urlll)), Download.STOP_REASON_NONE);
//                DownloadService.sendSetStopReason(
//                        HomePageVideoActivity.this,
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

    private void exoButtonPrepareDecision() {
//        if (downloadTracker.downloads.size() > 0) {
//            Download download = downloadTracker.downloads.get(parse(urlll));
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
////                            HomePageVideoActivity.this,
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
    }


    private void fetchDownloadOptions() {
        trackKeys.clear();

        if (pDialog == null || !pDialog.isShowing()) {
            pDialog = new ProgressDialog(HomePageVideoActivity.this);
            pDialog.setTitle(null);
            pDialog.setCancelable(false);
            pDialog.setMessage("Preparing Download Options...");
            pDialog.show();
        }
        //  DownloadHelper downloadHelper = DownloadHelper.forDash(HomePageVideoActivity.this, parse(urlll), dataSourceFactory, new DefaultRenderersFactory(HomePageVideoActivity.this));

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
                } else {

                    Toast.makeText(getApplicationContext(), "d", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }

                optionsToDownload.clear();
                showDownloadOptionsDialog(myDownloadHelper, trackKeys);
            }

            @Override
            public void onPrepareError(DownloadHelper helper, IOException e) {

                Log.d("sdsdd", String.valueOf(e));
                Toast.makeText(getApplicationContext(), "Failed: " + e, Toast.LENGTH_LONG).show();
                pDialog.dismiss();

            }
        });
*/
    }

    private void showDownloadOptionsDialog(DownloadHelper helper, List<TrackKey> trackKeyss) {

        if (helper == null) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(HomePageVideoActivity.this);
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
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(HomePageVideoActivity.this, // Context
                android.R.layout.simple_list_item_single_choice, // Layout
                optionsToDownload // List
        );

        TrackKey trackKey = trackKeyss.get(0);
        qualityParams = ((DefaultTrackSelector) trackSelector).getParameters().buildUpon().setMaxVideoSize(trackKey.getTrackFormat().width, trackKey.getTrackFormat().height).setMaxVideoBitrate(trackKey.getTrackFormat().bitrate).build();

        builder.setSingleChoiceItems(arrayAdapter, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TrackKey trackKey = trackKeyss.get(i);

                qualityParams = ((DefaultTrackSelector) trackSelector).getParameters().buildUpon().setMaxVideoSize(trackKey.getTrackFormat().width, trackKey.getTrackFormat().height).setMaxVideoBitrate(trackKey.getTrackFormat().bitrate).build();


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
                        helper.addTrackSelection(periodIndex, qualityParams);
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

*/
/*
    private void initializePlayer() {


        TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory();
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(this, null, DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER);

        trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        trackSelector.setParameters(trackSelectorParameters);
        lastSeenTrackGroupArray = null;

        DefaultAllocator defaultAllocator = new DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE);
        DefaultLoadControl defaultLoadControl = new DefaultLoadControl(defaultAllocator, DefaultLoadControl.DEFAULT_MIN_BUFFER_MS, DefaultLoadControl.DEFAULT_MAX_BUFFER_MS, DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS, DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS, DefaultLoadControl.DEFAULT_TARGET_BUFFER_BYTES, DefaultLoadControl.DEFAULT_PRIORITIZE_TIME_OVER_SIZE_THRESHOLDS);


        if (user_id == null || user_role == null) {

            player = ExoPlayerFactory.newSimpleInstance(*/
    /* context= *//*
 HomePageVideoActivity.this, renderersFactory, trackSelector, defaultLoadControl);
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

            if (rented.equalsIgnoreCase("rentted")) {

                playerView.setVisibility(View.GONE);
                videoThumb.setVisibility(View.VISIBLE);
                player = ExoPlayerFactory.newSimpleInstance(*/
    /* context= *//*
 HomePageVideoActivity.this, renderersFactory, trackSelector, defaultLoadControl);
                player.addListener(new PlayerEventListener());
                player.setPlayWhenReady(false);
                player.addAnalyticsListener(new EventLogger(trackSelector));
                playerView.setPlayer(player);
                playerView.setPlaybackPreparer(this);

               */
/* mediaSource = buildMediaSource(Uri.parse(urlll));
                if (player != null) {
                    player.prepare(mediaSource, false, true);
                }*//*



                exoButtonPrepareDecision();

                updateButtonVisibilities();
                initBwd();
                initFwd();

            } else {

                // Create the ExoPlayer instance using the new ExoPlayer.Builder
                // Create the ExoPlayer instance using the new ExoPlayer.Builder
                player = (SimpleExoPlayer) new ExoPlayer.Builder(HomePageVideoActivity.this)
                        .setRenderersFactory(renderersFactory)      // Set RenderersFactory
                        .setTrackSelector(trackSelector)            // Set TrackSelector
                        .setLoadControl(defaultLoadControl)         // Set LoadControl
                        .build();

// Set player listeners and configuration
                player.addListener((Player.Listener) new PlayerEventListener());
                player.setPlayWhenReady(false);  // Disable auto-play initially

// Analytics: Use EventLogger with the track selector
                player.addAnalyticsListener(new EventLogger(trackSelector));

// Set the player to the playerView
                playerView.setPlayer(player);

// No need for setPlaybackPreparer, handle media preparation manually if needed




                exoButtonPrepareDecision();
                updateButtonVisibilities();

                initBwd();
                initFwd();
            }
        } else {

            if (rented.equalsIgnoreCase("")) {

                playerView.setVisibility(View.GONE);
                videoThumb.setVisibility(View.VISIBLE);
                player = ExoPlayerFactory.newSimpleInstance(*/
    /* context= *//*
 HomePageVideoActivity.this, renderersFactory, trackSelector, defaultLoadControl);
                player.addListener((Player.Listener) new PlayerEventListener());
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
                player = ExoPlayerFactory.newSimpleInstance(*/
    /* context= *//*
 HomePageVideoActivity.this, renderersFactory, trackSelector, defaultLoadControl);
                player.addListener(new PlayerEventListener());
                player.setPlayWhenReady(false);
                player.addAnalyticsListener(new EventLogger(trackSelector));
                playerView.setPlayer(player);
                playerView.setPlaybackPreparer(this);


            }

        }

    }
*/

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
                // return new DashMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            case C.TYPE_SS:
                //  return new SsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            case C.TYPE_HLS:
                // return new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
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
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        llParentContainer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
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


     /*   if (isScreenLandscape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            playerView.setLayoutParams(
                    new PlayerView.LayoutParams(
                            // or ViewGroup.LayoutParams.WRAP_CONTENT
                            PlayerView.LayoutParams.MATCH_PARENT,
                            // or ViewGroup.LayoutParams.WRAP_CONTENT,
                            ScreenUtils.convertDIPToPixels(HomePageVideoActivity.this, playerHeight)));


            frameLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

            imgFullScreenEnterExit.setImageResource(R.drawable.exo_controls_fullscreen_enter);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            isScreenLandscape = false;
            hide();


        } else {*/
        super.onBackPressed();
        finish();

        //  }

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
        startAutoPlay = false;
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
                initializePlayer();
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
                    if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO) == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                        showToast(R.string.error_unsupported_video);
                    }
                    if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_AUDIO) == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
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
                    MediaCodecRenderer.DecoderInitializationException decoderInitializationException = (MediaCodecRenderer.DecoderInitializationException) cause;
                    if (decoderInitializationException.codecInfo == null) {
                        if (decoderInitializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException) {
                            errorString = getString(R.string.error_querying_decoders);
                        } else if (decoderInitializationException.secureDecoderRequired) {
                            errorString = getString(R.string.error_no_secure_decoder, decoderInitializationException.mimeType);
                        } else {
                            errorString = getString(R.string.error_no_decoder, decoderInitializationException.mimeType);
                        }
                    } else {
                        errorString = getString(R.string.error_instantiating_decoder, decoderInitializationException.codecInfo);
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

    public static synchronized HomePageVideoActivity getInstance() {
        return mInstancee;
    }

    public void changeAccess() {


        Api.getClient().getContinueexist(user_id, videoId, new Callback<ReadNotification>() {
            @Override
            public void success(ReadNotification readNotification, Response response) {

                readNotification = readNotification;

                if (readNotification.getStatus().equalsIgnoreCase("true")) {
                    continuee = "1";
                } else {
                    continuee = "0";
                }
                Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, videoId);
                res.enqueue(new retrofit2.Callback<JSONResponse>() {

                    @Override
                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            JSONResponse jsonResponse = response.body();
                            movie_detaildata = safeArrayToList(jsonResponse.getVideodetail());

                            // Check if movie_detaildata is empty or null before proceeding
                            if (movie_detaildata == null || movie_detaildata.isEmpty()) {
                                android.util.Log.e("CHANGE_ACCESS_ERROR", "No video details available in changeAccess method");
                                Toast.makeText(HomePageVideoActivity.this, "Video details not available. Please try again later.", Toast.LENGTH_LONG).show();
                                return;
                            }

                          /*  if (continuee == "1") {
                                if (user_role.equalsIgnoreCase("admin")) {
                                    resume.setVisibility(View.VISIBLE);
                                    play_begin.setVisibility(View.VISIBLE);
                                    paynow.setVisibility(View.GONE);
                                    rent.setVisibility(View.GONE);
                                    playnow.setVisibility(View.GONE);
                                }

                                else if (movie_detaildata.get(0).getAccess().equalsIgnoreCase("ppv")) {

                                    if (jsonResponse.getPpv_video_status().equalsIgnoreCase("can_view")) {
                                        resume.setVisibility(View.VISIBLE);
                                        play_begin.setVisibility(View.VISIBLE);
                                        paynow.setVisibility(View.GONE);
                                        rent.setVisibility(View.GONE);
                                        playnow.setVisibility(View.GONE);
                                    } else {
                                        if (false) {
                                            int free_duration = Integer.parseInt(movie_detaildata.get(0).getFree_duration());//610
                                            int minutes = free_duration / 60;//10

                                            if (minutes < 1) {
                                                rent_text.setText("Play Video ( Free " + free_duration + " seconds )");
                                            } else {
                                                rent_text.setText("Play Video ( Free " + minutes + " minutes )");
                                            }
                                        } else {
                                            rent_text.setText("Rent");
                                        }

                                        resume.setVisibility(View.GONE);
                                        play_begin.setVisibility(View.GONE);
                                        paynow.setVisibility(View.GONE);
                                        rent.setVisibility(View.VISIBLE);
                                        playnow.setVisibility(View.GONE);
                                    }

                                } else if (movie_detaildata.get(0).getAccess().equalsIgnoreCase("subscriber")) {

                                    if (user_role.equalsIgnoreCase("subscriber")) {
                                        resume.setVisibility(View.VISIBLE);
                                        play_begin.setVisibility(View.VISIBLE);
                                        paynow.setVisibility(View.GONE);
                                        rent.setVisibility(View.GONE);
                                        playnow.setVisibility(View.GONE);
                                    } else {
                                        resume.setVisibility(View.GONE);
                                        play_begin.setVisibility(View.GONE);
                                        paynow.setVisibility(View.VISIBLE);
                                        rent.setVisibility(View.GONE);
                                        playnow.setVisibility(View.GONE);
                                    }
                                } else {
                                    resume.setVisibility(View.GONE);
                                    play_begin.setVisibility(View.GONE);
                                    paynow.setVisibility(View.GONE);
                                    rent.setVisibility(View.GONE);
                                    playnow.setVisibility(View.VISIBLE);
                                }
                            }*/

                            // else {

                            if (user_role.equalsIgnoreCase("admin")) {
                                resume.setVisibility(View.GONE);
                                play_begin.setVisibility(View.GONE);
                                paynow.setVisibility(View.GONE);
                                rent.setVisibility(View.GONE);
                                playnow.setVisibility(View.VISIBLE);
                            } else if (movie_detaildata.get(0).getAccess().equalsIgnoreCase("ppv")) {

                                if (jsonResponse.getPpv_exist().equalsIgnoreCase("1")) {

                                    resume.setVisibility(View.GONE);
                                    play_begin.setVisibility(View.GONE);
                                    paynow.setVisibility(View.GONE);
                                    rent.setVisibility(View.GONE);
                                    playnow.setVisibility(View.VISIBLE);
                                } else {
                                    if (false) {
                                        int free_duration = Integer.parseInt(movie_detaildata.get(0).getFree_duration());//610
                                        int minutes = free_duration / 60;//10

                                        if (minutes < 1) {
                                            rent_text.setText("Play Video ( Free " + free_duration + " seconds )");
                                        } else {
                                            rent_text.setText("Play Video ( Free " + minutes + " minutes )");
                                        }
                                    } else {
                                        rent_text.setText("Rent");
                                    }

                                    resume.setVisibility(View.GONE);
                                    play_begin.setVisibility(View.GONE);
                                    paynow.setVisibility(View.GONE);
                                    rent.setVisibility(View.VISIBLE);
                                    playnow.setVisibility(View.GONE);
                                }

                            } else if (movie_detaildata.get(0).getAccess().equalsIgnoreCase("subscriber")) {

                                if (user_role.equalsIgnoreCase("subscriber")) {
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
                                resume.setVisibility(View.GONE);
                                play_begin.setVisibility(View.GONE);
                                paynow.setVisibility(View.GONE);
                                rent.setVisibility(View.GONE);
                                playnow.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                    //  }

                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable t) {

                    }
                });

            }

            @Override
            public void failure(RetrofitError error) {

                //Toast.makeText(HomePageVideoActivity.this, "Check Your Internet Connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showDetails(JSONObject jsonDetails) throws JSONException {

        Toast.makeText(getApplicationContext(), "" + jsonDetails.getString("id"), Toast.LENGTH_LONG).show();
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
                    Log.w("Log_direct_pay", "if: " + direct_pay.getStatus() + " trac_ID: " + direct_pay.getTransactionId());
                    return direct_pay.getStatus() + ":" + direct_pay.getTransactionId();
                } else {
                    Log.w("Log_direct_pay", "else: " + direct_pay.getStatus() + direct_pay.getResponseText());
                    return direct_pay.getStatus() + ":" + direct_pay.getResponseText();
                }
            } catch (Exception e) {
                e.printStackTrace();
                this.exception = e;
                Log.w("Log_direct_pay", "catch: " + e.getMessage());
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
            Call<JSONResponse> pay_api = ApiClient.getInstance1().getApi().paydunyaVideoRent(user_id, videoId, "Android");
            pay_api.enqueue(new retrofit2.Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        paydunya_dialog.cancel();
                        JSONResponse jsonResponse = response.body();
                        if (jsonResponse.getStatus().equalsIgnoreCase("true")) {
                            Toast.makeText(HomePageVideoActivity.this, "" + jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            changeAccess();
                        } else {
                            Toast.makeText(HomePageVideoActivity.this, "" + jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
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

    /**
     * Finds the position of the purchased quality in the qualities list
     * @param videoDetails The video details containing user's PPV plan
     * @param qualitiesList The list of available qualities
     * @return The position of purchased quality, or -1 if not found
     */
    private int findPurchasedQualityPosition(videodetail videoDetails, ArrayList<quality_free> qualitiesList) {
        if (videoDetails == null || qualitiesList == null || qualitiesList.isEmpty()) {
            return -1;
        }
        
        String userOwnedPlan = videoDetails.getPPV_Plan();
        if (userOwnedPlan == null || userOwnedPlan.isEmpty()) {
            Log.w("LOG_TraditionalQuality", "User has no purchased quality");
            return -1;
        }
        
        // Extract resolution from user's plan (e.g., "720p" from "Medium Quality (720p)")
        String ownedResolution = extractResolutionFromQualityName(userOwnedPlan);
        Log.w("LOG_TraditionalQuality", "Looking for owned resolution: " + ownedResolution);
        
        // Find matching quality in the list
        for (int i = 0; i < qualitiesList.size(); i++) {
            quality_free quality = qualitiesList.get(i);
            String qualityResolution = quality.getResolution();
            
            Log.w("LOG_TraditionalQuality", "Comparing: '" + ownedResolution + "' with '" + qualityResolution + "'");
            
            if (ownedResolution.equalsIgnoreCase(qualityResolution)) {
                Log.w("LOG_TraditionalQuality", "Found purchased quality at position: " + i);
                return i;
            }
        }
        
        Log.w("LOG_TraditionalQuality", "Purchased quality not found in list");
        return -1;
    }

}