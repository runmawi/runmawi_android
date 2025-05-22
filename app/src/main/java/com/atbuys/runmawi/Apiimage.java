package com.atbuys.runmawi;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Apiimage {

    String BASE_URL = "https://runmawi.com/api/auth/";

    //this is our multipart request
    //we have two parameters on is name and other one is description
    @Multipart
    @POST("updateProfile")
    Call<MyResponse> uploadImage(@Part MultipartBody.Part file,
                                 @Part("user_id") RequestBody user_id,
                                 @Part("username") RequestBody username,
                                 @Part("email") RequestBody email);

    @Multipart
    @POST("signup")
    Call<MyResponse> getFreeSignup(
            @Part MultipartBody.Part file,
            @Part("username") RequestBody username,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password);




    @Multipart
    @POST("signup")
    Call<MyResponse> getSignupWithSkip(@Part MultipartBody.Part file,
                                       @Part("username") RequestBody username,
                                       @Part("email") RequestBody email,
                                       @Part("password") RequestBody password,
                                       @Part("ccode") RequestBody ccode,
                                       @Part("mobile") RequestBody mobile,
                                       @Part("skip") RequestBody skipp,
                                       @Part("referrer_code") RequestBody referrer_code

    );

    @Multipart
    @POST("signup")
    Call<MyResponse> getSignup1(@Part MultipartBody.Part file,
                                @Part("username") RequestBody username,
                                @Part("email") RequestBody email,
                                @Part("password") RequestBody password,
                                @Part("py_id") RequestBody py_id,
                                @Part("subscrip_plan") RequestBody sub,
                                @Part("ccode") RequestBody ccode,
                                @Part("mobile") RequestBody mobile,
                                @Part("skip") RequestBody skip,
                                @Part("referrer_code") RequestBody referrer_code
    );

    @Multipart
    @POST("updateProfile")
    Call<MyResponse> getUpdate(@Part MultipartBody.Part file,
                               @Part("user_id") RequestBody user_id,
                               @Part("user_username") RequestBody username,
                               @Part("user_email") RequestBody email,
                               @Part("user_ccode") RequestBody ccode,
                               @Part("user_mobile") RequestBody mobile,
                               @Part("user_name") RequestBody skip,
                               @Part("DOB") RequestBody dob,
                               @Part("gender") RequestBody gender
                               );




    @Multipart
    @POST("addchilduserprofile")
    Call<MyResponse> getChildCreate(@Part MultipartBody.Part file,
                               @Part("parent_id") RequestBody user_id,
                               @Part("user_name") RequestBody username,
                               @Part("user_type") RequestBody email   );


    @Multipart
    @POST("updatechildprofile")
    Call<MyResponse> getChildupdate(@Part MultipartBody.Part file,
                                    @Part("child_id") RequestBody user_id
                                      );

}
