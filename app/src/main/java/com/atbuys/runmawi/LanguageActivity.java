package com.atbuys.runmawi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;

public class LanguageActivity extends AppCompatActivity {

    private ArrayList<all_languages> channelListData;
    RecyclerView allChannelRecycler;
    ProgressBar allChannelProgress;
    LanguageAdopter channelListAdapter;
    private RecyclerView.LayoutManager channelManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        channelListData =new ArrayList<all_languages>();

        allChannelRecycler = (RecyclerView) findViewById(R.id.channelistrecycler);
        allChannelProgress=(ProgressBar)findViewById(R.id.catoryprogress);

        channelListAdapter = new LanguageAdopter(channelListData, this);
        channelManager = new GridLayoutManager(this, 3);


        allChannelRecycler.setHasFixedSize(true);
        allChannelRecycler.setLayoutManager(channelManager);
        allChannelRecycler.setAdapter(channelListAdapter);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });


        Call<JSONResponse> channel = ApiClient.getInstance1().getApi().getLanguage();
        channel.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                allChannelProgress.setVisibility(View.GONE);
                allChannelRecycler.setVisibility(View.VISIBLE);
                JSONResponse jsonResponse = response.body();

                channelListData = new ArrayList<>(Arrays.asList(jsonResponse.getAll_language()));
                channelListAdapter = new LanguageAdopter(channelListData);
                allChannelRecycler.setAdapter(channelListAdapter);


            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });


        allChannelRecycler.addOnItemTouchListener(
                (RecyclerView.OnItemTouchListener) new RecyclerItemClickListener(LanguageActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (channelListData.size() > position) {
                            if (channelListData.get(position) != null) {

                                Intent in=new Intent(LanguageActivity.this, LangageVideoActivity.class);
                                in.putExtra("id",channelListData.get(position).getId());
                                in.putExtra("name",channelListData.get(position).getName());
                                startActivity(in);


                            }
                        }
                    }


                })
        );




    }
}
