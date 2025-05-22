package com.atbuys.runmawi.Adapter;


import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atbuys.runmawi.Model.LibraryData;
import com.atbuys.runmawi.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private Context context;
    private List<LibraryData> video_andriod;


    private VideoLibraryPageAdapter videoAdapter;
    ProgressDialog progressDialog;

    private RecyclerView.RecycledViewPool recycledViewPool;

    public RecyclerViewAdapter(List<LibraryData> itemList , Context context  ) {

        this.video_andriod = itemList;
        this.context = context;
        recycledViewPool = new RecyclerView.RecycledViewPool();
        progressDialog = new ProgressDialog(context);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lib, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ItemViewHolder) {

            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return video_andriod == null ? 0 : video_andriod.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return video_andriod.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {


        private RecyclerView recyclerViewHorizontal;
        private TextView textViewCategory;
        private TextView textViewCategoryid,nodata;
        private RelativeLayout relativeLayout;

        private LinearLayoutManager horizontalManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false);


        public ItemViewHolder(@NonNull View itemView) {
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
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ItemViewHolder holder, int position) {

      //  String item = video_android.get(position);
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
     //   videoAdapter = new VideoLibraryPageAdapter(Arrays.asList(video_andriod.get(position).getData()), context);
        holder.recyclerViewHorizontal.setAdapter(videoAdapter);
        holder.recyclerViewHorizontal.setRecycledViewPool(recycledViewPool);

    }


}
