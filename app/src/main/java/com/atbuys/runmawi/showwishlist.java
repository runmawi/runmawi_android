package com.atbuys.runmawi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class showwishlist {

    private String status;
    private String wish_count;
    private String id;
    private String plan;
    private String username;
    private String email;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private List<showwishlist> user_details1;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public String getWish_count()
    {
        return wish_count;
    }

    public void setWish_count(String wish_count)
    {
        this.wish_count=wish_count;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
