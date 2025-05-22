package com.atbuys.runmawi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HelpCenterActivity extends AppCompatActivity {

    LinearLayout password_update,changepass_layout,subscribe_layout,payment_settings,help_center;
    private RecyclerView recyclerView;
    private ArrayList<plans> data;
    private PlansAdapter adapter;
    private resetpass resetPass;

    public String userid,email,theme;
    Button verifybutton;
    EditText password,confirmpass,oldpass;
    ProgressDialog progressDialog;
    RecyclerView cmsRecycler;
    PagesAdapter pagesadapter;
    TabLayout tableLayout;
    ViewPager viewPager;
    private ArrayList<pages> pageslist;
    ImageView back_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(  AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {


            setTheme(R.style.darktheme);

        }

        else {


            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);

        progressDialog = new ProgressDialog(this);

        password_update = (LinearLayout) findViewById(R.id.password_update);
        changepass_layout = (LinearLayout) findViewById(R.id.changepass_layout);
        subscribe_layout = (LinearLayout)  findViewById(R.id.subscribe_layout);
        help_center = (LinearLayout) findViewById(R.id.help_center);
        payment_settings = (LinearLayout) findViewById(R.id.payment_settings);
        cmsRecycler = (RecyclerView) findViewById(R.id.help_recycler_view);
        tableLayout=(TabLayout ) findViewById(R.id.tab_layout);
        viewPager=(ViewPager)findViewById(R.id.view_pager);
        back_arrow=(ImageView)findViewById(R.id.back_arrow);

        pageslist = new ArrayList<pages>();

        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        userid = prefs.getString(sharedpreferences.user_id,null );
        email=prefs.getString(sharedpreferences.email,null);
        theme = prefs.getString(sharedpreferences.theme,null);


        verifybutton=(Button)findViewById(R.id.submit);
        password=(EditText)findViewById(R.id.password1);
        confirmpass=(EditText)findViewById(R.id.confirmpassword1) ;
        oldpass=(EditText)findViewById(R.id.oldpass);


        tableLayout.addTab(tableLayout.newTab().setText("FAQ"));
        tableLayout.addTab(tableLayout.newTab().setText("Contact us"));
        tableLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final HelpCenterAdapter adapter = new HelpCenterAdapter(this,getSupportFragmentManager(), tableLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tableLayout));

        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        initViews();
        password_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(changepass_layout.getVisibility() == View.VISIBLE) {

                    changepass_layout.setVisibility(View.GONE);

                }
                else
                {
                    changepass_layout.setVisibility(View.VISIBLE);
                    Animation aniSlide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
                    changepass_layout.startAnimation(aniSlide);
                }
            }
        });

        pagesadapter = new PagesAdapter(pageslist, getApplicationContext());
        cmsRecycler.setHasFixedSize(true);
        cmsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        cmsRecycler.setAdapter(pagesadapter);


        Call<JSONResponse> callser = ApiClient.getInstance1().getApi().getCmsPage();
        callser.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                JSONResponse jsonResponse = response.body();

                if (jsonResponse.getStatus().equalsIgnoreCase("true")) {

                 //   cmsRecycler.setVisibility(View.VISIBLE);
                    pageslist = new ArrayList<>(Arrays.asList(jsonResponse.getPages()));
                    pagesadapter = new PagesAdapter(pageslist);
                    cmsRecycler.setAdapter(pagesadapter);
                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });

        cmsRecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {


                        if (pageslist.size() > position) {
                            if (pageslist.get(position) != null) {

                                Intent in = new Intent(getApplicationContext(), FaqActivity.class);
                                in.putExtra("title", pageslist.get(position).getTitle());
                                in.putExtra("body", pageslist.get(position).getBody());
                                startActivity(in);
                            }

                        }
                    }
                }
                ));

        verifybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.setMessage("Please Wait");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.setMax(100);
                progressDialog.show();


                if(oldpass.getText().toString().length()==0){
                    oldpass.setError("Old password not entered");
                    oldpass.requestFocus();
                }

                else if(password.getText().toString().length()==0){
                    password.setError("Password not entered");
                    password.requestFocus();
                }
                else if(confirmpass.getText().toString().length()==0){
                    confirmpass.setError("Confirm password not entered");
                    confirmpass.requestFocus();
                }

                else if(!password.getText().toString().equals(confirmpass.getText().toString())){
                    confirmpass.setError("Password Not matched");
                    confirmpass.requestFocus();
                }

                else if (password.getText().toString().length() < 8) {
                    password.setError("Password must be at least 8 characters.");
                    password.requestFocus();
                }

                else {

                    final String pass=password.getText().toString();
                    final String oldpass1=oldpass.getText().toString();

                    Api.getClient().getChangepassword(pass,email,oldpass1, new Callback<resetpass>() {

                        @Override
                        public void success(resetpass resetpass, retrofit.client.Response response) {

                            resetPass = resetpass;

                            if(resetpass.getStatus().equalsIgnoreCase("true")) {

                                progressDialog.hide();

                                Toast.makeText(getApplicationContext(),""+resetpass.getMessage(), Toast.LENGTH_LONG).show();

                                changepass_layout.setVisibility(View.GONE);
                            }
                            else {

                                Toast.makeText(getApplicationContext(),""+resetpass.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }});

                }
            }
        });



/*
        payment_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(subscribe_layout.getVisibility() == View.VISIBLE) {
                    subscribe_layout.setVisibility(View.GONE);

                }
                else
                {

                    subscribe_layout.setVisibility(View.VISIBLE);
                    Animation aniSlide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
                    subscribe_layout.startAnimation(aniSlide);
                }
            }
        });
*/

    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        loadJSON();
    }

    private void loadJSON() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://runmawi.com/api/auth/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponse> call = request.getStripeOnetime();
        call.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                data = new ArrayList<>(Arrays.asList(jsonResponse.getPlans()));
                adapter = new PlansAdapter(data, jsonResponse.getCurrency_Symbol(),"off",HelpCenterActivity.this);
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });

    }

    public void newmethod(View view) {

        if(help_center.getVisibility() == View.VISIBLE) {
            help_center.setVisibility(View.GONE);

        }
        else
        {

            help_center.setVisibility(View.VISIBLE);
            Animation aniSlide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
            help_center.startAnimation(aniSlide);
        }

    }
}