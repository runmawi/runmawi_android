package com.atbuys.runmawi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class NotificationSettings extends AppCompatActivity {

    ImageView backarrow;
    Switch dark_icon;
    String theme_change;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(  AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            getApplicationContext().setTheme(R.style.darktheme);
        }
        else {
            getApplicationContext().setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_notification_settings);

        backarrow=(ImageView)findViewById(R.id.backarrow);
        dark_icon = (Switch) findViewById(R.id.dark_icon);

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        prefs = this.getApplicationContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        theme_change=prefs.getString(sharedpreferences.theme,null);

        if (theme_change.equalsIgnoreCase("1")){
            dark_icon.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        dark_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dark_icon.setChecked(dark_icon.isChecked());
                editor=prefs.edit();

                if (dark_icon.isChecked()){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putString(sharedpreferences.theme, String.valueOf(1));
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putString(sharedpreferences.theme, String.valueOf(0));
                }
                editor.apply();
            }
        });

    }
}