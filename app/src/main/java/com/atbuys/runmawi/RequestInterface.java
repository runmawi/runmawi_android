package com.atbuys.runmawi;

import com.atbuys.runmawi.Adapter.series_imgae;
import com.atbuys.runmawi.Model.HomeBodyResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RequestInterface {


    @GET("{endpoint}")
    Call<JSONResponse> dynamicGetRequest(@Header("Authorization") String token,
                                         @Path("endpoint") String endpoint);

    @GET("categorylist")
    Call<JSONResponse> getCategories();

    @POST("orders")
    Call<JSONResponse> createOrder(@Header("Authorization") String token,
                                   @Body Order order);
    @FormUrlEncoded
    @POST("payment-transaction-log")
    Call<JSONResponse1> transactioLog(@Field("user_id") String user_id,
                                     @Field("total_amount") String total_amount,
                                     @Field("payment_for") String payment_for,
                                     @Field("payment_gateway") String payment_gateway,
                                     @Field("platform") String platform,
                                     @Field("ppv_plan") String ppv_plan,
                                     @Field("video_id") String video_id,
                                     @Field("live_id") String live_id,
                                     @Field("season_id") String season_id,
                                     @Field("series_id") String series_id);

    @GET("planslist")
    Call<JSONResponse> getJSON();

    @FormUrlEncoded
    @POST("account_delete")
    Call<JSONResponse> deleteAccount(@Field("user_id") String user_id);

    @GET("Comming_soon")
    Call<JSONResponse> getComingSoon();

    @FormUrlEncoded
    @POST("remaining_Episode")
    Call<JSONResponse> getRemaingEpisode(@Field("seasonid") String seasonid,
                                         @Field("episodeid") String episodeid);

    @FormUrlEncoded
    @POST("related_series")
    Call<JSONResponse> getRelatedSeries(@Field("series_id") String username);

    @GET("stripe_recurring")
    Call<JSONResponse> getStripeOnetime();


    /*@GET("customer")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<JSONResponse> getCustomerId(@Header("Authorization") String authorization, @Body CustomerId customerid);*/
    //@Headers({ "Content-Type: application/json;charset=UTF-8"})

    @POST("customer")
    Call<CustomerId> getCustomerId(@Header("Authorization") String token, @Body CustomerId customerId);

    @POST("subscription")
    Call<CustomerId> Subscription(@Header("Authorization") String token, @Body CustomerId customerId);

    @POST("transaction/initialize")
    Call<CustomerId> getReference(@Header("Authorization") String token, @Body CustomerId customerId);

    @POST("v1/billing/subscriptions")
    Call<PaypalSubscription> PaypalSubscription(@Header("Authorization") String token, @Body PaypalSubscription paypalSubscription);

    @Headers({"Accept: application/json",
            "Accept-Language: en_US"})
    @FormUrlEncoded
    @POST("v1/oauth2/token")
    Call<PaypalSubscription> getAccessToken(@Field("grant_type") String grantType);

    @POST("PayPalSubscription")
    @FormUrlEncoded
    Call<JSONResponse> getPayPalSubscription(@Field("user_id") String user_id,
                                             @Field("plan_id") String plan_id,
                                             @Field("platform") String platform);

    @POST("channel_partner")
    @FormUrlEncoded
    Call<JSONResponse> getChannallist(@Field("slug") String slug);

    @POST("content_partner")
    @FormUrlEncoded
    Call<JSONResponse> getContentllist(@Field("slug") String slug);

    @GET("channel_partner_list")
    Call<JSONResponse> getChannel();

    @GET("home_content_partner")
    Call<JSONResponse> getContentPartner();


    @GET("splash")
    Call<JSONResponse> getSplash();

    @FormUrlEncoded
    @POST("login")
    Call<JSONResponse> getLogin(@Field("email") String username,
                                @Field("password") String password,
                                @Field("token") String token);

    @GET("AllAudios")
    Call<JSONResponse> getAllAudios();

    @FormUrlEncoded
    @POST("searchapi")
    Call<JSONResponse> getSearchcat(@Field("search") String search,
                                    @Field("type") String type);

    @GET("settings")
    Call<JSONResponse> getSettings();


    @GET("site_theme_setting")
    Call<JSONResponse> getthemeSettings();

    @GET("Currency_setting")
    Call<JSONResponse> getCurrencySetting();

    @GET("getCountries")
    Call<JSONResponse> getCountry();


    @GET("cmspages")
    Call<JSONResponse> getCmsPage();

    @GET("social_network_setting")
    Call<JSONResponse> getSocialNetworkSetting();

    @GET("latestvideos")
    Call<JSONResponse> getLatestVideos();

    @GET("MostwatchedVideos")
    Call<JSONResponse> getmostwatchedVideos();

    @GET("Country_MostwatchedVideos")
    Call<JSONResponse> getmostwatchedCountryVideos();

    @GET("data_free")
    Call<JSONResponse> getDatafree();

    @GET("FeaturedVideo")
    Call<JSONResponse> getFeaturedVideos();

    @GET("serieslist")
    Call<JSONResponse> getSeriesList();

    @FormUrlEncoded
    @POST("relatedseries")
    Call<JSONResponse> getRelatedseies(@Field("series_id") String username);

    @FormUrlEncoded
    @POST("seasonlist")
    Call<JSONResponse> getSeasonlist(@Field("seriesid") String username);

    @FormUrlEncoded
    @POST("MostwatchedVideosUser")
    Call<JSONResponse> getmostuser(@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("viewchildprofile")
    Call<JSONResponse> getChildProfile(@Field("parent_id") String username);

    @FormUrlEncoded
    @POST("listcontinuewatchings")
    Call<JSONResponse> getContinelisting(@Field("user_id") String username);


    @FormUrlEncoded
    @POST("listcontinuewatchingsepisode")
    Call<JSONResponse> getContineEpisodelisting(@Field("user_id") String username);

    @FormUrlEncoded
    @POST("seriesepisodes")
    Call<JSONResponse> getEpisodeList(@Field("seasonid") String username);

    @FormUrlEncoded
    @POST("episodedetails")
    Call<JSONResponse> getEpisodesDetail(@Field("episodeid") String username,
                                         @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("SeasonsPPV")
    Call<JSONResponse> getSeasonPpv(@Field("season_id") String season_id,
                                    @Field("episode_id") String episode_id,
                                    @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("social_user")
    Call<JSONResponse> getGooleclient(@Field("username") String username,
                                      @Field("email") String email,
                                      @Field("user_url") String url,
                                      @Field("login_type") String type);

    @GET("socialsetting")
    Call<JSONResponse> getSociallogin();


    @FormUrlEncoded
    @POST("mobile_signup")
    Call<JSONResponse> getPhonenumber(@Field("username") String username,
                                      @Field("email") String email);


    @FormUrlEncoded
    @POST("mobile_login")
    Call<JSONResponse> getMobileLogin(@Field("mobile") String mobile);


    @GET("payment_settings")
    Call<JSONResponse> getPaymentDetails();

    @GET("trendingvideos")
    Call<JSONResponse> getTrendingVideos();

    @GET("categorylist")
    Call<JSONResponse> getChannelList();

    @GET("menus")
    Call<JSONResponse> getMenu();

    @GET("Alllanguage")
    Call<JSONResponse> getLanguage();


    @GET("library-TV")
    Call<JSONResponse> getlibrary();


    @GET("andriod_slider")
    Call<JSONResponse> getBanners();

    @GET("Welcome_Screen")
    Call<JSONResponse> getScreen();

    @FormUrlEncoded
    @POST("ViewProfile")
    Call<JSONResponse> getUserprofile(@Field("user_id") String userid);

    @FormUrlEncoded
    @POST("becomesubscriber")
    Call<JSONResponse> getBecomSubcriber(@Field("userid") String userid,
                                         @Field("py_id") String py_id,
                                         @Field("subscrip_plan") String subscrib_plan);

    @FormUrlEncoded
    @POST("stripe-become-subscriber")
    Call<JSONResponse> getStripeBecomeSubcriber(@Field("userid") String userid,
                                                @Field("py_id") String py_id,
                                                @Field("plan_id") String plan_id,
                                                @Field("coupon_code") String coupon_code,
                                                @Field("platform") String platform);

    @FormUrlEncoded
    @POST("CinetPaySubscription")
    Call<JSONResponse> getBecomSubcriberViaCinetPay(@Field("user_id") String user_id,
                                                    @Field("plan_id") String plan_id,
                                                    @Field("amount") String amount,
                                                    @Field("country") String country,
                                                    @Field("state") String state,
                                                    @Field("city") String city,
                                                    @Field("platform") String platform);

    @FormUrlEncoded
    @POST("Paystack-become-subscriber")
    Call<JSONResponse> getBecomSubcriberPaystack(@Field("user_id") String user_id,
                                                 @Field("paystack_subscription_id") String paystack_subscription_id,
                                                 @Field("platform") String platform);

    @FormUrlEncoded
    @POST("related_livestream")
    Call<JSONResponse> getLiveRelatedVideos(@Field("live_id") String live_id);

    @FormUrlEncoded
    @POST("user_notifications")
    Call<JSONResponse> getNotification(@Field("user_id") String username);

    @FormUrlEncoded
    @POST("coupons")
    Call<JSONResponse> getCoupons(@Field("user_id") String userid);


    @FormUrlEncoded
    @POST("getStates")
    Call<JSONResponse> getState(@Field("country_id") String userid);


    @FormUrlEncoded
    @POST("getCities")
    Call<JSONResponse> getCity(@Field("state_id") String userid);

    @FormUrlEncoded
    @POST("videodetail")
    Call<JSONResponse> getMovieDetails(@Field("user_id") String userid,
                                       @Field("videoid") String id);

    @FormUrlEncoded
    @POST("videodetail")
    Call<JSONResponse> getMovieDetails1(@Field("user_id") String userid,
                                        @Field("videoid") String id,
                                        @Field("play_videoid") String play_video);


    @FormUrlEncoded
    @POST("SeasonsPPV")
    Call<JSONResponse> getMovieDetailsepisodes(@Field("user_id") String userid,
                                               @Field("season_id") String id,
                                               @Field("episode_id") String episode_id,
                                               @Field("play_videoid") String play_video);


    @FormUrlEncoded
    @POST("video_cast")
    Call<JSONResponse> getVideoCast(@Field("video_id") String id);

    @FormUrlEncoded
    @POST("livestreamdetail")
    Call<JSONResponse> getLiveDetail(@Field("user_id") String userid,
                                     @Field("liveid") String id);

    @FormUrlEncoded
    @POST("ppvvideodetail")
    Call<JSONResponse> getPPVMovieDetails(@Field("user_id") String userid,
                                          @Field("ppvvideoid") String id);

    @FormUrlEncoded
    @POST("nextwatchlatervideo")
    Call<JSONResponse> getWatchlatertNext(@Field("user_id") String userid,
                                          @Field("video_id") String id);

    @FormUrlEncoded
    @POST("nextwatchlatervideo")
    Call<JSONResponse> getWatchlatertNextEpisode(@Field("user_id") String userid,
                                                 @Field("episode_id") String id);

    @FormUrlEncoded
    @POST("prevwatchlatervideo")
    Call<JSONResponse> getWatchlatertPrev(@Field("user_id") String userid,
                                          @Field("video_id") String id);

    @FormUrlEncoded
    @POST("prevwatchlatervideo")
    Call<JSONResponse> getWatchlatertPrevEpisode(@Field("user_id") String userid,
                                                 @Field("video_id") String id);


    @FormUrlEncoded
    @POST("nextfavouritevideo")
    Call<JSONResponse> getfavouriteNext(@Field("user_id") String userid,
                                        @Field("video_id") String id);

    @FormUrlEncoded
    @POST("nextfavouritevideo")
    Call<JSONResponse> getfavouriteNextEpisode(@Field("user_id") String userid,
                                               @Field("episode_id") String id);

    @FormUrlEncoded
    @POST("prevfavouritevideo")
    Call<JSONResponse> getfavouritePrev(@Field("user_id") String userid,
                                        @Field("video_id") String id);

    @FormUrlEncoded
    @POST("prevfavouritevideo")
    Call<JSONResponse> getfavouritePrevEpisode(@Field("user_id") String userid,
                                               @Field("episode_id") String id);


    @FormUrlEncoded
    @POST("nextwishlistvideo")
    Call<JSONResponse> getWishlistNext(@Field("user_id") String userid,
                                       @Field("video_id") String id);

    @FormUrlEncoded
    @POST("nextwishlistvideo")
    Call<JSONResponse> getWishlistNextEpisode(@Field("user_id") String userid,
                                              @Field("episode_id") String id);


    @FormUrlEncoded
    @POST("prevwishlistvideo")
    Call<JSONResponse> getWishlistPrev(@Field("user_id") String userid,
                                       @Field("video_id") String id);

    @FormUrlEncoded
    @POST("prevwishlistvideo")
    Call<JSONResponse> getWishlistPrevEpisode(@Field("user_id") String userid,
                                              @Field("episode_id") String id);

    @FormUrlEncoded
    @POST("next_video")
    Call<JSONResponse> getNext_video(@Field("id") String id);

    @FormUrlEncoded
    @POST("retrieve_stripe_coupon")
    Call<JSONResponse> verifyCoupon(@Field("coupon_code") String coupon_code,
                                    @Field("plan_price") String plan_price);

    @FormUrlEncoded
    @POST("NextEpisode")
    Call<JSONResponse> getNext_Episode(@Field("episode_id") String id,
                                       @Field("seasonid") String seasonid);

    @FormUrlEncoded
    @POST("prev_video")
    Call<JSONResponse> getPrev_video(@Field("id") String id);

    @FormUrlEncoded
    @POST("PrevEpisode")
    Call<JSONResponse> getPrev_Episode(@Field("episode_id") String id,
                                       @Field("seasonid") String seasonid);

    @FormUrlEncoded
    @POST("relatedchannelvideos")
    Call<JSONResponse> getAlsolikeVideo(@Field("videoid") String movieid);

    @FormUrlEncoded
    @POST("relatedepisodes")
    Call<JSONResponse> getAlsolikeEpisodes(@Field("episodeid") String episodeid);

    @FormUrlEncoded
    @POST("relatedppvvideos")
    Call<JSONResponse> getAlsolikePPV(@Field("ppvvideoid") String movieid);


    @FormUrlEncoded
    @POST("myWishlists")
    Call<JSONResponse> getMyWishlist(@Field("user_id") String movieid);

    @FormUrlEncoded
    @POST("myWishlistsEpisode")
    Call<JSONResponse> getMyWishlistEpisode(@Field("user_id") String movieid);


    @FormUrlEncoded
    @POST("mywatchlaters")
    Call<JSONResponse> getMyWatchlater(@Field("user_id") String movieid);

    @FormUrlEncoded
    @POST("mywatchlatersEpisode")
    Call<JSONResponse> getMyWatchlaterEpisode(@Field("user_id") String movieid);


    @FormUrlEncoded
    @POST("myFavorites")
    Call<JSONResponse> getMyFavorites(@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("getPPV")
    Call<JSONResponse> getPPv(@Field("user_id") String userid);


    @FormUrlEncoded
    @POST("search_andriod")
    Call<JSONResponse> getSearchResult(@Field("search_value") String search);

    @POST("channelvideos")
    @FormUrlEncoded
    Call<JSONResponse> getChannelVideo(@Field("channelid") String email_id);


    @POST("Livestream-based-categories")
    Call<HomeBodyResponse> getLiveBasedCate();

    @POST("All_Pagelist")
    @FormUrlEncoded
    Call<JSONResponse> gethomelink(@Field("source_name") String Source_name, @Field("user_id") String user_id);

    @POST("All_Pagelist")
    @FormUrlEncoded
    Call<JSONResponse> gethomelink1(@Field("source_name") String Source_name, @Field("category_id") String user_id);



   /* @POST("channelvideos")
    @FormUrlEncoded
    Call<JSONResponse> getChannelVideo(@Field("channelid") String email_id);
*/

    @POST("category_live")
    @FormUrlEncoded
    Call<JSONResponse> getLiveCat(@Field("category_id") String email_id);


    @POST("VideoLanguage")
    @FormUrlEncoded
    Call<JSONResponse> getVideoLangage(@Field("language_id") String email_id,
                                       @Field("user_id") String user_id);


    @POST("user_comments")
    @FormUrlEncoded
    Call<JSONResponse> getUsercomments(@Field("video_id") String email_id);


    @POST("comment_message")
    @FormUrlEncoded
    Call<JSONResponse> getUsercommentslive(@Field("source_id") String email_id,
                                           @Field("commentable_type") String type);

    @FormUrlEncoded
    @POST("audiodetail")
    Call<JSONResponse> getAudioDetail(@Field("user_id") String userid,
                                      @Field("audio_id") String id);

    @FormUrlEncoded
    @POST("relatedaudios")
    Call<JSONResponse> getRecommendedAudio(@Field("audio_id") String audio_id);

    @FormUrlEncoded
    @POST("next_audio")
    Call<JSONResponse> getNextAudioDetail(@Field("user_id") String userid,
                                          @Field("audio_id") String id);

    @FormUrlEncoded
    @POST("audio_shufffle")
    Call<JSONResponse> getSuffleAudios(@Field("album_id") String id);

    @FormUrlEncoded
    @POST("prev_audio")
    Call<JSONResponse> getPrevAudioDetail(@Field("user_id") String userid,
                                          @Field("audio_id") String id);

    @GET("getRecentAudios")
    Call<JSONResponse> getRecentAudios();

    @GET("artistlist")
    Call<JSONResponse> getArtistList();

    @FormUrlEncoded
    @POST("artistfollowings")
    Call<JSONResponse> getArtistyouFollow(@Field("user_id") String userid);

    @FormUrlEncoded
    @POST("artistfavorites")
    Call<JSONResponse> getFavouriteArtist(@Field("user_id") String userid);

    @FormUrlEncoded
    @POST("artistdetail")
    Call<JSONResponse> getArtistDetails(@Field("artist_id") String audio_id,
                                        @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("payment_plan")
    Call<JSONResponse> getPlanID(@Field("plan_name") String audio_id,
                                 @Field("payment_type") String user_id);

    @FormUrlEncoded
    @POST("RazorpaySubscription")
    Call<JSONResponse> getSubscriptionDeatails(@Field("plan_id") String audio_id);

    @FormUrlEncoded
    @POST("album_audios")
    Call<JSONResponse> getAudioAlbum(@Field("album_id") String audio_id);

    @FormUrlEncoded
    @POST("PurchaseSeries")
    Call<JSONResponse> getSeriesDetail(@Field("seriesid") String seriesid);


    @FormUrlEncoded
    @POST("series_image_details")
    Call<series_imgae> getseason_image(@Field("series id") String id);


    @FormUrlEncoded
    @POST("audioscategory")
    Call<JSONResponse> getCategoryAudios(@Field("audiocategoryid") String audio_id,
                                         @Field("userid") String user_id);


    @GET("trendingaudio")
    Call<JSONResponse> getTrendingAudios();


    @GET("albumlist")
    Call<JSONResponse> getAudiocatList();

    @FormUrlEncoded
    @POST("multi-currency-converter")
    Call<JSONResponse> currencyConverter(@Field("Country_name") String Country_name,
                                         @Field("amount") String amount);

    @FormUrlEncoded
    @POST("Paydunya_become_subscriber")
    Call<JSONResponse> paydunyaBecomeSubscriber(@Field("paydunya_trans_id") String paydunya_trans_id,
                                                @Field("user_id") String user_id,
                                                @Field("plan") String plan,
                                                @Field("paydunya_status") String paydunya_status,
                                                @Field("platform") String platform);

    @FormUrlEncoded
    @POST("signup")
    Call<JSONResponse> paydunyaSignup(@Field("username") String name,
                                      @Field("email") String email,
                                      @Field("password") String password,
                                      @Field("paydunya_plan") String sub,
                                      @Field("ccode") String ccode,
                                      @Field("mobile") String mobile,
                                      @Field("skip") String skip,
                                      @Field("token") String token,
                                      @Field("payment_type") String pt,
                                      @Field("payment_mode") String pm,
                                      @Field("paydunya_trans_id") String paydunya_trans_id,
                                      @Field("paydunya_status") String paydunya_status);

    @FormUrlEncoded
    @POST("Paydunya_video_Rent_payment_verify")
    Call<JSONResponse> paydunyaVideoRent(@Field("user_id") String user_id,
                                         @Field("video_id") String video_id,
                                         @retrofit.http.Field("platform") String platform);

    @FormUrlEncoded
    @POST("Paydunya_live_Rent_payment_verify")
    Call<JSONResponse> paydunyaLiveRent(@Field("user_id") String user_id,
                                        @Field("live_id") String live_id,
                                        @retrofit.http.Field("platform") String platform);

    @FormUrlEncoded
    @POST("Paydunya_SeriesSeason_Rent_payment_verify")
    Call<JSONResponse> paydunyaSeasonRent(@Field("user_id") String user_id,
                                          @Field("SeriesSeason_id") String SeriesSeason_id,
                                          @retrofit.http.Field("platform") String platform);

    @FormUrlEncoded
    @POST("PaydunyaCancelSubscriptions")
    Call<JSONResponse> cancelSubscrption(@Field("user_id") String user_id);
   /* @FormUrlEncoded
    @POST("Mobile-exists-verify")
    Call<JSONResponse> mobileExistVerify1(@Field("mobile_number") String mobile_number,@Field("ccode") String ccode);*/

    //@FormUrlEncoded
    @POST("Mobile-exists-verify")
    Call<JSONResponse> mobileExistVerify(@Body mobile_number mobile_number);

    @FormUrlEncoded
    @POST("Sending-OTP")
    Call<JSONResponse> sendingOTP(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("Verify-OTP")
    Call<JSONResponse> verifyOTP(@Field("mobile_number") String mobile_number,
                                 @Field("ccode") String ccode,
                                 @Field("user_id") String user_id,
                                 @Field("otp") String otp);

    @FormUrlEncoded
    @POST("login")
    Call<JSONResponse> getSMSLogin(@Field("mobile") String mobile,
                                   @Field("otp") String otp,
                                   @Field("password") String password);
    @FormUrlEncoded
    @POST("resetpassword")
    Call<JSONResponse> getRestpassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("verify-token-reset-password")
    Call<JSONResponse> verifyResetpassword(@Field("email") String email,
                                           @Field("verify_code") String verify_code);
    @FormUrlEncoded
    @POST("update-reset-password")
    Call<JSONResponse> updateResetpassword(@Field("email") String email,
                                           @Field("password") String password);
}
