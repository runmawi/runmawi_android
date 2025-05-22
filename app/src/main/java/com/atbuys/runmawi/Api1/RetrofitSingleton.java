package com.atbuys.runmawi.Api1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {

    private String BASE_URL = "https://runmawi.com/api/auth/";//https://runmawi.com/
    private Retrofit retrofit;
    OkHttpClient.Builder okHttpClient;
    private static RetrofitSingleton retrofitSingletonInstance;
    private Gson gson;

    private RetrofitSingleton() {

        gson = new GsonBuilder().create();

        okHttpClient = new OkHttpClient.Builder().readTimeout(3, TimeUnit.MINUTES);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static synchronized RetrofitSingleton getInstance() {

        if (retrofitSingletonInstance == null) {
            retrofitSingletonInstance = new RetrofitSingleton();
        }

        return retrofitSingletonInstance;


    }

    public RetrofitApiClient getApi() {

        return retrofit.create(RetrofitApiClient.class);

    }


}
