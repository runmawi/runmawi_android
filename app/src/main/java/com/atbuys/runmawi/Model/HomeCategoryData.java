package com.atbuys.runmawi.Model;




import com.atbuys.runmawi.live_banner;
import com.atbuys.runmawi.sliders;

import java.util.ArrayList;

public class HomeCategoryData {


    private String gener_name,gener_id,message,header_name,name,source,header_name_IOS,source_type;
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

    public String getSource_type() {
        return source_type;
    }

    public String getHeader_name_IOS() {
        return header_name_IOS;
    }

    public void setHeader_name_IOS(String header_name_IOS) {
        this.header_name_IOS = header_name_IOS;
    }

    public void setSource_type(String source_type) {
        this.source_type = source_type;
    }

    /* public void setData(ArrayList<com.atbuys.runmawiv.Model.data> data) {
        this.data = data.toArray(new data[0]);
    }*/

    public void setHeader_name(String header_name) {
        this.header_name = header_name;
    }

    public void setData(ArrayList<data> data) {
        if (data != null) {
            this.data = data.toArray(new data[data.size()]);
        } else {
            this.data = new data[0]; // or handle the null case accordingly
        }
    }


    public void setSource(String source) {
        this.source = source;
    }

    public String getHeader_name() {
        return header_name;
    }

    public String getName() {
        return name;
    }

    public String getSource()
    {
        return source;
    }

}
