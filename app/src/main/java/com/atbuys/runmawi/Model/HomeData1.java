package com.atbuys.runmawi.Model;


public class HomeData1 {


    private String genre_name,genre_id,message;
    private Movie[] movies;
    private videoss[] videos;


    public String getGenreName() { return genre_name; }

    public void setGenreName(String value) { this.genre_name = value; }

    public String getGenre_id()
    {
        return genre_id;
    }

    public void setGenre_id(String genre_id) {
        this.genre_id = genre_id;
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
}
