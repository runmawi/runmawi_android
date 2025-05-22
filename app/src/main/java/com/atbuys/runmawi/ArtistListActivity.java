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

public class ArtistListActivity extends AppCompatActivity {

    private ArrayList<artistlist> artistlistsdata;
    RecyclerView artistRecycler;
    ProgressBar artistProgress;
    artistlit_artist_page_Adopter artistListAdopter;
    private RecyclerView.LayoutManager channelManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_list);

        artistlistsdata =new ArrayList<artistlist>();

        artistRecycler = (RecyclerView) findViewById(R.id.artistlistRecycler);
        artistProgress=(ProgressBar)findViewById(R.id.artistlistprogress);

        artistListAdopter = new artistlit_artist_page_Adopter(artistlistsdata, this);
        channelManager = new GridLayoutManager(this, 3);


        artistRecycler.setHasFixedSize(true);
        artistRecycler.setLayoutManager(channelManager);
        artistRecycler.setAdapter(artistListAdopter);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);
        mToolbar.setTitle("Artist");

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });


        Call<JSONResponse> channel = ApiClient.getInstance1().getApi().getArtistList();
        channel.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                artistProgress.setVisibility(View.GONE);
                artistRecycler.setVisibility(View.VISIBLE);
                JSONResponse jsonResponse = response.body();

                artistlistsdata = new ArrayList<>(Arrays.asList(jsonResponse.getArtistlist()));
                artistListAdopter = new artistlit_artist_page_Adopter(artistlistsdata);
                artistRecycler.setAdapter(artistListAdopter);


            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });


        artistRecycler.addOnItemTouchListener(
                (RecyclerView.OnItemTouchListener) new RecyclerItemClickListener(ArtistListActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (artistlistsdata.size() > position) {
                            if (artistlistsdata.get(position) != null) {

                                Intent in=new Intent(ArtistListActivity.this, ArtistPageActivity.class);
                                in.putExtra("artist_id",artistlistsdata.get(position).getId());
                                startActivity(in);


                            }
                        }
                    }


                })
        );

    }
}