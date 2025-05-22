package com.atbuys.runmawi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit2.Call;

public class NotificationActivty extends AppCompatActivity {


    private ArrayList<notifications> notificationdata;
    NotificaationAdopter notificaationAdopter;


    RecyclerView notificationrecycler;
    ProgressBar notificationprogress;
    private RecyclerView.LayoutManager notificationmanager;
    String user_id,user_role;
    LinearLayout notify,nonotify;
    ReadNotification readNotifications;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        notificationdata = new ArrayList<notifications>();

        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);



        notificationrecycler = (RecyclerView) findViewById(R.id.notificationrecycler);
        notify = (LinearLayout) findViewById(R.id.notify);
        nonotify = (LinearLayout) findViewById(R.id.nonotify);

        //notificationprogress=(ProgressBar)findViewById(R.id.catoryprogress);

        notificaationAdopter = new NotificaationAdopter(notificationdata, this);
        notificationmanager = new GridLayoutManager(this, 1);


        notificationrecycler.setHasFixedSize(true);
        notificationrecycler.setLayoutManager(notificationmanager);
        notificationrecycler.setAdapter(notificaationAdopter);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });


        Call<JSONResponse> channel = ApiClient.getInstance1().getApi().getNotification(user_id);
        channel.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


               // allChannelProgress.setVisibility(View.GONE);
                JSONResponse jsonResponse = response.body();

                if(jsonResponse.getNotifications().length == 0) {

                    nonotify.setVisibility(View.GONE);
                    nonotify.setVisibility(View.VISIBLE);

                }
                else {

                    nonotify.setVisibility(View.VISIBLE);
                    nonotify.setVisibility(View.GONE);
                    notificationrecycler.setVisibility(View.VISIBLE);
                    notificationdata = new ArrayList<>(Arrays.asList(jsonResponse.getNotifications()));
                    notificaationAdopter = new NotificaationAdopter(notificationdata);
                    notificationrecycler.setAdapter(notificaationAdopter);

                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });

        notificationrecycler.addOnItemTouchListener(
                (RecyclerView.OnItemTouchListener) new RecyclerItemClickListener(NotificationActivty.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (notificationdata.size() > position) {
                            if (notificationdata.get(position) != null) {

                                Api.getClient().getReadNotification(notificationdata.get(position).getId(),

                                        new Callback<ReadNotification>() {
                                            @Override
                                            public void success(ReadNotification readNotification, retrofit.client.Response response) {
                                                readNotifications = readNotification;

                                                if(readNotification.getStatus().equalsIgnoreCase("true")) {

                                                    Intent in = new Intent(getApplicationContext(), HomePageActivitywithFragments.class);
                                                    startActivity(in);

                                                }
                                            }

                                            @Override
                                            public void failure(RetrofitError error) {

                                                Toast.makeText(NotificationActivty.this, "Check Your Internet Connection", Toast.LENGTH_LONG).show();

                                            }
                                        });



                            }
                        }
                    }


                })


        );

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
