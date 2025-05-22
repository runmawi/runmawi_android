package com.atbuys.runmawi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.atbuys.runmawi.AlbumAudioPageActivity;
import com.atbuys.runmawi.ApiClient;
import com.atbuys.runmawi.ChannelpartnerlistActivity;
import com.atbuys.runmawi.ContentpartnerlistActivity;
import com.atbuys.runmawi.HomePageLiveActivity;
import com.atbuys.runmawi.HomePageVideoActivity;
import com.atbuys.runmawi.JSONResponse;
import com.atbuys.runmawi.PageList;
import com.atbuys.runmawi.R;
import com.atbuys.runmawi.SeasonEpisodeCopyActivity;
import com.atbuys.runmawi.livedetail;
import com.atbuys.runmawi.sharedpreferences;
import com.atbuys.runmawi.videodetail;
import com.atbuys.runmawi.videossubtitles;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;

class chennalCategoryvideoAdapter1 extends RecyclerView.Adapter<chennalCategoryvideoAdapter1.MyViewHolder> {


    private ArrayList<videodetail> movie_detaildata;
    private static SharedPreferences prefs;
    String user_id,user_role;
    private ArrayList<livedetail> livemovie_detaildata;
    private ArrayList<videossubtitles> videossubtitles;
    private ArrayList<PageList> thismaylikelilst;
    private Context context;
    String channal_image,channal_banner,channel_slug,channel_name;

    private static int currentPosition = 0;

    public chennalCategoryvideoAdapter1(ArrayList<PageList> thismaylikelilst, Context context) {
        this.thismaylikelilst = thismaylikelilst;
        this.context = context;

        prefs = context.getSharedPreferences(sharedpreferences.My_preference_name, 0);
        user_id=prefs.getString(sharedpreferences.user_id,null);
        user_role=prefs.getString(sharedpreferences.role,null);
        movie_detaildata = new ArrayList<videodetail>();

    }


    public chennalCategoryvideoAdapter1(ArrayList<PageList> thismaylikelilst) {
        this.thismaylikelilst = thismaylikelilst;
        this.context = context;


        user_id=prefs.getString(sharedpreferences.user_id,null);
        user_role=prefs.getString(sharedpreferences.role,null);

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_uploads1, parent, false);

        return new MyViewHolder(itemView);

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
                load( thismaylikelilst.get(position).getImage_url())
                .into(holder.image);

        channel_slug = thismaylikelilst.get(position).getChannel_slug();
        holder.textsub.setText(thismaylikelilst.get(position).getSource());
        holder.textViewTitle.setText(String.valueOf(thismaylikelilst.get(position).getId()));
        channal_image = thismaylikelilst.get(position).getChannel_image();




    }


    @Override
    public int getItemCount() {
        return thismaylikelilst.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;
        private TextView textViewGenre,textsub,trailerurl,id;
        private ImageView image;



        public MyViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.newuploadid);
            textViewGenre = itemView.findViewById(R.id.tv_genre);
            image = itemView.findViewById(R.id.newuploadimg);
            textsub = itemView.findViewById(R.id.views);
            trailerurl = itemView.findViewById(R.id.tv_trailerurl);
            id = itemView.findViewById(R.id.id);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    //Toast.makeText(context.getApplicationContext(), "welcome", Toast.LENGTH_SHORT).show();
                    if (textsub.getText().toString().equalsIgnoreCase("Videos"))
                    {

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
                                    in.putExtra("data","videos");
                                    in.putExtra("ads","");
                                   // view.getContext().startActivity(in);

                                }

                                else if (user_id != null && user_role.equalsIgnoreCase("registered")) {

                                    if (textsub.getText().toString().equalsIgnoreCase("ppv"))
                                    {

                                        Intent in = new Intent(view.getContext(), HomePageVideoActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url",  trailerurl.getText().toString());
                                        in.putExtra("xtra", "norent");
                                        in.putExtra("ads","");
                                        in.putExtra("data","videos");
                                       // view.getContext().startActivity(in);

                                    } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                        Log.d("nbdncbdbdncbx","yyy");

                                        Intent in = new Intent(view.getContext(), HomePageVideoActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url",  trailerurl.getText().toString());
                                        in.putExtra("xtra", "norent");
                                        in.putExtra("ads","");
                                        in.putExtra("data","videos");
                                       // context.startActivity(in);
                                    }
                                    else if (textsub.getText().toString().equalsIgnoreCase("subscriber") || textsub.getText().toString().equalsIgnoreCase("admin")) {

                                        Intent in = new Intent(view.getContext(), HomePageVideoActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url",  trailerurl.getText().toString());
                                        in.putExtra("xtra", "subscriber_content");
                                        in.putExtra("ads","");
                                        in.putExtra("data","videos");
                                        //view.getContext().startActivity(in);
                                    }

                                    else {

                                    //    String videourl =  textViewGenre.getText().toString() ;

                                                /*Intent in = new Intent(getContext(), OnlinePlayerActivity.class);
                                                in.putExtra("id", movie_detaildata.get(position).getId());
                                                in.putExtra("url",videourl);
                                                startActivity(in);*/


                                        Intent in = new Intent(view.getContext(), HomePageVideoActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url", "videourl");
                                        in.putExtra("xtra", "rentted");
                                        in.putExtra("data","videos");
                                        in.putExtra("ads","");
                                       view.getContext().startActivity(in);
                                    }

                                } else {

                                    if (textsub.getText().toString().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view")) {

                                        Intent in = new Intent(view.getContext(), HomePageVideoActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url",  trailerurl.getText().toString());
                                        in.putExtra("xtra", "subscriberented");
                                        in.putExtra("data","videos");
                                        in.putExtra("ads","");
                                     //   view.getContext().startActivity(in);

                                    }

                                    else if(textsub.getText().toString().equalsIgnoreCase("ppv") ||textsub.getText().toString().equalsIgnoreCase("expired"))
                                    {

                                        Intent in = new Intent(view.getContext(), HomePageVideoActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url",  trailerurl.getText().toString());
                                        in.putExtra("xtra", "subscriberrent");
                                        in.putExtra("data","videos");
                                        in.putExtra("ads","");
                                      //  view.getContext().startActivity(in);
                                    }

                                    else {

                                        Intent in = new Intent(view.getContext(), HomePageVideoActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("url",  trailerurl.getText().toString());
                                        in.putExtra("xtra", "Norent");
                                        in.putExtra("data","videos");
                                        in.putExtra("ads","");
                                       // view.getContext().startActivity(in);
                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                Log.d("Error", t.getMessage());
                            }
                        });

                    }
                    else if (textsub.getText().toString().equalsIgnoreCase("livestream")) {



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
                                    in.putExtra("data","videos");
                                    in.putExtra("ads","");
                                    view.getContext().startActivity(in);

                                }

                                else if (user_id != null && user_role.equalsIgnoreCase("registered")) {

                                    if (textsub.getText().toString().equalsIgnoreCase("ppv"))
                                    {

                                        Intent in = new Intent(itemView.getContext(), HomePageLiveActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                      //  in.putExtra("url",  trailerurl.getText().toString());
                                        in.putExtra("xtra", "norent");
                                        in.putExtra("ads","");
                                        in.putExtra("data","videos");
                                        itemView.getContext().startActivity(in);

                                    }
                                    else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                        Log.d("nbdncbdbdncbx","yyy");
                                        Intent in = new Intent(itemView.getContext(), HomePageLiveActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                     //   in.putExtra("url",  trailerurl.getText().toString());
                                        in.putExtra("xtra", "norent");
                                        in.putExtra("ads","");
                                        in.putExtra("data","videos");
                                        itemView.getContext().startActivity(in);
                                    }
                                    else if (textsub.getText().toString().equalsIgnoreCase("subscriber") || textsub.getText().toString().equalsIgnoreCase("admin")) {

                                        Intent in = new Intent(itemView.getContext(), HomePageLiveActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                     //   in.putExtra("url",  trailerurl.getText().toString());
                                        in.putExtra("xtra", "subscriber_content");
                                        in.putExtra("ads","");
                                        in.putExtra("data","videos");
                                        itemView.getContext().startActivity(in);
                                    }

                                    else {

                                    //    String videourl =  textViewGenre.getText().toString() ;

                                                /*Intent in = new Intent(getContext(), OnlinePlayerActivity.class);
                                                in.putExtra("id", movie_detaildata.get(position).getId());
                                                in.putExtra("url",videourl);
                                                startActivity(in);*/
                                        Intent in = new Intent(itemView.getContext(), HomePageLiveActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                       // in.putExtra("url", videourl);
                                        in.putExtra("xtra", "rentted");
                                        in.putExtra("data","videos");
                                        in.putExtra("ads","");
                                        itemView.getContext().startActivity(in);
                                    }

                                } else {

                                    if (textsub.getText().toString().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view")) {

                                        Intent in = new Intent(itemView.getContext(), HomePageLiveActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                       // in.putExtra("url",  trailerurl.getText().toString());
                                        in.putExtra("xtra", "subscriberented");
                                        in.putExtra("data","videos");
                                        in.putExtra("ads","");
                                        itemView.getContext().startActivity(in);

                                    }

                                    else if(textsub.getText().toString().equalsIgnoreCase("ppv") ||textsub.getText().toString().equalsIgnoreCase("expired"))
                                    {
                                        Intent in = new Intent(itemView.getContext(), HomePageLiveActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        //in.putExtra("url",  trailerurl.getText().toString());
                                        in.putExtra("xtra", "subscriberrent");
                                        in.putExtra("data","videos");
                                        in.putExtra("ads","");
                                        itemView.getContext().startActivity(in);
                                    }

                                    else {
                                        Intent in = new Intent(itemView.getContext(), HomePageLiveActivity.class);
                                        in.putExtra("id", textViewTitle.getText().toString());
                                        in.putExtra("xtra", "Norent");
                                        in.putExtra("data","videos");
                                        in.putExtra("ads","");
                                        itemView.getContext().startActivity(in);
                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                Log.d("Error", t.getMessage());
                            }
                        });

                    }
                    else if (textsub.getText().toString().equalsIgnoreCase("Series")) {
                        Intent in = new Intent(itemView.getContext(), SeasonEpisodeCopyActivity.class);
                        in.putExtra("id", textViewTitle.getText().toString());
                       // in.putExtra("image",movie1.getImage_url());
                        //in.putExtra("desc", serieslist.get(position).getDescription());
                        itemView.getContext().startActivity(in);
                    }

                    else if (textsub.getText().toString().equalsIgnoreCase("Audios_album")) {


                        Intent in=new Intent(itemView.getContext(), AlbumAudioPageActivity.class);
                        in.putExtra("album_id",textViewTitle.getText().toString());
                        itemView.getContext().startActivity(in);


                    }

                    else if (textsub.getText().toString().equalsIgnoreCase("Channel_Partner")) {

                        Intent in = new Intent(view.getContext(), ChannelpartnerlistActivity.class);
                        in.putExtra("slug",channel_slug);
                        in.putExtra("image",channal_image);
                        in.putExtra("name",channel_name);
                        in.putExtra("channal_image",channal_banner);
                        view.getContext().startActivity(in);

                    }

                    else if (textsub.getText().toString().equalsIgnoreCase("ContentPartner")) {

                        Intent in = new Intent(view.getContext(), ContentpartnerlistActivity.class);
                        in.putExtra("slug",channel_slug);
                        in.putExtra("image",channal_image);
                        in.putExtra("name",channel_name);
                        in.putExtra("channal_image",channal_banner);
                        view.getContext().startActivity(in);




                    }

                }
            });

        }
    }
}

