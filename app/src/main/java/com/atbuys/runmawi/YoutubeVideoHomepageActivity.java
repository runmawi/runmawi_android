//package com.atbuys.runmawi;
//
//import static com.atbuys.runmawi.Connectivity.getNetworkInfo;
//
//import android.accessibilityservice.AccessibilityService;
//import android.annotation.SuppressLint;
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.ActivityInfo;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.telephony.TelephonyManager;
//import android.util.Log;
//import android.util.Pair;
//import android.view.Display;
//import android.view.MotionEvent;
//import android.view.Surface;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.CompoundButton;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.Switch;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.app.AppCompatDelegate;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.exoplayer2.C;
//import com.google.android.exoplayer2.ExoPlaybackException;
//import com.google.android.exoplayer2.PlaybackParameters;
//import com.google.android.exoplayer2.SimpleExoPlayer;
//import com.google.android.exoplayer2.extractor.ExtractorsFactory;
//import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
//import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
//import com.google.android.exoplayer2.offline.DownloadHelper;
//import com.google.android.exoplayer2.offline.DownloadManager;
//import com.google.android.exoplayer2.offline.DownloadService;
//import com.google.android.exoplayer2.source.BehindLiveWindowException;
//import com.google.android.exoplayer2.source.ExtractorMediaSource;
//import com.google.android.exoplayer2.source.MediaSource;
//import com.google.android.exoplayer2.source.MergingMediaSource;
//import com.google.android.exoplayer2.source.TrackGroupArray;
//import com.google.android.exoplayer2.source.ads.AdsLoader;
//import com.google.android.exoplayer2.source.dash.DashMediaSource;
//import com.google.android.exoplayer2.source.hls.HlsMediaSource;
//import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
//import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
//import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
//import com.google.android.exoplayer2.ui.DefaultTimeBar;
//import com.google.android.exoplayer2.ui.PlayerView;
//import com.google.android.exoplayer2.upstream.BandwidthMeter;
//import com.google.android.exoplayer2.upstream.DataSource;
//import com.google.android.exoplayer2.util.ErrorMessageProvider;
//import com.google.android.exoplayer2.util.Util;
//import com.squareup.picasso.Picasso;
//
//import java.net.CookieHandler;
//import java.net.CookieManager;
//import java.net.CookiePolicy;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Formatter;
//import java.util.List;
//import java.util.Locale;
//
//import retrofit.Callback;
//import retrofit.RetrofitError;
//import retrofit2.Call;
//
//public class YoutubeVideoHomepageActivity extends AppCompatActivity implements View.OnClickListener {
//    private static final int playerHeight = 250;
//    ProgressDialog pDialog;
//    protected static final CookieManager DEFAULT_COOKIE_MANAGER;
//    private static final boolean AUTO_HIDE = true;
//    private static final int AUTO_HIDE_DELAY_MILLIS = 2000;
//    private static final int UI_ANIMATION_DELAY = 300;
//    // Saved instance state keys.
//    private static final String KEY_TRACK_SELECTOR_PARAMETERS = "track_selector_parameters";
//    private static final String KEY_WINDOW = "window";
//    private static final String KEY_POSITION = "position";
//    private static final String KEY_AUTO_PLAY = "auto_play";
//
//
//    MediaSource subsource;
//    MergingMediaSource mergedSource;
//
//    BandwidthMeter bandwidthMeter;
//
//    ImageView cast;
//    TextView cast1;
//
//
//    static {
//        DEFAULT_COOKIE_MANAGER = new CookieManager();
//        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
//    }
//
//    private final Handler mHideHandler = new Handler();
//    private final Runnable mShowRunnable = new Runnable() {
//        @Override
//        public void run() {
//            // Delayed display of UI elements
//            ActionBar actionBar = getSupportActionBar();
//            if (actionBar != null) {
//                actionBar.show();
//            }
//
//        }
//    };
//    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            if (AUTO_HIDE) {
//                delayedHide(AUTO_HIDE_DELAY_MILLIS);
//            }
//            return false;
//        }
//    };
//    int tapCount = 1;
//    LinearLayout llParentContainer;
//    Boolean isScreenLandscape = false;
//    List<TrackKey> trackKeys = new ArrayList<>();
//    List<String> optionsToDownload = new ArrayList<String>();
////    TrackKey trackKeyDownload;
//DefaultTrackSelector.Parameters qualityParams;
//    private boolean mVisible;
//    private final Runnable mHideRunnable = new Runnable() {
//        @Override
//        public void run() {
//            hide();
//        }
//    };
//    private PlayerView playerView;
//    private DataSource.Factory dataSourceFactory;
//    private SimpleExoPlayer player;
//    private MediaSource mediaSource;
//    private DefaultTrackSelector trackSelector;
//    private boolean isShowingTrackSelectionDialog;
//    private DefaultTrackSelector.Parameters trackSelectorParameters;
//    private TrackGroupArray lastSeenTrackGroupArray;
//    private TextView tvPlaybackSpeed, tvPlaybackSpeedSymbol;
//    private boolean startAutoPlay;
//    private int startWindow;
//    // Fields used only for ad playback. The ads loader is loaded via reflection.
//    private long startPosition;
//    private AdsLoader adsLoader;
//    private Uri loadedAdTagUri;
//    private FrameLayout frameLayoutMain;
//    private ImageView imgBwd;
//    private ImageView exoPlay;
//    private ImageView exoPause;
//    private ImageView imgFwd,imgBackPlayer,skipintro;;
//    private TextView tvPlayerCurrentTime;
//    private DefaultTimeBar exoTimebar;
//    private ProgressBar exoProgressbar;
//    private TextView tvPlayerEndTime;
//    private ImageView imgSetting;
//    private ImageView imgFullScreenEnterExit;
//    private LinearLayout playnow,paynow,userfeatuerlayout;
//    private ImageView coverimage;
//
//    private ImageView subtitle_off,subtitle_on;
//
//    private StringBuilder mFormatBuilder;
//    private Formatter mFormatter;
//    private DownloadTracker downloadTracker;
//    private DownloadManager downloadManager;
//    private DownloadHelper myDownloadHelper;
//    private LinearLayout llDownloadContainer;
//    private LinearLayout llDownloadVideo;
//    private ImageView imgDownloadState;
//    private TextView tvDownloadState;
//    private ProgressBar progressBarPercentage;
//    private String videoId;
//    private String videoName;
//    private String videoUrl;
//    /*private TextView cast_name;*/
//    private long videoDurationInSeconds;
//    private Runnable runnableCode;
//    private Handler handler;
//    private TextView comments;
//    private EditText commenttext;
//    private ImageView commentsend;
//    private LinearLayout commentlayout;
//    private RecyclerView usercommentrecycler;
//    private TextView description;
//
//    private int currentWindow = 0;
//    private long playbackPosition = 0;
//    private Switch autoplayswitch;
//    private TextView views;
//
//    private ImageView imageyoutube;
//
//
//  //  private String videoUrl  ="https://bitmovin-a.akamaihd.net/content/playhouse-vr/mpds/105560.mpd";
//    private AccessibilityService context;
//    public static Dialog dialog;
//    private ImageView like,liked,dislikee,dislikeed;
//    LinearLayout unlikelayout,undislikelayout;
//    AddComment addComment;
//
//
//
//
//    String nxtvidid,nextvidurl;
//
//    LinearLayout watchlist,watchlater,hidelayout1,likee,dislike,favouriteLayout;
//    ImageView watchlistimg,watchlistaddedimg,watchlaterimg,watchlateraddedimg,favlistimg,favlistaddimg;
//   // ShowMoreTextView videotext;
//    LinearLayout share;
//    TextView videotitle1,language1,genre2,duration,year,getCast1;
//    private String shareurl,user_id,user_role,auto_play;
//
//
//
//    private RecyclerView.LayoutManager layoutManager,layoutManager1,manager;
//    private ArrayList<movies> moviesdata;
//    private ArrayList<channelrecomended> recomendeddatalist;
//    private ArrayList<genre> genredata;
//    private ArrayList<videodetail> movie_detaildata;
//    private ArrayList<videossubtitles> moviesubtitlesdata;
//    private ArrayList<user_comments> usercommentslist;
//    showwishlist showwish;
//    private ArrayList<movieresolution> movieresolutiondata;
//    private ArrayList<video_cast> castdetails;
//
//    private List<videossubtitles> statelistdata;
//   // public  String urlll ="";
//    String type="channel";
//    TextView norecommanded;
//
//    RecyclerView thismaylikerecycler;
//    RecyclerView castandcrewrecycler;
//    ExtractorsFactory extractorsFactory;
//    ThismaylikeAdopter thismaylikeadopter;
//    castandcrewadapter castandcrewadapter;
//
//    UserCommentsAdopter userCommentsAdopter;
//    private long position;
//   // private Object cast_details;
//
//
//    private static boolean isBehindLiveWindow(ExoPlaybackException e) {
//        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
//            return false;
//        }
//        Throwable cause = e.getSourceException();
//        while (cause != null) {
//            if (cause instanceof BehindLiveWindowException) {
//                return true;
//            }
//            cause = cause.getCause();
//        }
//        return false;
//    }
//
//    @SuppressLint("WrongViewCast")
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//
//        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
//
//
//            setTheme(R.style.darktheme);
//
//        }
//
//        else {
//
//
//            setTheme(R.style.AppTheme);
//        }
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
////        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
//        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
//
//        dataSourceFactory = buildDataSourceFactory();
//
//
//
//        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
//            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
//        }
//
//        hideStatusBar();
//
//        setContentView(R.layout.activity_youtube_home);
//
//        Intent in=getIntent();
//        videoId =in.getStringExtra("id");
//        //urlll=in.getStringExtra("url");
//        // suburl = in.getStringExtra("suburl");
//
//
//        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
//
//        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
//        user_id = prefs.getString(sharedpreferences.user_id,null );
//        user_role = prefs.getString(sharedpreferences.role,null );
//        auto_play = prefs.getString(sharedpreferences.autoplay,null);
//
//
//
//        if (savedInstanceState != null) {
//            trackSelectorParameters = savedInstanceState.getParcelable(KEY_TRACK_SELECTOR_PARAMETERS);
//            startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
//            startWindow = savedInstanceState.getInt(KEY_WINDOW);
//            startPosition = savedInstanceState.getLong(KEY_POSITION);
//        } else {
//            trackSelectorParameters = new DefaultTrackSelector.ParametersBuilder().build();
//
//        }
//
//        AdaptiveExoplayer application = (AdaptiveExoplayer) getApplication();
//        downloadTracker = application.getDownloadTracker();
//        downloadManager = application.getDownloadManager();
//
//
//        // Start the download service if it should be running but it's not currently.
//        // Starting the service in the foreground causes notification flicker if there is no scheduled
//        // action. Starting it in the background throws an exception if the app is in the background too
//        // (e.g. if device screen is locked).
//
//        try {
//            DownloadService.start(this, DemoDownloadService.class);
//        } catch (IllegalStateException e) {
//            DownloadService.startForeground(this, DemoDownloadService.class);
//        }
//
////        Requirements requirements = new Requirements(Requirements.NETWORK_UNMETERED);
////        DownloadService.sendSetRequirements(
////                OnlinePlayerActivity.this,
////                DemoDownloadService.class,
////                requirements,
////                /* foreground= */ false);
//
//
//
//
//
//        //videotext=(ShowMoreTextView) findViewById(R.id.videotext);
//        watchlist=(LinearLayout)findViewById(R.id.watchlist);
//        watchlater=(LinearLayout)findViewById(R.id.watchlater);
//        watchlistimg=(ImageView)findViewById(R.id.watchlistimg);
//        comments = (TextView) findViewById(R.id.comments);
//        commentsend = (ImageView) findViewById(R.id.sendcomment);
//        commenttext = (EditText) findViewById(R.id.commenttext);
//        commentlayout = (LinearLayout)  findViewById(R.id.commentlayout);
//        usercommentrecycler = (RecyclerView) findViewById(R.id.usercomments);
//        playnow = (LinearLayout) findViewById(R.id.play_now);
//        paynow =(LinearLayout) findViewById(R.id.paynow);
//        coverimage = (ImageView) findViewById(R.id.coverimage);
//        userfeatuerlayout = (LinearLayout) findViewById(R.id.userfeatuerlayout);
//        autoplayswitch = (Switch) findViewById(R.id.autoplay);
//        imageyoutube = (ImageView) findViewById(R.id.imageyoutube);
//        views = (TextView) findViewById(R.id.view);
//
//
//        likee = (LinearLayout) findViewById(R.id.likeed);
//        dislike = (LinearLayout) findViewById(R.id.dislike);
//        unlikelayout = (LinearLayout) findViewById(R.id.unlikelayout);
//        undislikelayout = (LinearLayout) findViewById(R.id.undislikelayout);
//
//
//        like = (ImageView) findViewById(R.id.like);
//        liked = (ImageView) findViewById(R.id.liked);
//        dislikee = (ImageView) findViewById(R.id.dislikee);
//        dislikeed = (ImageView) findViewById(R.id.dislikeed);
//
//
//        watchlistaddedimg=(ImageView)findViewById(R.id.watchlistaddimg);
//        watchlaterimg=(ImageView)findViewById(R.id.watchlateradd);
//        watchlateraddedimg=(ImageView)findViewById(R.id.watchlateraddedimg);
//        favouriteLayout = (LinearLayout) findViewById(R.id.favlist);
//        favlistimg = (ImageView) findViewById(R.id.favlistimg);
//        favlistaddimg = (ImageView) findViewById(R.id.favlistaddimg);
//
//        share=(LinearLayout)findViewById(R.id.share);
//        norecommanded=(TextView) findViewById(R.id.norecommanded);
//        cast =(ImageView)findViewById(R.id.cast);
//        genre2=(TextView)findViewById(R.id.genre);
//   /*     cast_name=(TextView)findViewById(R.id.cast_name);*/
//        language1=(TextView)findViewById(R.id.language);
//        year=(TextView)findViewById(R.id.movieyear);
//        duration=(TextView)findViewById(R.id.videoduration);
//        videotitle1=(TextView)findViewById(R.id.videotitle);
//        moviesdata = new ArrayList<movies>();
//        genredata = new ArrayList<genre>();
//        movieresolutiondata = new ArrayList<movieresolution>();
//        movie_detaildata = new ArrayList<videodetail>();
//        moviesubtitlesdata = new ArrayList<videossubtitles>();
//        recomendeddatalist=new ArrayList<channelrecomended>();
//        usercommentslist = new ArrayList<user_comments>();
//        castdetails=new ArrayList<video_cast>();
//
//
//        if (user_role == null)
//        {
//
//        }
//
//       else  if(user_role.equalsIgnoreCase("registered"))
//        {
//
//            paynow.setVisibility(View.VISIBLE);
//            playnow.setVisibility(View.GONE);
//            //llDownloadVideo.setVisibility(View.GONE);
//            comments.setVisibility(View.GONE);
//            userfeatuerlayout.setVisibility(View.GONE);
//
//        }
//        else
//        {
//
//            paynow.setVisibility(View.GONE);
//            playnow.setVisibility(View.VISIBLE);
//            userfeatuerlayout.setVisibility(View.VISIBLE);
//            comments.setVisibility(View.VISIBLE);
//
//        }
//
//        playnow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                   Intent in=new Intent(getApplicationContext(), YoutubeActivity.class);
//                   in.putExtra("id",videoId);
//                   startActivity(in);
//            }
//        });
//
//
//        paynow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent in = new Intent(getApplicationContext(), SubscribeActivity.class);
//                startActivity(in);
//
//            }
//        });
//
//        thismaylikeadopter = new ThismaylikeAdopter(recomendeddatalist, this);
//        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//
//        thismaylikerecycler = (RecyclerView) findViewById(R.id.thismayalsolike);
//
//
//        userCommentsAdopter = new UserCommentsAdopter(usercommentslist, this);
//        layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
//
//
//        thismaylikerecycler.setHasFixedSize(true);
//        thismaylikerecycler.setLayoutManager(layoutManager);
//        thismaylikerecycler.setAdapter(thismaylikeadopter);
//
//        castandcrewadapter = new castandcrewadapter(castdetails, this);
//        manager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
//        castandcrewrecycler = (RecyclerView) findViewById(R.id.castandcrew);
//        castandcrewrecycler.setHasFixedSize(true);
//        castandcrewrecycler.setLayoutManager(manager);
//        castandcrewrecycler.setAdapter(castandcrewadapter);
//
//
//        usercommentrecycler.setHasFixedSize(true);
//        usercommentrecycler.setLayoutManager(layoutManager1);
//        usercommentrecycler.setAdapter(thismaylikeadopter);
//
//
//
//
//
//        autoplayswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//
//
//                    SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
//                    editor.putString(sharedpreferences.autoplay, String.valueOf(1));
//                    editor.apply();
//                    editor.commit();
//
//                }
//                else
//                {
//
//                    SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
//                    editor.putString(sharedpreferences.autoplay, String.valueOf(0));
//                    editor.apply();
//                    editor.commit();
//
//
//                }
//            }
//        });
//
//
//
//        if(auto_play == null)
//        {
//            autoplayswitch.setChecked(false);
//        }
//        else if(auto_play.equalsIgnoreCase("1"))
//        {
//
//            autoplayswitch.setChecked(true);
//        }
//
//        else
//        {
//            autoplayswitch.setChecked(false);
//        }
///*
//        comments.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(commentlayout.getVisibility()== View.VISIBLE)
//                {
//
//                    commentlayout.setVisibility(View.GONE);
//                }
//                else
//                {
//
//                    commentlayout.setVisibility(View.VISIBLE);
//
//                    Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getUsercomments(videoId);
//                    movieres.enqueue(new retrofit2.Callback<JSONResponse>() {
//                        @Override
//                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
//
//                            JSONResponse jsonResponse = response.body();
//
//                            if (jsonResponse.getUser_comments().length == 0) {
//
//
//                            }
//
//                            else {
//
//                                usercommentslist = new ArrayList<>(Arrays.asList(jsonResponse.getUser_comments()));
//                                userCommentsAdopter = new UserCommentsAdopter(usercommentslist);
//                                usercommentrecycler.setAdapter(userCommentsAdopter);
//
//                            }
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<JSONResponse> call, Throwable t) {
//                            Log.d("Error", t.getMessage());
//                        }
//                    });
//
//                }
//
//            }
//        });
//*/
//
//
///*
//        commentsend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String comment = commenttext.getText().toString();
//
//
//                Api.getClient().getAddcomment(user_id,videoId,comment, new Callback<AddComment>() {
//
//                    @Override
//                    public void success(AddComment addComment, retrofit.client.Response response) {
//
//                        addComment = addComment;
//
//                        if(addComment.getStatus().equalsIgnoreCase("true")) {
//
//                            Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getUsercomments(videoId);
//                            movieres.enqueue(new retrofit2.Callback<JSONResponse>() {
//                                @Override
//                                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
//
//                                    JSONResponse jsonResponse = response.body();
//
//
//                                    commenttext.setText("");
//
//                                        usercommentslist = new ArrayList<>(Arrays.asList(jsonResponse.getUser_comments()));
//                                        userCommentsAdopter = new UserCommentsAdopter(usercommentslist);
//                                        usercommentrecycler.setAdapter(userCommentsAdopter);
//
//
//                                }
//
//                                @Override
//                                public void onFailure(Call<JSONResponse> call, Throwable t) {
//                                    Log.d("Error", t.getMessage());
//                                }
//                            });
//
//                        }
//                        else {
//
//                            Toast.makeText(getApplicationContext(),""+addComment.getMessage(), Toast.LENGTH_LONG).show();
//
//                        }
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//
//                    }});
//
//
//
//            }
//        });
//*/
//
//
//
//        share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                Intent myIntent = new Intent(Intent.ACTION_SEND);
//                myIntent.setType("text/plain");
//                String shareBody = "I am watching videos";
//                String shareSub = shareurl;
//                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
//                myIntent.putExtra(Intent.EXTRA_TEXT, shareSub);
//                startActivity(Intent.createChooser(myIntent, "Share using"));
//
//
//            }
//        });
//
//
//
//        favouriteLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                {
//
//                    Api.getClient().getFaourite(user_id, videoId, new Callback<Addtofavouritemovie>() {
//
//                        @Override
//                        public void success(Addtofavouritemovie addwishmovie, retrofit.client.Response response) {
//
//                            addwishmovie = addwishmovie;
//                            if (addwishmovie.getStatus().equalsIgnoreCase("true")) {
//
//                                favlistaddimg.setVisibility(View.VISIBLE);
//                                favlistimg.setVisibility(View.GONE);
//
//                                Toast.makeText(getApplicationContext(), "" + addwishmovie.getMessage(), Toast.LENGTH_LONG).show();
//                            } else if (addwishmovie.getStatus().equalsIgnoreCase("false")) {
//
//                                favlistaddimg.setVisibility(View.GONE);
//                                favlistimg.setVisibility(View.VISIBLE);
//
//                                Toast.makeText(getApplicationContext(), "" + addwishmovie.getMessage(), Toast.LENGTH_LONG).show();
//                            } else {
//                                Toast.makeText(getApplicationContext(), "You are not registered user", Toast.LENGTH_LONG).show();
//
//                            }
//
//                        }
//
//                        @Override
//                        public void failure(RetrofitError error) {
//
//                            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
//
//
//            }
//        });
//
//
//        watchlater.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                {
//
//                    Api.getClient().getAddwatchlater(user_id, videoId,type, new Callback<Addtowishlistmovie>() {
//
//                        @Override
//                        public void success(Addtowishlistmovie addwishmovie, retrofit.client.Response response) {
//
//                            addwishmovie=addwishmovie;
//                            if (addwishmovie.getStatus().equalsIgnoreCase("true")) {
//
//                                watchlateraddedimg.setVisibility(View.VISIBLE);
//                                watchlaterimg.setVisibility(View.GONE);
//
//                                Toast.makeText(getApplicationContext(),""+addwishmovie.getMessage(), Toast.LENGTH_LONG).show();
//                            }
//
//                            else if (addwishmovie.getStatus().equalsIgnoreCase("false"))
//                            {
//
//                                watchlateraddedimg.setVisibility(View.GONE);
//                                watchlaterimg.setVisibility(View.VISIBLE);
//
//                                Toast.makeText(getApplicationContext(), ""+addwishmovie.getMessage(), Toast.LENGTH_LONG).show();
//                            }
//                            else
//                            {
//                                Toast.makeText(getApplicationContext(),"You are not registered user", Toast.LENGTH_LONG).show();
//
//                            }
//
//                        }
//
//                        @Override
//                        public void failure(RetrofitError error) {
//
//                            Toast.makeText(getApplicationContext(),"", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
//
//
//            }
//        });
//
//
//
//        watchlist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                {
//
//                    Api.getClient().getAddWishlistMovie(user_id, videoId,type, new Callback<Addtowishlistmovie>() {
//
//                        @Override
//                        public void success(Addtowishlistmovie addwishmovie, retrofit.client.Response response) {
//
//                            addwishmovie=addwishmovie;
//                            if (addwishmovie.getStatus().equalsIgnoreCase("true")) {
//
//                                watchlistaddedimg.setVisibility(View.VISIBLE);
//                                watchlistimg.setVisibility(View.GONE);
//
//                                Toast.makeText(getApplicationContext(),""+addwishmovie.getMessage(), Toast.LENGTH_LONG).show();
//                            }
//
//                            else if (addwishmovie.getStatus().equalsIgnoreCase("false"))
//                            {
//
//                                watchlistaddedimg.setVisibility(View.GONE);
//                                watchlistimg.setVisibility(View.VISIBLE);
//
//                                Toast.makeText(getApplicationContext(), ""+addwishmovie.getMessage(), Toast.LENGTH_LONG).show();
//                            }
//                            else
//                            {
//                                Toast.makeText(getApplicationContext(),"You are not registered user", Toast.LENGTH_LONG).show();
//
//                            }
//
//                        }
//
//                        @Override
//                        public void failure(RetrofitError error) {
//
//                            Toast.makeText(getApplicationContext(),"sd", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
//
//
//            }
//        });
//
//
//
//        likee.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                {
//
//                    Api.getClient().getlikeandunlike(user_id, videoId,"1", new Callback<likeandunlike>() {
//
//                        @Override
//                        public void success(likeandunlike addwishmovie, retrofit.client.Response response) {
//
//                            addwishmovie=addwishmovie;
//                            if (addwishmovie.getLiked().equalsIgnoreCase("1")) {
//
//                                unlikelayout.setVisibility(View.VISIBLE);
//                                likee.setVisibility(View.GONE);
//
//                                Toast.makeText(getApplicationContext(),"You liked video", Toast.LENGTH_LONG).show();
//                            }
//
//                        }
//
//                        @Override
//                        public void failure(RetrofitError error) {
//
//                            Toast.makeText(getApplicationContext(),"sd", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
//
//
//            }
//        });
//
//
//
//        unlikelayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                {
//
//                    Api.getClient().getlikeandunlike(user_id, videoId,"0", new Callback<likeandunlike>() {
//
//                        @Override
//                        public void success(likeandunlike addwishmovie, retrofit.client.Response response) {
//
//                            addwishmovie=addwishmovie;
//                            if (addwishmovie.getLiked().equalsIgnoreCase("0")) {
//
//                                likee.setVisibility(View.VISIBLE);
//                                unlikelayout.setVisibility(View.GONE);
//
//                                Toast.makeText(getApplicationContext(),"You un liked video", Toast.LENGTH_LONG).show();
//                            }
//
//                        }
//
//                        @Override
//                        public void failure(RetrofitError error) {
//
//                            Toast.makeText(getApplicationContext(),"sd", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
//
//
//            }
//        });
//
//
//
//        dislike.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                {
//
//                    Api.getClient().getDislikecount(user_id, videoId,"1", new Callback<likeandunlike>() {
//
//                        @Override
//                        public void success(likeandunlike addwishmovie, retrofit.client.Response response) {
//
//                            addwishmovie=addwishmovie;
//
//                            if (addwishmovie.getLiked().equalsIgnoreCase("1")) {
//
//                                undislikelayout.setVisibility(View.VISIBLE);
//                                dislike.setVisibility(View.GONE);
//                                Toast.makeText(getApplicationContext(),"You disliked video", Toast.LENGTH_LONG).show();
//                            }
//
//                        }
//
//                        @Override
//                        public void failure(RetrofitError error) {
//
//                            Toast.makeText(getApplicationContext(),"sd", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
//
//
//            }
//        });
//
//
//
//        undislikelayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                {
//
//                    Api.getClient().getDislikecount(user_id, videoId,"0", new Callback<likeandunlike>() {
//
//                        @Override
//                        public void success(likeandunlike addwishmovie, retrofit.client.Response response) {
//
//                            addwishmovie=addwishmovie;
//                            if (addwishmovie.getLiked().equalsIgnoreCase("0")) {
//
//                                dislike.setVisibility(View.VISIBLE);
//                                undislikelayout.setVisibility(View.GONE);
//
//                                Toast.makeText(getApplicationContext(),"You disliked video", Toast.LENGTH_LONG).show();
//                            }
//
//                        }
//
//                        @Override
//                        public void failure(RetrofitError error) {
//
//                            Toast.makeText(getApplicationContext(),"sd", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
//
//
//            }
//        });
//
//        watchlist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                {
//
//                    Api.getClient().getAddWishlistMovie(user_id, videoId,type, new Callback<Addtowishlistmovie>() {
//
//                        @Override
//                        public void success(Addtowishlistmovie addwishmovie, retrofit.client.Response response) {
//
//                            addwishmovie=addwishmovie;
//                            if (addwishmovie.getStatus().equalsIgnoreCase("true")) {
//
//                                watchlistaddedimg.setVisibility(View.VISIBLE);
//                                watchlistimg.setVisibility(View.GONE);
//
//                                Toast.makeText(getApplicationContext(),""+addwishmovie.getMessage(), Toast.LENGTH_LONG).show();
//                            }
//
//                            else if (addwishmovie.getStatus().equalsIgnoreCase("false"))
//                            {
//
//                                watchlistaddedimg.setVisibility(View.GONE);
//                                watchlistimg.setVisibility(View.VISIBLE);
//
//                                Toast.makeText(getApplicationContext(), ""+addwishmovie.getMessage(), Toast.LENGTH_LONG).show();
//                            }
//                            else
//                            {
//                                Toast.makeText(getApplicationContext(),"You are not registered user", Toast.LENGTH_LONG).show();
//
//                            }
//
//                        }
//
//                        @Override
//                        public void failure(RetrofitError error) {
//
//                            Toast.makeText(getApplicationContext(),"You are not a registered user ", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
//
//
//            }
//        });
//
//
//
//        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, videoId);
//        res.enqueue(new retrofit2.Callback<JSONResponse>() {
//            @Override
//            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
//
//                JSONResponse jsonResponse = response.body();
//                movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));
//                // moviesubtitlesdata = new ArrayList<>(Arrays.asList(jsonResponse.getmoviesubtitles()));
//
//                shareurl = jsonResponse.getShareurl();
//
//
//                genre2.setText(jsonResponse.getMain_genre());
//
//                nxtvidid = jsonResponse.getVideonext();
//                nextvidurl = jsonResponse.getNext_url();
//
//                String userprofile = movie_detaildata.get(0).getImage_url();
//
//
//                Picasso.get().load(userprofile).into(coverimage);
//
//                //videotext.setText(movie_detaildata.get(0).getDescription());
//
//                if (jsonResponse.getWishlist().equalsIgnoreCase("true")) {
//
//                    watchlistaddedimg.setVisibility(View.VISIBLE);
//                    watchlistimg.setVisibility(View.GONE);
//                } else {
//                    watchlistaddedimg.setVisibility(View.GONE);
//                    watchlistimg.setVisibility(View.VISIBLE);
//
//                }
//
//                if (jsonResponse.getWatchlater().equalsIgnoreCase("true")) {
//                    watchlateraddedimg.setVisibility(View.VISIBLE);
//                    watchlaterimg.setVisibility(View.GONE);
//
//                } else {
//
//                    watchlateraddedimg.setVisibility(View.GONE);
//                    watchlaterimg.setVisibility(View.VISIBLE);
//
//                }
//
//
//                language1.setText(jsonResponse.getLanguages());
//
//
//                year.setText(movie_detaildata.get(0).getYear());
//                views.setText(movie_detaildata.get(0).getViews() + " Views");
//                videotitle1.setText(movie_detaildata.get(0).getTitle());
//
//
//              /*  videotext.setText(movie_detaildata.get(0).getDescription());
//                //textView.setShowingChar(200);
//                //number of line you want to short
//                videotext.setShowingLine(2);
//
//                videotext.addShowMoreText("show More");
//                videotext.addShowLessText("show Less");
//
//                videotext.setShowMoreColor(R.attr.colorAccent); // or other color
//                videotext.setShowLessTextColor(R.attr.colorAccent);*/
//
//                Picasso.get().load(movie_detaildata.get(0).getImage_url()).into(imageyoutube);
//
//             //   urlll = "https://bitmovin-a.akamaihd.net/content/playhouse-vr/mpds/105560.mpd";
//               // Uri dashUri = Uri.parse(urlll);
//                // Uri subUri =Uri.parse(suburl);
//
//
//             /*   if(subtitle_on.getVisibility() == View.GONE) {
//
//                  *//*  MediaSource contentMediaSource = buildMediaSource1(dashUri);
//                    MediaSource[] mediaSources = new MediaSource[2]; //The Size must change depending on the Uris
//                    mediaSources[0] = contentMediaSource; // uri
//
//                    //Add subtitles
//                    SingleSampleMediaSource subtitleSource = new SingleSampleMediaSource(subUri, dataSourceFactory,
//                            Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP, Format.NO_VALUE, "en", null),
//                            C.TIME_UNSET);
//
//                    mediaSources[1] = subtitleSource;
//
//                    MediaSource mediaSource = new MergingMediaSource(mediaSources);
//
//                    player.prepare(mediaSource);
//                    player.setPlayWhenReady(true);*//*
//
//                }*/
//
//
//
//
//           /*     mediaSource = buildMediaSource(Uri.parse(urlll));
//                player.prepare(mediaSource);
//                player.addListener(new PlayerEventListener());
//                player.setPlayWhenReady(true);
//                player.addAnalyticsListener(new EventLogger(trackSelector));
//                playerView.setPlayer(player);
//*/
//
//           /*     if(player.getCurrentPosition() == 15245) {
//
//                    skipintro.setVisibility(View.VISIBLE);
//                    imgBwd.setVisibility(View.GONE);
//                    imgFwd.setVisibility(View.GONE);
//                    exoPause.setVisibility(View.GONE);
//                    exoPlay.setVisibility(View.GONE);
//
//                }*/
//
//
//
//            }
//            @Override
//            public void onFailure(Call<JSONResponse> call, Throwable t) {
//                Log.d("Error", t.getMessage());
//            }
//        });
//
//
//
//
//
//        Call<JSONResponse> alsolikeres = ApiClient.getInstance1().getApi().getAlsolikeVideo(videoId);
//        alsolikeres.enqueue(new retrofit2.Callback<JSONResponse>() {
//            @Override
//            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
//
//
//                JSONResponse jsonResponse = response.body();
//
//                if(jsonResponse.getChannelrecomended().length ==0) {
//
//                    norecommanded.setVisibility(View.VISIBLE);
//                    thismaylikerecycler.setVisibility(View.GONE);
//                }
//                else {
//
//                    norecommanded.setVisibility(View.GONE);
//                    thismaylikerecycler.setVisibility(View.VISIBLE);
//                    recomendeddatalist = new ArrayList<>(Arrays.asList(jsonResponse.getChannelrecomended()));
//                    thismaylikeadopter = new ThismaylikeAdopter(recomendeddatalist);
//                    thismaylikerecycler.setAdapter(thismaylikeadopter);
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<JSONResponse> call, Throwable t) {
//                Log.d("Error", t.getMessage());
//
//            }
//        });
//        //Toast.makeText(getApplicationContext(),"videocast"+videoId,Toast.LENGTH_LONG).show();
//
//       /* Call<JSONResponse> videocast = ApiClient.getInstance1().getApi().getVideoCast("39");
//        videocast.enqueue(new retrofit2.Callback<JSONResponse>() {
//            @Override
//            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
//
//                JSONResponse jsonResponse = response.body();
//
//
//
//                if(jsonResponse.getCast_details().length ==0) {
//
//
//                }
//                else {
//
//
//                    castdetails = new ArrayList<>(Arrays.asList(jsonResponse.getCast_details()));
//                    castandcrewadapter = new castandcrewadapter(castdetails);
//                    castandcrewrecycler.setAdapter(castandcrewadapter);
//                   *//* cast_name.setText(jsonResponse.getCast());*//*
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<JSONResponse> call, Throwable t) {
//                Log.d("Error", t.getMessage());
//
//            }
//        });*/
//
//    }
//
//
//
//
//
// /*   @Override
//    public void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        releasePlayer();
//        clearStartPosition();
//        setIntent(intent);
//
//        Bundle bundle = getIntent().getExtras();
//
//
//        if (bundle != null) {
//            videoId = bundle.getString("video_id");
//            videoName = bundle.getString("video_name");
//            urlll = bundle.getString("video_url");
//            videoDurationInSeconds =bundle.getLong("video_duration");
//        }
//
//
//    }*/
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//
//
//
//        if (Util.SDK_INT > 23) {
//
//            setProgress();
//            if (playerView != null) {
//                playerView.onResume();
//            }
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (Util.SDK_INT <= 23 || player == null) {
//
//            setProgress();
//
//            if (playerView != null) {
//                playerView.onResume();
//            }
//        }
//
//        FullScreencall();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (Util.SDK_INT <= 23) {
//            if (playerView != null) {
//                playerView.onPause();
//            }
//
//
//
//        }
//
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        handler.removeCallbacks(runnableCode);
//
//
//        if (Util.SDK_INT > 23) {
//            if (playerView != null) {
//                playerView.onPause();
//            }
//
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//    }
//
//
//// OnClickListener methods
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        outState.putParcelable(KEY_TRACK_SELECTOR_PARAMETERS, trackSelectorParameters);
//        outState.putBoolean(KEY_AUTO_PLAY, startAutoPlay);
//        outState.putInt(KEY_WINDOW, startWindow);
//        outState.putLong(KEY_POSITION, startPosition);
//    }
//
//
//
//
//
//    @SuppressLint("SourceLockedOrientationActivity")
//    @Override
//    public void onClick(View view) {
//
//        switch (view.getId()) {
//            case R.id.img_setting:
//
//                MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
//                if (mappedTrackInfo != null) {
//                    if (!isShowingTrackSelectionDialog && TrackSelectionDialog.willHaveContent(trackSelector)) {
//                        isShowingTrackSelectionDialog = true;
//                        TrackSelectionDialog trackSelectionDialog = TrackSelectionDialog.createForTrackSelector(trackSelector,/* onDismissListener= */ dismissedDialog -> isShowingTrackSelectionDialog = false);
//                        trackSelectionDialog.show(getSupportFragmentManager(), /* tag= */ null);
//                    }
//                }
//
//                break;
//
//            case R.id.img_full_screen_enter_exit:
//                Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
//                int orientation = display.getOrientation();
//
//                if (orientation == Surface.ROTATION_90 || orientation == Surface.ROTATION_270) {
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//
//
//                    playerView.setLayoutParams(
//                            new PlayerView.LayoutParams(
//                                    // or ViewGroup.LayoutParams.WRAP_CONTENT
//                                    PlayerView.LayoutParams.MATCH_PARENT,
//                                    // or ViewGroup.LayoutParams.WRAP_CONTENT,
//                                    ScreenUtils.convertDIPToPixels(YoutubeVideoHomepageActivity.this, playerHeight)));
//
//
//                    frameLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
//                    imgFullScreenEnterExit.setImageResource(R.drawable.exo_controls_fullscreen_enter);
//                    isScreenLandscape = false;
//                    FullScreencall();
//
//                    hide();
//                } else {
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                    FullScreencall();
//
//                    playerView.setLayoutParams(
//                            new PlayerView.LayoutParams(
//                                    // or ViewGroup.LayoutParams.WRAP_CONTENT
//                                    PlayerView.LayoutParams.MATCH_PARENT,
//                                    // or ViewGroup.LayoutParams.WRAP_CONTENT,
//                                    PlayerView.LayoutParams.MATCH_PARENT));
//
//
//                    frameLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
//                    imgFullScreenEnterExit.setImageResource(R.drawable.exo_controls_fullscreen_exit);
//                    isScreenLandscape = true;
//                    hide();
//
//                }
//
//                break;
//
//            case R.id.tv_play_back_speed:
//            case R.id.tv_play_back_speed_symbol:
//
//                if (tvPlaybackSpeed.getText().equals("1")) {
//                    tapCount++;
//                    PlaybackParameters param = new PlaybackParameters(2f);
//                    player.setPlaybackParameters(param);
//                    tvPlaybackSpeed.setText("" + 2);
//                } else if (tvPlaybackSpeed.getText().equals("2")) {
//                    tapCount++;
//                    PlaybackParameters param = new PlaybackParameters(4f);
//                    player.setPlaybackParameters(param);
//                    tvPlaybackSpeed.setText("" + 4);
//
//                } else if (tvPlaybackSpeed.getText().equals("4")) {
//                    tapCount++;
//                    PlaybackParameters param = new PlaybackParameters(6f);
//                    player.setPlaybackParameters(param);
//                    tvPlaybackSpeed.setText("" + 6);
//                } else if (tvPlaybackSpeed.getText().equals("6")) {
//                    tapCount++;
//                    PlaybackParameters param = new PlaybackParameters(8f);
//                    player.setPlaybackParameters(param);
//                    tvPlaybackSpeed.setText("" + 8);
//                } else {
//                    tapCount = 0;
//                    player.setPlaybackParameters(null);
//                    tvPlaybackSpeed.setText("" + 1);
//
//                }
//
//                break;
//
//            case R.id.img_back_player:
//                onBackPressed();
//                break;
//            case R.id.ll_download_video:
//                 ExoDownloadState exoDownloadState = (ExoDownloadState) llDownloadVideo.getTag();
//
//
//                break;
//
//        }
//
//
//    }
//
//
//
//
//
//    private void fetchDownloadOptions() {
//        trackKeys.clear();
//
//        if (pDialog == null || !pDialog.isShowing()) {
//            pDialog = new ProgressDialog(YoutubeVideoHomepageActivity.this);
//            pDialog.setTitle(null);
//            pDialog.setCancelable(false);
//            pDialog.setMessage("Preparing Download Options...");
//            pDialog.show();
//        }
//
//
//
//
//    }
//
//
//
//
//
//
//
//
//    @SuppressWarnings("unchecked")
//    private MediaSource buildMediaSource(Uri uri, @Nullable String overrideExtension) {
//        @C.ContentType int type = Util.inferContentType(uri, overrideExtension);
//        switch (type) {
//            case C.TYPE_DASH:
//                return new DashMediaSource.Factory(dataSourceFactory)
//                        .createMediaSource(uri);
//            case C.TYPE_SS:
//                return new SsMediaSource.Factory(dataSourceFactory)
//                        .createMediaSource(uri);
//            case C.TYPE_HLS:
//                return new HlsMediaSource.Factory(dataSourceFactory)
//                        .createMediaSource(uri);
//            case C.TYPE_OTHER:
//                return new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
//            default: {
//                throw new IllegalStateException("Unsupported type: " + type);
//            }
//        }
//    }
//
//
//    /**
//     * Returns a new DataSource factory.
//     */
//    private DataSource.Factory buildDataSourceFactory() {
//        return ((AdaptiveExoplayer) getApplication()).buildDataSourceFactory();
//
//    }
//
//
//    private void updateButtonVisibilities() {
//        if (player == null) {
//            return;
//        }
//
//        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
//        if (mappedTrackInfo == null) {
//            return;
//        }
//
//        for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
//            TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
//            if (trackGroups.length != 0) {
//                int label;
//                switch (player.getRendererType(i)) {
//                    case C.TRACK_TYPE_AUDIO:
//                        label = R.string.exo_track_selection_title_audio;
//                        break;
//                    case C.TRACK_TYPE_VIDEO:
//                        label = R.string.exo_track_selection_title_video;
//                        break;
//                    case C.TRACK_TYPE_TEXT:
//                        label = R.string.exo_track_selection_title_text;
//                        break;
//                    default:
//                        continue;
//                }
//            }
//        }
//    }
//
//
//    private void showToast(int messageId) {
//        showToast(getString(messageId));
//    }
//
//    private void showToast(String message) {
//        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//    }
//
//
//    private void setProgress() {
//
//        handler = new Handler();
//        //Make sure you update Seekbar on UI thread
//        handler.post(new Runnable() {
//
//            @Override
//            public void run() {
//                if (player != null) {
//                    tvPlayerCurrentTime.setText(stringForTime((int) player.getCurrentPosition()));
//                    tvPlayerEndTime.setText(stringForTime((int) player.getDuration()));
//
//                    handler.postDelayed(this, 1000);
//                }
//            }
//        });
//    }
//
//    private void initBwd() {
//        imgBwd.requestFocus();
//        imgBwd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                player.seekTo(player.getCurrentPosition() - 10000);
//            }
//        });
//    }
//
//    private void initFwd() {
//        imgFwd.requestFocus();
//        imgFwd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                player.seekTo(player.getCurrentPosition() + 10000);
//            }
//        });
//
//    }
//
//    private String stringForTime(int timeMs) {
//        mFormatBuilder = new StringBuilder();
//        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
//        int totalSeconds = timeMs / 1000;
//
//        int seconds = totalSeconds % 60;
//        int minutes = (totalSeconds / 60) % 60;
//        int hours = totalSeconds / 3600;
//
//        mFormatBuilder.setLength(0);
//        if (hours > 0) {
//            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
//        } else {
//            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
//        }
//    }
//
//    public void FullScreencall() {
//
//
//        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
//            View v = this.getWindow().getDecorView();
//            v.setSystemUiVisibility(View.GONE);
//        } else if (Build.VERSION.SDK_INT >= 19) {
//            //for new api versions.
//            View decorView = getWindow().getDecorView();
//            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
//            decorView.setSystemUiVisibility(uiOptions);
//        }
//
//
//    }
//
//    public void hideStatusBar() {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
//
//    }
//
//    @Override
//    public void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//
//    }
//
//    @SuppressLint("InlinedApi")
//    private void show() {
//        // Show the system bar
//        llParentContainer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
//        mVisible = true;
//
//        // Schedule a runnable to display UI elements after a delay
//        mHideHandler.removeCallbacks(mHideRunnable);
//        mHideHandler.postDelayed(mHideRunnable, UI_ANIMATION_DELAY);
//    }
//
//    /**
//     * Schedules a call to hide() in delay milliseconds, canceling any
//     * previously scheduled calls.
//     */
//    private void delayedHide(int delayMillis) {
//        mHideHandler.removeCallbacks(mHideRunnable);
//        mHideHandler.postDelayed(mHideRunnable, delayMillis);
//    }
//
//    private void hide() {
//        // Hide UI first
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.hide();
//        }
//
//        mVisible = false;
//
//        // Schedule a runnable to remove the status and navigation bar after a delay
//        mHideHandler.removeCallbacks(mHideRunnable);
//        mHideHandler.postDelayed(mHideRunnable, UI_ANIMATION_DELAY);
//    }
//
//
//
//
//    public static boolean isConnectedFast(final Context context) {
//        final NetworkInfo info = getNetworkInfo(context);
//        return (info != null) && info.isConnected() && isConnectionFast(info.getType(), info.getSubtype());
//    }
//
//
//
//    public static boolean isConnectionFast(final int type, final int subType) {
//        if (type == ConnectivityManager.TYPE_WIFI) {
//            return true;
//        } else if (type == ConnectivityManager.TYPE_MOBILE) {
//            switch (subType) {
//
//                case TelephonyManager.NETWORK_TYPE_CDMA:
//                    Log.e("logg", String.valueOf(12));
//                    return true; // ~ 14-64 kbps
//
//                case TelephonyManager.NETWORK_TYPE_1xRTT:
//                    Log.e("logg", String.valueOf(60));
//                    return true; // ~ 50-100 kbps
//                case TelephonyManager.NETWORK_TYPE_EDGE:
//                    Log.e("logg", String.valueOf(50));
//                    return true; // ~ 50-100 kbps
//                case TelephonyManager.NETWORK_TYPE_EVDO_0:
//                    Log.e("logg", String.valueOf(400));
//                    return true; // ~ 400-1000 kbps
//                case TelephonyManager.NETWORK_TYPE_EVDO_A:
//                    Log.e("logg", String.valueOf(600));
//                    return true; // ~ 600-1400 kbps
//                case TelephonyManager.NETWORK_TYPE_GPRS:
//                    Log.e("logg", String.valueOf(100));
//                    return true; // ~ 100 kbps
//                case TelephonyManager.NETWORK_TYPE_HSDPA:
//                    Log.e("logg", "2mb");
//                    return true; // ~ 2-14 Mbps
//                case TelephonyManager.NETWORK_TYPE_HSPA:
//                    Log.e("logg", String.valueOf(700));
//                    return true; // ~ 700-1700 kbps
//                case TelephonyManager.NETWORK_TYPE_HSUPA:
//                    Log.e("logg", "23mb");
//                    return true; // ~ 1-23 Mbps
//                case TelephonyManager.NETWORK_TYPE_UMTS:
//                    Log.e("logg", String.valueOf(4007000));
//                    return true; // ~ 400-7000 kbps
//                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
//                    Log.e("logg", "1-2mb");
//                    return true; // ~ 1-2 Mbps
//                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
//                    Log.e("logg", "5mb");
//                    return true; // ~ 5 Mbps
//                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
//                    Log.e("logg", "10mb");
//                    return true; // ~ 10-20 Mbps
//                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
//                    Log.e("logg", "25kb");
//                    return false; // ~25 kbps
//                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
//                    Log.e("logg", "10+mb");
//                    return true; // ~ 10+ Mbps
//                // Unknown
//                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
//                default:
//                    return true;
//            }
//        } else {
//            return false;
//        }
//
//    }
//
//
//
//    @SuppressLint("SourceLockedOrientationActivity")
//    @Override
//    public void onBackPressed() {
//
//        if (isScreenLandscape) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//
//            playerView.setLayoutParams(
//                    new PlayerView.LayoutParams(
//                            // or ViewGroup.LayoutParams.WRAP_CONTENT
//                            PlayerView.LayoutParams.MATCH_PARENT,
//                            // or ViewGroup.LayoutParams.WRAP_CONTENT,
//                            ScreenUtils.convertDIPToPixels(YoutubeVideoHomepageActivity.this, playerHeight)));
//
//
//            frameLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
//
//            imgFullScreenEnterExit.setImageResource(R.drawable.exo_controls_fullscreen_enter);
//            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//            isScreenLandscape = false;
//            hide();
//
//
//        } else {
//            finish();
//
//        }
//
//    }
//
//
//
//
//    private class PlayerErrorMessageProvider implements ErrorMessageProvider<ExoPlaybackException> {
//
//        @Override
//        public Pair<Integer, String> getErrorMessage(ExoPlaybackException e) {
//            String errorString = getString(R.string.error_generic);
//            if (e.type == ExoPlaybackException.TYPE_RENDERER) {
//                Exception cause = e.getRendererException();
//                if (cause instanceof MediaCodecRenderer.DecoderInitializationException) {
//                    // Special case for decoder initialization failures.
//                    MediaCodecRenderer.DecoderInitializationException decoderInitializationException =
//                            (MediaCodecRenderer.DecoderInitializationException) cause;
//                    if (decoderInitializationException.codecInfo == null) {
//                        if (decoderInitializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException) {
//                            errorString = getString(R.string.error_querying_decoders);
//                        } else if (decoderInitializationException.secureDecoderRequired) {
//                            errorString =
//                                    getString(
//                                            R.string.error_no_secure_decoder, decoderInitializationException.mimeType);
//                        } else {
//                            errorString =
//                                    getString(R.string.error_no_decoder, decoderInitializationException.mimeType);
//                        }
//                    } else {
//                        errorString =
//                                getString(
//                                        R.string.error_instantiating_decoder,
//                                        decoderInitializationException.codecInfo);
//                    }
//                }
//            }
//            return Pair.create(0, errorString);
//        }
//    }
//
//
//
//
//
//
//}