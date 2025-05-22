package com.atbuys.runmawi;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit2.Call;


public class MyAccountActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    @SuppressLint("ResourceAsColor")
    LinearLayout subscriber, nonsubscriber, logout, changepass, editprofile;

    TextView username, useremail, usercountry, userstate, usercity;
    CircleImageView userimage;
    LinearLayout editicon;
    Button getsubscription;
    LinearLayout cancelpayment, upgrade, renewsubscribe;
    cancelpayment signUpResponsesData;
    UpgradeResponse upgradeResponse;
    RenewSubscribe renewSubscribe;
    subscriberesponse subscribeResponse;

    String user_id, user_role;

    private ArrayList<user_details> user_detailsdata;
    TextView billing_date, period;

    RelativeLayout progresslayout;
    LinearLayout myaccountlayout;
    public static GoogleApiClient googleApiClient;

    TextView plan;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {


            setTheme(R.style.darktheme);

        } else {


            setTheme(R.style.AppTheme);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        subscriber = (LinearLayout) findViewById(R.id.subscriber);
        nonsubscriber = (LinearLayout) findViewById(R.id.register);
        logout = (LinearLayout) findViewById(R.id.logout);
        username = (TextView) findViewById(R.id.username);
        useremail = (TextView) findViewById(R.id.useremail);
        userimage = (CircleImageView) findViewById(R.id.image);
        changepass = (LinearLayout) findViewById(R.id.changepassword);
        editprofile = (LinearLayout) findViewById(R.id.editprofile);
        editicon = (LinearLayout) findViewById(R.id.editicon);
        usercountry = (TextView) findViewById(R.id.countryprofile);
        userstate = (TextView) findViewById(R.id.stateprofile);
        cancelpayment = findViewById(R.id.cancelpayment);
        billing_date = (TextView) findViewById(R.id.billing_date);
        period = (TextView) findViewById(R.id.period);
        upgrade = findViewById(R.id.upgradepayment);
        renewsubscribe = findViewById(R.id.renewmembership);
        usercity = (TextView) findViewById(R.id.cityprofile);
        plan = (TextView) findViewById(R.id.planvalue);


        myaccountlayout = (LinearLayout) findViewById(R.id.myaccountlayout);
        progresslayout = (RelativeLayout) findViewById(R.id.progresslayout);

        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);


        if (user_id != null && user_role != null) {
            if (user_role.equalsIgnoreCase("subscriber") || user_role.equalsIgnoreCase("admin")) {

                subscriber.setVisibility(View.GONE);
                nonsubscriber.setVisibility(View.GONE);

                Api.getClient().getSubscribeDetail(user_id, new Callback<subscriberesponse>() {
                    @Override
                    public void success(subscriberesponse regresponse, retrofit.client.Response response) {
                        subscribeResponse = regresponse;

                        if (regresponse.getStatus().equalsIgnoreCase("Cancel Subscription")) {

                            period.setText("Next Billing Date:");

                            cancelpayment.setVisibility(View.VISIBLE);
                            renewsubscribe.setVisibility(View.GONE);

                        } else if (regresponse.getStatus().equalsIgnoreCase("Renew Subscription")) {

                            period.setText("Your Subscription Ends At");
                            renewsubscribe.setVisibility(View.VISIBLE);
                            cancelpayment.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                        Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                nonsubscriber.setVisibility(View.GONE);
                subscriber.setVisibility(View.GONE);
            }
        }


        cancelpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(MyAccountActivity.this);
                builder.setMessage("Are you sure you want to Cancel Your Subscription?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Api.getClient().getcancelpayment(user_id,

                                        new Callback<cancelpayment>() {
                                            @Override
                                            public void success(cancelpayment signUpResponse, retrofit.client.Response response) {
                                                signUpResponsesData = signUpResponse;

                                                cancelpayment.setVisibility(View.GONE);
                                                renewsubscribe.setVisibility(View.VISIBLE);

                                                Toast.makeText(getApplicationContext(), "" + signUpResponse.getMessage(), Toast.LENGTH_LONG).show();


                                            }

                                            @Override
                                            public void failure(RetrofitError error) {

                                                Toast.makeText(MyAccountActivity.this, "Check Your Internet Connection", Toast.LENGTH_LONG).show();

                                            }
                                        });

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });


        renewsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MyAccountActivity.this);
                builder.setMessage("Are you sure you want to Renew Your Subscription?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Api.getClient().getRenewsub(user_id,
                                        new Callback<RenewSubscribe>() {
                                            @Override
                                            public void success(RenewSubscribe renewSubscribe1, retrofit.client.Response response) {
                                                renewSubscribe = renewSubscribe1;

                                                cancelpayment.setVisibility(View.VISIBLE);
                                                renewsubscribe.setVisibility(View.GONE);

                                                Toast.makeText(getApplicationContext(), "" + renewSubscribe1.getMessage(), Toast.LENGTH_LONG).show();

                                            }

                                            @Override
                                            public void failure(RetrofitError error) {

                                                Toast.makeText(MyAccountActivity.this, "Check Your Internet Connection", Toast.LENGTH_LONG).show();

                                            }
                                        });

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });


        getsubscription = (Button) findViewById(R.id.getsubscription);
        getsubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MyAccountActivity.this, SubscribeActivity.class);
                startActivity(in);
            }
        });


        upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), UpgradeSubscription.class);
                startActivity(intent);

            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);


       /* editicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/


        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MyAccountActivity.this, ChangePasswordActivity.class);
                startActivity(in);
            }
        });


        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MyAccountActivity.this, EditProfileActivity.class);
                startActivity(in);

         ///       Toast.makeText(getApplicationContext(),"Coming soon",Toast.LENGTH_SHORT).show();
            }
        });


        String language_code ="es-co";
        Resources res = getApplicationContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(language_code.toLowerCase())); // API 17+ only.
        res.updateConfiguration(conf, dm);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  AdaptiveExoplayer.getInstance().getDownloadManager().removeAllDownloads();

                final SharedPreferences prefs = getApplicationContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
                final SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();
                Intent in = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(in);
                Toast.makeText(getApplicationContext(), "User Successfully logged out", Toast.LENGTH_SHORT).show();

                LoginManager.getInstance().logOut();


                if (googleApiClient.isConnected()) {

                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    if (status.isSuccess()) {
                                        editor.clear();
                                        editor.apply();


                                    } else {
                                        Toast.makeText(getApplicationContext(), "Session not close", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                }

            }
        });


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(getApplicationContext(), HomePageActivitywithFragments.class);
                startActivity(in);

            }
        });


        if (user_role != null) {

            if (user_role.equalsIgnoreCase("subscriber") || user_role.equalsIgnoreCase("admin")) {

                subscriber.setVisibility(View.GONE);
                nonsubscriber.setVisibility(View.GONE);
            } else {
                nonsubscriber.setVisibility(View.GONE);
                subscriber.setVisibility(View.GONE);
            }
        }

        Call<JSONResponse> profileres = ApiClient.getInstance1().getApi().getUserprofile(user_id);
        profileres.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                if (response.body() != null) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        JSONResponse jsonResponse = response.body();

                        plan.setText("You Are Subscribed :  " + jsonResponse.getCurren_stripe_plan());


                        Api.getClient().getSubscribeDetail(user_id, new Callback<subscriberesponse>() {
                            @Override
                            public void success(subscriberesponse regresponse, retrofit.client.Response response) {
                                subscribeResponse = regresponse;

                                myaccountlayout.setVisibility(View.VISIBLE);
                                progresslayout.setVisibility(View.GONE);


                                if (regresponse.getStatus().equalsIgnoreCase("Cancel Subscription")) {

                                    billing_date.setText(jsonResponse.getNext_billing());


                                } else if (regresponse.getStatus().equalsIgnoreCase("Renew Subscription")) {

                                    billing_date.setText(jsonResponse.getEnds_at());

                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {

                                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });


                        user_detailsdata = new ArrayList<>(Arrays.asList(jsonResponse.getUser_details()));

                        //Toast.makeText(MyAccountActivity.this, user_detailsdata.get(k).getProfile_url(), Toast.LENGTH_SHORT).show();

                        for (int k = 0; k < user_detailsdata.size(); k++) {
                            //Toast.makeText(MyAccountActivity.this, user_detailsdata.get(k).getProfile_url(), Toast.LENGTH_SHORT).show();

                            String name = user_detailsdata.get(k).getUsername();
                            String[] splited = name.split("\\s+");

                            username.setText(splited[0]);
                            useremail.setText(user_detailsdata.get(k).getEmail());
                            usercountry.setText(user_detailsdata.get(k).getCountry_name());
                            userstate.setText(user_detailsdata.get(k).getState_name());
                            usercity.setText(user_detailsdata.get(k).getCity_name());


                            String userprofile = user_detailsdata.get(k).getProfile_url();

                            Picasso.get().load(userprofile).into(userimage);

                        }

                    }


                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
