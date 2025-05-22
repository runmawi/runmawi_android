package com.atbuys.runmawi;

import java.util.HashMap;
import java.util.Map;

public class settinghome {


    private String latest_videos;
    private String category_videos;
    private String live_videos;
    private String series;
    private String featured_videos;
    private String audios;
    private String albums;


    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getLatest_videos() {
        return latest_videos;
    }

    public void setLatest_videos(String latest_videos) {
        this.latest_videos = latest_videos;
    }


    public String getFeatured_videos()
    {
        return featured_videos;
    }

    public void setFeatured_videos(String featured_videos)
    {
        this.featured_videos = featured_videos;
    }

    public String getCategory_videos()
    {

        return category_videos;
    }

    public void setCategory_videos(String category_videos)
    {
        this.category_videos =category_videos;
    }

    public String getLive_videos() {
        return live_videos;
    }

    public void setLive_videos(String live_videos) {
        this.live_videos = live_videos;
    }



    public String getSeries()
    {

        return series;
    }

    public void setSeries(String series)
    {
        this.series =series;
    }

    public String getAudios() {
        return audios;
    }

    public void setAudios(String audios) {
        this.audios = audios;
    }



    public String getAlbums()
    {

        return albums;
    }

    public void setAlbums(String albums)
    {
        this.albums =albums;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
