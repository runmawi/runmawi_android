package com.atbuys.runmawi.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atbuys.runmawi.ChannalPageActivity;
import com.atbuys.runmawi.Model.HomepageData;
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


public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.HomeViewHolder> {


    private Context context;
    private List<HomepageData> Home_page;


    private HomePageVideoAdapter videoAdapter;
    ProgressDialog progressDialog;

    private RecyclerView.RecycledViewPool recycledViewPool;

    public HomePageAdapter(ArrayList<HomepageData> Home_page, Context context) {
        this.Home_page = Home_page;
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

        holder.textViewCategory.setText(Home_page.get(position).getHeader_name());
        holder.textViewCategoryid.setText(Home_page.get(position).getSource());
/*

        if(Home_page.get(position).get().equalsIgnoreCase("nodata")) {

            holder.nodata.setVisibility(View.VISIBLE);
            holder.recyclerViewHorizontal.setVisibility(View.GONE);
           // holder.nodata.setText(genre_movies.get(position).getMessage());
        }
        else {
*/
            holder.nodata.setVisibility(View.GONE);
            holder.recyclerViewHorizontal.setVisibility(View.VISIBLE);
            videoAdapter = new HomePageVideoAdapter(Arrays.asList(Home_page.get(position).getData()), context);
            holder.recyclerViewHorizontal.setAdapter(videoAdapter);
            holder.recyclerViewHorizontal.setRecycledViewPool(recycledViewPool);
        }
    //}

    @Override
    public int getItemCount() {
        return Home_page.size();

    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView recyclerViewHorizontal;
        private TextView textViewCategory;
        private TextView textViewCategoryid,nodata;

        private LinearLayoutManager horizontalManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        public HomeViewHolder(View itemView) {
            super(itemView);

            recyclerViewHorizontal = itemView.findViewById(R.id.home_recycler_view_horizontal);
            recyclerViewHorizontal.setHasFixedSize(true);
            recyclerViewHorizontal.setNestedScrollingEnabled(false);
            recyclerViewHorizontal.setLayoutManager(horizontalManager);
            recyclerViewHorizontal.setItemAnimator(new DefaultItemAnimator());

            textViewCategory = itemView.findViewById(R.id.tv_movie_category);
            textViewCategoryid = itemView.findViewById(R.id.tv_movie_category_id);
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
