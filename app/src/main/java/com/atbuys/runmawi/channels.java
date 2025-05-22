package com.atbuys.runmawi;

class channels {

    private String channel_name, email, id;
    private String password,channel_banner,channel_slug,channel_image;

    public channels() {
    }

    public channels(String channel_name, String email, String id, String password, String channel_banner, String channel_slug, String channel_image) {
        this.channel_name = channel_name;
        this.email = email;
        this.id = id;
        this.password = password;
        this.channel_banner = channel_banner;
        this.channel_slug = channel_slug;
        this.channel_name = channel_name;

    }


    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getChannel_banner() {
        return channel_banner;
    }

    public void setChannel_banner(String channel_banner) {
        this.channel_banner = channel_banner;
    }

    public String getChannel_slug()
    {
        return channel_slug;
    }

    public void setChannel_slug(String channel_slug)
    {
        this.channel_slug = channel_slug;
    }

    public String getChannel_image()
    {
        return channel_image;
    }

    public void setChannel_image(String channel_image)
    {
        this.channel_image = channel_image;
    }

}
