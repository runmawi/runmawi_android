package com.atbuys.runmawi;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;
import com.atbuys.runmawi.Adapter.AudiocatAdopter;
import com.atbuys.runmawi.Api1.RetrofitSingleton;
import com.atbuys.runmawi.Model.HomeBodyResponse;
import com.atbuys.runmawi.Model.HomeData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;


public class UserHomeFragment extends Fragment {
    @Nullable

    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    private ArrayList<live_banner> bannersdata;

    RecentAudioAdapter recentAudioAdapter;
    artistlistAdopter artistlistAdopter;
    followingartistAdopter followartistAdopter;
    Favourite_artistAdopter favourite_artistAdopter;
    Trending_AudioAdapter trending_audioAdapter;
    AlbumlistHomeAdapter channelListAdapter;

  //  UserToken userToken;


    SeekBar seekbar;
    private double startTime = 0;
    private double finalTime = 0;
    public static MediaPlayer mediaplayer;
    private Handler myHandler = new Handler();

    private int currentPage = 0;
    private int NUM_PAGES = 0;
    DrawerLayout drawer_layout;
    public static String audio_id;
    private String testing;


    RecyclerView recentplayrecycler, trendingrecycler, newreleaserecycler,artistfollowrecycler,favouriteartistrecycler,recommendedtodayrecyler;
    ProgressBar recentprogress, trendingprogress, newreleaseprogress,favouriteartistprogress,artistfollowprogress,recommendedtodayprogress;
    RecyclerView artistRecycler;
    ProgressBar artistprogress;

    private ArrayList<user_details> user_detailsdata;
    private ArrayList<recent_audios> recentAudioslist;
    private ArrayList<audioalbums> allaudioslist;
    private ArrayList<audiodetail> movie_detaildata;
    private ArrayList<artistlist> artistlists;
    private ArrayList<followinglist> followArtistList;
    private ArrayList<favoriteslist> favoriteslists;
    private ArrayList<trending_audios> trending_audioslist;
    private ArrayList<HomeData> dataList;






    List<recent> recentlist;
    List<trending> trendingList;



    List<recommendedtoday> recommendedtodaylist;



    LinearLayout userdetails;
    TextView viewpagerid;
    LinearLayout logout,homelayout;




    String user_id, user_role,theme;
    LinearLayout userdetailsmenu;
    private ArrayList<Site_theme_setting> settings;
    ImageView logo;



    RecyclerView Allalbumrecycler;
    ProgressBar Allalbumprogress;
    private RecyclerView.LayoutManager seriesManeger;

    LinearLayout nolatest, notrending,noartist,norecent,nofavartist,nofollow,noalbum;
    ProgressBar homeprogress,bannerprogress,aud_cat_progress;
    ImageView notify;
    Dialog popUP_dialog;
    String x;
    LinearLayout artistlayout,favouriteartistlayout,followartistlayout,aud_cat_layout;


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_user_home, null);
        mediaplayer = new MediaPlayer();


        SharedPreferences prefs = this.getContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);
        audio_id = prefs.getString(sharedpreferences.audioid,null);


        bannersdata = new ArrayList<live_banner>();
        settings = new ArrayList<Site_theme_setting>();
        recentAudioslist = new ArrayList<recent_audios>();
        allaudioslist = new ArrayList<audioalbums>();
        movie_detaildata = new ArrayList<audiodetail>();
        artistlists = new ArrayList<artistlist>();
        followArtistList =  new ArrayList<followinglist>();
        dataList = new ArrayList<HomeData>();
        favoriteslists = new ArrayList<favoriteslist>();
        trending_audioslist = new ArrayList<trending_audios>();


        viewPager = (ViewPager) root.findViewById(R.id.viewPager);
        logout = (LinearLayout) root.findViewById(R.id.logout);
        notify = (ImageView) root.findViewById(R.id.notification);
        homelayout = (LinearLayout) root.findViewById(R.id.homelayout);
        homeprogress =(ProgressBar) root.findViewById(R.id.homepogress);
        bannerprogress =(ProgressBar) root.findViewById(R.id.bannerprogress);
        artistlayout = (LinearLayout) root.findViewById(R.id.artistlayout);
        followartistlayout = (LinearLayout) root.findViewById(R.id.followartistlayout);
        favouriteartistlayout = (LinearLayout) root.findViewById(R.id.favouriteartistlayout);
        Allalbumrecycler = (RecyclerView) root.findViewById(R.id.allalbumrecycler);
        Allalbumprogress = (ProgressBar)  root.findViewById(R.id.allalbumprogress);
        userdetailsmenu = (LinearLayout) root.findViewById(R.id.userdetailsmenu);
        logo = (ImageView) root.findViewById(R.id.logo);




        recyclerView = root.findViewById(R.id.aud_cat_recycler);

        nolatest = (LinearLayout) root.findViewById(R.id.nolatest);
        notrending = (LinearLayout) root.findViewById(R.id.notrending);
        noartist = (LinearLayout) root.findViewById(R.id.noartist);
        nofavartist = (LinearLayout) root.findViewById(R.id.nofavartist);
        norecent = (LinearLayout) root.findViewById(R.id.norecent);
        nofollow = (LinearLayout) root.findViewById(R.id.nofollow);
        noalbum = (LinearLayout) root.findViewById(R.id.noalbum);
        aud_cat_layout =(LinearLayout) root.findViewById(R.id.aud_cat_layout);
        aud_cat_progress = (ProgressBar) root.findViewById(R.id.aud_cat_progress);



        userdetails = (LinearLayout) root.findViewById(R.id.userdetails);
        //viewpagerid = (TextView) root.findViewById(R.id.viewpager.id);



        recentplayrecycler = (RecyclerView) root.findViewById(R.id.recentrecycler);
        recentprogress = (ProgressBar) root.findViewById(R.id.recentproress);
        recentplayrecycler.setHasFixedSize(true);
        recentplayrecycler.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));

        trendingrecycler = (RecyclerView) root.findViewById(R.id.trendingvideosRecycler);
        trendingprogress = (ProgressBar) root.findViewById(R.id.trendingvideoprogress);
        trending_audioAdapter = new Trending_AudioAdapter(trending_audioslist, this.getContext());
        trendingrecycler.setHasFixedSize(true);
        trendingrecycler.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        trendingrecycler.setAdapter(trending_audioAdapter);

        newreleaserecycler = (RecyclerView) root.findViewById(R.id.newreleaserecycler);
        newreleaseprogress = (ProgressBar) root.findViewById(R.id.newreleseprogress);
        recentAudioAdapter = new RecentAudioAdapter(recentAudioslist, this.getContext());
        newreleaserecycler.setHasFixedSize(true);
        newreleaserecycler.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        newreleaserecycler.setAdapter(recentAudioAdapter);

        adapter = new AudiocatAdopter(dataList, this.getContext());
        layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        channelListAdapter = new AlbumlistHomeAdapter(allaudioslist, this.getContext());
        Allalbumrecycler.setHasFixedSize(true);
        Allalbumrecycler.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        Allalbumrecycler.setAdapter(channelListAdapter);


        artistRecycler = (RecyclerView) root.findViewById(R.id.artistrecycler);
        artistprogress = (ProgressBar) root.findViewById(R.id.artistprogress);
        artistlistAdopter = new artistlistAdopter(artistlists, this.getContext());
        artistRecycler.setHasFixedSize(true);
        artistRecycler.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        artistRecycler.setAdapter(artistlistAdopter);


        artistfollowrecycler = (RecyclerView) root.findViewById(R.id.followartistrecycler);
        artistfollowprogress = (ProgressBar) root.findViewById(R.id.followartistprogress);
        followartistAdopter = new followingartistAdopter(followArtistList, this.getContext());
        artistfollowrecycler.setHasFixedSize(true);
        artistfollowrecycler.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        artistfollowrecycler.setAdapter(followartistAdopter);


        favouriteartistrecycler = (RecyclerView) root.findViewById(R.id.favartistrecycler);
        favouriteartistprogress = (ProgressBar) root.findViewById(R.id.favartistprogress);
        favourite_artistAdopter= new Favourite_artistAdopter(favoriteslists, this.getContext());
        favouriteartistrecycler.setHasFixedSize(true);
        favouriteartistrecycler.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        favouriteartistrecycler.setAdapter(favourite_artistAdopter);

        recommendedtodayrecyler = (RecyclerView) root.findViewById(R.id.norecommandedtodayrecycler);
        recommendedtodayprogress = (ProgressBar) root.findViewById(R.id.recomendedtodayprogress);
        recommendedtodayrecyler.setHasFixedSize(true);
        recommendedtodayrecyler.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));





        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment newCase = new DashboardFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newCase); // give your fragment container id in first parameter
                // if written, this transaction will be added to backstack
                transaction.addToBackStack(getFragmentManager().getClass().getName());
                transaction.commit();
            }
        });



        trendingList = new ArrayList<>();
        recommendedtodaylist = new ArrayList<>();
        recentlist = new ArrayList<>();


        Call<HomeBodyResponse> responseCall = RetrofitSingleton.getInstance().getApi().getAudioByCategory();

        responseCall.enqueue(new retrofit2.Callback<HomeBodyResponse>() {
            @Override
            public void onResponse(Call<HomeBodyResponse> call, retrofit2.Response<HomeBodyResponse> response) {




                if (response.isSuccessful()) {



                    for (HomeData data : response.body().getGenreMovies()) {

                        dataList.add(data);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<HomeBodyResponse> call, Throwable t) {

                //  progressBar.setVisibility(View.GONE);
            //    Toast.makeText(getContext(),""+t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });




        Toolbar mToolbar = (Toolbar) root.findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.backarrow);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().onBackPressed();
            }
        });

    /*    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawer_layout.openDrawer(GravityCompat.START);
            }
        });

*/

        Call<JSONResponse> callimg = ApiClient.getInstance1().getApi().getthemeSettings();
        callimg.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();

                settings = new ArrayList<>(Arrays.asList(jsonResponse.getSite_theme_setting()));

                String x = settings.get(0).getImage_url();
                Picasso.get().load(x).into(logo);

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error1", t.getMessage());
            }
        });


        drawer_layout = root.findViewById(R.id.drawer_layout);
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;

                }

                viewPager.setCurrentItem(currentPage++, true);

                if (currentPage == bannersdata.size()) {

                    currentPage = 0;
                }

            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });






       /* final Call<JSONResponse> bannerreq = ApiClient.getInstance1().getApi().getBanners();
        bannerreq.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                bannersdata = new ArrayList<>(Arrays.asList(jsonResponse.getBanners()));
                viewPagerAdapter = new ViewPagerAdapter(bannersdata, getContext());
                viewPager.setAdapter(viewPagerAdapter);
                bannerprogress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });



*/








        recentlist.add(
                new recent(
                        "Notification",
                        "http://flicknexui.webnexs.org/public/uploads/images/gfg5.jpg",
                        "Click here to on/off"));

        recentlist.add(
                new recent(
                        "WatchLater",
                        "http://flicknexui.webnexs.org/public/uploads/images/gfg5.jpg",
                        "Save To Watchlater"));


        recentlist.add(
                new recent(
                        "My WishList",
                        "http://flicknexui.webnexs.org/public/uploads/images/gfg5.jpg",
                        "Save To Watchlist"));


        recentAdopter recentadapter = new recentAdopter(recentlist);
        recentplayrecycler.setAdapter(recentadapter);


        Call<JSONResponse> callser = ApiClient.getInstance1().getApi().getRecentAudios();
        callser.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                JSONResponse jsonResponse = response.body();

                if (jsonResponse.getStatus().equalsIgnoreCase("false")) {

                    newreleaseprogress.setVisibility(View.GONE);
                    norecent.setVisibility(View.VISIBLE);
                    newreleaserecycler.setVisibility(View.GONE);


                } else {
                    newreleaseprogress.setVisibility(View.GONE);
                    norecent.setVisibility(View.GONE);
                    newreleaserecycler.setVisibility(View.VISIBLE);
                    recentAudioslist = new ArrayList<>(Arrays.asList(jsonResponse.getRecent_audios()));
                    recentAudioAdapter = new RecentAudioAdapter(recentAudioslist);
                    newreleaserecycler.setAdapter(recentAudioAdapter);

                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });


        Call<JSONResponse> callser1 = ApiClient.getInstance1().getApi().getAudiocatList();
        callser1.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                JSONResponse jsonResponse = response.body();

                if (jsonResponse.getStatus().equalsIgnoreCase("false")) {

                   // norecent.setVisibility(View.VISIBLE);
                    Allalbumrecycler.setVisibility(View.GONE);
                    Allalbumprogress.setVisibility(View.GONE);
                    noalbum.setVisibility(View.VISIBLE);



                } else {
                   // norecent.setVisibility(View.GONE);
                    Allalbumrecycler.setVisibility(View.VISIBLE);
                    allaudioslist = new ArrayList<>(Arrays.asList(jsonResponse.getAudioalbums()));
                    channelListAdapter = new AlbumlistHomeAdapter(allaudioslist);
                    Allalbumrecycler.setAdapter(channelListAdapter);

                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Errorwewedwsd", t.getMessage());
            }
        });


        Allalbumrecycler.addOnItemTouchListener(
                (RecyclerView.OnItemTouchListener) new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (allaudioslist.size() > position) {
                            if (allaudioslist.get(position) != null) {

                                Intent in=new Intent(getActivity(), AlbumAudioPageActivity.class);
                                in.putExtra("album_id",allaudioslist.get(position).getId());
                                startActivity(in);


                            }
                        }
                    }


                })
        );



        Call<JSONResponse> callartist = ApiClient.getInstance1().getApi().getArtistList();
        callartist.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();

                if (jsonResponse.getArtistlist().length == 0) {

                    artistprogress.setVisibility(View.GONE);
                     noartist.setVisibility(View.VISIBLE);
                    artistRecycler.setVisibility(View.GONE);

                } else {
                    artistprogress.setVisibility(View.GONE);
                    noartist.setVisibility(View.GONE);
                    artistRecycler.setVisibility(View.VISIBLE);
                    artistlists = new ArrayList<>(Arrays.asList(jsonResponse.getArtistlist()));
                    artistlistAdopter = new artistlistAdopter(artistlists);
                    artistRecycler.setAdapter(artistlistAdopter);

                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });


        Call<JSONResponse> callartistfollow = ApiClient.getInstance1().getApi().getArtistyouFollow(user_id);
        callartistfollow.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                artistfollowprogress.setVisibility(View.GONE);

                JSONResponse jsonResponse = response.body();


                if (jsonResponse.getStatus().equalsIgnoreCase("false")) {


                    nofollow.setVisibility(View.VISIBLE);
                    artistfollowrecycler.setVisibility(View.GONE);
                    artistfollowprogress.setVisibility(View.GONE);

                } else {
                    nofollow.setVisibility(View.GONE);
                    artistfollowrecycler.setVisibility(View.VISIBLE);
                    followArtistList = new ArrayList<>(Arrays.asList(jsonResponse.getFollowinglist()));
                    followartistAdopter = new followingartistAdopter(followArtistList);
                    artistfollowrecycler.setAdapter(followartistAdopter);

                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });

        Call<JSONResponse> calltrnding = ApiClient.getInstance1().getApi().getTrendingAudios();
        calltrnding.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                JSONResponse jsonResponse = response.body();

                if (jsonResponse.getTrending_audios().length ==0) {

                    notrending.setVisibility(View.VISIBLE);
                    trendingprogress.setVisibility(View.GONE);
                    trendingrecycler.setVisibility(View.GONE);


                } else {
                    notrending.setVisibility(View.GONE);
                    trendingrecycler.setVisibility(View.VISIBLE);
                    trending_audioslist = new ArrayList<>(Arrays.asList(jsonResponse.getTrending_audios()));
                    trending_audioAdapter = new Trending_AudioAdapter(trending_audioslist);
                    trendingrecycler.setAdapter(trending_audioAdapter);

                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });


        Call<JSONResponse> callartistfav = ApiClient.getInstance1().getApi().getFavouriteArtist(user_id);
        callartistfav.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                favouriteartistprogress.setVisibility(View.GONE);

                JSONResponse jsonResponse = response.body();


                if (jsonResponse.getFavoriteslist().length == 0) {

                     nofavartist.setVisibility(View.VISIBLE);
                    favouriteartistrecycler.setVisibility(View.GONE);

                } else {
                    nofavartist.setVisibility(View.GONE);
                    favouriteartistrecycler.setVisibility(View.VISIBLE);
                    favoriteslists = new ArrayList<>(Arrays.asList(jsonResponse.getFavoriteslist()));
                    favourite_artistAdopter = new Favourite_artistAdopter(favoriteslists);
                    favouriteartistrecycler.setAdapter(favourite_artistAdopter);

                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });

        newreleaserecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (recentAudioslist.size() > position) {
                            if (recentAudioslist.get(position) != null) {

                                if(user_role == null && recentAudioslist.get(position).getAccess().equalsIgnoreCase("registered")
                                        || user_role == null && recentAudioslist.get(position).getAccess().equalsIgnoreCase("subscriber"))
                                {

                                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                    alert.setTitle("Your Not Subscribed user Kindly Subscribe and access the contents");

                                    alert.setPositiveButton("Subscribe", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent in = new Intent(getContext(), SubscribeActivity.class);
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

                                else if (user_role == null && recentAudioslist.get(position).getAccess().equalsIgnoreCase("guest") ) {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, recentAudioslist.get(position).getId());

                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));

                                            SharedPreferences.Editor editor = getActivity().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
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

                                    Intent in = new Intent(getContext(), MediaPlayerPageActivity.class);
                                    in.putExtra("id", recentAudioslist.get(position).getId());
                                    startActivity(in);
                                    mediaplayer.reset();
                                    mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                                    try {

                                        mediaplayer.setDataSource(recentAudioslist.get(position).getMp3_url());
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
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, recentAudioslist.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));

                                            SharedPreferences.Editor editor = getActivity().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
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

                                    Intent in = new Intent(getContext(), MediaPlayerPageActivity.class);
                                    in.putExtra("id", recentAudioslist.get(position).getId());
                                    startActivity(in);
                                    mediaplayer.reset();
                                    mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                                    try {
                                        mediaplayer.setDataSource(recentAudioslist.get(position).getMp3_url());
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

                                else if(user_role.equalsIgnoreCase("registered") && recentAudioslist.get(position).getAccess().equalsIgnoreCase("guest") || user_role.equalsIgnoreCase("registered") && recentAudioslist.get(position).getAccess().equalsIgnoreCase("registered"))
                                {

                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, recentAudioslist.get(position).getId());
                                res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                    @Override
                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                        JSONResponse jsonResponse = response.body();
                                        movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));

                                        SharedPreferences.Editor editor = getActivity().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
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

                                Intent in = new Intent(getContext(), MediaPlayerPageActivity.class);
                                in.putExtra("id", recentAudioslist.get(position).getId());
                                startActivity(in);
                                mediaplayer.reset();
                                mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                                try {
                                    mediaplayer.setDataSource(recentAudioslist.get(position).getMp3_url());
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
                                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                    alert.setTitle("Your Not Subscribed user Kindly Subscribe and access the contents");

                                   alert.setPositiveButton("Subscribe", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {

                                           Intent in = new Intent(getContext(), SubscribeActivity.class);
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



        trendingrecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (trending_audioslist.size() > position) {
                            if (trending_audioslist.get(position) != null)
                            {
                                if(user_role == null)
                                {

                                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                    alert.setTitle("Your Not Subscribed user Kindly Subscribe and access the contents");

                                    alert.setPositiveButton("Subscribe", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent in = new Intent(getContext(), SubscribeActivity.class);
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


                                else if (user_role.equalsIgnoreCase("subscriber") || user_role.equalsIgnoreCase("admin")) {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, trending_audioslist.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));

                                            SharedPreferences.Editor editor = getActivity().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
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

                                    Intent in = new Intent(getContext(), MediaPlayerPageActivity.class);
                                    in.putExtra("id", trending_audioslist.get(position).getId());
                                    startActivity(in);
                                    mediaplayer.reset();
                                    mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                                    try {
                                        mediaplayer.setDataSource(trending_audioslist.get(position).getMp3_url());
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

                                else if(user_role.equalsIgnoreCase("registered") && trending_audioslist.get(position).getAccess().equalsIgnoreCase("guest") || user_role.equalsIgnoreCase("registered") && trending_audioslist.get(position).getAccess().equalsIgnoreCase("registered"))
                                {

                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, trending_audioslist.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));

                                            SharedPreferences.Editor editor = getActivity().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
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

                                    Intent in = new Intent(getContext(), MediaPlayerPageActivity.class);
                                    in.putExtra("id", trending_audioslist.get(position).getId());
                                    startActivity(in);
                                    mediaplayer.reset();
                                    mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                                    try {
                                        mediaplayer.setDataSource(trending_audioslist.get(position).getMp3_url());
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
                                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                    alert.setTitle("Your Not Subscribed user Kindly Subscribe and access the contents");

                                    alert.setPositiveButton("Subscribe", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent in = new Intent(getContext(), SubscribeActivity.class);
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

        artistlayout.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {

                             Intent in = new Intent(getActivity(), ArtistListActivity.class);
                             startActivity(in);

                         }
                         });

        favouriteartistlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getActivity(), FavouriteArtistListActivity.class);
                startActivity(in);

            }
        });


        followartistlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getActivity(), FollowingArtistListActivity.class);
                startActivity(in);

            }
        });


        artistRecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (artistlists.size() > position) {
                            if (artistlists.get(position) != null) {

                                Intent in = new Intent(getActivity(), ArtistPageActivity.class);
                                in.putExtra("artist_id",artistlists.get(position).getId());
                                startActivity(in);

                            }
                        }
                    }
                }));


        favouriteartistrecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (favoriteslists.size() > position) {
                            if (favoriteslists.get(position) != null) {

                                Intent in = new Intent(getActivity(), ArtistPageActivity.class);
                                in.putExtra("artist_id",favoriteslists.get(position).getId());
                                startActivity(in);

                            }
                        }
                    }
                }));


        artistfollowrecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (followArtistList.size() > position) {
                            if (followArtistList.get(position) != null) {

                                Intent in = new Intent(getActivity(), ArtistPageActivity.class);
                                in.putExtra("artist_id",followArtistList.get(position).getId());
                                startActivity(in);

                            }
                        }
                    }
                }));





        recommendedtodaylist.add(
                new recommendedtoday(
                        "Notification",
                        "http://flicknexui.webnexs.org/public/uploads/images/gfg5.jpg",
                        "Click here to on/off"));

        recommendedtodaylist.add(
                new recommendedtoday(
                        "WatchLater",
                        "http://flicknexui.webnexs.org/public/uploads/images/gfg5.jpg",
                        "Save To Watchlater"));


        recommendedtodaylist.add(
                new recommendedtoday(
                        "My WishList",
                        "http://flicknexui.webnexs.org/public/uploads/images/gfg5.jpg",
                        "Save To Watchlist"));


        recommandtodayAdopter recommandeddapter = new recommandtodayAdopter(recommendedtodaylist);
        recommendedtodayrecyler.setAdapter(recommandeddapter);










        return root;

    }

    private class BecomingNoisyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
               mediaplayer.pause();
            }
        }
    }

    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        }


    }

}


