package com.atbuys.runmawi;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.HashMap;
import java.util.Map;

public class RemoteConfigManager {
    private static final String TAG = "RemoteConfigManager";
    private static RemoteConfigManager instance;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    
    // Remote Config Keys
    public static final String MIN_SUPPORTED_VERSION = "min_supported_version";
    public static final String FORCE_UPDATE_ENABLED = "force_update_enabled";
    public static final String UPDATE_MESSAGE = "update_message";
    public static final String MAINTENANCE_MODE = "maintenance_mode";
    public static final String MAINTENANCE_MESSAGE = "maintenance_message";
    public static final String API_BASE_URL = "api_base_url";
    public static final String ENABLE_GOOGLE_LOGIN = "enable_google_login";
    public static final String ENABLE_FACEBOOK_LOGIN = "enable_facebook_login";
    public static final String DEFAULT_VIDEO_QUALITY = "default_video_quality";
    public static final String PPV_ENABLED = "ppv_enabled";
    public static final String SOCIAL_LOGIN_ENABLED = "social_login_enabled";
    public static final String OTP_LOGIN_ENABLED = "otp_login_enabled";
    public static final String STREAMING_QUALITY_OPTIONS = "streaming_quality_options";
    public static final String ENABLE_OFFLINE_DOWNLOAD = "enable_offline_download";
    public static final String MAX_CONCURRENT_DOWNLOADS = "max_concurrent_downloads";
    public static final String SHOW_ADS = "show_ads";
    public static final String AD_FREQUENCY = "ad_frequency";
    
    private RemoteConfigManager() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        setupRemoteConfig();
    }
    
    public static synchronized RemoteConfigManager getInstance() {
        if (instance == null) {
            instance = new RemoteConfigManager();
        }
        return instance;
    }
    
    private void setupRemoteConfig() {
        // Configure Remote Config settings
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(BuildConfig.DEBUG ? 0 : 3600) // 0 for debug, 1 hour for release
                .build();
        
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        
        // Set default values
        Map<String, Object> defaults = getDefaultValues();
        mFirebaseRemoteConfig.setDefaultsAsync(defaults);
    }
    
    private Map<String, Object> getDefaultValues() {
        Map<String, Object> defaults = new HashMap<>();
        
        // App Version Control
        defaults.put(MIN_SUPPORTED_VERSION, BuildConfig.VERSION_CODE);
        defaults.put(FORCE_UPDATE_ENABLED, false);
        defaults.put(UPDATE_MESSAGE, "Please update the app to continue using the latest features.");
        
        // Maintenance Mode
        defaults.put(MAINTENANCE_MODE, false);
        defaults.put(MAINTENANCE_MESSAGE, "The app is currently under maintenance. Please try again later.");
        
        // API Configuration
        defaults.put(API_BASE_URL, "https://runmawi.com/api/");
        
        // Login Options
        defaults.put(ENABLE_GOOGLE_LOGIN, true);
        defaults.put(ENABLE_FACEBOOK_LOGIN, true);
        defaults.put(SOCIAL_LOGIN_ENABLED, true);
        defaults.put(OTP_LOGIN_ENABLED, true);
        
        // Video Settings
        defaults.put(DEFAULT_VIDEO_QUALITY, "720p");
        defaults.put(STREAMING_QUALITY_OPTIONS, "[\"480p\", \"720p\", \"1080p\"]");
        defaults.put(PPV_ENABLED, true);
        
        // Download Settings
        defaults.put(ENABLE_OFFLINE_DOWNLOAD, true);
        defaults.put(MAX_CONCURRENT_DOWNLOADS, 3);
        
        // Ad Settings
        defaults.put(SHOW_ADS, true);
        defaults.put(AD_FREQUENCY, 300); // seconds
        
        return defaults;
    }
    
    public void fetchAndActivate(OnCompleteListener<Boolean> listener) {
        Log.d(TAG, "Fetching remote config...");
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean updated = task.getResult();
                        Log.d(TAG, "Config params updated: " + updated);
                        Log.d(TAG, "Remote config fetched successfully");
                    } else {
                        Log.e(TAG, "Remote config fetch failed", task.getException());
                    }
                    
                    if (listener != null) {
                        listener.onComplete(task);
                    }
                });
    }
    
    // Version Control Methods
    public long getMinSupportedVersion() {
        return mFirebaseRemoteConfig.getLong(MIN_SUPPORTED_VERSION);
    }
    
    public boolean isForceUpdateEnabled() {
        return mFirebaseRemoteConfig.getBoolean(FORCE_UPDATE_ENABLED);
    }
    
    public String getUpdateMessage() {
        return mFirebaseRemoteConfig.getString(UPDATE_MESSAGE);
    }
    
    // Maintenance Mode Methods
    public boolean isMaintenanceMode() {
        return mFirebaseRemoteConfig.getBoolean(MAINTENANCE_MODE);
    }
    
    public String getMaintenanceMessage() {
        return mFirebaseRemoteConfig.getString(MAINTENANCE_MESSAGE);
    }
    
    // API Configuration Methods
    public String getApiBaseUrl() {
        return mFirebaseRemoteConfig.getString(API_BASE_URL);
    }
    
    // Login Configuration Methods
    public boolean isGoogleLoginEnabled() {
        return mFirebaseRemoteConfig.getBoolean(ENABLE_GOOGLE_LOGIN);
    }
    
    public boolean isFacebookLoginEnabled() {
        return mFirebaseRemoteConfig.getBoolean(ENABLE_FACEBOOK_LOGIN);
    }
    
    public boolean isSocialLoginEnabled() {
        return mFirebaseRemoteConfig.getBoolean(SOCIAL_LOGIN_ENABLED);
    }
    
    public boolean isOtpLoginEnabled() {
        return mFirebaseRemoteConfig.getBoolean(OTP_LOGIN_ENABLED);
    }
    
    // Video Configuration Methods
    public String getDefaultVideoQuality() {
        return mFirebaseRemoteConfig.getString(DEFAULT_VIDEO_QUALITY);
    }
    
    public String getStreamingQualityOptions() {
        return mFirebaseRemoteConfig.getString(STREAMING_QUALITY_OPTIONS);
    }
    
    public boolean isPpvEnabled() {
        return mFirebaseRemoteConfig.getBoolean(PPV_ENABLED);
    }
    
    // Download Configuration Methods
    public boolean isOfflineDownloadEnabled() {
        return mFirebaseRemoteConfig.getBoolean(ENABLE_OFFLINE_DOWNLOAD);
    }
    
    public long getMaxConcurrentDownloads() {
        return mFirebaseRemoteConfig.getLong(MAX_CONCURRENT_DOWNLOADS);
    }
    
    // Ad Configuration Methods
    public boolean shouldShowAds() {
        return mFirebaseRemoteConfig.getBoolean(SHOW_ADS);
    }
    
    public long getAdFrequency() {
        return mFirebaseRemoteConfig.getLong(AD_FREQUENCY);
    }
    
    // Utility Methods
    public boolean isAppVersionSupported(int currentVersionCode) {
        return currentVersionCode >= getMinSupportedVersion();
    }
    
    public void logAllValues() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "=== Remote Config Values ===");
            Log.d(TAG, "Min Supported Version: " + getMinSupportedVersion());
            Log.d(TAG, "Force Update Enabled: " + isForceUpdateEnabled());
            Log.d(TAG, "Maintenance Mode: " + isMaintenanceMode());
            Log.d(TAG, "API Base URL: " + getApiBaseUrl());
            Log.d(TAG, "Google Login Enabled: " + isGoogleLoginEnabled());
            Log.d(TAG, "Facebook Login Enabled: " + isFacebookLoginEnabled());
            Log.d(TAG, "PPV Enabled: " + isPpvEnabled());
            Log.d(TAG, "Default Video Quality: " + getDefaultVideoQuality());
            Log.d(TAG, "Show Ads: " + shouldShowAds());
            Log.d(TAG, "============================");
        }
    }
} 