package com.atbuys.runmawi;

public class quality_free {

    private String quality,resolution,price;

   /* public quality_free(String quality, String r) {
        this.quality = quality;
        this.resolution = r;
    }*/
    public quality_free(String quality,String price,String r) {
        this.quality = quality;
        this.price = price;
        this.resolution = r;
    }

    public String getPrice() {
        return price;
    }

    public String getQuality() {
        return quality;
    }

    public String getResolution() {
        return resolution;
    }



}
