package com.atbuys.runmawi.Model;

import com.atbuys.runmawi.live_banner;
import com.atbuys.runmawi.sliders;

public class HomeCategoryData1 {


    private String gener_name,gener_id,message,header_name,source;
    private Movie[] movies;
    private videoss[] videos;
    private audioss[] audio;
    private sliders[] sliders;
    private live_banner[] live_banner;
    private data[] data;


    public String getGenreName() { return gener_name; }

    public void setGenreName(String value) { this.gener_name = value; }

    public String getGenre_id()
    {
        return gener_id;
    }

    public void setGenre_id(String genre_id) {
        this.gener_id = genre_id;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message=message;
    }

    public Movie[] getMovies() { return movies; }

    public void setMovies(Movie[] value) { this.movies = value; }

    public videoss[] getVideos()
    {
        return videos;
    }

    public void setVideos(videoss[] value1)
    {
        this.videos=value1;
    }

    public audioss[] getAudios()
    {
        return audio;
    }

    public void setAudios(audioss[] value1)
    {
        this.audio=value1;
    }

    public live_banner[] getLive_banner()
    {
        return live_banner;
    }

    public void setLive_banner(live_banner[] value)
    {
        this.live_banner = value;
    }

    public data[] getData()
    {
        return data;
    }

    public String getHeader_name() {
        return header_name;
    }

    public String getSource()
    {
        return source;
    }
}
