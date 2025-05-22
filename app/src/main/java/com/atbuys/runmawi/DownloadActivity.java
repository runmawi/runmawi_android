/*
package com.atbuys.runmawi;

import static com.atbuys.runmawi.HomePageVideoActivity.videoModels;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.android.exoplayer2.offline.Download;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

*/
/**
 * Created by Mayur Solanki (mayursolanki120@gmail.com) on 21/06/20, 7:38 PM.
 *//*

public class DownloadActivity extends AppCompatActivity {

    List<Download> videosList;
    RecyclerView recyclerView;
    private List<Download> downloadedVideoList;
    private DownloadedVideoAdapter downloadedVideoAdapter;
    private Runnable runnableCode;
    private Handler handler;
    LinearLayout downloadLayout,nodowmloadLayout;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(  AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {


            setTheme(R.style.darktheme);

        }

        else {


            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);





        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            }
        });


        downloadLayout = (LinearLayout) findViewById(R.id.downloadlayout) ;
        nodowmloadLayout = (LinearLayout) findViewById(R.id.nodownlayout);

        recyclerView = findViewById(R.id.recycler_view_downloaded_video);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DownloadActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);


        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);

            Gson gson = new Gson();
            String json = prefs.getString("key", null);
            Type type = new TypeToken<ArrayList<VideoModel>>() {}.getType();
            videoModels = gson.fromJson(json, type);

        loadVideos();

        handler = new Handler();
        runnableCode = new Runnable() {
            @Override
            public void run() {
                List<Download> exoVideoList = new ArrayList<>();
                for(Map.Entry<Uri, Download> entry : AdaptiveExoplayer.getInstance().getDownloadTracker().downloads.entrySet()) {
                    Uri keyUri = entry.getKey();
                    Download download = entry.getValue();
                    exoVideoList.add(download);
                }
                downloadedVideoAdapter.onNewData(exoVideoList);
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnableCode);




    }


    private void loadVideos() {
        downloadedVideoList = new ArrayList<>();

        for(Map.Entry<Uri, Download> entry : AdaptiveExoplayer.getInstance().getDownloadTracker().downloads.entrySet()) {
            Uri keyUri = entry.getKey();
            Download download = entry.getValue();
            downloadedVideoList.add(download);
        }




        if(downloadedVideoList.size() == 0)
        {
            nodowmloadLayout.setVisibility(View.VISIBLE);
            downloadLayout.setVisibility(View.GONE);

        }
        else {

            downloadLayout.setVisibility(View.VISIBLE);
            nodowmloadLayout.setVisibility(View.GONE);

        }

        downloadedVideoAdapter = new DownloadedVideoAdapter(DownloadActivity.this, DownloadActivity.this);
        recyclerView.setAdapter(downloadedVideoAdapter);
        downloadedVideoAdapter.addItems(downloadedVideoList);


    }

    public void openBottomSheet(Download download){


        VideoModel videoModel = AppUtil.getVideoDetail(download.request.id);

        String statusTitle = videoModel.getVideoName();


        View dialogView = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_dialog, null);
        BottomSheetDialog dialog = new BottomSheetDialog(DownloadActivity.this);
        dialog.setContentView(dialogView);

        TextView tvVideoTitle =  dialog.findViewById(R.id.tv_video_title);
        LinearLayout llDownloadStart = dialog.findViewById(R.id.ll_start_download);
        LinearLayout llDownloadResume = dialog.findViewById(R.id.ll_resume_download);
        LinearLayout llDownloadPause = dialog.findViewById(R.id.ll_pause_download);
        LinearLayout llDownloadDelete = dialog.findViewById(R.id.ll_delete_download);

        llDownloadStart.setVisibility(View.GONE);


        if(download.state == Download.STATE_DOWNLOADING){
            llDownloadPause.setVisibility(View.VISIBLE);
            llDownloadResume.setVisibility(View.GONE);

        }else if(download.state == Download.STATE_STOPPED){
            llDownloadPause.setVisibility(View.GONE);
            llDownloadResume.setVisibility(View.VISIBLE);

        } else if(download.state == Download.STATE_QUEUED){
            llDownloadStart.setVisibility(View.VISIBLE);
            llDownloadPause.setVisibility(View.GONE);
            llDownloadResume.setVisibility(View.GONE);
        }else {
            llDownloadStart.setVisibility(View.GONE);
            llDownloadPause.setVisibility(View.GONE);
            llDownloadResume.setVisibility(View.GONE);
        }

        tvVideoTitle.setText(statusTitle);
        llDownloadStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdaptiveExoplayer.getInstance().getDownloadManager().addDownload(download.request);
                dialog.dismiss();
            }
        });
        llDownloadResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdaptiveExoplayer.getInstance().getDownloadManager().addDownload(download.request, Download.STOP_REASON_NONE);

                dialog.dismiss();
            }
        });

        llDownloadPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdaptiveExoplayer.getInstance().getDownloadManager().addDownload(download.request, Download.STATE_STOPPED);
                dialog.dismiss();
            }
        });

        llDownloadDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdaptiveExoplayer.getInstance().getDownloadManager().removeDownload(download.request.id);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnableCode);
    }

    @Override
    public void onBackPressed() {

        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }
}
*/
