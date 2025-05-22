package com.atbuys.runmawi;


import retrofit.RestAdapter;

public class Api {

    public static ApiInterface getClient() {


        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("https://runmawi.com/api/auth/")
                .build();

        ApiInterface api = adapter.create(ApiInterface.class);
        return api;

    }
}
