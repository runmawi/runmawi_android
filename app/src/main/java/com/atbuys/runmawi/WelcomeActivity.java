package com.atbuys.runmawi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;


import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {

    ViewPager viewPager;
   // DotsIndicator dot1;
    ViewAdapter viewAdapter;
    CardView getStart;
    TextView welcome_para;
    private int currentPage = 0, NUM_PAGES = 0;

    private PrefManager prefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }
        //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);

        viewPager = findViewById(R.id.view_pager);
       // dot1 = findViewById(R.id.dot);
        getStart=(CardView)findViewById(R.id.get_start_layout);
        welcome_para=(TextView)findViewById(R.id.welcome_para);

        viewAdapter = new ViewAdapter(getApplicationContext());
        viewPager.setAdapter(viewAdapter);
       // dot1.setViewPager(viewPager);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;

                }

                viewPager.setCurrentItem(currentPage++, true);

                if (currentPage == 3) {

                    currentPage = 0;

                }

            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 1000, 2500);



        getStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
                //startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
            }
        });


    }
    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(WelcomeActivity.this, MailandOtpLoginActivity.class));
        finish();
    }
}