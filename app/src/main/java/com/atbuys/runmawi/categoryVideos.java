package com.atbuys.runmawi;

class categoryVideos {

    private String name, url, video_id, ppvstatus, expire, image_url, trailer, path, video_url;
    private String image,type,access,mp4_url,m3u8_url,rating,id;

    public categoryVideos() {
    }

    public categoryVideos(String name, String image, String url, String video_id, String ppvstatus, String expire, String image_url, String trailer, String path, String video_url, String type, String mp4_url, String access, String m3u8_url) {
        this.name = name;
        this.image = image;
        this.url = url;
        this.video_id = video_id;
        this.ppvstatus = ppvstatus;
        this.expire = expire;
        this.image_url = image_url;
        this.trailer = trailer;
        this.path = path;
        this.video_url = video_url;
        this.type = type;
        this.mp4_url = mp4_url;
        this.access = access;
        this.m3u8_url = m3u8_url;
    }

    public String getVideo_id() {
        return id;
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
        return video_id;
    }

    public void setId(String id) {
        this.video_id = id;
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

    public String getRating() {
        return rating;
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

    public String getVideo_url()
    {
        return  video_url;
    }

    public void  setVideo_url(String video_url)
    {
        this.video_url = video_url;
    }

    public String getType()
    {
        return type;
    }

    public  void setType(String type)
    {
       this.type = type;
    }

    public String getMp4_url()
    {
        return  mp4_url;
    }

    public void  setMp4_url(String mp4_url)
    {
        this.mp4_url = mp4_url;
    }

    public String getAccess()
    {
        return access;
    }

    public  void setAccess(String access)
    {
        this.access = access;
    }

    public String getM3u8_url()
    {
        return m3u8_url;
    }


}
