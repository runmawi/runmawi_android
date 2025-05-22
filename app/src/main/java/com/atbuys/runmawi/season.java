package com.atbuys.runmawi;

public class season {
    private String id,access;
    private String title;
    private String mobile_image;
    private String ppv_status,ppv_price;
    private String mp4_url,ppv_price_480p,ppv_price_720p,ppv_price_1080p;
    private String image_url,trailer,image;

    public String getTrailer() {
        return trailer;
    }

    public String getImage() {
        return image;
    }

    public String getId() {
        return id;
    }

    public String getPpv_price() {
        return ppv_price;
    }

    public String getTitle() {
        return title;
    }

    public String getMobile_image() {
        return mobile_image;
    }

    public String getImage_url() {
        return image_url;
    }


    public String getPpv_status()
    {
        return ppv_status;
    }

    public String getMp4_url()
    {
        return mp4_url;
    }

    public String getPpv_price_480p() {
        return ppv_price_480p;
    }

    public String getPpv_price_720p() {
        return ppv_price_720p;
    }

    public String getPpv_price_1080p() {
        return ppv_price_1080p;
    }

    public String getAccess() {
        return access;
    }
}
