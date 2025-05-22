package com.atbuys.runmawi;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class InternetConnectionActivity extends AppCompatActivity {

    Button retry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_connection);

        retry=findViewById(R.id.retry);
        if(CheckNetwork.isInternetAvailable(InternetConnectionActivity.this)) {
            finish();
        }

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(50);
                anim.setStartOffset(20);
                retry.startAnimation(anim);
                if(CheckNetwork.isInternetAvailable(InternetConnectionActivity.this)) {
                    finish();
                }
            }
        });

      /*  final Handler handler1 = new Handler();
        final Runnable Update1 = new Runnable() {
            public void run() {
                if(CheckNetwork.isInternetAvailable(InternetConnectionActivity.this)) {
                    finish();
                }
            }
        };
        Timer swipeTimerr = new Timer();
        swipeTimerr.schedule(new TimerTask() {
            @Override
            public void run() {
                handler1.post(Update1);
            }
        }, 1000, 1000);*/

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(CheckNetwork.isInternetAvailable(InternetConnectionActivity.this)) {
            finish();
        }
    }
}