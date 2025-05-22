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
import com.atbuys.runmawi.Model.learn;
import com.atbuys.runmawi.R;
import com.atbuys.runmawi.SeasonEpisodeCopyActivity;
import com.atbuys.runmawi.sharedpreferences;
import com.atbuys.runmawi.videodetail;
import com.atbuys.runmawi.videossubtitles;

import java.util.ArrayList;
import java.util.List;


public class VideoLearnPageAdapter extends RecyclerView.Adapter<VideoLearnPageAdapter.MovieViewHolder> {


    private List<learn> movieList;
    private Context context;
    learn movie1;

    private static SharedPreferences prefs;
    String user_id,user_role,img;
    private ArrayList<videodetail> movie_detaildata;

    private ArrayList<videossubtitles> videossubtitles;


    public VideoLearnPageAdapter(List<learn> list, Context context) {
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

        holder.textViewTitle.setText(movie1.getId());
        holder.trailerurl.setText(movie1.getDescription());
        img = movie1.getImage_url();

      holder.textViewGenre.setText(movie1.getMp4_url());

        Picasso.get().
                load(movie1.getImage_url())
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
                    Intent in = new Intent(context,SeasonEpisodeCopyActivity.class);
                    in.putExtra("id",textViewTitle.getText().toString());
                    in.putExtra("image",movie1.getImage_url());
                    in.putExtra("desc",trailerurl.getText().toString());
                 context.startActivity(in);
                }
            });
        }

    }
}