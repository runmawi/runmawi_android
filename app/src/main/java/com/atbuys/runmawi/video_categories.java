package com.atbuys.runmawi;

class video_categories {

    private String name, url, id, ppvstatus, expire, type, trailer,path;
    private String image, image_url,video_url,mp4_url,access,title;

    public video_categories() {
    }

    public video_categories(String name, String image, String url, String id, String ppvstatus, String expire, String image_url, String type, String trailer, String video_url, String path, String mp4_url , String access , String title) {
        this.name = name;
        this.image = image;
        this.url = url;
        this.id = id;
        this.ppvstatus = ppvstatus;
        this.expire = expire;
        this.image_url = image_url;
        this.type = type;
        this.trailer = trailer;
        this.video_url = video_url;
        this.path = path;
        this.mp4_url = mp4_url;
        this.access = access;
        this.title = title;
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

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getType()
    {
        return  type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getTrailer()
    {
        return trailer;
    }

    public void setTrailer(String trailer)
    {
        this.trailer = trailer;
    }

    public String getVideo_url()
    {
        return video_url;
    }

    public void  setVideo_url(String video_url)
    {
        this.video_url = video_url;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getMp4_url()
    {
        return mp4_url;
    }

    public void  setMp4_url(String mp4_url)
    {
        this.mp4_url = mp4_url;
    }

    public String getAccess()
    {
        return access;
    }

    public void setAccess(String access)
    {
        this.access = access;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }


}
