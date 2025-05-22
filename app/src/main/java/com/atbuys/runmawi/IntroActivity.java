package com.atbuys.runmawi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    Button letstart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        if(prefs.getBoolean(sharedpreferences.isstart, false)) {
            startActivity(new Intent(IntroActivity.this, HomePageActivitywithFragments.class));
            finish();
        }


        letstart = findViewById(R.id.let_start);
        letstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                editor.putBoolean(sharedpreferences.isstart,true);
                editor.apply();
                editor.commit();

                Intent intent = new Intent(IntroActivity.this, SigninActivity.class);
                startActivity(intent);


            }
        });



    }



}



