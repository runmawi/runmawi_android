package com.atbuys.runmawi.Api1;

/*

import com.example.bop.Model.ChannelBodyResponse;
import com.example.bop.Model.HomeBodyResponse;
import com.example.bop.Model.PPvBodyResponse;
*/

import com.atbuys.runmawi.Model.HomeBodyResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

//import Model.SocailLoginUser;


public interface RetrofitApiClient {


    @GET("categoryvideos")
    Call<HomeBodyResponse> getMovieByCategory();


    @FormUrlEncoded
    @POST("relatedchannelvideos")
    Call<HomeBodyResponse> getRecommaned(@Field("videoid") String user_id);

    @FormUrlEncoded
    @POST("All_Homepage")
    Call<HomeBodyResponse> getHomeCategory(@Field("user_id") String user_id,@Field("page") int page);

    @GET("menus")
    Call<HomeBodyResponse> getMenu1();

    @GET("learn")
    Call<HomeBodyResponse> getLearn();


    @GET("library")
    Call<HomeBodyResponse> getLibrary();

    @GET("audiocategory")
    Call<HomeBodyResponse> getAudioByCategory();

    @GET("sliders")
    Call<HomeBodyResponse> getBanners();

    @GET("livestreams")
    Call<HomeBodyResponse> getLivestreams();


    @FormUrlEncoded
    @POST("SeasonsEpisodes")
    Call<HomeBodyResponse> getSeasonEpisodes(@Field("seriesid") String seriesid);

}
