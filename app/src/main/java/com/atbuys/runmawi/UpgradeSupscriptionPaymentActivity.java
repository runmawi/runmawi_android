package com.atbuys.runmawi;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit2.Call;

public class UpgradeSupscriptionPaymentActivity extends AppCompatActivity {


    EditText coupon_code;
    Button submit,skip;
    String user_id,user_role;
    String plan;

    UpgradeResponse upgradeResponse;
    LinearLayout couponlist;
    RecyclerView usercouponlistrecycler;

    private ArrayList<myrefferals> myrefferallist;
    Refercode12Adopter refercodeAdopter;

    private  RecyclerView.LayoutManager channelManager;
    String refid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment__become_page);

      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        couponlist =(LinearLayout) findViewById(R.id.couponlist);
        usercouponlistrecycler =(RecyclerView)  findViewById(R.id.usercouponcode);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


        myrefferallist =new ArrayList<myrefferals>();

        refercodeAdopter = new Refercode12Adopter(myrefferallist, this);
        channelManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        usercouponlistrecycler.setHasFixedSize(true);
        usercouponlistrecycler.setLayoutManager(channelManager);
        usercouponlistrecycler.setAdapter(refercodeAdopter);

        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id,null );
        user_role = prefs.getString(sharedpreferences.role,null );


        couponlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Call<JSONResponse> channel = ApiClient.getInstance1().getApi().getCoupons(String.valueOf(user_id));
                channel.enqueue(new retrofit2.Callback<JSONResponse>() {
                    @Override
                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                        JSONResponse jsonResponse = response.body();

                        if(jsonResponse.getMyrefferals().length == 0) {


                            AlertDialog.Builder builder = new AlertDialog.Builder(UpgradeSupscriptionPaymentActivity.this);
                            builder.setMessage("You Don't have enough Coupons. Refer a Friend You have to Earn Coupons")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            dialog.cancel();

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
                        else {

                            usercouponlistrecycler.setVisibility(View.VISIBLE);
                            myrefferallist = new ArrayList<>(Arrays.asList(jsonResponse.getMyrefferals()));
                            refercodeAdopter = new Refercode12Adopter(myrefferallist);
                            usercouponlistrecycler.setAdapter(refercodeAdopter);
                        }


                    }

                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                        Log.d("Error", t.getMessage());
                    }
                });


            }
        });




        usercouponlistrecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(UpgradeSupscriptionPaymentActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (myrefferallist.size() > position) {
                            if (myrefferallist.get(position) != null) {


                                usercouponlistrecycler.setVisibility(View.GONE);
                                coupon_code.setText(myrefferallist.get(position).getCoupon_code());
                                 refid = myrefferallist.get(position).getReferrer_id();


                            }
                        }
                    }
                }));

        Intent in=getIntent();
        plan=in.getStringExtra("plan");



        coupon_code =(EditText) findViewById(R.id.couponcode);
        submit =(Button) findViewById(R.id.submit);
        skip =(Button) findViewById(R.id.skip);


    /*    if(coupon_code.getText().toString().isEmpty()) {

            skip.setVisibility(View.VISIBLE);
            submit.setVisibility(View.GONE);
        }

        else {

            skip.setVisibility(View.VISIBLE);
            submit.setVisibility(View.GONE);
        }
*/

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String coupon ="";
                String ref="";


                Api.getClient().getUpgrade(user_id,plan,coupon,ref,

                        new Callback<UpgradeResponse>() {
                            @Override
                            public void success(UpgradeResponse upgradeResponse1, retrofit.client.Response response) {
                                upgradeResponse = upgradeResponse1;

                                Toast.makeText(getApplicationContext(),""+upgradeResponse1.getSuccess(), Toast.LENGTH_LONG).show();
                                Intent in= new Intent(getApplicationContext(), MyAccountActivity.class);
                                startActivity(in);

                            }

                            @Override
                            public void failure(RetrofitError error) {

                                Toast.makeText(UpgradeSupscriptionPaymentActivity.this, "Check Your Internet Connection", Toast.LENGTH_LONG).show();

                            }
                        });


            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String coupon = coupon_code.getText().toString();

                Api.getClient().getUpgrade(user_id,plan,coupon,refid,


                        new Callback<UpgradeResponse>() {
                            @Override
                            public void success(UpgradeResponse upgradeResponse1, retrofit.client.Response response) {
                                upgradeResponse = upgradeResponse1;

                                Toast.makeText(getApplicationContext(),""+upgradeResponse1.getMessage(), Toast.LENGTH_LONG).show();

                                Intent in= new Intent(getApplicationContext(), MyAccountActivity.class);
                                startActivity(in);

                            }

                            @Override
                            public void failure(RetrofitError error) {

                                Toast.makeText(UpgradeSupscriptionPaymentActivity.this, "Check Your Internet Connection", Toast.LENGTH_LONG).show();

                            }
                        });

            }
        });



    }


}
