package com.atbuys.runmawi.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atbuys.runmawi.Model.EpisodeHomeData;
import com.atbuys.runmawi.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EpisodehomeAdopter extends RecyclerView.Adapter<EpisodehomeAdopter.HomeViewHolder> {

    private Context context;
    private List<EpisodeHomeData> SeasonsEpisodes;
    private EpisodesApdopter videoAdapter;
    ProgressDialog progressDialog;
    private RecyclerView.RecycledViewPool recycledViewPool;

    public EpisodehomeAdopter(ArrayList<EpisodeHomeData> SeasonsEpisodes, Context context) {
        this.SeasonsEpisodes = SeasonsEpisodes;
        this.context = context;
        recycledViewPool = new RecyclerView.RecycledViewPool();
        progressDialog = new ProgressDialog(context);
    }


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View theView = LayoutInflater.from(context).inflate(R.layout.row_layout_home_season, parent, false);
        return new HomeViewHolder(theView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, final int position) {

        holder.textViewCategory.setText(SeasonsEpisodes.get(position).getSeason_name());
        holder.textViewCategoryid.setText(SeasonsEpisodes.get(position).getSeason_id());


        if(SeasonsEpisodes.get(position).getMessage().equalsIgnoreCase("nodata")) {


            holder.nodata.setVisibility(View.VISIBLE);
            // holder.nodata.setText(genre_movies.get(position).getMessage());

        }
        else {
            holder.nodata.setVisibility(View.GONE);
            holder.recyclerViewHorizontal.setVisibility(View.VISIBLE);
            videoAdapter = new EpisodesApdopter(Arrays.asList(SeasonsEpisodes.get(position).getEpisodes()), context);
            holder.recyclerViewHorizontal.setAdapter(videoAdapter);
            holder.recyclerViewHorizontal.setRecycledViewPool(recycledViewPool);

        }
    }

    @Override
    public int getItemCount() {
        return SeasonsEpisodes.size();

    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView recyclerViewHorizontal;
        private TextView textViewCategory;
        private TextView textViewCategoryid,nodata;
        private RelativeLayout layuot;
        ImageView up_arrow,down_arrow;

        private LinearLayoutManager horizontalManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        public HomeViewHolder(View itemView) {
            super(itemView);

            recyclerViewHorizontal = itemView.findViewById(R.id.home_recycler_view_horizontal);
            layuot = itemView .findViewById(R.id.layuot);
            recyclerViewHorizontal.setHasFixedSize(true);

            recyclerViewHorizontal.setNestedScrollingEnabled(false);
            recyclerViewHorizontal.setLayoutManager(horizontalManager);
            recyclerViewHorizontal.setItemAnimator(new DefaultItemAnimator());

            textViewCategory = itemView.findViewById(R.id.tv_movie_category);
            textViewCategoryid = itemView.findViewById(R.id.tv_movie_category_id);
            nodata=itemView.findViewById(R.id.nodata);
            down_arrow=itemView.findViewById(R.id.down_arrow);
            up_arrow=itemView.findViewById(R.id.up_arrow);



            textViewCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(layuot.getVisibility() == View.VISIBLE)
                    {
                        layuot.setVisibility(View.GONE);
                        down_arrow.setVisibility(View.VISIBLE);
                        up_arrow.setVisibility(View.GONE);
                    }

                    else
                    {
                        layuot.setVisibility(View.VISIBLE);
                        up_arrow.setVisibility(View.VISIBLE);
                        down_arrow.setVisibility(View.GONE);

                    }


                }
            });

        }

    }
}
