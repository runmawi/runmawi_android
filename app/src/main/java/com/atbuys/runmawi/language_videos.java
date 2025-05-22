package com.atbuys.runmawi;

class language_videos {

    private String name, url, id, ppvstatus, expire, image_url, trailer, path, video_url,m3u8_url;
    private String image, type, access,mp4_url;

    public language_videos() {
    }

    public language_videos(String name, String image, String url, String id, String ppvstatus, String expire, String image_url, String trailer, String path, String video_url, String type, String access , String mp4_url,String m3u8_url) {
        this.name = name;
        this.image = image;
        this.url = url;
        this.id = id;
        this.ppvstatus = ppvstatus;
        this.expire = expire;
        this.image_url = image_url;
        this.trailer = trailer;
        this.path = path;
        this.video_url = video_url;
        this.type = type;
        this.access = access;
        this.mp4_url =mp4_url;
        this.m3u8_url=m3u8_url;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPpvstatus() {
        return ppvstatus;
    }

    public void setPpvstatus(String ppvstatus) {
        this.ppvstatus = ppvstatus;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String value) {
        this.image_url = image_url;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccess()
    {
        return access;
    }

    public void setAccess(String access)
    {
        this.access =access;
    }

    public String getMp4_url()
    {
        return mp4_url;
    }

    public void setMp4_url(String mp4_url)
    {
        this.mp4_url = mp4_url;
    }

    public String getM3u8_url()
    {
        return m3u8_url;
    }

}






