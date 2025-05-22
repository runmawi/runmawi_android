package com.atbuys.runmawi;

import java.util.HashMap;
import java.util.Map;

public class respond {


    private String status;
    private String subscriptionId;
    private String name;


    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubscriptionid() {
        return subscriptionId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setSubscriptionid(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }


    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
