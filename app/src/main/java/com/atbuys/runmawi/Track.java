package com.atbuys.runmawi;

public class Track {

    private String title;
    private String artist;
    private String image;
    private String url;
    private String id;

    public Track(String title, String artist, String image , String url, String id) {
        this.title = title;
        this.artist = artist;
        this.image = image;
        this.url =url;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id=id;
    }

}
