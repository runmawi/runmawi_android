package com.atbuys.runmawi;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

/**
 * InAppUpdateManager handles in-app updates using Firebase Remote Config for version control
 * and Google Play In-App Update API for the update flow
 */
public class InAppUpdateManager {

    private static final String TAG = "InAppUpdateManager";
    private static final int REQUEST_CODE_UPDATE = 1001;
    
    private Activity activity;
    private AppUpdateManager appUpdateManager;
    private RemoteConfigManager remoteConfigManager;
    private UpdateListener updateListener;
    
    // Update flow state
    private boolean isUpdateInProgress = false;
    private AppUpdateInfo currentUpdateInfo;
    
    public interface UpdateListener {
        void onUpdateCheckComplete(boolean updateRequired, boolean forceUpdate);
        void onUpdateDownloadStarted();
        void onUpdateDownloadCompleted();
        void onUpdateFailed(String error);
        void onUpdateCancelled();
    }
    
    public InAppUpdateManager(Activity activity) {
        this.activity = activity;
        this.appUpdateManager = AppUpdateManagerFactory.create(activity);
        this.remoteConfigManager = RemoteConfigManager.getInstance();
    }
    
    public void setUpdateListener(UpdateListener listener) {
        this.updateListener = listener;
    }
    
    /**
     * Main method to check for updates using Firebase Remote Config
     */
    public void checkForUpdates() {
        Log.d(TAG, "Checking for updates...");
        
        // First fetch remote config to get latest version info
        remoteConfigManager.fetchAndActivate(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Remote config fetched successfully");
                checkVersionRequirements();
            } else {
                Log.e(TAG, "Failed to fetch remote config", task.getException());
                // Still check for updates even if remote config fails
                checkPlayStoreUpdate();
            }
        });
    }
    
    /**
     * Check version requirements from Firebase Remote Config
     */
    private void checkVersionRequirements() {
        int currentVersion = BuildConfig.VERSION_CODE;
        long minSupportedVersion = remoteConfigManager.getMinSupportedVersion();
        boolean forceUpdateEnabled = remoteConfigManager.isForceUpdateEnabled();
        
        Log.d(TAG, "Current version: " + currentVersion);
        Log.d(TAG, "Min supported version: " + minSupportedVersion);
        Log.d(TAG, "Force update enabled: " + forceUpdateEnabled);
        
        if (currentVersion < minSupportedVersion) {
            Log.d(TAG, "Update required - version below minimum supported");
            
            if (forceUpdateEnabled) {
                // Force immediate update
                startImmediateUpdate();
            } else {
                // Check if Play Store has update available
                checkPlayStoreUpdate();
            }
            
            if (updateListener != null) {
                updateListener.onUpdateCheckComplete(true, forceUpdateEnabled);
            }
        } else {
            Log.d(TAG, "Version is supported, checking for optional updates");
            // Version is supported, but check for optional updates
            checkPlayStoreUpdate();
            
            if (updateListener != null) {
                updateListener.onUpdateCheckComplete(false, false);
            }
        }
    }
    
    /**
     * Check for updates available in Play Store
     */
    private void checkPlayStoreUpdate() {
        Log.d(TAG, "Checking Play Store for updates...");
        
        // Check if app was installed from Play Store
        if (!isInstalledFromPlayStore()) {
            Log.w(TAG, "App not installed from Play Store - in-app updates not available");
            if (updateListener != null) {
                updateListener.onUpdateFailed("In-app updates only work for apps installed from Google Play Store");
            }
            return;
        }
        
        // First check if Google Play Services is available
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
        
        if (resultCode != ConnectionResult.SUCCESS) {
            String errorMsg = "Google Play Services not available: " + apiAvailability.getErrorString(resultCode);
            Log.e(TAG, errorMsg);
            Log.e(TAG, "Result code: " + resultCode);
            
            if (updateListener != null) {
                updateListener.onUpdateFailed(errorMsg);
            }
            return;
        }
        
        Log.d(TAG, "Google Play Services is available, proceeding with update check");
        
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            currentUpdateInfo = appUpdateInfo;
            
            Log.d(TAG, "Update availability: " + appUpdateInfo.updateAvailability());
            Log.d(TAG, "Available version code: " + appUpdateInfo.availableVersionCode());
            Log.d(TAG, "Current version code: " + BuildConfig.VERSION_CODE);
            Log.d(TAG, "Package name: " + activity.getPackageName());
            
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                Log.d(TAG, "Update is available in Play Store");
                handleUpdateAvailable(appUpdateInfo);
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_NOT_AVAILABLE) {
                Log.d(TAG, "No updates available in Play Store");
            } else {
                Log.d(TAG, "Update availability unknown: " + appUpdateInfo.updateAvailability());
            }
        });
        
        appUpdateInfoTask.addOnFailureListener(e -> {
            Log.e(TAG, "Failed to check for Play Store updates", e);
            Log.e(TAG, "Exception class: " + e.getClass().getSimpleName());
            Log.e(TAG, "Exception message: " + e.getMessage());
            
            if (updateListener != null) {
                updateListener.onUpdateFailed("Unable to check for updates. Please ensure you're signed into Google Play Store.");
            }
        });
    }
    
    /**
     * Handle when an update is available in Play Store
     */
    private void handleUpdateAvailable(AppUpdateInfo appUpdateInfo) {
        int currentVersion = BuildConfig.VERSION_CODE;
        long minSupportedVersion = remoteConfigManager.getMinSupportedVersion();
        boolean forceUpdateEnabled = remoteConfigManager.isForceUpdateEnabled();
        
        // Determine update type based on remote config settings
        if (currentVersion < minSupportedVersion && forceUpdateEnabled) {
            // Force immediate update
            Log.d(TAG, "Starting immediate update (forced)");
            requestImmediateUpdate(appUpdateInfo);
        } else {
            // Optional flexible update
            Log.d(TAG, "Starting flexible update (optional)");
            requestFlexibleUpdate(appUpdateInfo);
        }
    }
    
    /**
     * Start immediate update (blocks app usage)
     */
    public void startImmediateUpdate() {
        if (currentUpdateInfo != null) {
            requestImmediateUpdate(currentUpdateInfo);
        } else {
            // If we don't have update info, check first
            checkPlayStoreUpdate();
        }
    }
    
    /**
     * Request immediate update
     */
    private void requestImmediateUpdate(AppUpdateInfo appUpdateInfo) {
        try {
            if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                Log.d(TAG, "Requesting immediate update");
                isUpdateInProgress = true;
                
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    activity,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build(),
                    REQUEST_CODE_UPDATE
                );
            } else {
                Log.w(TAG, "Immediate update not allowed, trying flexible update");
                requestFlexibleUpdate(appUpdateInfo);
            }
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Failed to start immediate update", e);
            if (updateListener != null) {
                updateListener.onUpdateFailed("Failed to start update: " + e.getMessage());
            }
        }
    }
    
    /**
     * Request flexible update (app can continue running)
     */
    private void requestFlexibleUpdate(AppUpdateInfo appUpdateInfo) {
        try {
            if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                Log.d(TAG, "Requesting flexible update");
                isUpdateInProgress = true;
                
                // Register listener for flexible update
                appUpdateManager.registerListener(installStateUpdatedListener);
                
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    activity,
                    AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build(),
                    REQUEST_CODE_UPDATE
                );
            } else {
                Log.w(TAG, "Flexible update not allowed");
                if (updateListener != null) {
                    updateListener.onUpdateFailed("Update not available");
                }
            }
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Failed to start flexible update", e);
            if (updateListener != null) {
                updateListener.onUpdateFailed("Failed to start update: " + e.getMessage());
            }
        }
    }
    
    /**
     * Listener for flexible update install state
     */
    private final InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(InstallState state) {
            Log.d(TAG, "Install state: " + state.installStatus());
            
            switch (state.installStatus()) {
                case InstallStatus.DOWNLOADING:
                    Log.d(TAG, "Update downloading...");
                    if (updateListener != null) {
                        updateListener.onUpdateDownloadStarted();
                    }
                    break;
                    
                case InstallStatus.DOWNLOADED:
                    Log.d(TAG, "Update downloaded, ready to install");
                    if (updateListener != null) {
                        updateListener.onUpdateDownloadCompleted();
                    }
                    // Optionally show a notification to complete the update
                    showUpdateCompleteNotification();
                    break;
                    
                case InstallStatus.INSTALLED:
                    Log.d(TAG, "Update installed successfully");
                    appUpdateManager.unregisterListener(installStateUpdatedListener);
                    isUpdateInProgress = false;
                    break;
                    
                case InstallStatus.FAILED:
                    Log.e(TAG, "Update installation failed");
                    if (updateListener != null) {
                        updateListener.onUpdateFailed("Installation failed");
                    }
                    appUpdateManager.unregisterListener(installStateUpdatedListener);
                    isUpdateInProgress = false;
                    break;
                    
                case InstallStatus.CANCELED:
                    Log.d(TAG, "Update cancelled by user");
                    if (updateListener != null) {
                        updateListener.onUpdateCancelled();
                    }
                    appUpdateManager.unregisterListener(installStateUpdatedListener);
                    isUpdateInProgress = false;
                    break;
            }
        }
    };
    
    /**
     * Show notification or dialog to complete flexible update
     */
    private void showUpdateCompleteNotification() {
        // You can customize this based on your UI requirements
        Log.d(TAG, "Update ready to install - showing completion dialog");
        // Call completeUpdate() when user is ready
    }
    
    /**
     * Complete the flexible update (restart the app)
     */
    public void completeUpdate() {
        Log.d(TAG, "Completing flexible update");
        appUpdateManager.completeUpdate();
    }
    
    /**
     * Handle activity result from update flow
     */
    public void onActivityResult(int requestCode, int resultCode) {
        if (requestCode == REQUEST_CODE_UPDATE) {
            isUpdateInProgress = false;
            
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "Update flow completed successfully");
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d(TAG, "Update flow cancelled by user");
                if (updateListener != null) {
                    updateListener.onUpdateCancelled();
                }
                
                // Check if this was a forced update
                int currentVersion = BuildConfig.VERSION_CODE;
                long minSupportedVersion = remoteConfigManager.getMinSupportedVersion();
                boolean forceUpdateEnabled = remoteConfigManager.isForceUpdateEnabled();
                
                if (currentVersion < minSupportedVersion && forceUpdateEnabled) {
                    // Force update was cancelled - exit app or show dialog
                    Log.w(TAG, "Forced update cancelled - app may need to exit");
                }
            } else {
                Log.e(TAG, "Update flow failed with result code: " + resultCode);
                if (updateListener != null) {
                    updateListener.onUpdateFailed("Update failed");
                }
            }
        }
    }
    
    /**
     * Check if app was updated while in background (for immediate updates)
     */
    public void checkForResumableUpdate() {
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                // An immediate update was started but not completed
                Log.d(TAG, "Resuming interrupted immediate update");
                try {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        activity,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build(),
                        REQUEST_CODE_UPDATE
                    );
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Failed to resume update", e);
                }
            }
        });
    }
    
    /**
     * Cleanup method - call in activity onDestroy
     */
    public void cleanup() {
        if (appUpdateManager != null) {
            appUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }
    
    /**
     * Utility methods
     */
    public boolean isUpdateInProgress() {
        return isUpdateInProgress;
    }
    
    public String getUpdateMessage() {
        return remoteConfigManager.getUpdateMessage();
    }
    
    public boolean isForceUpdateRequired() {
        int currentVersion = BuildConfig.VERSION_CODE;
        long minSupportedVersion = remoteConfigManager.getMinSupportedVersion();
        boolean forceUpdateEnabled = remoteConfigManager.isForceUpdateEnabled();
        
        return currentVersion < minSupportedVersion && forceUpdateEnabled;
    }
    
    /**
     * Check if app was installed from Google Play Store
     */
    private boolean isInstalledFromPlayStore() {
        try {
            String installerPackageName = activity.getPackageManager()
                    .getInstallerPackageName(activity.getPackageName());
            
            Log.d(TAG, "App installer package: " + installerPackageName);
            
            // Check if installed from Play Store
            boolean isFromPlayStore = "com.android.vending".equals(installerPackageName);
            
            if (!isFromPlayStore) {
                Log.w(TAG, "App not installed from Play Store. Installer: " + installerPackageName);
                Log.w(TAG, "In-app updates require installation from Google Play Store");
            }
            
            return isFromPlayStore;
            
        } catch (Exception e) {
            Log.e(TAG, "Error checking installer package", e);
            return false;
        }
    }
} 