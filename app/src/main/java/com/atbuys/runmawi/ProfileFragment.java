package com.atbuys.runmawi;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.offline.Download;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.vdocipher.aegis.offline.DownloadRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;


public class ProfileFragment extends Fragment {

  //  DownloadActivity downloadActivity;
    RecyclerView recyclerView;
    private List<Download> downloadedVideoList;
    private DownloadRequest downloadedVideoAdapter;
    private Runnable runnableCode;
    private Handler handler;
    private ArrayList<user_details> user_detailsdata;
    LinearLayout downloadLayout,nodowmloadLayout;

    ImageView notification;
    CircleImageView profiledp;

    String user_id;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_profile, null);

        Toolbar mToolbar = (Toolbar) root.findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });



        notification = (ImageView) root.findViewById(R.id.notification);
        profiledp = (CircleImageView) root.findViewById(R.id.profiledp);


        nodowmloadLayout = (LinearLayout) root.findViewById(R.id.nodownlayout);

        recyclerView = root.findViewById(R.id.recycler_view_downloaded_video);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);


        SharedPreferences prefs = this.getContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);


        Gson gson = new Gson();
        String json = prefs.getString("key", null);
        Type type = new TypeToken<ArrayList<VideoModel>>() {
        }.getType();
        //videoModels = gson.fromJson(json, type);


        loadVideos();

        handler = new Handler();
        runnableCode = new Runnable() {
            @OptIn(markerClass = UnstableApi.class)
            @Override
            public void run() {
                List<Download> exoVideoList = new ArrayList<>();
               /* for (Map.Entry<Uri, Download> entry : AdaptiveExoplayer.getInstance().getDownloadTracker().downloads.entrySet()) {
                    Uri keyUri = entry.getKey();
                    Download download = entry.getValue();
                    exoVideoList.add(download);
                }*/
              //  downloadedVideoAdapter.onNewData(exoVideoList);
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnableCode);


        return root;
    }

    private void loadVideos() {
        downloadedVideoList = new ArrayList<>();



       /* for(Map.Entry<Uri, Download> entry : AdaptiveExoplayer.getInstance().getDownloadTracker().downloads.entrySet()) {
            Uri keyUri = entry.getKey();
            Download download = entry.getValue();
            downloadedVideoList.add(download);
        }*/


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
                            Picasso.get().load(userprofile).fit().into(profiledp);


                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

                //  Toast.makeText(getContext(), "check your internet connection", Toast.LENGTH_LONG).show();
            }
        });


        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Intent in = new Intent(getContext(),DownloadActivity.class);
                startActivity(in);*/

                Fragment newCase = new DashboardFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newCase); // give your fragment container id in first parameter
                // if written, this transaction will be added to backstack
                transaction.addToBackStack(getFragmentManager().getClass().getName());
                transaction.commit();

            }
        });


        profiledp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Fragment newCase = new UserprofilFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newCase); // give your fragment container id in first parameter
                transaction.commit();*/

                Intent in = new Intent(getActivity(), MyAccountActivity.class);
                startActivity(in);

            }
        });

        if(downloadedVideoList.size() == 0)
        {
            nodowmloadLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

        }
        else {

            recyclerView.setVisibility(View.VISIBLE);
            nodowmloadLayout.setVisibility(View.GONE);

        }



      //  downloadedVideoAdapter = new DownloadedVideoAdapterCopy(this.getContext(),getContext());
      //  recyclerView.setAdapter(downloadedVideoAdapter);
      //  downloadedVideoAdapter.addItems(downloadedVideoList);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @OptIn(markerClass = UnstableApi.class)
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (downloadedVideoList.size() > position) {
                            if (downloadedVideoList.get(position) != null) {

                                Download download = downloadedVideoList.get(position);

                                if(download.state == Download.STATE_COMPLETED){
                                    Bundle bundle = new Bundle();
                                    bundle.putString("video_url", download.request.id);
                                   // Intent intent = new Intent(getContext(), OfflinePlayerActivity.class);
                                  //  intent.putExtras(bundle);
                                  //  getContext().startActivity(intent);
                                }else {
                                    openBottomSheet(download);
                                }
                            }
                        }

                    }

                })
        );

    }

    @OptIn(markerClass = UnstableApi.class)
    public void openBottomSheet(Download download) {

        VideoModel videoModel = AppUtil.getVideoDetail(download.request.id);

        String statusTitle = videoModel.getVideoName();


        View dialogView = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_dialog, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this.getContext());
        dialog.setContentView(dialogView);

        TextView tvVideoTitle = dialog.findViewById(R.id.tv_video_title);
        LinearLayout llDownloadStart = dialog.findViewById(R.id.ll_start_download);
        LinearLayout llDownloadResume = dialog.findViewById(R.id.ll_resume_download);
        LinearLayout llDownloadPause = dialog.findViewById(R.id.ll_pause_download);
        LinearLayout llDownloadDelete = dialog.findViewById(R.id.ll_delete_download);

        llDownloadStart.setVisibility(View.GONE);


        if (download.state == Download.STATE_DOWNLOADING) {
            llDownloadPause.setVisibility(View.VISIBLE);
            llDownloadResume.setVisibility(View.GONE);

        } else if (download.state == Download.STATE_STOPPED) {
            llDownloadPause.setVisibility(View.GONE);
            llDownloadResume.setVisibility(View.VISIBLE);

        } else if (download.state == Download.STATE_QUEUED) {
            llDownloadStart.setVisibility(View.VISIBLE);
            llDownloadPause.setVisibility(View.GONE);
            llDownloadResume.setVisibility(View.GONE);
        } else {
            llDownloadStart.setVisibility(View.GONE);
            llDownloadPause.setVisibility(View.GONE);
            llDownloadResume.setVisibility(View.GONE);
        }

        tvVideoTitle.setText(statusTitle);
        llDownloadStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // AdaptiveExoplayer.getInstance().getDownloadManager().addDownload(download.request);
                dialog.dismiss();
            }
        });
        llDownloadResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  AdaptiveExoplayer.getInstance().getDownloadManager().addDownload(download.request, Download.STOP_REASON_NONE);

                dialog.dismiss();
            }
        });

        llDownloadPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // AdaptiveExoplayer.getInstance().getDownloadManager().addDownload(download.request, Download.STATE_STOPPED);
                dialog.dismiss();
            }
        });

        llDownloadDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   AdaptiveExoplayer.getInstance().getDownloadManager().removeDownload(download.request.id);
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnableCode);
    }




}


