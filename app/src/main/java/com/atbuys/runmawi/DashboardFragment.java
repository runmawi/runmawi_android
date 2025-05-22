package com.atbuys.runmawi;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

public class DashboardFragment extends Fragment {
    @Nullable

    EditText searchtext;
    ImageView searchimage, cross, mic, not_found;
    TextView txt, storeart;
    LinearLayout detaillayout, generlayout;
    //ImageView logo;

    RecyclerView searchRecycler, searchRecycler3;

    SeriessearchAdapter pagesAdapter;
    channelvideosadaptersearch pagerAdapter1;
    CategoryvideosAdapter video_Categories;
    ppvVideosAdapter ppvVideosAdapter;
    ppvcategoryAdapter ppvcategoryAdapter;
    chennalCategoryAdapter chennalCategoryAdapter;

    private ArrayList<series> pageslist;
    private ArrayList<data> searchdatalist;
    private ArrayList<video_categories> videoslist;
    private ArrayList<video_artist> video_artistlist;
    private ArrayList<channel_category> channel_categorylist;
    private ArrayList<ppv_category> ppv_categorylist;
    private ArrayList<ppv_videos> ppv_videoslist;
    private ArrayList<videodetail> movie_detaildata;
    EditText edit;

    private RecyclerView.LayoutManager cmsManager, cmsManager1, cmsManager2, cmsManager3;
    String x, user_id, user_role;
    private static final int REQUEST_CODE = 1234;

    private ArrayList<categorylist> channelListData;
    private ArrayList<artistlist> artistlistsdata;

    RecyclerView allChannelRecycler;
    ProgressBar allChannelProgress;
    ChannelListSearchAdapter channelListAdapter;
    ChannelSearchAdopter channelSearchAdopter;
    ArtistSearchAdopter artistAdopter;
    ArtistvideosAdapter artistvideosAdapter;
    private RecyclerView.LayoutManager channelManager;
    private RecyclerView.LayoutManager channelManager1;
    private RecyclerView.LayoutManager artistManager;
    private ArrayList<user_details> user_detailsdata;

    CircleImageView profiledp;
    TextView category, artist;

    LinearLayout generlayout1;
    RecyclerView channelistrecycler1, artistRecycler;
    LinearLayout artsitlayout;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard, null);

        pageslist = new ArrayList<series>();
        searchdatalist = new ArrayList<data>();
        videoslist = new ArrayList<video_categories>();
        video_artistlist = new ArrayList<video_artist>();
        channel_categorylist = new ArrayList<channel_category>();
        ppv_categorylist = new ArrayList<ppv_category>();
        ppv_videoslist = new ArrayList<ppv_videos>();
        movie_detaildata = new ArrayList<videodetail>();


        edit = root.findViewById(R.id.edit);

        SharedPreferences prefs = this.getContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);

        pagesAdapter = new SeriessearchAdapter(searchdatalist, getContext());

        pagerAdapter1 = new channelvideosadaptersearch(searchdatalist, getContext());
        ppvVideosAdapter = new ppvVideosAdapter(ppv_videoslist, getContext());

        ppvcategoryAdapter = new ppvcategoryAdapter(ppv_categorylist, getContext());

        chennalCategoryAdapter = new chennalCategoryAdapter(channel_categorylist, getContext());
        video_Categories = new CategoryvideosAdapter(videoslist, getContext());
        artistvideosAdapter = new ArtistvideosAdapter(video_artistlist, getContext());

        channelListData = new ArrayList<categorylist>();
        artistlistsdata = new ArrayList<artistlist>();

        allChannelRecycler = (RecyclerView) root.findViewById(R.id.channelistrecycler);
        allChannelProgress = (ProgressBar) root.findViewById(R.id.catoryprogress);


        channelistrecycler1 = (RecyclerView) root.findViewById(R.id.channelistrecycler1);
        artistRecycler = (RecyclerView) root.findViewById(R.id.artistrecycler);


        category = (TextView) root.findViewById(R.id.cat);
        artist = (TextView) root.findViewById(R.id.artist);

        profiledp = (CircleImageView) root.findViewById(R.id.profiledp);


        channelListAdapter = new ChannelListSearchAdapter(channelListData, this);
        channelManager = new GridLayoutManager(getContext(), 1);


        channelSearchAdopter = new ChannelSearchAdopter(channelListData, this);
        channelManager1 = new GridLayoutManager(getContext(), 2);

        artistAdopter = new ArtistSearchAdopter(artistlistsdata, this.getContext());
        artistManager = new GridLayoutManager(getContext(), 2);

        artsitlayout = (LinearLayout) root.findViewById(R.id.artsitlayout);


        allChannelRecycler.setHasFixedSize(true);
        allChannelRecycler.setLayoutManager(channelManager);
        allChannelRecycler.setAdapter(channelListAdapter);

        channelistrecycler1.setHasFixedSize(true);
        channelistrecycler1.setLayoutManager(channelManager1);
        channelistrecycler1.setAdapter(channelSearchAdopter);


        artistRecycler.setHasFixedSize(true);
        artistRecycler.setLayoutManager(artistManager);
        artistRecycler.setAdapter(artistAdopter);

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                category.setBackgroundResource(R.drawable.border);
                artist.setBackgroundResource(R.drawable.borderstyle);
                artsitlayout.setVisibility(View.GONE);
                generlayout1.setVisibility(View.VISIBLE);
                storeart.setText("");
            }
        });


        artist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txt.setText("");
                category.setBackgroundResource(R.drawable.borderstyle);
                artist.setBackgroundResource(R.drawable.border);
                artsitlayout.setVisibility(View.VISIBLE);
                generlayout1.setVisibility(View.GONE);
            }
        });
        profiledp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent in = new Intent(getActivity(), MyAccountActivity.class);
                startActivity(in);

            }
        });


        allChannelRecycler.addOnItemTouchListener(
                (RecyclerView.OnItemTouchListener) new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (channelListData.size() > position) {
                            if (channelListData.get(position) != null) {

                                Intent in = new Intent(getActivity(), ChannalPageActivity.class);
                                in.putExtra("channelid", channelListData.get(position).getId());
                                in.putExtra("name", channelListData.get(position).getName());
                                startActivity(in);

                            }
                        }
                    }
                })
        );

        artistRecycler.addOnItemTouchListener(
                (RecyclerView.OnItemTouchListener) new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (artistlistsdata.size() > position) {
                            if (artistlistsdata.get(position) != null) {

                                storeart.setText(artistlistsdata.get(position).getId());

                                Call<JSONResponse> req = ApiClient.getInstance1().getApi().getSearchResult(searchtext.getText().toString());
                                req.enqueue(new retrofit2.Callback<JSONResponse>() {
                                    @Override
                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                        JSONResponse jsonResponse = response.body();


                                        detaillayout.setVisibility(View.VISIBLE);
                                        searchRecycler.setVisibility(View.GONE);
                                        searchRecycler3.setVisibility(View.GONE);


                                        video_artistlist = new ArrayList<>(Arrays.asList(jsonResponse.getVideo_artist()));
                                        artistvideosAdapter = new ArtistvideosAdapter(video_artistlist);
                                        searchRecycler.setAdapter(artistvideosAdapter);


                                    }


                                    @Override
                                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                                        Log.d("Error12", t.getMessage());
                                    }
                                });

                            }
                        }
                    }
                })
        );


        channelistrecycler1.addOnItemTouchListener(
                (RecyclerView.OnItemTouchListener) new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (channelListData.size() > position) {
                            if (channelListData.get(position) != null) {

                                txt.setText(channelListData.get(position).getId());


                                Call<JSONResponse> req = ApiClient.getInstance1().getApi().getSearchResult(searchtext.getText().toString());
                                req.enqueue(new retrofit2.Callback<JSONResponse>() {
                                    @Override
                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                        JSONResponse jsonResponse = response.body();


                                        detaillayout.setVisibility(View.VISIBLE);
                                        searchRecycler.setVisibility(View.GONE);
                                        searchRecycler3.setVisibility(View.GONE);


                                        videoslist = new ArrayList<>(Arrays.asList(jsonResponse.getVideo_categories()));
                                        video_Categories = new CategoryvideosAdapter(videoslist);
                                        searchRecycler.setAdapter(video_Categories);


                                    }


                                    @Override
                                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                                        Log.d("Error12", t.getMessage());
                                    }
                                });

                            }
                        }
                    }
                })
        );

        PackageManager pm = getActivity().getPackageManager();

        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);

        if (activities.size() == 0) {

        }

        searchtext = (EditText) root.findViewById(R.id.searchtext);
        searchimage = (ImageView) root.findViewById(R.id.search);
        cross = (ImageView) root.findViewById(R.id.cross);
        // logo = (ImageView) root.findViewById(R.id.logo);
        mic = (ImageView) root.findViewById(R.id.mic);
        detaillayout = (LinearLayout) root.findViewById(R.id.detailslayout);
        not_found = (ImageView) root.findViewById(R.id.not_found);
        txt = (TextView) root.findViewById(R.id.storecat);
        storeart = (TextView) root.findViewById(R.id.storeart);
        generlayout = (LinearLayout) root.findViewById(R.id.generlayout);
        generlayout1 = (LinearLayout) root.findViewById(R.id.generlayout1);


        searchRecycler = (RecyclerView) root.findViewById(R.id.searchdetails);
        searchRecycler3 = (RecyclerView) root.findViewById(R.id.searchdetails3);
        // catsearchREcycler  = (RecyclerView) root.findViewById(R.id.catsearchdetails);

        searchRecycler.setHasFixedSize(true);
        searchRecycler.setLayoutManager(new GridLayoutManager(getContext(), 1));
        searchRecycler.setAdapter(pagesAdapter);


        searchRecycler3.setHasFixedSize(true);
        searchRecycler3.setLayoutManager(new GridLayoutManager(getContext(), 3));
        searchRecycler3.setAdapter(chennalCategoryAdapter);


        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceRecognitionActivity();

            }
        });


        Call<JSONResponse> profileres = ApiClient.getInstance1().getApi().getUserprofile(user_id);
        profileres.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        JSONResponse jsonResponse = response.body();
                        user_detailsdata = new ArrayList<>(Arrays.asList(jsonResponse.getUser_details()));

                        for (int k = 0; k < user_detailsdata.size(); k++) {


                            String userprofile = user_detailsdata.get(k).getProfile_url();
                            Picasso.get().load(userprofile).fit().into(profiledp);


                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

                //  Toast.makeText(getContext(), "check your internet connection", Toast.LENGTH_LONG).show();
            }
        });


        Call<JSONResponse> channelser = ApiClient.getInstance1().getApi().getChannelList();

        channelser.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                // channelistrecycler1.setVisibility(View.VISIBLE);
                JSONResponse jsonResponse = response.body();

                channelListData = new ArrayList<>(Arrays.asList(jsonResponse.getCategorylist()));
                channelSearchAdopter = new ChannelSearchAdopter(channelListData);
                channelistrecycler1.setAdapter(channelSearchAdopter);

                txt.setText(channelListData.get(0).getId());


            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });


        Call<JSONResponse> channell = ApiClient.getInstance1().getApi().getArtistList();
        channell.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                artistRecycler.setVisibility(View.VISIBLE);
                JSONResponse jsonResponse = response.body();

                artistlistsdata = new ArrayList<>(Arrays.asList(jsonResponse.getArtistlist()));
                artistAdopter = new ArtistSearchAdopter(artistlistsdata);
                artistRecycler.setAdapter(artistAdopter);

                //    storeart .setText(artistlistsdata.get(0).getId());
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });



        Toolbar mToolbar = (Toolbar) root.findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

       /* Call<JSONResponse> req = ApiClient.getInstance1().getApi().getSearchResult(searchtext.getText().toString());
        req.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                if (jsonResponse.getData() == null || jsonResponse.getData().length == 0) {
                    not_found.setVisibility(View.VISIBLE);
                    detaillayout.setVisibility(View.VISIBLE);
                    searchRecycler.setVisibility(View.GONE);
                    searchRecycler3.setVisibility(View.GONE);
                } else {
                    not_found.setVisibility(View.GONE);
                    detaillayout.setVisibility(View.VISIBLE);
                    searchRecycler.setVisibility(View.GONE);
                    searchRecycler3.setVisibility(View.VISIBLE);

                    searchdatalist = new ArrayList<>(Arrays.asList(jsonResponse.getData()));
                    pagesAdapter = new SeriessearchAdapter(searchdatalist);
                    searchRecycler.setAdapter(pagesAdapter);

                    searchdatalist = new ArrayList<>(Arrays.asList(jsonResponse.getData()));
                    pagerAdapter1 = new channelvideosadaptersearch(searchdatalist);
                    searchRecycler3.setAdapter(pagerAdapter1);
                }

            }


            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
            }
        });*/

        searchtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {


                if (searchtext.getText().toString().isEmpty()) {
                    searchimage.setVisibility(View.VISIBLE);
                    cross.setVisibility(View.GONE);
                    mic.setVisibility(View.VISIBLE);
                    searchRecycler.setVisibility(View.GONE);
                    searchRecycler3.setVisibility(View.GONE);
                    detaillayout.setVisibility(View.VISIBLE);
                    generlayout.setVisibility(View.GONE);
                } else {

                    searchimage.setVisibility(View.VISIBLE);
                    cross.setVisibility(View.VISIBLE);
                    mic.setVisibility(View.GONE);
                    generlayout.setVisibility(View.GONE);

                    Call<JSONResponse> req = ApiClient.getInstance1().getApi().getSearchResult(searchtext.getText().toString());
                    req.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                            JSONResponse jsonResponse = response.body();
                            if (jsonResponse.getData() == null || jsonResponse.getData().length == 0) {
                                not_found.setVisibility(View.VISIBLE);
                                detaillayout.setVisibility(View.VISIBLE);
                                searchRecycler.setVisibility(View.GONE);
                                searchRecycler3.setVisibility(View.GONE);
                            } else {
                                not_found.setVisibility(View.GONE);
                                detaillayout.setVisibility(View.VISIBLE);
                                searchRecycler.setVisibility(View.GONE);
                                searchRecycler3.setVisibility(View.VISIBLE);

                                searchdatalist = new ArrayList<>(Arrays.asList(jsonResponse.getData()));
                                pagesAdapter = new SeriessearchAdapter(searchdatalist);
                                searchRecycler.setAdapter(pagesAdapter);

                                searchdatalist = new ArrayList<>(Arrays.asList(jsonResponse.getData()));
                                pagerAdapter1 = new channelvideosadaptersearch(searchdatalist);
                                searchRecycler3.setAdapter(pagerAdapter1);
                            }

                        }


                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Log.d("Error12", t.getMessage());
                        }
                    });

                }

            }
        });

        searchRecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (searchdatalist.size() > position) {
                            if (searchdatalist.get(position) != null) {

                                Call<JSONResponse> req = ApiClient.getInstance1().getApi().getSearchResult(searchtext.getText().toString());
                                req.enqueue(new retrofit2.Callback<JSONResponse>() {
                                    @Override
                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                        JSONResponse jsonResponse = response.body();


                                        detaillayout.setVisibility(View.VISIBLE);
                                        searchRecycler.setVisibility(View.GONE);
                                        searchRecycler3.setVisibility(View.VISIBLE);

                                        searchdatalist = new ArrayList<>(Arrays.asList(jsonResponse.getData()));
                                        pagerAdapter1 = new channelvideosadaptersearch(searchdatalist);
                                        searchRecycler3.setAdapter(pagerAdapter1);

                                    }


                                    @Override
                                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                                        Log.d("Error12", t.getMessage());
                                    }
                                });


                            }

                        }
                    }

                })
        );


        searchRecycler3.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (searchdatalist.size() > position) {
                            if (searchdatalist.get(position).getId() != null) {

                                if (searchdatalist.get(position).getSource().equalsIgnoreCase("Videos")) {

                                    Intent in = new Intent(getContext(), HomePageVideoActivity.class);
                                    in.putExtra("id", searchdatalist.get(position).getId());
                                    in.putExtra("url", "");
                                    in.putExtra("suburl", "");
                                    in.putExtra("data", "");
                                    in.putExtra("xtra", "");
                                    startActivity(in);
                                } else if (searchdatalist.get(position).getSource().equalsIgnoreCase("Livestream")) {

                                    Intent in = new Intent(getContext(), HomePageLiveActivity.class);
                                    in.putExtra("id", searchdatalist.get(position).getId());
                                    in.putExtra("url", "videourl");
                                    in.putExtra("xtra", "rentted");
                                    in.putExtra("data", "videos");
                                    in.putExtra("ads", "");
                                    startActivity(in);
                                } else if (searchdatalist.get(position).getSource().equalsIgnoreCase("Series")) {
                                    Intent in = new Intent(getContext(), SeasonEpisodeCopyActivity.class);
                                    in.putExtra("id", searchdatalist.get(position).getId());
                                    in.putExtra("image", searchdatalist.get(position).getImage_url());
                                    in.putExtra("desc", searchdatalist.get(position).getDescription());
                                    in.putExtra("title", searchdatalist.get(position).getTitle());
                                    startActivity(in);

                                }else if (searchdatalist.get(position).getSource().equalsIgnoreCase("audios")) {

                                }else if (searchdatalist.get(position).getSource().equalsIgnoreCase("Episode")) {
                                    Intent in = new Intent(getContext(), HomePageEpisodeActivity.class);
                                    in.putExtra("id", searchdatalist.get(position).getId());
                                    in.putExtra("url", searchdatalist.get(position).getMp4_url());
                                    in.putExtra("seasonid",searchdatalist.get(position).getSeason_id());
                                    in.putExtra("series_id",searchdatalist.get(position).getSeries_id());
                                    startActivity(in);
                                }

                            }
                        }
                    }
                }));

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchtext.setText("");
                detaillayout.setVisibility(View.GONE);
                searchimage.setVisibility(View.VISIBLE);

                mic.setVisibility(View.VISIBLE);
                cross.setVisibility(View.GONE);
            }
        });

        return root;


    }

    private void startVoiceRecognitionActivity() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice searching...");
        startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // Populate the wordsList with the String values the recognition engine thought it heard
            final ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (!matches.isEmpty()) {
                String Query = matches.get(0);
                searchtext.setText(Query);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void onBackPressed() {

        if (getFragmentManager() != null && getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        }

    }

}
