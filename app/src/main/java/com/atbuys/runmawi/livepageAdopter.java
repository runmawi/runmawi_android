package com.atbuys.runmawi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

class livepageAdopter extends RecyclerView.Adapter<livepageAdopter.MyViewHolder> {



    private List<LiveCategory> sidemenulist2;
    private Context context;

    private static int currentPosition = 0;

    public livepageAdopter(List<LiveCategory> sidemenulist2, Context context) {
        this.sidemenulist2 = sidemenulist2;
        this.context = context;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name1;
        public ImageView image;
        public TextView streamInfoText; // Add text view to show stream info

        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.newuploadimg);
            
            // Use the existing TextView in the layout
            try {
                // Try to use newuploadname TextView which exists in the layout
                streamInfoText = view.findViewById(R.id.newuploadname);
                // Make it visible since it's set to GONE in the layout
                if (streamInfoText != null) {
                    streamInfoText.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                android.util.Log.d("ADAPTER_DEBUG", "Could not find or set up TextView for channel info");
            }
        }
    }

    public livepageAdopter(List<LiveCategory> sidemenulist2) {
        this.sidemenulist2 = sidemenulist2;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_uploads5, parent, false);
        // Log view creation for debugging
        android.util.Log.d("ADAPTER_DEBUG", "Creating view holder for livepageAdopter");

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // Add detailed logging to debug binding issues
        android.util.Log.d("ADAPTER_DEBUG", "Binding position " + position + " in livepageAdopter");
        
        try {
            LiveCategory sidemenu2 = sidemenulist2.get(position);
            
            // Extract all available information from the LiveCategory object
            String name = sidemenu2.getName();
            String image = sidemenu2.getImage();
            String id = sidemenu2.getId();
            String streamUrl = sidemenu2.getLivestream_url();
            String streamFormat = sidemenu2.getLivestream_format();
            
            // Log complete item details for debugging
            android.util.Log.d("ADAPTER_DEBUG", "Item details - Position: " + position + 
                    ", ID: " + (id != null ? id : "<null>") + 
                    ", Name: " + (name != null ? name : "<null>") + 
                    ", Stream URL: " + (streamUrl != null ? streamUrl : "<null>") + 
                    ", Format: " + (streamFormat != null ? streamFormat : "<null>") + 
                    ", Image: " + (image != null ? image : "<null>"));
            
            // Set a display title based on available data (preferring name, but falling back to other fields)
            String displayTitle = "";
            if (name != null && !name.isEmpty()) {
                displayTitle = name;
            } else if (streamUrl != null && !streamUrl.isEmpty()) {
                // Extract a title from the stream URL if possible
                String[] parts = streamUrl.split("/");
                displayTitle = "Stream " + (parts.length > 0 ? parts[parts.length-1] : id);
            } else {
                displayTitle = "Channel " + (id != null ? id : position);
            }
            
            // Display the stream information if the TextView exists
            if (holder.streamInfoText != null) {
                holder.streamInfoText.setText(displayTitle);
                holder.streamInfoText.setVisibility(View.VISIBLE);
            }
            
            // Load image using Picasso with error handling
            if (image != null && !image.isEmpty()) {
                String imageUrl = "https://runmawi.com/public/uploads/images/" + image;
                android.util.Log.d("ADAPTER_DEBUG", "Loading image from: " + imageUrl);
                
                Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.image);
            } else {
                android.util.Log.d("ADAPTER_DEBUG", "No image URL available, using default image for item " + displayTitle);
                holder.image.setImageResource(R.drawable.logo);
            }
            
            // Add click listener to the image for better user interaction
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.util.Log.d("ADAPTER_DEBUG", "Item clicked at position " + position);
                    // You could implement navigation to a detail view here
                }
            });
            
        } catch (Exception e) {
            android.util.Log.e("ADAPTER_DEBUG", "Error binding view holder: " + e.getMessage());
            holder.image.setImageResource(R.drawable.logo); // Fallback image
        }

    }


    @Override
    public int getItemCount() {
        return sidemenulist2.size();
    }



}

