package com.atbuys.runmawi;

import com.atbuys.runmawi.Model.recommandedd;

public class channelrecomended {
    private String id;
    private String title;

    private recommandedd[] recomendeds;


    public recommandedd[] getRecomendeds()
    {
        return recomendeds;
    }
    private String mobile_image;
    private String ppv_status;
    private String mp4_url;
    private String image_url;
    private String type;
    private String video_url;
    private String path;
    private String trailer;
    private String access;
    private String m3u8_url;



    public String getId() {
        return id;
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

    public String getType() {
        return type;
    }

    public String getTrailer() {
        return trailer;
    }


    public String getVideo_url()
    {
        return video_url;
    }

    public String getPath()
    {
        return path;
    }

    public String getAccess()
    {
        return access;
    }


    public String getM3u8_url()
    {
        return m3u8_url;
    }
}
