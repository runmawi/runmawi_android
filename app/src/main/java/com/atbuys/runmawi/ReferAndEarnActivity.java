package com.atbuys.runmawi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit2.Call;


public class ReferAndEarnActivity extends AppCompatActivity {

    private ArrayList<user_details> user_detailsdata;
    String user_id, user_role;
    TextView username, email, referrer, referralcount, availablereferral, usedrreferal, referralcode, refshare,couponearned;

    private ReferralDetails referralDetails;

    RelativeLayout progresslayout;
    LinearLayout referlayout;

    Button yourreferral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(  AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {


            setTheme(R.style.darktheme);

        }

        else {


            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_and_earn);


        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        yourreferral =(Button) findViewById(R.id.yourreferral);


        mToolbar.setNavigationIcon(R.drawable.back_arrow);


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        yourreferral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(getApplicationContext(), ReferralDetailsActivity.class);
                startActivity(in);
            }
        });


        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);



        if (user_id == null) {

            Intent in = new Intent(getApplicationContext(), SigninActivity.class);
            startActivity(in);

        } else if (user_role.equalsIgnoreCase("registered")) {

            Intent in = new Intent(getApplicationContext(), Main3Activity.class);
            startActivity(in);

        } else {
            username = (TextView) findViewById(R.id.username);
            email = (TextView) findViewById(R.id.email);
            referralcode = (TextView) findViewById(R.id.referralcode);
            referralcount = (TextView) findViewById(R.id.referralcount);
            availablereferral = (TextView) findViewById(R.id.availablereferral);
            usedrreferal = (TextView) findViewById(R.id.usedrreferal);
            referrer = (TextView) findViewById(R.id.referrer);
            refshare = (TextView) findViewById(R.id.refshare);
            couponearned = (TextView) findViewById(R.id.couponearned);

            progresslayout = (RelativeLayout) findViewById(R.id.progresslayout);
            referlayout = (LinearLayout) findViewById(R.id.referlayout);


            refshare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Elite Club Refer'N'Earn offer for Coupon Codes. Each user can earn a coupon code after your get subscribed with us. The offer provides Coupon Code on every successful referral Subscriptions. " + referralcode.getText().toString());
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, "Share With"));
                }
            });


            Api.getClient().getReferDetail(user_id, new Callback<ReferralDetails>() {


                @Override
                public void success(ReferralDetails referralDetails1, retrofit.client.Response response) {

                    referralDetails = referralDetails1;

                    referralcount.setText(referralDetails.getReferrer_count());
                    availablereferral.setText(referralDetails.getAvailable_coupon());
                    usedrreferal.setText(referralDetails.getUsed_coupon());
                    referrer.setText(referralDetails.getReferrer_name());
                    couponearned.setText(referralDetails.getEarned_coupon());

                    referlayout.setVisibility(View.VISIBLE);
                    progresslayout.setVisibility(View.GONE);


                }

                @Override
                public void failure(RetrofitError error) {

                }
            });


            Call<JSONResponse> profileres = ApiClient.getInstance1().getApi().getUserprofile(user_id);
            profileres.enqueue(new retrofit2.Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase("true")) {

                            JSONResponse jsonResponse = response.body();


                            user_detailsdata = new ArrayList<>(Arrays.asList(jsonResponse.getUser_details()));

                            for (int k = 0; k < user_detailsdata.size(); k++) {

                                username.setText(user_detailsdata.get(k).getUsername());
                                email.setText(user_detailsdata.get(k).getEmail());
                                referralcode.setText(user_detailsdata.get(k).getReferral_token());


                            }

                        }


                    }

                }

                @Override
                public void onFailure(Call<JSONResponse> call, Throwable t) {

                }
            });


        }
    }

    private String getInvitationMessage() {
        String playStoreLink = "https://play.google.com/store/apps/details?id=com.atbuys.runmawi&referrer=utm_source=";
        ///return invitationId = playStoreLink + getReferralId();
       return String.valueOf(true);
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
