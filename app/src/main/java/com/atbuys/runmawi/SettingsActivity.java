package com.atbuys.runmawi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {

    Switch dialog_button1,dialog_button2,dialog_button3;
    String fingerprint,theme;

    TextView payments;
    TextView comingsoon;
    ImageView backarrow;

    LinearLayout faq_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        if(  AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {

            setTheme(R.style.darktheme);

        }

        else
            {
            setTheme(R.style.AppTheme);
            }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dialog_button1  = findViewById(R.id.dialog_button1);
        dialog_button2=findViewById(R.id.dialog_button2);
        dialog_button3 = findViewById(R.id.dialog_button3);
        faq_settings = (LinearLayout) findViewById(R.id.faq_settings);
        backarrow = (ImageView) findViewById(R.id.backarrow);

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        payments= findViewById(R.id.payments);
        comingsoon=findViewById(R.id.comingsoon);



        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        fingerprint = prefs.getString(sharedpreferences.fingerprint,null );
        theme = prefs.getString(sharedpreferences.theme,null);


        if(fingerprint == null)
        {

            dialog_button2.setChecked(false);
        }

        else if(fingerprint.equalsIgnoreCase("1"))
        {
            dialog_button2.setChecked(true);
        }

        else
        {
            dialog_button2.setChecked(false);
        }





        payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SettingsActivity.this, SubscribeActivity.class);
                startActivity(intent);
            }
        });


       comingsoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SettingsActivity.this,  CommingsoonActivity.class);
                startActivity(intent);
            }
        });


        dialog_button2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {

                    dialog_button2.setChecked(true);
                    SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name,MODE_PRIVATE).edit();
                    editor.putString(sharedpreferences.fingerprint, "1");
                    editor.apply();
                    editor.commit();
                }

                else
                    {
                    dialog_button2.setChecked(false);

                    SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                    editor.putString(sharedpreferences.fingerprint, "0");
                   // editor.putBoolean("switchkey",true);
                    editor.apply();
                    editor.commit();
                }
            }
        });

        faq_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent( getApplicationContext(), FaqsActivity.class);
                startActivity(in);
            }
        });


    }
}