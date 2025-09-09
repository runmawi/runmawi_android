package com.atbuys.runmawi;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity implements InAppUpdateManager.UpdateListener {

    private static int SPLASH_TIME_OUT = 8000;
    ImageView   logo;
    private static final int PERMISSION_REQUEST_CODE = 1;
    String theme, fingerprint;
    private ArrayList<Splash_Screen> splashlist;
    String user_id;
    private InAppUpdateManager inAppUpdateManager;
    private Handler splashHandler;
    private boolean updateCheckCompleted = false;
    private boolean proceedToMainApp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.darktheme);
        }
        super.onCreate(savedInstanceState);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        splashlist = new ArrayList<>();
        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        theme = prefs.getString(sharedpreferences.theme, null);


        if (theme == null) {
            SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
            editor.putString(sharedpreferences.theme, "0");
            editor.apply();
            editor.commit();
        } else {
            if (theme.equalsIgnoreCase("1")) {
                setTheme(R.style.AppTheme);
            } else {
                setTheme(R.style.darktheme);
            }
        }

        SharedPreferences prefs1 = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        fingerprint = prefs1.getString(sharedpreferences.fingerprint, null);
        user_id = prefs1.getString(sharedpreferences.user_id, null);

        logo = (ImageView) findViewById(R.id.splash);

        //   getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        Call<JSONResponse> call = ApiClient.getInstance1().getApi().getSplash();
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JSONResponse jsonResponse = response.body();
                    splashlist = new ArrayList<>(Arrays.asList(jsonResponse.getSplash_Screen()));
                    /*Picasso.get()
                            .load(splashlist.get(0).getSplash_url())
                            .into(logo);*/

                    Glide.with(getApplicationContext())
                            .asGif()
                            .load(splashlist.get(0).getSplash_url())
                           // .load("https://runmawi.com/public/uploads/settings/andriod_splash_image_1734335082.gif")
                            .into(logo);

                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

            }
        });

        // Initialize In-App Update Manager
        setupInAppUpdate();
        
        // TEMPORARY: Add direct update tester for debugging
        InAppUpdateTester tester = new InAppUpdateTester(this);
        tester.testUpdateAvailability();
        
        // Start update check immediately
        startUpdateCheck();
    }

    private void setupInAppUpdate() {
        inAppUpdateManager = new InAppUpdateManager(this);
        inAppUpdateManager.setUpdateListener(this);
        Log.d("SplashScreen", "InAppUpdateManager setup complete");
    }
    
    private void startUpdateCheck() {
        Log.d("SplashScreen", "Starting update check...");
        
        // Check internet connection first
        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        
        if (isNetworkConnected(connec)) {
            // Start the update check
            inAppUpdateManager.checkForUpdates();
            
            // Set a timeout to proceed even if update check takes too long
            splashHandler = new Handler();
            splashHandler.postDelayed(() -> {
                if (!updateCheckCompleted) {
                    Log.w("SplashScreen", "Update check timeout, proceeding to main app");
                    proceedToMainAppInternal();
                }
            }, SPLASH_TIME_OUT);
        } else {
            // No internet connection
            new Handler().postDelayed(() -> {
                Intent in = new Intent(SplashScreenActivity.this, InternetConnectionActivity.class);
                startActivity(in);
                finish();
            }, SPLASH_TIME_OUT);
        }
         }
     
    // ==================== UpdateListener Implementation ====================
    
    @Override
    public void onUpdateCheckComplete(boolean updateRequired, boolean forceUpdate) {
        updateCheckCompleted = true;
        
        Log.d("SplashScreen", "Update check complete - Required: " + updateRequired + ", Force: " + forceUpdate);
        
        // Add debug logging
        if (inAppUpdateManager != null) {
            RemoteConfigManager remoteConfig = RemoteConfigManager.getInstance();
            remoteConfig.logAllValues();
        }
        
        if (forceUpdate) {
            Log.d("SplashScreen", "Critical update required - blocking app");
            // Don't proceed to main app - wait for update
        } else if (updateRequired) {
            Log.d("SplashScreen", "Update available but optional - proceeding to app");
            // Optional update available, but can proceed
            scheduleMainAppStart();
        } else {
            Log.d("SplashScreen", "App is up to date - proceeding to app");
            scheduleMainAppStart();
        }
    }
    
    @Override
    public void onUpdateDownloadStarted() {
        Log.d("SplashScreen", "Update download started");
        // You can show a progress indicator here if needed
    }
    
    @Override
    public void onUpdateDownloadCompleted() {
        Log.d("SplashScreen", "Update download completed");
        // Show snackbar to complete update
        showUpdateCompleteSnackbar();
    }
    
    @Override
    public void onUpdateFailed(String error) {
        Log.e("SplashScreen", "Update failed: " + error);
        
        // Show user-friendly error message
        if (error.contains("Google Play Services")) {
            Toast.makeText(this, "Please update Google Play Services and try again", Toast.LENGTH_LONG).show();
        } else if (error.contains("signed into Google Play Store")) {
            Toast.makeText(this, "Please sign into Google Play Store", Toast.LENGTH_LONG).show();
        } else if (error.contains("only work for apps installed from Google Play Store")) {
            Toast.makeText(this, "App installed via USB. In-app updates require Play Store installation.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Update check failed. Using current version.", Toast.LENGTH_SHORT).show();
        }
        
        scheduleMainAppStart();
    }
    
    @Override
    public void onUpdateCancelled() {
        Log.d("SplashScreen", "Update cancelled by user");
        
        // Check if this was a force update
        if (inAppUpdateManager.isForceUpdateRequired()) {
            showForceUpdateDialog();
        } else {
            // Optional update cancelled, proceed to main app
            scheduleMainAppStart();
        }
    }
    
    // ==================== Helper Methods ====================
    
    private void scheduleMainAppStart() {
        if (!proceedToMainApp) {
            proceedToMainApp = true;
            
            // Cancel any existing handler
            if (splashHandler != null) {
                splashHandler.removeCallbacksAndMessages(null);
            }
            
            // Start main app after a short delay (or immediately if timeout already passed)
            splashHandler = new Handler();
            splashHandler.postDelayed(this::proceedToMainAppInternal, 1000);
        }
    }
    
    private void proceedToMainAppInternal() {
        if (!isFinishing()) {
            Log.d("SplashScreen", "Proceeding to main app");
            
            // Use your existing navigation logic
            if (user_id == null) {
                Intent homeIntent1 = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                startActivity(homeIntent1);
            } else {
                Intent homeIntent1 = new Intent(SplashScreenActivity.this, HomePageActivitywithFragments.class);
                startActivity(homeIntent1);
            }
            finish();
        }
    }
    
    private void showUpdateCompleteSnackbar() {
        Snackbar snackbar = Snackbar.make(
            findViewById(android.R.id.content),
            "Update downloaded",
            Snackbar.LENGTH_INDEFINITE
        );
        
        snackbar.setAction("RESTART", v -> {
            if (inAppUpdateManager != null) {
                inAppUpdateManager.completeUpdate();
            }
        });
        
        snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }
    
    private void showForceUpdateDialog() {
        String message = inAppUpdateManager.getUpdateMessage();
        
        new AlertDialog.Builder(this)
                .setTitle("Update Required")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Update", (dialog, which) -> {
                    inAppUpdateManager.startImmediateUpdate();
                })
                .setNegativeButton("Exit", (dialog, which) -> {
                    finish();
                })
                .show();
    }
    
    private boolean isNetworkConnected(ConnectivityManager connec) {
        return connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
               connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
               connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
               connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED;
    }
    
    // ==================== Activity Lifecycle Methods ====================
    
    @Override
    protected void onResume() {
        super.onResume();
        
        // Check for resumable updates (in case immediate update was interrupted)
        if (inAppUpdateManager != null) {
            inAppUpdateManager.checkForResumableUpdate();
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        // Handle update result
        if (inAppUpdateManager != null) {
            inAppUpdateManager.onActivityResult(requestCode, resultCode);
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        // Cleanup
        if (splashHandler != null) {
            splashHandler.removeCallbacksAndMessages(null);
        }
        
        if (inAppUpdateManager != null) {
            inAppUpdateManager.cleanup();
        }
    }
}