package com.atbuys.runmawi;

class ContentPartner {

    private String username, email, id;
    private String picture,slug,banner;

    public ContentPartner() {
    }

    public ContentPartner(String channel_name, String email, String id, String picture, String slug , String banner) {
        this.username = channel_name;
        this.email = email;
        this.id = id;
        this.picture = picture;
        this.slug = slug;
        this.banner =banner;

    }


    public String getChannel_name() {
        return username;
    }

    public void setChannel_name(String channel_name) {
        this.username = channel_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }


    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }
}
