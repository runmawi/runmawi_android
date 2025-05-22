package com.atbuys.runmawi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RelatedLiveVideosAdapter extends RecyclerView.Adapter<RelatedLiveVideosAdapter.RelatedViewHolder> {
    ArrayList<livestream> livestreamArrayList;
    Context context;
    View itemView;

    public RelatedLiveVideosAdapter(ArrayList<livestream> livestreamArrayList, Context context) {
        this.livestreamArrayList = livestreamArrayList;
        this.context = context;
    }
    @NonNull
    @Override
    public RelatedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return new RelatedLiveVideosAdapter.RelatedViewHolder(LayoutInflater.from(context).inflate(R.layout.row_layout_channel2, parent, false));
         itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_channel2, parent, false);
        return new RelatedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedViewHolder holder, int position) {
        Picasso.get().load(livestreamArrayList.get(position).getImage()).into(holder.imageViewMovie);
    }

    @Override
    public int getItemCount() {
        return livestreamArrayList.size();
    }

    public class RelatedViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewMovie;

        public RelatedViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewMovie = itemView.findViewById(R.id.image_view_movie);

        }
    }
}
