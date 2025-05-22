package com.atbuys.runmawi.Adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.atbuys.runmawi.UserHomeFragment.mediaplayer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.atbuys.runmawi.ApiClient;
import com.atbuys.runmawi.JSONResponse;
import com.atbuys.runmawi.MediaPlayerPageActivity;
import com.atbuys.runmawi.Model.audioss;
import com.atbuys.runmawi.R;
import com.atbuys.runmawi.SubscribeActivity;
import com.atbuys.runmawi.audiodetail;
import com.atbuys.runmawi.sharedpreferences;
import com.atbuys.runmawi.videossubtitles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;

/*import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;*/
/*
import com.example.bop.LiveActivity;
import com.example.bop.Model.videos;
import com.example.bop.R;
import com.example.bop.sharedpreferences;*/


public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.MovieViewHolder> {


    private List<audioss> movieList;
    private Context context;
    audioss movie1;
    public String audio_id;

    private static SharedPreferences prefs;
    String user_id,user_role;
    private ArrayList<audiodetail> movie_detaildata;
    private ArrayList<videossubtitles> videossubtitles;




    public AudioAdapter(List<audioss> list, Context context) {
        this.movieList = list;
        this.context = context;

        prefs = context.getSharedPreferences(sharedpreferences.My_preference_name, 0);
         user_id=prefs.getString(sharedpreferences.user_id,null);
         user_role=prefs.getString(sharedpreferences.role,null);
        movie_detaildata = new ArrayList<audiodetail>();
        videossubtitles = new ArrayList<videossubtitles>();
        audio_id = prefs.getString(sharedpreferences.audioid,null);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MovieViewHolder(LayoutInflater.from(context).inflate(R.layout.row_layout_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, final int position) {

        movie1= movieList.get(position);


      //  holder.textsub.setText(moviesubtitles.get(position).getUrl());

        holder.textViewTitle.setText(movie1.getId());
        holder.trailerurl.setText(movie1.getTrailer());
        holder.textsub.setText(movie1.getAccess());

        //String txt=movie.getId();
        if(movie1.getMp4_url() == null)
        {
            holder.textViewGenre.setText(movie1.getTrailer());
            Log.d("nxbncbxncbxn",holder.textViewGenre.getText().toString());
        }
        else
        {
            holder.textViewGenre.setText(movie1.getMp4_url());

        }



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
                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, textViewTitle.getText().toString());
                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                            JSONResponse jsonResponse = response.body();
                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));


                            if(user_role == null && movie_detaildata.get(0).getAccess().equalsIgnoreCase("registered")
                                    || user_role == null && movie_detaildata.get(0).getAccess().equalsIgnoreCase("subscriber"))
                            {

                                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                alert.setTitle("Your Not Subscribed user Kindly Subscribe and access the contents");

                                alert.setPositiveButton("Subscribe", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent in = new Intent(context, SubscribeActivity.class);
                                        context.startActivity(in);
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

                            else if (user_role == null && movie_detaildata.get(0).getAccess().equalsIgnoreCase("guest") ) {
                                Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, movie_detaildata.get(0).getId());

                                res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                    @Override
                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                        JSONResponse jsonResponse = response.body();
                                        movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));

                                        SharedPreferences.Editor editor = context.getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
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

                                Intent in = new Intent(context, MediaPlayerPageActivity.class);
                                in.putExtra("id", movie_detaildata.get(0).getId());
                                context.startActivity(in);
                                mediaplayer.reset();
                                mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                                try {

                                    mediaplayer.setDataSource(movie_detaildata.get(0).getMp3_url());
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

                            else if (user_role.equalsIgnoreCase("subscriber") || user_role.equalsIgnoreCase("admin") ) {
                                Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, movie_detaildata.get(0).getId());
                                res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                    @Override
                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                        JSONResponse jsonResponse = response.body();
                                        movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));

                                        SharedPreferences.Editor editor = context.getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
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

                                Intent in = new Intent(context, MediaPlayerPageActivity.class);
                                in.putExtra("id", movie_detaildata.get(0).getId());
                                context.startActivity(in);
                                mediaplayer.reset();
                                mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                                try {
                                    mediaplayer.setDataSource(movie_detaildata.get(0).getMp3_url());
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

                            else if(user_role.equalsIgnoreCase("registered") && movie_detaildata.get(0).getAccess().equalsIgnoreCase("guest") || user_role.equalsIgnoreCase("registered") && movie_detaildata.get(0).getAccess().equalsIgnoreCase("registered"))
                            {

                                Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id,movie_detaildata.get(0).getId());
                                res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                    @Override
                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                        JSONResponse jsonResponse = response.body();
                                        movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));

                                        SharedPreferences.Editor editor = context.getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
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

                                Intent in = new Intent(context, MediaPlayerPageActivity.class);
                                in.putExtra("id", movie_detaildata.get(0).getId());
                                context.startActivity(in);
                                mediaplayer.reset();
                                mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                                try {
                                    mediaplayer.setDataSource(movie_detaildata.get(0).getMp3_url());
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
                                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                alert.setTitle("Your Not Subscribed user Kindly Subscribe and access the contents");

                                alert.setPositiveButton("Subscribe", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent in = new Intent(context, SubscribeActivity.class);
                                        context.startActivity(in);
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

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {

                        }

                });



                }
            });

          /*  itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {


                    Intent in=new Intent(context, HoverPlayActivity.class);
                    context.startActivity(in);

                    return false;




                }
            });
*/
        }




    }
}