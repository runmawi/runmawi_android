package com.atbuys.runmawi;

public class video_cast {

    private String cast_id,cast,artist_name;
    private String image_url;

    public video_cast() {
    }

    public video_cast(String cast_id, String cast, String image_url ) {
            this.cast_id = cast;
            this.image_url = image_url;
            this.cast = cast;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public String getCast_id() {
        return cast_id;
    }

    public void setCast_id(String cast_id) {
        this.cast_id = cast_id;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}




