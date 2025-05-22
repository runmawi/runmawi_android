package com.atbuys.runmawi;



import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WebAPIService {
    @GET("country_code.json")
     Call<List<Countrycode>>readJson();
}
