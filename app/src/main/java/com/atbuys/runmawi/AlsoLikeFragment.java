package com.atbuys.runmawi;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atbuys.runmawi.Adapter.RecommandeHomeAdapter;
import com.atbuys.runmawi.Adapter.RecommandedVideoAdapter;
import com.atbuys.runmawi.Model.RecommandedHomeData;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;

public class AlsoLikeFragment extends Fragment {

    RecyclerView thismayalsolike;
    String videoId,user_id;
    RecommandeHomeAdapter thismaylikeadopter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<RecommandedHomeData> dataList;
    ArrayList<channelrecomended> channelrecomendeds;
    RecommandedVideoAdapter recommandedVideoAdapter;

    public AlsoLikeFragment() {
    }

    public AlsoLikeFragment(String videoId, String user_id) {
        this.videoId=videoId;
        this.user_id=user_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_also_like, container, false);

        thismayalsolike = (RecyclerView) root.findViewById(R.id.thismayalsolike);
        dataList = new ArrayList<RecommandedHomeData>();
        channelrecomendeds=new ArrayList<>();


        thismaylikeadopter = new RecommandeHomeAdapter(dataList, getActivity());
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);


        thismayalsolike.setHasFixedSize(true);
        thismayalsolike.setLayoutManager(layoutManager);
        thismayalsolike.setAdapter(recommandedVideoAdapter);

        Call<JSONResponse> alsolikeres = ApiClient.getInstance1().getApi().getAlsolikeVideo(videoId);
        alsolikeres.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                JSONResponse jsonResponse = response.body();
                channelrecomendeds=new ArrayList<>(Arrays.asList(jsonResponse.getChannelrecomended()));
                recommandedVideoAdapter = new RecommandedVideoAdapter(Arrays.asList(channelrecomendeds.get(0).getRecomendeds()), getContext());
                thismayalsolike.setAdapter(recommandedVideoAdapter);
            }}

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());

            }
        });

/*
        Call<HomeBodyResponse> responseCall = RetrofitSingleton.getInstance().getApi().getRecommaned(videoId);
        responseCall.enqueue(new retrofit2.Callback<HomeBodyResponse>() {
            @Override
            public void onResponse(Call<HomeBodyResponse> call, retrofit2.Response<HomeBodyResponse> response) {


                thismayalsolike.setVisibility(View.VISIBLE);

                if (response.isSuccessful()) {
                    for (RecommandedHomeData data : response.body().getChannelrecomended()) {

                        dataList.add(data);
                    }
                    thismaylikeadopter.notifyDataSetChanged();
                }

            }
            @Override
            public void onFailure(Call<HomeBodyResponse> call, Throwable t) {

                //  progressBar.setVisibility(View.GONE);

            }
        });*/

        return root;
    }
}