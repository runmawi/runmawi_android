package com.atbuys.runmawi;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;


public class LatestvideosPageFragment extends Fragment {



    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<latestvideos> channelvideosdata;
    LatestvideosAdapter channelvideoAdapter;
    String id,name;
    TextView channelid;
    LinearLayout novideoscat;
    String user_id,user_role;
    private ArrayList<videodetail> movie_detaildata;

    RelativeLayout progresslayout;








        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_latest_videos, container, false);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        Toolbar mToolbar = (Toolbar)root. findViewById(R.id.toolbar);

            mToolbar.setNavigationIcon(R.drawable.back_arrow);

        channelid =(TextView) root.findViewById(R.id.namee);
        novideoscat =(LinearLayout) root.findViewById(R.id.novideoscat);
        progresslayout =(RelativeLayout) root.findViewById(R.id.progresslayout);


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(getActivity(), HomePageActivitywithFragments.class);
                startActivity(in);
                getActivity().finish();

            }
        });




         //   getActivity().getIntent().getExtras().getString("name");

        movie_detaildata = new ArrayList<videodetail>();
        channelvideosdata = new ArrayList<latestvideos>();

        adapter = new LatestvideosAdapter(channelvideosdata, getActivity());
        layoutManager = new GridLayoutManager(getActivity(), 3);



            recyclerView = root.findViewById(R.id.rv_main);
        progressBar = root.findViewById(R.id.pb_home);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

            SharedPreferences  prefs = this.getContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);

            user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);


        Call<JSONResponse> channel = ApiClient.getInstance1().getApi().getLatestVideos();
        channel.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                progresslayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                JSONResponse jsonResponse = response.body();

                if (jsonResponse.getLatestvideos().length ==0) {


                    progresslayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    novideoscat.setVisibility(View.VISIBLE);

                }
                else
                {
                    novideoscat.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    channelvideosdata = new ArrayList<>(Arrays.asList(jsonResponse.getLatestvideos()));
                    channelvideoAdapter = new LatestvideosAdapter(channelvideosdata);
                    recyclerView.setAdapter(channelvideoAdapter);

                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });



        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (channelvideosdata.size() > position) {
                            if (channelvideosdata.get(position) != null)

                            {

                                if(channelvideosdata.get(position).getType() == null)
                                {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, channelvideosdata.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                                            if (user_id == null) {


                                                Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                in.putExtra("id", channelvideosdata.get(position).getId());
                                                in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                startActivity(in);

                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (channelvideosdata.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((channelvideosdata.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = channelvideosdata.get(position).getVideo_url()+channelvideosdata.get(position).getPath()+".m3u8";

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    } else {

                                                        Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((channelvideosdata.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (channelvideosdata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getVideo_url()+channelvideosdata.get(position).getPath()+".m3u8");
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    }
                                                } else if (channelvideosdata.get(position).getAccess().equalsIgnoreCase("ppv") || channelvideosdata.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    startActivity(in);
                                                } else {

                                                    if (channelvideosdata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getVideo_url()+channelvideosdata.get(position).getPath()+".m3u8");
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
                             else   if (channelvideosdata.get(position).getType().equalsIgnoreCase("embed")) {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, channelvideosdata.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));

                                            if (user_id == null) {
                                                Intent in = new Intent(getActivity(), TrailerPlayerActivity.class);
                                                in.putExtra("id", channelvideosdata.get(position).getId());
                                                in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                startActivity(in);
                                            }

                                            if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (response.body().getPpv_video_status().equalsIgnoreCase("pay_now")) {
//                                                    Intent in = new Intent(getActivity(), YoutubeVideoHomepageActivity.class);
//                                                    in.putExtra("id", channelvideosdata.get(position).getId());
//                                                    startActivity(in);

                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

//                                                    Intent in = new Intent(getActivity(), YoutubeVideoHomepageActivity.class);
//                                                    in.putExtra("id", channelvideosdata.get(position).getId());
//                                                    startActivity(in);

                                                } else {

//                                                    Intent in = new Intent(getActivity(), YoutubeVideoHomepageActivity.class);
//                                                    in.putExtra("id", channelvideosdata.get(position).getId());
//                                                    startActivity(in);

                                                }

                                            } else {

//                                                Intent in = new Intent(getActivity(), YoutubeVideoHomepageActivity.class);
//                                                in.putExtra("id", channelvideosdata.get(position).getId());
//                                                startActivity(in);
//

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                                            Log.d("Error", t.getMessage());
                                        }
                                    });

                                }
                                else if (channelvideosdata.get(position).getType().equalsIgnoreCase("mp4_url")) {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, channelvideosdata.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                                            if (user_id == null) {


                                                Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                in.putExtra("id", channelvideosdata.get(position).getId());
                                                in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                startActivity(in);

                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (channelvideosdata.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((channelvideosdata.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = channelvideosdata.get(position).getMp4_url();

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    } else {

                                                        Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((channelvideosdata.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (channelvideosdata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getMp4_url());
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    }
                                                } else if (channelvideosdata.get(position).getAccess().equalsIgnoreCase("ppv") || channelvideosdata.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    startActivity(in);
                                                } else {

                                                    if (channelvideosdata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getMp4_url());
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
                                } else {
                                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, channelvideosdata.get(position).getId());
                                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();
                                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));


                                            if (user_id == null) {


                                                Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                in.putExtra("id", channelvideosdata.get(position).getId());
                                                in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                startActivity(in);

                                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {


                                                if (channelvideosdata.get(position).getAccess().equalsIgnoreCase("ppv")) {


                                                    Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                                    Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "norent");
                                                    startActivity(in);

                                                } else if ((channelvideosdata.get(position).getAccess().equalsIgnoreCase("subscriber"))) {

                                                    Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriber_content");
                                                    startActivity(in);

                                                } else {

                                                    String videourl = channelvideosdata.get(position).getVideo_url() + channelvideosdata.get(position).getPath() + ".m3u8";

                                                    if (videourl == null) {
                                                        Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    } else {

                                                        Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", videourl);
                                                        in.putExtra("xtra", "rentted");
                                                        startActivity(in);
                                                    }
                                                }

                                            } else {

                                                if ((channelvideosdata.get(position).getAccess().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view"))) {

                                                    if (channelvideosdata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getVideo_url() + channelvideosdata.get(position).getPath() + ".m3u8");
                                                        in.putExtra("xtra", "subscriberented");
                                                        startActivity(in);

                                                    }
                                                } else if (channelvideosdata.get(position).getAccess().equalsIgnoreCase("ppv") || channelvideosdata.get(position).getAccess().equalsIgnoreCase("expired")) {
                                                    Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                    in.putExtra("id", channelvideosdata.get(position).getId());
                                                    in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                    in.putExtra("xtra", "subscriberrent");
                                                    startActivity(in);
                                                } else {

                                                    if (channelvideosdata.get(position).getMp4_url() == null) {
                                                        Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getTrailer());
                                                        in.putExtra("xtra", "Norent");
                                                        startActivity(in);
                                                    } else {
                                                        Intent in = new Intent(getActivity(), HomePageVideoActivity.class);
                                                        in.putExtra("id", channelvideosdata.get(position).getId());
                                                        in.putExtra("url", channelvideosdata.get(position).getVideo_url() + channelvideosdata.get(position).getPath() + ".m3u8");
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

            return root;
        }

    public void onBackPressed() {
        Intent in = new Intent(getActivity(), HomePageActivitywithFragments.class);
        startActivity(in);
    }
}
