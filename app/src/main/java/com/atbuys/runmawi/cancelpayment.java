package com.atbuys.runmawi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class cancelpayment {

    private String note;
    private String message;
    private String id;
    private String role;
    private String email;
    private String username;
    private String media;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private List<user_details> user_details;

    public String getSuccess() {
        return note;
    }

    public void setSuccess(String note) {
        this.note = note;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getEmail() {
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }




    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
