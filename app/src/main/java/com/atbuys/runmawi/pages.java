package com.atbuys.runmawi;

public class pages {
    private String id;
    private String title, body, slug;

    private String page_url;

    private String genre_name;
    private boolean visible;

    public pages(String genre_name, boolean visible) {
        this.genre_name = genre_name;
        this.visible = visible;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSlug() {
        return slug;
    }

    public String getPage_url() {
        return page_url;
    }

    public String getBody() {
        return body;
    }

    public String getGenre_name() {
        return genre_name;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }
}
