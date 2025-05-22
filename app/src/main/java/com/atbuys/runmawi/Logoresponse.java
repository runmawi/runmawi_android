package com.atbuys.runmawi;

import java.util.HashMap;
import java.util.Map;

public class Logoresponse {


    private String image_url;
    private String settings;


    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }


    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
