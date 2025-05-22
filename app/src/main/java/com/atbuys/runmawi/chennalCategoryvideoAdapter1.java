package com.atbuys.runmawi;

import static com.atbuys.runmawi.UserHomeFragment.mediaplayer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;

class chennalCategoryvideoAdapter1 extends RecyclerView.Adapter<chennalCategoryvideoAdapter1.MyViewHolder> {


    private ArrayList<videodetail> movie_detaildata;
    private ArrayList<audiodetail> audiodetail;
    private static SharedPreferences prefs;
    String user_id, user_role;
    private ArrayList<livedetail> livemovie_detaildata;
    private ArrayList<videossubtitles> videossubtitles;
    private ArrayList<PageList> thismaylikelilst;
    private Context context;
    String channal_image, channal_banner, channel_slug, channel_name,title,mp3;
    boolean status;

    private static int currentPosition = 0;

    public chennalCategoryvideoAdapter1(ArrayList<PageList> thismaylikelilst, Context context) {
        this.thismaylikelilst = thismaylikelilst;
        this.context = context;

        prefs = context.getSharedPreferences(sharedpreferences.My_preference_name, 0);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);
        movie_detaildata = new ArrayList<videodetail>();
        audiodetail =new ArrayList<>();
    }


    public chennalCategoryvideoAdapter1(ArrayList<PageList> thismaylikelilst,boolean status) {
        this.thismaylikelilst = thismaylikelilst;
        this.context = context;
        this.status = status;


        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(status){
            return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.landscape_seeall_layout, parent, false));
        }else {
            return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.new_uploads1, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        //Picasso.get().load(videos.get(position).getGenre).into(holder.genre);

        //  holder.id.setText(thismaylikelilst.get(position).getId());
        // holder.url.setText(thismaylikelilst.get(position).getMp4_url());
        // holder.name.setText(thismaylikelilst.get(position).getTitle());
        //holder.ppvstatus.setText(thismaylikelilst.get(position).getPpv_status());

        // channal_banner = movie1.getChannel_image();
        //channal_image = movie1.getImage_url();
        // channel_name = thismaylikelilst.get(position).g;

        Picasso.get().
                load(thismaylikelilst.get(position).getImage_url())
                .into(holder.image);

        channel_slug = thismaylikelilst.get(position).getChannel_slug();
        holder.textsub.setText(thismaylikelilst.get(position).getSource());
        holder.textViewTitle.setText(String.valueOf(thismaylikelilst.get(position).getId()));
        title=thismaylikelilst.get(position).getName();
        channal_image = thismaylikelilst.get(position).getChannel_image();


        if (thismaylikelilst.get(position).getRating() == null || thismaylikelilst.get(position).getRating() == "" || thismaylikelilst.get(position).getRating() == "0") {
            holder.raingcard.setVisibility(View.GONE);
        } else {
            holder.rating.setText(thismaylikelilst.get(position).getRating() + ". 0");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.textsub.getText().toString().equalsIgnoreCase("Videos")) {

                    Intent in = new Intent(view.getContext(), HomePageVideoActivity.class);
                    in.putExtra("id", String.valueOf(thismaylikelilst.get(position).getId()));
                    in.putExtra("data", "videos");
                    in.putExtra("ads", "");
                    in.putExtra("xtra", "norent");
                    view.getContext().startActivity(in);

                } else if ( holder.textsub.getText().toString().equalsIgnoreCase("livestream")) {

                    Intent in = new Intent(view.getContext(), HomePageLiveActivity.class);
                    in.putExtra("id",  String.valueOf(thismaylikelilst.get(position).getId()));
                    in.putExtra("data", "videos");
                    in.putExtra("ads", "");
                    in.putExtra("xtra", "norent");
                    view.getContext().startActivity(in);

                    /*Call<JSONResponse> res = ApiClient.getInstance1().getApi().getLiveDetail(user_id,  String.valueOf(thismaylikelilst.get(position).getId()));
                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                            JSONResponse jsonResponse = response.body();
                            livemovie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getLivedetail()));
                            if (user_id == null) {

                                Intent in = new Intent(view.getContext(), HomePageLiveActivity.class);
                                in.putExtra("id",  String.valueOf(thismaylikelilst.get(position).getId()));
                                //    in.putExtra("url",  trailerurl.getText().toString());
                                in.putExtra("data", "videos");
                                in.putExtra("ads", "");
                                view.getContext().startActivity(in);

                            } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {

                                if ( holder.textsub.getText().toString().equalsIgnoreCase("ppv")) {

                                    Intent in = new Intent( holder.itemView.getContext(), HomePageLiveActivity.class);
                                    in.putExtra("id",  String.valueOf(thismaylikelilst.get(position).getId()));
                                    //  in.putExtra("url",  trailerurl.getText().toString());
                                    in.putExtra("xtra", "norent");
                                    in.putExtra("ads", "");
                                    in.putExtra("data", "videos");
                                    holder.itemView.getContext().startActivity(in);

                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                    Log.d("nbdncbdbdncbx", "yyy");
                                    Intent in = new Intent( holder.itemView.getContext(), HomePageLiveActivity.class);
                                    in.putExtra("id",  String.valueOf(thismaylikelilst.get(position).getId()));
                                    //   in.putExtra("url",  trailerurl.getText().toString());
                                    in.putExtra("xtra", "norent");
                                    in.putExtra("ads", "");
                                    in.putExtra("data", "videos");
                                    holder.itemView.getContext().startActivity(in);
                                } else if ( holder.textsub.getText().toString().equalsIgnoreCase("subscriber") ||  holder.textsub.getText().toString().equalsIgnoreCase("admin")) {

                                    Intent in = new Intent( holder.itemView.getContext(), HomePageLiveActivity.class);
                                    in.putExtra("id",  String.valueOf(thismaylikelilst.get(position).getId()));
                                    //   in.putExtra("url",  trailerurl.getText().toString());
                                    in.putExtra("xtra", "subscriber_content");
                                    in.putExtra("ads", "");
                                    in.putExtra("data", "videos");
                                    holder.itemView.getContext().startActivity(in);
                                } else {
                                                */
                    /*Intent in = new Intent(getContext(), OnlinePlayerActivity.class);
                                                in.putExtra("id", movie_detaildata.get(position).getId());
                                                in.putExtra("url",videourl);
                                                startActivity(in);*/
                    /*
                                    Intent in = new Intent(context, HomePageLiveActivity.class);
                                    in.putExtra("id",  String.valueOf(thismaylikelilst.get(position).getId()));
                                    //in.putExtra("url", videourl);
                                    in.putExtra("xtra", "rentted");
                                    in.putExtra("data", "videos");
                                    in.putExtra("ads", "");
                                    context.startActivity(in);
                                }

                            } else {

                                if ( holder.textsub.getText().toString().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view")) {

                                    Intent in = new Intent(context, HomePageLiveActivity.class);
                                    in.putExtra("id",  String.valueOf(thismaylikelilst.get(position).getId()));
                                    // in.putExtra("url",  trailerurl.getText().toString());
                                    in.putExtra("xtra", "subscriberented");
                                    in.putExtra("data", "videos");
                                    in.putExtra("ads", "");
                                    context.startActivity(in);

                                } else if ( holder.textsub.getText().toString().equalsIgnoreCase("ppv") ||  holder.textsub.getText().toString().equalsIgnoreCase("expired")) {
                                    Intent in = new Intent( holder.itemView.getContext(), HomePageLiveActivity.class);
                                    in.putExtra("id",  String.valueOf(thismaylikelilst.get(position).getId()));
                                    //in.putExtra("url",  trailerurl.getText().toString());
                                    in.putExtra("xtra", "subscriberrent");
                                    in.putExtra("data", "videos");
                                    in.putExtra("ads", "");
                                    holder.itemView.getContext().startActivity(in);
                                } else {
                                    Intent in = new Intent(context, HomePageLiveActivity.class);
                                    in.putExtra("id",  String.valueOf(thismaylikelilst.get(position).getId()));
                                    //in.putExtra("url",  holder.trailerurl.getText().toString());
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
                    });*/

                } else if ( holder.textsub.getText().toString().equalsIgnoreCase("Series")) {

                    Intent in = new Intent( holder.itemView.getContext(), SeasonEpisodeCopyActivity.class);
                    in.putExtra("id",  String.valueOf(thismaylikelilst.get(position).getId()));
                    // in.putExtra("image",movie1.getImage_url());
                    //in.putExtra("desc", serieslist.get(position).getDescription());
                    holder.itemView.getContext().startActivity(in);

                } else if ( holder.textsub.getText().toString().equalsIgnoreCase("Audios_album")) {

                    Intent in = new Intent( holder.itemView.getContext(), AlbumAudioPageActivity.class);
                    in.putExtra("album_id",  String.valueOf(thismaylikelilst.get(position).getId()));
                    holder.itemView.getContext().startActivity(in);

                } else if ( holder.textsub.getText().toString().equalsIgnoreCase("Audios")) {

                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, String.valueOf(thismaylikelilst.get(position).getId()));
                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                            JSONResponse jsonResponse = response.body();
                            if (jsonResponse.getAudiodetail().length==0 || jsonResponse.getAudiodetail()==null){
                                Toast.makeText(context, "Audio Not Available ", Toast.LENGTH_LONG).show();
                            }else {
                                audiodetail = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));

                                SharedPreferences.Editor editor =  holder.itemView.getContext().getSharedPreferences(sharedpreferences.My_preference_name, holder.itemView.getContext().MODE_PRIVATE).edit();
                                editor.putBoolean(sharedpreferences.login, true);
                                editor.putString(sharedpreferences.audioid, audiodetail.get(0).getId());
                                editor.apply();
                                editor.commit();

                                Intent in = new Intent( holder.itemView.getContext(), MediaPlayerPageActivity.class);
                                in.putExtra("id", audiodetail.get(0).getId());
                                holder.itemView.getContext().startActivity(in);
                                mediaplayer.reset();
                                mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                try {
                                    mediaplayer.setDataSource(audiodetail.get(0).getMp3_url());
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
                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Log.d("Error", t.getMessage());
                        }
                    });

                }else if ( holder.textsub.getText().toString().equalsIgnoreCase("Channel_Partner")) {

                    Intent in = new Intent(view.getContext(), ChannelpartnerlistActivity.class);
                    in.putExtra("slug", thismaylikelilst.get(position).getChannel_slug());
                    in.putExtra("image", thismaylikelilst.get(position).getChannel_image());
                    in.putExtra("name", channel_name);
                    in.putExtra("channal_image", channal_banner);
                    view.getContext().startActivity(in);

                } else if ( holder.textsub.getText().toString().equalsIgnoreCase("Content_Partner")) {

                    Intent in = new Intent(view.getContext(), ContentpartnerlistActivity.class);
                    in.putExtra("slug", thismaylikelilst.get(position).getChannel_slug());
                    in.putExtra("image",  thismaylikelilst.get(position).getChannel_image());
                    in.putExtra("name", channel_name);
                    in.putExtra("channal_image", channal_banner);
                    view.getContext().startActivity(in);
                } else if ( holder.textsub.getText().toString().equalsIgnoreCase("LiveCategory")) {

                    //check this line
                    Intent in = new Intent(view.getContext(), ChannalPageActivity1.class);
                    in.putExtra("id", String.valueOf(thismaylikelilst.get(position).getId()) );
                    in.putExtra("name", "live_category");
                    in.putExtra("name1", thismaylikelilst.get(position).getName());
                    in.putExtra("number", "z");
                    view.getContext().startActivity(in);
                }else{
                    Intent in = new Intent(view.getContext(), ChannalPageActivity1.class);
                    in.putExtra("id", String.valueOf(thismaylikelilst.get(position).getId()) );
                    in.putExtra("name", "category_videos");
                    in.putExtra("name1", thismaylikelilst.get(position).getName());
                    in.putExtra("number", "z");
                    view.getContext().startActivity(in);
                }

            }
        });


    }


    @Override
    public int getItemCount() {
        return thismaylikelilst.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle, rating;
        private TextView textViewGenre, textsub, trailerurl, id;
        private ImageView image;
        private CardView raingcard;

        public MyViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.newuploadid);
            textViewGenre = itemView.findViewById(R.id.tv_genre);
            image = itemView.findViewById(R.id.newuploadimg);
            textsub = itemView.findViewById(R.id.views);
            trailerurl = itemView.findViewById(R.id.tv_trailerurl);
            id = itemView.findViewById(R.id.id);
            raingcard = (CardView) itemView.findViewById(R.id.ratingcard);
            rating = (TextView) itemView.findViewById(R.id.rating);


           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    //Toast.makeText(context.getApplicationContext(), "welcome", Toast.LENGTH_SHORT).show();
                    if (textsub.getText().toString().equalsIgnoreCase("Videos")) {

                        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, textViewTitle.getText().toString());
                        res.enqueue(new retrofit2.Callback<JSONResponse>() {
                            @Override
                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                JSONResponse jsonResponse = response.body();
                                movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));
                                if (user_id == null) {

                                    Intent in = new Intent(view.getContext(), HomePageVideoActivity.class);
                                    in.putExtra("id", textViewTitle.getText().toString());
                                    //  in.putExtra("url",  trailerurl.getText().toString());
                                    in.putExtra("data", "videos");
                                    in.putExtra("ads", "");
                                    // view.getContext().startActivity(in);

                                } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {

                                    if (textsub.getText().toString().equalsIgnoreCase("ppv")) {

                                        Intent in = new Intent(view.getContext(), HomePageVideoActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url", trailerurl.getText().toString());
                                        in.putExtra("xtra", "norent");
                                        in.putExtra("ads", "");
                                        in.putExtra("data", "videos");
                                        // view.getContext().startActivity(in);

                                    } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                        Log.d("nbdncbdbdncbx", "yyy");

                                        Intent in = new Intent(view.getContext(), HomePageVideoActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url", trailerurl.getText().toString());
                                        in.putExtra("xtra", "norent");
                                        in.putExtra("ads", "");
                                        in.putExtra("data", "videos");
                                        // context.startActivity(in);
                                    } else if (textsub.getText().toString().equalsIgnoreCase("subscriber") || textsub.getText().toString().equalsIgnoreCase("admin")) {

                                        Intent in = new Intent(view.getContext(), HomePageVideoActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url", trailerurl.getText().toString());
                                        in.putExtra("xtra", "subscriber_content");
                                        in.putExtra("ads", "");
                                        in.putExtra("data", "videos");
                                        //view.getContext().startActivity(in);
                                    } else {

                                        //    String videourl =  textViewGenre.getText().toString() ;

                                                *//*Intent in = new Intent(getContext(), OnlinePlayerActivity.class);
                                                in.putExtra("id", movie_detaildata.get(position).getId());
                                                in.putExtra("url",videourl);
                                                startActivity(in);*//*


                                        Intent in = new Intent(view.getContext(), HomePageVideoActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url", "videourl");
                                        in.putExtra("xtra", "rentted");
                                        in.putExtra("data", "videos");
                                        in.putExtra("ads", "");
                                        view.getContext().startActivity(in);
                                    }

                                } else {

                                    if (textsub.getText().toString().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view")) {

                                        Intent in = new Intent(view.getContext(), HomePageVideoActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url", trailerurl.getText().toString());
                                        in.putExtra("xtra", "subscriberented");
                                        in.putExtra("data", "videos");
                                        in.putExtra("ads", "");
                                        //   view.getContext().startActivity(in);

                                    } else if (textsub.getText().toString().equalsIgnoreCase("ppv") || textsub.getText().toString().equalsIgnoreCase("expired")) {

                                        Intent in = new Intent(view.getContext(), HomePageVideoActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url", trailerurl.getText().toString());
                                        in.putExtra("xtra", "subscriberrent");
                                        in.putExtra("data", "videos");
                                        in.putExtra("ads", "");
                                        //  view.getContext().startActivity(in);
                                    } else {

                                        Intent in = new Intent(view.getContext(), HomePageVideoActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url", trailerurl.getText().toString());
                                        in.putExtra("xtra", "Norent");
                                        in.putExtra("data", "videos");
                                        in.putExtra("ads", "");
                                        // view.getContext().startActivity(in);
                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                Log.d("Error", t.getMessage());
                            }
                        });

                    } else if (textsub.getText().toString().equalsIgnoreCase("livestream")) {


                        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getLiveDetail(user_id, textViewTitle.getText().toString());
                        res.enqueue(new retrofit2.Callback<JSONResponse>() {
                            @Override
                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                JSONResponse jsonResponse = response.body();
                                livemovie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getLivedetail()));
                                if (user_id == null) {

                                    Intent in = new Intent(view.getContext(), HomePageLiveActivity.class);
                                    in.putExtra("id", textViewTitle.getText().toString());
                                    //    in.putExtra("url",  trailerurl.getText().toString());
                                    in.putExtra("data", "videos");
                                    in.putExtra("ads", "");
                                    view.getContext().startActivity(in);

                                } else if (user_id != null && user_role.equalsIgnoreCase("registered")) {

                                    if (textsub.getText().toString().equalsIgnoreCase("ppv")) {

                                        Intent in = new Intent(itemView.getContext(), HomePageLiveActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        //  in.putExtra("url",  trailerurl.getText().toString());
                                        in.putExtra("xtra", "norent");
                                        in.putExtra("ads", "");
                                        in.putExtra("data", "videos");
                                        itemView.getContext().startActivity(in);

                                    } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                        Log.d("nbdncbdbdncbx", "yyy");
                                        Intent in = new Intent(itemView.getContext(), HomePageLiveActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        //   in.putExtra("url",  trailerurl.getText().toString());
                                        in.putExtra("xtra", "norent");
                                        in.putExtra("ads", "");
                                        in.putExtra("data", "videos");
                                        itemView.getContext().startActivity(in);
                                    } else if (textsub.getText().toString().equalsIgnoreCase("subscriber") || textsub.getText().toString().equalsIgnoreCase("admin")) {

                                        Intent in = new Intent(itemView.getContext(), HomePageLiveActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        //   in.putExtra("url",  trailerurl.getText().toString());
                                        in.putExtra("xtra", "subscriber_content");
                                        in.putExtra("ads", "");
                                        in.putExtra("data", "videos");
                                        itemView.getContext().startActivity(in);
                                    } else {

                                        String videourl = textViewGenre.getText().toString();

                                                *//*Intent in = new Intent(getContext(), OnlinePlayerActivity.class);
                                                in.putExtra("id", movie_detaildata.get(position).getId());
                                                in.putExtra("url",videourl);
                                                startActivity(in);*//*
                                        Intent in = new Intent(context, HomePageLiveActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url", videourl);
                                        in.putExtra("xtra", "rentted");
                                        in.putExtra("data", "videos");
                                        in.putExtra("ads", "");
                                        context.startActivity(in);
                                    }

                                } else {

                                    if (textsub.getText().toString().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view")) {

                                        Intent in = new Intent(context, HomePageLiveActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        // in.putExtra("url",  trailerurl.getText().toString());
                                        in.putExtra("xtra", "subscriberented");
                                        in.putExtra("data", "videos");
                                        in.putExtra("ads", "");
                                        context.startActivity(in);

                                    } else if (textsub.getText().toString().equalsIgnoreCase("ppv") || textsub.getText().toString().equalsIgnoreCase("expired")) {
                                        Intent in = new Intent(itemView.getContext(), HomePageLiveActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        //in.putExtra("url",  trailerurl.getText().toString());
                                        in.putExtra("xtra", "subscriberrent");
                                        in.putExtra("data", "videos");
                                        in.putExtra("ads", "");
                                        itemView.getContext().startActivity(in);
                                    } else {
                                        Intent in = new Intent(context, HomePageLiveActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url", trailerurl.getText().toString());
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

                    } else if (textsub.getText().toString().equalsIgnoreCase("Series")) {

                        Intent in = new Intent(itemView.getContext(), SeasonEpisodeCopyActivity.class);
                        in.putExtra("id", textViewTitle.getText().toString());
                        // in.putExtra("image",movie1.getImage_url());
                        //in.putExtra("desc", serieslist.get(position).getDescription());
                        itemView.getContext().startActivity(in);

                    } else if (textsub.getText().toString().equalsIgnoreCase("Audios_album")) {

                        Intent in = new Intent(itemView.getContext(), AlbumAudioPageActivity.class);
                        in.putExtra("album_id", textViewTitle.getText().toString());
                        itemView.getContext().startActivity(in);

                    } else if (textsub.getText().toString().equalsIgnoreCase("Audios")) {

                        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, textViewTitle.getText().toString());
                        res.enqueue(new retrofit2.Callback<JSONResponse>() {
                            @Override
                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                JSONResponse jsonResponse = response.body();
                                if (jsonResponse.getAudiodetail().length==0 || jsonResponse.getAudiodetail()==null){
                                    Toast.makeText(context, "Audio Not Available ", Toast.LENGTH_LONG).show();
                                }else {
                                    audiodetail = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));

                                    SharedPreferences.Editor editor = itemView.getContext().getSharedPreferences(sharedpreferences.My_preference_name, itemView.getContext().MODE_PRIVATE).edit();
                                    editor.putBoolean(sharedpreferences.login, true);
                                    editor.putString(sharedpreferences.audioid, audiodetail.get(0).getId());
                                    editor.apply();
                                    editor.commit();

                                    Intent in = new Intent(itemView.getContext(), MediaPlayerPageActivity.class);
                                    in.putExtra("id", audiodetail.get(0).getId());
                                    itemView.getContext().startActivity(in);
                                    mediaplayer.reset();
                                    mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                    try {
                                        mediaplayer.setDataSource(audiodetail.get(0).getMp3_url());
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
                            }

                            @Override
                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                Log.d("Error", t.getMessage());
                            }
                        });

                    }else if (textsub.getText().toString().equalsIgnoreCase("Channel_Partner")) {

                        Intent in = new Intent(view.getContext(), ChannelpartnerlistActivity.class);
                        in.putExtra("slug", channel_slug);
                        in.putExtra("image", channal_image);
                        in.putExtra("name", channel_name);
                        in.putExtra("channal_image", channal_banner);
                        view.getContext().startActivity(in);

                    } else if (textsub.getText().toString().equalsIgnoreCase("Content_Partner")) {

                        Intent in = new Intent(view.getContext(), ContentpartnerlistActivity.class);
                        in.putExtra("slug", channel_slug);
                        in.putExtra("image", channal_image);
                        in.putExtra("name", channel_name);
                        in.putExtra("channal_image", channal_banner);
                        view.getContext().startActivity(in);
                    } else if (textsub.getText().toString().equalsIgnoreCase("LiveCategory")) {

                        //check this line
                        Intent in = new Intent(view.getContext(), ChannalPageActivity1.class);
                        in.putExtra("id",textViewTitle.getText().toString() );
                        in.putExtra("name", "live_category");
                        in.putExtra("name1", title);
                        in.putExtra("number", "z");
                        view.getContext().startActivity(in);
                    }

                }
            });*/

        }
    }
}

