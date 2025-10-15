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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.atbuys.runmawi.ApiClient;
import com.atbuys.runmawi.HomePageVideoActivity;
import com.atbuys.runmawi.JSONResponse;
import com.atbuys.runmawi.Model.library;
import com.atbuys.runmawi.R;
import com.atbuys.runmawi.sharedpreferences;
import com.atbuys.runmawi.videodetail;
import com.atbuys.runmawi.videossubtitles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;


public class VideoLibraryPageAdapter extends RecyclerView.Adapter<VideoLibraryPageAdapter.MovieViewHolder> {


    private List<library> movieList;
    private Context context;
    library movie1;

    private static SharedPreferences prefs;
    String user_id,user_role;
    private ArrayList<videodetail> movie_detaildata;
    private ArrayList<videossubtitles> videossubtitles;


    public VideoLibraryPageAdapter(List<library> list, Context context) {
        this.movieList = list;
        this.context = context;

        prefs = context.getSharedPreferences(sharedpreferences.My_preference_name, 0);
         user_id=prefs.getString(sharedpreferences.user_id,null);
         user_role=prefs.getString(sharedpreferences.role,null);
        movie_detaildata = new ArrayList<videodetail>();
        videossubtitles = new ArrayList<videossubtitles>();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MovieViewHolder(LayoutInflater.from(context).inflate(R.layout.row_layout_channel2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, final int position) {

        movie1= movieList.get(position);


      //  holder.textsub.setText(moviesubtitles.get(position).getUrl());

        holder.textViewTitle.setText(movie1.getId());
        holder.trailerurl.setText(movie1.getTrailer());


        //String txt=movie.getId();


      holder.textViewGenre.setText(movie1.getMp4_url());

        Picasso.get().
                load("https://runmawi.com/public/uploads/images/"+movie1.getImage())
                .into(holder.imageViewMovie);

    }

    @Override
    public int getItemCount() {

        return movieList.size();

    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;
        private TextView textViewGenre,textsub,trailerurl;
        private ImageView imageViewMovie;


        public MovieViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_title);
            textViewGenre = itemView.findViewById(R.id.tv_genre);
            imageViewMovie = itemView.findViewById(R.id.image_view_movie);
            textsub = itemView.findViewById(R.id.tv_sub);
            trailerurl = itemView.findViewById(R.id.tv_trailerurl);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, textViewTitle.getText().toString());
                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                            JSONResponse jsonResponse = response.body();
                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));

                            if (user_id == null) {

                                Intent in = new Intent(context, HomePageVideoActivity.class);
                                in.putExtra("id", textViewTitle.getText().toString());
                                in.putExtra("url",  trailerurl.getText().toString());
                                in.putExtra("data","videos");
                                in.putExtra("ads","");
                                context.startActivity(in);

                            }

                            else if (user_id != null && user_role.equalsIgnoreCase("registered")) {

                                if (textsub.getText().toString().equalsIgnoreCase("ppv"))
                                {

                                    Intent in = new Intent(context, HomePageVideoActivity.class);
                                    in.putExtra("id", textViewTitle.getText().toString());
                                    in.putExtra("url",  trailerurl.getText().toString());
                                    in.putExtra("xtra", "norent");
                                    in.putExtra("ads","");
                                    in.putExtra("data","videos");
                                    context.startActivity(in);

                                } else if ((response.body().getPpv_video_status().equalsIgnoreCase("Expired"))) {

                                    Log.d("nbdncbdbdncbx","yyy");
                                    Intent in = new Intent(context, HomePageVideoActivity.class);
                                    in.putExtra("id", textViewTitle.getText().toString());
                                    in.putExtra("url",  trailerurl.getText().toString());
                                    in.putExtra("xtra", "norent");
                                    in.putExtra("ads","");
                                    in.putExtra("data","videos");
                                    context.startActivity(in);
                                }
                                else if (textsub.getText().toString().equalsIgnoreCase("subscriber") || textsub.getText().toString().equalsIgnoreCase("admin")) {

                                    Intent in = new Intent(context, HomePageVideoActivity.class);
                                    in.putExtra("id", textViewTitle.getText().toString());
                                    in.putExtra("url",  trailerurl.getText().toString());
                                    in.putExtra("xtra", "subscriber_content");
                                    in.putExtra("ads","");
                                    in.putExtra("data","videos");
                                    context.startActivity(in);
                                }

                                else {

                                    String videourl =  textViewGenre.getText().toString() ;

                                                /*Intent in = new Intent(getContext(), OnlinePlayerActivity.class);
                                                in.putExtra("id", movie_detaildata.get(position).getId());
                                                in.putExtra("url",videourl);
                                                startActivity(in);*/
                                    Intent in = new Intent(context, HomePageVideoActivity.class);
                                    in.putExtra("id", textViewTitle.getText().toString());
                                    in.putExtra("url", videourl);
                                    in.putExtra("xtra", "rentted");
                                    in.putExtra("data","videos");
                                    in.putExtra("ads","");
                                    context.startActivity(in);
                                }

                            } else {

                                if (textsub.getText().toString().equalsIgnoreCase("ppv") && response.body().getPpv_video_status().equalsIgnoreCase("can_view")) {

                                    Intent in = new Intent(context, HomePageVideoActivity.class);
                                    in.putExtra("id", textViewTitle.getText().toString());
                                    in.putExtra("url",  trailerurl.getText().toString());
                                    in.putExtra("xtra", "subscriberented");
                                    in.putExtra("data","videos");
                                    in.putExtra("ads","");
                                    context.startActivity(in);

                                }

                                else if(textsub.getText().toString().equalsIgnoreCase("ppv") ||textsub.getText().toString().equalsIgnoreCase("expired"))
                                {
                                    Intent in = new Intent(context, HomePageVideoActivity.class);
                                    in.putExtra("id", textViewTitle.getText().toString());
                                    in.putExtra("url",  trailerurl.getText().toString());
                                    in.putExtra("xtra", "subscriberrent");
                                    in.putExtra("data","videos");
                                    in.putExtra("ads","");
                                    context.startActivity(in);
                                }

                                else {
                                    Intent in = new Intent(context, HomePageVideoActivity.class);
                                    in.putExtra("id", textViewTitle.getText().toString());
                                    in.putExtra("url",  trailerurl.getText().toString());
                                    in.putExtra("xtra", "Norent");
                                    in.putExtra("data","videos");
                                    in.putExtra("ads","");
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




    }
}