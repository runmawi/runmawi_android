package com.atbuys.runmawi.Adapter;

import com.google.gson.annotations.SerializedName;

public class SeriesImage {

    public int id;
    public String image;
    public String player_image;
    public String tv_image;
    public String image_url;
    public String banner_image_url;
    @SerializedName("Tv_image_url")
    public String tv_image_url;
    public String source;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPlayer_image() {
        return player_image;
    }

    public void setPlayer_image(String player_image) {
        this.player_image = player_image;
    }

    public String getTv_image() {
        return tv_image;
    }

    public void setTv_image(String tv_image) {
        this.tv_image = tv_image;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getBanner_image_url() {
        return banner_image_url;
    }

    public void setBanner_image_url(String banner_image_url) {
        this.banner_image_url = banner_image_url;
    }

    public String getTv_image_url() {
        return tv_image_url;
    }

    public void setTv_image_url(String tv_image_url) {
        this.tv_image_url = tv_image_url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
