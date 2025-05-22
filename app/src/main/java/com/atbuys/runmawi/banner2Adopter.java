package com.atbuys.runmawi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


class banner2Adopter extends RecyclerView.Adapter<banner2Adopter.MyViewHolder> {



    private ArrayList<video_banner> latestvideoslist;
    Context context;
    View itemView;
    ImageView hoverimg;
    private RecyclerView.LayoutManager hoverManager;
    LatestBannerAdapter latestBannerAdapter;

    public banner2Adopter(ArrayList<video_banner> latestvideoslist, Context context) {
        this.latestvideoslist = latestvideoslist;
        this.context = context;
    }


    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, url,id,ppvstatus;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.newuploadid);
            image = (ImageView) view.findViewById(R.id.newuploadimg);
             name = (TextView) view.findViewById(R.id.newuploadname);
            url = (TextView) view.findViewById(R.id.newuploadurl);
            ppvstatus=(TextView)view.findViewById(R.id.ppvstatus);

        }
    }

    public banner2Adopter(ArrayList<video_banner> latestvideoslist) {
        this.latestvideoslist = latestvideoslist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.banner, parent, false);
        return new MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.id.setText(latestvideoslist.get(position).getId());

        Picasso.get().
                load( "https://runmawi.com/public/uploads/images/"+latestvideoslist.get(position).getPlayer_image())
                .fit()
                .into(holder.image);



      //  Glide.with(holder.itemView.getContext()).load("https://res.cloudinary.com/demo/image/upload/kitten_fighting.gif").into(holder.image);


/*
        holder.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {



                View view2= LayoutInflater.from(holder.image.getContext()).inflate(R.layout.activity_focus,null);
             //   View view2 = getLayoutInflater().inflate(R.layout.activity_focus, null);
                 RecyclerView recycler_bottom = view2.findViewById(R.id.bottomrecycler);


                 latestBannerAdapter = new LatestBannerAdapter(latestvideoslist, holder.image.getContext());
                 hoverManager = new LinearLayoutManager(holder.image.getContext(), LinearLayoutManager.HORIZONTAL, false);

                recycler_bottom.setHasFixedSize(true);
                recycler_bottom.setLayoutManager(hoverManager);
                recycler_bottom.setAdapter(latestBannerAdapter);

                Call<JSONResponse> callfeat = ApiClient.getInstance1().getApi().getLatestVideos();
                callfeat.enqueue(new retrofit2.Callback<JSONResponse>() {
                    @Override
                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                        JSONResponse jsonResponse = response.body();

                        if (jsonResponse.getLatestvideos().length == 0) {

   //  nolatest.setVisibility(View.VISIBLE);
                            //                featurevideoprogress.setVisibility(View.GONE);
                          //                  feature_layout.setVisibility(View.VISIBLE);
                                            //  shimlayout.setVisibility(View.GONE);
                                            //featurevideoRecycler.setVisibility(View.VISIBLE);


                        }

                        else {


                            SnapHelper snapHelper = new PagerSnapHelper();

                            latestvideoslist = new ArrayList<>(Arrays.asList(jsonResponse.getLatestvideos()));
                            latestBannerAdapter = new LatestBannerAdapter(latestvideoslist, holder.image.getContext());
                            recycler_bottom.setAdapter(latestBannerAdapter);
                            recycler_bottom.scrollToPosition(latestvideoslist.indexOf(latestvideoslist.get(position)));
                            snapHelper.attachToRecyclerView(recycler_bottom);




                        }

                    }

                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                        Log.d("Error", t.getMessage());
                    }
                });



                dialog = new BottomSheetDialog(holder.image.getContext());
                dialog.setContentView(view2);
                dialog.show();


                return true;
            }
        });
*/



    }

/*
    private void showDialog(View.OnLongClickListener onLongClickListener) {

        dialog = new Dialog(itemView.getContext());
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.imagehovering);

        dialog.show();

         hoverimg = (ImageView) dialog.findViewById(R.id.hoverimage);

          Glide.with(itemView.getContext()).load("https://res.cloudinary.com/demo/image/upload/kitten_fighting.gif").into(hoverimg);


    }
*/


    @Override
    public int getItemCount() {
        return latestvideoslist.size();
    }

}

