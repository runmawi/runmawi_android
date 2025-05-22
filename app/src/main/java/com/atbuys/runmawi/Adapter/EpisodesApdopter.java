package com.atbuys.runmawi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.atbuys.runmawi.HomePageEpisodeActivity;
import com.atbuys.runmawi.Model.episodes;
import com.atbuys.runmawi.R;
import com.atbuys.runmawi.episode;
import com.atbuys.runmawi.sharedpreferences;
import com.atbuys.runmawi.videossubtitles;

import java.util.ArrayList;
import java.util.List;

/*import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;*/
/*
import com.example.bop.LiveActivity;
import com.example.bop.Model.videos;
import com.example.bop.R;
import com.example.bop.sharedpreferences;*/


public class EpisodesApdopter extends RecyclerView.Adapter<EpisodesApdopter.MovieViewHolder> {


    private List<episodes> movieList;
    private Context context;
    episodes movie1;

    private static SharedPreferences prefs;
    String user_id,user_role;
    private ArrayList<episode> movie_detaildata;
    private ArrayList<videossubtitles> videossubtitles;


    public EpisodesApdopter(List<episodes> list, Context context) {
        this.movieList = list;
        this.context = context;

        prefs = context.getSharedPreferences(sharedpreferences.My_preference_name, 0);
         user_id=prefs.getString(sharedpreferences.user_id,null);
         user_role=prefs.getString(sharedpreferences.role,null);
         movie_detaildata = new ArrayList<episode>();
         videossubtitles = new ArrayList<videossubtitles>();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MovieViewHolder(LayoutInflater.from(context).inflate(R.layout.row_layout_movie_season, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, final int position) {

        movie1= movieList.get(position);

        holder.textsub.setText(movie1.getMp4_url());

        holder.textViewTitle.setText(movie1.getId());
        if (movie1.getEpisode_order()!=null && movie1.getEpisode_order()!="0"){
            holder.episode_order.setText("Episode "+movie1.getEpisode_order());
        }
        holder.trailerurl.setText(movie1.getSeason_id());
        holder.series_id.setText(movie1.getSeries_id());

        //String txt=movie.getId();
     // holder.textViewGenre.setText(movie1.getVideo_url()+movie1.getPath());

        Picasso.get().
                load("https://runmawi.com/public/uploads/images/"+movie1.getPlayer_image())
                .into(holder.imageViewMovie);

    }

    @Override
    public int getItemCount() {

        return movieList.size();

    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle,episode_order;
        private TextView textViewGenre,textsub,trailerurl,series_id;
        private ImageView imageViewMovie;

        public MovieViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_title);
            episode_order=itemView.findViewById(R.id.episode_order);
            textViewGenre = itemView.findViewById(R.id.tv_genre);
            imageViewMovie = itemView.findViewById(R.id.image_view_movie);
            textsub = itemView.findViewById(R.id.tv_sub);
            series_id=itemView.findViewById(R.id.series_id);
            trailerurl = itemView.findViewById(R.id.tv_trailerurl);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent in = new Intent(context, HomePageEpisodeActivity.class);
                    in.putExtra("id", textViewTitle.getText().toString());
                    in.putExtra("url", textsub.getText().toString());
                    in.putExtra("seasonid",trailerurl.getText().toString());
                    in.putExtra("series_id",series_id.getText().toString());
                    context.startActivity(in);

                }
            });


        }




    }
}