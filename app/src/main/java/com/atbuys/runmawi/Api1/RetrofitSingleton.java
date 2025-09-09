package com.atbuys.runmawi.Api1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor; // Import the interceptor
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

        // Create a logging interceptor
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // Log request and response lines and their respective headers and bodies (if present).

        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor) // Add the interceptor
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient.build()) // Attach the custom client
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
