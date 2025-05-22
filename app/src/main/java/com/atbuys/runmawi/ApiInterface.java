package com.atbuys.runmawi;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;


public interface ApiInterface {

    @FormUrlEncoded
    @POST("/signup")
    public void getSignup(@Field("username") String name,
                          @Field("email") String email,
                          @Field("password") String password,
                          Callback<registerresponse> callback);

    @FormUrlEncoded
    @POST("/ContinueWatchingExits")
    public void getContinueexist(@Field("user_id") String notify_id,
                                 @Field("video_id") String video_id,
                                 Callback<ReadNotification> callback);

    @GET("/socialsetting")
    public void getSocialLogin(Callback<socialsetting> callback);


    @FormUrlEncoded
    @POST("/read_notification")
    public void getReadNotification(@Field("notification_id") String notify_id,
                                    Callback<ReadNotification> callback);

    @FormUrlEncoded
    @POST("/signup")
    public void getSignupWithSkip(@Field("username") String name,
                                  @Field("email") String email,
                                  @Field("password") String password,
                                  @Field("ccode") String ccode,
                                  @Field("mobile") String mobile,
                                  @Field("skip") String skip,
                                  @Field("referrer_code") String referrer_code,
                                  @Field("token") String token,
                                  @Field("payment_type") String payment,
                                  Callback<registerresponse> callback);


    @FormUrlEncoded
    @POST("/signup")
    public void getSignup1(@Field("username") String name,
                           @Field("email") String email,
                           @Field("password") String password,
                           @Field("py_id") String py_id,
                           @Field("plan") String sub,
                           @Field("ccode") String ccode,
                           @Field("mobile") String mobile,
                           @Field("skip") String skip,
                           @Field("referrer_code") String referrer_code,
                           @Field("token") String token,
                           @Field("amount") String amt,
                           @Field("payment_type") String pt,
                           @Field("payment_mode") String pm,
                           @Field("razorpay_subscription_id") String sub_id,
                           @Field("coupon_code") String coupon_code,
                           Callback<registerresponse> callback);

    @FormUrlEncoded
    @POST("/signup")
    public void getPaystackSignup(@Field("username") String username,
                                  @Field("email") String email,
                                  @Field("password") String password,
                                  @Field("paystack_subscription_id") String paystack_subscription_id,
                                  @Field("plan") String plan,
                                  @Field("ccode") String ccode,
                                  @Field("mobile") String mobile,
                                  @Field("skip") String skip,
                                  @Field("reference_code") String reference_code,
                                  @Field("token") String token,
                                  @Field("amt") String amt,
                                  @Field("payment_type") String payment_type,
                                  @Field("payment_mode") String payment_mode,
                                  @Field("sub_id ") String sub_id,
                                  @Field("platform") String platform,
                                  Callback<registerresponse> callback);

    @FormUrlEncoded
    @POST("/signup")
    public void getPaypalSignup(@Field("username") String username,
                                @Field("email") String email,
                                @Field("password") String password,
                                @Field("plan_id") String plan_id,
                                @Field("ccode") String ccode,
                                @Field("mobile") String mobile,
                                @Field("payment_type") String payment_type,
                                @Field("payment_mode") String payment_mode,
                                @Field("platform") String platform,
                                Callback<registerresponse> callback);

    @FormUrlEncoded
    @POST("/signup")
    public void getCinetpaySignup(@Field("username") String username,
                                  @Field("email") String email,
                                  @Field("password") String password,
                                  @Field("paystack_subscription_id") String paystack_subscription_id,
                                  @Field("plan") String plan,
                                  @Field("ccode") String ccode,
                                  @Field("mobile") String mobile,
                                  @Field("skip") String skip,
                                  @Field("reference_code") String reference_code,
                                  @Field("token") String token,
                                  @Field("amt") String amt,
                                  @Field("payment_type") String payment_type,
                                  @Field("payment_mode") String payment_mode,
                                  @Field("sub_id ") String sub_id,
                                  @Field("platform") String platform,
                                  Callback<registerresponse> callback);

    @FormUrlEncoded
    @POST("/signup")
    public void getRazorSignup1(@Field("username") String name,
                                @Field("email") String email,
                                @Field("password") String password,
                                @Field("plan") String sub,
                                @Field("ccode") String ccode,
                                @Field("mobile") String mobile,
                                @Field("skip") String skip,
                                @Field("token") String token,
                                @Field("payment_type") String pt,
                                @Field("payment_mode") String pm,
                                @Field("razorpay_subscription_id") String sub_id,
                                Callback<registerresponse> callback);


    @FormUrlEncoded
    @POST("/updateProfile")
    public void getUps(@Field("avatar") String avatar,
                       @Field("user_id") String user_id,
                       @Field("user_username") String username,
                       @Field("user_email") String email,
                       @Field("user_ccode") String ccode,
                       @Field("user_mobile") String mobile,
                       @Field("user_name") String skip,
                       Callback<resetpass> callback);


    @FormUrlEncoded
    @POST("/resetpassword")
    public void getRestpassword(@Field("email") String email,
                                Callback<resetpass> callback);


    @FormUrlEncoded
    @POST("/verfiy_become_subscription")
    public void getSubPaystack(@Field("user-id") String email,
                               Callback<paystackverify> callback);

    @FormUrlEncoded
    @POST("/comment_update")
    public void getupdateLivecomment(@Field("user_id") String user_id,
                                     @Field("source_id") String video_id,
                                     @Field("message") String body,
                                     @Field("source") String source,
                                     @Field("comment_id") String id,
                                     Callback<AddComment> callback);

    @FormUrlEncoded
    @POST("/comment_destroy")
    public void getDeleteComment(@Field("comment_id") String user_id,
                                 Callback<AddComment> callback);


    @FormUrlEncoded
    @POST("/comment_update")
    public void getEditLiveComment(@Field("user_id") String user_id,
                                   @Field("source_id") String video_id,
                                   @Field("message") String body,
                                   @Field("source") String source,
                                   @Field("comment_id") String comment_id,
                                   Callback<AddComment> callback);

    @FormUrlEncoded
    @POST("/comment_store")
    public void getAddLivecomment(@Field("user_id") String user_id,
                                  @Field("source_id") String video_id,
                                  @Field("message") String body,
                                  @Field("source") String source,
                                  Callback<AddComment> callback);

    @FormUrlEncoded
    @POST("/add_payperview")
    public void getAddPayperView(@Field("user_id") String user_id,
                                 @Field("video_id") String video_id,
                                 @Field("py_id") String py_id,
                                 @Field("payment_type") String payment_type,
                                 Callback<Addpayperview> callback);

    @FormUrlEncoded
    @POST("/add_payperview")
    public void getAddPayperView1(@Field("user_id") String user_id,
                                  @Field("video_id") String video_id,
                                  @Field("py_id") String py_id,
                                  @Field("payment_type") String payment_type,
                                  @Field("amount") String amount,
                                  @Field("platform") String platform,
                                  Callback<Addpayperview> callback);

    @FormUrlEncoded
    @POST("/add_payperview")
    public void getAddPayperViewWithqualityStore(@Field("user_id") String user_id,
                                  @Field("video_id") String video_id,
                                  @Field("py_id") String py_id,
                                  @Field("py_status") String py_status,
                                  @Field("payment_type") String payment_type,
                                  @Field("ppv_plan") String ppv_plan,
                                  @Field("amount") String amount,
                                  @Field("platform") String platform,
                                  Callback<Addpayperview> callback);

    @FormUrlEncoded
    @POST("/add_payperview")
    public void getAddPayperViewLive(@Field("user_id") String user_id,
                                     @Field("live_id") String live_id,
                                     @Field("py_id") String py_id,
                                     @Field("payment_type") String payment_type,
                                     @Field("amount") String amount,
                                     @Field("platform") String platform,
                                     Callback<Addpayperview> callback);

    @FormUrlEncoded
    @POST("/add_payperview")
    public void getAddPayperViewLiveNew(@Field("user_id") String user_id,
                                     @Field("live_id") String live_id,
                                     @Field("py_id") String py_id,
                                        @Field("py_status") String py_status,
                                     @Field("payment_type") String payment_type,
                                     @Field("amount") String amount,
                                     @Field("platform") String platform,
                                     Callback<Addpayperview> callback);

    @FormUrlEncoded
    @POST("/add_payperview")
    public void getAddPayperViewSeason(@Field("user_id") String user_id,
                                       @Field("series_id") String series_id,
                                       @Field("season_id") String season_id,
                                       @Field("py_id") String py_id,
                                       @Field("py_status") String py_status,
                                       @Field("payment_type") String payment_type,
                                       @Field("ppv_plan") String ppv_plan,
                                       @Field("amount") String amount,
                                       @Field("platform") String platform,
                                       Callback<Addpayperview> callback);
    @FormUrlEncoded
    @POST("/add_payperview")
    public void getAddPayperViewAudio(@Field("user_id") String user_id,
                                       @Field("audio_id") String audio_id,
                                       @Field("py_id") String py_id,
                                       @Field("payment_type") String payment_type,
                                       @Field("amount") String amount,
                                      @Field("platform") String platform,
                                       Callback<Addpayperview> callback);

    @FormUrlEncoded
    @POST("/add_payperview")
    public void getAddPayperViewSeries(@Field("user_id") String user_id,
                                       @Field("series_id") String series_id,
                                       @Field("py_id") String py_id,
                                       @Field("payment_type") String payment_type,
                                       @Field("amount") String amount,
                                       Callback<Addpayperview> callback);

    @FormUrlEncoded
    @POST("/Paystack-VideoRent-Paymentverify")
    public void getPaystackppv(@Field("user_id") String user_id,
                               @Field("video_id") String video_id,
                               @Field("reference_id") String reference_id,
                               @retrofit.http.Field("platform") String platform,
                               Callback<Addpayperview> callback);

    @FormUrlEncoded
    @POST("/Paystack-liveRent-Paymentverify")
    public void getPaystackppvLive(@Field("user_id") String user_id,
                                   @Field("live_id") String live_id,
                                   @Field("reference_id") String reference_id,
                                   @retrofit.http.Field("platform") String platform,
                                   Callback<Addpayperview> callback);

    @FormUrlEncoded
    @POST("/Paystack-SerieSeasonRent-Paymentverify")
    public void getPaystackppvSeason(@Field("user_id") String user_id,
                                     @Field("series_id") String series_id,
                                     @Field("season_id") String season_id,
                                     @Field("reference_id") String reference_id,
                                     @retrofit.http.Field("platform") String platform,
                                     Callback<Addpayperview> callback);
    @FormUrlEncoded
    @POST("/Paystack-AudioRent-Paymentverify")
    public void getPaystackppvAudio(@Field("user_id") String user_id,
                                     @Field("audio_id") String audio_id,
                                     @Field("reference_id") String reference_id,
                                    @retrofit.http.Field("platform") String platform,
                                     Callback<Addpayperview> callback);

    @FormUrlEncoded
    @POST("/directVerify")
    public void getMobileotp(@Field("mobile") String user_id,
                             @Field("ccode") String video_id,
                             @Field("activation code") String py_id,
                             Callback<mobileOTP> callback);

    @FormUrlEncoded
    @POST("/subscriptiondetail")
    public void getSubscribeDetail(@Field("user_id") String email,
                                   Callback<subscriberesponse> callback);


    @FormUrlEncoded
    @POST("/addppvpaypal")
    public void getPaypalRent(@Field("user_id") String user_id,
                              @Field("video_id") String video_id,
                              @Field("status") String status,
                              Callback<Paypalresponse> callback);


    @FormUrlEncoded
    @POST("/updatepassword")
    public void getRestpassword1(@Field("password") String password,
                                 @Field("email") String email,
                                 @Field("verification_code") String verification,
                                 Callback<resetpass> callback);

    @FormUrlEncoded
    @POST("/cancelsubscription")
    public void getcancelpayment(@Field("user_id") String name,
                                 Callback<cancelpayment> callback);

    @FormUrlEncoded
    @POST("/upgradesubscription")
    public void getUpgrade(@Field("user_id") String name,
                           @Field("plan_name") String plan,
                           @Field("coupon_code") String couponcode,
                           @Field("ref_id") String ref_code,
                           Callback<UpgradeResponse> callback);


    @FormUrlEncoded
    @POST("/renewsubscription")
    public void getRenewsub(@Field("user_id") String name,
                            Callback<RenewSubscribe> callback);


    @FormUrlEncoded
    @POST("/refferal")
    public void getReferDetail(@Field("user_id") String name,
                               Callback<ReferralDetails> callback);


    @FormUrlEncoded
    @POST("/like-dislike")
    public void getlikeandunlike(@Field("user_id") String name,
                                 @Field("videoid") String email,
                                 @Field("like") String like,
                                 Callback<likeandunlike> callback);


    @FormUrlEncoded
    @POST("/dislike")
    public void getDislikecount(@Field("user_id") String name,
                                @Field("video_id") String email,
                                @Field("dislike") String like,
                                Callback<likeandunlike> callback);

    @FormUrlEncoded
    @POST("/Episode_dislike")
    public void getDislikecountEpisode(@Field("user_id") String name,
                                       @Field("video_id") String email,
                                       @Field("dislike") String like,
                                       Callback<likeandunlike> callback);


    @FormUrlEncoded
    @POST("/audio_dislike")
    public void getAudioDislikecount(@Field("user_id") String name,
                                     @Field("audio_id") String email,
                                     @Field("dislike") String like,
                                     Callback<likeandunlike> callback);


    @FormUrlEncoded
    @POST("/like")
    public void getLikecount(@Field("user_id") String name,
                             @Field("video_id") String email,
                             @Field("like") String like,
                             Callback<likeandunlike> callback);

    @FormUrlEncoded
    @POST("/Episode_like")
    public void getLikecountEpisode(@Field("user_id") String name,
                                    @Field("episode_id") String email,
                                    @Field("like") String like,
                                    Callback<likeandunlike> callback);


    @FormUrlEncoded
    @POST("/audio_like")
    public void getAudioLikecount(@Field("user_id") String name,
                                  @Field("audio_id") String email,
                                  @Field("like") String like,
                                  Callback<likeandunlike> callback);


    @FormUrlEncoded
    @POST("/addwishlist")
    public void getAddWishlistMovie(@Field("user_id") String name,
                                    @Field("video_id") String email,
                                    @Field("type") String type,
                                    Callback<Addtowishlistmovie> callback);


    @FormUrlEncoded
    @POST("/Episode_addwishlist")
    public void getAddWishlistEpisode(@Field("user_id") String name,
                                      @Field("episodeid") String email,
                                      Callback<Addtowishlistmovie> callback);


    @FormUrlEncoded
    @POST("/addwishlist")
    public void getAddWishlistMovie(@Field("user_id") String name,
                                    @Field("audio_id") String email,
                                    Callback<Addtowishlistmovie> callback);


    @FormUrlEncoded
    @POST("/addwatchlater")
    public void getAddwatchlater(@Field("user_id") String name,
                                 @Field("video_id") String email,
                                 @Field("type") String type,
                                 Callback<Addtowishlistmovie> callback);


    @FormUrlEncoded
    @POST("/Episode_addwatchlater")
    public void getAddwatchlaterEpisode(@Field("user_id") String name,
                                        @Field("Episode_id") String email,
                                        Callback<Addtowishlistmovie> callback);

    @FormUrlEncoded
    @POST("/Episode_addfavorite")
    public void getFaouriteEpisode(@Field("user_id") String name,
                                   @Field("episode_id") String email,
                                   Callback<Addtofavouritemovie> callback);

    @FormUrlEncoded
    @POST("/addfavorite")
    public void getFaourite(@Field("user_id") String name,
                            @Field("video_id") String email,
                            Callback<Addtofavouritemovie> callback);


    @GET("/series_title_status")
    public void getSeriesTitle(
            Callback<seriestitle> callback);


    @GET("/splash")
    public void getsplash(
            Callback<spalashscreenlist> callback);


    @GET("/homesetting")
    public void getsetting(
            Callback<settinghome> callback);

    @GET("/skip_time")
    public void getskiptime(
            Callback<skiptime> callback);


    @FormUrlEncoded
    @POST("/verifyandupdatepassword")
    public void getChangepassword(@Field("password") String password,
                                  @Field("email") String email,
                                  @Field("old_password") String verification,
                                  Callback<resetpass> callback);


    @GET("/isPaymentEnable")
    public void getIsPayment(
            Callback<IsPayment> callback);


    @FormUrlEncoded
    @POST("/checkEmailExists")
    public void getEmailExists(@Field("email") String email,
                               @Field("username") String username,
                               Callback<emailexists> callback);

    @FormUrlEncoded
    @POST("/SendOtp")
    public void getSendOtp(@Field("mobile") String email,
                           @Field("ccode") String username,
                           Callback<sendotp> callback);

    @FormUrlEncoded
    @POST("/VerifyOtp")
    public void getVerifyOTP(@Field("otp") String email,
                             @Field("verify_id") String username,
                             Callback<verifyotp> callback);


    @FormUrlEncoded
    @POST("/add_comment")
    public void getAddcomment(@Field("user_id") String user_id,
                              @Field("video_id") String video_id,
                              @Field("body") String body,
                              Callback<AddComment> callback);


    @FormUrlEncoded
    @POST("/addtocontinuewatching")
    public void getContinueWatching(@Field("user_id") String user_id,
                                    @Field("video_id") String video_id,
                                    @Field("current_duration") String duration,
                                    @Field("watch_percentage") String percenage,
                                    Callback<ContinuWatching> callback);

    @FormUrlEncoded
    @POST("/EpisodeContinuewatching")
    public void getContinueWatchingEpisode(@Field("user_id") String user_id,
                                           @Field("episode_id") String video_id,
                                           @Field("current_duration") String duration,
                                           @Field("watch_percentage") String percenage,
                                           Callback<ContinuWatching> callback);


    @FormUrlEncoded
    @POST("/remove_continue_watchingvideo")
    public void getRemoveContinueWatching(@Field("user_id") String user_id,
                                          @Field("video_id") String video_id,
                                          Callback<Addtowishlistmovie> callback);


    @FormUrlEncoded
    @POST("/addchilduserprofile")
    public void getaddProfile(@Field("parent_id") String user_id,
                              @Field("user_name") String video_id,
                              @Field("user_type") String type,
                              Callback<ContinuWatching> callback);


    @FormUrlEncoded
    @POST("/addfavorite")
    public void getAddFavourite(@Field("user_id") String name,
                                @Field("audio_id") String email,
                                Callback<Addtowishlistmovie> callback);


    @FormUrlEncoded
    @POST("/artistaddremovefav")
    public void getAddorRemoveFavourite(@Field("user_id") String name,
                                        @Field("artist_id") String email,
                                        @Field("favourites") String favartist,
                                        Callback<Addtowishlistmovie> callback);


    @GET("/check_block_list")
    public void getIP(
            Callback<Ipaddress1> callback);


    @FormUrlEncoded
    @POST("/artistaddremovefollow")
    public void getAddorRemoveFollowing(@Field("user_id") String user_id,
                                        @Field("artist_id") String email,
                                        @Field("following") String followartist,
                                        Callback<Addtowishlistmovie> callback);


    @FormUrlEncoded
    @POST("/addfavoriteaudio")
    public void getAddFavouriteAudio(@Field("user_id") String name,
                                     @Field("audios_id") String email,
                                     Callback<Addtowishlistmovie> callback);

    @FormUrlEncoded
    @POST("/RazorpayStore")
    public void getSub(@Field("razorpay_subscription_id") String name,
                       @Field("userId") String email,
                       Callback<becomesub> callback);

    @FormUrlEncoded
    @POST("/addwatchlateraudio")
    public void getAddWishlistAudio(@Field("user_id") String name,
                                    @Field("audios_id") String email,
                                    Callback<Addtowishlistmovie> callback);


}
