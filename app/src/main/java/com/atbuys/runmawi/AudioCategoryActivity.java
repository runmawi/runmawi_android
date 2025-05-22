package com.atbuys.runmawi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;

public class AudioCategoryActivity extends AppCompatActivity {

    private ArrayList<audioalbums> channelListData;
    RecyclerView allChannelRecycler;
    TextView channelid,no_text;
    LinearLayout novideoscat;
    ProgressBar allChannelProgress;
    AlbumlistHomeAdapter channelListAdapter;
    private RecyclerView.LayoutManager channelManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);

        channelListData =new ArrayList<audioalbums>();

        allChannelRecycler = (RecyclerView) findViewById(R.id.channelistrecycler);
        allChannelProgress=(ProgressBar)findViewById(R.id.catoryprogress);
        channelid =(TextView) findViewById(R.id.namee);
        novideoscat =(LinearLayout) findViewById(R.id.novideoscat);
        no_text=(TextView)findViewById(R.id.no_text);
        no_text.setText("No Audios");

        channelListAdapter = new AlbumlistHomeAdapter(channelListData, this);
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

        Intent intent=getIntent();
        channelid.setText(intent.getStringExtra("name"));

        Call<JSONResponse> channel = ApiClient.getInstance1().getApi().getAudiocatList();
        channel.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                allChannelProgress.setVisibility(View.GONE);
                allChannelRecycler.setVisibility(View.VISIBLE);
                JSONResponse jsonResponse = response.body();

                if (jsonResponse.getAudioalbums()==null||jsonResponse.getAudioalbums().length==0){
                    allChannelRecycler.setVisibility(View.GONE);
                    novideoscat.setVisibility(View.VISIBLE);
                }else {
                    novideoscat.setVisibility(View.GONE);
                    allChannelRecycler.setVisibility(View.VISIBLE);
                    channelListData = new ArrayList<>(Arrays.asList(jsonResponse.getAudioalbums()));
                    channelListAdapter = new AlbumlistHomeAdapter(channelListData);
                    allChannelRecycler.setAdapter(channelListAdapter);
                }


            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });


        allChannelRecycler.addOnItemTouchListener(
                (RecyclerView.OnItemTouchListener) new RecyclerItemClickListener(AudioCategoryActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (channelListData.size() > position) {
                            if (channelListData.get(position) != null) {

                                Intent in=new Intent(AudioCategoryActivity.this, AlbumAudioPageActivity.class);
                                in.putExtra("album_id",channelListData.get(position).getId());
                                startActivity(in);


                            }
                        }
                    }


                })
        );




    }
}
