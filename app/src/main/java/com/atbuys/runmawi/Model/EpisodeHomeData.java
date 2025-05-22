package com.atbuys.runmawi.Model;


public class EpisodeHomeData {


    private String genre_name, genre_id, message,season_id;
    private String season_name;
    private Movie[] movies;
    private videoss[] videos;
    private episodes[] episodes;


    public String getGenreName() {
        return genre_name;
    }

    public void setGenreName(String value) {
        this.genre_name = value;
    }

    public String getGenre_id() {
        return genre_id;
    }

    public void setGenre_id(String genre_id) {
        this.genre_id = genre_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSeason_name()
    {
        return season_name;
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


    public episodes[] getEpisodes() {
        return episodes;
    }

    public void setEpisodes(episodes[] valuee)
    {
        this.episodes = valuee;
    }

    public String getSeason_id()
    {
        return season_id;
    }
}
