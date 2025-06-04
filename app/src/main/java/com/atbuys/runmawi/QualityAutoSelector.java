package com.atbuys.runmawi;

import android.util.Log;

public class QualityAutoSelector {
    
    /**
     * Android-only quality auto-selection utility
     * Determines if we should show quality selection or auto-select best quality
     */
    public static QualityChoice getAutoQualityChoice(videodetail video) {
        try {
            String price_480p = video.getPpv_price_480p();
            String price_720p = video.getPpv_price_720p();
            String price_1080p = video.getPpv_price_1080p();
            
            // Handle null or empty prices
            if (price_480p == null) price_480p = "0";
            if (price_720p == null) price_720p = "0";
            if (price_1080p == null) price_1080p = "0";
            
            Log.w("QualityAutoSelector", "Prices - 480p: " + price_480p + ", 720p: " + price_720p + ", 1080p: " + price_1080p);
            
            // Check if all non-zero prices are the same
            boolean allSamePrice = price_480p.equals(price_720p) && price_720p.equals(price_1080p);
            
            if (allSamePrice && !price_480p.equals("0")) {
                // All prices are same and not zero - auto-select highest quality
                Log.w("QualityAutoSelector", "All prices same (" + price_480p + ") - auto-selecting 1080p");
                return new QualityChoice(
                    "High Quality (1080p)", 
                    price_1080p, 
                    "1080p", 
                    true  // auto-selected
                );
            } else {
                // Different prices or all zero - show selection UI
                Log.w("QualityAutoSelector", "Different prices detected - showing selection UI");
                return new QualityChoice(null, null, null, false);
            }
            
        } catch (Exception e) {
            Log.e("QualityAutoSelector", "Error in quality selection: " + e.getMessage());
            // Fallback to showing selection UI
            return new QualityChoice(null, null, null, false);
        }
    }
    
    /**
     * Android-only season quality auto-selection
     */
    public static QualityChoice getAutoQualityChoiceForSeason(season seasonData) {
        try {
            String price_480p = seasonData.getPpv_price_480p();
            String price_720p = seasonData.getPpv_price_720p();
            String price_1080p = seasonData.getPpv_price_1080p();
            
            // Handle null or empty prices
            if (price_480p == null) price_480p = "0";
            if (price_720p == null) price_720p = "0";
            if (price_1080p == null) price_1080p = "0";
            
            Log.w("QualityAutoSelector", "Season Prices - 480p: " + price_480p + ", 720p: " + price_720p + ", 1080p: " + price_1080p);
            
            // Check if all non-zero prices are the same
            boolean allSamePrice = price_480p.equals(price_720p) && price_720p.equals(price_1080p);
            
            if (allSamePrice && !price_480p.equals("0")) {
                // All prices are same and not zero - auto-select highest quality
                Log.w("QualityAutoSelector", "Season: All prices same (" + price_480p + ") - auto-selecting 1080p");
                return new QualityChoice(
                    "High Quality (1080p)", 
                    price_1080p, 
                    "1080p", 
                    true  // auto-selected
                );
            } else {
                // Different prices or all zero - show selection UI
                Log.w("QualityAutoSelector", "Season: Different prices detected - showing selection UI");
                return new QualityChoice(null, null, null, false);
            }
            
        } catch (Exception e) {
            Log.e("QualityAutoSelector", "Error in season quality selection: " + e.getMessage());
            // Fallback to showing selection UI
            return new QualityChoice(null, null, null, false);
        }
    }
    
    public static class QualityChoice {
        public String name;
        public String price;
        public String resolution;
        public boolean isAutoSelected;
        
        public QualityChoice(String name, String price, String resolution, boolean isAutoSelected) {
            this.name = name;
            this.price = price;
            this.resolution = resolution;
            this.isAutoSelected = isAutoSelected;
        }
    }
} 