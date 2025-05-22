package com.atbuys.runmawi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atbuys.runmawi.Remote.IpService;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class UpgradeSubscription extends AppCompatActivity {



    Button paynow;
    String user_id;
    paymentresponse signUpResponsesData;
    private Object Context;
    private RecyclerView recyclerView;
    private ArrayList<plans> data;
    private PlansAdapter adapter;
    registerresponse registerresponse1;
    RelativeLayout pro;
    LinearLayout page;
  //  ImageView logo;
    String plan;
    CheckBox checkBox;
    String x,y,z;
    String username,email,password,country,state,city;
    IsPayment isPayment;
    ImageView logo1;
    private ArrayList<settings> settings;
    IpService mService;
    String ipAddress;
    Button pay_now;
    ProgressDialog progressDialog;
    TextView skip;
    TextView planid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_payment);


        progressDialog = new ProgressDialog(this);
        initViews();

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);
        pro = (RelativeLayout) findViewById(R.id.pro);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        page = (LinearLayout) findViewById(R.id.page);
        pay_now=(Button) findViewById(R.id.pay_now);
        skip=(TextView) findViewById(R.id.skipp);
       // planid =(TextView) findViewById(R.id.planid);




        progressDialog.setMessage("Please Wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.show();


      /*  Call<JSONResponse> call = ApiClient.getInstance1().getApi().getSettings();

        call.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();

                settings = new ArrayList<>(Arrays.asList(jsonResponse.getSettings()));

                String x = settings.get(0).getImage_url();
                Picasso.get().load(x).into(logo1);

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error1", t.getMessage());
            }
        });*/


        Api.getClient().getIsPayment(
        new Callback<IsPayment>() {
            @Override
            public void success(IsPayment latestenablemovie1, Response response) {
                isPayment = latestenablemovie1;
               // checkBox.setVisibility(View.VISIBLE);
                if(isPayment.getIs_payment().equalsIgnoreCase(String.valueOf(0))) {



                    skip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(checkBox.isChecked()) {

                                checkBox.setChecked(false);
                            }

                            else {
                                checkBox.setChecked(true);
                                finish();

                            }

                        }
                    });


                }

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(UpgradeSubscription.this, error.toString(), Toast.LENGTH_LONG).show();
                    }

                });



        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            finish();
            }
        });







        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        final String user_role = prefs.getString(sharedpreferences.role, null);


    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        loadJSON();
    }

    private void loadJSON() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://runmawi.com/api/auth/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponse> call = request.getJSON();
        call.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                data = new ArrayList<>(Arrays.asList(jsonResponse.getPlans()));
                adapter = new PlansAdapter(data, jsonResponse.getCurrency_Symbol(),"off",UpgradeSubscription.this);
                progressDialog.cancel();
                recyclerView.setAdapter(adapter);



                recyclerView.addOnItemTouchListener(
                        new RecyclerItemClickListener(UpgradeSubscription.this, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                if (data.size() > position) {
                                    if (data.get(position) != null) {


                                        AlertDialog.Builder builder = new AlertDialog.Builder(UpgradeSubscription.this);
                                        builder.setMessage("Are you sure you want to Upgrade Your Subscription?")
                                                .setCancelable(false)
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {

                                                        Intent in = new Intent(getApplicationContext(), UpgradeSupscriptionPaymentActivity.class);
                                                        in.putExtra("plan",data.get(position).getPlan_id());
                                                        startActivity(in);
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
                                }
                            }
                        }));


/*
                pay_now.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {




                    }
                });
*/
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

}