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
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 8000;
    ImageView   logo;
    private static final int PERMISSION_REQUEST_CODE = 1;
    String theme, fingerprint;
    private ArrayList<Splash_Screen> splashlist;
    String user_id;
    private final int APP_UPDATE_REQUEST_CODE = 1230;
    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;

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

    }

    private void checkUpdate(){

        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int minAllowedVersion = (int) firebaseRemoteConfig.getLong("min_supported_version");
                        int currentVersion = BuildConfig.VERSION_CODE;

                        if (currentVersion < minAllowedVersion) {//39<38
                            // Block access
                            showForceUpdateDialog();
                        }else{
                            checkInternetConenction();
                        }
                    }
                });
    }

    private void showForceUpdateDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Update Required")
                .setMessage("Please update the app to continue.")
                .setCancelable(false)
                .setPositiveButton("Update", (dialog, which) -> {
                    // Open Play Store
                    final String appPackageName = getPackageName();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }).show();
    }



    private boolean checkInternetConenction() {
        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() ==
                android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
                    Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

                    appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
                        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                            try {
                                appUpdateManager.startUpdateFlowForResult(
                                        appUpdateInfo,
                                        AppUpdateType.IMMEDIATE,
                                        SplashScreenActivity.this,
                                        APP_UPDATE_REQUEST_CODE
                                );
                            } catch (IntentSender.SendIntentException e) {
                            }
                        } else {
                            if (user_id == null) {
                                Intent homeIntent1 = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                                startActivity(homeIntent1);
                            } else {
                                Intent homeIntent1 = new Intent(SplashScreenActivity.this, HomePageActivitywithFragments.class);
                                startActivity(homeIntent1);
                            }
                            finish();
                        }
                    });
                    appUpdateInfoTask.addOnFailureListener(command -> {
                        if (user_id == null) {
                            Intent homeIntent1 = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                            startActivity(homeIntent1);
                        } else {
                            Intent homeIntent1 = new Intent(SplashScreenActivity.this, HomePageActivitywithFragments.class);
                            startActivity(homeIntent1);
                        }
                        finish();
                    });

                    installStateUpdatedListener = state -> {
                        Log.w("inAppUpdates", String.valueOf(state.installStatus()));
                        if (state.installStatus() == InstallStatus.DOWNLOADING) {
                            long bytesDownloaded = state.bytesDownloaded();
                            long totalBytesToDownload = state.totalBytesToDownload();
                            Log.w("inAppUpdates", "bytesDownloaded " + bytesDownloaded + " / " + totalBytesToDownload);
                            // Update UI to show download progress.
                        } else if (state.installStatus() == InstallStatus.DOWNLOADED) {
                            Log.w("inAppUpdates", "Update is downloaded and ready to install ");
                            popupSnackbarForCompleteUpdate();
                            // Notify the user and request installation.
                        } else if (state.installStatus() == InstallStatus.INSTALLING) {
                            Log.w("inAppUpdates", "Update is being installed");
                            // Update UI to show installation progress.
                        } else if (state.installStatus() == InstallStatus.INSTALLED) {
                            Log.w("inAppUpdates", "Update is installed");
                            // Notify the user and perform any necessary actions.
                        } else if (state.installStatus() == InstallStatus.FAILED) {
                            Log.w("inAppUpdates", "Update failed to install");
                            // Notify the user and handle the error.
                        }
                    };
                    appUpdateManager.registerListener(installStateUpdatedListener);


                }
            }, SPLASH_TIME_OUT);

            return true;
        } else if (
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                android.net.NetworkInfo.State.DISCONNECTED) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent in = new Intent(SplashScreenActivity.this, InternetConnectionActivity.class);
                    startActivity(in);
                }
            }, SPLASH_TIME_OUT);
            return false;
        }
        return false;
    }

    private void popupSnackbarForCompleteUpdate() {
        Snackbar.make(findViewById(android.R.id.content), "An update has just been downloaded.", Snackbar.LENGTH_INDEFINITE
        ).setAction("INSTALL", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appUpdateManager.completeUpdate();
            }
        }).setActionTextColor(getResources().getColor(R.color.colorAccent)).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_UPDATE_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
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
    }

    @Override
    protected void onDestroy() {
        if (appUpdateManager != null) {
            appUpdateManager.unregisterListener(installStateUpdatedListener);
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        appUpdateManager = AppUpdateManagerFactory.create(this);
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            // If the update is downloaded but not installed,
            // notify the user to complete the update.
            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate();
            }
        });
        checkInternetConenction();
        //checkUpdate();

    }


}