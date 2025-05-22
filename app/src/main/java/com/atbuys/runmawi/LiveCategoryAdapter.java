package com.atbuys.runmawi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atbuys.runmawi.Adapter.VideoCategoryHomePageAdapter;
import com.atbuys.runmawi.ChannalPageActivity1;
import com.atbuys.runmawi.Model.HomeCategoryData;
import com.atbuys.runmawi.Model.data;
import com.atbuys.runmawi.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class LiveCategoryAdapter extends RecyclerView.Adapter<LiveCategoryAdapter.HomeViewHolder> {


    private Context context;
    private List<HomeCategoryData> Home_page;
    private List<data> data;
    private List<data> data1;


    private VideoCategoryHomePageAdapter videoAdapter;
    ProgressDialog progressDialog;

    private RecyclerView.RecycledViewPool recycledViewPool;

    public LiveCategoryAdapter(ArrayList<data> genre_movies, Context context) {
        this.data = genre_movies;
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

        ArrayList<data> categoryData = new ArrayList<>();

        data row1 = data.get(position);
        data row2 = new data();

        row2.setData(row1.getCategory_livestream());
        row2.setHeader_name(row1.getName());
        row2.setSource(row1.getName());
        row2.setSource_type(row1.getSource());
        row2.setHeader_name_IOS(row1.getId1());
        categoryData.add(row2);
        holder.id3.setText(data.get(position).getId1());

        data.remove(position);
        if (categoryData.size() > 0) {
            data.addAll(position, categoryData);
            holder.textViewCategory.setText(data.get(position).getHeader_name());
            holder.textViewCategoryid.setText(data.get(position).getSource());
            holder.id.setText(data.get(position).getSource_type());
            holder.id1.setText(data.get(position).getHeader_name_IOS());
            videoAdapter = new VideoCategoryHomePageAdapter(Arrays.asList(data.get(position).getData()), context, false);
            holder.recyclerViewHorizontal.setAdapter(videoAdapter);
            holder.nodata.setVisibility(View.GONE);
            holder.recyclerViewHorizontal.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView recyclerViewHorizontal;
        private TextView textViewCategory, id, id1, id3;
        private TextView textViewCategoryid, nodata;

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
            nodata = itemView.findViewById(R.id.nodata);
            id = itemView.findViewById(R.id.id);
            id1 = itemView.findViewById(R.id.id1);
            id3 = itemView.findViewById(R.id.id3);

            textViewCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent in = new Intent(context, ChannalPageActivity1.class);
                    in.putExtra("id", id3.getText().toString());
                    in.putExtra("name", id.getText().toString());
                    in.putExtra("name1", textViewCategory.getText().toString());
                    in.putExtra("number", "z");
                    context.startActivity(in);

                }
            });

        }

    }
}