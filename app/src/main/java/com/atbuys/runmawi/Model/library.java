package com.atbuys.runmawi.Model;


public class library {

    private String title, id;
    private String image,  mp4_url,trailer,season_id;


    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getMp4_url() {
        return mp4_url;
    }

    public void setMp4_url(String mp4_url) {
        this.mp4_url = mp4_url;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getTrailer()
    {
        return trailer;
    }

    public void setTrailer(String trailer)
    {
        this.trailer = trailer;
    }

    public String getSeason_id()
    {
        return season_id;
    }

    public void setSeason_id(String season_id)
    {
        this.season_id = season_id;
    }

}




