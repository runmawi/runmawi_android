package com.atbuys.runmawi;


import com.atbuys.runmawi.Remote.IpService;
import com.atbuys.runmawi.Remote.RetrofitClient;


public class Common {
    private static final String BASE_URL="https://api.myip.com/";

    public static IpService getIpService(){
        return RetrofitClient.getClient(BASE_URL).create(IpService.class);
    }
}
