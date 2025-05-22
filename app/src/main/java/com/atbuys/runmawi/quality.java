package com.atbuys.runmawi;

public class quality {

    private String quality,price,resolution;

    public quality(String quality,String price,String r) {
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
