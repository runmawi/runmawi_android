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

import com.atbuys.runmawi.ChannalPageActivity;
import com.atbuys.runmawi.Model.RecommandedHomeData;
import com.atbuys.runmawi.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecommandeHomeAdapter extends RecyclerView.Adapter<RecommandeHomeAdapter.HomeViewHolder> {


    private Context context;
    private List<RecommandedHomeData> channelrecomended;



    private RecommandedVideoAdapter recommandedVideoAdapter;
    ProgressDialog progressDialog;

    private RecyclerView.RecycledViewPool recycledViewPool;

    public RecommandeHomeAdapter(ArrayList<RecommandedHomeData> channelrecomended, Context context) {
        this.channelrecomended = channelrecomended;
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


        holder.relativeLayout.setVisibility(View.VISIBLE);
        holder.nodata.setVisibility(View.GONE);
        holder.recyclerViewHorizontal.setVisibility(View.VISIBLE);
        recommandedVideoAdapter = new RecommandedVideoAdapter(Arrays.asList(channelrecomended.get(position).getLive_banner()), context);
        holder.recyclerViewHorizontal.setAdapter(recommandedVideoAdapter);
        holder.recyclerViewHorizontal.setRecycledViewPool(recycledViewPool);


    }

    @Override
    public int getItemCount() {
        return channelrecomended.size();

    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView recyclerViewHorizontal;
        private TextView textViewCategory;
        private TextView textViewCategoryid,nodata;
        private RelativeLayout relativeLayout;

        private LinearLayoutManager horizontalManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

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

                    Intent in=new Intent(context, ChannalPageActivity.class);
                    in.putExtra("id",textViewCategoryid.getText().toString());
                    in.putExtra("name",textViewCategory.getText().toString());

                    // Toast.makeText(context.getApplicationContext(), ""+textViewCategoryid, Toast.LENGTH_SHORT).show();
                    context.startActivity(in);

                }
            });

        }

    }
}
