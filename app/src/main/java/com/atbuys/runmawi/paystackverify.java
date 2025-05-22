package com.atbuys.runmawi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class paystackverify {

    private String status;
    private String message;
    private String user_role;


    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private List<paystackverify> user_details1;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public String getUser_role()
    {
        return user_role;
    }

    public void setUser_role(String user_role)
    {
        this.user_role=user_role;
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
