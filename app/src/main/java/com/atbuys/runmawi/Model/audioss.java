package com.atbuys.runmawi.Model;


public class audioss {

    private String title, audio_id, ppv_status, image_url;
    private String image, mobile_image, trailer, video_url, mp3_url, path,access;


    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    public String getImage() {
        return image;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMobile_image() {
        return mobile_image;
    }

    public void setMobile_image(String mobile_image) {
        this.mobile_image = mobile_image;
    }

    public String getId() {
        return audio_id;
    }

    public String getTrailer() {
        return trailer;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getMp4_url() {
        return mp3_url;
    }

    public void setMp4_url(String mp4_url) {
        this.mp3_url = mp4_url;
    }

    public String getPpv_status() {
        return ppv_status;
    }

    public void setPpv_status(String ppv_status) {
        this.ppv_status = ppv_status;
    }

    public void setId(String id) {
        this.audio_id = id;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }


    public String getAccess()
    {
        return access;
    }

    public void setAccess(String access)
    {
        this.access = access;
    }

}




