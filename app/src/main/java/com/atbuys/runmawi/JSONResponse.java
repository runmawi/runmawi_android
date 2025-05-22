package com.atbuys.runmawi;


import com.atbuys.runmawi.Adapter.series_imgae;

public class JSONResponse {
    //  private plans[] plans;
    private user_details[] user_details;
    private settings[] settings;
    private user_detail user_detail;
    private socail_networl_setting[] socail_networl_setting;
    private sliders[] sliders;
    private livestream[] livestream;
    private latestvideos[] latestvideos;
    private categorylist[] categorylist;
    private pages[] pages;
    private videodetail[] videodetail;
    private movie_details[] movie_detail;
    private payperview_video[] payperview_video;
    private channel_videos[] channel_videos;
    private channelrecomended[] channelrecomended;
    private video_cast[] video_cast;
    private country[] country;
    private videos[] videos;
    private ppvrecomended[] ppvrecomended;
    private states[] states;
    private cities[] cities;
    private channelvideos[] channelvideos;
    private channel_category[] channel_category;
    private ppv_videos[] ppv_videos;
    private ppv_category[] ppv_category;
    private video_banner[] video_banner;
    private plans[] plans;
    private trendingvideos[] trendingvideos;
    private series[] series;
    private series_banner[] series_banner;
    private season[] season;
    private episodes[] episodes;
    private comment[] comment;
    private episode[] episode;
    private categoryVideos[] categoryVideos;
    private related_episode[] related_episode;
    private payment_settings[] payment_settings;
    private myrefferals[] myrefferals;
    private videossubtitles[] videossubtitles;
    private notifications[] notifications;
    private user_comments[] user_comments;
    private user user;
    private user_data[] user_data;
    private sub_users[] sub_users;
    private audiodetail[] audiodetail;
    private recomendedaudios[] recomendedaudios;
    private audio[] audio;
    private audio_shufffle[] audio_shufffle;
    private recent_audios[] recent_audios;
    private artistlist[] artistlist;
    private followinglist[] followinglist;
    private trending_audios[] trending_audios;
    private favoriteslist[] favoriteslist;
    private channel_audios[] channel_audios;
    private artist_audios[] artist_audios;
    private audioalbums[] audioalbums;
    private series_imgae[] season_image;
    private Menus[] Menus;
    private LiveCategory[] LiveCategory;
    private data[] data;
    private artist[] artist;
    private audiocategories[] audiocategories;
    private all_languages[] all_languages;
    private language_videos[] language_videos;
    private featured_videos[] featured_videos;
    private video_categories[] video_categories;
    private video_artist[] video_artist;
    private allaudios[] allaudios;
    private plan[] plan;
    private WelcomeScreen[] WelcomeScreen;

    private active_payment_settings[] active_payment_settings;
    private livedetail[] livedetail;
    private Splash_Screen[] Splash_Screen;
    private albumcategoryauido[] albumcategoryauido;
    private categoryaudio[] categoryaudio;
    private respond[] respond;
    private live_banner[] live_banner;
    private Site_theme_setting[] Site_theme_setting;//socail_networl_setting

    private Currency_Setting[] Currency_Setting;
    private channels[] channels;
    private ContentPartner[] ContentPartner;

    private latest_video[] latest_video;
    private latest_series[] latest_series;
    private audios[] audios;
    private livetream[] livetream;
    private live_streams[] live_streams;
    private socialsetting[] socialsetting;
    private Mostwatched[] Mostwatched;
    private Mostwatchedvideos[] Mostwatchedvideos;
    private mostWatchedUserVideos[] mostWatchedUserVideos;
    private Series_list[] Series_list;


    private PageList[] Page_List;

    private String status,amount,email;
    private String otp_status;
    private String message,mobile_number_status;
    private String ppv_status;
    private String movieurl;
    private String languages;
    private String wishlist;
    private String watchlater;
    private String shareurl;
    private String ends_at;
    private String next_billing,Currency_Symbol;
    private String ppvstatus;
    private String first_season_id,promo_code_amt,discount_amt;
    private String ppv_video_status;
    private String ppv_exist;
    private String playbackInfo;

    private String ppv_price;
    private String main_genre;
    private String curren_stripe_plan;
    private String coupon_code;
    private String videonext,userrole;
    private String next_url;
    private String diaplay_name;
    private String cast;
    private String otp;
    private String like;
    private String dislike;
    private String next_videoid,id;
    private String curr_time;
    private String favorite;
    private String favourites;
    private String following;
    private String videoads,access,users_role;
    private String next_episodeid;
    private String media_url,Currency_Converted;


    public String getUsers_role() {
        return users_role;
    }

    public String getCurrency_Converted() {
        return Currency_Converted;
    }

    public String getStatus() {
        return status;
    }

    public String getAccess() {
        return access;
    }

    public String getMessage() {
        return message;
    }

    public String getppv_status() {
        return ppv_status;
    }

    public String getShareurl() {
        return shareurl;
    }

    public String getDiscount_amt() {
        return discount_amt;
    }

    public String getLanguages() {
        return languages;
    }

    public String getCurrency_Symbol() {
        return Currency_Symbol;
    }

    public String getEnds_at() {
        return ends_at;
    }

    public String getNext_billing() {
        return next_billing;
    }

    public String getWishlist() {
        return wishlist;
    }

    public String getWatchlater() {
        return watchlater;
    }

    public String getPpvstatus() {
        return ppvstatus;
    }

    public String getPromo_code_amt() {
        return promo_code_amt;
    }

    public String getUserrole() {
        return userrole;
    }

    public String getFirst_season_id() {
        return first_season_id;
    }

    public String getPpv_video_status() {
        return ppv_video_status;
    }

    public String getMain_genre() {
        return main_genre;
    }

    public String getCurren_stripe_plan() {
        return curren_stripe_plan;
    }

    public String getCoupon_code() {
        return coupon_code;
    }


    public String getPpv_price() {
        return ppv_price;
    }

    public String getVideonext() {
        return videonext;
    }

    public String getAmount() {
        return amount;
    }

    public String getCast() {
        return cast;
    }

    public String getNext_url() {
        return next_url;
    }

    public String getEmail() {
        return email;
    }

    public String getDiaplay_name() {
        return diaplay_name;
    }

    public String getOtp() {
        return otp;
    }

    public String getLike() {
        return like;
    }

    public String getDislike() {
        return dislike;
    }

    public String getNext_videoid() {
        return next_videoid;
    }

    public String getNext_episodeid()
    {
        return next_episodeid;
    }

    public String getVideoads() {
        return videoads;
    }


    public String getCurr_time() {
        return curr_time;
    }

    public String getFavorite() {
        return favorite;
    }

    public episodes[] getEpisodes() {
        return episodes;
    }

    public categoryVideos[] getCategoryVideos() {
        return categoryVideos;
    }

    public com.atbuys.runmawi.user_detail getUser_detail() {
        return user_detail;
    }

    public episode[] getEpisode() {
        return episode;
    }

    public related_episode[] getRelated_episode() {
        return related_episode;
    }

    public plans[] getPlans() {
        return plans;
    }

    public trendingvideos[] getTrendingvideos() {
        return trendingvideos;
    }

    public user_details[] getUser_details() {
        return user_details;
    }

    public video_cast[] getCast_details() {
        return video_cast;
    }

    public settings[] getSettings() {
        return settings;
    }

    public Site_theme_setting[] getSite_theme_setting()
    {
        return Site_theme_setting;
    }

    public Currency_Setting[] getCurrency_Setting() {
        return Currency_Setting;
    }

    public sliders[] getSliders() {
        return sliders;
    }

    public latestvideos[] getLatestvideos() {
        return latestvideos;
    }
    public livestream[] getLivestreamNew()
    {
        return livestream;
    }
    public series[] getSeries() {
        return series;
    }

    public categorylist[] getCategorylist() {
        return categorylist;
    }

    public pages[] getPages() {
        return pages;
    }

    public videodetail[] getVideodetail() {
        return videodetail;
    }


    public season[] getSeason() {
        return season;
    }

    public movie_details[] getMovie_details() {
        return movie_detail;
    }

    public audio_shufffle[] getAudio_shufffle() {
        return audio_shufffle;
    }

    public comment[] getComment()
    {
        return comment;
    }


    public payperview_video[] getpayperview_movie() {
        return payperview_video;
    }

    public String getId() {
        return id;
    }

    public channel_videos[] getChannel_videos() {
        return channel_videos;
    }

    public channelrecomended[] getChannelrecomended() {
        return channelrecomended;
    }

    public country[] getCountry() {
        return country;
    }


    public ppvrecomended[] getPpvrecomended() {
        return ppvrecomended;
    }

    public states[] getStates() {
        return states;
    }

    public cities[] getCities() {
        return cities;
    }

    public ppv_videos[] getPpv_videos() {
        return ppv_videos;
    }

    public channel_category[] getChannel_category() {
        return channel_category;
    }

    public channelvideos[] getChannelvideos() {
        return channelvideos;
    }

    public ppv_category[] getPpv_category() {
        return ppv_category;
    }

    public payment_settings[] getPayment_settings() {
        return payment_settings;
    }


    public myrefferals[] getMyrefferals() {
        return myrefferals;
    }

    public videossubtitles[] getmoviesubtitles() {

        return videossubtitles;
    }

    public notifications[] getNotifications() {
        return notifications;
    }

    public user_comments[] getUser_comments() {
        return user_comments;
    }

    public user getUser() {
        return user;
    }

    public user_data[] getUser_data() {
        return user_data;
    }


    public videos[] getVideos() {
        return videos;
    }


    public sub_users[] getSub_users() {
        return sub_users;
    }

    public audiodetail[] getAudiodetail() {
        return audiodetail;
    }

    public recomendedaudios[] getRecomendedaudios() {
        return recomendedaudios;
    }

    public audio[] getAudio() {
        return audio;
    }

    public recent_audios[] getRecent_audios() {
        return recent_audios;
    }

    public socail_networl_setting[] getSocail_networl_setting() {
        return socail_networl_setting;
    }

    public artistlist[] getArtistlist() {
        return artistlist;
    }

    public followinglist[] getFollowinglist() {
        return followinglist;
    }

    public trending_audios[] getTrending_audios() {
        return trending_audios;
    }

    public favoriteslist[] getFavoriteslist() {
        return favoriteslist;
    }

    public channel_audios[] getChannel_audios() {
        return channel_audios;
    }

    public artist_audios[] getArtist_audios() {
        return artist_audios;
    }

    public artist[] getArtist() {
        return artist;
    }

    public String getFavourites() {
        return favourites;
    }

    public String getFollowing() {
        return following;
    }

    public audiocategories[] getAudiocategories() {
        return audiocategories;
    }

    public all_languages[] getAll_language() {
        return all_languages;
    }

    public language_videos[] getLanguage_videos() {
        return language_videos;
    }

    public featured_videos[] getFeatured_videos() {
        return featured_videos;
    }

    public String getMobile_number_status() {
        return mobile_number_status;
    }

    public video_categories[] getVideo_categories() {
        return video_categories;
    }

    public video_artist[] getVideo_artist() {
        return video_artist;
    }

    public allaudios[] getAllaudios() {
        return allaudios;
    }

    public plan[] getPlan() {
        return plan;
    }


    public WelcomeScreen[] getWelcomeScreen() {
        return WelcomeScreen;
    }


    public active_payment_settings[] getActive_payment_settings() {
        return active_payment_settings;
    }


    public livedetail[] getLivedetail() {
        return livedetail;
    }

    public Splash_Screen[] getSplash_Screen() {
        return Splash_Screen;
    }

    public albumcategoryauido[] getAlbumcategoryauido()

    {
        return albumcategoryauido;
    }

    public respond[] getRespond()
    {
        return respond;
    }


    public audioalbums[] getAudioalbums()
    {
        return audioalbums;
    }
    public series_imgae[] getseason_image()

    {
        return season_image;
    }


   public live_banner[] getLive_banner()
   {
       return live_banner;
   }

    public video_banner[] getVideo_banners()
    {
        return video_banner;
    }

    public series_banner[] getSeries_banner()
    {
        return series_banner;
    }

    public categoryaudio[] getCategoryaudio()
    {
        return categoryaudio;
    }


    public channels[] getChannels()
    {
        return channels;
    }

    public ContentPartner[] getContentPartner()
    {
        return ContentPartner;
    }

    public latest_video[] getLatest_videos()
    {
        return latest_video;
    }

    public latest_series[] getLatest_series()
    {
        return latest_series;
    }

    public audios[] getAudios()
    {
        return audios;
    }

    public livetream[] getLivestream()
    {
        return livetream;
    }


    public live_streams[] getLive_streams()
    {
        return live_streams;
    }

    public String getMedia_url()
    {
        return media_url;
    }

    public Menus[] getMenus() {
        return Menus;
    }


    public LiveCategory[] getLiveCategory()
    {
        return LiveCategory;
    }

    public socialsetting[] getSocialsetting()
    {
        return socialsetting;
    }

    public Mostwatched[] getMostwatched()
    {
        return Mostwatched;
    }

    public Mostwatchedvideos[] getMostwatchedvideos()
    {
        return Mostwatchedvideos;
    }

    public mostWatchedUserVideos[] getMostWatchedUserVideos()
    {
        return mostWatchedUserVideos;
    }

    public data[] getData() {
        return data;
    }

    public PageList[] getPage_List() {
        return Page_List;
    }

    public com.atbuys.runmawi.Series_list[] getSeries_list() {
        return Series_list;
    }

    public String getPpv_exist() {
        return ppv_exist;
    }

    public String getPpv_status() {
        return ppv_status;
    }

    public String getOtp_status() {
        return otp_status;
    }

    public String getPlaybackInfo() {
        return playbackInfo;
    }
}
