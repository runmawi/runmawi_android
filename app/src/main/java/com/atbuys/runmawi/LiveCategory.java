package com.atbuys.runmawi;

public class LiveCategory {
    private String id;
    private String name;
    private String image;
    private String access;
    private String mp4_url;
    private String livestream_url;
    private String livestream_format;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getAccess()
    {
        return access;
    }

    public String getHls_url()
    {
        return mp4_url;
    }

    public String getLivestream_url()
    {
        return livestream_url;
    }

    public String getLivestream_format() {
        return livestream_format;
    }
}
