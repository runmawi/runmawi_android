package com.atbuys.runmawi;

public class related_episode {
    private String id;
    private String title;
    private String mobile_image;
    private String ppv_status;
    private String mp4_url,series_id,season_id;
    private String duration;
    private String image_url;
    private String image,rating;

    public String getRating() {
        return rating;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
    public String getDuration()
    {
        return duration;
    }

    public String getMobile_image() {
        return mobile_image;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getSeries_id() {
        return series_id;
    }

    public String getSeason_id() {
        return season_id;
    }

    public String getPpv_status()
    {
        return ppv_status;
    }

    public String getMp4_url()
    {
        return mp4_url;
    }


    public String getImage()
    {
        return image;
    }
}
