package com.atbuys.runmawi;

public class Constants {
    // Force development mode for testing - change to false to use production URLs
    private static final boolean FORCE_DEV_MODE = true; // Set to false for production
    
    // Set to true when building debug version or if FORCE_DEV_MODE is true
    public static final boolean IS_DEBUG_MODE = FORCE_DEV_MODE || com.atbuys.runmawi.BuildConfig.DEBUG;

    // For emulator, use 10.0.2.2; for physical device, use your machine's IP address
    // Change this to match your setup - use "10.0.2.2" for emulator, or your IP for physical device
    private static final String LOCAL_HOST_ADDRESS = "https:"; // Use "10.0.2.2" for emulator
    
    // Production URLs
    private static final String PROD_BASE_URL = "https://a5d2abf69a69.ngrok-free.app/api/auth/";
    private static final String PROD_IMAGE_BASE_URL = "https://a5d2abf69a69.ngrok-free.app/public/uploads/";
    
    // Development URLs (replace with your machine's IP address or 10.0.2.2 for emulator)
    private static final String DEV_BASE_URL = "http://" + LOCAL_HOST_ADDRESS + "/api/auth/";
    private static final String DEV_IMAGE_BASE_URL = "http://" + LOCAL_HOST_ADDRESS + "/public/uploads/";
    
    // Current URLs based on build type
    public static final String BASE_URL = IS_DEBUG_MODE ? DEV_BASE_URL : PROD_BASE_URL;
    public static final String IMAGE_BASE_URL = IS_DEBUG_MODE ? DEV_IMAGE_BASE_URL : PROD_IMAGE_BASE_URL;
    
    // API endpoints
    public static final String UPDATE_PROFILE_URL = IS_DEBUG_MODE ? 
        "http://" + LOCAL_HOST_ADDRESS + "/api/auth/updateProfile" : 
        "https://a5d2abf69a69.ngrok-free.app/api/auth/updateProfile";
}