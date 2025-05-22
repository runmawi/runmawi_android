package com.atbuys.runmawi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class likeandunlike {

    private String status;
    private String message;
    private String liked;
    private String plan;
    private String username;
    private String email;
    private String disliked;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private List<likeandunlike> user_details1;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {

        this.liked = liked;
    }

    public String getDisliked()
    {
        return disliked;
    }

    public void  setDisliked(String disliked)
    {
        this. disliked = disliked;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message=message;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
