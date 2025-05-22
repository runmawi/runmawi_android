package com.atbuys.runmawi.Model;


import com.atbuys.runmawi.lists;
import com.atbuys.runmawi.videos;

public class HomeBodyResponse {

    private String status;
    private HomeData[] genre_movies;
    private EpisodeHomeData[] SeasonsEpisodes;
    private LibraryData[] video_andriod;
    private HomeData1[] live_streams;
    private LearnData[] series;

    private HomeCategoryData[] Home_page;
    private data[] data;
    private videos[] videos;

    private RecommandedHomeData[] channelrecomended;

    //private HomeCategoryData1[] data;

    private lists lists;

    public lists getLists() {
        return lists;
    }
    public LibraryData[] getVideos() {
        return video_andriod;
    }

    public String getStatus() { return status; }

    public void setStatus(String value) { this.status = value; }

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

    public RecommandedHomeData[] getChannelrecomended() {
        return channelrecomended;
    }

    public data[] getData() {
        return data;
    }

    public HomeCategoryData[] getHome_page()
    {
        return Home_page;
    }

    /*public HomeCategoryData1[] getData()
    {
        return data;
    }*/

    public LearnData[] getEpisode_videos() {
        return series;
    }
    public void setEpisode_videos(LearnData[] episode_videos) {
        series = episode_videos;
    }


    /*public videos[] getVideos() {
        return videos;
    }*/

    public void setVideos(videos[] videos) {
        this.videos = videos;
    }
}
