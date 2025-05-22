package com.atbuys.runmawi;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;


public class ComingSoonFragment extends Fragment {


    RecyclerView CommingsoonRecycler;
    private RecyclerView.LayoutManager layoutManager1;
    private ArrayList<latestvideos> commimgsoonlist;
    CommingsoonAdapter commingsoonAdapter;
    String user_id,user_role;
    CircleImageView profiledp;
    private ArrayList<user_details> user_detailsdata;
    ImageView searchpage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences prefs = this.getContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);


        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_coming_soon, null);

        CommingsoonRecycler = (RecyclerView) root.findViewById(R.id.commingsoonrecycler);
        profiledp = (CircleImageView)root. findViewById(R.id.profiledp);
        searchpage = (ImageView) root.findViewById(R.id.searchpage) ;


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


        profiledp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent in = new Intent(getActivity(), MyAccountActivity.class);
                startActivity(in);

            }
        });


        searchpage.setOnClickListener(new View.OnClickListener() {
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

        commimgsoonlist = new ArrayList<latestvideos>();

        commingsoonAdapter = new CommingsoonAdapter(commimgsoonlist, this.getContext());
        layoutManager1 = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);

        CommingsoonRecycler.setHasFixedSize(true);
        CommingsoonRecycler.setLayoutManager(layoutManager1);
        CommingsoonRecycler.setAdapter(commingsoonAdapter);


        Call<JSONResponse> callser = ApiClient.getInstance1().getApi().getLatestVideos();
        callser.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                JSONResponse jsonResponse = response.body();

                //   latesttitle.setText(jsonResponse.getDiaplay_name());

                if (jsonResponse.getLatestvideos().length == 0) {



                } else {

                    CommingsoonRecycler.setVisibility(View.VISIBLE);
                    commimgsoonlist = new ArrayList<>(Arrays.asList(jsonResponse.getLatestvideos()));
                    commingsoonAdapter = new CommingsoonAdapter(commimgsoonlist);
                    CommingsoonRecycler.setAdapter(commingsoonAdapter);

                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });


        return root;
    }
}