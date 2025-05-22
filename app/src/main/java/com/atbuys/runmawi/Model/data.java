package com.atbuys.runmawi.Model;


import java.util.ArrayList;

public class data {

    private String title,url_type, video_id, ppv_status,description, image_url,id, hls_url,name,source,rating;
    private String image, mobile_image, trailer, video_url, mp4_url, path,access,type,m3u8_url,mp3_url;
    private String channel_image,channel_slug,channel_name,slug;
    public ArrayList<data> category_videos;
    public ArrayList<data> category_livestream;
    private data[] data;
    private String header_name,source_type,header_name_IOS;


    public String getHeader_name() {
        return header_name;
    }

    public void setHeader_name_IOS(String header_name_IOS) {
        this.header_name_IOS = header_name_IOS;
    }

    public void setHeader_name(String header_name) {
        this.header_name = header_name;
    }

    public void setSource_type(String source_type) {
        this.source_type = source_type;
    }

    public String getSource_type() {
        return source_type;
    }

    public String getHeader_name_IOS() {
        return header_name_IOS;
    }

    public void setData(ArrayList<data> data) {
        if (data != null) {
            this.data = data.toArray(new data[data.size()]);
        } else {
            this.data = new data[0]; // or handle the null case accordingly
        }
    }
    public data[] getData()
    {
        return data;
    }

    public String getMp3_url() {
        return mp3_url;
    }

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
        return video_id;
    }

    public String getTrailer() {
        return trailer;
    }

    public String getVideo_url() {
        return video_url;
    }

    public String getDescription() {
        return description;
    }

    public String getSlug() {
        return slug;
    }

    public String getId1()
    {
        return id;

    }

    public String getRating() {
        return rating;
    }

    public void setId1(String id1)
    {
        this.id =id1;
    }

    public String getChannel_image()
    {
        return channel_image;

    }

    public void setChannel_image(String id1)
    {
        this.channel_image =id1;
    }

    public String getChannel_slug()
    {
        return channel_slug;

    }

    public void setChannel_slug(String id1)
    {
        this.channel_slug =id1;
    }

    public String getChannel_name()
    {
        return channel_name;

    }

    public void setChannel_name(String id1)
    {
        this.channel_name=id1;
    }
    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getMp4_url() {
        return mp4_url;
    }

    public void setMp4_url(String mp4_url) {
        this.mp4_url = mp4_url;
    }

    public String getPpv_status() {
        return ppv_status;
    }

    public void setPpv_status(String ppv_status) {
        this.ppv_status = ppv_status;
    }

    public void setId(String id) {
        this.video_id = id;
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

    public String getUrl_type()
    {
        return url_type;
    }

    public void setUrl_type(String url_type)
    {
        this.url_type = url_type;
    }




    public String getHls_url()
    {
        return hls_url;
    }

    public void setHls_url(String hls_url)
    {
        this.hls_url = hls_url;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type =type;
    }

    public String getM3u8_url()
    {
        return m3u8_url;
    }

    public void setM3u8_url(String m3u8_url)
    {
        this.m3u8_url = m3u8_url;
    }

    public ArrayList<data> getCategory_videos() {
        return category_videos;
    }

    public void setCategory_videos(ArrayList<data> category_videos) {
        this.category_videos = category_videos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<data> getCategory_livestream() {
        return category_livestream;
    }

    public void setCategory_livestream(ArrayList<data> category_livestream) {
        this.category_livestream = category_livestream;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}




