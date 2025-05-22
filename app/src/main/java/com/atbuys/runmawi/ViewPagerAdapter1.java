package com.atbuys.runmawi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;

public class ViewPagerAdapter1 extends PagerAdapter {
    private Context context;
    private ArrayList<videodetail> movie_detaildata;

    private LayoutInflater layoutInflater;
    private List<series_banner> sliderImg;
    private List<sliders> slidersList;
    private List<video_banner> video_banner;
    private List<live_banner> live_banner;
    private ArrayList<livedetail> livemovie_detaildata;

    private ImageLoader imageLoader;
    private static SharedPreferences prefs;
    String user_id,user_role;
    private String textsub,id,image,description,access;
    TextView viewpagerid;

    public ViewPagerAdapter1(List<sliders> slidersList,List<series_banner> sliderImg, List<live_banner> live_banner, List<video_banner> video_banner, Context context) {
        this.slidersList=slidersList;
        this.sliderImg = sliderImg;
        this.live_banner = live_banner;
        this.video_banner = video_banner;
        this.context = context;

        prefs = context.getSharedPreferences(sharedpreferences.My_preference_name, 0);
        user_id=prefs.getString(sharedpreferences.user_id,null);
        user_role=prefs.getString(sharedpreferences.role,null);

    }

    public int getCount() {
        int liveBannerSize = live_banner != null ? live_banner.size() : 0;
        int sliderImgSize = sliderImg != null ? sliderImg.size() : 0;
        int sliderSize = slidersList != null ? slidersList.size() : 0;
        int videoBannerSize = video_banner != null ? video_banner.size() : 0;

        // Calculate the total count of non-empty arrays
        int totalCount =liveBannerSize + sliderImgSize + videoBannerSize+sliderSize;
        return totalCount > 0 ? totalCount : 1; // Return at least 1 to avoid an empty ViewPager
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);


        ImageView imageView = view.findViewById(R.id.imageView);
        CardView play_now=view.findViewById(R.id.play_now);
        TextView tittle=view.findViewById(R.id.tittle);
        TextView genre=view.findViewById(R.id.genre);
        viewpagerid = view.findViewById(R.id.view1);

        if (slidersList != null && position < slidersList.size()) {
            sliders utils = slidersList.get(position);
            imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
            imageLoader.get(utils.getSlider(), ImageLoader.getImageListener(imageView, R.drawable.favicon,R.drawable.favicon));
            image =utils.getSlider();
            play_now.setVisibility(View.GONE);
        } else if (live_banner != null && position < live_banner.size()+ slidersList.size()) {
            // Get elements from live_banner list
            int videoPosition = position - slidersList.size();
            live_banner utils = live_banner.get(videoPosition);
            imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
            imageLoader.get(utils.getPlayer_image(), ImageLoader.getImageListener(imageView, R.drawable.favicon,R.drawable.favicon));
            //  value=utils.getSource();
            access=utils.getAccess();
            tittle.setText(utils.getTitle());

            id= utils.getId();
            image =utils.getPlayer_image();
            String title = utils.getTitle();

            play_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getLiveDetail(user_id,utils.getId());
                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                            JSONResponse jsonResponse = response.body();
                            livemovie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getLivedetail()));
                            if (user_id == null) {

                                Intent in = new Intent(context, HomePageLiveActivity.class);
                                in.putExtra("id",utils.getId());
                                in.putExtra("url", title);
                                in.putExtra("data", "videos");
                                in.putExtra("ads", "");
                                context.startActivity(in);

                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {

                                if (access.equalsIgnoreCase("ppv")) {

                                    Intent in = new Intent(context, HomePageLiveActivity.class);
                                    in.putExtra("id",utils.getId());
                                    in.putExtra("url", title);
                                    in.putExtra("xtra", "norent");
                                    in.putExtra("ads", "");
                                    in.putExtra("data", "videos");
                                    context.startActivity(in);

                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                    Log.d("nbdncbdbdncbx", "yyy");
                                    Intent in = new Intent(context, HomePageLiveActivity.class);
                                    in.putExtra("id",utils.getId());
                                    in.putExtra("url", title);
                                    in.putExtra("xtra", "norent");
                                    in.putExtra("ads", "");
                                    in.putExtra("data", "videos");
                                    context.startActivity(in);
                                } else if (access.equalsIgnoreCase("subscriber") ||access.equalsIgnoreCase("admin")) {

                                    Intent in = new Intent(context, HomePageLiveActivity.class);
                                    in.putExtra("id",utils.getId());
                                    in.putExtra("url", title);
                                    in.putExtra("xtra", "subscriber_content");
                                    in.putExtra("ads", "");
                                    in.putExtra("data", "videos");
                                    context.startActivity(in);
                                } else {

                                    //  String videourl = textViewGenre.getText().toString();

                                                /*Intent in = new Intent(getContext(), OnlinePlayerActivity.class);
                                                in.putExtra("id", movie_detaildata.get(position).getId());
                                                in.putExtra("url",videourl);
                                                startActivity(in);*/
                                    Intent in = new Intent(context, HomePageLiveActivity.class);
                                    in.putExtra("id",utils.getId());
                                    in.putExtra("url", title);
                                    in.putExtra("xtra", "rentted");
                                    in.putExtra("data", "videos");
                                    in.putExtra("ads", "");
                                    context.startActivity(in);
                                }

                            } else {

                                if (access.equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view")) {

                                    Intent in = new Intent(context, HomePageLiveActivity.class);
                                    in.putExtra("id",utils.getId());
                                    in.putExtra("url", title);
                                    in.putExtra("xtra", "subscriberented");
                                    in.putExtra("data", "videos");
                                    in.putExtra("ads", "");
                                    context.startActivity(in);

                                } else if (access.equalsIgnoreCase("ppv") || access.equalsIgnoreCase("expired")) {
                                    Intent in = new Intent(context, HomePageLiveActivity.class);
                                    in.putExtra("id",utils.getId());
                                    in.putExtra("url", title);
                                    in.putExtra("xtra", "subscriberrent");
                                    in.putExtra("data", "videos");
                                    in.putExtra("ads", "");
                                    context.startActivity(in);
                                } else {
                                    Intent in = new Intent(context, HomePageLiveActivity.class);
                                    in.putExtra("id",utils.getId());
                                    in.putExtra("url", title);
                                    in.putExtra("xtra", "Norent");
                                    in.putExtra("data", "videos");
                                    in.putExtra("ads", "");
                                    context.startActivity(in);
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Log.d("Error", t.getMessage());
                        }
                    });

                }





            });




        }
        else if (video_banner != null && position < live_banner.size() + video_banner.size()+slidersList.size()) {

            // Get elements from video_banner list
            int videoPosition = position -  (live_banner.size()+slidersList.size());
            video_banner utils2 = video_banner.get(videoPosition);
            imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
            imageLoader.get(utils2.getPlayer_image(), ImageLoader.getImageListener(imageView, R.drawable.favicon,R.drawable.favicon));
            textsub=utils2.getSource();
            access=utils2.getAccess();
            tittle.setText(utils2.getTitle());

            id= utils2.getId();
            image =utils2.getPlayer_image();
            String title = utils2.getTitle();
            // description =utils2.getDescription();

            play_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id,utils2.getId());
                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                            JSONResponse jsonResponse = response.body();
                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));

                            if (user_id == null) {

                                Intent in = new Intent(context, HomePageVideoActivity.class);
                                in.putExtra("id", utils2.getId());
                                in.putExtra("url", title);
                                in.putExtra("data", "videos");
                                in.putExtra("ads", "");
                                context.startActivity(in);

                            }

                            else if (user_id != null && user_role.equalsIgnoreCase("registered")) {

                                if (access.equalsIgnoreCase("ppv")) {

                                    Intent in = new Intent(context, HomePageVideoActivity.class);
                                    in.putExtra("id", utils2.getId());
                                    in.putExtra("url", title);
                                    in.putExtra("xtra", "norent");
                                    in.putExtra("ads", "");
                                    in.putExtra("data", "videos");
                                    context.startActivity(in);

                                }
                                else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                    Log.d("nbdncbdbdncbx", "yyy");
                                    Intent in = new Intent(context, HomePageVideoActivity.class);
                                    in.putExtra("id", utils2.getId());
                                    in.putExtra("url", title);
                                    in.putExtra("xtra", "norent");
                                    in.putExtra("ads", "");
                                    in.putExtra("data", "videos");
                                    context.startActivity(in);
                                } else if (access.equalsIgnoreCase("subscriber") || access.equalsIgnoreCase("admin")) {

                                    Intent in = new Intent(context, HomePageVideoActivity.class);
                                    in.putExtra("id", utils2.getId());
                                    in.putExtra("url", title);
                                    in.putExtra("xtra", "subscriber_content");
                                    in.putExtra("ads", "");
                                    in.putExtra("data", "videos");
                                    context.startActivity(in);
                                } else {

                                    //  String videourl = textViewGenre.getText().toString();

                                                /*Intent in = new Intent(getContext(), OnlinePlayerActivity.class);
                                                in.putExtra("id", movie_detaildata.get(position).getId());
                                                in.putExtra("url",videourl);
                                                startActivity(in);*/

                                    Intent in = new Intent(context, HomePageVideoActivity.class);
                                    in.putExtra("id", utils2.getId());
                                    in.putExtra("url", title);
                                    in.putExtra("xtra", "rentted");
                                    in.putExtra("data", "videos");
                                    in.putExtra("ads", "");
                                    context.startActivity(in);
                                }

                            } else {

                                if (access.equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view")) {

                                    Intent in = new Intent(context, HomePageVideoActivity.class);
                                    in.putExtra("id", utils2.getId());
                                    in.putExtra("url", title);
                                    in.putExtra("xtra", "subscriberented");
                                    in.putExtra("data", "videos");
                                    in.putExtra("ads", "");
                                    context.startActivity(in);

                                } else if (access.equalsIgnoreCase("ppv") || access.equalsIgnoreCase("expired")) {

                                    Intent in = new Intent(context, HomePageVideoActivity.class);
                                    in.putExtra("id", utils2.getId());
                                    in.putExtra("url", title);
                                    in.putExtra("xtra", "subscriberrent");
                                    in.putExtra("data", "videos");
                                    in.putExtra("ads", "");
                                    context.startActivity(in);
                                } else if (access.equalsIgnoreCase("guest"))
                                {
                                    Intent in = new Intent(context, HomePageVideoActivity.class);
                                    in.putExtra("id", utils2.getId());
                                    in.putExtra("url", title);
                                    in.putExtra("xtra", "guest");
                                    context.startActivity(in);
                                }
                                else {

                                    Intent in = new Intent(context, HomePageVideoActivity.class);
                                    in.putExtra("id", utils2.getId());
                                    in.putExtra("url", title);
                                    in.putExtra("xtra", "Norent");
                                    in.putExtra("data", "videos");
                                    in.putExtra("ads", "");
                                    context.startActivity(in);
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Log.d("Error", t.getMessage());
                        }
                    });





                }
            });

        }
        else if (sliderImg != null && position < live_banner.size() + video_banner.size() + sliderImg.size()+slidersList.size()) {

            // Get elements from sliderImg list
            int sliderPosition = position - (live_banner.size() + video_banner.size()+slidersList.size());
            series_banner utils3 = sliderImg.get(sliderPosition);
            imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
            imageLoader.get(utils3.getPlayer_image(), ImageLoader.getImageListener(imageView, R.drawable.favicon,R.drawable.favicon));
            //    value=utils3.getSource();
            id= utils3.getId();
            image =utils3.getPlayer_image();
            description =utils3.getDescription();
            tittle.setText(utils3.getTitle());
            play_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent in = new Intent(context, SeasonEpisodeCopyActivity.class);
                    in.putExtra("id",utils3.getId());
                    in.putExtra("image",image);
                    in.putExtra("desc",description);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(in);

                }
            });

        }
        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
