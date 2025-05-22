package com.atbuys.runmawi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.atbuys.runmawi.Adapter.HomeCategoryPageAdapter;
import com.atbuys.runmawi.Api1.RetrofitSingleton;
import com.atbuys.runmawi.Model.HomeBodyResponse;
import com.atbuys.runmawi.Model.HomeData;

import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;


public class VideosFragment extends Fragment {


    private ArrayList<categorylist> channelListData;
    ProgressBar allChannelProgress;
    private ArrayList<HomeData> dataList;

    private RecyclerView allChannelRecycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    ImageView notification;
    CircleImageView profiledp;
    String user_id;

    private ArrayList<user_details> user_detailsdata;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_videos, null);

        dataList = new ArrayList<HomeData>();

        allChannelRecycler = (RecyclerView) root.findViewById(R.id.channelistrecycler);
        allChannelProgress = (ProgressBar) root.findViewById(R.id.catoryprogress);

        adapter = new HomeCategoryPageAdapter(dataList, getContext());
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);


        allChannelRecycler.setHasFixedSize(true);
        allChannelRecycler.setLayoutManager(layoutManager);
        allChannelRecycler.setAdapter(adapter);

        Call<JSONResponse> profileres = ApiClient.getInstance1().getApi().getUserprofile(user_id);
        profileres.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        JSONResponse jsonResponse = response.body();
                        user_detailsdata = new ArrayList<>(Arrays.asList(jsonResponse.getUser_details()));

                        for (int k = 0; k < user_detailsdata.size(); k++) {


                            String userprofile = user_detailsdata.get(k).getProfile_url();
                            Picasso.get().load(userprofile).into(profiledp);


                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

                //  Toast.makeText(getContext(), "check your internet connection", Toast.LENGTH_LONG).show();
            }
        });


        Toolbar mToolbar = (Toolbar) root.findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });



        Call<HomeBodyResponse> responseCall = RetrofitSingleton.getInstance().getApi().getMovieByCategory();
        responseCall.enqueue(new retrofit2.Callback<HomeBodyResponse>() {
            @Override
            public void onResponse(Call<HomeBodyResponse> call, retrofit2.Response<HomeBodyResponse> response) {

                allChannelProgress.setVisibility(View.GONE);
                allChannelRecycler.setVisibility(View.VISIBLE);

                if (response.isSuccessful()) {
                    for (HomeData data : response.body().getGenreMovies()) {

                        dataList.add(data);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<HomeBodyResponse> call, Throwable t) {

                //  progressBar.setVisibility(View.GONE);

            }
        });

        return root;
    }

    public void onBackPressed() {

        if (getFragmentManager() != null && getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        }

    }

}