package com.atbuys.runmawi;

import android.app.Activity;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;

/**
 * Enhanced test class for comprehensive Play Store update checking
 */
public class InAppUpdateTester {
    
    private static final String TAG = "InAppUpdateTester";
    private static final int REQUEST_CODE_UPDATE = 1002;
    
    private Activity activity;
    private AppUpdateManager appUpdateManager;
    
    public InAppUpdateTester(Activity activity) {
        this.activity = activity;
        this.appUpdateManager = AppUpdateManagerFactory.create(activity);
    }
    
    /**
     * Comprehensive test method to examine Play Store API responses
     */
    public void testUpdateAvailability() {
        Log.d(TAG, "=== COMPREHENSIVE UPDATE CHECK ===");
        
        // Environment information
        logEnvironmentInfo();
        
        // Check Google Play Services
        checkGooglePlayServices();
        
        // Check installer source
        if (!checkInstallerSource()) {
            return;
        }
        
        // Get Play Store update info
        checkPlayStoreUpdates();
    }
    
    private void logEnvironmentInfo() {
        Log.d(TAG, "📱 ENVIRONMENT INFO:");
        Log.d(TAG, "Current app version: " + BuildConfig.VERSION_CODE);
        Log.d(TAG, "Version name: " + BuildConfig.VERSION_NAME);
        Log.d(TAG, "Package name: " + activity.getPackageName());
        
        try {
            PackageInfo packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            Log.d(TAG, "Package version code: " + packageInfo.versionCode);
            Log.d(TAG, "Package version name: " + packageInfo.versionName);
            Log.d(TAG, "First install time: " + packageInfo.firstInstallTime);
            Log.d(TAG, "Last update time: " + packageInfo.lastUpdateTime);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to get package info", e);
        }
    }
    
    private void checkGooglePlayServices() {
        Log.d(TAG, "🔧 GOOGLE PLAY SERVICES:");
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int status = googleAPI.isGooglePlayServicesAvailable(activity);
        Log.d(TAG, "Play Services status: " + status);
        Log.d(TAG, "Play Services available: " + (status == com.google.android.gms.common.ConnectionResult.SUCCESS));
    }
    
    private boolean checkInstallerSource() {
        Log.d(TAG, "📦 INSTALLER SOURCE:");
        String installer = activity.getPackageManager().getInstallerPackageName(activity.getPackageName());
        Log.d(TAG, "App installer: " + installer);
        
        if (installer == null) {
            Log.w(TAG, "⚠️ Installer is null (sideloaded)");
            return false;
        }
        
        switch (installer) {
            case "com.android.vending":
                Log.d(TAG, "✅ Installed from Google Play Store");
                return true;
            case "com.google.android.packageinstaller":
                Log.w(TAG, "⚠️ Installed via Package Installer");
                return false;
            default:
                Log.w(TAG, "⚠️ Unknown installer: " + installer);
                return false;
        }
    }
    
    private void checkPlayStoreUpdates() {
        Log.d(TAG, "🛒 PLAY STORE UPDATE CHECK:");
        Log.d(TAG, "Creating AppUpdateManager...");
        
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            Log.d(TAG, "✅ PLAY STORE RESPONSE SUCCESS");
            logDetailedUpdateInfo(appUpdateInfo);
            
            // Try to understand why no update is available
            analyzeUpdateAvailability(appUpdateInfo);
        });
        
        appUpdateInfoTask.addOnFailureListener(e -> {
            Log.e(TAG, "❌ PLAY STORE RESPONSE FAILED");
            Log.e(TAG, "Exception type: " + e.getClass().getSimpleName());
            Log.e(TAG, "Exception message: " + e.getMessage());
            Log.e(TAG, "Full exception: ", e);
        });
        
        appUpdateInfoTask.addOnCompleteListener(task -> {
            Log.d(TAG, "🏁 Update check completed. Success: " + task.isSuccessful());
        });
    }
    
    private void logDetailedUpdateInfo(AppUpdateInfo appUpdateInfo) {
        Log.d(TAG, "📊 DETAILED UPDATE INFO:");
        Log.d(TAG, "Update availability: " + appUpdateInfo.updateAvailability() + " (" + getUpdateAvailabilityString(appUpdateInfo.updateAvailability()) + ")");
        Log.d(TAG, "Available version code: " + appUpdateInfo.availableVersionCode());
        Log.d(TAG, "Current version code: " + BuildConfig.VERSION_CODE);
        Log.d(TAG, "Version difference: " + (appUpdateInfo.availableVersionCode() - BuildConfig.VERSION_CODE));
        Log.d(TAG, "Total bytes to download: " + appUpdateInfo.totalBytesToDownload());
        Log.d(TAG, "Client version staleness days: " + appUpdateInfo.clientVersionStalenessDays());
        Log.d(TAG, "Install status: " + appUpdateInfo.installStatus());
        Log.d(TAG, "Package name: " + appUpdateInfo.packageName());
        Log.d(TAG, "Is immediate update allowed: " + appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE));
        Log.d(TAG, "Is flexible update allowed: " + appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE));
    }
    
    private void analyzeUpdateAvailability(AppUpdateInfo appUpdateInfo) {
        Log.d(TAG, "🔍 ANALYSIS:");
        
        int currentVersion = BuildConfig.VERSION_CODE;
        int availableVersion = appUpdateInfo.availableVersionCode();
        
        if (availableVersion > currentVersion) {
            Log.d(TAG, "✅ Newer version available: " + availableVersion + " > " + currentVersion);
            
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                Log.d(TAG, "✅ Update is marked as available - should show dialog");
                startFlexibleUpdate(appUpdateInfo);
            } else {
                Log.w(TAG, "⚠️ Newer version exists but not marked as available");
                Log.w(TAG, "This might be due to staged rollout or other factors");
            }
        } else if (availableVersion == currentVersion) {
            Log.d(TAG, "ℹ️ Same version: " + availableVersion + " = " + currentVersion);
            Log.d(TAG, "This suggests Play Store hasn't recognized the newer production version");
        } else {
            Log.w(TAG, "⚠️ Available version is older: " + availableVersion + " < " + currentVersion);
        }
        
        // Check if this might be Internal App Sharing limitation
        if (availableVersion == currentVersion && currentVersion < 41) {
            Log.w(TAG, "🚨 POTENTIAL ISSUE: Play Store reports same version as current");
            Log.w(TAG, "🚨 But production has version 41 according to console");
            Log.w(TAG, "🚨 This confirms Internal App Sharing isolation theory");
        }
    }
    
    private String getUpdateAvailabilityString(int availability) {
        switch (availability) {
            case UpdateAvailability.UPDATE_AVAILABLE:
                return "UPDATE_AVAILABLE";
            case UpdateAvailability.UPDATE_NOT_AVAILABLE:
                return "UPDATE_NOT_AVAILABLE";
            case UpdateAvailability.UNKNOWN:
                return "UNKNOWN";
            default:
                return "UNEXPECTED_VALUE";
        }
    }
    
    private void startFlexibleUpdate(AppUpdateInfo appUpdateInfo) {
        try {
            if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                Log.d(TAG, "🚀 Starting FLEXIBLE update...");
                
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    activity,
                    AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build(),
                    REQUEST_CODE_UPDATE
                );
            } else {
                Log.w(TAG, "Flexible update not allowed, trying immediate...");
                startImmediateUpdate(appUpdateInfo);
            }
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Failed to start flexible update", e);
        }
    }
    
    private void startImmediateUpdate(AppUpdateInfo appUpdateInfo) {
        try {
            if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                Log.d(TAG, "🚀 Starting IMMEDIATE update...");
                
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    activity,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build(),
                    REQUEST_CODE_UPDATE
                );
            } else {
                Log.w(TAG, "No update types allowed");
            }
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Failed to start immediate update", e);
        }
    }
} 