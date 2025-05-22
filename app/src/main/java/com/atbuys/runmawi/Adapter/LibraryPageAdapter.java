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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atbuys.runmawi.ChannalPageActivity;
import com.atbuys.runmawi.Model.LibraryData;
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


public class LibraryPageAdapter extends RecyclerView.Adapter<LibraryPageAdapter.HomeViewHolder> {


    private Context context;
    private List<LibraryData> video_andriod;


    private VideoLibraryPageAdapter videoAdapter;
    ProgressDialog progressDialog;

    private RecyclerView.RecycledViewPool recycledViewPool;

    public LibraryPageAdapter(ArrayList<LibraryData> videos, Context context) {
        this.video_andriod = videos;
        this.context = context;
        recycledViewPool = new RecyclerView.RecycledViewPool();
        progressDialog = new ProgressDialog(context);
    }


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View theView = LayoutInflater.from(context).inflate(R.layout.lib, parent, false);
        return new HomeViewHolder(theView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, final int position) {

        holder.textViewCategory.setText(video_andriod.get(position).getCurrent_page());
     //   holder.textViewCategoryid.setText(genre_movies.get(position).getGenre_id());
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
            videoAdapter = new VideoLibraryPageAdapter(Arrays.asList(video_andriod.get(position).getData()), context);
            holder.recyclerViewHorizontal.setAdapter(videoAdapter);
            holder.recyclerViewHorizontal.setRecycledViewPool(recycledViewPool);
    //    }
    }

    @Override
    public int getItemCount() {
        return video_andriod.size();

    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView recyclerViewHorizontal;
        private TextView textViewCategory;
        private TextView textViewCategoryid,nodata;
        private RelativeLayout relativeLayout;

        private GridLayoutManager horizontalManager = new GridLayoutManager(context,3);

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
                    Intent in=new Intent(context, ChannalPageActivity.class);
                    in.putExtra("id",textViewCategoryid.getText().toString());
                    in.putExtra("name",textViewCategory.getText().toString());
                    context.startActivity(in);

                }
            });

        }

    }
}
