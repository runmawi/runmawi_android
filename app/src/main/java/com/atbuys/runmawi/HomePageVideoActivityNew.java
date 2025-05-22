package com.atbuys.runmawi;

import static androidx.media3.exoplayer.offline.Download.STATE_COMPLETED;
import static androidx.media3.exoplayer.offline.Download.STATE_DOWNLOADING;
import static androidx.media3.exoplayer.offline.Download.STATE_QUEUED;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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
import androidx.media3.exoplayer.upstream.BandwidthMeter;
import androidx.media3.ui.AspectRatioFrameLayout;
import androidx.media3.ui.DefaultTimeBar;
import androidx.media3.ui.PlayerControlView;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.cinetpay.androidsdk.CinetPayActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.atbuys.runmawi.Vdocipher.WebViewActivity;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardMultilineWidget;
import com.atbuys.runmawi.Config.Config;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
public class HomePageVideoActivityNew extends AppCompatActivity implements View.OnClickListener, PlayerControlView.VisibilityListener,  PaymentResultListener, GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {
    private static final int playerHeight = 250;
    private static final int STATE_STOPPED =22 ;
    ProgressDialog pDialog;

    private ArrayList<payment_settings> payment_settingslist;
    String key,py_id;
    private Stripe stripe;
    PaymentMethodCreateParams params;
    protected static final CookieManager DEFAULT_COOKIE_MANAGER;
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 2000;
    private static final int UI_ANIMATION_DELAY = 300;
    // Saved instance state keys.
    // Saved instance state keys.
    private static final String KEY_TRACK_SELECTOR_PARAMETERS = "track_selector_parameters";
    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "position";
    private static final String KEY_AUTO_PLAY = "auto_play";
    private static HomePageVideoActivityNew mInstancee;
    protected String useragent1;
    Set<String> usersSet = new HashSet<>();

    GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;

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
    private AdsLoader adsLoader;
    private Uri loadedAdTagUri;
    private FrameLayout frameLayoutMain;
    private ImageView imgBwd;
    private ImageView exoPlay;
    private ImageView exoPause;
    private ImageView back;
    private BottomSheetDialog bottomSheetDialog;
    private ImageView exoreply;
    private ImageView imgFwd, imgBackPlayer;
    private TextView skipintro;
    private TextView tvPlayerCurrentTime;
    private DefaultTimeBar exoTimebar;
    private ProgressBar exoProgressbar;
    private TextView tvPlayerEndTime;
    private ImageView imgSetting;
    private ImageView imgFullScreenEnterExit;
    private CardView playnow, paynow, rent;
    TextView rent_text;
    private LinearLayout userfeatuerlayout;
    private ImageView coverimage;
    private ImageView videoThumb;
    private String ppv_price,currency;
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
    private String videoUrl;
    RelativeLayout progress_layout;

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
    private String videoads;

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
    private ImageView like, liked, dislikee, dislikeed;
    LinearLayout unlikelayout, undislikelayout;
    AddComment addComment;


    String nxtvidid, nextvidurl;

    LinearLayout watchlist, watchlater, hidelayout1, likee, dislike, favouriteLayout,genere_layout,share1;
    ImageView watchlistimg, watchlistaddedimg, watchlaterimg, watchlateraddedimg, favlistimg, favlistaddimg,share;
    TextView videotext, views, nextep;
    LinearLayout  thismayalsolike_layout;
    TextView videotitle1, language1, genre2, duration, year, getCast1;
    TextView imdb, durationandyear, genre_text,view_more;
    ViewPager viewPager;
    TabLayout tableLayout;
    String shareurl, user_id, autoplay, subtitles_text, subchecked1, subchecked2, subchecked3, subchecked4;
    String video_like, video_dislike;

    LinearLayout trailerLayout;
    ImageView trailerImage;


    private RecyclerView.LayoutManager layoutManager, layoutManager1, manager;
    private ArrayList<movies> moviesdata;
    private ArrayList<channelrecomended> recomendeddatalist;
    private ArrayList<genre> genredata;
    private ArrayList<livedetail> movie_detaildata;
    private ArrayList<videossubtitles> moviesubtitlesdata;
    private ArrayList<active_payment_settings> active_payment_settingsList;
    private ArrayList<comment> usercommentslist;
    showwishlist showwish;
    private ArrayList<movieresolution> movieresolutiondata;
    private ArrayList<video_cast> castdetails;
    public static ArrayList<VideoModel> videoModels = new ArrayList<>();
    public ArrayList<String> arrPackage;

    //  DownloadActivity downloadActivity;
    Active_Payment_settingsAdopter active_payment_settingsAdopter;


    private List<videossubtitles> statelistdata;
    public String urlll = "";
    public String rented = "";
    public String suburl = "";
    String type = "channel";
    TextView norecommanded, idd, urll, downimgurl, downname, downurl, downloadtext;

    RecyclerView thismaylikerecycler;
    RecyclerView castandcrewrecycler;
    LinearLayout castlayout;
    // ExtractorsFactory extractorsFactory;
    ThismaylikeAdopter thismaylikeadopter;
    castandcrewadapter castandcrewadapter;
    subtitleLangAdopter subtitleLangadopter;
    String user_role, auto_play;
    //ShowMoreTextView textView;
    CommentsAdopter commentsAdopter;
    TextView pays;
    LinearLayout recurring_layout;
    TextView scheduled_time;
    String renturl, dataa;
    private long position;
    // private Object cast_details;.
    private TextView no_cast;
    int PAYPAL_REQUEST_CODE = 7777;
    public static PayPalConfiguration config;


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

    String RAZORPAY_KEY_ID;
    String RAZORPAY_KEY_SECRET;
    String orderId;
   /* String RAZORPAY_KEY_ID = "rzp_test_2YHmQefc5LWtAu";
    String RAZORPAY_KEY_SECRET = "vfMkD21soV9h3R0HZqrkh7nb";*/

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

        gestureDetector = new GestureDetector(HomePageVideoActivityNew.this, HomePageVideoActivityNew.this);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        arrPackage = new ArrayList<>();

        dataSourceFactory = buildDataSourceFactory();
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }

        hideStatusBar();

        setContentView(R.layout.live_home_player);

        Intent in = getIntent();
        videoId = in.getStringExtra("id");
        urlll = in.getStringExtra("url");
        suburl = in.getStringExtra("suburl");
        dataa = in.getStringExtra("data");
        rented = in.getStringExtra("xtra");

        config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION).clientId(Config.PAYPAL_CLIENT_ID);
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        thismayalsolike_layout = (LinearLayout) findViewById(R.id.thismayalsolike_layout);
        idd = (TextView) findViewById(R.id.idd);
        urll = (TextView) findViewById(R.id.urll);
        downurl = (TextView) findViewById(R.id.downurl);
        downimgurl = (TextView) findViewById(R.id.downimgurl);
        downname = (TextView) findViewById(R.id.downname);
        downloadtext = (TextView) findViewById(R.id.download);
        progress_text = (TextView) findViewById(R.id.progress_text);
        progress_layout = (RelativeLayout) findViewById(R.id.progress_layout);
        recurring_layout=(LinearLayout)findViewById(R.id.recurring_layout);
        scheduled_time=(TextView)findViewById(R.id.scheduled_time);

        //    textView = findViewById(R.id.text_view_show_more);
        pays = (TextView) findViewById(R.id.pays);

        back = (ImageView) findViewById(R.id.back);


        trailerLayout = (LinearLayout) findViewById(R.id.trailerlayout);
        // ll_download_video_progress = (LinearLayout) findViewById(R.id.ll_download_video_progress);
        trailerImage = (ImageView) findViewById(R.id.trailerimage);


        idd.setText(videoId);
        urll.setText(urlll);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pDialog = new ProgressDialog(this);


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
            // DownloadService.start(this, DemoDownloadService.class);
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
                //    observerVideoStatus();
                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnableCode);


        videotext = (TextView) findViewById(R.id.videotext);
        //nextep = (TextView) findViewById(R.id.nextepisode);
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
        rent_text=(TextView)findViewById(R.id.rent_text);
        rent = (CardView) findViewById(R.id.rent_now);
        userfeatuerlayout = (LinearLayout) findViewById(R.id.userfeatuerlayout);
        autoplayswitch = (Switch) findViewById(R.id.autoplay);
        views = (TextView) findViewById(R.id.view);


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
        share1=(LinearLayout)findViewById(R.id.share1);
        norecommanded = (TextView) findViewById(R.id.norecommanded);
        //  cast = (ImageView) findViewById(R.id.cast);
        genre2 = (TextView) findViewById(R.id.genre);
        /*     cast_name=(TextView)findViewById(R.id.cast_name);*/
        language1 = (TextView) findViewById(R.id.language);
        year = (TextView) findViewById(R.id.movieyear);
        duration = (TextView) findViewById(R.id.videoduration);
        videotitle1 = (TextView) findViewById(R.id.videotitle);
        genredata = new ArrayList<genre>();
        movieresolutiondata = new ArrayList<movieresolution>();
        movie_detaildata = new ArrayList<livedetail>();
        moviesubtitlesdata = new ArrayList<videossubtitles>();
        recomendeddatalist = new ArrayList<channelrecomended>();
        usercommentslist = new ArrayList<comment>();
        castdetails = new ArrayList<video_cast>();
        active_payment_settingsList = new ArrayList<active_payment_settings>();
        imdb = (TextView) findViewById(R.id.imdb);
        durationandyear = (TextView) findViewById(R.id.durationandyear);
        genre_text = (TextView) findViewById(R.id.genre_text);
        genere_layout = (LinearLayout) findViewById(R.id.genere_layout);
        view_more=(TextView)findViewById(R.id.view_more);
        tableLayout=(TabLayout ) findViewById(R.id.tab_layout);
        viewPager=(ViewPager)findViewById(R.id.view_pager);

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

        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getLiveDetail(user_id, videoId);
        res.enqueue(new retrofit2.Callback<JSONResponse>() {

            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getLivedetail()));
                String start_time="",end_time="";

                if (movie_detaildata.get(0).getPublish_type().equalsIgnoreCase("recurring_program")){
                    try {
                        String startHourTime = movie_detaildata.get(0).getProgram_start_time();
                        String endHourTime = movie_detaildata.get(0).getProgram_end_time();
                        SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                        SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");

                        Date _24HourDt = _24HourSDF.parse(startHourTime);
                        Date _24HourDt1 = _24HourSDF.parse(endHourTime);
                        start_time=_12HourSDF.format(_24HourDt);
                        //end_time=_12HourSDF.format(_24HourDt1);
                        end_time= String.valueOf(_24HourDt1);

                        //System.out.println(_24HourDt);
                        //System.out.println(_12HourSDF.format(_24HourDt));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    paynow.setVisibility(View.GONE);
                    rent.setVisibility(View.GONE);
                    playnow.setVisibility(View.GONE);
                    recurring_layout.setVisibility(View.VISIBLE);
                    switch (movie_detaildata.get(0).getRecurring_program()){
                        case "monthly":scheduled_time.setText("Live Streaming On Every Month on Day "+movie_detaildata.get(0).getRecurring_program_month_day()+" from "+start_time+" to "+end_time+" - US/Pacific");
                            break;
                        case "weekly": {
                            String day="";
                            switch (movie_detaildata.get(0).getRecurring_program_week_day()){
                                case "0":day="Sunday";
                                    break;
                                case "1":day="Monday";
                                    break;
                                case "2":day="Tuesday";
                                    break;
                                case "3":day="Wednesday";
                                    break;
                                case "4":day="Thursday";
                                    break;
                                case "5":day="Friday";
                                    break;
                                case "6":day="Saturday";
                                    break;
                            }
                            scheduled_time.setText("Live Streaming On Every Week " + day + " from " +  start_time + " to " + end_time + " - US/Pacific");
                        }break;
                        case "daily":scheduled_time.setText("Live Streaming On daily from "+start_time+" to "+end_time+" - US/Pacific");
                            break;
                        case "custom":scheduled_time.setText("Live Streaming On "+movie_detaildata.get(0).getCustom_start_program_time()+" - "+movie_detaildata.get(0).getCustom_end_program_time()+" - US/Pacific");
                            break;
                    }
                } else if (user_role.equalsIgnoreCase("admin")) {
                    paynow.setVisibility(View.GONE);
                    rent.setVisibility(View.GONE);
                    playnow.setVisibility(View.VISIBLE);
                }else if (movie_detaildata.get(0).getAccess().equalsIgnoreCase("ppv")) {

                    if (jsonResponse.getPpv_video_status().equalsIgnoreCase("can_view") || user_role.equalsIgnoreCase("subscriber") ){

                        paynow.setVisibility(View.GONE);
                        rent.setVisibility(View.GONE);
                        playnow.setVisibility(View.VISIBLE);
                    }else {
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

                        paynow.setVisibility(View.GONE);
                        rent.setVisibility(View.VISIBLE);
                        playnow.setVisibility(View.GONE);
                    }

                } else if (movie_detaildata.get(0).getAccess().equalsIgnoreCase("subscriber")) {

                    if (user_role.equalsIgnoreCase("subscriber")) {

                        paynow.setVisibility(View.GONE);
                        rent.setVisibility(View.GONE);
                        playnow.setVisibility(View.VISIBLE);
                    } else {

                        paynow.setVisibility(View.VISIBLE);
                        rent.setVisibility(View.GONE);
                        playnow.setVisibility(View.GONE);
                    }
                } else {

                    paynow.setVisibility(View.GONE);
                    rent.setVisibility(View.GONE);
                    playnow.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

            }
        });

        tableLayout.addTab(tableLayout.newTab().setText("Trailers"));
        tableLayout.addTab(tableLayout.newTab().setText("Comments"));
        tableLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);
        auto_play = prefs.getString(sharedpreferences.autoplay, null);

        final LiveTabAdapter adapter = new LiveTabAdapter(this,getSupportFragmentManager(), tableLayout.getTabCount(),videoId,user_id);
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


        playnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity(new Intent(getApplicationContext(),LiveStreamVideoCheckActivity.class));
                playnow.setEnabled(false);

                //Log.w("Runmawi_test",movie_detaildata.get(0).getLivestream_url());
                /*Intent in = new Intent(getApplicationContext(), WebViewActivity.class);
                in.putExtra("embed", movie_detaildata.get(0).getLivestream_url());
                startActivity(in);*/

                Intent intent = new Intent(getApplicationContext(), EncodedWebViewActivity.class);
                intent.putExtra("url", movie_detaildata.get(0).getEmbed_url());
                startActivity(intent);
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

                if (user_id == null) {
                    Intent in = new Intent(getApplicationContext(), SigninActivity.class);
                    startActivity(in);
                } else {
                    Intent in = new Intent(getApplicationContext(), SubscribeActivity.class);
                    startActivity(in);

                }
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
                currency=response.body().getCurrency_Symbol();
            }
            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
            }
        });

        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call<JSONResponse> res = ApiClient.getInstance1().getApi().getLiveDetail(user_id, videoId);
                res.enqueue(new retrofit2.Callback<JSONResponse>() {
                    @Override
                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                        JSONResponse jsonResponse = response.body();
                        movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getLivedetail()));


                        if (movie_detaildata.get(0).getType().equalsIgnoreCase("Encode_video")) {
                            renturl = movie_detaildata.get(0).getHls_url();
                        } else {

                            renturl = movie_detaildata.get(0).getLivestream_url();
                        }

                        ppv_price = movie_detaildata.get(0).getPpv_price();

                        if (renturl == null) {

                        }

                    }

                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                        Log.d("Error", t.getMessage());
                    }
                });

                if (false){

                    Intent in = new Intent(getApplicationContext(), LiveOnlinePlayerActivity.class);
                    in.putExtra("id", idd.getText().toString());
                    in.putExtra("url", urll.getText().toString());
                    in.putExtra("suburl", suburl);
                    in.putExtra("data", dataa);
                    in.putExtra("ads", videoads);
                    startActivity(in);

                }else {


                    if (user_id == null) {
                        Intent in = new Intent(getApplicationContext(), SigninActivity.class);
                        startActivity(in);
                    } else {

                        dialog = new Dialog(HomePageVideoActivityNew.this);
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

                                JSONResponse jsonResponse = response.body();


                                wes.setHasFixedSize(true);
                                wes.setLayoutManager(new GridLayoutManager(HomePageVideoActivityNew.this, 1));

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
                                new RecyclerItemClickListener(HomePageVideoActivityNew.this, new RecyclerItemClickListener.OnItemClickListener() {
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

                                if (pays.getText().toString().equalsIgnoreCase("stripe")) {

                                    View view = getLayoutInflater().inflate(R.layout.activity_payperview_payment_page, null);
                                    BottomSheetDialog dialog1 = new BottomSheetDialog(HomePageVideoActivityNew.this);
                                    dialog1.setContentView(view);

                                    CardMultilineWidget cardMultilineWidget = view.findViewById(R.id.card_input_widget);
                                    Button paynow1 = (Button) view.findViewById(R.id.save_payment);
                                    ProgressBar payment_progress = (ProgressBar) view.findViewById(R.id.payment_progress);
                                    CheckBox check = (CheckBox) view.findViewById(R.id.check);
                                    TextView terms=(TextView)view.findViewById(R.id.terms);
                                    TextView movie_name=(TextView)view.findViewById(R.id.movie_name);
                                    TextView movie_price=(TextView)view.findViewById(R.id.movie_price);

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
                                    movie_price.setText(ppv_price+" "+currency);
                                    movie_name.setText(videotitle1.getText().toString());

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
                                                        Api.getClient().getAddPayperViewLive(user_id, idd.getText().toString(), py_id, "stripe", ppv_price,"Android", new Callback<Addpayperview>() {

                                                            @Override
                                                            public void success(Addpayperview addpayperview1, Response response) {

                                                                dialog1.cancel();
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
                                                                Toast.makeText(getApplicationContext(), "Api issue: "+ error.getMessage(), Toast.LENGTH_LONG).show();
                                                                paynow1.setVisibility(View.VISIBLE);
                                                                payment_progress.setVisibility(View.GONE);
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onError(@NotNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), "Payment Issue: "+e.getMessage(), Toast.LENGTH_LONG).show();
                                                        paynow1.setVisibility(View.VISIBLE);
                                                        payment_progress.setVisibility(View.GONE);

                                                    }
                                                });
                                            }
                                        }
                                    });

                                    dialog1.show();
                                    dialog.dismiss();

                                }  else if (pays.getText().toString().equalsIgnoreCase("Paydunya")) {

                                    dialog.dismiss();
                                    View view = getLayoutInflater().inflate(R.layout.paydunya_payment_page, null);
                                    paydunya_dialog = new BottomSheetDialog(HomePageVideoActivityNew.this);
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
                                            paydunya_dialog.cancel();
                                        }
                                    });
                                    plan_name.setText(videotitle1.getText().toString());
                                    plan_price.setText(ppv_price + " " + currency);
                                    paydunya_pay_now.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (paydunya_email_address.getText().toString().isEmpty() || paydunya_email_address.getText().toString().trim().isEmpty()) {
                                                Toast.makeText(HomePageVideoActivityNew.this, "EMAIL ADDRESS not entered", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Call<JSONResponse> call = ApiClient.getInstance1().getApi().getPaymentDetails();
                                                call.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                    @Override
                                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

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

                                                    @Override
                                                    public void onFailure(Call<JSONResponse> call, Throwable t) {

                                                    }
                                                });
                                            }
                                        }
                                    });


                                }else if (pays.getText().toString().equalsIgnoreCase("razorpay")) {

                                    dialog.dismiss();
                                    //startPayment();
                                    createOrder();

                                } else if (pays.getText().toString().equalsIgnoreCase("paystack")) {

                                    dialog.dismiss();
                                    Intent intent=new Intent(HomePageVideoActivityNew.this, CheckoutActivity.class);
                                    intent.putExtra("price", ppv_price);
                                    intent.putExtra("id", idd.getText().toString());
                                    intent.putExtra("url", urlll);
                                    intent.putExtra("type", "live_rent");
                                    startActivity(intent);
                                } else if (pays.getText().toString().equalsIgnoreCase("CinetPay")) {
                                    dialog.dismiss();

                                    Call<JSONResponse> pay_api = ApiClient.getInstance1().getApi().getPaymentDetails();
                                    pay_api.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response2) {

                                            JSONResponse jsonResponse2 = response2.body();
                                            ArrayList<payment_settings> payment_settingslist = new ArrayList<>(Arrays.asList(jsonResponse2.getPayment_settings()));

                                            for (int j = 0; j < payment_settingslist.size(); j++) {
                                                if (payment_settingslist.get(j).getPayment_type().equalsIgnoreCase("CinetPay")) {

                                                    Intent in = new Intent(getApplicationContext(), MyCinetPayActivity.class);
                                                    //in.putExtra("amount", ppv_price);
                                                    in.putExtra("id", idd.getText().toString());
                                                    in.putExtra("url", urlll);
                                                    in.putExtra("type", "live_rent");
                                                    in.putExtra(CinetPayActivity.KEY_API_KEY, payment_settingslist.get(j).getCinetPay_APIKEY());
                                                    in.putExtra(CinetPayActivity.KEY_SITE_ID, payment_settingslist.get(j).getCinetPay_SITE_ID());
                                                    in.putExtra(CinetPayActivity.KEY_TRANSACTION_ID, String.valueOf(new Date().getTime()));
                                                    in.putExtra(CinetPayActivity.KEY_AMOUNT, ppv_price);
                                                    in.putExtra(CinetPayActivity.KEY_CURRENCY, currency);
                                                    in.putExtra(CinetPayActivity.KEY_DESCRIPTION, "Live Video Purchase");
                                                    in.putExtra(CinetPayActivity.KEY_CHANNELS, "MOBILE_MONEY,WALLET,CREDIT_CARD");
                                                    startActivity(in);
                                                }
                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<JSONResponse> call, Throwable t) {

                                        }
                                    });
                                }else if (pays.getText().toString().equalsIgnoreCase("payPal")) {

                                    dialog.dismiss();
                                    getPayment();
                                }

                            }

                        });
                    }
                }

            }
        });


        thismaylikeadopter = new ThismaylikeAdopter(recomendeddatalist, this);
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
        castlayout=(LinearLayout)findViewById(R.id.castlayout);

        castandcrewrecycler.setHasFixedSize(true);
        castandcrewrecycler.setLayoutManager(manager);
        castandcrewrecycler.setAdapter(castandcrewadapter);

        usercommentrecycler.setHasFixedSize(true);
        usercommentrecycler.setLayoutManager(layoutManager1);
        usercommentrecycler.setAdapter(thismaylikeadopter);


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


        Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getUsercommentslive(videoId, "LiveStream_play");
        movieres.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();

                if (jsonResponse.getComment().length == 0) {


                } else {

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


                    Api.getClient().getAddLivecomment(user_id, videoId, comment, "LiveStream_play", new Callback<AddComment>() {

                        @Override
                        public void success(AddComment addComment, Response response) {

                            addComment = addComment;

                            if (addComment.getStatus().equalsIgnoreCase("true")) {

                                Toast.makeText(getApplicationContext(), "" + addComment.getMessage(), Toast.LENGTH_LONG).show();

                                Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getUsercommentslive(videoId, "LiveStream_play");
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


/*
        cast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent("android.settings.CAST_SETTINGS"));

            }
        });*/


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


        usercommentrecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(HomePageVideoActivityNew.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (usercommentslist.size() > position) {
                            if (usercommentslist.get(position) != null) {


                                View view1 = getLayoutInflater().inflate(R.layout.comments_edit, null);
                                BottomSheetDialog dialog1 = new BottomSheetDialog(HomePageVideoActivityNew.this);
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


                                                    Api.getClient().getupdateLivecomment(user_id, videoId, comment, "LiveStream_play", usercommentslist.get(position).getId(), new Callback<AddComment>() {

                                                        @Override
                                                        public void success(AddComment addComment, Response response) {

                                                            addComment = addComment;

                                                            if (addComment.getStatus().equalsIgnoreCase("true")) {

                                                                Toast.makeText(getApplicationContext(), "" + addComment.getMessage(), Toast.LENGTH_LONG).show();

                                                                dialog1.cancel();
                                                                Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getUsercommentslive(videoId, "LiveStream_play");
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


                                                Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getUsercommentslive(videoId, "LiveStream_play");
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

        thismaylikerecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(HomePageVideoActivityNew.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (recomendeddatalist.size() > position) {
                            if (recomendeddatalist.get(position) != null) {


                                if (recomendeddatalist.get(position).getType() == null) {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, recomendeddatalist.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getLivedetail()));


                                            if (user_id == null) {


                                                Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                in.putExtra("id", recomendeddatalist.get(position).getId());
                                                in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                startActivity(in);

                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (recomendeddatalist.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", recomendeddatalist.get(position).getId());
                                                    in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", recomendeddatalist.get(position).getId());
                                                    in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((recomendeddatalist.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", recomendeddatalist.get(position).getId());
                                                    in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = recomendeddatalist.get(position).getVideo_url() + recomendeddatalist.get(position).getPath() + ".m3u8";

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((recomendeddatalist.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (recomendeddatalist.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", recomendeddatalist.get(position).getVideo_url() + recomendeddatalist.get(position).getPath() + ".m3u8");
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    }
                                                } else if (recomendeddatalist.get(position).getAccess().equalsIgnoreCase("ppv") || recomendeddatalist.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", recomendeddatalist.get(position).getId());
                                                    in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    startActivity(in);
                                                } else {

                                                    if (recomendeddatalist.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", recomendeddatalist.get(position).getVideo_url() + recomendeddatalist.get(position).getPath() + ".m3u8");
                                                        in.putExtra("xtra", "Norent");
                                                        startActivity(in);
                                                    }
                                                }

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                                            Log.d("Error", t.getMessage());
                                        }
                                    });

                                } else if (recomendeddatalist.get(position).getType().equalsIgnoreCase("embed")) {

                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getLiveDetail(user_id, recomendeddatalist.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getLivedetail()));


                                            if (user_id == null) {

                                                Intent in = new Intent(getApplicationContext(), TrailerPlayerActivity.class);
                                                in.putExtra("id", recomendeddatalist.get(position).getId());
                                                in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                startActivity(in);

                                            }

                                            if (user_id != null && user_role.equalsIgnoreCase("registered")) {

                                                if (response.body().getPpv_video_status().equalsIgnoreCase("pay_now")) {
//                                                    Intent in = new Intent(getApplicationContext(), YoutubeVideoHomepageActivity.class);
//                                                    in.putExtra("id", recomendeddatalist.get(position).getId());
//                                                    startActivity(in);

                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {
//
//                                                    Intent in = new Intent(getApplicationContext(), YoutubeVideoHomepageActivity.class);
//                                                    in.putExtra("id", recomendeddatalist.get(position).getId());
//                                                    startActivity(in);

                                                } else {


//                                                    Intent in = new Intent(getApplicationContext(), YoutubeVideoHomepageActivity.class);
//                                                    in.putExtra("id", recomendeddatalist.get(position).getId());
//                                                    startActivity(in);

                                                }

                                            } else {

//                                                Intent in = new Intent(getApplicationContext(), YoutubeVideoHomepageActivity.class);
//                                                in.putExtra("id", recomendeddatalist.get(position).getId());
//                                                startActivity(in);


                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                                            Log.d("Error", t.getMessage());
                                        }
                                    });

                                } else if (recomendeddatalist.get(position).getType().equalsIgnoreCase("mp4_url")) {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, recomendeddatalist.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getLivedetail()));


                                            if (user_id == null) {


                                                Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                in.putExtra("id", recomendeddatalist.get(position).getId());
                                                in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                startActivity(in);

                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (recomendeddatalist.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", recomendeddatalist.get(position).getId());
                                                    in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", recomendeddatalist.get(position).getId());
                                                    in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((recomendeddatalist.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", recomendeddatalist.get(position).getId());
                                                    in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = recomendeddatalist.get(position).getMp4_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((recomendeddatalist.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (recomendeddatalist.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", recomendeddatalist.get(position).getMp4_url());
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    }
                                                } else if (recomendeddatalist.get(position).getAccess().equalsIgnoreCase("ppv") || recomendeddatalist.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", recomendeddatalist.get(position).getId());
                                                    in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    startActivity(in);
                                                } else {

                                                    if (recomendeddatalist.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", recomendeddatalist.get(position).getMp4_url());
                                                        in.putExtra("xtra", "Norent");
                                                        startActivity(in);
                                                    }
                                                }

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                                            Log.d("Error", t.getMessage());
                                        }
                                    });
                                } else if (recomendeddatalist.get(position).getType().equalsIgnoreCase("m3u8_url")) {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, recomendeddatalist.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getLivedetail()));


                                            if (user_id == null) {


                                                if (recomendeddatalist.get(position).getAccess().equalsIgnoreCase("guest")) {
                                                    String videourl = recomendeddatalist.get(position).getM3u8_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                        in.putExtra("xtra", "guest");
                                                        //     in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "guest");
                                                        //in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    }
                                                }


                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (recomendeddatalist.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", recomendeddatalist.get(position).getId());
                                                    in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    in.putExtra("continuee", "0");
                                                    startActivity(in);

                                                } else if (recomendeddatalist.get(position).getAccess().equalsIgnoreCase("guest")) {
                                                    String videourl = recomendeddatalist.get(position).getM3u8_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                        in.putExtra("xtra", "guest");
                                                        //     in.putExtra("continuee","0");
                                                        startActivity(in);

                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "guest");
                                                        //in.putExtra("continuee","0");
                                                        startActivity(in);
                                                    }
                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", recomendeddatalist.get(position).getId());
                                                    in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    in.putExtra("continuee", "0");
                                                    startActivity(in);

                                                } else if ((recomendeddatalist.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", recomendeddatalist.get(position).getId());
                                                    in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    in.putExtra("continuee", "0");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = recomendeddatalist.get(position).getM3u8_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        in.putExtra("continuee", "0");
                                                        startActivity(in);
                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        in.putExtra("continuee", "0");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((recomendeddatalist.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (recomendeddatalist.get(position).getM3u8_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        in.putExtra("continuee", "0");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", recomendeddatalist.get(position).getM3u8_url());
                                                        in.putExtra("xtra", "subscriberented");
                                                        in.putExtra("continuee", "0");
                                                        startActivity(in);

                                                    }
                                                } else if (recomendeddatalist.get(position).getAccess().equalsIgnoreCase("ppv") || recomendeddatalist.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", recomendeddatalist.get(position).getId());
                                                    in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    in.putExtra("continuee", "0");
                                                    startActivity(in);
                                                } else {

                                                    if (recomendeddatalist.get(position).getM3u8_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        in.putExtra("continuee", "0");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", recomendeddatalist.get(position).getM3u8_url());
                                                        in.putExtra("xtra", "Norent");
                                                        in.putExtra("continuee", "0");
                                                        startActivity(in);
                                                    }
                                                }

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                                            Log.d("Error", t.getMessage());
                                        }
                                    });
                                } else {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, recomendeddatalist.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getLivedetail()));


                                            if (user_id == null) {


                                                Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                in.putExtra("id", recomendeddatalist.get(position).getId());
                                                in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                startActivity(in);

                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (recomendeddatalist.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", recomendeddatalist.get(position).getId());
                                                    in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", recomendeddatalist.get(position).getId());
                                                    in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((recomendeddatalist.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", recomendeddatalist.get(position).getId());
                                                    in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = recomendeddatalist.get(position).getVideo_url() + recomendeddatalist.get(position).getPath() + ".m3u8";

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    } else {

                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((recomendeddatalist.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (recomendeddatalist.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", recomendeddatalist.get(position).getVideo_url() + recomendeddatalist.get(position).getPath() + ".m3u8");
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    }
                                                } else if (recomendeddatalist.get(position).getAccess().equalsIgnoreCase("ppv") || recomendeddatalist.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                    in.putExtra("id", recomendeddatalist.get(position).getId());
                                                    in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    startActivity(in);
                                                } else {

                                                    if (recomendeddatalist.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", recomendeddatalist.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getApplicationContext(), HomePageVideoActivity.class);
                                                        in.putExtra("id", recomendeddatalist.get(position).getId());
                                                        in.putExtra("url", recomendeddatalist.get(position).getVideo_url() + recomendeddatalist.get(position).getPath() + ".m3u8");
                                                        in.putExtra("xtra", "Norent");
                                                        startActivity(in);
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
                            }
                        }

                    }
                })
        );

        //  VideoDetails();
        // ThisMayAlsoLikeVideos();

        //   Toast.makeText(getApplicationContext(),""+videoId,Toast.LENGTH_LONG).show();

        Call<JSONResponse> res1 = ApiClient.getInstance1().getApi().getLiveDetail(user_id, videoId);
        res1.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getLivedetail()));
                // moviesubtitlesdata = new ArrayList<>(Arrays.asList(jsonResponse.getmoviesubtitles()));

                shareurl = jsonResponse.getShareurl();

                video_like = jsonResponse.getLike();
                video_dislike = jsonResponse.getDislike();
                if (jsonResponse.getMain_genre() == null||jsonResponse.getMain_genre().isEmpty()) {
                    genere_layout.setVisibility(View.GONE);
                } else {
                    genere_layout.setVisibility(View.VISIBLE);
                    genre_text.setText(jsonResponse.getMain_genre());
                }

                genre2.setText(jsonResponse.getMain_genre());

                nxtvidid = jsonResponse.getVideonext();
                nextvidurl = jsonResponse.getNext_url();
                videoads = jsonResponse.getVideoads();

                String userprofile = movie_detaildata.get(0).getImage_url();


                Picasso.get().load(userprofile).into(videoThumb);
                Picasso.get().load(userprofile).into(trailerImage);
                String next = "<font color='#ff0000'>  View Less</font>";


                final Handler handler223 = new Handler();
                final Runnable Update223 = new Runnable() {
                    public void run() {
                        if (movie_detaildata.get(0).getDescription()=="" ||  movie_detaildata.get(0).getDescription() == null) {
                        }else {

                            if (videotext.getMaxLines() == 2) {

                                if (!(movie_detaildata.get(0).getDescription() == null)) {

                                    if (movie_detaildata.get(0).getDescription().length() <= 160) {
                                        view_more.setVisibility(View.GONE);
                                        videotext.setText(Html.fromHtml(movie_detaildata.get(0).getDescription()));
                                    } else {
                                        view_more.setVisibility(View.VISIBLE);
                                        videotext.setText(Html.fromHtml(movie_detaildata.get(0).getDescription()));
                                    }
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
                    public void run() {handler223.post(Update223);
                    }
                }, 1000, 1000);

                if (movie_detaildata.get(0).getDescription()==null|| movie_detaildata.get(0).getDescription().isEmpty()){
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
                        if (movie_detaildata.get(0).getDescription().length() <= 160){
                            view_more.setVisibility(View.GONE);
                        }else {
                            view_more.setVisibility(View.VISIBLE);
                        }
                        videotext.setMaxLines(2);
                    }
                });


            /*    if (jsonResponse.getWishlist().equalsIgnoreCase("true")) {

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

                if(jsonResponse.getFavorite().equalsIgnoreCase("true"))
                {
                    favlistaddimg.setVisibility(View.VISIBLE);
                    favlistimg.setVisibility(View.GONE);

                }
                else
                {
                    favlistaddimg.setVisibility(View.GONE);
                    favlistimg.setVisibility(View.VISIBLE);
                }

            */
                language1.setText(jsonResponse.getLanguages());


                year.setText(movie_detaildata.get(0).getYear());
                views.setText(movie_detaildata.get(0).getViews() + " Views");
                videotitle1.setText(movie_detaildata.get(0).getTitle());
                imdb.setText(movie_detaildata.get(0).getRating());
                durationandyear.setText(movie_detaildata.get(0).getDuration() + " " + movie_detaildata.get(0).getYear());
               /* textView.setText(movie_detaildata.get(0).getDescription());

                //textView.setShowingChar(200);
                //number of line you want to short
                textView.setShowingLine(2);

                textView.addShowMoreText("show More");
                textView.addShowLessText("show Less");

                textView.setShowMoreColor(Color.WHITE); // or other color
                textView.setShowLessTextColor(Color.WHITE);*/


                if (movie_detaildata.get(0).getLivestream_format().equalsIgnoreCase("m3u_url")) {
                    // urlll = movie_detaildata.get(0).getHls_url();
                } else {

                    urlll = movie_detaildata.get(0).getLivestream_url();
                }


                //    urlll = movie_detaildata.get(0).getVideo_url() + movie_detaildata.get(0).getPath();


                // urlll = movie_detaildata.get(0).getMp4_url();
                String x1 = movie_detaildata.get(0).getTitle();
                String x2 = movie_detaildata.get(0).getLivestream_url();

                downurl.setText(x2);
                downname.setText(x1);
                downimgurl.setText(userprofile);

                getDetails();


                // mInstancee =this;

                if (player == null) {

                } else {
                    videoDurationInSeconds = player.getDuration();


                }

                trailerImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent in = new Intent(getApplicationContext(), TrailerPlayerActivity.class);

                        in.putExtra("id", movie_detaildata.get(0).getId());
                        in.putExtra("url", movie_detaildata.get(0).getTrailer());
                        startActivity(in);
                    }
                });


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


        Call<JSONResponse> alsolikeres = ApiClient.getInstance1().getApi().getAlsolikeVideo(videoId);
        alsolikeres.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();

                if (jsonResponse.getChannelrecomended().length == 0) {

                    norecommanded.setVisibility(View.VISIBLE);
                    thismaylikerecycler.setVisibility(View.GONE);
                    thismayalsolike_layout.setVisibility(View.GONE);
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


        //Toast.makeText(getApplicationContext(),"videocast"+videoId,Toast.LENGTH_LONG).show();

        Call<JSONResponse> videocast = ApiClient.getInstance1().getApi().getVideoCast(videoId);
        videocast.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();


                if (jsonResponse.getCast_details().length == 0) {


                    castandcrewrecycler.setVisibility(View.GONE);
                    castlayout.setVisibility(View.GONE);
                } else {

                    castandcrewrecycler.setVisibility(View.VISIBLE);
                    castlayout.setVisibility(View.VISIBLE);
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
        Call<JSONResponse> callimg = ApiClient.getInstance1().getApi().currencyConverter("United States of America",ppv_price);
        callimg.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                if (!(jsonResponse.getCurrency_Converted().isEmpty())|| jsonResponse.getCurrency_Converted()!=null){

                    String[] curencySplit=jsonResponse.getCurrency_Converted().split(" ");
                    String[] remCurr=curencySplit[1].split("");
                    String totalAmount=remCurr[0]+remCurr[1]+remCurr[2]+remCurr[3];
                    Double addTax= Double.parseDouble(totalAmount)+0.2;
                    paypalamount = String.valueOf(addTax);
                    PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paypalamount)), "USD", "Live video on rent", PayPalPayment.PAYMENT_INTENT_SALE);
                    Intent intent = new Intent(HomePageVideoActivityNew.this, PaymentActivity.class);
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
                    //Toast.makeText(HomePageVideoActivityNew.this, "Payment " + state + "\n with payment id is " + payID, Toast.LENGTH_LONG).show();
                    Api.getClient().getAddPayperViewLive(user_id, idd.getText().toString(), payID, "PayPal",ppv_price,"Android", new Callback<Addpayperview>() {
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
                            Toast.makeText(getApplicationContext(), "Error: "+error.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });



                } catch (JSONException e) {
                    Toast.makeText(HomePageVideoActivityNew.this, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == Activity.RESULT_CANCELED) {
                Toast.makeText(HomePageVideoActivityNew.this, "Payment Canceled", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(HomePageVideoActivityNew.this, "Invalid Payment", Toast.LENGTH_LONG).show();
        } else if (requestCode == Activity.RESULT_CANCELED) {
            Toast.makeText(HomePageVideoActivityNew.this, "Payment Canceled", Toast.LENGTH_LONG).show();
        }
    }

    private void transactionLog(){

        Log.w("runmawwi","ui: "+user_id+" price: "+ppv_price+" vi: "+videoId);
        Call<JSONResponse1> trac_api=ApiClient.getInstance1().getApi().transactioLog(user_id,ppv_price,"ppv", "razorpay","android","","",videoId,"","");
        trac_api.enqueue(new retrofit2.Callback<JSONResponse1>() {
            @Override
            public void onResponse(Call<JSONResponse1> call, retrofit2.Response<JSONResponse1> response) {
            }
            @Override
            public void onFailure(Call<JSONResponse1> call, Throwable throwable) {
            }
        });
    }

    private void createOrder() {
        transactionLog();
        String credentials = RAZORPAY_KEY_ID + ":" + RAZORPAY_KEY_SECRET;
        String authHeader = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        int amt = Integer.parseInt(ppv_price);
        String priceTotal = String.valueOf(amt * 100);

        Order order = new Order(priceTotal, "INR", "order_rcptid_12", "1");
        Call<JSONResponse> list = ApiClient.getInstance1().getApi2().createOrder("Authorization" + authHeader, order);
        list.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                if (response.code() == 200) {
                    orderId = response.body().getId();
                    startPaymentWithQuality(response.body().getId());
                    Log.w("runmawii", "amt: " + ppv_price);
                    Log.w("runmawii", "id: " + response.body().getId());
                } else {
                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.w("runmawii", "Error: " + response.message());
                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable throwable) {
                Log.w("runmawii", "api failed: " + throwable.getMessage());
            }
        });

    }

    private void startPaymentWithQuality(String id) {

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
            String payment = ppv_price;

            //amount is in paise so please multiple it by 100
            //Payment failed Invalid amount (should be passed in integer paise. Minimum value is 100 paise, i.e. ₹ 1)
            double total = Double.parseDouble(payment);
            Log.w("runmawii", "p: " + payment + " d: " + total + " t: " + (total * 100));
            total = total * 100;
            // p: 100 d: 100.0 t: 10000.0

            options.put("amount", total);
            JSONObject preFill = new JSONObject();
            preFill.put("email", "");
            preFill.put("contact", "");
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            //Toast.makeText(activity, "Error in payment: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            Log.w("runmawii", "Exception: " + e);
        }

    }

    private void startPayment() {

        final Activity activity = this;
        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Runmawi");
            options.put("description", "Live On Rent");
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
        Log.w("runmawii", "success: " + s);
        fetchPaymentDetails(s);

        /*Api.getClient().getAddPayperViewLive(user_id, idd.getText().toString(), "", "razorpay",ppv_price,"Android", new Callback<Addpayperview>() {

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
        });*/

    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            Api.getClient().getAddPayperViewLiveNew(user_id, idd.getText().toString(), orderId, "failed", "razorpay",ppv_price,"Android", new Callback<Addpayperview>() {

                @Override
                public void success(Addpayperview addpayperview1, Response response) {

                    addpayperview = addpayperview1;
                    if (addpayperview.getStatus().equalsIgnoreCase("true")) {
                        changeAccess();
                    } else if (addpayperview.getStatus().equalsIgnoreCase("false")) {
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

                Api.getClient().getAddPayperViewLiveNew(user_id, idd.getText().toString(), orderId, response.body().getStatus(), "razorpay",ppv_price,"Android", new Callback<Addpayperview>() {

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
        //playerView.setErrorMessageProvider(new PlayerErrorMessageProvider());
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
                        ScreenUtils.convertDIPToPixels(HomePageVideoActivityNew.this, playerHeight)));


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

//        downloadTracker.addListener(this);


        if (Util.SDK_INT > 23) {
            //  initializePlayer();
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
//        downloadTracker.removeListener(this);
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
        //outState.putParcelable(KEY_TRACK_SELECTOR_PARAMETERS, trackSelectorParameters);
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

                /*MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
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
                                    ScreenUtils.convertDIPToPixels(HomePageVideoActivityNew.this, playerHeight)));


                    frameLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                    //   imgFullScreenEnterExit.setImageResource(R.drawable.exo_controls_fullscreen_enter);
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
                    // imgFullScreenEnterExit.setImageResource(R.drawable.exo_controls_fullscreen_exit);
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

//                downloadManager.addDownload(downloadTracker.getDownloadRequest(Uri.parse(urlll)), Download.STATE_STOPPED);

//                DownloadService.sendSetStopReason(
//                        HomePageVideoActivity.this,
//                        DemoDownloadService.class,
//                        downloadTracker.getDownloadRequest(Uri.parse(urlll)).id,
//                        Download.STATE_STOPPED,
//                        /* foreground= */ false);

                break;

            case DOWNLOAD_RESUME:

//                downloadManager.addDownload(downloadTracker.getDownloadRequest(Uri.parse(urlll)), Download.STOP_REASON_NONE);
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
            pDialog = new ProgressDialog(HomePageVideoActivityNew.this);
            pDialog.setTitle(null);
            pDialog.setCancelable(false);
            pDialog.setMessage("Preparing Download Options...");
            pDialog.show();
        }


      /*  DownloadHelper downloadHelper = DownloadHelper.forDash(HomePageVideoActivityNew.this, Uri.parse(urlll), dataSourceFactory, new DefaultRenderersFactory(HomePageVideoActivityNew.this));


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
                Toast.makeText(getApplicationContext(), "failed" + e, Toast.LENGTH_LONG).show();
                pDialog.dismiss();

            }
        });*/
    }

    private void showDownloadOptionsDialog(DownloadHelper helper, List<TrackKey> trackKeyss) {

        if (helper == null) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(HomePageVideoActivityNew.this);
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
                HomePageVideoActivityNew.this, // Context
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

  /*  @Override
    public void preparePlayback() {
        initializePlayer();
    }*/

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
//        if (user_id == null || user_role == null) {
//
//            //player = ExoPlayerFactory.newSimpleInstance(/* context= */ HomePageVideoActivityNew.this, renderersFactory, trackSelector, defaultLoadControl);
//            player.addListener(new PlayerEventListener());
//            player.setPlayWhenReady(startAutoPlay);
//            player.addAnalyticsListener(new EventLogger(trackSelector));
//            playerView.setPlayer(player);
//            playerView.setPlaybackPreparer(this);
//
//
//            exoButtonPrepareDecision();
//            updateButtonVisibilities();
//
//            initBwd();
//            initFwd();
//
//        } else if (user_role != null && user_role.equalsIgnoreCase("registered")) {
//
//            if (rented.equalsIgnoreCase("rentted")) {
//
//                playerView.setVisibility(View.INVISIBLE);
//                videoThumb.setVisibility(View.VISIBLE);
//              //  player = ExoPlayerFactory.newSimpleInstance(/* context= */ HomePageVideoActivityNew.this, renderersFactory, trackSelector, defaultLoadControl);
//             //   player.addListener(new PlayerEventListener());
//                player.setPlayWhenReady(false);
//                player.addAnalyticsListener(new EventLogger(trackSelector));
//                playerView.setPlayer(player);
//              //  playerView.setPlaybackPreparer(this);
//
//
//
//                exoButtonPrepareDecision();
//
//                updateButtonVisibilities();
//                initBwd();
//                initFwd();
//
//            } else {
//
//              ///  player = ExoPlayerFactory.newSimpleInstance(/* context= */ HomePageVideoActivityNew.this, renderersFactory, trackSelector, defaultLoadControl);
//             //   player.addListener(new PlayerEventListener());
//                player.setPlayWhenReady(startAutoPlay);
//                player.addAnalyticsListener(new EventLogger(trackSelector));
//                playerView.setPlayer(player);
//              //  playerView.setPlaybackPreparer(this);
//
//
//
//                exoButtonPrepareDecision();
//                updateButtonVisibilities();
//
//                initBwd();
//                initFwd();
//            }
//        } else {
//
//            if (rented.equalsIgnoreCase("")) {
//
//                playerView.setVisibility(View.INVISIBLE);
//                videoThumb.setVisibility(View.VISIBLE);
//            //    player = ExoPlayerFactory.newSimpleInstance(/* context= */ HomePageVideoActivityNew.this, renderersFactory, trackSelector, defaultLoadControl);
//              //  player.addListener(new PlayerEventListener());
//                player.setPlayWhenReady(false);
//                player.addAnalyticsListener(new EventLogger(trackSelector));
//                playerView.setPlayer(player);
//             //   playerView.setPlaybackPreparer(this);
//
//
//
//                exoButtonPrepareDecision();
//                updateButtonVisibilities();
//                initBwd();
//                initFwd();
//
//            } else {
//
//
//                playerView.setVisibility(View.INVISIBLE);
//                videoThumb.setVisibility(View.VISIBLE);
//              //  player = ExoPlayerFactory.newSimpleInstance(/* context= */ HomePageVideoActivityNew.this, renderersFactory, trackSelector, defaultLoadControl);
//              //  player.addListener(new PlayerEventListener());
//                player.setPlayWhenReady(false);
//                player.addAnalyticsListener(new EventLogger(trackSelector));
//                playerView.setPlayer(player);
//               // playerView.setPlaybackPreparer(this);
//
//            }
//
//        }
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
                //return new DashMediaSource.Factory(dataSourceFactory)
                //        .createMediaSource(uri);
            case C.TYPE_SS:
                //  return new SsMediaSource.Factory(dataSourceFactory)
                //       .createMediaSource(uri);
            case C.TYPE_HLS:
                //  return new HlsMediaSource.Factory(dataSourceFactory)
                //    .createMediaSource(uri);
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
                            ScreenUtils.convertDIPToPixels(HomePageVideoActivityNew.this, playerHeight)));


            frameLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

            // imgFullScreenEnterExit.setImageResource(R.drawable.exo_controls_fullscreen_enter);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            isScreenLandscape = false;
            hide();


        } else {
            finish();

        }

    }


    //    @Override
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

                    exoProgressbar.setVisibility(View.GONE);
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

      //  @Override
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

      //  @Override
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

    public static synchronized HomePageVideoActivityNew getInstance() {
        return mInstancee;
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
            Call<JSONResponse> pay_api = ApiClient.getInstance1().getApi().paydunyaLiveRent(user_id, videoId,"Android");
            pay_api.enqueue(new retrofit2.Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                    paydunya_dialog.cancel();
                    JSONResponse jsonResponse= response.body();
                    if (jsonResponse.getStatus().equalsIgnoreCase("true")){
                        Toast.makeText(HomePageVideoActivityNew.this, ""+jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        changeAccess();
                    }else {
                        Toast.makeText(HomePageVideoActivityNew.this, ""+jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void changeAccess(){
        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getLiveDetail(user_id, videoId);
        res.enqueue(new retrofit2.Callback<JSONResponse>() {

            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getLivedetail()));

                if (user_role.equalsIgnoreCase("admin")) {
                    paynow.setVisibility(View.GONE);
                    rent.setVisibility(View.GONE);
                    playnow.setVisibility(View.VISIBLE);
                }else if (movie_detaildata.get(0).getAccess().equalsIgnoreCase("ppv")) {

                    if (jsonResponse.getPpv_video_status().equalsIgnoreCase("can_view")){

                        paynow.setVisibility(View.GONE);
                        rent.setVisibility(View.GONE);
                        playnow.setVisibility(View.VISIBLE);
                    }else {
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

                        paynow.setVisibility(View.GONE);
                        rent.setVisibility(View.VISIBLE);
                        playnow.setVisibility(View.GONE);
                    }

                } else if (movie_detaildata.get(0).getAccess().equalsIgnoreCase("subscriber")) {

                    if (user_role.equalsIgnoreCase("subscriber")) {

                        paynow.setVisibility(View.GONE);
                        rent.setVisibility(View.GONE);
                        playnow.setVisibility(View.VISIBLE);
                    } else {

                        paynow.setVisibility(View.VISIBLE);
                        rent.setVisibility(View.GONE);
                        playnow.setVisibility(View.GONE);
                    }
                } else {

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