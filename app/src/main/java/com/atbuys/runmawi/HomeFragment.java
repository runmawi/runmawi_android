package com.atbuys.runmawi;

import static android.content.Context.MODE_PRIVATE;

import static com.atbuys.runmawi.UserHomeFragment.mediaplayer;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.atbuys.runmawi.Adapter.GenreAdapter;
import com.atbuys.runmawi.Adapter.GenreCategoriesAdapter;
import com.atbuys.runmawi.Adapter.LiveCategoriesSimpleAdapter;
import com.atbuys.runmawi.adapter.RunmawiTvBannerAdapter;
import com.atbuys.runmawi.channelvideos;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import com.atbuys.runmawi.Model.data;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;
import com.atbuys.runmawi.Adapter.CategoryhomepageAdopter;
import com.atbuys.runmawi.Api1.RetrofitSingleton;
import com.atbuys.runmawi.Model.HomeBodyResponse;
import com.atbuys.runmawi.Model.HomeCategoryData;
import com.atbuys.runmawi.LiveCategory;
import com.atbuys.runmawi.livepageAdopter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("deprecation")
public class HomeFragment extends Fragment {
    @Nullable

    ViewPager viewPager;
    ViewPagerAdapter1 viewPagerAdapter1;
    ViewPager runmawiTvViewPager;
    ViewPagerAdapter1 runmawiTvViewPagerAdapter;
    RelativeLayout runmawiTvBannerContainer;
    androidx.core.widget.NestedScrollView runmawiTvContentContainer;
    private ArrayList<series_banner> bannersdata;
    private ArrayList<video_banner> videobanner;
    private ArrayList<live_banner> livebannerdata;
    private ArrayList<sliders> sliderdata;
    private ArrayList<videos> continuwatchilinglist;
    private ArrayList<pages> genreList;
    private ArrayList<categorylist> cateList;
    private ArrayList<categorylist> allCateList; // Store all categories
    private String activeTabType = "runmawi_tv"; // Default is Runmawi TV
    private ArrayList<categoryVideos> movieCategoryList;
    private ArrayList<data> liveCategoryList;
    private ArrayList<data> liveCategoriesForDisplay; // Preserved copy for the Categories tab
    private ArrayList<pages> pageslist;
    private ArrayList<Menus> menuslist;
    private ArrayList<user_details> user_detailsdata;
    private int currentPage = 0;
    private int NUM_PAGES = 0;
    DrawerLayout drawer_layout;
    TextView version;

    RecyclerView allChannelRecycler, continueREcycler;
    private ProgressBar loadingPB;
    private NestedScrollView nestedSV;
    int page = 1, limit = 1;
    LinearLayout continue_watching_layour, sidemenudplayout, loader_layout;
    ContinueWatchingAdopter continueWatchingAdopter;
    GenreAdapter genreAdapter;
    GenreCategoriesAdapter genreCategoriesAdapter;
    MovieCategoryAdapter movieCategoryAdapter;
    LiveCategoryAdapter liveCategoryAdapter;
    private RecyclerView.Adapter live_adapter;
    sidemennuAdopter sidemennuAdopter1;
    private RecyclerView.Adapter all_adapter;

    TextView sidemenuusername, sidemenuuseremail;
    ImageView sidemenudp;
    private ArrayList<HomeCategoryData> categoryList;

    RelativeLayout activity_main;
    LinearLayout logout;
    RecyclerView sidemenu1recycler;

    String user_id, user_role, theme;
    LinearLayout userdetailsmenu;
    private ArrayList<Site_theme_setting> Site_theme_setting;
    ImageView logo;
    ProgressBar bannerprogress;
    TextView continue_text;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView movieRecyclerview, liveCateRecyclerview, genrerecyclerview, cateRecyclerView;
    CardView runmawi_page, movie_page, categories_page;
    TextView cate_name, movie_name, runmawi_name;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setRetainInstance(true);

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home, null);
        SharedPreferences prefs = this.getContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);

        bannersdata = new ArrayList<series_banner>();
        livebannerdata = new ArrayList<live_banner>();
        videobanner = new ArrayList<video_banner>();
        sliderdata = new ArrayList<>();
        user_detailsdata = new ArrayList<>();

        continuwatchilinglist = new ArrayList<videos>();
        genreList = new ArrayList<>();
        cateList = new ArrayList<>();
        movieCategoryList = new ArrayList<>();
        liveCategoryList = new ArrayList<>();
        liveCategoriesForDisplay = new ArrayList<>();
        categoryList = new ArrayList<HomeCategoryData>();
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.refreshLayout);
        runmawi_page = (CardView) root.findViewById(R.id.runmawi_page);
        movie_page = (CardView) root.findViewById(R.id.movie_page);
        categories_page = (CardView) root.findViewById(R.id.categories_page);
        runmawi_name = (TextView) root.findViewById(R.id.runmawi_name);
        movie_name = (TextView) root.findViewById(R.id.movie_name);
        cate_name = (TextView) root.findViewById(R.id.cate_name);


        pageslist = new ArrayList<pages>();
        menuslist = new ArrayList<Menus>();
        Site_theme_setting = new ArrayList<Site_theme_setting>();

        viewPager = (ViewPager) root.findViewById(R.id.viewPager);
        runmawiTvViewPager = (ViewPager) root.findViewById(R.id.runmawi_tv_viewPager);
        runmawiTvBannerContainer = (RelativeLayout) root.findViewById(R.id.runmawi_tv_banner_container);
        runmawiTvContentContainer = (androidx.core.widget.NestedScrollView) root.findViewById(R.id.runmawi_tv_content_container);
        activity_main = root.findViewById(R.id.activity_main);
        bannerprogress = root.findViewById(R.id.bannerprogress);


        logout = (LinearLayout) root.findViewById(R.id.logout);
        continue_text = (TextView) root.findViewById(R.id.continue_text);
        continue_watching_layour = root.findViewById(R.id.continue_watching_layour);
        userdetailsmenu = (LinearLayout) root.findViewById(R.id.userdetailsmenu);
        logo = (ImageView) root.findViewById(R.id.logo);
        version = (TextView) root.findViewById(R.id.version);


        version.setText("Version:  " + BuildConfig.VERSION_NAME);

        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new
                    String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }

        Call<JSONResponse> callimg = ApiClient.getInstance1().getApi().getthemeSettings();
        callimg.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                Site_theme_setting = new ArrayList<>(Arrays.asList(jsonResponse.getSite_theme_setting()));

                String x = Site_theme_setting.get(0).getImage_url();
                Picasso.get().load(x).into(logo);
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error1", t.getMessage());
            }
        });
        Call<HomeBodyResponse> allChannel = RetrofitSingleton.getInstance().getApi().getHomeCategory(user_id, page);
        allChannel.enqueue(new retrofit2.Callback<HomeBodyResponse>() {
            @Override
            public void onResponse(Call<HomeBodyResponse> call, retrofit2.Response<HomeBodyResponse> response) {

                lists lists = response.body().getLists();
                limit = lists.getLast_page();
            }

            @Override
            public void onFailure(Call<HomeBodyResponse> call, Throwable t) {
            }
        });

        loadingPB = root.findViewById(R.id.idPBLoading);
        nestedSV = root.findViewById(R.id.idNestedSV);
        allChannelRecycler = root.findViewById(R.id.channelistrecycler);
        sidemenu1recycler = root.findViewById(R.id.sidemenu1);
        continueREcycler = root.findViewById(R.id.continueRecycler);
        movieRecyclerview = (RecyclerView) root.findViewById(R.id.movieRecyclerview);
        liveCateRecyclerview = (RecyclerView) root.findViewById(R.id.liveCateRecyclerview);
        genrerecyclerview = (RecyclerView) root.findViewById(R.id.genrerecyclerview);
        cateRecyclerView = (RecyclerView) root.findViewById(R.id.cateRecyclerView);

        pages pages = new pages("Movies", true);
        genreList.add(pages);
        pages pages1 = new pages("Runmawi TV", false);
        genreList.add(pages1);
        pages pages2 = new pages("Categories", false);
        genreList.add(pages2);

        genrerecyclerview.setHasFixedSize(true);
        genrerecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        //genrerecyclerview.setLayoutManager(new GridLayoutManager(getContext(),3));
        genreAdapter = new GenreAdapter(genreList, this.getContext());
        genrerecyclerview.setAdapter(genreAdapter);

        cateRecyclerView.setHasFixedSize(true);
        cateRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        genreCategoriesAdapter = new GenreCategoriesAdapter(cateList, this.getContext());
        cateRecyclerView.setAdapter(genreCategoriesAdapter);

        Call<JSONResponse> cate_api = ApiClient.getInstance1().getApi().getCategories();
        cate_api.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                if (response.isSuccessful()) {
                    // Store all categories
                    allCateList = new ArrayList<>(Arrays.asList(jsonResponse.getCategorylist()));
                    // Print out all categories for debugging
                    for (categorylist category : allCateList) {
                        Log.d("CATEGORIES_DEBUG", "ID: " + category.getId() + ", Name: " + category.getName());
                    }
                    // Initially filter based on the active tab
                    filterCategoriesByActiveTab();
                    genreCategoriesAdapter = new GenreCategoriesAdapter(cateList, getContext());
                    cateRecyclerView.setAdapter(genreCategoriesAdapter);
                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable throwable) {
            }
        });


        movieRecyclerview.setHasFixedSize(true);
        movieRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 3));
        movieCategoryAdapter = new MovieCategoryAdapter(movieCategoryList, this.getContext());
        movieRecyclerview.setAdapter(movieCategoryAdapter);

        liveCateRecyclerview.setHasFixedSize(true);
        liveCateRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        live_adapter = new LiveCategoryAdapter(liveCategoryList, getContext());
        liveCateRecyclerview.setAdapter(live_adapter);

        Call<HomeBodyResponse> channel = ApiClient.getInstance1().getApi().getLiveBasedCate();
        channel.enqueue(new retrofit2.Callback<HomeBodyResponse>() {
            @Override
            public void onResponse(Call<HomeBodyResponse> call, retrofit2.Response<HomeBodyResponse> response) {

                if (response.isSuccessful()) {
                    Log.d("LIVE_CATEGORIES_DEBUG", "Loaded " + response.body().getData().length + " live categories");
                    
                    // Clear both lists to avoid duplicates
                    liveCategoryList.clear();
                    liveCategoriesForDisplay.clear();
                    
                    // Add data to both lists
                    for (int i = 0; i < response.body().getData().length; i++) {
                        data categoryData = response.body().getData()[i];
                        
                        // Generate ID for categories with null IDs
                        if (categoryData.getId() == null) {
                            // Generate ID based on name: "live_" + index + "_" + name_without_spaces
                            String generatedId = "live_" + (i + 1);
                            if (categoryData.getName() != null) {
                                generatedId += "_" + categoryData.getName().toLowerCase().replace(" ", "_");
                                categoryData.setId(generatedId);
                            }
                        }
                        
                        // Add to the main list for the LiveCategoryAdapter
                        liveCategoryList.add(categoryData);
                        
                        // Create a clean copy for the Categories tab display
                        data displayCopy = new data();
                        displayCopy.setName(categoryData.getName());
                        displayCopy.setId(categoryData.getId()); // Now using the generated ID if original was null
                        displayCopy.setId1(categoryData.getId1());
                        liveCategoriesForDisplay.add(displayCopy);
                        
                        Log.d("LIVE_CATEGORIES_DEBUG", "ID: " + categoryData.getId() + ", Name: " + categoryData.getName());
                    }
                    
                    // Update the adapter
                    live_adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<HomeBodyResponse> call, Throwable t) {
            }
        });

        cateRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        // Add bounds checking to prevent IndexOutOfBoundsException
                        if (cateList == null || cateList.isEmpty() || position >= cateList.size()) {
                            Log.e("CATEGORY_DEBUG", "Invalid position or empty cateList. Position: " + position + 
                                  ", List size: " + (cateList != null ? cateList.size() : 0));
                            return;
                        }
                        
                        // Proceed only if we have a valid list and position
                        Call<JSONResponse> cate_api = ApiClient.getInstance1().getApi().getChannelVideo(cateList.get(position).getId());
                        cate_api.enqueue(new Callback<JSONResponse>() {
                            @Override
                            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                                JSONResponse jsonResponse = response.body();

                                if (response.isSuccessful()) {

                                    cateRecyclerView.setVisibility(View.GONE);
                                    swipeRefreshLayout.setVisibility(View.GONE);
                                    movieRecyclerview.setVisibility(View.VISIBLE);
                                    liveCateRecyclerview.setVisibility(View.GONE);

                                    movieCategoryList = new ArrayList<>(Arrays.asList(jsonResponse.getCategoryVideos()));
                                    movieCategoryAdapter = new MovieCategoryAdapter(movieCategoryList, getContext());
                                    movieRecyclerview.setAdapter(movieCategoryAdapter);
                                }
                            }

                            @Override
                            public void onFailure(Call<JSONResponse> call, Throwable throwable) {
                            }
                        });
                    }
                }));

        movieRecyclerview.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (movieCategoryList.size() > position) {
                            if (movieCategoryList.get(position) != null) {

                                Intent in = new Intent(getContext(), HomePageVideoActivity.class);
                                in.putExtra("id", movieCategoryList.get(position).getId());
                                in.putExtra("xtra", "Norent");
                                in.putExtra("data", "videos");
                                in.putExtra("ads", "");
                                startActivity(in);

                            }
                        }
                    }
                }));

        continueWatchingAdopter = new ContinueWatchingAdopter(continuwatchilinglist, this.getContext());
        continueREcycler.setHasFixedSize(true);
        continueREcycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        continueREcycler.setAdapter(continueWatchingAdopter);

        all_adapter = new CategoryhomepageAdopter(categoryList, getContext());
        allChannelRecycler.setHasFixedSize(true);
        allChannelRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        allChannelRecycler.setAdapter(all_adapter);

        sidemennuAdopter1 = new sidemennuAdopter(menuslist, this.getContext());
        sidemenu1recycler.setHasFixedSize(true);
        sidemenu1recycler.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        sidemenu1recycler.setAdapter(sidemennuAdopter1);

        sidemenuuseremail = (TextView) root.findViewById(R.id.useremail);
        sidemenuusername = (TextView) root.findViewById(R.id.username);
        sidemenudp = (ImageView) root.findViewById(R.id.userdp);
        sidemenudplayout = (LinearLayout) root.findViewById(R.id.userdplayout);
        loader_layout = (LinearLayout) root.findViewById(R.id.loader_layout);


        if (user_id != null) {
            logout.setVisibility(View.GONE);
            sidemenudplayout.setVisibility(View.GONE);
        } else {
            logout.setVisibility(View.GONE);
            sidemenudplayout.setVisibility(View.VISIBLE);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //   AdaptiveExoplayer.getInstance().getDownloadManager().removeAllDownloads();

                final SharedPreferences prefs = getContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
                final SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();
                Intent in = new Intent(getContext(), HomePageActivitywithFragments.class);
                startActivity(in);
                Toast.makeText(getContext(), "User Successfully logged out", Toast.LENGTH_SHORT).show();

                LoginManager.getInstance().logOut();

            }
        });

        Toolbar mToolbar = (Toolbar) root.findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.baseline_dehaze_24);

        drawer_layout = root.findViewById(R.id.drawer_layout);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.openDrawer(GravityCompat.START);
            }
        });

        // Set Runmawi TV tab as initially selected
        runmawi_page.setCardBackgroundColor(Color.parseColor("#ff0000"));
        movie_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movie_page.setCardBackgroundColor(Color.parseColor("#ff0000"));
                categories_page.setCardBackgroundColor(Color.parseColor("#000000"));
                runmawi_page.setCardBackgroundColor(Color.parseColor("#000000"));

                swipeRefreshLayout.setVisibility(View.VISIBLE);
                movieRecyclerview.setVisibility(View.GONE);
                cateRecyclerView.setVisibility(View.GONE);
                
                // Show Movies banner with only video banners, hide Runmawi TV content container
                activity_main.setVisibility(View.VISIBLE); // Main container for movies
                runmawiTvContentContainer.setVisibility(View.GONE); // Hide Runmawi TV content
                if (runmawiTvViewPager != null) runmawiTvViewPager.setVisibility(View.GONE); // Hide Runmawi TV slider

                // Update the ViewPager to show only video banners AND MAKE IT VISIBLE
                if (viewPager != null) { // Check if viewPager is not null
                    if (videobanner != null && !videobanner.isEmpty()) {
                        viewPagerAdapter1 = new ViewPagerAdapter1(null, null, null, videobanner, getContext());
                        viewPager.setAdapter(viewPagerAdapter1);
                        viewPager.setVisibility(View.VISIBLE); // Make movie slider visible
                    } else {
                        viewPager.setVisibility(View.GONE); // Hide if no banners for movies
                    }
                }
                
                // Set active tab type to movies
                activeTabType = "movies";
                
                // Just prepare the filtered list for when Categories tab is clicked
                if (allCateList != null && !allCateList.isEmpty()) {
                    // Clear current category list
                    cateList = new ArrayList<>();
                    
                    // Filter movie categories based on naming patterns
                    for (categorylist category : allCateList) {
                        String name = category.getName() != null ? category.getName().toLowerCase() : "";
                        
                        // Add movie-related categories
                        if (name.contains("movie") || name.contains("film") || 
                            name.contains("cinema") || 
                            (!name.contains("tv") && !name.contains("live") && !name.contains("news"))) {
                            cateList.add(category);
                        }
                    }
                    
                    // If no categories matched our filter, show all categories
                    if (cateList.isEmpty()) {
                        cateList.addAll(allCateList);
                    }
                    
                    // Update the adapter but don't show it yet
                    if (genreCategoriesAdapter != null) {
                        genreCategoriesAdapter = new GenreCategoriesAdapter(cateList, getContext());
                        cateRecyclerView.setAdapter(genreCategoriesAdapter);
                    }
                }
            }
        });
        // Set initial visibility for Runmawi TV tab since it's the default
        swipeRefreshLayout.setVisibility(View.GONE);
        movieRecyclerview.setVisibility(View.GONE);
        cateRecyclerView.setVisibility(View.GONE);
        activity_main.setVisibility(View.GONE);
        runmawiTvContentContainer.setVisibility(View.VISIBLE);
        
        // activeTabType is already defined above with default value "runmawi_tv"
        // No need to set it again here

        runmawi_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runmawi_page.setCardBackgroundColor(Color.parseColor("#ff0000"));
                categories_page.setCardBackgroundColor(Color.parseColor("#000000"));
                movie_page.setCardBackgroundColor(Color.parseColor("#000000"));

                // Reset all visibility states first
                swipeRefreshLayout.setVisibility(View.GONE);
                movieRecyclerview.setVisibility(View.GONE);
                cateRecyclerView.setVisibility(View.GONE);
                activity_main.setVisibility(View.GONE);
                
                // Show Runmawi TV content container
                runmawiTvContentContainer.setVisibility(View.VISIBLE);
                
                // Make sure liveCateRecyclerview is also visible (it might have been hidden)
                if (liveCateRecyclerview != null) {
                    liveCateRecyclerview.setVisibility(View.VISIBLE);
                    Log.d("VISIBILITY_DEBUG", "liveCateRecyclerview visibility set to VISIBLE");
                }
                
                // Reset any potentially affected UI elements from movie category selection
                // Ensure any Runmawi TV specific headers are visible
                // Note: We don't have a liveTvCategoriesHeader variable, so we'll focus on ensuring
                // other critical components are visible instead
                
                // Ensure specific content sections within runmawiTvContentContainer are visible
                // More aggressively restore visibility of allChannelRecycler and ensure adapter is attached
                if (allChannelRecycler != null) {
                    // Set adapter again if it's null (might have been cleared by movie category selection)
                    if (allChannelRecycler.getAdapter() == null && all_adapter != null) {
                        Log.d("VISIBILITY_DEBUG", "Re-setting adapter on allChannelRecycler");
                        allChannelRecycler.setAdapter(all_adapter);
                    }
                    
                    // Force allChannelRecycler to VISIBLE
                    allChannelRecycler.setVisibility(View.VISIBLE);
                    
                    // Log state for debugging
                    Log.d("VISIBILITY_DEBUG", "allChannelRecycler visibility set to VISIBLE, adapter: " + 
                          (allChannelRecycler.getAdapter() != null ? "present" : "null") + 
                          ", parent visibility: " + 
                          (allChannelRecycler.getParent() instanceof View ? 
                           ((View)allChannelRecycler.getParent()).getVisibility() : "unknown"));
                } else {
                    Log.e("VISIBILITY_DEBUG", "allChannelRecycler is null, cannot make visible");
                }

                // Update the ViewPager to show only live banners for Runmawi TV and manage its visibility
                if (runmawiTvViewPager != null) { // Ensure viewpager is not null
                    if (livebannerdata != null && !livebannerdata.isEmpty()) {
                        runmawiTvViewPagerAdapter = new ViewPagerAdapter1(null, null, livebannerdata, null, getContext());
                        runmawiTvViewPager.setAdapter(runmawiTvViewPagerAdapter);
                        runmawiTvViewPager.setVisibility(View.VISIBLE); // Make sure slider is visible
                    } else {
                        runmawiTvViewPager.setVisibility(View.GONE); // Hide slider if no data
                    }
                }
                
                // Set active tab type to Runmawi TV
                activeTabType = "runmawi_tv";
                // Filter categories to show only TV categories (prepares data for when Categories tab is clicked)
                filterCategoriesByActiveTab();
                
                // Refresh live TV content to ensure we have the latest data
                if (allChannelRecycler != null) {
                    // Request fresh content for Runmawi TV tab - using the same approach as in getDataFromAPI2 method
                    Call<JSONResponse> refreshLiveCall = ApiClient.getInstance1().getApi().gethomelink("live", user_id);
                    refreshLiveCall.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                // Simply refresh the existing adapter if it exists
                                if (all_adapter != null) {
                                    all_adapter.notifyDataSetChanged();
                                    Log.d("VISIBILITY_DEBUG", "Refreshed existing live TV content adapter");
                                } else {
                                    // If adapter doesn't exist, recreate it using the same approach as in getDataFromAPI2
                                    all_adapter = new CategoryhomepageAdopter(categoryList, getContext());
                                    allChannelRecycler.setAdapter(all_adapter);
                                    Log.d("VISIBILITY_DEBUG", "Created new adapter for live TV content");
                                }
                            } else {
                                Log.e("VISIBILITY_DEBUG", "Failed to refresh live TV content, response not successful");
                            }
                        }
                        
                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Log.e("VISIBILITY_DEBUG", "Error refreshing live TV content: " + t.getMessage());
                        }
                    });
                }

                /*Call<JSONResponse> channel = ApiClient.getInstance1().getApi().gethomelink("liveCategories", user_id);
                channel.enqueue(new retrofit2.Callback<JSONResponse>() {
                    @Override
                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {


                            JSONResponse jsonResponse = response.body();
                            if (jsonResponse.getPage_List() == null || jsonResponse.getPage_List().length == 0) {
                                //Toast.makeText(getContext(), "No Livestream Available", Toast.LENGTH_SHORT).show();
                            } else {

                                liveCategoryList = new ArrayList<>(Arrays.asList(jsonResponse.getPage_List()));
                                // Print out all live categories for debugging
                                for (data liveCat : liveCategoryList) {
                                    Log.d("LIVE_CATEGORIES_DEBUG", "ID: " + liveCat.getId() + ", Name: " + liveCat.getName());
                                }
                                liveCategoryAdapter = new LiveCategoryAdapter(liveCategoryList, getContext());
                                liveCateRecyclerview.setAdapter(liveCategoryAdapter);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                    }
                });*/
            }
        });

        categories_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categories_page.setCardBackgroundColor(Color.parseColor("#ff0000"));
                runmawi_page.setCardBackgroundColor(Color.parseColor("#000000"));
                movie_page.setCardBackgroundColor(Color.parseColor("#000000"));
                
                Log.d("CATEGORIES_DEBUG", "Categories tab clicked, activeTabType=" + activeTabType);

                // If cateRecyclerView is currently visible, hide it and restore slider visibility for the active tab.
                if (cateRecyclerView.getVisibility() == View.VISIBLE) {
                    Log.d("CATEGORIES_DEBUG", "Categories (cateRecyclerView) were visible, now hiding.");
                    cateRecyclerView.setVisibility(View.GONE);
                    // Ensure the correct slider for the active tab becomes visible again if it was hidden by old logic
                    if (activeTabType.equals("movies")) {
                        if (viewPager != null) viewPager.setVisibility(View.VISIBLE);
                    } else { // runmawi_tv
                        if (runmawiTvViewPager != null) runmawiTvViewPager.setVisibility(View.VISIBLE);
                    }
                    return;
                }

                // If we are here, it means cateRecyclerView was hidden, and we need to show it.
                // The underlying content (sliders, main content areas) should remain visible as categories overlay them.
                Log.d("CATEGORIES_DEBUG", "Categories (cateRecyclerView) were hidden, now showing for " + activeTabType);

                if (activeTabType.equals("movies")) {
                    Log.d("CATEGORIES_DEBUG", "Setting up movie categories on cateRecyclerView");
                    // Ensure movie tab's main content and slider are visible
                    if (activity_main != null) activity_main.setVisibility(View.VISIBLE);
                    if (viewPager != null) viewPager.setVisibility(View.VISIBLE);
                    // Ensure Runmawi TV specific content is GONE (should be handled by movie_page click listener)
                    if (runmawiTvContentContainer != null) runmawiTvContentContainer.setVisibility(View.GONE);
                    if (runmawiTvViewPager != null) runmawiTvViewPager.setVisibility(View.GONE);

                    if (allCateList == null || allCateList.isEmpty()) {
                        Toast.makeText(getContext(), "No movie categories available", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (cateList == null) cateList = new ArrayList<>(); else cateList.clear();
                    for (categorylist category : allCateList) {
                        String name = category.getName() != null ? category.getName().toLowerCase() : "";
                        if (name.contains("movie") || name.contains("film") || name.contains("cinema") ||
                            (!name.contains("tv") && !name.contains("live") && !name.contains("news"))) {
                            cateList.add(category);
                        }
                    }
                    if (cateList.isEmpty() && allCateList != null) cateList.addAll(allCateList);

                    Log.d("CATEGORIES_DEBUG", "Found " + cateList.size() + " movie categories to display.");
                    genreCategoriesAdapter = new GenreCategoriesAdapter(cateList, getContext());
                    cateRecyclerView.setAdapter(genreCategoriesAdapter);
                    if (cateRecyclerView.getLayoutManager() == null) {
                        cateRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    }
                    cateRecyclerView.setVisibility(View.VISIBLE); // Show movie categories overlaying current content

                } else { // runmawi_tv
                    Log.d("CATEGORIES_DEBUG", "Setting up live TV categories on cateRecyclerView");
                    // Ensure Runmawi TV tab's main content and slider are visible
                    if (runmawiTvContentContainer != null) runmawiTvContentContainer.setVisibility(View.VISIBLE);
                    if (allChannelRecycler != null) allChannelRecycler.setVisibility(View.VISIBLE);
                    if (runmawiTvViewPager != null) runmawiTvViewPager.setVisibility(View.VISIBLE);
                    // Ensure Movie specific content is GONE (should be handled by runmawi_page click listener)
                    if (activity_main != null) activity_main.setVisibility(View.GONE);
                    if (viewPager != null) viewPager.setVisibility(View.GONE);
                    
                    showLiveCategories(); // This method sets the adapter on cateRecyclerView and makes it VISIBLE
                }
            }
        });


        genrerecyclerview.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (genreList.get(position).getGenre_name().equalsIgnoreCase("Movies")) {
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                    movieRecyclerview.setVisibility(View.GONE);
                    cateRecyclerView.setVisibility(View.GONE);
                } else if (genreList.get(position).getGenre_name().equalsIgnoreCase("Runmawi TV")) {
                    swipeRefreshLayout.setVisibility(View.GONE);
                    movieRecyclerview.setVisibility(View.VISIBLE);
                    cateRecyclerView.setVisibility(View.GONE);

                    Call<JSONResponse> cate_api = ApiClient.getInstance1().getApi().getChannelVideo("12");
                    cate_api.enqueue(new Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                            JSONResponse jsonResponse = response.body();

                            if (response.isSuccessful()) {
                                movieCategoryList = new ArrayList<>(Arrays.asList(jsonResponse.getCategoryVideos()));
                                movieCategoryAdapter = new MovieCategoryAdapter(movieCategoryList, getContext());
                                movieRecyclerview.setAdapter(movieCategoryAdapter);
                            }
                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable throwable) {
                        }
                    });

                } else if (genreList.get(position).getGenre_name().equalsIgnoreCase("Categories")) {
                    if (cateRecyclerView.getVisibility() == View.VISIBLE) {
                        cateRecyclerView.setVisibility(View.GONE);
                    } else {
                        cateRecyclerView.setVisibility(View.VISIBLE);
                    }
                    //showCategoryMenu(getContext());
                }
            }
        }));

        final Handler handler = new Handler();
        final int slidertodalsize = sliderdata.size();
        final int videoTotalSize1 = livebannerdata.size();
        final int videoTotalSize2 = videobanner.size();
        final int series = bannersdata.size();
        final int totalSize = videoTotalSize1 + videoTotalSize2 + series + slidertodalsize;

        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }

                currentPage = viewPager.getCurrentItem() + 1;
                viewPager.setCurrentItem(currentPage, true);

                if (currentPage == sliderdata.size() + livebannerdata.size() + videobanner.size() + bannersdata.size()) {
                    currentPage = 0;
                    viewPager.setCurrentItem(0, true);
                }
            }
        };

// Start the automatic page transition
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 1000, 5000);


        userdetailsmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_id != null) {
                    Intent in = new Intent(getContext(), EditProfileActivity.class);
                    startActivity(in);
                   /* drawer_layout.closeDrawer(GravityCompat.START);
                    Fragment fragment = new UserprofilFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.drawer_layout, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();*/
                } else {
                    Intent in = new Intent(getContext(), MailandOtpLoginActivity.class);
                    startActivity(in);
                }
            }
        });


        final Call<JSONResponse> bannerreq = ApiClient.getInstance1().getApi().getBanners();
        bannerreq.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();

                if (jsonResponse.getLive_banner().length == 0 && jsonResponse.getSeries_banner().length == 0 && jsonResponse.getVideo_banners().length == 0 && jsonResponse.getSliders().length == 0) {
                    activity_main.setVisibility(View.GONE);
                } else {
                    activity_main.setVisibility(View.VISIBLE);
                    livebannerdata = new ArrayList<>(Arrays.asList(jsonResponse.getLive_banner()));
                    bannersdata = new ArrayList<>(Arrays.asList(jsonResponse.getSeries_banner()));
                    videobanner = new ArrayList<>(Arrays.asList(jsonResponse.getVideo_banners()));
                    sliderdata = new ArrayList<>(Arrays.asList(jsonResponse.getSliders()));
                    
                    // Set up main ViewPager for Movies tab - only show videobanner
                    viewPagerAdapter1 = new ViewPagerAdapter1(null, null, null, videobanner, getContext());
                    viewPager.setAdapter(viewPagerAdapter1);
                    bannerprogress.setVisibility(View.GONE);
                    
                    // Set up runmawi TV ViewPager with only livebannerdata
                    runmawiTvViewPagerAdapter = new ViewPagerAdapter1(null, null, livebannerdata, null, getContext());
                    runmawiTvViewPager.setAdapter(runmawiTvViewPagerAdapter);
                    runmawiTvBannerContainer.findViewById(R.id.runmawi_tv_bannerprogress).setVisibility(View.GONE);
                    
                    // Make sure the Runmawi TV content is visible on initial load
                    runmawiTvContentContainer.setVisibility(View.VISIBLE);
                    activity_main.setVisibility(View.GONE);
                }


            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        continuwatchilinglist.clear();
                        categoryList.clear();
                        Call<JSONResponse> callelist = ApiClient.getInstance1().getApi().getContinelisting(user_id);
                        callelist.enqueue(new retrofit2.Callback<JSONResponse>() {
                            @Override
                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                JSONResponse jsonResponse = response.body();

                                if (jsonResponse.getVideos().length == 0) {
                                    continueREcycler.setVisibility(View.GONE);
                                    continue_watching_layour.setVisibility(View.GONE);
                                } else {

                                    continue_watching_layour.setVisibility(View.VISIBLE);
                                    continueREcycler.setVisibility(View.VISIBLE);
                                    continuwatchilinglist = new ArrayList<>(Arrays.asList(jsonResponse.getVideos()));
                                    Collections.reverse(continuwatchilinglist);
                                    continueWatchingAdopter = new ContinueWatchingAdopter(continuwatchilinglist);
                                    continueREcycler.setAdapter(continueWatchingAdopter);
                                    continueWatchingAdopter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                            }
                        });
                        page = 1;
                        getDataFromAPI2(page, limit);
                    }
                }
        );


        final Handler handler223 = new Handler();
        final Runnable Update223 = new Runnable() {
            public void run() {

                Call<JSONResponse> callelist = ApiClient.getInstance1().getApi().getContinelisting(user_id);
                callelist.enqueue(new retrofit2.Callback<JSONResponse>() {
                    @Override
                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            JSONResponse jsonResponse = response.body();

                            if (jsonResponse.getVideos().length == 0) {

                                continueREcycler.setVisibility(View.GONE);
                                continue_watching_layour.setVisibility(View.GONE);

                            } else {

                                continue_watching_layour.setVisibility(View.VISIBLE);
                                continueREcycler.setVisibility(View.VISIBLE);
                                continuwatchilinglist = new ArrayList<>(Arrays.asList(jsonResponse.getVideos()));
                                Collections.reverse(continuwatchilinglist);
                                continueWatchingAdopter = new ContinueWatchingAdopter(continuwatchilinglist);
                                continueREcycler.setAdapter(continueWatchingAdopter);
                                continueWatchingAdopter.notifyDataSetChanged();

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
        }, 1000, 10000);


        continue_text.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ContinueWatchingSeeall.class);
            startActivity(intent);
        });

        continueREcycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (continuwatchilinglist.size() > position) {
                            if (continuwatchilinglist.get(position) != null) {

                                String videourl;

                                if (continuwatchilinglist.get(position).getMp4_url() == null) {
                                    videourl = continuwatchilinglist.get(position).getTrailer();
                                } else {
                                    videourl = continuwatchilinglist.get(position).getMp4_url();
                                }

                                Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                in.putExtra("id", continuwatchilinglist.get(position).getId());
                                in.putExtra("url", videourl);
                                in.putExtra("xtra", "");
                                in.putExtra("ads", "");
                                startActivity(in);
                            }
                        }
                    }
                }));


        if (user_id != null) {

            Call<JSONResponse> profileres = ApiClient.getInstance1().getApi().getUserprofile(user_id);
            profileres.enqueue(new retrofit2.Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase("true")) {

                            JSONResponse jsonResponse = response.body();
                            user_detailsdata = new ArrayList<>(Arrays.asList(jsonResponse.getUser_details()));

                            for (int k = 0; k < user_detailsdata.size(); k++) {

                                String name = user_detailsdata.get(k).getUsername();
                                sidemenuusername.setText(name);
                                sidemenuuseremail.setText(user_detailsdata.get(k).getEmail());


                                Picasso.get()
                                        .load(user_detailsdata.get(k).getProfile_url())
                                        .placeholder(R.drawable.ic_baseline_account_circle_24)
                                        .transform(new CircleTransform())
                                        .into(sidemenudp);

                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<JSONResponse> call, Throwable t) {
                }
            });
        } else {

            sidemenuusername.setText("Login");
            sidemenuuseremail.setText("For Better Experience");
            Picasso.get().load("https://runmawi.com/public/uploads/avatars/default.png").into(sidemenudp);

        }

        Call<JSONResponse> callser = ApiClient.getInstance1().getApi().getCmsPage();
        callser.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                if (jsonResponse.getStatus().equalsIgnoreCase("true")) {
                    pageslist = new ArrayList<>(Arrays.asList(jsonResponse.getPages()));
                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
            }
        });


        sidemenu1recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(this.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        if (menuslist.size() > position) {
                            if (menuslist.get(position) != null) {

                                if (menuslist.get(position).getName().equalsIgnoreCase("Home")) {
                                    drawer_layout.closeDrawer(GravityCompat.START);

                                } else if (menuslist.get(position).getName().equalsIgnoreCase(" Categories")) {
                                    Intent in = new Intent(getContext(), ChannelActivity.class);
                                    in.putExtra("name", menuslist.get(position).getName());
                                    startActivity(in);
                                } else if (menuslist.get(position).getName().equalsIgnoreCase("Movies")) {

                                    Intent in = new Intent(getContext(), LatestvideosPageActivity.class);
                                    in.putExtra("name", menuslist.get(position).getName());
                                    startActivity(in);

                                } else if (menuslist.get(position).getName().equalsIgnoreCase("Latest Videos")) {
                                    Intent in = new Intent(getContext(), LatestvideosPageActivity.class);
                                    in.putExtra("name", menuslist.get(position).getName());
                                    startActivity(in);
                                } else if (menuslist.get(position).getName().equalsIgnoreCase("series")) {
                                    Intent in = new Intent(getContext(), SeriesActivity.class);
                                    startActivity(in);
                                } else if (menuslist.get(position).getName().equalsIgnoreCase("Audio")) {
                                    Intent in = new Intent(getContext(), AudioCategoryActivity.class);
                                    in.putExtra("name", menuslist.get(position).getName());
                                    startActivity(in);
                                } else if (menuslist.get(position).getName().equalsIgnoreCase("Live")) {

                                    Intent in = new Intent(getContext(), ChannalPageActivity1.class);
                                    in.putExtra("id", "live_videos");
                                    in.putExtra("name", menuslist.get(position).getName());
                                    in.putExtra("number", "x");
                                    startActivity(in);
                                } else if (menuslist.get(position).getName().equalsIgnoreCase("Channels")) {

                                    Intent in = new Intent(getContext(), ChannalPageActivity1.class);
                                    in.putExtra("id", "ChannelPartner");
                                    in.putExtra("name", menuslist.get(position).getName());
                                    in.putExtra("number", "x");
                                    startActivity(in);
                                } else if (menuslist.get(position).getName().equalsIgnoreCase("My Favourite")) {
                                    Intent in = new Intent(getContext(), MyFavouriteActivity.class);
                                    startActivity(in);
                                } else if (menuslist.get(position).getName().equalsIgnoreCase("My WishList")) {
                                    Intent in = new Intent(getContext(), WatchlistActivity.class);
                                    startActivity(in);
                                } else if (menuslist.get(position).getName().equalsIgnoreCase("Join Membership")) {

                                    Intent in = new Intent(getContext(), SubscribeActivity.class);
                                    startActivity(in);

                                } else if (menuslist.get(position).getName().equalsIgnoreCase("About us")) {

                                    for (int i = 0; i < pageslist.size(); i++) {
                                        if (pageslist.get(i).getTitle().equalsIgnoreCase("About Us")) {
                                            Intent in = new Intent(getContext(), FaqActivity.class);
                                            in.putExtra("title", menuslist.get(position).getName());
                                            in.putExtra("body", pageslist.get(i).getBody());
                                            startActivity(in);
                                        }
                                    }

                                } else if (menuslist.get(position).getName().equalsIgnoreCase("Contact")) {

                                    for (int i = 0; i < pageslist.size(); i++) {
                                        if (pageslist.get(i).getTitle().equalsIgnoreCase("Contact")) {
                                            Intent in = new Intent(getContext(), FaqActivity.class);
                                            in.putExtra("title", menuslist.get(position).getName());
                                            in.putExtra("body", pageslist.get(i).getBody());
                                            startActivity(in);
                                        }
                                    }

                                } else if (menuslist.get(position).getName().equalsIgnoreCase("Coming soon")) {
                                    Intent intent = new Intent(getContext(), ComingSoon.class);
                                    intent.putExtra("name", menuslist.get(position).getName());
                                    startActivity(intent);

                                } else {
                                    drawer_layout.closeDrawer(GravityCompat.START);
                                }

                            }
                        }

                    }
                }));

        Call<JSONResponse> req = ApiClient.getInstance1().getApi().getMenu();
        req.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                menuslist = new ArrayList<>(Arrays.asList(jsonResponse.getMenus()));
                sidemennuAdopter1 = new sidemennuAdopter(menuslist);
                sidemenu1recycler.setAdapter(sidemennuAdopter1);
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
            }
        });


        getDataFromAPI2(page, limit);

        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    page++;
                    loadingPB.setVisibility(View.VISIBLE);
                    getDataFromAPI2(page, limit);
                }
            }

        });

        return root;

    }

    /**
     * Filter categories based on which tab is currently active (Movies or Runmawi TV)
     * and display them in the appropriate adapter
     */
    private void filterCategoriesByActiveTab() {
        if (activeTabType.equals("movies")) {
            // For Movies tab, filter from allCateList
            if (allCateList == null || allCateList.isEmpty()) {
                return; // No categories to filter
            }
            
            // Clear current category list
            cateList = new ArrayList<>();
            
            // Filter movie categories based on naming patterns
            for (categorylist category : allCateList) {
                String name = category.getName() != null ? category.getName().toLowerCase() : "";
                
                // Add movie-related categories
                if (name.contains("movie") || name.contains("film") || 
                    name.contains("cinema") || 
                    (!name.contains("tv") && !name.contains("live") && !name.contains("news"))) {
                    cateList.add(category);
                }
            }
            
            // If no categories matched our filter, show all categories
            if (cateList.isEmpty()) {
                cateList.addAll(allCateList);
            }
            
            // Update the movie categories adapter
            if (genreCategoriesAdapter != null) {
                genreCategoriesAdapter = new GenreCategoriesAdapter(cateList, getContext());
                cateRecyclerView.setAdapter(genreCategoriesAdapter);
                cateRecyclerView.setVisibility(View.VISIBLE);
            }
        } else { 
            // For Runmawi TV tab, we'll use liveCategoryList in showLiveCategories()
            // which is called when the Categories tab is clicked
        }
    }
    
    /**
     * Show live categories when in Runmawi TV tab and Categories is clicked
     */
    private void showLiveCategories() {
        Log.d("CATEGORIES_DEBUG", "showLiveCategories called, activeTabType=" + activeTabType);
        
        if (liveCategoriesForDisplay == null || liveCategoriesForDisplay.isEmpty()) {
            Log.d("CATEGORIES_DEBUG", "No live categories available");
            Toast.makeText(getContext(), "No live categories available", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Log.d("CATEGORIES_DEBUG", "Preparing to show " + liveCategoriesForDisplay.size() + " live categories");
        
        // Log each category for verification
        for (int i = 0; i < liveCategoriesForDisplay.size(); i++) {
            data category = liveCategoriesForDisplay.get(i);
            Log.d("CATEGORIES_DEBUG", "Live category " + i + ": Name=" + category.getName());
        }
        
        // Make sure the RecyclerView is properly configured
        if (cateRecyclerView == null) {
            Log.e("CATEGORIES_DEBUG", "cateRecyclerView is null");
            return;
        }
        
        // Make sure the RecyclerView's parent is visible
        ViewGroup parent = (ViewGroup) cateRecyclerView.getParent();
        if (parent != null) {
            parent.setVisibility(View.VISIBLE);
        }
        
        // Create adapter with the preserved copy of live categories list and a click listener
        LiveCategoriesSimpleAdapter simpleLiveCategoriesAdapter = 
                new LiveCategoriesSimpleAdapter(liveCategoriesForDisplay, getContext(), new LiveCategoriesSimpleAdapter.OnCategoryClickListener() {
                    @Override
                    public void onCategoryClick(data selectedCategory) {
                        Log.d("LIVE_CATEGORY_DEBUG", "Live category clicked (from adapter): " + selectedCategory.getName());
                        
                        // Determine the category ID to use for the API call
                        String categoryId;
                        if (selectedCategory.getId() != null) {
                            categoryId = selectedCategory.getId();
                            Log.d("LIVE_CATEGORY_DEBUG", "Using ID: " + categoryId);
                        } else if (selectedCategory.getId1() != null) {
                            categoryId = selectedCategory.getId1();
                            Log.d("LIVE_CATEGORY_DEBUG", "Using ID1: " + categoryId);
                        } else {
                            // Generate ID from category name if no ID is available
                            categoryId = "category_" + selectedCategory.getName().toLowerCase().replace(" ", "_");
                            Log.d("LIVE_CATEGORY_DEBUG", "Using generated ID: " + categoryId);
                        }
                        
                        // Make API call to get live content for this category
                        Call<JSONResponse> liveCategoryApi = ApiClient.getInstance1().getApi().getLiveCat(categoryId);
                        liveCategoryApi.enqueue(new Callback<JSONResponse>() {
                            @Override
                            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                                try {
                                    if (response.isSuccessful() && response.body() != null) {
                                        JSONResponse jsonResponse = response.body();
                                        Log.d("LIVE_CATEGORY_DEBUG", "API response received for category: " + selectedCategory.getName());
                                        
                                        // Hide categories list
                                        cateRecyclerView.setVisibility(View.GONE);
                                        
                                        // Check if the response contains LiveCategory data
                                        if (jsonResponse.getLiveCategory() != null && jsonResponse.getLiveCategory().length > 0) {
                                            // Hide the all channels recycler view first
                                            if (allChannelRecycler != null) {
                                                allChannelRecycler.setVisibility(View.GONE);
                                            }
                                            
                                            // Get the filtered live content
                                            ArrayList<LiveCategory> filteredLiveContent = new ArrayList<>(Arrays.asList(jsonResponse.getLiveCategory()));
                                            
                                            // Check if we have an adapter for this content type
                                            if (filteredLiveContent.size() > 0) {
                                                // Create an adapter for the filtered content
                                                // Note: We need to use the appropriate adapter for LiveCategory objects
                                                livepageAdopter filteredAdapter = new livepageAdopter(filteredLiveContent, getContext());
                                                
                                                // Show and update allChannelRecycler with filtered content
                                                allChannelRecycler.setAdapter(filteredAdapter);
                                                allChannelRecycler.setVisibility(View.VISIBLE);
                                                
                                                // Add a title or indicator showing which category is being displayed
                                                Toast.makeText(getContext(), "Showing: " + selectedCategory.getName(), Toast.LENGTH_SHORT).show();
                                                
                                                Log.d("LIVE_CATEGORY_DEBUG", "Displaying " + filteredLiveContent.size() + 
                                                        " channels for category: " + selectedCategory.getName());
                                            } else {
                                                // No content found for this category
                                                Toast.makeText(getContext(), "No channels found in " + selectedCategory.getName(), Toast.LENGTH_SHORT).show();
                                                Log.d("LIVE_CATEGORY_DEBUG", "No channels found for category: " + selectedCategory.getName());
                                                
                                                // Show empty state or fallback to all channels
                                                allChannelRecycler.setVisibility(View.VISIBLE);
                                            }
                                        } else {
                                            // No content found for this category
                                            Toast.makeText(getContext(), "No channels found in " + selectedCategory.getName(), Toast.LENGTH_SHORT).show();
                                            Log.d("LIVE_CATEGORY_DEBUG", "No LiveCategory data in response for: " + selectedCategory.getName());
                                            
                                            // Show all channels as fallback
                                            allChannelRecycler.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        // API call failed
                                        Log.e("LIVE_CATEGORY_DEBUG", "API call failed for category: " + selectedCategory.getName());
                                        Toast.makeText(getContext(), "Failed to load channels for " + selectedCategory.getName(), Toast.LENGTH_SHORT).show();
                                        
                                        // Show all channels as fallback
                                        allChannelRecycler.setVisibility(View.VISIBLE);
                                    }
                                } catch (Exception e) {
                                    // Catch any exceptions to prevent app crashes
                                    Log.e("LIVE_CATEGORY_DEBUG", "Exception processing response: " + e.getMessage());
                                    Toast.makeText(getContext(), "Error processing data for " + selectedCategory.getName(), Toast.LENGTH_SHORT).show();
                                    
                                    // Show all channels as fallback
                                    allChannelRecycler.setVisibility(View.VISIBLE);
                                }
                            }
                            
                            @Override
                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                Log.e("LIVE_CATEGORY_DEBUG", "API call failed: " + t.getMessage());
                                Toast.makeText(getContext(), "Network error. Please try again.", Toast.LENGTH_SHORT).show();
                                
                                // Show all channels as fallback
                                allChannelRecycler.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                });
        
        // Set up the RecyclerView with the correct layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        cateRecyclerView.setLayoutManager(layoutManager);
        
        // Make sure the RecyclerView has the correct properties
        cateRecyclerView.setHasFixedSize(true);
        cateRecyclerView.setNestedScrollingEnabled(true);
        
        // Set the adapter and make sure the RecyclerView is visible
        cateRecyclerView.setAdapter(simpleLiveCategoriesAdapter);
        cateRecyclerView.setVisibility(View.VISIBLE);
        
        // Force layout refresh
        cateRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                simpleLiveCategoriesAdapter.notifyDataSetChanged();
            }
        });
        
        Log.d("CATEGORIES_DEBUG", "LiveCategoriesSimpleAdapter set on RecyclerView with click listener");
    }
    
    private void clearNotifications(String userId) {
        Call<ReadNotification> call = ApiClient.getInstance1().getApi().getClearAll(userId);
        call.enqueue(new retrofit2.Callback<ReadNotification>() {
            @Override
            public void onResponse(Call<ReadNotification> call, retrofit2.Response<ReadNotification> response) {
                ReadNotification jsonResponse = response.body();
            }

            @Override
            public void onFailure(Call<ReadNotification> call, Throwable t) {

            }
        });
    }
    
    private void getDataFromAPI2(int page, int limit) {
        loader_layout.setVisibility(View.GONE);
        allChannelRecycler.setVisibility(View.VISIBLE);
        if (page > limit) {
            loadingPB.setVisibility(View.GONE);
            return;
        }
        Call<HomeBodyResponse> allChannel = RetrofitSingleton.getInstance().getApi().getHomeCategory(user_id, page);
        allChannel.enqueue(new retrofit2.Callback<HomeBodyResponse>() {
            @Override
            public void onResponse(Call<HomeBodyResponse> call, retrofit2.Response<HomeBodyResponse> response) {


                if (response.isSuccessful()) {
                    for (HomeCategoryData data : response.body().getHome_page()) {
                        categoryList.add(data);
                    }
                    swipeRefreshLayout.setRefreshing(false);
                    all_adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<HomeBodyResponse> call, Throwable t) {
            }
        });

    }

    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    public static void showCategoryMenu(Context context) {

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_category_menu); // Create an XML layout for this
        dialog.setCancelable(true);

        // Set window properties
        /*Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.END); // Open from right side
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setWindowAnimations(R.style.RightToLeftDialogAnimation); // Custom animation
        }*/

        /*// Example list setup
        ListView listView = dialog.findViewById(R.id.listViewCategories);
        String[] categories = {"Electronics", "Clothing", "Home & Kitchen", "Books", "Toys"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, categories);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(context, "Selected: " + categories[position], Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });*/

        dialog.show();
    }
}