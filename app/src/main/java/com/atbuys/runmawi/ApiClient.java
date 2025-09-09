package com.atbuys.runmawi;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    private static ApiClient apiclient;


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://runmawi.com/api/auth/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    RequestInterface request = retrofit.create(RequestInterface.class);


    public static synchronized ApiClient getInstance1() {

        if (apiclient == null) {
            apiclient = new ApiClient();
        }

        return apiclient;
    }

    public RequestInterface getApi() {
        return retrofit.create(RequestInterface.class);
    }

    Retrofit retrofit1 = new Retrofit.Builder()
            .baseUrl("https://api.razorpay.com/v1/payments/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public RequestInterface getApi1() {
        return retrofit1.create(RequestInterface.class);
    }

    //order
    Retrofit retrofit2 = new Retrofit.Builder()
            .baseUrl("https://api.razorpay.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public RequestInterface getApi2() {
        return retrofit2.create(RequestInterface.class);
    }


}
