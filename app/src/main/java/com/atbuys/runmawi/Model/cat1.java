package com.atbuys.runmawi.Model;


public class cat1 {

    private String name;
    private HomeData[] genre_movies;
    private HomeCategoryData[] Home_page;
    private HomeCategoryData1[] data;
    private EpisodeHomeData[] SeasonsEpisodes;
    private HomeData1[] live_streams;


    public String getStatus() { return name; }

    public void setStatus(String value) { this.name = value; }

    public HomeData[] getGenreMovies() { return genre_movies; }

    public void setGenreMovies(HomeData[] value) { this.genre_movies = value; }

    public EpisodeHomeData[] getSeasonsEpisodes()
    {
        return SeasonsEpisodes;
    }


    public void setSeasonsEpisodes(EpisodeHomeData[] vale)
    {
        this.SeasonsEpisodes = vale;
    }

    public HomeData1[] getLive_streams() {
        return live_streams;
    }

    public void setLive_streams(HomeData1[] val)
    {
        this.live_streams = val;
    }

    public HomeCategoryData[] getHome_page()
    {
        return Home_page;
    }

    public HomeCategoryData1[] getData()
    {
        return data;
    }





}
