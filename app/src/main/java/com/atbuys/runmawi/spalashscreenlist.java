package com.atbuys.runmawi;

import java.util.HashMap;
import java.util.Map;

public class spalashscreenlist {


    private String splash_url;
    //private String settings;


    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getImage_url() {
        return splash_url;
    }

    public void setImage_url(String image_url) {
        this.splash_url = image_url;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
