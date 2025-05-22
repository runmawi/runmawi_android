package com.atbuys.runmawi.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atbuys.runmawi.ChannalPageActivity1;
import com.atbuys.runmawi.Model.LearnData;
import com.atbuys.runmawi.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;*/
/*import com.example.bop.Model.HomeData;
import com.example.bop.Model.videos;
import com.example.bop.R;*/


public class LearnPageAdapter extends RecyclerView.Adapter<LearnPageAdapter.HomeViewHolder> {


    private Context context;
    private List<LearnData> Episode_videos;


    private VideoLearnPageAdapter videoAdapter;
    ProgressDialog progressDialog;

    private RecyclerView.RecycledViewPool recycledViewPool;

    public LearnPageAdapter(ArrayList<LearnData> Episode_videos, Context context) {
        this.Episode_videos = Episode_videos;
        this.context = context;
        recycledViewPool = new RecyclerView.RecycledViewPool();
        progressDialog = new ProgressDialog(context);
    }


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View theView = LayoutInflater.from(context).inflate(R.layout.row_layout_channel, parent, false);
        return new HomeViewHolder(theView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, final int position) {

        holder.textViewCategory.setText(Episode_videos.get(position).getCurrent_page());
       holder.textViewCategoryid.setText(Episode_videos.get(position).getId());
/*
        if(genre_movies.get(position).getMessage().equalsIgnoreCase("nodata")) {

            holder.nodata.setVisibility(View.VISIBLE);
            holder.recyclerViewHorizontal.setVisibility(View.GONE);
            holder.relativeLayout.setVisibility(View.GONE);
            // holder.nodata.setText(genre_movies.get(position).getMessage());
        }
        if (genre_movies.get(position).getMessage().equalsIgnoreCase("success"))
        {*/
            holder.recyclerViewHorizontal.setVisibility(View.VISIBLE);
            holder.relativeLayout.setVisibility(View.VISIBLE);
            holder.nodata.setVisibility(View.GONE);
            holder.recyclerViewHorizontal.setVisibility(View.VISIBLE);
            videoAdapter = new VideoLearnPageAdapter(Arrays.asList(Episode_videos.get(position).getData()), context);
            holder.recyclerViewHorizontal.setAdapter(videoAdapter);
            holder.recyclerViewHorizontal.setRecycledViewPool(recycledViewPool);
    //    }
    }

    @Override
    public int getItemCount() {
        return Episode_videos.size();

    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView recyclerViewHorizontal;
        private TextView textViewCategory;
        private TextView textViewCategoryid,nodata;
        private RelativeLayout relativeLayout;

        private LinearLayoutManager horizontalManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false);

        public HomeViewHolder(View itemView) {
            super(itemView);

            recyclerViewHorizontal = itemView.findViewById(R.id.home_recycler_view_horizontal);
            recyclerViewHorizontal.setHasFixedSize(true);
            recyclerViewHorizontal.setNestedScrollingEnabled(false);
            recyclerViewHorizontal.setLayoutManager(horizontalManager);
            recyclerViewHorizontal.setItemAnimator(new DefaultItemAnimator());

            textViewCategory = itemView.findViewById(R.id.tv_movie_category);
            textViewCategoryid = itemView.findViewById(R.id.tv_movie_category_id);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            nodata=itemView.findViewById(R.id.nodata);




            textViewCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                 //   Toast.makeText(context,""+textViewCategoryid.getText().toString(),Toast.LENGTH_LONG).show();
                    Intent in=new Intent(context, ChannalPageActivity1.class);
                    in.putExtra("id",textViewCategoryid.getText().toString());
                    in.putExtra("name",textViewCategory.getText().toString());
                    in.putExtra("number","y");
                    context.startActivity(in);

                }
            });

        }

    }
}
